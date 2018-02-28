package crazypants.enderio.base.recipe.soul;

import crazypants.enderio.base.config.Config;
import crazypants.enderio.base.material.material.Material;
import crazypants.enderio.util.CapturedMob;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class SoulBinderEnderCystalRecipe extends AbstractSoulBinderRecipe {

  public static final @Nonnull SoulBinderEnderCystalRecipe instance = new SoulBinderEnderCystalRecipe();

  private SoulBinderEnderCystalRecipe() {
    super(Config.soulBinderEnderCystalRF, Config.soulBinderEnderCystalLevels, "SoulBinderEnderCystalRecipe", new ResourceLocation("minecraft", "enderman"));
  }

  @Override
  public @Nonnull ItemStack getInputStack() {
    return Material.VIBRANT_CYSTAL.getStack();
  }

  @Override
  public @Nonnull ItemStack getOutputStack() {
    return Material.ENDER_CRYSTAL.getStack();
  }

  @Override
  protected @Nonnull ItemStack getOutputStack(@Nonnull ItemStack input, @Nonnull CapturedMob mobType) {
    return getOutputStack();
  }

}
