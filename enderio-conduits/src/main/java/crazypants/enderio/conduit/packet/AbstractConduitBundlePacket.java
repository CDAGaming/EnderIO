package crazypants.enderio.conduit.packet;

import com.enderio.core.common.network.MessageTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

public abstract class AbstractConduitBundlePacket extends MessageTileEntity<TileEntity> {

  public AbstractConduitBundlePacket() {
  }

  public AbstractConduitBundlePacket(@Nonnull TileEntity tile) {
    super(tile);
  }

}
