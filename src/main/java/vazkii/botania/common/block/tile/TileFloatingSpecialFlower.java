/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 17, 2014, 5:41:58 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.decor.IFloatingFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class TileFloatingSpecialFlower extends TileSpecialFlower implements IFloatingFlower {

	@Override
	public ItemStack getDisplayStack() {
		return ItemBlockSpecialFlower.ofType(subTileName);
	}
	
	
}
