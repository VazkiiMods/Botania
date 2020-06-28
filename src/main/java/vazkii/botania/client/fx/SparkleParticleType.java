/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.fx;

import com.mojang.serialization.Codec;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleType;

import javax.annotation.Nonnull;

public class SparkleParticleType extends ParticleType<SparkleParticleData> {
	public SparkleParticleType() {
		super(false, SparkleParticleData.DESERIALIZER);
	}

	@Nonnull
	@Override
	public Codec<SparkleParticleData> func_230522_e_() {
		return SparkleParticleData.CODEC;
	}

	public static class Factory implements IParticleFactory<SparkleParticleData> {
		private final IAnimatedSprite sprite;

		public Factory(IAnimatedSprite sprite) {
			this.sprite = sprite;
		}

		@Override
		public Particle makeParticle(SparkleParticleData data, ClientWorld world, double x, double y, double z, double mx, double my, double mz) {
			return new FXSparkle(world, x, y, z, data.size, data.r, data.g, data.b, data.m, data.fake, data.noClip, data.corrupt, sprite);
		}
	}
}
