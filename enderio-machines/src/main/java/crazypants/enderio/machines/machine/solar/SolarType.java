package crazypants.enderio.machines.machine.solar;

import com.enderio.core.common.util.NullHelper;
import crazypants.enderio.machines.config.config.SolarConfig;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum SolarType implements IStringSerializable {

  SIMPLE(".simple"),
  NORMAL(""),
  ADVANCED(".advanced"),
  VIBRANT(".vibrant");

  public static final @Nonnull PropertyEnum<SolarType> KIND = PropertyEnum.<SolarType> create("kind", SolarType.class);

  private final @Nonnull String unlocalisedName;

  private SolarType(@Nonnull String unlocalisedName) {
    this.unlocalisedName = unlocalisedName;
  }

  public boolean connectTo(@Nonnull SolarType other) {
    return this == other;
  }

  @Override
  public @Nonnull String getName() {
    return NullHelper.notnullJ(name().toLowerCase(Locale.ENGLISH), "String.toLowerCase()");
  }

  public static @Nonnull SolarType getTypeFromMeta(int meta) {
    return NullHelper.notnullJ(values()[meta >= 0 && meta < values().length ? meta : 0], "Enum.values()");
  }

  public static int getMetaFromType(@Nonnull SolarType solarType) {
    return solarType.ordinal();
  }

  public @Nonnull String getUnlocalisedName() {
    return unlocalisedName;
  }

  public int getRfperTick() {
    switch (this) {
    case SIMPLE:
      return SolarConfig.solarPanelOneOutput.get();
    case NORMAL:
      return SolarConfig.solarPanelTwoOutput.get();
    case ADVANCED:
      return SolarConfig.solarPanelThreeOutput.get();
    case VIBRANT:
      return SolarConfig.solarPanelFourOutput.get();
    default:
      return 0;
    }
  }
}