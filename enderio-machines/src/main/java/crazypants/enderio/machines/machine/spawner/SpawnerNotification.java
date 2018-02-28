package crazypants.enderio.machines.machine.spawner;

import crazypants.enderio.api.ILocalizable;
import crazypants.enderio.machines.EnderIOMachines;

import javax.annotation.Nonnull;

public enum SpawnerNotification implements ILocalizable {

  AREA_FULL("areaFull"),
  NO_LOCATION_FOUND("noLocationFound"),
  NO_LOCATION_AT_ALL("noLocationAtAll"),
  BAD_SOUL("badSoul"),
  NO_PLAYER("noPlayer");

  private final String langStr;

  private SpawnerNotification(String langStr) {
    this.langStr = langStr;
  }

  public @Nonnull String getDisplayString() {
    return EnderIOMachines.lang.localizeExact(getUnlocalizedName());
  }

  @Override
  @Nonnull
  public String getUnlocalizedName() {
    return EnderIOMachines.lang.addPrefix("block_powered_spawner.note." + langStr);
  }

}
