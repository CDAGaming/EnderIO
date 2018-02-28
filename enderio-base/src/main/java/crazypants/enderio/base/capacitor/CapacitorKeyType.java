package crazypants.enderio.base.capacitor;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum CapacitorKeyType {
  ENERGY_BUFFER,
  ENERGY_INTAKE,
  ENERGY_USE,
  ENERGY_LOSS,
  ENERGY_GEN,
  SPEED,
  AREA,
  AMOUNT,;

  public @Nonnull String getName() {
    return name().toLowerCase(Locale.ENGLISH);
  }

}