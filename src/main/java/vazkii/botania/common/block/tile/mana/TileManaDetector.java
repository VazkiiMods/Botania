/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 10, 2014, 7:55:12 PM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileMod;

public class TileManaDetector extends TileMod implements IManaCollisionGhost {

	@Override
	public void updateEntity() {
		if(!worldObj.isRemote) {
			int meta = getBlockMetadata();
			int expectedMeta = worldObj.getEntitiesWithinAABB(IManaBurst.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1)).size() != 0 ? 1 : 0;
			if(meta != expectedMeta)
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, expectedMeta, 1 | 2);

			if(expectedMeta == 1)
				for(int i = 0; i < 4; i++)
					Botania.proxy.sparkleFX(getWorldObj(), xCoord + Math.random(), yCoord + Math.random(), zCoord + Math.random(), 1F, 0.2F, 0.2F, 0.7F + 0.5F * (float) Math.random(), 5);
		}
	}

	@Override
	public boolean isGhost() {
		return true;
	}

}
