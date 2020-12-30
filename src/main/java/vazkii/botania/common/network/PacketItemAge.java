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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import vazkii.botania.mixin.AccessorItemEntity;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

public class PacketItemAge {
	public static final Identifier ID = prefix("ia");

	public static void send(ServerPlayerEntity player, int entityId, int age) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeVarInt(entityId);
		buf.writeVarInt(age);
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static class Handler {
		public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			int entityId = buf.readVarInt();
			int age = buf.readVarInt();
			client.execute(() -> {
				Entity e = MinecraftClient.getInstance().world.getEntityById(entityId);
				if (e instanceof ItemEntity) {
					((AccessorItemEntity) e).setAge(age);
				}
			});
		}
	}

}
