package crazypants.enderio.base.item.coordselector;

import crazypants.enderio.api.teleport.ITravelAccessable;

import javax.annotation.Nonnull;

public interface ICoordinateAware {

  interface SingleTarget extends ICoordinateAware, ITravelAccessable {

    void setTarget(@Nonnull TelepadTarget target);

  }

  interface MultipleTargets extends ICoordinateAware {

    void addTarget(@Nonnull TelepadTarget target);

  }

}
