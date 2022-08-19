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
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.TileMod;

public class TilePump extends TileMod {

	private static final String TAG_ACTIVE = "active";

	public float innerRingPos;
	public boolean active = false;
	public boolean hasCart = false;
	public boolean hasCartOnTop = false;
	public float moving = 0F;

	public int comparator;
	public boolean hasRedstone = false;
	int lastComparator = 0;

	@Override
	public void updateEntity() {
		hasRedstone = false;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			int redstoneSide = worldObj.getIndirectPowerLevelTo(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, dir.ordinal());
			if(redstoneSide > 0) {
				hasRedstone = true;
				break;
			}
		}

		float max = 8F;
		float min = 0F;

		float incr = max / 10F;

		if(innerRingPos < max && active && moving >= 0F) {
			innerRingPos += incr;
			moving = incr;
			if(innerRingPos >= max) {
				innerRingPos = Math.min(max, innerRingPos);
				moving = 0F;
				for(int x = 0; x < 2; x++)
					worldObj.spawnParticle("explode", xCoord + Math.random(), yCoord + Math.random(), zCoord + Math.random(), 0, 0, 0);
			}
		} else if(innerRingPos > min) {
			innerRingPos -= incr * 2;
			moving = -incr * 2;
			if(innerRingPos <= min) {
				innerRingPos = Math.max(min, innerRingPos);
				moving = 0F;
			}
		}

		if(!hasCartOnTop)
			comparator = 0;
		if(!hasCart && active)
			setActive(false);
		if(active && hasRedstone)
			setActive(false);

		hasCart = false;
		hasCartOnTop = false;

		if(comparator != lastComparator)
			worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
		lastComparator = comparator;

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
			boolean diff = this.active != active;
			this.active = active;
			if(diff)
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord);
		}
	}

}
