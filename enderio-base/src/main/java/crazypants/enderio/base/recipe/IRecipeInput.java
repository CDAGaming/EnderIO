package crazypants.enderio.base.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public interface IRecipeInput {

  @Nonnull
  IRecipeInput copy();

  boolean isFluid();

  @Nonnull
  ItemStack getInput();

  FluidStack getFluidInput();

  float getMulitplier();

  int getSlotNumber();

  boolean isInput(@Nonnull ItemStack test);

  boolean isInput(FluidStack test);

  ItemStack[] getEquivelentInputs();

  boolean isValid();

  void shrinkStack(int count);

}