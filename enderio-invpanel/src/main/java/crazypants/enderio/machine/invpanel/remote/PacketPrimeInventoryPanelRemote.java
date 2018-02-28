package crazypants.enderio.machine.invpanel.remote;

import com.enderio.core.common.NBTAction;
import com.enderio.core.common.network.NetworkUtil;
import crazypants.enderio.base.EnderIO;
import crazypants.enderio.machine.invpanel.TileInventoryPanel;
import info.loenwind.autosave.Reader;
import info.loenwind.autosave.Writer;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nonnull;

public class PacketPrimeInventoryPanelRemote implements IMessage, IMessageHandler<PacketPrimeInventoryPanelRemote, IMessage> {

  private NBTTagCompound tag;

  public PacketPrimeInventoryPanelRemote() {
  }

  public PacketPrimeInventoryPanelRemote(@Nonnull TileInventoryPanel te) {
    tag = new NBTTagCompound();
    Writer.write(tag, te);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    tag = NetworkUtil.readNBTTagCompound(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    NetworkUtil.writeNBTTagCompound(tag, buf);
  }

  @Override
  public IMessage onMessage(PacketPrimeInventoryPanelRemote message, MessageContext ctx) {
    ClientRemoteGuiManager.targetTEtime = EnderIO.proxy.getTickCount() + 10;
    TileInventoryPanel te = new TileInventoryPanel();
    Reader.read(NBTAction.CLIENT, message.tag, te);
    ClientRemoteGuiManager.targetTE = te;
    return null;
  }

}
