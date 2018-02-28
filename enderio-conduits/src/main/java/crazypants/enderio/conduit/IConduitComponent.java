package crazypants.enderio.conduit;

import crazypants.enderio.base.render.IBlockStateWrapper;
import crazypants.enderio.conduit.render.BlockStateWrapperConduitBundle;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IConduitComponent {

  @SideOnly(Side.CLIENT)
  void hashCodeForModelCaching(BlockStateWrapperConduitBundle.ConduitCacheKey hashCodes);

  interface IConduitComponentProvider {

    @SideOnly(Side.CLIENT)
    void hashCodeForModelCaching(IBlockStateWrapper wrapper, BlockStateWrapperConduitBundle.ConduitCacheKey hashCodes);

  }

}
