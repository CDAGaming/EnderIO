package crazypants.enderio.base.power;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public interface IPowerApiAdapter {

  @Nullable
  IPowerInterface getPowerInterface(@Nullable ICapabilityProvider provider, @Nullable EnumFacing side);

  @Nullable
  IEnergyStorage getCapability(@Nullable ICapabilityProvider provider, @Nullable EnumFacing side);

}
