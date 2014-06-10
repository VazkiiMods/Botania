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
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;

import com.google.common.base.Function;

public class TileAlfPortal extends TileMod {

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
	
	private static final int[][] AIR_POSITIONS = {
		{ -1, 1, 0 }, { 0, 1, 0 }, { 1, 1, 0 },	{ -1, 2, 0 }, { 0, 2, 0 }, { 1, 2, 0 },	{ -1, 3, 0 }, { 0, 3, 0 }, { 1, 3, 0 }
	};
	
	private static final String TAG_TICKS_OPEN = "ticksOpen";
	
	public int ticksOpen = 0;
	
	private static final Function<int[], int[]> CONVERTER_X_Z = new Function<int[], int[]>() {
		@Override
		public int[] apply(int[] input) {
			return new int[] { input[2], input[1], input[0] };
		}
	};
	
	private static final Function<float[], float[]> CONVERTER_X_Z_FLOAT = new Function<float[], float[]>() {
		@Override
		public float[] apply(float[] input) {
			return new float[] { input[2], input[1], input[0] };
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
		if(meta == 0) {
			ticksOpen = 0;
			return;
		}
		
		ticksOpen++;
		if(ticksOpen > 60) {
			int i = worldObj.rand.nextInt(AIR_POSITIONS.length);
			float[] pos = new float[] {
					AIR_POSITIONS[i][0] + 0.5F, AIR_POSITIONS[i][1] + 0.5F, AIR_POSITIONS[i][2] + 0.5F
			};
			if(meta == 2)
				pos = CONVERTER_X_Z_FLOAT.apply(pos);
			
			float motionMul = 0.2F;
			Botania.proxy.wispFX(getWorldObj(), xCoord + pos[0], yCoord + pos[1], zCoord + pos[2], (float) (Math.random() * 0.25F), (float) (Math.random() * 0.5F + 0.5F), (float) (Math.random() * 0.25F), (float) (Math.random() * 0.15F + 0.1F), (float) (Math.random() - 0.5F) * motionMul, (float) (Math.random() - 0.5F) * motionMul, (float) (Math.random() - 0.5F) * motionMul);
			
		}
		
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
		} else {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 1 | 2);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_TICKS_OPEN, ticksOpen);
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		ticksOpen = cmp.getInteger(TAG_TICKS_OPEN);
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
		if(!check2DArray(AIR_POSITIONS, Blocks.air, -1, converters))
			return false;
		if(!check2DArray(LIVINGWOOD_POSITIONS, ModBlocks.livingwood, 0, converters))
			return false;
		if(!check2DArray(GLIMMERING_LIVINGWOOD_POSITIONS, ModBlocks.livingwood, 5, converters))
			return false;
		if(!check2DArray(PYLON_POSITIONS, ModBlocks.pylon, 1, converters))
			return false;
		if(!check2DArray(POOL_POSITIONS, ModBlocks.pool, -1, converters))
			return false;
		
		lightPylons(converters);
		return true;
	}
	
	private void lightPylons(Function<int[], int[]>... converters) {
		if(ticksOpen < 50)
			return;
		
		for(int[] pos : PYLON_POSITIONS) {
			for(Function<int[], int[]> f : converters)
				if(f != null)
					pos = f.apply(pos);
			
			TileEntity tile = worldObj.getTileEntity(xCoord + pos[0], yCoord + pos[1], zCoord + pos[2]);
			if(tile instanceof TilePylon) {
				TilePylon pylon = (TilePylon) tile;
				pylon.activated = true;
				pylon.centerX = xCoord;
				pylon.centerY = yCoord;
				pylon.centerZ = zCoord;
			}
		}

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
		if(block == Blocks.air ? blockat.isAir(worldObj, x, y, z) : blockat == block) {
			if(meta == -1)
				return true;
			
			int metaat = worldObj.getBlockMetadata(x, y, z);
			return meta == metaat;
		}
		
		return false;
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}
