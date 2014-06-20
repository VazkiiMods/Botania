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
import vazkii.botania.common.core.handler.ConfigHandler;
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
		addBlock(ModBlocks.storage);
		addItem(ModItems.manaCookie);
		addItem(ModItems.rune);

		addItem(ModItems.dirtRod);
		addItem(ModItems.terraformRod);
		addItem(ModItems.grassHorn);
		addItem(ModItems.waterRod);
		addItem(ModItems.openBucket);
		addItem(ModItems.spawnerMover);
		addItem(ModItems.rainbowRod);
		addBlock(ModBlocks.forestDrum);
		addBlock(ModBlocks.forestEye);

		addBlock(ModBlocks.livingrock);
		addBlock(ModBlocks.livingwood);
		addBlock(ModBlocks.openCrate);
		addBlock(ModBlocks.platform);
		addBlock(ModBlocks.alfPortal);
		addBlock(ModBlocks.altar);
		addBlock(ModBlocks.runeAltar);
		addBlock(ModBlocks.pylon);
		addBlock(ModBlocks.pistonRelay);
		addBlock(ModBlocks.shinyFlower);
		addBlock(ModBlocks.unstableBlock);
		addBlock(ModBlocks.manaBeacon);
		addItem(ModItems.signalFlare);

		addBlock(ModBlocks.dreamwood);
		
		addBlock(ModBlocks.livingwoodStairs);
		addBlock(ModBlocks.livingwoodSlab);
		addBlock(ModBlocks.livingwoodPlankStairs);
		addBlock(ModBlocks.livingwoodPlankSlab);
		addBlock(ModBlocks.livingrockStairs);
		addBlock(ModBlocks.livingrockSlab);
		addBlock(ModBlocks.livingrockBrickStairs);
		addBlock(ModBlocks.livingrockBrickSlab);
		addBlock(ModBlocks.dreamwoodStairs);
		addBlock(ModBlocks.dreamwoodSlab);
		addBlock(ModBlocks.dreamwoodPlankStairs);
		addBlock(ModBlocks.dreamwoodPlankSlab);
		
		addItem(ModItems.quartz);
		if(ConfigHandler.darkQuartzEnabled) {
			addBlock(ModBlocks.darkQuartz);
			addBlock(ModBlocks.darkQuartzSlab);
			addBlock(ModBlocks.darkQuartzStairs);
		}

		addBlock(ModBlocks.manaQuartz);
		addBlock(ModBlocks.manaQuartzSlab);
		addBlock(ModBlocks.manaQuartzStairs);
		addBlock(ModBlocks.blazeQuartz);
		addBlock(ModBlocks.blazeQuartzSlab);
		addBlock(ModBlocks.blazeQuartzStairs);
		addBlock(ModBlocks.lavenderQuartz);
		addBlock(ModBlocks.lavenderQuartzSlab);
		addBlock(ModBlocks.lavenderQuartzStairs);
		addBlock(ModBlocks.redQuartz);
		addBlock(ModBlocks.redQuartzSlab);
		addBlock(ModBlocks.redQuartzStairs);

		addItem(ModItems.manasteelPick);
		addItem(ModItems.manasteelShovel);
		addItem(ModItems.manasteelAxe);
		addItem(ModItems.manasteelShears);
		addItem(ModItems.manasteelSword);
		addItem(ModItems.enderDagger);
		addItem(ModItems.manasteelHelm);
		addItem(ModItems.manasteelChest);
		addItem(ModItems.manasteelLegs);
		addItem(ModItems.manasteelBoots);
		addItem(ModItems.terraSword);
		addItem(ModItems.terraPick);
		addItem(ModItems.terrasteelHelm);
		addItem(ModItems.terrasteelChest);
		addItem(ModItems.terrasteelLegs);
		addItem(ModItems.terrasteelBoots);
		addItem(ModItems.elementiumPick);
		addItem(ModItems.elementiumShovel);
		addItem(ModItems.elementiumAxe);
		addItem(ModItems.elementiumShears);
		addItem(ModItems.elementiumSword);
		addItem(ModItems.elementiumHelm);
		addItem(ModItems.elementiumChest);
		addItem(ModItems.elementiumLegs);
		addItem(ModItems.elementiumBoots);
		
		addItem(ModItems.tinyPlanet);
		addBlock(ModBlocks.tinyPlanet);
		addItem(ModItems.manaRing);
		addItem(ModItems.auraRing);
		addItem(ModItems.manaRingGreater);
		addItem(ModItems.auraRingGreater);
		addItem(ModItems.waterRing);
		addItem(ModItems.miningRing);
		addItem(ModItems.magnetRing);
		addItem(ModItems.pixieRing);
		addItem(ModItems.travelBelt);
		addItem(ModItems.superTravelBelt);
		addItem(ModItems.knockbackBelt);
		addItem(ModItems.icePendant);
		addItem(ModItems.lavaPendant);
		addItem(ModItems.goldLaurel);
		addItem(ModItems.divaCharm);
		addItem(ModItems.flightTiara);

		addItem(ModItems.manaTablet);
		addItem(ModItems.manaMirror);
		addBlock(ModBlocks.pool);
		addBlock(ModBlocks.alchemyCatalyst);
		addBlock(ModBlocks.conjurationCatalyst);
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
