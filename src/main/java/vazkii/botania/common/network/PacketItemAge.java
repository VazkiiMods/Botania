/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.botania.mixin.AccessorItemEntity;

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
		if (ctx.get().getDirection().getReceptionSide().isClient()) {
			ctx.get().enqueueWork(() -> {
				Entity e = Minecraft.getInstance().world.getEntityByID(message.entityId);
				if (e instanceof ItemEntity) {
					((AccessorItemEntity) e).setAge(message.age);
				}
			});
		}
		ctx.get().setPacketHandled(true);
	}
}
