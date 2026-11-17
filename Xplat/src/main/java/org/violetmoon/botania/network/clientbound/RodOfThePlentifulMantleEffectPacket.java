/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.network.clientbound;

import it.unimi.dsi.fastutil.HashCommon;

import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import vazkii.botania.common.item.rod.PlentifulMantleRodItem;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record RodOfThePlentifulMantleEffectPacket(BlockPos center, byte range, boolean randomize) implements CustomPacketPayload {

	public static final Type<RodOfThePlentifulMantleEffectPacket> ID = new Type<>(botaniaRL("pm"));
	public static final StreamCodec<ByteBuf, RodOfThePlentifulMantleEffectPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, RodOfThePlentifulMantleEffectPacket::center,
			ByteBufCodecs.BYTE, RodOfThePlentifulMantleEffectPacket::range,
			ByteBufCodecs.BOOL, RodOfThePlentifulMantleEffectPacket::randomize,
			RodOfThePlentifulMantleEffectPacket::new
	);

	@Override
	public Type<RodOfThePlentifulMantleEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(RodOfThePlentifulMantleEffectPacket packet, Player localPlayer) {
			PlentifulMantleRodItem.doHighlight(localPlayer.level(), packet.center(), packet.range(),
					HashCommon.mix(packet.center().asLong()
							^ (packet.randomize() ? localPlayer.level().getGameTime() : 0L))
			);
		}
	}
}
