package crazypants.enderio.base.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public interface IRemoteExec {

  void setGuiID(int id);

  int getGuiID();

  interface IContainer extends IRemoteExec {

    IMessage networkExec(int id, GuiPacket message);

  }

  interface IGui extends IRemoteExec {

  }

}