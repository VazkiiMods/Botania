/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.network.serverbound;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.api.corporea.CorporeaHelper;
import org.violetmoon.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public record IndexKeybindRequestPacket(ItemStack stack) implements CustomPacketPayload {
	public static final Type<IndexKeybindRequestPacket> ID = new Type<>(botaniaRL("idx"));
	public static final StreamCodec<RegistryFriendlyByteBuf, IndexKeybindRequestPacket> STREAM_CODEC = ItemStack.STREAM_CODEC
			.map(IndexKeybindRequestPacket::new, IndexKeybindRequestPacket::stack);

	@Override
	public Type<IndexKeybindRequestPacket> type() {
		return ID;
	}

	public void handle(ServerPlayer player) {
		if (player.isSpectator()) {
			return;
		}
		// The count comes from the client unchecked; clamp it like chat requests are clamped,
		// and limit how often one player can trigger a network-wide request
		int count = Math.min(this.stack().getCount(), CorporeaIndexBlockEntity.MAX_REQUEST);
		if (count <= 0 || !CorporeaIndexBlockEntity.tryStartPlayerRequest(player)) {
			return;
		}

		for (CorporeaIndexBlockEntity index : CorporeaIndexBlockEntity.getNearbyValidIndexes(player)) {
			index.performPlayerRequest(player, CorporeaHelper.instance().createMatcher(this.stack(), true), count);
		}
	}
}
