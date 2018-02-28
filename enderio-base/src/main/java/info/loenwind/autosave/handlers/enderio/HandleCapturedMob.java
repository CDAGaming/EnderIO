package info.loenwind.autosave.handlers.enderio;

import com.enderio.core.common.NBTAction;
import crazypants.enderio.util.CapturedMob;
import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Set;

public class HandleCapturedMob implements IHandler<CapturedMob> {

  public HandleCapturedMob() {
  }

  @Override
  public boolean canHandle(Class<?> clazz) {
    return CapturedMob.class.isAssignableFrom(clazz);
  }

  @Override
  public boolean store(@Nonnull Registry registry, @Nonnull Set<NBTAction> phase, @Nonnull NBTTagCompound nbt, @Nonnull String name, @Nonnull CapturedMob object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    nbt.setTag(name, object.toNbt(null));
    return true;
  }

  @Override
  public CapturedMob read(@Nonnull Registry registry, @Nonnull Set<NBTAction> phase, @Nonnull NBTTagCompound nbt, @Nullable Field field, @Nonnull String name,
      @Nullable CapturedMob object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    if (nbt.hasKey(name)) {
      return CapturedMob.create(nbt.getCompoundTag(name));
    }
    return null;
  }

}
