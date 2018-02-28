package info.loenwind.autosave.handlers.endercore;

import com.enderio.core.common.NBTAction;
import com.enderio.core.common.util.UserIdent;
import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.handlers.java.HandleArrayList;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Set;

public class HandleUserIdent implements IHandler<UserIdent> {

  public HandleUserIdent() {
  }

  @Override
  public boolean canHandle(Class<?> clazz) {
    return UserIdent.class.isAssignableFrom(clazz);
  }

  @Override
  public boolean store(@Nonnull Registry registry, @Nonnull Set<NBTAction> phase, @Nonnull NBTTagCompound nbt, @Nonnull String name, @Nonnull UserIdent object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    object.saveToNbt(nbt, name);
    return true;
  }

  @Override
  public UserIdent read(@Nonnull Registry registry, @Nonnull Set<NBTAction> phase, @Nonnull NBTTagCompound nbt, @Nullable Field field, @Nonnull String name,
      @Nullable UserIdent object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    return UserIdent.readfromNbt(nbt, name);
  }

  public static class HandleUserIdentArrayList extends HandleArrayList<UserIdent> {

    public HandleUserIdentArrayList() {
      super(new HandleUserIdent());
    }

    @Override
    protected @Nonnull UserIdent makeEmptyValueObject() {
      return UserIdent.NOBODY;
    }

  }

}
