package crazypants.enderio.api.farm;

import crazypants.enderio.api.ILocalizable;

import javax.annotation.Nonnull;

public enum FarmNotification implements ILocalizable {

  OUTPUT_FULL("outputFull"),
  NO_SEEDS("noSeeds"),
  NO_AXE("noAxe"),
  NO_HOE("noHoe"),
  NO_TREETAP("noTreetap"),
  NO_POWER("noPower"),
  NO_SHEARS("noShears");

  private final @Nonnull String langStr;

  FarmNotification(@Nonnull String langStr) {
    this.langStr = "enderio.farm.note." + langStr;
  }

  @Override
  public @Nonnull String getUnlocalizedName() {
    return langStr;
  }

}
