/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.network.serverbound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.network.IPacket;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public record PacketIndexKeybindRequest(ItemStack stack) implements IPacket {
	public static final ResourceLocation ID = prefix("idx");

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeItem(stack());
	}

	@Override
	public ResourceLocation getFabricId() {
		return ID;
	}

	public static PacketIndexKeybindRequest decode(FriendlyByteBuf buf) {
		return new PacketIndexKeybindRequest(buf.readItem());
	}

	public void handle(MinecraftServer server, ServerPlayer player) {
		var stack = this.stack();
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
