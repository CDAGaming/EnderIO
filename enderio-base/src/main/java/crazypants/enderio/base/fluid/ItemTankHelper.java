package crazypants.enderio.base.fluid;

import com.enderio.core.common.fluid.SmartTank;
import crazypants.enderio.util.NbtValue;
import info.loenwind.autosave.Reader;
import info.loenwind.autosave.Writer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemTankHelper {

  private ItemTankHelper() {
  }

  public static SmartTank getTank(@Nonnull ItemStack stack) {
    if (NbtValue.DATAROOT.hasTag(stack)) {
      return Reader.readField(NbtValue.DATAROOT.getTag(stack), SmartTank.class, "tank", null);
    }
    return null;
  }

  public static void setTank(@Nonnull ItemStack stack, SmartTank tank) {
    Writer.writeField(NbtValue.DATAROOT.getTag(stack), SmartTank.class, "tank", tank);
  }

}
