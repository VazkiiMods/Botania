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
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.NoiseSettings;
import net.minecraft.world.gen.settings.ScalingSettings;
import net.minecraft.world.gen.settings.SlideSettings;
import net.minecraft.world.server.ServerChunkProvider;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibObfuscation;

import java.lang.invoke.MethodHandle;
import java.util.Optional;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class SkyblockChunkGenerator extends ChunkGenerator {
	// [VanillaCopy] overworld chunk generator codec
	public static final Codec<SkyblockChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(BiomeProvider.field_235202_a_.fieldOf("biome_source").forGetter((gen) -> gen.biomeProvider),
			Codec.LONG.fieldOf("seed").stable().forGetter((gen) -> gen.seed),
			DimensionSettings.field_236098_b_.fieldOf("settings").forGetter((gen) -> gen.settings))
			.apply(instance, instance.stable(SkyblockChunkGenerator::new)));
	public static DimensionSettings.Preset dimSettingsPreset;

	public static void init() {
		Registry.register(Registry.field_239690_aB_, prefix("skyblock"), SkyblockChunkGenerator.CODEC);
		MethodHandle createOverWorldPreset = LibObfuscation.getMethod(DimensionSettings.Preset.class, "func_236135_a_", DimensionStructuresSettings.class, boolean.class, DimensionSettings.Preset.class);
		dimSettingsPreset = new DimensionSettings.Preset(LibResources.PREFIX_MOD + "skyblock", preset -> {
			// [VanillaCopy] overworld preset creation
			try {
				return (DimensionSettings) createOverWorldPreset.invokeExact(new DimensionStructuresSettings(true), false, preset);
			} catch (Throwable throwable) {
				throw new RuntimeException(throwable);
			}
		});
	}

	private final long seed;
	private final DimensionSettings settings;

	public SkyblockChunkGenerator(BiomeProvider provider, long seed, DimensionSettings settings) {
		super(provider, provider, settings.func_236108_a_(), seed);
		this.seed = seed;
		this.settings = settings;
	}

	public static boolean isWorldSkyblock(World world) {
		return world.getChunkProvider() instanceof ServerChunkProvider
				&& ((ServerChunkProvider) world.getChunkProvider()).getChunkGenerator() instanceof SkyblockChunkGenerator;
	}

	@Override
	protected Codec<? extends ChunkGenerator> func_230347_a_() {
		return CODEC;
	}

	@Override
	public ChunkGenerator func_230349_a_(long newSeed) {
		return new SkyblockChunkGenerator(this.biomeProvider.func_230320_a_(newSeed), newSeed, settings);
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
