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
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;

import javax.annotation.Nullable;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

public class PacketUpdateItemsRemaining {
	public static final Identifier ID = prefix("rem");

	public static void send(PlayerEntity player, ItemStack stack, int count, @Nullable Text tooltip) {
		if (player instanceof ServerPlayerEntity) {
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			buf.writeItemStack(stack);
			buf.writeVarInt(count);
			buf.writeText(tooltip);
			ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
		}
	}

	public static class Handler {
		public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			ItemStack stack = buf.readItemStack();
			int count = buf.readVarInt();
			Text tooltip = buf.readText();
			client.execute(() -> ItemsRemainingRenderHandler.set(stack, count, tooltip));
		}
	}
}
