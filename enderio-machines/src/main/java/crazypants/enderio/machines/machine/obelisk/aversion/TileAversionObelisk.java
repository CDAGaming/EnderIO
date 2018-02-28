package crazypants.enderio.machines.machine.obelisk.aversion;

import crazypants.enderio.base.machine.baselegacy.SlotDefinition;
import crazypants.enderio.base.machine.modes.EntityAction;
import crazypants.enderio.machines.init.MachineObject;
import crazypants.enderio.machines.machine.obelisk.base.AbstractSpawningObeliskEntity;
import info.loenwind.autosave.annotations.Storable;
import net.minecraft.entity.EntityLivingBase;

import javax.annotation.Nonnull;

import static crazypants.enderio.machines.capacitor.CapacitorKey.*;

@Storable
public class TileAversionObelisk extends AbstractSpawningObeliskEntity {

  public TileAversionObelisk() {
    super(new SlotDefinition(12, 0), AVERSION_POWER_INTAKE, AVERSION_POWER_BUFFER, AVERSION_POWER_USE);
  }

  @Override
  public float getRange() {
    return AVERSION_RANGE.get(getCapacitorData());
  }

  @Override
  public @Nonnull String getMachineName() {
    return MachineObject.block_aversion_obelisk.getUnlocalisedName();
  }

  @Override
  public @Nonnull Result isSpawnPrevented(EntityLivingBase mob) {
    return (redstoneCheckPassed && hasPower() && isMobInRange(mob) && isMobInFilter(mob)) ? Result.DENY : Result.NEXT;
  }

  @Override
  public @Nonnull EntityAction getEntityAction() {
    return EntityAction.AVERT;
  }

}
