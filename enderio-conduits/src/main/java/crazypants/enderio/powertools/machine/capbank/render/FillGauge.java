package crazypants.enderio.powertools.machine.capbank.render;

import crazypants.enderio.powertools.machine.capbank.BlockCapBank;
import crazypants.enderio.powertools.machine.capbank.TileCapBank;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

public class FillGauge implements IInfoRenderer {

  @Override
  public void render(@Nonnull TileCapBank te, @Nonnull EnumFacing dir, float partialTick) {
    FillGaugeBakery bakery = new FillGaugeBakery(te.getWorld(), te.getPos(), dir, BlockCapBank.getGaugeIcon());
    if (bakery.canRender()) {
      bakery.render();
    }
  }

}
