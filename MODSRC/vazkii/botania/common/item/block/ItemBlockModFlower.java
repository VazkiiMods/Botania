/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 16, 2014, 5:54:06 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemBlockModFlower extends ItemBlockWithMetadata {

	public ItemBlockModFlower(int par1, Block par2Block) {
		super(par1, par2Block);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + par1ItemStack.getItemDamage();
	}

}
