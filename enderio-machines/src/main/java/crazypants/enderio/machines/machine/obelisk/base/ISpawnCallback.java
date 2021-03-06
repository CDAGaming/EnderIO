package crazypants.enderio.machines.machine.obelisk.base;

import net.minecraft.entity.EntityLivingBase;

import javax.annotation.Nonnull;

public interface ISpawnCallback {

  enum Result {
    NEXT,
    DENY,
    DONE
  }

  @Nonnull
  Result isSpawnPrevented(EntityLivingBase mob);

}