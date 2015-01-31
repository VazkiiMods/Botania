/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 26, 2015, 5:59:21 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibItemNames;

public class ItemOvergrowthSeed extends ItemMod {

	public ItemOvergrowthSeed() {
		setUnlocalizedName(LibItemNames.OVERGROWTH_SEED);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int s, float xs, float ys, float zs) {
		Block block = world.getBlock(x, y, z);
		ItemStack blockStack = new ItemStack(block);
		int[] ids = OreDictionary.getOreIDs(blockStack);
		for(int i : ids) {
			String name = OreDictionary.getOreName(i);
			if(name.equals("grass")) {
				world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block));
				world.setBlock(x, y, z, ModBlocks.enchantedSoil);
				stack.stackSize--;

				return true;
			}
		}
		return false;
	}

}
