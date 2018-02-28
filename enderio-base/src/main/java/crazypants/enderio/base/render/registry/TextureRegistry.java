package crazypants.enderio.base.render.registry;

import com.enderio.core.client.render.RenderUtil;
import com.enderio.core.common.util.NullHelper;
import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.Log;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TextureRegistry {

  public static interface TextureSupplier {
    @Nonnull
    <T extends Object> T get(@Nonnull Class<T> clazz);
  }

  private static final @Nonnull TextureSupplier noSupplier = new TextureSupplier() {
    @SuppressWarnings("null")
    @Override
    @Nonnull
    public <T> T get(@Nonnull Class<T> clazz) {
      Log.error("Client side method TextureSupplier.get() called on server!");
      return null;
    }
  };

  private static class TextureRegistryServer {
    private TextureRegistryServer() {
    }

    protected void init() {
    }

    public @Nonnull TextureSupplier registerTexture(final @Nonnull String location, boolean prependDomain) {
      return noSupplier;
    }

  }

  private static class TextureRegistryClient extends TextureRegistryServer {

    @SideOnly(Side.CLIENT)
    private Map<String, TextureAtlasSprite> sprites;

    private TextureRegistryClient() {
      super();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void init() {
      sprites = new HashMap<String, TextureAtlasSprite>();
      MinecraftForge.EVENT_BUS.register(instance);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onIconLoad(TextureStitchEvent.Pre event) {
      for (Entry<String, TextureAtlasSprite> entry : sprites.entrySet()) {
        entry.setValue(event.getMap().registerSprite(new ResourceLocation(NullHelper.notnull(entry.getKey(), "internal data corruption"))));
      }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public @Nonnull TextureSupplier registerTexture(final @Nonnull String location, boolean prependModID) {
      String key = location;
      if (prependModID) {
        key = EnderIO.DOMAIN + ":" + location;
      }
      final String keyF = key;
      if (!sprites.containsKey(keyF)) {
        sprites.put(keyF, null);
      }
      return new TextureSupplier() {
        @SuppressWarnings("unchecked")
        @Override
        @Nonnull
        public <T> T get(@Nonnull Class<T> clazz) {
          if (clazz == TextureAtlasSprite.class) {
            final TextureAtlasSprite sprite = sprites.get(keyF);
            if (sprite != null) {
              return (T) sprite;
            } else {
              Log.error("Missing texture: " + keyF);
              return (T) RenderUtil.getMissingSprite();
            }
          } else {
            throw new UnsupportedOperationException("TextureSupplier can only supply TextureAtlasSprite");
          }
        }
      };
    }
  }

  private static TextureRegistryServer instance;

  public static @Nonnull TextureSupplier registerTexture(final @Nonnull String location) {
    return registerTexture(location, true);
  }

  public static @Nonnull TextureSupplier registerTexture(final @Nonnull String location, boolean prependDomain) {
    if (instance == null) {
      instance = new TextureRegistryClient();
      instance.init();
    }
    return instance.registerTexture(location, prependDomain);
  }

  private TextureRegistry() {
  }

}
