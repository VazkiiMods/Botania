/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 7, 2014, 6:42:33 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import vazkii.botania.common.Botania;

public class TileStarfield extends TileMod {

	@Override
	public void updateEntity() {
		int meta = getBlockMetadata();
		if(!worldObj.isRemote) {
			int newMeta = worldObj.isDaytime() ? 0 : 1;
			if(newMeta != meta) {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 1 | 2);
				meta = newMeta;
			}
		}

		if(meta == 1) {
			double radius = 512;
			int iter = 2;
			for(int i = 0; i < iter; i++) {
				double x = xCoord + 0.5 + (Math.random() - 0.5) * radius;
				double y = yCoord + 256;
				double z = zCoord + 0.5 + (Math.random() - 0.5) * radius;

				float w = 0.6F;
				float c = 1F - w;

				float r = w + (float) Math.random() * c;
				float g = w + (float) Math.random() * c;
				float b = w + (float) Math.random() * c;

				float s = 20F + (float) Math.random() * 20F;
				int m = 50;

				Botania.proxy.sparkleFX(worldObj, x, y, z, r, g, b, s, m);
			}
		}
	}

}
