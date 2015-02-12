/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 17, 2014, 5:41:58 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.decor.IFloatingFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class TileFloatingSpecialFlower extends TileSpecialFlower implements IFloatingFlower {

	@Override
	public boolean isOnSpecialSoil() {
		return false;
	}
	
	@Override
	public ItemStack getDisplayStack() {
		return ItemBlockSpecialFlower.ofType(subTileName);
	}

}
