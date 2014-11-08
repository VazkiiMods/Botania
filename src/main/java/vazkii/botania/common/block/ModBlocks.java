/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
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
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.decor.BlockBuriedPetals;
import vazkii.botania.common.block.decor.BlockCustomBrick;
import vazkii.botania.common.block.decor.BlockElfGlass;
import vazkii.botania.common.block.decor.BlockManaBeacon;
import vazkii.botania.common.block.decor.BlockManaGlass;
import vazkii.botania.common.block.decor.BlockMiniIsland;
import vazkii.botania.common.block.decor.BlockPrismarine;
import vazkii.botania.common.block.decor.BlockReeds;
import vazkii.botania.common.block.decor.BlockSeaLamp;
import vazkii.botania.common.block.decor.BlockShinyFlower;
import vazkii.botania.common.block.decor.BlockStarfield;
import vazkii.botania.common.block.decor.BlockThatch;
import vazkii.botania.common.block.decor.BlockTinyPotato;
import vazkii.botania.common.block.decor.BlockUnstable;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartz;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartzSlab;
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartzStairs;
import vazkii.botania.common.block.decor.slabs.BlockModSlab;
import vazkii.botania.common.block.decor.slabs.BlockReedSlab;
import vazkii.botania.common.block.decor.slabs.BlockThatchSlab;
import vazkii.botania.common.block.decor.slabs.bricks.BlockCustomBrickSlab;
import vazkii.botania.common.block.decor.slabs.bricks.BlockSnowBrickSlab;
import vazkii.botania.common.block.decor.slabs.bricks.BlockSoulBrickSlab;
import vazkii.botania.common.block.decor.slabs.bricks.BlockTileSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockDreamwoodPlankSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockDreamwoodSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockLivingrockBrickSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockLivingrockSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockLivingwoodPlankSlab;
import vazkii.botania.common.block.decor.slabs.living.BlockLivingwoodSlab;
import vazkii.botania.common.block.decor.slabs.prismarine.BlockDarkPrismarineSlab;
import vazkii.botania.common.block.decor.slabs.prismarine.BlockPrismarineBrickSlab;
import vazkii.botania.common.block.decor.slabs.prismarine.BlockPrismarineSlab;
import vazkii.botania.common.block.decor.stairs.BlockReedStairs;
import vazkii.botania.common.block.decor.stairs.BlockThatchStairs;
import vazkii.botania.common.block.decor.stairs.bricks.BlockCustomBrickStairs;
import vazkii.botania.common.block.decor.stairs.bricks.BlockSnowBrickStairs;
import vazkii.botania.common.block.decor.stairs.bricks.BlockSoulBrickStairs;
import vazkii.botania.common.block.decor.stairs.bricks.BlockTileStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockDreamwoodPlankStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockDreamwoodStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockLivingrockBrickStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockLivingrockStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockLivingwoodPlankStairs;
import vazkii.botania.common.block.decor.stairs.living.BlockLivingwoodStairs;
import vazkii.botania.common.block.decor.stairs.prismarine.BlockDarkPrismarineStairs;
import vazkii.botania.common.block.decor.stairs.prismarine.BlockPrismarineBrickStairs;
import vazkii.botania.common.block.decor.stairs.prismarine.BlockPrismarineStairs;
import vazkii.botania.common.block.dispenser.BehaviourSeeds;
import vazkii.botania.common.block.dispenser.BehaviourWand;
import vazkii.botania.common.block.subtile.SubTileManastar;
import vazkii.botania.common.block.subtile.SubTilePureDaisy;
import vazkii.botania.common.block.subtile.functional.SubTileAgricarnation;
import vazkii.botania.common.block.subtile.functional.SubTileBellethorn;
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
import vazkii.botania.common.block.subtile.functional.SubTilePollidisiac;
import vazkii.botania.common.block.subtile.functional.SubTileRannuncarpus;
import vazkii.botania.common.block.subtile.functional.SubTileTangleberrie;
import vazkii.botania.common.block.subtile.functional.SubTileTigerseye;
import vazkii.botania.common.block.subtile.functional.SubTileVinculotus;
import vazkii.botania.common.block.subtile.functional.SubtileOrechid;
import vazkii.botania.common.block.subtile.generating.SubTileArcaneRose;
import vazkii.botania.common.block.subtile.generating.SubTileDaybloom;
import vazkii.botania.common.block.subtile.generating.SubTileEndoflame;
import vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum;
import vazkii.botania.common.block.subtile.generating.SubTileGourmaryllis;
import vazkii.botania.common.block.subtile.generating.SubTileHydroangeas;
import vazkii.botania.common.block.subtile.generating.SubTileKekimurus;
import vazkii.botania.common.block.subtile.generating.SubTileMunchdew;
import vazkii.botania.common.block.subtile.generating.SubTileNightshade;
import vazkii.botania.common.block.subtile.generating.SubTileThermalily;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileBifrost;
import vazkii.botania.common.block.tile.TileBrewery;
import vazkii.botania.common.block.tile.TileCraftCrate;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.block.tile.TileEnderEye;
import vazkii.botania.common.block.tile.TileForestEye;
import vazkii.botania.common.block.tile.TileManaBeacon;
import vazkii.botania.common.block.tile.TileMiniIsland;
import vazkii.botania.common.block.tile.TileOpenCrate;
import vazkii.botania.common.block.tile.TilePlatform;
import vazkii.botania.common.block.tile.TilePylon;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.TileSpawnerClaw;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.block.tile.TileStarfield;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.block.tile.TileTinyPlanet;
import vazkii.botania.common.block.tile.TileTinyPotato;
import vazkii.botania.common.block.tile.mana.TileDistributor;
import vazkii.botania.common.block.tile.mana.TileManaDetector;
import vazkii.botania.common.block.tile.mana.TileManaVoid;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.block.tile.mana.TileRFGenerator;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.block.tile.mana.TileTurntable;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibOreDict;
import cpw.mods.fml.common.registry.GameRegistry;

public final class ModBlocks {

	public static Block flower;
	public static Block altar;
	public static Block livingrock;
	public static Block livingwood;
	public static Block specialFlower;
	public static Block spreader;
	public static Block pool;
	public static Block runeAltar;
	public static Block unstableBlock;
	public static Block pylon;
	public static Block pistonRelay;
	public static Block distributor;
	public static Block manaBeacon;
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
	public static Block prismarine;
	public static Block seaLamp;
	public static Block miniIsland;
	public static Block tinyPotato;
	public static Block spawnerClaw;
	public static Block reedBlock;
	public static Block thatch;
	public static Block customBrick;
	public static Block enderEye;
	public static Block starfield;
	public static Block rfGenerator;
	public static Block elfGlass;
	public static Block brewery;
	public static Block manaGlass;
	public static Block terraPlate;

	public static Block livingwoodStairs;
	public static Block livingwoodSlab;
	public static Block livingwoodSlabFull;
	public static Block livingwoodPlankStairs;
	public static Block livingwoodPlankSlab;
	public static Block livingwoodPlankSlabFull;
	public static Block livingrockStairs;
	public static Block livingrockSlab;
	public static Block livingrockSlabFull;
	public static Block livingrockBrickStairs;
	public static Block livingrockBrickSlab;
	public static Block livingrockBrickSlabFull;
	public static Block dreamwoodStairs;
	public static Block dreamwoodSlab;
	public static Block dreamwoodSlabFull;
	public static Block dreamwoodPlankStairs;
	public static Block dreamwoodPlankSlab;
	public static Block dreamwoodPlankSlabFull;

	public static Block prismarineStairs;
	public static Block prismarineSlab;
	public static Block prismarineSlabFull;
	public static Block prismarineBrickStairs;
	public static Block prismarineBrickSlab;
	public static Block prismarineBrickSlabFull;
	public static Block darkPrismarineStairs;
	public static Block darkPrismarineSlab;
	public static Block darkPrismarineSlabFull;

	public static Block reedStairs;
	public static Block reedSlab;
	public static Block reedSlabFull;
	public static Block thatchStairs;
	public static Block thatchSlab;
	public static Block thatchSlabFull;

	public static Block netherBrickStairs;
	public static Block netherBrickSlab;
	public static Block netherBrickSlabFull;
	public static Block soulBrickStairs;
	public static Block soulBrickSlab;
	public static Block soulBrickSlabFull;
	public static Block snowBrickStairs;
	public static Block snowBrickSlab;
	public static Block snowBrickSlabFull;
	public static Block tileStairs;
	public static Block tileSlab;
	public static Block tileSlabFull;

	public static Block darkQuartz;
	public static Block darkQuartzSlab;
	public static Block darkQuartzSlabFull;
	public static Block darkQuartzStairs;
	public static Block manaQuartz;
	public static Block manaQuartzSlab;
	public static Block manaQuartzSlabFull;
	public static Block manaQuartzStairs;
	public static Block blazeQuartz;
	public static Block blazeQuartzSlab;
	public static Block blazeQuartzSlabFull;
	public static Block blazeQuartzStairs;
	public static Block lavenderQuartz;
	public static Block lavenderQuartzSlab;
	public static Block lavenderQuartzSlabFull;
	public static Block lavenderQuartzStairs;
	public static Block redQuartz;
	public static Block redQuartzSlab;
	public static Block redQuartzSlabFull;
	public static Block redQuartzStairs;
	public static Block elfQuartz;
	public static Block elfQuartzSlab;
	public static Block elfQuartzSlabFull;
	public static Block elfQuartzStairs;

	public static void init() {
		flower = new BlockModFlower();
		altar = new BlockAltar();
		livingrock = new BlockLivingrock();
		livingwood = new BlockLivingwood();
		specialFlower = new BlockSpecialFlower();
		spreader = new BlockSpreader();
		pool = new BlockPool();
		runeAltar = new BlockRuneAltar();
		unstableBlock = new BlockUnstable();
		pylon = new BlockPylon();
		pistonRelay = new BlockPistonRelay();
		distributor = new BlockDistributor();
		manaBeacon = new BlockManaBeacon();
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
		prismarine = new BlockPrismarine();
		seaLamp = new BlockSeaLamp();
		miniIsland = new BlockMiniIsland();
		tinyPotato = new BlockTinyPotato();
		spawnerClaw = new BlockSpawnerClaw();
		reedBlock = new BlockReeds();
		thatch = new BlockThatch();
		customBrick = new BlockCustomBrick();
		enderEye = new BlockEnderEye();
		starfield = new BlockStarfield();
		rfGenerator	= new BlockRFGenerator();
		elfGlass = new BlockElfGlass();
		brewery = new BlockBrewery();
		manaGlass = new BlockManaGlass();
		terraPlate = new BlockTerraPlate();
		
		livingwoodStairs = new BlockLivingwoodStairs();
		livingwoodSlab = new BlockLivingwoodSlab(false);
		livingwoodSlabFull = new BlockLivingwoodSlab(true);
		livingwoodPlankStairs = new BlockLivingwoodPlankStairs();
		livingwoodPlankSlab = new BlockLivingwoodPlankSlab(false);
		livingwoodPlankSlabFull = new BlockLivingwoodPlankSlab(true);
		livingrockStairs = new BlockLivingrockStairs();
		livingrockSlab = new BlockLivingrockSlab(false);
		livingrockSlabFull = new BlockLivingrockSlab(true);
		livingrockBrickStairs = new BlockLivingrockBrickStairs();
		livingrockBrickSlab = new BlockLivingrockBrickSlab(false);
		livingrockBrickSlabFull = new BlockLivingrockBrickSlab(true);
		dreamwoodStairs = new BlockDreamwoodStairs();
		dreamwoodSlab = new BlockDreamwoodSlab(false);
		dreamwoodSlabFull = new BlockDreamwoodSlab(true);
		dreamwoodPlankStairs = new BlockDreamwoodPlankStairs();
		dreamwoodPlankSlab = new BlockDreamwoodPlankSlab(false);
		dreamwoodPlankSlabFull = new BlockDreamwoodPlankSlab(true);

		reedStairs = new BlockReedStairs();
		reedSlab = new BlockReedSlab(false);
		reedSlabFull = new BlockReedSlab(true);
		thatchStairs = new BlockThatchStairs();
		thatchSlab = new BlockThatchSlab(false);
		thatchSlabFull = new BlockThatchSlab(true);

		prismarineStairs = new BlockPrismarineStairs();
		prismarineSlab = new BlockPrismarineSlab(false);
		prismarineSlabFull = new BlockPrismarineSlab(true);
		prismarineBrickStairs = new BlockPrismarineBrickStairs();
		prismarineBrickSlab = new BlockPrismarineBrickSlab(false);
		prismarineBrickSlabFull = new BlockPrismarineBrickSlab(true);
		darkPrismarineStairs = new BlockDarkPrismarineStairs();
		darkPrismarineSlab = new BlockDarkPrismarineSlab(false);
		darkPrismarineSlabFull = new BlockDarkPrismarineSlab(true);

		netherBrickStairs = new BlockCustomBrickStairs();
		netherBrickSlab = new BlockCustomBrickSlab(false);
		netherBrickSlabFull = new BlockCustomBrickSlab(true);
		soulBrickStairs = new BlockSoulBrickStairs();
		soulBrickSlab = new BlockSoulBrickSlab(false);
		soulBrickSlabFull = new BlockSoulBrickSlab(true);
		snowBrickStairs = new BlockSnowBrickStairs();
		snowBrickSlab = new BlockSnowBrickSlab(false);
		snowBrickSlabFull = new BlockSnowBrickSlab(true);
		tileStairs = new BlockTileStairs();
		tileSlab = new BlockTileSlab(false);
		tileSlabFull = new BlockTileSlab(true);

		if(ConfigHandler.darkQuartzEnabled) {
			darkQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_DARK);
			darkQuartzSlab = new BlockSpecialQuartzSlab(darkQuartz, false);
			darkQuartzSlabFull = new BlockSpecialQuartzSlab(darkQuartz, true);
			darkQuartzStairs = new BlockSpecialQuartzStairs(darkQuartz);
		}

		manaQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_MANA);
		manaQuartzSlab = new BlockSpecialQuartzSlab(manaQuartz, false);
		manaQuartzSlabFull = new BlockSpecialQuartzSlab(manaQuartz, true);
		manaQuartzStairs = new BlockSpecialQuartzStairs(manaQuartz);
		blazeQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_BLAZE);
		blazeQuartzSlab = new BlockSpecialQuartzSlab(blazeQuartz, false);
		blazeQuartzSlabFull = new BlockSpecialQuartzSlab(blazeQuartz, true);
		blazeQuartzStairs = new BlockSpecialQuartzStairs(blazeQuartz);
		lavenderQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_LAVENDER);
		lavenderQuartzSlab = new BlockSpecialQuartzSlab(lavenderQuartz, false);
		lavenderQuartzSlabFull = new BlockSpecialQuartzSlab(lavenderQuartz, true);
		lavenderQuartzStairs = new BlockSpecialQuartzStairs(lavenderQuartz);
		redQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_RED);
		redQuartzSlab = new BlockSpecialQuartzSlab(redQuartz, false);
		redQuartzSlabFull = new BlockSpecialQuartzSlab(redQuartz, true);
		redQuartzStairs = new BlockSpecialQuartzStairs(redQuartz);
		elfQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_ELF);
		elfQuartzSlab = new BlockSpecialQuartzSlab(elfQuartz, false);
		elfQuartzSlabFull = new BlockSpecialQuartzSlab(elfQuartz, true);
		elfQuartzStairs = new BlockSpecialQuartzStairs(elfQuartz);

		if(ConfigHandler.darkQuartzEnabled) {
			((BlockModSlab) darkQuartzSlab).register();
			((BlockModSlab) darkQuartzSlabFull).register();
		}
		((BlockModSlab) manaQuartzSlab).register();
		((BlockModSlab) manaQuartzSlabFull).register();
		((BlockModSlab) blazeQuartzSlab).register();
		((BlockModSlab) blazeQuartzSlabFull).register();
		((BlockModSlab) lavenderQuartzSlab).register();
		((BlockModSlab) lavenderQuartzSlabFull).register();
		((BlockModSlab) redQuartzSlab).register();
		((BlockModSlab) redQuartzSlabFull).register();
		((BlockModSlab) elfQuartzSlab).register();
		((BlockModSlab) elfQuartzSlabFull).register();

		((BlockModSlab) livingwoodSlab).register();
		((BlockModSlab) livingwoodSlabFull).register();
		((BlockModSlab) livingwoodPlankSlab).register();
		((BlockModSlab) livingwoodPlankSlabFull).register();
		((BlockModSlab) livingrockSlab).register();
		((BlockModSlab) livingrockSlabFull).register();
		((BlockModSlab) livingrockBrickSlab).register();
		((BlockModSlab) livingrockBrickSlabFull).register();
		((BlockModSlab) dreamwoodSlab).register();
		((BlockModSlab) dreamwoodSlabFull).register();
		((BlockModSlab) dreamwoodPlankSlab).register();
		((BlockModSlab) dreamwoodPlankSlabFull).register();

		((BlockModSlab) reedSlab).register();
		((BlockModSlab) reedSlabFull).register();
		((BlockModSlab) thatchSlab).register();
		((BlockModSlab) thatchSlabFull).register();

		((BlockModSlab) prismarineSlab).register();
		((BlockModSlab) prismarineSlabFull).register();
		((BlockModSlab) prismarineBrickSlab).register();
		((BlockModSlab) prismarineBrickSlabFull).register();
		((BlockModSlab) darkPrismarineSlab).register();
		((BlockModSlab) darkPrismarineSlabFull).register();

		((BlockModSlab) netherBrickSlab).register();
		((BlockModSlab) netherBrickSlabFull).register();
		((BlockModSlab) soulBrickSlab).register();
		((BlockModSlab) soulBrickSlabFull).register();
		((BlockModSlab) snowBrickSlab).register();
		((BlockModSlab) snowBrickSlabFull).register();
		((BlockModSlab) tileSlab).register();
		((BlockModSlab) tileSlabFull).register();

		for(int i = 0; i < 16; i++)
			OreDictionary.registerOre(LibOreDict.FLOWER[i], new ItemStack(flower, 1, i));

		OreDictionary.registerOre(LibOreDict.LIVING_ROCK, livingrock);
		OreDictionary.registerOre(LibOreDict.LIVING_WOOD, livingwood);
		OreDictionary.registerOre(LibOreDict.DREAM_WOOD, dreamwood);

		// Vanilla OreDict entries
		OreDictionary.registerOre("dirt", Blocks.dirt);
		OreDictionary.registerOre("grass", Blocks.grass);
		OreDictionary.registerOre("sand", Block.getBlockFromName("sand"));
		OreDictionary.registerOre("gravel", Block.getBlockFromName("gravel"));
		OreDictionary.registerOre("hardenedClay", new ItemStack(Blocks.hardened_clay, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("snowLayer", Blocks.snow_layer);
		OreDictionary.registerOre("mycelium", Blocks.mycelium);

		initTileEntities();
	}

	public static void addDispenserBehaviours() {
		for(Item seed : BotaniaAPI.seeds.keySet())
			BlockDispenser.dispenseBehaviorRegistry.putObject(seed, new BehaviourSeeds(BotaniaAPI.seeds.get(seed)));
		BlockDispenser.dispenseBehaviorRegistry.putObject(ModItems.twigWand, new BehaviourWand());
	}

	private static void initTileEntities() {
		registerTile(TileAltar.class, LibBlockNames.ALTAR);
		registerTile(TileSpecialFlower.class, LibBlockNames.SPECIAL_FLOWER);
		registerTile(TileSpreader.class, LibBlockNames.SPREADER);
		registerTile(TilePool.class, LibBlockNames.POOL);
		registerTile(TileRuneAltar.class, LibBlockNames.RUNE_ALTAR);
		registerTile(TilePylon.class, LibBlockNames.PYLON);
		registerTile(TileDistributor.class, LibBlockNames.DISTRIBUTOR);
		registerTile(TileManaBeacon.class, LibBlockNames.MANA_BEACON);
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
		registerTile(TileMiniIsland.class, LibBlockNames.MINI_ISLAND);
		registerTile(TileTinyPotato.class, LibBlockNames.TINY_POTATO);
		registerTile(TileSpawnerClaw.class, LibBlockNames.SPAWNER_CLAW);
		registerTile(TileEnderEye.class, LibBlockNames.ENDER_EYE_BLOCK);
		registerTile(TileStarfield.class, LibBlockNames.STARFIELD);
		registerTile(TileRFGenerator.class, LibBlockNames.RF_GENERATOR);
		registerTile(TileBrewery.class, LibBlockNames.BREWERY);
		registerTile(TileTerraPlate.class, LibBlockNames.TERRA_PLATE);

		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_PUREDAISY, SubTilePureDaisy.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_MANASTAR, SubTileManastar.class);

		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_DAYBLOOM, SubTileDaybloom.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_ENDOFLAME, SubTileEndoflame.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_HYDROANGEAS, SubTileHydroangeas.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_THERMALILY, SubTileThermalily.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_NIGHTSHADE, SubTileNightshade.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_ARCANE_ROSE, SubTileArcaneRose.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_MUNCHDEW, SubTileMunchdew.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_ENTROPINNYUM, SubTileEntropinnyum.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_KEKIMURUS, SubTileKekimurus.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_GOURMARYLLIS, SubTileGourmaryllis.class);

		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_BELLETHORN, SubTileBellethorn.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_DREADTHORN, SubTileDreadthorn.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_HEISEI_DREAM, SubTileHeiseiDream.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_TIGERSEYE, SubTileTigerseye.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_JADED_AMARANTHUS, SubTileJadedAmaranthus.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_ORECHID, SubtileOrechid.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_FALLEN_KANADE, SubTileFallenKanade.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_EXOFLAME, SubTileExoflame.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_AGRICARNATION, SubTileAgricarnation.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_HOPPERHOCK, SubTileHopperhock.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_TANGLEBERRIE, SubTileTangleberrie.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_JIYUULIA, SubTileJiyuulia.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_RANNUNCARPUS, SubTileRannuncarpus.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_HYACIDUS, SubTileHyacidus.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_POLLIDISIAC, SubTilePollidisiac.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_CLAYCONIA, SubTileClayconia.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_LOONIUM, SubTileLoonuim.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_DAFFOMILL, SubTileDaffomill.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_VINCULOTUS, SubTileVinculotus.class);
	}

	private static void registerTile(Class<? extends TileEntity> clazz, String key) {
		GameRegistry.registerTileEntityWithAlternatives(clazz, LibResources.PREFIX_MOD + key, key);
	}

}