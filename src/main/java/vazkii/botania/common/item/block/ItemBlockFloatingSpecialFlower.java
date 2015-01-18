/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 17, 2014, 5:39:08 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemBlockFloatingSpecialFlower extends ItemBlockSpecialFlower {

	public ItemBlockFloatingSpecialFlower(Block block1) {
		super(block1);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String flowerName = getUnlocalizedName(stack) + ".name";
		return String.format(StatCollector.translateToLocal("botaniamisc.floatingPrefix"), StatCollector.translateToLocal(flowerName));
	}

}
