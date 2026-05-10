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

public record FluegelEyeEffectPacket(int entityId) implements CustomPacketPayload {

	public static final Type<FluegelEyeEffectPacket> ID = new Type<>(botaniaRL("fe"));
	public static final StreamCodec<ByteBuf, FluegelEyeEffectPacket> STREAM_CODEC = ByteBufCodecs.VAR_INT
			.map(FluegelEyeEffectPacket::new, FluegelEyeEffectPacket::entityId);

	@Override
	public Type<FluegelEyeEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(FluegelEyeEffectPacket packet, Player localPlayer) {
			Level level = localPlayer.level();
			Entity entity = level.getEntity(packet.entityId());
			if (entity != null) {
				RandomSource rng = level.random;
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
