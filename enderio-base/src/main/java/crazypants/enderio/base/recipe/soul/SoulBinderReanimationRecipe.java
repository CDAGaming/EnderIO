package crazypants.enderio.base.recipe.soul;

import crazypants.enderio.base.config.Config;
import crazypants.enderio.base.material.material.Material;
import crazypants.enderio.util.CapturedMob;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class SoulBinderReanimationRecipe extends AbstractSoulBinderRecipe {

  public static final @Nonnull SoulBinderReanimationRecipe instance = new SoulBinderReanimationRecipe();

  private SoulBinderReanimationRecipe() {
    super(Config.soulBinderReanimationRF, Config.soulBinderReanimationLevels, "SoulBinderReanimationRecipe", new ResourceLocation("minecraft", "zombie"),
        new ResourceLocation("minecraft", "zombie_villager"));
  }

  @Override
  public @Nonnull ItemStack getInputStack() {
    return Material.ZOMBIE_CONTROLLER.getStack();
  }

  @Override
  public @Nonnull ItemStack getOutputStack() {
    return Material.FRANKEN_ZOMBIE.getStack();
  }

  @Override
  protected @Nonnull ItemStack getOutputStack(@Nonnull ItemStack input, @Nonnull CapturedMob mobType) {
    return getOutputStack();
  }

}
