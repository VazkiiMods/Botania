/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.client.gui.screen.BiomeGeneratorTypeScreens;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.server.ServerChunkProvider;

public class WorldTypeSkyblock extends BiomeGeneratorTypeScreens {

	public WorldTypeSkyblock() {
		super("botania-skyblock");
	}

	public static boolean isWorldSkyblock(World world) {
		return world.getChunkProvider() instanceof ServerChunkProvider
						&& ((ServerChunkProvider) world.getChunkProvider()).getChunkGenerator() instanceof SkyblockChunkGenerator;
	}

	/* todo 1.16 impossible now?

	@Override
	public float getCloudHeight() {
		return 260f;
	}

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
		return new SkyblockChunkGenerator(new OverworldBiomeProvider(seed, false, false), DimensionSettings.Preset.field_236122_b_.func_236137_b_().func_236108_a_());
	}
}
