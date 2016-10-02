/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 9, 2014, 5:02:06 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.IElvenItem;

public class ItemBlockElven extends ItemBlockMod implements IElvenItem {

	public ItemBlockElven(Block block) {
		super(block);
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return ((IElvenItem) block).isElvenItem(stack);
	}

}
