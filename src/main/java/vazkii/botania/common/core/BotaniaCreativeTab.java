/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 14, 2014, 5:20:53 PM (GMT)]
 */
package vazkii.botania.common.core;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

public final class BotaniaCreativeTab extends CreativeTabs {

	public static BotaniaCreativeTab INSTANCE = new BotaniaCreativeTab();
	List list;

	public BotaniaCreativeTab() {
		super(LibMisc.MOD_ID);
		setNoTitle();
		setBackgroundImageName(LibResources.GUI_CREATIVE);
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(ModItems.lexicon);
	}

	@Override
	public Item getTabIconItem() {
		return getIconItemStack().getItem();
	}

	@Override
	public boolean hasSearchBar() {
		return true;
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
		addItem(ModItems.blackLotus);
		addItem(ModItems.twigWand);
		addItem(ModItems.manaResource);
		addBlock(ModBlocks.storage);
		addItem(ModItems.manaCookie);
		addItem(ModItems.rune);

		addItem(ModItems.dirtRod);
		addItem(ModItems.skyDirtRod);
		addItem(ModItems.cobbleRod);
		addItem(ModItems.terraformRod);
		addItem(ModItems.laputaShard);
		addItem(ModItems.grassHorn);
		addItem(ModItems.waterRod);
		addItem(ModItems.openBucket);
		addItem(ModItems.rainbowRod);
		addItem(ModItems.tornadoRod);
		addItem(ModItems.fireRod);
		addItem(ModItems.smeltRod);
		addItem(ModItems.diviningRod);
		addItem(ModItems.gravityRod);
		addItem(ModItems.missileRod);
		addItem(ModItems.virus);
		addItem(ModItems.slingshot);
		addItem(ModItems.vineBall);
		addItem(ModItems.regenIvy);
		addItem(ModItems.worldSeed);
		addItem(ModItems.overgrowthSeed);
		if(Botania.thaumcraftLoaded)
			addItem(ModItems.manaInkwell);
		addBlock(ModBlocks.forestDrum);
		addBlock(ModBlocks.forestEye);
		addBlock(ModBlocks.enderEye);
		addItem(ModItems.enderHand);
		addItem(ModItems.spellCloth);
		addItem(ModItems.craftingHalo);
		addItem(ModItems.spawnerMover);
		addBlock(ModBlocks.spawnerClaw);

		addBlock(ModBlocks.livingrock);
		addBlock(ModBlocks.livingwood);
		addBlock(ModBlocks.openCrate);
		addItem(ModItems.craftPattern);
		addBlock(ModBlocks.platform);
		addBlock(ModBlocks.alfPortal);
		addBlock(ModBlocks.altar);
		addBlock(ModBlocks.runeAltar);
		addBlock(ModBlocks.terraPlate);
		addBlock(ModBlocks.brewery);
		addItem(ModItems.vial);
		addItem(ModItems.brewVial);
		addItem(ModItems.brewFlask);
		addItem(ModItems.bloodPendant);
		addBlock(ModBlocks.pylon);
		addBlock(ModBlocks.pistonRelay);

		addBlock(ModBlocks.redStringContainer);
		addBlock(ModBlocks.redStringDispenser);
		addBlock(ModBlocks.redStringFertilizer);
		addBlock(ModBlocks.redStringComparator);
		addBlock(ModBlocks.redStringRelay);

		addBlock(ModBlocks.shinyFlower);
		addBlock(ModBlocks.floatingFlower);
		addBlock(ModBlocks.floatingSpecialFlower);
		addBlock(ModBlocks.tinyPotato);
		addBlock(ModBlocks.starfield);
		addBlock(ModBlocks.unstableBlock);
		addBlock(ModBlocks.manaBeacon);
		addItem(ModItems.signalFlare);

		addBlock(ModBlocks.dirtPath);
		addBlock(ModFluffBlocks.dirtPathSlab);

		addBlock(ModBlocks.dreamwood);
		addBlock(ModBlocks.manaGlass);
		addBlock(ModBlocks.elfGlass);

		addBlock(ModBlocks.prismarine);
		addBlock(ModBlocks.seaLamp);
		addBlock(ModFluffBlocks.prismarineStairs);
		addBlock(ModFluffBlocks.prismarineSlab);
		addBlock(ModFluffBlocks.prismarineBrickStairs);
		addBlock(ModFluffBlocks.prismarineBrickSlab);
		addBlock(ModFluffBlocks.darkPrismarineStairs);
		addBlock(ModFluffBlocks.darkPrismarineSlab);

		addBlock(ModBlocks.reedBlock);
		addBlock(ModFluffBlocks.reedStairs);
		addBlock(ModFluffBlocks.reedSlab);
		addBlock(ModBlocks.thatch);
		addBlock(ModFluffBlocks.thatchStairs);
		addBlock(ModFluffBlocks.thatchSlab);

		addBlock(ModBlocks.customBrick);
		addBlock(ModFluffBlocks.netherBrickStairs);
		addBlock(ModFluffBlocks.netherBrickSlab);
		addBlock(ModFluffBlocks.soulBrickStairs);
		addBlock(ModFluffBlocks.soulBrickSlab);
		addBlock(ModFluffBlocks.snowBrickStairs);
		addBlock(ModFluffBlocks.snowBrickSlab);
		addBlock(ModFluffBlocks.tileStairs);
		addBlock(ModFluffBlocks.tileSlab);

		addBlock(ModFluffBlocks.livingwoodStairs);
		addBlock(ModFluffBlocks.livingwoodSlab);
		addBlock(ModFluffBlocks.livingwoodPlankStairs);
		addBlock(ModFluffBlocks.livingwoodPlankSlab);
		addBlock(ModFluffBlocks.livingrockStairs);
		addBlock(ModFluffBlocks.livingrockSlab);
		addBlock(ModFluffBlocks.livingrockBrickStairs);
		addBlock(ModFluffBlocks.livingrockBrickSlab);
		addBlock(ModFluffBlocks.dreamwoodStairs);
		addBlock(ModFluffBlocks.dreamwoodSlab);
		addBlock(ModFluffBlocks.dreamwoodPlankStairs);
		addBlock(ModFluffBlocks.dreamwoodPlankSlab);

		addItem(ModItems.quartz);
		if(ConfigHandler.darkQuartzEnabled) {
			addBlock(ModFluffBlocks.darkQuartz);
			addBlock(ModFluffBlocks.darkQuartzSlab);
			addBlock(ModFluffBlocks.darkQuartzStairs);
		}

		addBlock(ModFluffBlocks.manaQuartz);
		addBlock(ModFluffBlocks.manaQuartzSlab);
		addBlock(ModFluffBlocks.manaQuartzStairs);
		addBlock(ModFluffBlocks.blazeQuartz);
		addBlock(ModFluffBlocks.blazeQuartzSlab);
		addBlock(ModFluffBlocks.blazeQuartzStairs);
		addBlock(ModFluffBlocks.lavenderQuartz);
		addBlock(ModFluffBlocks.lavenderQuartzSlab);
		addBlock(ModFluffBlocks.lavenderQuartzStairs);
		addBlock(ModFluffBlocks.redQuartz);
		addBlock(ModFluffBlocks.redQuartzSlab);
		addBlock(ModFluffBlocks.redQuartzStairs);
		addBlock(ModFluffBlocks.elfQuartz);
		addBlock(ModFluffBlocks.elfQuartzSlab);
		addBlock(ModFluffBlocks.elfQuartzStairs);
		
		addBlock(ModFluffBlocks.biomeStoneA);
		addBlock(ModFluffBlocks.biomeStoneB);
		for(int i = 0; i < 24; i++)
			addBlock(ModFluffBlocks.biomeStoneStairs[i]);
		for(int i = 0; i < 24; i++)
			addBlock(ModFluffBlocks.biomeStoneSlabs[i]);

		addItem(ModItems.glassPick);
		addItem(ModItems.manasteelPick);
		addItem(ModItems.manasteelShovel);
		addItem(ModItems.manasteelAxe);
		addItem(ModItems.manasteelShears);
		addItem(ModItems.manasteelSword);
		addItem(ModItems.enderDagger);
		addItem(ModItems.manasteelHelm);
		if(Botania.thaumcraftLoaded)
			addItem(ModItems.manasteelHelmRevealing);
		addItem(ModItems.manasteelChest);
		addItem(ModItems.manasteelLegs);
		addItem(ModItems.manasteelBoots);
		addItem(ModItems.terraSword);
		addItem(ModItems.terraPick);
		addItem(ModItems.terrasteelHelm);
		if(Botania.thaumcraftLoaded)
			addItem(ModItems.terrasteelHelmRevealing);
		addItem(ModItems.terrasteelChest);
		addItem(ModItems.terrasteelLegs);
		addItem(ModItems.terrasteelBoots);
		addItem(ModItems.elementiumPick);
		addItem(ModItems.elementiumShovel);
		addItem(ModItems.elementiumAxe);
		addItem(ModItems.elementiumShears);
		addItem(ModItems.elementiumSword);
		addItem(ModItems.elementiumHelm);
		if(Botania.thaumcraftLoaded)
			addItem(ModItems.elementiumHelmRevealing);
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
		addItem(ModItems.reachRing);
		addItem(ModItems.pixieRing);
		addItem(ModItems.travelBelt);
		addItem(ModItems.superTravelBelt);
		addItem(ModItems.knockbackBelt);
		addItem(ModItems.itemFinder);
		addItem(ModItems.monocle);
		addItem(ModItems.icePendant);
		addItem(ModItems.lavaPendant);
		addItem(ModItems.superLavaPendant);
		addItem(ModItems.holyCloak);
		addItem(ModItems.unholyCloak);
		addItem(ModItems.goldLaurel);
		addItem(ModItems.divaCharm);
		addItem(ModItems.flightTiara);

		addItem(ModItems.manaTablet);
		addItem(ModItems.manaMirror);
		addItem(ModItems.manaBottle);
		addBlock(ModBlocks.pool);
		addBlock(ModBlocks.alchemyCatalyst);
		addBlock(ModBlocks.conjurationCatalyst);
		addBlock(ModBlocks.distributor);
		addBlock(ModBlocks.manaVoid);
		addBlock(ModBlocks.manaDetector);
		addBlock(ModBlocks.rfGenerator);
		addBlock(ModBlocks.spreader);
		addBlock(ModBlocks.turntable);
		addBlock(ModBlocks.prism);
		addItem(ModItems.spark);
		addItem(ModItems.sparkUpgrade);
		addItem(ModItems.manaGun);
		addItem(ModItems.clip);
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

