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
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSampler;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class SkyblockChunkGenerator extends ChunkGenerator {
	public static final Codec<SkyblockChunkGenerator> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					RegistryLookupCodec.create(Registry.NOISE_REGISTRY).forGetter(gen -> gen.noises),
					BiomeSource.CODEC.fieldOf("biome_source").forGetter(gen -> gen.biomeSource),
					Codec.LONG.fieldOf("seed").stable().forGetter(gen -> gen.seed),
					NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(gen -> gen.settings)
			).apply(instance, instance.stable(SkyblockChunkGenerator::new)));

	public static void init() {
		Registry.register(Registry.CHUNK_GENERATOR, prefix("skyblock"), SkyblockChunkGenerator.CODEC);
	}

	private final Registry<NormalNoise.NoiseParameters> noises;
	private final long seed;
	private final Supplier<NoiseGeneratorSettings> settings;
	private final Climate.Sampler sampler;

	public SkyblockChunkGenerator(Registry<NormalNoise.NoiseParameters> noises, BiomeSource provider, long seed, Supplier<NoiseGeneratorSettings> settings) {
		super(provider, provider, settings.get().structureSettings(), seed);
		this.noises = noises;
		this.seed = seed;
		this.settings = settings;
		this.sampler = new NoiseSampler(settings.get().noiseSettings(), settings.get().isNoiseCavesEnabled(), seed, noises, settings.get().getRandomSource());
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
		return new SkyblockChunkGenerator(this.noises, this.biomeSource.withSeed(newSeed), newSeed, settings);
	}

	@Override
	public Climate.Sampler climateSampler() {
		return this.sampler;
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, StructureFeatureManager structureManager, ChunkAccess chunk) {
		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	public int getSeaLevel() {
		return this.settings.get().seaLevel();
	}

	@Override
	public int getMinY() {
		return this.settings.get().noiseSettings().minY();
	}

	@Override
	public void buildSurface(WorldGenRegion region, StructureFeatureManager structures, ChunkAccess chunk) {

	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {

	}

	@Override
	public int getGenDepth() {
		return this.settings.get().noiseSettings().height();
	}

	@Override
	public void applyCarvers(WorldGenRegion worldGenRegion, long seed, BiomeManager biomeManager, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess, GenerationStep.Carving carving) {

	}

	@Override
	public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureFeatureManager structureManager) {}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types heightmapType, LevelHeightAccessor levelHeightAccessor) {
		return DimensionType.MIN_Y;
	}

	@Override
	public NoiseColumn getBaseColumn(int i, int j, LevelHeightAccessor levelHeightAccessor) {
		return new NoiseColumn(levelHeightAccessor.getMinBuildHeight(), new BlockState[0]);
	}
}
