package crazypants.enderio.machines.machine.obelisk.base;

import net.minecraft.entity.EntityLivingBase;

import javax.annotation.Nonnull;

public interface ISpawnCallback {

  public enum Result {
    NEXT,
    DENY,
    DONE;
  }

  @Nonnull
  Result isSpawnPrevented(EntityLivingBase mob);

}