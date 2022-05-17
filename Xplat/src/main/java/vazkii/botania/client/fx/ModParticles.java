/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.fx;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModParticles {
	public static final ParticleType<WispParticleData> WISP = new WispParticleType();
	public static final ParticleType<SparkleParticleData> SPARKLE = new SparkleParticleType();

	public static void registerParticles(BiConsumer<ParticleType<?>, ResourceLocation> r) {
		r.accept(WISP, prefix("wisp"));
		r.accept(SPARKLE, prefix("sparkle"));
	}

	public static class FactoryHandler {
		public interface Consumer {
			<T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor);
		}

		public static void registerFactories(Consumer consumer) {
			consumer.register(ModParticles.WISP, WispParticleType.Factory::new);
			consumer.register(ModParticles.SPARKLE, SparkleParticleType.Factory::new);
		}
	}
}
