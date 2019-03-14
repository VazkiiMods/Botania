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

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
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
import vazkii.botania.common.item.equipment.bauble.ItemBaubleCosmetic;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibOreDict;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class ModCraftingRecipes {

	public static ResourceLocation recipeLexicon;
	public static List<ResourceLocation> recipesPetals;
	public static List<ResourceLocation> recipesDyes;
	public static List<ResourceLocation> recipesPetalBlocks;
	public static List<ResourceLocation> recipesReversePetalBlocks;
	public static ResourceLocation recipePestleAndMortar;
	public static List<ResourceLocation> recipesTwigWand;
	public static ResourceLocation recipeApothecary;
	public static List<ResourceLocation> recipesSpreader;
	public static List<ResourceLocation> recipesManaLens;
	public static ResourceLocation recipePool;
	public static ResourceLocation recipePoolDiluted;
	public static ResourceLocation recipePoolFabulous;
	public static ResourceLocation recipeRuneAltar;
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
	public static ResourceLocation recipeManaTablet;
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
	public static ResourceLocation recipeKnockbackBelt;
	public static ResourceLocation recipeIcePendant;
	public static ResourceLocation recipeLavaPendant;
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
	public static ResourceLocation recipeLavenderQuartz;
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
	public static ResourceLocation recipeDreamwoodSpreader;
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
	public static ResourceLocation recipeLaputaShard;
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
	public static ResourceLocation recipeSpark;
	public static List<ResourceLocation> recipesSparkUpgrades;
	public static ResourceLocation recipeLeafHorn;
	public static ResourceLocation recipeDiviningRod;
	public static List<ResourceLocation> recipesWings;
	public static ResourceLocation recipeRFGenerator;
	public static ResourceLocation recipeGravityRod;
	public static ResourceLocation recipeUltraSpreader;
	public static List<ResourceLocation> recipeHelmetOfRevealing;
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

		for (ResourceLocation s : BotaniaAPI.getAllSubTiles()) {
			r.register(new SpecialFloatingFlowerRecipe(s).setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "floating_" + s.toString().replaceAll(":", "_"))));
		}

		// Terrasteel Armor Recipes
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

		r.register(new ManaUpgradeRecipe(new ItemStack(ModItems.manaRing),
				"TI ", "I I", " I ",
				'T', new ItemStack(ModItems.manaTablet, 1, Short.MAX_VALUE),
				'I', LibOreDict.MANA_STEEL).setRegistryName(ModItems.manaRing.getRegistryName()));

		r.register(new ShapelessManaUpgradeRecipe(new ItemStack(ModItems.manaRingGreater), LibOreDict.TERRA_STEEL, new ItemStack(ModItems.manaRing, 1, Short.MAX_VALUE)).setRegistryName(ModItems.manaRingGreater.getRegistryName()));

		// Terra Shatterer Recipe
		r.register(new ManaUpgradeRecipe(new ItemStack(ModItems.terraPick),
				"ITI", "ILI", " L ",
				'T', new ItemStack(ModItems.manaTablet, 1, Short.MAX_VALUE),
				'I', LibOreDict.TERRA_STEEL,
				'L', LibOreDict.LIVINGWOOD_TWIG).setRegistryName(ModItems.terraPick.getRegistryName()));

	}

	public static void init() {
		// Can't do this in RegistryEvent.Register event handler since it seems JSON recipes aren't loaded yet
		recipesPetals = allOfGroup(new ResourceLocation(LibMisc.MOD_ID, LibItemNames.PETAL));
		recipePestleAndMortar = ModItems.pestleAndMortar.getRegistryName();
		recipesDyes = allOfGroup(new ResourceLocation(LibMisc.MOD_ID, LibItemNames.DYE));
		recipeFertilizerPowder = gogPath("fertilizer_powder");
		recipeFerilizerDye = path("fertilizer_dye");
		recipesPetalsDouble = allOfGroup("petal_double");
		recipesPetalBlocks = allOfGroup("petal_block");
		recipesReversePetalBlocks = allOfGroup("petal_block_deconstruct");

		recipeApothecary = ModBlocks.defaultAltar.getRegistryName();
		recipeLexicon = ModItems.lexicon.getRegistryName();
		recipesTwigWand = allOfGroup(ModItems.twigWand.getRegistryName());
		recipeLivingwoodTwig = ModItems.livingwoodTwig.getRegistryName();
		recipeRuneAltar = ModBlocks.runeAltar.getRegistryName();
		recipeTerraPlate = ModBlocks.terraPlate.getRegistryName();
		recipeFlowerBag = ModItems.flowerBag.getRegistryName();

		if(Botania.gardenOfGlassLoaded) {
			recipeRootToSapling = gogPath("root_to_sapling");
			recipeRootToFertilizer = gogPath("root_to_fertilizer");
			recipePebbleCobblestone = gogPath("pebble_to_cobblestone");
			recipeMagmaToSlimeball = gogPath("magma_cream_to_slime_ball");
			recipeEndPortal = gogPath("end_portal_frame");
		}

		recipesSpreader = allOfGroup("spreader");

		recipePool = ModBlocks.manaPool.getRegistryName();
		recipePoolDiluted = ModBlocks.dilutedPool.getRegistryName();
		recipePoolFabulous = ModBlocks.fabulousPool.getRegistryName();
		recipeCobweb = path("web");

		recipeSpark = ModItems.spark.getRegistryName();
		recipesSparkUpgrades = allOfGroup("spark_upgrade");
		recipeRFGenerator = ModBlocks.rfGenerator.getRegistryName();

		ResourceLocation name = ModItems.lensNormal.getRegistryName();
		recipesManaLens = ImmutableList.of(path(name.getPath() + "_glass_block"), path(name.getPath() + "_glass_pane"));
		recipeLensVelocity = ModItems.lensSpeed.getRegistryName();
		recipeLensPotency = ModItems.lensPower.getRegistryName();
		recipeLensResistance = ModItems.lensTime.getRegistryName();
		recipeLensEfficiency = ModItems.lensEfficiency.getRegistryName();
		recipeLensBounce = ModItems.lensBounce.getRegistryName();
		recipeLensGravity = ModItems.lensGravity.getRegistryName();
		recipeLensBore = ModItems.lensMine.getRegistryName();
		recipeLensDamaging = ModItems.lensDamage.getRegistryName();
		recipeLensPhantom = ModItems.lensPhantom.getRegistryName();
		recipeLensMagnet = ModItems.lensMagnet.getRegistryName();
		recipeLensExplosive = ModItems.lensExplosive.getRegistryName();
		recipeLensInfluence = ModItems.lensInfluence.getRegistryName();
		recipeLensWeight = ModItems.lensWeight.getRegistryName();
		recipeLensPaint = ModItems.lensPaint.getRegistryName();
		recipeLensFire = ModItems.lensFire.getRegistryName();
		recipeLensPiston = ModItems.lensPiston.getRegistryName();
		name = ModItems.lensLight.getRegistryName();
		recipesLensFlash = ImmutableList.of(name, path(name.getPath() + "_alt"));
		recipeLensWarp = ModItems.lensWarp.getRegistryName();
		recipeLensRedirect = ModItems.lensRedirect.getRegistryName();
		recipeLensFirework = ModItems.lensFirework.getRegistryName();
		recipeLensFlare = ModItems.lensFlare.getRegistryName();
		recipeLensMessenger = ModItems.lensMessenger.getRegistryName();
		recipeLensTripwire = ModItems.lensTripwire.getRegistryName();

		recipeDistributor = ModBlocks.distributor.getRegistryName();
		recipeManaVoid = ModBlocks.manaVoid.getRegistryName();
		recipeManaTablet = ModItems.manaTablet.getRegistryName();
		recipeManaMirror = ModItems.manaMirror.getRegistryName();
		recipeManaDetector = ModBlocks.manaDetector.getRegistryName();
		recipeRedstoneSpreader = ModBlocks.redstoneSpreader.getRegistryName();
		recipeDreamwoodSpreader = ModBlocks.elvenSpreader.getRegistryName();
		recipeUltraSpreader = ModBlocks.gaiaSpreader.getRegistryName();
		recipeHelmetOfRevealing = allOfGroup("helm_revealing");
		recipePrism = ModBlocks.prism.getRegistryName();
		recipePoolCart = ModItems.poolMinecart.getRegistryName();
		recipePump = ModBlocks.pump.getRegistryName();
		recipeSparkChanger = ModBlocks.sparkChanger.getRegistryName();
		recipeBellows = ModBlocks.bellows.getRegistryName();
		recipeRedstoneRoot = ModItems.redstoneRoot.getRegistryName();
		recipesAltarMeta = allOfGroup("metamorphic_apothecary");
		recipePylon = ModBlocks.manaPylon.getRegistryName();
		recipeTurntable = ModBlocks.turntable.getRegistryName();
		recipeAlchemyCatalyst = ModBlocks.alchemyCatalyst.getRegistryName();
		recipeOpenCrate = ModBlocks.openCrate.getRegistryName();
		recipeForestEye = ModBlocks.forestEye.getRegistryName();
		recipeForestDrum = ModBlocks.wildDrum.getRegistryName();
		recipePlatform = ModBlocks.abstrusePlatform.getRegistryName();
		recipeConjurationCatalyst = ModBlocks.conjurationCatalyst.getRegistryName();
		recipeSpectralPlatform = ModBlocks.spectralPlatform.getRegistryName();
		recipeGatherDrum = ModBlocks.gatheringDrum.getRegistryName();
		recipePlaceholder = ModItems.placeholder.getRegistryName();
		recipeCraftCrate = ModBlocks.craftCrate.getRegistryName();
		recipesPatterns = allOfGroup("craft_pattern");
		recipeBrewery = ModBlocks.brewery.getRegistryName();
		recipeVial = ModItems.vial.getRegistryName();
		recipeFlask = ModItems.flask.getRegistryName();
		recipeIncenseStick = ModItems.incenseStick.getRegistryName();
		recipeIncensePlate = ModBlocks.incensePlate.getRegistryName();
		recipeHourglass = ModBlocks.hourglass.getRegistryName();
		recipeGhostRail = ModBlocks.ghostRail.getRegistryName();
		recipeCanopyDrum = ModBlocks.canopyDrum.getRegistryName();
		recipeCocoon = gogPath("cocoon");
		recipeManaBomb = ModBlocks.manaBomb.getRegistryName();
		recipeTeruTeruBozu = ModBlocks.teruTeruBozu.getRegistryName();
		recipeAvatar = ModBlocks.avatar.getRegistryName();
		recipeFelPumpkin = ModBlocks.felPumpkin.getRegistryName();
		recipeAnimatedTorch = ModBlocks.animatedTorch.getRegistryName();
		recipeManaBlaster = ModItems.manaGun.getRegistryName();
		recipesAltGrassSeeds = allOfGroup(new ResourceLocation(LibMisc.MOD_ID, LibItemNames.GRASS_SEEDS));
		recipeDirtRod = ModItems.dirtRod.getRegistryName();
		recipeTerraformRod = ModItems.terraformRod.getRegistryName();
		recipeManasteelPick = ModItems.manasteelPick.getRegistryName();
		recipeManasteelShovel = ModItems.manasteelShovel.getRegistryName();
		recipeManasteelAxe = ModItems.manasteelAxe.getRegistryName();
		recipeManasteelShears = ModItems.manasteelShears.getRegistryName();
		recipeManasteelSword = ModItems.manasteelSword.getRegistryName();
		recipeManasteelHelm = ModItems.manasteelHelm.getRegistryName();
		recipeManasteelChest = ModItems.manasteelChest.getRegistryName();
		recipeManasteelLegs = ModItems.manasteelLegs.getRegistryName();
		recipeManasteelBoots = ModItems.manasteelBoots.getRegistryName();
		recipeTerrasteelHelm = ModItems.terrasteelHelm.getRegistryName();
		recipeTerrasteelChest = ModItems.terrasteelChest.getRegistryName();
		recipeTerrasteelLegs = ModItems.terrasteelLegs.getRegistryName();
		recipeTerrasteelBoots = ModItems.terrasteelBoots.getRegistryName();
		recipeGrassHorn = ModItems.grassHorn.getRegistryName();
		recipeLeafHorn = ModItems.leavesHorn.getRegistryName();
		recipeSnowHorn = ModItems.snowHorn.getRegistryName();
		recipeTerraSword = ModItems.terraSword.getRegistryName();
		recipeTerraPick = ModItems.terraPick.getRegistryName();
		recipeWaterRod = ModItems.waterRod.getRegistryName();
		recipeElementiumPick = ModItems.elementiumPick.getRegistryName();
		recipeElementiumShovel = ModItems.elementiumShovel.getRegistryName();
		recipeElementiumAxe = ModItems.elementiumAxe.getRegistryName();
		recipeElementiumShears = ModItems.elementiumShears.getRegistryName();
		recipeElementiumSword = ModItems.elementiumSword.getRegistryName();
		recipeElementiumHelm = ModItems.elementiumHelm.getRegistryName();
		recipeElementiumChest = ModItems.elementiumChest.getRegistryName();
		recipeElementiumLegs = ModItems.elementiumLegs.getRegistryName();
		recipeElementiumBoots = ModItems.elementiumBoots.getRegistryName();
		recipeOpenBucket = ModItems.openBucket.getRegistryName();
		recipeRainbowRod = ModItems.rainbowRod.getRegistryName();
		recipeBifrost = ModBlocks.bifrostPerm.getRegistryName();
		recipeShimmerrock = ModBlocks.shimmerrock.getRegistryName();
		recipeShimmerwoodPlanks = ModBlocks.shimmerwoodPlanks.getRegistryName();
		recipeTornadoRod = ModItems.tornadoRod.getRegistryName();
		recipeFireRod = ModItems.fireRod.getRegistryName();
		recipeVineBall = ModItems.vineBall.getRegistryName();
		recipeSlingshot = ModItems.slingshot.getRegistryName();
		recipeLaputaShard = path("laputashard_0");
		recipesLaputaShardUpgrade = allOfGroup("laputashard_upgrade");
		recipeVirusZombie = ModItems.necroVirus.getRegistryName();
		recipeVirusSkeleton = ModItems.nullVirus.getRegistryName();
		recipeSkyDirtRod = ModItems.skyDirtRod.getRegistryName();
		recipeGlassPick = ModItems.glassPick.getRegistryName();
		recipeDiviningRod = ModItems.diviningRod.getRegistryName();
		recipeGravityRod = ModItems.gravityRod.getRegistryName();
		recipeMissileRod = ModItems.missileRod.getRegistryName();
		recipeCraftingHalo = ModItems.craftingHalo.getRegistryName();
		recipeClip = ModItems.clip.getRegistryName();
		recipeCobbleRod = ModItems.cobbleRod.getRegistryName();
		recipeSmeltRod = ModItems.smeltRod.getRegistryName();
		recipeWorldSeed = ModItems.worldSeed.getRegistryName();
		recipeSpellCloth = ModItems.spellCloth.getRegistryName();
		recipeThornChakram = ModItems.thornChakram.getRegistryName();
		recipeFireChakram = ModItems.flareChakram.getRegistryName();
		recipeLivingwoodBow = ModItems.livingwoodBow.getRegistryName();
		recipeCrystalBow = ModItems.crystalBow.getRegistryName();
		recipeTemperanceStone = ModItems.temperanceStone.getRegistryName();
		recipeTerraAxe = ModItems.terraAxe.getRegistryName();
		recipeObedienceStick = ModItems.obedienceStick.getRegistryName();
		recipeSlimeBottle = ModItems.slimeBottle.getRegistryName();
		recipeExchangeRod = ModItems.exchangeRod.getRegistryName();
		recipeManaweaveCloth = ModItems.manaweaveCloth.getRegistryName();
		recipeManaweaveHelm = ModItems.manaweaveHelm.getRegistryName();
		recipeManaweaveChest = ModItems.manaweaveChest.getRegistryName();
		recipeManaweaveLegs = ModItems.manaweaveLegs.getRegistryName();
		recipeManaweaveBoots = ModItems.manaweaveBoots.getRegistryName();
		recipeAutocraftingHalo = ModItems.autocraftingHalo.getRegistryName();
		recipeSextant = ModItems.sextant.getRegistryName();
		recipeAstrolabe = ModItems.astrolabe.getRegistryName();
		recipeEnderEyeBlock = ModBlocks.enderEye.getRegistryName();
		recipeEnderHand = ModItems.enderHand.getRegistryName();
		recipeEnderDagger = ModItems.enderDagger.getRegistryName();
		recipeSpawnerClaw = ModBlocks.spawnerClaw.getRegistryName();
		recipeRedString = ModItems.redString.getRegistryName();
		recipeRedStringContainer = ModBlocks.redStringContainer.getRegistryName();
		recipeRedStringDispenser = ModBlocks.redStringDispenser.getRegistryName();
		recipeRedStringFertilizer = ModBlocks.redStringFertilizer.getRegistryName();
		recipeRedStringComparator = ModBlocks.redStringComparator.getRegistryName();
		recipeRedStringRelay = ModBlocks.redStringRelay.getRegistryName();
		recipeRedStringInterceptor = ModBlocks.redStringInterceptor.getRegistryName();
		recipeFlightTiara = path("flighttiara_0");
		recipesWings = allOfGroup("flighttiara_wings");
		recipeCorporeaSpark = ModItems.corporeaSpark.getRegistryName();
		recipeMasterCorporeaSpark = ModItems.corporeaSparkMaster.getRegistryName();
		recipeCorporeaIndex = ModBlocks.corporeaIndex.getRegistryName();
		recipeCorporeaFunnel = ModBlocks.corporeaFunnel.getRegistryName();
		recipeCorporeaInterceptor = ModBlocks.corporeaInterceptor.getRegistryName();
		recipeSpawnerMover = ModItems.spawnerMover.getRegistryName();
		recipeKeepIvy = ModItems.keepIvy.getRegistryName();
		recipeBlackHoleTalisman = path("blackholetalisman_0");
		recipeCorporeaCrystalCube = ModBlocks.corporeaCrystalCube.getRegistryName();
		recipeLuminizer = path("lightrelay_0");
		recipeDetectorLuminizer = path("lightrelay_1");
		recipeToggleLuminizer = path("lightrelay_2");
		recipeForkLuminizer = path("lightrelay_3");
		recipeLuminizerLauncher = ModBlocks.lightLauncher.getRegistryName();
		recipeStarSword = ModItems.starSword.getRegistryName();
		recipeThunderSword = ModItems.thunderSword.getRegistryName();
		recipeCorporeaRetainer = ModBlocks.corporeaRetainer.getRegistryName();
		recipesCosmeticItems = IntStream.range(0, ItemBaubleCosmetic.SUBTYPES).mapToObj(i -> path("cosmetic_" + i)).collect(Collectors.toList());
		recipeTinyPlanet = ModItems.tinyPlanet.getRegistryName();
		recipeTinyPlanetBlock = ModBlocks.tinyPlanet.getRegistryName();
		recipeManaRing = ModItems.manaRing.getRegistryName();
		recipeAuraRing = ModItems.auraRing.getRegistryName();
		recipeGreaterManaRing = ModItems.manaRingGreater.getRegistryName();
		recipeGreaterAuraRing = ModItems.auraRingGreater.getRegistryName();
		recipeTravelBelt = ModItems.travelBelt.getRegistryName();
		recipeKnockbackBelt = ModItems.knockbackBelt.getRegistryName();
		recipeIcePendant = ModItems.icePendant.getRegistryName();
		recipeLavaPendant = ModItems.lavaPendant.getRegistryName();
		recipeWaterRing = ModItems.waterRing.getRegistryName();
		recipeMiningRing = ModItems.miningRing.getRegistryName();
		recipeMagnetRing = ModItems.magnetRing.getRegistryName();
		recipeGreaterMagnetRing = ModItems.magnetRingGreater.getRegistryName();
		recipeDivaCharm = ModItems.divaCharm.getRegistryName();
		recipePixieRing = ModItems.pixieRing.getRegistryName();
		recipeSuperTravelBelt = ModItems.superTravelBelt.getRegistryName();
		recipeReachRing = ModItems.reachRing.getRegistryName();
		recipeItemFinder = ModItems.itemFinder.getRegistryName();
		recipeSuperLavaPendant = ModItems.superLavaPendant.getRegistryName();
		recipeBloodPendant = ModItems.bloodPendant.getRegistryName();
		recipeHolyCloak = ModItems.holyCloak.getRegistryName();
		recipeUnholyCloak = ModItems.unholyCloak.getRegistryName();
		recipeBalanceCloak = ModItems.balanceCloak.getRegistryName();
		recipeMonocle = ModItems.monocle.getRegistryName();
		recipeSwapRing = ModItems.swapRing.getRegistryName();
		recipeSpeedUpBelt = ModItems.speedUpBelt.getRegistryName();
		recipeBaubleCase = ModItems.baubleBox.getRegistryName();
		recipeDodgeRing = ModItems.dodgeRing.getRegistryName();
		recipeInvisibilityCloak = ModItems.invisibilityCloak.getRegistryName();
		recipeCloudPendant = ModItems.cloudPendant.getRegistryName();
		recipeSuperCloudPendant = ModItems.superCloudPendant.getRegistryName();
		recipeThirdEye = ModItems.thirdEye.getRegistryName();
		recipeGoddessCharm = ModItems.goddessCharm.getRegistryName();
		recipeAlfPortal = ModBlocks.alfPortal.getRegistryName();
		recipeNaturaPylon = ModBlocks.naturaPylon.getRegistryName();
		recipeDreamwoodTwig = ModItems.dreamwoodTwig.getRegistryName();
		recipeGaiaPylon = ModBlocks.gaiaPylon.getRegistryName();
		recipeGaiaIngot = ModItems.gaiaIngot.getRegistryName();
		recipeLivingrockDecor1 = path("livingrock_1");
		recipeLivingrockDecor2 = path("livingrock_2");
		recipeLivingrockDecor3 = path("livingrock_3");
		recipeLivingrockDecor4 = path("livingrock_4");
		recipeLivingwoodDecor1 = path("livingwood_1");
		recipeLivingwoodDecor2 = path("livingwood_2");
		recipeLivingwoodDecor3 = path("livingwood_3");
		recipeLivingwoodDecor4 = path("livingwood_4");
		recipeLivingwoodDecor5 = path("livingwood_5");
		recipeDarkQuartz = ModItems.darkQuartz.getRegistryName();
		recipeBlazeQuartz = ModItems.blazeQuartz.getRegistryName();
		recipeLavenderQuartz = ModItems.lavenderQuartz.getRegistryName();
		recipeRedQuartz = ModItems.redQuartz.getRegistryName();
		recipeSunnyQuartz = ModItems.sunnyQuartz.getRegistryName();
		recipesShinyFlowers = allOfGroup(ModBlocks.shinyFlower.getRegistryName());
		recipesMiniIsland = allOfGroup(ModBlocks.floatingFlower.getRegistryName());
		recipeAzulejo = ModBlocks.azulejo0.getRegistryName();
		recipesAzulejoCycling = allOfGroup("azulejo_cycling");
		recipeStarfield = ModBlocks.starfield.getRegistryName();
		recipesMushrooms = allOfGroup(ModBlocks.mushroom.getRegistryName());
		recipePhantomInk = ModItems.phantomInk.getRegistryName();
		recipeBlazeBlock = gogPath("blazeblock");
		recipeCacophonium = ModItems.cacophonium.getRegistryName();
		recipesPavement = allOfGroup(ModFluffBlocks.pavement.getRegistryName());
		recipeCellBlock = ModBlocks.cellBlock.getRegistryName();

	}

	private static ResourceLocation gogPath(String path) {
		if(Botania.gardenOfGlassLoaded) {
			return new ResourceLocation(LibMisc.MOD_ID, "garden_of_glass/" + path);
		} else {
			return path(path);
		}
	}

	private static ResourceLocation path(String path) {
		return new ResourceLocation(LibMisc.MOD_ID, path);
	}

	private static List<ResourceLocation> allOfGroup(String group) {
		return allOfGroup(new ResourceLocation(LibMisc.MOD_ID, group));
	}

	private static List<ResourceLocation> allOfGroup(ResourceLocation group) {
		String jsonGroup = group.toString();

		return ForgeRegistries.RECIPES.getEntries().stream()
				.filter(e -> jsonGroup.equals(e.getValue().getGroup()))
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
	}
}
