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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.OverworldBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class WorldTypeSkyblock extends WorldPreset {
	public static final WorldPreset INSTANCE = new WorldTypeSkyblock();

	private WorldTypeSkyblock() {
		super("botania-skyblock");
	}

	@Override
	protected ChunkGenerator generator(Registry<Biome> biomes, Registry<NoiseGeneratorSettings> noiseSettings, long seed) {
		return new SkyblockChunkGenerator(new OverworldBiomeSource(seed, false, false, biomes), seed,
				() -> noiseSettings.get(NoiseGeneratorSettings.OVERWORLD));
	}
}
