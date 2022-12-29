package vazkii.botania.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.relic.ItemLokiRing;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketLokiToggleAck implements IMessage, IMessageHandler<PacketLokiToggleAck, IMessage> {

    public boolean state;

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        state = byteBuf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(PacketLokiToggleAck message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        final ItemStack aRing = ItemLokiRing.getLokiRing(mc.thePlayer) ;
        if (aRing != null) {
            ItemLokiRing.setMode(aRing, message.state);
            ItemLokiRing.renderHUDNotification();
        }

        return null;
    }
}
