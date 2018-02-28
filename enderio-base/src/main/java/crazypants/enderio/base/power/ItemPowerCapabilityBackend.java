package crazypants.enderio.base.power;

import com.enderio.core.common.util.NNList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemPowerCapabilityBackend implements ICapabilityProvider {

  private static final @Nonnull NNList<ItemPowerCapabilityProvider> providers = new NNList<ItemPowerCapabilityProvider>();

  public static void register(@Nonnull ItemPowerCapabilityProvider provider) {
    providers.add(provider);
  }

  private final @Nonnull ItemStack stack;

  public ItemPowerCapabilityBackend(@Nonnull ItemStack stack) {
    this.stack = stack;
  }

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
    for (ItemPowerCapabilityProvider itemPowerCapabilityProvider : providers) {
      if (stack.getCount() == 1 && itemPowerCapabilityProvider.hasCapability(stack, capability, facing)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
    if (stack.getCount() == 1) {
      for (ItemPowerCapabilityProvider itemPowerCapabilityProvider : providers) {
        T res = itemPowerCapabilityProvider.getCapability(stack, capability, facing);
        if (res != null) {
          return res;
        }
      }
    }
    return null;
  }

}
