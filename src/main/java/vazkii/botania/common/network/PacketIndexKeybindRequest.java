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
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

public class PacketIndexKeybindRequest {
	public static final Identifier ID = prefix("idx");

	public static void send(ItemStack stack) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeItemStack(stack);
		ClientPlayNetworking.send(ID, buf);
	}

	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		ItemStack stack = buf.readItemStack();
		server.execute(() -> {
			if (player.isSpectator()) {
				return;
			}

			boolean checkNBT = stack.getTag() != null && !stack.getTag().isEmpty();
			for (TileCorporeaIndex index : TileCorporeaIndex.InputHandler.getNearbyIndexes(player)) {
				if (index.getSpark() != null) {
					index.performPlayerRequest(player, CorporeaHelper.instance().createMatcher(stack, checkNBT), stack.getCount());
				}
			}
		});
	}
}
