package crazypants.enderio.base.conduit.redstone;

import crazypants.enderio.base.conduit.redstone.filters.IInputSignalFilter;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IItemInputFilterUpgrade {

  @Nullable
  IInputSignalFilter createInputSignalFilterFromStack(@Nonnull ItemStack stack);

}
