/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

import vazkii.botania.common.components.EntityComponents;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

public class PacketItemAge {
	public static final ResourceLocation ID = prefix("ia");

	public static void send(ServerPlayer player, int entityId, int counter) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeVarInt(entityId);
		buf.writeVarInt(counter);
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static class Handler {
		public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
			int entityId = buf.readVarInt();
			int counter = buf.readVarInt();
			client.execute(() -> {
				Entity e = Minecraft.getInstance().level.getEntity(entityId);
				if (e instanceof ItemEntity) {
					EntityComponents.INTERNAL_ITEM.get(e).timeCounter = counter;
				}
			});
		}
	}

}
