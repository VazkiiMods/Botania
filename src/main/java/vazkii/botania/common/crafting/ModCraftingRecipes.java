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

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.botania.common.block.ModFluffBlocks;
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
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibOreDict;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public final class ModCraftingRecipes {

	public static ResourceLocation recipeLexicon;
	public static List<ResourceLocation> recipesPetals;
	public static List<ResourceLocation> recipesDyes;
	public static List<ResourceLocation> recipesPetalBlocks;
	public static List<ResourceLocation> recipesReversePetalBlocks;
	public static ResourceLocation recipePestleAndMortar;
	public static List<ResourceLocation> recipesTwigWand;
	public static List<ResourceLocation> recipesApothecary;
	public static List<ResourceLocation> recipesSpreader;
	public static List<ResourceLocation> recipesManaLens;
	public static ResourceLocation recipePool;
	public static ResourceLocation recipePoolDiluted;
	public static ResourceLocation recipePoolFabulous;
	public static List<ResourceLocation> recipesRuneAltar;
	public static ResourceLocation recipeLensVelocity;
	public static ResourceLocation recipeLensPotency;
	public static ResourceLocation recipeLensResistance;
	public static ResourceLocation recipeLensEfficiency;
	public static ResourceLocation recipeLensBounce;
	public static ResourceLocation recipeLensGravity;
	public static ResourceLocation recipeLensBore;
	public static ResourceLocation recipeLensDamaging;
	public static ResourceLocation recipeLensPhantom;
	public static ResourceLocation recipeLensMagnet;
	public static ResourceLocation recipeLensExplosive;
	public static ResourceLocation recipePylon;
	public static ResourceLocation recipeDistributor;
	public static ResourceLocation recipeLivingrockDecor1;
	public static ResourceLocation recipeLivingrockDecor2;
	public static ResourceLocation recipeLivingrockDecor3;
	public static ResourceLocation recipeLivingrockDecor4;
	public static ResourceLocation recipeLivingwoodDecor1;
	public static ResourceLocation recipeLivingwoodDecor2;
	public static ResourceLocation recipeLivingwoodDecor3;
	public static ResourceLocation recipeLivingwoodDecor4;
	public static ResourceLocation recipeLivingwoodDecor5;
	public static ResourceLocation recipeManaVoid;
	public static List<ResourceLocation> recipesManaTablet;
	public static ResourceLocation recipeManaDetector;
	public static ResourceLocation recipeManaBlaster;
	public static ResourceLocation recipeTurntable;
	public static ResourceLocation recipeFertilizerPowder;
	public static ResourceLocation recipeFerilizerDye;
	public static ResourceLocation recipeLivingwoodTwig;
	public static ResourceLocation recipeDirtRod;
	public static ResourceLocation recipeTerraformRod;
	public static ResourceLocation recipeRedstoneSpreader;
	public static ResourceLocation recipeManaMirror;
	public static ResourceLocation recipeManasteelHelm;
	public static ResourceLocation recipeManasteelChest;
	public static ResourceLocation recipeManasteelLegs;
	public static ResourceLocation recipeManasteelBoots;
	public static ResourceLocation recipeManasteelPick;
	public static ResourceLocation recipeManasteelShovel;
	public static ResourceLocation recipeManasteelAxe;
	public static ResourceLocation recipeManasteelShears;
	public static ResourceLocation recipeManasteelSword;
	public static ResourceLocation recipeGrassHorn;
	public static ResourceLocation recipeTerrasteelHelm;
	public static ResourceLocation recipeTerrasteelChest;
	public static ResourceLocation recipeTerrasteelLegs;
	public static ResourceLocation recipeTerrasteelBoots;
	public static ResourceLocation recipeTerraSword;
	public static ResourceLocation recipeTinyPlanet;
	public static ResourceLocation recipeManaRing;
	public static ResourceLocation recipeAuraRing;
	public static ResourceLocation recipeGreaterManaRing;
	public static ResourceLocation recipeGreaterAuraRing;
	public static ResourceLocation recipeTravelBelt;
	public static ResourceLocation recipeKnocbackBelt;
	public static ResourceLocation recipeIcePendant;
	public static ResourceLocation recipeFirePendant;
	public static ResourceLocation recipeTinyPlanetBlock;
	public static ResourceLocation recipeAlchemyCatalyst;
	public static ResourceLocation recipeOpenCrate;
	public static ResourceLocation recipeForestEye;
	public static ResourceLocation recipeRedstoneRoot;
	public static ResourceLocation recipeForestDrum;
	public static ResourceLocation recipeWaterRing;
	public static ResourceLocation recipeMiningRing;
	public static ResourceLocation recipeMagnetRing;
	public static ResourceLocation recipeTerraPick;
	public static ResourceLocation recipeDivaCharm;
	public static ResourceLocation recipeFlightTiara;
	public static List<ResourceLocation> recipesShinyFlowers;
	public static ResourceLocation recipePlatform;
	public static ResourceLocation recipeEnderDagger;
	public static ResourceLocation recipeDarkQuartz;
	public static ResourceLocation recipeBlazeQuartz;
	public static List<ResourceLocation> recipesLavenderQuartz;
	public static ResourceLocation recipeRedQuartz;
	public static ResourceLocation recipeSunnyQuartz;
	public static ResourceLocation recipeAlfPortal;
	public static ResourceLocation recipeNaturaPylon;
	public static ResourceLocation recipeWaterRod;
	public static ResourceLocation recipeElementiumHelm;
	public static ResourceLocation recipeElementiumChest;
	public static ResourceLocation recipeElementiumLegs;
	public static ResourceLocation recipeElementiumBoots;
	public static ResourceLocation recipeElementiumPick;
	public static ResourceLocation recipeElementiumShovel;
	public static ResourceLocation recipeElementiumAxe;
	public static ResourceLocation recipeElementiumShears;
	public static ResourceLocation recipeElementiumSword;
	public static ResourceLocation recipeOpenBucket;
	public static ResourceLocation recipeConjurationCatalyst;
	public static ResourceLocation recipeSpawnerMover;
	public static ResourceLocation recipePixieRing;
	public static ResourceLocation recipeSuperTravelBelt;
	public static ResourceLocation recipeRainbowRod;
	public static ResourceLocation recipeSpectralPlatform;
	public static List<ResourceLocation> recipesDreamwoodSpreader;
	public static ResourceLocation recipeTornadoRod;
	public static ResourceLocation recipeFireRod;
	public static ResourceLocation recipeVineBall;
	public static ResourceLocation recipeSlingshot;
	public static ResourceLocation recipeLensInfluence;
	public static ResourceLocation recipeLensWeight;
	public static ResourceLocation recipeLensPaint;
	public static ResourceLocation recipeLensWarp;
	public static ResourceLocation recipeLensRedirect;
	public static ResourceLocation recipeLensFirework;
	public static ResourceLocation recipeLensFlare;
	public static ResourceLocation recipeLensMessenger;
	public static ResourceLocation recipeLensTripwire;
	public static List<ResourceLocation> recipesMiniIsland;
	public static ResourceLocation recipeGaiaPylon;
	public static ResourceLocation recipeGatherDrum;
	public static ResourceLocation recipeLensFire;
	public static ResourceLocation recipeLensPiston;
	public static List<ResourceLocation> recipesLaputaShard;
	public static List<ResourceLocation> recipesLaputaShardUpgrade;
	public static ResourceLocation recipeVirusZombie;
	public static ResourceLocation recipeVirusSkeleton;
	public static ResourceLocation recipeReachRing;
	public static ResourceLocation recipeSkyDirtRod;
	public static ResourceLocation recipeSpawnerClaw;
	public static ResourceLocation recipeCraftCrate;
	public static ResourceLocation recipePlaceholder;
	public static ResourceLocation recipeAzulejo;
	public static List<ResourceLocation> recipesAzulejoCycling;
	public static ResourceLocation recipeEnderEyeBlock;
	public static ResourceLocation recipeItemFinder;
	public static ResourceLocation recipeSuperLavaPendant;
	public static ResourceLocation recipeEnderHand;
	public static ResourceLocation recipeGlassPick;
	public static ResourceLocation recipeStarfield;
	public static List<ResourceLocation> recipesSpark;
	public static List<ResourceLocation> recipesSparkUpgrades;
	public static ResourceLocation recipeLeafHorn;
	public static ResourceLocation recipeDiviningRod;
	public static List<ResourceLocation> recipesWings;
	public static ResourceLocation recipeRFGenerator;
	public static ResourceLocation recipeGravityRod;
	public static ResourceLocation recipeUltraSpreader;
	public static ResourceLocation recipeHelmetOfRevealing;
	public static ResourceLocation recipeVial;
	public static ResourceLocation recipeFlask;
	public static ResourceLocation recipeBrewery;
	public static ResourceLocation recipeBloodPendant;
	public static ResourceLocation recipeTerraPlate;
	public static ResourceLocation recipeRedString;
	public static ResourceLocation recipeRedStringContainer;
	public static ResourceLocation recipeRedStringDispenser;
	public static ResourceLocation recipeRedStringFertilizer;
	public static ResourceLocation recipeRedStringComparator;
	public static ResourceLocation recipeRedStringRelay;
	public static ResourceLocation recipeRedStringInterceptor;
	public static ResourceLocation recipeMissileRod;
	public static ResourceLocation recipeHolyCloak;
	public static ResourceLocation recipeUnholyCloak;
	public static ResourceLocation recipeBalanceCloak;
	public static ResourceLocation recipeCraftingHalo;
	public static List<ResourceLocation> recipesLensFlash;
	public static ResourceLocation recipePrism;
	public static ResourceLocation recipeDreamwoodTwig;
	public static ResourceLocation recipeMonocle;
	public static ResourceLocation recipeClip;
	public static ResourceLocation recipeCobbleRod;
	public static ResourceLocation recipeSmeltRod;
	public static ResourceLocation recipeWorldSeed;
	public static ResourceLocation recipeSpellCloth;
	public static ResourceLocation recipeThornChakram;
	public static ResourceLocation recipeDirtPathSlab;
	public static List<ResourceLocation> recipesPatterns;
	public static ResourceLocation recipeGaiaIngot;
	public static ResourceLocation recipeCorporeaSpark;
	public static ResourceLocation recipeMasterCorporeaSpark;
	public static ResourceLocation recipeCorporeaIndex;
	public static ResourceLocation recipeCorporeaFunnel;
	public static ResourceLocation recipeCorporeaInterceptor;
	public static ResourceLocation recipeLivingwoodBow;
	public static ResourceLocation recipeCrystalBow;
	public static List<ResourceLocation> recipesCosmeticItems;
	public static List<ResourceLocation> recipesMushrooms;
	public static ResourceLocation recipeSwapRing;
	public static ResourceLocation recipeSnowHorn;
	public static ResourceLocation recipeFlowerBag;
	public static ResourceLocation recipePhantomInk;
	public static ResourceLocation recipePoolCart;
	public static ResourceLocation recipePump;
	public static List<ResourceLocation> recipesPetalsDouble;
	public static ResourceLocation recipeKeepIvy;
	public static ResourceLocation recipeBlackHoleTalisman;
	public static List<ResourceLocation> recipe18StoneChisel;
	public static ResourceLocation recipeBlazeBlock;
	public static List<ResourceLocation> recipesAltarMeta;
	public static ResourceLocation recipeCorporeaCrystalCube;
	public static ResourceLocation recipeTemperanceStone;
	public static ResourceLocation recipeIncenseStick;
	public static ResourceLocation recipeIncensePlate;
	public static ResourceLocation recipeTerraAxe;
	public static ResourceLocation recipeHourglass;
	public static ResourceLocation recipeGhostRail;
	public static ResourceLocation recipeCanopyDrum;
	public static ResourceLocation recipeSparkChanger;
	public static ResourceLocation recipeCocoon;
	public static ResourceLocation recipeLuminizer;
	public static ResourceLocation recipeDetectorLuminizer;
	public static ResourceLocation recipeLuminizerLauncher;
	public static ResourceLocation recipeObedienceStick;
	public static ResourceLocation recipeCacophonium;
	public static ResourceLocation recipeManaBomb;
	public static ResourceLocation recipeCobweb;
	public static ResourceLocation recipeSlimeBottle;
	public static ResourceLocation recipeStarSword;
	public static ResourceLocation recipeExchangeRod;
	public static ResourceLocation recipeGreaterMagnetRing;
	public static ResourceLocation recipeFireChakram;
	public static ResourceLocation recipeThunderSword;
	public static ResourceLocation recipeBellows;
	public static ResourceLocation recipeManaweaveCloth;
	public static ResourceLocation recipeManaweaveHelm;
	public static ResourceLocation recipeManaweaveChest;
	public static ResourceLocation recipeManaweaveLegs;
	public static ResourceLocation recipeManaweaveBoots;
	public static ResourceLocation recipeBifrost;
	public static ResourceLocation recipeShimmerrock;
	public static ResourceLocation recipeShimmerwoodPlanks;
	public static ResourceLocation recipeAutocraftingHalo;
	public static List<ResourceLocation> recipesPavement;
	public static ResourceLocation recipeCellBlock;
	public static ResourceLocation recipeCorporeaRetainer;
	public static ResourceLocation recipeTeruTeruBozu;
	public static ResourceLocation recipeAvatar;
	public static ResourceLocation recipeSextant;
	public static List<ResourceLocation> recipesAltGrassSeeds;
	public static ResourceLocation recipeSpeedUpBelt;
	public static ResourceLocation recipeBaubleCase;
	public static ResourceLocation recipeDodgeRing;
	public static ResourceLocation recipeAnimatedTorch;
	public static ResourceLocation recipeForkLuminizer;
	public static ResourceLocation recipeToggleLuminizer;
	public static ResourceLocation recipeInvisibilityCloak;
	public static ResourceLocation recipeCloudPendant;
	public static ResourceLocation recipeSuperCloudPendant;
	public static ResourceLocation recipeThirdEye;
	public static ResourceLocation recipeAstrolabe;
	public static ResourceLocation recipeGoddessCharm;

	// Garden of Glass
	public static ResourceLocation recipeRootToSapling;
	public static ResourceLocation recipeRootToFertilizer;
	public static ResourceLocation recipePebbleCobblestone;
	public static ResourceLocation recipeMagmaToSlimeball;
	public static ResourceLocation recipeFelPumpkin;
	public static ResourceLocation recipeEndPortal;

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
		*/

		// Terrasteel Armor Recipes
		RecipeSorter.register("botania:armorUpgrade", ArmorUpgradeRecipe.class, RecipeSorter.Category.SHAPED, "");
		r.register(new ArmorUpgradeRecipe(new ItemStack(ModItems.terrasteelHelmRevealing),
				"TRT", "SAS", " S ",
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'S', LibOreDict.TERRA_STEEL,
				'R', LibOreDict.RUNE[4],
				'A', new ItemStack(ModItems.manasteelHelmRevealing)).setRegistryName(ModItems.terrasteelHelmRevealing.getRegistryName()));

		r.register(new ArmorUpgradeRecipe(new ItemStack(ModItems.terrasteelHelm),
				"TRT", "SAS", " S ",
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'S', LibOreDict.TERRA_STEEL,
				'R', LibOreDict.RUNE[4],
				'A', new ItemStack(ModItems.manasteelHelm)).setRegistryName(ModItems.terrasteelHelm.getRegistryName()));

		r.register(new ArmorUpgradeRecipe(new ItemStack(ModItems.terrasteelChest),
				"TRT", "SAS", " S ",
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'S', LibOreDict.TERRA_STEEL,
				'R', LibOreDict.RUNE[5],
				'A', new ItemStack(ModItems.manasteelChest)).setRegistryName(ModItems.terrasteelChest.getRegistryName()));

		r.register(new ArmorUpgradeRecipe(new ItemStack(ModItems.terrasteelLegs),
				"TRT", "SAS", " S ",
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'S', LibOreDict.TERRA_STEEL,
				'R', LibOreDict.RUNE[6],
				'A', new ItemStack(ModItems.manasteelLegs)).setRegistryName(ModItems.terrasteelLegs.getRegistryName()));

		r.register(new ArmorUpgradeRecipe(new ItemStack(ModItems.terrasteelBoots),
				"TRT", "SAS", " S ",
				'T', LibOreDict.LIVINGWOOD_TWIG,
				'S', LibOreDict.TERRA_STEEL,
				'R', LibOreDict.RUNE[7],
				'A', new ItemStack(ModItems.manasteelBoots)).setRegistryName(ModItems.terrasteelBoots.getRegistryName()));

		RecipeSorter.register("botania:manaUpgrade", ManaUpgradeRecipe.class, RecipeSorter.Category.SHAPED, "");
		r.register(new ManaUpgradeRecipe(new ItemStack(ModItems.manaRing),
				"TI ", "I I", " I ",
				'T', new ItemStack(ModItems.manaTablet, 1, Short.MAX_VALUE),
				'I', LibOreDict.MANA_STEEL).setRegistryName(ModItems.manaRing.getRegistryName()));

		RecipeSorter.register("botania:manaUpgradeShapeless", ShapelessManaUpgradeRecipe.class, RecipeSorter.Category.SHAPELESS, "");
		r.register(new ShapelessManaUpgradeRecipe(new ItemStack(ModItems.manaRingGreater), LibOreDict.TERRA_STEEL, new ItemStack(ModItems.manaRing, 1, Short.MAX_VALUE)).setRegistryName(ModItems.manaRingGreater.getRegistryName()));

		// Terra Shatterer Recipe
		r.register(new ManaUpgradeRecipe(new ItemStack(ModItems.terraPick),
				"ITI", "ILI", " L ",
				'T', new ItemStack(ModItems.manaTablet, 1, Short.MAX_VALUE),
				'I', LibOreDict.TERRA_STEEL,
				'L', LibOreDict.LIVINGWOOD_TWIG).setRegistryName(ModItems.terraPick.getRegistryName()));

		/*
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
		
		recipeLexicon = path("lexicon");
		recipesPetals = allOfGroup("petal");
		recipesDyes = allOfGroup("dye");
		recipesPetalBlocks = allOfGroup("petalblock");
		recipesReversePetalBlocks = allOfGroup("petal_block_deconstruct");
	}
	
	private static ResourceLocation path(String path) {
		return new ResourceLocation(LibMisc.MOD_ID, path);
	}

	private static List<ResourceLocation> allOfGroup(String group) {
		String jsonGroup = LibMisc.MOD_ID + ":" + group;
		List<ResourceLocation> result = new ArrayList<>();

		for (Map.Entry<ResourceLocation, IRecipe> e : ForgeRegistries.RECIPES.getEntries()) {
			if (jsonGroup.equals(e.getValue().getGroup())) {
				result.add(e.getKey());
			}
		}

		return result;
	}
}
