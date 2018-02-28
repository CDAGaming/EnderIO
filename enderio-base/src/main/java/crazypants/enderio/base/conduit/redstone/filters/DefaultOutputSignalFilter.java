package crazypants.enderio.base.conduit.redstone.filters;

import com.enderio.core.common.util.DyeColor;
import crazypants.enderio.base.conduit.redstone.signals.BundledSignal;
import crazypants.enderio.base.conduit.redstone.signals.CombinedSignal;
import crazypants.enderio.base.conduit.redstone.signals.ISignalSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DefaultOutputSignalFilter implements IOutputSignalFilter {

  // The filter that is used if no filter is inserted

  @Override
  @Nonnull
  public CombinedSignal apply(@Nonnull DyeColor color, @Nonnull BundledSignal bundledSignal, @Nullable ISignalSource ignore) {
    return bundledSignal.get(color, ignore);
  }

}
