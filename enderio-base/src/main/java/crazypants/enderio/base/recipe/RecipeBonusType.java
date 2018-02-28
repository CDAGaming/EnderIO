package crazypants.enderio.base.recipe;

import javax.annotation.Nonnull;

public enum RecipeBonusType {
  NONE(false, false),
  MULTIPLY_OUTPUT(true, true),
  CHANCE_ONLY(false, true);

  private final boolean multiply, chances;

  RecipeBonusType(boolean multiply, boolean chances) {
    this.multiply = multiply;
    this.chances = chances;
  }

  public boolean doMultiply() {
    return multiply;
  }

  public boolean doChances() {
    return chances;
  }

  public boolean useBalls() {
    return multiply || chances;
  }

  public @Nonnull RecipeBonusType withoutMultiply(boolean stripMultiply) {
    if (!stripMultiply || !multiply) {
      return this;
    } else if (this == MULTIPLY_OUTPUT) {
      return CHANCE_ONLY;
    } else {
      return NONE;
    }
  }

}
