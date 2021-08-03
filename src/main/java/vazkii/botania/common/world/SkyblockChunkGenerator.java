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

import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.function.Supplier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class SkyblockChunkGenerator extends ChunkGenerator {
	// [VanillaCopy] overworld chunk generator codec
	public static final Codec<SkyblockChunkGenerator> CODEC = RecordCodecBuilder.create(
			(instance) -> instance.group(
					BiomeSource.CODEC.fieldOf("biome_source").forGetter((gen) -> gen.runtimeBiomeSource),
					Codec.LONG.fieldOf("seed").stable().forGetter((gen) -> gen.seed),
					NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter((gen) -> gen.settings)
			).apply(instance, instance.stable(SkyblockChunkGenerator::new)));

	public static void init() {
		Registry.register(Registry.CHUNK_GENERATOR, prefix("skyblock"), SkyblockChunkGenerator.CODEC);
	}

	private final long seed;
	private final Supplier<NoiseGeneratorSettings> settings;

	public SkyblockChunkGenerator(BiomeSource provider, long seed, Supplier<NoiseGeneratorSettings> settings) {
		super(provider, provider, settings.get().structureSettings(), seed);
		this.seed = seed;
		this.settings = settings;
	}

	public static boolean isWorldSkyblock(Level world) {
		return world.getChunkSource() instanceof ServerChunkCache
				&& ((ServerChunkCache) world.getChunkSource()).getGenerator() instanceof SkyblockChunkGenerator;
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long newSeed) {
		return new SkyblockChunkGenerator(this.runtimeBiomeSource.withSeed(newSeed), newSeed, settings);
	}

	@Override
	public void fillFromNoise(LevelAccessor world, StructureFeatureManager structureManager, ChunkAccess chunk) {

	}

	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion region, ChunkAccess chunk) {

	}

	@Override
	public void applyCarvers(long seed, BiomeManager biomes, ChunkAccess chunk, GenerationStep.Carving stage) {}

	@Override
	public void applyBiomeDecoration(WorldGenRegion region, StructureFeatureManager structureManager) {}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types heightmapType) {
		return 0;
	}

	@Override
	public BlockGetter getBaseColumn(int p_230348_1_, int p_230348_2_) {
		return new NoiseColumn(new BlockState[0]);
	}
}
