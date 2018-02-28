package crazypants.enderio.base.filter;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ILimitedItemFilter extends IItemFilter {

  @Override
  int getMaxCountThatPassesFilter(@Nullable INetworkedInventory inv, @Nonnull ItemStack item);

  @Override
  boolean isLimited();
}
