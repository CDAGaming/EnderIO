package crazypants.enderio.base.capacitor;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface ICapacitorDataItem {

  @Nonnull
  ICapacitorData getCapacitorData(@Nonnull ItemStack stack);

}
