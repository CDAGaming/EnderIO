package crazypants.enderio.base.machine.baselegacy;

import crazypants.enderio.base.capacitor.ICapacitorKey;
import crazypants.enderio.base.power.ILegacyPoweredTile;
import info.loenwind.autosave.annotations.Storable;

import javax.annotation.Nonnull;

import static crazypants.enderio.base.capacitor.CapacitorKey.NO_POWER_INTAKE;

@Storable
public abstract class AbstractGeneratorEntity extends AbstractPoweredMachineEntity implements ILegacyPoweredTile {

  protected AbstractGeneratorEntity(@Nonnull SlotDefinition slotDefinition, @Nonnull ICapacitorKey maxEnergyStored, @Nonnull ICapacitorKey maxEnergyUsed) {
    super(slotDefinition, NO_POWER_INTAKE, maxEnergyStored, maxEnergyUsed);
  }

}
