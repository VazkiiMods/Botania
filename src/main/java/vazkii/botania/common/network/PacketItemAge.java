/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import vazkii.botania.mixin.AccessorItemEntity;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

public class PacketItemAge {
	public static final Identifier ID = prefix("ia");

	public static void send(PlayerEntity player, int entityId, int age) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeVarInt(entityId);
		buf.writeVarInt(age);
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, buf);
	}

	public static void handle(PacketContext ctx, PacketByteBuf buf) {
		int entityId = buf.readVarInt();
		int age = buf.readVarInt();
		ctx.getTaskQueue().execute(() -> {
			Entity e = MinecraftClient.getInstance().world.getEntityById(entityId);
			if (e instanceof ItemEntity) {
				((AccessorItemEntity) e).setAge(age);
			}
		});
	}
}
