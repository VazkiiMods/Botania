/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 6, 2015, 4:07:16 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;

public class TileCell extends TileMod {
	
	private static final String TAG_GENERATION = "generation";
	
	private int generation;
	
	@Override
	public boolean canUpdate() {
		return false;
	}
	
	public void setGeneration(int gen) {
		generation = gen;
	}
	
	public int getGeneration() {
		return generation;
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_GENERATION, generation);
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		generation = cmp.getInteger(TAG_GENERATION);
	}
	
}
