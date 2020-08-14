/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import net.minecraft.util.Identifier;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;

import java.util.function.Supplier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PacketIndexKeybindRequest {
	public static final Identifier ID = prefix("idx");

	public static void send(ItemStack stack) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeItemStack(stack);
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID, buf);
	}

	public static void handle(PacketContext ctx, PacketByteBuf buf) {
		ItemStack stack = buf.readItemStack();
		ctx.getTaskQueue().execute(() -> {
			ServerPlayerEntity player = (ServerPlayerEntity) ctx.getPlayer();
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
