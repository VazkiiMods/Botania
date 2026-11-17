/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.network.clientbound;

import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.entity.GaiaGuardianEntity;
import vazkii.botania.common.proxy.Proxy;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record ArenaIndicatorEffectPacket(BlockPos pos) implements CustomPacketPayload {

	public static final Type<ArenaIndicatorEffectPacket> ID = new Type<>(botaniaRL("gai"));
	public static final StreamCodec<ByteBuf, ArenaIndicatorEffectPacket> STREAM_CODEC = BlockPos.STREAM_CODEC
			.map(ArenaIndicatorEffectPacket::new, ArenaIndicatorEffectPacket::pos);

	@Override
	public Type<ArenaIndicatorEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(ArenaIndicatorEffectPacket packet, Player localPlayer) {
			SparkleParticleData data = SparkleParticleData.sparkle(5, 1, 0, 1, 120);
			Level level = localPlayer.level();
			Vec3 center = packet.pos().getCenter();
			for (int i = 0; i < 360; i += 8) {
				double rad = i * (Math.PI / 180);
				Proxy.INSTANCE.addParticleForceNear(level, data,
						center.x - Math.cos(rad) * GaiaGuardianEntity.ARENA_RANGE,
						center.y,
						center.z - Math.sin(rad) * GaiaGuardianEntity.ARENA_RANGE,
						0, 0, 0);
			}
		}
	}
}
