/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.fx;

import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;

import static vazkii.botania.common.block.ModBlocks.register;

public class ModParticles {
	public static final ParticleType<WispParticleData> WISP = new WispParticleType();
	public static final ParticleType<SparkleParticleData> SPARKLE = new SparkleParticleType();

	public static void registerParticles(RegistryEvent.Register<ParticleType<?>> evt) {
		register(evt.getRegistry(), "wisp", WISP);
		register(evt.getRegistry(), "sparkle", SPARKLE);
	}

	public static class FactoryHandler {
		public static void registerFactories(ParticleFactoryRegisterEvent evt) {
			MinecraftClient.getInstance().particleManager.registerFactory(ModParticles.WISP, WispParticleType.Factory::new);
			MinecraftClient.getInstance().particleManager.registerFactory(ModParticles.SPARKLE, SparkleParticleType.Factory::new);
		}
	}
}
