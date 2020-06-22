/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;

public class SkyblockChunkGenerator extends OverworldChunkGenerator {
	public static final ChunkGeneratorType<OverworldGenSettings, SkyblockChunkGenerator> TYPE = new ChunkGeneratorType<>(SkyblockChunkGenerator::new, false, OverworldGenSettings::new);

	private SkyblockChunkGenerator(IWorld worldIn, BiomeProvider provider, OverworldGenSettings settingsIn) {
		super(worldIn, provider, settingsIn);
	}

	@Override
	public void makeBase(IWorld worldIn, IChunk chunkIn) {}

	@Override
	public void generateSurface(WorldGenRegion region, IChunk chunkIn) {}

	@Override
	public void decorate(WorldGenRegion region) {}

	@Override
	public void func_225550_a_(BiomeManager biomeManager, IChunk chunkIn, GenerationStage.Carving carvingSettings) {}
}
