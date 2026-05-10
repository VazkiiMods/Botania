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

public record BlackLotusDissolveEffectPacket(BlockPos pos) implements CustomPacketPayload {

	public static final Type<BlackLotusDissolveEffectPacket> ID = new Type<>(botaniaRL("bld"));
	public static final StreamCodec<ByteBuf, BlackLotusDissolveEffectPacket> STREAM_CODEC = BlockPos.STREAM_CODEC
			.map(BlackLotusDissolveEffectPacket::new, BlackLotusDissolveEffectPacket::pos);

	@Override
	public Type<BlackLotusDissolveEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(BlackLotusDissolveEffectPacket packet, Player localPlayer) {
			Level level = localPlayer.level();
			RandomSource rng = level.random;
			Vec3 pos = packet.pos().getCenter();
			for (int i = 0; i < 50; i++) {
				float r = rng.nextFloat() * 0.35f;
				float g = 0;
				float b = rng.nextFloat() * 0.35f;
				float s = rng.nextFloat() * 0.1125f;

				double mx = (rng.nextDouble() - 0.5) * 0.045;
				double my = rng.nextDouble() * 0.045;
				double mz = (rng.nextDouble() - 0.5) * 0.045;

				WispParticleData data = WispParticleData.wisp(s, r, g, b);
				level.addParticle(data, pos.x, pos.y, pos.z, mx, my, mz);
			}
		}
	}
}
