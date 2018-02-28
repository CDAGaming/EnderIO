package crazypants.enderio.conduit.liquid;

import crazypants.enderio.base.conduit.ConnectionMode;
import crazypants.enderio.base.conduit.IConduit;
import crazypants.enderio.base.conduit.IConduitBundle;
import crazypants.enderio.base.conduit.geom.CollidableComponent;
import crazypants.enderio.base.conduit.geom.Offset;
import crazypants.enderio.conduit.geom.ConnectionModeGeometry;
import crazypants.enderio.conduit.render.DefaultConduitRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;
import java.util.List;

public class EnderLiquidConduitRenderer extends DefaultConduitRenderer {

  @Override
  public boolean isRendererForConduit(@Nonnull IConduit conduit) {
      return conduit instanceof EnderLiquidConduit;
  }

  @Override
  protected void addConduitQuads(@Nonnull IConduitBundle bundle, @Nonnull IConduit conduit, @Nonnull TextureAtlasSprite tex,
      @Nonnull CollidableComponent component, float selfIllum, BlockRenderLayer layer, @Nonnull List<BakedQuad> quads) {

    super.addConduitQuads(bundle, conduit, tex, component, selfIllum, layer, quads);

    EnderLiquidConduit pc = (EnderLiquidConduit) conduit;
    for (EnumFacing dir : conduit.getExternalConnections()) {
      TextureAtlasSprite daTex = null;
      if (conduit.getConnectionMode(dir) == ConnectionMode.INPUT) {
        daTex = pc.getTextureForInputMode();
      } else if (conduit.getConnectionMode(dir) == ConnectionMode.OUTPUT) {
        daTex = pc.getTextureForOutputMode();
      } else if (conduit.getConnectionMode(dir) == ConnectionMode.IN_OUT) {
        daTex = pc.getTextureForInOutMode();
      }
      if (daTex != null) {
        Offset offset = bundle.getOffset(ILiquidConduit.class, dir);
        ConnectionModeGeometry.addModeConnectorQuads(dir, offset, daTex, null, quads);
      }
    }
  }
}
