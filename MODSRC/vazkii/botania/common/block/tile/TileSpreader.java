/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 25, 2014, 9:40:57 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.tileentity.TileEntity;
import vazkii.botania.api.IWandRotateable;

public class TileSpreader extends TileEntity implements IWandRotateable {

	@Override
	public int getHorizontalRotation() {
		return 0;
	}

	@Override
	public int getVerticalRotation() {
		return 0;
	}

	@Override
	public void changeRotation(float horizontal, float vertical) {
	}

	@Override
	public void onClientTick() {
	}

}
