package crazypants.enderio.base.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public interface ISmartRenderAwareBlock {

  /**
   * Return a render mapper for the item stack.
   * <p>
   * This is called in a render thread.
   */
  @SideOnly(Side.CLIENT)
  @Nonnull
  IRenderMapper.IItemRenderMapper getItemRenderMapper();

}
