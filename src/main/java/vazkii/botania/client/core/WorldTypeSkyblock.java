/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core;

import net.minecraft.client.gui.screen.BiomeGeneratorTypeScreens;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;

import vazkii.botania.common.world.SkyblockChunkGenerator;

public class WorldTypeSkyblock extends BiomeGeneratorTypeScreens {
	public static final BiomeGeneratorTypeScreens INSTANCE = new WorldTypeSkyblock();

	private WorldTypeSkyblock() {
		super("botania-skyblock");
	}

	/* todo 1.16 these have moved elsewhere
	
	moved to DimensionRenderInfo
	@Override
	public float getCloudHeight() {
		return 260f;
	}
	
	moved to ClientWorldInfo
	// In skyblock worlds, do not darken the sky until player hits y=0
	@Override
	public double getHorizon(World world) {
		return 0.0D;
	}
	
	@Override
	public double voidFadeMagnitude() {
		return 1.0D;
	}
	*/

	@Override
	protected ChunkGenerator func_230484_a_(long seed) {
		return new SkyblockChunkGenerator(new OverworldBiomeProvider(seed, false, false), seed, SkyblockChunkGenerator.dimSettingsPreset.func_236137_b_());
	}
}
