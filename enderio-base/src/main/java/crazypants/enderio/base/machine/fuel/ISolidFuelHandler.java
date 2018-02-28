package crazypants.enderio.base.machine.fuel;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface ISolidFuelHandler {

  /**
   * True if the current GUI belongs to this handler.
   */
  default boolean isInGUI() {
    return false;
  }

  default int getPowerUsePerTick() {
    return 0;
  }

  long getBurnTime(@Nonnull ItemStack itemstack);

  public static interface Provider {

    @Nonnull
    ISolidFuelHandler getSolidFuelHandler();

  }

}
