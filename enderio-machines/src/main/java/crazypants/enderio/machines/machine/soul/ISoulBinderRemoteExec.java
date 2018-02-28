package crazypants.enderio.machines.machine.soul;

import crazypants.enderio.base.network.GuiPacket;
import crazypants.enderio.base.network.IRemoteExec;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import javax.annotation.Nonnull;

public interface ISoulBinderRemoteExec {

  int DWN_XP = 0;

  interface GUI extends IRemoteExec.IGui {

    default void doDrainXP(int levels) {
      GuiPacket.send(this, DWN_XP, levels);
    }

  }

  interface Container extends IRemoteExec.IContainer {

    IMessage doDrainXP(@Nonnull EntityPlayer player, int level);

    @Override
    default IMessage networkExec(int id, GuiPacket message) {
      if (id == DWN_XP) {
        return doDrainXP(message.getPlayer(), message.getInt(0));
      }
      return null;
    }

  }

}
