package crazypants.enderio.machines.machine.teleport.telepad.gui;

import crazypants.enderio.base.network.GuiPacket;
import crazypants.enderio.base.network.IRemoteExec;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import javax.annotation.Nonnull;

public interface IDialingDeviceRemoteExec {

  int ID_DO_TELEPORT = 0;

  interface GUI extends IRemoteExec.IGui {

    default void doTeleport(@Nonnull BlockPos telepad, int targetID, boolean initiateTeleport) {
      GuiPacket.send(this, ID_DO_TELEPORT, targetID, initiateTeleport, telepad);
    }

  }

  interface Container extends IRemoteExec.IContainer {

    IMessage doTeleport(@Nonnull BlockPos telepad, int targetID, boolean initiateTeleport);

    @Override
    default IMessage networkExec(int id, GuiPacket message) {
      if (id == ID_DO_TELEPORT) {
        return doTeleport(BlockPos.fromLong(message.getLong(2)), message.getInt(0), message.getBoolean(1));
      }
      return null;
    }

  }

}
