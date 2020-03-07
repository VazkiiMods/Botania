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
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.client.render.tile.RenderTilePylon;
import vazkii.botania.client.render.tile.TEISR;
import vazkii.botania.common.block.corporea.BlockCorporeaCrystalCube;
import vazkii.botania.common.block.corporea.BlockCorporeaFunnel;
import vazkii.botania.common.block.corporea.BlockCorporeaIndex;
import vazkii.botania.common.block.corporea.BlockCorporeaInterceptor;
import vazkii.botania.common.block.corporea.BlockCorporeaRetainer;
import vazkii.botania.common.block.decor.BlockBuriedPetals;
import vazkii.botania.common.block.decor.BlockElfGlass;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.decor.BlockManaFlame;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.block.decor.BlockPetalBlock;
import vazkii.botania.common.block.decor.BlockShinyFlower;
import vazkii.botania.common.block.decor.BlockStarfield;
import vazkii.botania.common.block.decor.BlockTinyPotato;
import vazkii.botania.common.block.decor.ItemBlockBlaze;
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
import vazkii.botania.common.item.ItemGaiaHead;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockDreamwood;
import vazkii.botania.common.item.block.ItemBlockElven;
import vazkii.botania.common.item.block.ItemBlockPool;
import vazkii.botania.common.item.block.ItemBlockTinyPotato;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.Arrays;
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

		Block.Properties builder = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT);
		for(DyeColor color : DyeColor.values()) {
			register(r, new BlockModFlower(color, builder), color.getName() + LibBlockNames.MYSTICAL_FLOWER_SUFFIX);
		}

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).sound(SoundType.STONE);
		for(BlockAltar.Variant v : BlockAltar.Variant.values()) {
			r.register(new BlockAltar(v, builder)
					.setRegistryName(LibMisc.MOD_ID, LibBlockNames.APOTHECARY_PREFIX + v.name().toLowerCase(Locale.ROOT)));
		}

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		register(r, new BlockMod(builder), LibBlockNames.LIVING_ROCK);
		register(r, new BlockMod(builder), LibBlockNames.LIVING_ROCK_BRICK);
		register(r, new BlockMod(builder), LibBlockNames.LIVING_ROCK_BRICK_MOSSY);
		register(r, new BlockMod(builder), LibBlockNames.LIVING_ROCK_BRICK_CRACKED);
		register(r, new BlockMod(builder), LibBlockNames.LIVING_ROCK_BRICK_CHISELED);

		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD);
		// todo 1.13: livingwood should support leaves
		register(r, new BlockMod(builder), LibBlockNames.LIVING_WOOD);
		register(r, new BlockMod(builder), LibBlockNames.LIVING_WOOD_PLANKS);
		register(r, new BlockMod(builder), LibBlockNames.LIVING_WOOD_PLANKS_MOSSY);
		register(r, new BlockMod(builder), LibBlockNames.LIVING_WOOD_FRAMED);
		register(r, new BlockMod(builder), LibBlockNames.LIVING_WOOD_PATTERN_FRAMED);
		register(r, new BlockMod(builder.lightValue(12)), LibBlockNames.LIVING_WOOD_GLIMMERING);

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
		for (DyeColor color : DyeColor.values()) {
			register(r, new BlockShinyFlower(color, builder), color.getName() + LibBlockNames.SHINY_FLOWER_SUFFIX);
		}
		
		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2, 5).sound(SoundType.WOOD);
		register(r, new BlockPlatform(BlockPlatform.Variant.ABSTRUSE, builder), LibBlockNames.PLATFORM_ABSTRUSE);
		register(r, new BlockPlatform(BlockPlatform.Variant.SPECTRAL, builder), LibBlockNames.PLATFORM_SPECTRAL);
		register(r, new BlockPlatform(BlockPlatform.Variant.INFRANGIBLE, Block.Properties.create(Material.WOOD).hardnessAndResistance(-1, Float.MAX_VALUE).sound(SoundType.WOOD)), LibBlockNames.PLATFORM_INFRANGIBLE);
		register(r, new BlockAlfPortal(Block.Properties.create(Material.WOOD).hardnessAndResistance(10).sound(SoundType.WOOD)), LibBlockNames.ALF_PORTAL);

		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD);
		// todo 1.13: dreamwood should support leaves?
		register(r, new BlockMod(builder), LibBlockNames.DREAM_WOOD);
		register(r, new BlockMod(builder), LibBlockNames.DREAM_WOOD_PLANKS);
		register(r, new BlockMod(builder), LibBlockNames.DREAM_WOOD_PLANKS_MOSSY);
		register(r, new BlockMod(builder), LibBlockNames.DREAM_WOOD_FRAMED);
		register(r, new BlockMod(builder), LibBlockNames.DREAM_WOOD_PATTERN_FRAMED);
		register(r, new BlockMod(builder.lightValue(12)), LibBlockNames.DREAM_WOOD_GLIMMERING);

		register(r, new BlockConjurationCatalyst(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)), LibBlockNames.CONJURATION_CATALYST);
		register(r, new BlockBifrost(Block.Properties.create(Material.GLASS).hardnessAndResistance(-1, 0.3F).lightValue(15).sound(SoundType.GLASS).nonOpaque()), LibBlockNames.BIFROST);
		register(r, new BlockSolidVines(Block.Properties.create(Material.TALL_PLANTS).hardnessAndResistance(0.2F).sound(SoundType.PLANT).nonOpaque()), LibBlockNames.SOLID_VINE);
		
		builder = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT).lightValue(4);
		for(DyeColor color : DyeColor.values()) {
			register(r, new BlockBuriedPetals(color, builder), color.getName() + LibBlockNames.BURIED_PETALS_SUFFIX);
		}

		builder = Block.Properties.create(Material.EARTH).hardnessAndResistance(0.5F).sound(SoundType.GROUND).lightValue(15);
		for(DyeColor color : DyeColor.values()) {
			register(r, new BlockFloatingFlower(color, builder), color.getName() + LibBlockNames.FLOATING_FLOWER_SUFFIX);
		}

		register(r, new BlockTinyPotato(Block.Properties.create(Material.WOOL).hardnessAndResistance(0.25F)), LibBlockNames.TINY_POTATO);
		register(r, new BlockSpawnerClaw(Block.Properties.create(Material.IRON).hardnessAndResistance(3)), LibBlockNames.SPAWNER_CLAW);

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 5).sound(SoundType.STONE);
		for (int i = 0; i < 16; i++) {
			register(r, new BlockMod(builder), LibBlockNames.AZULEJO_PREFIX + i);
		}

		register(r, new BlockEnderEye(Block.Properties.create(Material.IRON).hardnessAndResistance(3, 10).sound(SoundType.METAL)), LibBlockNames.ENDER_EYE_BLOCK);
		register(r, new BlockStarfield(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 2000).sound(SoundType.METAL)), LibBlockNames.STARFIELD);
		register(r, new BlockRFGenerator(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)), LibBlockNames.FLUXFIELD);
		register(r, new BlockElfGlass(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).lightValue(15).nonOpaque()), LibBlockNames.ELF_GLASS);
		register(r, new BlockBrewery(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)), LibBlockNames.BREWERY);
		register(r, new GlassBlock(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).lightValue(15).nonOpaque()), LibBlockNames.MANA_GLASS);
		register(r, new BlockTerraPlate(Block.Properties.create(Material.IRON).hardnessAndResistance(3, 10).sound(SoundType.METAL)), LibBlockNames.TERRA_PLATE);

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		register(r, new BlockRedStringContainer(builder), LibBlockNames.RED_STRING_CONTAINER);
		register(r, new BlockRedStringDispenser(builder), LibBlockNames.RED_STRING_DISPENSER);
		register(r, new BlockRedStringFertilizer(builder), LibBlockNames.RED_STRING_FERTILIZER);
		register(r, new BlockRedStringComparator(builder), LibBlockNames.RED_STRING_COMPARATOR);
		register(r, new BlockRedStringRelay(builder), LibBlockNames.RED_STRING_RELAY);
		register(r, new BlockRedStringInterceptor(builder), LibBlockNames.RED_STRING_INTERCEPTOR);
		register(r, new BlockManaFlame(Block.Properties.create(Material.WOOL).sound(SoundType.CLOTH).lightValue(15).doesNotBlockMovement()), LibBlockNames.MANA_FLAME);
		register(r, new BlockPrism(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).lightValue(15).doesNotBlockMovement()), LibBlockNames.PRISM);
		register(r, new BlockEnchantedSoil(Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.6F).sound(SoundType.PLANT)), LibBlockNames.ENCHANTED_SOIL);

		builder = Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.4F).sound(SoundType.PLANT);
		for(DyeColor color : DyeColor.values()) {
			register(r, new BlockPetalBlock(color, builder), color.getName() + LibBlockNames.PETAL_BLOCK_SUFFIX);
		}

		register(r, new BlockCorporeaIndex(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL).nonOpaque()), LibBlockNames.CORPOREA_INDEX);
		register(r, new BlockCorporeaFunnel(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL)), LibBlockNames.CORPOREA_FUNNEL);
		
		builder = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT).lightValue(3);
		for(DyeColor color : DyeColor.values()) {
			register(r, new BlockModMushroom(color, builder), color.getName() + LibBlockNames.MUSHROOM_SUFFIX);
		}
		
		register(r, new BlockPump(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)), LibBlockNames.PUMP);

		builder = Block.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT);
		for(DyeColor color : DyeColor.values()) {
			register(r, new BlockModDoubleFlower(color, builder), color.getName() + LibBlockNames.DOUBLE_FLOWER_SUFFIX);
		}

		register(r, new BlockFakeAir(Block.Properties.create(Material.STRUCTURE_VOID).tickRandomly()), LibBlockNames.FAKE_AIR);
		register(r, new BlockMod(Block.Properties.create(Material.IRON).hardnessAndResistance(3, 10).sound(SoundType.METAL).lightValue(15)), LibBlockNames.BLAZE_BLOCK);
		register(r, new BlockCorporeaInterceptor(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL)), LibBlockNames.CORPOREA_INTERCEPTOR);
		register(r, new BlockCorporeaCrystalCube(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL)), LibBlockNames.CORPOREA_CRYSTAL_CUBE);
		register(r, new BlockIncensePlate(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)), LibBlockNames.INCENSE_PLATE);
		register(r, new BlockHourglass(Block.Properties.create(Material.IRON).hardnessAndResistance(2).sound(SoundType.METAL)), LibBlockNames.HOURGLASS);
		register(r, new BlockGhostRail(Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.7F).sound(SoundType.METAL)), LibBlockNames.GHOST_RAIL);
		register(r, new BlockSparkChanger(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)), LibBlockNames.SPARK_CHANGER);
		register(r, new BlockRoot(Block.Properties.create(Material.PLANTS).hardnessAndResistance(1.2F).sound(SoundType.WOOD)), LibBlockNames.ROOT);
		register(r, new BlockFelPumpkin(Block.Properties.from(Blocks.CARVED_PUMPKIN)), LibBlockNames.FEL_PUMPKIN);
		register(r, new BlockCocoon(Block.Properties.create(Material.WOOL).hardnessAndResistance(3, 60).sound(SoundType.CLOTH)), LibBlockNames.COCOON);

		builder = Block.Properties.create(Material.GLASS).doesNotBlockMovement();
		register(r, new BlockLightRelay(LuminizerVariant.DEFAULT, builder), LibBlockNames.LIGHT_RELAY);
		register(r, new BlockLightRelay(LuminizerVariant.DETECTOR, builder), "detector" + LibBlockNames.LIGHT_RELAY_SUFFIX);
		register(r, new BlockLightRelay(LuminizerVariant.FORK, builder), "fork" + LibBlockNames.LIGHT_RELAY_SUFFIX);
		register(r, new BlockLightRelay(LuminizerVariant.TOGGLE, builder), "toggle" + LibBlockNames.LIGHT_RELAY_SUFFIX);

		register(r, new BlockLightLauncher(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)), LibBlockNames.LIGHT_LAUNCHER);
		register(r, new BlockManaBomb(Block.Properties.create(Material.WOOD).hardnessAndResistance(12).sound(SoundType.WOOD)), LibBlockNames.MANA_BOMB);
		register(r, new BlockCacophonium(Block.Properties.create(Material.WOOD).hardnessAndResistance(0.8F)), LibBlockNames.CACOPHONIUM);
		register(r, new BlockBellows(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)), LibBlockNames.BELLOWS);
		register(r, new BlockBifrostPerm(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).lightValue(15).sound(SoundType.GLASS).nonOpaque()), LibBlockNames.BIFROST_PERM);
		register(r, new BlockCell(Block.Properties.create(Material.GOURD).sound(SoundType.CLOTH)), LibBlockNames.CELL_BLOCK);
		register(r, new BlockGaiaHeadWall(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1)), LibBlockNames.GAIA_WALL_HEAD);
		register(r, new BlockGaiaHead(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1)), LibBlockNames.GAIA_HEAD);
		register(r, new BlockCorporeaRetainer(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL)), LibBlockNames.CORPOREA_RETAINER);
		register(r, new BlockTeruTeruBozu(Block.Properties.create(Material.WOOL)), LibBlockNames.TERU_TERU_BOZU);
		register(r, new BlockMod(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)), LibBlockNames.SHIMMERROCK);
		register(r, new BlockMod(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)), LibBlockNames.SHIMMERWOOD_PLANKS);
		register(r, new BlockAvatar(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)), LibBlockNames.AVATAR);

		builder = Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.6F).tickRandomly().sound(SoundType.PLANT);
		for(BlockAltGrass.Variant v : BlockAltGrass.Variant.values()) {
			register(r, new BlockAltGrass(v, builder), v.name().toLowerCase(Locale.ROOT) + LibBlockNames.ALT_GRASS_SUFFIX);
		}

		register(r, new BlockAnimatedTorch(Block.Properties.create(Material.MISCELLANEOUS).lightValue(7).nonOpaque()), LibBlockNames.ANIMATED_TORCH);
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		Item.Properties props = ModItems.defaultBuilder();
		
		register(r, new BlockItem(whiteFlower, props), whiteFlower.getRegistryName());
		register(r, new BlockItem(orangeFlower, props), orangeFlower.getRegistryName());
		register(r, new BlockItem(magentaFlower, props), magentaFlower.getRegistryName());
		register(r, new BlockItem(lightBlueFlower, props), lightBlueFlower.getRegistryName());
		register(r, new BlockItem(yellowFlower, props), yellowFlower.getRegistryName());
		register(r, new BlockItem(limeFlower, props), limeFlower.getRegistryName());
		register(r, new BlockItem(pinkFlower, props), pinkFlower.getRegistryName());
		register(r, new BlockItem(grayFlower, props), grayFlower.getRegistryName());
		register(r, new BlockItem(lightGrayFlower, props), lightGrayFlower.getRegistryName());
		register(r, new BlockItem(cyanFlower, props), cyanFlower.getRegistryName());
		register(r, new BlockItem(purpleFlower, props), purpleFlower.getRegistryName());
		register(r, new BlockItem(blueFlower, props), blueFlower.getRegistryName());
		register(r, new BlockItem(brownFlower, props), brownFlower.getRegistryName());
		register(r, new BlockItem(greenFlower, props), greenFlower.getRegistryName());
		register(r, new BlockItem(redFlower, props), redFlower.getRegistryName());
		register(r, new BlockItem(blackFlower, props), blackFlower.getRegistryName());
		register(r, new BlockItem(defaultAltar, props), defaultAltar.getRegistryName());
		register(r, new BlockItem(forestAltar, props), forestAltar.getRegistryName());
		register(r, new BlockItem(plainsAltar, props), plainsAltar.getRegistryName());
		register(r, new BlockItem(mountainAltar, props), mountainAltar.getRegistryName());
		register(r, new BlockItem(fungalAltar, props), fungalAltar.getRegistryName());
		register(r, new BlockItem(swampAltar, props), swampAltar.getRegistryName());
		register(r, new BlockItem(desertAltar, props), desertAltar.getRegistryName());
		register(r, new BlockItem(taigaAltar, props), taigaAltar.getRegistryName());
		register(r, new BlockItem(mesaAltar, props), mesaAltar.getRegistryName());
		register(r, new BlockItem(mossyAltar, props), mossyAltar.getRegistryName());
		register(r, new BlockItem(livingrock, props), livingrock.getRegistryName());
		register(r, new BlockItem(livingrockBrick, props), livingrockBrick.getRegistryName());
		register(r, new BlockItem(livingrockBrickChiseled, props), livingrockBrickChiseled.getRegistryName());
		register(r, new BlockItem(livingrockBrickCracked, props), livingrockBrickCracked.getRegistryName());
		register(r, new BlockItem(livingrockBrickMossy, props), livingrockBrickMossy.getRegistryName());
		register(r, new BlockItem(livingwood, props), livingwood.getRegistryName());
		register(r, new BlockItem(livingwoodPlanks, props), livingwoodPlanks.getRegistryName());
		register(r, new BlockItem(livingwoodPlanksMossy, props), livingwoodPlanksMossy.getRegistryName());
		register(r, new BlockItem(livingwoodFramed, props), livingwoodFramed.getRegistryName());
		register(r, new BlockItem(livingwoodPatternFramed, props), livingwoodPatternFramed.getRegistryName());
		register(r, new BlockItem(livingwoodGlimmering, props), livingwoodGlimmering.getRegistryName());
		register(r, new BlockItem(manaSpreader, props), manaSpreader.getRegistryName());
		register(r, new BlockItem(redstoneSpreader, props), redstoneSpreader.getRegistryName());
		register(r, new BlockItem(elvenSpreader, props), elvenSpreader.getRegistryName());
		register(r, new BlockItem(gaiaSpreader, props), gaiaSpreader.getRegistryName());
		register(r, new ItemBlockPool(manaPool, props), manaPool.getRegistryName());
		register(r, new ItemBlockPool(creativePool, props), creativePool.getRegistryName());
		register(r, new ItemBlockPool(dilutedPool, props), dilutedPool.getRegistryName());
		register(r, new ItemBlockPool(fabulousPool, props), fabulousPool.getRegistryName());
		register(r, new BlockItem(runeAltar, props), runeAltar.getRegistryName());
		register(r, new BlockItem(pistonRelay, props), pistonRelay.getRegistryName());
		register(r, new BlockItem(distributor, props), distributor.getRegistryName());
		register(r, new BlockItem(manaVoid, props), manaVoid.getRegistryName());
		register(r, new BlockItem(manaDetector, props), manaDetector.getRegistryName());
		register(r, new BlockItem(enchanter, props), enchanter.getRegistryName());
		register(r, new BlockItem(turntable, props), turntable.getRegistryName());
		register(r, new BlockItem(tinyPlanet, props), tinyPlanet.getRegistryName());
		register(r, new BlockItem(alchemyCatalyst, props), alchemyCatalyst.getRegistryName());
		register(r, new BlockItem(openCrate, props), openCrate.getRegistryName());
		register(r, new BlockItem(craftCrate, props), craftCrate.getRegistryName());
		register(r, new BlockItem(forestEye, props), forestEye.getRegistryName());
		register(r, new BlockItem(manasteelBlock, props), manasteelBlock.getRegistryName());
		register(r, new BlockItem(terrasteelBlock, props), terrasteelBlock.getRegistryName());
		register(r, new ItemBlockElven(elementiumBlock, props), elementiumBlock.getRegistryName());
		register(r, new BlockItem(manaDiamondBlock, props), manaDiamondBlock.getRegistryName());
		register(r, new BlockItem(dragonstoneBlock, props), dragonstoneBlock.getRegistryName());
		register(r, new BlockItem(wildDrum, props), wildDrum.getRegistryName());
		register(r, new BlockItem(gatheringDrum, props), gatheringDrum.getRegistryName());
		register(r, new BlockItem(canopyDrum, props), canopyDrum.getRegistryName());
		register(r, new BlockItem(whiteShinyFlower, props), whiteShinyFlower.getRegistryName());
		register(r, new BlockItem(orangeShinyFlower, props), orangeShinyFlower.getRegistryName());
		register(r, new BlockItem(magentaShinyFlower, props), magentaShinyFlower.getRegistryName());
		register(r, new BlockItem(lightBlueShinyFlower, props), lightBlueShinyFlower.getRegistryName());
		register(r, new BlockItem(yellowShinyFlower, props), yellowShinyFlower.getRegistryName());
		register(r, new BlockItem(limeShinyFlower, props), limeShinyFlower.getRegistryName());
		register(r, new BlockItem(pinkShinyFlower, props), pinkShinyFlower.getRegistryName());
		register(r, new BlockItem(grayShinyFlower, props), grayShinyFlower.getRegistryName());
		register(r, new BlockItem(lightGrayShinyFlower, props), lightGrayShinyFlower.getRegistryName());
		register(r, new BlockItem(cyanShinyFlower, props), cyanShinyFlower.getRegistryName());
		register(r, new BlockItem(purpleShinyFlower, props), purpleShinyFlower.getRegistryName());
		register(r, new BlockItem(blueShinyFlower, props), blueShinyFlower.getRegistryName());
		register(r, new BlockItem(brownShinyFlower, props), brownShinyFlower.getRegistryName());
		register(r, new BlockItem(greenShinyFlower, props), greenShinyFlower.getRegistryName());
		register(r, new BlockItem(redShinyFlower, props), redShinyFlower.getRegistryName());
		register(r, new BlockItem(blackShinyFlower, props), blackShinyFlower.getRegistryName());
		register(r, new BlockItem(abstrusePlatform, props), abstrusePlatform.getRegistryName());
		register(r, new BlockItem(spectralPlatform, props), spectralPlatform.getRegistryName());
		register(r, new BlockItem(infrangiblePlatform, props), infrangiblePlatform.getRegistryName());
		register(r, new BlockItem(alfPortal, props), alfPortal.getRegistryName());
		register(r, new ItemBlockDreamwood(dreamwood, props), dreamwood.getRegistryName());
		register(r, new ItemBlockDreamwood(dreamwoodPlanks, props), dreamwoodPlanks.getRegistryName());
		register(r, new ItemBlockDreamwood(dreamwoodPlanksMossy, props), dreamwoodPlanksMossy.getRegistryName());
		register(r, new ItemBlockDreamwood(dreamwoodFramed, props), dreamwoodFramed.getRegistryName());
		register(r, new ItemBlockDreamwood(dreamwoodPatternFramed, props), dreamwoodPatternFramed.getRegistryName());
		register(r, new ItemBlockDreamwood(dreamwoodGlimmering, props), dreamwoodGlimmering.getRegistryName());
		register(r, new BlockItem(conjurationCatalyst, props), conjurationCatalyst.getRegistryName());
		register(r, new BlockItem(bifrost, props), bifrost.getRegistryName());
		register(r, new BlockItem(whiteFloatingFlower, props), whiteFloatingFlower.getRegistryName());
		register(r, new BlockItem(orangeFloatingFlower, props), orangeFloatingFlower.getRegistryName());
		register(r, new BlockItem(magentaFloatingFlower, props), magentaFloatingFlower.getRegistryName());
		register(r, new BlockItem(lightBlueFloatingFlower, props), lightBlueFloatingFlower.getRegistryName());
		register(r, new BlockItem(yellowFloatingFlower, props), yellowFloatingFlower.getRegistryName());
		register(r, new BlockItem(limeFloatingFlower, props), limeFloatingFlower.getRegistryName());
		register(r, new BlockItem(pinkFloatingFlower, props), pinkFloatingFlower.getRegistryName());
		register(r, new BlockItem(grayFloatingFlower, props), grayFloatingFlower.getRegistryName());
		register(r, new BlockItem(lightGrayFloatingFlower, props), lightGrayFloatingFlower.getRegistryName());
		register(r, new BlockItem(cyanFloatingFlower, props), cyanFloatingFlower.getRegistryName());
		register(r, new BlockItem(purpleFloatingFlower, props), purpleFloatingFlower.getRegistryName());
		register(r, new BlockItem(blueFloatingFlower, props), blueFloatingFlower.getRegistryName());
		register(r, new BlockItem(brownFloatingFlower, props), brownFloatingFlower.getRegistryName());
		register(r, new BlockItem(greenFloatingFlower, props), greenFloatingFlower.getRegistryName());
		register(r, new BlockItem(redFloatingFlower, props), redFloatingFlower.getRegistryName());
		register(r, new BlockItem(blackFloatingFlower, props), blackFloatingFlower.getRegistryName());
		register(r, new ItemBlockTinyPotato(tinyPotato, props), tinyPotato.getRegistryName());
		register(r, new BlockItem(spawnerClaw, props), spawnerClaw.getRegistryName());
		register(r, new BlockItem(azulejo0, props), azulejo0.getRegistryName());
		register(r, new BlockItem(azulejo1, props), azulejo1.getRegistryName());
		register(r, new BlockItem(azulejo2, props), azulejo2.getRegistryName());
		register(r, new BlockItem(azulejo3, props), azulejo3.getRegistryName());
		register(r, new BlockItem(azulejo4, props), azulejo4.getRegistryName());
		register(r, new BlockItem(azulejo5, props), azulejo5.getRegistryName());
		register(r, new BlockItem(azulejo6, props), azulejo6.getRegistryName());
		register(r, new BlockItem(azulejo7, props), azulejo7.getRegistryName());
		register(r, new BlockItem(azulejo8, props), azulejo8.getRegistryName());
		register(r, new BlockItem(azulejo9, props), azulejo9.getRegistryName());
		register(r, new BlockItem(azulejo10, props), azulejo10.getRegistryName());
		register(r, new BlockItem(azulejo11, props), azulejo11.getRegistryName());
		register(r, new BlockItem(azulejo12, props), azulejo12.getRegistryName());
		register(r, new BlockItem(azulejo13, props), azulejo13.getRegistryName());
		register(r, new BlockItem(azulejo14, props), azulejo14.getRegistryName());
		register(r, new BlockItem(azulejo15, props), azulejo15.getRegistryName());
		register(r, new BlockItem(enderEye, props), enderEye.getRegistryName());
		register(r, new BlockItem(starfield, props), starfield.getRegistryName());
		register(r, new BlockItem(rfGenerator, props), rfGenerator.getRegistryName());
		register(r, new ItemBlockElven(elfGlass, props), elfGlass.getRegistryName());
		register(r, new BlockItem(manaGlass, props), manaGlass.getRegistryName());
		register(r, new BlockItem(terraPlate, props), terraPlate.getRegistryName());
		register(r, new BlockItem(redStringContainer, props), redStringContainer.getRegistryName());
		register(r, new BlockItem(redStringDispenser, props), redStringDispenser.getRegistryName());
		register(r, new BlockItem(redStringFertilizer, props), redStringFertilizer.getRegistryName());
		register(r, new BlockItem(redStringComparator, props), redStringComparator.getRegistryName());
		register(r, new BlockItem(redStringRelay, props), redStringRelay.getRegistryName());
		register(r, new BlockItem(prism, props), prism.getRegistryName());
		register(r, new BlockItem(enchantedSoil, props), enchantedSoil.getRegistryName());
		register(r, new BlockItem(petalBlockWhite, props), petalBlockWhite.getRegistryName());
		register(r, new BlockItem(petalBlockOrange, props), petalBlockOrange.getRegistryName());
		register(r, new BlockItem(petalBlockMagenta, props), petalBlockMagenta.getRegistryName());
		register(r, new BlockItem(petalBlockLightBlue, props), petalBlockLightBlue.getRegistryName());
		register(r, new BlockItem(petalBlockYellow, props), petalBlockYellow.getRegistryName());
		register(r, new BlockItem(petalBlockLime, props), petalBlockLime.getRegistryName());
		register(r, new BlockItem(petalBlockPink, props), petalBlockPink.getRegistryName());
		register(r, new BlockItem(petalBlockGray, props), petalBlockGray.getRegistryName());
		register(r, new BlockItem(petalBlockSilver, props), petalBlockSilver.getRegistryName());
		register(r, new BlockItem(petalBlockCyan, props), petalBlockCyan.getRegistryName());
		register(r, new BlockItem(petalBlockPurple, props), petalBlockPurple.getRegistryName());
		register(r, new BlockItem(petalBlockBlue, props), petalBlockBlue.getRegistryName());
		register(r, new BlockItem(petalBlockBrown, props), petalBlockBrown.getRegistryName());
		register(r, new BlockItem(petalBlockGreen, props), petalBlockGreen.getRegistryName());
		register(r, new BlockItem(petalBlockRed, props), petalBlockRed.getRegistryName());
		register(r, new BlockItem(petalBlockBlack, props), petalBlockBlack.getRegistryName());
		register(r, new BlockItem(corporeaFunnel, props), corporeaFunnel.getRegistryName());
		register(r, new BlockItem(whiteMushroom, props), whiteMushroom.getRegistryName());
		register(r, new BlockItem(orangeMushroom, props), orangeMushroom.getRegistryName());
		register(r, new BlockItem(magentaMushroom, props), magentaMushroom.getRegistryName());
		register(r, new BlockItem(lightBlueMushroom, props), lightBlueMushroom.getRegistryName());
		register(r, new BlockItem(yellowMushroom, props), yellowMushroom.getRegistryName());
		register(r, new BlockItem(limeMushroom, props), limeMushroom.getRegistryName());
		register(r, new BlockItem(pinkMushroom, props), pinkMushroom.getRegistryName());
		register(r, new BlockItem(grayMushroom, props), grayMushroom.getRegistryName());
		register(r, new BlockItem(lightGrayMushroom, props), lightGrayMushroom.getRegistryName());
		register(r, new BlockItem(cyanMushroom, props), cyanMushroom.getRegistryName());
		register(r, new BlockItem(purpleMushroom, props), purpleMushroom.getRegistryName());
		register(r, new BlockItem(blueMushroom, props), blueMushroom.getRegistryName());
		register(r, new BlockItem(brownMushroom, props), brownMushroom.getRegistryName());
		register(r, new BlockItem(greenMushroom, props), greenMushroom.getRegistryName());
		register(r, new BlockItem(redMushroom, props), redMushroom.getRegistryName());
		register(r, new BlockItem(blackMushroom, props), blackMushroom.getRegistryName());
		register(r, new BlockItem(pump, props), pump.getRegistryName());
		register(r, new BlockItem(doubleFlowerWhite, props), doubleFlowerWhite.getRegistryName());
		register(r, new BlockItem(doubleFlowerOrange, props), doubleFlowerOrange.getRegistryName());
		register(r, new BlockItem(doubleFlowerMagenta, props), doubleFlowerMagenta.getRegistryName());
		register(r, new BlockItem(doubleFlowerLightBlue, props), doubleFlowerLightBlue.getRegistryName());
		register(r, new BlockItem(doubleFlowerYellow, props), doubleFlowerYellow.getRegistryName());
		register(r, new BlockItem(doubleFlowerLime, props), doubleFlowerLime.getRegistryName());
		register(r, new BlockItem(doubleFlowerPink, props), doubleFlowerPink.getRegistryName());
		register(r, new BlockItem(doubleFlowerGray, props), doubleFlowerGray.getRegistryName());
		register(r, new BlockItem(doubleFlowerLightGray, props), doubleFlowerLightGray.getRegistryName());
		register(r, new BlockItem(doubleFlowerCyan, props), doubleFlowerCyan.getRegistryName());
		register(r, new BlockItem(doubleFlowerPurple, props), doubleFlowerPurple.getRegistryName());
		register(r, new BlockItem(doubleFlowerBlue, props), doubleFlowerBlue.getRegistryName());
		register(r, new BlockItem(doubleFlowerBrown, props), doubleFlowerBrown.getRegistryName());
		register(r, new BlockItem(doubleFlowerGreen, props), doubleFlowerGreen.getRegistryName());
		register(r, new BlockItem(doubleFlowerRed, props), doubleFlowerRed.getRegistryName());
		register(r, new BlockItem(doubleFlowerBlack, props), doubleFlowerBlack.getRegistryName());
		register(r, new ItemBlockBlaze(blazeBlock, props), blazeBlock.getRegistryName());
		register(r, new BlockItem(corporeaInterceptor, props), corporeaInterceptor.getRegistryName());
		register(r, new BlockItem(incensePlate, props), incensePlate.getRegistryName());
		register(r, new BlockItem(ghostRail, props), ghostRail.getRegistryName());
		register(r, new BlockItem(sparkChanger, props), sparkChanger.getRegistryName());
		register(r, new BlockItem(root, props), root.getRegistryName());
		register(r, new BlockItem(felPumpkin, props), felPumpkin.getRegistryName());
		register(r, new BlockItem(cocoon, props), cocoon.getRegistryName());
		register(r, new BlockItem(lightRelayDefault, props), lightRelayDefault.getRegistryName());
		register(r, new BlockItem(lightRelayDetector, props), lightRelayDetector.getRegistryName());
		register(r, new BlockItem(lightRelayFork, props), lightRelayFork.getRegistryName());
		register(r, new BlockItem(lightRelayToggle, props), lightRelayToggle.getRegistryName());
		register(r, new BlockItem(lightLauncher, props), lightLauncher.getRegistryName());
		register(r, new BlockItem(manaBomb, props), manaBomb.getRegistryName());
		register(r, new BlockItem(cacophonium, props), cacophonium.getRegistryName());
		register(r, new BlockItem(bifrostPerm, props), bifrostPerm.getRegistryName());
		register(r, new BlockItem(cellBlock, props), cellBlock.getRegistryName());
		register(r, new BlockItem(redStringInterceptor, props), redStringInterceptor.getRegistryName());
		register(r, new BlockItem(corporeaRetainer, props), corporeaRetainer.getRegistryName());
		register(r, new BlockItem(shimmerrock, props), shimmerrock.getRegistryName());
		register(r, new BlockItem(shimmerwoodPlanks, props), shimmerwoodPlanks.getRegistryName());
		register(r, new BlockItem(dryGrass, props), dryGrass.getRegistryName());
		register(r, new BlockItem(goldenGrass, props), goldenGrass.getRegistryName());
		register(r, new BlockItem(vividGrass, props), vividGrass.getRegistryName());
		register(r, new BlockItem(scorchedGrass, props), scorchedGrass.getRegistryName());
		register(r, new BlockItem(infusedGrass, props), infusedGrass.getRegistryName());
		register(r, new BlockItem(mutatedGrass, props), mutatedGrass.getRegistryName());
		register(r, new BlockItem(animatedTorch, props), animatedTorch.getRegistryName());
		register(r, new BlockItem(corporeaCrystalCube, props), corporeaCrystalCube.getRegistryName());

		DistExecutor.runForDist(() -> () -> registerWithTEISRS(r), () -> () -> registerWithoutTEISRS(r));
	}

	// Yay side-safety -.-

	@OnlyIn(Dist.DEDICATED_SERVER)
	private static Void registerWithoutTEISRS(IForgeRegistry<Item> r) {
		Item.Properties props = ModItems.defaultBuilder();
		register(r, new BlockItem(manaPylon, props), manaPylon.getRegistryName());
		register(r, new BlockItem(naturaPylon, props), naturaPylon.getRegistryName());
		register(r, new BlockItem(gaiaPylon, props), gaiaPylon.getRegistryName());

		register(r, new BlockItem(teruTeruBozu, props), teruTeruBozu.getRegistryName());
		register(r, new BlockItem(avatar, props), avatar.getRegistryName());
		register(r, new BlockItem(bellows, props), bellows.getRegistryName());
		register(r, new BlockItem(brewery, props), brewery.getRegistryName());
		register(r, new BlockItem(corporeaIndex, props), corporeaIndex.getRegistryName());
		register(r, new BlockItem(hourglass, props), hourglass.getRegistryName());
		Item head = new ItemGaiaHead(gaiaHead, gaiaHeadWall, ModItems.defaultBuilder().rarity(Rarity.UNCOMMON));
		register(r, head, gaiaHead.getRegistryName());
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	private static Void registerWithTEISRS(IForgeRegistry<Item> r) {
		Item.Properties pylonProps = ModItems.defaultBuilder().setISTER(() -> () -> new RenderTilePylon.TEISR());
		register(r, new BlockItem(manaPylon, pylonProps), manaPylon.getRegistryName());
		register(r, new BlockItem(naturaPylon, pylonProps), naturaPylon.getRegistryName());
		register(r, new BlockItem(gaiaPylon, pylonProps), gaiaPylon.getRegistryName());

		register(r, new BlockItem(teruTeruBozu, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(teruTeruBozu))), teruTeruBozu.getRegistryName());
		register(r, new BlockItem(avatar, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(avatar))), avatar.getRegistryName());
		register(r, new BlockItem(bellows, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(bellows))), bellows.getRegistryName());
		register(r, new BlockItem(brewery, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(brewery))), brewery.getRegistryName());
		register(r, new BlockItem(corporeaIndex, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(corporeaIndex))), corporeaIndex.getRegistryName());
		register(r, new BlockItem(hourglass, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(hourglass))), hourglass.getRegistryName());
		Item head = new ItemGaiaHead(gaiaHead, gaiaHeadWall, ModItems.defaultBuilder().rarity(Rarity.UNCOMMON).setISTER(() -> () -> new TEISR(gaiaHead)));
		register(r, head, gaiaHead.getRegistryName());
		return null;
	}

	public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name) {
		reg.register(thing.setRegistryName(name));
	}
	
	public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name) {
		register(reg, thing, new ResourceLocation(LibMisc.MOD_ID, name));
	}

	public static void addDispenserBehaviours() {
		DispenserBlock.registerDispenseBehavior(ModItems.twigWand, new BehaviourWand());
		DispenserBlock.registerDispenseBehavior(ModItems.poolMinecart, new BehaviourPoolMinecart());
		DispenserBlock.registerDispenseBehavior(ModBlocks.felPumpkin, new BehaviourFelPumpkin());

		SeedBehaviours.init();
	}
	
	@SubscribeEvent
	public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt) {
		IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();
		register(r, TileEntityType.Builder.create(TileAltar::new,
				defaultAltar, forestAltar, plainsAltar, mountainAltar, fungalAltar,
				swampAltar, desertAltar, taigaAltar, mesaAltar, mossyAltar
		).build(null), LibBlockNames.ALTAR);
		register(r, TileEntityType.Builder.create(TileSpreader::new, manaSpreader, redstoneSpreader, elvenSpreader, gaiaSpreader).build(null), LibBlockNames.SPREADER);
		register(r, TileEntityType.Builder.create(TilePool::new, manaPool, dilutedPool, fabulousPool, creativePool).build(null), LibBlockNames.POOL);
		register(r, TileEntityType.Builder.create(TileRuneAltar::new, runeAltar).build(null), LibBlockNames.RUNE_ALTAR);
		register(r, TileEntityType.Builder.create(TilePylon::new, manaPylon, naturaPylon, gaiaPylon).build(null), LibBlockNames.PYLON);
		register(r, TileEntityType.Builder.create(TileDistributor::new, distributor).build(null), LibBlockNames.DISTRIBUTOR);
		register(r, TileEntityType.Builder.create(TileManaVoid::new, manaVoid).build(null), LibBlockNames.MANA_VOID);
		register(r, TileEntityType.Builder.create(TileManaDetector::new, manaDetector).build(null), LibBlockNames.MANA_DETECTOR);
		register(r, TileEntityType.Builder.create(TileEnchanter::new, enchanter).build(null), LibBlockNames.ENCHANTER);
		register(r, TileEntityType.Builder.create(TileTurntable::new, turntable).build(null), LibBlockNames.TURNTABLE);
		register(r, TileEntityType.Builder.create(TileTinyPlanet::new, tinyPlanet).build(null), LibBlockNames.TINY_PLANET);
		register(r, TileEntityType.Builder.create(TileOpenCrate::new, openCrate).build(null), LibBlockNames.OPEN_CRATE);
		register(r, TileEntityType.Builder.create(TileCraftCrate::new, craftCrate).build(null), LibBlockNames.CRAFT_CRATE);
		register(r, TileEntityType.Builder.create(TileForestEye::new, forestEye).build(null), LibBlockNames.FOREST_EYE);
		register(r, TileEntityType.Builder.create(TilePlatform::new, abstrusePlatform, spectralPlatform, infrangiblePlatform).build(null), LibBlockNames.PLATFORM);
		register(r, TileEntityType.Builder.create(TileAlfPortal::new, alfPortal).build(null), LibBlockNames.ALF_PORTAL);
		register(r, TileEntityType.Builder.create(TileBifrost::new, bifrost).build(null), LibBlockNames.BIFROST);
		register(r, TileEntityType.Builder.create(TileFloatingFlower::new, Arrays.stream(DyeColor.values()).map(ModBlocks::getFloatingFlower).toArray(Block[]::new)).build(null), LibBlockNames.MINI_ISLAND);
		register(r, TileEntityType.Builder.create(TileTinyPotato::new, tinyPotato).build(null), LibBlockNames.TINY_POTATO);
		register(r, TileEntityType.Builder.create(TileSpawnerClaw::new, spawnerClaw).build(null), LibBlockNames.SPAWNER_CLAW);
		register(r, TileEntityType.Builder.create(TileEnderEye::new, enderEye).build(null), LibBlockNames.ENDER_EYE_BLOCK);
		register(r, TileEntityType.Builder.create(TileStarfield::new, starfield).build(null), LibBlockNames.STARFIELD);
		register(r, TileEntityType.Builder.create(TileRFGenerator::new, rfGenerator).build(null), LibBlockNames.FLUXFIELD);
		register(r, TileEntityType.Builder.create(TileBrewery::new, brewery).build(null), LibBlockNames.BREWERY);
		register(r, TileEntityType.Builder.create(TileTerraPlate::new, terraPlate).build(null), LibBlockNames.TERRA_PLATE);
		register(r, TileEntityType.Builder.create(TileRedStringContainer::new, redStringContainer).build(null), LibBlockNames.RED_STRING_CONTAINER);
		register(r, TileEntityType.Builder.create(TileRedStringDispenser::new, redStringDispenser).build(null), LibBlockNames.RED_STRING_DISPENSER);
		register(r, TileEntityType.Builder.create(TileRedStringFertilizer::new, redStringFertilizer).build(null), LibBlockNames.RED_STRING_FERTILIZER);
		register(r, TileEntityType.Builder.create(TileRedStringComparator::new, redStringComparator).build(null), LibBlockNames.RED_STRING_COMPARATOR);
		register(r, TileEntityType.Builder.create(TileRedStringRelay::new, redStringRelay).build(null), LibBlockNames.RED_STRING_RELAY);
		register(r, TileEntityType.Builder.create(TileManaFlame::new, manaFlame).build(null), LibBlockNames.MANA_FLAME);
		register(r, TileEntityType.Builder.create(TilePrism::new, prism).build(null), LibBlockNames.PRISM);
		register(r, TileEntityType.Builder.create(TileCorporeaIndex::new, corporeaIndex).build(null), LibBlockNames.CORPOREA_INDEX);
		register(r, TileEntityType.Builder.create(TileCorporeaFunnel::new, corporeaFunnel).build(null), LibBlockNames.CORPOREA_FUNNEL);
		register(r, TileEntityType.Builder.create(TilePump::new, pump).build(null), LibBlockNames.PUMP);
		register(r, TileEntityType.Builder.create(TileFakeAir::new, fakeAir).build(null), LibBlockNames.FAKE_AIR);
		register(r, TileEntityType.Builder.create(TileCorporeaInterceptor::new, corporeaInterceptor).build(null), LibBlockNames.CORPOREA_INTERCEPTOR);
		register(r, TileEntityType.Builder.create(TileCorporeaCrystalCube::new, corporeaCrystalCube).build(null), LibBlockNames.CORPOREA_CRYSTAL_CUBE);
		register(r, TileEntityType.Builder.create(TileIncensePlate::new, incensePlate).build(null), LibBlockNames.INCENSE_PLATE);
		register(r, TileEntityType.Builder.create(TileHourglass::new, hourglass).build(null), LibBlockNames.HOURGLASS);
		register(r, TileEntityType.Builder.create(TileSparkChanger::new, sparkChanger).build(null), LibBlockNames.SPARK_CHANGER);
		register(r, TileEntityType.Builder.create(TileCocoon::new, cocoon).build(null), LibBlockNames.COCOON);
		register(r, TileEntityType.Builder.create(TileLightRelay::new, lightRelayDefault, lightRelayDetector, lightRelayToggle, lightRelayFork).build(null), LibBlockNames.LIGHT_RELAY);
		register(r, TileEntityType.Builder.create(TileCacophonium::new, cacophonium).build(null), LibBlockNames.CACOPHONIUM);
		register(r, TileEntityType.Builder.create(TileBellows::new, bellows).build(null), LibBlockNames.BELLOWS);
		register(r, TileEntityType.Builder.create(TileCell::new, cellBlock).build(null), LibBlockNames.CELL_BLOCK);
		register(r, TileEntityType.Builder.create(TileRedStringInterceptor::new, redStringInterceptor).build(null), LibBlockNames.RED_STRING_INTERCEPTOR);
		register(r, TileEntityType.Builder.create(TileGaiaHead::new, gaiaHead, gaiaHeadWall).build(null), LibBlockNames.GAIA_HEAD);
		register(r, TileEntityType.Builder.create(TileCorporeaRetainer::new, corporeaRetainer).build(null), LibBlockNames.CORPOREA_RETAINER);
		register(r, TileEntityType.Builder.create(TileTeruTeruBozu::new, teruTeruBozu).build(null), LibBlockNames.TERU_TERU_BOZU);
		register(r, TileEntityType.Builder.create(TileAvatar::new, avatar).build(null), LibBlockNames.AVATAR);
		register(r, TileEntityType.Builder.create(TileAnimatedTorch::new, animatedTorch).build(null), LibBlockNames.ANIMATED_TORCH);
	}

	public static Block getFlower(DyeColor color) {
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
	
	public static Block getMushroom(DyeColor color) {
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

	public static Block getBuriedPetal(DyeColor color) {
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
	
	public static Block getShinyFlower(DyeColor color) {
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

	public static Block getFloatingFlower(DyeColor color) {
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
	
	public static Block getDoubleFlower(DyeColor color) {
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
	public static Block getPetalBlock(DyeColor color) {
		switch (color) {
			default:
			case WHITE: return petalBlockWhite;
			case ORANGE: return petalBlockOrange;
			case MAGENTA: return petalBlockMagenta;
			case LIGHT_BLUE: return petalBlockLightBlue;
			case YELLOW: return petalBlockYellow;
			case LIME: return petalBlockLime;
			case PINK: return petalBlockPink;
			case GRAY: return petalBlockGray;
			case LIGHT_GRAY: return petalBlockSilver;
			case CYAN: return petalBlockCyan;
			case PURPLE: return petalBlockPurple;
			case BLUE: return petalBlockBlue;
			case BROWN: return petalBlockBrown;
			case GREEN: return petalBlockGreen;
			case RED: return petalBlockRed;
			case BLACK: return petalBlockBlack;
		}
	}
}
