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
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.DummySubTile;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileType;
import vazkii.botania.client.render.tile.RenderTilePylon;
import vazkii.botania.client.render.tile.TEISR;
import vazkii.botania.common.block.corporea.BlockCorporeaCrystalCube;
import vazkii.botania.common.block.corporea.BlockCorporeaFunnel;
import vazkii.botania.common.block.corporea.BlockCorporeaIndex;
import vazkii.botania.common.block.corporea.BlockCorporeaInterceptor;
import vazkii.botania.common.block.corporea.BlockCorporeaRetainer;
import vazkii.botania.common.block.decor.*;
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
import vazkii.botania.common.item.block.ItemBlockTinyPotato;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(LibMisc.MOD_ID)
public final class ModBlocks {
	@ObjectHolder("white" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block whiteFlower;
	@ObjectHolder("orange" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block orangeFlower;
	@ObjectHolder("magenta" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block magentaFlower;
	@ObjectHolder("light_blue" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block lightBlueFlower;
	@ObjectHolder("yellow" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block yellowFlower;
	@ObjectHolder("lime" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block limeFlower;
	@ObjectHolder("pink" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block pinkFlower;
	@ObjectHolder("gray" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block grayFlower;
	@ObjectHolder("light_gray" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block lightGrayFlower;
	@ObjectHolder("cyan" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block cyanFlower;
	@ObjectHolder("purple" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block purpleFlower;
	@ObjectHolder("blue" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block blueFlower;
	@ObjectHolder("brown" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block brownFlower;
	@ObjectHolder("green" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block greenFlower;
	@ObjectHolder("red" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block redFlower;
	@ObjectHolder("black" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX) public static Block blackFlower;

	@ObjectHolder("apothecary_default") public static Block defaultAltar;
	@ObjectHolder("apothecary_forest") public static Block forestAltar;
	@ObjectHolder("apothecary_plains") public static Block plainsAltar;
	@ObjectHolder("apothecary_mountain") public static Block mountainAltar;
	@ObjectHolder("apothecary_fungal") public static Block fungalAltar;
	@ObjectHolder("apothecary_swamp") public static Block swampAltar;
	@ObjectHolder("apothecary_desert") public static Block desertAltar;
	@ObjectHolder("apothecary_taiga") public static Block taigaAltar;
	@ObjectHolder("apothecary_mesa") public static Block mesaAltar;
	@ObjectHolder("apothecary_mossy") public static Block mossyAltar;

	@ObjectHolder(LibBlockNames.LIVING_ROCK) public static Block livingrock;
	@ObjectHolder(LibBlockNames.LIVING_ROCK_BRICK) public static Block livingrockBrick;
	@ObjectHolder(LibBlockNames.LIVING_ROCK_BRICK_CHISELED) public static Block livingrockBrickChiseled;
	@ObjectHolder(LibBlockNames.LIVING_ROCK_BRICK_CRACKED) public static Block livingrockBrickCracked;
	@ObjectHolder(LibBlockNames.LIVING_ROCK_BRICK_MOSSY) public static Block livingrockBrickMossy;

	@ObjectHolder(LibBlockNames.LIVING_WOOD) public static Block livingwood;
	@ObjectHolder(LibBlockNames.LIVING_WOOD_PLANKS) public static Block livingwoodPlanks;
	@ObjectHolder(LibBlockNames.LIVING_WOOD_PLANKS_MOSSY) public static Block livingwoodPlanksMossy;
	@ObjectHolder(LibBlockNames.LIVING_WOOD_FRAMED) public static Block livingwoodFramed;
	@ObjectHolder(LibBlockNames.LIVING_WOOD_PATTERN_FRAMED) public static Block livingwoodPatternFramed;
	@ObjectHolder(LibBlockNames.LIVING_WOOD_GLIMMERING) public static Block livingwoodGlimmering;

	@ObjectHolder(LibBlockNames.SPECIAL_FLOWER) public static Block specialFlower;
	@ObjectHolder(LibBlockNames.SPREADER) public static Block manaSpreader;
	@ObjectHolder(LibBlockNames.SPREADER_REDSTONE) public static Block redstoneSpreader;
	@ObjectHolder(LibBlockNames.SPREADER_ELVEN) public static Block elvenSpreader;
	@ObjectHolder(LibBlockNames.SPREADER_GAIA) public static Block gaiaSpreader;

	@ObjectHolder(LibBlockNames.POOL) public static Block manaPool;
	@ObjectHolder(LibBlockNames.POOL_CREATIVE) public static Block creativePool;
	@ObjectHolder(LibBlockNames.POOL_DILUTED) public static Block dilutedPool;
	@ObjectHolder(LibBlockNames.POOL_FABULOUS) public static Block fabulousPool;

	@ObjectHolder(LibBlockNames.RUNE_ALTAR) public static Block runeAltar;
	@ObjectHolder(LibBlockNames.PYLON) public static Block manaPylon;
	@ObjectHolder(LibBlockNames.PYLON_NATURA) public static Block naturaPylon;
	@ObjectHolder(LibBlockNames.PYLON_GAIA) public static Block gaiaPylon;

	@ObjectHolder(LibBlockNames.PISTON_RELAY) public static Block pistonRelay;
	@ObjectHolder(LibBlockNames.DISTRIBUTOR) public static Block distributor;
	@ObjectHolder(LibBlockNames.MANA_VOID) public static Block manaVoid;
	@ObjectHolder(LibBlockNames.MANA_DETECTOR) public static Block manaDetector;
	@ObjectHolder(LibBlockNames.ENCHANTER) public static Block enchanter;
	@ObjectHolder(LibBlockNames.TURNTABLE) public static Block turntable;
	@ObjectHolder(LibBlockNames.TINY_PLANET) public static Block tinyPlanet;
	@ObjectHolder(LibBlockNames.ALCHEMY_CATALYST) public static Block alchemyCatalyst;
	@ObjectHolder(LibBlockNames.OPEN_CRATE) public static Block openCrate;
	@ObjectHolder(LibBlockNames.CRAFT_CRATE) public static Block craftCrate;
	@ObjectHolder(LibBlockNames.FOREST_EYE) public static Block forestEye;
	@ObjectHolder(LibBlockNames.MANASTEEL_BLOCK) public static Block manasteelBlock;
	@ObjectHolder(LibBlockNames.TERRASTEEL_BLOCK) public static Block terrasteelBlock;
	@ObjectHolder(LibBlockNames.ELEMENTIUM_BLOCK) public static Block elementiumBlock;
	@ObjectHolder(LibBlockNames.MANA_DIAMOND_BLOCK) public static Block manaDiamondBlock;
	@ObjectHolder(LibBlockNames.DRAGONSTONE_BLOCK) public static Block dragonstoneBlock;
	@ObjectHolder(LibBlockNames.DRUM_WILD) public static Block wildDrum;
	@ObjectHolder(LibBlockNames.DRUM_GATHERING) public static Block gatheringDrum;
	@ObjectHolder(LibBlockNames.DRUM_CANOPY) public static Block canopyDrum;
	@ObjectHolder("white" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block whiteShinyFlower;
	@ObjectHolder("orange" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block orangeShinyFlower;
	@ObjectHolder("magenta" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block magentaShinyFlower;
	@ObjectHolder("light_blue" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block lightBlueShinyFlower;
	@ObjectHolder("yellow" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block yellowShinyFlower;
	@ObjectHolder("lime" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block limeShinyFlower;
	@ObjectHolder("pink" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block pinkShinyFlower;
	@ObjectHolder("gray" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block grayShinyFlower;
	@ObjectHolder("light_gray" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block lightGrayShinyFlower;
	@ObjectHolder("cyan" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block cyanShinyFlower;
	@ObjectHolder("purple" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block purpleShinyFlower;
	@ObjectHolder("blue" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block blueShinyFlower;
	@ObjectHolder("brown" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block brownShinyFlower;
	@ObjectHolder("green" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block greenShinyFlower;
	@ObjectHolder("red" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block redShinyFlower;
	@ObjectHolder("black" + LibBlockNames.SHINY_FLOWER_SUFFIX) public static Block blackShinyFlower;
	@ObjectHolder(LibBlockNames.PLATFORM_ABSTRUSE) public static Block abstrusePlatform;
	@ObjectHolder(LibBlockNames.PLATFORM_SPECTRAL) public static Block spectralPlatform;
	@ObjectHolder(LibBlockNames.PLATFORM_INFRANGIBLE) public static Block infrangiblePlatform;
	@ObjectHolder(LibBlockNames.ALF_PORTAL) public static Block alfPortal;
	@ObjectHolder(LibBlockNames.DREAM_WOOD) public static Block dreamwood;
	@ObjectHolder(LibBlockNames.DREAM_WOOD_PLANKS) public static Block dreamwoodPlanks;
	@ObjectHolder(LibBlockNames.DREAM_WOOD_PLANKS_MOSSY) public static Block dreamwoodPlanksMossy;
	@ObjectHolder(LibBlockNames.DREAM_WOOD_FRAMED) public static Block dreamwoodFramed;
	@ObjectHolder(LibBlockNames.DREAM_WOOD_PATTERN_FRAMED) public static Block dreamwoodPatternFramed;
	@ObjectHolder(LibBlockNames.DREAM_WOOD_GLIMMERING) public static Block dreamwoodGlimmering;
	@ObjectHolder(LibBlockNames.CONJURATION_CATALYST) public static Block conjurationCatalyst;
	@ObjectHolder(LibBlockNames.BIFROST) public static Block bifrost;
	@ObjectHolder(LibBlockNames.SOLID_VINE) public static Block solidVines;
	@ObjectHolder("white" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block whiteBuriedPetals;
	@ObjectHolder("orange" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block orangeBuriedPetals;
	@ObjectHolder("magenta" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block magentaBuriedPetals;
	@ObjectHolder("light_blue" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block lightBlueBuriedPetals;
	@ObjectHolder("yellow" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block yellowBuriedPetals;
	@ObjectHolder("lime" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block limeBuriedPetals;
	@ObjectHolder("pink" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block pinkBuriedPetals;
	@ObjectHolder("gray" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block grayBuriedPetals;
	@ObjectHolder("light_gray" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block lightGrayBuriedPetals;
	@ObjectHolder("cyan" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block cyanBuriedPetals;
	@ObjectHolder("purple" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block purpleBuriedPetals;
	@ObjectHolder("blue" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block blueBuriedPetals;
	@ObjectHolder("brown" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block brownBuriedPetals;
	@ObjectHolder("green" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block greenBuriedPetals;
	@ObjectHolder("red" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block redBuriedPetals;
	@ObjectHolder("black" + LibBlockNames.BURIED_PETALS_SUFFIX) public static Block blackBuriedPetals;
	@ObjectHolder("white" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block whiteFloatingFlower;
	@ObjectHolder("orange" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block orangeFloatingFlower;
	@ObjectHolder("magenta" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block magentaFloatingFlower;
	@ObjectHolder("light_blue" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block lightBlueFloatingFlower;
	@ObjectHolder("yellow" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block yellowFloatingFlower;
	@ObjectHolder("lime" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block limeFloatingFlower;
	@ObjectHolder("pink" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block pinkFloatingFlower;
	@ObjectHolder("gray" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block grayFloatingFlower;
	@ObjectHolder("light_gray" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block lightGrayFloatingFlower;
	@ObjectHolder("cyan" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block cyanFloatingFlower;
	@ObjectHolder("purple" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block purpleFloatingFlower;
	@ObjectHolder("blue" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block blueFloatingFlower;
	@ObjectHolder("brown" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block brownFloatingFlower;
	@ObjectHolder("green" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block greenFloatingFlower;
	@ObjectHolder("red" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block redFloatingFlower;
	@ObjectHolder("black" + LibBlockNames.FLOATING_FLOWER_SUFFIX) public static Block blackFloatingFlower;
	@ObjectHolder(LibBlockNames.TINY_POTATO) public static Block tinyPotato;
	@ObjectHolder(LibBlockNames.SPAWNER_CLAW) public static Block spawnerClaw;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 0) public static Block azulejo0;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 1) public static Block azulejo1;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 2) public static Block azulejo2;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 3) public static Block azulejo3;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 4) public static Block azulejo4;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 5) public static Block azulejo5;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 6) public static Block azulejo6;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 7) public static Block azulejo7;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 8) public static Block azulejo8;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 9) public static Block azulejo9;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 10) public static Block azulejo10;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 11) public static Block azulejo11;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 12) public static Block azulejo12;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 13) public static Block azulejo13;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 14) public static Block azulejo14;
	@ObjectHolder(LibBlockNames.AZULEJO_PREFIX + 15) public static Block azulejo15;
	@ObjectHolder(LibBlockNames.ENDER_EYE_BLOCK) public static Block enderEye;
	@ObjectHolder(LibBlockNames.STARFIELD) public static Block starfield;
	@ObjectHolder(LibBlockNames.FLUXFIELD) public static Block rfGenerator;
	@ObjectHolder(LibBlockNames.ELF_GLASS) public static Block elfGlass;
	@ObjectHolder(LibBlockNames.BREWERY) public static Block brewery;
	@ObjectHolder(LibBlockNames.MANA_GLASS) public static Block manaGlass;
	@ObjectHolder(LibBlockNames.TERRA_PLATE) public static Block terraPlate;
	@ObjectHolder(LibBlockNames.RED_STRING_CONTAINER) public static Block redStringContainer;
	@ObjectHolder(LibBlockNames.RED_STRING_DISPENSER) public static Block redStringDispenser;
	@ObjectHolder(LibBlockNames.RED_STRING_FERTILIZER) public static Block redStringFertilizer;
	@ObjectHolder(LibBlockNames.RED_STRING_COMPARATOR) public static Block redStringComparator;
	@ObjectHolder(LibBlockNames.RED_STRING_RELAY) public static Block redStringRelay;
	@ObjectHolder(LibBlockNames.FLOATING_SPECIAL_FLOWER) public static Block floatingSpecialFlower;
	@ObjectHolder(LibBlockNames.MANA_FLAME) public static Block manaFlame;
	@ObjectHolder(LibBlockNames.PRISM) public static Block prism;
	@ObjectHolder(LibBlockNames.ENCHANTED_SOIL) public static Block enchantedSoil;
	@ObjectHolder("white" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockWhite;
	@ObjectHolder("orange" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockOrange;
	@ObjectHolder("magenta" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockMagenta;
	@ObjectHolder("light_blue" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockLightBlue;
	@ObjectHolder("yellow" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockYellow;
	@ObjectHolder("lime" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockLime;
	@ObjectHolder("pink" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockPink;
	@ObjectHolder("gray" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockGray;
	@ObjectHolder("light_gray" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockSilver;
	@ObjectHolder("cyan" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockCyan;
	@ObjectHolder("purple" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockPurple;
	@ObjectHolder("blue" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockBlue;
	@ObjectHolder("brown" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockBrown;
	@ObjectHolder("green" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockGreen;
	@ObjectHolder("red" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockRed;
	@ObjectHolder("black" + LibBlockNames.PETAL_BLOCK_SUFFIX) public static Block petalBlockBlack;
	@ObjectHolder(LibBlockNames.CORPOREA_INDEX) public static Block corporeaIndex;
	@ObjectHolder(LibBlockNames.CORPOREA_FUNNEL) public static Block corporeaFunnel;
	@ObjectHolder("white" + LibBlockNames.MUSHROOM_SUFFIX) public static Block whiteMushroom;
	@ObjectHolder("orange" + LibBlockNames.MUSHROOM_SUFFIX) public static Block orangeMushroom;
	@ObjectHolder("magenta" + LibBlockNames.MUSHROOM_SUFFIX) public static Block magentaMushroom;
	@ObjectHolder("light_blue" + LibBlockNames.MUSHROOM_SUFFIX) public static Block lightBlueMushroom;
	@ObjectHolder("yellow" + LibBlockNames.MUSHROOM_SUFFIX) public static Block yellowMushroom;
	@ObjectHolder("lime" + LibBlockNames.MUSHROOM_SUFFIX) public static Block limeMushroom;
	@ObjectHolder("pink" + LibBlockNames.MUSHROOM_SUFFIX) public static Block pinkMushroom;
	@ObjectHolder("gray" + LibBlockNames.MUSHROOM_SUFFIX) public static Block grayMushroom;
	@ObjectHolder("light_gray" + LibBlockNames.MUSHROOM_SUFFIX) public static Block lightGrayMushroom;
	@ObjectHolder("cyan" + LibBlockNames.MUSHROOM_SUFFIX) public static Block cyanMushroom;
	@ObjectHolder("purple" + LibBlockNames.MUSHROOM_SUFFIX) public static Block purpleMushroom;
	@ObjectHolder("blue" + LibBlockNames.MUSHROOM_SUFFIX) public static Block blueMushroom;
	@ObjectHolder("brown" + LibBlockNames.MUSHROOM_SUFFIX) public static Block brownMushroom;
	@ObjectHolder("green" + LibBlockNames.MUSHROOM_SUFFIX) public static Block greenMushroom;
	@ObjectHolder("red" + LibBlockNames.MUSHROOM_SUFFIX) public static Block redMushroom;
	@ObjectHolder("black" + LibBlockNames.MUSHROOM_SUFFIX) public static Block blackMushroom;
	@ObjectHolder(LibBlockNames.PUMP) public static Block pump;
	@ObjectHolder("white" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerWhite;
	@ObjectHolder("orange" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerOrange;
	@ObjectHolder("magenta" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerMagenta;
	@ObjectHolder("light_blue" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerLightBlue;
	@ObjectHolder("yellow" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerYellow;
	@ObjectHolder("lime" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerLime;
	@ObjectHolder("pink" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerPink;
	@ObjectHolder("gray" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerGray;
	@ObjectHolder("light_gray" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerLightGray;
	@ObjectHolder("cyan" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerCyan;
	@ObjectHolder("purple" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerPurple;
	@ObjectHolder("blue" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerBlue;
	@ObjectHolder("brown" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerBrown;
	@ObjectHolder("green" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerGreen;
	@ObjectHolder("red" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerRed;
	@ObjectHolder("black" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerBlack;
	@ObjectHolder(LibBlockNames.FAKE_AIR) public static Block fakeAir;
	@ObjectHolder(LibBlockNames.BLAZE_BLOCK) public static Block blazeBlock;
	@ObjectHolder(LibBlockNames.CORPOREA_INTERCEPTOR) public static Block corporeaInterceptor;
	@ObjectHolder(LibBlockNames.CORPOREA_CRYSTAL_CUBE) public static Block corporeaCrystalCube;
	@ObjectHolder(LibBlockNames.INCENSE_PLATE) public static Block incensePlate;
	@ObjectHolder(LibBlockNames.HOURGLASS) public static Block hourglass;
	@ObjectHolder(LibBlockNames.GHOST_RAIL) public static Block ghostRail;
	@ObjectHolder(LibBlockNames.SPARK_CHANGER) public static Block sparkChanger;
	@ObjectHolder(LibBlockNames.ROOT) public static Block root;
	@ObjectHolder(LibBlockNames.FEL_PUMPKIN) public static Block felPumpkin;
	@ObjectHolder(LibBlockNames.COCOON) public static Block cocoon;
	@ObjectHolder(LibBlockNames.LIGHT_RELAY) public static Block lightRelayDefault;
	@ObjectHolder("detector" + LibBlockNames.LIGHT_RELAY_SUFFIX) public static Block lightRelayDetector;
	@ObjectHolder("toggle" + LibBlockNames.LIGHT_RELAY_SUFFIX) public static Block lightRelayToggle;
	@ObjectHolder("fork" + LibBlockNames.LIGHT_RELAY_SUFFIX) public static Block lightRelayFork;
	@ObjectHolder(LibBlockNames.LIGHT_LAUNCHER) public static Block lightLauncher;
	@ObjectHolder(LibBlockNames.MANA_BOMB) public static Block manaBomb;
	@ObjectHolder(LibBlockNames.CACOPHONIUM) public static Block cacophonium;
	@ObjectHolder(LibBlockNames.BELLOWS) public static Block bellows;
	@ObjectHolder(LibBlockNames.BIFROST_PERM) public static Block bifrostPerm;
	@ObjectHolder(LibBlockNames.CELL_BLOCK) public static Block cellBlock;
	@ObjectHolder(LibBlockNames.RED_STRING_INTERCEPTOR) public static Block redStringInterceptor;
	@ObjectHolder(LibBlockNames.GAIA_HEAD) public static Block gaiaHead;
	@ObjectHolder(LibBlockNames.GAIA_WALL_HEAD) public static Block gaiaHeadWall;
	@ObjectHolder(LibBlockNames.CORPOREA_RETAINER) public static Block corporeaRetainer;
	@ObjectHolder(LibBlockNames.TERU_TERU_BOZU) public static Block teruTeruBozu;
	@ObjectHolder(LibBlockNames.SHIMMERROCK) public static Block shimmerrock;
	@ObjectHolder(LibBlockNames.SHIMMERWOOD_PLANKS) public static Block shimmerwoodPlanks;
	@ObjectHolder(LibBlockNames.AVATAR) public static Block avatar;
	@ObjectHolder("dry" + LibBlockNames.ALT_GRASS_SUFFIX) public static Block dryGrass;
	@ObjectHolder("golden" + LibBlockNames.ALT_GRASS_SUFFIX) public static Block goldenGrass;
	@ObjectHolder("vivid" + LibBlockNames.ALT_GRASS_SUFFIX) public static Block vividGrass;
	@ObjectHolder("scorched" + LibBlockNames.ALT_GRASS_SUFFIX) public static Block scorchedGrass;
	@ObjectHolder("infused" + LibBlockNames.ALT_GRASS_SUFFIX) public static Block infusedGrass;
	@ObjectHolder("mutated" + LibBlockNames.ALT_GRASS_SUFFIX) public static Block mutatedGrass;
	@ObjectHolder(LibBlockNames.ANIMATED_TORCH) public static Block animatedTorch;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> evt) {
		IForgeRegistry<Block> r = evt.getRegistry();
		ILexiconable decorative = (w, po, pl, st) -> LexiconData.decorativeBlocks;
		ILexiconable elvenResource = (w, po, pl, st) -> LexiconData.elvenResources;

		Block.Properties builder = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT);
		for(EnumDyeColor color : EnumDyeColor.values()) {
			register(r, new BlockModFlower(color, builder), color.getName() + LibBlockNames.MYSTICAL_FLOWER_SUFFIX);
		}

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).sound(SoundType.STONE);
		for(BlockAltar.Variant v : BlockAltar.Variant.values()) {
			r.register(new BlockAltar(v, builder)
					.setRegistryName(LibMisc.MOD_ID, LibBlockNames.APOTHECARY_PREFIX + v.name().toLowerCase(Locale.ROOT)));
		}

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		register(r, new BlockModLexiconable(builder, (w, po, pl, st) -> LexiconData.pureDaisy), LibBlockNames.LIVING_ROCK);
		register(r, new BlockModLexiconable(builder, decorative), LibBlockNames.LIVING_ROCK_BRICK);
		register(r, new BlockModLexiconable(builder, decorative), LibBlockNames.LIVING_ROCK_BRICK_MOSSY);
		register(r, new BlockModLexiconable(builder, decorative), LibBlockNames.LIVING_ROCK_BRICK_CRACKED);
		register(r, new BlockModLexiconable(builder, decorative), LibBlockNames.LIVING_ROCK_BRICK_CHISELED);

		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD);
		// todo 1.13: livingwood should support leaves
		register(r, new BlockModLexiconable(builder, (w, po, pl, st) -> LexiconData.pureDaisy), LibBlockNames.LIVING_WOOD);
		register(r, new BlockModLexiconable(builder, decorative), LibBlockNames.LIVING_WOOD_PLANKS);
		register(r, new BlockModLexiconable(builder, decorative), LibBlockNames.LIVING_WOOD_PLANKS_MOSSY);
		register(r, new BlockModLexiconable(builder, decorative), LibBlockNames.LIVING_WOOD_FRAMED);
		register(r, new BlockModLexiconable(builder, decorative), LibBlockNames.LIVING_WOOD_PATTERN_FRAMED);
		register(r, new BlockModLexiconable(builder.lightValue(12), decorative), LibBlockNames.LIVING_WOOD_GLIMMERING);
		register(r, new BlockSpecialFlower(Block.Properties.from(Blocks.POPPY)), LibBlockNames.SPECIAL_FLOWER);

		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD);
		register(r, new BlockSpreader(BlockSpreader.Variant.MANA, builder), LibBlockNames.SPREADER);
		register(r, new BlockSpreader(BlockSpreader.Variant.REDSTONE, builder), LibBlockNames.SPREADER_REDSTONE);
		register(r, new BlockSpreader(BlockSpreader.Variant.ELVEN, builder), LibBlockNames.SPREADER_ELVEN);
		register(r, new BlockSpreader(BlockSpreader.Variant.GAIA, builder), LibBlockNames.SPREADER_GAIA);

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		register(r, new BlockPool(BlockPool.Variant.DEFAULT, builder), LibBlockNames.POOL);
		register(r, new BlockPool(BlockPool.Variant.CREATIVE, builder), LibBlockNames.POOL_CREATIVE);
		register(r, new BlockPool(BlockPool.Variant.DILUTED, builder), LibBlockNames.POOL_DILUTED);
		register(r, new BlockPool(BlockPool.Variant.FABULOUS, builder), LibBlockNames.POOL_FABULOUS);

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		register(r, new BlockRuneAltar(builder), LibBlockNames.RUNE_ALTAR);

		builder = Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL).lightValue(7);
		register(r, new BlockPylon(BlockPylon.Variant.MANA, builder), LibBlockNames.PYLON);
		register(r, new BlockPylon(BlockPylon.Variant.NATURA, builder), LibBlockNames.PYLON_NATURA);
		register(r, new BlockPylon(BlockPylon.Variant.GAIA, builder), LibBlockNames.PYLON_GAIA);

		builder = Block.Properties.create(Material.GOURD).hardnessAndResistance(2, 10).sound(SoundType.METAL);
		register(r, new BlockPistonRelay(builder), LibBlockNames.PISTON_RELAY);

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		register(r, new BlockDistributor(builder), LibBlockNames.DISTRIBUTOR);

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 2000).sound(SoundType.STONE);
		register(r, new BlockManaVoid(builder), LibBlockNames.MANA_VOID);

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		register(r, new BlockManaDetector(builder), LibBlockNames.MANA_DETECTOR);

		register(r, new BlockEnchanter(Block.Properties.create(Material.ROCK).hardnessAndResistance(3, 5).lightValue(15).sound(SoundType.STONE)), LibBlockNames.ENCHANTER);
		register(r, new BlockTurntable(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)), LibBlockNames.TURNTABLE);
		register(r, new BlockTinyPlanet(Block.Properties.create(Material.ROCK).hardnessAndResistance(20, 100).sound(SoundType.STONE)), LibBlockNames.TINY_PLANET);
		register(r, new BlockAlchemyCatalyst(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)), LibBlockNames.ALCHEMY_CATALYST);
		
		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD);
		register(r, new BlockOpenCrate(builder), LibBlockNames.OPEN_CRATE);
		register(r, new BlockCraftyCrate(builder), LibBlockNames.CRAFT_CRATE);
		
		register(r, new BlockForestEye(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL)), LibBlockNames.FOREST_EYE);

		builder = Block.Properties.create(Material.IRON).hardnessAndResistance(3, 10).sound(SoundType.METAL);
		register(r, new BlockStorage(builder), LibBlockNames.MANASTEEL_BLOCK);
		register(r, new BlockStorage(builder), LibBlockNames.TERRASTEEL_BLOCK);
		register(r, new BlockStorage(builder), LibBlockNames.ELEMENTIUM_BLOCK);
		register(r, new BlockStorage(builder), LibBlockNames.MANA_DIAMOND_BLOCK);
		register(r, new BlockStorage(builder), LibBlockNames.DRAGONSTONE_BLOCK);

		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD);
		register(r, new BlockForestDrum(BlockForestDrum.Variant.WILD, builder), LibBlockNames.DRUM_WILD);
		register(r, new BlockForestDrum(BlockForestDrum.Variant.CANOPY, builder), LibBlockNames.DRUM_CANOPY);
		register(r, new BlockForestDrum(BlockForestDrum.Variant.GATHERING, builder), LibBlockNames.DRUM_GATHERING);
		
		builder = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT).lightValue(15);
		for (EnumDyeColor color : EnumDyeColor.values()) {
			register(r, new BlockShinyFlower(color, builder), color.getName() + LibBlockNames.SHINY_FLOWER_SUFFIX);
		}
		
		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2, 5).sound(SoundType.WOOD);
		register(r, new BlockPlatform(BlockPlatform.Variant.ABSTRUSE, builder), LibBlockNames.PLATFORM_ABSTRUSE);
		register(r, new BlockPlatform(BlockPlatform.Variant.SPECTRAL, builder), LibBlockNames.PLATFORM_SPECTRAL);
		register(r, new BlockPlatform(BlockPlatform.Variant.INFRANGIBLE, Block.Properties.create(Material.WOOD).hardnessAndResistance(-1, Float.MAX_VALUE).sound(SoundType.WOOD)), LibBlockNames.PLATFORM_INFRANGIBLE);
		register(r, new BlockAlfPortal(Block.Properties.create(Material.WOOD).hardnessAndResistance(10).sound(SoundType.WOOD)), LibBlockNames.ALF_PORTAL);

		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD);
		// todo 1.13: dreamwood should support leaves?
		register(r, new BlockModLexiconable(builder, elvenResource), LibBlockNames.DREAM_WOOD);
		register(r, new BlockModLexiconable(builder, elvenResource), LibBlockNames.DREAM_WOOD_PLANKS);
		register(r, new BlockModLexiconable(builder, elvenResource), LibBlockNames.DREAM_WOOD_PLANKS_MOSSY);
		register(r, new BlockModLexiconable(builder, elvenResource), LibBlockNames.DREAM_WOOD_FRAMED);
		register(r, new BlockModLexiconable(builder, elvenResource), LibBlockNames.DREAM_WOOD_PATTERN_FRAMED);
		register(r, new BlockModLexiconable(builder.lightValue(12), elvenResource), LibBlockNames.DREAM_WOOD_GLIMMERING);

		register(r, new BlockConjurationCatalyst(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)), LibBlockNames.CONJURATION_CATALYST);
		register(r, new BlockBifrost(Block.Properties.create(Material.GLASS).hardnessAndResistance(-1, 0.3F).lightValue(15).sound(SoundType.GLASS)), LibBlockNames.BIFROST);
		register(r, new BlockSolidVines(Block.Properties.create(Material.VINE).hardnessAndResistance(0.2F).sound(SoundType.PLANT)), LibBlockNames.SOLID_VINE);
		
		builder = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT).lightValue(4);
		for(EnumDyeColor color : EnumDyeColor.values()) {
			register(r, new BlockBuriedPetals(color, builder), color.getName() + LibBlockNames.BURIED_PETALS_SUFFIX);
		}

		builder = Block.Properties.create(Material.GROUND).hardnessAndResistance(0.5F).sound(SoundType.GROUND).lightValue(15);
		register(r, new BlockFloatingSpecialFlower(builder), LibBlockNames.FLOATING_SPECIAL_FLOWER);
		for(EnumDyeColor color : EnumDyeColor.values()) {
			register(r, new BlockFloatingFlower(color, builder), color.getName() + LibBlockNames.FLOATING_FLOWER_SUFFIX);
		}

		register(r, new BlockTinyPotato(Block.Properties.create(Material.CLOTH).hardnessAndResistance(0.25F)), LibBlockNames.TINY_POTATO);
		register(r, new BlockSpawnerClaw(Block.Properties.create(Material.IRON).hardnessAndResistance(3)), LibBlockNames.SPAWNER_CLAW);

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 5).sound(SoundType.STONE);
		ILexiconable azulejo = (w, po ,pl, st) -> LexiconData.azulejo;
		for (int i = 0; i < 16; i++) {
			register(r, new BlockModLexiconable(builder, azulejo), LibBlockNames.AZULEJO_PREFIX + i);
		}

		register(r, new BlockEnderEye(Block.Properties.create(Material.IRON).hardnessAndResistance(3, 10).sound(SoundType.METAL)), LibBlockNames.ENDER_EYE_BLOCK);
		register(r, new BlockStarfield(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 2000).sound(SoundType.METAL)), LibBlockNames.STARFIELD);
		register(r, new BlockRFGenerator(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)), LibBlockNames.FLUXFIELD);
		register(r, new BlockElfGlass(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).lightValue(15)), LibBlockNames.ELF_GLASS);
		register(r, new BlockBrewery(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)), LibBlockNames.BREWERY);
		register(r, new BlockManaGlass(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).lightValue(15)), LibBlockNames.MANA_GLASS);
		register(r, new BlockTerraPlate(Block.Properties.create(Material.IRON).hardnessAndResistance(3, 10).sound(SoundType.METAL)), LibBlockNames.TERRA_PLATE);

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		register(r, new BlockRedStringContainer(builder), LibBlockNames.RED_STRING_CONTAINER);
		register(r, new BlockRedStringDispenser(builder), LibBlockNames.RED_STRING_DISPENSER);
		register(r, new BlockRedStringFertilizer(builder), LibBlockNames.RED_STRING_FERTILIZER);
		register(r, new BlockRedStringComparator(builder), LibBlockNames.RED_STRING_COMPARATOR);
		register(r, new BlockRedStringRelay(builder), LibBlockNames.RED_STRING_RELAY);
		register(r, new BlockRedStringInterceptor(builder), LibBlockNames.RED_STRING_INTERCEPTOR);
		register(r, new BlockManaFlame(Block.Properties.create(Material.CLOTH).sound(SoundType.CLOTH).lightValue(15).doesNotBlockMovement()), LibBlockNames.MANA_FLAME);
		register(r, new BlockPrism(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).lightValue(15).doesNotBlockMovement()), LibBlockNames.PRISM);
		register(r, new BlockEnchantedSoil(Block.Properties.create(Material.GRASS).hardnessAndResistance(0.6F).sound(SoundType.PLANT)), LibBlockNames.ENCHANTED_SOIL);

		builder = Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.4F).sound(SoundType.PLANT);
		for(EnumDyeColor color : EnumDyeColor.values()) {
			register(r, new BlockPetalBlock(color, builder), color.getName() + LibBlockNames.PETAL_BLOCK_SUFFIX);
		}

		register(r, new BlockCorporeaIndex(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL)), LibBlockNames.CORPOREA_INDEX);
		register(r, new BlockCorporeaFunnel(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL)), LibBlockNames.CORPOREA_FUNNEL);
		
		builder = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT).lightValue(3);
		for(EnumDyeColor color : EnumDyeColor.values()) {
			register(r, new BlockModMushroom(color, builder), color.getName() + LibBlockNames.MUSHROOM_SUFFIX);
		}
		
		register(r, new BlockPump(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)), LibBlockNames.PUMP);

		builder = Block.Properties.create(Material.VINE).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT);
		for(EnumDyeColor color : EnumDyeColor.values()) {
			register(r, new BlockModDoubleFlower(color, builder), color.getName() + LibBlockNames.DOUBLE_FLOWER_SUFFIX);
		}

		register(r, new BlockFakeAir(Block.Properties.create(Material.STRUCTURE_VOID).tickRandomly()), LibBlockNames.FAKE_AIR);
		register(r, new BlockModLexiconable(Block.Properties.create(Material.IRON).hardnessAndResistance(3, 10).sound(SoundType.METAL).lightValue(15), (w, po, pl, st) -> LexiconData.blazeBlock), LibBlockNames.BLAZE_BLOCK);
		register(r, new BlockCorporeaInterceptor(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL)), LibBlockNames.CORPOREA_INTERCEPTOR);
		register(r, new BlockCorporeaCrystalCube(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL)), LibBlockNames.CORPOREA_CRYSTAL_CUBE);
		register(r, new BlockIncensePlate(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)), LibBlockNames.INCENSE_PLATE);
		register(r, new BlockHourglass(Block.Properties.create(Material.IRON).hardnessAndResistance(2).sound(SoundType.METAL)), LibBlockNames.HOURGLASS);
		register(r, new BlockGhostRail(Block.Properties.create(Material.CIRCUITS).doesNotBlockMovement().hardnessAndResistance(0.7F).sound(SoundType.METAL)), LibBlockNames.GHOST_RAIL);
		register(r, new BlockSparkChanger(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)), LibBlockNames.SPARK_CHANGER);
		register(r, new BlockRoot(Block.Properties.create(Material.PLANTS).hardnessAndResistance(1.2F).sound(SoundType.WOOD)), LibBlockNames.ROOT);
		register(r, new BlockFelPumpkin(Block.Properties.from(Blocks.CARVED_PUMPKIN)), LibBlockNames.FEL_PUMPKIN);
		register(r, new BlockCocoon(Block.Properties.create(Material.CLOTH).hardnessAndResistance(3, 60).sound(SoundType.CLOTH)), LibBlockNames.COCOON);

		builder = Block.Properties.create(Material.GLASS).doesNotBlockMovement();
		register(r, new BlockLightRelay(LuminizerVariant.DEFAULT, builder), LibBlockNames.LIGHT_RELAY);
		register(r, new BlockLightRelay(LuminizerVariant.DETECTOR, builder), "detector" + LibBlockNames.LIGHT_RELAY_SUFFIX);
		register(r, new BlockLightRelay(LuminizerVariant.FORK, builder), "fork" + LibBlockNames.LIGHT_RELAY_SUFFIX);
		register(r, new BlockLightRelay(LuminizerVariant.TOGGLE, builder), "toggle" + LibBlockNames.LIGHT_RELAY_SUFFIX);

		register(r, new BlockLightLauncher(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)), LibBlockNames.LIGHT_LAUNCHER);
		register(r, new BlockManaBomb(Block.Properties.create(Material.WOOD).hardnessAndResistance(12).sound(SoundType.WOOD)), LibBlockNames.MANA_BOMB);
		register(r, new BlockCacophonium(Block.Properties.create(Material.WOOD).hardnessAndResistance(0.8F)), LibBlockNames.CACOPHONIUM);
		register(r, new BlockBellows(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)), LibBlockNames.BELLOWS);
		register(r, new BlockBifrostPerm(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).lightValue(15).sound(SoundType.GLASS)), LibBlockNames.BIFROST_PERM);
		register(r, new BlockCell(Block.Properties.create(Material.GOURD).sound(SoundType.CLOTH)), LibBlockNames.CELL_BLOCK);
		register(r, new BlockGaiaHeadWall(Block.Properties.create(Material.CIRCUITS).hardnessAndResistance(1)), LibBlockNames.GAIA_WALL_HEAD);
		register(r, new BlockGaiaHead(Block.Properties.create(Material.CIRCUITS).hardnessAndResistance(1)), LibBlockNames.GAIA_HEAD);
		register(r, new BlockCorporeaRetainer(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL)), LibBlockNames.CORPOREA_RETAINER);
		register(r, new BlockTeruTeruBozu(Block.Properties.create(Material.CLOTH)), LibBlockNames.TERU_TERU_BOZU);
		ILexiconable rainbowRod = (w, po, pl, st) -> LexiconData.rainbowRod;
		register(r, new BlockModLexiconable(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE), rainbowRod), LibBlockNames.SHIMMERROCK);
		register(r, new BlockModLexiconable(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD), rainbowRod), LibBlockNames.SHIMMERWOOD_PLANKS);
		register(r, new BlockAvatar(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)), LibBlockNames.AVATAR);

		builder = Block.Properties.create(Material.GRASS).hardnessAndResistance(0.6F).tickRandomly().sound(SoundType.PLANT);
		for(BlockAltGrass.Variant v : BlockAltGrass.Variant.values()) {
			register(r, new BlockAltGrass(v, builder), v.name().toLowerCase(Locale.ROOT) + LibBlockNames.ALT_GRASS_SUFFIX);
		}

		register(r, new BlockAnimatedTorch(Block.Properties.create(Material.CIRCUITS).lightValue(7)), LibBlockNames.ANIMATED_TORCH);
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		Item.Properties props = ModItems.defaultBuilder();
		
		r.register(new ItemBlockMod(whiteFlower, props).setRegistryName(whiteFlower.getRegistryName()));
		r.register(new ItemBlockMod(orangeFlower, props).setRegistryName(orangeFlower.getRegistryName()));
		r.register(new ItemBlockMod(magentaFlower, props).setRegistryName(magentaFlower.getRegistryName()));
		r.register(new ItemBlockMod(lightBlueFlower, props).setRegistryName(lightBlueFlower.getRegistryName()));
		r.register(new ItemBlockMod(yellowFlower, props).setRegistryName(yellowFlower.getRegistryName()));
		r.register(new ItemBlockMod(limeFlower, props).setRegistryName(limeFlower.getRegistryName()));
		r.register(new ItemBlockMod(pinkFlower, props).setRegistryName(pinkFlower.getRegistryName()));
		r.register(new ItemBlockMod(grayFlower, props).setRegistryName(grayFlower.getRegistryName()));
		r.register(new ItemBlockMod(lightGrayFlower, props).setRegistryName(lightGrayFlower.getRegistryName()));
		r.register(new ItemBlockMod(cyanFlower, props).setRegistryName(cyanFlower.getRegistryName()));
		r.register(new ItemBlockMod(purpleFlower, props).setRegistryName(purpleFlower.getRegistryName()));
		r.register(new ItemBlockMod(blueFlower, props).setRegistryName(blueFlower.getRegistryName()));
		r.register(new ItemBlockMod(brownFlower, props).setRegistryName(brownFlower.getRegistryName()));
		r.register(new ItemBlockMod(greenFlower, props).setRegistryName(greenFlower.getRegistryName()));
		r.register(new ItemBlockMod(redFlower, props).setRegistryName(redFlower.getRegistryName()));
		r.register(new ItemBlockMod(blackFlower, props).setRegistryName(blackFlower.getRegistryName()));
		r.register(new ItemBlockMod(defaultAltar, props).setRegistryName(defaultAltar.getRegistryName()));
		r.register(new ItemBlockMod(forestAltar, props).setRegistryName(forestAltar.getRegistryName()));
		r.register(new ItemBlockMod(plainsAltar, props).setRegistryName(plainsAltar.getRegistryName()));
		r.register(new ItemBlockMod(mountainAltar, props).setRegistryName(mountainAltar.getRegistryName()));
		r.register(new ItemBlockMod(fungalAltar, props).setRegistryName(fungalAltar.getRegistryName()));
		r.register(new ItemBlockMod(swampAltar, props).setRegistryName(swampAltar.getRegistryName()));
		r.register(new ItemBlockMod(desertAltar, props).setRegistryName(desertAltar.getRegistryName()));
		r.register(new ItemBlockMod(taigaAltar, props).setRegistryName(taigaAltar.getRegistryName()));
		r.register(new ItemBlockMod(mesaAltar, props).setRegistryName(mesaAltar.getRegistryName()));
		r.register(new ItemBlockMod(mossyAltar, props).setRegistryName(mossyAltar.getRegistryName()));
		r.register(new ItemBlockMod(livingrock, props).setRegistryName(livingrock.getRegistryName()));
		r.register(new ItemBlockMod(livingrockBrick, props).setRegistryName(livingrockBrick.getRegistryName()));
		r.register(new ItemBlockMod(livingrockBrickChiseled, props).setRegistryName(livingrockBrickChiseled.getRegistryName()));
		r.register(new ItemBlockMod(livingrockBrickCracked, props).setRegistryName(livingrockBrickCracked.getRegistryName()));
		r.register(new ItemBlockMod(livingrockBrickMossy, props).setRegistryName(livingrockBrickMossy.getRegistryName()));
		r.register(new ItemBlockMod(livingwood, props).setRegistryName(livingwood.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodPlanks, props).setRegistryName(livingwoodPlanks.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodPlanksMossy, props).setRegistryName(livingwoodPlanksMossy.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodFramed, props).setRegistryName(livingwoodFramed.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodPatternFramed, props).setRegistryName(livingwoodPatternFramed.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodGlimmering, props).setRegistryName(livingwoodGlimmering.getRegistryName()));
		r.register(new ItemBlockSpecialFlower(specialFlower, props).setRegistryName(specialFlower.getRegistryName()));
		r.register(new ItemBlockMod(manaSpreader, props).setRegistryName(manaSpreader.getRegistryName()));
		r.register(new ItemBlockMod(redstoneSpreader, props).setRegistryName(redstoneSpreader.getRegistryName()));
		r.register(new ItemBlockMod(elvenSpreader, props).setRegistryName(elvenSpreader.getRegistryName()));
		r.register(new ItemBlockMod(gaiaSpreader, props).setRegistryName(gaiaSpreader.getRegistryName()));
		r.register(new ItemBlockPool(manaPool, props).setRegistryName(manaPool.getRegistryName()));
		r.register(new ItemBlockPool(creativePool, props).setRegistryName(creativePool.getRegistryName()));
		r.register(new ItemBlockPool(dilutedPool, props).setRegistryName(dilutedPool.getRegistryName()));
		r.register(new ItemBlockPool(fabulousPool, props).setRegistryName(fabulousPool.getRegistryName()));
		r.register(new ItemBlockMod(runeAltar, props).setRegistryName(runeAltar.getRegistryName()));
		Item.Properties pylonProps = ModItems.defaultBuilder().setTEISR(() -> RenderTilePylon.TEISR::new);
		r.register(new ItemBlockMod(manaPylon, pylonProps).setRegistryName(manaPylon.getRegistryName()));
		r.register(new ItemBlockMod(naturaPylon, pylonProps).setRegistryName(naturaPylon.getRegistryName()));
		r.register(new ItemBlockMod(gaiaPylon, pylonProps).setRegistryName(gaiaPylon.getRegistryName()));
		r.register(new ItemBlockMod(pistonRelay, props).setRegistryName(pistonRelay.getRegistryName()));
		r.register(new ItemBlockMod(distributor, props).setRegistryName(distributor.getRegistryName()));
		r.register(new ItemBlockMod(manaVoid, props).setRegistryName(manaVoid.getRegistryName()));
		r.register(new ItemBlockMod(manaDetector, props).setRegistryName(manaDetector.getRegistryName()));
		r.register(new ItemBlockMod(enchanter, props).setRegistryName(enchanter.getRegistryName()));
		r.register(new ItemBlockMod(turntable, props).setRegistryName(turntable.getRegistryName()));
		r.register(new ItemBlockMod(tinyPlanet, props).setRegistryName(tinyPlanet.getRegistryName()));
		r.register(new ItemBlockMod(alchemyCatalyst, props).setRegistryName(alchemyCatalyst.getRegistryName()));
		r.register(new ItemBlockMod(openCrate, props).setRegistryName(openCrate.getRegistryName()));
		r.register(new ItemBlockMod(craftCrate, props).setRegistryName(craftCrate.getRegistryName()));
		r.register(new ItemBlockMod(forestEye, props).setRegistryName(forestEye.getRegistryName()));
		r.register(new ItemBlockMod(manasteelBlock, props).setRegistryName(manasteelBlock.getRegistryName()));
		r.register(new ItemBlockMod(terrasteelBlock, props).setRegistryName(terrasteelBlock.getRegistryName()));
		r.register(new ItemBlockElven(elementiumBlock, props).setRegistryName(elementiumBlock.getRegistryName()));
		r.register(new ItemBlockMod(manaDiamondBlock, props).setRegistryName(manaDiamondBlock.getRegistryName()));
		r.register(new ItemBlockMod(dragonstoneBlock, props).setRegistryName(dragonstoneBlock.getRegistryName()));
		r.register(new ItemBlockMod(wildDrum, props).setRegistryName(wildDrum.getRegistryName()));
		r.register(new ItemBlockMod(gatheringDrum, props).setRegistryName(gatheringDrum.getRegistryName()));
		r.register(new ItemBlockMod(canopyDrum, props).setRegistryName(canopyDrum.getRegistryName()));
		r.register(new ItemBlockMod(whiteShinyFlower, props).setRegistryName(whiteShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(orangeShinyFlower, props).setRegistryName(orangeShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(magentaShinyFlower, props).setRegistryName(magentaShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(lightBlueShinyFlower, props).setRegistryName(lightBlueShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(yellowShinyFlower, props).setRegistryName(yellowShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(limeShinyFlower, props).setRegistryName(limeShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(pinkShinyFlower, props).setRegistryName(pinkShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(grayShinyFlower, props).setRegistryName(grayShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(lightGrayShinyFlower, props).setRegistryName(lightGrayShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(cyanShinyFlower, props).setRegistryName(cyanShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(purpleShinyFlower, props).setRegistryName(purpleShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(blueShinyFlower, props).setRegistryName(blueShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(brownShinyFlower, props).setRegistryName(brownShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(greenShinyFlower, props).setRegistryName(greenShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(redShinyFlower, props).setRegistryName(redShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(blackShinyFlower, props).setRegistryName(blackShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(abstrusePlatform, props).setRegistryName(abstrusePlatform.getRegistryName()));
		r.register(new ItemBlockMod(spectralPlatform, props).setRegistryName(spectralPlatform.getRegistryName()));
		r.register(new ItemBlockMod(infrangiblePlatform, props).setRegistryName(infrangiblePlatform.getRegistryName()));
		r.register(new ItemBlockMod(alfPortal, props).setRegistryName(alfPortal.getRegistryName()));
		r.register(new ItemBlockDreamwood(dreamwood, props).setRegistryName(dreamwood.getRegistryName()));
		r.register(new ItemBlockDreamwood(dreamwoodPlanks, props).setRegistryName(dreamwoodPlanks.getRegistryName()));
		r.register(new ItemBlockDreamwood(dreamwoodPlanksMossy, props).setRegistryName(dreamwoodPlanksMossy.getRegistryName()));
		r.register(new ItemBlockDreamwood(dreamwoodFramed, props).setRegistryName(dreamwoodFramed.getRegistryName()));
		r.register(new ItemBlockDreamwood(dreamwoodPatternFramed, props).setRegistryName(dreamwoodPatternFramed.getRegistryName()));
		r.register(new ItemBlockDreamwood(dreamwoodGlimmering, props).setRegistryName(dreamwoodGlimmering.getRegistryName()));
		r.register(new ItemBlockMod(conjurationCatalyst, props).setRegistryName(conjurationCatalyst.getRegistryName()));
		r.register(new ItemBlockMod(bifrost, props).setRegistryName(bifrost.getRegistryName()));
		r.register(new ItemBlockMod(whiteFloatingFlower, props).setRegistryName(whiteFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(orangeFloatingFlower, props).setRegistryName(orangeFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(magentaFloatingFlower, props).setRegistryName(magentaFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(lightBlueFloatingFlower, props).setRegistryName(lightBlueFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(yellowFloatingFlower, props).setRegistryName(yellowFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(limeFloatingFlower, props).setRegistryName(limeFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(pinkFloatingFlower, props).setRegistryName(pinkFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(grayFloatingFlower, props).setRegistryName(grayFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(lightGrayFloatingFlower, props).setRegistryName(lightGrayFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(cyanFloatingFlower, props).setRegistryName(cyanFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(purpleFloatingFlower, props).setRegistryName(purpleFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(blueFloatingFlower, props).setRegistryName(blueFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(brownFloatingFlower, props).setRegistryName(brownFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(greenFloatingFlower, props).setRegistryName(greenFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(redFloatingFlower, props).setRegistryName(redFloatingFlower.getRegistryName()));
		r.register(new ItemBlockMod(blackFloatingFlower, props).setRegistryName(blackFloatingFlower.getRegistryName()));
		r.register(new ItemBlockTinyPotato(tinyPotato, props).setRegistryName(tinyPotato.getRegistryName()));
		r.register(new ItemBlockMod(spawnerClaw, props).setRegistryName(spawnerClaw.getRegistryName()));
		r.register(new ItemBlockMod(azulejo0, props).setRegistryName(azulejo0.getRegistryName()));
		r.register(new ItemBlockMod(azulejo1, props).setRegistryName(azulejo1.getRegistryName()));
		r.register(new ItemBlockMod(azulejo2, props).setRegistryName(azulejo2.getRegistryName()));
		r.register(new ItemBlockMod(azulejo3, props).setRegistryName(azulejo3.getRegistryName()));
		r.register(new ItemBlockMod(azulejo4, props).setRegistryName(azulejo4.getRegistryName()));
		r.register(new ItemBlockMod(azulejo5, props).setRegistryName(azulejo5.getRegistryName()));
		r.register(new ItemBlockMod(azulejo6, props).setRegistryName(azulejo6.getRegistryName()));
		r.register(new ItemBlockMod(azulejo7, props).setRegistryName(azulejo7.getRegistryName()));
		r.register(new ItemBlockMod(azulejo8, props).setRegistryName(azulejo8.getRegistryName()));
		r.register(new ItemBlockMod(azulejo9, props).setRegistryName(azulejo9.getRegistryName()));
		r.register(new ItemBlockMod(azulejo10, props).setRegistryName(azulejo10.getRegistryName()));
		r.register(new ItemBlockMod(azulejo11, props).setRegistryName(azulejo11.getRegistryName()));
		r.register(new ItemBlockMod(azulejo12, props).setRegistryName(azulejo12.getRegistryName()));
		r.register(new ItemBlockMod(azulejo13, props).setRegistryName(azulejo13.getRegistryName()));
		r.register(new ItemBlockMod(azulejo14, props).setRegistryName(azulejo14.getRegistryName()));
		r.register(new ItemBlockMod(azulejo15, props).setRegistryName(azulejo15.getRegistryName()));
		r.register(new ItemBlockMod(enderEye, props).setRegistryName(enderEye.getRegistryName()));
		r.register(new ItemBlockMod(starfield, props).setRegistryName(starfield.getRegistryName()));
		r.register(new ItemBlockMod(rfGenerator, props).setRegistryName(rfGenerator.getRegistryName()));
		r.register(new ItemBlockElven(elfGlass, props).setRegistryName(elfGlass.getRegistryName()));
		r.register(new ItemBlockMod(brewery, ModItems.defaultBuilder().setTEISR(() -> () -> new TEISR(ModBlocks.brewery, TileBrewery.class))).setRegistryName(brewery.getRegistryName()));
		r.register(new ItemBlockMod(manaGlass, props).setRegistryName(manaGlass.getRegistryName()));
		r.register(new ItemBlockMod(terraPlate, props).setRegistryName(terraPlate.getRegistryName()));
		r.register(new ItemBlockMod(redStringContainer, props).setRegistryName(redStringContainer.getRegistryName()));
		r.register(new ItemBlockMod(redStringDispenser, props).setRegistryName(redStringDispenser.getRegistryName()));
		r.register(new ItemBlockMod(redStringFertilizer, props).setRegistryName(redStringFertilizer.getRegistryName()));
		r.register(new ItemBlockMod(redStringComparator, props).setRegistryName(redStringComparator.getRegistryName()));
		r.register(new ItemBlockMod(redStringRelay, props).setRegistryName(redStringRelay.getRegistryName()));
		r.register(new ItemBlockFloatingSpecialFlower(floatingSpecialFlower, props).setRegistryName(floatingSpecialFlower.getRegistryName()));
		r.register(new ItemBlockMod(prism, props).setRegistryName(prism.getRegistryName()));
		r.register(new ItemBlockMod(enchantedSoil, props).setRegistryName(enchantedSoil.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockWhite, props).setRegistryName(petalBlockWhite.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockOrange, props).setRegistryName(petalBlockOrange.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockMagenta, props).setRegistryName(petalBlockMagenta.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockLightBlue, props).setRegistryName(petalBlockLightBlue.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockYellow, props).setRegistryName(petalBlockYellow.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockLime, props).setRegistryName(petalBlockLime.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockPink, props).setRegistryName(petalBlockPink.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockGray, props).setRegistryName(petalBlockGray.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockSilver, props).setRegistryName(petalBlockSilver.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockCyan, props).setRegistryName(petalBlockCyan.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockPurple, props).setRegistryName(petalBlockPurple.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockBlue, props).setRegistryName(petalBlockBlue.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockBrown, props).setRegistryName(petalBlockBrown.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockGreen, props).setRegistryName(petalBlockGreen.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockRed, props).setRegistryName(petalBlockRed.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockBlack, props).setRegistryName(petalBlockBlack.getRegistryName()));
		r.register(new ItemBlockMod(corporeaIndex, ModItems.defaultBuilder().setTEISR(() -> () -> new TEISR(ModBlocks.corporeaIndex, TileCorporeaIndex.class))).setRegistryName(corporeaIndex.getRegistryName()));
		r.register(new ItemBlockMod(corporeaFunnel, props).setRegistryName(corporeaFunnel.getRegistryName()));
		r.register(new ItemBlockMod(whiteMushroom, props).setRegistryName(whiteMushroom.getRegistryName()));
		r.register(new ItemBlockMod(orangeMushroom, props).setRegistryName(orangeMushroom.getRegistryName()));
		r.register(new ItemBlockMod(magentaMushroom, props).setRegistryName(magentaMushroom.getRegistryName()));
		r.register(new ItemBlockMod(lightBlueMushroom, props).setRegistryName(lightBlueMushroom.getRegistryName()));
		r.register(new ItemBlockMod(yellowMushroom, props).setRegistryName(yellowMushroom.getRegistryName()));
		r.register(new ItemBlockMod(limeMushroom, props).setRegistryName(limeMushroom.getRegistryName()));
		r.register(new ItemBlockMod(pinkMushroom, props).setRegistryName(pinkMushroom.getRegistryName()));
		r.register(new ItemBlockMod(grayMushroom, props).setRegistryName(grayMushroom.getRegistryName()));
		r.register(new ItemBlockMod(lightGrayMushroom, props).setRegistryName(lightGrayMushroom.getRegistryName()));
		r.register(new ItemBlockMod(cyanMushroom, props).setRegistryName(cyanMushroom.getRegistryName()));
		r.register(new ItemBlockMod(purpleMushroom, props).setRegistryName(purpleMushroom.getRegistryName()));
		r.register(new ItemBlockMod(blueMushroom, props).setRegistryName(blueMushroom.getRegistryName()));
		r.register(new ItemBlockMod(brownMushroom, props).setRegistryName(brownMushroom.getRegistryName()));
		r.register(new ItemBlockMod(greenMushroom, props).setRegistryName(greenMushroom.getRegistryName()));
		r.register(new ItemBlockMod(redMushroom, props).setRegistryName(redMushroom.getRegistryName()));
		r.register(new ItemBlockMod(blackMushroom, props).setRegistryName(blackMushroom.getRegistryName()));
		r.register(new ItemBlockMod(pump, props).setRegistryName(pump.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerWhite, props).setRegistryName(doubleFlowerWhite.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerOrange, props).setRegistryName(doubleFlowerOrange.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerMagenta, props).setRegistryName(doubleFlowerMagenta.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerLightBlue, props).setRegistryName(doubleFlowerLightBlue.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerYellow, props).setRegistryName(doubleFlowerYellow.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerLime, props).setRegistryName(doubleFlowerLime.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerPink, props).setRegistryName(doubleFlowerPink.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerGray, props).setRegistryName(doubleFlowerGray.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerLightGray, props).setRegistryName(doubleFlowerLightGray.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerCyan, props).setRegistryName(doubleFlowerCyan.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerPurple, props).setRegistryName(doubleFlowerPurple.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerBlue, props).setRegistryName(doubleFlowerBlue.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerBrown, props).setRegistryName(doubleFlowerBrown.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerGreen, props).setRegistryName(doubleFlowerGreen.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerRed, props).setRegistryName(doubleFlowerRed.getRegistryName()));
		r.register(new ItemBlockMod(doubleFlowerBlack, props).setRegistryName(doubleFlowerBlack.getRegistryName()));
		r.register(new ItemBlockBlaze(blazeBlock, props).setRegistryName(blazeBlock.getRegistryName()));
		r.register(new ItemBlockMod(corporeaInterceptor, props).setRegistryName(corporeaInterceptor.getRegistryName()));
		r.register(new ItemBlockMod(corporeaCrystalCube, ModItems.defaultBuilder().setTEISR(() -> () -> new TEISR(ModBlocks.corporeaCrystalCube, TileCorporeaCrystalCube.class))).setRegistryName(corporeaCrystalCube.getRegistryName()));
		r.register(new ItemBlockMod(incensePlate, props).setRegistryName(incensePlate.getRegistryName()));
		r.register(new ItemBlockMod(hourglass, ModItems.defaultBuilder().setTEISR(() -> () -> new TEISR(ModBlocks.hourglass, TileHourglass.class))).setRegistryName(hourglass.getRegistryName()));
		r.register(new ItemBlockMod(ghostRail, props).setRegistryName(ghostRail.getRegistryName()));
		r.register(new ItemBlockMod(sparkChanger, props).setRegistryName(sparkChanger.getRegistryName()));
		r.register(new ItemBlockMod(root, props).setRegistryName(root.getRegistryName()));
		r.register(new ItemBlockMod(felPumpkin, props).setRegistryName(felPumpkin.getRegistryName()));
		r.register(new ItemBlockMod(cocoon, props).setRegistryName(cocoon.getRegistryName()));
		r.register(new ItemBlockMod(lightRelayDefault, props).setRegistryName(lightRelayDefault.getRegistryName()));
		r.register(new ItemBlockMod(lightRelayDetector, props).setRegistryName(lightRelayDetector.getRegistryName()));
		r.register(new ItemBlockMod(lightRelayFork, props).setRegistryName(lightRelayFork.getRegistryName()));
		r.register(new ItemBlockMod(lightRelayToggle, props).setRegistryName(lightRelayToggle.getRegistryName()));
		r.register(new ItemBlockMod(lightLauncher, props).setRegistryName(lightLauncher.getRegistryName()));
		r.register(new ItemBlockMod(manaBomb, props).setRegistryName(manaBomb.getRegistryName()));
		r.register(new ItemBlockMod(cacophonium, props).setRegistryName(cacophonium.getRegistryName()));
		r.register(new ItemBlockMod(bellows, ModItems.defaultBuilder().setTEISR(() -> () -> new TEISR(ModBlocks.bellows, TileBellows.class))).setRegistryName(bellows.getRegistryName()));
		r.register(new ItemBlockMod(bifrostPerm, props).setRegistryName(bifrostPerm.getRegistryName()));
		r.register(new ItemBlockMod(cellBlock, props).setRegistryName(cellBlock.getRegistryName()));
		r.register(new ItemBlockMod(redStringInterceptor, props).setRegistryName(redStringInterceptor.getRegistryName()));
		r.register(new ItemBlockMod(corporeaRetainer, props).setRegistryName(corporeaRetainer.getRegistryName()));
		r.register(new ItemBlockMod(teruTeruBozu, ModItems.defaultBuilder().setTEISR(() -> () -> new TEISR(ModBlocks.teruTeruBozu, TileTeruTeruBozu.class))).setRegistryName(teruTeruBozu.getRegistryName()));
		r.register(new ItemBlockMod(shimmerrock, props).setRegistryName(shimmerrock.getRegistryName()));
		r.register(new ItemBlockMod(shimmerwoodPlanks, props).setRegistryName(shimmerwoodPlanks.getRegistryName()));
		r.register(new ItemBlockMod(avatar, ModItems.defaultBuilder().setTEISR(() -> () -> new TEISR(ModBlocks.avatar, TileAvatar.class))).setRegistryName(avatar.getRegistryName()));
		r.register(new ItemBlockMod(dryGrass, props).setRegistryName(dryGrass.getRegistryName()));
		r.register(new ItemBlockMod(goldenGrass, props).setRegistryName(goldenGrass.getRegistryName()));
		r.register(new ItemBlockMod(vividGrass, props).setRegistryName(vividGrass.getRegistryName()));
		r.register(new ItemBlockMod(scorchedGrass, props).setRegistryName(scorchedGrass.getRegistryName()));
		r.register(new ItemBlockMod(infusedGrass, props).setRegistryName(infusedGrass.getRegistryName()));
		r.register(new ItemBlockMod(mutatedGrass, props).setRegistryName(mutatedGrass.getRegistryName()));
		r.register(new ItemBlockMod(animatedTorch, props).setRegistryName(animatedTorch.getRegistryName()));
	}
	
	public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name) {
		reg.register(thing.setRegistryName(new ResourceLocation(LibMisc.MOD_ID, name)));
	}

	public static void addDispenserBehaviours() {
		BlockDispenser.registerDispenseBehavior(ModItems.twigWand, new BehaviourWand());
		BlockDispenser.registerDispenseBehavior(ModItems.poolMinecart, new BehaviourPoolMinecart());
		BlockDispenser.registerDispenseBehavior(ModBlocks.felPumpkin, new BehaviourFelPumpkin());

		SeedBehaviours.init();
	}
	
	@SubscribeEvent
	public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt) {
		IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();
		r.register(TileEntityType.Builder.create(TileAltar::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.ALTAR));
		r.register(TileEntityType.Builder.create(TileSpecialFlower::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SPECIAL_FLOWER));
		r.register(TileEntityType.Builder.create(TileSpreader::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SPREADER));
		r.register(TileEntityType.Builder.create(TilePool::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.POOL));
		r.register(TileEntityType.Builder.create(TileRuneAltar::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RUNE_ALTAR));
		r.register(TileEntityType.Builder.create(TilePylon::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.PYLON));
		r.register(TileEntityType.Builder.create(TileDistributor::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.DISTRIBUTOR));
		r.register(TileEntityType.Builder.create(TileManaVoid::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.MANA_VOID));
		r.register(TileEntityType.Builder.create(TileManaDetector::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.MANA_DETECTOR));
		r.register(TileEntityType.Builder.create(TileEnchanter::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.ENCHANTER));
		r.register(TileEntityType.Builder.create(TileTurntable::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.TURNTABLE));
		r.register(TileEntityType.Builder.create(TileTinyPlanet::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.TINY_PLANET));
		r.register(TileEntityType.Builder.create(TileOpenCrate::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.OPEN_CRATE));
		r.register(TileEntityType.Builder.create(TileCraftCrate::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CRAFT_CRATE));
		r.register(TileEntityType.Builder.create(TileForestEye::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.FOREST_EYE));
		r.register(TileEntityType.Builder.create(TilePlatform::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.PLATFORM));
		r.register(TileEntityType.Builder.create(TileAlfPortal::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.ALF_PORTAL));
		r.register(TileEntityType.Builder.create(TileBifrost::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.BIFROST));
		r.register(TileEntityType.Builder.create(TileFloatingFlower::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.MINI_ISLAND));
		r.register(TileEntityType.Builder.create(TileTinyPotato::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.TINY_POTATO));
		r.register(TileEntityType.Builder.create(TileSpawnerClaw::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SPAWNER_CLAW));
		r.register(TileEntityType.Builder.create(TileEnderEye::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.ENDER_EYE_BLOCK));
		r.register(TileEntityType.Builder.create(TileStarfield::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.STARFIELD));
		r.register(TileEntityType.Builder.create(TileRFGenerator::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.FLUXFIELD));
		r.register(TileEntityType.Builder.create(TileBrewery::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.BREWERY));
		r.register(TileEntityType.Builder.create(TileTerraPlate::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.TERRA_PLATE));
		r.register(TileEntityType.Builder.create(TileRedStringContainer::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RED_STRING_CONTAINER));
		r.register(TileEntityType.Builder.create(TileRedStringDispenser::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RED_STRING_DISPENSER));
		r.register(TileEntityType.Builder.create(TileRedStringFertilizer::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RED_STRING_FERTILIZER));
		r.register(TileEntityType.Builder.create(TileRedStringComparator::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RED_STRING_COMPARATOR));
		r.register(TileEntityType.Builder.create(TileRedStringRelay::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RED_STRING_RELAY));
		r.register(TileEntityType.Builder.create(TileFloatingSpecialFlower::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.FLOATING_SPECIAL_FLOWER));
		r.register(TileEntityType.Builder.create(TileManaFlame::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.MANA_FLAME));
		r.register(TileEntityType.Builder.create(TilePrism::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.PRISM));
		r.register(TileEntityType.Builder.create(TileCorporeaIndex::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CORPOREA_INDEX));
		r.register(TileEntityType.Builder.create(TileCorporeaFunnel::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CORPOREA_FUNNEL));
		r.register(TileEntityType.Builder.create(TilePump::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.PUMP));
		r.register(TileEntityType.Builder.create(TileFakeAir::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.FAKE_AIR));
		r.register(TileEntityType.Builder.create(TileCorporeaInterceptor::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CORPOREA_INTERCEPTOR));
		r.register(TileEntityType.Builder.create(TileCorporeaCrystalCube::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CORPOREA_CRYSTAL_CUBE));
		r.register(TileEntityType.Builder.create(TileIncensePlate::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.INCENSE_PLATE));
		r.register(TileEntityType.Builder.create(TileHourglass::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.HOURGLASS));
		r.register(TileEntityType.Builder.create(TileSparkChanger::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SPARK_CHANGER));
		r.register(TileEntityType.Builder.create(TileCocoon::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.COCOON));
		r.register(TileEntityType.Builder.create(TileLightRelay::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.LIGHT_RELAY));
		r.register(TileEntityType.Builder.create(TileCacophonium::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CACOPHONIUM));
		r.register(TileEntityType.Builder.create(TileBellows::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.BELLOWS));
		r.register(TileEntityType.Builder.create(TileCell::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CELL_BLOCK));
		r.register(TileEntityType.Builder.create(TileRedStringInterceptor::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RED_STRING_INTERCEPTOR));
		r.register(TileEntityType.Builder.create(TileGaiaHead::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.GAIA_HEAD));
		r.register(TileEntityType.Builder.create(TileCorporeaRetainer::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CORPOREA_RETAINER));
		r.register(TileEntityType.Builder.create(TileTeruTeruBozu::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.TERU_TERU_BOZU));
		r.register(TileEntityType.Builder.create(TileAvatar::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.AVATAR));
		r.register(TileEntityType.Builder.create(TileAnimatedTorch::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.ANIMATED_TORCH));
	}

	@SubscribeEvent
	public static void initSubtiles(RegistryEvent.Register<SubTileType> evt) {
		IForgeRegistry<SubTileType> r = evt.getRegistry();
		r.register(new SubTileType(SubTileType.MISC_TYPE, DummySubTile::new).setRegistryName(BotaniaAPI.DUMMY_SUBTILE_NAME));
		r.register(new SubTileType(SubTileType.MISC_TYPE, SubTilePureDaisy::new).setRegistryName(LibBlockNames.SUBTILE_PUREDAISY));
		r.register(new SubTileType(SubTileType.MISC_TYPE, SubTileManastar::new).setRegistryName(LibBlockNames.SUBTILE_MANASTAR));

		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileHydroangeas::new).setRegistryName(LibBlockNames.SUBTILE_HYDROANGEAS));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileEndoflame::new).setRegistryName(LibBlockNames.SUBTILE_ENDOFLAME));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileThermalily::new).setRegistryName(LibBlockNames.SUBTILE_THERMALILY));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileArcaneRose::new).setRegistryName(LibBlockNames.SUBTILE_ARCANE_ROSE));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileMunchdew::new).setRegistryName(LibBlockNames.SUBTILE_MUNCHDEW));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileEntropinnyum::new).setRegistryName(LibBlockNames.SUBTILE_ENTROPINNYUM));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileKekimurus::new).setRegistryName(LibBlockNames.SUBTILE_KEKIMURUS));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileGourmaryllis::new).setRegistryName(LibBlockNames.SUBTILE_GOURMARYLLIS));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileNarslimmus::new).setRegistryName(LibBlockNames.SUBTILE_NARSLIMMUS));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileSpectrolus::new).setRegistryName(LibBlockNames.SUBTILE_SPECTROLUS));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileDandelifeon::new).setRegistryName(LibBlockNames.SUBTILE_DANDELIFEON));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileRafflowsia::new).setRegistryName(LibBlockNames.SUBTILE_RAFFLOWSIA));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileShulkMeNot::new).setRegistryName(LibBlockNames.SUBTILE_SHULK_ME_NOT));

		// todo 1.13 register recipes for chibis: BotaniaAPI.registerMiniSubTile(new ResourceLocation(key.getNamespace(), key.getPath() + "_chibi"), innerClazz, key);
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileBellethorn::new).setRegistryName(LibBlockNames.SUBTILE_BELLETHORN));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileBellethorn.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_BELLETHORN.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileDreadthorn::new).setRegistryName(LibBlockNames.SUBTILE_DREADTHORN));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileHeiseiDream::new).setRegistryName(LibBlockNames.SUBTILE_HEISEI_DREAM));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileTigerseye::new).setRegistryName(LibBlockNames.SUBTILE_TIGERSEYE));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileJadedAmaranthus::new).setRegistryName(LibBlockNames.SUBTILE_JADED_AMARANTHUS));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileOrechid::new).setRegistryName(LibBlockNames.SUBTILE_ORECHID));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileOrechidIgnem::new).setRegistryName(LibBlockNames.SUBTILE_ORECHID_IGNEM));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileFallenKanade::new).setRegistryName(LibBlockNames.SUBTILE_FALLEN_KANADE));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileExoflame::new).setRegistryName(LibBlockNames.SUBTILE_EXOFLAME));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileAgricarnation::new).setRegistryName(LibBlockNames.SUBTILE_AGRICARNATION));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileAgricarnation.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_AGRICARNATION.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileHopperhock::new).setRegistryName(LibBlockNames.SUBTILE_HOPPERHOCK));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileHopperhock.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_HOPPERHOCK.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileTangleberrie::new).setRegistryName(LibBlockNames.SUBTILE_TANGLEBERRIE));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileJiyuulia::new).setRegistryName(LibBlockNames.SUBTILE_JIYUULIA));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileRannuncarpus::new).setRegistryName(LibBlockNames.SUBTILE_RANNUNCARPUS));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileRannuncarpus.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_RANNUNCARPUS.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileHyacidus::new).setRegistryName(LibBlockNames.SUBTILE_HYACIDUS));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTilePollidisiac::new).setRegistryName(LibBlockNames.SUBTILE_POLLIDISIAC));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileClayconia::new).setRegistryName(LibBlockNames.SUBTILE_CLAYCONIA));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileClayconia.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_CLAYCONIA.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileLoonuim::new).setRegistryName(LibBlockNames.SUBTILE_LOONIUM));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileDaffomill::new).setRegistryName(LibBlockNames.SUBTILE_DAFFOMILL));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileVinculotus::new).setRegistryName(LibBlockNames.SUBTILE_VINCULOTUS));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileSpectranthemum::new).setRegistryName(LibBlockNames.SUBTILE_SPECTRANTHEMUM));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileMedumone::new).setRegistryName(LibBlockNames.SUBTILE_MEDUMONE));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileMarimorphosis::new).setRegistryName(LibBlockNames.SUBTILE_MARIMORPHOSIS));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileMarimorphosis.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_MARIMORPHOSIS.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileBubbell::new).setRegistryName(LibBlockNames.SUBTILE_BUBBELL));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileBubbell.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_BUBBELL.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileSolegnolia::new).setRegistryName(LibBlockNames.SUBTILE_SOLEGNOLIA));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileSolegnolia.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_SOLEGNOLIA.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileBergamute::new).setRegistryName(LibBlockNames.SUBTILE_BERGAMUTE));
	}

	public static Block getFlower(EnumDyeColor color) {
		switch (color) {
			default:
			case WHITE: return whiteFlower;
			case ORANGE: return orangeFlower;
			case MAGENTA: return magentaFlower;
			case LIGHT_BLUE: return lightBlueFlower;
			case YELLOW: return yellowFlower;
			case LIME: return limeFlower;
			case PINK: return pinkFlower;
			case GRAY: return grayFlower;
			case LIGHT_GRAY: return lightGrayFlower;
			case CYAN: return cyanFlower;
			case PURPLE: return purpleFlower;
			case BLUE: return blueFlower;
			case BROWN: return brownFlower;
			case GREEN: return greenFlower;
			case RED: return redFlower;
			case BLACK: return blackFlower;
		}
	}
	
	public static Block getMushroom(EnumDyeColor color) {
		switch (color) {
			default:
			case WHITE: return whiteMushroom;
			case ORANGE: return orangeMushroom;
			case MAGENTA: return magentaMushroom;
			case LIGHT_BLUE: return lightBlueMushroom;
			case YELLOW: return yellowMushroom;
			case LIME: return limeMushroom;
			case PINK: return pinkMushroom;
			case GRAY: return grayMushroom;
			case LIGHT_GRAY: return lightGrayMushroom;
			case CYAN: return cyanMushroom;
			case PURPLE: return purpleMushroom;
			case BLUE: return blueMushroom;
			case BROWN: return brownMushroom;
			case GREEN: return greenMushroom;
			case RED: return redMushroom;
			case BLACK: return blackMushroom;
		}
	}

	public static Block getWool(EnumDyeColor color) {
		switch (color) {
			default:
			case WHITE: return Blocks.WHITE_WOOL;
			case ORANGE: return Blocks.ORANGE_WOOL;
			case MAGENTA: return Blocks.ORANGE_WOOL;
			case LIGHT_BLUE: return Blocks.ORANGE_WOOL;
			case YELLOW: return Blocks.ORANGE_WOOL;
			case LIME: return Blocks.ORANGE_WOOL;
			case PINK: return Blocks.ORANGE_WOOL;
			case GRAY: return Blocks.ORANGE_WOOL;
			case LIGHT_GRAY: return Blocks.ORANGE_WOOL;
			case CYAN: return Blocks.ORANGE_WOOL;
			case PURPLE: return Blocks.ORANGE_WOOL;
			case BLUE: return Blocks.ORANGE_WOOL;
			case BROWN: return Blocks.ORANGE_WOOL;
			case GREEN: return Blocks.ORANGE_WOOL;
			case RED: return Blocks.ORANGE_WOOL;
			case BLACK: return Blocks.ORANGE_WOOL;
		}
	}

	public static Block getCarpet(EnumDyeColor color) {
		switch (color) {
			default:
			case WHITE: return Blocks.WHITE_CARPET;
			case ORANGE: return Blocks.ORANGE_CARPET;
			case MAGENTA: return Blocks.ORANGE_CARPET;
			case LIGHT_BLUE: return Blocks.ORANGE_CARPET;
			case YELLOW: return Blocks.ORANGE_CARPET;
			case LIME: return Blocks.ORANGE_CARPET;
			case PINK: return Blocks.ORANGE_CARPET;
			case GRAY: return Blocks.ORANGE_CARPET;
			case LIGHT_GRAY: return Blocks.ORANGE_CARPET;
			case CYAN: return Blocks.ORANGE_CARPET;
			case PURPLE: return Blocks.ORANGE_CARPET;
			case BLUE: return Blocks.ORANGE_CARPET;
			case BROWN: return Blocks.ORANGE_CARPET;
			case GREEN: return Blocks.ORANGE_CARPET;
			case RED: return Blocks.ORANGE_CARPET;
			case BLACK: return Blocks.ORANGE_CARPET;
		}
	}

	public static Block getBuriedPetal(EnumDyeColor color) {
		switch (color) {
			default:
			case WHITE: return whiteBuriedPetals;
			case ORANGE: return orangeBuriedPetals;
			case MAGENTA: return magentaBuriedPetals;
			case LIGHT_BLUE: return lightBlueBuriedPetals;
			case YELLOW: return yellowBuriedPetals;
			case LIME: return limeBuriedPetals;
			case PINK: return pinkBuriedPetals;
			case GRAY: return grayBuriedPetals;
			case LIGHT_GRAY: return lightGrayBuriedPetals;
			case CYAN: return cyanBuriedPetals;
			case PURPLE: return purpleBuriedPetals;
			case BLUE: return blueBuriedPetals;
			case BROWN: return brownBuriedPetals;
			case GREEN: return greenBuriedPetals;
			case RED: return redBuriedPetals;
			case BLACK: return blackBuriedPetals;
		}
	}
	
	public static Block getShinyFlower(EnumDyeColor color) {
		switch (color) {
			default:
			case WHITE: return whiteShinyFlower;
			case ORANGE: return orangeShinyFlower;
			case MAGENTA: return magentaShinyFlower;
			case LIGHT_BLUE: return lightBlueShinyFlower;
			case YELLOW: return yellowShinyFlower;
			case LIME: return limeShinyFlower;
			case PINK: return pinkShinyFlower;
			case GRAY: return grayShinyFlower;
			case LIGHT_GRAY: return lightGrayShinyFlower;
			case CYAN: return cyanShinyFlower;
			case PURPLE: return purpleShinyFlower;
			case BLUE: return blueShinyFlower;
			case BROWN: return brownShinyFlower;
			case GREEN: return greenShinyFlower;
			case RED: return redShinyFlower;
			case BLACK: return blackShinyFlower;
		}
	}

	public static Block getFloatingFlower(EnumDyeColor color) {
		switch (color) {
			default:
			case WHITE: return whiteFloatingFlower;
			case ORANGE: return orangeFloatingFlower;
			case MAGENTA: return magentaFloatingFlower;
			case LIGHT_BLUE: return lightBlueFloatingFlower;
			case YELLOW: return yellowFloatingFlower;
			case LIME: return limeFloatingFlower;
			case PINK: return pinkFloatingFlower;
			case GRAY: return grayFloatingFlower;
			case LIGHT_GRAY: return lightGrayFloatingFlower;
			case CYAN: return cyanFloatingFlower;
			case PURPLE: return purpleFloatingFlower;
			case BLUE: return blueFloatingFlower;
			case BROWN: return brownFloatingFlower;
			case GREEN: return greenFloatingFlower;
			case RED: return redFloatingFlower;
			case BLACK: return blackFloatingFlower;
		}
	}
	
	public static Block getDoubleFlower(EnumDyeColor color) {
		switch (color) {
			default:
			case WHITE: return doubleFlowerWhite;
			case ORANGE: return doubleFlowerOrange;
			case MAGENTA: return doubleFlowerMagenta;
			case LIGHT_BLUE: return doubleFlowerLightBlue;
			case YELLOW: return doubleFlowerYellow;
			case LIME: return doubleFlowerLime;
			case PINK: return doubleFlowerPink;
			case GRAY: return doubleFlowerGray;
			case LIGHT_GRAY: return doubleFlowerLightGray;
			case CYAN: return doubleFlowerCyan;
			case PURPLE: return doubleFlowerPurple;
			case BLUE: return doubleFlowerBlue;
			case BROWN: return doubleFlowerBrown;
			case GREEN: return doubleFlowerGreen;
			case RED: return doubleFlowerRed;
			case BLACK: return doubleFlowerBlack;
		}
	}

}
