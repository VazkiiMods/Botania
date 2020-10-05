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
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.chunk.*;

import java.util.function.Supplier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class SkyblockChunkGenerator extends ChunkGenerator {
	// [VanillaCopy] overworld chunk generator codec
	public static final Codec<SkyblockChunkGenerator> CODEC = RecordCodecBuilder.create(
			(instance) -> instance.group(
					BiomeSource.CODEC.fieldOf("biome_source").forGetter((gen) -> gen.biomeSource),
					Codec.LONG.fieldOf("seed").stable().forGetter((gen) -> gen.seed),
					ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter((gen) -> gen.settings)
			).apply(instance, instance.stable(SkyblockChunkGenerator::new)));

	public static void init() {
		Registry.register(Registry.CHUNK_GENERATOR, prefix("skyblock"), SkyblockChunkGenerator.CODEC);
	}

	private final long seed;
	private final Supplier<ChunkGeneratorSettings> settings;

	public SkyblockChunkGenerator(BiomeSource provider, long seed, Supplier<ChunkGeneratorSettings> settings) {
		super(provider, provider, settings.get().getStructures(), seed);
		this.seed = seed;
		this.settings = settings;
	}

	public static boolean isWorldSkyblock(World world) {
		return world.getChunkManager() instanceof ServerChunkManager
				&& ((ServerChunkManager) world.getChunkManager()).getChunkGenerator() instanceof SkyblockChunkGenerator;
	}

	@Override
	protected Codec<? extends ChunkGenerator> getCodec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long newSeed) {
		return new SkyblockChunkGenerator(this.biomeSource.withSeed(newSeed), newSeed, settings);
	}

	@Override
	public void populateNoise(WorldAccess world, StructureAccessor structureManager, Chunk chunk) {

	}

	@Override
	public void buildSurface(ChunkRegion region, Chunk chunk) {

	}

	@Override
	public void carve(long seed, BiomeAccess biomes, Chunk chunk, GenerationStep.Carver stage) {}

	@Override
	public void generateFeatures(ChunkRegion region, StructureAccessor structureManager) {}

	@Override
	public int getHeight(int x, int z, Heightmap.Type heightmapType) {
		return 0;
	}

	@Override
	public BlockView getColumnSample(int p_230348_1_, int p_230348_2_) {
		return new VerticalBlockSample(new BlockState[0]);
	}
}
