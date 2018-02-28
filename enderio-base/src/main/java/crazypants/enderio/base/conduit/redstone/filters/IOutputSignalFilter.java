package crazypants.enderio.base.conduit.redstone.filters;

import com.enderio.core.common.util.DyeColor;
import crazypants.enderio.base.conduit.redstone.signals.BundledSignal;
import crazypants.enderio.base.conduit.redstone.signals.CombinedSignal;
import crazypants.enderio.base.conduit.redstone.signals.ISignalSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A filter that can be added to a redstone conduit to filter its output
 *
 */
public interface IOutputSignalFilter {

  default @Nonnull CombinedSignal apply(@Nonnull DyeColor color, @Nonnull BundledSignal bundledSignal, @Nullable ISignalSource ignore) {
    return CombinedSignal.NONE;
  }

}