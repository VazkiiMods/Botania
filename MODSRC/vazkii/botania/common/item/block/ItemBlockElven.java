/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Sep 9, 2014, 5:02:06 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import vazkii.botania.api.recipe.IElvenItem;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockElven extends ItemBlockMod implements IElvenItem {

	public ItemBlockElven(Block block) {
		super(block);
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return ((IElvenItem) field_150939_a).isElvenItem(stack);
	}

	
	
}
