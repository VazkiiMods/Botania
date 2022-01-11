/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;

import javax.annotation.Nullable;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

public class PacketUpdateItemsRemaining {
	public static final ResourceLocation ID = prefix("rem");

	public static void send(Player player, ItemStack stack, int count, @Nullable Component tooltip) {
		if (player instanceof ServerPlayer) {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeItem(stack);
			buf.writeVarInt(count);
			buf.writeComponent(tooltip);
			ServerPlayNetworking.send((ServerPlayer) player, ID, buf);
		}
	}

	public static class Handler {
		public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
			ItemStack stack = buf.readItem();
			int count = buf.readVarInt();
			Component tooltip = buf.readComponent();
			client.execute(() -> ItemsRemainingRenderHandler.set(stack, count, tooltip));
		}
	}
}
