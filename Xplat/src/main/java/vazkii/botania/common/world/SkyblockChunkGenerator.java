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

import net.minecraft.core.*;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import vazkii.botania.mixin.NoiseBasedChunkGeneratorAccessor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class SkyblockChunkGenerator extends NoiseBasedChunkGenerator {
	// [VanillaCopy] NoiseBasedChunkGenerator's codec, but calls our constructor
	public static final Codec<SkyblockChunkGenerator> CODEC = RecordCodecBuilder.create(
			instance -> commonCodec(instance)
					.and(instance.group(
							RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter(gen -> ((NoiseBasedChunkGeneratorAccessor) gen).botania_getNoises()),
							BiomeSource.CODEC.fieldOf("biome_source").forGetter((gen) -> gen.biomeSource),
							NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(gen -> gen.settings)))
					.apply(instance, instance.stable(SkyblockChunkGenerator::new)));

	public static void submitRegistration(BiConsumer<Codec<? extends ChunkGenerator>, ResourceLocation> consumer) {
		consumer.accept(SkyblockChunkGenerator.CODEC, prefix("skyblock"));
	}

	public static boolean isWorldSkyblock(Level world) {
		return world.getChunkSource() instanceof ServerChunkCache chunkCache && chunkCache.getGenerator() instanceof SkyblockChunkGenerator;
	}

	private SkyblockChunkGenerator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
		super(structureSets, noises, biomeSource, settings);
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
	public void buildSurface(ChunkAccess chunkAccess, WorldGenerationContext context,
			RandomState randomState, StructureManager structureManager, BiomeManager biomeManager,
			Registry<Biome> biomes, Blender blender) {}

	@Override
	public void applyCarvers(WorldGenRegion worldGenRegion, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess, GenerationStep.Carving carving) {

	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion region) {}

	@Override
	public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunkAccess, StructureManager structureManager) {}
}
