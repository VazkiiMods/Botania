/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.network.clientbound;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import vazkii.botania.client.fx.WispParticleData;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record FlugelEyeEffectPacket(int entityId) implements CustomPacketPayload {

	public static final Type<FlugelEyeEffectPacket> ID = new Type<>(botaniaRL("fe"));
	public static final StreamCodec<ByteBuf, FlugelEyeEffectPacket> STREAM_CODEC = ByteBufCodecs.VAR_INT
			.map(FlugelEyeEffectPacket::new, FlugelEyeEffectPacket::entityId);

	@Override
	public Type<FlugelEyeEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(FlugelEyeEffectPacket packet, Player localPlayer) {
			Level level = localPlayer.level();
			Entity entity = level.getEntity(packet.entityId());
			if (entity != null) {
				RandomSource rng = level.getRandom();
				for (int i = 0; i < 15; i++) {
					double x1 = entity.getX() + Math.random();
					double y1 = entity.getY() + Math.random();
					double z1 = entity.getZ() + Math.random();
					WispParticleData data = WispParticleData.wisp(rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), 1);
					level.addParticle(data, x1, y1, z1, 0, 0.3 - rng.nextDouble() * 0.2, 0);
				}
			}
		}
	}
}
