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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.corporea.BlockCorporeaCrystalCube;
import vazkii.botania.common.block.corporea.BlockCorporeaFunnel;
import vazkii.botania.common.block.corporea.BlockCorporeaIndex;
import vazkii.botania.common.block.corporea.BlockCorporeaInterceptor;
import vazkii.botania.common.block.corporea.BlockCorporeaRetainer;
import vazkii.botania.common.block.decor.*;
import vazkii.botania.common.block.dispenser.BehaviourFelPumpkin;
import vazkii.botania.common.block.dispenser.BehaviourPoolMinecart;
import vazkii.botania.common.block.dispenser.BehaviourStick;
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
import vazkii.botania.common.block.subtile.functional.*;
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
import vazkii.botania.common.block.subtile.generating.SubTileShulkMeNot;
import vazkii.botania.common.block.subtile.generating.SubTileSpectrolus;
import vazkii.botania.common.block.subtile.generating.SubTileThermalily;
import vazkii.botania.common.block.tile.*;
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
import vazkii.botania.common.item.block.ItemBlockDreamwood;
import vazkii.botania.common.item.block.ItemBlockElven;
import vazkii.botania.common.item.block.ItemBlockFloatingSpecialFlower;
import vazkii.botania.common.item.block.ItemBlockMod;
import vazkii.botania.common.item.block.ItemBlockPool;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.item.block.ItemBlockStorage;
import vazkii.botania.common.item.block.ItemBlockTinyPotato;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibOreDict;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class ModBlocks {
	public static final Block flower = new BlockModFlower();
	public static final Block altar = new BlockAltar();
	public static final Block livingrock = new BlockLivingrock();
	public static final Block livingwood = new BlockLivingwood();
	public static final Block specialFlower = new BlockSpecialFlower();
	public static final Block spreader = new BlockSpreader();
	public static final Block pool = new BlockPool();
	public static final Block runeAltar = new BlockRuneAltar();
	public static final Block pylon = new BlockPylon();
	public static final Block pistonRelay = new BlockPistonRelay();
	public static final Block distributor = new BlockDistributor();
	public static final Block manaVoid = new BlockManaVoid();
	public static final Block manaDetector = new BlockManaDetector();
	public static final Block enchanter = new BlockEnchanter();
	public static final Block turntable = new BlockTurntable();
	public static final Block tinyPlanet = new BlockTinyPlanet();
	public static final Block alchemyCatalyst = new BlockAlchemyCatalyst();
	public static final Block openCrate = new BlockOpenCrate();
	public static final Block forestEye = new BlockForestEye();
	public static final Block storage = new BlockStorage();
	public static final Block forestDrum = new BlockForestDrum();
	public static final Block shinyFlower = new BlockShinyFlower();
	public static final Block platform = new BlockPlatform();
	public static final Block alfPortal = new BlockAlfPortal();
	public static final Block dreamwood = new BlockDreamwood();
	public static final Block conjurationCatalyst = new BlockConjurationCatalyst();
	public static final Block bifrost = new BlockBifrost();
	public static final Block solidVines = new BlockSolidVines();
	public static final Block buriedPetals = new BlockBuriedPetals();
	public static final Block floatingFlower = new BlockFloatingFlower();
	public static final Block tinyPotato = new BlockTinyPotato();
	public static final Block spawnerClaw = new BlockSpawnerClaw();
	public static final Block customBrick = new BlockCustomBrick();
	public static final Block enderEye = new BlockEnderEye();
	public static final Block starfield = new BlockStarfield();
	public static final Block rfGenerator = new BlockRFGenerator();
	public static final Block elfGlass = new BlockElfGlass();
	public static final Block brewery = new BlockBrewery();
	public static final Block manaGlass = new BlockManaGlass();
	public static final Block terraPlate = new BlockTerraPlate();
	public static final Block redStringContainer = new BlockRedStringContainer();
	public static final Block redStringDispenser = new BlockRedStringDispenser();
	public static final Block redStringFertilizer = new BlockRedStringFertilizer();
	public static final Block redStringComparator = new BlockRedStringComparator();
	public static final Block redStringRelay = new BlockRedStringRelay();
	public static final Block floatingSpecialFlower = new BlockFloatingSpecialFlower();
	public static final Block manaFlame = new BlockManaFlame();
	public static final Block prism = new BlockPrism();
	public static final Block enchantedSoil = new BlockEnchantedSoil();
	public static final Block petalBlock = new BlockPetalBlock();
	public static final Block corporeaIndex = new BlockCorporeaIndex();
	public static final Block corporeaFunnel = new BlockCorporeaFunnel();
	public static final Block mushroom = new BlockModMushroom();
	public static final Block pump = new BlockPump();
	public static final Block doubleFlower1 = new BlockModDoubleFlower1();
	public static final Block doubleFlower2 = new BlockModDoubleFlower2();
	public static final Block fakeAir = new BlockFakeAir();
	public static final Block blazeBlock = new BlockBlaze();
	public static final Block corporeaInterceptor = new BlockCorporeaInterceptor();
	public static final Block corporeaCrystalCube = new BlockCorporeaCrystalCube();
	public static final Block incensePlate = new BlockIncensePlate();
	public static final Block hourglass = new BlockHourglass();
	public static final Block ghostRail = new BlockGhostRail();
	public static final Block sparkChanger = new BlockSparkChanger();
	public static final Block root = new BlockRoot();
	public static final Block felPumpkin = new BlockFelPumpkin();
	public static final Block cocoon = new BlockCocoon();
	public static final Block lightRelay = new BlockLightRelay();
	public static final Block lightLauncher = new BlockLightLauncher();
	public static final Block manaBomb = new BlockManaBomb();
	public static final Block cacophonium = new BlockCacophonium();
	public static final Block bellows = new BlockBellows();
	public static final Block bifrostPerm = new BlockBifrostPerm();
	public static final Block cellBlock = new BlockCell();
	public static final Block redStringInterceptor = new BlockRedStringInterceptor();
	public static final Block gaiaHead = new BlockGaiaHead();
	public static final Block corporeaRetainer = new BlockCorporeaRetainer();
	public static final Block teruTeruBozu = new BlockTeruTeruBozu();
	public static final Block shimmerrock = new BlockShimmerrock();
	public static final Block shimmerwoodPlanks = new BlockShimmerwoodPlanks();
	public static final Block avatar = new BlockAvatar();
	public static final Block altGrass = new BlockAltGrass();
	public static final Block animatedTorch = new BlockAnimatedTorch();

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> evt) {
		IForgeRegistry<Block> r = evt.getRegistry();

		r.register(flower);
		r.register(altar);
		r.register(livingrock);
		r.register(livingwood);
		r.register(specialFlower);
		r.register(spreader);
		r.register(pool);
		r.register(runeAltar);
		r.register(pylon);
		r.register(pistonRelay);
		r.register(distributor);
		r.register(manaVoid);
		r.register(manaDetector);
		r.register(enchanter);
		r.register(turntable);
		r.register(tinyPlanet);
		r.register(alchemyCatalyst);
		r.register(openCrate);
		r.register(forestEye);
		r.register(storage);
		r.register(forestDrum);
		r.register(shinyFlower);
		r.register(platform);
		r.register(alfPortal);
		r.register(dreamwood);
		r.register(conjurationCatalyst);
		r.register(bifrost);
		r.register(solidVines);
		r.register(buriedPetals);
		r.register(floatingFlower);
		r.register(tinyPotato);
		r.register(spawnerClaw);
		r.register(customBrick);
		r.register(enderEye);
		r.register(starfield);
		r.register(rfGenerator);
		r.register(elfGlass);
		r.register(brewery);
		r.register(manaGlass);
		r.register(terraPlate);
		r.register(redStringContainer);
		r.register(redStringDispenser);
		r.register(redStringFertilizer);
		r.register(redStringComparator);
		r.register(redStringRelay);
		r.register(floatingSpecialFlower);
		r.register(manaFlame);
		r.register(prism);
		r.register(enchantedSoil);
		r.register(petalBlock);
		r.register(corporeaIndex);
		r.register(corporeaFunnel);
		r.register(mushroom);
		r.register(pump);
		r.register(doubleFlower1);
		r.register(doubleFlower2);
		r.register(fakeAir);
		r.register(blazeBlock);
		r.register(corporeaInterceptor);
		r.register(corporeaCrystalCube);
		r.register(incensePlate);
		r.register(hourglass);
		r.register(ghostRail);
		r.register(sparkChanger);
		r.register(root);
		r.register(felPumpkin);
		r.register(cocoon);
		r.register(lightRelay);
		r.register(lightLauncher);
		r.register(manaBomb);
		r.register(cacophonium);
		r.register(bellows);
		r.register(bifrostPerm);
		r.register(cellBlock);
		r.register(redStringInterceptor);
		r.register(gaiaHead);
		r.register(corporeaRetainer);
		r.register(teruTeruBozu);
		r.register(shimmerrock);
		r.register(shimmerwoodPlanks);
		r.register(avatar);
		r.register(altGrass);
		r.register(animatedTorch);

		RecipeManaInfusion.alchemyState = alchemyCatalyst.getDefaultState();
		RecipeManaInfusion.conjurationState = conjurationCatalyst.getDefaultState();
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		r.register(new ItemBlockWithMetadataAndName(flower).setRegistryName(flower.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(altar).setRegistryName(altar.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(livingrock).setRegistryName(livingrock.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(livingwood).setRegistryName(livingwood.getRegistryName()));
		r.register(new ItemBlockSpecialFlower(specialFlower).setRegistryName(specialFlower.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(spreader).setRegistryName(spreader.getRegistryName()));
		r.register(new ItemBlockPool(pool).setRegistryName(pool.getRegistryName()));
		r.register(new ItemBlockMod(runeAltar).setRegistryName(runeAltar.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(pylon).setRegistryName(pylon.getRegistryName()));
		r.register(new ItemBlockMod(pistonRelay).setRegistryName(pistonRelay.getRegistryName()));
		r.register(new ItemBlockMod(distributor).setRegistryName(distributor.getRegistryName()));
		r.register(new ItemBlockMod(manaVoid).setRegistryName(manaVoid.getRegistryName()));
		r.register(new ItemBlockMod(manaDetector).setRegistryName(manaDetector.getRegistryName()));
		r.register(new ItemBlockMod(enchanter).setRegistryName(enchanter.getRegistryName()));
		r.register(new ItemBlockMod(turntable).setRegistryName(turntable.getRegistryName()));
		r.register(new ItemBlockMod(tinyPlanet).setRegistryName(tinyPlanet.getRegistryName()));
		r.register(new ItemBlockMod(alchemyCatalyst).setRegistryName(alchemyCatalyst.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(openCrate).setRegistryName(openCrate.getRegistryName()));
		r.register(new ItemBlockMod(forestEye).setRegistryName(forestEye.getRegistryName()));
		r.register(new ItemBlockStorage(storage).setRegistryName(storage.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(forestDrum).setRegistryName(forestDrum.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(shinyFlower).setRegistryName(shinyFlower.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(platform).setRegistryName(platform.getRegistryName()));
		r.register(new ItemBlockMod(alfPortal).setRegistryName(alfPortal.getRegistryName()));
		r.register(new ItemBlockDreamwood(dreamwood).setRegistryName(dreamwood.getRegistryName()));
		r.register(new ItemBlockMod(conjurationCatalyst).setRegistryName(conjurationCatalyst.getRegistryName()));
		r.register(new ItemBlockMod(bifrost).setRegistryName(bifrost.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(floatingFlower).setRegistryName(floatingFlower.getRegistryName()));
		r.register(new ItemBlockTinyPotato(tinyPotato).setRegistryName(tinyPotato.getRegistryName()));
		r.register(new ItemBlockMod(spawnerClaw).setRegistryName(spawnerClaw.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(customBrick).setRegistryName(customBrick.getRegistryName()));
		r.register(new ItemBlockMod(enderEye).setRegistryName(enderEye.getRegistryName()));
		r.register(new ItemBlockMod(starfield).setRegistryName(starfield.getRegistryName()));
		r.register(new ItemBlockMod(rfGenerator).setRegistryName(rfGenerator.getRegistryName()));
		r.register(new ItemBlockElven(elfGlass).setRegistryName(elfGlass.getRegistryName()));
		r.register(new ItemBlockMod(brewery).setRegistryName(brewery.getRegistryName()));
		r.register(new ItemBlockMod(manaGlass).setRegistryName(manaGlass.getRegistryName()));
		r.register(new ItemBlockMod(terraPlate).setRegistryName(terraPlate.getRegistryName()));
		r.register(new ItemBlockMod(redStringContainer).setRegistryName(redStringContainer.getRegistryName()));
		r.register(new ItemBlockMod(redStringDispenser).setRegistryName(redStringDispenser.getRegistryName()));
		r.register(new ItemBlockMod(redStringFertilizer).setRegistryName(redStringFertilizer.getRegistryName()));
		r.register(new ItemBlockMod(redStringComparator).setRegistryName(redStringComparator.getRegistryName()));
		r.register(new ItemBlockMod(redStringRelay).setRegistryName(redStringRelay.getRegistryName()));
		r.register(new ItemBlockFloatingSpecialFlower(floatingSpecialFlower).setRegistryName(floatingSpecialFlower.getRegistryName()));
		r.register(new ItemBlockMod(prism).setRegistryName(prism.getRegistryName()));
		r.register(new ItemBlockMod(enchantedSoil).setRegistryName(enchantedSoil.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(petalBlock).setRegistryName(petalBlock.getRegistryName()));
		r.register(new ItemBlockMod(corporeaIndex).setRegistryName(corporeaIndex.getRegistryName()));
		r.register(new ItemBlockMod(corporeaFunnel).setRegistryName(corporeaFunnel.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(mushroom).setRegistryName(mushroom.getRegistryName()));
		r.register(new ItemBlockMod(pump).setRegistryName(pump.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(doubleFlower1).setRegistryName(doubleFlower1.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(doubleFlower2).setRegistryName(doubleFlower2.getRegistryName()));
		r.register(new ItemBlockBlaze(blazeBlock).setRegistryName(blazeBlock.getRegistryName()));
		r.register(new ItemBlockMod(corporeaInterceptor).setRegistryName(corporeaInterceptor.getRegistryName()));
		r.register(new ItemBlockMod(corporeaCrystalCube).setRegistryName(corporeaCrystalCube.getRegistryName()));
		r.register(new ItemBlockMod(incensePlate).setRegistryName(incensePlate.getRegistryName()));
		r.register(new ItemBlockMod(hourglass).setRegistryName(hourglass.getRegistryName()));
		r.register(new ItemBlockMod(ghostRail).setRegistryName(ghostRail.getRegistryName()));
		r.register(new ItemBlockMod(sparkChanger).setRegistryName(sparkChanger.getRegistryName()));
		r.register(new ItemBlockMod(root).setRegistryName(root.getRegistryName()));
		r.register(new ItemBlockMod(felPumpkin).setRegistryName(felPumpkin.getRegistryName()));
		r.register(new ItemBlockMod(cocoon).setRegistryName(cocoon.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(lightRelay).setRegistryName(lightRelay.getRegistryName()));
		r.register(new ItemBlockMod(lightLauncher).setRegistryName(lightLauncher.getRegistryName()));
		r.register(new ItemBlockMod(manaBomb).setRegistryName(manaBomb.getRegistryName()));
		r.register(new ItemBlockMod(cacophonium).setRegistryName(cacophonium.getRegistryName()));
		r.register(new ItemBlockMod(bellows).setRegistryName(bellows.getRegistryName()));
		r.register(new ItemBlockMod(bifrostPerm).setRegistryName(bifrostPerm.getRegistryName()));
		r.register(new ItemBlockMod(cellBlock).setRegistryName(cellBlock.getRegistryName()));
		r.register(new ItemBlockMod(redStringInterceptor).setRegistryName(redStringInterceptor.getRegistryName()));
		r.register(new ItemBlockMod(corporeaRetainer).setRegistryName(corporeaRetainer.getRegistryName()));
		r.register(new ItemBlockMod(teruTeruBozu).setRegistryName(teruTeruBozu.getRegistryName()));
		r.register(new ItemBlockMod(shimmerrock).setRegistryName(shimmerrock.getRegistryName()));
		r.register(new ItemBlockMod(shimmerwoodPlanks).setRegistryName(shimmerwoodPlanks.getRegistryName()));
		r.register(new ItemBlockMod(avatar).setRegistryName(avatar.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(altGrass).setRegistryName(altGrass.getRegistryName()));
		r.register(new ItemBlockMod(animatedTorch).setRegistryName(animatedTorch.getRegistryName()));
		initOreDict();

		initTileEntities();
	}

	public static void addDispenserBehaviours() {
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.twigWand, new BehaviourWand());
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.obedienceStick, new BehaviourStick());
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.poolMinecart, new BehaviourPoolMinecart());
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(ModBlocks.felPumpkin), new BehaviourFelPumpkin());

		SeedBehaviours.init();
	}

	private static void initOreDict() {
		for(int i = 0; i < 16; i++) {
			OreDictionary.registerOre(LibOreDict.FLOWER[i], new ItemStack(flower, 1, i));
			OreDictionary.registerOre(LibOreDict.PETAL[i], new ItemStack(mushroom, 1, i));
		}

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
		registerTile(TileAnimatedTorch.class, LibBlockNames.ANIMATED_TORCH);

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
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_SHULK_ME_NOT, SubTileShulkMeNot.class);

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
