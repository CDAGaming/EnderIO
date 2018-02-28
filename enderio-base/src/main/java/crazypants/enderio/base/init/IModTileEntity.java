package crazypants.enderio.base.init;

import com.enderio.core.common.util.NullHelper;
import crazypants.enderio.base.EnderIO;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IModTileEntity {

  @Nonnull
  String getUnlocalisedName();

  default @Nonnull ResourceLocation getRegistryName() {
    return new ResourceLocation(EnderIO.DOMAIN, getUnlocalisedName());
  }

  @Nonnull
  Class<? extends TileEntity> getTileEntityClass();

  default @Nullable TileEntity getTileEntity() {
    try {
      return getTileEntityClass().newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  default @Nonnull TileEntity getTileEntityNN() {
    return NullHelper.notnull(getTileEntity(), "TileEntity " + this + " is unexpectedly missing");
  }

}
