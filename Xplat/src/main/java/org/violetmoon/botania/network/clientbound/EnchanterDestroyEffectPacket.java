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
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.WispParticleData;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record EnchanterDestroyEffectPacket(BlockPos pos) implements CustomPacketPayload {

	public static final Type<EnchanterDestroyEffectPacket> ID = new Type<>(botaniaRL("ed"));
	public static final StreamCodec<ByteBuf, EnchanterDestroyEffectPacket> STREAM_CODEC = BlockPos.STREAM_CODEC
			.map(EnchanterDestroyEffectPacket::new, EnchanterDestroyEffectPacket::pos);

	@Override
	public Type<EnchanterDestroyEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(EnchanterDestroyEffectPacket packet, Player localPlayer) {
			Level world = localPlayer.level();
			RandomSource rng = world.random;
			Vec3 pos = packet.pos().getCenter();
			for (int i = 0; i < 50; i++) {
				float red = rng.nextFloat();
				float green = rng.nextFloat();
				float blue = rng.nextFloat();
				WispParticleData data = WispParticleData.wisp(rng.nextFloat() * 0.15f + 0.15f, red, green, blue);
				world.addParticle(data, pos.x, pos.y, pos.z,
						(rng.nextDouble() - 0.5) * 0.25,
						(rng.nextDouble() - 0.5) * 0.25,
						(rng.nextDouble() - 0.5) * 0.25);
			}
		}
	}
}
