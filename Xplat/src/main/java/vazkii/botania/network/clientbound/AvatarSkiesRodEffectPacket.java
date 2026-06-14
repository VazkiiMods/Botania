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
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.handler.BotaniaSounds;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record AvatarSkiesRodEffectPacket(boolean elytra, int entityId) implements CustomPacketPayload {

	public static final Type<AvatarSkiesRodEffectPacket> ID = new Type<>(botaniaRL("as"));
	public static final StreamCodec<ByteBuf, AvatarSkiesRodEffectPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, AvatarSkiesRodEffectPacket::elytra,
			ByteBufCodecs.VAR_INT, AvatarSkiesRodEffectPacket::entityId,
			AvatarSkiesRodEffectPacket::new
	);

	@Override
	public Type<AvatarSkiesRodEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(AvatarSkiesRodEffectPacket packet, Player localPlayer) {
			Level world = localPlayer.level();
			Entity p = world.getEntity(packet.entityId());
			if (p != null) {
				p.playSound(BotaniaSounds.dash, 1, 1);
				RandomSource rng = world.random;
				WispParticleData data =
						WispParticleData.wisp(0.35f + rng.nextFloat() * 0.1f, 0.25f, 0.25f, 0.25f);
				if (packet.elytra()) {
					Vec3 lookDir = p.getLookAngle();
					for (int i = 0; i < 20; i++) {
						for (int j = 0; j < 5; j++) {
							world.addParticle(data,
									p.getX() + lookDir.x() * i,
									p.getY() + lookDir.y() * i,
									p.getZ() + lookDir.z() * i,
									0.2 * (rng.nextDouble() - 0.5) * (Math.abs(lookDir.y()) + Math.abs(lookDir.z())) + -0.01 * rng.nextDouble() * lookDir.x(),
									0.2 * (rng.nextDouble() - 0.5) * (Math.abs(lookDir.x()) + Math.abs(lookDir.z())) + -0.01 * rng.nextDouble() * lookDir.y(),
									0.2 * (rng.nextDouble() - 0.5) * (Math.abs(lookDir.y()) + Math.abs(lookDir.x())) + -0.01 * rng.nextDouble() * lookDir.z());
						}
					}
				} else {
					for (int i = 0; i < 20; i++) {
						for (int j = 0; j < 5; j++) {
							world.addParticle(data,
									p.getX(),
									p.getY() + i,
									p.getZ(),
									0.2 * (rng.nextDouble() - 0.5),
									-0.01 * rng.nextDouble(),
									0.2 * (rng.nextDouble() - 0.5));
						}
					}
				}
			}
		}
	}
}
