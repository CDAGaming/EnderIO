package crazypants.enderio.base.tool;

import crazypants.enderio.api.tool.ITool;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IToolProvider {

  @Nullable
  ITool getTool(@Nonnull ItemStack stack);

}
