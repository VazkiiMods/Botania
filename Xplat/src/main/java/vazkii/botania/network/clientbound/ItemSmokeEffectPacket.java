package vazkii.botania.network.clientbound;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.network.CompressedPosition;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record ItemSmokeEffectPacket(Vec3 position, int itemId, int numParticles) implements CustomPacketPayload {

	public static final Type<ItemSmokeEffectPacket> ID = new Type<>(botaniaRL("is"));
	public static final StreamCodec<ByteBuf, ItemSmokeEffectPacket> STREAM_CODEC = StreamCodec.composite(
			CompressedPosition.VEC3_STREAM_CODEC, ItemSmokeEffectPacket::position,
			ByteBufCodecs.VAR_INT, ItemSmokeEffectPacket::itemId,
			ByteBufCodecs.VAR_INT, ItemSmokeEffectPacket::numParticles,
			ItemSmokeEffectPacket::new
	);

	@Override
	public Type<ItemSmokeEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(ItemSmokeEffectPacket packet, Player localPlayer) {
			Level level = localPlayer.level();
			RandomSource rng = level.getRandom();
			Entity item = level.getEntity(packet.itemId());
			if (item == null) {
				return;
			}

			int p = packet.numParticles();
			Vec3 pos = packet.position();

			// [VanillaCopy] similar to Mob::spawnAnim(), except at a different world position
			for (int i = 0; i < p; i++) {
				double d0 = rng.nextGaussian() * 0.01;
				double d1 = rng.nextGaussian() * 0.01;
				double d2 = rng.nextGaussian() * 0.01;
				double d3 = 10.0;
				item.level().addParticle(ParticleTypes.POOF,
						pos.x + rng.nextDouble() * item.getBbWidth() * 2 - item.getBbWidth() - d0 * d3,
						pos.y + rng.nextDouble() * item.getBbHeight() - d1 * d3,
						pos.z + rng.nextDouble() * item.getBbWidth() * 2 - item.getBbWidth() - d2 * d3,
						d0, d1, d2);
			}
		}
	}
}
