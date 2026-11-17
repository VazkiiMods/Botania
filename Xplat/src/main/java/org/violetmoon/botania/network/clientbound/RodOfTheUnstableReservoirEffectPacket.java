/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.network.clientbound;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.network.CompressedPosition;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record RodOfTheUnstableReservoirEffectPacket(Vec3 pos) implements CustomPacketPayload {

	public static final Type<RodOfTheUnstableReservoirEffectPacket> ID = new Type<>(botaniaRL("ur"));
	public static final StreamCodec<ByteBuf, RodOfTheUnstableReservoirEffectPacket> STREAM_CODEC = CompressedPosition.VEC3_STREAM_CODEC
			.map(RodOfTheUnstableReservoirEffectPacket::new, RodOfTheUnstableReservoirEffectPacket::pos);

	@Override
	public Type<RodOfTheUnstableReservoirEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(RodOfTheUnstableReservoirEffectPacket packet, Player localPlayer) {
			SparkleParticleData data = SparkleParticleData.sparkle(6, 1, 0.4f, 1, 6);
			localPlayer.level().addParticle(data, packet.pos.x, packet.pos.y, packet.pos.z, 0, 0, 0);
		}
	}
}
