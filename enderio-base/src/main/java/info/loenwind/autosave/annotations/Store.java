package info.loenwind.autosave.annotations;

import com.enderio.core.common.NBTAction;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.handlers.internal.NullHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field to be stored in NBT.
 *
 * <p>
 * Parameters:
 * 
 * <ul>
 * <li>value: An array of {@link NBTAction} keys to designate for which targets the data should be stored.
 * <li>handler: A class implementing {@link IHandler} to use for this field instead of the registered handler.
 * </ul>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Store {

  // Note: @Inherit does not work on fields. HandleStorable has special code to handle that.

  NBTAction[] value() default { NBTAction.SAVE, NBTAction.CLIENT, NBTAction.ITEM };

  @SuppressWarnings("rawtypes") Class<? extends IHandler> handler() default NullHandler.class;
}
