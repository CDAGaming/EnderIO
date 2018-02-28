package info.loenwind.autosave.handlers.java;

import info.loenwind.autosave.handlers.IHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class HandleArrayList<E> extends HandleAbstractCollection<E, ArrayList<E>> {

  protected HandleArrayList(IHandler<E> elemHandler) {
    super(elemHandler);
  }

  @Override
  protected @Nonnull ArrayList<E> makeCollection() {
    return new ArrayList<E>();
  }

}
