/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.OverworldBiomeProviderSettings;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.OverworldGenSettings;
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
	public ChunkGenerator<?> createChunkGenerator(@Nonnull World world) {
		if (world.dimension.getType() == DimensionType.OVERWORLD) {
			OverworldGenSettings genSettings = SkyblockChunkGenerator.TYPE.createSettings();
			OverworldBiomeProviderSettings biomeSettings = BiomeProviderType.VANILLA_LAYERED.createSettings(world.getWorldInfo()).setGeneratorSettings(genSettings);
			return SkyblockChunkGenerator.TYPE.create(world, BiomeProviderType.VANILLA_LAYERED.create(biomeSettings), genSettings);
		}
		return super.createChunkGenerator(world);
	}

	/* In skyblock worlds, do not darken the sky until player hits y=0 */
	@Override
	public double getHorizon(World world) {
		return 0.0D;
	}

	@Override
	public double voidFadeMagnitude() {
		return 1.0D;
	}

}
