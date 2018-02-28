package crazypants.enderio.base.conduit.redstone.signals;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public interface ISignalSource {

  @Nonnull
  BlockPos getSource();

  @Nonnull
  EnumFacing getDir();

}