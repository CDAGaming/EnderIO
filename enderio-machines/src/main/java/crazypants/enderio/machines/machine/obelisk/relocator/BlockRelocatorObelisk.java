package crazypants.enderio.machines.machine.obelisk.relocator;

import crazypants.enderio.base.init.IModObject;
import crazypants.enderio.machines.machine.obelisk.base.AbstractBlockRangedObelisk;
import crazypants.enderio.machines.machine.obelisk.base.SpawningObeliskController;

import javax.annotation.Nonnull;

public class BlockRelocatorObelisk extends AbstractBlockRangedObelisk<TileRelocatorObelisk> {

  public static BlockRelocatorObelisk create(@Nonnull IModObject modObject) {
    BlockRelocatorObelisk res = new BlockRelocatorObelisk(modObject);
    res.init();

    // Just making sure its loaded
    SpawningObeliskController.instance.toString();

    return res;
  }

  protected BlockRelocatorObelisk(@Nonnull IModObject modObject) {
    super(modObject);
  }

}
