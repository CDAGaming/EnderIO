package crazypants.enderio.base.conduit.redstone;

import crazypants.enderio.base.conduit.redstone.filters.IOutputSignalFilter;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IItemOutputFilterUpgrade {

  @Nullable
  IOutputSignalFilter createOutputSignalFilterFromStack(@Nonnull ItemStack stack);

}
