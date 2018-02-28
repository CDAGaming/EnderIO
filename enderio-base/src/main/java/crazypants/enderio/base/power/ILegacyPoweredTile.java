package crazypants.enderio.base.power;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public interface ILegacyPoweredTile {

  boolean canConnectEnergy(@Nonnull EnumFacing from);

  int getEnergyStored();

  int getMaxEnergyStored();

  void setEnergyStored(int storedEnergy);

  @Nonnull
  BlockPos getLocation();

  /**
   * Should the power be displayed in WAILA or other places
   */
  boolean displayPower();

}
