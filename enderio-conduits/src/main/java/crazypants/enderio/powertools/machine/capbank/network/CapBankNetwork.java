package crazypants.enderio.powertools.machine.capbank.network;

import com.enderio.core.common.util.NNList;
import com.enderio.core.common.util.RoundRobinIterator;
import crazypants.enderio.base.Log;
import crazypants.enderio.base.conduit.ConnectionMode;
import crazypants.enderio.base.config.config.DiagnosticsConfig;
import crazypants.enderio.base.diagnostics.Prof;
import crazypants.enderio.base.handler.ServerTickHandler;
import crazypants.enderio.base.machine.modes.IoMode;
import crazypants.enderio.base.machine.modes.RedstoneControlMode;
import crazypants.enderio.base.network.PacketHandler;
import crazypants.enderio.base.power.IPowerInterface;
import crazypants.enderio.base.power.IPowerStorage;
import crazypants.enderio.base.power.PerTickIntAverageCalculator;
import crazypants.enderio.base.power.PowerHandlerUtil;
import crazypants.enderio.conduit.power.IPowerConduit;
import crazypants.enderio.powertools.machine.capbank.CapBankType;
import crazypants.enderio.powertools.machine.capbank.TileCapBank;
import crazypants.enderio.powertools.machine.capbank.packet.PacketNetworkEnergyResponse;
import crazypants.enderio.powertools.machine.capbank.packet.PacketNetworkStateResponse;
import crazypants.enderio.util.Prep;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

import javax.annotation.Nonnull;
import java.util.*;

public class CapBankNetwork implements ICapBankNetwork, ServerTickHandler.ITickListener {

  private static final int IO_CAP = 2000000000;

  private final @Nonnull List<TileCapBank> capBanks = new ArrayList<TileCapBank>();

  private final @Nonnull Set<EnergyReceptor> receptors = new HashSet<EnergyReceptor>();
  private RoundRobinIterator<EnergyReceptor> receptorIterator;

  private final int id;

  private int maxIO;

  private int maxInput = -1;

  private int maxOutput = -1;

  private long energyStored;
  private long prevEnergyStored = -1;
  private long energyReceived;
  private long energySend;

  private long maxEnergyStored;

  private CapBankType type;

  private final @Nonnull Set<BlockPos> redstoneRecievers = new HashSet<>();

  private @Nonnull RedstoneControlMode inputControlMode = RedstoneControlMode.IGNORE;
  private @Nonnull RedstoneControlMode outputControlMode = RedstoneControlMode.IGNORE;

  private boolean inputRedstoneConditionMet = true;
  private boolean outputRedstoneConditionMet = true;

  private final @Nonnull PerTickIntAverageCalculator powerTrackerIn = new PerTickIntAverageCalculator(2);
  private final @Nonnull PerTickIntAverageCalculator powerTrackerOut = new PerTickIntAverageCalculator(2);

  private final @Nonnull InventoryImpl inventory = new InventoryImpl();

  private boolean firstUpate = true;

  public CapBankNetwork(int id) {
    this.id = id;
  }

  // --------- Network Management

  public void init(TileCapBank cap, Collection<TileCapBank> neighbours, World world) {
    if (world.isRemote) {
      throw new UnsupportedOperationException();
    }

    type = cap.getType();
    inputControlMode = cap.getInputControlMode();
    outputControlMode = cap.getOutputControlMode();
    for (TileCapBank con : neighbours) {
      ICapBankNetwork network = con.getNetwork();
      if (network != null) {
        network.destroyNetwork();
      }
    }
    setNetwork(world, cap);
    addEnergy(0); // ensure energy level is within bounds
    ServerTickHandler.addListener(this);
  }

  protected void setNetwork(World world, TileCapBank cap) {
    if (cap == null) {
      return;
    }
    Set<TileCapBank> work = new HashSet<TileCapBank>();
    for (;;) {
      ICapBankNetwork network = cap.getNetwork();
      if (network != this) {
        if (network != null) {
          network.destroyNetwork();
        }
        if (cap.setNetwork(this)) {
          addMember(cap);
          NetworkUtil.getNeigbours(cap, work);
        }
      }
      if (work.isEmpty()) {
        return;
      }
      Iterator<TileCapBank> iter = work.iterator();
      cap = iter.next();
      iter.remove();
    }
  }

  @Override
  public void destroyNetwork() {
    ServerTickHandler.removeListener(this);
    distributeEnergyToBanks();
    TileCapBank cap = null;
    for (TileCapBank cb : capBanks) {
      cb.setNetwork(null);
      if (cap == null) {
        cap = cb;
      }
    }
    capBanks.clear();
    inventory.setCapBank(null);
    if (cap != null) {
      PacketHandler.INSTANCE.sendToAll(new PacketNetworkStateResponse(this, true));
    }
  }

  @Override
  public @Nonnull Collection<TileCapBank> getMembers() {
    return capBanks;
  }

  @Override
  public void addMember(@Nonnull TileCapBank cap) {
    if (!capBanks.contains(cap)) {
      capBanks.add(cap);
      long newIO = maxIO + cap.getType().getMaxIO();
      if (newIO > IO_CAP) {
        newIO = IO_CAP;
      }
      maxIO = (int) newIO;

      energyStored += cap.getEnergyStored();
      maxEnergyStored += cap.getMaxEnergyStored();
      if (maxInput == -1) {
        maxInput = cap.getMaxInputOverride();
        if (DiagnosticsConfig.debugTraceCapLimitsExtremelyDetailed.get()) {
          StringBuilder sb = new StringBuilder("CapBankNetwork ").append(this).append(" intput changed from -1 to ").append(maxInput);
          for (StackTraceElement elem : new Exception("Stackstrace").getStackTrace()) {
            sb.append(" at ").append(elem);
          }
          Log.warn(sb);
        }
      }
      if (maxOutput == -1) {
        maxOutput = cap.getMaxOutputOverride();
        if (DiagnosticsConfig.debugTraceCapLimitsExtremelyDetailed.get()) {
          StringBuilder sb = new StringBuilder("CapBankNetwork ").append(this).append(" output changed from -1 to ").append(maxOutput);
          for (StackTraceElement elem : new Exception("Stackstrace").getStackTrace()) {
            sb.append(" at ").append(elem);
          }
          Log.warn(sb);
        }
      }
      cap.setInputControlMode(inputControlMode);
      cap.setOutputControlMode(outputControlMode);

      List<EnergyReceptor> recs = cap.getReceptors();
      if (!recs.isEmpty()) {
        addReceptors(recs);
      }

      if (inventory.isEmtpy()) {
        inventory.setCapBank(cap);
      } else if (!InventoryImpl.isInventoryEmtpy(cap)) {
        if (inventory.isEmtpy()) {
          inventory.setCapBank(cap);
        } else {
          cap.dropItems();
        }
      }

    }
  }

  @Override
  public @Nonnull InventoryImpl getInventory() {
    return inventory;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public @Nonnull NetworkState getState() {
    return new NetworkState(this);
  }

  // --------- Tick Handling

  @Override
  public void tickEnd(TickEvent.ServerTickEvent evt, Profiler profiler) {
    Prof.start(profiler, "ItemCharging");
    chargeItems(inventory.getStacks());
    Prof.next(profiler, "EnergyTransmitting");
    transmitEnergy();

    if (energyStored != prevEnergyStored) {
      Prof.next(profiler, "EnergyBalancing");
      distributeEnergyToBanks();
    }
    Prof.next(profiler, "EnergyTracking");
    powerTrackerIn.tick(energyReceived);
    powerTrackerOut.tick(energySend);
    prevEnergyStored = energyStored;
    energyReceived = 0;
    energySend = 0;
    Prof.stop(profiler);

    if (firstUpate) {
      if (!capBanks.isEmpty()) {
        PacketHandler.sendToAllAround(new PacketNetworkStateResponse(this), capBanks.get(0));
        PacketHandler.sendToAllAround(new PacketNetworkEnergyResponse(this), capBanks.get(0));
      }
      firstUpate = false;
    }

  }

  private void transmitEnergy() {

    if (!outputRedstoneConditionMet) {
      return;
    }

    if (receptors.isEmpty()) {
      return;
    }

    int available = getEnergyAvailableForTick(getMaxOutput());
    if (available <= 0) {
      return;
    }

    if (receptorIterator == null) {
      List<EnergyReceptor> rl = new ArrayList<EnergyReceptor>(receptors);
      receptorIterator = new RoundRobinIterator<EnergyReceptor>(rl);
    }

    int totalSent = 0;
    Iterator<EnergyReceptor> iter = receptorIterator.iterator();
    while (available > 0 && iter.hasNext()) {
      int sent = sendPowerTo(iter.next(), available);
      totalSent += sent;
      available -= sent;
    }
    addEnergy(-totalSent);
  }

  protected int getEnergyAvailableForTick(int limit) {
    int available;
    if (energyStored > limit) {
      available = limit;
    } else {
      available = (int) energyStored;
    }
    return available;
  }

  private int sendPowerTo(EnergyReceptor next, int available) {
    // Can only send to power conduits if we are in push mode or the conduit is in pull mode
    // With default setting interaction between conduits and Cap Banks is handled by NetworkPowerManager
    IPowerConduit con = next.getConduit();
    if (con != null && next.getMode() == IoMode.NONE && con.getConnectionMode(next.getDir().getOpposite()) == ConnectionMode.IN_OUT) {
      return 0;
    }
    IPowerInterface inf = next.getReceptor();
    int result = inf.receiveEnergy(available, false);
    if (result < 0) {
      result = 0;
    }
    return result;
  }

  public boolean chargeItems(NNList<ItemStack> items) {
    if (items == null) {
      return false;
    }
    boolean chargedItem = false;
    int available = getEnergyAvailableForTick(getMaxIO());
    for (ItemStack item : items) {
      if (item != null && Prep.isValid(item) && available > 0 && item.getCount() == 1) {
        IEnergyStorage chargable = PowerHandlerUtil.getCapability(item);
        if (chargable != null) {
          int max = chargable.getMaxEnergyStored();
          int cur = chargable.getEnergyStored();
          if (cur < max) {
            int canUse = Math.min(available, max - cur);
            int used = chargable.receiveEnergy(canUse, false);
            if (used > 0) {
              addEnergy(-used);
              chargedItem = true;
              available -= used;
            }
          }
        }
      }
    }
    return chargedItem;
  }

  private void distributeEnergyToBanks() {
    if (capBanks.isEmpty()) {
      return;
    }
    int energyPerCapBank = (int) (energyStored / capBanks.size());
    int remaining = (int) (energyStored % capBanks.size());
    for (TileCapBank cb : capBanks) {
      cb.setEnergyStored(energyPerCapBank);
    }
    TileCapBank cb = capBanks.get(0);
    cb.setEnergyStored(cb.getEnergyStored() + remaining);
  }

  // ------ Power

  @Override
  public float getAverageChangePerTick() {
    return powerTrackerIn.getAverage() - powerTrackerOut.getAverage();
  }

  @Override
  public int getAverageIOPerTick() {
    return Math.round(getAverageChangePerTick());
  }

  @Override
  public float getAverageInputPerTick() {
    return powerTrackerIn.getAverage();
  }

  @Override
  public float getAverageOutputPerTick() {
    return powerTrackerOut.getAverage();
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    if (maxReceive <= 0 || !inputRedstoneConditionMet) {
      return 0;
    }

    long spaceAvailable = maxEnergyStored - energyStored;
    if (spaceAvailable > Integer.MAX_VALUE) {
      spaceAvailable = Integer.MAX_VALUE;
    }
    int res = Math.min(maxReceive, (int) spaceAvailable);
    res = Math.min(res, getMaxInput());
    if (!simulate) {
      addEnergy(res);
    }
    return res;
  }

  @Override
  public void addEnergy(int energy) {
    if (energy > 0) {
      energyReceived += energy;
    } else {
      energySend -= energy;
    }
    if (!type.isCreative()) {
      energyStored += energy;
      if (energyStored > maxEnergyStored) {
        energyStored = maxEnergyStored;
      } else if (energyStored < 0) {
        energyStored = 0;
      }
    } else {
      energyStored = maxEnergyStored / 2;
    }
  }

  public void addEnergyReceptor(EnergyReceptor rec) {
    receptors.add(rec);
    receptorIterator = null;
  }

  @Override
  public void addReceptors(@Nonnull Collection<EnergyReceptor> rec) {
    if (rec.isEmpty()) {
      return;
    }
    receptors.addAll(rec);
    receptorIterator = null;
  }

  @Override
  public void removeReceptors(@Nonnull Collection<EnergyReceptor> rec) {
    if (rec.isEmpty()) {
      return;
    }
    receptors.removeAll(rec);
    receptorIterator = null;
  }

  public void removeReceptor(EnergyReceptor rec) {
    receptors.remove(rec);
    receptorIterator = null;
  }

  @Override
  public long getEnergyStoredL() {
    return energyStored;
  }

  @Override
  public long getMaxEnergyStoredL() {
    return maxEnergyStored;
  }

  @Override
  public int getMaxIO() {
    return maxIO;
  }

  // ----- IO overrides

  @Override
  public int getMaxInput() {
    if (maxInput == -1) {
      return maxIO;
    }
    return Math.min(maxInput, maxIO);
  }

  @Override
  public int getMaxOutput() {
    if (maxOutput == -1) {
      return maxIO;
    }
    return Math.min(maxOutput, maxIO);
  }

  @Override
  public void setMaxInput(int max) {
    if (DiagnosticsConfig.debugTraceCapLimitsExtremelyDetailed.get()) {
      StringBuilder sb = new StringBuilder("CapBankNetwork ").append(this).append(" intput changed from ").append(this.maxInput).append(" to ").append(max);
      for (StackTraceElement elem : new Exception("Stackstrace").getStackTrace()) {
        sb.append(" at ").append(elem);
      }
      Log.warn(sb);
    }
    if (max >= maxIO) {
      maxInput = -1;
    } else if (max < 0) {
      maxInput = 0;
    } else {
      maxInput = max;
    }
    for (TileCapBank cb : capBanks) {
      cb.setMaxInput(maxInput);
    }
  }

  @Override
  public void setMaxOutput(int max) {
    if (DiagnosticsConfig.debugTraceCapLimitsExtremelyDetailed.get()) {
      StringBuilder sb = new StringBuilder("CapBankNetwork ").append(this).append(" output changed from ").append(this.maxOutput).append(" to ").append(max);
      for (StackTraceElement elem : new Exception("Stackstrace").getStackTrace()) {
        sb.append(" at ").append(elem);
      }
      Log.warn(sb);
    }
    if (max >= maxIO) {
      maxOutput = -1;
    } else if (max < 0) {
      maxOutput = 0;
    } else {
      maxOutput = max;
    }
    for (TileCapBank cb : capBanks) {
      cb.setMaxOutput(maxOutput);
    }
  }

  // ----------- Redstone

  @Override
  public @Nonnull RedstoneControlMode getInputControlMode() {
    return inputControlMode;
  }

  @Override
  public void setInputControlMode(@Nonnull RedstoneControlMode inputControlMode) {
    if (this.inputControlMode == inputControlMode) {
      return;
    }
    this.inputControlMode = inputControlMode;
    for (TileCapBank capBank : capBanks) {
      capBank.setInputControlMode(inputControlMode);
    }
    updateRedstoneConditions();
  }

  @Override
  public @Nonnull RedstoneControlMode getOutputControlMode() {
    return outputControlMode;
  }

  @Override
  public void setOutputControlMode(@Nonnull RedstoneControlMode outputControlMode) {
    if (this.outputControlMode == outputControlMode) {
      return;
    }
    this.outputControlMode = outputControlMode;
    for (TileCapBank capBank : capBanks) {
      capBank.setOutputControlMode(outputControlMode);
    }
    updateRedstoneConditions();
  }

  @Override
  public void updateRedstoneSignal(@Nonnull TileCapBank tileCapBank, boolean recievingSignal) {
    if (recievingSignal) {
      redstoneRecievers.add(tileCapBank.getLocation());
    } else {
      redstoneRecievers.remove(tileCapBank.getLocation());
    }
    updateRedstoneConditions();
  }

  @Override
  public boolean isInputEnabled() {
    return inputRedstoneConditionMet;
  }

  @Override
  public boolean isOutputEnabled() {
    return outputRedstoneConditionMet;
  }

  private void updateRedstoneConditions() {
    int powerLevel = redstoneRecievers.isEmpty() ? 0 : 15;
    inputRedstoneConditionMet = RedstoneControlMode.isConditionMet(inputControlMode, powerLevel);
    outputRedstoneConditionMet = RedstoneControlMode.isConditionMet(outputControlMode, powerLevel);

  }

  @Override
  public IPowerStorage getController() {
    return this;
  }

  @Override
  public boolean isOutputEnabled(@Nonnull EnumFacing direction) {
    return isOutputEnabled();
  }

  @Override
  public boolean isInputEnabled(@Nonnull EnumFacing direction) {
    return isInputEnabled();
  }

  @Override
  public boolean isCreative() {
    return type.isCreative();
  }

  @Override
  public boolean isNetworkControlledIo(@Nonnull EnumFacing direction) {
    // This is handled at the block level based on the IO mode
    return true;
  }

  @Override
  public void invalidateDisplayInfoCache() {
  }

  @Override
  public void tickStart(ServerTickEvent event, Profiler profiler) {
  }

}
