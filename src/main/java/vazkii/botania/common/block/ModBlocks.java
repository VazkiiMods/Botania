/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.registry.Registry;

import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.corporea.*;
import vazkii.botania.common.block.decor.*;
import vazkii.botania.common.block.decor.stairs.BlockModStairs;
import vazkii.botania.common.block.dispenser.*;
import vazkii.botania.common.block.mana.*;
import vazkii.botania.common.block.string.*;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockDreamwood;
import vazkii.botania.common.item.block.ItemBlockElven;
import vazkii.botania.common.item.block.ItemBlockPool;
import vazkii.botania.common.item.block.ItemBlockTinyPotato;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.mixin.AccessorDispenserBlock;

import javax.annotation.Nonnull;

import java.util.Locale;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class ModBlocks {
	private static final AbstractBlock.TypedContextPredicate<EntityType<?>> NO_SPAWN = (state, world, pos, et) -> false;
	private static final AbstractBlock.ContextPredicate NO_SUFFOCATION = (state, world, pos) -> false;

	public static final Block whiteFlower = new BlockModFlower(DyeColor.WHITE, AbstractBlock.Settings.of(Material.PLANT).noCollision().strength(0).sounds(BlockSoundGroup.GRASS));
	public static final Block orangeFlower = new BlockModFlower(DyeColor.ORANGE, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block magentaFlower = new BlockModFlower(DyeColor.MAGENTA, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block lightBlueFlower = new BlockModFlower(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block yellowFlower = new BlockModFlower(DyeColor.YELLOW, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block limeFlower = new BlockModFlower(DyeColor.LIME, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block pinkFlower = new BlockModFlower(DyeColor.PINK, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block grayFlower = new BlockModFlower(DyeColor.GRAY, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block lightGrayFlower = new BlockModFlower(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block cyanFlower = new BlockModFlower(DyeColor.CYAN, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block purpleFlower = new BlockModFlower(DyeColor.PURPLE, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block blueFlower = new BlockModFlower(DyeColor.BLUE, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block brownFlower = new BlockModFlower(DyeColor.BROWN, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block greenFlower = new BlockModFlower(DyeColor.GREEN, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block redFlower = new BlockModFlower(DyeColor.RED, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block blackFlower = new BlockModFlower(DyeColor.BLACK, AbstractBlock.Settings.copy(whiteFlower));

	public static final Block whiteShinyFlower = new BlockShinyFlower(DyeColor.WHITE, AbstractBlock.Settings.copy(whiteFlower).luminance(s -> 15));
	public static final Block orangeShinyFlower = new BlockShinyFlower(DyeColor.ORANGE, AbstractBlock.Settings.copy(whiteShinyFlower));
	public static final Block magentaShinyFlower = new BlockShinyFlower(DyeColor.MAGENTA, AbstractBlock.Settings.copy(whiteShinyFlower));
	public static final Block lightBlueShinyFlower = new BlockShinyFlower(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(whiteShinyFlower));
	public static final Block yellowShinyFlower = new BlockShinyFlower(DyeColor.YELLOW, AbstractBlock.Settings.copy(whiteShinyFlower));
	public static final Block limeShinyFlower = new BlockShinyFlower(DyeColor.LIME, AbstractBlock.Settings.copy(whiteShinyFlower));
	public static final Block pinkShinyFlower = new BlockShinyFlower(DyeColor.PINK, AbstractBlock.Settings.copy(whiteShinyFlower));
	public static final Block grayShinyFlower = new BlockShinyFlower(DyeColor.GRAY, AbstractBlock.Settings.copy(whiteShinyFlower));
	public static final Block lightGrayShinyFlower = new BlockShinyFlower(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(whiteShinyFlower));
	public static final Block cyanShinyFlower = new BlockShinyFlower(DyeColor.CYAN, AbstractBlock.Settings.copy(whiteShinyFlower));
	public static final Block purpleShinyFlower = new BlockShinyFlower(DyeColor.PURPLE, AbstractBlock.Settings.copy(whiteShinyFlower));
	public static final Block blueShinyFlower = new BlockShinyFlower(DyeColor.BLUE, AbstractBlock.Settings.copy(whiteShinyFlower));
	public static final Block brownShinyFlower = new BlockShinyFlower(DyeColor.BROWN, AbstractBlock.Settings.copy(whiteShinyFlower));
	public static final Block greenShinyFlower = new BlockShinyFlower(DyeColor.GREEN, AbstractBlock.Settings.copy(whiteShinyFlower));
	public static final Block redShinyFlower = new BlockShinyFlower(DyeColor.RED, AbstractBlock.Settings.copy(whiteShinyFlower));
	public static final Block blackShinyFlower = new BlockShinyFlower(DyeColor.BLACK, AbstractBlock.Settings.copy(whiteShinyFlower));

	public static final Block whiteBuriedPetals = new BlockBuriedPetals(DyeColor.WHITE, AbstractBlock.Settings.copy(whiteFlower).luminance(s -> 4));
	public static final Block orangeBuriedPetals = new BlockBuriedPetals(DyeColor.ORANGE, AbstractBlock.Settings.copy(whiteBuriedPetals));
	public static final Block magentaBuriedPetals = new BlockBuriedPetals(DyeColor.MAGENTA, AbstractBlock.Settings.copy(whiteBuriedPetals));
	public static final Block lightBlueBuriedPetals = new BlockBuriedPetals(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(whiteBuriedPetals));
	public static final Block yellowBuriedPetals = new BlockBuriedPetals(DyeColor.YELLOW, AbstractBlock.Settings.copy(whiteBuriedPetals));
	public static final Block limeBuriedPetals = new BlockBuriedPetals(DyeColor.LIME, AbstractBlock.Settings.copy(whiteBuriedPetals));
	public static final Block pinkBuriedPetals = new BlockBuriedPetals(DyeColor.PINK, AbstractBlock.Settings.copy(whiteBuriedPetals));
	public static final Block grayBuriedPetals = new BlockBuriedPetals(DyeColor.GRAY, AbstractBlock.Settings.copy(whiteBuriedPetals));
	public static final Block lightGrayBuriedPetals = new BlockBuriedPetals(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(whiteBuriedPetals));
	public static final Block cyanBuriedPetals = new BlockBuriedPetals(DyeColor.CYAN, AbstractBlock.Settings.copy(whiteBuriedPetals));
	public static final Block purpleBuriedPetals = new BlockBuriedPetals(DyeColor.PURPLE, AbstractBlock.Settings.copy(whiteBuriedPetals));
	public static final Block blueBuriedPetals = new BlockBuriedPetals(DyeColor.BLUE, AbstractBlock.Settings.copy(whiteBuriedPetals));
	public static final Block brownBuriedPetals = new BlockBuriedPetals(DyeColor.BROWN, AbstractBlock.Settings.copy(whiteBuriedPetals));
	public static final Block greenBuriedPetals = new BlockBuriedPetals(DyeColor.GREEN, AbstractBlock.Settings.copy(whiteBuriedPetals));
	public static final Block redBuriedPetals = new BlockBuriedPetals(DyeColor.RED, AbstractBlock.Settings.copy(whiteBuriedPetals));
	public static final Block blackBuriedPetals = new BlockBuriedPetals(DyeColor.BLACK, AbstractBlock.Settings.copy(whiteBuriedPetals));

	public static final AbstractBlock.Settings FLOATING_PROPS = FabricBlockSettings.copyOf(AbstractBlock.Settings.of(Material.SOIL).strength(0.5F).sounds(BlockSoundGroup.GRAVEL).luminance(s -> 15))
		.breakByTool(FabricToolTags.SHOVELS);
	public static final Block whiteFloatingFlower = new BlockFloatingFlower(DyeColor.WHITE, FLOATING_PROPS);
	public static final Block orangeFloatingFlower = new BlockFloatingFlower(DyeColor.ORANGE, FLOATING_PROPS);
	public static final Block magentaFloatingFlower = new BlockFloatingFlower(DyeColor.MAGENTA, FLOATING_PROPS);
	public static final Block lightBlueFloatingFlower = new BlockFloatingFlower(DyeColor.LIGHT_BLUE, FLOATING_PROPS);
	public static final Block yellowFloatingFlower = new BlockFloatingFlower(DyeColor.YELLOW, FLOATING_PROPS);
	public static final Block limeFloatingFlower = new BlockFloatingFlower(DyeColor.LIME, FLOATING_PROPS);
	public static final Block pinkFloatingFlower = new BlockFloatingFlower(DyeColor.PINK, FLOATING_PROPS);
	public static final Block grayFloatingFlower = new BlockFloatingFlower(DyeColor.GRAY, FLOATING_PROPS);
	public static final Block lightGrayFloatingFlower = new BlockFloatingFlower(DyeColor.LIGHT_GRAY, FLOATING_PROPS);
	public static final Block cyanFloatingFlower = new BlockFloatingFlower(DyeColor.CYAN, FLOATING_PROPS);
	public static final Block purpleFloatingFlower = new BlockFloatingFlower(DyeColor.PURPLE, FLOATING_PROPS);
	public static final Block blueFloatingFlower = new BlockFloatingFlower(DyeColor.BLUE, FLOATING_PROPS);
	public static final Block brownFloatingFlower = new BlockFloatingFlower(DyeColor.BROWN, FLOATING_PROPS);
	public static final Block greenFloatingFlower = new BlockFloatingFlower(DyeColor.GREEN, FLOATING_PROPS);
	public static final Block redFloatingFlower = new BlockFloatingFlower(DyeColor.RED, FLOATING_PROPS);
	public static final Block blackFloatingFlower = new BlockFloatingFlower(DyeColor.BLACK, FLOATING_PROPS);

	public static final Block petalBlockWhite = new BlockPetalBlock(DyeColor.WHITE, AbstractBlock.Settings.of(Material.PLANT).strength(0.4F).sounds(BlockSoundGroup.GRASS));
	public static final Block petalBlockOrange = new BlockPetalBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(petalBlockWhite));
	public static final Block petalBlockMagenta = new BlockPetalBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(petalBlockWhite));
	public static final Block petalBlockLightBlue = new BlockPetalBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(petalBlockWhite));
	public static final Block petalBlockYellow = new BlockPetalBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(petalBlockWhite));
	public static final Block petalBlockLime = new BlockPetalBlock(DyeColor.LIME, AbstractBlock.Settings.copy(petalBlockWhite));
	public static final Block petalBlockPink = new BlockPetalBlock(DyeColor.PINK, AbstractBlock.Settings.copy(petalBlockWhite));
	public static final Block petalBlockGray = new BlockPetalBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(petalBlockWhite));
	public static final Block petalBlockSilver = new BlockPetalBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(petalBlockWhite));
	public static final Block petalBlockCyan = new BlockPetalBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(petalBlockWhite));
	public static final Block petalBlockPurple = new BlockPetalBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(petalBlockWhite));
	public static final Block petalBlockBlue = new BlockPetalBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(petalBlockWhite));
	public static final Block petalBlockBrown = new BlockPetalBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(petalBlockWhite));
	public static final Block petalBlockGreen = new BlockPetalBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(petalBlockWhite));
	public static final Block petalBlockRed = new BlockPetalBlock(DyeColor.RED, AbstractBlock.Settings.copy(petalBlockWhite));
	public static final Block petalBlockBlack = new BlockPetalBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(petalBlockWhite));

	public static final Block whiteMushroom = new BlockModMushroom(DyeColor.WHITE, AbstractBlock.Settings.copy(whiteFlower).luminance(s -> 3));
	public static final Block orangeMushroom = new BlockModMushroom(DyeColor.ORANGE, AbstractBlock.Settings.copy(whiteMushroom));
	public static final Block magentaMushroom = new BlockModMushroom(DyeColor.MAGENTA, AbstractBlock.Settings.copy(whiteMushroom));
	public static final Block lightBlueMushroom = new BlockModMushroom(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(whiteMushroom));
	public static final Block yellowMushroom = new BlockModMushroom(DyeColor.YELLOW, AbstractBlock.Settings.copy(whiteMushroom));
	public static final Block limeMushroom = new BlockModMushroom(DyeColor.LIME, AbstractBlock.Settings.copy(whiteMushroom));
	public static final Block pinkMushroom = new BlockModMushroom(DyeColor.PINK, AbstractBlock.Settings.copy(whiteMushroom));
	public static final Block grayMushroom = new BlockModMushroom(DyeColor.GRAY, AbstractBlock.Settings.copy(whiteMushroom));
	public static final Block lightGrayMushroom = new BlockModMushroom(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(whiteMushroom));
	public static final Block cyanMushroom = new BlockModMushroom(DyeColor.CYAN, AbstractBlock.Settings.copy(whiteMushroom));
	public static final Block purpleMushroom = new BlockModMushroom(DyeColor.PURPLE, AbstractBlock.Settings.copy(whiteMushroom));
	public static final Block blueMushroom = new BlockModMushroom(DyeColor.BLUE, AbstractBlock.Settings.copy(whiteMushroom));
	public static final Block brownMushroom = new BlockModMushroom(DyeColor.BROWN, AbstractBlock.Settings.copy(whiteMushroom));
	public static final Block greenMushroom = new BlockModMushroom(DyeColor.GREEN, AbstractBlock.Settings.copy(whiteMushroom));
	public static final Block redMushroom = new BlockModMushroom(DyeColor.RED, AbstractBlock.Settings.copy(whiteMushroom));
	public static final Block blackMushroom = new BlockModMushroom(DyeColor.BLACK, AbstractBlock.Settings.copy(whiteMushroom));

	public static final Block doubleFlowerWhite = new BlockModDoubleFlower(DyeColor.WHITE, AbstractBlock.Settings.copy(whiteFlower));
	public static final Block doubleFlowerOrange = new BlockModDoubleFlower(DyeColor.ORANGE, AbstractBlock.Settings.copy(doubleFlowerWhite));
	public static final Block doubleFlowerMagenta = new BlockModDoubleFlower(DyeColor.MAGENTA, AbstractBlock.Settings.copy(doubleFlowerWhite));
	public static final Block doubleFlowerLightBlue = new BlockModDoubleFlower(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(doubleFlowerWhite));
	public static final Block doubleFlowerYellow = new BlockModDoubleFlower(DyeColor.YELLOW, AbstractBlock.Settings.copy(doubleFlowerWhite));
	public static final Block doubleFlowerLime = new BlockModDoubleFlower(DyeColor.LIME, AbstractBlock.Settings.copy(doubleFlowerWhite));
	public static final Block doubleFlowerPink = new BlockModDoubleFlower(DyeColor.PINK, AbstractBlock.Settings.copy(doubleFlowerWhite));
	public static final Block doubleFlowerGray = new BlockModDoubleFlower(DyeColor.GRAY, AbstractBlock.Settings.copy(doubleFlowerWhite));
	public static final Block doubleFlowerLightGray = new BlockModDoubleFlower(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(doubleFlowerWhite));
	public static final Block doubleFlowerCyan = new BlockModDoubleFlower(DyeColor.CYAN, AbstractBlock.Settings.copy(doubleFlowerWhite));
	public static final Block doubleFlowerPurple = new BlockModDoubleFlower(DyeColor.PURPLE, AbstractBlock.Settings.copy(doubleFlowerWhite));
	public static final Block doubleFlowerBlue = new BlockModDoubleFlower(DyeColor.BLUE, AbstractBlock.Settings.copy(doubleFlowerWhite));
	public static final Block doubleFlowerBrown = new BlockModDoubleFlower(DyeColor.BROWN, AbstractBlock.Settings.copy(doubleFlowerWhite));
	public static final Block doubleFlowerGreen = new BlockModDoubleFlower(DyeColor.GREEN, AbstractBlock.Settings.copy(doubleFlowerWhite));
	public static final Block doubleFlowerRed = new BlockModDoubleFlower(DyeColor.RED, AbstractBlock.Settings.copy(doubleFlowerWhite));
	public static final Block doubleFlowerBlack = new BlockModDoubleFlower(DyeColor.BLACK, AbstractBlock.Settings.copy(doubleFlowerWhite));

	public static final Block defaultAltar = new BlockAltar(BlockAltar.Variant.DEFAULT, AbstractBlock.Settings.of(Material.STONE).strength(3.5F).sounds(BlockSoundGroup.STONE)
			.luminance(s -> s.get(BlockAltar.FLUID) == IPetalApothecary.State.LAVA ? 15 : 0));
	public static final Block forestAltar = new BlockAltar(BlockAltar.Variant.FOREST, AbstractBlock.Settings.copy(defaultAltar));
	public static final Block plainsAltar = new BlockAltar(BlockAltar.Variant.PLAINS, AbstractBlock.Settings.copy(defaultAltar));
	public static final Block mountainAltar = new BlockAltar(BlockAltar.Variant.MOUNTAIN, AbstractBlock.Settings.copy(defaultAltar));
	public static final Block fungalAltar = new BlockAltar(BlockAltar.Variant.FUNGAL, AbstractBlock.Settings.copy(defaultAltar));
	public static final Block swampAltar = new BlockAltar(BlockAltar.Variant.SWAMP, AbstractBlock.Settings.copy(defaultAltar));
	public static final Block desertAltar = new BlockAltar(BlockAltar.Variant.DESERT, AbstractBlock.Settings.copy(defaultAltar));
	public static final Block taigaAltar = new BlockAltar(BlockAltar.Variant.TAIGA, AbstractBlock.Settings.copy(defaultAltar));
	public static final Block mesaAltar = new BlockAltar(BlockAltar.Variant.MESA, AbstractBlock.Settings.copy(defaultAltar));
	public static final Block mossyAltar = new BlockAltar(BlockAltar.Variant.MOSSY, AbstractBlock.Settings.copy(defaultAltar));

	public static final Block livingrock = new BlockMod(AbstractBlock.Settings.of(Material.STONE).strength(2, 10).sounds(BlockSoundGroup.STONE));
	public static final Block livingrockBrick = new BlockMod(AbstractBlock.Settings.copy(livingrock));
	public static final Block livingrockBrickChiseled = new BlockMod(AbstractBlock.Settings.copy(livingrock));
	public static final Block livingrockBrickCracked = new BlockMod(AbstractBlock.Settings.copy(livingrock));
	public static final Block livingrockBrickMossy = new BlockMod(AbstractBlock.Settings.copy(livingrock));

	// TODO 1.16+ livingwood/dreamwood should support leaves?
	public static final Block livingwood = new BlockMod(AbstractBlock.Settings.of(Material.WOOD).strength(2).sounds(BlockSoundGroup.WOOD));
	public static final Block livingwoodPlanks = new BlockMod(AbstractBlock.Settings.copy(livingwood));
	public static final Block livingwoodPlanksMossy = new BlockMod(AbstractBlock.Settings.copy(livingwood));
	public static final Block livingwoodFramed = new BlockMod(AbstractBlock.Settings.copy(livingwood));
	public static final Block livingwoodPatternFramed = new BlockMod(AbstractBlock.Settings.copy(livingwood));
	public static final Block livingwoodGlimmering = new BlockMod(AbstractBlock.Settings.copy(livingwood).luminance(s -> 12));

	public static final Block dreamwood = new BlockMod(AbstractBlock.Settings.copy(livingwood));
	public static final Block dreamwoodPlanks = new BlockMod(AbstractBlock.Settings.copy(livingwood));
	public static final Block dreamwoodPlanksMossy = new BlockMod(AbstractBlock.Settings.copy(livingwood));
	public static final Block dreamwoodFramed = new BlockMod(AbstractBlock.Settings.copy(livingwood));
	public static final Block dreamwoodPatternFramed = new BlockMod(AbstractBlock.Settings.copy(livingwood));
	public static final Block dreamwoodGlimmering = new BlockMod(AbstractBlock.Settings.copy(livingwoodGlimmering));

	public static final Block manaSpreader = new BlockSpreader(BlockSpreader.Variant.MANA, AbstractBlock.Settings.copy(livingwood).allowsSpawning(NO_SPAWN));
	public static final Block redstoneSpreader = new BlockSpreader(BlockSpreader.Variant.REDSTONE, AbstractBlock.Settings.copy(livingwood).allowsSpawning(NO_SPAWN));
	public static final Block elvenSpreader = new BlockSpreader(BlockSpreader.Variant.ELVEN, AbstractBlock.Settings.copy(livingwood).allowsSpawning(NO_SPAWN));
	public static final Block gaiaSpreader = new BlockSpreader(BlockSpreader.Variant.GAIA, AbstractBlock.Settings.copy(livingwood).allowsSpawning(NO_SPAWN));

	public static final Block manaPool = new BlockPool(BlockPool.Variant.DEFAULT, AbstractBlock.Settings.copy(livingrock));
	public static final Block creativePool = new BlockPool(BlockPool.Variant.CREATIVE, AbstractBlock.Settings.copy(livingrock));
	public static final Block dilutedPool = new BlockPool(BlockPool.Variant.DILUTED, AbstractBlock.Settings.copy(livingrock));
	public static final Block fabulousPool = new BlockPool(BlockPool.Variant.FABULOUS, AbstractBlock.Settings.copy(livingrock));
	public static final Block alchemyCatalyst = new BlockAlchemyCatalyst(AbstractBlock.Settings.copy(livingrock));
	public static final Block conjurationCatalyst = new BlockConjurationCatalyst(AbstractBlock.Settings.copy(livingrock));

	public static final Block manasteelBlock = new BlockMod(AbstractBlock.Settings.of(Material.METAL).strength(3, 10).sounds(BlockSoundGroup.METAL));
	public static final Block terrasteelBlock = new BlockMod(AbstractBlock.Settings.copy(manasteelBlock));
	public static final Block elementiumBlock = new BlockMod(AbstractBlock.Settings.copy(manasteelBlock));
	public static final Block manaDiamondBlock = new BlockMod(AbstractBlock.Settings.copy(manasteelBlock));
	public static final Block dragonstoneBlock = new BlockMod(AbstractBlock.Settings.copy(manasteelBlock));

	public static final Block manaGlass = new BlockModGlass(AbstractBlock.Settings.copy(Blocks.GLASS).luminance(s -> 15).blockVision(NO_SUFFOCATION).suffocates(NO_SUFFOCATION).allowsSpawning(NO_SPAWN));
	public static final Block elfGlass = new BlockModGlass(AbstractBlock.Settings.copy(manaGlass).blockVision(NO_SUFFOCATION).suffocates(NO_SUFFOCATION).allowsSpawning(NO_SPAWN));
	public static final Block bifrost = new BlockBifrost(AbstractBlock.Settings.of(Material.GLASS).strength(-1, 0.3F)
			.luminance(s -> 15).sounds(BlockSoundGroup.GLASS).nonOpaque().blockVision(NO_SUFFOCATION).suffocates(NO_SUFFOCATION).allowsSpawning(NO_SPAWN));
	public static final Block bifrostPerm = new BlockBifrostPerm(AbstractBlock.Settings.of(Material.GLASS).strength(0.3F)
			.luminance(s -> 15).sounds(BlockSoundGroup.GLASS).nonOpaque().blockVision(NO_SUFFOCATION).suffocates(NO_SUFFOCATION).allowsSpawning(NO_SPAWN));

	public static final Block runeAltar = new BlockRuneAltar(AbstractBlock.Settings.copy(livingrock));
	public static final Block enchanter = new BlockEnchanter(AbstractBlock.Settings.of(Material.STONE).strength(3, 5).luminance(s -> 15).sounds(BlockSoundGroup.STONE));
	public static final Block brewery = new BlockBrewery(AbstractBlock.Settings.copy(livingrock));
	public static final Block terraPlate = new BlockTerraPlate(AbstractBlock.Settings.of(Material.METAL).strength(3, 10).sounds(BlockSoundGroup.METAL));
	public static final Block alfPortal = new BlockAlfPortal(AbstractBlock.Settings.of(Material.WOOD).strength(10).sounds(BlockSoundGroup.WOOD)
			.luminance(s -> s.get(BotaniaStateProps.ALFPORTAL_STATE) != AlfPortalState.OFF ? 15 : 0));

	public static final Block manaPylon = new BlockPylon(BlockPylon.Variant.MANA, AbstractBlock.Settings.of(Material.METAL).strength(5.5F).sounds(BlockSoundGroup.METAL).luminance(s -> 7));
	public static final Block naturaPylon = new BlockPylon(BlockPylon.Variant.NATURA, AbstractBlock.Settings.copy(manaPylon));
	public static final Block gaiaPylon = new BlockPylon(BlockPylon.Variant.GAIA, AbstractBlock.Settings.copy(manaPylon));

	public static final Block distributor = new BlockDistributor(AbstractBlock.Settings.of(Material.STONE).strength(2, 10).sounds(BlockSoundGroup.STONE));
	public static final Block manaVoid = new BlockManaVoid(AbstractBlock.Settings.of(Material.STONE).strength(2, 2000).sounds(BlockSoundGroup.STONE));
	public static final Block manaDetector = new BlockManaDetector(AbstractBlock.Settings.copy(livingrock));
	public static final Block pistonRelay = new BlockPistonRelay(AbstractBlock.Settings.of(Material.GOURD).strength(2, 10).sounds(BlockSoundGroup.METAL).allowsSpawning(NO_SPAWN));
	public static final Block turntable = new BlockTurntable(AbstractBlock.Settings.copy(livingwood));
	public static final Block tinyPlanet = new BlockTinyPlanet(AbstractBlock.Settings.of(Material.STONE).strength(20, 100).sounds(BlockSoundGroup.STONE));
	public static final Block wildDrum = new BlockForestDrum(BlockForestDrum.Variant.WILD, AbstractBlock.Settings.copy(livingwood));
	public static final Block gatheringDrum = new BlockForestDrum(BlockForestDrum.Variant.GATHERING, AbstractBlock.Settings.copy(livingwood));
	public static final Block canopyDrum = new BlockForestDrum(BlockForestDrum.Variant.CANOPY, AbstractBlock.Settings.copy(livingwood));
	public static final Block spawnerClaw = new BlockSpawnerClaw(AbstractBlock.Settings.of(Material.METAL).strength(3));
	public static final Block rfGenerator = new BlockRFGenerator(AbstractBlock.Settings.copy(livingrock));
	public static final Block prism = new BlockPrism(AbstractBlock.Settings.copy(elfGlass).noCollision());
	public static final Block pump = new BlockPump(AbstractBlock.Settings.copy(livingrock));
	public static final Block sparkChanger = new BlockSparkChanger(AbstractBlock.Settings.copy(livingrock));
	public static final Block manaBomb = new BlockManaBomb(AbstractBlock.Settings.of(Material.WOOD).strength(12).sounds(BlockSoundGroup.WOOD));
	public static final Block bellows = new BlockBellows(AbstractBlock.Settings.copy(livingwood));

	public static final Block openCrate = new BlockOpenCrate(AbstractBlock.Settings.copy(livingwood));
	public static final Block craftCrate = new BlockCraftyCrate(AbstractBlock.Settings.copy(livingwood));
	public static final Block forestEye = new BlockForestEye(AbstractBlock.Settings.of(Material.METAL).strength(5, 10).sounds(BlockSoundGroup.METAL));
	public static final Block solidVines = new BlockSolidVines(AbstractBlock.Settings.of(Material.REPLACEABLE_PLANT).strength(0.2F).sounds(BlockSoundGroup.GRASS).nonOpaque());
	public static final Block abstrusePlatform = new BlockPlatform(BlockPlatform.Variant.ABSTRUSE, AbstractBlock.Settings.of(Material.WOOD).strength(2, 5).sounds(BlockSoundGroup.WOOD));
	public static final Block spectralPlatform = new BlockPlatform(BlockPlatform.Variant.SPECTRAL, AbstractBlock.Settings.copy(abstrusePlatform));
	public static final Block infrangiblePlatform = new BlockPlatform(BlockPlatform.Variant.INFRANGIBLE, AbstractBlock.Settings.of(Material.WOOD).strength(-1, Float.MAX_VALUE).sounds(BlockSoundGroup.WOOD));
	public static final Block tinyPotato = new BlockTinyPotato(AbstractBlock.Settings.of(Material.WOOL).strength(0.25F));
	public static final Block enderEye = new BlockEnderEye(AbstractBlock.Settings.copy(manasteelBlock));
	public static final Block redStringContainer = new BlockRedStringContainer(AbstractBlock.Settings.copy(livingrock));
	public static final Block redStringDispenser = new BlockRedStringDispenser(AbstractBlock.Settings.copy(livingrock));
	public static final Block redStringFertilizer = new BlockRedStringFertilizer(AbstractBlock.Settings.copy(livingrock));
	public static final Block redStringComparator = new BlockRedStringComparator(AbstractBlock.Settings.copy(livingrock));
	public static final Block redStringRelay = new BlockRedStringRelay(AbstractBlock.Settings.copy(livingrock));
	public static final Block redStringInterceptor = new BlockRedStringInterceptor(AbstractBlock.Settings.copy(livingrock));
	
	public static final Block corporeaIndex = new BlockCorporeaIndex(AbstractBlock.Settings.of(Material.METAL).strength(5.5F).sounds(BlockSoundGroup.METAL).nonOpaque());
	public static final Block corporeaFunnel = new BlockCorporeaFunnel(AbstractBlock.Settings.of(Material.METAL).strength(5.5F).sounds(BlockSoundGroup.METAL));
	public static final Block corporeaInterceptor = new BlockCorporeaInterceptor(AbstractBlock.Settings.of(Material.METAL).strength(5.5F).sounds(BlockSoundGroup.METAL));
	public static final Block corporeaCrystalCube = new BlockCorporeaCrystalCube(AbstractBlock.Settings.copy(corporeaInterceptor));
	public static final Block corporeaRetainer = new BlockCorporeaRetainer(AbstractBlock.Settings.copy(corporeaInterceptor));

	public static final Block corporeaBlock = new BlockMod(AbstractBlock.Settings.of(Material.METAL).strength(5.5F).sounds(BlockSoundGroup.METAL));
	public static final Block corporeaBrick = new BlockMod(AbstractBlock.Settings.copy(corporeaBlock));
	public static final SlabBlock corporeaSlab = new SlabBlock(AbstractBlock.Settings.copy(corporeaBlock));
	public static final StairsBlock corporeaStairs = new BlockModStairs(corporeaBlock.getDefaultState(), AbstractBlock.Settings.copy(corporeaBlock));
	public static final SlabBlock corporeaBrickSlab = new SlabBlock(AbstractBlock.Settings.copy(corporeaBrick));
	public static final StairsBlock corporeaBrickStairs = new BlockModStairs(corporeaBrick.getDefaultState(), AbstractBlock.Settings.copy(corporeaBrick));
	public static final Block corporeaBrickWall = new WallBlock(AbstractBlock.Settings.copy(corporeaBrick));
	
	public static final Block incensePlate = new BlockIncensePlate(AbstractBlock.Settings.copy(livingwood));
	public static final Block hourglass = new BlockHourglass(AbstractBlock.Settings.of(Material.METAL).strength(2).sounds(BlockSoundGroup.METAL));
	public static final Block ghostRail = new BlockGhostRail(AbstractBlock.Settings.copy(Blocks.RAIL));
	public static final Block lightRelayDefault = new BlockLightRelay(LuminizerVariant.DEFAULT, AbstractBlock.Settings.of(Material.GLASS).noCollision());
	public static final Block lightRelayDetector = new BlockLightRelay(LuminizerVariant.DETECTOR, AbstractBlock.Settings.copy(lightRelayDefault));
	public static final Block lightRelayFork = new BlockLightRelay(LuminizerVariant.FORK, AbstractBlock.Settings.copy(lightRelayDefault));
	public static final Block lightRelayToggle = new BlockLightRelay(LuminizerVariant.TOGGLE, AbstractBlock.Settings.copy(lightRelayDefault));
	public static final Block lightLauncher = new BlockLightLauncher(AbstractBlock.Settings.copy(livingwood));
	public static final Block cacophonium = new BlockCacophonium(AbstractBlock.Settings.of(Material.WOOD).strength(0.8F));
	public static final Block cellBlock = new BlockCell(AbstractBlock.Settings.of(Material.GOURD).sounds(BlockSoundGroup.WOOL));
	public static final Block teruTeruBozu = new BlockTeruTeruBozu(AbstractBlock.Settings.of(Material.WOOL));
	public static final Block avatar = new BlockAvatar(AbstractBlock.Settings.copy(livingwood));
	public static final Block fakeAir = new BlockFakeAir(AbstractBlock.Settings.of(Material.STRUCTURE_VOID).air().ticksRandomly());
	public static final Block root = new BlockRoot(AbstractBlock.Settings.of(Material.PLANT).strength(1.2F).sounds(BlockSoundGroup.WOOD));
	public static final Block felPumpkin = new BlockFelPumpkin(AbstractBlock.Settings.copy(Blocks.CARVED_PUMPKIN));
	public static final Block cocoon = new BlockCocoon(AbstractBlock.Settings.of(Material.WOOL).strength(3, 60).sounds(BlockSoundGroup.WOOL));
	public static final Block enchantedSoil = new BlockEnchantedSoil(AbstractBlock.Settings.of(Material.SOLID_ORGANIC).strength(0.6F).sounds(BlockSoundGroup.GRASS));
	public static final Block animatedTorch = new BlockAnimatedTorch(AbstractBlock.Settings.of(Material.SUPPORTED).luminance(s -> 7).nonOpaque());
	public static final Block starfield = new BlockStarfield(AbstractBlock.Settings.of(Material.METAL).strength(5, 2000).sounds(BlockSoundGroup.METAL));

	public static final Block azulejo0 = new BlockMod(AbstractBlock.Settings.of(Material.STONE).strength(2, 5).sounds(BlockSoundGroup.STONE));
	public static final Block azulejo1 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block azulejo2 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block azulejo3 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block azulejo4 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block azulejo5 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block azulejo6 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block azulejo7 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block azulejo8 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block azulejo9 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block azulejo10 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block azulejo11 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block azulejo12 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block azulejo13 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block azulejo14 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block azulejo15 = new BlockMod(AbstractBlock.Settings.copy(azulejo0));
	public static final Block manaFlame = new BlockManaFlame(AbstractBlock.Settings.of(Material.SUPPORTED).sounds(BlockSoundGroup.WOOL).luminance(s -> 15).noCollision());
	public static final Block blazeBlock = new BlockMod(AbstractBlock.Settings.copy(manasteelBlock).luminance(s -> 15));
	public static final Block gaiaHeadWall = new BlockGaiaHeadWall(AbstractBlock.Settings.of(Material.SUPPORTED).strength(1));
	public static final Block gaiaHead = new BlockGaiaHead(AbstractBlock.Settings.of(Material.SUPPORTED).strength(1));
	public static final Block shimmerrock = new BlockMod(AbstractBlock.Settings.copy(livingrock));
	public static final Block shimmerwoodPlanks = new BlockMod(AbstractBlock.Settings.copy(livingwood));
	public static final Block dryGrass = new BlockAltGrass(BlockAltGrass.Variant.DRY, FabricBlockSettings.copyOf(AbstractBlock.Settings.of(Material.SOLID_ORGANIC).strength(0.6F).ticksRandomly().sounds(BlockSoundGroup.GRASS)).breakByTool(FabricToolTags.SHOVELS));
	public static final Block goldenGrass = new BlockAltGrass(BlockAltGrass.Variant.GOLDEN, AbstractBlock.Settings.copy(dryGrass));
	public static final Block vividGrass = new BlockAltGrass(BlockAltGrass.Variant.VIVID, AbstractBlock.Settings.copy(dryGrass));
	public static final Block scorchedGrass = new BlockAltGrass(BlockAltGrass.Variant.SCORCHED, AbstractBlock.Settings.copy(dryGrass));
	public static final Block infusedGrass = new BlockAltGrass(BlockAltGrass.Variant.INFUSED, AbstractBlock.Settings.copy(dryGrass));
	public static final Block mutatedGrass = new BlockAltGrass(BlockAltGrass.Variant.MUTATED, AbstractBlock.Settings.copy(dryGrass));

	public static final Block motifDaybloom = new BlockMotifFlower(StatusEffects.BLINDNESS, 15, AbstractBlock.Settings.copy(Blocks.POPPY), true);
	public static final Block motifNightshade = new BlockMotifFlower(StatusEffects.POISON, 20, AbstractBlock.Settings.copy(Blocks.POPPY), true);
	public static final Block motifHydroangeas = new BlockMotifFlower(StatusEffects.UNLUCK, 10, AbstractBlock.Settings.copy(Blocks.POPPY), false);

	public static void registerBlocks() {
		Registry<Block> r = Registry.BLOCK;
		register(r, "white" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, whiteFlower);
		register(r, "orange" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, orangeFlower);
		register(r, "magenta" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, magentaFlower);
		register(r, "light_blue" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, lightBlueFlower);
		register(r, "yellow" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, yellowFlower);
		register(r, "lime" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, limeFlower);
		register(r, "pink" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, pinkFlower);
		register(r, "gray" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, grayFlower);
		register(r, "light_gray" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, lightGrayFlower);
		register(r, "cyan" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, cyanFlower);
		register(r, "purple" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, purpleFlower);
		register(r, "blue" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, blueFlower);
		register(r, "brown" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, brownFlower);
		register(r, "green" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, greenFlower);
		register(r, "red" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, redFlower);
		register(r, "black" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, blackFlower);
		register(r, "white" + LibBlockNames.SHINY_FLOWER_SUFFIX, whiteShinyFlower);
		register(r, "orange" + LibBlockNames.SHINY_FLOWER_SUFFIX, orangeShinyFlower);
		register(r, "magenta" + LibBlockNames.SHINY_FLOWER_SUFFIX, magentaShinyFlower);
		register(r, "light_blue" + LibBlockNames.SHINY_FLOWER_SUFFIX, lightBlueShinyFlower);
		register(r, "yellow" + LibBlockNames.SHINY_FLOWER_SUFFIX, yellowShinyFlower);
		register(r, "lime" + LibBlockNames.SHINY_FLOWER_SUFFIX, limeShinyFlower);
		register(r, "pink" + LibBlockNames.SHINY_FLOWER_SUFFIX, pinkShinyFlower);
		register(r, "gray" + LibBlockNames.SHINY_FLOWER_SUFFIX, grayShinyFlower);
		register(r, "light_gray" + LibBlockNames.SHINY_FLOWER_SUFFIX, lightGrayShinyFlower);
		register(r, "cyan" + LibBlockNames.SHINY_FLOWER_SUFFIX, cyanShinyFlower);
		register(r, "purple" + LibBlockNames.SHINY_FLOWER_SUFFIX, purpleShinyFlower);
		register(r, "blue" + LibBlockNames.SHINY_FLOWER_SUFFIX, blueShinyFlower);
		register(r, "brown" + LibBlockNames.SHINY_FLOWER_SUFFIX, brownShinyFlower);
		register(r, "green" + LibBlockNames.SHINY_FLOWER_SUFFIX, greenShinyFlower);
		register(r, "red" + LibBlockNames.SHINY_FLOWER_SUFFIX, redShinyFlower);
		register(r, "black" + LibBlockNames.SHINY_FLOWER_SUFFIX, blackShinyFlower);
		register(r, "white" + LibBlockNames.BURIED_PETALS_SUFFIX, whiteBuriedPetals);
		register(r, "orange" + LibBlockNames.BURIED_PETALS_SUFFIX, orangeBuriedPetals);
		register(r, "magenta" + LibBlockNames.BURIED_PETALS_SUFFIX, magentaBuriedPetals);
		register(r, "light_blue" + LibBlockNames.BURIED_PETALS_SUFFIX, lightBlueBuriedPetals);
		register(r, "yellow" + LibBlockNames.BURIED_PETALS_SUFFIX, yellowBuriedPetals);
		register(r, "lime" + LibBlockNames.BURIED_PETALS_SUFFIX, limeBuriedPetals);
		register(r, "pink" + LibBlockNames.BURIED_PETALS_SUFFIX, pinkBuriedPetals);
		register(r, "gray" + LibBlockNames.BURIED_PETALS_SUFFIX, grayBuriedPetals);
		register(r, "light_gray" + LibBlockNames.BURIED_PETALS_SUFFIX, lightGrayBuriedPetals);
		register(r, "cyan" + LibBlockNames.BURIED_PETALS_SUFFIX, cyanBuriedPetals);
		register(r, "purple" + LibBlockNames.BURIED_PETALS_SUFFIX, purpleBuriedPetals);
		register(r, "blue" + LibBlockNames.BURIED_PETALS_SUFFIX, blueBuriedPetals);
		register(r, "brown" + LibBlockNames.BURIED_PETALS_SUFFIX, brownBuriedPetals);
		register(r, "green" + LibBlockNames.BURIED_PETALS_SUFFIX, greenBuriedPetals);
		register(r, "red" + LibBlockNames.BURIED_PETALS_SUFFIX, redBuriedPetals);
		register(r, "black" + LibBlockNames.BURIED_PETALS_SUFFIX, blackBuriedPetals);
		register(r, "white" + LibBlockNames.FLOATING_FLOWER_SUFFIX, whiteFloatingFlower);
		register(r, "orange" + LibBlockNames.FLOATING_FLOWER_SUFFIX, orangeFloatingFlower);
		register(r, "magenta" + LibBlockNames.FLOATING_FLOWER_SUFFIX, magentaFloatingFlower);
		register(r, "light_blue" + LibBlockNames.FLOATING_FLOWER_SUFFIX, lightBlueFloatingFlower);
		register(r, "yellow" + LibBlockNames.FLOATING_FLOWER_SUFFIX, yellowFloatingFlower);
		register(r, "lime" + LibBlockNames.FLOATING_FLOWER_SUFFIX, limeFloatingFlower);
		register(r, "pink" + LibBlockNames.FLOATING_FLOWER_SUFFIX, pinkFloatingFlower);
		register(r, "gray" + LibBlockNames.FLOATING_FLOWER_SUFFIX, grayFloatingFlower);
		register(r, "light_gray" + LibBlockNames.FLOATING_FLOWER_SUFFIX, lightGrayFloatingFlower);
		register(r, "cyan" + LibBlockNames.FLOATING_FLOWER_SUFFIX, cyanFloatingFlower);
		register(r, "purple" + LibBlockNames.FLOATING_FLOWER_SUFFIX, purpleFloatingFlower);
		register(r, "blue" + LibBlockNames.FLOATING_FLOWER_SUFFIX, blueFloatingFlower);
		register(r, "brown" + LibBlockNames.FLOATING_FLOWER_SUFFIX, brownFloatingFlower);
		register(r, "green" + LibBlockNames.FLOATING_FLOWER_SUFFIX, greenFloatingFlower);
		register(r, "red" + LibBlockNames.FLOATING_FLOWER_SUFFIX, redFloatingFlower);
		register(r, "black" + LibBlockNames.FLOATING_FLOWER_SUFFIX, blackFloatingFlower);
		register(r, "white" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockWhite);
		register(r, "orange" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockOrange);
		register(r, "magenta" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockMagenta);
		register(r, "light_blue" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockLightBlue);
		register(r, "yellow" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockYellow);
		register(r, "lime" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockLime);
		register(r, "pink" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockPink);
		register(r, "gray" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockGray);
		register(r, "light_gray" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockSilver);
		register(r, "cyan" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockCyan);
		register(r, "purple" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockPurple);
		register(r, "blue" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockBlue);
		register(r, "brown" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockBrown);
		register(r, "green" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockGreen);
		register(r, "red" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockRed);
		register(r, "black" + LibBlockNames.PETAL_BLOCK_SUFFIX, petalBlockBlack);
		register(r, "white" + LibBlockNames.MUSHROOM_SUFFIX, whiteMushroom);
		register(r, "orange" + LibBlockNames.MUSHROOM_SUFFIX, orangeMushroom);
		register(r, "magenta" + LibBlockNames.MUSHROOM_SUFFIX, magentaMushroom);
		register(r, "light_blue" + LibBlockNames.MUSHROOM_SUFFIX, lightBlueMushroom);
		register(r, "yellow" + LibBlockNames.MUSHROOM_SUFFIX, yellowMushroom);
		register(r, "lime" + LibBlockNames.MUSHROOM_SUFFIX, limeMushroom);
		register(r, "pink" + LibBlockNames.MUSHROOM_SUFFIX, pinkMushroom);
		register(r, "gray" + LibBlockNames.MUSHROOM_SUFFIX, grayMushroom);
		register(r, "light_gray" + LibBlockNames.MUSHROOM_SUFFIX, lightGrayMushroom);
		register(r, "cyan" + LibBlockNames.MUSHROOM_SUFFIX, cyanMushroom);
		register(r, "purple" + LibBlockNames.MUSHROOM_SUFFIX, purpleMushroom);
		register(r, "blue" + LibBlockNames.MUSHROOM_SUFFIX, blueMushroom);
		register(r, "brown" + LibBlockNames.MUSHROOM_SUFFIX, brownMushroom);
		register(r, "green" + LibBlockNames.MUSHROOM_SUFFIX, greenMushroom);
		register(r, "red" + LibBlockNames.MUSHROOM_SUFFIX, redMushroom);
		register(r, "black" + LibBlockNames.MUSHROOM_SUFFIX, blackMushroom);
		register(r, "white" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerWhite);
		register(r, "orange" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerOrange);
		register(r, "magenta" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerMagenta);
		register(r, "light_blue" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerLightBlue);
		register(r, "yellow" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerYellow);
		register(r, "lime" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerLime);
		register(r, "pink" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerPink);
		register(r, "gray" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerGray);
		register(r, "light_gray" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerLightGray);
		register(r, "cyan" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerCyan);
		register(r, "purple" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerPurple);
		register(r, "blue" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerBlue);
		register(r, "brown" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerBrown);
		register(r, "green" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerGreen);
		register(r, "red" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerRed);
		register(r, "black" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, doubleFlowerBlack);
		register(r, LibBlockNames.APOTHECARY_PREFIX + BlockAltar.Variant.DEFAULT.name().toLowerCase(Locale.ROOT), defaultAltar);
		register(r, LibBlockNames.APOTHECARY_PREFIX + BlockAltar.Variant.FOREST.name().toLowerCase(Locale.ROOT), forestAltar);
		register(r, LibBlockNames.APOTHECARY_PREFIX + BlockAltar.Variant.PLAINS.name().toLowerCase(Locale.ROOT), plainsAltar);
		register(r, LibBlockNames.APOTHECARY_PREFIX + BlockAltar.Variant.MOUNTAIN.name().toLowerCase(Locale.ROOT), mountainAltar);
		register(r, LibBlockNames.APOTHECARY_PREFIX + BlockAltar.Variant.FUNGAL.name().toLowerCase(Locale.ROOT), fungalAltar);
		register(r, LibBlockNames.APOTHECARY_PREFIX + BlockAltar.Variant.SWAMP.name().toLowerCase(Locale.ROOT), swampAltar);
		register(r, LibBlockNames.APOTHECARY_PREFIX + BlockAltar.Variant.DESERT.name().toLowerCase(Locale.ROOT), desertAltar);
		register(r, LibBlockNames.APOTHECARY_PREFIX + BlockAltar.Variant.TAIGA.name().toLowerCase(Locale.ROOT), taigaAltar);
		register(r, LibBlockNames.APOTHECARY_PREFIX + BlockAltar.Variant.MESA.name().toLowerCase(Locale.ROOT), mesaAltar);
		register(r, LibBlockNames.APOTHECARY_PREFIX + BlockAltar.Variant.MOSSY.name().toLowerCase(Locale.ROOT), mossyAltar);
		register(r, LibBlockNames.LIVING_ROCK, livingrock);
		register(r, LibBlockNames.LIVING_ROCK_BRICK, livingrockBrick);
		register(r, LibBlockNames.LIVING_ROCK_BRICK_CHISELED, livingrockBrickChiseled);
		register(r, LibBlockNames.LIVING_ROCK_BRICK_CRACKED, livingrockBrickCracked);
		register(r, LibBlockNames.LIVING_ROCK_BRICK_MOSSY, livingrockBrickMossy);
		register(r, LibBlockNames.LIVING_WOOD, livingwood);
		register(r, LibBlockNames.LIVING_WOOD_PLANKS, livingwoodPlanks);
		register(r, LibBlockNames.LIVING_WOOD_PLANKS_MOSSY, livingwoodPlanksMossy);
		register(r, LibBlockNames.LIVING_WOOD_FRAMED, livingwoodFramed);
		register(r, LibBlockNames.LIVING_WOOD_PATTERN_FRAMED, livingwoodPatternFramed);
		register(r, LibBlockNames.LIVING_WOOD_GLIMMERING, livingwoodGlimmering);
		register(r, LibBlockNames.DREAM_WOOD, dreamwood);
		register(r, LibBlockNames.DREAM_WOOD_PLANKS, dreamwoodPlanks);
		register(r, LibBlockNames.DREAM_WOOD_PLANKS_MOSSY, dreamwoodPlanksMossy);
		register(r, LibBlockNames.DREAM_WOOD_FRAMED, dreamwoodFramed);
		register(r, LibBlockNames.DREAM_WOOD_PATTERN_FRAMED, dreamwoodPatternFramed);
		register(r, LibBlockNames.DREAM_WOOD_GLIMMERING, dreamwoodGlimmering);
		register(r, LibBlockNames.SPREADER, manaSpreader);
		register(r, LibBlockNames.SPREADER_REDSTONE, redstoneSpreader);
		register(r, LibBlockNames.SPREADER_ELVEN, elvenSpreader);
		register(r, LibBlockNames.SPREADER_GAIA, gaiaSpreader);
		register(r, LibBlockNames.POOL, manaPool);
		register(r, LibBlockNames.POOL_CREATIVE, creativePool);
		register(r, LibBlockNames.POOL_DILUTED, dilutedPool);
		register(r, LibBlockNames.POOL_FABULOUS, fabulousPool);
		register(r, LibBlockNames.ALCHEMY_CATALYST, alchemyCatalyst);
		register(r, LibBlockNames.CONJURATION_CATALYST, conjurationCatalyst);
		register(r, LibBlockNames.MANASTEEL_BLOCK, manasteelBlock);
		register(r, LibBlockNames.TERRASTEEL_BLOCK, terrasteelBlock);
		register(r, LibBlockNames.ELEMENTIUM_BLOCK, elementiumBlock);
		register(r, LibBlockNames.MANA_DIAMOND_BLOCK, manaDiamondBlock);
		register(r, LibBlockNames.DRAGONSTONE_BLOCK, dragonstoneBlock);
		register(r, LibBlockNames.MANA_GLASS, manaGlass);
		register(r, LibBlockNames.ELF_GLASS, elfGlass);
		register(r, LibBlockNames.BIFROST, bifrost);
		register(r, LibBlockNames.BIFROST_PERM, bifrostPerm);
		register(r, LibBlockNames.RUNE_ALTAR, runeAltar);
		register(r, LibBlockNames.ENCHANTER, enchanter);
		register(r, LibBlockNames.BREWERY, brewery);
		register(r, LibBlockNames.TERRA_PLATE, terraPlate);
		register(r, LibBlockNames.ALF_PORTAL, alfPortal);
		register(r, LibBlockNames.PYLON, manaPylon);
		register(r, LibBlockNames.PYLON_NATURA, naturaPylon);
		register(r, LibBlockNames.PYLON_GAIA, gaiaPylon);
		register(r, LibBlockNames.DISTRIBUTOR, distributor);
		register(r, LibBlockNames.MANA_VOID, manaVoid);
		register(r, LibBlockNames.MANA_DETECTOR, manaDetector);
		register(r, LibBlockNames.PISTON_RELAY, pistonRelay);
		register(r, LibBlockNames.TURNTABLE, turntable);
		register(r, LibBlockNames.TINY_PLANET, tinyPlanet);
		register(r, LibBlockNames.DRUM_WILD, wildDrum);
		register(r, LibBlockNames.DRUM_GATHERING, gatheringDrum);
		register(r, LibBlockNames.DRUM_CANOPY, canopyDrum);
		register(r, LibBlockNames.SPAWNER_CLAW, spawnerClaw);
		register(r, LibBlockNames.FLUXFIELD, rfGenerator);
		register(r, LibBlockNames.PRISM, prism);
		register(r, LibBlockNames.PUMP, pump);
		register(r, LibBlockNames.SPARK_CHANGER, sparkChanger);
		register(r, LibBlockNames.MANA_BOMB, manaBomb);
		register(r, LibBlockNames.BELLOWS, bellows);
		register(r, LibBlockNames.OPEN_CRATE, openCrate);
		register(r, LibBlockNames.CRAFT_CRATE, craftCrate);
		register(r, LibBlockNames.FOREST_EYE, forestEye);
		register(r, LibBlockNames.SOLID_VINE, solidVines);
		register(r, LibBlockNames.PLATFORM_ABSTRUSE, abstrusePlatform);
		register(r, LibBlockNames.PLATFORM_SPECTRAL, spectralPlatform);
		register(r, LibBlockNames.PLATFORM_INFRANGIBLE, infrangiblePlatform);
		register(r, LibBlockNames.TINY_POTATO, tinyPotato);
		register(r, LibBlockNames.ENDER_EYE_BLOCK, enderEye);
		register(r, LibBlockNames.RED_STRING_CONTAINER, redStringContainer);
		register(r, LibBlockNames.RED_STRING_DISPENSER, redStringDispenser);
		register(r, LibBlockNames.RED_STRING_FERTILIZER, redStringFertilizer);
		register(r, LibBlockNames.RED_STRING_COMPARATOR, redStringComparator);
		register(r, LibBlockNames.RED_STRING_RELAY, redStringRelay);
		register(r, LibBlockNames.RED_STRING_INTERCEPTOR, redStringInterceptor);
		register(r, LibBlockNames.CORPOREA_INDEX, corporeaIndex);
		register(r, LibBlockNames.CORPOREA_FUNNEL, corporeaFunnel);
		register(r, LibBlockNames.CORPOREA_INTERCEPTOR, corporeaInterceptor);
		register(r, LibBlockNames.CORPOREA_CRYSTAL_CUBE, corporeaCrystalCube);
		register(r, LibBlockNames.CORPOREA_RETAINER, corporeaRetainer);
		register(r, LibBlockNames.CORPOREA_BLOCK, corporeaBlock);
		register(r, LibBlockNames.CORPOREA_SLAB, corporeaSlab);
		register(r, LibBlockNames.CORPOREA_STAIRS, corporeaStairs);
		register(r, LibBlockNames.CORPOREA_BRICK, corporeaBrick);
		register(r, LibBlockNames.CORPOREA_BRICK + LibBlockNames.SLAB_SUFFIX, corporeaBrickSlab);
		register(r, LibBlockNames.CORPOREA_BRICK + LibBlockNames.STAIR_SUFFIX, corporeaBrickStairs);
		register(r, LibBlockNames.CORPOREA_BRICK + LibBlockNames.WALL_SUFFIX, corporeaBrickWall);
		register(r, LibBlockNames.INCENSE_PLATE, incensePlate);
		register(r, LibBlockNames.HOURGLASS, hourglass);
		register(r, LibBlockNames.GHOST_RAIL, ghostRail);
		register(r, LibBlockNames.LIGHT_RELAY, lightRelayDefault);
		register(r, "detector" + LibBlockNames.LIGHT_RELAY_SUFFIX, lightRelayDetector);
		register(r, "fork" + LibBlockNames.LIGHT_RELAY_SUFFIX, lightRelayFork);
		register(r, "toggle" + LibBlockNames.LIGHT_RELAY_SUFFIX, lightRelayToggle);
		register(r, LibBlockNames.LIGHT_LAUNCHER, lightLauncher);
		register(r, LibBlockNames.CACOPHONIUM, cacophonium);
		register(r, LibBlockNames.CELL_BLOCK, cellBlock);
		register(r, LibBlockNames.TERU_TERU_BOZU, teruTeruBozu);
		register(r, LibBlockNames.AVATAR, avatar);
		register(r, LibBlockNames.FAKE_AIR, fakeAir);
		register(r, LibBlockNames.ROOT, root);
		register(r, LibBlockNames.FEL_PUMPKIN, felPumpkin);
		register(r, LibBlockNames.COCOON, cocoon);
		register(r, LibBlockNames.ENCHANTED_SOIL, enchantedSoil);
		register(r, LibBlockNames.ANIMATED_TORCH, animatedTorch);
		register(r, LibBlockNames.STARFIELD, starfield);
		register(r, LibBlockNames.AZULEJO_PREFIX + 0, azulejo0);
		register(r, LibBlockNames.AZULEJO_PREFIX + 1, azulejo1);
		register(r, LibBlockNames.AZULEJO_PREFIX + 2, azulejo2);
		register(r, LibBlockNames.AZULEJO_PREFIX + 3, azulejo3);
		register(r, LibBlockNames.AZULEJO_PREFIX + 4, azulejo4);
		register(r, LibBlockNames.AZULEJO_PREFIX + 5, azulejo5);
		register(r, LibBlockNames.AZULEJO_PREFIX + 6, azulejo6);
		register(r, LibBlockNames.AZULEJO_PREFIX + 7, azulejo7);
		register(r, LibBlockNames.AZULEJO_PREFIX + 8, azulejo8);
		register(r, LibBlockNames.AZULEJO_PREFIX + 9, azulejo9);
		register(r, LibBlockNames.AZULEJO_PREFIX + 10, azulejo10);
		register(r, LibBlockNames.AZULEJO_PREFIX + 11, azulejo11);
		register(r, LibBlockNames.AZULEJO_PREFIX + 12, azulejo12);
		register(r, LibBlockNames.AZULEJO_PREFIX + 13, azulejo13);
		register(r, LibBlockNames.AZULEJO_PREFIX + 14, azulejo14);
		register(r, LibBlockNames.AZULEJO_PREFIX + 15, azulejo15);
		register(r, LibBlockNames.MANA_FLAME, manaFlame);
		register(r, LibBlockNames.BLAZE_BLOCK, blazeBlock);
		register(r, LibBlockNames.GAIA_WALL_HEAD, gaiaHeadWall);
		register(r, LibBlockNames.GAIA_HEAD, gaiaHead);
		register(r, LibBlockNames.SHIMMERROCK, shimmerrock);
		register(r, LibBlockNames.SHIMMERWOOD_PLANKS, shimmerwoodPlanks);
		register(r, BlockAltGrass.Variant.DRY.name().toLowerCase(Locale.ROOT) + LibBlockNames.ALT_GRASS_SUFFIX, dryGrass);
		register(r, BlockAltGrass.Variant.GOLDEN.name().toLowerCase(Locale.ROOT) + LibBlockNames.ALT_GRASS_SUFFIX, goldenGrass);
		register(r, BlockAltGrass.Variant.VIVID.name().toLowerCase(Locale.ROOT) + LibBlockNames.ALT_GRASS_SUFFIX, vividGrass);
		register(r, BlockAltGrass.Variant.SCORCHED.name().toLowerCase(Locale.ROOT) + LibBlockNames.ALT_GRASS_SUFFIX, scorchedGrass);
		register(r, BlockAltGrass.Variant.INFUSED.name().toLowerCase(Locale.ROOT) + LibBlockNames.ALT_GRASS_SUFFIX, infusedGrass);
		register(r, BlockAltGrass.Variant.MUTATED.name().toLowerCase(Locale.ROOT) + LibBlockNames.ALT_GRASS_SUFFIX, mutatedGrass);
		register(r, LibBlockNames.MOTIF_DAYBLOOM, motifDaybloom);
		register(r, LibBlockNames.MOTIF_NIGHTSHADE, motifNightshade);
		register(r, LibBlockNames.MOTIF_HYDROANGEAS, motifHydroangeas);
	}

	public static void registerItemBlocks() {
		Registry<Item> r = Registry.ITEM;
		Item.Settings props = ModItems.defaultBuilder();
		register(r, Registry.BLOCK.getId(whiteFlower), new BlockItem(whiteFlower, props));
		register(r, Registry.BLOCK.getId(orangeFlower), new BlockItem(orangeFlower, props));
		register(r, Registry.BLOCK.getId(magentaFlower), new BlockItem(magentaFlower, props));
		register(r, Registry.BLOCK.getId(lightBlueFlower), new BlockItem(lightBlueFlower, props));
		register(r, Registry.BLOCK.getId(yellowFlower), new BlockItem(yellowFlower, props));
		register(r, Registry.BLOCK.getId(limeFlower), new BlockItem(limeFlower, props));
		register(r, Registry.BLOCK.getId(pinkFlower), new BlockItem(pinkFlower, props));
		register(r, Registry.BLOCK.getId(grayFlower), new BlockItem(grayFlower, props));
		register(r, Registry.BLOCK.getId(lightGrayFlower), new BlockItem(lightGrayFlower, props));
		register(r, Registry.BLOCK.getId(cyanFlower), new BlockItem(cyanFlower, props));
		register(r, Registry.BLOCK.getId(purpleFlower), new BlockItem(purpleFlower, props));
		register(r, Registry.BLOCK.getId(blueFlower), new BlockItem(blueFlower, props));
		register(r, Registry.BLOCK.getId(brownFlower), new BlockItem(brownFlower, props));
		register(r, Registry.BLOCK.getId(greenFlower), new BlockItem(greenFlower, props));
		register(r, Registry.BLOCK.getId(redFlower), new BlockItem(redFlower, props));
		register(r, Registry.BLOCK.getId(blackFlower), new BlockItem(blackFlower, props));
		register(r, Registry.BLOCK.getId(whiteShinyFlower), new BlockItem(whiteShinyFlower, props));
		register(r, Registry.BLOCK.getId(orangeShinyFlower), new BlockItem(orangeShinyFlower, props));
		register(r, Registry.BLOCK.getId(magentaShinyFlower), new BlockItem(magentaShinyFlower, props));
		register(r, Registry.BLOCK.getId(lightBlueShinyFlower), new BlockItem(lightBlueShinyFlower, props));
		register(r, Registry.BLOCK.getId(yellowShinyFlower), new BlockItem(yellowShinyFlower, props));
		register(r, Registry.BLOCK.getId(limeShinyFlower), new BlockItem(limeShinyFlower, props));
		register(r, Registry.BLOCK.getId(pinkShinyFlower), new BlockItem(pinkShinyFlower, props));
		register(r, Registry.BLOCK.getId(grayShinyFlower), new BlockItem(grayShinyFlower, props));
		register(r, Registry.BLOCK.getId(lightGrayShinyFlower), new BlockItem(lightGrayShinyFlower, props));
		register(r, Registry.BLOCK.getId(cyanShinyFlower), new BlockItem(cyanShinyFlower, props));
		register(r, Registry.BLOCK.getId(purpleShinyFlower), new BlockItem(purpleShinyFlower, props));
		register(r, Registry.BLOCK.getId(blueShinyFlower), new BlockItem(blueShinyFlower, props));
		register(r, Registry.BLOCK.getId(brownShinyFlower), new BlockItem(brownShinyFlower, props));
		register(r, Registry.BLOCK.getId(greenShinyFlower), new BlockItem(greenShinyFlower, props));
		register(r, Registry.BLOCK.getId(redShinyFlower), new BlockItem(redShinyFlower, props));
		register(r, Registry.BLOCK.getId(blackShinyFlower), new BlockItem(blackShinyFlower, props));
		register(r, Registry.BLOCK.getId(whiteFloatingFlower), new BlockItem(whiteFloatingFlower, props));
		register(r, Registry.BLOCK.getId(orangeFloatingFlower), new BlockItem(orangeFloatingFlower, props));
		register(r, Registry.BLOCK.getId(magentaFloatingFlower), new BlockItem(magentaFloatingFlower, props));
		register(r, Registry.BLOCK.getId(lightBlueFloatingFlower), new BlockItem(lightBlueFloatingFlower, props));
		register(r, Registry.BLOCK.getId(yellowFloatingFlower), new BlockItem(yellowFloatingFlower, props));
		register(r, Registry.BLOCK.getId(limeFloatingFlower), new BlockItem(limeFloatingFlower, props));
		register(r, Registry.BLOCK.getId(pinkFloatingFlower), new BlockItem(pinkFloatingFlower, props));
		register(r, Registry.BLOCK.getId(grayFloatingFlower), new BlockItem(grayFloatingFlower, props));
		register(r, Registry.BLOCK.getId(lightGrayFloatingFlower), new BlockItem(lightGrayFloatingFlower, props));
		register(r, Registry.BLOCK.getId(cyanFloatingFlower), new BlockItem(cyanFloatingFlower, props));
		register(r, Registry.BLOCK.getId(purpleFloatingFlower), new BlockItem(purpleFloatingFlower, props));
		register(r, Registry.BLOCK.getId(blueFloatingFlower), new BlockItem(blueFloatingFlower, props));
		register(r, Registry.BLOCK.getId(brownFloatingFlower), new BlockItem(brownFloatingFlower, props));
		register(r, Registry.BLOCK.getId(greenFloatingFlower), new BlockItem(greenFloatingFlower, props));
		register(r, Registry.BLOCK.getId(redFloatingFlower), new BlockItem(redFloatingFlower, props));
		register(r, Registry.BLOCK.getId(blackFloatingFlower), new BlockItem(blackFloatingFlower, props));
		register(r, Registry.BLOCK.getId(petalBlockWhite), new BlockItem(petalBlockWhite, props));
		register(r, Registry.BLOCK.getId(petalBlockOrange), new BlockItem(petalBlockOrange, props));
		register(r, Registry.BLOCK.getId(petalBlockMagenta), new BlockItem(petalBlockMagenta, props));
		register(r, Registry.BLOCK.getId(petalBlockLightBlue), new BlockItem(petalBlockLightBlue, props));
		register(r, Registry.BLOCK.getId(petalBlockYellow), new BlockItem(petalBlockYellow, props));
		register(r, Registry.BLOCK.getId(petalBlockLime), new BlockItem(petalBlockLime, props));
		register(r, Registry.BLOCK.getId(petalBlockPink), new BlockItem(petalBlockPink, props));
		register(r, Registry.BLOCK.getId(petalBlockGray), new BlockItem(petalBlockGray, props));
		register(r, Registry.BLOCK.getId(petalBlockSilver), new BlockItem(petalBlockSilver, props));
		register(r, Registry.BLOCK.getId(petalBlockCyan), new BlockItem(petalBlockCyan, props));
		register(r, Registry.BLOCK.getId(petalBlockPurple), new BlockItem(petalBlockPurple, props));
		register(r, Registry.BLOCK.getId(petalBlockBlue), new BlockItem(petalBlockBlue, props));
		register(r, Registry.BLOCK.getId(petalBlockBrown), new BlockItem(petalBlockBrown, props));
		register(r, Registry.BLOCK.getId(petalBlockGreen), new BlockItem(petalBlockGreen, props));
		register(r, Registry.BLOCK.getId(petalBlockRed), new BlockItem(petalBlockRed, props));
		register(r, Registry.BLOCK.getId(petalBlockBlack), new BlockItem(petalBlockBlack, props));
		register(r, Registry.BLOCK.getId(whiteMushroom), new BlockItem(whiteMushroom, props));
		register(r, Registry.BLOCK.getId(orangeMushroom), new BlockItem(orangeMushroom, props));
		register(r, Registry.BLOCK.getId(magentaMushroom), new BlockItem(magentaMushroom, props));
		register(r, Registry.BLOCK.getId(lightBlueMushroom), new BlockItem(lightBlueMushroom, props));
		register(r, Registry.BLOCK.getId(yellowMushroom), new BlockItem(yellowMushroom, props));
		register(r, Registry.BLOCK.getId(limeMushroom), new BlockItem(limeMushroom, props));
		register(r, Registry.BLOCK.getId(pinkMushroom), new BlockItem(pinkMushroom, props));
		register(r, Registry.BLOCK.getId(grayMushroom), new BlockItem(grayMushroom, props));
		register(r, Registry.BLOCK.getId(lightGrayMushroom), new BlockItem(lightGrayMushroom, props));
		register(r, Registry.BLOCK.getId(cyanMushroom), new BlockItem(cyanMushroom, props));
		register(r, Registry.BLOCK.getId(purpleMushroom), new BlockItem(purpleMushroom, props));
		register(r, Registry.BLOCK.getId(blueMushroom), new BlockItem(blueMushroom, props));
		register(r, Registry.BLOCK.getId(brownMushroom), new BlockItem(brownMushroom, props));
		register(r, Registry.BLOCK.getId(greenMushroom), new BlockItem(greenMushroom, props));
		register(r, Registry.BLOCK.getId(redMushroom), new BlockItem(redMushroom, props));
		register(r, Registry.BLOCK.getId(blackMushroom), new BlockItem(blackMushroom, props));
		register(r, Registry.BLOCK.getId(doubleFlowerWhite), new BlockItem(doubleFlowerWhite, props));
		register(r, Registry.BLOCK.getId(doubleFlowerOrange), new BlockItem(doubleFlowerOrange, props));
		register(r, Registry.BLOCK.getId(doubleFlowerMagenta), new BlockItem(doubleFlowerMagenta, props));
		register(r, Registry.BLOCK.getId(doubleFlowerLightBlue), new BlockItem(doubleFlowerLightBlue, props));
		register(r, Registry.BLOCK.getId(doubleFlowerYellow), new BlockItem(doubleFlowerYellow, props));
		register(r, Registry.BLOCK.getId(doubleFlowerLime), new BlockItem(doubleFlowerLime, props));
		register(r, Registry.BLOCK.getId(doubleFlowerPink), new BlockItem(doubleFlowerPink, props));
		register(r, Registry.BLOCK.getId(doubleFlowerGray), new BlockItem(doubleFlowerGray, props));
		register(r, Registry.BLOCK.getId(doubleFlowerLightGray), new BlockItem(doubleFlowerLightGray, props));
		register(r, Registry.BLOCK.getId(doubleFlowerCyan), new BlockItem(doubleFlowerCyan, props));
		register(r, Registry.BLOCK.getId(doubleFlowerPurple), new BlockItem(doubleFlowerPurple, props));
		register(r, Registry.BLOCK.getId(doubleFlowerBlue), new BlockItem(doubleFlowerBlue, props));
		register(r, Registry.BLOCK.getId(doubleFlowerBrown), new BlockItem(doubleFlowerBrown, props));
		register(r, Registry.BLOCK.getId(doubleFlowerGreen), new BlockItem(doubleFlowerGreen, props));
		register(r, Registry.BLOCK.getId(doubleFlowerRed), new BlockItem(doubleFlowerRed, props));
		register(r, Registry.BLOCK.getId(doubleFlowerBlack), new BlockItem(doubleFlowerBlack, props));
		register(r, Registry.BLOCK.getId(defaultAltar), new BlockItem(defaultAltar, props));
		register(r, Registry.BLOCK.getId(forestAltar), new BlockItem(forestAltar, props));
		register(r, Registry.BLOCK.getId(plainsAltar), new BlockItem(plainsAltar, props));
		register(r, Registry.BLOCK.getId(mountainAltar), new BlockItem(mountainAltar, props));
		register(r, Registry.BLOCK.getId(fungalAltar), new BlockItem(fungalAltar, props));
		register(r, Registry.BLOCK.getId(swampAltar), new BlockItem(swampAltar, props));
		register(r, Registry.BLOCK.getId(desertAltar), new BlockItem(desertAltar, props));
		register(r, Registry.BLOCK.getId(taigaAltar), new BlockItem(taigaAltar, props));
		register(r, Registry.BLOCK.getId(mesaAltar), new BlockItem(mesaAltar, props));
		register(r, Registry.BLOCK.getId(mossyAltar), new BlockItem(mossyAltar, props));
		register(r, Registry.BLOCK.getId(livingrock), new BlockItem(livingrock, props));
		register(r, Registry.BLOCK.getId(livingrockBrick), new BlockItem(livingrockBrick, props));
		register(r, Registry.BLOCK.getId(livingrockBrickChiseled), new BlockItem(livingrockBrickChiseled, props));
		register(r, Registry.BLOCK.getId(livingrockBrickCracked), new BlockItem(livingrockBrickCracked, props));
		register(r, Registry.BLOCK.getId(livingrockBrickMossy), new BlockItem(livingrockBrickMossy, props));
		register(r, Registry.BLOCK.getId(livingwood), new BlockItem(livingwood, props));
		register(r, Registry.BLOCK.getId(livingwoodPlanks), new BlockItem(livingwoodPlanks, props));
		register(r, Registry.BLOCK.getId(livingwoodPlanksMossy), new BlockItem(livingwoodPlanksMossy, props));
		register(r, Registry.BLOCK.getId(livingwoodFramed), new BlockItem(livingwoodFramed, props));
		register(r, Registry.BLOCK.getId(livingwoodPatternFramed), new BlockItem(livingwoodPatternFramed, props));
		register(r, Registry.BLOCK.getId(livingwoodGlimmering), new BlockItem(livingwoodGlimmering, props));
		register(r, Registry.BLOCK.getId(dreamwood), new ItemBlockDreamwood(dreamwood, props));
		register(r, Registry.BLOCK.getId(dreamwoodPlanks), new ItemBlockDreamwood(dreamwoodPlanks, props));
		register(r, Registry.BLOCK.getId(dreamwoodPlanksMossy), new ItemBlockDreamwood(dreamwoodPlanksMossy, props));
		register(r, Registry.BLOCK.getId(dreamwoodFramed), new ItemBlockDreamwood(dreamwoodFramed, props));
		register(r, Registry.BLOCK.getId(dreamwoodPatternFramed), new ItemBlockDreamwood(dreamwoodPatternFramed, props));
		register(r, Registry.BLOCK.getId(dreamwoodGlimmering), new ItemBlockDreamwood(dreamwoodGlimmering, props));
		register(r, Registry.BLOCK.getId(manaSpreader), new BlockItem(manaSpreader, props));
		register(r, Registry.BLOCK.getId(redstoneSpreader), new BlockItem(redstoneSpreader, props));
		register(r, Registry.BLOCK.getId(elvenSpreader), new BlockItem(elvenSpreader, props));
		register(r, Registry.BLOCK.getId(gaiaSpreader), new BlockItem(gaiaSpreader, props));
		register(r, Registry.BLOCK.getId(manaPool), new ItemBlockPool(manaPool, props));
		register(r, Registry.BLOCK.getId(creativePool), new ItemBlockPool(creativePool, ModItems.defaultBuilder().rarity(Rarity.EPIC)));
		register(r, Registry.BLOCK.getId(dilutedPool), new ItemBlockPool(dilutedPool, props));
		register(r, Registry.BLOCK.getId(fabulousPool), new ItemBlockPool(fabulousPool, props));
		register(r, Registry.BLOCK.getId(alchemyCatalyst), new BlockItem(alchemyCatalyst, props));
		register(r, Registry.BLOCK.getId(conjurationCatalyst), new BlockItem(conjurationCatalyst, props));
		register(r, Registry.BLOCK.getId(manasteelBlock), new BlockItem(manasteelBlock, props));
		register(r, Registry.BLOCK.getId(terrasteelBlock), new BlockItem(terrasteelBlock, ModItems.defaultBuilder().rarity(Rarity.UNCOMMON)));
		register(r, Registry.BLOCK.getId(elementiumBlock), new ItemBlockElven(elementiumBlock, props));
		register(r, Registry.BLOCK.getId(manaDiamondBlock), new BlockItem(manaDiamondBlock, props));
		register(r, Registry.BLOCK.getId(dragonstoneBlock), new BlockItem(dragonstoneBlock, props));
		register(r, Registry.BLOCK.getId(manaGlass), new BlockItem(manaGlass, props));
		register(r, Registry.BLOCK.getId(elfGlass), new ItemBlockElven(elfGlass, props));
		register(r, Registry.BLOCK.getId(bifrostPerm), new BlockItem(bifrostPerm, props));
		register(r, Registry.BLOCK.getId(runeAltar), new BlockItem(runeAltar, props));
		register(r, Registry.BLOCK.getId(enchanter), new BlockItem(enchanter, props));
		register(r, Registry.BLOCK.getId(brewery), new BlockItem(brewery, props));
		register(r, Registry.BLOCK.getId(terraPlate), new BlockItem(terraPlate, props));
		register(r, Registry.BLOCK.getId(alfPortal), new BlockItem(alfPortal, ModItems.defaultBuilder().rarity(Rarity.UNCOMMON)));

		register(r, Registry.BLOCK.getId(manaPylon), new BlockItem(manaPylon, props));
		register(r, Registry.BLOCK.getId(naturaPylon), new BlockItem(naturaPylon, props));
		register(r, Registry.BLOCK.getId(gaiaPylon), new BlockItem(gaiaPylon, props));
		register(r, Registry.BLOCK.getId(distributor), new BlockItem(distributor, props));
		register(r, Registry.BLOCK.getId(manaVoid), new BlockItem(manaVoid, props));
		register(r, Registry.BLOCK.getId(manaDetector), new BlockItem(manaDetector, props));
		register(r, Registry.BLOCK.getId(pistonRelay), new BlockItem(pistonRelay, props));
		register(r, Registry.BLOCK.getId(turntable), new BlockItem(turntable, props));
		register(r, Registry.BLOCK.getId(tinyPlanet), new BlockItem(tinyPlanet, props));
		register(r, Registry.BLOCK.getId(wildDrum), new BlockItem(wildDrum, props));
		register(r, Registry.BLOCK.getId(gatheringDrum), new BlockItem(gatheringDrum, props));
		register(r, Registry.BLOCK.getId(canopyDrum), new BlockItem(canopyDrum, props));
		register(r, Registry.BLOCK.getId(spawnerClaw), new BlockItem(spawnerClaw, props));
		register(r, Registry.BLOCK.getId(rfGenerator), new BlockItem(rfGenerator, props));
		register(r, Registry.BLOCK.getId(prism), new BlockItem(prism, props));
		register(r, Registry.BLOCK.getId(pump), new BlockItem(pump, props));
		register(r, Registry.BLOCK.getId(sparkChanger), new BlockItem(sparkChanger, props));
		register(r, Registry.BLOCK.getId(manaBomb), new BlockItem(manaBomb, props));
		register(r, Registry.BLOCK.getId(bellows), new BlockItem(bellows, props));
		register(r, Registry.BLOCK.getId(openCrate), new BlockItem(openCrate, props));
		register(r, Registry.BLOCK.getId(craftCrate), new BlockItem(craftCrate, props));
		register(r, Registry.BLOCK.getId(forestEye), new BlockItem(forestEye, props));
		register(r, Registry.BLOCK.getId(abstrusePlatform), new BlockItem(abstrusePlatform, props));
		register(r, Registry.BLOCK.getId(spectralPlatform), new BlockItem(spectralPlatform, props));
		register(r, Registry.BLOCK.getId(infrangiblePlatform), new BlockItem(infrangiblePlatform, ModItems.defaultBuilder().rarity(Rarity.EPIC)));
		register(r, Registry.BLOCK.getId(tinyPotato), new ItemBlockTinyPotato(tinyPotato, ModItems.defaultBuilder().rarity(Rarity.UNCOMMON)));
		register(r, Registry.BLOCK.getId(enderEye), new BlockItem(enderEye, props));
		register(r, Registry.BLOCK.getId(redStringContainer), new BlockItem(redStringContainer, props));
		register(r, Registry.BLOCK.getId(redStringDispenser), new BlockItem(redStringDispenser, props));
		register(r, Registry.BLOCK.getId(redStringFertilizer), new BlockItem(redStringFertilizer, props));
		register(r, Registry.BLOCK.getId(redStringComparator), new BlockItem(redStringComparator, props));
		register(r, Registry.BLOCK.getId(redStringRelay), new BlockItem(redStringRelay, props));
		register(r, Registry.BLOCK.getId(redStringInterceptor), new BlockItem(redStringInterceptor, props));
		register(r, Registry.BLOCK.getId(corporeaIndex), new BlockItem(corporeaIndex, props));
		register(r, Registry.BLOCK.getId(corporeaFunnel), new BlockItem(corporeaFunnel, props));
		register(r, Registry.BLOCK.getId(corporeaInterceptor), new BlockItem(corporeaInterceptor, props));
		register(r, Registry.BLOCK.getId(corporeaCrystalCube), new BlockItem(corporeaCrystalCube, props));
		register(r, Registry.BLOCK.getId(corporeaRetainer), new BlockItem(corporeaRetainer, props));
		register(r, Registry.BLOCK.getId(corporeaBlock), new BlockItem(corporeaBlock, props));
		register(r, Registry.BLOCK.getId(corporeaSlab), new BlockItem(corporeaSlab, props));
		register(r, Registry.BLOCK.getId(corporeaStairs), new BlockItem(corporeaStairs, props));
		register(r, Registry.BLOCK.getId(corporeaBrick), new BlockItem(corporeaBrick, props));
		register(r, Registry.BLOCK.getId(corporeaBrickSlab), new BlockItem(corporeaBrickSlab, props));
		register(r, Registry.BLOCK.getId(corporeaBrickStairs), new BlockItem(corporeaBrickStairs, props));
		register(r, Registry.BLOCK.getId(corporeaBrickWall), new BlockItem(corporeaBrickWall, props));
		register(r, Registry.BLOCK.getId(incensePlate), new BlockItem(incensePlate, props));
		register(r, Registry.BLOCK.getId(hourglass), new BlockItem(hourglass, props));
		register(r, Registry.BLOCK.getId(ghostRail), new BlockItem(ghostRail, props));
		register(r, Registry.BLOCK.getId(lightRelayDefault), new BlockItem(lightRelayDefault, props));
		register(r, Registry.BLOCK.getId(lightRelayDetector), new BlockItem(lightRelayDetector, props));
		register(r, Registry.BLOCK.getId(lightRelayFork), new BlockItem(lightRelayFork, props));
		register(r, Registry.BLOCK.getId(lightRelayToggle), new BlockItem(lightRelayToggle, props));
		register(r, Registry.BLOCK.getId(lightLauncher), new BlockItem(lightLauncher, props));
		register(r, Registry.BLOCK.getId(cacophonium), new BlockItem(cacophonium, props));
		register(r, Registry.BLOCK.getId(cellBlock), new BlockItem(cellBlock, props));
		register(r, Registry.BLOCK.getId(teruTeruBozu), new BlockItem(teruTeruBozu, props));
		register(r, Registry.BLOCK.getId(avatar), new BlockItem(avatar, props));
		register(r, Registry.BLOCK.getId(root), new BlockItem(root, props));
		register(r, Registry.BLOCK.getId(felPumpkin), new BlockItem(felPumpkin, props));
		register(r, Registry.BLOCK.getId(cocoon), new BlockItem(cocoon, props));
		register(r, Registry.BLOCK.getId(enchantedSoil), new BlockItem(enchantedSoil, ModItems.defaultBuilder().rarity(Rarity.RARE)));
		register(r, Registry.BLOCK.getId(animatedTorch), new BlockItem(animatedTorch, props));
		register(r, Registry.BLOCK.getId(starfield), new BlockItem(starfield, props));
		register(r, Registry.BLOCK.getId(azulejo0), new BlockItem(azulejo0, props));
		register(r, Registry.BLOCK.getId(azulejo1), new BlockItem(azulejo1, props));
		register(r, Registry.BLOCK.getId(azulejo2), new BlockItem(azulejo2, props));
		register(r, Registry.BLOCK.getId(azulejo3), new BlockItem(azulejo3, props));
		register(r, Registry.BLOCK.getId(azulejo4), new BlockItem(azulejo4, props));
		register(r, Registry.BLOCK.getId(azulejo5), new BlockItem(azulejo5, props));
		register(r, Registry.BLOCK.getId(azulejo6), new BlockItem(azulejo6, props));
		register(r, Registry.BLOCK.getId(azulejo7), new BlockItem(azulejo7, props));
		register(r, Registry.BLOCK.getId(azulejo8), new BlockItem(azulejo8, props));
		register(r, Registry.BLOCK.getId(azulejo9), new BlockItem(azulejo9, props));
		register(r, Registry.BLOCK.getId(azulejo10), new BlockItem(azulejo10, props));
		register(r, Registry.BLOCK.getId(azulejo11), new BlockItem(azulejo11, props));
		register(r, Registry.BLOCK.getId(azulejo12), new BlockItem(azulejo12, props));
		register(r, Registry.BLOCK.getId(azulejo13), new BlockItem(azulejo13, props));
		register(r, Registry.BLOCK.getId(azulejo14), new BlockItem(azulejo14, props));
		register(r, Registry.BLOCK.getId(azulejo15), new BlockItem(azulejo15, props));
		Item blazeBlockItem = new ItemBlockBlaze(blazeBlock, props);
		register(r, Registry.BLOCK.getId(blazeBlock), blazeBlockItem);
		int blazeTime = FuelRegistry.INSTANCE.get(Items.BLAZE_ROD);
		FuelRegistry.INSTANCE.add(blazeBlockItem, blazeTime * (Botania.gardenOfGlassLoaded ? 5 : 10));
		register(r, Registry.BLOCK.getId(gaiaHead), new WallStandingBlockItem(gaiaHead, gaiaHeadWall, ModItems.defaultBuilder().rarity(Rarity.UNCOMMON)));
		register(r, Registry.BLOCK.getId(shimmerrock), new BlockItem(shimmerrock, props));
		register(r, Registry.BLOCK.getId(shimmerwoodPlanks), new BlockItem(shimmerwoodPlanks, props));
		register(r, Registry.BLOCK.getId(dryGrass), new BlockItem(dryGrass, props));
		register(r, Registry.BLOCK.getId(goldenGrass), new BlockItem(goldenGrass, props));
		register(r, Registry.BLOCK.getId(vividGrass), new BlockItem(vividGrass, props));
		register(r, Registry.BLOCK.getId(scorchedGrass), new BlockItem(scorchedGrass, props));
		register(r, Registry.BLOCK.getId(infusedGrass), new BlockItem(infusedGrass, props));
		register(r, Registry.BLOCK.getId(mutatedGrass), new BlockItem(mutatedGrass, props));
		register(r, Registry.BLOCK.getId(motifDaybloom), new BlockItem(motifDaybloom, props));
		register(r, Registry.BLOCK.getId(motifNightshade), new BlockItem(motifNightshade, props));
		register(r, Registry.BLOCK.getId(motifHydroangeas), new BlockItem(motifHydroangeas, props));
	}

	public static <T> void register(Registry<? super T> reg, Identifier name, T thing) {
		Registry.register(reg, name, thing);
	}

	public static <T> void register(Registry<? super T> reg, String name, T thing) {
		register(reg, prefix(name), thing);
	}

	public static void addDispenserBehaviours() {
		DispenserBlock.registerBehavior(ModItems.twigWand, new BehaviourWand());
		DispenserBlock.registerBehavior(ModItems.obedienceStick, new BehaviourStick());
		DispenserBlock.registerBehavior(ModItems.poolMinecart, new BehaviourPoolMinecart());
		DispenserBlock.registerBehavior(ModBlocks.felPumpkin, new BehaviourFelPumpkin());
		DispenserBlock.registerBehavior(ModItems.spark, new BehaviourSpark());
		DispenserBlock.registerBehavior(ModBlocks.gaiaHead, new FallibleItemDispenserBehavior() {
			@Nonnull
			@Override
			protected ItemStack dispenseSilently(@Nonnull BlockPointer source, @Nonnull ItemStack stack) {
				setSuccess(ArmorItem.dispenseArmor(source, stack));
				return stack;
			}
		});

		DispenserBehavior behavior = new BehaviourCorporeaSpark();
		DispenserBlock.registerBehavior(ModItems.corporeaSpark, behavior);
		DispenserBlock.registerBehavior(ModItems.corporeaSparkMaster, behavior);

		behavior = AccessorDispenserBlock.getDispenseBehaviorRegistry().get(Items.GLASS_BOTTLE);
		DispenserBlock.registerBehavior(Items.GLASS_BOTTLE, new BehaviourEnderAirBottling(behavior));

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
