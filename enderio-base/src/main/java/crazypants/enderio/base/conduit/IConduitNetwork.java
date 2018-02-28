package crazypants.enderio.base.conduit;

import crazypants.enderio.base.handler.ServerTickHandler.ITickListener;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author EpicSquid315
 *
 * @param <T>
 *          Base Conduit Class
 * @param <I>
 *          Implementation of the Conduit Class
 */
public interface IConduitNetwork<T extends IConduit, I extends T> extends ITickListener {

  // TODO: Tidy and edit Javadocs
  void init(@Nonnull IConduitBundle tile, Collection<I> connections, @Nonnull World world);

  @Nonnull Class<T> getBaseConduitType();

  void setNetwork(@Nonnull World world, @Nonnull IConduitBundle tile);

  void addConduit(@Nonnull I newConduit);

  void destroyNetwork();

  @Nonnull List<I> getConduits();

  void sendBlockUpdatesForEntireNetwork();

}
