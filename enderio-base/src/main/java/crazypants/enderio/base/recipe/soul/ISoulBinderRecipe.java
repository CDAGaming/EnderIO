package crazypants.enderio.base.recipe.soul;

import com.enderio.core.common.util.NNList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface ISoulBinderRecipe {

  @Nonnull
  ItemStack getInputStack();

  @Nonnull
  ItemStack getOutputStack();

  NNList<ResourceLocation> getSupportedSouls();

  int getEnergyRequired();

  int getExperienceLevelsRequired();

  int getExperienceRequired();

}
