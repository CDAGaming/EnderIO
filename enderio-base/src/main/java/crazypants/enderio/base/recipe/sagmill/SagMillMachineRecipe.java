package crazypants.enderio.base.recipe.sagmill;

import com.enderio.core.common.util.NNList;
import crazypants.enderio.base.recipe.*;

import javax.annotation.Nonnull;

public class SagMillMachineRecipe extends AbstractMachineRecipe {

  @Override
  public @Nonnull String getUid() {
    return "CrusherRecipe";
  }

  @Override
  public IRecipe getRecipeForInputs(@Nonnull NNList<MachineRecipeInput> inputs) {
    return SagMillRecipeManager.instance.getRecipeForInput(inputs.get(0).item);
  }

  @Override
  public boolean isValidInput(@Nonnull MachineRecipeInput input) {
    return SagMillRecipeManager.instance.isValidInput(input);
  }

  @Override
  public @Nonnull String getMachineName() {
    return MachineRecipeRegistry.SAGMILL;
  }

  @Override
  public @Nonnull RecipeBonusType getBonusType(@Nonnull NNList<MachineRecipeInput> inputs) {
    if (inputs.size() <= 0) {
      return RecipeBonusType.NONE;
    }
    IRecipe recipe = getRecipeForInputs(inputs);
    if (recipe == null) {
      return RecipeBonusType.NONE;
    } else {
      return recipe.getBonusType().withoutMultiply(SagMillRecipeManager.getInstance().isExcludedFromBallBonus(inputs));
    }
  }

}
