package crazypants.enderio.base.conduit;

import crazypants.enderio.api.tool.IHideFacades;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IConduitItem extends IHideFacades {

  @Nonnull
  Class<? extends IConduit> getBaseConduitType();

  IConduit createConduit(@Nonnull ItemStack item, @Nonnull EntityPlayer player);

}
