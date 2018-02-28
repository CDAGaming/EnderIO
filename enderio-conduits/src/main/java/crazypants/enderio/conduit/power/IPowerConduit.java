package crazypants.enderio.conduit.power;

import crazypants.enderio.base.conduit.IExtractor;
import crazypants.enderio.base.power.IPowerInterface;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;

public interface IPowerConduit extends IEnergyStorage, IExtractor {

  // TODO Lang

  String ICON_KEY = "blocks/power_conduit";
  String ICON_KEY_INPUT = "blocks/power_conduit_input";
  String ICON_KEY_OUTPUT = "blocks/power_conduit_output";
  String ICON_CORE_KEY = "blocks/power_conduit_core";
  String ICON_TRANSMISSION_KEY = "blocks/power_conduit_transmission";

  String COLOR_CONTROLLER_ID = "ColorController";

  IPowerInterface getExternalPowerReceptor(@Nonnull EnumFacing direction);

  TextureAtlasSprite getTextureForInputMode();

  TextureAtlasSprite getTextureForOutputMode();

  // called from NetworkPowerManager
  void onTick();

  boolean getConnectionsDirty();

  void setEnergyStored(int energy);

  int getMaxEnergyRecieved(@Nonnull EnumFacing dir);

  int getMaxEnergyExtracted(@Nonnull EnumFacing dir);
}
