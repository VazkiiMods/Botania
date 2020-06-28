/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

public class SkyblockChunkGenerator extends ChunkGenerator {
	private static final Codec<SkyblockChunkGenerator> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					BiomeProvider.field_235202_a_.fieldOf("biome_source").forGetter(scg -> scg.biomeProvider),
					DimensionStructuresSettings.field_236190_a_.fieldOf("structures").forGetter(ChunkGenerator::func_235957_b_)
			).apply(instance, instance.stable(SkyblockChunkGenerator::new)));

	public SkyblockChunkGenerator(BiomeProvider provider, DimensionStructuresSettings settings) {
		super(provider, settings);
	}

	@Override
	protected Codec<? extends ChunkGenerator> func_230347_a_() {
		return CODEC;
	}

	@Override
	public ChunkGenerator func_230349_a_(long newSeed) {
		return new SkyblockChunkGenerator(this.biomeProvider.func_230320_a_(newSeed), func_235957_b_());
	}

	@Override
	public void func_230352_b_(IWorld world, StructureManager structureManager, IChunk chunk) {

	}

	@Override
	public void generateSurface(WorldGenRegion region, IChunk chunk) {

	}

	@Override
	public void func_230350_a_(long seed, BiomeManager biomes, IChunk chunk, GenerationStage.Carving stage) {}

	@Override
	public void func_230351_a_(WorldGenRegion region, StructureManager structureManager) {}

	@Override
	public int func_222529_a(int x, int z, Heightmap.Type heightmapType) {
		return 0;
	}

	@Override
	public IBlockReader func_230348_a_(int p_230348_1_, int p_230348_2_) {
		return new Blockreader(new BlockState[0]);
	}
}
