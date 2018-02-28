package crazypants.enderio.base.filter.gui;

import crazypants.enderio.base.filter.IItemFilter;

import javax.annotation.Nonnull;

public interface IItemFilterContainer {

  /**
   *
   * @return The ItemFilter in the container
   */
  @Nonnull
  IItemFilter getItemFilter();

  /**
   * Called when the filter in the container is changed
   */
  void onFilterChanged();

}
