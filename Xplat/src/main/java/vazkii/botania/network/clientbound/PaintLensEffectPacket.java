package vazkii.botania.network.clientbound;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.SparkleParticleData;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record PaintLensEffectPacket(BlockPos pos, DyeColor color) implements CustomPacketPayload {

	public static final CustomPacketPayload.Type<PaintLensEffectPacket> ID = new CustomPacketPayload.Type<>(botaniaRL("pl"));
	public static final StreamCodec<ByteBuf, PaintLensEffectPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PaintLensEffectPacket::pos,
			DyeColor.STREAM_CODEC, PaintLensEffectPacket::color,
			PaintLensEffectPacket::new
	);

	@Override
	public Type<PaintLensEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(PaintLensEffectPacket packet, Player localPlayer) {
			int color = packet.color().getTextureDiffuseColor();
			float r = FastColor.ARGB32.red(color) / 255f;
			float g = FastColor.ARGB32.green(color) / 255f;
			float b = FastColor.ARGB32.blue(color) / 255f;
			Level level = localPlayer.level();
			RandomSource random = level.getRandom();
			for (int i = 0; i < 10; i++) {
				Vec3 pos = Vec3.atLowerCornerOf(packet.pos().relative(Direction.getRandom(random)));
				SparkleParticleData data = SparkleParticleData.sparkle(0.6f + random.nextFloat() * 0.5f, r, g, b, 5);
				level.addParticle(data,
						pos.x() + random.nextDouble(),
						pos.y() + random.nextDouble(),
						pos.z() + random.nextDouble(),
						0, 0, 0);
			}
		}
	}
}
