package crazypants.enderio.machines.machine.solar;

import crazypants.enderio.base.item.conduitprobe.PacketConduitProbe.IHasConduitProbeData;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.Set;

public interface ISolarPanelNetwork extends IHasConduitProbeData {

  boolean isValid();

  void extractEnergy(int maxExtract);

  int getEnergyAvailableThisTick();

  int getEnergyAvailablePerTick();

  int getEnergyMaxPerTick();

  void destroyNetwork();

  @Nonnull
  Set<BlockPos> getPanels();

}