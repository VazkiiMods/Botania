package vazkii.botania.network.clientbound;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import vazkii.botania.client.fx.SparkleParticleData;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record DivaCharmEffectPacket(int entityId) implements CustomPacketPayload {

	public static final Type<DivaCharmEffectPacket> ID = new Type<>(botaniaRL("dc"));
	public static final StreamCodec<ByteBuf, DivaCharmEffectPacket> STREAM_CODEC = ByteBufCodecs.VAR_INT
			.map(DivaCharmEffectPacket::new, DivaCharmEffectPacket::entityId);

	@Override
	public Type<DivaCharmEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(DivaCharmEffectPacket packet, Player localPlayer) {
			Level level = localPlayer.level();
			Entity target = level.getEntity(packet.entityId());
			if (target == null) {
				return;
			}

			SparkleParticleData data = SparkleParticleData.sparkle(1, 1, 1, 0.25f, 3);
			for (int i = 0; i < 50; i++) {
				level.addParticle(data,
						target.getRandomX(1.0),
						target.getRandomY(),
						target.getRandomZ(1.0),
						0, 0, 0);
			}
		}
	}
}
