package crazypants.enderio.powertools.machine.gauge;

import com.enderio.core.client.render.ManagedTESR;
import com.enderio.core.common.util.NullHelper;
import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.power.IPowerInterface;
import crazypants.enderio.powertools.machine.capbank.render.FillGaugeBakery;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Map.Entry;

import static crazypants.enderio.powertools.init.PowerToolObject.block_gauge;

public class TESRGauge extends ManagedTESR<TileGauge> {

  public TESRGauge() {
    super(block_gauge.getBlock());
  }

  @Override
  protected void renderTileEntity(@Nonnull TileGauge te, @Nonnull IBlockState blockState, float partialTicks, int destroyStage) {
    RenderHelper.enableStandardItemLighting();
    World world = te.getWorld();
    Map<EnumFacing, IPowerInterface> sides = BlockGauge.getDisplays(world, te.getPos());
    if (!sides.isEmpty()) {
      for (Entry<EnumFacing, IPowerInterface> side : sides.entrySet()) {
        IPowerInterface eh = side.getValue();
        EnumFacing face = side.getKey().getOpposite();
        int energyStored = eh.getEnergyStored();
        int maxEnergyStored = eh.getMaxEnergyStored();
        float ratio = maxEnergyStored > 0 ? (float) energyStored / (float) maxEnergyStored : 0f;
        FillGaugeBakery bakery = new FillGaugeBakery(world, te.getPos().offset(NullHelper.first(side.getKey(), EnumFacing.DOWN)), face,
            BlockGauge.gaugeIcon.get(TextureAtlasSprite.class), ratio);
        if (bakery.canRender()) {
          GL11.glPushMatrix();
          GL11.glTranslated(-face.getFrontOffsetX(), -face.getFrontOffsetY(), -face.getFrontOffsetZ());
          bakery.render();
          GL11.glPopMatrix();
        }
      }
    } else {
      double v = EnderIO.proxy.getTickCount() % 100 + partialTicks;
      if (v > 50) {
        v = 100 - v;
      }
      double ratio = v / 50d;
      for (EnumFacing face : EnumFacing.Plane.HORIZONTAL) {
        FillGaugeBakery bakery = new FillGaugeBakery(world, te.getPos().offset(face.getOpposite()), face, BlockGauge.gaugeIcon.get(TextureAtlasSprite.class),
            ratio);
        if (bakery.canRender()) {
          GL11.glPushMatrix(); // sic!
          GL11.glTranslated(-face.getFrontOffsetX(), -face.getFrontOffsetY(), -face.getFrontOffsetZ());
          bakery.render();
          GL11.glPopMatrix();
        }
      }
    }
  }

}
