/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 15, 2014, 4:57:52 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import vazkii.botania.common.block.ModBlocks;

public class TileEnchanter extends TileMod {

	public int stage = -1;
	public int stageTicks = 0;

	private static final int[][] OBSIDIAN_LOCATIONS = new int[][] {
		{ 0, -1, 0 },
		{ 0, -1, 1 }, { 0, -1, -1 }, { 1, -1, 0 }, { -1, -1, 0 },
		{ 0, -1, 2 }, { -1, -1, 2 }, { 1, -1, 2 },
		{ 0, -1, -2 }, { -1, -1, -2 }, { 1, -1, -2 },
		{ 2, -1, 0 }, { 2, -1, 1 }, { 2, -1, -1 },
		{ -2, -1, 0 }, { -2, -1, 1 }, { -2, -1, -1 }
	};

	private static final int[][][] PYLON_LOCATIONS = new int[][][] { 
		{ { -5, 1, 0 }, { 5, 1, 0 }, { -4, 1, 3 }, { 4, 1, 3 }, { -4, 1, -3 }, { 4, 1, -3 } }, 
		{ { 0, 1, -5 }, { 0, 1, 5 }, { 3, 1, -4 }, { 3, 1, 4 }, { -3, 1, -4 }, { -3, 1, 4 } }
	};

	private static final int[][] FLOWER_LOCATIONS = new int[][] {
		{ -1, 0, -1 }, { 1, 0, -1 }, { -1, 0, 1 }, { 1, 0, 1 } 
	};

	@Override
	public void updateEntity() {
		if(!canEnchanterExist(worldObj, xCoord, yCoord, zCoord, getBlockMetadata()))
			worldObj.setBlock(xCoord, yCoord, zCoord, Block.blockLapis.blockID, 0, 1 | 2);

		switch(stage) {
		case 1 : {

			break;
		}
		case 2 : {

			break;
		}
		case 3 : {
			
			break;
		}
		case 4 : {
			break;
		}
		}

		if(stage != 0)
			stageTicks++;
	}

	public void advanceStage() {
		stage++;
		if(stage == 5)
			stage = 0;

		stageTicks = 0;
	}

	public static boolean canEnchanterExist(World world, int x, int y, int z, int meta) {
		for(int[] obsidian : OBSIDIAN_LOCATIONS)
			if(world.getBlockId(obsidian[0] + x, obsidian[1] + y, obsidian[2] + z) != Block.obsidian.blockID)
				return false;

		for(int[] pylon : PYLON_LOCATIONS[meta])
			if(world.getBlockId(pylon[0] + x, pylon[1] + y, pylon[2] + z) != ModBlocks.pylon.blockID || 
			world.getBlockId(pylon[0] + x, pylon[1] + y - 1, pylon[2] + z) != ModBlocks.flower.blockID)
				return false;

		for(int[] flower : FLOWER_LOCATIONS)
			if(world.getBlockId(flower[0] + x, flower[1] + y, flower[2] + z) != ModBlocks.flower.blockID)
				return false;

		return true;
	}

}
