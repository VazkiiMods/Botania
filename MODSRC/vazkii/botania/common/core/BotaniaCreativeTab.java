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
	public Item getTabIconItem() {
		return getIconItemStack().getItem();
	}

	@Override
	public void displayAllReleventItems(List list) {
		this.list = list;

		addItem(ModItems.lexicon);

		addBlock(ModBlocks.flower);
		addBlock(ModBlocks.specialFlower);
		addItem(ModItems.petal);
		addItem(ModItems.manaPetal);
		addItem(ModItems.pestleAndMortar);
		addItem(ModItems.dye);
		addItem(ModItems.fertilizer);
		addItem(ModItems.grassSeeds);
		addItem(ModItems.twigWand);
		addItem(ModItems.manaResource);
		addItem(ModItems.manaCookie);
		addItem(ModItems.rune);

		addItem(ModItems.dirtRod);
		addItem(ModItems.terraformRod);
		addItem(ModItems.timeRod);
		
		addBlock(ModBlocks.livingrock);
		addBlock(ModBlocks.livingwood);
		addBlock(ModBlocks.altar);
		addBlock(ModBlocks.runeAltar);
		addBlock(ModBlocks.pylon);
		addBlock(ModBlocks.pistonRelay);
		addBlock(ModBlocks.unstableBlock);
		addBlock(ModBlocks.manaBeacon);
		addItem(ModItems.signalFlare);
		
		addItem(ModItems.manaTablet);
		addBlock(ModBlocks.pool);
		addBlock(ModBlocks.distributor);
		addBlock(ModBlocks.manaVoid);
		addBlock(ModBlocks.manaDetector);
		addBlock(ModBlocks.spreader);
		addBlock(ModBlocks.turntable);
		addItem(ModItems.manaGun);
		addItem(ModItems.lens);
	}

	private void addItem(Item item) {
		item.getSubItems(item, this, list);
	}

	private void addBlock(Block block) {
		ItemStack stack = new ItemStack(block);
		block.getSubBlocks(stack.getItem(), this, list);
	}

}
