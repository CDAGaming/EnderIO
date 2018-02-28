package crazypants.enderio.machines.machine.obelisk.base;

import crazypants.enderio.base.capacitor.ICapacitorKey;
import crazypants.enderio.base.machine.baselegacy.SlotDefinition;
import info.loenwind.autosave.annotations.Storable;

import javax.annotation.Nonnull;

@Storable
public abstract class AbstractSpawningObeliskEntity extends AbstractMobObeliskEntity implements ISpawnCallback {

  private boolean registered = false;

  @Override
  public abstract @Nonnull String getMachineName();

  public AbstractSpawningObeliskEntity(@Nonnull SlotDefinition slotDefinition, @Nonnull ICapacitorKey maxEnergyRecieved,
      @Nonnull ICapacitorKey maxEnergyStored, @Nonnull ICapacitorKey maxEnergyUsed) {
    super(slotDefinition, maxEnergyRecieved, maxEnergyStored, maxEnergyUsed);
  }

  @Override
  public void invalidate() {
    super.invalidate();
    SpawningObeliskController.instance.deregisterGuard(this);
    registered = false;
  }

  @Override
  protected boolean processTasks(boolean redstoneCheck) {
    if (redstoneCheck && hasPower() && canWork()) {
      if (!registered) {
        SpawningObeliskController.instance.registerGuard(this);
        registered = true;
      }
      usePower();
    }
    return false;
  }

}