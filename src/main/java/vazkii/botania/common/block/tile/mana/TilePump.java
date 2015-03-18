/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 18, 2015, 3:16:57 PM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.common.block.tile.TileMod;

public class TilePump extends TileMod {
	
	private static final String TAG_ACTIVE = "active";

	public float innerRingPos;
	public boolean active = false;
	public boolean hasCart = false;
	public float moving = 0F;
	
	@Override
	public void updateEntity() {
		float max = 8F;
		float min = 0F;
		
		float incr = max / 10F;
		
		if(innerRingPos < max && active && moving >= 0F) {
			innerRingPos += incr;
			moving = incr;
			if(innerRingPos >= max) {
				innerRingPos = Math.min(max, innerRingPos);
				moving = 0F;
			}
		} else if(innerRingPos > min) {
			innerRingPos -= incr * 2;
			moving = -incr * 2;
			if(innerRingPos <= min) {
				innerRingPos = Math.max(min, innerRingPos);
				moving = 0F;
			}
		}
		
		if(!hasCart && active)
			setActive(false);
		hasCart = false;
		
		super.updateEntity();
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setBoolean(TAG_ACTIVE, active);
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		active = cmp.getBoolean(TAG_ACTIVE);
	}
	
	public void setActive(boolean active) {
		if(!worldObj.isRemote) {
			this.active = active;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);			
		}
	}
	
}
