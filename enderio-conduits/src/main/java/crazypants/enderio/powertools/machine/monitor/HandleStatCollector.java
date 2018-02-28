package crazypants.enderio.powertools.machine.monitor;

import com.enderio.core.common.NBTAction;
import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.util.Set;

@ParametersAreNonnullByDefault
public class HandleStatCollector implements IHandler<StatCollector> {

  @Override
  public boolean canHandle(@Nullable Class<?> clazz) {
    return false;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, String name, StatCollector object)
      throws IllegalArgumentException {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("pos", object.getPos());
    tag.setInteger("count", object.getCollectCount());
    tag.setByteArray("data", object.getData());
    nbt.setTag(name, tag);
    return true;
  }

  @Override
  public StatCollector read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, @Nullable Field field, String name, @Nullable StatCollector object)
      throws IllegalArgumentException {
    if (object == null) {
      throw new IllegalArgumentException();
    }
    NBTTagCompound tag = nbt.getCompoundTag(name);
    object.setPos(tag.getInteger("pos"));
    object.setCollectCount(tag.getInteger("count"));
    object.setData(tag.getByteArray("data"));
    return object;
  }

}
