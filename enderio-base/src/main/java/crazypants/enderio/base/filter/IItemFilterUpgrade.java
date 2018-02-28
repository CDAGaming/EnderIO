package crazypants.enderio.base.filter;

import crazypants.enderio.base.machine.interfaces.IClearableConfiguration;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IItemFilterUpgrade extends IClearableConfiguration {

  IItemFilter createFilterFromStack(@Nonnull ItemStack stack);

}
