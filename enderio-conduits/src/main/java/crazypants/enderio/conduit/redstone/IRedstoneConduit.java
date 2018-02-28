package crazypants.enderio.conduit.redstone;

import com.enderio.core.common.util.DyeColor;
import crazypants.enderio.base.conduit.ConnectionMode;
import crazypants.enderio.base.conduit.IConduit;
import crazypants.enderio.base.conduit.redstone.signals.Signal;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;

// TODO Javadocs

public interface IRedstoneConduit extends IConduit {

  String KEY_CONDUIT_ICON = "blocks/redstone_conduit";
  String KEY_TRANSMISSION_ICON = "blocks/redstone_conduit_transmission";

  String KEY_INS_CONDUIT_ICON = "blocks/redstone_insulated_conduit";
  String KEY_INS_CORE_OFF_ICON = "blocks/redstone_insulated_conduit_core_off";
  String KEY_INS_CORE_ON_ICON = "blocks/redstone_insulated_conduit_core_on";

  // External redstone interface

  int isProvidingStrongPower(@Nonnull EnumFacing toDirection);

  int isProvidingWeakPower(@Nonnull EnumFacing toDirection);

  Set<Signal> getNetworkInputs(@Nonnull EnumFacing side);

  Collection<Signal> getNetworkOutputs(@Nonnull EnumFacing side);

  DyeColor getSignalColor(@Nonnull EnumFacing dir);

  void updateNetwork();

  // Old insulated interface

  String COLOR_CONTROLLER_ID = "ColorController";

  void onInputsChanged(@Nonnull EnumFacing side, int[] inputValues);

  void onInputChanged(@Nonnull EnumFacing side, int inputValue);

  void forceConnectionMode(@Nonnull EnumFacing dir, @Nonnull ConnectionMode mode);

  void setSignalColor(@Nonnull EnumFacing dir, @Nonnull DyeColor col);

  boolean isOutputStrong(@Nonnull EnumFacing dir);

  void setOutputStrength(@Nonnull EnumFacing dir, boolean isStrong);
}
