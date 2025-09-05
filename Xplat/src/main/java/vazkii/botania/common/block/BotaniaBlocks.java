/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.*;
import net.minecraft.core.dispenser.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.PetalApothecary;
import vazkii.botania.api.internal.Colored;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.corporea.*;
import vazkii.botania.common.block.decor.*;
import vazkii.botania.common.block.dispenser.*;
import vazkii.botania.common.block.mana.*;
import vazkii.botania.common.block.red_string.*;
import vazkii.botania.common.brew.BotaniaMobEffects;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.block.ColoredBlockItem;
import vazkii.botania.common.item.block.SpecialFlowerBlockItem;
import vazkii.botania.common.item.block.TinyPotatoBlockItem;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.mixin.DispenserBlockAccessor;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;
import static vazkii.botania.common.lib.LibBlockNames.*;

public final class BotaniaBlocks {
	private static final Map<String, Block> ALL = new LinkedHashMap<>(); // Preserve insertion order

	private static final BlockBehaviour.StateArgumentPredicate<EntityType<?>> NO_SPAWN = (state, world, pos, et) -> false;
	private static final BlockBehaviour.StatePredicate NEVER = (state, world, pos) -> false;

	public static final Block whiteFlower = make("white" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.WHITE,
			effectForFlower(DyeColor.WHITE), 4, BotaniaBlocks::getDoubleFlower,
			BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak()
					.offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS)
	));
	public static final Block orangeFlower = make("orange" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.ORANGE,
			effectForFlower(DyeColor.ORANGE), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block magentaFlower = make("magenta" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.MAGENTA,
			effectForFlower(DyeColor.MAGENTA), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block lightBlueFlower = make("light_blue" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.LIGHT_BLUE,
			effectForFlower(DyeColor.LIGHT_BLUE), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block yellowFlower = make("yellow" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.YELLOW,
			effectForFlower(DyeColor.YELLOW), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block limeFlower = make("lime" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.LIME,
			effectForFlower(DyeColor.LIME), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block pinkFlower = make("pink" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.PINK,
			effectForFlower(DyeColor.PINK), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block grayFlower = make("gray" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.GRAY,
			effectForFlower(DyeColor.GRAY), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block lightGrayFlower = make("light_gray" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.LIGHT_GRAY,
			effectForFlower(DyeColor.LIGHT_GRAY), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block cyanFlower = make("cyan" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.CYAN,
			effectForFlower(DyeColor.CYAN), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block purpleFlower = make("purple" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.PURPLE,
			effectForFlower(DyeColor.PURPLE), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block blueFlower = make("blue" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.BLUE,
			effectForFlower(DyeColor.BLUE), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block brownFlower = make("brown" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.BROWN,
			effectForFlower(DyeColor.BROWN), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block greenFlower = make("green" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.GREEN,
			effectForFlower(DyeColor.GREEN), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block redFlower = make("red" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.RED,
			effectForFlower(DyeColor.RED), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block blackFlower = make("black" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, new MysticalFlowerBlock(DyeColor.BLACK,
			effectForFlower(DyeColor.BLACK), 4, BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));

	public static final Block whiteShinyFlower = make("white" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.WHITE,
			effectForFlower(DyeColor.WHITE), 6, BlockBehaviour.Properties.ofFullCopy(whiteFlower).lightLevel(s -> 15)));
	public static final Block orangeShinyFlower = make("orange" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.ORANGE,
			effectForFlower(DyeColor.ORANGE), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));
	public static final Block magentaShinyFlower = make("magenta" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.MAGENTA,
			effectForFlower(DyeColor.MAGENTA), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));
	public static final Block lightBlueShinyFlower = make("light_blue" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.LIGHT_BLUE,
			effectForFlower(DyeColor.LIGHT_BLUE), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));
	public static final Block yellowShinyFlower = make("yellow" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.YELLOW,
			effectForFlower(DyeColor.YELLOW), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));
	public static final Block limeShinyFlower = make("lime" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.LIME,
			effectForFlower(DyeColor.LIME), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));
	public static final Block pinkShinyFlower = make("pink" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.PINK,
			effectForFlower(DyeColor.PINK), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));
	public static final Block grayShinyFlower = make("gray" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.GRAY,
			effectForFlower(DyeColor.GRAY), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));
	public static final Block lightGrayShinyFlower = make("light_gray" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.LIGHT_GRAY,
			effectForFlower(DyeColor.LIGHT_GRAY), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));
	public static final Block cyanShinyFlower = make("cyan" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.CYAN,
			effectForFlower(DyeColor.CYAN), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));
	public static final Block purpleShinyFlower = make("purple" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.PURPLE,
			effectForFlower(DyeColor.PURPLE), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));
	public static final Block blueShinyFlower = make("blue" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.BLUE,
			effectForFlower(DyeColor.BLUE), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));
	public static final Block brownShinyFlower = make("brown" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.BROWN,
			effectForFlower(DyeColor.BROWN), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));
	public static final Block greenShinyFlower = make("green" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.GREEN,
			effectForFlower(DyeColor.GREEN), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));
	public static final Block redShinyFlower = make("red" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.RED,
			effectForFlower(DyeColor.RED), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));
	public static final Block blackShinyFlower = make("black" + LibBlockNames.SHINY_FLOWER_SUFFIX, new GlimmeringFlowerBlock(DyeColor.BLACK,
			effectForFlower(DyeColor.BLACK), 6, BlockBehaviour.Properties.ofFullCopy(whiteShinyFlower)));

	public static final Block whiteBuriedPetals = make("white" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.WHITE, BotaniaBlocks::getDoubleFlower,
			BlockBehaviour.Properties.ofFullCopy(whiteFlower).sound(SoundType.MOSS).lightLevel(s -> 4)));
	public static final Block orangeBuriedPetals = make("orange" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.ORANGE,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));
	public static final Block magentaBuriedPetals = make("magenta" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.MAGENTA,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));
	public static final Block lightBlueBuriedPetals = make("light_blue" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.LIGHT_BLUE,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));
	public static final Block yellowBuriedPetals = make("yellow" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.YELLOW,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));
	public static final Block limeBuriedPetals = make("lime" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.LIME,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));
	public static final Block pinkBuriedPetals = make("pink" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.PINK,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));
	public static final Block grayBuriedPetals = make("gray" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.GRAY,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));
	public static final Block lightGrayBuriedPetals = make("light_gray" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.LIGHT_GRAY,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));
	public static final Block cyanBuriedPetals = make("cyan" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.CYAN,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));
	public static final Block purpleBuriedPetals = make("purple" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.PURPLE,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));
	public static final Block blueBuriedPetals = make("blue" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.BLUE,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));
	public static final Block brownBuriedPetals = make("brown" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.BROWN,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));
	public static final Block greenBuriedPetals = make("green" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.GREEN,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));
	public static final Block redBuriedPetals = make("red" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.RED,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));
	public static final Block blackBuriedPetals = make("black" + LibBlockNames.BURIED_PETALS_SUFFIX, new BuriedPetalBlock(DyeColor.BLACK,
			BotaniaBlocks::getDoubleFlower, BlockBehaviour.Properties.ofFullCopy(whiteBuriedPetals)));

	public static final BlockBehaviour.Properties FLOATING_PROPS = BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).strength(0.5F).sound(SoundType.GRAVEL).lightLevel(s -> 15);
	public static final Block whiteFloatingFlower = make("white" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.WHITE, FLOATING_PROPS));
	public static final Block orangeFloatingFlower = make("orange" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.ORANGE, FLOATING_PROPS));
	public static final Block magentaFloatingFlower = make("magenta" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.MAGENTA, FLOATING_PROPS));
	public static final Block lightBlueFloatingFlower = make("light_blue" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.LIGHT_BLUE, FLOATING_PROPS));
	public static final Block yellowFloatingFlower = make("yellow" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.YELLOW, FLOATING_PROPS));
	public static final Block limeFloatingFlower = make("lime" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.LIME, FLOATING_PROPS));
	public static final Block pinkFloatingFlower = make("pink" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.PINK, FLOATING_PROPS));
	public static final Block grayFloatingFlower = make("gray" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.GRAY, FLOATING_PROPS));
	public static final Block lightGrayFloatingFlower = make("light_gray" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.LIGHT_GRAY, FLOATING_PROPS));
	public static final Block cyanFloatingFlower = make("cyan" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.CYAN, FLOATING_PROPS));
	public static final Block purpleFloatingFlower = make("purple" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.PURPLE, FLOATING_PROPS));
	public static final Block blueFloatingFlower = make("blue" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.BLUE, FLOATING_PROPS));
	public static final Block brownFloatingFlower = make("brown" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.BROWN, FLOATING_PROPS));
	public static final Block greenFloatingFlower = make("green" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.GREEN, FLOATING_PROPS));
	public static final Block redFloatingFlower = make("red" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.RED, FLOATING_PROPS));
	public static final Block blackFloatingFlower = make("black" + LibBlockNames.FLOATING_FLOWER_SUFFIX, new FloatingFlowerBlock(DyeColor.BLACK, FLOATING_PROPS));

	public static final Block petalBlockWhite = make("white" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.WHITE, BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).strength(0.4F).sound(SoundType.MOSS)));
	public static final Block petalBlockOrange = make("orange" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.ORANGE, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.ORANGE)));
	public static final Block petalBlockMagenta = make("magenta" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.MAGENTA, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.MAGENTA)));
	public static final Block petalBlockLightBlue = make("light_blue" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.LIGHT_BLUE, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.LIGHT_BLUE)));
	public static final Block petalBlockYellow = make("yellow" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.YELLOW, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.YELLOW)));
	public static final Block petalBlockLime = make("lime" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.LIME, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.LIME)));
	public static final Block petalBlockPink = make("pink" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.PINK, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.PINK)));
	public static final Block petalBlockGray = make("gray" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.GRAY, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.GRAY)));
	public static final Block petalBlockSilver = make("light_gray" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.LIGHT_GRAY, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.LIGHT_GRAY)));
	public static final Block petalBlockCyan = make("cyan" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.CYAN, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.CYAN)));
	public static final Block petalBlockPurple = make("purple" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.PURPLE, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.PURPLE)));
	public static final Block petalBlockBlue = make("blue" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.BLUE, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.BLUE)));
	public static final Block petalBlockBrown = make("brown" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.BROWN, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.BROWN)));
	public static final Block petalBlockGreen = make("green" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.GREEN, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.GREEN)));
	public static final Block petalBlockRed = make("red" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.RED, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.RED)));
	public static final Block petalBlockBlack = make("black" + LibBlockNames.PETAL_BLOCK_SUFFIX, new PetalBlock(DyeColor.BLACK, BlockBehaviour.Properties.ofFullCopy(petalBlockWhite).mapColor(DyeColor.BLACK)));

	public static final Block whiteMushroom = make("white" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.WHITE, BlockBehaviour.Properties.ofFullCopy(whiteFlower).lightLevel(s -> 3).offsetType(BlockBehaviour.OffsetType.NONE)));
	public static final Block orangeMushroom = make("orange" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.ORANGE, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));
	public static final Block magentaMushroom = make("magenta" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.MAGENTA, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));
	public static final Block lightBlueMushroom = make("light_blue" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.LIGHT_BLUE, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));
	public static final Block yellowMushroom = make("yellow" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.YELLOW, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));
	public static final Block limeMushroom = make("lime" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.LIME, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));
	public static final Block pinkMushroom = make("pink" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.PINK, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));
	public static final Block grayMushroom = make("gray" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.GRAY, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));
	public static final Block lightGrayMushroom = make("light_gray" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.LIGHT_GRAY, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));
	public static final Block cyanMushroom = make("cyan" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.CYAN, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));
	public static final Block purpleMushroom = make("purple" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.PURPLE, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));
	public static final Block blueMushroom = make("blue" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.BLUE, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));
	public static final Block brownMushroom = make("brown" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.BROWN, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));
	public static final Block greenMushroom = make("green" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.GREEN, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));
	public static final Block redMushroom = make("red" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.RED, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));
	public static final Block blackMushroom = make("black" + LibBlockNames.MUSHROOM_SUFFIX, new BotaniaMushroomBlock(DyeColor.BLACK, BlockBehaviour.Properties.ofFullCopy(whiteMushroom)));

	public static final Block doubleFlowerWhite = make("white" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.WHITE, BlockBehaviour.Properties.ofFullCopy(whiteFlower)));
	public static final Block doubleFlowerOrange = make("orange" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.ORANGE, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));
	public static final Block doubleFlowerMagenta = make("magenta" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.MAGENTA, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));
	public static final Block doubleFlowerLightBlue = make("light_blue" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.LIGHT_BLUE, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));
	public static final Block doubleFlowerYellow = make("yellow" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.YELLOW, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));
	public static final Block doubleFlowerLime = make("lime" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.LIME, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));
	public static final Block doubleFlowerPink = make("pink" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.PINK, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));
	public static final Block doubleFlowerGray = make("gray" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.GRAY, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));
	public static final Block doubleFlowerLightGray = make("light_gray" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.LIGHT_GRAY, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));
	public static final Block doubleFlowerCyan = make("cyan" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.CYAN, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));
	public static final Block doubleFlowerPurple = make("purple" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.PURPLE, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));
	public static final Block doubleFlowerBlue = make("blue" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.BLUE, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));
	public static final Block doubleFlowerBrown = make("brown" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.BROWN, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));
	public static final Block doubleFlowerGreen = make("green" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.GREEN, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));
	public static final Block doubleFlowerRed = make("red" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.RED, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));
	public static final Block doubleFlowerBlack = make("black" + LibBlockNames.DOUBLE_FLOWER_SUFFIX, new BotaniaDoubleFlowerBlock(DyeColor.BLACK, BlockBehaviour.Properties.ofFullCopy(doubleFlowerWhite)));

	public static final Block pottedWhiteFlower = make(LibBlockNames.POTTED_PREFIX + "white" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(whiteFlower, 0));
	public static final Block pottedOrangeFlower = make(LibBlockNames.POTTED_PREFIX + "orange" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(orangeFlower, 0));
	public static final Block pottedMagentaFlower = make(LibBlockNames.POTTED_PREFIX + "magenta" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(magentaFlower, 0));
	public static final Block pottedLightBlueFlower = make(LibBlockNames.POTTED_PREFIX + "light_blue" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(lightBlueFlower, 0));
	public static final Block pottedYellowFlower = make(LibBlockNames.POTTED_PREFIX + "yellow" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(yellowFlower, 0));
	public static final Block pottedLimeFlower = make(LibBlockNames.POTTED_PREFIX + "lime" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(limeFlower, 0));
	public static final Block pottedPinkFlower = make(LibBlockNames.POTTED_PREFIX + "pink" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(pinkFlower, 0));
	public static final Block pottedGrayFlower = make(LibBlockNames.POTTED_PREFIX + "gray" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(grayFlower, 0));
	public static final Block pottedLightGrayFlower = make(LibBlockNames.POTTED_PREFIX + "light_gray" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(lightGrayFlower, 0));
	public static final Block pottedCyanFlower = make(LibBlockNames.POTTED_PREFIX + "cyan" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(cyanFlower, 0));
	public static final Block pottedPurpleFlower = make(LibBlockNames.POTTED_PREFIX + "purple" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(purpleFlower, 0));
	public static final Block pottedBlueFlower = make(LibBlockNames.POTTED_PREFIX + "blue" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(blueFlower, 0));
	public static final Block pottedBrownFlower = make(LibBlockNames.POTTED_PREFIX + "brown" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(brownFlower, 0));
	public static final Block pottedGreenFlower = make(LibBlockNames.POTTED_PREFIX + "green" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(greenFlower, 0));
	public static final Block pottedRedFlower = make(LibBlockNames.POTTED_PREFIX + "red" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(redFlower, 0));
	public static final Block pottedBlackFlower = make(LibBlockNames.POTTED_PREFIX + "black" + LibBlockNames.MYSTICAL_FLOWER_SUFFIX, flowerPot(blackFlower, 0));

	public static final Block pottedWhiteShinyFlower = make(LibBlockNames.POTTED_PREFIX + "white" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(whiteShinyFlower, 15));
	public static final Block pottedOrangeShinyFlower = make(LibBlockNames.POTTED_PREFIX + "orange" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(orangeShinyFlower, 15));
	public static final Block pottedMagentaShinyFlower = make(LibBlockNames.POTTED_PREFIX + "magenta" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(magentaShinyFlower, 15));
	public static final Block pottedLightBlueShinyFlower = make(LibBlockNames.POTTED_PREFIX + "light_blue" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(lightBlueShinyFlower, 15));
	public static final Block pottedYellowShinyFlower = make(LibBlockNames.POTTED_PREFIX + "yellow" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(yellowShinyFlower, 15));
	public static final Block pottedLimeShinyFlower = make(LibBlockNames.POTTED_PREFIX + "lime" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(limeShinyFlower, 15));
	public static final Block pottedPinkShinyFlower = make(LibBlockNames.POTTED_PREFIX + "pink" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(pinkShinyFlower, 15));
	public static final Block pottedGrayShinyFlower = make(LibBlockNames.POTTED_PREFIX + "gray" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(grayShinyFlower, 15));
	public static final Block pottedLightGrayShinyFlower = make(LibBlockNames.POTTED_PREFIX + "light_gray" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(lightGrayShinyFlower, 15));
	public static final Block pottedCyanShinyFlower = make(LibBlockNames.POTTED_PREFIX + "cyan" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(cyanShinyFlower, 15));
	public static final Block pottedPurpleShinyFlower = make(LibBlockNames.POTTED_PREFIX + "purple" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(purpleShinyFlower, 15));
	public static final Block pottedBlueShinyFlower = make(LibBlockNames.POTTED_PREFIX + "blue" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(blueShinyFlower, 15));
	public static final Block pottedBrownShinyFlower = make(LibBlockNames.POTTED_PREFIX + "brown" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(brownShinyFlower, 15));
	public static final Block pottedGreenShinyFlower = make(LibBlockNames.POTTED_PREFIX + "green" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(greenShinyFlower, 15));
	public static final Block pottedRedShinyFlower = make(LibBlockNames.POTTED_PREFIX + "red" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(redShinyFlower, 15));
	public static final Block pottedBlackShinyFlower = make(LibBlockNames.POTTED_PREFIX + "black" + LibBlockNames.SHINY_FLOWER_SUFFIX, flowerPot(blackShinyFlower, 15));

	public static final Block pottedWhiteMushroom = make(LibBlockNames.POTTED_PREFIX + "white" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(whiteMushroom, 3));
	public static final Block pottedOrangeMushroom = make(LibBlockNames.POTTED_PREFIX + "orange" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(orangeMushroom, 3));
	public static final Block pottedMagentaMushroom = make(LibBlockNames.POTTED_PREFIX + "magenta" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(magentaMushroom, 3));
	public static final Block pottedLightBlueMushroom = make(LibBlockNames.POTTED_PREFIX + "light_blue" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(lightBlueMushroom, 3));
	public static final Block pottedYellowMushroom = make(LibBlockNames.POTTED_PREFIX + "yellow" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(yellowMushroom, 3));
	public static final Block pottedLimeMushroom = make(LibBlockNames.POTTED_PREFIX + "lime" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(limeMushroom, 3));
	public static final Block pottedPinkMushroom = make(LibBlockNames.POTTED_PREFIX + "pink" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(pinkMushroom, 3));
	public static final Block pottedGrayMushroom = make(LibBlockNames.POTTED_PREFIX + "gray" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(grayMushroom, 3));
	public static final Block pottedLightGrayMushroom = make(LibBlockNames.POTTED_PREFIX + "light_gray" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(lightGrayMushroom, 3));
	public static final Block pottedCyanMushroom = make(LibBlockNames.POTTED_PREFIX + "cyan" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(cyanMushroom, 3));
	public static final Block pottedPurpleMushroom = make(LibBlockNames.POTTED_PREFIX + "purple" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(purpleMushroom, 3));
	public static final Block pottedBlueMushroom = make(LibBlockNames.POTTED_PREFIX + "blue" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(blueMushroom, 3));
	public static final Block pottedBrownMushroom = make(LibBlockNames.POTTED_PREFIX + "brown" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(brownMushroom, 3));
	public static final Block pottedGreenMushroom = make(LibBlockNames.POTTED_PREFIX + "green" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(greenMushroom, 3));
	public static final Block pottedRedMushroom = make(LibBlockNames.POTTED_PREFIX + "red" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(redMushroom, 3));
	public static final Block pottedBlackMushroom = make(LibBlockNames.POTTED_PREFIX + "black" + LibBlockNames.MUSHROOM_SUFFIX, flowerPot(blackMushroom, 3));

	private static final BlockBehaviour.Properties FLOWER_PROPS = BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY);
	public static final Block pureDaisy = make(LibBlockNames.SUBTILE_PUREDAISY, new SpecialFlowerBlock(BotaniaMobEffects.CLEAR, 1, FLOWER_PROPS, () -> BotaniaBlockEntities.PURE_DAISY));
	public static final Block pureDaisyFloating = make(floating(LibBlockNames.SUBTILE_PUREDAISY), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.PURE_DAISY));
	public static final Block pureDaisyPotted = make(potted(LibBlockNames.SUBTILE_PUREDAISY), BotaniaBlocks.flowerPot(pureDaisy, 0));

	public static final Block manastar = make(LibBlockNames.SUBTILE_MANASTAR, new SpecialFlowerBlock(MobEffects.GLOWING, 10, FLOWER_PROPS, () -> BotaniaBlockEntities.MANASTAR));
	public static final Block manastarFloating = make(floating(LibBlockNames.SUBTILE_MANASTAR), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.MANASTAR));
	public static final Block manastarPotted = make(potted(LibBlockNames.SUBTILE_MANASTAR), BotaniaBlocks.flowerPot(manastar, 0));

	public static final Block hydroangeas = make(LibBlockNames.SUBTILE_HYDROANGEAS, new SpecialFlowerBlock(MobEffects.UNLUCK, 10, FLOWER_PROPS, () -> BotaniaBlockEntities.HYDROANGEAS));
	public static final Block hydroangeasFloating = make(floating(LibBlockNames.SUBTILE_HYDROANGEAS), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.HYDROANGEAS));
	public static final Block hydroangeasPotted = make(potted(LibBlockNames.SUBTILE_HYDROANGEAS), BotaniaBlocks.flowerPot(hydroangeas, 0));

	public static final Block endoflame = make(LibBlockNames.SUBTILE_ENDOFLAME, new SpecialFlowerBlock(MobEffects.MOVEMENT_SLOWDOWN, 10, FLOWER_PROPS, () -> BotaniaBlockEntities.ENDOFLAME));
	public static final Block endoflameFloating = make(floating(LibBlockNames.SUBTILE_ENDOFLAME), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.ENDOFLAME));
	public static final Block endoflamePotted = make(potted(LibBlockNames.SUBTILE_ENDOFLAME), BotaniaBlocks.flowerPot(endoflame, 0));

	public static final Block thermalily = make(LibBlockNames.SUBTILE_THERMALILY, new SpecialFlowerBlock(MobEffects.FIRE_RESISTANCE, 120, FLOWER_PROPS, () -> BotaniaBlockEntities.THERMALILY, true));
	public static final Block thermalilyFloating = make(floating(LibBlockNames.SUBTILE_THERMALILY), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.THERMALILY, true));
	public static final Block thermalilyPotted = make(potted(LibBlockNames.SUBTILE_THERMALILY), BotaniaBlocks.flowerPot(thermalily, 0));

	public static final Block rosaArcana = make(LibBlockNames.SUBTILE_ARCANE_ROSE, new SpecialFlowerBlock(MobEffects.LUCK, 64, FLOWER_PROPS, () -> BotaniaBlockEntities.ROSA_ARCANA));
	public static final Block rosaArcanaFloating = make(floating(LibBlockNames.SUBTILE_ARCANE_ROSE), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.ROSA_ARCANA));
	public static final Block rosaArcanaPotted = make(potted(LibBlockNames.SUBTILE_ARCANE_ROSE), BotaniaBlocks.flowerPot(rosaArcana, 0));

	public static final Block munchdew = make(LibBlockNames.SUBTILE_MUNCHDEW, new SpecialFlowerBlock(MobEffects.SLOW_FALLING, 300, FLOWER_PROPS, () -> BotaniaBlockEntities.MUNCHDEW));
	public static final Block munchdewFloating = make(floating(LibBlockNames.SUBTILE_MUNCHDEW), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.MUNCHDEW));
	public static final Block munchdewPotted = make(potted(LibBlockNames.SUBTILE_MUNCHDEW), BotaniaBlocks.flowerPot(munchdew, 0));

	public static final Block entropinnyum = make(LibBlockNames.SUBTILE_ENTROPINNYUM, new SpecialFlowerBlock(MobEffects.DAMAGE_RESISTANCE, 72, FLOWER_PROPS, () -> BotaniaBlockEntities.ENTROPINNYUM));
	public static final Block entropinnyumFloating = make(floating(LibBlockNames.SUBTILE_ENTROPINNYUM), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.ENTROPINNYUM));
	public static final Block entropinnyumPotted = make(potted(LibBlockNames.SUBTILE_ENTROPINNYUM), BotaniaBlocks.flowerPot(entropinnyum, 0));

	public static final Block kekimurus = make(LibBlockNames.SUBTILE_KEKIMURUS, new SpecialFlowerBlock(MobEffects.SATURATION, 15, FLOWER_PROPS, () -> BotaniaBlockEntities.KEKIMURUS));
	public static final Block kekimurusFloating = make(floating(LibBlockNames.SUBTILE_KEKIMURUS), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.KEKIMURUS));
	public static final Block kekimurusPotted = make(potted(LibBlockNames.SUBTILE_KEKIMURUS), BotaniaBlocks.flowerPot(kekimurus, 0));

	public static final Block gourmaryllis = make(LibBlockNames.SUBTILE_GOURMARYLLIS, new SpecialFlowerBlock(MobEffects.HUNGER, 180, FLOWER_PROPS, () -> BotaniaBlockEntities.GOURMARYLLIS));
	public static final Block gourmaryllisFloating = make(floating(LibBlockNames.SUBTILE_GOURMARYLLIS), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.GOURMARYLLIS));
	public static final Block gourmaryllisPotted = make(potted(LibBlockNames.SUBTILE_GOURMARYLLIS), BotaniaBlocks.flowerPot(gourmaryllis, 0));

	public static final Block narslimmus = make(LibBlockNames.SUBTILE_NARSLIMMUS, new SpecialFlowerBlock(BotaniaMobEffects.FEATHER_FEET, 240, FLOWER_PROPS, () -> BotaniaBlockEntities.NARSLIMMUS));
	public static final Block narslimmusFloating = make(floating(LibBlockNames.SUBTILE_NARSLIMMUS), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.NARSLIMMUS));
	public static final Block narslimmusPotted = make(potted(LibBlockNames.SUBTILE_NARSLIMMUS), BotaniaBlocks.flowerPot(narslimmus, 0));

	public static final Block spectrolus = make(LibBlockNames.SUBTILE_SPECTROLUS, new SpecialFlowerBlock(MobEffects.BLINDNESS, 240, FLOWER_PROPS, () -> BotaniaBlockEntities.SPECTROLUS));
	public static final Block spectrolusFloating = make(floating(LibBlockNames.SUBTILE_SPECTROLUS), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.SPECTROLUS));
	public static final Block spectrolusPotted = make(potted(LibBlockNames.SUBTILE_SPECTROLUS), BotaniaBlocks.flowerPot(spectrolus, 0));

	public static final Block dandelifeon = make(LibBlockNames.SUBTILE_DANDELIFEON, new SpecialFlowerBlock(MobEffects.CONFUSION, 240, FLOWER_PROPS, () -> BotaniaBlockEntities.DANDELIFEON));
	public static final Block dandelifeonFloating = make(floating(LibBlockNames.SUBTILE_DANDELIFEON), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.DANDELIFEON));
	public static final Block dandelifeonPotted = make(potted(LibBlockNames.SUBTILE_DANDELIFEON), BotaniaBlocks.flowerPot(dandelifeon, 0));

	public static final Block rafflowsia = make(LibBlockNames.SUBTILE_RAFFLOWSIA, new SpecialFlowerBlock(MobEffects.HEALTH_BOOST, 18, FLOWER_PROPS, () -> BotaniaBlockEntities.RAFFLOWSIA));
	public static final Block rafflowsiaFloating = make(floating(LibBlockNames.SUBTILE_RAFFLOWSIA), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.RAFFLOWSIA));
	public static final Block rafflowsiaPotted = make(potted(LibBlockNames.SUBTILE_RAFFLOWSIA), BotaniaBlocks.flowerPot(rafflowsia, 0));

	public static final Block shulkMeNot = make(LibBlockNames.SUBTILE_SHULK_ME_NOT, new SpecialFlowerBlock(MobEffects.LEVITATION, 72, FLOWER_PROPS, () -> BotaniaBlockEntities.SHULK_ME_NOT));
	public static final Block shulkMeNotFloating = make(floating(LibBlockNames.SUBTILE_SHULK_ME_NOT), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.SHULK_ME_NOT));
	public static final Block shulkMeNotPotted = make(potted(LibBlockNames.SUBTILE_SHULK_ME_NOT), BotaniaBlocks.flowerPot(shulkMeNot, 0));

	public static final Block bellethorn = make(LibBlockNames.SUBTILE_BELLETHORN, new SpecialFlowerBlock(MobEffects.WITHER, 10, FLOWER_PROPS, () -> BotaniaBlockEntities.BELLETHORNE));
	public static final Block bellethornChibi = make(chibi(LibBlockNames.SUBTILE_BELLETHORN), new SpecialFlowerBlock(MobEffects.WITHER, 10, FLOWER_PROPS, () -> BotaniaBlockEntities.BELLETHORNE_CHIBI));
	public static final Block bellethornFloating = make(floating(LibBlockNames.SUBTILE_BELLETHORN), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.BELLETHORNE));
	public static final Block bellethornChibiFloating = make(chibi(floating(LibBlockNames.SUBTILE_BELLETHORN)), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.BELLETHORNE_CHIBI));
	public static final Block bellethornPotted = make(potted(LibBlockNames.SUBTILE_BELLETHORN), BotaniaBlocks.flowerPot(bellethorn, 0));
	public static final Block bellethornChibiPotted = make(chibi(potted(LibBlockNames.SUBTILE_BELLETHORN)), BotaniaBlocks.flowerPot(bellethornChibi, 0));

	public static final Block bergamute = make(LibBlockNames.SUBTILE_BERGAMUTE, new SpecialFlowerBlock(MobEffects.BLINDNESS, 10, FLOWER_PROPS, () -> BotaniaBlockEntities.BERGAMUTE));
	public static final Block bergamuteFloating = make(floating(LibBlockNames.SUBTILE_BERGAMUTE), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.BERGAMUTE));
	public static final Block bergamutePotted = make(potted(LibBlockNames.SUBTILE_BERGAMUTE), BotaniaBlocks.flowerPot(bergamute, 0));

	public static final Block dreadthorn = make(LibBlockNames.SUBTILE_DREADTHORN, new SpecialFlowerBlock(MobEffects.WITHER, 10, FLOWER_PROPS, () -> BotaniaBlockEntities.DREADTHORN));
	public static final Block dreadthornFloating = make(floating(LibBlockNames.SUBTILE_DREADTHORN), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.DREADTHORN));
	public static final Block dreadthornPotted = make(potted(LibBlockNames.SUBTILE_DREADTHORN), BotaniaBlocks.flowerPot(dreadthorn, 0));

	public static final Block heiseiDream = make(LibBlockNames.SUBTILE_HEISEI_DREAM, new SpecialFlowerBlock(BotaniaMobEffects.SOUL_CROSS, 300, FLOWER_PROPS, () -> BotaniaBlockEntities.HEISEI_DREAM));
	public static final Block heiseiDreamFloating = make(floating(LibBlockNames.SUBTILE_HEISEI_DREAM), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.HEISEI_DREAM));
	public static final Block heiseiDreamPotted = make(potted(LibBlockNames.SUBTILE_HEISEI_DREAM), BotaniaBlocks.flowerPot(heiseiDream, 0));

	public static final Block tigerseye = make(LibBlockNames.SUBTILE_TIGERSEYE, new SpecialFlowerBlock(MobEffects.DAMAGE_BOOST, 90, FLOWER_PROPS, () -> BotaniaBlockEntities.TIGERSEYE));
	public static final Block tigerseyeFloating = make(floating(LibBlockNames.SUBTILE_TIGERSEYE), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.TIGERSEYE));
	public static final Block tigerseyePotted = make(potted(LibBlockNames.SUBTILE_TIGERSEYE), BotaniaBlocks.flowerPot(tigerseye, 0));

	public static final Block jadedAmaranthus = make(LibBlockNames.SUBTILE_JADED_AMARANTHUS, new SpecialFlowerBlock(MobEffects.HEAL, 1, FLOWER_PROPS, () -> BotaniaBlockEntities.JADED_AMARANTHUS));
	public static final Block jadedAmaranthusFloating = make(floating(LibBlockNames.SUBTILE_JADED_AMARANTHUS), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.JADED_AMARANTHUS));
	public static final Block jadedAmaranthusPotted = make(potted(LibBlockNames.SUBTILE_JADED_AMARANTHUS), BotaniaBlocks.flowerPot(jadedAmaranthus, 0));

	public static final Block orechid = make(LibBlockNames.SUBTILE_ORECHID, new SpecialFlowerBlock(MobEffects.DIG_SPEED, 10, FLOWER_PROPS, () -> BotaniaBlockEntities.ORECHID));
	public static final Block orechidFloating = make(floating(LibBlockNames.SUBTILE_ORECHID), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.ORECHID));
	public static final Block orechidPotted = make(potted(LibBlockNames.SUBTILE_ORECHID), BotaniaBlocks.flowerPot(orechid, 0));

	public static final Block fallenKanade = make(LibBlockNames.SUBTILE_FALLEN_KANADE, new SpecialFlowerBlock(MobEffects.REGENERATION, 90, FLOWER_PROPS, () -> BotaniaBlockEntities.FALLEN_KANADE));
	public static final Block fallenKanadeFloating = make(floating(LibBlockNames.SUBTILE_FALLEN_KANADE), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.FALLEN_KANADE));
	public static final Block fallenKanadePotted = make(potted(LibBlockNames.SUBTILE_FALLEN_KANADE), BotaniaBlocks.flowerPot(fallenKanade, 0));

	public static final Block exoflame = make(LibBlockNames.SUBTILE_EXOFLAME, new SpecialFlowerBlock(MobEffects.MOVEMENT_SPEED, 240, FLOWER_PROPS, () -> BotaniaBlockEntities.EXOFLAME));
	public static final Block exoflameFloating = make(floating(LibBlockNames.SUBTILE_EXOFLAME), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.EXOFLAME));
	public static final Block exoflamePotted = make(potted(LibBlockNames.SUBTILE_EXOFLAME), BotaniaBlocks.flowerPot(exoflame, 0));

	public static final Block agricarnation = make(LibBlockNames.SUBTILE_AGRICARNATION, new SpecialFlowerBlock(MobEffects.ABSORPTION, 48, FLOWER_PROPS, () -> BotaniaBlockEntities.AGRICARNATION));
	public static final Block agricarnationChibi = make(chibi(LibBlockNames.SUBTILE_AGRICARNATION), new SpecialFlowerBlock(MobEffects.ABSORPTION, 48, FLOWER_PROPS, () -> BotaniaBlockEntities.AGRICARNATION_CHIBI));
	public static final Block agricarnationFloating = make(floating(LibBlockNames.SUBTILE_AGRICARNATION), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.AGRICARNATION));
	public static final Block agricarnationChibiFloating = make(chibi(floating(LibBlockNames.SUBTILE_AGRICARNATION)), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.AGRICARNATION_CHIBI));
	public static final Block agricarnationPotted = make(potted(LibBlockNames.SUBTILE_AGRICARNATION), BotaniaBlocks.flowerPot(agricarnation, 0));
	public static final Block agricarnationChibiPotted = make(chibi(potted(LibBlockNames.SUBTILE_AGRICARNATION)), BotaniaBlocks.flowerPot(agricarnationChibi, 0));

	public static final Block hopperhock = make(LibBlockNames.SUBTILE_HOPPERHOCK, new SpecialFlowerBlock(MobEffects.MOVEMENT_SPEED, 30, FLOWER_PROPS, () -> BotaniaBlockEntities.HOPPERHOCK));
	public static final Block hopperhockChibi = make(chibi(LibBlockNames.SUBTILE_HOPPERHOCK), new SpecialFlowerBlock(MobEffects.MOVEMENT_SPEED, 30, FLOWER_PROPS, () -> BotaniaBlockEntities.HOPPERHOCK_CHIBI));
	public static final Block hopperhockFloating = make(floating(LibBlockNames.SUBTILE_HOPPERHOCK), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.HOPPERHOCK));
	public static final Block hopperhockChibiFloating = make(chibi(floating(LibBlockNames.SUBTILE_HOPPERHOCK)), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.HOPPERHOCK_CHIBI));
	public static final Block hopperhockPotted = make(potted(LibBlockNames.SUBTILE_HOPPERHOCK), BotaniaBlocks.flowerPot(hopperhock, 0));
	public static final Block hopperhockChibiPotted = make(chibi(potted(LibBlockNames.SUBTILE_HOPPERHOCK)), BotaniaBlocks.flowerPot(hopperhockChibi, 0));

	public static final Block tangleberrie = make(LibBlockNames.SUBTILE_TANGLEBERRIE, new SpecialFlowerBlock(BotaniaMobEffects.BLOODTHIRST, 120, FLOWER_PROPS, () -> BotaniaBlockEntities.TANGLEBERRIE));
	public static final Block tangleberrieChibi = make(chibi(LibBlockNames.SUBTILE_TANGLEBERRIE), new SpecialFlowerBlock(BotaniaMobEffects.BLOODTHIRST, 120, FLOWER_PROPS, () -> BotaniaBlockEntities.TANGLEBERRIE_CHIBI));
	public static final Block tangleberrieFloating = make(floating(LibBlockNames.SUBTILE_TANGLEBERRIE), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.TANGLEBERRIE));
	public static final Block tangleberrieChibiFloating = make(chibi(floating(LibBlockNames.SUBTILE_TANGLEBERRIE)), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.TANGLEBERRIE_CHIBI));
	public static final Block tangleberriePotted = make(potted(LibBlockNames.SUBTILE_TANGLEBERRIE), BotaniaBlocks.flowerPot(tangleberrie, 0));
	public static final Block tangleberrieChibiPotted = make(chibi(potted(LibBlockNames.SUBTILE_TANGLEBERRIE)), BotaniaBlocks.flowerPot(tangleberrieChibi, 0));

	public static final Block jiyuulia = make(LibBlockNames.SUBTILE_JIYUULIA, new SpecialFlowerBlock(BotaniaMobEffects.EMPTINESS, 120, FLOWER_PROPS, () -> BotaniaBlockEntities.JIYUULIA));
	public static final Block jiyuuliaChibi = make(chibi(LibBlockNames.SUBTILE_JIYUULIA), new SpecialFlowerBlock(BotaniaMobEffects.EMPTINESS, 120, FLOWER_PROPS, () -> BotaniaBlockEntities.JIYUULIA_CHIBI));
	public static final Block jiyuuliaFloating = make(floating(LibBlockNames.SUBTILE_JIYUULIA), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.JIYUULIA));
	public static final Block jiyuuliaChibiFloating = make(chibi(floating(LibBlockNames.SUBTILE_JIYUULIA)), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.JIYUULIA_CHIBI));
	public static final Block jiyuuliaPotted = make(potted(LibBlockNames.SUBTILE_JIYUULIA), BotaniaBlocks.flowerPot(jiyuulia, 0));
	public static final Block jiyuuliaChibiPotted = make(chibi(potted(LibBlockNames.SUBTILE_JIYUULIA)), BotaniaBlocks.flowerPot(jiyuuliaChibi, 0));

	public static final Block rannuncarpus = make(LibBlockNames.SUBTILE_RANNUNCARPUS, new SpecialFlowerBlock(MobEffects.JUMP, 30, FLOWER_PROPS, () -> BotaniaBlockEntities.RANNUNCARPUS));
	public static final Block rannuncarpusChibi = make(chibi(LibBlockNames.SUBTILE_RANNUNCARPUS), new SpecialFlowerBlock(MobEffects.JUMP, 30, FLOWER_PROPS, () -> BotaniaBlockEntities.RANNUNCARPUS_CHIBI));
	public static final Block rannuncarpusFloating = make(floating(LibBlockNames.SUBTILE_RANNUNCARPUS), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.RANNUNCARPUS));
	public static final Block rannuncarpusChibiFloating = make(chibi(floating(LibBlockNames.SUBTILE_RANNUNCARPUS)), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.RANNUNCARPUS_CHIBI));
	public static final Block rannuncarpusPotted = make(potted(LibBlockNames.SUBTILE_RANNUNCARPUS), BotaniaBlocks.flowerPot(rannuncarpus, 0));
	public static final Block rannuncarpusChibiPotted = make(chibi(potted(LibBlockNames.SUBTILE_RANNUNCARPUS)), BotaniaBlocks.flowerPot(rannuncarpusChibi, 0));

	public static final Block hyacidus = make(LibBlockNames.SUBTILE_HYACIDUS, new SpecialFlowerBlock(MobEffects.POISON, 48, FLOWER_PROPS, () -> BotaniaBlockEntities.HYACIDUS));
	public static final Block hyacidusFloating = make(floating(LibBlockNames.SUBTILE_HYACIDUS), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.HYACIDUS));
	public static final Block hyacidusPotted = make(potted(LibBlockNames.SUBTILE_HYACIDUS), BotaniaBlocks.flowerPot(hyacidus, 0));

	public static final Block pollidisiac = make(LibBlockNames.SUBTILE_POLLIDISIAC, new SpecialFlowerBlock(MobEffects.DIG_SPEED, 369, FLOWER_PROPS, () -> BotaniaBlockEntities.POLLIDISIAC));
	public static final Block pollidisiacFloating = make(floating(LibBlockNames.SUBTILE_POLLIDISIAC), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.POLLIDISIAC));
	public static final Block pollidisiacPotted = make(potted(LibBlockNames.SUBTILE_POLLIDISIAC), BotaniaBlocks.flowerPot(pollidisiac, 0));

	public static final Block clayconia = make(LibBlockNames.SUBTILE_CLAYCONIA, new SpecialFlowerBlock(MobEffects.WEAKNESS, 30, FLOWER_PROPS, () -> BotaniaBlockEntities.CLAYCONIA));
	public static final Block clayconiaChibi = make(chibi(LibBlockNames.SUBTILE_CLAYCONIA), new SpecialFlowerBlock(MobEffects.WEAKNESS, 30, FLOWER_PROPS, () -> BotaniaBlockEntities.CLAYCONIA_CHIBI));
	public static final Block clayconiaFloating = make(floating(LibBlockNames.SUBTILE_CLAYCONIA), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.CLAYCONIA));
	public static final Block clayconiaChibiFloating = make(chibi(floating(LibBlockNames.SUBTILE_CLAYCONIA)), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.CLAYCONIA_CHIBI));
	public static final Block clayconiaPotted = make(potted(LibBlockNames.SUBTILE_CLAYCONIA), BotaniaBlocks.flowerPot(clayconia, 0));
	public static final Block clayconiaChibiPotted = make(chibi(potted(LibBlockNames.SUBTILE_CLAYCONIA)), BotaniaBlocks.flowerPot(clayconiaChibi, 0));

	public static final Block loonium = make(LibBlockNames.SUBTILE_LOONIUM, new SpecialFlowerBlock(BotaniaMobEffects.ALLURE, 900, FLOWER_PROPS, () -> BotaniaBlockEntities.LOONIUM));
	public static final Block looniumFloating = make(floating(LibBlockNames.SUBTILE_LOONIUM), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.LOONIUM));
	public static final Block looniumPotted = make(potted(LibBlockNames.SUBTILE_LOONIUM), BotaniaBlocks.flowerPot(loonium, 0));

	public static final Block daffomill = make(LibBlockNames.SUBTILE_DAFFOMILL, new SpecialFlowerBlock(MobEffects.LEVITATION, 6, FLOWER_PROPS, () -> BotaniaBlockEntities.DAFFOMILL));
	public static final Block daffomillFloating = make(floating(LibBlockNames.SUBTILE_DAFFOMILL), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.DAFFOMILL));
	public static final Block daffomillPotted = make(potted(LibBlockNames.SUBTILE_DAFFOMILL), BotaniaBlocks.flowerPot(daffomill, 0));

	public static final Block vinculotus = make(LibBlockNames.SUBTILE_VINCULOTUS, new SpecialFlowerBlock(MobEffects.NIGHT_VISION, 900, FLOWER_PROPS, () -> BotaniaBlockEntities.VINCULOTUS));
	public static final Block vinculotusFloating = make(floating(LibBlockNames.SUBTILE_VINCULOTUS), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.VINCULOTUS));
	public static final Block vinculotusPotted = make(potted(LibBlockNames.SUBTILE_VINCULOTUS), BotaniaBlocks.flowerPot(vinculotus, 0));

	public static final Block spectranthemum = make(LibBlockNames.SUBTILE_SPECTRANTHEMUM, new SpecialFlowerBlock(MobEffects.INVISIBILITY, 360, FLOWER_PROPS, () -> BotaniaBlockEntities.SPECTRANTHEMUM));
	public static final Block spectranthemumFloating = make(floating(LibBlockNames.SUBTILE_SPECTRANTHEMUM), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.SPECTRANTHEMUM));
	public static final Block spectranthemumPotted = make(potted(LibBlockNames.SUBTILE_SPECTRANTHEMUM), BotaniaBlocks.flowerPot(spectranthemum, 0));

	public static final Block medumone = make(LibBlockNames.SUBTILE_MEDUMONE, new SpecialFlowerBlock(MobEffects.MOVEMENT_SLOWDOWN, 3600, FLOWER_PROPS, () -> BotaniaBlockEntities.MEDUMONE));
	public static final Block medumoneFloating = make(floating(LibBlockNames.SUBTILE_MEDUMONE), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.MEDUMONE));
	public static final Block medumonePotted = make(potted(LibBlockNames.SUBTILE_MEDUMONE), BotaniaBlocks.flowerPot(medumone, 0));

	public static final Block marimorphosis = make(LibBlockNames.SUBTILE_MARIMORPHOSIS, new SpecialFlowerBlock(MobEffects.DIG_SLOWDOWN, 60, FLOWER_PROPS, () -> BotaniaBlockEntities.MARIMORPHOSIS));
	public static final Block marimorphosisChibi = make(chibi(LibBlockNames.SUBTILE_MARIMORPHOSIS), new SpecialFlowerBlock(MobEffects.DIG_SLOWDOWN, 60, FLOWER_PROPS, () -> BotaniaBlockEntities.MARIMORPHOSIS_CHIBI));
	public static final Block marimorphosisFloating = make(floating(LibBlockNames.SUBTILE_MARIMORPHOSIS), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.MARIMORPHOSIS));
	public static final Block marimorphosisChibiFloating = make(chibi(floating(LibBlockNames.SUBTILE_MARIMORPHOSIS)), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.MARIMORPHOSIS_CHIBI));
	public static final Block marimorphosisPotted = make(potted(LibBlockNames.SUBTILE_MARIMORPHOSIS), BotaniaBlocks.flowerPot(marimorphosis, 0));
	public static final Block marimorphosisChibiPotted = make(chibi(potted(LibBlockNames.SUBTILE_MARIMORPHOSIS)), BotaniaBlocks.flowerPot(marimorphosisChibi, 0));

	public static final Block bubbell = make(LibBlockNames.SUBTILE_BUBBELL, new SpecialFlowerBlock(MobEffects.WATER_BREATHING, 240, FLOWER_PROPS, () -> BotaniaBlockEntities.BUBBELL));
	public static final Block bubbellChibi = make(chibi(LibBlockNames.SUBTILE_BUBBELL), new SpecialFlowerBlock(MobEffects.WATER_BREATHING, 240, FLOWER_PROPS, () -> BotaniaBlockEntities.BUBBELL_CHIBI));
	public static final Block bubbellFloating = make(floating(LibBlockNames.SUBTILE_BUBBELL), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.BUBBELL));
	public static final Block bubbellChibiFloating = make(chibi(floating(LibBlockNames.SUBTILE_BUBBELL)), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.BUBBELL_CHIBI));
	public static final Block bubbellPotted = make(potted(LibBlockNames.SUBTILE_BUBBELL), BotaniaBlocks.flowerPot(bubbell, 0));
	public static final Block bubbellChibiPotted = make(chibi(potted(LibBlockNames.SUBTILE_BUBBELL)), BotaniaBlocks.flowerPot(bubbellChibi, 0));

	public static final Block solegnolia = make(LibBlockNames.SUBTILE_SOLEGNOLIA, new SpecialFlowerBlock(MobEffects.HARM, 1, FLOWER_PROPS, () -> BotaniaBlockEntities.SOLEGNOLIA));
	public static final Block solegnoliaChibi = make(chibi(LibBlockNames.SUBTILE_SOLEGNOLIA), new SpecialFlowerBlock(MobEffects.HARM, 1, FLOWER_PROPS, () -> BotaniaBlockEntities.SOLEGNOLIA_CHIBI));
	public static final Block solegnoliaFloating = make(floating(LibBlockNames.SUBTILE_SOLEGNOLIA), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.SOLEGNOLIA));
	public static final Block solegnoliaChibiFloating = make(chibi(floating(LibBlockNames.SUBTILE_SOLEGNOLIA)), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.SOLEGNOLIA_CHIBI));
	public static final Block solegnoliaPotted = make(potted(LibBlockNames.SUBTILE_SOLEGNOLIA), BotaniaBlocks.flowerPot(solegnolia, 0));
	public static final Block solegnoliaChibiPotted = make(chibi(potted(LibBlockNames.SUBTILE_SOLEGNOLIA)), BotaniaBlocks.flowerPot(solegnoliaChibi, 0));

	public static final Block orechidIgnem = make(LibBlockNames.SUBTILE_ORECHID_IGNEM, new SpecialFlowerBlock(MobEffects.FIRE_RESISTANCE, 600, FLOWER_PROPS, () -> BotaniaBlockEntities.ORECHID_IGNEM));
	public static final Block orechidIgnemFloating = make(floating(LibBlockNames.SUBTILE_ORECHID_IGNEM), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.ORECHID_IGNEM));
	public static final Block orechidIgnemPotted = make(potted(LibBlockNames.SUBTILE_ORECHID_IGNEM), BotaniaBlocks.flowerPot(orechidIgnem, 0));

	public static final Block labellia = make(LibBlockNames.SUBTILE_LABELLIA, new SpecialFlowerBlock(MobEffects.FIRE_RESISTANCE, 600, FLOWER_PROPS, () -> BotaniaBlockEntities.LABELLIA));
	public static final Block labelliaFloating = make(floating(LibBlockNames.SUBTILE_LABELLIA), new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> BotaniaBlockEntities.LABELLIA));
	public static final Block labelliaPotted = make(potted(LibBlockNames.SUBTILE_LABELLIA), BotaniaBlocks.flowerPot(labellia, 0));

	public static final Block defaultAltar = make(LibBlockNames.APOTHECARY_PREFIX + "default", new PetalApothecaryBlock(BlockBehaviour.Properties.of()
			.strength(3.5F).sound(SoundType.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().mapColor(MapColor.STONE)
			.lightLevel(s -> s.getValue(PetalApothecaryBlock.FLUID) == PetalApothecary.State.LAVA ? 15 : 0)));
	public static final Block deepslateAltar = make(LibBlockNames.APOTHECARY_PREFIX + "deepslate", new PetalApothecaryBlock(
			BlockBehaviour.Properties.ofFullCopy(defaultAltar).sound(SoundType.DEEPSLATE).mapColor(MapColor.DEEPSLATE)));
	public static final Block livingrockAltar = make(LibBlockNames.APOTHECARY_PREFIX + "livingrock", new PetalApothecaryBlock(
			BlockBehaviour.Properties.ofFullCopy(defaultAltar).sound(SoundType.STONE).mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block mossyAltar = make(LibBlockNames.APOTHECARY_PREFIX + "mossy", new PetalApothecaryBlock(BlockBehaviour.Properties.ofFullCopy(defaultAltar)));
	public static final Block forestAltar = make(LibBlockNames.APOTHECARY_PREFIX + "forest", new PetalApothecaryBlock(
			BlockBehaviour.Properties.ofFullCopy(defaultAltar).sound(SoundType.TUFF).mapColor(MapColor.PLANT)));
	public static final Block plainsAltar = make(LibBlockNames.APOTHECARY_PREFIX + "plains", new PetalApothecaryBlock(
			BlockBehaviour.Properties.ofFullCopy(defaultAltar).sound(SoundType.CALCITE).mapColor(DyeColor.WHITE)));
	public static final Block mountainAltar = make(LibBlockNames.APOTHECARY_PREFIX + "mountain", new PetalApothecaryBlock(
			BlockBehaviour.Properties.ofFullCopy(defaultAltar).sound(SoundType.DEEPSLATE_TILES).mapColor(DyeColor.LIGHT_GRAY)));
	public static final Block fungalAltar = make(LibBlockNames.APOTHECARY_PREFIX + "fungal", new PetalApothecaryBlock(
			BlockBehaviour.Properties.ofFullCopy(defaultAltar).sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.CRIMSON_STEM)));
	public static final Block swampAltar = make(LibBlockNames.APOTHECARY_PREFIX + "swamp", new PetalApothecaryBlock(
			BlockBehaviour.Properties.ofFullCopy(defaultAltar).sound(SoundType.DEEPSLATE_TILES).mapColor(MapColor.TERRACOTTA_BROWN)));
	public static final Block desertAltar = make(LibBlockNames.APOTHECARY_PREFIX + "desert", new PetalApothecaryBlock(
			BlockBehaviour.Properties.ofFullCopy(defaultAltar).sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_ORANGE)));
	public static final Block taigaAltar = make(LibBlockNames.APOTHECARY_PREFIX + "taiga", new PetalApothecaryBlock(
			BlockBehaviour.Properties.ofFullCopy(defaultAltar).sound(SoundType.DEEPSLATE).mapColor(DyeColor.BLUE)));
	public static final Block mesaAltar = make(LibBlockNames.APOTHECARY_PREFIX + "mesa", new PetalApothecaryBlock(
			BlockBehaviour.Properties.ofFullCopy(defaultAltar).sound(SoundType.CALCITE).mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block[] ALL_APOTHECARIES = new Block[] { defaultAltar, deepslateAltar, livingrockAltar, mossyAltar, forestAltar, plainsAltar, mountainAltar, fungalAltar, swampAltar, desertAltar, taigaAltar, mesaAltar };

	public static final Block livingrock = make(LibBlockNames.LIVING_ROCK, new Block(BlockBehaviour.Properties.of().strength(2, 10).sound(SoundType.STONE)
			.instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops()));
	public static final Block livingrockStairs = make(LibBlockNames.LIVING_ROCK + STAIR_SUFFIX, new StairBlock(livingrock.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block livingrockSlab = make(LibBlockNames.LIVING_ROCK + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block livingrockWall = make(LibBlockNames.LIVING_ROCK + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block livingrockPolished = make(LibBlockNames.LIVING_ROCK_POLISHED, new Block(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block livingrockPolishedStairs = make(LibBlockNames.LIVING_ROCK_POLISHED + STAIR_SUFFIX, new StairBlock(livingrock.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(livingrockPolished)));
	public static final Block livingrockPolishedSlab = make(LibBlockNames.LIVING_ROCK_POLISHED + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(livingrockPolished)));
	public static final Block livingrockPolishedWall = make(LibBlockNames.LIVING_ROCK_POLISHED + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(livingrockPolished)));
	public static final Block livingrockSlate = make(LibBlockNames.LIVING_ROCK_SLATE, new Block(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block livingrockBrick = make(LibBlockNames.LIVING_ROCK_BRICK, new Block(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block livingrockBrickStairs = make(LibBlockNames.LIVING_ROCK_BRICK + STAIR_SUFFIX, new StairBlock(livingrockBrick.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(livingrockBrick)));
	public static final Block livingrockBrickSlab = make(LibBlockNames.LIVING_ROCK_BRICK + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(livingrockBrick)));
	public static final Block livingrockBrickWall = make(LibBlockNames.LIVING_ROCK_BRICK + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(livingrockBrick)));
	public static final Block livingrockBrickMossy = make(LibBlockNames.LIVING_ROCK_BRICK_MOSSY, new Block(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block livingrockBrickMossyStairs = make(LibBlockNames.LIVING_ROCK_BRICK_MOSSY + STAIR_SUFFIX, new StairBlock(livingrockBrickMossy.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(livingrockBrickMossy)));
	public static final Block livingrockBrickMossySlab = make(LibBlockNames.LIVING_ROCK_BRICK_MOSSY + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(livingrockBrickMossy)));
	public static final Block livingrockBrickMossyWall = make(LibBlockNames.LIVING_ROCK_BRICK_MOSSY + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(livingrockBrickMossy)));
	public static final Block livingrockBrickCracked = make(LibBlockNames.LIVING_ROCK_BRICK_CRACKED, new Block(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block livingrockBrickChiseled = make(LibBlockNames.LIVING_ROCK_BRICK_CHISELED, new Block(BlockBehaviour.Properties.ofFullCopy(livingrock)));

	public static final Block livingwoodLog = make(LibBlockNames.LIVING_WOOD_LOG, new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(2)
			.sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS).mapColor(state -> state.getValue(BlockStateProperties.AXIS) == Direction.Axis.Y ? MapColor.TERRACOTTA_RED : MapColor.TERRACOTTA_BROWN)));
	public static final Block livingwood = make(LibBlockNames.LIVING_WOOD, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(livingwoodLog).mapColor(MapColor.TERRACOTTA_BROWN)));
	public static final Block livingwoodStairs = make(LibBlockNames.LIVING_WOOD + STAIR_SUFFIX, new StairBlock(livingwood.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block livingwoodSlab = make(LibBlockNames.LIVING_WOOD + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block livingwoodWall = make(LibBlockNames.LIVING_WOOD + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block livingwoodLogStripped = make(LibBlockNames.LIVING_WOOD_LOG_STRIPPED, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(livingwoodLog).mapColor(MapColor.TERRACOTTA_RED)));
	public static final Block livingwoodStripped = make(LibBlockNames.LIVING_WOOD_STRIPPED, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(livingwoodLogStripped)));
	public static final Block livingwoodStrippedStairs = make(LibBlockNames.LIVING_WOOD_STRIPPED + STAIR_SUFFIX, new StairBlock(livingwoodStripped.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block livingwoodStrippedSlab = make(LibBlockNames.LIVING_WOOD_STRIPPED + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block livingwoodStrippedWall = make(LibBlockNames.LIVING_WOOD_STRIPPED + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block livingwoodLogGlimmering = make(LibBlockNames.LIVING_WOOD_LOG_GLIMMERING, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(livingwoodLog).lightLevel(b -> 12)));
	public static final Block livingwoodGlimmering = make(LibBlockNames.LIVING_WOOD_GLIMMERING, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(livingwoodLogGlimmering).mapColor(MapColor.TERRACOTTA_BROWN)));
	public static final Block livingwoodLogStrippedGlimmering = make(LibBlockNames.LIVING_WOOD_LOG_GLIMMERING_STRIPPED, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(livingwoodLogStripped).lightLevel(b -> 8)));
	public static final Block livingwoodStrippedGlimmering = make(LibBlockNames.LIVING_WOOD_GLIMMERING_STRIPPED, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(livingwoodLogStrippedGlimmering).mapColor(MapColor.TERRACOTTA_BROWN)));
	public static final Block livingwoodPlanks = make(LibBlockNames.LIVING_WOOD_PLANKS, new Block(BlockBehaviour.Properties.ofFullCopy(livingwoodLog).mapColor(MapColor.TERRACOTTA_RED)));
	public static final Block livingwoodPlankStairs = make(LibBlockNames.LIVING_WOOD_PLANKS + STAIR_SUFFIX, new StairBlock(livingwoodPlanks.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(livingwoodPlanks)));
	public static final Block livingwoodPlankSlab = make(LibBlockNames.LIVING_WOOD_PLANKS + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(livingwoodPlanks)));
	public static final Block livingwoodFence = make(LibBlockNames.LIVING_WOOD + FENCE_SUFFIX, new FenceBlock(BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block livingwoodFenceGate = make(LibBlockNames.LIVING_WOOD + FENCE_GATE_SUFFIX, new FenceGateBlock(BotaniaBlockSetTypes.LIVINGWOOD, BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block livingwoodDoor = make(LIVING_WOOD + DOOR_SUFFIX, new DoorBlock(BotaniaBlockSetTypes.LIVINGWOOD_BLOCK_SET,
			BlockBehaviour.Properties.ofFullCopy(livingwoodPlanks).noOcclusion().pushReaction(PushReaction.DESTROY)));
	public static final Block livingwoodTrapdoor = make(LIVING_WOOD + TRAPDOOR_SUFFIX, new TrapDoorBlock(BotaniaBlockSetTypes.LIVINGWOOD_BLOCK_SET,
			BlockBehaviour.Properties.ofFullCopy(livingwoodPlanks).noOcclusion().isValidSpawn(NO_SPAWN)));
	public static final Block livingwoodPressurePlate = make(LIVING_WOOD + PRESSURE_PLATE_SUFFIX, new PressurePlateBlock(BotaniaBlockSetTypes.LIVINGWOOD_BLOCK_SET,
			BlockBehaviour.Properties.ofFullCopy(livingwoodPlanks).noCollission().strength(0.5F).forceSolidOn().pushReaction(PushReaction.DESTROY)));
	public static final Block livingwoodButton = make(LIVING_WOOD + BUTTON_SUFFIX, new ButtonBlock(BotaniaBlockSetTypes.LIVINGWOOD_BLOCK_SET, 30,
			BlockBehaviour.Properties.ofFullCopy(livingwoodPlanks).noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));
	public static final Block livingwoodSign = make(LIVING_WOOD + SIGN_SUFFIX, new StandingSignBlock(BotaniaBlockSetTypes.LIVINGWOOD,
			BlockBehaviour.Properties.ofFullCopy(livingwoodPlanks).noCollission().strength(1.0f).forceSolidOn()));
	public static final Block livingwoodWallSign = make(LIVING_WOOD + WALL_INFIX + SIGN_SUFFIX, new WallSignBlock(BotaniaBlockSetTypes.LIVINGWOOD,
			BlockBehaviour.Properties.ofFullCopy(livingwoodSign)));
	public static final Block livingwoodHangingSign = make(LIVING_WOOD + HANGING_SIGN_SUFFIX, new CeilingHangingSignBlock(BotaniaBlockSetTypes.LIVINGWOOD,
			BlockBehaviour.Properties.ofFullCopy(livingwoodSign)));
	public static final Block livingwoodWallHangingSign = make(LIVING_WOOD + WALL_INFIX + HANGING_SIGN_SUFFIX, new WallHangingSignBlock(BotaniaBlockSetTypes.LIVINGWOOD,
			BlockBehaviour.Properties.ofFullCopy(livingwoodSign)));
	public static final Block livingwoodPlanksMossy = make(LibBlockNames.LIVING_WOOD_PLANKS_MOSSY, new Block(BlockBehaviour.Properties.ofFullCopy(livingwoodPlanks)));
	public static final Block livingwoodFramed = make(LibBlockNames.LIVING_WOOD_FRAMED, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(livingwoodPlanks)));
	public static final Block livingwoodPatternFramed = make(LibBlockNames.LIVING_WOOD_PATTERN_FRAMED, new Block(BlockBehaviour.Properties.ofFullCopy(livingwoodPlanks)));

	public static final Block dreamwoodLog = make(LibBlockNames.DREAM_WOOD_LOG, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(livingwoodLog).mapColor(MapColor.QUARTZ)));
	public static final Block dreamwood = make(LibBlockNames.DREAM_WOOD, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(dreamwoodLog)));
	public static final Block dreamwoodStairs = make(LibBlockNames.DREAM_WOOD + STAIR_SUFFIX, new StairBlock(dreamwood.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(dreamwood)));
	public static final Block dreamwoodSlab = make(LibBlockNames.DREAM_WOOD + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(dreamwood)));
	public static final Block dreamwoodWall = make(LibBlockNames.DREAM_WOOD + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(dreamwood)));
	public static final Block dreamwoodLogStripped = make(LibBlockNames.DREAM_WOOD_LOG_STRIPPED, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(dreamwoodLog)));
	public static final Block dreamwoodStripped = make(LibBlockNames.DREAM_WOOD_STRIPPED, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(dreamwoodLog)));
	public static final Block dreamwoodStrippedStairs = make(LibBlockNames.DREAM_WOOD_STRIPPED + STAIR_SUFFIX, new StairBlock(dreamwoodStripped.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(dreamwood)));
	public static final Block dreamwoodStrippedSlab = make(LibBlockNames.DREAM_WOOD_STRIPPED + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(dreamwood)));
	public static final Block dreamwoodStrippedWall = make(LibBlockNames.DREAM_WOOD_STRIPPED + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(dreamwood)));
	public static final Block dreamwoodLogGlimmering = make(LibBlockNames.DREAM_WOOD_LOG_GLIMMERING, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(livingwoodLogGlimmering).mapColor(MapColor.QUARTZ)));
	public static final Block dreamwoodGlimmering = make(LibBlockNames.DREAM_WOOD_GLIMMERING, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(dreamwoodLogGlimmering)));
	public static final Block dreamwoodLogStrippedGlimmering = make(LibBlockNames.DREAM_WOOD_LOG_GLIMMERING_STRIPPED, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(livingwoodLogStrippedGlimmering).mapColor(MapColor.QUARTZ)));
	public static final Block dreamwoodStrippedGlimmering = make(LibBlockNames.DREAM_WOOD_GLIMMERING_STRIPPED, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(dreamwoodLogStrippedGlimmering)));
	public static final Block dreamwoodPlanks = make(LibBlockNames.DREAM_WOOD_PLANKS, new Block(BlockBehaviour.Properties.ofFullCopy(dreamwoodLog)));
	public static final Block dreamwoodPlankStairs = make(LibBlockNames.DREAM_WOOD_PLANKS + STAIR_SUFFIX, new StairBlock(dreamwoodPlanks.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(dreamwoodPlanks)));
	public static final Block dreamwoodPlankSlab = make(LibBlockNames.DREAM_WOOD_PLANKS + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(dreamwoodPlanks)));
	public static final Block dreamwoodFence = make(LibBlockNames.DREAM_WOOD + FENCE_SUFFIX, new FenceBlock(BlockBehaviour.Properties.ofFullCopy(dreamwoodPlanks)));
	public static final Block dreamwoodFenceGate = make(LibBlockNames.DREAM_WOOD + FENCE_GATE_SUFFIX, new FenceGateBlock(BotaniaBlockSetTypes.DREAMWOOD, BlockBehaviour.Properties.ofFullCopy(dreamwoodPlanks)));
	public static final Block dreamwoodDoor = make(DREAM_WOOD + DOOR_SUFFIX, new DoorBlock(BotaniaBlockSetTypes.DREAMWOOD_BLOCK_SET,
			BlockBehaviour.Properties.ofFullCopy(dreamwoodPlanks).noOcclusion().pushReaction(PushReaction.DESTROY)));
	public static final Block dreamwoodTrapdoor = make(DREAM_WOOD + TRAPDOOR_SUFFIX, new TrapDoorBlock(BotaniaBlockSetTypes.DREAMWOOD_BLOCK_SET,
			BlockBehaviour.Properties.ofFullCopy(dreamwoodPlanks).noOcclusion().isValidSpawn(NO_SPAWN)));
	public static final Block dreamwoodPressurePlate = make(DREAM_WOOD + PRESSURE_PLATE_SUFFIX, new PressurePlateBlock(BotaniaBlockSetTypes.DREAMWOOD_BLOCK_SET,
			BlockBehaviour.Properties.ofFullCopy(dreamwoodPlanks).noCollission().strength(0.5F).forceSolidOn().pushReaction(PushReaction.DESTROY)));
	public static final Block dreamwoodButton = make(DREAM_WOOD + BUTTON_SUFFIX, new ButtonBlock(BotaniaBlockSetTypes.DREAMWOOD_BLOCK_SET, 30,
			BlockBehaviour.Properties.ofFullCopy(dreamwoodPlanks).noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));
	public static final Block dreamwoodSign = make(DREAM_WOOD + SIGN_SUFFIX, new StandingSignBlock(BotaniaBlockSetTypes.DREAMWOOD,
			BlockBehaviour.Properties.ofFullCopy(dreamwoodPlanks).noCollission().strength(1.0f).forceSolidOn()));
	public static final Block dreamwoodWallSign = make(DREAM_WOOD + WALL_INFIX + SIGN_SUFFIX, new WallSignBlock(BotaniaBlockSetTypes.DREAMWOOD,
			BlockBehaviour.Properties.ofFullCopy(dreamwoodSign)));
	public static final Block dreamwoodHangingSign = make(DREAM_WOOD + HANGING_SIGN_SUFFIX, new CeilingHangingSignBlock(BotaniaBlockSetTypes.DREAMWOOD,
			BlockBehaviour.Properties.ofFullCopy(dreamwoodSign)));
	public static final Block dreamwoodWallHangingSign = make(DREAM_WOOD + WALL_INFIX + HANGING_SIGN_SUFFIX, new WallHangingSignBlock(BotaniaBlockSetTypes.DREAMWOOD,
			BlockBehaviour.Properties.ofFullCopy(dreamwoodSign)));
	public static final Block dreamwoodPlanksMossy = make(LibBlockNames.DREAM_WOOD_PLANKS_MOSSY, new Block(BlockBehaviour.Properties.ofFullCopy(dreamwoodPlanks)));
	public static final Block dreamwoodFramed = make(LibBlockNames.DREAM_WOOD_FRAMED, new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(dreamwoodPlanks)));
	public static final Block dreamwoodPatternFramed = make(LibBlockNames.DREAM_WOOD_PATTERN_FRAMED, new Block(BlockBehaviour.Properties.ofFullCopy(dreamwoodPlanks)));

	public static final ManaSpreaderBlock manaSpreader = make(LibBlockNames.SPREADER,
			new ManaSpreaderBlock(BlockBehaviour.Properties.ofFullCopy(livingwood).isValidSpawn(NO_SPAWN).isRedstoneConductor(NEVER)));
	public static final ManaSpreaderBlock redstoneSpreader = make(LibBlockNames.SPREADER_REDSTONE,
			new PulseManaSpreaderBlock(BlockBehaviour.Properties.ofFullCopy(livingwood).isValidSpawn(NO_SPAWN).isRedstoneConductor(NEVER)));
	public static final ManaSpreaderBlock elvenSpreader = make(LibBlockNames.SPREADER_ELVEN,
			new ElvenManaSpreaderBlock(BlockBehaviour.Properties.ofFullCopy(dreamwood).isValidSpawn(NO_SPAWN).isRedstoneConductor(NEVER)));
	public static final ManaSpreaderBlock gaiaSpreader = make(LibBlockNames.SPREADER_GAIA,
			new GaiaManaSpreaderBlock(BlockBehaviour.Properties.ofFullCopy(dreamwood).isValidSpawn(NO_SPAWN).isRedstoneConductor(NEVER)));

	public static final Block manaPool = make(LibBlockNames.POOL, new ManaPoolBlock(ManaPoolBlock.Variant.DEFAULT, BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block creativePool = make(LibBlockNames.POOL_CREATIVE, new ManaPoolBlock(ManaPoolBlock.Variant.CREATIVE, BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block dilutedPool = make(LibBlockNames.POOL_DILUTED, new ManaPoolBlock(ManaPoolBlock.Variant.DILUTED, BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block fabulousPool = make(LibBlockNames.POOL_FABULOUS, new ManaPoolBlock(ManaPoolBlock.Variant.FABULOUS, BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block alchemyCatalyst = make(LibBlockNames.ALCHEMY_CATALYST, new AlchemyCatalystBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block conjurationCatalyst = make(LibBlockNames.CONJURATION_CATALYST, new ConjurationCatalystBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));

	public static final Block manasteelBlock = make(LibBlockNames.MANASTEEL_BLOCK, new Block(BlockBehaviour.Properties.of().strength(3, 10).mapColor(MapColor.LAPIS)
			.sound(SoundType.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops()));
	public static final Block terrasteelBlock = make(LibBlockNames.TERRASTEEL_BLOCK, new Block(BlockBehaviour.Properties.ofFullCopy(manasteelBlock).mapColor(MapColor.EMERALD)));
	public static final Block elementiumBlock = make(LibBlockNames.ELEMENTIUM_BLOCK, new Block(BlockBehaviour.Properties.ofFullCopy(manasteelBlock).mapColor(MapColor.COLOR_PINK)));
	public static final Block manaDiamondBlock = make(LibBlockNames.MANA_DIAMOND_BLOCK, new Block(BlockBehaviour.Properties.ofFullCopy(manasteelBlock).mapColor(MapColor.DIAMOND)));
	public static final Block dragonstoneBlock = make(LibBlockNames.DRAGONSTONE_BLOCK, new Block(BlockBehaviour.Properties.ofFullCopy(manasteelBlock).instrument(NoteBlockInstrument.HARP).mapColor(MapColor.COLOR_PINK)));

	public static final Block manaGlass = make(LibBlockNames.MANA_GLASS, new HalfTransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel(s -> 15).isViewBlocking(
			NEVER).isSuffocating(NEVER).isValidSpawn(NO_SPAWN)));
	public static final Block elfGlass = make(LibBlockNames.ELF_GLASS, new HalfTransparentBlock(BlockBehaviour.Properties.ofFullCopy(manaGlass).isViewBlocking(
			NEVER).isSuffocating(NEVER).isValidSpawn(NO_SPAWN)));
	public static final Block bifrost = make(LibBlockNames.BIFROST, new BifrostBlock(BlockBehaviour.Properties.of().strength(-1, 0.3F)
			.lightLevel(s -> 15).sound(SoundType.GLASS).instrument(NoteBlockInstrument.HAT).noOcclusion()
			.isViewBlocking(NEVER).isSuffocating(NEVER).isValidSpawn(NO_SPAWN)));
	public static final Block bifrostPerm = make(LibBlockNames.BIFROST_PERM, new PermanentBifrostBlock(BlockBehaviour.Properties.of().strength(0.3F)
			.lightLevel(s -> 15).sound(SoundType.GLASS).instrument(NoteBlockInstrument.HAT).noOcclusion()
			.isViewBlocking(NEVER).isSuffocating(NEVER).isValidSpawn(NO_SPAWN)));

	public static final Block runeAltar = make(LibBlockNames.RUNE_ALTAR, new RunicAltarBlock(BlockBehaviour.Properties.ofFullCopy(livingrock).requiresCorrectToolForDrops()));
	public static final Block enchanter = make(LibBlockNames.ENCHANTER, new ManaEnchanterBlock(BlockBehaviour.Properties.of().mapColor(MapColor.LAPIS).strength(3, 5).lightLevel(s -> 15).sound(SoundType.STONE)));
	public static final Block brewery = make(LibBlockNames.BREWERY, new BotanicalBreweryBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block terraPlate = make(LibBlockNames.TERRA_PLATE, new TerrestrialAgglomerationPlateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.LAPIS).strength(3, 10).sound(SoundType.METAL).requiresCorrectToolForDrops()));
	public static final Block alfPortal = make(LibBlockNames.ALF_PORTAL, new AlfheimPortalBlock(BlockBehaviour.Properties.ofFullCopy(livingwood).strength(10).sound(SoundType.WOOD)
			.lightLevel(s -> s.getValue(BotaniaStateProperties.ALFPORTAL_STATE) != AlfheimPortalState.OFF ? 15 : 0)));

	public static final Block manaPylon = make(LibBlockNames.PYLON, new ManaPylonBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_BLUE).strength(5.5F).sound(SoundType.METAL).lightLevel(s -> 7).requiresCorrectToolForDrops()));
	public static final Block naturaPylon = make(LibBlockNames.PYLON_NATURA, new NaturaPylonBlock(BlockBehaviour.Properties.ofFullCopy(manaPylon).mapColor(MapColor.EMERALD)));
	public static final Block gaiaPylon = make(LibBlockNames.PYLON_GAIA, new GaiaPylonBlock(BlockBehaviour.Properties.ofFullCopy(manaPylon).mapColor(DyeColor.PINK)));

	public static final Block distributor = make(LibBlockNames.DISTRIBUTOR, new ManaSplitterBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block manaVoid = make(LibBlockNames.MANA_VOID, new ManaVoidBlock(BlockBehaviour.Properties.ofFullCopy(livingrock).strength(2, 2000)));
	public static final Block manaDetector = make(LibBlockNames.MANA_DETECTOR, new ManaDetectorBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block pistonRelay = make(LibBlockNames.PISTON_RELAY, new ForceRelayBlock(BlockBehaviour.Properties.of().strength(2, 10).sound(SoundType.METAL).mapColor(MapColor.COLOR_PURPLE).isValidSpawn(NO_SPAWN)));
	public static final Block turntable = make(LibBlockNames.TURNTABLE, new SpreaderTurntableBlock(BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block tinyPlanet = make(LibBlockNames.TINY_PLANET, new TinyPlanetBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).strength(20, 100)
			.sound(SoundType.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()));
	public static final Block wildDrum = make(LibBlockNames.DRUM_WILD, new DrumOfTheWildBlock(BlockBehaviour.Properties.ofFullCopy(livingwood).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block gatheringDrum = make(LibBlockNames.DRUM_GATHERING, new DrumOfTheGatheringBlock(BlockBehaviour.Properties.ofFullCopy(wildDrum)));
	public static final Block canopyDrum = make(LibBlockNames.DRUM_CANOPY, new DrumOfTheCanopyBlock(BlockBehaviour.Properties.ofFullCopy(wildDrum)));
	public static final Block spawnerClaw = make(LibBlockNames.SPAWNER_CLAW, new LifeImbuerBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(3).requiresCorrectToolForDrops()));
	public static final Block rfGenerator = make(LibBlockNames.FLUXFIELD, new PowerGeneratorBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block prism = make(LibBlockNames.PRISM, new ManaPrismBlock(BlockBehaviour.Properties.ofFullCopy(elfGlass).noCollission()));
	public static final Block pump = make(LibBlockNames.PUMP, new ManaPumpBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block sparkChanger = make(LibBlockNames.SPARK_CHANGER, new SparkTinkererBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block manaBomb = make(LibBlockNames.MANA_BOMB, new ManastormChargeBlock(BlockBehaviour.Properties.ofFullCopy(livingwood).strength(12)));
	public static final Block bellows = make(LibBlockNames.BELLOWS, new BellowsBlock(BlockBehaviour.Properties.ofFullCopy(livingwood)));

	public static final Block openCrate = make(LibBlockNames.OPEN_CRATE, new OpenCrateBlock(BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block craftCrate = make(LibBlockNames.CRAFT_CRATE, new CraftyCrateBlock(BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block forestEye = make(LibBlockNames.FOREST_EYE, new EyeOfTheAncientsBlock(BlockBehaviour.Properties.of().strength(5, 10).sound(SoundType.METAL).requiresCorrectToolForDrops()));
	public static final Block solidVines = make(LibBlockNames.SOLID_VINE, new SolidVineBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.VINE)));
	public static final Block abstrusePlatform = make(LibBlockNames.PLATFORM_ABSTRUSE, new AbstrusePlatformBlock(BlockBehaviour.Properties.ofFullCopy(livingwood).strength(2, 5).isValidSpawn(NO_SPAWN).noOcclusion().isViewBlocking(
			NEVER).isSuffocating(NEVER)));
	public static final Block spectralPlatform = make(LibBlockNames.PLATFORM_SPECTRAL, new SpectralPlatformBlock(BlockBehaviour.Properties.ofFullCopy(abstrusePlatform)));
	public static final Block infrangiblePlatform = make(LibBlockNames.PLATFORM_INFRANGIBLE, new InfrangiblePlatformBlock(BlockBehaviour.Properties.ofFullCopy(abstrusePlatform).strength(-1, Float.MAX_VALUE).isValidSpawn(NO_SPAWN).noOcclusion()));
	public static final Block tinyPotato = make(LibBlockNames.TINY_POTATO, new TinyPotatoBlock(BlockBehaviour.Properties.of().strength(0.25F).mapColor(DyeColor.PINK)));
	public static final Block enderEye = make(LibBlockNames.ENDER_EYE_BLOCK, new EnderOverseerBlock(BlockBehaviour.Properties.ofFullCopy(manasteelBlock)));
	public static final Block redStringContainer = make(LibBlockNames.RED_STRING_CONTAINER, new RedStringContainerBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block redStringDispenser = make(LibBlockNames.RED_STRING_DISPENSER, new RedStringDispenserBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block redStringFertilizer = make(LibBlockNames.RED_STRING_FERTILIZER, new RedStringNutrifierBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block redStringComparator = make(LibBlockNames.RED_STRING_COMPARATOR, new RedStringComparatorBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block redStringRelay = make(LibBlockNames.RED_STRING_RELAY, new RedStringSpooferBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block redStringInterceptor = make(LibBlockNames.RED_STRING_INTERCEPTOR, new RedStringInterceptorBlock(BlockBehaviour.Properties.ofFullCopy(livingrock)));

	public static final Block corporeaFunnel = make(LibBlockNames.CORPOREA_FUNNEL, new CorporeaFunnelBlock(BlockBehaviour.Properties.of().strength(5.5F)
			.mapColor(DyeColor.PURPLE).sound(SoundType.METAL).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()));
	public static final Block corporeaInterceptor = make(LibBlockNames.CORPOREA_INTERCEPTOR, new CorporeaInterceptorBlock(BlockBehaviour.Properties.ofFullCopy(corporeaFunnel)));
	public static final Block corporeaIndex = make(LibBlockNames.CORPOREA_INDEX, new CorporeaIndexBlock(BlockBehaviour.Properties.ofFullCopy(corporeaFunnel).noOcclusion()));
	public static final Block corporeaCrystalCube = make(LibBlockNames.CORPOREA_CRYSTAL_CUBE, new CorporeaCrystalCubeBlock(BlockBehaviour.Properties.ofFullCopy(corporeaFunnel)));
	public static final Block corporeaRetainer = make(LibBlockNames.CORPOREA_RETAINER, new CorporeaRetainerBlock(BlockBehaviour.Properties.ofFullCopy(corporeaFunnel)));

	public static final Block corporeaBlock = make(LibBlockNames.CORPOREA_BLOCK, new Block(BlockBehaviour.Properties.ofFullCopy(corporeaFunnel)));
	public static final Block corporeaStairs = make(LibBlockNames.CORPOREA_STAIRS, new StairBlock(corporeaBlock.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(corporeaBlock)));
	public static final Block corporeaSlab = make(LibBlockNames.CORPOREA_SLAB, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(corporeaBlock)));
	public static final Block corporeaBrick = make(LibBlockNames.CORPOREA_BRICK, new Block(BlockBehaviour.Properties.ofFullCopy(corporeaBlock)));
	public static final Block corporeaBrickStairs = make(LibBlockNames.CORPOREA_BRICK + STAIR_SUFFIX, new StairBlock(corporeaBrick.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(corporeaBrick)));
	public static final Block corporeaBrickSlab = make(LibBlockNames.CORPOREA_BRICK + LibBlockNames.SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(corporeaBrick)));
	public static final Block corporeaBrickWall = make(LibBlockNames.CORPOREA_BRICK + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(corporeaBrick)));

	public static final Block incensePlate = make(LibBlockNames.INCENSE_PLATE, new IncensePlateBlock(BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block hourglass = make(LibBlockNames.HOURGLASS, new HoveringHourglassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).strength(2).sound(SoundType.METAL)));
	public static final Block ghostRail = make(LibBlockNames.GHOST_RAIL, new SpectralRailBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RAIL)));
	public static final Block lightRelayDefault = make(LibBlockNames.LIGHT_RELAY, new LuminizerBlock(BlockBehaviour.Properties.of().noCollission()));
	public static final Block lightRelayDetector = make("detector" + LibBlockNames.LIGHT_RELAY_SUFFIX, new LuminizerDetectorBlock(BlockBehaviour.Properties.ofFullCopy(lightRelayDefault)));
	public static final Block lightRelayFork = make("fork" + LibBlockNames.LIGHT_RELAY_SUFFIX, new LuminizerForkBlock(BlockBehaviour.Properties.ofFullCopy(lightRelayDefault)));
	public static final Block lightRelayToggle = make("toggle" + LibBlockNames.LIGHT_RELAY_SUFFIX, new LuminizerToggleBlock(BlockBehaviour.Properties.ofFullCopy(lightRelayDefault)));
	public static final Block lightLauncher = make(LibBlockNames.LIGHT_LAUNCHER, new LuminizerLauncherBlock(BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block cacophonium = make(LibBlockNames.CACOPHONIUM, new CacophoniumBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NOTE_BLOCK).strength(0.8F)));
	public static final Block cellBlock = make(LibBlockNames.CELL_BLOCK, new CellularBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL)));
	public static final Block teruTeruBozu = make(LibBlockNames.TERU_TERU_BOZU, new TeruTeruBozuBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOL).instrument(NoteBlockInstrument.GUITAR).mapColor(DyeColor.WHITE)));
	public static final Block avatar = make(LibBlockNames.AVATAR, new AvatarBlock(BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block fakeAir = make(LibBlockNames.FAKE_AIR, new FakeAirBlock(BlockBehaviour.Properties.of().replaceable().noCollission().noLootTable().air().randomTicks()));
	public static final Block root = make(LibBlockNames.ROOT, new LivingRootBlock(BlockBehaviour.Properties.of().strength(1.2F).sound(SoundType.WOOD)));
	public static final Block felPumpkin = make(LibBlockNames.FEL_PUMPKIN, new FelPumpkinBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CARVED_PUMPKIN)));
	public static final Block cocoon = make(LibBlockNames.COCOON, new CocoonBlock(BlockBehaviour.Properties.of().strength(3, 60).sound(SoundType.WOOL)));
	public static final Block enchantedSoil = make(LibBlockNames.ENCHANTED_SOIL, new EnchantedSoilBlock(BlockBehaviour.Properties.of().strength(0.6F).sound(SoundType.GRASS).mapColor(MapColor.GRASS)));
	public static final Block animatedTorch = make(LibBlockNames.ANIMATED_TORCH, new AnimatedTorchBlock(BlockBehaviour.Properties.of().lightLevel(s -> 7).noOcclusion()));
	public static final Block starfield = make(LibBlockNames.STARFIELD, new StarfieldCreatorBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.PINK).strength(5, 2000).sound(SoundType.METAL)));

	public static final Block azulejo0 = make(LibBlockNames.AZULEJO_PREFIX + 0, new Block(BlockBehaviour.Properties.of().mapColor(MapColor.LAPIS).strength(2, 5)
			.sound(SoundType.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()));
	public static final Block azulejo1 = make(LibBlockNames.AZULEJO_PREFIX + 1, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block azulejo2 = make(LibBlockNames.AZULEJO_PREFIX + 2, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block azulejo3 = make(LibBlockNames.AZULEJO_PREFIX + 3, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block azulejo4 = make(LibBlockNames.AZULEJO_PREFIX + 4, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block azulejo5 = make(LibBlockNames.AZULEJO_PREFIX + 5, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block azulejo6 = make(LibBlockNames.AZULEJO_PREFIX + 6, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block azulejo7 = make(LibBlockNames.AZULEJO_PREFIX + 7, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block azulejo8 = make(LibBlockNames.AZULEJO_PREFIX + 8, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block azulejo9 = make(LibBlockNames.AZULEJO_PREFIX + 9, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block azulejo10 = make(LibBlockNames.AZULEJO_PREFIX + 10, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block azulejo11 = make(LibBlockNames.AZULEJO_PREFIX + 11, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block azulejo12 = make(LibBlockNames.AZULEJO_PREFIX + 12, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block azulejo13 = make(LibBlockNames.AZULEJO_PREFIX + 13, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block azulejo14 = make(LibBlockNames.AZULEJO_PREFIX + 14, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block azulejo15 = make(LibBlockNames.AZULEJO_PREFIX + 15, new Block(BlockBehaviour.Properties.ofFullCopy(azulejo0)));
	public static final Block manaFlame = make(LibBlockNames.MANA_FLAME, new ManaFlameBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).lightLevel(s -> 15).noCollission()));
	public static final Block blazeBlock = make(LibBlockNames.BLAZE_BLOCK, new Block(BlockBehaviour.Properties.ofFullCopy(manasteelBlock).instrument(NoteBlockInstrument.PLING).lightLevel(s -> 15).mapColor(MapColor.GOLD)));
	public static final Block gaiaHead = make(LibBlockNames.GAIA_HEAD, new GaiaHeadBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).strength(1)));
	public static final Block gaiaHeadWall = make(LibBlockNames.GAIA_WALL_HEAD, new WallGaiaHeadBlock(BlockBehaviour.Properties.ofFullCopy(gaiaHead)));

	public static final Block shimmerrock = make(LibBlockNames.SHIMMERROCK, new Block(BlockBehaviour.Properties.ofFullCopy(livingrock)));
	public static final Block shimmerrockStairs = make(LibBlockNames.SHIMMERROCK + STAIR_SUFFIX, new StairBlock(shimmerrock.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(shimmerrock)));
	public static final Block shimmerrockSlab = make(LibBlockNames.SHIMMERROCK + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(shimmerrock)));
	public static final Block shimmerwoodPlanks = make(LibBlockNames.SHIMMERWOOD_PLANKS, new Block(BlockBehaviour.Properties.ofFullCopy(livingwood)));
	public static final Block shimmerwoodPlankStairs = make(LibBlockNames.SHIMMERWOOD_PLANKS + STAIR_SUFFIX, new StairBlock(shimmerwoodPlanks.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(shimmerwoodPlanks)));
	public static final Block shimmerwoodPlankSlab = make(LibBlockNames.SHIMMERWOOD_PLANKS + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(shimmerwoodPlanks)));

	public static final Block dryGrass = make("dry" + LibBlockNames.ALT_GRASS_SUFFIX, new BotaniaGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_GREEN)));
	public static final Block goldenGrass = make("golden" + LibBlockNames.ALT_GRASS_SUFFIX, new BotaniaGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).mapColor(MapColor.GOLD)));
	public static final Block vividGrass = make("vivid" + LibBlockNames.ALT_GRASS_SUFFIX, new BotaniaGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).mapColor(MapColor.PLANT)));
	public static final Block scorchedGrass = make("scorched" + LibBlockNames.ALT_GRASS_SUFFIX, new BotaniaScorchedGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).mapColor(MapColor.NETHER)));
	public static final Block infusedGrass = make("infused" + LibBlockNames.ALT_GRASS_SUFFIX, new BotaniaInfusedGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).mapColor(MapColor.COLOR_CYAN)));
	public static final Block mutatedGrass = make("mutated" + LibBlockNames.ALT_GRASS_SUFFIX, new BotaniaMutatedGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).mapColor(MapColor.WARPED_HYPHAE)));

	public static final Block motifDaybloom = make(LibBlockNames.MOTIF_DAYBLOOM, new FlowerMotifBlock(MobEffects.BLINDNESS, 15, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY), true));
	public static final Block motifNightshade = make(LibBlockNames.MOTIF_NIGHTSHADE, new FlowerMotifBlock(MobEffects.POISON, 20, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY), true));
	public static final Block motifHydroangeas = make(LibBlockNames.MOTIF_HYDROANGEAS, new FlowerMotifBlock(MobEffects.UNLUCK, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY), false));

	public static final Block pottedMotifDaybloom = make(LibBlockNames.POTTED_PREFIX + LibBlockNames.MOTIF_DAYBLOOM, flowerPot(motifDaybloom, 0));
	public static final Block pottedMotifNightshade = make(LibBlockNames.POTTED_PREFIX + LibBlockNames.MOTIF_NIGHTSHADE, flowerPot(motifNightshade, 0));
	public static final Block pottedMotifHydroangeas = make(LibBlockNames.POTTED_PREFIX + LibBlockNames.MOTIF_HYDROANGEAS, flowerPot(motifHydroangeas, 0));

	public static final Block darkQuartz = make(QUARTZ_DARK, new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK).mapColor(MapColor.COLOR_BLACK)));
	public static final Block darkQuartzStairs = make(QUARTZ_DARK + STAIR_SUFFIX, new StairBlock(darkQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(darkQuartz)));
	public static final Block darkQuartzSlab = make(QUARTZ_DARK + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(darkQuartz)));
	public static final Block darkQuartzChiseled = make("chiseled_" + QUARTZ_DARK, new Block(BlockBehaviour.Properties.ofFullCopy(darkQuartz)));
	public static final Block darkQuartzBricks = make(QUARTZ_DARK + "_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(darkQuartz)));
	public static final Block darkQuartzPillar = make(QUARTZ_DARK + "_pillar", new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(darkQuartz)));
	public static final Block darkSmoothQuartz = make(SMOOTH_PREFIX + QUARTZ_DARK, new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).mapColor(MapColor.COLOR_BLACK)));
	public static final Block darkSmoothQuartzStairs = make(SMOOTH_PREFIX + QUARTZ_DARK + STAIR_SUFFIX, new StairBlock(darkSmoothQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(darkSmoothQuartz)));
	public static final Block darkSmoothQuartzSlab = make(SMOOTH_PREFIX + QUARTZ_DARK + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(darkSmoothQuartz)));

	public static final Block manaQuartz = make(QUARTZ_MANA, new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK).mapColor(MapColor.DIAMOND)));
	public static final Block manaQuartzStairs = make(QUARTZ_MANA + STAIR_SUFFIX, new StairBlock(manaQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(darkQuartz)));
	public static final Block manaQuartzSlab = make(QUARTZ_MANA + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(manaQuartz)));
	public static final Block manaQuartzChiseled = make("chiseled_" + QUARTZ_MANA, new Block(BlockBehaviour.Properties.ofFullCopy(manaQuartz)));
	public static final Block manaQuartzBricks = make(QUARTZ_MANA + "_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(manaQuartz)));
	public static final Block manaQuartzPillar = make(QUARTZ_MANA + "_pillar", new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(manaQuartz)));
	public static final Block manaSmoothQuartz = make(SMOOTH_PREFIX + QUARTZ_MANA, new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).mapColor(MapColor.DIAMOND)));
	public static final Block manaSmoothQuartzStairs = make(SMOOTH_PREFIX + QUARTZ_MANA + STAIR_SUFFIX, new StairBlock(manaSmoothQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(manaSmoothQuartz)));
	public static final Block manaSmoothQuartzSlab = make(SMOOTH_PREFIX + QUARTZ_MANA + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(manaSmoothQuartz)));

	public static final Block blazeQuartz = make(QUARTZ_BLAZE, new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK).mapColor(MapColor.SAND)));
	public static final Block blazeQuartzStairs = make(QUARTZ_BLAZE + STAIR_SUFFIX, new StairBlock(blazeQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(blazeQuartz)));
	public static final Block blazeQuartzSlab = make(QUARTZ_BLAZE + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(blazeQuartz)));
	public static final Block blazeQuartzChiseled = make("chiseled_" + QUARTZ_BLAZE, new Block(BlockBehaviour.Properties.ofFullCopy(blazeQuartz)));
	public static final Block blazeQuartzBricks = make(QUARTZ_BLAZE + "_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(blazeQuartz)));
	public static final Block blazeQuartzPillar = make(QUARTZ_BLAZE + "_pillar", new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(blazeQuartz)));
	public static final Block blazeSmoothQuartz = make(SMOOTH_PREFIX + QUARTZ_BLAZE, new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).mapColor(MapColor.SAND)));
	public static final Block blazeSmoothQuartzStairs = make(SMOOTH_PREFIX + QUARTZ_BLAZE + STAIR_SUFFIX, new StairBlock(blazeSmoothQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(blazeSmoothQuartz)));
	public static final Block blazeSmoothQuartzSlab = make(SMOOTH_PREFIX + QUARTZ_BLAZE + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(blazeSmoothQuartz)));

	public static final Block lavenderQuartz = make(QUARTZ_LAVENDER, new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK).mapColor(MapColor.COLOR_PINK)));
	public static final Block lavenderQuartzStairs = make(QUARTZ_LAVENDER + STAIR_SUFFIX, new StairBlock(lavenderQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(lavenderQuartz)));
	public static final Block lavenderQuartzSlab = make(QUARTZ_LAVENDER + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(lavenderQuartz)));
	public static final Block lavenderQuartzChiseled = make("chiseled_" + QUARTZ_LAVENDER, new Block(BlockBehaviour.Properties.ofFullCopy(lavenderQuartz)));
	public static final Block lavenderQuartzBricks = make(QUARTZ_LAVENDER + "_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(lavenderQuartz)));
	public static final Block lavenderQuartzPillar = make(QUARTZ_LAVENDER + "_pillar", new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(lavenderQuartz)));
	public static final Block lavenderSmoothQuartz = make(SMOOTH_PREFIX + QUARTZ_LAVENDER, new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).mapColor(MapColor.COLOR_PINK)));
	public static final Block lavenderSmoothQuartzStairs = make(SMOOTH_PREFIX + QUARTZ_LAVENDER + STAIR_SUFFIX, new StairBlock(lavenderSmoothQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(lavenderSmoothQuartz)));
	public static final Block lavenderSmoothQuartzSlab = make(SMOOTH_PREFIX + QUARTZ_LAVENDER + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(lavenderSmoothQuartz)));

	public static final Block redQuartz = make(QUARTZ_RED, new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK).mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block redQuartzStairs = make(QUARTZ_RED + STAIR_SUFFIX, new StairBlock(redQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(redQuartz)));
	public static final Block redQuartzSlab = make(QUARTZ_RED + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(redQuartz)));
	public static final Block redQuartzChiseled = make("chiseled_" + QUARTZ_RED, new Block(BlockBehaviour.Properties.ofFullCopy(redQuartz)));
	public static final Block redQuartzBricks = make(QUARTZ_RED + "_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(redQuartz)));
	public static final Block redQuartzPillar = make(QUARTZ_RED + "_pillar", new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(redQuartz)));
	public static final Block redSmoothQuartz = make(SMOOTH_PREFIX + QUARTZ_RED, new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block redSmoothQuartzStairs = make(SMOOTH_PREFIX + QUARTZ_RED + STAIR_SUFFIX, new StairBlock(redSmoothQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(redSmoothQuartz)));
	public static final Block redSmoothQuartzSlab = make(SMOOTH_PREFIX + QUARTZ_RED + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(redSmoothQuartz)));

	public static final Block elfQuartz = make(QUARTZ_ELF, new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK).mapColor(MapColor.COLOR_LIGHT_GREEN)));
	public static final Block elfQuartzStairs = make(QUARTZ_ELF + STAIR_SUFFIX, new StairBlock(elfQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(elfQuartz)));
	public static final Block elfQuartzSlab = make(QUARTZ_ELF + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(elfQuartz)));
	public static final Block elfQuartzChiseled = make("chiseled_" + QUARTZ_ELF, new Block(BlockBehaviour.Properties.ofFullCopy(elfQuartz)));
	public static final Block elfQuartzBricks = make(QUARTZ_ELF + "_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(elfQuartz)));
	public static final Block elfQuartzPillar = make(QUARTZ_ELF + "_pillar", new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(elfQuartz)));
	public static final Block elfSmoothQuartz = make(SMOOTH_PREFIX + QUARTZ_ELF, new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).mapColor(MapColor.COLOR_LIGHT_GREEN)));
	public static final Block elfSmoothQuartzStairs = make(SMOOTH_PREFIX + QUARTZ_ELF + STAIR_SUFFIX, new StairBlock(elfSmoothQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(elfSmoothQuartz)));
	public static final Block elfSmoothQuartzSlab = make(SMOOTH_PREFIX + QUARTZ_ELF + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(elfSmoothQuartz)));

	public static final Block sunnyQuartz = make(QUARTZ_SUNNY, new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK).mapColor(MapColor.COLOR_YELLOW)));
	public static final Block sunnyQuartzStairs = make(QUARTZ_SUNNY + STAIR_SUFFIX, new StairBlock(sunnyQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(sunnyQuartz)));
	public static final Block sunnyQuartzSlab = make(QUARTZ_SUNNY + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(sunnyQuartz)));
	public static final Block sunnyQuartzChiseled = make("chiseled_" + QUARTZ_SUNNY, new Block(BlockBehaviour.Properties.ofFullCopy(sunnyQuartz)));
	public static final Block sunnyQuartzBricks = make(QUARTZ_SUNNY + "_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(sunnyQuartz)));
	public static final Block sunnyQuartzPillar = make(QUARTZ_SUNNY + "_pillar", new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(sunnyQuartz)));
	public static final Block sunnySmoothQuartz = make(SMOOTH_PREFIX + QUARTZ_SUNNY, new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).mapColor(MapColor.COLOR_YELLOW)));
	public static final Block sunnySmoothQuartzStairs = make(SMOOTH_PREFIX + QUARTZ_SUNNY + STAIR_SUFFIX, new StairBlock(sunnySmoothQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(sunnySmoothQuartz)));
	public static final Block sunnySmoothQuartzSlab = make(SMOOTH_PREFIX + QUARTZ_SUNNY + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(sunnySmoothQuartz)));

	public static final Block biomeStoneForest = make(METAMORPHIC_PREFIX + "forest_stone", new Block(BlockBehaviour.Properties.of().strength(1.5F, 10)
			.sound(SoundType.TUFF).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().mapColor(MapColor.WARPED_NYLIUM)));
	public static final Block biomeStoneForestStairs = make(METAMORPHIC_PREFIX + "forest_stone" + STAIR_SUFFIX, new StairBlock(biomeStoneForest.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)));
	public static final Block biomeStoneForestSlab = make(METAMORPHIC_PREFIX + "forest_stone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)));
	public static final Block biomeStoneForestWall = make(METAMORPHIC_PREFIX + "forest_stone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)));
	public static final Block biomeCobblestoneForest = make(METAMORPHIC_PREFIX + "forest_cobblestone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)));
	public static final Block biomeCobblestoneForestStairs = make(METAMORPHIC_PREFIX + "forest_cobblestone" + STAIR_SUFFIX, new StairBlock(biomeStoneForest.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)));
	public static final Block biomeCobblestoneForestSlab = make(METAMORPHIC_PREFIX + "forest_cobblestone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)));
	public static final Block biomeCobblestoneForestWall = make(METAMORPHIC_PREFIX + "forest_cobblestone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)));
	public static final Block biomeBrickForest = make(METAMORPHIC_PREFIX + "forest_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)));
	public static final Block biomeBrickForestStairs = make(METAMORPHIC_PREFIX + "forest_bricks" + STAIR_SUFFIX, new StairBlock(biomeStoneForest.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)));
	public static final Block biomeBrickForestSlab = make(METAMORPHIC_PREFIX + "forest_bricks" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)));
	public static final Block biomeBrickForestWall = make(METAMORPHIC_PREFIX + "forest_bricks" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)));
	public static final Block biomeChiseledBrickForest = make("chiseled_" + METAMORPHIC_PREFIX + "forest_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)));

	public static final Block biomeStonePlains = make(METAMORPHIC_PREFIX + "plains_stone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)
			.sound(SoundType.CALCITE).mapColor(MapColor.QUARTZ)));
	public static final Block biomeStonePlainsStairs = make(METAMORPHIC_PREFIX + "plains_stone" + STAIR_SUFFIX, new StairBlock(biomeStonePlains.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStonePlains)));
	public static final Block biomeStonePlainsSlab = make(METAMORPHIC_PREFIX + "plains_stone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStonePlains)));
	public static final Block biomeStonePlainsWall = make(METAMORPHIC_PREFIX + "plains_stone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStonePlains)));
	public static final Block biomeCobblestonePlains = make(METAMORPHIC_PREFIX + "plains_cobblestone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStonePlains)));
	public static final Block biomeCobblestonePlainsStairs = make(METAMORPHIC_PREFIX + "plains_cobblestone" + STAIR_SUFFIX, new StairBlock(biomeStonePlains.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStonePlains)));
	public static final Block biomeCobblestonePlainsSlab = make(METAMORPHIC_PREFIX + "plains_cobblestone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStonePlains)));
	public static final Block biomeCobblestonePlainsWall = make(METAMORPHIC_PREFIX + "plains_cobblestone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStonePlains)));
	public static final Block biomeBrickPlains = make(METAMORPHIC_PREFIX + "plains_bricks", new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(biomeStonePlains)));
	public static final Block biomeBrickPlainsStairs = make(METAMORPHIC_PREFIX + "plains_bricks" + STAIR_SUFFIX, new StairBlock(biomeStonePlains.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStonePlains)));
	public static final Block biomeBrickPlainsSlab = make(METAMORPHIC_PREFIX + "plains_bricks" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStonePlains)));
	public static final Block biomeBrickPlainsWall = make(METAMORPHIC_PREFIX + "plains_bricks" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStonePlains)));
	public static final Block biomeChiseledBrickPlains = make("chiseled_" + METAMORPHIC_PREFIX + "plains_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStonePlains)));

	public static final Block biomeStoneMountain = make(METAMORPHIC_PREFIX + "mountain_stone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)
			.sound(SoundType.DEEPSLATE_TILES).mapColor(MapColor.GLOW_LICHEN)));
	public static final Block biomeStoneMountainStairs = make(METAMORPHIC_PREFIX + "mountain_stone" + STAIR_SUFFIX, new StairBlock(biomeStoneMountain.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneMountain)));
	public static final Block biomeStoneMountainSlab = make(METAMORPHIC_PREFIX + "mountain_stone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneMountain)));
	public static final Block biomeStoneMountainWall = make(METAMORPHIC_PREFIX + "mountain_stone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneMountain)));
	public static final Block biomeCobblestoneMountain = make(METAMORPHIC_PREFIX + "mountain_cobblestone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneMountain)));
	public static final Block biomeCobblestoneMountainStairs = make(METAMORPHIC_PREFIX + "mountain_cobblestone" + STAIR_SUFFIX, new StairBlock(biomeStoneMountain.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneMountain)));
	public static final Block biomeCobblestoneMountainSlab = make(METAMORPHIC_PREFIX + "mountain_cobblestone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneMountain)));
	public static final Block biomeCobblestoneMountainWall = make(METAMORPHIC_PREFIX + "mountain_cobblestone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneMountain)));
	public static final Block biomeBrickMountain = make(METAMORPHIC_PREFIX + "mountain_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneMountain)));
	public static final Block biomeBrickMountainStairs = make(METAMORPHIC_PREFIX + "mountain_bricks" + STAIR_SUFFIX, new StairBlock(biomeStoneMountain.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneMountain)));
	public static final Block biomeBrickMountainSlab = make(METAMORPHIC_PREFIX + "mountain_bricks" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneMountain)));
	public static final Block biomeBrickMountainWall = make(METAMORPHIC_PREFIX + "mountain_bricks" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneMountain)));
	public static final Block biomeChiseledBrickMountain = make("chiseled_" + METAMORPHIC_PREFIX + "mountain_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneMountain)));

	public static final Block biomeStoneFungal = make(METAMORPHIC_PREFIX + "fungal_stone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)
			.sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_PURPLE)));
	public static final Block biomeStoneFungalStairs = make(METAMORPHIC_PREFIX + "fungal_stone" + STAIR_SUFFIX, new StairBlock(biomeStoneFungal.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneFungal)));
	public static final Block biomeStoneFungalSlab = make(METAMORPHIC_PREFIX + "fungal_stone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneFungal)));
	public static final Block biomeStoneFungalWall = make(METAMORPHIC_PREFIX + "fungal_stone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneFungal)));
	public static final Block biomeCobblestoneFungal = make(METAMORPHIC_PREFIX + "fungal_cobblestone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneFungal)));
	public static final Block biomeCobblestoneFungalStairs = make(METAMORPHIC_PREFIX + "fungal_cobblestone" + STAIR_SUFFIX, new StairBlock(biomeStoneFungal.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneFungal)));
	public static final Block biomeCobblestoneFungalSlab = make(METAMORPHIC_PREFIX + "fungal_cobblestone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneFungal)));
	public static final Block biomeCobblestoneFungalWall = make(METAMORPHIC_PREFIX + "fungal_cobblestone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneFungal)));
	public static final Block biomeBrickFungal = make(METAMORPHIC_PREFIX + "fungal_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneFungal)));
	public static final Block biomeBrickFungalStairs = make(METAMORPHIC_PREFIX + "fungal_bricks" + STAIR_SUFFIX, new StairBlock(biomeStoneFungal.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneFungal)));
	public static final Block biomeBrickFungalSlab = make(METAMORPHIC_PREFIX + "fungal_bricks" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneFungal)));
	public static final Block biomeBrickFungalWall = make(METAMORPHIC_PREFIX + "fungal_bricks" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneFungal)));
	public static final Block biomeChiseledBrickFungal = make("chiseled_" + METAMORPHIC_PREFIX + "fungal_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneFungal)));

	public static final Block biomeStoneSwamp = make(METAMORPHIC_PREFIX + "swamp_stone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)
			.sound(SoundType.DEEPSLATE_TILES).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
	public static final Block biomeStoneSwampStairs = make(METAMORPHIC_PREFIX + "swamp_stone" + STAIR_SUFFIX, new StairBlock(biomeStoneSwamp.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneSwamp)));
	public static final Block biomeStoneSwampSlab = make(METAMORPHIC_PREFIX + "swamp_stone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneSwamp)));
	public static final Block biomeStoneSwampWall = make(METAMORPHIC_PREFIX + "swamp_stone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneSwamp)));
	public static final Block biomeCobblestoneSwamp = make(METAMORPHIC_PREFIX + "swamp_cobblestone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneSwamp)));
	public static final Block biomeCobblestoneSwampStairs = make(METAMORPHIC_PREFIX + "swamp_cobblestone" + STAIR_SUFFIX, new StairBlock(biomeStoneSwamp.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneSwamp)));
	public static final Block biomeCobblestoneSwampSlab = make(METAMORPHIC_PREFIX + "swamp_cobblestone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneSwamp)));
	public static final Block biomeCobblestoneSwampWall = make(METAMORPHIC_PREFIX + "swamp_cobblestone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneSwamp)));
	public static final Block biomeBrickSwamp = make(METAMORPHIC_PREFIX + "swamp_bricks", new BotaniaDirectionalBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneSwamp)));
	public static final Block biomeBrickSwampStairs = make(METAMORPHIC_PREFIX + "swamp_bricks" + STAIR_SUFFIX, new StairBlock(biomeStoneSwamp.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneSwamp)));
	public static final Block biomeBrickSwampSlab = make(METAMORPHIC_PREFIX + "swamp_bricks" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneSwamp)));
	public static final Block biomeBrickSwampWall = make(METAMORPHIC_PREFIX + "swamp_bricks" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneSwamp)));
	public static final Block biomeChiseledBrickSwamp = make("chiseled_" + METAMORPHIC_PREFIX + "swamp_bricks", new BotaniaDirectionalBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneSwamp)));

	public static final Block biomeStoneDesert = make(METAMORPHIC_PREFIX + "desert_stone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)
			.sound(SoundType.DEEPSLATE).mapColor(MapColor.DIRT)));
	public static final Block biomeStoneDesertStairs = make(METAMORPHIC_PREFIX + "desert_stone" + STAIR_SUFFIX, new StairBlock(biomeStoneDesert.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneDesert)));
	public static final Block biomeStoneDesertSlab = make(METAMORPHIC_PREFIX + "desert_stone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneDesert)));
	public static final Block biomeStoneDesertWall = make(METAMORPHIC_PREFIX + "desert_stone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneDesert)));
	public static final Block biomeCobblestoneDesert = make(METAMORPHIC_PREFIX + "desert_cobblestone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneDesert)));
	public static final Block biomeCobblestoneDesertStairs = make(METAMORPHIC_PREFIX + "desert_cobblestone" + STAIR_SUFFIX, new StairBlock(biomeStoneDesert.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneDesert)));
	public static final Block biomeCobblestoneDesertSlab = make(METAMORPHIC_PREFIX + "desert_cobblestone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneDesert)));
	public static final Block biomeCobblestoneDesertWall = make(METAMORPHIC_PREFIX + "desert_cobblestone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneDesert)));
	public static final Block biomeBrickDesert = make(METAMORPHIC_PREFIX + "desert_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneDesert)));
	public static final Block biomeBrickDesertStairs = make(METAMORPHIC_PREFIX + "desert_bricks" + STAIR_SUFFIX, new StairBlock(biomeStoneDesert.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneDesert)));
	public static final Block biomeBrickDesertSlab = make(METAMORPHIC_PREFIX + "desert_bricks" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneDesert)));
	public static final Block biomeBrickDesertWall = make(METAMORPHIC_PREFIX + "desert_bricks" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneDesert)));
	public static final Block biomeChiseledBrickDesert = make("chiseled_" + METAMORPHIC_PREFIX + "desert_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneDesert)));

	public static final Block biomeStoneTaiga = make(METAMORPHIC_PREFIX + "taiga_stone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)
			.sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE)));
	public static final Block biomeStoneTaigaStairs = make(METAMORPHIC_PREFIX + "taiga_stone" + STAIR_SUFFIX, new StairBlock(biomeStoneTaiga.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneTaiga)));
	public static final Block biomeStoneTaigaSlab = make(METAMORPHIC_PREFIX + "taiga_stone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneTaiga)));
	public static final Block biomeStoneTaigaWall = make(METAMORPHIC_PREFIX + "taiga_stone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneTaiga)));
	public static final Block biomeCobblestoneTaiga = make(METAMORPHIC_PREFIX + "taiga_cobblestone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneTaiga)));
	public static final Block biomeCobblestoneTaigaStairs = make(METAMORPHIC_PREFIX + "taiga_cobblestone" + STAIR_SUFFIX, new StairBlock(biomeStoneTaiga.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneTaiga)));
	public static final Block biomeCobblestoneTaigaSlab = make(METAMORPHIC_PREFIX + "taiga_cobblestone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneTaiga)));
	public static final Block biomeCobblestoneTaigaWall = make(METAMORPHIC_PREFIX + "taiga_cobblestone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneTaiga)));
	public static final Block biomeBrickTaiga = make(METAMORPHIC_PREFIX + "taiga_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneTaiga)));
	public static final Block biomeBrickTaigaStairs = make(METAMORPHIC_PREFIX + "taiga_bricks" + STAIR_SUFFIX, new StairBlock(biomeStoneTaiga.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneTaiga)));
	public static final Block biomeBrickTaigaSlab = make(METAMORPHIC_PREFIX + "taiga_bricks" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneTaiga)));
	public static final Block biomeBrickTaigaWall = make(METAMORPHIC_PREFIX + "taiga_bricks" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneTaiga)));
	public static final Block biomeChiseledBrickTaiga = make("chiseled_" + METAMORPHIC_PREFIX + "taiga_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneTaiga)));

	public static final Block biomeStoneMesa = make(METAMORPHIC_PREFIX + "mesa_stone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneForest)
			.sound(SoundType.CALCITE).mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block biomeStoneMesaStairs = make(METAMORPHIC_PREFIX + "mesa_stone" + STAIR_SUFFIX, new StairBlock(biomeStoneMesa.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneMesa)));
	public static final Block biomeStoneMesaSlab = make(METAMORPHIC_PREFIX + "mesa_stone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneMesa)));
	public static final Block biomeStoneMesaWall = make(METAMORPHIC_PREFIX + "mesa_stone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneMesa)));
	public static final Block biomeCobblestoneMesa = make(METAMORPHIC_PREFIX + "mesa_cobblestone", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneMesa)));
	public static final Block biomeCobblestoneMesaStairs = make(METAMORPHIC_PREFIX + "mesa_cobblestone" + STAIR_SUFFIX, new StairBlock(biomeStoneMesa.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneMesa)));
	public static final Block biomeCobblestoneMesaSlab = make(METAMORPHIC_PREFIX + "mesa_cobblestone" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneMesa)));
	public static final Block biomeCobblestoneMesaWall = make(METAMORPHIC_PREFIX + "mesa_cobblestone" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneMesa)));
	public static final Block biomeBrickMesa = make(METAMORPHIC_PREFIX + "mesa_bricks", new Block(BlockBehaviour.Properties.ofFullCopy(biomeStoneMesa)));
	public static final Block biomeBrickMesaStairs = make(METAMORPHIC_PREFIX + "mesa_bricks" + STAIR_SUFFIX, new StairBlock(biomeStoneMesa.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(biomeStoneMesa)));
	public static final Block biomeBrickMesaSlab = make(METAMORPHIC_PREFIX + "mesa_bricks" + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneMesa)));
	public static final Block biomeBrickMesaWall = make(METAMORPHIC_PREFIX + "mesa_bricks" + WALL_SUFFIX, new WallBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneMesa)));
	public static final Block biomeChiseledBrickMesa = make("chiseled_" + METAMORPHIC_PREFIX + "mesa_bricks", new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(biomeStoneMesa)));

	public static final Block whitePavement = make("white" + PAVEMENT_SUFFIX, new Block(BlockBehaviour.Properties.ofFullCopy(livingrock).mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block whitePavementStair = make("white" + PAVEMENT_SUFFIX + STAIR_SUFFIX, new StairBlock(whitePavement.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(whitePavement)));
	public static final Block whitePavementSlab = make("white" + PAVEMENT_SUFFIX + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(whitePavement)));

	public static final Block blackPavement = make("black" + PAVEMENT_SUFFIX, new Block(BlockBehaviour.Properties.ofFullCopy(whitePavement).mapColor(MapColor.COLOR_GRAY)));
	public static final Block blackPavementStair = make("black" + PAVEMENT_SUFFIX + STAIR_SUFFIX, new StairBlock(blackPavement.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(blackPavement)));
	public static final Block blackPavementSlab = make("black" + PAVEMENT_SUFFIX + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(blackPavement)));

	public static final Block bluePavement = make("blue" + PAVEMENT_SUFFIX, new Block(BlockBehaviour.Properties.ofFullCopy(whitePavement).mapColor(MapColor.COLOR_BLUE)));
	public static final Block bluePavementStair = make("blue" + PAVEMENT_SUFFIX + STAIR_SUFFIX, new StairBlock(bluePavement.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(bluePavement)));
	public static final Block bluePavementSlab = make("blue" + PAVEMENT_SUFFIX + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(bluePavement)));

	public static final Block yellowPavement = make("yellow" + PAVEMENT_SUFFIX, new Block(BlockBehaviour.Properties.ofFullCopy(whitePavement).mapColor(MapColor.TERRACOTTA_YELLOW)));
	public static final Block yellowPavementStair = make("yellow" + PAVEMENT_SUFFIX + STAIR_SUFFIX, new StairBlock(yellowPavement.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(yellowPavement)));
	public static final Block yellowPavementSlab = make("yellow" + PAVEMENT_SUFFIX + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(yellowPavement)));

	public static final Block redPavement = make("red" + PAVEMENT_SUFFIX, new Block(BlockBehaviour.Properties.ofFullCopy(whitePavement).mapColor(MapColor.TERRACOTTA_RED)));
	public static final Block redPavementStair = make("red" + PAVEMENT_SUFFIX + STAIR_SUFFIX, new StairBlock(redPavement.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(redPavement)));
	public static final Block redPavementSlab = make("red" + PAVEMENT_SUFFIX + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(redPavement)));

	public static final Block greenPavement = make("green" + PAVEMENT_SUFFIX, new Block(BlockBehaviour.Properties.ofFullCopy(whitePavement).mapColor(MapColor.TERRACOTTA_GREEN)));
	public static final Block greenPavementStair = make("green" + PAVEMENT_SUFFIX + STAIR_SUFFIX, new StairBlock(greenPavement.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(greenPavement)));
	public static final Block greenPavementSlab = make("green" + PAVEMENT_SUFFIX + SLAB_SUFFIX, new SlabBlock(BlockBehaviour.Properties.ofFullCopy(greenPavement)));

	public static final Block managlassPane = make(LibBlockNames.MANA_GLASS + "_pane", new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(manaGlass)));
	public static final Block alfglassPane = make(LibBlockNames.ELF_GLASS + "_pane", new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(elfGlass)));
	public static final Block bifrostPane = make(LibBlockNames.BIFROST + "_pane", new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(bifrostPerm)));

	static FlowerPotBlock flowerPot(Block block, int lightLevel) {
		BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
		return new FlowerPotBlock(block, lightLevel > 0 ? properties.lightLevel(blockState -> lightLevel) : properties);
	}

	private static String floating(String orig) {
		return "floating_" + orig;
	}

	private static String potted(String orig) {
		return "potted_" + orig;
	}

	private static String chibi(String orig) {
		return orig + "_chibi";
	}

	public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {
		for (var e : ALL.entrySet()) {
			r.accept(e.getValue(), botaniaRL(e.getKey()));
		}
	}

	public static void registerItemBlocks(BiConsumer<Item, ResourceLocation> r) {
		Item.Properties props = BotaniaItems.defaultBuilder();
		Item.Properties uncommonProps = BotaniaItems.defaultBuilder().rarity(Rarity.UNCOMMON);
		Item.Properties rareProps = BotaniaItems.defaultBuilder().rarity(Rarity.RARE);
		Item.Properties epicProps = BotaniaItems.defaultBuilder().rarity(Rarity.EPIC);
		Stream.<Function<DyeColor, Block>>of(
				BotaniaBlocks::getFlower,
				BotaniaBlocks::getShinyFlower,
				BotaniaBlocks::getFloatingFlower,
				BotaniaBlocks::getPetalBlock,
				BotaniaBlocks::getMushroom,
				BotaniaBlocks::getDoubleFlower
		).forEach(
				blockGetter -> ColorHelper.supportedColors().map(blockGetter).forEach(
						block -> r.accept(new ColoredBlockItem(block, ((Colored) block).getColor(), props), BuiltInRegistries.BLOCK.getKey(block))
				)
		);

		r.accept(new SpecialFlowerBlockItem(pureDaisy, props), BuiltInRegistries.BLOCK.getKey(pureDaisy));
		r.accept(new SpecialFlowerBlockItem(pureDaisyFloating, props), BuiltInRegistries.BLOCK.getKey(pureDaisyFloating));

		r.accept(new SpecialFlowerBlockItem(manastar, props), BuiltInRegistries.BLOCK.getKey(manastar));
		r.accept(new SpecialFlowerBlockItem(manastarFloating, props), BuiltInRegistries.BLOCK.getKey(manastarFloating));

		r.accept(new SpecialFlowerBlockItem(hydroangeas, props), BuiltInRegistries.BLOCK.getKey(hydroangeas));
		r.accept(new SpecialFlowerBlockItem(hydroangeasFloating, props), BuiltInRegistries.BLOCK.getKey(hydroangeasFloating));

		r.accept(new SpecialFlowerBlockItem(endoflame, props), BuiltInRegistries.BLOCK.getKey(endoflame));
		r.accept(new SpecialFlowerBlockItem(endoflameFloating, props), BuiltInRegistries.BLOCK.getKey(endoflameFloating));

		r.accept(new SpecialFlowerBlockItem(thermalily, props), BuiltInRegistries.BLOCK.getKey(thermalily));
		r.accept(new SpecialFlowerBlockItem(thermalilyFloating, props), BuiltInRegistries.BLOCK.getKey(thermalilyFloating));

		r.accept(new SpecialFlowerBlockItem(rosaArcana, props), BuiltInRegistries.BLOCK.getKey(rosaArcana));
		r.accept(new SpecialFlowerBlockItem(rosaArcanaFloating, props), BuiltInRegistries.BLOCK.getKey(rosaArcanaFloating));

		r.accept(new SpecialFlowerBlockItem(munchdew, props), BuiltInRegistries.BLOCK.getKey(munchdew));
		r.accept(new SpecialFlowerBlockItem(munchdewFloating, props), BuiltInRegistries.BLOCK.getKey(munchdewFloating));

		r.accept(new SpecialFlowerBlockItem(entropinnyum, props), BuiltInRegistries.BLOCK.getKey(entropinnyum));
		r.accept(new SpecialFlowerBlockItem(entropinnyumFloating, props), BuiltInRegistries.BLOCK.getKey(entropinnyumFloating));

		r.accept(new SpecialFlowerBlockItem(kekimurus, props), BuiltInRegistries.BLOCK.getKey(kekimurus));
		r.accept(new SpecialFlowerBlockItem(kekimurusFloating, props), BuiltInRegistries.BLOCK.getKey(kekimurusFloating));

		r.accept(new SpecialFlowerBlockItem(gourmaryllis, props), BuiltInRegistries.BLOCK.getKey(gourmaryllis));
		r.accept(new SpecialFlowerBlockItem(gourmaryllisFloating, props), BuiltInRegistries.BLOCK.getKey(gourmaryllisFloating));

		r.accept(new SpecialFlowerBlockItem(narslimmus, props), BuiltInRegistries.BLOCK.getKey(narslimmus));
		r.accept(new SpecialFlowerBlockItem(narslimmusFloating, props), BuiltInRegistries.BLOCK.getKey(narslimmusFloating));

		r.accept(new SpecialFlowerBlockItem(spectrolus, props), BuiltInRegistries.BLOCK.getKey(spectrolus));
		r.accept(new SpecialFlowerBlockItem(spectrolusFloating, props), BuiltInRegistries.BLOCK.getKey(spectrolusFloating));

		r.accept(new SpecialFlowerBlockItem(dandelifeon, rareProps), BuiltInRegistries.BLOCK.getKey(dandelifeon));
		r.accept(new SpecialFlowerBlockItem(dandelifeonFloating, rareProps), BuiltInRegistries.BLOCK.getKey(dandelifeonFloating));

		r.accept(new SpecialFlowerBlockItem(rafflowsia, props), BuiltInRegistries.BLOCK.getKey(rafflowsia));
		r.accept(new SpecialFlowerBlockItem(rafflowsiaFloating, props), BuiltInRegistries.BLOCK.getKey(rafflowsiaFloating));

		r.accept(new SpecialFlowerBlockItem(shulkMeNot, rareProps), BuiltInRegistries.BLOCK.getKey(shulkMeNot));
		r.accept(new SpecialFlowerBlockItem(shulkMeNotFloating, rareProps), BuiltInRegistries.BLOCK.getKey(shulkMeNotFloating));

		r.accept(new SpecialFlowerBlockItem(bellethorn, props), BuiltInRegistries.BLOCK.getKey(bellethorn));
		r.accept(new SpecialFlowerBlockItem(bellethornChibi, props), BuiltInRegistries.BLOCK.getKey(bellethornChibi));
		r.accept(new SpecialFlowerBlockItem(bellethornFloating, props), BuiltInRegistries.BLOCK.getKey(bellethornFloating));
		r.accept(new SpecialFlowerBlockItem(bellethornChibiFloating, props), BuiltInRegistries.BLOCK.getKey(bellethornChibiFloating));

		r.accept(new SpecialFlowerBlockItem(bergamute, props), BuiltInRegistries.BLOCK.getKey(bergamute));
		r.accept(new SpecialFlowerBlockItem(bergamuteFloating, props), BuiltInRegistries.BLOCK.getKey(bergamuteFloating));

		r.accept(new SpecialFlowerBlockItem(dreadthorn, props), BuiltInRegistries.BLOCK.getKey(dreadthorn));
		r.accept(new SpecialFlowerBlockItem(dreadthornFloating, props), BuiltInRegistries.BLOCK.getKey(dreadthornFloating));

		r.accept(new SpecialFlowerBlockItem(heiseiDream, props), BuiltInRegistries.BLOCK.getKey(heiseiDream));
		r.accept(new SpecialFlowerBlockItem(heiseiDreamFloating, props), BuiltInRegistries.BLOCK.getKey(heiseiDreamFloating));

		r.accept(new SpecialFlowerBlockItem(tigerseye, props), BuiltInRegistries.BLOCK.getKey(tigerseye));
		r.accept(new SpecialFlowerBlockItem(tigerseyeFloating, props), BuiltInRegistries.BLOCK.getKey(tigerseyeFloating));

		r.accept(new SpecialFlowerBlockItem(jadedAmaranthus, props), BuiltInRegistries.BLOCK.getKey(jadedAmaranthus));
		r.accept(new SpecialFlowerBlockItem(jadedAmaranthusFloating, props), BuiltInRegistries.BLOCK.getKey(jadedAmaranthusFloating));

		r.accept(new SpecialFlowerBlockItem(orechid, props), BuiltInRegistries.BLOCK.getKey(orechid));
		r.accept(new SpecialFlowerBlockItem(orechidFloating, props), BuiltInRegistries.BLOCK.getKey(orechidFloating));

		r.accept(new SpecialFlowerBlockItem(fallenKanade, props), BuiltInRegistries.BLOCK.getKey(fallenKanade));
		r.accept(new SpecialFlowerBlockItem(fallenKanadeFloating, props), BuiltInRegistries.BLOCK.getKey(fallenKanadeFloating));

		r.accept(new SpecialFlowerBlockItem(exoflame, props), BuiltInRegistries.BLOCK.getKey(exoflame));
		r.accept(new SpecialFlowerBlockItem(exoflameFloating, props), BuiltInRegistries.BLOCK.getKey(exoflameFloating));

		r.accept(new SpecialFlowerBlockItem(agricarnation, props), BuiltInRegistries.BLOCK.getKey(agricarnation));
		r.accept(new SpecialFlowerBlockItem(agricarnationChibi, props), BuiltInRegistries.BLOCK.getKey(agricarnationChibi));
		r.accept(new SpecialFlowerBlockItem(agricarnationFloating, props), BuiltInRegistries.BLOCK.getKey(agricarnationFloating));
		r.accept(new SpecialFlowerBlockItem(agricarnationChibiFloating, props), BuiltInRegistries.BLOCK.getKey(agricarnationChibiFloating));

		r.accept(new SpecialFlowerBlockItem(hopperhock, props), BuiltInRegistries.BLOCK.getKey(hopperhock));
		r.accept(new SpecialFlowerBlockItem(hopperhockChibi, props), BuiltInRegistries.BLOCK.getKey(hopperhockChibi));
		r.accept(new SpecialFlowerBlockItem(hopperhockFloating, props), BuiltInRegistries.BLOCK.getKey(hopperhockFloating));
		r.accept(new SpecialFlowerBlockItem(hopperhockChibiFloating, props), BuiltInRegistries.BLOCK.getKey(hopperhockChibiFloating));

		r.accept(new SpecialFlowerBlockItem(tangleberrie, props), BuiltInRegistries.BLOCK.getKey(tangleberrie));
		r.accept(new SpecialFlowerBlockItem(tangleberrieChibi, props), BuiltInRegistries.BLOCK.getKey(tangleberrieChibi));
		r.accept(new SpecialFlowerBlockItem(tangleberrieFloating, props), BuiltInRegistries.BLOCK.getKey(tangleberrieFloating));
		r.accept(new SpecialFlowerBlockItem(tangleberrieChibiFloating, props), BuiltInRegistries.BLOCK.getKey(tangleberrieChibiFloating));

		r.accept(new SpecialFlowerBlockItem(jiyuulia, props), BuiltInRegistries.BLOCK.getKey(jiyuulia));
		r.accept(new SpecialFlowerBlockItem(jiyuuliaChibi, props), BuiltInRegistries.BLOCK.getKey(jiyuuliaChibi));
		r.accept(new SpecialFlowerBlockItem(jiyuuliaFloating, props), BuiltInRegistries.BLOCK.getKey(jiyuuliaFloating));
		r.accept(new SpecialFlowerBlockItem(jiyuuliaChibiFloating, props), BuiltInRegistries.BLOCK.getKey(jiyuuliaChibiFloating));

		r.accept(new SpecialFlowerBlockItem(rannuncarpus, props), BuiltInRegistries.BLOCK.getKey(rannuncarpus));
		r.accept(new SpecialFlowerBlockItem(rannuncarpusChibi, props), BuiltInRegistries.BLOCK.getKey(rannuncarpusChibi));
		r.accept(new SpecialFlowerBlockItem(rannuncarpusFloating, props), BuiltInRegistries.BLOCK.getKey(rannuncarpusFloating));
		r.accept(new SpecialFlowerBlockItem(rannuncarpusChibiFloating, props), BuiltInRegistries.BLOCK.getKey(rannuncarpusChibiFloating));

		r.accept(new SpecialFlowerBlockItem(hyacidus, props), BuiltInRegistries.BLOCK.getKey(hyacidus));
		r.accept(new SpecialFlowerBlockItem(hyacidusFloating, props), BuiltInRegistries.BLOCK.getKey(hyacidusFloating));

		r.accept(new SpecialFlowerBlockItem(pollidisiac, props), BuiltInRegistries.BLOCK.getKey(pollidisiac));
		r.accept(new SpecialFlowerBlockItem(pollidisiacFloating, props), BuiltInRegistries.BLOCK.getKey(pollidisiacFloating));

		r.accept(new SpecialFlowerBlockItem(clayconia, props), BuiltInRegistries.BLOCK.getKey(clayconia));
		r.accept(new SpecialFlowerBlockItem(clayconiaChibi, props), BuiltInRegistries.BLOCK.getKey(clayconiaChibi));
		r.accept(new SpecialFlowerBlockItem(clayconiaFloating, props), BuiltInRegistries.BLOCK.getKey(clayconiaFloating));
		r.accept(new SpecialFlowerBlockItem(clayconiaChibiFloating, props), BuiltInRegistries.BLOCK.getKey(clayconiaChibiFloating));

		r.accept(new SpecialFlowerBlockItem(loonium, props), BuiltInRegistries.BLOCK.getKey(loonium));
		r.accept(new SpecialFlowerBlockItem(looniumFloating, props), BuiltInRegistries.BLOCK.getKey(looniumFloating));

		r.accept(new SpecialFlowerBlockItem(daffomill, props), BuiltInRegistries.BLOCK.getKey(daffomill));
		r.accept(new SpecialFlowerBlockItem(daffomillFloating, props), BuiltInRegistries.BLOCK.getKey(daffomillFloating));

		r.accept(new SpecialFlowerBlockItem(vinculotus, props), BuiltInRegistries.BLOCK.getKey(vinculotus));
		r.accept(new SpecialFlowerBlockItem(vinculotusFloating, props), BuiltInRegistries.BLOCK.getKey(vinculotusFloating));

		r.accept(new SpecialFlowerBlockItem(spectranthemum, props), BuiltInRegistries.BLOCK.getKey(spectranthemum));
		r.accept(new SpecialFlowerBlockItem(spectranthemumFloating, props), BuiltInRegistries.BLOCK.getKey(spectranthemumFloating));

		r.accept(new SpecialFlowerBlockItem(medumone, props), BuiltInRegistries.BLOCK.getKey(medumone));
		r.accept(new SpecialFlowerBlockItem(medumoneFloating, props), BuiltInRegistries.BLOCK.getKey(medumoneFloating));

		r.accept(new SpecialFlowerBlockItem(marimorphosis, props), BuiltInRegistries.BLOCK.getKey(marimorphosis));
		r.accept(new SpecialFlowerBlockItem(marimorphosisChibi, props), BuiltInRegistries.BLOCK.getKey(marimorphosisChibi));
		r.accept(new SpecialFlowerBlockItem(marimorphosisFloating, props), BuiltInRegistries.BLOCK.getKey(marimorphosisFloating));
		r.accept(new SpecialFlowerBlockItem(marimorphosisChibiFloating, props), BuiltInRegistries.BLOCK.getKey(marimorphosisChibiFloating));

		r.accept(new SpecialFlowerBlockItem(bubbell, props), BuiltInRegistries.BLOCK.getKey(bubbell));
		r.accept(new SpecialFlowerBlockItem(bubbellChibi, props), BuiltInRegistries.BLOCK.getKey(bubbellChibi));
		r.accept(new SpecialFlowerBlockItem(bubbellFloating, props), BuiltInRegistries.BLOCK.getKey(bubbellFloating));
		r.accept(new SpecialFlowerBlockItem(bubbellChibiFloating, props), BuiltInRegistries.BLOCK.getKey(bubbellChibiFloating));

		r.accept(new SpecialFlowerBlockItem(solegnolia, props), BuiltInRegistries.BLOCK.getKey(solegnolia));
		r.accept(new SpecialFlowerBlockItem(solegnoliaChibi, props), BuiltInRegistries.BLOCK.getKey(solegnoliaChibi));
		r.accept(new SpecialFlowerBlockItem(solegnoliaFloating, props), BuiltInRegistries.BLOCK.getKey(solegnoliaFloating));
		r.accept(new SpecialFlowerBlockItem(solegnoliaChibiFloating, props), BuiltInRegistries.BLOCK.getKey(solegnoliaChibiFloating));

		r.accept(new SpecialFlowerBlockItem(orechidIgnem, props), BuiltInRegistries.BLOCK.getKey(orechidIgnem));
		r.accept(new SpecialFlowerBlockItem(orechidIgnemFloating, props), BuiltInRegistries.BLOCK.getKey(orechidIgnemFloating));

		r.accept(new SpecialFlowerBlockItem(labellia, props), BuiltInRegistries.BLOCK.getKey(labellia));
		r.accept(new SpecialFlowerBlockItem(labelliaFloating, props), BuiltInRegistries.BLOCK.getKey(labelliaFloating));

		r.accept(new BlockItem(defaultAltar, props), BuiltInRegistries.BLOCK.getKey(defaultAltar));
		r.accept(new BlockItem(deepslateAltar, props), BuiltInRegistries.BLOCK.getKey(deepslateAltar));
		r.accept(new BlockItem(livingrockAltar, props), BuiltInRegistries.BLOCK.getKey(livingrockAltar));
		r.accept(new BlockItem(mossyAltar, props), BuiltInRegistries.BLOCK.getKey(mossyAltar));
		r.accept(new BlockItem(forestAltar, props), BuiltInRegistries.BLOCK.getKey(forestAltar));
		r.accept(new BlockItem(plainsAltar, props), BuiltInRegistries.BLOCK.getKey(plainsAltar));
		r.accept(new BlockItem(mountainAltar, props), BuiltInRegistries.BLOCK.getKey(mountainAltar));
		r.accept(new BlockItem(fungalAltar, props), BuiltInRegistries.BLOCK.getKey(fungalAltar));
		r.accept(new BlockItem(swampAltar, props), BuiltInRegistries.BLOCK.getKey(swampAltar));
		r.accept(new BlockItem(desertAltar, props), BuiltInRegistries.BLOCK.getKey(desertAltar));
		r.accept(new BlockItem(taigaAltar, props), BuiltInRegistries.BLOCK.getKey(taigaAltar));
		r.accept(new BlockItem(mesaAltar, props), BuiltInRegistries.BLOCK.getKey(mesaAltar));

		r.accept(new BlockItem(livingrock, props), BuiltInRegistries.BLOCK.getKey(livingrock));
		r.accept(new BlockItem(livingrockStairs, props), BuiltInRegistries.BLOCK.getKey(livingrockStairs));
		r.accept(new BlockItem(livingrockSlab, props), BuiltInRegistries.BLOCK.getKey(livingrockSlab));
		r.accept(new BlockItem(livingrockWall, props), BuiltInRegistries.BLOCK.getKey(livingrockWall));
		r.accept(new BlockItem(livingrockPolished, props), BuiltInRegistries.BLOCK.getKey(livingrockPolished));
		r.accept(new BlockItem(livingrockPolishedStairs, props), BuiltInRegistries.BLOCK.getKey(livingrockPolishedStairs));
		r.accept(new BlockItem(livingrockPolishedSlab, props), BuiltInRegistries.BLOCK.getKey(livingrockPolishedSlab));
		r.accept(new BlockItem(livingrockPolishedWall, props), BuiltInRegistries.BLOCK.getKey(livingrockPolishedWall));
		r.accept(new BlockItem(livingrockSlate, props), BuiltInRegistries.BLOCK.getKey(livingrockSlate));
		r.accept(new BlockItem(livingrockBrick, props), BuiltInRegistries.BLOCK.getKey(livingrockBrick));
		r.accept(new BlockItem(livingrockBrickStairs, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickStairs));
		r.accept(new BlockItem(livingrockBrickSlab, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickSlab));
		r.accept(new BlockItem(livingrockBrickWall, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickWall));
		r.accept(new BlockItem(livingrockBrickMossy, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickMossy));
		r.accept(new BlockItem(livingrockBrickMossyStairs, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickMossyStairs));
		r.accept(new BlockItem(livingrockBrickMossySlab, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickMossySlab));
		r.accept(new BlockItem(livingrockBrickMossyWall, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickMossyWall));
		r.accept(new BlockItem(livingrockBrickCracked, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickCracked));
		r.accept(new BlockItem(livingrockBrickChiseled, props), BuiltInRegistries.BLOCK.getKey(livingrockBrickChiseled));

		r.accept(new BlockItem(livingwoodLog, props), BuiltInRegistries.BLOCK.getKey(livingwoodLog));
		r.accept(new BlockItem(livingwood, props), BuiltInRegistries.BLOCK.getKey(livingwood));
		r.accept(new BlockItem(livingwoodStairs, props), BuiltInRegistries.BLOCK.getKey(livingwoodStairs));
		r.accept(new BlockItem(livingwoodSlab, props), BuiltInRegistries.BLOCK.getKey(livingwoodSlab));
		r.accept(new BlockItem(livingwoodWall, props), BuiltInRegistries.BLOCK.getKey(livingwoodWall));
		r.accept(new BlockItem(livingwoodLogStripped, props), BuiltInRegistries.BLOCK.getKey(livingwoodLogStripped));
		r.accept(new BlockItem(livingwoodStripped, props), BuiltInRegistries.BLOCK.getKey(livingwoodStripped));
		r.accept(new BlockItem(livingwoodStrippedStairs, props), BuiltInRegistries.BLOCK.getKey(livingwoodStrippedStairs));
		r.accept(new BlockItem(livingwoodStrippedSlab, props), BuiltInRegistries.BLOCK.getKey(livingwoodStrippedSlab));
		r.accept(new BlockItem(livingwoodStrippedWall, props), BuiltInRegistries.BLOCK.getKey(livingwoodStrippedWall));
		r.accept(new BlockItem(livingwoodLogGlimmering, props), BuiltInRegistries.BLOCK.getKey(livingwoodLogGlimmering));
		r.accept(new BlockItem(livingwoodGlimmering, props), BuiltInRegistries.BLOCK.getKey(livingwoodGlimmering));
		r.accept(new BlockItem(livingwoodLogStrippedGlimmering, props), BuiltInRegistries.BLOCK.getKey(livingwoodLogStrippedGlimmering));
		r.accept(new BlockItem(livingwoodStrippedGlimmering, props), BuiltInRegistries.BLOCK.getKey(livingwoodStrippedGlimmering));
		r.accept(new BlockItem(livingwoodPlanks, props), BuiltInRegistries.BLOCK.getKey(livingwoodPlanks));
		r.accept(new BlockItem(livingwoodPlankStairs, props), BuiltInRegistries.BLOCK.getKey(livingwoodPlankStairs));
		r.accept(new BlockItem(livingwoodPlankSlab, props), BuiltInRegistries.BLOCK.getKey(livingwoodPlankSlab));
		r.accept(new BlockItem(livingwoodFence, props), BuiltInRegistries.BLOCK.getKey(livingwoodFence));
		r.accept(new BlockItem(livingwoodFenceGate, props), BuiltInRegistries.BLOCK.getKey(livingwoodFenceGate));
		r.accept(new BlockItem(livingwoodDoor, props), BuiltInRegistries.BLOCK.getKey(livingwoodDoor));
		r.accept(new BlockItem(livingwoodTrapdoor, props), BuiltInRegistries.BLOCK.getKey(livingwoodTrapdoor));
		r.accept(new BlockItem(livingwoodPressurePlate, props), BuiltInRegistries.BLOCK.getKey(livingwoodPressurePlate));
		r.accept(new BlockItem(livingwoodButton, props), BuiltInRegistries.BLOCK.getKey(livingwoodButton));
		r.accept(new SignItem(BotaniaItems.defaultBuilder().stacksTo(16), BotaniaBlocks.livingwoodSign,
				BotaniaBlocks.livingwoodWallSign), BuiltInRegistries.BLOCK.getKey(livingwoodSign));
		r.accept(new HangingSignItem(BotaniaBlocks.livingwoodHangingSign, BotaniaBlocks.livingwoodWallHangingSign,
				BotaniaItems.defaultBuilder().stacksTo(16)), BuiltInRegistries.BLOCK.getKey(livingwoodHangingSign));
		r.accept(new BlockItem(livingwoodPlanksMossy, props), BuiltInRegistries.BLOCK.getKey(livingwoodPlanksMossy));
		r.accept(new BlockItem(livingwoodFramed, props), BuiltInRegistries.BLOCK.getKey(livingwoodFramed));
		r.accept(new BlockItem(livingwoodPatternFramed, props), BuiltInRegistries.BLOCK.getKey(livingwoodPatternFramed));

		r.accept(new BlockItem(dreamwoodLog, props), BuiltInRegistries.BLOCK.getKey(dreamwoodLog));
		r.accept(new BlockItem(dreamwood, props), BuiltInRegistries.BLOCK.getKey(dreamwood));
		r.accept(new BlockItem(dreamwoodStairs, props), BuiltInRegistries.BLOCK.getKey(dreamwoodStairs));
		r.accept(new BlockItem(dreamwoodSlab, props), BuiltInRegistries.BLOCK.getKey(dreamwoodSlab));
		r.accept(new BlockItem(dreamwoodWall, props), BuiltInRegistries.BLOCK.getKey(dreamwoodWall));
		r.accept(new BlockItem(dreamwoodLogStripped, props), BuiltInRegistries.BLOCK.getKey(dreamwoodLogStripped));
		r.accept(new BlockItem(dreamwoodStripped, props), BuiltInRegistries.BLOCK.getKey(dreamwoodStripped));
		r.accept(new BlockItem(dreamwoodStrippedStairs, props), BuiltInRegistries.BLOCK.getKey(dreamwoodStrippedStairs));
		r.accept(new BlockItem(dreamwoodStrippedSlab, props), BuiltInRegistries.BLOCK.getKey(dreamwoodStrippedSlab));
		r.accept(new BlockItem(dreamwoodStrippedWall, props), BuiltInRegistries.BLOCK.getKey(dreamwoodStrippedWall));
		r.accept(new BlockItem(dreamwoodLogGlimmering, props), BuiltInRegistries.BLOCK.getKey(dreamwoodLogGlimmering));
		r.accept(new BlockItem(dreamwoodGlimmering, props), BuiltInRegistries.BLOCK.getKey(dreamwoodGlimmering));
		r.accept(new BlockItem(dreamwoodLogStrippedGlimmering, props), BuiltInRegistries.BLOCK.getKey(dreamwoodLogStrippedGlimmering));
		r.accept(new BlockItem(dreamwoodStrippedGlimmering, props), BuiltInRegistries.BLOCK.getKey(dreamwoodStrippedGlimmering));
		r.accept(new BlockItem(dreamwoodPlanks, props), BuiltInRegistries.BLOCK.getKey(dreamwoodPlanks));
		r.accept(new BlockItem(dreamwoodPlankStairs, props), BuiltInRegistries.BLOCK.getKey(dreamwoodPlankStairs));
		r.accept(new BlockItem(dreamwoodPlankSlab, props), BuiltInRegistries.BLOCK.getKey(dreamwoodPlankSlab));
		r.accept(new BlockItem(dreamwoodFence, props), BuiltInRegistries.BLOCK.getKey(dreamwoodFence));
		r.accept(new BlockItem(dreamwoodFenceGate, props), BuiltInRegistries.BLOCK.getKey(dreamwoodFenceGate));
		r.accept(new BlockItem(dreamwoodDoor, props), BuiltInRegistries.BLOCK.getKey(dreamwoodDoor));
		r.accept(new BlockItem(dreamwoodTrapdoor, props), BuiltInRegistries.BLOCK.getKey(dreamwoodTrapdoor));
		r.accept(new BlockItem(dreamwoodPressurePlate, props), BuiltInRegistries.BLOCK.getKey(dreamwoodPressurePlate));
		r.accept(new BlockItem(dreamwoodButton, props), BuiltInRegistries.BLOCK.getKey(dreamwoodButton));
		r.accept(new SignItem(BotaniaItems.defaultBuilder().stacksTo(16), BotaniaBlocks.dreamwoodSign,
				BotaniaBlocks.dreamwoodWallSign), BuiltInRegistries.BLOCK.getKey(dreamwoodSign));
		r.accept(new HangingSignItem(BotaniaBlocks.dreamwoodHangingSign, BotaniaBlocks.dreamwoodWallHangingSign,
				BotaniaItems.defaultBuilder().stacksTo(16)), BuiltInRegistries.BLOCK.getKey(dreamwoodHangingSign));
		r.accept(new BlockItem(dreamwoodPlanksMossy, props), BuiltInRegistries.BLOCK.getKey(dreamwoodPlanksMossy));
		r.accept(new BlockItem(dreamwoodFramed, props), BuiltInRegistries.BLOCK.getKey(dreamwoodFramed));
		r.accept(new BlockItem(dreamwoodPatternFramed, props), BuiltInRegistries.BLOCK.getKey(dreamwoodPatternFramed));

		r.accept(new BlockItem(manaSpreader, props), BuiltInRegistries.BLOCK.getKey(manaSpreader));
		r.accept(new BlockItem(redstoneSpreader, props), BuiltInRegistries.BLOCK.getKey(redstoneSpreader));
		r.accept(new BlockItem(elvenSpreader, props), BuiltInRegistries.BLOCK.getKey(elvenSpreader));
		r.accept(new BlockItem(gaiaSpreader, rareProps), BuiltInRegistries.BLOCK.getKey(gaiaSpreader));
		r.accept(new BlockItem(manaPool, props), BuiltInRegistries.BLOCK.getKey(manaPool));
		r.accept(new BlockItem(creativePool, epicProps), BuiltInRegistries.BLOCK.getKey(creativePool));
		r.accept(new BlockItem(dilutedPool, props), BuiltInRegistries.BLOCK.getKey(dilutedPool));
		r.accept(new BlockItem(fabulousPool, props), BuiltInRegistries.BLOCK.getKey(fabulousPool));
		r.accept(new BlockItem(alchemyCatalyst, props), BuiltInRegistries.BLOCK.getKey(alchemyCatalyst));
		r.accept(new BlockItem(conjurationCatalyst, props), BuiltInRegistries.BLOCK.getKey(conjurationCatalyst));
		r.accept(new BlockItem(manasteelBlock, props), BuiltInRegistries.BLOCK.getKey(manasteelBlock));
		r.accept(new BlockItem(terrasteelBlock, uncommonProps), BuiltInRegistries.BLOCK.getKey(terrasteelBlock));
		r.accept(new BlockItem(elementiumBlock, props), BuiltInRegistries.BLOCK.getKey(elementiumBlock));
		r.accept(new BlockItem(manaDiamondBlock, props), BuiltInRegistries.BLOCK.getKey(manaDiamondBlock));
		r.accept(new BlockItem(dragonstoneBlock, props), BuiltInRegistries.BLOCK.getKey(dragonstoneBlock));
		r.accept(new BlockItem(manaGlass, props), BuiltInRegistries.BLOCK.getKey(manaGlass));
		r.accept(new BlockItem(elfGlass, props), BuiltInRegistries.BLOCK.getKey(elfGlass));
		r.accept(new BlockItem(bifrostPerm, props), BuiltInRegistries.BLOCK.getKey(bifrostPerm));
		r.accept(new BlockItem(runeAltar, props), BuiltInRegistries.BLOCK.getKey(runeAltar));
		r.accept(new BlockItem(enchanter, props), BuiltInRegistries.BLOCK.getKey(enchanter));
		r.accept(new BlockItem(brewery, props), BuiltInRegistries.BLOCK.getKey(brewery));
		r.accept(new BlockItem(terraPlate, props), BuiltInRegistries.BLOCK.getKey(terraPlate));
		r.accept(new BlockItem(alfPortal, uncommonProps), BuiltInRegistries.BLOCK.getKey(alfPortal));

		r.accept(new BlockItem(manaPylon, props), BuiltInRegistries.BLOCK.getKey(manaPylon));
		r.accept(new BlockItem(naturaPylon, uncommonProps), BuiltInRegistries.BLOCK.getKey(naturaPylon));
		r.accept(new BlockItem(gaiaPylon, props), BuiltInRegistries.BLOCK.getKey(gaiaPylon));
		r.accept(new BlockItem(distributor, props), BuiltInRegistries.BLOCK.getKey(distributor));
		r.accept(new BlockItem(manaVoid, props), BuiltInRegistries.BLOCK.getKey(manaVoid));
		r.accept(new BlockItem(manaDetector, props), BuiltInRegistries.BLOCK.getKey(manaDetector));
		r.accept(new BlockItem(pistonRelay, props), BuiltInRegistries.BLOCK.getKey(pistonRelay));
		r.accept(new BlockItem(turntable, props), BuiltInRegistries.BLOCK.getKey(turntable));
		r.accept(new BlockItem(tinyPlanet, props), BuiltInRegistries.BLOCK.getKey(tinyPlanet));
		r.accept(new BlockItem(wildDrum, props), BuiltInRegistries.BLOCK.getKey(wildDrum));
		r.accept(new BlockItem(gatheringDrum, props), BuiltInRegistries.BLOCK.getKey(gatheringDrum));
		r.accept(new BlockItem(canopyDrum, props), BuiltInRegistries.BLOCK.getKey(canopyDrum));
		r.accept(new BlockItem(spawnerClaw, props), BuiltInRegistries.BLOCK.getKey(spawnerClaw));
		r.accept(new BlockItem(rfGenerator, props), BuiltInRegistries.BLOCK.getKey(rfGenerator));
		r.accept(new BlockItem(prism, props), BuiltInRegistries.BLOCK.getKey(prism));
		r.accept(new BlockItem(pump, props), BuiltInRegistries.BLOCK.getKey(pump));
		r.accept(new BlockItem(sparkChanger, props), BuiltInRegistries.BLOCK.getKey(sparkChanger));
		r.accept(new BlockItem(manaBomb, rareProps), BuiltInRegistries.BLOCK.getKey(manaBomb));
		r.accept(new BlockItem(bellows, props), BuiltInRegistries.BLOCK.getKey(bellows));
		r.accept(new BlockItem(openCrate, props), BuiltInRegistries.BLOCK.getKey(openCrate));
		r.accept(new BlockItem(craftCrate, props), BuiltInRegistries.BLOCK.getKey(craftCrate));
		r.accept(new BlockItem(forestEye, props), BuiltInRegistries.BLOCK.getKey(forestEye));
		r.accept(new BlockItem(abstrusePlatform, props), BuiltInRegistries.BLOCK.getKey(abstrusePlatform));
		r.accept(new BlockItem(spectralPlatform, props), BuiltInRegistries.BLOCK.getKey(spectralPlatform));
		r.accept(new BlockItem(infrangiblePlatform, epicProps), BuiltInRegistries.BLOCK.getKey(infrangiblePlatform));
		r.accept(new TinyPotatoBlockItem(tinyPotato, props), BuiltInRegistries.BLOCK.getKey(tinyPotato));
		r.accept(new BlockItem(enderEye, props), BuiltInRegistries.BLOCK.getKey(enderEye));
		r.accept(new BlockItem(redStringContainer, props), BuiltInRegistries.BLOCK.getKey(redStringContainer));
		r.accept(new BlockItem(redStringDispenser, props), BuiltInRegistries.BLOCK.getKey(redStringDispenser));
		r.accept(new BlockItem(redStringFertilizer, props), BuiltInRegistries.BLOCK.getKey(redStringFertilizer));
		r.accept(new BlockItem(redStringComparator, props), BuiltInRegistries.BLOCK.getKey(redStringComparator));
		r.accept(new BlockItem(redStringRelay, props), BuiltInRegistries.BLOCK.getKey(redStringRelay));
		r.accept(new BlockItem(redStringInterceptor, props), BuiltInRegistries.BLOCK.getKey(redStringInterceptor));
		r.accept(new BlockItem(corporeaFunnel, props), BuiltInRegistries.BLOCK.getKey(corporeaFunnel));
		r.accept(new BlockItem(corporeaInterceptor, props), BuiltInRegistries.BLOCK.getKey(corporeaInterceptor));
		r.accept(new BlockItem(corporeaIndex, props), BuiltInRegistries.BLOCK.getKey(corporeaIndex));
		r.accept(new BlockItem(corporeaCrystalCube, props), BuiltInRegistries.BLOCK.getKey(corporeaCrystalCube));
		r.accept(new BlockItem(corporeaRetainer, props), BuiltInRegistries.BLOCK.getKey(corporeaRetainer));
		r.accept(new BlockItem(corporeaBlock, props), BuiltInRegistries.BLOCK.getKey(corporeaBlock));
		r.accept(new BlockItem(corporeaStairs, props), BuiltInRegistries.BLOCK.getKey(corporeaStairs));
		r.accept(new BlockItem(corporeaSlab, props), BuiltInRegistries.BLOCK.getKey(corporeaSlab));
		r.accept(new BlockItem(corporeaBrick, props), BuiltInRegistries.BLOCK.getKey(corporeaBrick));
		r.accept(new BlockItem(corporeaBrickStairs, props), BuiltInRegistries.BLOCK.getKey(corporeaBrickStairs));
		r.accept(new BlockItem(corporeaBrickSlab, props), BuiltInRegistries.BLOCK.getKey(corporeaBrickSlab));
		r.accept(new BlockItem(corporeaBrickWall, props), BuiltInRegistries.BLOCK.getKey(corporeaBrickWall));
		r.accept(new BlockItem(incensePlate, props), BuiltInRegistries.BLOCK.getKey(incensePlate));
		r.accept(new BlockItem(hourglass, props), BuiltInRegistries.BLOCK.getKey(hourglass));
		r.accept(new BlockItem(ghostRail, props), BuiltInRegistries.BLOCK.getKey(ghostRail));
		r.accept(new BlockItem(lightRelayDefault, props), BuiltInRegistries.BLOCK.getKey(lightRelayDefault));
		r.accept(new BlockItem(lightRelayDetector, props), BuiltInRegistries.BLOCK.getKey(lightRelayDetector));
		r.accept(new BlockItem(lightRelayFork, props), BuiltInRegistries.BLOCK.getKey(lightRelayFork));
		r.accept(new BlockItem(lightRelayToggle, props), BuiltInRegistries.BLOCK.getKey(lightRelayToggle));
		r.accept(new BlockItem(lightLauncher, props), BuiltInRegistries.BLOCK.getKey(lightLauncher));
		r.accept(new BlockItem(cacophonium, props), BuiltInRegistries.BLOCK.getKey(cacophonium));
		r.accept(new BlockItem(cellBlock, props), BuiltInRegistries.BLOCK.getKey(cellBlock));
		r.accept(new BlockItem(teruTeruBozu, props), BuiltInRegistries.BLOCK.getKey(teruTeruBozu));
		r.accept(new BlockItem(avatar, props), BuiltInRegistries.BLOCK.getKey(avatar));
		r.accept(new BlockItem(root, props), BuiltInRegistries.BLOCK.getKey(root));
		r.accept(new BlockItem(felPumpkin, props), BuiltInRegistries.BLOCK.getKey(felPumpkin));
		r.accept(new BlockItem(cocoon, props), BuiltInRegistries.BLOCK.getKey(cocoon));
		r.accept(new BlockItem(enchantedSoil, rareProps), BuiltInRegistries.BLOCK.getKey(enchantedSoil));
		r.accept(new BlockItem(animatedTorch, props), BuiltInRegistries.BLOCK.getKey(animatedTorch));
		r.accept(new BlockItem(starfield, props), BuiltInRegistries.BLOCK.getKey(starfield));
		r.accept(new BlockItem(azulejo0, props), BuiltInRegistries.BLOCK.getKey(azulejo0));
		r.accept(new BlockItem(azulejo1, props), BuiltInRegistries.BLOCK.getKey(azulejo1));
		r.accept(new BlockItem(azulejo2, props), BuiltInRegistries.BLOCK.getKey(azulejo2));
		r.accept(new BlockItem(azulejo3, props), BuiltInRegistries.BLOCK.getKey(azulejo3));
		r.accept(new BlockItem(azulejo4, props), BuiltInRegistries.BLOCK.getKey(azulejo4));
		r.accept(new BlockItem(azulejo5, props), BuiltInRegistries.BLOCK.getKey(azulejo5));
		r.accept(new BlockItem(azulejo6, props), BuiltInRegistries.BLOCK.getKey(azulejo6));
		r.accept(new BlockItem(azulejo7, props), BuiltInRegistries.BLOCK.getKey(azulejo7));
		r.accept(new BlockItem(azulejo8, props), BuiltInRegistries.BLOCK.getKey(azulejo8));
		r.accept(new BlockItem(azulejo9, props), BuiltInRegistries.BLOCK.getKey(azulejo9));
		r.accept(new BlockItem(azulejo10, props), BuiltInRegistries.BLOCK.getKey(azulejo10));
		r.accept(new BlockItem(azulejo11, props), BuiltInRegistries.BLOCK.getKey(azulejo11));
		r.accept(new BlockItem(azulejo12, props), BuiltInRegistries.BLOCK.getKey(azulejo12));
		r.accept(new BlockItem(azulejo13, props), BuiltInRegistries.BLOCK.getKey(azulejo13));
		r.accept(new BlockItem(azulejo14, props), BuiltInRegistries.BLOCK.getKey(azulejo14));
		r.accept(new BlockItem(azulejo15, props), BuiltInRegistries.BLOCK.getKey(azulejo15));
		r.accept(new BlockItem(blazeBlock, props), BuiltInRegistries.BLOCK.getKey(blazeBlock));
		r.accept(new StandingAndWallBlockItem(gaiaHead, gaiaHeadWall, rareProps, Direction.DOWN), BuiltInRegistries.BLOCK.getKey(gaiaHead));
		r.accept(new BlockItem(shimmerrock, props), BuiltInRegistries.BLOCK.getKey(shimmerrock));
		r.accept(new BlockItem(shimmerrockStairs, props), BuiltInRegistries.BLOCK.getKey(shimmerrockStairs));
		r.accept(new BlockItem(shimmerrockSlab, props), BuiltInRegistries.BLOCK.getKey(shimmerrockSlab));
		r.accept(new BlockItem(shimmerwoodPlanks, props), BuiltInRegistries.BLOCK.getKey(shimmerwoodPlanks));
		r.accept(new BlockItem(shimmerwoodPlankStairs, props), BuiltInRegistries.BLOCK.getKey(shimmerwoodPlankStairs));
		r.accept(new BlockItem(shimmerwoodPlankSlab, props), BuiltInRegistries.BLOCK.getKey(shimmerwoodPlankSlab));
		r.accept(new BlockItem(dryGrass, props), BuiltInRegistries.BLOCK.getKey(dryGrass));
		r.accept(new BlockItem(goldenGrass, props), BuiltInRegistries.BLOCK.getKey(goldenGrass));
		r.accept(new BlockItem(vividGrass, props), BuiltInRegistries.BLOCK.getKey(vividGrass));
		r.accept(new BlockItem(scorchedGrass, props), BuiltInRegistries.BLOCK.getKey(scorchedGrass));
		r.accept(new BlockItem(infusedGrass, props), BuiltInRegistries.BLOCK.getKey(infusedGrass));
		r.accept(new BlockItem(mutatedGrass, props), BuiltInRegistries.BLOCK.getKey(mutatedGrass));
		r.accept(new BlockItem(motifDaybloom, props), BuiltInRegistries.BLOCK.getKey(motifDaybloom));
		r.accept(new BlockItem(motifNightshade, props), BuiltInRegistries.BLOCK.getKey(motifNightshade));
		r.accept(new BlockItem(motifHydroangeas, props), BuiltInRegistries.BLOCK.getKey(motifHydroangeas));

		r.accept(new BlockItem(darkQuartz, props), BuiltInRegistries.BLOCK.getKey(darkQuartz));
		r.accept(new BlockItem(darkQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(darkQuartzStairs));
		r.accept(new BlockItem(darkQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(darkQuartzSlab));
		r.accept(new BlockItem(darkQuartzChiseled, props), BuiltInRegistries.BLOCK.getKey(darkQuartzChiseled));
		r.accept(new BlockItem(darkQuartzBricks, props), BuiltInRegistries.BLOCK.getKey(darkQuartzBricks));
		r.accept(new BlockItem(darkQuartzPillar, props), BuiltInRegistries.BLOCK.getKey(darkQuartzPillar));
		r.accept(new BlockItem(darkSmoothQuartz, props), BuiltInRegistries.BLOCK.getKey(darkSmoothQuartz));
		r.accept(new BlockItem(darkSmoothQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(darkSmoothQuartzStairs));
		r.accept(new BlockItem(darkSmoothQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(darkSmoothQuartzSlab));

		r.accept(new BlockItem(manaQuartz, props), BuiltInRegistries.BLOCK.getKey(manaQuartz));
		r.accept(new BlockItem(manaQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(manaQuartzStairs));
		r.accept(new BlockItem(manaQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(manaQuartzSlab));
		r.accept(new BlockItem(manaQuartzChiseled, props), BuiltInRegistries.BLOCK.getKey(manaQuartzChiseled));
		r.accept(new BlockItem(manaQuartzBricks, props), BuiltInRegistries.BLOCK.getKey(manaQuartzBricks));
		r.accept(new BlockItem(manaQuartzPillar, props), BuiltInRegistries.BLOCK.getKey(manaQuartzPillar));
		r.accept(new BlockItem(manaSmoothQuartz, props), BuiltInRegistries.BLOCK.getKey(manaSmoothQuartz));
		r.accept(new BlockItem(manaSmoothQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(manaSmoothQuartzStairs));
		r.accept(new BlockItem(manaSmoothQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(manaSmoothQuartzSlab));

		r.accept(new BlockItem(blazeQuartz, props), BuiltInRegistries.BLOCK.getKey(blazeQuartz));
		r.accept(new BlockItem(blazeQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(blazeQuartzStairs));
		r.accept(new BlockItem(blazeQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(blazeQuartzSlab));
		r.accept(new BlockItem(blazeQuartzChiseled, props), BuiltInRegistries.BLOCK.getKey(blazeQuartzChiseled));
		r.accept(new BlockItem(blazeQuartzBricks, props), BuiltInRegistries.BLOCK.getKey(blazeQuartzBricks));
		r.accept(new BlockItem(blazeQuartzPillar, props), BuiltInRegistries.BLOCK.getKey(blazeQuartzPillar));
		r.accept(new BlockItem(blazeSmoothQuartz, props), BuiltInRegistries.BLOCK.getKey(blazeSmoothQuartz));
		r.accept(new BlockItem(blazeSmoothQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(blazeSmoothQuartzStairs));
		r.accept(new BlockItem(blazeSmoothQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(blazeSmoothQuartzSlab));

		r.accept(new BlockItem(lavenderQuartz, props), BuiltInRegistries.BLOCK.getKey(lavenderQuartz));
		r.accept(new BlockItem(lavenderQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(lavenderQuartzStairs));
		r.accept(new BlockItem(lavenderQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(lavenderQuartzSlab));
		r.accept(new BlockItem(lavenderQuartzChiseled, props), BuiltInRegistries.BLOCK.getKey(lavenderQuartzChiseled));
		r.accept(new BlockItem(lavenderQuartzBricks, props), BuiltInRegistries.BLOCK.getKey(lavenderQuartzBricks));
		r.accept(new BlockItem(lavenderQuartzPillar, props), BuiltInRegistries.BLOCK.getKey(lavenderQuartzPillar));
		r.accept(new BlockItem(lavenderSmoothQuartz, props), BuiltInRegistries.BLOCK.getKey(lavenderSmoothQuartz));
		r.accept(new BlockItem(lavenderSmoothQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(lavenderSmoothQuartzStairs));
		r.accept(new BlockItem(lavenderSmoothQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(lavenderSmoothQuartzSlab));

		r.accept(new BlockItem(redQuartz, props), BuiltInRegistries.BLOCK.getKey(redQuartz));
		r.accept(new BlockItem(redQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(redQuartzStairs));
		r.accept(new BlockItem(redQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(redQuartzSlab));
		r.accept(new BlockItem(redQuartzChiseled, props), BuiltInRegistries.BLOCK.getKey(redQuartzChiseled));
		r.accept(new BlockItem(redQuartzBricks, props), BuiltInRegistries.BLOCK.getKey(redQuartzBricks));
		r.accept(new BlockItem(redQuartzPillar, props), BuiltInRegistries.BLOCK.getKey(redQuartzPillar));
		r.accept(new BlockItem(redSmoothQuartz, props), BuiltInRegistries.BLOCK.getKey(redSmoothQuartz));
		r.accept(new BlockItem(redSmoothQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(redSmoothQuartzStairs));
		r.accept(new BlockItem(redSmoothQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(redSmoothQuartzSlab));

		r.accept(new BlockItem(elfQuartz, props), BuiltInRegistries.BLOCK.getKey(elfQuartz));
		r.accept(new BlockItem(elfQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(elfQuartzStairs));
		r.accept(new BlockItem(elfQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(elfQuartzSlab));
		r.accept(new BlockItem(elfQuartzChiseled, props), BuiltInRegistries.BLOCK.getKey(elfQuartzChiseled));
		r.accept(new BlockItem(elfQuartzBricks, props), BuiltInRegistries.BLOCK.getKey(elfQuartzBricks));
		r.accept(new BlockItem(elfQuartzPillar, props), BuiltInRegistries.BLOCK.getKey(elfQuartzPillar));
		r.accept(new BlockItem(elfSmoothQuartz, props), BuiltInRegistries.BLOCK.getKey(elfSmoothQuartz));
		r.accept(new BlockItem(elfSmoothQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(elfSmoothQuartzStairs));
		r.accept(new BlockItem(elfSmoothQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(elfSmoothQuartzSlab));

		r.accept(new BlockItem(sunnyQuartz, props), BuiltInRegistries.BLOCK.getKey(sunnyQuartz));
		r.accept(new BlockItem(sunnyQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(sunnyQuartzStairs));
		r.accept(new BlockItem(sunnyQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(sunnyQuartzSlab));
		r.accept(new BlockItem(sunnyQuartzChiseled, props), BuiltInRegistries.BLOCK.getKey(sunnyQuartzChiseled));
		r.accept(new BlockItem(sunnyQuartzBricks, props), BuiltInRegistries.BLOCK.getKey(sunnyQuartzBricks));
		r.accept(new BlockItem(sunnyQuartzPillar, props), BuiltInRegistries.BLOCK.getKey(sunnyQuartzPillar));
		r.accept(new BlockItem(sunnySmoothQuartz, props), BuiltInRegistries.BLOCK.getKey(sunnySmoothQuartz));
		r.accept(new BlockItem(sunnySmoothQuartzStairs, props), BuiltInRegistries.BLOCK.getKey(sunnySmoothQuartzStairs));
		r.accept(new BlockItem(sunnySmoothQuartzSlab, props), BuiltInRegistries.BLOCK.getKey(sunnySmoothQuartzSlab));

		r.accept(new BlockItem(biomeStoneForest, props), BuiltInRegistries.BLOCK.getKey(biomeStoneForest));
		r.accept(new BlockItem(biomeStoneForestStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStoneForestStairs));
		r.accept(new BlockItem(biomeStoneForestSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStoneForestSlab));
		r.accept(new BlockItem(biomeStoneForestWall, props), BuiltInRegistries.BLOCK.getKey(biomeStoneForestWall));
		r.accept(new BlockItem(biomeBrickForest, props), BuiltInRegistries.BLOCK.getKey(biomeBrickForest));
		r.accept(new BlockItem(biomeBrickForestStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickForestStairs));
		r.accept(new BlockItem(biomeBrickForestSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickForestSlab));
		r.accept(new BlockItem(biomeBrickForestWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickForestWall));
		r.accept(new BlockItem(biomeCobblestoneForest, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneForest));
		r.accept(new BlockItem(biomeCobblestoneForestStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneForestStairs));
		r.accept(new BlockItem(biomeCobblestoneForestSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneForestSlab));
		r.accept(new BlockItem(biomeCobblestoneForestWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneForestWall));
		r.accept(new BlockItem(biomeChiseledBrickForest, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickForest));

		r.accept(new BlockItem(biomeStonePlains, props), BuiltInRegistries.BLOCK.getKey(biomeStonePlains));
		r.accept(new BlockItem(biomeStonePlainsStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStonePlainsStairs));
		r.accept(new BlockItem(biomeStonePlainsSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStonePlainsSlab));
		r.accept(new BlockItem(biomeStonePlainsWall, props), BuiltInRegistries.BLOCK.getKey(biomeStonePlainsWall));
		r.accept(new BlockItem(biomeBrickPlains, props), BuiltInRegistries.BLOCK.getKey(biomeBrickPlains));
		r.accept(new BlockItem(biomeBrickPlainsStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickPlainsStairs));
		r.accept(new BlockItem(biomeBrickPlainsSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickPlainsSlab));
		r.accept(new BlockItem(biomeBrickPlainsWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickPlainsWall));
		r.accept(new BlockItem(biomeCobblestonePlains, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestonePlains));
		r.accept(new BlockItem(biomeCobblestonePlainsStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestonePlainsStairs));
		r.accept(new BlockItem(biomeCobblestonePlainsSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestonePlainsSlab));
		r.accept(new BlockItem(biomeCobblestonePlainsWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestonePlainsWall));
		r.accept(new BlockItem(biomeChiseledBrickPlains, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickPlains));

		r.accept(new BlockItem(biomeStoneMountain, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMountain));
		r.accept(new BlockItem(biomeStoneMountainStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMountainStairs));
		r.accept(new BlockItem(biomeStoneMountainSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMountainSlab));
		r.accept(new BlockItem(biomeStoneMountainWall, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMountainWall));
		r.accept(new BlockItem(biomeBrickMountain, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMountain));
		r.accept(new BlockItem(biomeBrickMountainStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMountainStairs));
		r.accept(new BlockItem(biomeBrickMountainSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMountainSlab));
		r.accept(new BlockItem(biomeBrickMountainWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMountainWall));
		r.accept(new BlockItem(biomeCobblestoneMountain, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMountain));
		r.accept(new BlockItem(biomeCobblestoneMountainStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMountainStairs));
		r.accept(new BlockItem(biomeCobblestoneMountainSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMountainSlab));
		r.accept(new BlockItem(biomeCobblestoneMountainWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMountainWall));
		r.accept(new BlockItem(biomeChiseledBrickMountain, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickMountain));

		r.accept(new BlockItem(biomeStoneFungal, props), BuiltInRegistries.BLOCK.getKey(biomeStoneFungal));
		r.accept(new BlockItem(biomeStoneFungalStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStoneFungalStairs));
		r.accept(new BlockItem(biomeStoneFungalSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStoneFungalSlab));
		r.accept(new BlockItem(biomeStoneFungalWall, props), BuiltInRegistries.BLOCK.getKey(biomeStoneFungalWall));
		r.accept(new BlockItem(biomeBrickFungal, props), BuiltInRegistries.BLOCK.getKey(biomeBrickFungal));
		r.accept(new BlockItem(biomeBrickFungalStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickFungalStairs));
		r.accept(new BlockItem(biomeBrickFungalSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickFungalSlab));
		r.accept(new BlockItem(biomeBrickFungalWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickFungalWall));
		r.accept(new BlockItem(biomeCobblestoneFungal, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneFungal));
		r.accept(new BlockItem(biomeCobblestoneFungalStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneFungalStairs));
		r.accept(new BlockItem(biomeCobblestoneFungalSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneFungalSlab));
		r.accept(new BlockItem(biomeCobblestoneFungalWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneFungalWall));
		r.accept(new BlockItem(biomeChiseledBrickFungal, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickFungal));

		r.accept(new BlockItem(biomeStoneSwamp, props), BuiltInRegistries.BLOCK.getKey(biomeStoneSwamp));
		r.accept(new BlockItem(biomeStoneSwampStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStoneSwampStairs));
		r.accept(new BlockItem(biomeStoneSwampSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStoneSwampSlab));
		r.accept(new BlockItem(biomeStoneSwampWall, props), BuiltInRegistries.BLOCK.getKey(biomeStoneSwampWall));
		r.accept(new BlockItem(biomeBrickSwamp, props), BuiltInRegistries.BLOCK.getKey(biomeBrickSwamp));
		r.accept(new BlockItem(biomeBrickSwampStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickSwampStairs));
		r.accept(new BlockItem(biomeBrickSwampSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickSwampSlab));
		r.accept(new BlockItem(biomeBrickSwampWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickSwampWall));
		r.accept(new BlockItem(biomeCobblestoneSwamp, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneSwamp));
		r.accept(new BlockItem(biomeCobblestoneSwampStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneSwampStairs));
		r.accept(new BlockItem(biomeCobblestoneSwampSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneSwampSlab));
		r.accept(new BlockItem(biomeCobblestoneSwampWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneSwampWall));
		r.accept(new BlockItem(biomeChiseledBrickSwamp, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickSwamp));

		r.accept(new BlockItem(biomeStoneDesert, props), BuiltInRegistries.BLOCK.getKey(biomeStoneDesert));
		r.accept(new BlockItem(biomeStoneDesertStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStoneDesertStairs));
		r.accept(new BlockItem(biomeStoneDesertSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStoneDesertSlab));
		r.accept(new BlockItem(biomeStoneDesertWall, props), BuiltInRegistries.BLOCK.getKey(biomeStoneDesertWall));
		r.accept(new BlockItem(biomeBrickDesert, props), BuiltInRegistries.BLOCK.getKey(biomeBrickDesert));
		r.accept(new BlockItem(biomeBrickDesertStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickDesertStairs));
		r.accept(new BlockItem(biomeBrickDesertSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickDesertSlab));
		r.accept(new BlockItem(biomeBrickDesertWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickDesertWall));
		r.accept(new BlockItem(biomeCobblestoneDesert, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneDesert));
		r.accept(new BlockItem(biomeCobblestoneDesertStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneDesertStairs));
		r.accept(new BlockItem(biomeCobblestoneDesertSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneDesertSlab));
		r.accept(new BlockItem(biomeCobblestoneDesertWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneDesertWall));
		r.accept(new BlockItem(biomeChiseledBrickDesert, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickDesert));

		r.accept(new BlockItem(biomeStoneTaiga, props), BuiltInRegistries.BLOCK.getKey(biomeStoneTaiga));
		r.accept(new BlockItem(biomeStoneTaigaStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStoneTaigaStairs));
		r.accept(new BlockItem(biomeStoneTaigaSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStoneTaigaSlab));
		r.accept(new BlockItem(biomeStoneTaigaWall, props), BuiltInRegistries.BLOCK.getKey(biomeStoneTaigaWall));
		r.accept(new BlockItem(biomeBrickTaiga, props), BuiltInRegistries.BLOCK.getKey(biomeBrickTaiga));
		r.accept(new BlockItem(biomeBrickTaigaStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickTaigaStairs));
		r.accept(new BlockItem(biomeBrickTaigaSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickTaigaSlab));
		r.accept(new BlockItem(biomeBrickTaigaWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickTaigaWall));
		r.accept(new BlockItem(biomeCobblestoneTaiga, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneTaiga));
		r.accept(new BlockItem(biomeCobblestoneTaigaStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneTaigaStairs));
		r.accept(new BlockItem(biomeCobblestoneTaigaSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneTaigaSlab));
		r.accept(new BlockItem(biomeCobblestoneTaigaWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneTaigaWall));
		r.accept(new BlockItem(biomeChiseledBrickTaiga, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickTaiga));

		r.accept(new BlockItem(biomeStoneMesa, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMesa));
		r.accept(new BlockItem(biomeStoneMesaStairs, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMesaStairs));
		r.accept(new BlockItem(biomeStoneMesaSlab, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMesaSlab));
		r.accept(new BlockItem(biomeStoneMesaWall, props), BuiltInRegistries.BLOCK.getKey(biomeStoneMesaWall));
		r.accept(new BlockItem(biomeBrickMesa, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMesa));
		r.accept(new BlockItem(biomeBrickMesaStairs, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMesaStairs));
		r.accept(new BlockItem(biomeBrickMesaSlab, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMesaSlab));
		r.accept(new BlockItem(biomeBrickMesaWall, props), BuiltInRegistries.BLOCK.getKey(biomeBrickMesaWall));
		r.accept(new BlockItem(biomeCobblestoneMesa, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMesa));
		r.accept(new BlockItem(biomeCobblestoneMesaStairs, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMesaStairs));
		r.accept(new BlockItem(biomeCobblestoneMesaSlab, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMesaSlab));
		r.accept(new BlockItem(biomeCobblestoneMesaWall, props), BuiltInRegistries.BLOCK.getKey(biomeCobblestoneMesaWall));
		r.accept(new BlockItem(biomeChiseledBrickMesa, props), BuiltInRegistries.BLOCK.getKey(biomeChiseledBrickMesa));

		r.accept(new BlockItem(whitePavement, props), BuiltInRegistries.BLOCK.getKey(whitePavement));
		r.accept(new BlockItem(whitePavementStair, props), BuiltInRegistries.BLOCK.getKey(whitePavementStair));
		r.accept(new BlockItem(whitePavementSlab, props), BuiltInRegistries.BLOCK.getKey(whitePavementSlab));

		r.accept(new BlockItem(blackPavement, props), BuiltInRegistries.BLOCK.getKey(blackPavement));
		r.accept(new BlockItem(blackPavementSlab, props), BuiltInRegistries.BLOCK.getKey(blackPavementSlab));
		r.accept(new BlockItem(blackPavementStair, props), BuiltInRegistries.BLOCK.getKey(blackPavementStair));

		r.accept(new BlockItem(bluePavement, props), BuiltInRegistries.BLOCK.getKey(bluePavement));
		r.accept(new BlockItem(bluePavementStair, props), BuiltInRegistries.BLOCK.getKey(bluePavementStair));
		r.accept(new BlockItem(bluePavementSlab, props), BuiltInRegistries.BLOCK.getKey(bluePavementSlab));

		r.accept(new BlockItem(yellowPavement, props), BuiltInRegistries.BLOCK.getKey(yellowPavement));
		r.accept(new BlockItem(yellowPavementStair, props), BuiltInRegistries.BLOCK.getKey(yellowPavementStair));
		r.accept(new BlockItem(yellowPavementSlab, props), BuiltInRegistries.BLOCK.getKey(yellowPavementSlab));

		r.accept(new BlockItem(redPavement, props), BuiltInRegistries.BLOCK.getKey(redPavement));
		r.accept(new BlockItem(redPavementStair, props), BuiltInRegistries.BLOCK.getKey(redPavementStair));
		r.accept(new BlockItem(redPavementSlab, props), BuiltInRegistries.BLOCK.getKey(redPavementSlab));

		r.accept(new BlockItem(greenPavement, props), BuiltInRegistries.BLOCK.getKey(greenPavement));
		r.accept(new BlockItem(greenPavementStair, props), BuiltInRegistries.BLOCK.getKey(greenPavementStair));
		r.accept(new BlockItem(greenPavementSlab, props), BuiltInRegistries.BLOCK.getKey(greenPavementSlab));

		r.accept(new BlockItem(managlassPane, props), BuiltInRegistries.BLOCK.getKey(managlassPane));
		r.accept(new BlockItem(alfglassPane, props), BuiltInRegistries.BLOCK.getKey(alfglassPane));
		r.accept(new BlockItem(bifrostPane, props), BuiltInRegistries.BLOCK.getKey(bifrostPane));
	}

	private static <T extends Block> T make(String name, T block) {
		var old = ALL.put(name, block);
		if (old != null) {
			throw new IllegalArgumentException("Typo? Duplicate name: " + name);
		}
		return block;
	}

	public static void addDispenserBehaviours() {
		DispenserBlock.registerBehavior(BotaniaItems.twigWand, new WandBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.phantomInk, new PhantomInkBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.dreamwoodWand, new WandBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.obedienceStick, new StickBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.poolMinecart, new ManaPoolMinecartBehavior());
		DispenserBlock.registerBehavior(BotaniaBlocks.felPumpkin, new FelPumpkinBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.spark, new ManaSparkBehavior());
		DispenserBlock.registerBehavior(BotaniaBlocks.gaiaHead, new OptionalDispenseItemBehavior() {
			@Override
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				setSuccess(ArmorItem.dispenseArmor(source, stack));
				return stack;
			}
		});

		DispenseItemBehavior behavior = new CorporeaSparkBehavior();
		DispenserBlock.registerBehavior(BotaniaItems.corporeaSpark, behavior);
		DispenserBlock.registerBehavior(BotaniaItems.corporeaSparkMaster, behavior);
		DispenserBlock.registerBehavior(BotaniaItems.corporeaSparkCreative, behavior);
		DispenserBlock.registerBehavior(BotaniaItems.enderAirBottle, new ProjectileDispenseBehavior(BotaniaItems.enderAirBottle));
		behavior = DispenserBlockAccessor.getDispenserRegistry().get(Items.GLASS_BOTTLE);
		DispenserBlock.registerBehavior(Items.GLASS_BOTTLE, new EnderAirBottlingBehavior(behavior));

		behavior = new GrassSeedsBehavior();
		Item[] seedItems = {
				BotaniaItems.grassSeeds,
				BotaniaItems.podzolSeeds,
				BotaniaItems.mycelSeeds,
				BotaniaItems.drySeeds,
				BotaniaItems.goldenSeeds,
				BotaniaItems.vividSeeds,
				BotaniaItems.scorchedSeeds,
				BotaniaItems.infusedSeeds,
				BotaniaItems.mutatedSeeds,
		};
		for (Item seed : seedItems) {
			DispenserBlock.registerBehavior(seed, behavior);
		}

		DispenserBlock.registerBehavior(BotaniaItems.manasteelShears, new ShearsDispenseItemBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.elementiumShears, new ShearsDispenseItemBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.vineBall, new ProjectileDispenseBehavior(BotaniaItems.vineBall));

		SeedBehaviors.init();
	}

	public static void addAxeStripping() {
		XplatAbstractions xplat = XplatAbstractions.INSTANCE;
		xplat.addAxeStripping(livingwoodLog, livingwoodLogStripped);
		xplat.addAxeStripping(livingwoodLogGlimmering, livingwoodLogStrippedGlimmering);
		xplat.addAxeStripping(livingwood, livingwoodStripped);
		xplat.addAxeStripping(livingwoodGlimmering, livingwoodStrippedGlimmering);
		xplat.addAxeStripping(dreamwoodLog, dreamwoodLogStripped);
		xplat.addAxeStripping(dreamwoodLogGlimmering, dreamwoodLogStrippedGlimmering);
		xplat.addAxeStripping(dreamwood, dreamwoodStripped);
		xplat.addAxeStripping(dreamwoodGlimmering, dreamwoodStrippedGlimmering);

		xplat.addAxeStripping(BotaniaBlocks.livingwoodStairs, BotaniaBlocks.livingwoodStrippedStairs);
		xplat.addAxeStripping(BotaniaBlocks.livingwoodSlab, BotaniaBlocks.livingwoodStrippedSlab);
		xplat.addAxeStripping(BotaniaBlocks.livingwoodWall, BotaniaBlocks.livingwoodStrippedWall);
		xplat.addAxeStripping(BotaniaBlocks.dreamwoodStairs, BotaniaBlocks.dreamwoodStrippedStairs);
		xplat.addAxeStripping(BotaniaBlocks.dreamwoodSlab, BotaniaBlocks.dreamwoodStrippedSlab);
		xplat.addAxeStripping(BotaniaBlocks.dreamwoodWall, BotaniaBlocks.dreamwoodStrippedWall);
	}

	public static Block getFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> whiteFlower;
			case ORANGE -> orangeFlower;
			case MAGENTA -> magentaFlower;
			case LIGHT_BLUE -> lightBlueFlower;
			case YELLOW -> yellowFlower;
			case LIME -> limeFlower;
			case PINK -> pinkFlower;
			case GRAY -> grayFlower;
			case LIGHT_GRAY -> lightGrayFlower;
			case CYAN -> cyanFlower;
			case PURPLE -> purpleFlower;
			case BLUE -> blueFlower;
			case BROWN -> brownFlower;
			case GREEN -> greenFlower;
			case RED -> redFlower;
			case BLACK -> blackFlower;
		};
	}

	public static Block getMushroom(DyeColor color) {
		return switch (color) {
			case WHITE -> whiteMushroom;
			case ORANGE -> orangeMushroom;
			case MAGENTA -> magentaMushroom;
			case LIGHT_BLUE -> lightBlueMushroom;
			case YELLOW -> yellowMushroom;
			case LIME -> limeMushroom;
			case PINK -> pinkMushroom;
			case GRAY -> grayMushroom;
			case LIGHT_GRAY -> lightGrayMushroom;
			case CYAN -> cyanMushroom;
			case PURPLE -> purpleMushroom;
			case BLUE -> blueMushroom;
			case BROWN -> brownMushroom;
			case GREEN -> greenMushroom;
			case RED -> redMushroom;
			case BLACK -> blackMushroom;
		};
	}

	public static Block getBuriedPetal(DyeColor color) {
		return switch (color) {
			case WHITE -> whiteBuriedPetals;
			case ORANGE -> orangeBuriedPetals;
			case MAGENTA -> magentaBuriedPetals;
			case LIGHT_BLUE -> lightBlueBuriedPetals;
			case YELLOW -> yellowBuriedPetals;
			case LIME -> limeBuriedPetals;
			case PINK -> pinkBuriedPetals;
			case GRAY -> grayBuriedPetals;
			case LIGHT_GRAY -> lightGrayBuriedPetals;
			case CYAN -> cyanBuriedPetals;
			case PURPLE -> purpleBuriedPetals;
			case BLUE -> blueBuriedPetals;
			case BROWN -> brownBuriedPetals;
			case GREEN -> greenBuriedPetals;
			case RED -> redBuriedPetals;
			case BLACK -> blackBuriedPetals;
		};
	}

	public static Block getShinyFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> whiteShinyFlower;
			case ORANGE -> orangeShinyFlower;
			case MAGENTA -> magentaShinyFlower;
			case LIGHT_BLUE -> lightBlueShinyFlower;
			case YELLOW -> yellowShinyFlower;
			case LIME -> limeShinyFlower;
			case PINK -> pinkShinyFlower;
			case GRAY -> grayShinyFlower;
			case LIGHT_GRAY -> lightGrayShinyFlower;
			case CYAN -> cyanShinyFlower;
			case PURPLE -> purpleShinyFlower;
			case BLUE -> blueShinyFlower;
			case BROWN -> brownShinyFlower;
			case GREEN -> greenShinyFlower;
			case RED -> redShinyFlower;
			case BLACK -> blackShinyFlower;
		};
	}

	public static Block getFloatingFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> whiteFloatingFlower;
			case ORANGE -> orangeFloatingFlower;
			case MAGENTA -> magentaFloatingFlower;
			case LIGHT_BLUE -> lightBlueFloatingFlower;
			case YELLOW -> yellowFloatingFlower;
			case LIME -> limeFloatingFlower;
			case PINK -> pinkFloatingFlower;
			case GRAY -> grayFloatingFlower;
			case LIGHT_GRAY -> lightGrayFloatingFlower;
			case CYAN -> cyanFloatingFlower;
			case PURPLE -> purpleFloatingFlower;
			case BLUE -> blueFloatingFlower;
			case BROWN -> brownFloatingFlower;
			case GREEN -> greenFloatingFlower;
			case RED -> redFloatingFlower;
			case BLACK -> blackFloatingFlower;
		};
	}

	@Nullable
	public static Block getDoubleFlower(TallFlowerGrower grower) {
		return grower instanceof Colored colored ? getDoubleFlower(colored.getColor()) : null;
	}

	public static Block getDoubleFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> doubleFlowerWhite;
			case ORANGE -> doubleFlowerOrange;
			case MAGENTA -> doubleFlowerMagenta;
			case LIGHT_BLUE -> doubleFlowerLightBlue;
			case YELLOW -> doubleFlowerYellow;
			case LIME -> doubleFlowerLime;
			case PINK -> doubleFlowerPink;
			case GRAY -> doubleFlowerGray;
			case LIGHT_GRAY -> doubleFlowerLightGray;
			case CYAN -> doubleFlowerCyan;
			case PURPLE -> doubleFlowerPurple;
			case BLUE -> doubleFlowerBlue;
			case BROWN -> doubleFlowerBrown;
			case GREEN -> doubleFlowerGreen;
			case RED -> doubleFlowerRed;
			case BLACK -> doubleFlowerBlack;
		};
	}

	public static Block getPetalBlock(DyeColor color) {
		return switch (color) {
			case WHITE -> petalBlockWhite;
			case ORANGE -> petalBlockOrange;
			case MAGENTA -> petalBlockMagenta;
			case LIGHT_BLUE -> petalBlockLightBlue;
			case YELLOW -> petalBlockYellow;
			case LIME -> petalBlockLime;
			case PINK -> petalBlockPink;
			case GRAY -> petalBlockGray;
			case LIGHT_GRAY -> petalBlockSilver;
			case CYAN -> petalBlockCyan;
			case PURPLE -> petalBlockPurple;
			case BLUE -> petalBlockBlue;
			case BROWN -> petalBlockBrown;
			case GREEN -> petalBlockGreen;
			case RED -> petalBlockRed;
			case BLACK -> petalBlockBlack;
		};
	}

	public static Block getPottedFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> pottedWhiteFlower;
			case ORANGE -> pottedOrangeFlower;
			case MAGENTA -> pottedMagentaFlower;
			case LIGHT_BLUE -> pottedLightBlueFlower;
			case YELLOW -> pottedYellowFlower;
			case LIME -> pottedLimeFlower;
			case PINK -> pottedPinkFlower;
			case GRAY -> pottedGrayFlower;
			case LIGHT_GRAY -> pottedLightGrayFlower;
			case CYAN -> pottedCyanFlower;
			case PURPLE -> pottedPurpleFlower;
			case BLUE -> pottedBlueFlower;
			case BROWN -> pottedBrownFlower;
			case GREEN -> pottedGreenFlower;
			case RED -> pottedRedFlower;
			case BLACK -> pottedBlackFlower;
		};
	}

	public static Block getPottedShinyFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> pottedWhiteShinyFlower;
			case ORANGE -> pottedOrangeShinyFlower;
			case MAGENTA -> pottedMagentaShinyFlower;
			case LIGHT_BLUE -> pottedLightBlueShinyFlower;
			case YELLOW -> pottedYellowShinyFlower;
			case LIME -> pottedLimeShinyFlower;
			case PINK -> pottedPinkShinyFlower;
			case GRAY -> pottedGrayShinyFlower;
			case LIGHT_GRAY -> pottedLightGrayShinyFlower;
			case CYAN -> pottedCyanShinyFlower;
			case PURPLE -> pottedPurpleShinyFlower;
			case BLUE -> pottedBlueShinyFlower;
			case BROWN -> pottedBrownShinyFlower;
			case GREEN -> pottedGreenShinyFlower;
			case RED -> pottedRedShinyFlower;
			case BLACK -> pottedBlackShinyFlower;
		};
	}

	public static Block getPottedMushroom(DyeColor color) {
		return switch (color) {
			case WHITE -> pottedWhiteMushroom;
			case ORANGE -> pottedOrangeMushroom;
			case MAGENTA -> pottedMagentaMushroom;
			case LIGHT_BLUE -> pottedLightBlueMushroom;
			case YELLOW -> pottedYellowMushroom;
			case LIME -> pottedLimeMushroom;
			case PINK -> pottedPinkMushroom;
			case GRAY -> pottedGrayMushroom;
			case LIGHT_GRAY -> pottedLightGrayMushroom;
			case CYAN -> pottedCyanMushroom;
			case PURPLE -> pottedPurpleMushroom;
			case BLUE -> pottedBlueMushroom;
			case BROWN -> pottedBrownMushroom;
			case GREEN -> pottedGreenMushroom;
			case RED -> pottedRedMushroom;
			case BLACK -> pottedBlackMushroom;
		};
	}

	private static Holder<MobEffect> effectForFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> MobEffects.MOVEMENT_SPEED;
			case ORANGE -> MobEffects.FIRE_RESISTANCE;
			case MAGENTA -> MobEffects.DIG_SLOWDOWN;
			case LIGHT_BLUE -> MobEffects.JUMP;
			case YELLOW -> MobEffects.ABSORPTION;
			case LIME -> MobEffects.POISON;
			case PINK -> MobEffects.REGENERATION;
			case GRAY -> MobEffects.DAMAGE_RESISTANCE;
			case LIGHT_GRAY -> MobEffects.WEAKNESS;
			case CYAN -> MobEffects.WATER_BREATHING;
			case PURPLE -> MobEffects.CONFUSION;
			case BLUE -> MobEffects.NIGHT_VISION;
			case BROWN -> MobEffects.WITHER;
			case GREEN -> MobEffects.HUNGER;
			case RED -> MobEffects.DAMAGE_BOOST;
			case BLACK -> MobEffects.BLINDNESS;
		};
	}

	public static void registerFlowerPotPlants(BiConsumer<ResourceLocation, Supplier<? extends Block>> consumer) {
		registerBlocks((block, resourceLocation) -> {
			if (block instanceof FlowerPotBlock) {
				var id = BuiltInRegistries.BLOCK.getKey(block);
				consumer.accept(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath().substring(LibBlockNames.POTTED_PREFIX.length())), () -> block);
			}
		});
	}
}
