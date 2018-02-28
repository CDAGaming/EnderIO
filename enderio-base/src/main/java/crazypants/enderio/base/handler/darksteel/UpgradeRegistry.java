package crazypants.enderio.base.handler.darksteel;

import com.enderio.core.common.util.NNList;
import crazypants.enderio.api.upgrades.IDarkSteelUpgrade;
import crazypants.enderio.base.EnderIO;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@EventBusSubscriber(modid = EnderIO.MODID)
public class UpgradeRegistry {

  private static IForgeRegistry<IDarkSteelUpgrade> REGISTRY = null;

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public static void registerRegistry(@Nonnull RegistryEvent.NewRegistry event) {
    REGISTRY = new RegistryBuilder<IDarkSteelUpgrade>().setName(new ResourceLocation(EnderIO.DOMAIN, "upgrades")).setType(IDarkSteelUpgrade.class)
        .setIDRange(0, Integer.MAX_VALUE - 1).create();
  }

  public static @Nonnull NNList<IDarkSteelUpgrade> getUpgrades() {
    return NNList.wrap(REGISTRY.getValues());
  }

  public static @Nullable IDarkSteelUpgrade getUpgrade(ResourceLocation id) {
    return REGISTRY.getValue(id);
  }

}
