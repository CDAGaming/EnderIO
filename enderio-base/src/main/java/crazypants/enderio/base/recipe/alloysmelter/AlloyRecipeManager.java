package crazypants.enderio.base.recipe.alloysmelter;

import com.enderio.core.common.util.NNList;
import crazypants.enderio.base.recipe.*;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class AlloyRecipeManager extends ManyToOneRecipeManager {

  static final @Nonnull AlloyRecipeManager instance = new AlloyRecipeManager();

  public static AlloyRecipeManager getInstance() {
    return instance;
  }

  @Nonnull
  VanillaSmeltingRecipe vanillaRecipe = new VanillaSmeltingRecipe();

  public AlloyRecipeManager() {
    super("Alloy Smelter");
  }

  public @Nonnull VanillaSmeltingRecipe getVanillaRecipe() {
    return vanillaRecipe;
  }

  public void create() {
    MachineRecipeRegistry.instance.registerRecipe(MachineRecipeRegistry.ALLOYSMELTER,
        new ManyToOneMachineRecipe("AlloySmelterRecipe", MachineRecipeRegistry.ALLOYSMELTER, this));
    // vanilla alloy furnace recipes
    MachineRecipeRegistry.instance.registerRecipe(MachineRecipeRegistry.ALLOYSMELTER, vanillaRecipe);
  }

  public void addRecipe(@Nonnull NNList<IRecipeInput> input, @Nonnull ItemStack output, int energyCost, float xpChance) {
    RecipeOutput recipeOutput = new RecipeOutput(output, 1, xpChance);
    addRecipe(new Recipe(recipeOutput, energyCost, RecipeBonusType.NONE, input.toArray(new IRecipeInput[input.size()])));
  }

}
