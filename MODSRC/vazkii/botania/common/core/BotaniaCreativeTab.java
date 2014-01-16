/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 5:20:53 PM (GMT)]
 */
package vazkii.botania.common.core;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

public final class BotaniaCreativeTab extends CreativeTabs {

	public static BotaniaCreativeTab INSTANCE = new BotaniaCreativeTab();
	List list;
	
	public BotaniaCreativeTab() {
		super(LibMisc.MOD_ID);
	}
	
	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(ModBlocks.flower);
	}
	
	@Override
	public void displayAllReleventItems(List list) {
		this.list = list;
		
		addBlock(ModBlocks.flower);
		
		addItem(ModItems.lexicon);
	}
	
	private void addItem(Item item) {
		item.getSubItems(item.itemID, this, list);
	}

	private void addBlock(Block block) {
		block.getSubBlocks(block.blockID, this, list);
	}

}
