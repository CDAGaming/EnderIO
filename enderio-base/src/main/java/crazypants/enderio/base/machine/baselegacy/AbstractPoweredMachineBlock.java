package crazypants.enderio.base.machine.baselegacy;

import crazypants.enderio.base.init.IModObject;
import net.minecraft.block.material.Material;

import javax.annotation.Nonnull;

public abstract class AbstractPoweredMachineBlock<T extends AbstractPoweredMachineEntity> extends AbstractInventoryMachineBlock<T> {

  AbstractPoweredMachineBlock(@Nonnull IModObject mo, @Nonnull Material mat) {
    super(mo, mat);
  }

  AbstractPoweredMachineBlock(@Nonnull IModObject mo) {
    super(mo);
  }

}
