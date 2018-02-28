package crazypants.enderio.base.machine.base.network;

import com.enderio.core.common.network.MessageTileEntity;
import crazypants.enderio.base.machine.base.te.AbstractCapabilityPoweredMachineEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nonnull;

public class PacketPowerStorage extends MessageTileEntity<AbstractCapabilityPoweredMachineEntity> {

  private int storedEnergy;

  public PacketPowerStorage() {
  }

  public PacketPowerStorage(@Nonnull AbstractCapabilityPoweredMachineEntity ent) {
    super(ent);
    storedEnergy = ent.getEnergy().getEnergyStored();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    buf.writeInt(storedEnergy);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    storedEnergy = buf.readInt();
  }

  public static class Handler implements IMessageHandler<PacketPowerStorage, IMessage> {

    @Override
    public IMessage onMessage(PacketPowerStorage message, MessageContext ctx) {
      AbstractCapabilityPoweredMachineEntity te = message.getTileEntity(Minecraft.getMinecraft().player.world);
      if (te != null) {
        te.getEnergy().setEnergyStored(message.storedEnergy);
      }
      return null;
    }

  }

}
