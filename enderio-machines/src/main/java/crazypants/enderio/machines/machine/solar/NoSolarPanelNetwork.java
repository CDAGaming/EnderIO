package crazypants.enderio.machines.machine.solar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public class NoSolarPanelNetwork implements ISolarPanelNetwork {

  public static final @Nonnull ISolarPanelNetwork INSTANCE = new NoSolarPanelNetwork();

  private NoSolarPanelNetwork() {
  }

  @Override
  @Nonnull
  public String[] getConduitProbeData(@Nonnull EntityPlayer player, @Nullable EnumFacing side) {
    return new String[] { toString() };
  }

  @Override
  public boolean isValid() {
    return false;
  }

  @Override
  public void extractEnergy(int maxExtract) {
  }

  @Override
  public int getEnergyAvailableThisTick() {
    return 0;
  }

  @Override
  public int getEnergyAvailablePerTick() {
    return 0;
  }

  @Override
  public int getEnergyMaxPerTick() {
    return 0;
  }

  @Override
  public void destroyNetwork() {
  }

  @Override
  public @Nonnull Set<BlockPos> getPanels() {
    return Collections.emptySet();
  }

}
