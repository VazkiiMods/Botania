/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class SkyblockChunkGenerator extends NoiseBasedChunkGenerator {
	// [VanillaCopy] NoiseBasedChunkGenerator's codec, but calls our constructor
	public static final MapCodec<SkyblockChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
			(instance) -> instance.group(
					BiomeSource.CODEC.fieldOf("biome_source").forGetter((gen) -> gen.biomeSource),
					NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(NoiseBasedChunkGenerator::generatorSettings))
					.apply(instance, instance.stable(SkyblockChunkGenerator::new)));

	public static void submitRegistration(BiConsumer<MapCodec<? extends ChunkGenerator>, ResourceLocation> consumer) {
		consumer.accept(SkyblockChunkGenerator.CODEC, botaniaRL("skyblock"));
	}

	public static boolean isWorldSkyblock(Level world) {
		return world.getChunkSource() instanceof ServerChunkCache chunkCache && chunkCache.getGenerator() instanceof SkyblockChunkGenerator;
	}

	public SkyblockChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
		super(biomeSource, settings);
	}

	@Override
	protected MapCodec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public void buildSurface(ChunkAccess chunkAccess, WorldGenerationContext context,
			RandomState randomState, StructureManager structureManager,
			BiomeManager biomeManager, Registry<Biome> biomes, Blender blender) {}

	@Override
	public void applyCarvers(WorldGenRegion worldGenRegion, long seed, RandomState randomState,
			BiomeManager biomeManager, StructureManager structureManager,
			ChunkAccess chunkAccess, GenerationStep.Carving carving) {}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender,
			RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion region) {}

	@Override
	public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunkAccess,
			StructureManager structureManager) {}

	/**
	 * Calculates the height at a particular location as it would be in a world with the same seed that isn't empty.
	 * (Based on code from CarpetSkyAdditions.)
	 */
	public int getBaseHeightInEquivalentNoiseWorld(int x, int z, Heightmap.Types heightmap, WorldGenLevel level) {
		RandomState randomState = RandomState.create(
				generatorSettings().value(),
				level.registryAccess().registryOrThrow(Registries.NOISE).asLookup(),
				level.getSeed());
		return super.getBaseHeight(x, z, heightmap, level, randomState);
	}
}
