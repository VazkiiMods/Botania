/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 15, 2015, 12:42:29 AM (GMT)]
 */
package vazkii.botania.common.block.tile.corporea;

import java.util.List;

import vazkii.botania.common.core.helper.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class TileCorporeaIndex extends TileCorporeaBase {

	public static final double RADIUS = 2.5;
	
	public int ticks = 0;
	public int ticksWithCloseby = 0;
	public float closeby = 0F;
	public boolean hasCloseby;
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		double x = xCoord + 0.5;
		double y = yCoord + 0.5;
		double z = zCoord + 0.5;
		
		List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(x - RADIUS, y - RADIUS, z - RADIUS, x + RADIUS, y + RADIUS, z + RADIUS));
		hasCloseby = false;
		for(EntityPlayer player : players)
			if(MathHelper.pointDistanceSpace(x, y, z, player.posX, player.posY, player.posZ) < RADIUS) {
				hasCloseby = true;
				break;
			}
		
		float step = 0.2F;
		ticks++;
		if(hasCloseby) {
			ticksWithCloseby++;
			if(closeby < 1F)
				closeby += step;
		} else if(closeby > 0F)
			closeby -= step;
	}
	
	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public String getInventoryName() {
		return null;
	}

}
