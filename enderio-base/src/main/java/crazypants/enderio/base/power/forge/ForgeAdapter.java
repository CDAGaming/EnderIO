package crazypants.enderio.base.power.forge;

import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.Log;
import crazypants.enderio.base.power.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ForgeAdapter implements IPowerApiAdapter {

  private final @Nonnull Capability<IEnergyStorage> ENERGY_HANDLER;

  private static final ResourceLocation KEY = new ResourceLocation(EnderIO.DOMAIN, "forgeadapter");

  public static void capRegistered(@Nonnull Capability<IEnergyStorage> cap) {
    PowerHandlerUtil.addAdapter(new ForgeAdapter(cap));
    MinecraftForge.EVENT_BUS.register(ForgeAdapter.class);
    ItemPowerCapabilityBackend.register(new InternalPoweredItemWrapper.PoweredItemCapabilityProvider());
    Log.info("Forge Energy integration loaded");
  }

  private ForgeAdapter(@Nonnull Capability<IEnergyStorage> cap) {
    ENERGY_HANDLER = cap;
  }

  @Override
  public IPowerInterface getPowerInterface(@Nullable ICapabilityProvider provider, @Nullable EnumFacing side) {
    if (provider != null) {
      IEnergyStorage cap = getCapability(provider, side);
      if (cap != null) {
        return new PowerInterfaceForge(provider, cap);
      }
    }
    return null;
  }

  @Override
  public IEnergyStorage getCapability(@Nullable ICapabilityProvider provider, @Nullable EnumFacing side) {
    if (provider != null) {
      return provider.getCapability(ENERGY_HANDLER, side);
    }
    return null;
  }

  @SubscribeEvent
  public static void attachCapabilities(net.minecraftforge.event.AttachCapabilitiesEvent<TileEntity> evt) {
    if (evt.getCapabilities().containsKey(KEY)) {
      return;
    }
    TileEntity te = evt.getObject();
    if (te instanceof ILegacyPowerReceiver) {
      evt.addCapability(KEY, new InternalRecieverTileWrapper.RecieverTileCapabilityProvider((ILegacyPowerReceiver) te));
    } else if (te instanceof ILegacyPoweredTile) {
      evt.addCapability(KEY, new InternalPoweredTileWrapper.PoweredTileCapabilityProvider((ILegacyPoweredTile) te));
    }
  }

}