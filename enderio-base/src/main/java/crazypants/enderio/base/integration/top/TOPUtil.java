package crazypants.enderio.base.integration.top;

import crazypants.enderio.api.upgrades.IDarkSteelUpgrade;
import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.Log;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

@EventBusSubscriber(modid = EnderIO.MODID)
public class TOPUtil {

  private TOPUtil() {
  }

  public static void create() {
    if (Loader.isModLoaded("theoneprobe")) {
      FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "crazypants.enderio.base.integration.top.TOPCompatibility");
    }
  }

  @SubscribeEvent
  public static void registerDarkSteelUpgrades(@Nonnull RegistryEvent.Register<IDarkSteelUpgrade> event) {
    if (Loader.isModLoaded("theoneprobe")) {
      event.getRegistry().register(TheOneProbeUpgrade.getInstance());
      Log.info("Dark Steel Upgrades: TOP integration loaded");
    }
  }

}
