/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PacketLeftClick {
	public static final ResourceLocation ID = prefix("lc");

	public static void send() {
		ClientPlayNetworking.send(ID, PacketHandler.EMPTY_BUF);
	}

	public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
		server.execute(() -> ItemTerraSword.trySpawnBurst(player));
	}
}
