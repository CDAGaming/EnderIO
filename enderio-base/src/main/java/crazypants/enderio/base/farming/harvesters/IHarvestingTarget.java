package crazypants.enderio.base.farming.harvesters;

import com.enderio.core.common.util.stackable.Things;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public interface IHarvestingTarget {

  @Nonnull Things LEAVES = new Things("treeLeaves");
  @Nonnull Things NOT_LEAVES = new Things();

  default boolean isTarget(@Nonnull IBlockState state) {
    return isWood(state) || isLeaves(state);
  }

  boolean isWood(@Nonnull IBlockState state);

  default boolean isLeaves(@Nonnull IBlockState state) {
    return isDefaultLeaves(state);
  }

  static boolean isDefaultLeaves(@Nonnull IBlockState state) {
    return LEAVES.contains(state.getBlock()) && !NOT_LEAVES.contains(state.getBlock());
  }

  boolean isInBounds(@Nonnull BlockPos pos);

  static void addLeavesExcemption(@Nonnull Block block) {
    NOT_LEAVES.add(block);
  }

}
