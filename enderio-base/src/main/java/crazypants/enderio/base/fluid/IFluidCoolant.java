package crazypants.enderio.base.fluid;

import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;

public interface IFluidCoolant {

  @Nonnull
  Fluid getFluid();

  float getDegreesCoolingPerMB(float heat);
  
}
