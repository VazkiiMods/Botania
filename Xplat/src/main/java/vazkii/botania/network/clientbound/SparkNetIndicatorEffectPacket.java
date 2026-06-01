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
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.SparkleParticleData;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record SparkNetIndicatorEffectPacket(int sparkId1, int sparkId2) implements CustomPacketPayload {

	public static final Type<SparkNetIndicatorEffectPacket> ID = new Type<>(botaniaRL("sni"));
	public static final StreamCodec<ByteBuf, SparkNetIndicatorEffectPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SparkNetIndicatorEffectPacket::sparkId1,
			ByteBufCodecs.VAR_INT, SparkNetIndicatorEffectPacket::sparkId2,
			SparkNetIndicatorEffectPacket::new
	);

	@Override
	public Type<SparkNetIndicatorEffectPacket> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(SparkNetIndicatorEffectPacket packet, Player localPlayer) {
			Level world = localPlayer.level();
			Entity spark1 = world.getEntity(packet.sparkId1());
			Entity spark2 = world.getEntity(packet.sparkId2());

			if (spark1 == null || spark2 == null) {
				return;
			}

			Vec3 orig = new Vec3(spark1.getX(), spark1.getY() + 0.25, spark1.getZ());
			Vec3 end = new Vec3(spark2.getX(), spark2.getY() + 0.25, spark2.getZ());
			Vec3 diff = end.subtract(orig);
			Vec3 movement = diff.normalize().scale(0.1);
			int iters = (int) (diff.length() / movement.length());
			float huePer = 1f / iters;
			float hueSum = (float) Math.random();

			Vec3 currentPos = orig;
			for (int i = 0; i < iters; i++) {
				float hue = i * huePer + hueSum;
				int color = Mth.hsvToRgb(Mth.frac(hue), 1, 1);
				float r = Math.min(1, FastColor.ARGB32.red(color) / 255f + 0.4f);
				float g = Math.min(1, FastColor.ARGB32.green(color) / 255f + 0.4f);
				float b = Math.min(1, FastColor.ARGB32.blue(color) / 255f + 0.4f);

				SparkleParticleData data = SparkleParticleData.noClip(1, r, g, b, 12);
				world.addAlwaysVisibleParticle(data, true, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
				currentPos = currentPos.add(movement);
			}
		}
	}
}
