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

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;

public class WorldTypeSkyblock extends WorldType {

	public WorldTypeSkyblock() {
		super("botania-skyblock");
	}

	public static boolean isWorldSkyblock(World world) {
		return world.getWorldInfo().getTerrainType() instanceof WorldTypeSkyblock;
	}

	@Override
	public boolean showWorldInfoNotice() {
		return true;
	}

	@Override
	public boolean hasVoidParticles(boolean flag) {
		return false;
	}

	@Override
	public int getMinimumSpawnHeight(World world) {
		return 86;
	}

	@Override
	public int getSpawnFuzz() {
		return 1;
	}

	@Override
	public float getCloudHeight() {
		return 260f;
	}
	
	@Override
	public IChunkProvider getChunkGenerator(World world, String generatorOptions) {
		return new ChunkProviderFlat(world, world.getSeed(), false, "2;1x0;");
	}
	
}
