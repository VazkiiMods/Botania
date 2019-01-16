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

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class WorldTypeSkyblock extends WorldType {

	public WorldTypeSkyblock() {
		super("botania-skyblock");
		
	}

	public static boolean isWorldSkyblock(World world) {
		return world.getWorldInfo().getTerrainType() instanceof WorldTypeSkyblock;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasInfoNotice() {
		return true;
	}

	@Override
	public int getMinimumSpawnHeight(@Nonnull World world) {
		return 86;
	}

	@Override
	public int getSpawnFuzz(@Nonnull WorldServer world, MinecraftServer server) {
		return 0;
	}

	@Override
	public float getCloudHeight() {
		return 260f;
	}

	@Nonnull
	@Override
	public IChunkGenerator getChunkGenerator(@Nonnull World world, String generatorOptions) {
		ChunkGeneratorFlat flat = new ChunkGeneratorFlat(world, world.getSeed(), false, "3;minecraft:air;");
		world.setSeaLevel(64);
		return flat;
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
