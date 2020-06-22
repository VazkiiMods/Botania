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

		register(r, whiteFlower.getRegistryName(), new BlockItem(whiteFlower, props));
		register(r, orangeFlower.getRegistryName(), new BlockItem(orangeFlower, props));
		register(r, magentaFlower.getRegistryName(), new BlockItem(magentaFlower, props));
		register(r, lightBlueFlower.getRegistryName(), new BlockItem(lightBlueFlower, props));
		register(r, yellowFlower.getRegistryName(), new BlockItem(yellowFlower, props));
		register(r, limeFlower.getRegistryName(), new BlockItem(limeFlower, props));
		register(r, pinkFlower.getRegistryName(), new BlockItem(pinkFlower, props));
		register(r, grayFlower.getRegistryName(), new BlockItem(grayFlower, props));
		register(r, lightGrayFlower.getRegistryName(), new BlockItem(lightGrayFlower, props));
		register(r, cyanFlower.getRegistryName(), new BlockItem(cyanFlower, props));
		register(r, purpleFlower.getRegistryName(), new BlockItem(purpleFlower, props));
		register(r, blueFlower.getRegistryName(), new BlockItem(blueFlower, props));
		register(r, brownFlower.getRegistryName(), new BlockItem(brownFlower, props));
		register(r, greenFlower.getRegistryName(), new BlockItem(greenFlower, props));
		register(r, redFlower.getRegistryName(), new BlockItem(redFlower, props));
		register(r, blackFlower.getRegistryName(), new BlockItem(blackFlower, props));
		register(r, defaultAltar.getRegistryName(), new BlockItem(defaultAltar, props));
		register(r, forestAltar.getRegistryName(), new BlockItem(forestAltar, props));
		register(r, plainsAltar.getRegistryName(), new BlockItem(plainsAltar, props));
		register(r, mountainAltar.getRegistryName(), new BlockItem(mountainAltar, props));
		register(r, fungalAltar.getRegistryName(), new BlockItem(fungalAltar, props));
		register(r, swampAltar.getRegistryName(), new BlockItem(swampAltar, props));
		register(r, desertAltar.getRegistryName(), new BlockItem(desertAltar, props));
		register(r, taigaAltar.getRegistryName(), new BlockItem(taigaAltar, props));
		register(r, mesaAltar.getRegistryName(), new BlockItem(mesaAltar, props));
		register(r, mossyAltar.getRegistryName(), new BlockItem(mossyAltar, props));
		register(r, livingrock.getRegistryName(), new BlockItem(livingrock, props));
		register(r, livingrockBrick.getRegistryName(), new BlockItem(livingrockBrick, props));
		register(r, livingrockBrickChiseled.getRegistryName(), new BlockItem(livingrockBrickChiseled, props));
		register(r, livingrockBrickCracked.getRegistryName(), new BlockItem(livingrockBrickCracked, props));
		register(r, livingrockBrickMossy.getRegistryName(), new BlockItem(livingrockBrickMossy, props));
		register(r, livingwood.getRegistryName(), new BlockItem(livingwood, props));
		register(r, livingwoodPlanks.getRegistryName(), new BlockItem(livingwoodPlanks, props));
		register(r, livingwoodPlanksMossy.getRegistryName(), new BlockItem(livingwoodPlanksMossy, props));
		register(r, livingwoodFramed.getRegistryName(), new BlockItem(livingwoodFramed, props));
		register(r, livingwoodPatternFramed.getRegistryName(), new BlockItem(livingwoodPatternFramed, props));
		register(r, livingwoodGlimmering.getRegistryName(), new BlockItem(livingwoodGlimmering, props));
		register(r, manaSpreader.getRegistryName(), new BlockItem(manaSpreader, props));
		register(r, redstoneSpreader.getRegistryName(), new BlockItem(redstoneSpreader, props));
		register(r, elvenSpreader.getRegistryName(), new BlockItem(elvenSpreader, props));
		register(r, gaiaSpreader.getRegistryName(), new BlockItem(gaiaSpreader, props));
		register(r, manaPool.getRegistryName(), new ItemBlockPool(manaPool, props));
		register(r, creativePool.getRegistryName(), new ItemBlockPool(creativePool, props));
		register(r, dilutedPool.getRegistryName(), new ItemBlockPool(dilutedPool, props));
		register(r, fabulousPool.getRegistryName(), new ItemBlockPool(fabulousPool, props));
		register(r, runeAltar.getRegistryName(), new BlockItem(runeAltar, props));
		register(r, pistonRelay.getRegistryName(), new BlockItem(pistonRelay, props));
		register(r, distributor.getRegistryName(), new BlockItem(distributor, props));
		register(r, manaVoid.getRegistryName(), new BlockItem(manaVoid, props));
		register(r, manaDetector.getRegistryName(), new BlockItem(manaDetector, props));
		register(r, enchanter.getRegistryName(), new BlockItem(enchanter, props));
		register(r, turntable.getRegistryName(), new BlockItem(turntable, props));
		register(r, tinyPlanet.getRegistryName(), new BlockItem(tinyPlanet, props));
		register(r, alchemyCatalyst.getRegistryName(), new BlockItem(alchemyCatalyst, props));
		register(r, openCrate.getRegistryName(), new BlockItem(openCrate, props));
		register(r, craftCrate.getRegistryName(), new BlockItem(craftCrate, props));
		register(r, forestEye.getRegistryName(), new BlockItem(forestEye, props));
		register(r, manasteelBlock.getRegistryName(), new BlockItem(manasteelBlock, props));
		register(r, terrasteelBlock.getRegistryName(), new BlockItem(terrasteelBlock, props));
		register(r, elementiumBlock.getRegistryName(), new ItemBlockElven(elementiumBlock, props));
		register(r, manaDiamondBlock.getRegistryName(), new BlockItem(manaDiamondBlock, props));
		register(r, dragonstoneBlock.getRegistryName(), new BlockItem(dragonstoneBlock, props));
		register(r, wildDrum.getRegistryName(), new BlockItem(wildDrum, props));
		register(r, gatheringDrum.getRegistryName(), new BlockItem(gatheringDrum, props));
		register(r, canopyDrum.getRegistryName(), new BlockItem(canopyDrum, props));
		register(r, whiteShinyFlower.getRegistryName(), new BlockItem(whiteShinyFlower, props));
		register(r, orangeShinyFlower.getRegistryName(), new BlockItem(orangeShinyFlower, props));
		register(r, magentaShinyFlower.getRegistryName(), new BlockItem(magentaShinyFlower, props));
		register(r, lightBlueShinyFlower.getRegistryName(), new BlockItem(lightBlueShinyFlower, props));
		register(r, yellowShinyFlower.getRegistryName(), new BlockItem(yellowShinyFlower, props));
		register(r, limeShinyFlower.getRegistryName(), new BlockItem(limeShinyFlower, props));
		register(r, pinkShinyFlower.getRegistryName(), new BlockItem(pinkShinyFlower, props));
		register(r, grayShinyFlower.getRegistryName(), new BlockItem(grayShinyFlower, props));
		register(r, lightGrayShinyFlower.getRegistryName(), new BlockItem(lightGrayShinyFlower, props));
		register(r, cyanShinyFlower.getRegistryName(), new BlockItem(cyanShinyFlower, props));
		register(r, purpleShinyFlower.getRegistryName(), new BlockItem(purpleShinyFlower, props));
		register(r, blueShinyFlower.getRegistryName(), new BlockItem(blueShinyFlower, props));
		register(r, brownShinyFlower.getRegistryName(), new BlockItem(brownShinyFlower, props));
		register(r, greenShinyFlower.getRegistryName(), new BlockItem(greenShinyFlower, props));
		register(r, redShinyFlower.getRegistryName(), new BlockItem(redShinyFlower, props));
		register(r, blackShinyFlower.getRegistryName(), new BlockItem(blackShinyFlower, props));
		register(r, abstrusePlatform.getRegistryName(), new BlockItem(abstrusePlatform, props));
		register(r, spectralPlatform.getRegistryName(), new BlockItem(spectralPlatform, props));
		register(r, infrangiblePlatform.getRegistryName(), new BlockItem(infrangiblePlatform, props));
		register(r, alfPortal.getRegistryName(), new BlockItem(alfPortal, props));
		register(r, dreamwood.getRegistryName(), new ItemBlockDreamwood(dreamwood, props));
		register(r, dreamwoodPlanks.getRegistryName(), new ItemBlockDreamwood(dreamwoodPlanks, props));
		register(r, dreamwoodPlanksMossy.getRegistryName(), new ItemBlockDreamwood(dreamwoodPlanksMossy, props));
		register(r, dreamwoodFramed.getRegistryName(), new ItemBlockDreamwood(dreamwoodFramed, props));
		register(r, dreamwoodPatternFramed.getRegistryName(), new ItemBlockDreamwood(dreamwoodPatternFramed, props));
		register(r, dreamwoodGlimmering.getRegistryName(), new ItemBlockDreamwood(dreamwoodGlimmering, props));
		register(r, conjurationCatalyst.getRegistryName(), new BlockItem(conjurationCatalyst, props));
		register(r, bifrost.getRegistryName(), new BlockItem(bifrost, props));
		register(r, whiteFloatingFlower.getRegistryName(), new BlockItem(whiteFloatingFlower, props));
		register(r, orangeFloatingFlower.getRegistryName(), new BlockItem(orangeFloatingFlower, props));
		register(r, magentaFloatingFlower.getRegistryName(), new BlockItem(magentaFloatingFlower, props));
		register(r, lightBlueFloatingFlower.getRegistryName(), new BlockItem(lightBlueFloatingFlower, props));
		register(r, yellowFloatingFlower.getRegistryName(), new BlockItem(yellowFloatingFlower, props));
		register(r, limeFloatingFlower.getRegistryName(), new BlockItem(limeFloatingFlower, props));
		register(r, pinkFloatingFlower.getRegistryName(), new BlockItem(pinkFloatingFlower, props));
		register(r, grayFloatingFlower.getRegistryName(), new BlockItem(grayFloatingFlower, props));
		register(r, lightGrayFloatingFlower.getRegistryName(), new BlockItem(lightGrayFloatingFlower, props));
		register(r, cyanFloatingFlower.getRegistryName(), new BlockItem(cyanFloatingFlower, props));
		register(r, purpleFloatingFlower.getRegistryName(), new BlockItem(purpleFloatingFlower, props));
		register(r, blueFloatingFlower.getRegistryName(), new BlockItem(blueFloatingFlower, props));
		register(r, brownFloatingFlower.getRegistryName(), new BlockItem(brownFloatingFlower, props));
		register(r, greenFloatingFlower.getRegistryName(), new BlockItem(greenFloatingFlower, props));
		register(r, redFloatingFlower.getRegistryName(), new BlockItem(redFloatingFlower, props));
		register(r, blackFloatingFlower.getRegistryName(), new BlockItem(blackFloatingFlower, props));
		register(r, tinyPotato.getRegistryName(), new ItemBlockTinyPotato(tinyPotato, props));
		register(r, spawnerClaw.getRegistryName(), new BlockItem(spawnerClaw, props));
		register(r, azulejo0.getRegistryName(), new BlockItem(azulejo0, props));
		register(r, azulejo1.getRegistryName(), new BlockItem(azulejo1, props));
		register(r, azulejo2.getRegistryName(), new BlockItem(azulejo2, props));
		register(r, azulejo3.getRegistryName(), new BlockItem(azulejo3, props));
		register(r, azulejo4.getRegistryName(), new BlockItem(azulejo4, props));
		register(r, azulejo5.getRegistryName(), new BlockItem(azulejo5, props));
		register(r, azulejo6.getRegistryName(), new BlockItem(azulejo6, props));
		register(r, azulejo7.getRegistryName(), new BlockItem(azulejo7, props));
		register(r, azulejo8.getRegistryName(), new BlockItem(azulejo8, props));
		register(r, azulejo9.getRegistryName(), new BlockItem(azulejo9, props));
		register(r, azulejo10.getRegistryName(), new BlockItem(azulejo10, props));
		register(r, azulejo11.getRegistryName(), new BlockItem(azulejo11, props));
		register(r, azulejo12.getRegistryName(), new BlockItem(azulejo12, props));
		register(r, azulejo13.getRegistryName(), new BlockItem(azulejo13, props));
		register(r, azulejo14.getRegistryName(), new BlockItem(azulejo14, props));
		register(r, azulejo15.getRegistryName(), new BlockItem(azulejo15, props));
		register(r, enderEye.getRegistryName(), new BlockItem(enderEye, props));
		register(r, starfield.getRegistryName(), new BlockItem(starfield, props));
		register(r, rfGenerator.getRegistryName(), new BlockItem(rfGenerator, props));
		register(r, elfGlass.getRegistryName(), new ItemBlockElven(elfGlass, props));
		register(r, manaGlass.getRegistryName(), new BlockItem(manaGlass, props));
		register(r, terraPlate.getRegistryName(), new BlockItem(terraPlate, props));
		register(r, redStringContainer.getRegistryName(), new BlockItem(redStringContainer, props));
		register(r, redStringDispenser.getRegistryName(), new BlockItem(redStringDispenser, props));
		register(r, redStringFertilizer.getRegistryName(), new BlockItem(redStringFertilizer, props));
		register(r, redStringComparator.getRegistryName(), new BlockItem(redStringComparator, props));
		register(r, redStringRelay.getRegistryName(), new BlockItem(redStringRelay, props));
		register(r, prism.getRegistryName(), new BlockItem(prism, props));
		register(r, enchantedSoil.getRegistryName(), new BlockItem(enchantedSoil, props));
		register(r, petalBlockWhite.getRegistryName(), new BlockItem(petalBlockWhite, props));
		register(r, petalBlockOrange.getRegistryName(), new BlockItem(petalBlockOrange, props));
		register(r, petalBlockMagenta.getRegistryName(), new BlockItem(petalBlockMagenta, props));
		register(r, petalBlockLightBlue.getRegistryName(), new BlockItem(petalBlockLightBlue, props));
		register(r, petalBlockYellow.getRegistryName(), new BlockItem(petalBlockYellow, props));
		register(r, petalBlockLime.getRegistryName(), new BlockItem(petalBlockLime, props));
		register(r, petalBlockPink.getRegistryName(), new BlockItem(petalBlockPink, props));
		register(r, petalBlockGray.getRegistryName(), new BlockItem(petalBlockGray, props));
		register(r, petalBlockSilver.getRegistryName(), new BlockItem(petalBlockSilver, props));
		register(r, petalBlockCyan.getRegistryName(), new BlockItem(petalBlockCyan, props));
		register(r, petalBlockPurple.getRegistryName(), new BlockItem(petalBlockPurple, props));
		register(r, petalBlockBlue.getRegistryName(), new BlockItem(petalBlockBlue, props));
		register(r, petalBlockBrown.getRegistryName(), new BlockItem(petalBlockBrown, props));
		register(r, petalBlockGreen.getRegistryName(), new BlockItem(petalBlockGreen, props));
		register(r, petalBlockRed.getRegistryName(), new BlockItem(petalBlockRed, props));
		register(r, petalBlockBlack.getRegistryName(), new BlockItem(petalBlockBlack, props));
		register(r, corporeaFunnel.getRegistryName(), new BlockItem(corporeaFunnel, props));
		register(r, whiteMushroom.getRegistryName(), new BlockItem(whiteMushroom, props));
		register(r, orangeMushroom.getRegistryName(), new BlockItem(orangeMushroom, props));
		register(r, magentaMushroom.getRegistryName(), new BlockItem(magentaMushroom, props));
		register(r, lightBlueMushroom.getRegistryName(), new BlockItem(lightBlueMushroom, props));
		register(r, yellowMushroom.getRegistryName(), new BlockItem(yellowMushroom, props));
		register(r, limeMushroom.getRegistryName(), new BlockItem(limeMushroom, props));
		register(r, pinkMushroom.getRegistryName(), new BlockItem(pinkMushroom, props));
		register(r, grayMushroom.getRegistryName(), new BlockItem(grayMushroom, props));
		register(r, lightGrayMushroom.getRegistryName(), new BlockItem(lightGrayMushroom, props));
		register(r, cyanMushroom.getRegistryName(), new BlockItem(cyanMushroom, props));
		register(r, purpleMushroom.getRegistryName(), new BlockItem(purpleMushroom, props));
		register(r, blueMushroom.getRegistryName(), new BlockItem(blueMushroom, props));
		register(r, brownMushroom.getRegistryName(), new BlockItem(brownMushroom, props));
		register(r, greenMushroom.getRegistryName(), new BlockItem(greenMushroom, props));
		register(r, redMushroom.getRegistryName(), new BlockItem(redMushroom, props));
		register(r, blackMushroom.getRegistryName(), new BlockItem(blackMushroom, props));
		register(r, pump.getRegistryName(), new BlockItem(pump, props));
		register(r, doubleFlowerWhite.getRegistryName(), new BlockItem(doubleFlowerWhite, props));
		register(r, doubleFlowerOrange.getRegistryName(), new BlockItem(doubleFlowerOrange, props));
		register(r, doubleFlowerMagenta.getRegistryName(), new BlockItem(doubleFlowerMagenta, props));
		register(r, doubleFlowerLightBlue.getRegistryName(), new BlockItem(doubleFlowerLightBlue, props));
		register(r, doubleFlowerYellow.getRegistryName(), new BlockItem(doubleFlowerYellow, props));
		register(r, doubleFlowerLime.getRegistryName(), new BlockItem(doubleFlowerLime, props));
		register(r, doubleFlowerPink.getRegistryName(), new BlockItem(doubleFlowerPink, props));
		register(r, doubleFlowerGray.getRegistryName(), new BlockItem(doubleFlowerGray, props));
		register(r, doubleFlowerLightGray.getRegistryName(), new BlockItem(doubleFlowerLightGray, props));
		register(r, doubleFlowerCyan.getRegistryName(), new BlockItem(doubleFlowerCyan, props));
		register(r, doubleFlowerPurple.getRegistryName(), new BlockItem(doubleFlowerPurple, props));
		register(r, doubleFlowerBlue.getRegistryName(), new BlockItem(doubleFlowerBlue, props));
		register(r, doubleFlowerBrown.getRegistryName(), new BlockItem(doubleFlowerBrown, props));
		register(r, doubleFlowerGreen.getRegistryName(), new BlockItem(doubleFlowerGreen, props));
		register(r, doubleFlowerRed.getRegistryName(), new BlockItem(doubleFlowerRed, props));
		register(r, doubleFlowerBlack.getRegistryName(), new BlockItem(doubleFlowerBlack, props));
		register(r, blazeBlock.getRegistryName(), new ItemBlockBlaze(blazeBlock, props));
		register(r, corporeaInterceptor.getRegistryName(), new BlockItem(corporeaInterceptor, props));
		register(r, incensePlate.getRegistryName(), new BlockItem(incensePlate, props));
		register(r, ghostRail.getRegistryName(), new BlockItem(ghostRail, props));
		register(r, sparkChanger.getRegistryName(), new BlockItem(sparkChanger, props));
		register(r, root.getRegistryName(), new BlockItem(root, props));
		register(r, felPumpkin.getRegistryName(), new BlockItem(felPumpkin, props));
		register(r, cocoon.getRegistryName(), new BlockItem(cocoon, props));
		register(r, lightRelayDefault.getRegistryName(), new BlockItem(lightRelayDefault, props));
		register(r, lightRelayDetector.getRegistryName(), new BlockItem(lightRelayDetector, props));
		register(r, lightRelayFork.getRegistryName(), new BlockItem(lightRelayFork, props));
		register(r, lightRelayToggle.getRegistryName(), new BlockItem(lightRelayToggle, props));
		register(r, lightLauncher.getRegistryName(), new BlockItem(lightLauncher, props));
		register(r, manaBomb.getRegistryName(), new BlockItem(manaBomb, props));
		register(r, cacophonium.getRegistryName(), new BlockItem(cacophonium, props));
		register(r, bifrostPerm.getRegistryName(), new BlockItem(bifrostPerm, props));
		register(r, cellBlock.getRegistryName(), new BlockItem(cellBlock, props));
		register(r, redStringInterceptor.getRegistryName(), new BlockItem(redStringInterceptor, props));
		register(r, corporeaRetainer.getRegistryName(), new BlockItem(corporeaRetainer, props));
		register(r, shimmerrock.getRegistryName(), new BlockItem(shimmerrock, props));
		register(r, shimmerwoodPlanks.getRegistryName(), new BlockItem(shimmerwoodPlanks, props));
		register(r, dryGrass.getRegistryName(), new BlockItem(dryGrass, props));
		register(r, goldenGrass.getRegistryName(), new BlockItem(goldenGrass, props));
		register(r, vividGrass.getRegistryName(), new BlockItem(vividGrass, props));
		register(r, scorchedGrass.getRegistryName(), new BlockItem(scorchedGrass, props));
		register(r, infusedGrass.getRegistryName(), new BlockItem(infusedGrass, props));
		register(r, mutatedGrass.getRegistryName(), new BlockItem(mutatedGrass, props));
		register(r, animatedTorch.getRegistryName(), new BlockItem(animatedTorch, props));
		register(r, corporeaCrystalCube.getRegistryName(), new BlockItem(corporeaCrystalCube, props));

		DistExecutor.runForDist(() -> () -> registerWithTEISRS(r), () -> () -> registerWithoutTEISRS(r));
	}

	// Yay side-safety -.-

	@OnlyIn(Dist.DEDICATED_SERVER)
	private static Void registerWithoutTEISRS(IForgeRegistry<Item> r) {
		Item.Properties props = ModItems.defaultBuilder();
		register(r, manaPylon.getRegistryName(), new BlockItem(manaPylon, props));
		register(r, naturaPylon.getRegistryName(), new BlockItem(naturaPylon, props));
		register(r, gaiaPylon.getRegistryName(), new BlockItem(gaiaPylon, props));

		register(r, teruTeruBozu.getRegistryName(), new BlockItem(teruTeruBozu, props));
		register(r, avatar.getRegistryName(), new BlockItem(avatar, props));
		register(r, bellows.getRegistryName(), new BlockItem(bellows, props));
		register(r, brewery.getRegistryName(), new BlockItem(brewery, props));
		register(r, corporeaIndex.getRegistryName(), new BlockItem(corporeaIndex, props));
		register(r, hourglass.getRegistryName(), new BlockItem(hourglass, props));
		Item head = new ItemGaiaHead(gaiaHead, gaiaHeadWall, ModItems.defaultBuilder().rarity(Rarity.UNCOMMON));
		register(r, gaiaHead.getRegistryName(), head);
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	private static Void registerWithTEISRS(IForgeRegistry<Item> r) {
		Item.Properties pylonProps = ModItems.defaultBuilder().setISTER(() -> () -> new RenderTilePylon.TEISR());
		register(r, manaPylon.getRegistryName(), new BlockItem(manaPylon, pylonProps));
		register(r, naturaPylon.getRegistryName(), new BlockItem(naturaPylon, pylonProps));
		register(r, gaiaPylon.getRegistryName(), new BlockItem(gaiaPylon, pylonProps));

		register(r, teruTeruBozu.getRegistryName(), new BlockItem(teruTeruBozu, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(teruTeruBozu))));
		register(r, avatar.getRegistryName(), new BlockItem(avatar, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(avatar))));
		register(r, bellows.getRegistryName(), new BlockItem(bellows, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(bellows))));
		register(r, brewery.getRegistryName(), new BlockItem(brewery, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(brewery))));
		register(r, corporeaIndex.getRegistryName(), new BlockItem(corporeaIndex, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(corporeaIndex))));
		register(r, hourglass.getRegistryName(), new BlockItem(hourglass, ModItems.defaultBuilder().setISTER(() -> () -> new TEISR(hourglass))));
		Item head = new ItemGaiaHead(gaiaHead, gaiaHeadWall, ModItems.defaultBuilder().rarity(Rarity.UNCOMMON).setISTER(() -> () -> new TEISR(gaiaHead)));
		register(r, gaiaHead.getRegistryName(), head);
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

	public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt) {
		IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();
		register(r, LibBlockNames.ALTAR, TileEntityType.Builder.create(TileAltar::new,
				defaultAltar, forestAltar, plainsAltar, mountainAltar, fungalAltar,
				swampAltar, desertAltar, taigaAltar, mesaAltar, mossyAltar
		).build(null));
		register(r, LibBlockNames.SPREADER, TileEntityType.Builder.create(TileSpreader::new, manaSpreader, redstoneSpreader, elvenSpreader, gaiaSpreader).build(null));
		register(r, LibBlockNames.POOL, TileEntityType.Builder.create(TilePool::new, manaPool, dilutedPool, fabulousPool, creativePool).build(null));
		register(r, LibBlockNames.RUNE_ALTAR, TileEntityType.Builder.create(TileRuneAltar::new, runeAltar).build(null));
		register(r, LibBlockNames.PYLON, TileEntityType.Builder.create(TilePylon::new, manaPylon, naturaPylon, gaiaPylon).build(null));
		register(r, LibBlockNames.DISTRIBUTOR, TileEntityType.Builder.create(TileDistributor::new, distributor).build(null));
		register(r, LibBlockNames.MANA_VOID, TileEntityType.Builder.create(TileManaVoid::new, manaVoid).build(null));
		register(r, LibBlockNames.MANA_DETECTOR, TileEntityType.Builder.create(TileManaDetector::new, manaDetector).build(null));
		register(r, LibBlockNames.ENCHANTER, TileEntityType.Builder.create(TileEnchanter::new, enchanter).build(null));
		register(r, LibBlockNames.TURNTABLE, TileEntityType.Builder.create(TileTurntable::new, turntable).build(null));
		register(r, LibBlockNames.TINY_PLANET, TileEntityType.Builder.create(TileTinyPlanet::new, tinyPlanet).build(null));
		register(r, LibBlockNames.OPEN_CRATE, TileEntityType.Builder.create(TileOpenCrate::new, openCrate).build(null));
		register(r, LibBlockNames.CRAFT_CRATE, TileEntityType.Builder.create(TileCraftCrate::new, craftCrate).build(null));
		register(r, LibBlockNames.FOREST_EYE, TileEntityType.Builder.create(TileForestEye::new, forestEye).build(null));
		register(r, LibBlockNames.PLATFORM, TileEntityType.Builder.create(TilePlatform::new, abstrusePlatform, spectralPlatform, infrangiblePlatform).build(null));
		register(r, LibBlockNames.ALF_PORTAL, TileEntityType.Builder.create(TileAlfPortal::new, alfPortal).build(null));
		register(r, LibBlockNames.BIFROST, TileEntityType.Builder.create(TileBifrost::new, bifrost).build(null));
		register(r, LibBlockNames.MINI_ISLAND, TileEntityType.Builder.create(TileFloatingFlower::new, Arrays.stream(DyeColor.values()).map(ModBlocks::getFloatingFlower).toArray(Block[]::new)).build(null));
		register(r, LibBlockNames.TINY_POTATO, TileEntityType.Builder.create(TileTinyPotato::new, tinyPotato).build(null));
		register(r, LibBlockNames.SPAWNER_CLAW, TileEntityType.Builder.create(TileSpawnerClaw::new, spawnerClaw).build(null));
		register(r, LibBlockNames.ENDER_EYE_BLOCK, TileEntityType.Builder.create(TileEnderEye::new, enderEye).build(null));
		register(r, LibBlockNames.STARFIELD, TileEntityType.Builder.create(TileStarfield::new, starfield).build(null));
		register(r, LibBlockNames.FLUXFIELD, TileEntityType.Builder.create(TileRFGenerator::new, rfGenerator).build(null));
		register(r, LibBlockNames.BREWERY, TileEntityType.Builder.create(TileBrewery::new, brewery).build(null));
		register(r, LibBlockNames.TERRA_PLATE, TileEntityType.Builder.create(TileTerraPlate::new, terraPlate).build(null));
		register(r, LibBlockNames.RED_STRING_CONTAINER, TileEntityType.Builder.create(TileRedStringContainer::new, redStringContainer).build(null));
		register(r, LibBlockNames.RED_STRING_DISPENSER, TileEntityType.Builder.create(TileRedStringDispenser::new, redStringDispenser).build(null));
		register(r, LibBlockNames.RED_STRING_FERTILIZER, TileEntityType.Builder.create(TileRedStringFertilizer::new, redStringFertilizer).build(null));
		register(r, LibBlockNames.RED_STRING_COMPARATOR, TileEntityType.Builder.create(TileRedStringComparator::new, redStringComparator).build(null));
		register(r, LibBlockNames.RED_STRING_RELAY, TileEntityType.Builder.create(TileRedStringRelay::new, redStringRelay).build(null));
		register(r, LibBlockNames.MANA_FLAME, TileEntityType.Builder.create(TileManaFlame::new, manaFlame).build(null));
		register(r, LibBlockNames.PRISM, TileEntityType.Builder.create(TilePrism::new, prism).build(null));
		register(r, LibBlockNames.CORPOREA_INDEX, TileEntityType.Builder.create(TileCorporeaIndex::new, corporeaIndex).build(null));
		register(r, LibBlockNames.CORPOREA_FUNNEL, TileEntityType.Builder.create(TileCorporeaFunnel::new, corporeaFunnel).build(null));
		register(r, LibBlockNames.PUMP, TileEntityType.Builder.create(TilePump::new, pump).build(null));
		register(r, LibBlockNames.FAKE_AIR, TileEntityType.Builder.create(TileFakeAir::new, fakeAir).build(null));
		register(r, LibBlockNames.CORPOREA_INTERCEPTOR, TileEntityType.Builder.create(TileCorporeaInterceptor::new, corporeaInterceptor).build(null));
		register(r, LibBlockNames.CORPOREA_CRYSTAL_CUBE, TileEntityType.Builder.create(TileCorporeaCrystalCube::new, corporeaCrystalCube).build(null));
		register(r, LibBlockNames.INCENSE_PLATE, TileEntityType.Builder.create(TileIncensePlate::new, incensePlate).build(null));
		register(r, LibBlockNames.HOURGLASS, TileEntityType.Builder.create(TileHourglass::new, hourglass).build(null));
		register(r, LibBlockNames.SPARK_CHANGER, TileEntityType.Builder.create(TileSparkChanger::new, sparkChanger).build(null));
		register(r, LibBlockNames.COCOON, TileEntityType.Builder.create(TileCocoon::new, cocoon).build(null));
		register(r, LibBlockNames.LIGHT_RELAY, TileEntityType.Builder.create(TileLightRelay::new, lightRelayDefault, lightRelayDetector, lightRelayToggle, lightRelayFork).build(null));
		register(r, LibBlockNames.CACOPHONIUM, TileEntityType.Builder.create(TileCacophonium::new, cacophonium).build(null));
		register(r, LibBlockNames.BELLOWS, TileEntityType.Builder.create(TileBellows::new, bellows).build(null));
		register(r, LibBlockNames.CELL_BLOCK, TileEntityType.Builder.create(TileCell::new, cellBlock).build(null));
		register(r, LibBlockNames.RED_STRING_INTERCEPTOR, TileEntityType.Builder.create(TileRedStringInterceptor::new, redStringInterceptor).build(null));
		register(r, LibBlockNames.GAIA_HEAD, TileEntityType.Builder.create(TileGaiaHead::new, gaiaHead, gaiaHeadWall).build(null));
		register(r, LibBlockNames.CORPOREA_RETAINER, TileEntityType.Builder.create(TileCorporeaRetainer::new, corporeaRetainer).build(null));
		register(r, LibBlockNames.TERU_TERU_BOZU, TileEntityType.Builder.create(TileTeruTeruBozu::new, teruTeruBozu).build(null));
		register(r, LibBlockNames.AVATAR, TileEntityType.Builder.create(TileAvatar::new, avatar).build(null));
		register(r, LibBlockNames.ANIMATED_TORCH, TileEntityType.Builder.create(TileAnimatedTorch::new, animatedTorch).build(null));
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
