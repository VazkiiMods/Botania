/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.fx;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

import static vazkii.botania.common.block.ModBlocks.register;

public class ModParticles {
	public static final ParticleType<WispParticleData> WISP = new WispParticleType();
	public static final ParticleType<SparkleParticleData> SPARKLE = new SparkleParticleType();

	public static void registerParticles(BiConsumer<ParticleType<?>, ResourceLocation> r) {
		register(r, "wisp", WISP);
		register(r, "sparkle", SPARKLE);
	}

	public static class FactoryHandler {
		public static void registerFactories() {
			ParticleFactoryRegistry.getInstance().register(ModParticles.WISP, WispParticleType.Factory::new);
			ParticleFactoryRegistry.getInstance().register(ModParticles.SPARKLE, SparkleParticleType.Factory::new);
		}
	}
}
