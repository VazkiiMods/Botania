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
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleType;

import javax.annotation.Nonnull;

public class WispParticleType extends ParticleType<WispParticleData> {
	public WispParticleType() {
		super(false, WispParticleData.DESERIALIZER);
	}

	@Nonnull
	@Override
	public Codec<WispParticleData> func_230522_e_() {
		return WispParticleData.CODEC;
	}

	public static class Factory implements IParticleFactory<WispParticleData> {
		private final IAnimatedSprite sprite;

		public Factory(IAnimatedSprite sprite) {
			this.sprite = sprite;
		}

		@Override
		public Particle makeParticle(WispParticleData data, ClientWorld world, double x, double y, double z, double mx, double my, double mz) {
			FXWisp ret = new FXWisp(world, x, y, z, mx, my, mz, data.size, data.r, data.g, data.b, data.depthTest, data.maxAgeMul, data.noClip);
			ret.selectSpriteRandomly(sprite);
			return ret;
		}
	}
}
