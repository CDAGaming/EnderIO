package crazypants.enderio.base.power;

import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;

public interface IPowerInterface extends IEnergyStorage {

  @Nonnull
  Object getProvider();

}
