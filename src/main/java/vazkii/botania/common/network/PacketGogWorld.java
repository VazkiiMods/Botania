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
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import vazkii.botania.client.core.SkyblockWorldInfo;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PacketGogWorld {
	public static final Identifier ID = prefix("gog");

	public static void send(ServerPlayerEntity player) {
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, PacketHandler.EMPTY_BUF);
	}

	public static class Handler {
		public static void handle(PacketContext ctx, PacketByteBuf buf) {
			ctx.getTaskQueue().execute(() -> {
				ClientWorld.Properties info = MinecraftClient.getInstance().world.getLevelProperties();
				if (info instanceof SkyblockWorldInfo) {
					((SkyblockWorldInfo) info).markGardenOfGlass();
				}
			});
		}
	}
}
