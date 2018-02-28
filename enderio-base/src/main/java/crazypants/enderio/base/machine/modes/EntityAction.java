package crazypants.enderio.base.machine.modes;

import com.enderio.core.common.util.NNList;
import crazypants.enderio.util.CapturedMob;

import javax.annotation.Nonnull;

public enum EntityAction { // TODO 1.11 implement on obelisks and spawner
  ATTRACT("entity_action.block_attractor_obelisk.action"),
  AVERT("entity_action.block_aversion_obelisk.action"),
  RELOCATE("entity_action.block_relocator_obelisk.action"),
  SPAWN("entity_action.block_powered_spawner.action"),

  ;

  private final @Nonnull String langKey;

  private EntityAction(@Nonnull String langKey) {
    this.langKey = langKey;
  }

  public String getActionString() {
    // Note: Must be translated by the consumer
    return langKey;
  }

  public static interface Implementer {

    @Nonnull
    NNList<CapturedMob> getEntities();

    @Nonnull
    EntityAction getEntityAction();

  }

}
