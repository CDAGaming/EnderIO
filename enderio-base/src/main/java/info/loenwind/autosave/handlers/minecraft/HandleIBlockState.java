package info.loenwind.autosave.handlers.minecraft;

import com.enderio.core.common.NBTAction;
import crazypants.enderio.base.paint.PaintUtil;
import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Set;

public class HandleIBlockState implements IHandler<IBlockState> {

  public HandleIBlockState() {
  }

  @Override
  public boolean canHandle(Class<?> clazz) {
    return IBlockState.class.isAssignableFrom(clazz);
  }

  @Override
  public boolean store(@Nonnull Registry registry, @Nonnull Set<NBTAction> phase, @Nonnull NBTTagCompound nbt, @Nonnull String name, @Nonnull IBlockState object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    NBTTagCompound tag = new NBTTagCompound();
    PaintUtil.writeNbt(tag, object);
    nbt.setTag(name, tag);
    return true;
  }

  @Override
  public IBlockState read(@Nonnull Registry registry, @Nonnull Set<NBTAction> phase, @Nonnull NBTTagCompound nbt, @Nullable Field field, @Nonnull String name,
      @Nullable IBlockState object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    return PaintUtil.readNbt(nbt.getCompoundTag(name));
  }

}
