package vazkii.botania.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketItemAge implements IMessage {
	private int entityId;
	private int age;

	public PacketItemAge() {}

	public PacketItemAge(int entityId, int age) {
		this.entityId = entityId;
		this.age = age;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityId = ByteBufUtils.readVarInt(buf, 5);
		age = ByteBufUtils.readVarInt(buf, 5);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, entityId, 5);
		ByteBufUtils.writeVarInt(buf, age, 5);
	}

	public static class Handler implements IMessageHandler<PacketItemAge, IMessage> {
		@Override
		public IMessage onMessage(PacketItemAge message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				Entity e = Minecraft.getMinecraft().world.getEntityByID(message.entityId);
				if(e instanceof EntityItem) {
					((EntityItem) e).age = message.age;
				}
			});
			return null;
		}
	}
}
