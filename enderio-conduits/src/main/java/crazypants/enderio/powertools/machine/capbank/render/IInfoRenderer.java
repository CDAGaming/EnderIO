package crazypants.enderio.powertools.machine.capbank.render;

import crazypants.enderio.powertools.machine.capbank.TileCapBank;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

public interface IInfoRenderer {

  void render(@Nonnull TileCapBank cb, @Nonnull EnumFacing dir, float partialTick);

}
