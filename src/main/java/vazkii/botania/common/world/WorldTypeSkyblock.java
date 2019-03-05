/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 7, 2015, 2:06:01 AM (GMT)]
 */
package vazkii.botania.common.world;

import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.FlatGenSettings;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class WorldTypeSkyblock extends WorldType {

	public WorldTypeSkyblock() {
		super("botania-skyblock");
		
	}

	public static boolean isWorldSkyblock(World world) {
		return world.getWorldInfo().getGenerator() instanceof WorldTypeSkyblock;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean hasInfoNotice() {
		return true;
	}

	@Override
	public float getCloudHeight() {
		return 260f;
	}

	@Nonnull
	@Override
	public IChunkGenerator<?> createChunkGenerator(@Nonnull World world) {
		world.setSeaLevel(64);

		FlatGenSettings settings = new FlatGenSettings();
		settings.setBiome(Biomes.PLAINS);
		settings.getFlatLayers().add(new FlatLayerInfo(1, Blocks.AIR));
		settings.updateLayers();

		BiomeProvider biomeProvider = BiomeProviderType.FIXED.create(BiomeProviderType.FIXED.createSettings().setBiome(settings.getBiome()));

		return ChunkGeneratorType.FLAT.create(world, biomeProvider, settings);
	}

	/* In skyblock worlds, do not darken the sky until player hits y=0 */
	@Override
	public double getHorizon(World world)
	{
		return 0.0D;
	}

	@Override
	public double voidFadeMagnitude() {
		return 1.0D;
	}

}
