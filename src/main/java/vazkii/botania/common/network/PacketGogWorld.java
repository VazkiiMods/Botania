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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import vazkii.botania.client.core.SkyblockWorldInfo;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PacketGogWorld {
	public static final ResourceLocation ID = prefix("gog");

	public static void send(ServerPlayer player) {
		ServerPlayNetworking.send(player, ID, PacketHandler.EMPTY_BUF);
	}

	public static class Handler {
		public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
			client.execute(() -> {
				ClientLevel.ClientLevelData info = client.level.getLevelData();
				if (info instanceof SkyblockWorldInfo) {
					((SkyblockWorldInfo) info).markGardenOfGlass();
				}
			});
		}
	}
}
