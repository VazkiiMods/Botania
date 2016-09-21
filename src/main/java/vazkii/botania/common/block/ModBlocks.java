/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 14, 2014, 5:17:55 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.corporea.BlockCorporeaCrystalCube;
import vazkii.botania.common.block.corporea.BlockCorporeaFunnel;
import vazkii.botania.common.block.corporea.BlockCorporeaIndex;
import vazkii.botania.common.block.corporea.BlockCorporeaInterceptor;
import vazkii.botania.common.block.corporea.BlockCorporeaRetainer;
import vazkii.botania.common.block.decor.BlockBlaze;
import vazkii.botania.common.block.decor.BlockBuriedPetals;
import vazkii.botania.common.block.decor.BlockCustomBrick;
import vazkii.botania.common.block.decor.BlockElfGlass;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.decor.BlockManaFlame;
import vazkii.botania.common.block.decor.BlockManaGlass;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.block.decor.BlockPetalBlock;
import vazkii.botania.common.block.decor.BlockShimmerrock;
import vazkii.botania.common.block.decor.BlockShimmerwoodPlanks;
import vazkii.botania.common.block.decor.BlockShinyFlower;
import vazkii.botania.common.block.decor.BlockStarfield;
import vazkii.botania.common.block.decor.BlockTinyPotato;
import vazkii.botania.common.block.dispenser.BehaviourFelPumpkin;
import vazkii.botania.common.block.dispenser.BehaviourPoolMinecart;
import vazkii.botania.common.block.dispenser.BehaviourWand;
import vazkii.botania.common.block.dispenser.SeedBehaviours;
import vazkii.botania.common.block.mana.BlockAlchemyCatalyst;
import vazkii.botania.common.block.mana.BlockBellows;
import vazkii.botania.common.block.mana.BlockBrewery;
import vazkii.botania.common.block.mana.BlockConjurationCatalyst;
import vazkii.botania.common.block.mana.BlockDistributor;
import vazkii.botania.common.block.mana.BlockEnchanter;
import vazkii.botania.common.block.mana.BlockForestDrum;
import vazkii.botania.common.block.mana.BlockManaDetector;
import vazkii.botania.common.block.mana.BlockManaVoid;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.block.mana.BlockPrism;
import vazkii.botania.common.block.mana.BlockPump;
import vazkii.botania.common.block.mana.BlockRFGenerator;
import vazkii.botania.common.block.mana.BlockRuneAltar;
import vazkii.botania.common.block.mana.BlockSpawnerClaw;
import vazkii.botania.common.block.mana.BlockSpreader;
import vazkii.botania.common.block.mana.BlockTerraPlate;
import vazkii.botania.common.block.mana.BlockTurntable;
import vazkii.botania.common.block.string.BlockRedStringComparator;
import vazkii.botania.common.block.string.BlockRedStringContainer;
import vazkii.botania.common.block.string.BlockRedStringDispenser;
import vazkii.botania.common.block.string.BlockRedStringFertilizer;
import vazkii.botania.common.block.string.BlockRedStringInterceptor;
import vazkii.botania.common.block.string.BlockRedStringRelay;
import vazkii.botania.common.block.subtile.SubTileManastar;
import vazkii.botania.common.block.subtile.SubTilePureDaisy;
import vazkii.botania.common.block.subtile.functional.SubTileAgricarnation;
import vazkii.botania.common.block.subtile.functional.SubTileBellethorn;
import vazkii.botania.common.block.subtile.functional.SubTileBergamute;
import vazkii.botania.common.block.subtile.functional.SubTileBubbell;
import vazkii.botania.common.block.subtile.functional.SubTileClayconia;
import vazkii.botania.common.block.subtile.functional.SubTileDaffomill;
import vazkii.botania.common.block.subtile.functional.SubTileDreadthorn;
import vazkii.botania.common.block.subtile.functional.SubTileExoflame;
import vazkii.botania.common.block.subtile.functional.SubTileFallenKanade;
import vazkii.botania.common.block.subtile.functional.SubTileHeiseiDream;
import vazkii.botania.common.block.subtile.functional.SubTileHopperhock;
import vazkii.botania.common.block.subtile.functional.SubTileHyacidus;
import vazkii.botania.common.block.subtile.functional.SubTileJadedAmaranthus;
import vazkii.botania.common.block.subtile.functional.SubTileJiyuulia;
import vazkii.botania.common.block.subtile.functional.SubTileLoonuim;
import vazkii.botania.common.block.subtile.functional.SubTileMarimorphosis;
import vazkii.botania.common.block.subtile.functional.SubTileMedumone;
import vazkii.botania.common.block.subtile.functional.SubTileOrechid;
import vazkii.botania.common.block.subtile.functional.SubTileOrechidIgnem;
import vazkii.botania.common.block.subtile.functional.SubTilePollidisiac;
import vazkii.botania.common.block.subtile.functional.SubTileRannuncarpus;
import vazkii.botania.common.block.subtile.functional.SubTileSolegnolia;
import vazkii.botania.common.block.subtile.functional.SubTileSpectranthemum;
import vazkii.botania.common.block.subtile.functional.SubTileTangleberrie;
import vazkii.botania.common.block.subtile.functional.SubTileTigerseye;
import vazkii.botania.common.block.subtile.functional.SubTileVinculotus;
import vazkii.botania.common.block.subtile.generating.SubTileArcaneRose;
import vazkii.botania.common.block.subtile.generating.SubTileDandelifeon;
import vazkii.botania.common.block.subtile.generating.SubTileEndoflame;
import vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum;
import vazkii.botania.common.block.subtile.generating.SubTileGourmaryllis;
import vazkii.botania.common.block.subtile.generating.SubTileHydroangeas;
import vazkii.botania.common.block.subtile.generating.SubTileKekimurus;
import vazkii.botania.common.block.subtile.generating.SubTileMunchdew;
import vazkii.botania.common.block.subtile.generating.SubTileNarslimmus;
import vazkii.botania.common.block.subtile.generating.SubTileRafflowsia;
import vazkii.botania.common.block.subtile.generating.SubTileSpectrolus;
import vazkii.botania.common.block.subtile.generating.SubTileThermalily;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileAvatar;
import vazkii.botania.common.block.tile.TileBifrost;
import vazkii.botania.common.block.tile.TileBrewery;
import vazkii.botania.common.block.tile.TileCacophonium;
import vazkii.botania.common.block.tile.TileCell;
import vazkii.botania.common.block.tile.TileCocoon;
import vazkii.botania.common.block.tile.TileCraftCrate;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.block.tile.TileEnderEye;
import vazkii.botania.common.block.tile.TileFakeAir;
import vazkii.botania.common.block.tile.TileFloatingFlower;
import vazkii.botania.common.block.tile.TileFloatingSpecialFlower;
import vazkii.botania.common.block.tile.TileForestEye;
import vazkii.botania.common.block.tile.TileGaiaHead;
import vazkii.botania.common.block.tile.TileHourglass;
import vazkii.botania.common.block.tile.TileIncensePlate;
import vazkii.botania.common.block.tile.TileLightRelay;
import vazkii.botania.common.block.tile.TileManaFlame;
import vazkii.botania.common.block.tile.TileOpenCrate;
import vazkii.botania.common.block.tile.TilePlatform;
import vazkii.botania.common.block.tile.TilePylon;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.TileSparkChanger;
import vazkii.botania.common.block.tile.TileSpawnerClaw;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.block.tile.TileStarfield;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.block.tile.TileTeruTeruBozu;
import vazkii.botania.common.block.tile.TileTinyPlanet;
import vazkii.botania.common.block.tile.TileTinyPotato;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.block.tile.corporea.TileCorporeaFunnel;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.block.tile.corporea.TileCorporeaInterceptor;
import vazkii.botania.common.block.tile.corporea.TileCorporeaRetainer;
import vazkii.botania.common.block.tile.mana.TileBellows;
import vazkii.botania.common.block.tile.mana.TileDistributor;
import vazkii.botania.common.block.tile.mana.TileManaDetector;
import vazkii.botania.common.block.tile.mana.TileManaVoid;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.block.tile.mana.TilePrism;
import vazkii.botania.common.block.tile.mana.TilePump;
import vazkii.botania.common.block.tile.mana.TileRFGenerator;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.block.tile.mana.TileTurntable;
import vazkii.botania.common.block.tile.string.TileRedStringComparator;
import vazkii.botania.common.block.tile.string.TileRedStringContainer;
import vazkii.botania.common.block.tile.string.TileRedStringDispenser;
import vazkii.botania.common.block.tile.string.TileRedStringFertilizer;
import vazkii.botania.common.block.tile.string.TileRedStringInterceptor;
import vazkii.botania.common.block.tile.string.TileRedStringRelay;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibOreDict;

public final class ModBlocks {

	public static Block flower;
	public static Block altar;
	public static Block livingrock;
	public static Block livingwood;
	public static Block specialFlower;
	public static Block spreader;
	public static Block pool;
	public static Block runeAltar;
	public static Block pylon;
	public static Block pistonRelay;
	public static Block distributor;
	public static Block manaVoid;
	public static Block manaDetector;
	public static Block enchanter;
	public static Block turntable;
	public static Block tinyPlanet;
	public static Block alchemyCatalyst;
	public static Block openCrate;
	public static Block forestEye;
	public static Block storage;
	public static Block forestDrum;
	public static Block shinyFlower;
	public static Block platform;
	public static Block alfPortal;
	public static Block dreamwood;
	public static Block conjurationCatalyst;
	public static Block bifrost;
	public static Block solidVines;
	public static Block buriedPetals;
	public static Block floatingFlower;
	public static Block tinyPotato;
	public static Block spawnerClaw;
	public static Block customBrick;
	public static Block enderEye;
	public static Block starfield;
	public static Block rfGenerator;
	public static Block elfGlass;
	public static Block brewery;
	public static Block manaGlass;
	public static Block terraPlate;
	public static Block redStringContainer;
	public static Block redStringDispenser;
	public static Block redStringFertilizer;
	public static Block redStringComparator;
	public static Block redStringRelay;
	public static Block floatingSpecialFlower;
	public static Block manaFlame;
	public static Block prism;
	public static Block enchantedSoil;
	public static Block petalBlock;
	public static Block corporeaIndex;
	public static Block corporeaFunnel;
	public static Block mushroom;
	public static Block pump;
	public static Block doubleFlower1;
	public static Block doubleFlower2;
	public static Block fakeAir;
	public static Block blazeBlock;
	public static Block corporeaInterceptor;
	public static Block corporeaCrystalCube;
	public static Block incensePlate;
	public static Block hourglass;
	public static Block ghostRail;
	public static Block sparkChanger;
	public static Block root;
	public static Block felPumpkin;
	public static Block cocoon;
	public static Block lightRelay;
	public static Block lightLauncher;
	public static Block manaBomb;
	public static Block cacophonium;
	public static Block bellows;
	public static Block bifrostPerm;
	public static Block cellBlock;
	public static Block redStringInterceptor;
	public static Block gaiaHead;
	public static Block corporeaRetainer;
	public static Block teruTeruBozu;
	public static Block shimmerrock;
	public static Block shimmerwoodPlanks;
	public static Block avatar;
	public static Block altGrass;

	public static void init() {
		flower = new BlockModFlower();
		altar = new BlockAltar();
		livingrock = new BlockLivingrock();
		livingwood = new BlockLivingwood();
		specialFlower = new BlockSpecialFlower();
		spreader = new BlockSpreader();
		pool = new BlockPool();
		runeAltar = new BlockRuneAltar();
		pylon = new BlockPylon();
		pistonRelay = new BlockPistonRelay();
		distributor = new BlockDistributor();
		manaVoid = new BlockManaVoid();
		manaDetector = new BlockManaDetector();
		enchanter = new BlockEnchanter();
		turntable = new BlockTurntable();
		tinyPlanet = new BlockTinyPlanet();
		alchemyCatalyst = new BlockAlchemyCatalyst();
		openCrate = new BlockOpenCrate();
		forestEye = new BlockForestEye();
		storage = new BlockStorage();
		forestDrum = new BlockForestDrum();
		shinyFlower = new BlockShinyFlower();
		platform = new BlockPlatform();
		alfPortal = new BlockAlfPortal();
		dreamwood = new BlockDreamwood();
		conjurationCatalyst = new BlockConjurationCatalyst();
		bifrost = new BlockBifrost();
		solidVines = new BlockSolidVines();
		buriedPetals = new BlockBuriedPetals();
		floatingFlower = new BlockFloatingFlower();
		tinyPotato = new BlockTinyPotato();
		spawnerClaw = new BlockSpawnerClaw();
		customBrick = new BlockCustomBrick();
		enderEye = new BlockEnderEye();
		starfield = new BlockStarfield();
		rfGenerator	= new BlockRFGenerator();
		elfGlass = new BlockElfGlass();
		brewery = new BlockBrewery();
		manaGlass = new BlockManaGlass();
		terraPlate = new BlockTerraPlate();
		redStringContainer = new BlockRedStringContainer();
		redStringDispenser = new BlockRedStringDispenser();
		redStringFertilizer = new BlockRedStringFertilizer();
		redStringComparator = new BlockRedStringComparator();
		redStringRelay = new BlockRedStringRelay();
		floatingSpecialFlower = new BlockFloatingSpecialFlower();
		manaFlame = new BlockManaFlame();
		prism = new BlockPrism();
		enchantedSoil = new BlockEnchantedSoil();
		petalBlock = new BlockPetalBlock();
		corporeaIndex = new BlockCorporeaIndex();
		corporeaFunnel = new BlockCorporeaFunnel();
		mushroom = new BlockModMushroom();
		pump = new BlockPump();
		doubleFlower1 = new BlockModDoubleFlower1();
		doubleFlower2 = new BlockModDoubleFlower2();
		fakeAir = new BlockFakeAir();
		blazeBlock = new BlockBlaze();
		corporeaInterceptor = new BlockCorporeaInterceptor();
		corporeaCrystalCube = new BlockCorporeaCrystalCube();
		incensePlate = new BlockIncensePlate();
		hourglass = new BlockHourglass();
		ghostRail = new BlockGhostRail();
		sparkChanger = new BlockSparkChanger();
		root = new BlockRoot();
		felPumpkin = new BlockFelPumpkin();
		cocoon = new BlockCocoon();
		lightRelay = new BlockLightRelay();
		lightLauncher = new BlockLightLauncher();
		manaBomb = new BlockManaBomb();
		cacophonium = new BlockCacophonium();
		bellows = new BlockBellows();
		bifrostPerm = new BlockBifrostPerm();
		cellBlock = new BlockCell();
		redStringInterceptor = new BlockRedStringInterceptor();
		gaiaHead = new BlockGaiaHead();
		corporeaRetainer = new BlockCorporeaRetainer();
		teruTeruBozu = new BlockTeruTeruBozu();
		shimmerrock = new BlockShimmerrock();
		shimmerwoodPlanks = new BlockShimmerwoodPlanks();
		avatar = new BlockAvatar();
		altGrass = new BlockAltGrass();

		ModFluffBlocks.init();

		for(int i = 0; i < 16; i++)
			OreDictionary.registerOre(LibOreDict.FLOWER[i], new ItemStack(flower, 1, i));

		OreDictionary.registerOre(LibOreDict.LIVING_ROCK, livingrock);
		OreDictionary.registerOre(LibOreDict.LIVING_WOOD, livingwood);
		OreDictionary.registerOre(LibOreDict.DREAM_WOOD, dreamwood);

		for(int i = 0; i < 8; i++) {
			OreDictionary.registerOre(LibOreDict.DOUBLE_FLOWER[i], new ItemStack(doubleFlower1, 1, i));
			OreDictionary.registerOre(LibOreDict.DOUBLE_FLOWER[i + 8], new ItemStack(doubleFlower2, 1, i));
		}

		OreDictionary.registerOre(LibOreDict.BLAZE_BLOCK, blazeBlock);

		// Vanilla OreDict entries
		OreDictionary.registerOre("hardenedClay", new ItemStack(Blocks.HARDENED_CLAY, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("snowLayer", Blocks.SNOW_LAYER);
		OreDictionary.registerOre("mycelium", Blocks.MYCELIUM);
		OreDictionary.registerOre("podzol", new ItemStack(Blocks.DIRT, 1, 2));
		OreDictionary.registerOre("soulSand", Blocks.SOUL_SAND);
		OreDictionary.registerOre("ice", Blocks.ICE);
		OreDictionary.registerOre("slabCobblestone", new ItemStack(Blocks.STONE_SLAB, 1, 3));

		RecipeManaInfusion.alchemyState = alchemyCatalyst.getDefaultState();
		RecipeManaInfusion.conjurationState = conjurationCatalyst.getDefaultState();

		initTileEntities();
	}

	public static void addDispenserBehaviours() {
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.twigWand, new BehaviourWand());
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.poolMinecart, new BehaviourPoolMinecart());
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(ModBlocks.felPumpkin), new BehaviourFelPumpkin());
		
		SeedBehaviours.init();
	}

	private static void initTileEntities() {
		registerTile(TileAltar.class, LibBlockNames.ALTAR);
		registerTile(TileSpecialFlower.class, LibBlockNames.SPECIAL_FLOWER);
		registerTile(TileSpreader.class, LibBlockNames.SPREADER);
		registerTile(TilePool.class, LibBlockNames.POOL);
		registerTile(TileRuneAltar.class, LibBlockNames.RUNE_ALTAR);
		registerTile(TilePylon.class, LibBlockNames.PYLON);
		registerTile(TileDistributor.class, LibBlockNames.DISTRIBUTOR);
		registerTile(TileManaVoid.class, LibBlockNames.MANA_VOID);
		registerTile(TileManaDetector.class, LibBlockNames.MANA_DETECTOR);
		registerTile(TileEnchanter.class, LibBlockNames.ENCHANTER);
		registerTile(TileTurntable.class, LibBlockNames.TURNTABLE);
		registerTile(TileTinyPlanet.class, LibBlockNames.TINY_PLANET);
		registerTile(TileOpenCrate.class, LibBlockNames.OPEN_CRATE);
		registerTile(TileCraftCrate.class, LibBlockNames.CRAFT_CRATE);
		registerTile(TileForestEye.class, LibBlockNames.FOREST_EYE);
		registerTile(TilePlatform.class, LibBlockNames.PLATFORM);
		registerTile(TileAlfPortal.class, LibBlockNames.ALF_PORTAL);
		registerTile(TileBifrost.class, LibBlockNames.BIFROST);
		registerTile(TileFloatingFlower.class, LibBlockNames.MINI_ISLAND);
		registerTile(TileTinyPotato.class, LibBlockNames.TINY_POTATO);
		registerTile(TileSpawnerClaw.class, LibBlockNames.SPAWNER_CLAW);
		registerTile(TileEnderEye.class, LibBlockNames.ENDER_EYE_BLOCK);
		registerTile(TileStarfield.class, LibBlockNames.STARFIELD);
		registerTile(TileRFGenerator.class, LibBlockNames.RF_GENERATOR);
		registerTile(TileBrewery.class, LibBlockNames.BREWERY);
		registerTile(TileTerraPlate.class, LibBlockNames.TERRA_PLATE);
		registerTile(TileRedStringContainer.class, LibBlockNames.RED_STRING_CONTAINER);
		registerTile(TileRedStringDispenser.class, LibBlockNames.RED_STRING_DISPENSER);
		registerTile(TileRedStringFertilizer.class, LibBlockNames.RED_STRING_FERTILIZER);
		registerTile(TileRedStringComparator.class, LibBlockNames.RED_STRING_COMPARATOR);
		registerTile(TileRedStringRelay.class, LibBlockNames.RED_STRING_RELAY);
		registerTile(TileFloatingSpecialFlower.class, LibBlockNames.FLOATING_SPECIAL_FLOWER);
		registerTile(TileManaFlame.class, LibBlockNames.MANA_FLAME);
		registerTile(TilePrism.class, LibBlockNames.PRISM);
		registerTile(TileCorporeaIndex.class, LibBlockNames.CORPOREA_INDEX);
		registerTile(TileCorporeaFunnel.class, LibBlockNames.CORPOREA_FUNNEL);
		registerTile(TilePump.class, LibBlockNames.PUMP);
		registerTile(TileFakeAir.class, LibBlockNames.FAKE_AIR);
		registerTile(TileCorporeaInterceptor.class, LibBlockNames.CORPOREA_INTERCEPTOR);
		registerTile(TileCorporeaCrystalCube.class, LibBlockNames.CORPOREA_CRYSTAL_CUBE);
		registerTile(TileIncensePlate.class, LibBlockNames.INCENSE_PLATE);
		registerTile(TileHourglass.class, LibBlockNames.HOURGLASS);
		registerTile(TileSparkChanger.class, LibBlockNames.SPARK_CHANGER);
		registerTile(TileCocoon.class, LibBlockNames.COCOON);
		registerTile(TileLightRelay.class, LibBlockNames.LIGHT_RELAY);
		registerTile(TileCacophonium.class, LibBlockNames.CACOPHONIUM);
		registerTile(TileBellows.class, LibBlockNames.BELLOWS);
		registerTile(TileCell.class, LibBlockNames.CELL_BLOCK);
		registerTile(TileRedStringInterceptor.class, LibBlockNames.RED_STRING_INTERCEPTOR);
		registerTile(TileGaiaHead.class, LibBlockNames.GAIA_HEAD);
		registerTile(TileCorporeaRetainer.class, LibBlockNames.CORPOREA_RETAINER);
		registerTile(TileTeruTeruBozu.class, LibBlockNames.TERU_TERU_BOZU);
		registerTile(TileAvatar.class, LibBlockNames.AVATAR);

		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_PUREDAISY, SubTilePureDaisy.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_MANASTAR, SubTileManastar.class);

		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_HYDROANGEAS, SubTileHydroangeas.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_ENDOFLAME, SubTileEndoflame.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_THERMALILY, SubTileThermalily.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_ARCANE_ROSE, SubTileArcaneRose.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_MUNCHDEW, SubTileMunchdew.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_ENTROPINNYUM, SubTileEntropinnyum.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_KEKIMURUS, SubTileKekimurus.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_GOURMARYLLIS, SubTileGourmaryllis.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_NARSLIMMUS, SubTileNarslimmus.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_SPECTROLUS, SubTileSpectrolus.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_DANDELIFEON, SubTileDandelifeon.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_RAFFLOWSIA, SubTileRafflowsia.class);

		registerSubTileWithMini(LibBlockNames.SUBTILE_BELLETHORN, SubTileBellethorn.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_DREADTHORN, SubTileDreadthorn.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_HEISEI_DREAM, SubTileHeiseiDream.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_TIGERSEYE, SubTileTigerseye.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_JADED_AMARANTHUS, SubTileJadedAmaranthus.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_ORECHID, SubTileOrechid.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_ORECHID_IGNEM, SubTileOrechidIgnem.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_FALLEN_KANADE, SubTileFallenKanade.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_EXOFLAME, SubTileExoflame.class);
		registerSubTileWithMini(LibBlockNames.SUBTILE_AGRICARNATION, SubTileAgricarnation.class);
		registerSubTileWithMini(LibBlockNames.SUBTILE_HOPPERHOCK, SubTileHopperhock.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_TANGLEBERRIE, SubTileTangleberrie.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_JIYUULIA, SubTileJiyuulia.class);
		registerSubTileWithMini(LibBlockNames.SUBTILE_RANNUNCARPUS, SubTileRannuncarpus.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_HYACIDUS, SubTileHyacidus.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_POLLIDISIAC, SubTilePollidisiac.class);
		registerSubTileWithMini(LibBlockNames.SUBTILE_CLAYCONIA, SubTileClayconia.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_LOONIUM, SubTileLoonuim.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_DAFFOMILL, SubTileDaffomill.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_VINCULOTUS, SubTileVinculotus.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_SPECTRANTHEMUM, SubTileSpectranthemum.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_MEDUMONE, SubTileMedumone.class);
		registerSubTileWithMini(LibBlockNames.SUBTILE_MARIMORPHOSIS, SubTileMarimorphosis.class);
		registerSubTileWithMini(LibBlockNames.SUBTILE_BUBBELL, SubTileBubbell.class);
		registerSubTileWithMini(LibBlockNames.SUBTILE_SOLEGNOLIA, SubTileSolegnolia.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_BERGAMUTE, SubTileBergamute.class);
	}

	public static void registerMultiparts() {
		if(Loader.isModLoaded("ForgeMultipart")) {
			try {
				Class clazz = Class.forName("vazkii.botania.common.integration.multipart.MultipartHandler");
				clazz.newInstance();
			} catch(Throwable e) {}
		}
	}

	private static void registerSubTileWithMini(String key, Class<? extends SubTileEntity> clazz) {
		BotaniaAPI.registerSubTile(key, clazz);

		for(Class innerClazz : clazz.getDeclaredClasses())
			if(innerClazz.getSimpleName().equals("Mini"))
				BotaniaAPI.registerMiniSubTile(key + "Chibi", innerClazz, key);
	}

	private static void registerTile(Class<? extends TileEntity> clazz, String key) {
		GameRegistry.registerTileEntity(clazz, LibResources.PREFIX_MOD + key);
	}

}
