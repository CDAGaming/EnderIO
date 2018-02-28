package crazypants.enderio.machines.config.config;

import crazypants.enderio.base.Log;
import crazypants.enderio.base.config.Config.Section;
import crazypants.enderio.base.config.SectionedValueFactory;
import crazypants.enderio.base.config.ValueFactory.IValue;
import crazypants.enderio.base.config.config.PersonalConfig;
import crazypants.enderio.machines.config.Config;

import javax.annotation.Nonnull;
import java.util.Locale;

public final class ClientConfig {

  public static final SectionedValueFactory F = new SectionedValueFactory(Config.F, new Section("", "client"));

  public static final IValue<Boolean> jeiUseShortenedPainterRecipes = F.make("jeiUseShortenedPainterRecipes", true, //
      "If true, only a handful of sample painter recipes will be shown in JEI. Enable this if you have timing problems "
          + "starting a world or logging into a server.");

  public static final IValue<Boolean> machineSoundsEnabled = PersonalConfig.machineSoundsEnabled;

  public static final IValue<Float> machineSoundVolume = PersonalConfig.machineSoundsVolume;

  public static final IValue<Boolean> bloodEnabled = new IValue<Boolean>() {
    private final IValue<Integer> bloodEnabledInt = F.make("bloodEnabled", 0, "Should blood be red or green? (-1=green, 0=auto, 1=red)").setRange(-1, 1);
    private boolean hasLogged = false;

    @Override
    public @Nonnull Boolean get() {
      final boolean overrideNeeded = Locale.getDefault().getCountry().equals(Locale.GERMANY.getCountry());
      final Integer value = bloodEnabledInt.get();
      if (overrideNeeded && value == 0) {
        if (!hasLogged) {
          Log.warn("Detected local country '" + Locale.getDefault().getCountry() + "', cencoring blood.");
          hasLogged = true;
        }
        return false;
      } else {
        return value > 0;
      }
    }
  };

}
