package crazypants.enderio.base.farming.harvesters;

import com.enderio.core.client.render.BoundingBox;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class AxeHarvestingTarget implements IHarvestingTarget {

  private final @Nonnull IBlockState wood;
  private final EnumType variant;
  private final @Nonnull BoundingBox bb;

  public AxeHarvestingTarget(@Nonnull IBlockState wood, @Nonnull BlockPos pos) {
    this.wood = wood;
    variant = getVariant(wood);
    this.bb = new BoundingBox(pos, pos.up(30)).expand(12, 0, 12);
  }

  private static EnumType getVariant(IBlockState bs) {
    if (bs.getProperties().containsKey(BlockNewLog.VARIANT)) {
      return bs.getValue(BlockNewLog.VARIANT);
    }
    if (bs.getProperties().containsKey(BlockOldLog.VARIANT)) {
      return bs.getValue(BlockOldLog.VARIANT);
    }
    return null;
  }

  @Override
  public boolean isWood(@Nonnull IBlockState state) {
    // shortcut for same blockstate, then the long check
    return state == wood || (state.getBlock() == wood.getBlock() && (variant == null || variant == getVariant(state)));
  }

  @Override
  public boolean isInBounds(@Nonnull BlockPos pos) {
    return bb.contains(pos);
  }

}
