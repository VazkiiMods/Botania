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
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

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
				registryAccess.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).getHolderOrThrow(NoiseGeneratorSettings.OVERWORLD)
		);
	}

	protected final BlockState defaultBlock;
	private final Registry<NormalNoise.NoiseParameters> noises;
	protected final Holder<NoiseGeneratorSettings> settings;
	private final Aquifer.FluidPicker globalFluidPicker;

	private SkyblockChunkGenerator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
		super(structureSets, Optional.empty(), biomeSource);
		this.noises = noises;
		this.settings = settings;
		NoiseGeneratorSettings genSettings = this.settings.value();
		this.defaultBlock = genSettings.defaultBlock();
		Aquifer.FluidStatus lava = new Aquifer.FluidStatus(-54, Blocks.LAVA.defaultBlockState());
		int i = genSettings.seaLevel();
		Aquifer.FluidStatus defaultFluid = new Aquifer.FluidStatus(i, genSettings.defaultFluid());
		this.globalFluidPicker = (x, y, z) -> y < Math.min(-54, i) ? lava : defaultFluid;
	}

	@Override
	public CompletableFuture<ChunkAccess> createBiomes(Registry<Biome> biomes, Executor executor, RandomState randomState, Blender blender, StructureManager structureManager, ChunkAccess chunkAccess) {
		return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("init_biomes", () -> {
			this.doCreateBiomes(blender, randomState, structureManager, chunkAccess);
			return chunkAccess;
		}), Util.backgroundExecutor());
	}

	private void doCreateBiomes(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunkAccess) {
		NoiseChunk chunk = chunkAccess.getOrCreateNoiseChunk((access) -> this.createNoiseChunk(access, structureManager, blender, randomState));
		BiomeResolver biomeResolver = BelowZeroRetrogen.getBiomeResolver(blender.getBiomeResolver(this.biomeSource), chunkAccess);
		chunkAccess.fillBiomesFromNoise(biomeResolver, ((AccessorNoiseChunk) chunk).botania_cachedClimateSampler(randomState.router(), this.settings.value().spawnTarget()));
	}

	private NoiseChunk createNoiseChunk(ChunkAccess chunkAccess, StructureManager structureManager, Blender blender, RandomState randomState) {
		return NoiseChunk.forChunk(chunkAccess, randomState, Beardifier.forStructuresInChunk(structureManager, chunkAccess.getPos()), this.settings.value(), this.globalFluidPicker, blender);
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types heightmapTypes, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
		return levelHeightAccessor.getMinBuildHeight();
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
		return new NoiseColumn(levelHeightAccessor.getMinBuildHeight(), new BlockState[0]);
	}

	@Override
	public void addDebugScreenInfo(List<String> strings, RandomState randomState, BlockPos pos) {
		DecimalFormat format = new DecimalFormat("0.000");
		NoiseRouter router = randomState.router();
		DensityFunction.SinglePointContext ctx = new DensityFunction.SinglePointContext(pos.getX(), pos.getY(), pos.getZ());
		double $$6 = router.ridges().compute(ctx);
		String var10001 = format.format(router.temperature().compute(ctx));
		strings.add("NoiseRouter T: " + var10001 + " V: " + format.format(router.vegetation().compute(ctx)) + " C: " + format.format(router.continents().compute(ctx)) + " E: " + format.format(router.erosion().compute(ctx)) + " D: " + format.format(router.depth().compute(ctx)) + " W: " + format.format($$6) + " PV: " + format.format(NoiseRouterData.peaksAndValleys((float) $$6)) + " AS: " + format.format(router.initialDensityWithoutJaggedness().compute(ctx)) + " N: "
				+ format.format(router.finalDensity().compute(ctx)));
	}

	@Override
	public void buildSurface(WorldGenRegion region, StructureManager structures, RandomState randomState, ChunkAccess chunk) {

	}

	@Override
	public void applyCarvers(WorldGenRegion worldGenRegion, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess, GenerationStep.Carving carving) {

	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
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
}
