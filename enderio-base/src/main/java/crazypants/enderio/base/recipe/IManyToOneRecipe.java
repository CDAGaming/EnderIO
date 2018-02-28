package crazypants.enderio.base.recipe;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IManyToOneRecipe extends IRecipe {

  boolean isValidRecipeComponents(ItemStack... items);

  @Nonnull
  ItemStack getOutput();

}
