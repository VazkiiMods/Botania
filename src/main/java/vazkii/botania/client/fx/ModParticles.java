/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.common.lib.LibMisc;

import static vazkii.botania.common.block.ModBlocks.register;

public class ModParticles {
	@ObjectHolder(LibMisc.MOD_ID + ":wisp") public static ParticleType<WispParticleData> WISP;

	@ObjectHolder(LibMisc.MOD_ID + ":sparkle") public static ParticleType<SparkleParticleData> SPARKLE;

	public static void registerParticles(RegistryEvent.Register<ParticleType<?>> evt) {
		register(evt.getRegistry(), "wisp", new WispParticleType());
		register(evt.getRegistry(), "sparkle", new SparkleParticleType());
	}

	public static class FactoryHandler {
		public static void registerFactories(ParticleFactoryRegisterEvent evt) {
			Minecraft.getInstance().particles.registerFactory(ModParticles.WISP, WispParticleType.Factory::new);
			Minecraft.getInstance().particles.registerFactory(ModParticles.SPARKLE, SparkleParticleType.Factory::new);
		}
	}
}
