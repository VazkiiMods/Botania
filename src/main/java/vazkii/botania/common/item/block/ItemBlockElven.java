/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

import vazkii.botania.api.recipe.IElvenItem;

public class ItemBlockElven extends BlockItem implements IElvenItem {

	public ItemBlockElven(Block block, Settings props) {
		super(block, props);
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return true;
	}

}
