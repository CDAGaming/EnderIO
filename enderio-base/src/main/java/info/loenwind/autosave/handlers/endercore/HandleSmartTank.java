package info.loenwind.autosave.handlers.endercore;

import com.enderio.core.common.NBTAction;
import com.enderio.core.common.fluid.SmartTank;
import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Set;

public class HandleSmartTank implements IHandler<SmartTank> {

  public HandleSmartTank() {
  }

  @Override
  public boolean canHandle(Class<?> clazz) {
    return SmartTank.class.isAssignableFrom(clazz);
  }

  @Override
  public boolean store(@Nonnull Registry registry, @Nonnull Set<NBTAction> phase, @Nonnull NBTTagCompound nbt, @Nonnull String name, @Nonnull SmartTank object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    object.writeCommon(name, nbt);
    return true;
  }

  @Override
  public SmartTank read(@Nonnull Registry registry, @Nonnull Set<NBTAction> phase, @Nonnull NBTTagCompound nbt, @Nullable Field field, @Nonnull String name,
      @Nullable SmartTank object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    if (nbt.hasKey(name)) {
      if (object != null) {
        object.readCommon(name, nbt);
      } else {
        object = SmartTank.createFromNBT(name, nbt);
      }
    }
    return object;
  }

}
