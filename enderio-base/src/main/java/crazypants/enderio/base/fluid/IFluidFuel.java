package crazypants.enderio.base.fluid;

import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;

public interface IFluidFuel {

  @Nonnull
  Fluid getFluid();

  int getTotalBurningTime();

  int getPowerPerCycle();
}
