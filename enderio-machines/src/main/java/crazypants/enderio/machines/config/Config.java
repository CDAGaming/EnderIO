package crazypants.enderio.machines.config;

import crazypants.enderio.base.config.Config.Section;
import crazypants.enderio.base.config.ValueFactory;
import crazypants.enderio.base.config.ValueFactory.IValue;
import crazypants.enderio.machines.EnderIOMachines;
import crazypants.enderio.machines.config.config.*;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault // Not the right one, but eclipse knows only 3 null annotations anyway, so it's ok
public final class Config {

  public static final Section sectionCapacitor = new Section("", "capacitor");

  public static final ValueFactory F = new ValueFactory(EnderIOMachines.MODID);

  public static final IValue<Float> explosionResistantBlockHardness = new IValue<Float>() {
    @Override
    public @Nonnull Float get() {
      return crazypants.enderio.base.config.Config.EXPLOSION_RESISTANT;
    }
  };

  //

  public static void load() {
    // force sub-configs to be classloaded with the main config
    ClientConfig.F.getClass();
    CombustionGenConfig.F.getClass();
    ExperienceConfig.F.getClass();
    FarmConfig.F.getClass();
    InhibitorConfig.F.getClass();
    KillerJoeConfig.F.getClass();
    SolarConfig.F.getClass();
    SoulBinderConfig.F.getClass();
    SpawnerConfig.F.getClass();
    TankConfig.F.getClass();
    VacuumConfig.F.getClass();
    VatConfig.F.getClass();
    WeatherConfig.F.getClass();
    ZombieGenConfig.F.getClass();
  }
}
