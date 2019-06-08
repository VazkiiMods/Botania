package vazkii.botania.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketItemAge {
	private final int entityId;
	private final int age;

	public PacketItemAge(int entityId, int age) {
		this.entityId = entityId;
		this.age = age;
	}

	public static PacketItemAge decode(PacketBuffer buf) {
		return new PacketItemAge(buf.readVarInt(), buf.readVarInt());
	}

	public static void encode(PacketItemAge msg, PacketBuffer buf) {
		buf.writeVarInt(msg.entityId);
		buf.writeVarInt(msg.age);
	}

	public static void handle(PacketItemAge message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Entity e = Minecraft.getInstance().world.getEntityByID(message.entityId);
			if(e instanceof ItemEntity) {
				((ItemEntity) e).age = message.age;
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
