/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 9, 2014, 8:51:55 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.common.block.ModBlocks;

import com.google.common.base.Function;

public class TileAlfPortal extends TileEntity {

	private static final int[][] LIVINGWOOD_POSITIONS = {
		{ -1, 0, 0}, { 1, 0, 0}, { -2, 1, 0}, { 2, 1, 0}, { -2, 3, 0}, { 2, 3, 0}, { -1, 4, 0}, { 1, 4, 0}
	};
	
	private static final int[][] GLIMMERING_LIVINGWOOD_POSITIONS = {
		{ -2, 2, 0 }, { 2, 2, 0 }, { 0, 4, 0 }
	};

	private static final int[][] PYLON_POSITIONS = {
		{ -3, 1, 3 }, { 3, 1, 3 }
	};
	
	private static final int[][] POOL_POSITIONS = {
		{ -3, 0, 3 }, { 3, 0, 3 }
	};
	
	private static final Function<int[], int[]> CONVERTER_X_Z = new Function<int[], int[]>() {
		@Override
		public int[] apply(int[] input) {
			return new int[] { input[2], input[1], input[0] };
		}
	};
	
	private static final Function<int[], int[]> CONVERTER_Z_SWAP = new Function<int[], int[]>() {
		@Override
		public int[] apply(int[] input) {
			return new int[] { input[0], input[1], -input[2] };
		}
	};
	
	@Override
	public void updateEntity() {
		int meta = getBlockMetadata();
		if(meta == 0)
			return;
		
		int newMeta = getValidMetadata();
		if(newMeta != meta)
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 1 | 2);

	}
	
	public boolean onWanded() {
		int meta = getBlockMetadata();
		if(meta == 0) {
			int newMeta = getValidMetadata();
			if(newMeta != 0) {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 1 | 2);
				return true;
			}
		}
		
		return false;
	}
	
	private int getValidMetadata() {
		if(checkConverter(null))
			return 1;

		if(checkConverter(CONVERTER_X_Z))
			return 2;
		
		return 0;
	}
	
	private boolean checkConverter(Function<int[], int[]> baseConverter) {
		return checkMultipleConverters(baseConverter) || checkMultipleConverters(CONVERTER_Z_SWAP, baseConverter);
	}
	
	private boolean checkMultipleConverters(Function<int[], int[]>... converters) {
		if(!check2DArray(LIVINGWOOD_POSITIONS, ModBlocks.livingwood, 0, converters))
			return false;
		if(!check2DArray(GLIMMERING_LIVINGWOOD_POSITIONS, ModBlocks.livingwood, 5, converters))
			return false;
		if(!check2DArray(PYLON_POSITIONS, ModBlocks.pylon, 0, converters))
			return false;
		if(!check2DArray(POOL_POSITIONS, ModBlocks.pool, -1, converters))
			return false;
		
		return true;
	}
	
	private boolean check2DArray(int[][] positions, Block block, int meta, Function<int[], int[]>... converters) {
		for(int[] pos : positions) {
			for(Function<int[], int[]> f : converters)
				if(f != null)
					pos = f.apply(pos);
			
			if(!checkPosition(pos, block, meta))
				return false;
		}
		
		return true;
	}
	
	private boolean checkPosition(int[] pos, Block block, int meta) {
		int x = xCoord + pos[0];
		int y = yCoord + pos[1];
		int z = zCoord + pos[2];
		Block blockat = worldObj.getBlock(x, y, z);
		if(blockat == block) {
			if(meta == -1)
				return true;
			
			int metaat = worldObj.getBlockMetadata(x, y, z);
			return meta == metaat;
		}
		
		return false;
	}
}
