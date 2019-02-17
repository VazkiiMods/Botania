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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.subtile.SubTileEntity;
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
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibOreDict;

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

	public static final Block specialFlower = new BlockSpecialFlower();
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
	public static final Block floatingFlower = new BlockFloatingFlower();
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
	@ObjectHolder(LibBlockNames.RF_GENERATOR) public static Block rfGenerator;
	@ObjectHolder(LibBlockNames.ELF_GLASS) public static Block elfGlass;
	@ObjectHolder(LibBlockNames.BREWERY) public static Block brewery;
	@ObjectHolder(LibBlockNames.MANA_GLASS) public static Block manaGlass;
	@ObjectHolder(LibBlockNames.TERRA_PLATE) public static Block terraPlate;
	@ObjectHolder(LibBlockNames.RED_STRING_CONTAINER) public static Block redStringContainer;
	@ObjectHolder(LibBlockNames.RED_STRING_DISPENSER) public static Block redStringDispenser;
	@ObjectHolder(LibBlockNames.RED_STRING_FERTILIZER) public static Block redStringFertilizer;
	@ObjectHolder(LibBlockNames.RED_STRING_COMPARATOR) public static Block redStringComparator;
	@ObjectHolder(LibBlockNames.RED_STRING_RELAY) public static Block redStringRelay;
	public static final Block floatingSpecialFlower = new BlockFloatingSpecialFlower();
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
	@ObjectHolder("white" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomWhite;
	@ObjectHolder("orange" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomOrange;
	@ObjectHolder("magenta" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomMagenta;
	@ObjectHolder("light_blue" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomLightBlue;
	@ObjectHolder("yellow" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomYellow;
	@ObjectHolder("lime" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomLime;
	@ObjectHolder("pink" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomPink;
	@ObjectHolder("gray" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomGray;
	@ObjectHolder("light_gray" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomSilver;
	@ObjectHolder("cyan" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomCyan;
	@ObjectHolder("purple" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomPurple;
	@ObjectHolder("blue" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomBlue;
	@ObjectHolder("brown" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomBrown;
	@ObjectHolder("green" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomGreen;
	@ObjectHolder("red" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomRed;
	@ObjectHolder("black" + LibBlockNames.MUSHROOM_SUFFIX) public static Block mushroomBlack;
	@ObjectHolder(LibBlockNames.PUMP) public static Block pump;
	@ObjectHolder("white" + LibBlockNames.MUSHROOM_SUFFIX) public static Block doubleFlowerWhite;
	@ObjectHolder("orange" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerOrange;
	@ObjectHolder("magenta" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerMagenta;
	@ObjectHolder("light_blue" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerLightBlue;
	@ObjectHolder("yellow" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerYellow;
	@ObjectHolder("lime" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerLime;
	@ObjectHolder("pink" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerPink;
	@ObjectHolder("gray" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerGray;
	@ObjectHolder("light_gray" + LibBlockNames.DOUBLE_FLOWER_SUFFIX) public static Block doubleFlowerSilver;
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

		r.register(specialFlower);

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
		register(r, new BlockDistributor(builder), LibBlockNames.MANA_DETECTOR);

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
		register(r, new BlockPlatform(BlockPlatform.Variant.INFRANGIBLE, builder), LibBlockNames.PLATFORM_INFRANGIBLE);
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
		
		r.register(floatingFlower);
		register(r, new BlockTinyPotato(Block.Properties.create(Material.CLOTH).hardnessAndResistance(0.25F)), LibBlockNames.TINY_POTATO);
		register(r, new BlockSpawnerClaw(Block.Properties.create(Material.IRON).hardnessAndResistance(3)), LibBlockNames.SPAWNER_CLAW);

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 5).sound(SoundType.STONE);
		ILexiconable azulejo = (w, po ,pl, st) -> LexiconData.azulejo;
		for (int i = 0; i < 15; i++) {
			register(r, new BlockModLexiconable(builder, azulejo), LibBlockNames.AZULEJO_PREFIX + i);
		}

		register(r, new BlockEnderEye(Block.Properties.create(Material.IRON).hardnessAndResistance(3, 10).sound(SoundType.METAL)), LibBlockNames.ENDER_EYE_BLOCK);
		register(r, new BlockStarfield(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 2000).sound(SoundType.METAL)), LibBlockNames.STARFIELD);
		register(r, new BlockRFGenerator(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)), LibBlockNames.RF_GENERATOR);
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
		r.register(floatingSpecialFlower);
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

		register(r, new BlockFakeAir(Block.Properties.create(Material.STRUCTURE_VOID).needsRandomTick()), LibBlockNames.FAKE_AIR);
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
		register(r, new BlockGaiaHead(Block.Properties.create(Material.CIRCUITS).hardnessAndResistance(1)), LibBlockNames.GAIA_HEAD);
		register(r, new BlockCorporeaRetainer(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL)), LibBlockNames.CORPOREA_RETAINER);
		register(r, new BlockTeruTeruBozu(Block.Properties.create(Material.CLOTH)), LibBlockNames.TERU_TERU_BOZU);
		ILexiconable rainbowRod = (w, po, pl, st) -> LexiconData.rainbowRod;
		register(r, new BlockModLexiconable(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE), rainbowRod), LibBlockNames.SHIMMERROCK);
		register(r, new BlockModLexiconable(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD), rainbowRod), LibBlockNames.SHIMMERWOOD_PLANKS);
		register(r, new BlockAvatar(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)), LibBlockNames.AVATAR);

		builder = Block.Properties.create(Material.GRASS).hardnessAndResistance(0.6F).needsRandomTick().sound(SoundType.PLANT);
		for(BlockAltGrass.Variant v : BlockAltGrass.Variant.values()) {
			register(r, new BlockAltGrass(v, builder), v.name().toLowerCase(Locale.ROOT) + LibBlockNames.ALT_GRASS_SUFFIX);
		}

		register(r, new BlockAnimatedTorch(Block.Properties.create(Material.CIRCUITS).lightValue(7)), LibBlockNames.ANIMATED_TORCH);

		RecipeManaInfusion.alchemyState = alchemyCatalyst.getDefaultState();
		RecipeManaInfusion.conjurationState = conjurationCatalyst.getDefaultState();
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		r.register(new ItemBlockMod(whiteFlower).setRegistryName(whiteFlower.getRegistryName()));
		r.register(new ItemBlockMod(orangeFlower).setRegistryName(orangeFlower.getRegistryName()));
		r.register(new ItemBlockMod(magentaFlower).setRegistryName(magentaFlower.getRegistryName()));
		r.register(new ItemBlockMod(lightBlueFlower).setRegistryName(lightBlueFlower.getRegistryName()));
		r.register(new ItemBlockMod(yellowFlower).setRegistryName(yellowFlower.getRegistryName()));
		r.register(new ItemBlockMod(limeFlower).setRegistryName(limeFlower.getRegistryName()));
		r.register(new ItemBlockMod(pinkFlower).setRegistryName(pinkFlower.getRegistryName()));
		r.register(new ItemBlockMod(grayFlower).setRegistryName(grayFlower.getRegistryName()));
		r.register(new ItemBlockMod(lightGrayFlower).setRegistryName(lightGrayFlower.getRegistryName()));
		r.register(new ItemBlockMod(cyanFlower).setRegistryName(cyanFlower.getRegistryName()));
		r.register(new ItemBlockMod(purpleFlower).setRegistryName(purpleFlower.getRegistryName()));
		r.register(new ItemBlockMod(blueFlower).setRegistryName(blueFlower.getRegistryName()));
		r.register(new ItemBlockMod(brownFlower).setRegistryName(brownFlower.getRegistryName()));
		r.register(new ItemBlockMod(greenFlower).setRegistryName(greenFlower.getRegistryName()));
		r.register(new ItemBlockMod(redFlower).setRegistryName(redFlower.getRegistryName()));
		r.register(new ItemBlockMod(blackFlower).setRegistryName(blackFlower.getRegistryName()));
		r.register(new ItemBlockMod(defaultAltar).setRegistryName(defaultAltar.getRegistryName()));
		r.register(new ItemBlockMod(forestAltar).setRegistryName(forestAltar.getRegistryName()));
		r.register(new ItemBlockMod(plainsAltar).setRegistryName(plainsAltar.getRegistryName()));
		r.register(new ItemBlockMod(mountainAltar).setRegistryName(mountainAltar.getRegistryName()));
		r.register(new ItemBlockMod(fungalAltar).setRegistryName(fungalAltar.getRegistryName()));
		r.register(new ItemBlockMod(swampAltar).setRegistryName(swampAltar.getRegistryName()));
		r.register(new ItemBlockMod(desertAltar).setRegistryName(desertAltar.getRegistryName()));
		r.register(new ItemBlockMod(taigaAltar).setRegistryName(taigaAltar.getRegistryName()));
		r.register(new ItemBlockMod(mesaAltar).setRegistryName(mesaAltar.getRegistryName()));
		r.register(new ItemBlockMod(mossyAltar).setRegistryName(mossyAltar.getRegistryName()));
		r.register(new ItemBlockMod(livingrock).setRegistryName(livingrock.getRegistryName()));
		r.register(new ItemBlockMod(livingrockBrick).setRegistryName(livingrockBrick.getRegistryName()));
		r.register(new ItemBlockMod(livingrockBrickChiseled).setRegistryName(livingrockBrickChiseled.getRegistryName()));
		r.register(new ItemBlockMod(livingrockBrickCracked).setRegistryName(livingrockBrickCracked.getRegistryName()));
		r.register(new ItemBlockMod(livingrockBrickMossy).setRegistryName(livingrockBrickMossy.getRegistryName()));
		r.register(new ItemBlockMod(livingwood).setRegistryName(livingwood.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodPlanks).setRegistryName(livingwoodPlanks.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodPlanksMossy).setRegistryName(livingwoodPlanksMossy.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodFramed).setRegistryName(livingwoodFramed.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodPatternFramed).setRegistryName(livingwoodPatternFramed.getRegistryName()));
		r.register(new ItemBlockMod(livingwoodGlimmering).setRegistryName(livingwoodGlimmering.getRegistryName()));
		r.register(new ItemBlockSpecialFlower(specialFlower).setRegistryName(specialFlower.getRegistryName()));
		r.register(new ItemBlockMod(manaSpreader).setRegistryName(manaSpreader.getRegistryName()));
		r.register(new ItemBlockMod(redstoneSpreader).setRegistryName(redstoneSpreader.getRegistryName()));
		r.register(new ItemBlockMod(elvenSpreader).setRegistryName(elvenSpreader.getRegistryName()));
		r.register(new ItemBlockMod(gaiaSpreader).setRegistryName(gaiaSpreader.getRegistryName()));
		r.register(new ItemBlockPool(manaPool).setRegistryName(manaPool.getRegistryName()));
		r.register(new ItemBlockPool(creativePool).setRegistryName(creativePool.getRegistryName()));
		r.register(new ItemBlockPool(dilutedPool).setRegistryName(dilutedPool.getRegistryName()));
		r.register(new ItemBlockPool(fabulousPool).setRegistryName(fabulousPool.getRegistryName()));
		r.register(new ItemBlockMod(runeAltar).setRegistryName(runeAltar.getRegistryName()));
		r.register(new ItemBlockMod(manaPylon).setRegistryName(manaPylon.getRegistryName()));
		r.register(new ItemBlockMod(naturaPylon).setRegistryName(naturaPylon.getRegistryName()));
		r.register(new ItemBlockMod(gaiaPylon).setRegistryName(gaiaPylon.getRegistryName()));
		r.register(new ItemBlockMod(pistonRelay).setRegistryName(pistonRelay.getRegistryName()));
		r.register(new ItemBlockMod(distributor).setRegistryName(distributor.getRegistryName()));
		r.register(new ItemBlockMod(manaVoid).setRegistryName(manaVoid.getRegistryName()));
		r.register(new ItemBlockMod(manaDetector).setRegistryName(manaDetector.getRegistryName()));
		r.register(new ItemBlockMod(enchanter).setRegistryName(enchanter.getRegistryName()));
		r.register(new ItemBlockMod(turntable).setRegistryName(turntable.getRegistryName()));
		r.register(new ItemBlockMod(tinyPlanet).setRegistryName(tinyPlanet.getRegistryName()));
		r.register(new ItemBlockMod(alchemyCatalyst).setRegistryName(alchemyCatalyst.getRegistryName()));
		r.register(new ItemBlockMod(openCrate).setRegistryName(openCrate.getRegistryName()));
		r.register(new ItemBlockMod(craftCrate).setRegistryName(craftCrate.getRegistryName()));
		r.register(new ItemBlockMod(forestEye).setRegistryName(forestEye.getRegistryName()));
		r.register(new ItemBlockMod(manasteelBlock).setRegistryName(manasteelBlock.getRegistryName()));
		r.register(new ItemBlockMod(terrasteelBlock).setRegistryName(terrasteelBlock.getRegistryName()));
		r.register(new ItemBlockElven(elementiumBlock).setRegistryName(elementiumBlock.getRegistryName()));
		r.register(new ItemBlockMod(manaDiamondBlock).setRegistryName(manaDiamondBlock.getRegistryName()));
		r.register(new ItemBlockMod(dragonstoneBlock).setRegistryName(dragonstoneBlock.getRegistryName()));
		r.register(new ItemBlockMod(wildDrum).setRegistryName(wildDrum.getRegistryName()));
		r.register(new ItemBlockMod(gatheringDrum).setRegistryName(gatheringDrum.getRegistryName()));
		r.register(new ItemBlockMod(canopyDrum).setRegistryName(canopyDrum.getRegistryName()));
		r.register(new ItemBlockMod(whiteShinyFlower).setRegistryName(whiteShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(orangeShinyFlower).setRegistryName(orangeShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(magentaShinyFlower).setRegistryName(magentaShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(lightBlueShinyFlower).setRegistryName(lightBlueShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(yellowShinyFlower).setRegistryName(yellowShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(limeShinyFlower).setRegistryName(limeShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(pinkShinyFlower).setRegistryName(pinkShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(grayShinyFlower).setRegistryName(grayShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(lightGrayShinyFlower).setRegistryName(lightGrayShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(cyanShinyFlower).setRegistryName(cyanShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(purpleShinyFlower).setRegistryName(purpleShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(blueShinyFlower).setRegistryName(blueShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(brownShinyFlower).setRegistryName(brownShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(greenShinyFlower).setRegistryName(greenShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(redShinyFlower).setRegistryName(redShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(blackShinyFlower).setRegistryName(blackShinyFlower.getRegistryName()));
		r.register(new ItemBlockMod(abstrusePlatform).setRegistryName(abstrusePlatform.getRegistryName()));
		r.register(new ItemBlockMod(spectralPlatform).setRegistryName(spectralPlatform.getRegistryName()));
		r.register(new ItemBlockMod(infrangiblePlatform).setRegistryName(infrangiblePlatform.getRegistryName()));
		r.register(new ItemBlockMod(alfPortal).setRegistryName(alfPortal.getRegistryName()));
		r.register(new ItemBlockDreamwood(dreamwood).setRegistryName(dreamwood.getRegistryName()));
		r.register(new ItemBlockMod(conjurationCatalyst).setRegistryName(conjurationCatalyst.getRegistryName()));
		r.register(new ItemBlockMod(bifrost).setRegistryName(bifrost.getRegistryName()));
		r.register(new ItemBlockWithMetadataAndName(floatingFlower).setRegistryName(floatingFlower.getRegistryName()));
		r.register(new ItemBlockTinyPotato(tinyPotato).setRegistryName(tinyPotato.getRegistryName()));
		r.register(new ItemBlockMod(spawnerClaw).setRegistryName(spawnerClaw.getRegistryName()));
		r.register(new ItemBlockMod(azulejo0).setRegistryName(azulejo0.getRegistryName()));
		r.register(new ItemBlockMod(azulejo1).setRegistryName(azulejo1.getRegistryName()));
		r.register(new ItemBlockMod(azulejo2).setRegistryName(azulejo2.getRegistryName()));
		r.register(new ItemBlockMod(azulejo3).setRegistryName(azulejo3.getRegistryName()));
		r.register(new ItemBlockMod(azulejo4).setRegistryName(azulejo4.getRegistryName()));
		r.register(new ItemBlockMod(azulejo5).setRegistryName(azulejo5.getRegistryName()));
		r.register(new ItemBlockMod(azulejo6).setRegistryName(azulejo6.getRegistryName()));
		r.register(new ItemBlockMod(azulejo7).setRegistryName(azulejo7.getRegistryName()));
		r.register(new ItemBlockMod(azulejo8).setRegistryName(azulejo8.getRegistryName()));
		r.register(new ItemBlockMod(azulejo9).setRegistryName(azulejo9.getRegistryName()));
		r.register(new ItemBlockMod(azulejo10).setRegistryName(azulejo10.getRegistryName()));
		r.register(new ItemBlockMod(azulejo11).setRegistryName(azulejo11.getRegistryName()));
		r.register(new ItemBlockMod(azulejo12).setRegistryName(azulejo12.getRegistryName()));
		r.register(new ItemBlockMod(azulejo13).setRegistryName(azulejo13.getRegistryName()));
		r.register(new ItemBlockMod(azulejo14).setRegistryName(azulejo14.getRegistryName()));
		r.register(new ItemBlockMod(azulejo15).setRegistryName(azulejo15.getRegistryName()));
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
		r.register(new ItemBlockMod(petalBlockWhite).setRegistryName(petalBlockWhite.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockOrange).setRegistryName(petalBlockOrange.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockMagenta).setRegistryName(petalBlockMagenta.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockLightBlue).setRegistryName(petalBlockLightBlue.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockYellow).setRegistryName(petalBlockYellow.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockLime).setRegistryName(petalBlockLime.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockPink).setRegistryName(petalBlockPink.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockGray).setRegistryName(petalBlockGray.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockSilver).setRegistryName(petalBlockSilver.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockCyan).setRegistryName(petalBlockCyan.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockPurple).setRegistryName(petalBlockPurple.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockBlue).setRegistryName(petalBlockBlue.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockBrown).setRegistryName(petalBlockBrown.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockGreen).setRegistryName(petalBlockGreen.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockRed).setRegistryName(petalBlockRed.getRegistryName()));
		r.register(new ItemBlockMod(petalBlockBlack).setRegistryName(petalBlockBlack.getRegistryName()));
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
		r.register(new ItemBlockMod(dryGrass).setRegistryName(dryGrass.getRegistryName()));
		r.register(new ItemBlockMod(goldenGrass).setRegistryName(goldenGrass.getRegistryName()));
		r.register(new ItemBlockMod(vividGrass).setRegistryName(vividGrass.getRegistryName()));
		r.register(new ItemBlockMod(scorchedGrass).setRegistryName(scorchedGrass.getRegistryName()));
		r.register(new ItemBlockMod(infusedGrass).setRegistryName(infusedGrass.getRegistryName()));
		r.register(new ItemBlockMod(mutatedGrass).setRegistryName(mutatedGrass.getRegistryName()));
		r.register(new ItemBlockMod(animatedTorch).setRegistryName(animatedTorch.getRegistryName()));
		initOreDict();
	}
	
	private static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name) {
		reg.register(thing.setRegistryName(new ResourceLocation(LibMisc.MOD_ID, name)));
	}

	public static void addDispenserBehaviours() {
		BlockDispenser.registerDispenseBehavior(ModItems.twigWand, new BehaviourWand());
		BlockDispenser.registerDispenseBehavior(ModItems.poolMinecart, new BehaviourPoolMinecart());
		BlockDispenser.registerDispenseBehavior(ModBlocks.felPumpkin, new BehaviourFelPumpkin());

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

	@SubscribeEvent
	public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt) {
		TileEntityType.Builder.create(TileAltar::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.ALTAR);
		TileEntityType.Builder.create(TileSpecialFlower::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SPECIAL_FLOWER);
		TileEntityType.Builder.create(TileSpreader::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SPREADER);
		TileEntityType.Builder.create(TilePool::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.POOL);
		TileEntityType.Builder.create(TileRuneAltar::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RUNE_ALTAR);
		TileEntityType.Builder.create(TilePylon::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.PYLON);
		TileEntityType.Builder.create(TileDistributor::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.DISTRIBUTOR);
		TileEntityType.Builder.create(TileManaVoid::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.MANA_VOID);
		TileEntityType.Builder.create(TileManaDetector::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.MANA_DETECTOR);
		TileEntityType.Builder.create(TileEnchanter::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.ENCHANTER);
		TileEntityType.Builder.create(TileTurntable::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.TURNTABLE);
		TileEntityType.Builder.create(TileTinyPlanet::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.TINY_PLANET);
		TileEntityType.Builder.create(TileOpenCrate::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.OPEN_CRATE);
		TileEntityType.Builder.create(TileCraftCrate::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CRAFT_CRATE);
		TileEntityType.Builder.create(TileForestEye::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.FOREST_EYE);
		TileEntityType.Builder.create(TilePlatform::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.PLATFORM);
		TileEntityType.Builder.create(TileAlfPortal::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.ALF_PORTAL);
		TileEntityType.Builder.create(TileBifrost::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.BIFROST);
		TileEntityType.Builder.create(TileFloatingFlower::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.MINI_ISLAND);
		TileEntityType.Builder.create(TileTinyPotato::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.TINY_POTATO);
		TileEntityType.Builder.create(TileSpawnerClaw::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SPAWNER_CLAW);
		TileEntityType.Builder.create(TileEnderEye::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.ENDER_EYE_BLOCK);
		TileEntityType.Builder.create(TileStarfield::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.STARFIELD);
		TileEntityType.Builder.create(TileRFGenerator::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RF_GENERATOR);
		TileEntityType.Builder.create(TileBrewery::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.BREWERY);
		TileEntityType.Builder.create(TileTerraPlate::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.TERRA_PLATE);
		TileEntityType.Builder.create(TileRedStringContainer::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RED_STRING_CONTAINER);
		TileEntityType.Builder.create(TileRedStringDispenser::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RED_STRING_DISPENSER);
		TileEntityType.Builder.create(TileRedStringFertilizer::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RED_STRING_FERTILIZER);
		TileEntityType.Builder.create(TileRedStringComparator::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RED_STRING_COMPARATOR);
		TileEntityType.Builder.create(TileRedStringRelay::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RED_STRING_RELAY);
		TileEntityType.Builder.create(TileFloatingSpecialFlower::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.FLOATING_SPECIAL_FLOWER);
		TileEntityType.Builder.create(TileManaFlame::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.MANA_FLAME);
		TileEntityType.Builder.create(TilePrism::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.PRISM);
		TileEntityType.Builder.create(TileCorporeaIndex::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CORPOREA_INDEX);
		TileEntityType.Builder.create(TileCorporeaFunnel::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CORPOREA_FUNNEL);
		TileEntityType.Builder.create(TilePump::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.PUMP);
		TileEntityType.Builder.create(TileFakeAir::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.FAKE_AIR);
		TileEntityType.Builder.create(TileCorporeaInterceptor::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CORPOREA_INTERCEPTOR);
		TileEntityType.Builder.create(TileCorporeaCrystalCube::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CORPOREA_CRYSTAL_CUBE);
		TileEntityType.Builder.create(TileIncensePlate::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.INCENSE_PLATE);
		TileEntityType.Builder.create(TileHourglass::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.HOURGLASS);
		TileEntityType.Builder.create(TileSparkChanger::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SPARK_CHANGER);
		TileEntityType.Builder.create(TileCocoon::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.COCOON);
		TileEntityType.Builder.create(TileLightRelay::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.LIGHT_RELAY);
		TileEntityType.Builder.create(TileCacophonium::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CACOPHONIUM);
		TileEntityType.Builder.create(TileBellows::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.BELLOWS);
		TileEntityType.Builder.create(TileCell::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CELL_BLOCK);
		TileEntityType.Builder.create(TileRedStringInterceptor::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.RED_STRING_INTERCEPTOR);
		TileEntityType.Builder.create(TileGaiaHead::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.GAIA_HEAD);
		TileEntityType.Builder.create(TileCorporeaRetainer::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CORPOREA_RETAINER);
		TileEntityType.Builder.create(TileTeruTeruBozu::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.TERU_TERU_BOZU);
		TileEntityType.Builder.create(TileAvatar::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.AVATAR);
		TileEntityType.Builder.create(TileAnimatedTorch::new).build(null).setRegistryName(LibMisc.MOD_ID, LibBlockNames.ANIMATED_TORCH);

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

	private static void registerSubTileWithMini(ResourceLocation key, Class<? extends SubTileEntity> clazz) {
		BotaniaAPI.registerSubTile(key, clazz);

		for(Class innerClazz : clazz.getDeclaredClasses())
			if(innerClazz.getSimpleName().equals("Mini"))
				BotaniaAPI.registerMiniSubTile(new ResourceLocation(key.getNamespace(), key.getPath() + "Chibi"), innerClazz, key);
	}

	// This is a method returning fresh arrays to account for registry replacements
	private static Block[] getOrderedFlowers() {
		return new Block[] {
				whiteFlower, orangeFlower, magentaFlower, lightBlueFlower,
				yellowFlower, limeFlower, pinkFlower, grayFlower,
				lightGrayFlower, cyanFlower, purpleFlower, blueFlower,
				brownFlower, greenFlower, redFlower, blackFlower
		};
	}

	public static Block getFlower(EnumDyeColor color) {
		return getOrderedFlowers()[color.getId()];
	}

}
