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

public class TileStarfield extends TileMod implements ITickable {

	@Override
	public void update() {
		boolean state = world.getBlockState(getPos()).getValue(BotaniaStateProps.POWERED);
		if(!world.isRemote) {
			boolean newState = !world.isDaytime();
			if(newState != state) {
				world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BotaniaStateProps.POWERED, newState), 1 | 2);
				state = newState;
			}
		}

		if(state) {
			double radius = 512;
			int iter = 2;
			for(int i = 0; i < iter; i++) {
				double x = pos.getX() + 0.5 + (Math.random() - 0.5) * radius;
				double y = Math.min(256, pos.getY() + Botania.proxy.getClientRenderDistance() * 16);
				double z = pos.getZ() + 0.5 + (Math.random() - 0.5) * radius;

				float w = 0.6F;
				float c = 1F - w;

				float r = w + (float) Math.random() * c;
				float g = w + (float) Math.random() * c;
				float b = w + (float) Math.random() * c;

				float s = 20F + (float) Math.random() * 20F;
				int m = 50;

				Botania.proxy.sparkleFX(x, y, z, r, g, b, s, m);
			}
		}
	}

}
