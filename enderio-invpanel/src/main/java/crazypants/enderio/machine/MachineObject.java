package crazypants.enderio.machine;

import com.enderio.core.common.util.NullHelper;
import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.init.IModObject;
import crazypants.enderio.base.init.ModObjectRegistry;
import crazypants.enderio.base.machine.alloy.BlockAlloySmelter;
import crazypants.enderio.base.machine.buffer.BlockBuffer;
import crazypants.enderio.base.machine.enchanter.BlockEnchanter;
import crazypants.enderio.base.machine.farm.BlockFarmStation;
import crazypants.enderio.base.machine.generator.combustion.BlockCombustionGenerator;
import crazypants.enderio.base.machine.generator.stirling.BlockStirlingGenerator;
import crazypants.enderio.base.machine.generator.zombie.BlockZombieGenerator;
import crazypants.enderio.base.machine.killera.BlockKillerJoe;
import crazypants.enderio.base.machine.light.BlockElectricLight;
import crazypants.enderio.base.machine.light.BlockLightNode;
import crazypants.enderio.base.machine.obelisk.attractor.BlockAttractor;
import crazypants.enderio.base.machine.obelisk.aversion.BlockAversionObelisk;
import crazypants.enderio.base.machine.obelisk.inhibitor.BlockInhibitorObelisk;
import crazypants.enderio.base.machine.obelisk.relocator.BlockRelocatorObelisk;
import crazypants.enderio.base.machine.obelisk.weather.BlockWeatherObelisk;
import crazypants.enderio.base.machine.obelisk.xp.BlockExperienceObelisk;
import crazypants.enderio.base.machine.painter.BlockPainter;
import crazypants.enderio.base.machine.reservoir.BlockReservoir;
import crazypants.enderio.base.machine.sagmill.BlockSagMill;
import crazypants.enderio.base.machine.slicensplice.BlockSliceAndSplice;
import crazypants.enderio.base.machine.solar.BlockSolarPanel;
import crazypants.enderio.base.machine.soul.BlockSoulBinder;
import crazypants.enderio.base.machine.spawner.BlockPoweredSpawner;
import crazypants.enderio.base.machine.tank.BlockTank;
import crazypants.enderio.base.machine.transceiver.BlockTransceiver;
import crazypants.enderio.base.machine.vacuum.BlockVacuumChest;
import crazypants.enderio.base.machine.vacuum.BlockXPVacuum;
import crazypants.enderio.base.machine.vat.BlockVat;
import crazypants.enderio.base.machine.wireless.BlockWirelessCharger;
import crazypants.enderio.base.render.dummy.BlockMachineIO;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@EventBusSubscriber(modid = EnderIOMachines.MODID)
public enum MachineObject implements IModObject.Registerable {

  blockMachineIO(BlockMachineIO.class),
  
  blockAlloySmelter(BlockAlloySmelter.class),
  blockBuffer(BlockBuffer.class),
//  blockCapBank(BlockCapBank.class),
  blockEnchanter(BlockEnchanter.class),
  blockFarmStation(BlockFarmStation.class),
  blockCombustionGenerator(BlockCombustionGenerator.class),
  blockStirlingGenerator(BlockStirlingGenerator.class),
  blockZombieGenerator(BlockZombieGenerator.class),

  blockKillerJoe(BlockKillerJoe.class),
  blockElectricLight(BlockElectricLight.class),
  blockLightNode(BlockLightNode.class),
  
  //Obelisks
  blockAttractorObelisk(BlockAttractor.class),
  blockAversionObelisk(BlockAversionObelisk.class),
  blockInhibitorObelisk(BlockInhibitorObelisk.class),
  blockRelocatorObelisk(BlockRelocatorObelisk.class),
  blockWeatherObelisk(BlockWeatherObelisk.class),
  blockExperienceObelisk(BlockExperienceObelisk.class),
  
  blockPainter(BlockPainter.class),
  blockReservoir(BlockReservoir.class),
  blockSagMill(BlockSagMill.class),
  blockSliceAndSplice(BlockSliceAndSplice.class),
  blockSolarPanel(BlockSolarPanel.class),
  blockSoulBinder(BlockSoulBinder.class),
  blockPoweredSpawner(BlockPoweredSpawner.class),
  blockVat(BlockVat.class),
  blockWirelessCharger(BlockWirelessCharger.class),
  blockTank(BlockTank.class),
  blockTransceiver(BlockTransceiver.class),
  blockVacuumChest(BlockVacuumChest.class),
  blockXPVacuum(BlockXPVacuum.class),
  ;

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void registerBlocksEarly(@Nonnull RegistryEvent.Register<Block> event) {
    ModObjectRegistry.addModObjects(MachineObject.class);
  }

  final @Nonnull String unlocalisedName;

  protected @Nullable Block block;
  protected @Nullable Item item;

  protected final @Nonnull Class<?> clazz;
  protected final @Nullable String blockMethodName, itemMethodName;
  protected final @Nullable Class<? extends TileEntity> teClazz;

  private MachineObject(@Nonnull Class<?> clazz) {
    this(clazz, "create", (Class<? extends TileEntity>) null);
  }

  private MachineObject(@Nonnull Class<?> clazz, Class<? extends TileEntity> teClazz) {
    this(clazz, "create", teClazz);
  }

  private MachineObject(@Nonnull Class<?> clazz, @Nonnull String methodName) {
    this(clazz, methodName, (Class<? extends TileEntity>) null);
  }

  private MachineObject(@Nonnull Class<?> clazz, @Nonnull String blockMethodName, @Nonnull String itemMethodName) {
    this(clazz, blockMethodName, itemMethodName, null);
  }

  private MachineObject(@Nonnull Class<?> clazz, @Nonnull String methodName, Class<? extends TileEntity> teClazz) {
    this.unlocalisedName = ModObjectRegistry.sanitizeName(NullHelper.notnullJ(name(), "Enum.name()"));
    this.clazz = clazz;
    if (Block.class.isAssignableFrom(clazz)) {
      this.blockMethodName = methodName;
      this.itemMethodName = null;
    } else if (Item.class.isAssignableFrom(clazz)) {
      this.blockMethodName = null;
      this.itemMethodName = methodName;
    } else {
      throw new RuntimeException("Clazz " + clazz + " unexpectedly is neither a Block nor an Item.");
    }
    this.teClazz = teClazz;
  }

  private MachineObject(@Nonnull Class<?> clazz, @Nullable String blockMethodName, @Nullable String itemMethodName, Class<? extends TileEntity> teClazz) {
    this.unlocalisedName = ModObjectRegistry.sanitizeName(NullHelper.notnullJ(name(), "Enum.name()"));
    this.clazz = clazz;
    this.blockMethodName = blockMethodName == null || blockMethodName.isEmpty() ? null : blockMethodName;
    this.itemMethodName = itemMethodName == null || itemMethodName.isEmpty() ? null : itemMethodName;
    this.teClazz = teClazz;
  }

  @Override
  public @Nonnull Class<?> getClazz() {
    return clazz;
  }

  @Override
  public void setItem(@Nullable Item obj) {
    this.item = obj;
  }

  @Override
  public void setBlock(@Nullable Block obj) {
    this.block = obj;
  }

  @Nonnull
  @Override
  public String getUnlocalisedName() {
    return unlocalisedName;
  }

  @Nonnull
  @Override
  public ResourceLocation getRegistryName() {
    return new ResourceLocation(EnderIO.DOMAIN, getUnlocalisedName());
  }

  @Nullable
  @Override
  public Block getBlock() {
    return block;
  }

  @Nullable
  @Override
  public Item getItem() {
    return item;
  }

  @Nullable
  @Override
  public Class<? extends TileEntity> getTileClass() {
    return teClazz;
  }

  @Override
  public final @Nonnull <B extends Block> B apply(@Nonnull B blockIn) {
    blockIn.setUnlocalizedName(getUnlocalisedName());
    blockIn.setRegistryName(getRegistryName());
    return blockIn;
  }

  @Override
  public final @Nonnull <I extends Item> I apply(@Nonnull I itemIn) {
    itemIn.setUnlocalizedName(getUnlocalisedName());
    itemIn.setRegistryName(getRegistryName());
    return itemIn;
  }

  @Override
  @Nullable
  public String getBlockMethodName() {
    return blockMethodName;
  }

  @Override
  @Nullable
  public String getItemMethodName() {
    return itemMethodName;
  }

}
