/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 3, 2014, 5:09:30 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.util.EnumFacing;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.Botania;

public class TileManaBeacon extends TileMod {

	@Override
	public void update() {
		boolean redstone = false;
		for(EnumFacing dir : EnumFacing.VALUES) {
			int redstoneSide = worldObj.getRedstonePower(pos.offset(dir), dir);
			if(redstoneSide > 0)
				redstone = true;
		}

		if(!redstone) {
			int hex = worldObj.getBlockState(getPos()).getValue(BotaniaStateProps.COLOR).getMapColor().colorValue;
			int r = (hex & 0xFF0000) >> 16;
			int g = (hex & 0xFF00) >> 8;
			int b = (hex & 0xFF);

			Botania.proxy.setWispFXDistanceLimit(false);
			Botania.proxy.wispFX(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, r / 255F, g / 255F, b / 255F, (float) Math.random() * 5 + 1F, (float) (Math.random() - 0.5F), 10F * (float) Math.sqrt(256F / (256F - pos.getY())), (float) (Math.random() - 0.5F));

			for(int i = 0; i < 2; i++)
				Botania.proxy.wispFX(pos.getX() + 0.5, Math.min(256, pos.getY() + Botania.proxy.getClientRenderDistance() * 16), pos.getZ() + 0.5, r / 255F, g / 255F, b / 255F, (float) Math.random() * 15 + 8F, (float) (Math.random() - 0.5F) * 8F, 0F, (float) (Math.random() - 0.5F) * 8F);
			Botania.proxy.setWispFXDistanceLimit(true);
		}
	}
}
