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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import vazkii.botania.common.item.rod.ItemTornadoRod;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

public class PacketAvatarTornadoRod {
	public static final Identifier ID = prefix("avatar_tornado_rod");

	public static void sendTo(ServerPlayerEntity player, boolean elytra) {
		if (!player.world.isClient) {
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			buf.writeBoolean(elytra);
			ServerPlayNetworking.send(player, ID, buf);
		}
	}

	public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		boolean elytra = buf.readBoolean();
		client.execute(() -> {
			PlayerEntity player = MinecraftClient.getInstance().player;
			World world = MinecraftClient.getInstance().world;
			if (elytra) {
				ItemTornadoRod.doAvatarElytraBoost(player, world);
			} else {
				ItemTornadoRod.doAvatarJump(player, world);
			}
		});
	}
}
