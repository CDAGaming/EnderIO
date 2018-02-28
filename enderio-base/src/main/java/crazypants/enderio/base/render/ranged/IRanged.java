package crazypants.enderio.base.render.ranged;

import com.enderio.core.client.render.BoundingBox;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public interface IRanged {

  @SideOnly(Side.CLIENT)
  boolean isShowingRange();

  @Nonnull
  BoundingBox getBounds();

}
