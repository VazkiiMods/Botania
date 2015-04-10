/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 10, 2015, 10:28:42 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.BlockLiquid;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.MathHelper;

public class SubTileBubbell extends SubTileFunctional {

	private static final int RANGE = 12;
	private static final String TAG_RANGE = "range";
	
	int range = 2;
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if(ticksExisted % 10 == 0 && range < RANGE)
			range++;
		
		// TODO Mana check
		for(int i = -range; i < range + 1; i++)
			for(int j = -range; j < range + 1; j++)
				for(int k = -range; k < range + 1; k++)
					if(MathHelper.pointDistanceSpace(i, j, k, 0, 0, 0) < range && supertile.getWorldObj().getBlock(supertile.xCoord + i, supertile.yCoord + j, supertile.zCoord + k) instanceof BlockLiquid) {
						System.out.println("doplace"); // TODO
						supertile.getWorldObj().setBlock(supertile.xCoord + i, supertile.yCoord + j, supertile.zCoord + k, ModBlocks.fakeAir);
					}
	}
	
	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);
		cmp.setInteger(TAG_RANGE, range);
	}
	
	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);
		range = cmp.getInteger(TAG_RANGE);
	}
	
	@Override
	public int getMaxMana() {
		return 10000;
	}
	
	@Override
	public int getColor() {
		return 0x0DCF89;
	}
	
}
