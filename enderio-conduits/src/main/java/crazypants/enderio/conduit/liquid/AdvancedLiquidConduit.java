package crazypants.enderio.conduit.liquid;

import com.enderio.core.client.render.RenderUtil;
import com.enderio.core.common.vecmath.Vector4f;
import crazypants.enderio.base.conduit.ConnectionMode;
import crazypants.enderio.base.conduit.IConduit;
import crazypants.enderio.base.conduit.IConduitNetwork;
import crazypants.enderio.base.conduit.geom.CollidableComponent;
import crazypants.enderio.base.machine.modes.RedstoneControlMode;
import crazypants.enderio.base.render.registry.TextureRegistry;
import crazypants.enderio.base.render.registry.TextureRegistry.TextureSupplier;
import crazypants.enderio.conduit.AbstractConduitNetwork;
import crazypants.enderio.conduit.IConduitComponent;
import crazypants.enderio.conduit.config.ConduitConfig;
import crazypants.enderio.conduit.render.BlockStateWrapperConduitBundle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static crazypants.enderio.conduit.init.ConduitObject.item_liquid_conduit;

public class AdvancedLiquidConduit extends AbstractTankConduit implements IConduitComponent {

  public static final int CONDUIT_VOLUME = Fluid.BUCKET_VOLUME;

  // TODO Lang
  public static final TextureSupplier ICON_KEY = TextureRegistry.registerTexture("blocks/liquid_conduit_advanced");
  public static final TextureSupplier ICON_KEY_LOCKED = TextureRegistry.registerTexture("blocks/liquid_conduit_advanced_locked");
  public static final TextureSupplier ICON_CORE_KEY = TextureRegistry.registerTexture("blocks/liquid_conduit_core_advanced");
  public static final TextureSupplier ICON_EXTRACT_KEY = TextureRegistry.registerTexture("blocks/liquid_conduit_advanced_input");
  public static final TextureSupplier ICON_INSERT_KEY = TextureRegistry.registerTexture("blocks/liquid_conduit_advanced_output");

  public static final TextureSupplier ICON_EMPTY_EDGE = TextureRegistry.registerTexture("blocks/liquid_conduit_advanced_edge");

  private AdvancedLiquidConduitNetwork network;

  private long ticksSinceFailedExtract = 0;

  public AdvancedLiquidConduit() {
    updateTank();
  }

  @Override
  public void updateEntity(@Nonnull World world) {
    super.updateEntity(world);
    if (world.isRemote) {
      return;
    }
    doExtract();
    if (stateDirty) {
      getBundle().dirty();
      stateDirty = false;
    }
  }

  private void doExtract() {
    // Extraction can happen on extract mode or in/out mode
    if (!hasExtractableMode()) {
      return;
    }
    if (network == null) {
      return;
    }

    // assume failure, reset to 0 if we do extract
    ticksSinceFailedExtract++;
    if (ticksSinceFailedExtract > 25 && ticksSinceFailedExtract % 10 != 0) {
      // after 25 ticks of failing, only check every 10 ticks
      return;
    }

    for (EnumFacing dir : externalConnections) {
      if (autoExtractForDir(dir)) {
        if (network.extractFrom(this, dir, ConduitConfig.fluid_tier2_extractRate.get())) {
          ticksSinceFailedExtract = 0;
        }
      }
    }

  }

  @Override
  protected void updateTank() {
    tank.setCapacity(CONDUIT_VOLUME);
    if (network != null) {
      network.updateConduitVolumes();
    }
  }

  @Override
  @Nonnull
  public ItemStack createItem() {
    return new ItemStack(item_liquid_conduit.getItemNN(), 1, 1);
  }

  @Override
  public @Nullable AbstractConduitNetwork<?, ?> getNetwork() {
    return network;
  }

  @Override
  public boolean setNetwork(@Nonnull IConduitNetwork<?, ?> network) {
    if (!(network instanceof AdvancedLiquidConduitNetwork)) {
      return false;
    }

    AdvancedLiquidConduitNetwork n = (AdvancedLiquidConduitNetwork) network;
    if (tank.getFluid() == null) {
      tank.setLiquid(n.getFluidType() == null ? null : n.getFluidType().copy());
    } else if (n.getFluidType() == null) {
      n.setFluidType(tank.getFluid());
    } else if (!tank.getFluid().isFluidEqual(n.getFluidType())) {
      return false;
    }
    this.network = n;
    return true;

  }

  @Override
  public void clearNetwork() {
    this.network = null;
    // TODO: Spill fluid
  }

  @Override
  public boolean canConnectToConduit(@Nonnull EnumFacing direction, @Nonnull IConduit con) {
    if (!super.canConnectToConduit(direction, con)) {
      return false;
    }
    if (!(con instanceof AdvancedLiquidConduit)) {
      return false;
    }
    if (getFluidType() != null && ((AdvancedLiquidConduit) con).getFluidType() == null) {
      return false;
    }
    return LiquidConduitNetwork.areFluidsCompatable(getFluidType(), ((AdvancedLiquidConduit) con).getFluidType());
  }

  @Override
  public void setConnectionMode(@Nonnull EnumFacing dir, @Nonnull ConnectionMode mode) {
    super.setConnectionMode(dir, mode);
    refreshInputs(dir);
  }

  @Override
  public void setExtractionRedstoneMode(@Nonnull RedstoneControlMode mode, @Nonnull EnumFacing dir) {
    super.setExtractionRedstoneMode(mode, dir);
    refreshInputs(dir);
  }

  private void refreshInputs(@Nonnull EnumFacing dir) {
    if (network == null) {
      return;
    }
    LiquidOutput lo = new LiquidOutput(getBundle().getLocation().offset(dir), dir.getOpposite());
    network.removeInput(lo);
    if (canInputToDir(dir) && containsExternalConnection(dir)) {
      network.addInput(lo);
    }
  }

  @Override
  public void externalConnectionAdded(@Nonnull EnumFacing fromDirection) {
    super.externalConnectionAdded(fromDirection);
    refreshInputs(fromDirection);
  }

  @Override
  public void externalConnectionRemoved(@Nonnull EnumFacing fromDirection) {
    super.externalConnectionRemoved(fromDirection);
    refreshInputs(fromDirection);
  }

  // -------------------------------------
  // TEXTURES
  // -------------------------------------

  @SideOnly(Side.CLIENT)
  @Override
  @Nonnull
  public TextureAtlasSprite getTextureForState(@Nonnull CollidableComponent component) {
    if (component.dir == null) {
      return ICON_CORE_KEY.get(TextureAtlasSprite.class);
    }
    return fluidTypeLocked ? ICON_KEY_LOCKED.get(TextureAtlasSprite.class) : ICON_KEY.get(TextureAtlasSprite.class);
  }

  @SideOnly(Side.CLIENT)
  public TextureAtlasSprite getTextureForInputMode() {
    return ICON_EXTRACT_KEY.get(TextureAtlasSprite.class);
  }

  @SideOnly(Side.CLIENT)
  public TextureAtlasSprite getTextureForOutputMode() {
    return ICON_INSERT_KEY.get(TextureAtlasSprite.class);
  }

  @SideOnly(Side.CLIENT)
  public @Nonnull TextureAtlasSprite getNotSetEdgeTexture() {
    return ICON_EMPTY_EDGE.get(TextureAtlasSprite.class);
  }

  @Override
  public @Nonnull TextureAtlasSprite getTransmitionTextureForState(@Nonnull CollidableComponent component) {
    if (isActive() && tank.containsValidLiquid()) {
      return RenderUtil.getStillTexture(tank.getFluid());
    }
    return null;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public @Nonnull Vector4f getTransmitionTextureColorForState(@Nonnull CollidableComponent component) {
    if (isActive() && tank.containsValidLiquid()) {
      int color = tank.getFluid().getFluid().getColor(tank.getFluid());
      return new Vector4f((color >> 16 & 0xFF) / 255d, (color >> 8 & 0xFF) / 255d, (color & 0xFF) / 255d, 1);
    }
    return null;
  }

  // --------------- Fluid Capability ------------

  @Override
  public IFluidTankProperties[] getTankProperties() {
    if (network == null) {
      return new FluidTankProperties[0];
    }
    return new FluidTankProperties[] { new FluidTankProperties(tank.getFluid(), tank.getCapacity()) };
  }

  @Override
  public int fill(FluidStack resource, boolean doFill) {
    return network.fill(resource, doFill);
  }

  @Nullable
  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
    return network.drain(resource, doDrain);
  }

  @Nullable
  @Override
  public FluidStack drain(int maxDrain, boolean doDrain) {
    return network.drain(maxDrain, doDrain);
  }

  // --------------- End -------------------------

  @Override
  protected boolean canJoinNeighbour(ILiquidConduit n) {
    return n instanceof AdvancedLiquidConduit;
  }

  @Override
  public AbstractTankConduitNetwork<? extends AbstractTankConduit> getTankNetwork() {
    return network;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void hashCodeForModelCaching(BlockStateWrapperConduitBundle.ConduitCacheKey hashCodes) {
    super.hashCodeForModelCaching(hashCodes);
    FluidStack fluidType = getFluidType();
    if (fluidType != null && fluidType.getFluid() != null) {
      hashCodes.add(fluidType.getFluid());
    }
  }

  @Override
  @Nonnull
  public AdvancedLiquidConduitNetwork createNetworkForType() {
    return new AdvancedLiquidConduitNetwork();
  }

}
