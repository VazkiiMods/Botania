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
import vazkii.botania.common.helper.VecHelper;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record HaloCraftEffectPacket(int entityId) implements CustomPacketPayload {

	public static final Type<HaloCraftEffectPacket> ID = new Type<>(botaniaRL("hc"));
	public static final StreamCodec<ByteBuf, HaloCraftEffectPacket> STREAM_CODEC = ByteBufCodecs.VAR_INT
			.map(HaloCraftEffectPacket::new, HaloCraftEffectPacket::entityId);

	@Override
	public Type<HaloCraftEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(HaloCraftEffectPacket packet, Player localPlayer) {
			Level level = localPlayer.level();
			Entity target = level.getEntity(packet.entityId());
			if (target != null) {
				Vec3 lookVec3 = target.getLookAngle();
				Vec3 centerVector = VecHelper.fromEntityCenter(target).add(lookVec3.x * 3, 1.3, lookVec3.z * 3);
				RandomSource rng = level.getRandom();
				for (int i = 0; i < 4; i++) {
					WispParticleData data = WispParticleData.wisp(0.2f + 0.2f * rng.nextFloat(), 1, 0, 1);
					target.level().addParticle(data, centerVector.x, centerVector.y, centerVector.z,
							(rng.nextDouble() - 0.5) * 0.1,
							(rng.nextDouble() - 0.5) * 0.1,
							(rng.nextDouble() - 0.5) * 0.1);
				}
			}
		}
	}
}
