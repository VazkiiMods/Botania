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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.dispenser.BehaviourSeeds;
import vazkii.botania.common.block.dispenser.BehaviourWand;
import vazkii.botania.common.block.quartz.BlockSpecialQuartz;
import vazkii.botania.common.block.quartz.BlockSpecialQuartzSlab;
import vazkii.botania.common.block.quartz.BlockSpecialQuartzStairs;
import vazkii.botania.common.block.subtile.SubTileManastar;
import vazkii.botania.common.block.subtile.SubTilePureDaisy;
import vazkii.botania.common.block.subtile.functional.SubTileAgricarnation;
import vazkii.botania.common.block.subtile.functional.SubTileBellethorn;
import vazkii.botania.common.block.subtile.functional.SubTileClayconia;
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
import vazkii.botania.common.block.subtile.functional.SubtileOrechid;
import vazkii.botania.common.block.subtile.generating.SubTileArcaneRose;
import vazkii.botania.common.block.subtile.generating.SubTileDaybloom;
import vazkii.botania.common.block.subtile.generating.SubTileEndoflame;
import vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum;
import vazkii.botania.common.block.subtile.generating.SubTileHydroangeas;
import vazkii.botania.common.block.subtile.generating.SubTileKekimurus;
import vazkii.botania.common.block.subtile.generating.SubTileMunchdew;
import vazkii.botania.common.block.subtile.generating.SubTileNightshade;
import vazkii.botania.common.block.subtile.generating.SubTileThermalily;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.block.tile.TileForestEye;
import vazkii.botania.common.block.tile.TileManaBeacon;
import vazkii.botania.common.block.tile.TileOpenCrate;
import vazkii.botania.common.block.tile.TilePlatform;
import vazkii.botania.common.block.tile.TilePylon;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.block.tile.TileTinyPlanet;
import vazkii.botania.common.block.tile.mana.TileDistributor;
import vazkii.botania.common.block.tile.mana.TileManaDetector;
import vazkii.botania.common.block.tile.mana.TileManaVoid;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.block.tile.mana.TileTurntable;
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

		darkQuartz = new BlockSpecialQuartz(LibBlockNames.QUARTZ_DARK);
		darkQuartzSlab = new BlockSpecialQuartzSlab(darkQuartz, false);
		darkQuartzSlabFull = new BlockSpecialQuartzSlab(darkQuartz, true);
		darkQuartzStairs = new BlockSpecialQuartzStairs(darkQuartz);
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

		((BlockSpecialQuartzSlab) darkQuartzSlab).register();
		((BlockSpecialQuartzSlab) darkQuartzSlabFull).register();
		((BlockSpecialQuartzSlab) manaQuartzSlab).register();
		((BlockSpecialQuartzSlab) manaQuartzSlabFull).register();
		((BlockSpecialQuartzSlab) blazeQuartzSlab).register();
		((BlockSpecialQuartzSlab) blazeQuartzSlabFull).register();
		((BlockSpecialQuartzSlab) lavenderQuartzSlab).register();
		((BlockSpecialQuartzSlab) lavenderQuartzSlabFull).register();
		((BlockSpecialQuartzSlab) redQuartzSlab).register();
		((BlockSpecialQuartzSlab) redQuartzSlabFull).register();

		for(int i = 0; i < 16; i++)
			OreDictionary.registerOre(LibOreDict.FLOWER[i], new ItemStack(flower, 1, i));

		OreDictionary.registerOre(LibOreDict.LIVING_ROCK, livingrock);
		OreDictionary.registerOre(LibOreDict.LIVING_WOOD, livingwood);

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
		registerTile(TileForestEye.class, LibBlockNames.FOREST_EYE);
		registerTile(TilePlatform.class, LibBlockNames.PLATFORM);
		registerTile(TileAlfPortal.class, LibBlockNames.ALF_PORTAL);

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
	}

	private static void registerTile(Class<? extends TileEntity> clazz, String key) {
		GameRegistry.registerTileEntityWithAlternatives(clazz, LibResources.PREFIX_MOD + key, key);
	}

}