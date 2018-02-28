package info.loenwind.autosave.handlers.java;

import com.enderio.core.common.NBTAction;
import info.loenwind.autosave.Registry;
import info.loenwind.autosave.handlers.IHandler;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Set;

public class HandleString implements IHandler<String> {

  public HandleString() {
  }

  @Override
  public boolean canHandle(Class<?> clazz) {
    return String.class.isAssignableFrom(clazz);
  }

  @Override
  public boolean store(@Nonnull Registry registry, @Nonnull Set<NBTAction> phase, @Nonnull NBTTagCompound nbt, @Nonnull String name, @Nonnull String object)
      throws IllegalArgumentException, IllegalAccessException {
    nbt.setString(name, object);
    return true;
  }

  @Override
  public String read(@Nonnull Registry registry, @Nonnull Set<NBTAction> phase, @Nonnull NBTTagCompound nbt, @Nullable Field field, @Nonnull String name,
      @Nullable String object) {
    return nbt.hasKey(name) ? nbt.getString(name) : object;
  }

}
