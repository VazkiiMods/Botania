/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 19, 2014, 3:54:48 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.tile.TileCraftCrate;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.crafting.recipe.AesirRingRecipe;
import vazkii.botania.common.crafting.recipe.AncientWillRecipe;
import vazkii.botania.common.crafting.recipe.ArmorUpgradeRecipe;
import vazkii.botania.common.crafting.recipe.BlackHoleTalismanExtractRecipe;
import vazkii.botania.common.crafting.recipe.CompositeLensRecipe;
import vazkii.botania.common.crafting.recipe.CosmeticAttachRecipe;
import vazkii.botania.common.crafting.recipe.CosmeticRemoveRecipe;
import vazkii.botania.common.crafting.recipe.HelmRevealingRecipe;
import vazkii.botania.common.crafting.recipe.KeepIvyRecipe;
import vazkii.botania.common.crafting.recipe.LensDyeingRecipe;
import vazkii.botania.common.crafting.recipe.ManaGunClipRecipe;
import vazkii.botania.common.crafting.recipe.ManaGunLensRecipe;
import vazkii.botania.common.crafting.recipe.ManaGunRemoveLensRecipe;
import vazkii.botania.common.crafting.recipe.ManaUpgradeRecipe;
import vazkii.botania.common.crafting.recipe.PhantomInkRecipe;
import vazkii.botania.common.crafting.recipe.ShapelessManaUpgradeRecipe;
import vazkii.botania.common.crafting.recipe.SpecialFloatingFlowerRecipe;
import vazkii.botania.common.crafting.recipe.SpellClothRecipe;
import vazkii.botania.common.crafting.recipe.TerraPickTippingRecipe;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibOreDict;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Mod.EventBusSubscriber
public final class ModCraftingRecipes {

	public static IRecipe recipeLexicon;
	public static List<IRecipe> recipesPetals;
	public static List<IRecipe> recipesDyes;
	public static List<IRecipe> recipesDyesVanilla;
	public static List<IRecipe> recipesPetalBlocks;
	public static List<IRecipe> recipesReversePetalBlocks;
	public static IRecipe recipePestleAndMortar;
	public static List<IRecipe> recipesTwigWand;
	public static List<IRecipe> recipesApothecary;
	public static List<IRecipe> recipesSpreader;
	public static List<IRecipe> recipesManaLens;
	public static IRecipe recipePool;
	public static IRecipe recipePoolDiluted;
	public static IRecipe recipePoolFabulous;
	public static List<IRecipe> recipesRuneAltar;
	public static IRecipe recipeLensVelocity;
	public static IRecipe recipeLensPotency;
	public static IRecipe recipeLensResistance;
	public static IRecipe recipeLensEfficiency;
	public static IRecipe recipeLensBounce;
	public static IRecipe recipeLensGravity;
	public static IRecipe recipeLensBore;
	public static IRecipe recipeLensDamaging;
	public static IRecipe recipeLensPhantom;
	public static IRecipe recipeLensMagnet;
	public static IRecipe recipeLensExplosive;
	public static IRecipe recipePylon;
	public static IRecipe recipeDistributor;
	public static IRecipe recipeLivingrockDecor1;
	public static IRecipe recipeLivingrockDecor2;
	public static IRecipe recipeLivingrockDecor3;
	public static IRecipe recipeLivingrockDecor4;
	public static IRecipe recipeLivingwoodDecor1;
	public static IRecipe recipeLivingwoodDecor2;
	public static IRecipe recipeLivingwoodDecor3;
	public static IRecipe recipeLivingwoodDecor4;
	public static IRecipe recipeLivingwoodDecor5;
	public static IRecipe recipeManaVoid;
	public static List<IRecipe> recipesManaTablet;
	public static IRecipe recipeManaDetector;
	public static IRecipe recipeManaBlaster;
	public static IRecipe recipeTurntable;
	public static IRecipe recipeFertilizerPowder;
	public static IRecipe recipeFerilizerDye;
	public static IRecipe recipeLivingwoodTwig;
	public static IRecipe recipeDirtRod;
	public static IRecipe recipeTerraformRod;
	public static IRecipe recipeRedstoneSpreader;
	public static IRecipe recipeManaMirror;
	public static IRecipe recipeManasteelHelm;
	public static IRecipe recipeManasteelChest;
	public static IRecipe recipeManasteelLegs;
	public static IRecipe recipeManasteelBoots;
	public static IRecipe recipeManasteelPick;
	public static IRecipe recipeManasteelShovel;
	public static IRecipe recipeManasteelAxe;
	public static IRecipe recipeManasteelShears;
	public static IRecipe recipeManasteelSword;
	public static IRecipe recipeGrassHorn;
	public static IRecipe recipeTerrasteelHelm;
	public static IRecipe recipeTerrasteelChest;
	public static IRecipe recipeTerrasteelLegs;
	public static IRecipe recipeTerrasteelBoots;
	public static IRecipe recipeTerraSword;
	public static IRecipe recipeTinyPlanet;
	public static IRecipe recipeManaRing;
	public static IRecipe recipeAuraRing;
	public static IRecipe recipeGreaterManaRing;
	public static IRecipe recipeGreaterAuraRing;
	public static IRecipe recipeTravelBelt;
	public static IRecipe recipeKnocbackBelt;
	public static IRecipe recipeIcePendant;
	public static IRecipe recipeFirePendant;
	public static IRecipe recipeTinyPlanetBlock;
	public static IRecipe recipeAlchemyCatalyst;
	public static IRecipe recipeOpenCrate;
	public static IRecipe recipeForestEye;
	public static IRecipe recipeRedstoneRoot;
	public static IRecipe recipeForestDrum;
	public static IRecipe recipeWaterRing;
	public static IRecipe recipeMiningRing;
	public static IRecipe recipeMagnetRing;
	public static IRecipe recipeTerraPick;
	public static IRecipe recipeDivaCharm;
	public static IRecipe recipeFlightTiara;
	public static List<IRecipe> recipesShinyFlowers;
	public static IRecipe recipePlatform;
	public static IRecipe recipeEnderDagger;
	public static IRecipe recipeDarkQuartz;
	public static IRecipe recipeBlazeQuartz;
	public static List<IRecipe> recipesLavenderQuartz;
	public static IRecipe recipeRedQuartz;
	public static IRecipe recipeSunnyQuartz;
	public static IRecipe recipeAlfPortal;
	public static IRecipe recipeNaturaPylon;
	public static IRecipe recipeWaterRod;
	public static IRecipe recipeElementiumHelm;
	public static IRecipe recipeElementiumChest;
	public static IRecipe recipeElementiumLegs;
	public static IRecipe recipeElementiumBoots;
	public static IRecipe recipeElementiumPick;
	public static IRecipe recipeElementiumShovel;
	public static IRecipe recipeElementiumAxe;
	public static IRecipe recipeElementiumShears;
	public static IRecipe recipeElementiumSword;
	public static IRecipe recipeOpenBucket;
	public static IRecipe recipeConjurationCatalyst;
	public static IRecipe recipeSpawnerMover;
	public static IRecipe recipePixieRing;
	public static IRecipe recipeSuperTravelBelt;
	public static IRecipe recipeRainbowRod;
	public static IRecipe recipeSpectralPlatform;
	public static List<IRecipe> recipesDreamwoodSpreader;
	public static IRecipe recipeTornadoRod;
	public static IRecipe recipeFireRod;
	public static IRecipe recipeVineBall;
	public static IRecipe recipeSlingshot;
	public static IRecipe recipeLensInfluence;
	public static IRecipe recipeLensWeight;
	public static IRecipe recipeLensPaint;
	public static IRecipe recipeLensWarp;
	public static IRecipe recipeLensRedirect;
	public static IRecipe recipeLensFirework;
	public static IRecipe recipeLensFlare;
	public static IRecipe recipeLensMessenger;
	public static IRecipe recipeLensTripwire;
	public static List<IRecipe> recipesMiniIsland;
	public static IRecipe recipeGaiaPylon;
	public static IRecipe recipeGatherDrum;
	public static IRecipe recipeLensFire;
	public static IRecipe recipeLensPiston;
	public static List<IRecipe> recipesLaputaShard;
	public static List<IRecipe> recipesLaputaShardUpgrade;
	public static IRecipe recipeVirusZombie;
	public static IRecipe recipeVirusSkeleton;
	public static IRecipe recipeReachRing;
	public static IRecipe recipeSkyDirtRod;
	public static IRecipe recipeSpawnerClaw;
	public static IRecipe recipeCraftCrate;
	public static IRecipe recipePlaceholder;
	public static IRecipe recipeAzulejo;
	public static List<IRecipe> recipesAzulejoCycling;
	public static IRecipe recipeEnderEyeBlock;
	public static IRecipe recipeItemFinder;
	public static IRecipe recipeSuperLavaPendant;
	public static IRecipe recipeEnderHand;
	public static IRecipe recipeGlassPick;
	public static IRecipe recipeStarfield;
	public static List<IRecipe> recipesSpark;
	public static List<IRecipe> recipesSparkUpgrades;
	public static IRecipe recipeLeafHorn;
	public static IRecipe recipeDiviningRod;
	public static List<IRecipe> recipesWings;
	public static IRecipe recipeRFGenerator;
	public static IRecipe recipeGravityRod;
	public static IRecipe recipeUltraSpreader;
	public static IRecipe recipeHelmetOfRevealing;
	public static IRecipe recipeVial;
	public static IRecipe recipeFlask;
	public static IRecipe recipeBrewery;
	public static IRecipe recipeBloodPendant;
	public static IRecipe recipeTerraPlate;
	public static IRecipe recipeRedString;
	public static IRecipe recipeRedStringContainer;
	public static IRecipe recipeRedStringDispenser;
	public static IRecipe recipeRedStringFertilizer;
	public static IRecipe recipeRedStringComparator;
	public static IRecipe recipeRedStringRelay;
	public static IRecipe recipeRedStringInterceptor;
	public static IRecipe recipeMissileRod;
	public static IRecipe recipeHolyCloak;
	public static IRecipe recipeUnholyCloak;
	public static IRecipe recipeBalanceCloak;
	public static IRecipe recipeCraftingHalo;
	public static List<IRecipe> recipesLensFlash;
	public static IRecipe recipePrism;
	public static IRecipe recipeDreamwoodTwig;
	public static IRecipe recipeMonocle;
	public static IRecipe recipeClip;
	public static IRecipe recipeCobbleRod;
	public static IRecipe recipeSmeltRod;
	public static IRecipe recipeWorldSeed;
	public static IRecipe recipeSpellCloth;
	public static IRecipe recipeThornChakram;
	public static IRecipe recipeDirtPathSlab;
	public static List<IRecipe> recipesPatterns;
	public static IRecipe recipeGaiaIngot;
	public static IRecipe recipeCorporeaSpark;
	public static IRecipe recipeMasterCorporeaSpark;
	public static IRecipe recipeCorporeaIndex;
	public static IRecipe recipeCorporeaFunnel;
	public static IRecipe recipeCorporeaInterceptor;
	public static IRecipe recipeLivingwoodBow;
	public static IRecipe recipeCrystalBow;
	public static List<IRecipe> recipesCosmeticItems;
	public static List<IRecipe> recipesMushrooms;
	public static IRecipe recipeSwapRing;
	public static IRecipe recipeSnowHorn;
	public static IRecipe recipeFlowerBag;
	public static IRecipe recipePhantomInk;
	public static IRecipe recipePoolCart;
	public static IRecipe recipePump;
	public static List<IRecipe> recipesPetalsDouble;
	public static IRecipe recipeKeepIvy;
	public static IRecipe recipeBlackHoleTalisman;
	public static List<IRecipe> recipe18StoneChisel;
	public static IRecipe recipeBlazeBlock;
	public static List<IRecipe> recipesAltarMeta;
	public static IRecipe recipeCorporeaCrystalCube;
	public static IRecipe recipeTemperanceStone;
	public static IRecipe recipeIncenseStick;
	public static IRecipe recipeIncensePlate;
	public static IRecipe recipeTerraAxe;
	public static IRecipe recipeHourglass;
	public static IRecipe recipeGhostRail;
	public static IRecipe recipeCanopyDrum;
	public static IRecipe recipeSparkChanger;
	public static IRecipe recipeCocoon;
	public static IRecipe recipeLuminizer;
	public static IRecipe recipeDetectorLuminizer;
	public static IRecipe recipeLuminizerLauncher;
	public static IRecipe recipeObedienceStick;
	public static IRecipe recipeCacophonium;
	public static IRecipe recipeManaBomb;
	public static IRecipe recipeCobweb;
	public static IRecipe recipeSlimeBottle;
	public static IRecipe recipeStarSword;
	public static IRecipe recipeExchangeRod;
	public static IRecipe recipeGreaterMagnetRing;
	public static IRecipe recipeFireChakram;
	public static IRecipe recipeThunderSword;
	public static IRecipe recipeBellows;
	public static IRecipe recipeManaweaveCloth;
	public static IRecipe recipeManaweaveHelm;
	public static IRecipe recipeManaweaveChest;
	public static IRecipe recipeManaweaveLegs;
	public static IRecipe recipeManaweaveBoots;
	public static IRecipe recipeBifrost;
	public static IRecipe recipeShimmerrock;
	public static IRecipe recipeShimmerwoodPlanks;
	public static IRecipe recipeAutocraftingHalo;
	public static List<IRecipe> recipesPavement;
	public static IRecipe recipeCellBlock;
	public static IRecipe recipeCorporeaRetainer;
	public static IRecipe recipeTeruTeruBozu;
	public static IRecipe recipeAvatar;
	public static IRecipe recipeSextant;
	public static List<IRecipe> recipesAltGrassSeeds;
	public static IRecipe recipeSpeedUpBelt;
	public static IRecipe recipeBaubleCase;
	public static IRecipe recipeDodgeRing;
	public static IRecipe recipeAnimatedTorch;
	public static IRecipe recipeForkLuminizer;
	public static IRecipe recipeToggleLuminizer;
	public static IRecipe recipeInvisibilityCloak;
	public static IRecipe recipeCloudPendant;
	public static IRecipe recipeSuperCloudPendant;
	public static IRecipe recipeThirdEye;
	public static IRecipe recipeAstrolabe;
	public static IRecipe recipeGoddessCharm;

	// Garden of Glass
	public static IRecipe recipeRootToSapling;
	public static IRecipe recipeRootToFertilizer;
	public static IRecipe recipePebbleCobblestone;
	public static IRecipe recipeMagmaToSlimeball;
	public static IRecipe recipeFelPumpkin;
	public static IRecipe recipeEndPortal;

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> evt) {
		IForgeRegistry<IRecipe> r = evt.getRegistry();

		r.register(new AesirRingRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "aesir_ring")));
		RecipeSorter.register("botania:aesirRing", AesirRingRecipe.class, RecipeSorter.Category.SHAPELESS, "");

		r.register(new AncientWillRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "ancient_will_attach")));
		RecipeSorter.register("botania:ancientWill", AncientWillRecipe.class, RecipeSorter.Category.SHAPELESS, "");

		r.register(new BlackHoleTalismanExtractRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "black_hole_talisman_extract")));
		RecipeSorter.register("botania:blackHoleTalismanExtract", BlackHoleTalismanExtractRecipe.class, RecipeSorter.Category.SHAPELESS, "");

		r.register(new SpellClothRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "cleanse_enchants")));
		RecipeSorter.register("botania:spellCloth", SpellClothRecipe.class, RecipeSorter.Category.SHAPELESS, "");

		r.register(new CompositeLensRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "composite_lens")));
		RecipeSorter.register("botania:compositeLens", CompositeLensRecipe.class, RecipeSorter.Category.SHAPELESS, "");

		r.register(new CosmeticAttachRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "cosmetic_attach")));
		r.register(new CosmeticRemoveRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "cosmetic_remove")));
		RecipeSorter.register("botania:cosmeticAttach", CosmeticAttachRecipe.class, RecipeSorter.Category.SHAPELESS, "");
		RecipeSorter.register("botania:cosmeticRemove", CosmeticRemoveRecipe.class, RecipeSorter.Category.SHAPELESS, "");

		r.register(new HelmRevealingRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "helm_revealing")));
		RecipeSorter.register("botania:helmRevealing", HelmRevealingRecipe.class, RecipeSorter.Category.SHAPELESS, "");

		r.register(new KeepIvyRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "keep_ivy_attach")));
		RecipeSorter.register("botania:keepIvy", KeepIvyRecipe.class, RecipeSorter.Category.SHAPELESS, "");

		r.register(new LensDyeingRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "lens_dye")));
		RecipeSorter.register("botania:lensDying", LensDyeingRecipe.class, RecipeSorter.Category.SHAPELESS, "");

		r.register(new ManaGunLensRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "mana_gun_add_lens")));
		r.register(new ManaGunRemoveLensRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "mana_gun_remove_lens")));
		r.register(new ManaGunClipRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "mana_gun_add_clip")));
		RecipeSorter.register("botania:manaGunLens", ManaGunLensRecipe.class, RecipeSorter.Category.SHAPELESS, "");
		RecipeSorter.register("botania:manaGunRemoveLens", ManaGunRemoveLensRecipe.class, RecipeSorter.Category.SHAPELESS, "");
		RecipeSorter.register("botania:manaGunClip", ManaGunClipRecipe.class, RecipeSorter.Category.SHAPELESS, "");
		
		r.register(new PhantomInkRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "phantom_ink")));
		RecipeSorter.register("botania:phantomInk", PhantomInkRecipe.class, RecipeSorter.Category.SHAPELESS, "");

		r.register(new SpecialFloatingFlowerRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "special_floating_flower")));
		RecipeSorter.register("botania:floatingSpecialFlower", SpecialFloatingFlowerRecipe.class, RecipeSorter.Category.SHAPELESS, "");
		
		r.register(new TerraPickTippingRecipe().setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "terra_pick_tipping")));
		RecipeSorter.register("botania:terraPickTipping", TerraPickTippingRecipe.class, RecipeSorter.Category.SHAPELESS, "");

		for(int i = 0; i < 8; i++) {
			GameRegistry.addSmelting(new ItemStack(ModFluffBlocks.biomeStoneA, 1, i + 8), new ItemStack(ModFluffBlocks.biomeStoneA, 1, i), 0.1F);
		}

		/*
		// Wand of the Forest Recipes
		for(int i = 0; i < 16; i++)
			for(int j = 0; j < 16; j++) {
				addOreDictRecipe(ItemTwigWand.forColors(i, j),
						" AS", " SB", "S  ",
						'A', LibOreDict.PETAL[i],
						'B', LibOreDict.PETAL[j],
						'S', LibOreDict.LIVINGWOOD_TWIG);
			}
		recipesTwigWand = BotaniaAPI.getLatestAddedRecipes(256);

		// Terrasteel Armor Recipes
		RecipeSorter.register("botania:armorUpgrade", ArmorUpgradeRecipe.class, RecipeSorter.Category.SHAPED, "");
		GameRegistry.addRecipe(new ArmorUpgradeRecipe(new ItemStack(ModItems.terrasteelHelmRevealing),
				"TRT", "SAS", " S ",
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'S', LibOreDict.TERRA_STEEL,
				'R', LibOreDict.RUNE[4],
				'A', new ItemStack(ModItems.manasteelHelmRevealing)));
		GameRegistry.addRecipe(new ArmorUpgradeRecipe(new ItemStack(ModItems.terrasteelHelm),
				"TRT", "SAS", " S ",
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'S', LibOreDict.TERRA_STEEL,
				'R', LibOreDict.RUNE[4],
				'A', new ItemStack(ModItems.manasteelHelm)));
		recipeTerrasteelHelm = BotaniaAPI.getLatestAddedRecipe();
		GameRegistry.addRecipe(new ArmorUpgradeRecipe(new ItemStack(ModItems.terrasteelChest),
				"TRT", "SAS", " S ",
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'S', LibOreDict.TERRA_STEEL,
				'R', LibOreDict.RUNE[5],
				'A', new ItemStack(ModItems.manasteelChest)));
		recipeTerrasteelChest = BotaniaAPI.getLatestAddedRecipe();
		GameRegistry.addRecipe(new ArmorUpgradeRecipe(new ItemStack(ModItems.terrasteelLegs),
				"TRT", "SAS", " S ",
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'S', LibOreDict.TERRA_STEEL,
				'R', LibOreDict.RUNE[6],
				'A', new ItemStack(ModItems.manasteelLegs)));
		recipeTerrasteelLegs = BotaniaAPI.getLatestAddedRecipe();
		GameRegistry.addRecipe(new ArmorUpgradeRecipe(new ItemStack(ModItems.terrasteelBoots),
				"TRT", "SAS", " S ",
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'S', LibOreDict.TERRA_STEEL,
				'R', LibOreDict.RUNE[7],
				'A', new ItemStack(ModItems.manasteelBoots)));
		recipeTerrasteelBoots = BotaniaAPI.getLatestAddedRecipe();*/

		// Mana Band Recipe
	/*	RecipeSorter.register("botania:manaUpgrade", ManaUpgradeRecipe.class, RecipeSorter.Category.SHAPED, "");
		GameRegistry.addRecipe(new ManaUpgradeRecipe(new ItemStack(ModItems.manaRing),
				"TI ", "I I", " I ",
				'T', new ItemStack(ModItems.manaTablet, 1, Short.MAX_VALUE),
				'I', LibOreDict.MANA_STEEL));
		recipeManaRing = BotaniaAPI.getLatestAddedRecipe();*/

		// Greater Mana Band Recipe
		/*RecipeSorter.register("botania:manaUpgradeShapeless", ShapelessManaUpgradeRecipe.class, RecipeSorter.Category.SHAPELESS, "");
		GameRegistry.addRecipe(new ShapelessManaUpgradeRecipe(new ItemStack(ModItems.manaRingGreater), LibOreDict.TERRA_STEEL, new ItemStack(ModItems.manaRing, 1, Short.MAX_VALUE)));
		recipeGreaterManaRing = BotaniaAPI.getLatestAddedRecipe();

		// Terra Shatterer Recipe
		GameRegistry.addRecipe(new ManaUpgradeRecipe(new ItemStack(ModItems.terraPick),
				"ITI", "ILI", " L ",
				'T', new ItemStack(ModItems.manaTablet, 1, Short.MAX_VALUE),
				'I', LibOreDict.TERRA_STEEL,
				'L', LibOreDict.LIVINGWOOD_TWIG));
		recipeTerraPick = BotaniaAPI.getLatestAddedRecipe();

		// Rod of the Seas Recipe
		addOreDictRecipe(new ItemStack(ModItems.waterRod),
				"  B", " T ", "R  ",
				'B', new ItemStack(Items.POTIONITEM), // todo 1.12 water bottle
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'R', LibOreDict.RUNE[0]);
		recipeWaterRod = BotaniaAPI.getLatestAddedRecipe();

		todo if tc ever comes back?
		// Revealing Helmet Recipes
		if(Botania.thaumcraftLoaded) {
			Item goggles = Item.REGISTRY.getObject(new ResourceLocation("thaumcraft", "goggles"));
			addShapelessRecipe(new ItemStack(ModItems.manasteelHelmRevealing), new ItemStack(ModItems.manasteelHelm), goggles);
			recipeHelmetOfRevealing = BotaniaAPI.getLatestAddedRecipe(); //We want manasteel to show in the Lexicon
			addShapelessRecipe(new ItemStack(ModItems.terrasteelHelmRevealing), new ItemStack(ModItems.terrasteelHelm), goggles);
			addShapelessRecipe(new ItemStack(ModItems.elementiumHelmRevealing), new ItemStack(ModItems.elementiumHelm), goggles);
		}
		*/
	}
}
