package crazypants.enderio.base.machine.interfaces;

import crazypants.enderio.api.ILocalizable;

import javax.annotation.Nonnull;
import java.util.Set;

public interface INotifier {

  @Nonnull
  Set<? extends ILocalizable> getNotification();

}
