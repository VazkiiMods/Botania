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

import net.minecraft.util.ITickable;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;

public class TileStarfield extends TileMod {

	@Override
	public void updateEntity() {
		boolean state = worldObj.getBlockState(getPos()).getValue(BotaniaStateProps.POWERED);
		if(!worldObj.isRemote) {
			boolean newState = !worldObj.isDaytime();
			if(newState != state) {
				worldObj.setBlockState(getPos(), worldObj.getBlockState(getPos()).withProperty(BotaniaStateProps.POWERED, newState), 1 | 2);
				state = newState;
			}
		}

		if(state) {
			double radius = 512;
			int iter = 2;
			for(int i = 0; i < iter; i++) {
				double x = pos.getX() + 0.5 + (Math.random() - 0.5) * radius;
				double y = pos.getY() + 256;
				double z = pos.getZ() + 0.5 + (Math.random() - 0.5) * radius;

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
