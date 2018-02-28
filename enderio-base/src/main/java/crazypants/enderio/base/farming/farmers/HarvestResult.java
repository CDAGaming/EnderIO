package crazypants.enderio.base.farming.farmers;

import com.enderio.core.common.util.NNList;
import crazypants.enderio.api.farm.IHarvestResult;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.List;

public class HarvestResult implements IHarvestResult {

  private final @Nonnull NNList<EntityItem> drops;
  private final @Nonnull NNList<BlockPos> harvestedBlocks;

  public HarvestResult(@Nonnull List<EntityItem> drops, @Nonnull List<BlockPos> harvestedBlocks) {
    this.drops = NNList.wrap(drops);
    this.harvestedBlocks = NNList.wrap(harvestedBlocks);
  }

  public HarvestResult(@Nonnull NNList<EntityItem> drops, @Nonnull NNList<BlockPos> harvestedBlocks) {
    this.drops = drops;
    this.harvestedBlocks = harvestedBlocks;
  }

  public HarvestResult(@Nonnull NNList<EntityItem> drops, BlockPos harvestedBlock) {
    this.drops = drops;
    this.harvestedBlocks = new NNList<>();
    harvestedBlocks.add(harvestedBlock);
  }

  public HarvestResult() {
    drops = new NNList<>();
    harvestedBlocks = new NNList<>();
  }

  @Override
  public @Nonnull NNList<EntityItem> getDrops() {
    return drops;
  }

  @Override
  public @Nonnull NNList<BlockPos> getHarvestedBlocks() {
    return harvestedBlocks;
  }

}
