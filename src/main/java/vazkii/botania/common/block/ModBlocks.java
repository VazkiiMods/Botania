/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.client.render.tile.RenderTilePylon;
import vazkii.botania.client.render.tile.TEISR;
import vazkii.botania.common.block.corporea.*;
import vazkii.botania.common.block.decor.*;
import vazkii.botania.common.block.dispenser.BehaviourFelPumpkin;
import vazkii.botania.common.block.dispenser.BehaviourPoolMinecart;
import vazkii.botania.common.block.dispenser.BehaviourWand;
import vazkii.botania.common.block.dispenser.SeedBehaviours;
import vazkii.botania.common.block.mana.*;
import vazkii.botania.common.block.string.*;
import vazkii.botania.common.block.tile.*;
import vazkii.botania.common.block.tile.corporea.*;
import vazkii.botania.common.block.tile.mana.*;
import vazkii.botania.common.block.tile.string.*;
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

	public static void registerBlocks(RegistryEvent.Register<Block> evt) {
		IForgeRegistry<Block> r = evt.getRegistry();

		Block.Properties builder = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT);
		for (DyeColor color : DyeColor.values()) {
			register(r, color.getName() + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new BlockModFlower(color, builder));
		}

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).sound(SoundType.STONE);
		for (BlockAltar.Variant v : BlockAltar.Variant.values()) {
			r.register(new BlockAltar(v, builder)
					.setRegistryName(LibMisc.MOD_ID, LibBlockNames.APOTHECARY_PREFIX + v.name().toLowerCase(Locale.ROOT)));
		}

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		register(r, LibBlockNames.LIVING_ROCK, new BlockMod(builder));
		register(r, LibBlockNames.LIVING_ROCK_BRICK, new BlockMod(builder));
		register(r, LibBlockNames.LIVING_ROCK_BRICK_MOSSY, new BlockMod(builder));
		register(r, LibBlockNames.LIVING_ROCK_BRICK_CRACKED, new BlockMod(builder));
		register(r, LibBlockNames.LIVING_ROCK_BRICK_CHISELED, new BlockMod(builder));

		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD);
		// todo 1.13: livingwood should support leaves
		register(r, LibBlockNames.LIVING_WOOD, new BlockMod(builder));
		register(r, LibBlockNames.LIVING_WOOD_PLANKS, new BlockMod(builder));
		register(r, LibBlockNames.LIVING_WOOD_PLANKS_MOSSY, new BlockMod(builder));
		register(r, LibBlockNames.LIVING_WOOD_FRAMED, new BlockMod(builder));
		register(r, LibBlockNames.LIVING_WOOD_PATTERN_FRAMED, new BlockMod(builder));
		register(r, LibBlockNames.LIVING_WOOD_GLIMMERING, new BlockMod(builder.lightValue(12)));

		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD);
		register(r, LibBlockNames.SPREADER, new BlockSpreader(BlockSpreader.Variant.MANA, builder));
		register(r, LibBlockNames.SPREADER_REDSTONE, new BlockSpreader(BlockSpreader.Variant.REDSTONE, builder));
		register(r, LibBlockNames.SPREADER_ELVEN, new BlockSpreader(BlockSpreader.Variant.ELVEN, builder));
		register(r, LibBlockNames.SPREADER_GAIA, new BlockSpreader(BlockSpreader.Variant.GAIA, builder));

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		register(r, LibBlockNames.POOL, new BlockPool(BlockPool.Variant.DEFAULT, builder));
		register(r, LibBlockNames.POOL_CREATIVE, new BlockPool(BlockPool.Variant.CREATIVE, builder));
		register(r, LibBlockNames.POOL_DILUTED, new BlockPool(BlockPool.Variant.DILUTED, builder));
		register(r, LibBlockNames.POOL_FABULOUS, new BlockPool(BlockPool.Variant.FABULOUS, builder));

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		register(r, LibBlockNames.RUNE_ALTAR, new BlockRuneAltar(builder));

		builder = Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL).lightValue(7);
		register(r, LibBlockNames.PYLON, new BlockPylon(BlockPylon.Variant.MANA, builder));
		register(r, LibBlockNames.PYLON_NATURA, new BlockPylon(BlockPylon.Variant.NATURA, builder));
		register(r, LibBlockNames.PYLON_GAIA, new BlockPylon(BlockPylon.Variant.GAIA, builder));

		builder = Block.Properties.create(Material.GOURD).hardnessAndResistance(2, 10).sound(SoundType.METAL);
		register(r, LibBlockNames.PISTON_RELAY, new BlockPistonRelay(builder));

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		register(r, LibBlockNames.DISTRIBUTOR, new BlockDistributor(builder));

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 2000).sound(SoundType.STONE);
		register(r, LibBlockNames.MANA_VOID, new BlockManaVoid(builder));

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		register(r, LibBlockNames.MANA_DETECTOR, new BlockManaDetector(builder));

		register(r, LibBlockNames.ENCHANTER, new BlockEnchanter(Block.Properties.create(Material.ROCK).hardnessAndResistance(3, 5).lightValue(15).sound(SoundType.STONE)));
		register(r, LibBlockNames.TURNTABLE, new BlockTurntable(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
		register(r, LibBlockNames.TINY_PLANET, new BlockTinyPlanet(Block.Properties.create(Material.ROCK).hardnessAndResistance(20, 100).sound(SoundType.STONE)));
		register(r, LibBlockNames.ALCHEMY_CATALYST, new BlockAlchemyCatalyst(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)));

		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD);
		register(r, LibBlockNames.OPEN_CRATE, new BlockOpenCrate(builder));
		register(r, LibBlockNames.CRAFT_CRATE, new BlockCraftyCrate(builder));

		register(r, LibBlockNames.FOREST_EYE, new BlockForestEye(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL)));

		builder = Block.Properties.create(Material.IRON).hardnessAndResistance(3, 10).sound(SoundType.METAL);
		register(r, LibBlockNames.MANASTEEL_BLOCK, new BlockStorage(builder));
		register(r, LibBlockNames.TERRASTEEL_BLOCK, new BlockStorage(builder));
		register(r, LibBlockNames.ELEMENTIUM_BLOCK, new BlockStorage(builder));
		register(r, LibBlockNames.MANA_DIAMOND_BLOCK, new BlockStorage(builder));
		register(r, LibBlockNames.DRAGONSTONE_BLOCK, new BlockStorage(builder));

		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD);
		register(r, LibBlockNames.DRUM_WILD, new BlockForestDrum(BlockForestDrum.Variant.WILD, builder));
		register(r, LibBlockNames.DRUM_CANOPY, new BlockForestDrum(BlockForestDrum.Variant.CANOPY, builder));
		register(r, LibBlockNames.DRUM_GATHERING, new BlockForestDrum(BlockForestDrum.Variant.GATHERING, builder));

		builder = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT).lightValue(15);
		for (DyeColor color : DyeColor.values()) {
			register(r, color.getName() + LibBlockNames.SHINY_FLOWER_SUFFIX, new BlockShinyFlower(color, builder));
		}

		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2, 5).sound(SoundType.WOOD);
		register(r, LibBlockNames.PLATFORM_ABSTRUSE, new BlockPlatform(BlockPlatform.Variant.ABSTRUSE, builder));
		register(r, LibBlockNames.PLATFORM_SPECTRAL, new BlockPlatform(BlockPlatform.Variant.SPECTRAL, builder));
		register(r, LibBlockNames.PLATFORM_INFRANGIBLE, new BlockPlatform(BlockPlatform.Variant.INFRANGIBLE, Block.Properties.create(Material.WOOD).hardnessAndResistance(-1, Float.MAX_VALUE).sound(SoundType.WOOD)));
		register(r, LibBlockNames.ALF_PORTAL, new BlockAlfPortal(Block.Properties.create(Material.WOOD).hardnessAndResistance(10).sound(SoundType.WOOD)));

		builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD);
		// todo 1.13: dreamwood should support leaves?
		register(r, LibBlockNames.DREAM_WOOD, new BlockMod(builder));
		register(r, LibBlockNames.DREAM_WOOD_PLANKS, new BlockMod(builder));
		register(r, LibBlockNames.DREAM_WOOD_PLANKS_MOSSY, new BlockMod(builder));
		register(r, LibBlockNames.DREAM_WOOD_FRAMED, new BlockMod(builder));
		register(r, LibBlockNames.DREAM_WOOD_PATTERN_FRAMED, new BlockMod(builder));
		register(r, LibBlockNames.DREAM_WOOD_GLIMMERING, new BlockMod(builder.lightValue(12)));

		register(r, LibBlockNames.CONJURATION_CATALYST, new BlockConjurationCatalyst(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)));
		register(r, LibBlockNames.BIFROST, new BlockBifrost(Block.Properties.create(Material.GLASS).hardnessAndResistance(-1, 0.3F).lightValue(15).sound(SoundType.GLASS).notSolid()));
		register(r, LibBlockNames.SOLID_VINE, new BlockSolidVines(Block.Properties.create(Material.TALL_PLANTS).hardnessAndResistance(0.2F).sound(SoundType.PLANT).notSolid()));

		builder = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT).lightValue(4);
		for (DyeColor color : DyeColor.values()) {
			register(r, color.getName() + LibBlockNames.BURIED_PETALS_SUFFIX, new BlockBuriedPetals(color, builder));
		}

		builder = Block.Properties.create(Material.EARTH).hardnessAndResistance(0.5F).sound(SoundType.GROUND).lightValue(15);
		for (DyeColor color : DyeColor.values()) {
			register(r, color.getName() + LibBlockNames.FLOATING_FLOWER_SUFFIX, new BlockFloatingFlower(color, builder));
		}

		register(r, LibBlockNames.TINY_POTATO, new BlockTinyPotato(Block.Properties.create(Material.WOOL).hardnessAndResistance(0.25F)));
		register(r, LibBlockNames.SPAWNER_CLAW, new BlockSpawnerClaw(Block.Properties.create(Material.IRON).hardnessAndResistance(3)));

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 5).sound(SoundType.STONE);
		for (int i = 0; i < 16; i++) {
			register(r, LibBlockNames.AZULEJO_PREFIX + i, new BlockMod(builder));
		}

		register(r, LibBlockNames.ENDER_EYE_BLOCK, new BlockEnderEye(Block.Properties.create(Material.IRON).hardnessAndResistance(3, 10).sound(SoundType.METAL)));
		register(r, LibBlockNames.STARFIELD, new BlockStarfield(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 2000).sound(SoundType.METAL)));
		register(r, LibBlockNames.FLUXFIELD, new BlockRFGenerator(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)));
		register(r, LibBlockNames.ELF_GLASS, new BlockElfGlass(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).lightValue(15).notSolid()));
		register(r, LibBlockNames.BREWERY, new BlockBrewery(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)));
		register(r, LibBlockNames.MANA_GLASS, new BlockModGlass(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).lightValue(15).notSolid()));
		register(r, LibBlockNames.TERRA_PLATE, new BlockTerraPlate(Block.Properties.create(Material.IRON).hardnessAndResistance(3, 10).sound(SoundType.METAL)));

		builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE);
		register(r, LibBlockNames.RED_STRING_CONTAINER, new BlockRedStringContainer(builder));
		register(r, LibBlockNames.RED_STRING_DISPENSER, new BlockRedStringDispenser(builder));
		register(r, LibBlockNames.RED_STRING_FERTILIZER, new BlockRedStringFertilizer(builder));
		register(r, LibBlockNames.RED_STRING_COMPARATOR, new BlockRedStringComparator(builder));
		register(r, LibBlockNames.RED_STRING_RELAY, new BlockRedStringRelay(builder));
		register(r, LibBlockNames.RED_STRING_INTERCEPTOR, new BlockRedStringInterceptor(builder));
		register(r, LibBlockNames.MANA_FLAME, new BlockManaFlame(Block.Properties.create(Material.WOOL).sound(SoundType.CLOTH).lightValue(15).doesNotBlockMovement()));
		register(r, LibBlockNames.PRISM, new BlockPrism(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).lightValue(15).doesNotBlockMovement()));
		register(r, LibBlockNames.ENCHANTED_SOIL, new BlockEnchantedSoil(Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.6F).sound(SoundType.PLANT)));

		builder = Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.4F).sound(SoundType.PLANT);
		for (DyeColor color : DyeColor.values()) {
			register(r, color.getName() + LibBlockNames.PETAL_BLOCK_SUFFIX, new BlockPetalBlock(color, builder));
		}

		register(r, LibBlockNames.CORPOREA_INDEX, new BlockCorporeaIndex(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL).notSolid()));
		register(r, LibBlockNames.CORPOREA_FUNNEL, new BlockCorporeaFunnel(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL)));

		builder = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT).lightValue(3);
		for (DyeColor color : DyeColor.values()) {
			register(r, color.getName() + LibBlockNames.MUSHROOM_SUFFIX, new BlockModMushroom(color, builder));
		}

		register(r, LibBlockNames.PUMP, new BlockPump(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)));

		builder = Block.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT);
		for (DyeColor color : DyeColor.values()) {
			register(r, color.getName() + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BlockModDoubleFlower(color, builder));
		}

		register(r, LibBlockNames.FAKE_AIR, new BlockFakeAir(Block.Properties.create(Material.STRUCTURE_VOID).tickRandomly()));
		register(r, LibBlockNames.BLAZE_BLOCK, new BlockMod(Block.Properties.create(Material.IRON).hardnessAndResistance(3, 10).sound(SoundType.METAL).lightValue(15)));
		register(r, LibBlockNames.CORPOREA_INTERCEPTOR, new BlockCorporeaInterceptor(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL)));
		register(r, LibBlockNames.CORPOREA_CRYSTAL_CUBE, new BlockCorporeaCrystalCube(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL)));
		register(r, LibBlockNames.INCENSE_PLATE, new BlockIncensePlate(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
		register(r, LibBlockNames.HOURGLASS, new BlockHourglass(Block.Properties.create(Material.IRON).hardnessAndResistance(2).sound(SoundType.METAL)));
		register(r, LibBlockNames.GHOST_RAIL, new BlockGhostRail(Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.7F).sound(SoundType.METAL)));
		register(r, LibBlockNames.SPARK_CHANGER, new BlockSparkChanger(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)));
		register(r, LibBlockNames.ROOT, new BlockRoot(Block.Properties.create(Material.PLANTS).hardnessAndResistance(1.2F).sound(SoundType.WOOD)));
		register(r, LibBlockNames.FEL_PUMPKIN, new BlockFelPumpkin(Block.Properties.from(Blocks.CARVED_PUMPKIN)));
		register(r, LibBlockNames.COCOON, new BlockCocoon(Block.Properties.create(Material.WOOL).hardnessAndResistance(3, 60).sound(SoundType.CLOTH)));

		builder = Block.Properties.create(Material.GLASS).doesNotBlockMovement();
		register(r, LibBlockNames.LIGHT_RELAY, new BlockLightRelay(LuminizerVariant.DEFAULT, builder));
		register(r, "detector" + LibBlockNames.LIGHT_RELAY_SUFFIX, new BlockLightRelay(LuminizerVariant.DETECTOR, builder));
		register(r, "fork" + LibBlockNames.LIGHT_RELAY_SUFFIX, new BlockLightRelay(LuminizerVariant.FORK, builder));
		register(r, "toggle" + LibBlockNames.LIGHT_RELAY_SUFFIX, new BlockLightRelay(LuminizerVariant.TOGGLE, builder));

		register(r, LibBlockNames.LIGHT_LAUNCHER, new BlockLightLauncher(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
		register(r, LibBlockNames.MANA_BOMB, new BlockManaBomb(Block.Properties.create(Material.WOOD).hardnessAndResistance(12).sound(SoundType.WOOD)));
		register(r, LibBlockNames.CACOPHONIUM, new BlockCacophonium(Block.Properties.create(Material.WOOD).hardnessAndResistance(0.8F)));
		register(r, LibBlockNames.BELLOWS, new BlockBellows(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
		register(r, LibBlockNames.BIFROST_PERM, new BlockBifrostPerm(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).lightValue(15).sound(SoundType.GLASS).notSolid()));
		register(r, LibBlockNames.CELL_BLOCK, new BlockCell(Block.Properties.create(Material.GOURD).sound(SoundType.CLOTH)));
		register(r, LibBlockNames.GAIA_WALL_HEAD, new BlockGaiaHeadWall(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1)));
		register(r, LibBlockNames.GAIA_HEAD, new BlockGaiaHead(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1)));
		register(r, LibBlockNames.CORPOREA_RETAINER, new BlockCorporeaRetainer(Block.Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL)));
		register(r, LibBlockNames.TERU_TERU_BOZU, new BlockTeruTeruBozu(Block.Properties.create(Material.WOOL)));
		register(r, LibBlockNames.SHIMMERROCK, new BlockMod(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 10).sound(SoundType.STONE)));
		register(r, LibBlockNames.SHIMMERWOOD_PLANKS, new BlockMod(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
		register(r, LibBlockNames.AVATAR, new BlockAvatar(Block.Properties.create(Material.WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));

		builder = Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.6F).tickRandomly().sound(SoundType.PLANT);
		for (BlockAltGrass.Variant v : BlockAltGrass.Variant.values()) {
			register(r, v.name().toLowerCase(Locale.ROOT) + LibBlockNames.ALT_GRASS_SUFFIX, new BlockAltGrass(v, builder));
		}

		register(r, LibBlockNames.ANIMATED_TORCH, new BlockAnimatedTorch(Block.Properties.create(Material.MISCELLANEOUS).lightValue(7).notSolid()));
	}

	public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		Item.Properties props = ModItems.defaultBuilder();

		register(r, Registry.BLOCK.getKey(whiteFlower), new BlockItem(whiteFlower, props));
		register(r, Registry.BLOCK.getKey(orangeFlower), new BlockItem(orangeFlower, props));
		register(r, Registry.BLOCK.getKey(magentaFlower), new BlockItem(magentaFlower, props));
		register(r, Registry.BLOCK.getKey(lightBlueFlower), new BlockItem(lightBlueFlower, props));
		register(r, Registry.BLOCK.getKey(yellowFlower), new BlockItem(yellowFlower, props));
		register(r, Registry.BLOCK.getKey(limeFlower), new BlockItem(limeFlower, props));
		register(r, Registry.BLOCK.getKey(pinkFlower), new BlockItem(pinkFlower, props));
		register(r, Registry.BLOCK.getKey(grayFlower), new BlockItem(grayFlower, props));
		register(r, Registry.BLOCK.getKey(lightGrayFlower), new BlockItem(lightGrayFlower, props));
		register(r, Registry.BLOCK.getKey(cyanFlower), new BlockItem(cyanFlower, props));
		register(r, Registry.BLOCK.getKey(purpleFlower), new BlockItem(purpleFlower, props));
		register(r, Registry.BLOCK.getKey(blueFlower), new BlockItem(blueFlower, props));
		register(r, Registry.BLOCK.getKey(brownFlower), new BlockItem(brownFlower, props));
		register(r, Registry.BLOCK.getKey(greenFlower), new BlockItem(greenFlower, props));
		register(r, Registry.BLOCK.getKey(redFlower), new BlockItem(redFlower, props));
		register(r, Registry.BLOCK.getKey(blackFlower), new BlockItem(blackFlower, props));
		register(r, Registry.BLOCK.getKey(defaultAltar), new BlockItem(defaultAltar, props));
		register(r, Registry.BLOCK.getKey(forestAltar), new BlockItem(forestAltar, props));
		register(r, Registry.BLOCK.getKey(plainsAltar), new BlockItem(plainsAltar, props));
		register(r, Registry.BLOCK.getKey(mountainAltar), new BlockItem(mountainAltar, props));
		register(r, Registry.BLOCK.getKey(fungalAltar), new BlockItem(fungalAltar, props));
		register(r, Registry.BLOCK.getKey(swampAltar), new BlockItem(swampAltar, props));
		register(r, Registry.BLOCK.getKey(desertAltar), new BlockItem(desertAltar, props));
		register(r, Registry.BLOCK.getKey(taigaAltar), new BlockItem(taigaAltar, props));
		register(r, Registry.BLOCK.getKey(mesaAltar), new BlockItem(mesaAltar, props));
		register(r, Registry.BLOCK.getKey(mossyAltar), new BlockItem(mossyAltar, props));
		register(r, Registry.BLOCK.getKey(livingrock), new BlockItem(livingrock, props));
		register(r, Registry.BLOCK.getKey(livingrockBrick), new BlockItem(livingrockBrick, props));
		register(r, Registry.BLOCK.getKey(livingrockBrickChiseled), new BlockItem(livingrockBrickChiseled, props));
		register(r, Registry.BLOCK.getKey(livingrockBrickCracked), new BlockItem(livingrockBrickCracked, props));
		register(r, Registry.BLOCK.getKey(livingrockBrickMossy), new BlockItem(livingrockBrickMossy, props));
		register(r, Registry.BLOCK.getKey(livingwood), new BlockItem(livingwood, props));
		register(r, Registry.BLOCK.getKey(livingwoodPlanks), new BlockItem(livingwoodPlanks, props));
		register(r, Registry.BLOCK.getKey(livingwoodPlanksMossy), new BlockItem(livingwoodPlanksMossy, props));
		register(r, Registry.BLOCK.getKey(livingwoodFramed), new BlockItem(livingwoodFramed, props));
		register(r, Registry.BLOCK.getKey(livingwoodPatternFramed), new BlockItem(livingwoodPatternFramed, props));
		register(r, Registry.BLOCK.getKey(livingwoodGlimmering), new BlockItem(livingwoodGlimmering, props));
		register(r, Registry.BLOCK.getKey(manaSpreader), new BlockItem(manaSpreader, props));
		register(r, Registry.BLOCK.getKey(redstoneSpreader), new BlockItem(redstoneSpreader, props));
		register(r, Registry.BLOCK.getKey(elvenSpreader), new BlockItem(elvenSpreader, props));
		register(r, Registry.BLOCK.getKey(gaiaSpreader), new BlockItem(gaiaSpreader, props));
		register(r, Registry.BLOCK.getKey(manaPool), new ItemBlockPool(manaPool, props));
		register(r, Registry.BLOCK.getKey(creativePool), new ItemBlockPool(creativePool, props));
		register(r, Registry.BLOCK.getKey(dilutedPool), new ItemBlockPool(dilutedPool, props));
		register(r, Registry.BLOCK.getKey(fabulousPool), new ItemBlockPool(fabulousPool, props));
		register(r, Registry.BLOCK.getKey(runeAltar), new BlockItem(runeAltar, props));
		register(r, Registry.BLOCK.getKey(pistonRelay), new BlockItem(pistonRelay, props));
		register(r, Registry.BLOCK.getKey(distributor), new BlockItem(distributor, props));
		register(r, Registry.BLOCK.getKey(manaVoid), new BlockItem(manaVoid, props));
		register(r, Registry.BLOCK.getKey(manaDetector), new BlockItem(manaDetector, props));
		register(r, Registry.BLOCK.getKey(enchanter), new BlockItem(enchanter, props));
		register(r, Registry.BLOCK.getKey(turntable), new BlockItem(turntable, props));
		register(r, Registry.BLOCK.getKey(tinyPlanet), new BlockItem(tinyPlanet, props));
		register(r, Registry.BLOCK.getKey(alchemyCatalyst), new BlockItem(alchemyCatalyst, props));
		register(r, Registry.BLOCK.getKey(openCrate), new BlockItem(openCrate, props));
		register(r, Registry.BLOCK.getKey(craftCrate), new BlockItem(craftCrate, props));
		register(r, Registry.BLOCK.getKey(forestEye), new BlockItem(forestEye, props));
		register(r, Registry.BLOCK.getKey(manasteelBlock), new BlockItem(manasteelBlock, props));
		register(r, Registry.BLOCK.getKey(terrasteelBlock), new BlockItem(terrasteelBlock, props));
		register(r, Registry.BLOCK.getKey(elementiumBlock), new ItemBlockElven(elementiumBlock, props));
		register(r, Registry.BLOCK.getKey(manaDiamondBlock), new BlockItem(manaDiamondBlock, props));
		register(r, Registry.BLOCK.getKey(dragonstoneBlock), new BlockItem(dragonstoneBlock, props));
		register(r, Registry.BLOCK.getKey(wildDrum), new BlockItem(wildDrum, props));
		register(r, Registry.BLOCK.getKey(gatheringDrum), new BlockItem(gatheringDrum, props));
		register(r, Registry.BLOCK.getKey(canopyDrum), new BlockItem(canopyDrum, props));
		register(r, Registry.BLOCK.getKey(whiteShinyFlower), new BlockItem(whiteShinyFlower, props));
		register(r, Registry.BLOCK.getKey(orangeShinyFlower), new BlockItem(orangeShinyFlower, props));
		register(r, Registry.BLOCK.getKey(magentaShinyFlower), new BlockItem(magentaShinyFlower, props));
		register(r, Registry.BLOCK.getKey(lightBlueShinyFlower), new BlockItem(lightBlueShinyFlower, props));
		register(r, Registry.BLOCK.getKey(yellowShinyFlower), new BlockItem(yellowShinyFlower, props));
		register(r, Registry.BLOCK.getKey(limeShinyFlower), new BlockItem(limeShinyFlower, props));
		register(r, Registry.BLOCK.getKey(pinkShinyFlower), new BlockItem(pinkShinyFlower, props));
		register(r, Registry.BLOCK.getKey(grayShinyFlower), new BlockItem(grayShinyFlower, props));
		register(r, Registry.BLOCK.getKey(lightGrayShinyFlower), new BlockItem(lightGrayShinyFlower, props));
		register(r, Registry.BLOCK.getKey(cyanShinyFlower), new BlockItem(cyanShinyFlower, props));
		register(r, Registry.BLOCK.getKey(purpleShinyFlower), new BlockItem(purpleShinyFlower, props));
		register(r, Registry.BLOCK.getKey(blueShinyFlower), new BlockItem(blueShinyFlower, props));
		register(r, Registry.BLOCK.getKey(brownShinyFlower), new BlockItem(brownShinyFlower, props));
		register(r, Registry.BLOCK.getKey(greenShinyFlower), new BlockItem(greenShinyFlower, props));
		register(r, Registry.BLOCK.getKey(redShinyFlower), new BlockItem(redShinyFlower, props));
		register(r, Registry.BLOCK.getKey(blackShinyFlower), new BlockItem(blackShinyFlower, props));
		register(r, Registry.BLOCK.getKey(abstrusePlatform), new BlockItem(abstrusePlatform, props));
		register(r, Registry.BLOCK.getKey(spectralPlatform), new BlockItem(spectralPlatform, props));
		register(r, Registry.BLOCK.getKey(infrangiblePlatform), new BlockItem(infrangiblePlatform, props));
		register(r, Registry.BLOCK.getKey(alfPortal), new BlockItem(alfPortal, props));
		register(r, Registry.BLOCK.getKey(dreamwood), new ItemBlockDreamwood(dreamwood, props));
		register(r, Registry.BLOCK.getKey(dreamwoodPlanks), new ItemBlockDreamwood(dreamwoodPlanks, props));
		register(r, Registry.BLOCK.getKey(dreamwoodPlanksMossy), new ItemBlockDreamwood(dreamwoodPlanksMossy, props));
		register(r, Registry.BLOCK.getKey(dreamwoodFramed), new ItemBlockDreamwood(dreamwoodFramed, props));
		register(r, Registry.BLOCK.getKey(dreamwoodPatternFramed), new ItemBlockDreamwood(dreamwoodPatternFramed, props));
		register(r, Registry.BLOCK.getKey(dreamwoodGlimmering), new ItemBlockDreamwood(dreamwoodGlimmering, props));
		register(r, Registry.BLOCK.getKey(conjurationCatalyst), new BlockItem(conjurationCatalyst, props));
		register(r, Registry.BLOCK.getKey(bifrost), new BlockItem(bifrost, props));
		register(r, Registry.BLOCK.getKey(whiteFloatingFlower), new BlockItem(whiteFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(orangeFloatingFlower), new BlockItem(orangeFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(magentaFloatingFlower), new BlockItem(magentaFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(lightBlueFloatingFlower), new BlockItem(lightBlueFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(yellowFloatingFlower), new BlockItem(yellowFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(limeFloatingFlower), new BlockItem(limeFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(pinkFloatingFlower), new BlockItem(pinkFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(grayFloatingFlower), new BlockItem(grayFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(lightGrayFloatingFlower), new BlockItem(lightGrayFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(cyanFloatingFlower), new BlockItem(cyanFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(purpleFloatingFlower), new BlockItem(purpleFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(blueFloatingFlower), new BlockItem(blueFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(brownFloatingFlower), new BlockItem(brownFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(greenFloatingFlower), new BlockItem(greenFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(redFloatingFlower), new BlockItem(redFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(blackFloatingFlower), new BlockItem(blackFloatingFlower, props));
		register(r, Registry.BLOCK.getKey(tinyPotato), new ItemBlockTinyPotato(tinyPotato, props));
		register(r, Registry.BLOCK.getKey(spawnerClaw), new BlockItem(spawnerClaw, props));
		register(r, Registry.BLOCK.getKey(azulejo0), new BlockItem(azulejo0, props));
		register(r, Registry.BLOCK.getKey(azulejo1), new BlockItem(azulejo1, props));
		register(r, Registry.BLOCK.getKey(azulejo2), new BlockItem(azulejo2, props));
		register(r, Registry.BLOCK.getKey(azulejo3), new BlockItem(azulejo3, props));
		register(r, Registry.BLOCK.getKey(azulejo4), new BlockItem(azulejo4, props));
		register(r, Registry.BLOCK.getKey(azulejo5), new BlockItem(azulejo5, props));
		register(r, Registry.BLOCK.getKey(azulejo6), new BlockItem(azulejo6, props));
		register(r, Registry.BLOCK.getKey(azulejo7), new BlockItem(azulejo7, props));
		register(r, Registry.BLOCK.getKey(azulejo8), new BlockItem(azulejo8, props));
		register(r, Registry.BLOCK.getKey(azulejo9), new BlockItem(azulejo9, props));
		register(r, Registry.BLOCK.getKey(azulejo10), new BlockItem(azulejo10, props));
		register(r, Registry.BLOCK.getKey(azulejo11), new BlockItem(azulejo11, props));
		register(r, Registry.BLOCK.getKey(azulejo12), new BlockItem(azulejo12, props));
		register(r, Registry.BLOCK.getKey(azulejo13), new BlockItem(azulejo13, props));
		register(r, Registry.BLOCK.getKey(azulejo14), new BlockItem(azulejo14, props));
		register(r, Registry.BLOCK.getKey(azulejo15), new BlockItem(azulejo15, props));
		register(r, Registry.BLOCK.getKey(enderEye), new BlockItem(enderEye, props));
		register(r, Registry.BLOCK.getKey(starfield), new BlockItem(starfield, props));
		register(r, Registry.BLOCK.getKey(rfGenerator), new BlockItem(rfGenerator, props));
		register(r, Registry.BLOCK.getKey(elfGlass), new ItemBlockElven(elfGlass, props));
		register(r, Registry.BLOCK.getKey(manaGlass), new BlockItem(manaGlass, props));
		register(r, Registry.BLOCK.getKey(terraPlate), new BlockItem(terraPlate, props));
		register(r, Registry.BLOCK.getKey(redStringContainer), new BlockItem(redStringContainer, props));
		register(r, Registry.BLOCK.getKey(redStringDispenser), new BlockItem(redStringDispenser, props));
		register(r, Registry.BLOCK.getKey(redStringFertilizer), new BlockItem(redStringFertilizer, props));
		register(r, Registry.BLOCK.getKey(redStringComparator), new BlockItem(redStringComparator, props));
		register(r, Registry.BLOCK.getKey(redStringRelay), new BlockItem(redStringRelay, props));
		register(r, Registry.BLOCK.getKey(prism), new BlockItem(prism, props));
		register(r, Registry.BLOCK.getKey(enchantedSoil), new BlockItem(enchantedSoil, props));
		register(r, Registry.BLOCK.getKey(petalBlockWhite), new BlockItem(petalBlockWhite, props));
		register(r, Registry.BLOCK.getKey(petalBlockOrange), new BlockItem(petalBlockOrange, props));
		register(r, Registry.BLOCK.getKey(petalBlockMagenta), new BlockItem(petalBlockMagenta, props));
		register(r, Registry.BLOCK.getKey(petalBlockLightBlue), new BlockItem(petalBlockLightBlue, props));
		register(r, Registry.BLOCK.getKey(petalBlockYellow), new BlockItem(petalBlockYellow, props));
		register(r, Registry.BLOCK.getKey(petalBlockLime), new BlockItem(petalBlockLime, props));
		register(r, Registry.BLOCK.getKey(petalBlockPink), new BlockItem(petalBlockPink, props));
		register(r, Registry.BLOCK.getKey(petalBlockGray), new BlockItem(petalBlockGray, props));
		register(r, Registry.BLOCK.getKey(petalBlockSilver), new BlockItem(petalBlockSilver, props));
		register(r, Registry.BLOCK.getKey(petalBlockCyan), new BlockItem(petalBlockCyan, props));
		register(r, Registry.BLOCK.getKey(petalBlockPurple), new BlockItem(petalBlockPurple, props));
		register(r, Registry.BLOCK.getKey(petalBlockBlue), new BlockItem(petalBlockBlue, props));
		register(r, Registry.BLOCK.getKey(petalBlockBrown), new BlockItem(petalBlockBrown, props));
		register(r, Registry.BLOCK.getKey(petalBlockGreen), new BlockItem(petalBlockGreen, props));
		register(r, Registry.BLOCK.getKey(petalBlockRed), new BlockItem(petalBlockRed, props));
		register(r, Registry.BLOCK.getKey(petalBlockBlack), new BlockItem(petalBlockBlack, props));
		register(r, Registry.BLOCK.getKey(corporeaFunnel), new BlockItem(corporeaFunnel, props));
		register(r, Registry.BLOCK.getKey(whiteMushroom), new BlockItem(whiteMushroom, props));
		register(r, Registry.BLOCK.getKey(orangeMushroom), new BlockItem(orangeMushroom, props));
		register(r, Registry.BLOCK.getKey(magentaMushroom), new BlockItem(magentaMushroom, props));
		register(r, Registry.BLOCK.getKey(lightBlueMushroom), new BlockItem(lightBlueMushroom, props));
		register(r, Registry.BLOCK.getKey(yellowMushroom), new BlockItem(yellowMushroom, props));
		register(r, Registry.BLOCK.getKey(limeMushroom), new BlockItem(limeMushroom, props));
		register(r, Registry.BLOCK.getKey(pinkMushroom), new BlockItem(pinkMushroom, props));
		register(r, Registry.BLOCK.getKey(grayMushroom), new BlockItem(grayMushroom, props));
		register(r, Registry.BLOCK.getKey(lightGrayMushroom), new BlockItem(lightGrayMushroom, props));
		register(r, Registry.BLOCK.getKey(cyanMushroom), new BlockItem(cyanMushroom, props));
		register(r, Registry.BLOCK.getKey(purpleMushroom), new BlockItem(purpleMushroom, props));
		register(r, Registry.BLOCK.getKey(blueMushroom), new BlockItem(blueMushroom, props));
		register(r, Registry.BLOCK.getKey(brownMushroom), new BlockItem(brownMushroom, props));
		register(r, Registry.BLOCK.getKey(greenMushroom), new BlockItem(greenMushroom, props));
		register(r, Registry.BLOCK.getKey(redMushroom), new BlockItem(redMushroom, props));
		register(r, Registry.BLOCK.getKey(blackMushroom), new BlockItem(blackMushroom, props));
		register(r, Registry.BLOCK.getKey(pump), new BlockItem(pump, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerWhite), new BlockItem(doubleFlowerWhite, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerOrange), new BlockItem(doubleFlowerOrange, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerMagenta), new BlockItem(doubleFlowerMagenta, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerLightBlue), new BlockItem(doubleFlowerLightBlue, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerYellow), new BlockItem(doubleFlowerYellow, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerLime), new BlockItem(doubleFlowerLime, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerPink), new BlockItem(doubleFlowerPink, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerGray), new BlockItem(doubleFlowerGray, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerLightGray), new BlockItem(doubleFlowerLightGray, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerCyan), new BlockItem(doubleFlowerCyan, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerPurple), new BlockItem(doubleFlowerPurple, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerBlue), new BlockItem(doubleFlowerBlue, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerBrown), new BlockItem(doubleFlowerBrown, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerGreen), new BlockItem(doubleFlowerGreen, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerRed), new BlockItem(doubleFlowerRed, props));
		register(r, Registry.BLOCK.getKey(doubleFlowerBlack), new BlockItem(doubleFlowerBlack, props));
		register(r, Registry.BLOCK.getKey(blazeBlock), new ItemBlockBlaze(blazeBlock, props));
		register(r, Registry.BLOCK.getKey(corporeaInterceptor), new BlockItem(corporeaInterceptor, props));
		register(r, Registry.BLOCK.getKey(incensePlate), new BlockItem(incensePlate, props));
		register(r, Registry.BLOCK.getKey(ghostRail), new BlockItem(ghostRail, props));
		register(r, Registry.BLOCK.getKey(sparkChanger), new BlockItem(sparkChanger, props));
		register(r, Registry.BLOCK.getKey(root), new BlockItem(root, props));
		register(r, Registry.BLOCK.getKey(felPumpkin), new BlockItem(felPumpkin, props));
		register(r, Registry.BLOCK.getKey(cocoon), new BlockItem(cocoon, props));
		register(r, Registry.BLOCK.getKey(lightRelayDefault), new BlockItem(lightRelayDefault, props));
		register(r, Registry.BLOCK.getKey(lightRelayDetector), new BlockItem(lightRelayDetector, props));
		register(r, Registry.BLOCK.getKey(lightRelayFork), new BlockItem(lightRelayFork, props));
		register(r, Registry.BLOCK.getKey(lightRelayToggle), new BlockItem(lightRelayToggle, props));
		register(r, Registry.BLOCK.getKey(lightLauncher), new BlockItem(lightLauncher, props));
		register(r, Registry.BLOCK.getKey(manaBomb), new BlockItem(manaBomb, props));
		register(r, Registry.BLOCK.getKey(cacophonium), new BlockItem(cacophonium, props));
		register(r, Registry.BLOCK.getKey(bifrostPerm), new BlockItem(bifrostPerm, props));
		register(r, Registry.BLOCK.getKey(cellBlock), new BlockItem(cellBlock, props));
		register(r, Registry.BLOCK.getKey(redStringInterceptor), new BlockItem(redStringInterceptor, props));
		register(r, Registry.BLOCK.getKey(corporeaRetainer), new BlockItem(corporeaRetainer, props));
		register(r, Registry.BLOCK.getKey(shimmerrock), new BlockItem(shimmerrock, props));
		register(r, Registry.BLOCK.getKey(shimmerwoodPlanks), new BlockItem(shimmerwoodPlanks, props));
		register(r, Registry.BLOCK.getKey(dryGrass), new BlockItem(dryGrass, props));
		register(r, Registry.BLOCK.getKey(goldenGrass), new BlockItem(goldenGrass, props));
		register(r, Registry.BLOCK.getKey(vividGrass), new BlockItem(vividGrass, props));
		register(r, Registry.BLOCK.getKey(scorchedGrass), new BlockItem(scorchedGrass, props));
		register(r, Registry.BLOCK.getKey(infusedGrass), new BlockItem(infusedGrass, props));
		register(r, Registry.BLOCK.getKey(mutatedGrass), new BlockItem(mutatedGrass, props));
		register(r, Registry.BLOCK.getKey(animatedTorch), new BlockItem(animatedTorch, props));
		register(r, Registry.BLOCK.getKey(corporeaCrystalCube), new BlockItem(corporeaCrystalCube, props));

		DistExecutor.runForDist(() -> () -> registerWithTEISRS(r), () -> () -> registerWithoutTEISRS(r));
	}

	// Yay side-safety -.-

	@OnlyIn(Dist.DEDICATED_SERVER)
	private static Void registerWithoutTEISRS(IForgeRegistry<Item> r) {
		Item.Properties props = ModItems.defaultBuilder();
		register(r, Registry.BLOCK.getKey(manaPylon), new BlockItem(manaPylon, props));
		register(r, Registry.BLOCK.getKey(naturaPylon), new BlockItem(naturaPylon, props));
		register(r, Registry.BLOCK.getKey(gaiaPylon), new BlockItem(gaiaPylon, props));

		register(r, Registry.BLOCK.getKey(teruTeruBozu), new BlockItem(teruTeruBozu, props));
		register(r, Registry.BLOCK.getKey(avatar), new BlockItem(avatar, props));
		register(r, Registry.BLOCK.getKey(bellows), new BlockItem(bellows, props));
		register(r, Registry.BLOCK.getKey(brewery), new BlockItem(brewery, props));
		register(r, Registry.BLOCK.getKey(corporeaIndex), new BlockItem(corporeaIndex, props));
		register(r, Registry.BLOCK.getKey(hourglass), new BlockItem(hourglass, props));
		Item head = new ItemGaiaHead(gaiaHead, gaiaHeadWall, ModItems.defaultBuilder().rarity(Rarity.UNCOMMON));
		register(r, Registry.BLOCK.getKey(gaiaHead), head);
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	private static Void registerWithTEISRS(IForgeRegistry<Item> r) {
		Item.Properties pylonProps = ModItems.defaultBuilder().setISTER(() -> () -> new RenderTilePylon.TEISR());
		register(r, Registry.BLOCK.getKey(manaPylon), new BlockItem(manaPylon, pylonProps));
		register(r, Registry.BLOCK.getKey(naturaPylon), new BlockItem(naturaPylon, pylonProps));
		register(r, Registry.BLOCK.getKey(gaiaPylon), new BlockItem(gaiaPylon, pylonProps));

		register(r, Registry.BLOCK.getKey(teruTeruBozu), new BlockItem(teruTeruBozu, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(teruTeruBozu))));
		register(r, Registry.BLOCK.getKey(avatar), new BlockItem(avatar, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(avatar))));
		register(r, Registry.BLOCK.getKey(bellows), new BlockItem(bellows, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(bellows))));
		register(r, Registry.BLOCK.getKey(brewery), new BlockItem(brewery, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(brewery))));
		register(r, Registry.BLOCK.getKey(corporeaIndex), new BlockItem(corporeaIndex, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(corporeaIndex))));
		register(r, Registry.BLOCK.getKey(hourglass), new BlockItem(hourglass, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(hourglass))));
		Item head = new ItemGaiaHead(gaiaHead, gaiaHeadWall, ModItems.defaultBuilder().rarity(Rarity.UNCOMMON).setISTER(() -> () -> new TEISR(gaiaHead)));
		register(r, Registry.BLOCK.getKey(gaiaHead), head);
		return null;
	}

	public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, ResourceLocation name, IForgeRegistryEntry<V> thing) {
		reg.register(thing.setRegistryName(name));
	}

	public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, String name, IForgeRegistryEntry<V> thing) {
		register(reg, new ResourceLocation(LibMisc.MOD_ID, name), thing);
	}

	public static void addDispenserBehaviours() {
		DispenserBlock.registerDispenseBehavior(ModItems.twigWand, new BehaviourWand());
		DispenserBlock.registerDispenseBehavior(ModItems.poolMinecart, new BehaviourPoolMinecart());
		DispenserBlock.registerDispenseBehavior(ModBlocks.felPumpkin, new BehaviourFelPumpkin());

		SeedBehaviours.init();
	}

	public static Block getFlower(DyeColor color) {
		switch (color) {
		default:
		case WHITE:
			return whiteFlower;
		case ORANGE:
			return orangeFlower;
		case MAGENTA:
			return magentaFlower;
		case LIGHT_BLUE:
			return lightBlueFlower;
		case YELLOW:
			return yellowFlower;
		case LIME:
			return limeFlower;
		case PINK:
			return pinkFlower;
		case GRAY:
			return grayFlower;
		case LIGHT_GRAY:
			return lightGrayFlower;
		case CYAN:
			return cyanFlower;
		case PURPLE:
			return purpleFlower;
		case BLUE:
			return blueFlower;
		case BROWN:
			return brownFlower;
		case GREEN:
			return greenFlower;
		case RED:
			return redFlower;
		case BLACK:
			return blackFlower;
		}
	}

	public static Block getMushroom(DyeColor color) {
		switch (color) {
		default:
		case WHITE:
			return whiteMushroom;
		case ORANGE:
			return orangeMushroom;
		case MAGENTA:
			return magentaMushroom;
		case LIGHT_BLUE:
			return lightBlueMushroom;
		case YELLOW:
			return yellowMushroom;
		case LIME:
			return limeMushroom;
		case PINK:
			return pinkMushroom;
		case GRAY:
			return grayMushroom;
		case LIGHT_GRAY:
			return lightGrayMushroom;
		case CYAN:
			return cyanMushroom;
		case PURPLE:
			return purpleMushroom;
		case BLUE:
			return blueMushroom;
		case BROWN:
			return brownMushroom;
		case GREEN:
			return greenMushroom;
		case RED:
			return redMushroom;
		case BLACK:
			return blackMushroom;
		}
	}

	public static Block getBuriedPetal(DyeColor color) {
		switch (color) {
		default:
		case WHITE:
			return whiteBuriedPetals;
		case ORANGE:
			return orangeBuriedPetals;
		case MAGENTA:
			return magentaBuriedPetals;
		case LIGHT_BLUE:
			return lightBlueBuriedPetals;
		case YELLOW:
			return yellowBuriedPetals;
		case LIME:
			return limeBuriedPetals;
		case PINK:
			return pinkBuriedPetals;
		case GRAY:
			return grayBuriedPetals;
		case LIGHT_GRAY:
			return lightGrayBuriedPetals;
		case CYAN:
			return cyanBuriedPetals;
		case PURPLE:
			return purpleBuriedPetals;
		case BLUE:
			return blueBuriedPetals;
		case BROWN:
			return brownBuriedPetals;
		case GREEN:
			return greenBuriedPetals;
		case RED:
			return redBuriedPetals;
		case BLACK:
			return blackBuriedPetals;
		}
	}

	public static Block getShinyFlower(DyeColor color) {
		switch (color) {
		default:
		case WHITE:
			return whiteShinyFlower;
		case ORANGE:
			return orangeShinyFlower;
		case MAGENTA:
			return magentaShinyFlower;
		case LIGHT_BLUE:
			return lightBlueShinyFlower;
		case YELLOW:
			return yellowShinyFlower;
		case LIME:
			return limeShinyFlower;
		case PINK:
			return pinkShinyFlower;
		case GRAY:
			return grayShinyFlower;
		case LIGHT_GRAY:
			return lightGrayShinyFlower;
		case CYAN:
			return cyanShinyFlower;
		case PURPLE:
			return purpleShinyFlower;
		case BLUE:
			return blueShinyFlower;
		case BROWN:
			return brownShinyFlower;
		case GREEN:
			return greenShinyFlower;
		case RED:
			return redShinyFlower;
		case BLACK:
			return blackShinyFlower;
		}
	}

	public static Block getFloatingFlower(DyeColor color) {
		switch (color) {
		default:
		case WHITE:
			return whiteFloatingFlower;
		case ORANGE:
			return orangeFloatingFlower;
		case MAGENTA:
			return magentaFloatingFlower;
		case LIGHT_BLUE:
			return lightBlueFloatingFlower;
		case YELLOW:
			return yellowFloatingFlower;
		case LIME:
			return limeFloatingFlower;
		case PINK:
			return pinkFloatingFlower;
		case GRAY:
			return grayFloatingFlower;
		case LIGHT_GRAY:
			return lightGrayFloatingFlower;
		case CYAN:
			return cyanFloatingFlower;
		case PURPLE:
			return purpleFloatingFlower;
		case BLUE:
			return blueFloatingFlower;
		case BROWN:
			return brownFloatingFlower;
		case GREEN:
			return greenFloatingFlower;
		case RED:
			return redFloatingFlower;
		case BLACK:
			return blackFloatingFlower;
		}
	}

	public static Block getDoubleFlower(DyeColor color) {
		switch (color) {
		default:
		case WHITE:
			return doubleFlowerWhite;
		case ORANGE:
			return doubleFlowerOrange;
		case MAGENTA:
			return doubleFlowerMagenta;
		case LIGHT_BLUE:
			return doubleFlowerLightBlue;
		case YELLOW:
			return doubleFlowerYellow;
		case LIME:
			return doubleFlowerLime;
		case PINK:
			return doubleFlowerPink;
		case GRAY:
			return doubleFlowerGray;
		case LIGHT_GRAY:
			return doubleFlowerLightGray;
		case CYAN:
			return doubleFlowerCyan;
		case PURPLE:
			return doubleFlowerPurple;
		case BLUE:
			return doubleFlowerBlue;
		case BROWN:
			return doubleFlowerBrown;
		case GREEN:
			return doubleFlowerGreen;
		case RED:
			return doubleFlowerRed;
		case BLACK:
			return doubleFlowerBlack;
		}
	}

	public static Block getPetalBlock(DyeColor color) {
		switch (color) {
		default:
		case WHITE:
			return petalBlockWhite;
		case ORANGE:
			return petalBlockOrange;
		case MAGENTA:
			return petalBlockMagenta;
		case LIGHT_BLUE:
			return petalBlockLightBlue;
		case YELLOW:
			return petalBlockYellow;
		case LIME:
			return petalBlockLime;
		case PINK:
			return petalBlockPink;
		case GRAY:
			return petalBlockGray;
		case LIGHT_GRAY:
			return petalBlockSilver;
		case CYAN:
			return petalBlockCyan;
		case PURPLE:
			return petalBlockPurple;
		case BLUE:
			return petalBlockBlue;
		case BROWN:
			return petalBlockBrown;
		case GREEN:
			return petalBlockGreen;
		case RED:
			return petalBlockRed;
		case BLACK:
			return petalBlockBlack;
		}
	}
}
