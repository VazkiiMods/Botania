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

import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import vazkii.botania.mixin.AccessorBeardifier;
import vazkii.botania.mixin.AccessorNoiseChunk;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

// [VanillaCopy] NoiseBasedChunkGenerator, with actual worldgen methods dummied out,
// leaving the structure bounding boxes and biomes.
public class SkyblockChunkGenerator extends ChunkGenerator {
	public static final Codec<SkyblockChunkGenerator> CODEC = RecordCodecBuilder.create(
			instance -> commonCodec(instance)
					.and(instance.group(
							RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter(gen -> gen.noises),
							BiomeSource.CODEC.fieldOf("biome_source").forGetter((gen) -> gen.biomeSource),
							Codec.LONG.fieldOf("seed").stable().forGetter(gen -> gen.seed),
							NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(gen -> gen.settings)))
					.apply(instance, instance.stable(SkyblockChunkGenerator::new)));

	public static void init() {
		Registry.register(Registry.CHUNK_GENERATOR, prefix("skyblock"), SkyblockChunkGenerator.CODEC);
	}

	public static boolean isWorldSkyblock(Level world) {
		return world.getChunkSource() instanceof ServerChunkCache
				&& ((ServerChunkCache) world.getChunkSource()).getGenerator() instanceof SkyblockChunkGenerator;
	}

	public static ChunkGenerator createForWorldType(RegistryAccess registryAccess, long seed) {
		return new SkyblockChunkGenerator(
				registryAccess.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
				registryAccess.registryOrThrow(Registry.NOISE_REGISTRY),
				MultiNoiseBiomeSource.Preset.OVERWORLD.biomeSource(registryAccess.registryOrThrow(Registry.BIOME_REGISTRY)),
				seed,
				registryAccess.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).getHolderOrThrow(NoiseGeneratorSettings.OVERWORLD)
		);
	}

	protected final BlockState defaultBlock;
	private final Registry<NormalNoise.NoiseParameters> noises;
	protected final long seed;
	protected final Holder<NoiseGeneratorSettings> settings;
	private final NoiseRouter router;
	protected final Climate.Sampler sampler;
	private final Aquifer.FluidPicker globalFluidPicker;

	private SkyblockChunkGenerator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, BiomeSource biomeSource, long seed, Holder<NoiseGeneratorSettings> settings) {
		this(structureSets, noises, biomeSource, biomeSource, seed, settings);
	}

	private SkyblockChunkGenerator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, BiomeSource biomeSource, BiomeSource runtimeBiomeSource, long seed, Holder<NoiseGeneratorSettings> settings) {
		super(structureSets, Optional.empty(), biomeSource, runtimeBiomeSource, seed);
		this.noises = noises;
		this.seed = seed;
		this.settings = settings;
		NoiseGeneratorSettings genSettings = this.settings.value();
		this.defaultBlock = genSettings.defaultBlock();
		this.router = genSettings.createNoiseRouter(noises, seed);
		this.sampler = new Climate.Sampler(this.router.temperature(), this.router.humidity(), this.router.continents(), this.router.erosion(), this.router.depth(), this.router.ridges(), this.router.spawnTarget());
		Aquifer.FluidStatus lava = new Aquifer.FluidStatus(-54, Blocks.LAVA.defaultBlockState());
		int i = genSettings.seaLevel();
		Aquifer.FluidStatus defaultFluid = new Aquifer.FluidStatus(i, genSettings.defaultFluid());
		this.globalFluidPicker = (p_198228_, p_198229_, p_198230_) -> p_198229_ < Math.min(-54, i) ? lava : defaultFluid;
	}

	@Override
	public CompletableFuture<ChunkAccess> createBiomes(Registry<Biome> biomes, Executor p_197006_, Blender blender, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess) {
		return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("init_biomes", () -> {
			this.doCreateBiomes(blender, structureFeatureManager, chunkAccess);
			return chunkAccess;
		}), Util.backgroundExecutor());
	}

	private void doCreateBiomes(Blender blender, StructureFeatureManager sfm, ChunkAccess chunkAccess) {
		NoiseChunk chunk = chunkAccess.getOrCreateNoiseChunk(this.router, () -> AccessorBeardifier.botania_make(sfm, chunkAccess), this.settings.value(), this.globalFluidPicker, blender);
		BiomeResolver biomeresolver = BelowZeroRetrogen.getBiomeResolver(blender.getBiomeResolver(this.runtimeBiomeSource), chunkAccess);
		chunkAccess.fillBiomesFromNoise(biomeresolver, ((AccessorNoiseChunk) chunk).botania_cachedClimateSampler(this.router));
	}

	@VisibleForDebug
	public NoiseRouter router() {
		return this.router;
	}

	@Override
	public Climate.Sampler climateSampler() {
		return this.sampler;
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return new SkyblockChunkGenerator(this.structureSets, this.noises, this.biomeSource.withSeed(seed), seed, this.settings);
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types heightmapTypes, LevelHeightAccessor levelHeightAccessor) {
		return levelHeightAccessor.getMinBuildHeight();
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor levelHeightAccessor) {
		return new NoiseColumn(levelHeightAccessor.getMinBuildHeight(), new BlockState[0]);
	}

	@Override
	public void addDebugScreenInfo(List<String> strings, BlockPos pos) {
		DecimalFormat decimalformat = new DecimalFormat("0.000");
		DensityFunction.SinglePointContext spc = new DensityFunction.SinglePointContext(pos.getX(), pos.getY(), pos.getZ());
		double d0 = this.router.ridges().compute(spc);
		strings.add("NoiseRouter T: " + decimalformat.format(this.router.temperature().compute(spc)) + " H: " + decimalformat.format(this.router.humidity().compute(spc)) + " C: " + decimalformat.format(this.router.continents().compute(spc)) + " E: " + decimalformat.format(this.router.erosion().compute(spc)) + " D: " + decimalformat.format(this.router.depth().compute(spc)) + " W: " + decimalformat.format(d0) + " PV: " + decimalformat.format(TerrainShaper.peaksAndValleys((float) d0)) + " AS: "
				+ decimalformat.format(this.router.initialDensityWithoutJaggedness().compute(spc)) + " N: " + decimalformat.format(this.router.finalDensity().compute(spc)));
	}

	@Override
	public void buildSurface(WorldGenRegion region, StructureFeatureManager structures, ChunkAccess chunk) {

	}

	@Override
	public void applyCarvers(WorldGenRegion worldGenRegion, long seed, BiomeManager biomeManager, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess, GenerationStep.Carving carving) {

	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, StructureFeatureManager structureManager, ChunkAccess chunk) {
		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	public int getGenDepth() {
		return this.settings.value().noiseSettings().height();
	}

	@Override
	public int getSeaLevel() {
		return this.settings.value().seaLevel();
	}

	@Override
	public int getMinY() {
		return this.settings.value().noiseSettings().minY();
	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion region) {}

	@Override
	public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunkAccess, StructureFeatureManager structureFeatureManager) {}
}
