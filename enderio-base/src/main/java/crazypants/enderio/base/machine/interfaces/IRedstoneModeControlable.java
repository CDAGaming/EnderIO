package crazypants.enderio.base.machine.interfaces;

import crazypants.enderio.base.machine.modes.RedstoneControlMode;

import javax.annotation.Nonnull;

public interface IRedstoneModeControlable {

  @Nonnull
  RedstoneControlMode getRedstoneControlMode();

  void setRedstoneControlMode(@Nonnull RedstoneControlMode mode);

  boolean getRedstoneControlStatus();

}
