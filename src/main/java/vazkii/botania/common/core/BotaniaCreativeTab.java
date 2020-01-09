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

import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Comparator;

public final class BotaniaCreativeTab extends ItemGroup {

	public static final BotaniaCreativeTab INSTANCE = new BotaniaCreativeTab();
	private NonNullList<ItemStack> list;

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
	public void fill(@Nonnull NonNullList<ItemStack> list) {
		this.list = list;

		addItem(ModItems.lexicon);

		for(DyeColor color : DyeColor.values())
			addItem(ModBlocks.getFlower(color));
		addTag(new ResourceLocation(LibMisc.MOD_ID, "special_flowers"));
		for(DyeColor color : DyeColor.values())
			this.addItem(ModItems.getPetal(color));
		addItem(ModItems.pestleAndMortar);
		for(DyeColor color : DyeColor.values())
			this.addItem(ModItems.getDye(color));
		addItem(ModItems.fertilizer);
		addItem(ModItems.flowerBag);
		addItem(ModItems.blackLotus);
		addItem(ModItems.blackerLotus);
		addItem(ModItems.twigWand);
		addItem(ModItems.obedienceStick);
		addItem(ModItems.manaSteel);
		addItem(ModItems.manaPearl);
		addItem(ModItems.manaDiamond);
		addItem(ModItems.livingwoodTwig);
		addItem(ModItems.terrasteel);
		addItem(ModItems.lifeEssence);
		addItem(ModItems.redstoneRoot);
		addItem(ModItems.elementium);
		addItem(ModItems.pixieDust);
		addItem(ModItems.dragonstone);
		addItem(ModItems.placeholder);
		addItem(ModItems.redString);
		addItem(ModItems.dreamwoodTwig);
		addItem(ModItems.gaiaIngot);
		addItem(ModItems.enderAirBottle);
		addItem(ModItems.manaString);
		addItem(ModItems.manasteelNugget);
		addItem(ModItems.terrasteelNugget);
		addItem(ModItems.elementiumNugget);
		addItem(ModItems.livingroot);
		addItem(ModItems.pebble);
		addItem(ModItems.manaweaveCloth);
		addItem(ModItems.manaPowder);
		addItem(ModBlocks.manasteelBlock);
		addItem(ModBlocks.terrasteelBlock);
		addItem(ModBlocks.elementiumBlock);
		addItem(ModBlocks.manaDiamondBlock);
		addItem(ModBlocks.dragonstoneBlock);
		addItem(ModItems.manaCookie);
		addItem(ModItems.runeWater);
		addItem(ModItems.runeFire);
		addItem(ModItems.runeEarth);
		addItem(ModItems.runeAir);
		addItem(ModItems.runeSpring);
		addItem(ModItems.runeSummer);
		addItem(ModItems.runeAutumn);
		addItem(ModItems.runeWinter);
		addItem(ModItems.runeMana);
		addItem(ModItems.runeLust);
		addItem(ModItems.runeGluttony);
		addItem(ModItems.runeGreed);
		addItem(ModItems.runeSloth);
		addItem(ModItems.runeWrath);
		addItem(ModItems.runeEnvy);
		addItem(ModItems.runePride);
		addItem(ModBlocks.avatar);
		addItem(ModItems.dirtRod);
		addItem(ModItems.skyDirtRod);
		addItem(ModItems.cobbleRod);
		addItem(ModItems.terraformRod);
		addItem(ModItems.laputaShard);
		addItem(ModItems.grassHorn);
		addItem(ModItems.leavesHorn);
		addItem(ModItems.snowHorn);
		addItem(ModItems.waterRod);
		addItem(ModItems.openBucket);
		addItem(ModItems.rainbowRod);
		addItem(ModBlocks.bifrostPerm);
		addItem(ModFluffBlocks.bifrostPane);
		addItem(ModBlocks.shimmerrock);
		addItem(ModBlocks.shimmerwoodPlanks);
		addItem(ModItems.tornadoRod);
		addItem(ModItems.fireRod);
		addItem(ModItems.smeltRod);
		addItem(ModItems.exchangeRod);
		addItem(ModItems.diviningRod);
		addItem(ModItems.gravityRod);
		addItem(ModItems.missileRod);
		addItem(ModItems.necroVirus);
		addItem(ModItems.nullVirus);
		addItem(ModItems.slingshot);
		addItem(ModItems.vineBall);
		addItem(ModItems.keepIvy);
		addItem(ModItems.regenIvy);
		addItem(ModItems.worldSeed);
		addItem(ModItems.overgrowthSeed);
		addItem(ModBlocks.enchantedSoil);
		addItem(ModItems.grassSeeds);
		addItem(ModItems.podzolSeeds);
		addItem(ModItems.mycelSeeds);
		addItem(ModItems.drySeeds);
		addItem(ModItems.goldenSeeds);
		addItem(ModItems.vividSeeds);
		addItem(ModItems.scorchedSeeds);
		addItem(ModItems.infusedSeeds);
		addItem(ModItems.mutatedSeeds);
		addItem(ModBlocks.dryGrass);
		addItem(ModBlocks.goldenGrass);
		addItem(ModBlocks.vividGrass);
		addItem(ModBlocks.scorchedGrass);
		addItem(ModBlocks.infusedGrass);
		addItem(ModBlocks.mutatedGrass);
		if(Botania.thaumcraftLoaded)
			addItem(ModItems.manaInkwell);
		addItem(ModBlocks.wildDrum);
		addItem(ModBlocks.gatheringDrum);
		addItem(ModBlocks.canopyDrum);
		addItem(ModBlocks.forestEye);
		addItem(ModBlocks.enderEye);
		addItem(ModItems.enderHand);
		addItem(ModItems.spellCloth);
		addItem(ModItems.craftingHalo);
		addItem(ModItems.autocraftingHalo);
		addItem(ModItems.spawnerMover);
		addItem(ModBlocks.spawnerClaw);
		addItem(ModBlocks.cocoon);
		addItem(ModBlocks.teruTeruBozu);
		addItem(ModItems.slimeBottle);
		addItem(ModItems.sextant);
		addItem(ModItems.astrolabe);
		addItem(ModItems.blackHoleTalisman);

		if(Botania.gardenOfGlassLoaded) {
			addItem(ModBlocks.root);
			addItem(ModItems.waterBowl);
		}

		addItem(ModBlocks.livingrock);
		addItem(ModBlocks.livingwood);
		addItem(ModBlocks.livingwoodPlanks);
		addItem(ModBlocks.livingwoodPlanksMossy);
		addItem(ModBlocks.livingwoodGlimmering);
		addItem(ModBlocks.livingwoodFramed);
		addItem(ModBlocks.livingwoodPatternFramed);

		addItem(ModBlocks.openCrate);
		addItem(ModBlocks.craftCrate);
		addItem(ModItems.craftPattern1_1);
		addItem(ModItems.craftPattern2_2);
		addItem(ModItems.craftPattern1_2);
		addItem(ModItems.craftPattern2_1);
		addItem(ModItems.craftPattern1_3);
		addItem(ModItems.craftPattern3_1);
		addItem(ModItems.craftPattern2_3);
		addItem(ModItems.craftPattern3_2);
		addItem(ModItems.craftPatternDonut);

		addItem(ModBlocks.abstrusePlatform);
		addItem(ModBlocks.spectralPlatform);
		addItem(ModBlocks.infrangiblePlatform);
		addItem(ModBlocks.alfPortal);
		addItem(ModBlocks.defaultAltar);
		addItem(ModBlocks.forestAltar);
		addItem(ModBlocks.plainsAltar);
		addItem(ModBlocks.mountainAltar);
		addItem(ModBlocks.fungalAltar);
		addItem(ModBlocks.swampAltar);
		addItem(ModBlocks.desertAltar);
		addItem(ModBlocks.taigaAltar);
		addItem(ModBlocks.mesaAltar);
		addItem(ModBlocks.mossyAltar);
		addItem(ModBlocks.runeAltar);
		addItem(ModBlocks.terraPlate);
		addItem(ModBlocks.brewery);
		addItem(ModItems.vial);
		addItem(ModItems.flask);
		addItem(ModItems.brewVial);
		addItem(ModItems.brewFlask);
		addItem(ModBlocks.incensePlate);
		addItem(ModItems.incenseStick);
		addItem(ModItems.bloodPendant);
		addItem(ModBlocks.felPumpkin);
		addItem(ModBlocks.manaPylon);
		addItem(ModBlocks.naturaPylon);
		addItem(ModBlocks.gaiaPylon);
		addItem(ModBlocks.pistonRelay);
		addItem(ModBlocks.hourglass);
		addItem(ModBlocks.animatedTorch);

		addItem(ModBlocks.redStringContainer);
		addItem(ModBlocks.redStringDispenser);
		addItem(ModBlocks.redStringFertilizer);
		addItem(ModBlocks.redStringComparator);
		addItem(ModBlocks.redStringRelay);
		addItem(ModBlocks.redStringInterceptor);

		addItem(ModBlocks.tinyPotato);
		addItem(ModBlocks.starfield);

		addItem(ModBlocks.dreamwood);
		addItem(ModBlocks.dreamwoodPlanks);
		addItem(ModBlocks.dreamwoodPlanksMossy);
		addItem(ModBlocks.dreamwoodFramed);
		addItem(ModBlocks.dreamwoodPatternFramed);
		addItem(ModBlocks.dreamwoodGlimmering);
		addItem(ModBlocks.manaGlass);
		addItem(ModFluffBlocks.managlassPane);
		addItem(ModBlocks.elfGlass);
		addItem(ModFluffBlocks.alfglassPane);

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
		addItem(ModItems.flareChakram);
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
		addItem(ModItems.ancientWillAhrim);
		addItem(ModItems.ancientWillDharok);
		addItem(ModItems.ancientWillGuthan);
		addItem(ModItems.ancientWillTorag);
		addItem(ModItems.ancientWillVerac);
		addItem(ModItems.ancientWillKaril);
		addItem(ModItems.pinkinator);
		addItem(ModBlocks.gaiaHead);
		if(ConfigHandler.COMMON.relicsEnabled.get()) {
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
		addItem(ModBlocks.tinyPlanet);
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
		addItem(ModBlocks.manaPool);
		addItem(ModBlocks.creativePool);
		addItem(ModBlocks.dilutedPool);
		addItem(ModBlocks.fabulousPool);
		addItem(ModBlocks.alchemyCatalyst);
		addItem(ModBlocks.conjurationCatalyst);
		addItem(ModBlocks.distributor);
		addItem(ModBlocks.manaVoid);
		addItem(ModBlocks.bellows);
		addItem(ModBlocks.manaDetector);
		addItem(ModBlocks.manaBomb);
		addItem(ModBlocks.ghostRail);
		addItem(ModItems.poolMinecart);
		addItem(ModBlocks.pump);
		addItem(ModBlocks.rfGenerator);
		addItem(ModBlocks.manaSpreader);
		addItem(ModBlocks.redstoneSpreader);
		addItem(ModBlocks.elvenSpreader);
		addItem(ModBlocks.gaiaSpreader);
		addItem(ModBlocks.turntable);
		addItem(ModBlocks.prism);
		addItem(ModItems.lensNormal);
		addItem(ModItems.lensSpeed);
		addItem(ModItems.lensPower);
		addItem(ModItems.lensTime);
		addItem(ModItems.lensEfficiency);
		addItem(ModItems.lensBounce);
		addItem(ModItems.lensGravity);
		addItem(ModItems.lensMine);
		addItem(ModItems.lensDamage);
		addItem(ModItems.lensPhantom);
		addItem(ModItems.lensMagnet);
		addItem(ModItems.lensExplosive);
		addItem(ModItems.lensInfluence);
		addItem(ModItems.lensWeight);
		addItem(ModItems.lensPaint);
		addItem(ModItems.lensFire);
		addItem(ModItems.lensPiston);
		addItem(ModItems.lensLight);
		addItem(ModItems.lensWarp);
		addItem(ModItems.lensRedirect);
		addItem(ModItems.lensFirework);
		addItem(ModItems.lensFlare);
		addItem(ModItems.lensMessenger);
		addItem(ModItems.lensTripwire);
		addItem(ModItems.manaGun);
		addItem(ModItems.clip);
		addItem(ModItems.spark);
		addItem(ModItems.sparkUpgradeDispersive);
		addItem(ModItems.sparkUpgradeDominant);
		addItem(ModItems.sparkUpgradeRecessive);
		addItem(ModItems.sparkUpgradeIsolated);
		addItem(ModBlocks.sparkChanger);
		addItem(ModItems.corporeaSpark);
		addItem(ModItems.corporeaSparkMaster);
		addItem(ModBlocks.corporeaIndex);
		addItem(ModBlocks.corporeaFunnel);
		addItem(ModBlocks.corporeaInterceptor);
		addItem(ModBlocks.corporeaRetainer);
		addItem(ModBlocks.corporeaCrystalCube);
		addItem(ModBlocks.lightRelayDefault);
		addItem(ModBlocks.lightRelayDetector);
		addItem(ModBlocks.lightRelayFork);
		addItem(ModBlocks.lightRelayToggle);
		addItem(ModBlocks.lightLauncher);
		addItem(ModBlocks.cellBlock);

		// FLUFF

		for(DyeColor color : DyeColor.values())
			addItem(ModBlocks.getDoubleFlower(color));
		for(DyeColor color : DyeColor.values())
			addItem(ModBlocks.getShinyFlower(color));
		for(DyeColor color : DyeColor.values())
			addItem(ModBlocks.getFloatingFlower(color));
		addTag(new ResourceLocation(LibMisc.MOD_ID, "special_floating_flowers"));
		addItem(ModBlocks.petalBlockWhite);
		addItem(ModBlocks.petalBlockOrange);
		addItem(ModBlocks.petalBlockMagenta);
		addItem(ModBlocks.petalBlockLightBlue);
		addItem(ModBlocks.petalBlockYellow);
		addItem(ModBlocks.petalBlockLime);
		addItem(ModBlocks.petalBlockPink);
		addItem(ModBlocks.petalBlockGray);
		addItem(ModBlocks.petalBlockSilver);
		addItem(ModBlocks.petalBlockCyan);
		addItem(ModBlocks.petalBlockPurple);
		addItem(ModBlocks.petalBlockBlue);
		addItem(ModBlocks.petalBlockBrown);
		addItem(ModBlocks.petalBlockGreen);
		addItem(ModBlocks.petalBlockRed);
		addItem(ModBlocks.petalBlockBlack);
		for(DyeColor color : DyeColor.values())
			addItem(ModBlocks.getMushroom(color));

		addItem(ModBlocks.blazeBlock);

		addItem(ModBlocks.azulejo0);
		addItem(ModBlocks.azulejo1);
		addItem(ModBlocks.azulejo2);
		addItem(ModBlocks.azulejo3);
		addItem(ModBlocks.azulejo4);
		addItem(ModBlocks.azulejo5);
		addItem(ModBlocks.azulejo6);
		addItem(ModBlocks.azulejo7);
		addItem(ModBlocks.azulejo8);
		addItem(ModBlocks.azulejo9);
		addItem(ModBlocks.azulejo10);
		addItem(ModBlocks.azulejo11);
		addItem(ModBlocks.azulejo12);
		addItem(ModBlocks.azulejo13);
		addItem(ModBlocks.azulejo14);
		addItem(ModBlocks.azulejo15);

		addItem(ModFluffBlocks.livingwoodStairs);
		addItem(ModFluffBlocks.livingwoodSlab);
		addItem(ModFluffBlocks.livingwoodWall);
		addItem(ModFluffBlocks.livingwoodFence);
		addItem(ModFluffBlocks.livingwoodFenceGate);
		addItem(ModFluffBlocks.livingwoodPlankStairs);
		addItem(ModFluffBlocks.livingwoodPlankSlab);
		addItem(ModBlocks.livingrockBrick);
		addItem(ModBlocks.livingrockBrickCracked);
		addItem(ModBlocks.livingrockBrickMossy);
		addItem(ModBlocks.livingrockBrickChiseled);
		addItem(ModFluffBlocks.livingrockStairs);
		addItem(ModFluffBlocks.livingrockSlab);
		addItem(ModFluffBlocks.livingrockWall);
		addItem(ModFluffBlocks.livingrockBrickStairs);
		addItem(ModFluffBlocks.livingrockBrickSlab);
		addItem(ModFluffBlocks.dreamwoodStairs);
		addItem(ModFluffBlocks.dreamwoodSlab);
		addItem(ModFluffBlocks.dreamwoodWall);
		addItem(ModFluffBlocks.dreamwoodFence);
		addItem(ModFluffBlocks.dreamwoodFenceGate);
		addItem(ModFluffBlocks.dreamwoodPlankStairs);
		addItem(ModFluffBlocks.dreamwoodPlankSlab);
		addItem(ModFluffBlocks.shimmerwoodPlankStairs);
		addItem(ModFluffBlocks.shimmerwoodPlankSlab);
		addItem(ModFluffBlocks.shimmerrockStairs);
		addItem(ModFluffBlocks.shimmerrockSlab);

		addItem(ModItems.darkQuartz);
		addItem(ModItems.manaQuartz);
		addItem(ModItems.blazeQuartz);
		addItem(ModItems.lavenderQuartz);
		addItem(ModItems.redQuartz);
		addItem(ModItems.elfQuartz);
		addItem(ModItems.sunnyQuartz);

		addItem(ModFluffBlocks.darkQuartz);
		addItem(ModFluffBlocks.darkQuartzSlab);
		addItem(ModFluffBlocks.darkQuartzStairs);
		addItem(ModFluffBlocks.darkQuartzChiseled);
		addItem(ModFluffBlocks.darkQuartzPillar);
		addItem(ModFluffBlocks.manaQuartz);
		addItem(ModFluffBlocks.manaQuartzSlab);
		addItem(ModFluffBlocks.manaQuartzStairs);
		addItem(ModFluffBlocks.manaQuartzChiseled);
		addItem(ModFluffBlocks.manaQuartzPillar);
		addItem(ModFluffBlocks.blazeQuartz);
		addItem(ModFluffBlocks.blazeQuartzSlab);
		addItem(ModFluffBlocks.blazeQuartzStairs);
		addItem(ModFluffBlocks.blazeQuartzChiseled);
		addItem(ModFluffBlocks.blazeQuartzPillar);
		addItem(ModFluffBlocks.lavenderQuartz);
		addItem(ModFluffBlocks.lavenderQuartzSlab);
		addItem(ModFluffBlocks.lavenderQuartzStairs);
		addItem(ModFluffBlocks.lavenderQuartzChiseled);
		addItem(ModFluffBlocks.lavenderQuartzPillar);
		addItem(ModFluffBlocks.redQuartz);
		addItem(ModFluffBlocks.redQuartzSlab);
		addItem(ModFluffBlocks.redQuartzStairs);
		addItem(ModFluffBlocks.redQuartzChiseled);
		addItem(ModFluffBlocks.redQuartzPillar);
		addItem(ModFluffBlocks.elfQuartz);
		addItem(ModFluffBlocks.elfQuartzSlab);
		addItem(ModFluffBlocks.elfQuartzStairs);
		addItem(ModFluffBlocks.elfQuartzChiseled);
		addItem(ModFluffBlocks.elfQuartzPillar);
		addItem(ModFluffBlocks.sunnyQuartz);
		addItem(ModFluffBlocks.sunnyQuartzSlab);
		addItem(ModFluffBlocks.sunnyQuartzStairs);
		addItem(ModFluffBlocks.sunnyQuartzChiseled);
		addItem(ModFluffBlocks.sunnyQuartzPillar);

		addItem(ModFluffBlocks.biomeStoneForest);
		addItem(ModFluffBlocks.biomeStonePlains);
		addItem(ModFluffBlocks.biomeStoneMountain);
		addItem(ModFluffBlocks.biomeStoneFungal);
		addItem(ModFluffBlocks.biomeStoneSwamp);
		addItem(ModFluffBlocks.biomeStoneDesert);
		addItem(ModFluffBlocks.biomeStoneTaiga);
		addItem(ModFluffBlocks.biomeStoneMesa);
		addItem(ModFluffBlocks.biomeCobblestoneForest);
		addItem(ModFluffBlocks.biomeCobblestonePlains);
		addItem(ModFluffBlocks.biomeCobblestoneMountain);
		addItem(ModFluffBlocks.biomeCobblestoneFungal);
		addItem(ModFluffBlocks.biomeCobblestoneSwamp);
		addItem(ModFluffBlocks.biomeCobblestoneDesert);
		addItem(ModFluffBlocks.biomeCobblestoneTaiga);
		addItem(ModFluffBlocks.biomeCobblestoneMesa);
		addItem(ModFluffBlocks.biomeBrickForest);
		addItem(ModFluffBlocks.biomeBrickPlains);
		addItem(ModFluffBlocks.biomeBrickMountain);
		addItem(ModFluffBlocks.biomeBrickFungal);
		addItem(ModFluffBlocks.biomeBrickSwamp);
		addItem(ModFluffBlocks.biomeBrickDesert);
		addItem(ModFluffBlocks.biomeBrickTaiga);
		addItem(ModFluffBlocks.biomeBrickMesa);
		addItem(ModFluffBlocks.biomeChiseledBrickForest);
		addItem(ModFluffBlocks.biomeChiseledBrickPlains);
		addItem(ModFluffBlocks.biomeChiseledBrickMountain);
		addItem(ModFluffBlocks.biomeChiseledBrickFungal);
		addItem(ModFluffBlocks.biomeChiseledBrickSwamp);
		addItem(ModFluffBlocks.biomeChiseledBrickDesert);
		addItem(ModFluffBlocks.biomeChiseledBrickTaiga);
		addItem(ModFluffBlocks.biomeChiseledBrickMesa);

		addItem(ModFluffBlocks.biomeStoneForestStairs);
		addItem(ModFluffBlocks.biomeStonePlainsStairs);
		addItem(ModFluffBlocks.biomeStoneMountainStairs);
		addItem(ModFluffBlocks.biomeStoneFungalStairs);
		addItem(ModFluffBlocks.biomeStoneSwampStairs);
		addItem(ModFluffBlocks.biomeStoneDesertStairs);
		addItem(ModFluffBlocks.biomeStoneTaigaStairs);
		addItem(ModFluffBlocks.biomeStoneMesaStairs);
		addItem(ModFluffBlocks.biomeCobblestoneForestStairs);
		addItem(ModFluffBlocks.biomeCobblestonePlainsStairs);
		addItem(ModFluffBlocks.biomeCobblestoneMountainStairs);
		addItem(ModFluffBlocks.biomeCobblestoneFungalStairs);
		addItem(ModFluffBlocks.biomeCobblestoneSwampStairs);
		addItem(ModFluffBlocks.biomeCobblestoneDesertStairs);
		addItem(ModFluffBlocks.biomeCobblestoneTaigaStairs);
		addItem(ModFluffBlocks.biomeCobblestoneMesaStairs);
		addItem(ModFluffBlocks.biomeBrickForestStairs);
		addItem(ModFluffBlocks.biomeBrickPlainsStairs);
		addItem(ModFluffBlocks.biomeBrickMountainStairs);
		addItem(ModFluffBlocks.biomeBrickFungalStairs);
		addItem(ModFluffBlocks.biomeBrickSwampStairs);
		addItem(ModFluffBlocks.biomeBrickDesertStairs);
		addItem(ModFluffBlocks.biomeBrickTaigaStairs);
		addItem(ModFluffBlocks.biomeBrickMesaStairs);
		
		addItem(ModFluffBlocks.biomeStoneForestSlab);
		addItem(ModFluffBlocks.biomeStonePlainsSlab);
		addItem(ModFluffBlocks.biomeStoneMountainSlab);
		addItem(ModFluffBlocks.biomeStoneFungalSlab);
		addItem(ModFluffBlocks.biomeStoneSwampSlab);
		addItem(ModFluffBlocks.biomeStoneDesertSlab);
		addItem(ModFluffBlocks.biomeStoneTaigaSlab);
		addItem(ModFluffBlocks.biomeStoneMesaSlab);
		addItem(ModFluffBlocks.biomeCobblestoneForestSlab);
		addItem(ModFluffBlocks.biomeCobblestonePlainsSlab);
		addItem(ModFluffBlocks.biomeCobblestoneMountainSlab);
		addItem(ModFluffBlocks.biomeCobblestoneFungalSlab);
		addItem(ModFluffBlocks.biomeCobblestoneSwampSlab);
		addItem(ModFluffBlocks.biomeCobblestoneDesertSlab);
		addItem(ModFluffBlocks.biomeCobblestoneTaigaSlab);
		addItem(ModFluffBlocks.biomeCobblestoneMesaSlab);
		addItem(ModFluffBlocks.biomeBrickForestSlab);
		addItem(ModFluffBlocks.biomeBrickPlainsSlab);
		addItem(ModFluffBlocks.biomeBrickMountainSlab);
		addItem(ModFluffBlocks.biomeBrickFungalSlab);
		addItem(ModFluffBlocks.biomeBrickSwampSlab);
		addItem(ModFluffBlocks.biomeBrickDesertSlab);
		addItem(ModFluffBlocks.biomeBrickTaigaSlab);
		addItem(ModFluffBlocks.biomeBrickMesaSlab);
			
		addItem(ModFluffBlocks.biomeWallForest);
		addItem(ModFluffBlocks.biomeWallPlains);
		addItem(ModFluffBlocks.biomeWallMountain);
		addItem(ModFluffBlocks.biomeWallFungal);
		addItem(ModFluffBlocks.biomeWallSwamp);
		addItem(ModFluffBlocks.biomeWallDesert);
		addItem(ModFluffBlocks.biomeWallTaiga);
		addItem(ModFluffBlocks.biomeWallMesa);

		addItem(ModFluffBlocks.whitePavement);
		addItem(ModFluffBlocks.blackPavement);
		addItem(ModFluffBlocks.bluePavement);
		addItem(ModFluffBlocks.yellowPavement);
		addItem(ModFluffBlocks.redPavement);
		addItem(ModFluffBlocks.greenPavement);
		addItem(ModFluffBlocks.whitePavementStair);
		addItem(ModFluffBlocks.blackPavementStair);
		addItem(ModFluffBlocks.bluePavementStair);
		addItem(ModFluffBlocks.yellowPavementStair);
		addItem(ModFluffBlocks.redPavementStair);
		addItem(ModFluffBlocks.greenPavementStair);
		addItem(ModFluffBlocks.whitePavementSlab);
		addItem(ModFluffBlocks.blackPavementSlab);
		addItem(ModFluffBlocks.bluePavementSlab);
		addItem(ModFluffBlocks.yellowPavementSlab);
		addItem(ModFluffBlocks.redPavementSlab);
		addItem(ModFluffBlocks.greenPavementSlab);

		addItem(ModItems.blackBowtie);
		addItem(ModItems.blackTie);
		addItem(ModItems.redGlasses);
		addItem(ModItems.puffyScarf);
		addItem(ModItems.engineerGoggles);
		addItem(ModItems.eyepatch);
		addItem(ModItems.wickedEyepatch);
		addItem(ModItems.redRibbons);
		addItem(ModItems.pinkFlowerBud);
		addItem(ModItems.polkaDottedBows);
		addItem(ModItems.blueButterfly);
		addItem(ModItems.catEars);
		addItem(ModItems.witchPin);
		addItem(ModItems.devilTail);
		addItem(ModItems.kamuiEye);
		addItem(ModItems.googlyEyes);
		addItem(ModItems.fourLeafClover);
		addItem(ModItems.clockEye);
		addItem(ModItems.unicornHorn);
		addItem(ModItems.devilHorns);
		addItem(ModItems.hyperPlus);
		addItem(ModItems.botanistEmblem);
		addItem(ModItems.ancientMask);
		addItem(ModItems.eerieMask);
		addItem(ModItems.alienAntenna);
		addItem(ModItems.anaglyphGlasses);
		addItem(ModItems.orangeShades);
		addItem(ModItems.grouchoGlasses);
		addItem(ModItems.thickEyebrows);
		addItem(ModItems.lusitanicShield);
		addItem(ModItems.tinyPotatoMask);
		addItem(ModItems.questgiverMark);
		addItem(ModItems.thinkingHand);
	}

	private void addTag(ResourceLocation tagId) {
		ItemTags.getCollection().getOrCreate(tagId).getAllElements().stream()
				.sorted(Comparator.comparing(ForgeRegistryEntry::getRegistryName))
				.forEach(this::addItem);
	}

	private void addItem(IItemProvider item) {
		item.asItem().fillItemGroup(this, list);
	}
}