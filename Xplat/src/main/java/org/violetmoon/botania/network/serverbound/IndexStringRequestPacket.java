/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.network.serverbound;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import org.violetmoon.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import org.violetmoon.botania.common.helper.PlayerHelper;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record IndexStringRequestPacket(String message) implements CustomPacketPayload {
	public static final Type<IndexStringRequestPacket> ID = new Type<>(botaniaRL("idxs"));
	public static final StreamCodec<ByteBuf, IndexStringRequestPacket> STREAM_CODEC = ByteBufCodecs.STRING_UTF8
			.map(IndexStringRequestPacket::new, IndexStringRequestPacket::message);
	public static final String DOG_CODE = "\t  \n\t\t\t\n\t\t ";

	@Override
	public Type<IndexStringRequestPacket> type() {
		return ID;
	}

	public void handle(ServerPlayer player) {
		if (!message().isBlank()) {
			// A chat request is a chat message, so limit it to the vanilla chat length, and limit
			// how often one player can trigger a network-wide request
			if (message().length() > CorporeaIndexBlockEntity.MAX_REQUEST_MESSAGE_LENGTH
					|| !CorporeaIndexBlockEntity.tryStartPlayerRequest(player)) {
				return;
			}
			CorporeaIndexBlockEntity.onChatMessage(player, message());
		} else if (message().equals(DOG_CODE)) {
			PlayerHelper.grantCriterion(player, botaniaRL("main/dog"), "code_triggered");
		}
	}
}
