/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 16, 2014, 7:00:36 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vazkii.botania.client.lib.LibResources;

public class ItemBlockMod extends ItemBlock {

	public ItemBlockMod(Block block) {
		super(block);
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		return getUnlocalizedNameInefficiently_(par1ItemStack).replaceAll("tile.", "tile." + LibResources.PREFIX_MOD);
	}

	public String getUnlocalizedNameInefficiently_(ItemStack stack) {
		return super.getUnlocalizedNameInefficiently(stack);
	}

}
