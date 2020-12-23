/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class WorldTypeSkyblock extends GeneratorType {
	public static final GeneratorType INSTANCE = new WorldTypeSkyblock();

	private WorldTypeSkyblock() {
		super("botania-skyblock");
	}

	@Override
	protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> noiseSettings, long seed) {
		return new SkyblockChunkGenerator(new VanillaLayeredBiomeSource(seed, false, false, biomes), seed,
				() -> noiseSettings.get(ChunkGeneratorSettings.OVERWORLD));
	}
}
