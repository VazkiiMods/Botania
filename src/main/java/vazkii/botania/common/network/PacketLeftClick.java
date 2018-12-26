package vazkii.botania.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;

public class PacketLeftClick implements IMessage {
	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}

	public static class Handler implements IMessageHandler<PacketLeftClick, IMessage> {

		@Override
		public IMessage onMessage(PacketLeftClick message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			player.server.addScheduledTask(() -> ((ItemTerraSword) ModItems.terraSword).trySpawnBurst(player));
			return null;
		}
	}

}
