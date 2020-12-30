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
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import vazkii.botania.client.core.SkyblockWorldInfo;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PacketGogWorld {
	public static final Identifier ID = prefix("gog");

	public static void send(ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, ID, PacketHandler.EMPTY_BUF);
	}

	public static class Handler {
		public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			client.execute(() -> {
				ClientWorld.Properties info = client.world.getLevelProperties();
				if (info instanceof SkyblockWorldInfo) {
					((SkyblockWorldInfo) info).markGardenOfGlass();
				}
			});
		}
	}
}
