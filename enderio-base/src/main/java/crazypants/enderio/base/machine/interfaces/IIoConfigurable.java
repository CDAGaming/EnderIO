package crazypants.enderio.base.machine.interfaces;

import crazypants.enderio.base.machine.modes.IoMode;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IIoConfigurable {

  @Nonnull IoMode toggleIoModeForFace(@Nullable EnumFacing faceHit);

  boolean supportsMode(@Nullable EnumFacing faceHit, @Nullable IoMode mode);

  void setIoMode(@Nullable EnumFacing faceHit, @Nullable IoMode mode);

  @Nonnull IoMode getIoMode(@Nullable EnumFacing face);

  void clearAllIoModes();
  
  @Nonnull
  BlockPos getLocation();

}
