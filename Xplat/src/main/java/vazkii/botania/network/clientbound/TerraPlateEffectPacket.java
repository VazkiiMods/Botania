package vazkii.botania.network.clientbound;

import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.block_entity.TerrestrialAgglomerationPlateBlockEntity;
import vazkii.botania.common.proxy.Proxy;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record TerraPlateEffectPacket(BlockPos pos, int completionPercent) implements CustomPacketPayload {

	public static final Type<TerraPlateEffectPacket> ID = new Type<>(botaniaRL("tp"));
	public static final StreamCodec<ByteBuf, TerraPlateEffectPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, TerraPlateEffectPacket::pos,
			ByteBufCodecs.VAR_INT, TerraPlateEffectPacket::completionPercent,
			TerraPlateEffectPacket::new
	);

	@Override
	public Type<TerraPlateEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(TerraPlateEffectPacket packet, Player localPlayer) {
			Level level = localPlayer.level();
			BlockEntity te = level.getBlockEntity(packet.pos());
			if (te instanceof TerrestrialAgglomerationPlateBlockEntity) {
				int ticks = packet.completionPercent();

				int totalSpiritCount = 3;
				double tickIncrement = 360.0 / totalSpiritCount;

				int speed = 5;
				double wticks = ticks * speed - tickIncrement;

				double radius = Math.sin((ticks - 100) / 10.0) * 2;
				double vY = Math.sin(wticks * Math.PI / 180 * 0.55) * -0.05;

				float r = 0F;
				float g = ticks / 100f;
				float b = 1f - ticks / 100f;
				Vec3 pos = packet.pos().getCenter();
				RandomSource rng = level.random;

				for (int i = 0; i < totalSpiritCount; i++) {
					double angle = wticks * (Math.PI / 180);
					double wx = pos.x + Math.sin(angle) * radius;
					double wy = pos.y - 0.25 + Math.abs(radius) * 0.7;
					double wz = pos.z + Math.cos(angle) * radius;

					wticks += tickIncrement;
					WispParticleData primaryData = WispParticleData.wisp(0.85f, r, g, b, 0.25f);
					Proxy.INSTANCE.addParticleForceNear(level, primaryData, wx, wy, wz, 0, vY, 0);
					WispParticleData data = WispParticleData.wisp(rng.nextFloat() * 0.1f + 0.1f, r, g, b, 0.9f);
					level.addParticle(data, wx, wy, wz,
							(rng.nextDouble() - 0.5) * 0.05,
							(rng.nextDouble() - 0.5) * 0.05,
							(rng.nextDouble() - 0.5) * 0.05);
				}

				if (ticks == 100) {
					for (int j = 0; j < 15; j++) {
						WispParticleData data = WispParticleData.wisp(rng.nextFloat() * 0.15f + 0.15f, r, g, b);
						level.addParticle(data, pos.x, pos.y, pos.z,
								(rng.nextDouble() - 0.5) * 0.125,
								(rng.nextDouble() - 0.5) * 0.125,
								(rng.nextDouble() - 0.5) * 0.125);
					}
				}
			}
		}
	}
}
