package vazkii.botania.network.clientbound;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.helper.VecHelper;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record SparkManaFlowEffectPacket(int sparkId1, int sparkId2, DyeColor network) implements CustomPacketPayload {

	public static final Type<SparkManaFlowEffectPacket> ID = new Type<>(botaniaRL("sf"));
	public static final StreamCodec<ByteBuf, SparkManaFlowEffectPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SparkManaFlowEffectPacket::sparkId1,
			ByteBufCodecs.VAR_INT, SparkManaFlowEffectPacket::sparkId2,
			DyeColor.STREAM_CODEC, SparkManaFlowEffectPacket::network,
			SparkManaFlowEffectPacket::new
	);

	@Override
	public Type<SparkManaFlowEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(SparkManaFlowEffectPacket packet, Player localPlayer) {
			Level level = localPlayer.level();
			Entity spark1 = level.getEntity(packet.sparkId1());
			Entity spark2 = level.getEntity(packet.sparkId2());

			if (spark1 == null || spark2 == null) {
				return;
			}

			RandomSource rng = level.getRandom();
			double rc = 0.45;
			Vec3 thisVec = VecHelper.fromEntityCenter(spark1).add((rng.nextDouble() - 0.5) * rc, (rng.nextDouble() - 0.5) * rc, (rng.nextDouble() - 0.5) * rc);
			Vec3 receiverVec = VecHelper.fromEntityCenter(spark2).add((rng.nextDouble() - 0.5) * rc, (rng.nextDouble() - 0.5) * rc, (rng.nextDouble() - 0.5) * rc);

			Vec3 motion = receiverVec.subtract(thisVec).scale(0.04);
			int color = packet.network().getTextureDiffuseColor();
			float r = FastColor.ARGB32.red(color) / 255f;
			float g = FastColor.ARGB32.green(color) / 255f;
			float b = FastColor.ARGB32.blue(color) / 255f;
			if (rng.nextFloat() < 0.25f) {
				r += 0.2f * (float) rng.nextGaussian();
				g += 0.2f * (float) rng.nextGaussian();
				b += 0.2f * (float) rng.nextGaussian();
			}
			float size = 0.125f + 0.125f * rng.nextFloat();

			WispParticleData data = WispParticleData.wisp(size, r, g, b).withNoClip(true);
			level.addAlwaysVisibleParticle(data, thisVec.x, thisVec.y, thisVec.z, motion.x, motion.y, motion.z);
		}
	}
}
