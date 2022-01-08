/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.client.gui.screens.worldselection.WorldPreset;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class WorldTypeSkyblock extends WorldPreset {
	public static final WorldPreset INSTANCE = new WorldTypeSkyblock();

	private WorldTypeSkyblock() {
		super("botania-skyblock");
	}

	@Override
	protected ChunkGenerator generator(RegistryAccess registryAccess, long seed) {
		return new SkyblockChunkGenerator(
				registryAccess.registryOrThrow(Registry.NOISE_REGISTRY),
				MultiNoiseBiomeSource.Preset.OVERWORLD.biomeSource(registryAccess.registryOrThrow(Registry.BIOME_REGISTRY)),
				seed,
				() -> registryAccess.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).getOrThrow(NoiseGeneratorSettings.OVERWORLD)
		);
	}
}
