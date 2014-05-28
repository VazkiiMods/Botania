/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 10, 2014, 7:55:12 PM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.common.block.tile.TileMod;

public class TileManaDetector extends TileMod implements IManaCollisionGhost {

	@Override
	public void updateEntity() {
		if(!worldObj.isRemote) {
			int meta = getBlockMetadata();
			int expectedMeta = worldObj.getEntitiesWithinAABB(IManaBurst.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1)).size() != 0 ? 1 : 0;
			if(meta != expectedMeta)
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, expectedMeta, 1 | 2);
		}
	}

	@Override
	public boolean isGhost() {
		return true;
	}

}
