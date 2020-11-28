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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;

import javax.annotation.Nullable;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

public class PacketUpdateItemsRemaining {
	public static final Identifier ID = prefix("rem");

	public static void send(PlayerEntity player, ItemStack stack, int count, @Nullable Text tooltip) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeItemStack(stack);
		buf.writeVarInt(count);
		buf.writeText(tooltip);
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, buf);
	}

	public static void handle(PacketContext ctx, PacketByteBuf buf) {
		ItemStack stack = buf.readItemStack();
		int count = buf.readVarInt();
		Text tooltip = buf.readText();
		ctx.getTaskQueue().execute(() -> ItemsRemainingRenderHandler.set(stack, count, tooltip));
	}
}
