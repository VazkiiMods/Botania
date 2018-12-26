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

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public final class BotaniaCreativeTab extends CreativeTabs {

	public static final BotaniaCreativeTab INSTANCE = new BotaniaCreativeTab();
	NonNullList<ItemStack> list;

	public BotaniaCreativeTab() {
		super(LibMisc.MOD_ID);
		setNoTitle();
		setBackgroundImageName(LibResources.GUI_CREATIVE);
	}

	@Nonnull
	@Override
	public ItemStack createIcon() {
		return new ItemStack(ModItems.lexicon);
	}

	@Override
	public boolean hasSearchBar() {
		return true;
	}

	@Override
	public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list) {
		this.list = list;

		addItem(ModItems.lexicon);

		addBlock(ModBlocks.flower);
		addBlock(ModBlocks.specialFlower);
		addItem(ModItems.petal);
		addItem(ModItems.pestleAndMortar);
		addItem(ModItems.dye);
		addItem(ModItems.fertilizer);
		addItem(ModItems.flowerBag);
		addItem(ModItems.blackLotus);
		addItem(ModItems.twigWand);
		addItem(ModItems.obedienceStick);
		addItem(ModItems.manaResource);
		addBlock(ModBlocks.storage);
		addItem(ModItems.manaCookie);
		addItem(ModItems.rune);

		addBlock(ModBlocks.avatar);
		addItem(ModItems.dirtRod);
		addItem(ModItems.skyDirtRod);
		addItem(ModItems.cobbleRod);
		addItem(ModItems.terraformRod);
		addItem(ModItems.laputaShard);
		addItem(ModItems.grassHorn);
		addItem(ModItems.waterRod);
		addItem(ModItems.openBucket);
		addItem(ModItems.rainbowRod);
		addBlock(ModBlocks.bifrostPerm);
		addBlock(ModFluffBlocks.bifrostPane);
		addBlock(ModBlocks.shimmerrock);
		addBlock(ModBlocks.shimmerwoodPlanks);
		addItem(ModItems.tornadoRod);
		addItem(ModItems.fireRod);
		addItem(ModItems.smeltRod);
		addItem(ModItems.exchangeRod);
		addItem(ModItems.diviningRod);
		addItem(ModItems.gravityRod);
		addItem(ModItems.missileRod);
		addItem(ModItems.virus);
		addItem(ModItems.slingshot);
		addItem(ModItems.vineBall);
		addItem(ModItems.keepIvy);
		addItem(ModItems.worldSeed);
		addItem(ModItems.overgrowthSeed);
		addBlock(ModBlocks.enchantedSoil);
		addItem(ModItems.grassSeeds);
		addBlock(ModBlocks.altGrass);
		if(Botania.thaumcraftLoaded)
			addItem(ModItems.manaInkwell);
		addBlock(ModBlocks.forestDrum);
		addBlock(ModBlocks.forestEye);
		addBlock(ModBlocks.enderEye);
		addItem(ModItems.enderHand);
		addItem(ModItems.spellCloth);
		addItem(ModItems.craftingHalo);
		addItem(ModItems.autocraftingHalo);
		addItem(ModItems.spawnerMover);
		addBlock(ModBlocks.spawnerClaw);
		addBlock(ModBlocks.cocoon);
		addBlock(ModBlocks.teruTeruBozu);
		addItem(ModItems.slimeBottle);
		addItem(ModItems.sextant);
		addItem(ModItems.astrolabe);
		addItem(ModItems.blackHoleTalisman);

		if(Botania.gardenOfGlassLoaded) {
			addBlock(ModBlocks.root);
			addItem(ModItems.waterBowl);
		}

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
		addBlock(ModBlocks.incensePlate);
		addItem(ModItems.incenseStick);
		addItem(ModItems.bloodPendant);
		addBlock(ModBlocks.felPumpkin);
		addBlock(ModBlocks.pylon);
		addBlock(ModBlocks.pistonRelay);
		addBlock(ModBlocks.hourglass);
		addBlock(ModBlocks.animatedTorch);

		addBlock(ModBlocks.redStringContainer);
		addBlock(ModBlocks.redStringDispenser);
		addBlock(ModBlocks.redStringFertilizer);
		addBlock(ModBlocks.redStringComparator);
		addBlock(ModBlocks.redStringRelay);
		addBlock(ModBlocks.redStringInterceptor);

		addBlock(ModBlocks.tinyPotato);
		addBlock(ModBlocks.starfield);

		addBlock(ModBlocks.dreamwood);
		addBlock(ModBlocks.manaGlass);
		addBlock(ModFluffBlocks.managlassPane);
		addBlock(ModBlocks.elfGlass);
		addBlock(ModFluffBlocks.alfglassPane);

		addItem(ModItems.glassPick);
		addItem(ModItems.manasteelPick);
		addItem(ModItems.manasteelShovel);
		addItem(ModItems.manasteelAxe);
		addItem(ModItems.manasteelShears);
		addItem(ModItems.manasteelSword);
		addItem(ModItems.enderDagger);
		addItem(ModItems.livingwoodBow);
		addItem(ModItems.manasteelHelm);
		if(Botania.thaumcraftLoaded)
			addItem(ModItems.manasteelHelmRevealing);
		addItem(ModItems.manasteelChest);
		addItem(ModItems.manasteelLegs);
		addItem(ModItems.manasteelBoots);
		addItem(ModItems.manaweaveHelm);
		addItem(ModItems.manaweaveChest);
		addItem(ModItems.manaweaveLegs);
		addItem(ModItems.manaweaveBoots);
		addItem(ModItems.elementiumPick);
		addItem(ModItems.elementiumShovel);
		addItem(ModItems.elementiumAxe);
		addItem(ModItems.elementiumShears);
		addItem(ModItems.elementiumSword);
		addItem(ModItems.starSword);
		addItem(ModItems.thunderSword);
		addItem(ModItems.crystalBow);
		addItem(ModItems.elementiumHelm);
		if(Botania.thaumcraftLoaded)
			addItem(ModItems.elementiumHelmRevealing);
		addItem(ModItems.elementiumChest);
		addItem(ModItems.elementiumLegs);
		addItem(ModItems.elementiumBoots);
		addItem(ModItems.terraSword);
		addItem(ModItems.thornChakram);
		addItem(ModItems.terraPick);
		addItem(ModItems.terraAxe);
		addItem(ModItems.temperanceStone);
		addItem(ModItems.terrasteelHelm);
		if(Botania.thaumcraftLoaded)
			addItem(ModItems.terrasteelHelmRevealing);
		addItem(ModItems.terrasteelChest);
		addItem(ModItems.terrasteelLegs);
		addItem(ModItems.terrasteelBoots);
		addItem(ModItems.phantomInk);
		addItem(ModItems.cacophonium);
		addItem(ModItems.recordGaia1);
		addItem(ModItems.recordGaia2);
		addItem(ModItems.ancientWill);
		addItem(ModItems.pinkinator);
		addItem(ModItems.gaiaHead);
		if(ConfigHandler.relicsEnabled) {
			addItem(ModItems.dice);
			addItem(ModItems.infiniteFruit);
			addItem(ModItems.kingKey);
			addItem(ModItems.flugelEye);
			addItem(ModItems.thorRing);
			addItem(ModItems.odinRing);
			addItem(ModItems.lokiRing);
		}

		addItem(ModItems.baubleBox);
		addItem(ModItems.tinyPlanet);
		addBlock(ModBlocks.tinyPlanet);
		addItem(ModItems.manaRing);
		addItem(ModItems.auraRing);
		addItem(ModItems.manaRingGreater);
		addItem(ModItems.auraRingGreater);
		addItem(ModItems.waterRing);
		addItem(ModItems.miningRing);
		addItem(ModItems.magnetRing);
		addItem(ModItems.magnetRingGreater);
		addItem(ModItems.swapRing);
		addItem(ModItems.dodgeRing);
		addItem(ModItems.reachRing);
		addItem(ModItems.pixieRing);
		addItem(ModItems.travelBelt);
		addItem(ModItems.superTravelBelt);
		addItem(ModItems.speedUpBelt);
		addItem(ModItems.knockbackBelt);
		addItem(ModItems.itemFinder);
		addItem(ModItems.monocle);
		addItem(ModItems.icePendant);
		addItem(ModItems.lavaPendant);
		addItem(ModItems.superLavaPendant);
		addItem(ModItems.cloudPendant);
		addItem(ModItems.superCloudPendant);
		addItem(ModItems.invisibilityCloak);
		addItem(ModItems.holyCloak);
		addItem(ModItems.unholyCloak);
		addItem(ModItems.balanceCloak);
		addItem(ModItems.goddessCharm);
		addItem(ModItems.divaCharm);
		addItem(ModItems.thirdEye);
		addItem(ModItems.flightTiara);

		addItem(ModItems.manaTablet);
		addItem(ModItems.manaMirror);
		addItem(ModItems.manaBottle);
		addBlock(ModBlocks.pool);
		addBlock(ModBlocks.alchemyCatalyst);
		addBlock(ModBlocks.conjurationCatalyst);
		addBlock(ModBlocks.distributor);
		addBlock(ModBlocks.manaVoid);
		addBlock(ModBlocks.bellows);
		addBlock(ModBlocks.manaDetector);
		addBlock(ModBlocks.manaBomb);
		addBlock(ModBlocks.ghostRail);
		addItem(ModItems.poolMinecart);
		addBlock(ModBlocks.pump);
		addBlock(ModBlocks.rfGenerator);
		addBlock(ModBlocks.spreader);
		addBlock(ModBlocks.turntable);
		addBlock(ModBlocks.prism);
		addItem(ModItems.lens);
		addItem(ModItems.manaGun);
		addItem(ModItems.clip);
		addItem(ModItems.spark);
		addItem(ModItems.sparkUpgrade);
		addBlock(ModBlocks.sparkChanger);
		addItem(ModItems.corporeaSpark);
		addBlock(ModBlocks.corporeaIndex);
		addBlock(ModBlocks.corporeaFunnel);
		addBlock(ModBlocks.corporeaInterceptor);
		addBlock(ModBlocks.corporeaRetainer);
		addBlock(ModBlocks.corporeaCrystalCube);
		addBlock(ModBlocks.lightRelay);
		addBlock(ModBlocks.lightLauncher);
		addBlock(ModBlocks.cellBlock);

		// FLUFF

		addBlock(ModBlocks.doubleFlower1);
		addBlock(ModBlocks.doubleFlower2);
		addBlock(ModBlocks.shinyFlower);
		addBlock(ModBlocks.floatingFlower);
		addBlock(ModBlocks.floatingSpecialFlower);
		addBlock(ModBlocks.petalBlock);
		addBlock(ModBlocks.mushroom);

		addBlock(ModBlocks.blazeBlock);

		addBlock(ModBlocks.customBrick);

		addBlock(ModFluffBlocks.livingwoodStairs);
		addBlock(ModFluffBlocks.livingwoodSlab);
		addBlock(ModFluffBlocks.livingwoodWall);
		addBlock(ModFluffBlocks.livingwoodPlankStairs);
		addBlock(ModFluffBlocks.livingwoodPlankSlab);
		addBlock(ModFluffBlocks.livingrockStairs);
		addBlock(ModFluffBlocks.livingrockSlab);
		addBlock(ModFluffBlocks.livingrockWall);
		addBlock(ModFluffBlocks.livingrockBrickStairs);
		addBlock(ModFluffBlocks.livingrockBrickSlab);
		addBlock(ModFluffBlocks.dreamwoodStairs);
		addBlock(ModFluffBlocks.dreamwoodSlab);
		addBlock(ModFluffBlocks.dreamwoodWall);
		addBlock(ModFluffBlocks.dreamwoodPlankStairs);
		addBlock(ModFluffBlocks.dreamwoodPlankSlab);
		addBlock(ModFluffBlocks.shimmerwoodPlankStairs);
		addBlock(ModFluffBlocks.shimmerwoodPlankSlab);
		addBlock(ModFluffBlocks.shimmerrockStairs);
		addBlock(ModFluffBlocks.shimmerrockSlab);

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
		addBlock(ModFluffBlocks.sunnyQuartz);
		addBlock(ModFluffBlocks.sunnyQuartzSlab);
		addBlock(ModFluffBlocks.sunnyQuartzStairs);

		addBlock(ModFluffBlocks.biomeStoneA);
		addBlock(ModFluffBlocks.biomeStoneB);
		for(int i = 0; i < 24; i++)
			addBlock(ModFluffBlocks.biomeStoneStairs[i]);
		for(int i = 0; i < 24; i++)
			addBlock(ModFluffBlocks.biomeStoneSlabs[i]);
		addBlock(ModFluffBlocks.biomeStoneWall);

		addBlock(ModFluffBlocks.pavement);
		for (Block pavementStair : ModFluffBlocks.pavementStairs)
			addBlock(pavementStair);
		for (Block pavementSlab : ModFluffBlocks.pavementSlabs)
			addBlock(pavementSlab);

		addItem(ModItems.cosmetic);
	}

	private void addItem(Item item) {
		item.getSubItems(this, list);
	}

	private void addBlock(Block block) {
		ItemStack stack = new ItemStack(block);
		block.getSubBlocks(this, list);
	}

}