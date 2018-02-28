package crazypants.enderio.powertools;

import com.enderio.core.common.Lang;
import com.enderio.core.common.util.NNList;
import crazypants.enderio.api.addon.IEnderIOAddon;
import crazypants.enderio.base.config.recipes.RecipeFactory;
import crazypants.enderio.conduit.EnderIOConduits;
import crazypants.enderio.powertools.config.ConfigHandler;
import crazypants.enderio.powertools.network.PacketHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod(modid = EnderIOPowerTools.MODID, name = EnderIOPowerTools.MOD_NAME, version = EnderIOPowerTools.VERSION, dependencies = "after:" + EnderIOConduits.MODID)
public class EnderIOPowerTools implements IEnderIOAddon {

  public static final @Nonnull String MODID = "enderiopowertools";
  public static final @Nonnull String DOMAIN = "enderio";
  public static final @Nonnull String MOD_NAME = "Ender IO Powertools";
  public static final @Nonnull String VERSION = "@VERSION@";

  public static final @Nonnull Lang lang = new Lang(DOMAIN);

  @Override
  @Nullable
  public Configuration getConfiguration() {
    return ConfigHandler.config;
  }

  @Override
  @Nonnull
  public NNList<Triple<Integer, RecipeFactory, String>> getRecipeFiles() {
    return new NNList<>(Triple.of(2, null, "powertools"));
  }

  @Override
  @Nonnull
  public NNList<String> getExampleFiles() {
    return new NNList<>("powertools_easy_recipes", "powertools_easy_recipes");
  }

  @EventHandler
  public static void preinit(@Nonnull FMLPreInitializationEvent event) {
    ConfigHandler.init(event);
  }

  @EventHandler
  public static void init(FMLInitializationEvent event) {
    PacketHandler.init(event);
  }

  @EventHandler
  public static void postinit(FMLPostInitializationEvent event) {
  }

}
