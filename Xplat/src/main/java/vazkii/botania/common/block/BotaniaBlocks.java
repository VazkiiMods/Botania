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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.PetalApothecary;
import vazkii.botania.api.internal.Colored;
import vazkii.botania.api.internal.OptionallyColored;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.flower.generating.HydroangeasBlockEntity;
import vazkii.botania.common.block.corporea.*;
import vazkii.botania.common.block.dispenser.*;
import vazkii.botania.common.block.flower.*;
import vazkii.botania.common.block.mana.*;
import vazkii.botania.common.block.red_string.*;
import vazkii.botania.common.brew.BotaniaMobEffects;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.block.ColoredBlockItem;
import vazkii.botania.common.item.block.DecayableSpecialFlowerBlockItem;
import vazkii.botania.common.item.block.SpecialFlowerBlockItem;
import vazkii.botania.common.item.block.TinyPotatoBlockItem;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public final class BotaniaBlocks {

	private static final Map<String, Block> ALL = new LinkedHashMap<>(); // Preserve insertion order

	private static final BlockBehaviour.StateArgumentPredicate<EntityType<?>> NO_SPAWN = (state, world, pos, et) -> false;
	private static final BlockBehaviour.StatePredicate NEVER = (state, world, pos) -> false;

	// small mundane flowers
	public static final MysticalFlowerBlock WHITE_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.WHITE,
			// [VanillaCopy] Blocks.POPPY
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.PLANT)
					.noCollission()
					.instabreak()
					.offsetType(BlockBehaviour.OffsetType.XZ)
					.pushReaction(PushReaction.DESTROY)
					.sound(SoundType.GRASS));
	public static final MysticalFlowerBlock ORANGE_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.ORANGE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock MAGENTA_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.MAGENTA,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock LIGHT_BLUE_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.LIGHT_BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock YELLOW_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.YELLOW,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock LIME_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.LIME,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock PINK_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.PINK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock GRAY_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock LIGHT_GRAY_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.LIGHT_GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock CYAN_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.CYAN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock PURPLE_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.PURPLE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock BLUE_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock BROWN_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.BROWN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock GREEN_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.GREEN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock RED_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.RED,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock BLACK_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.BLACK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));

	// glimmering mundane flowers
	public static final GlimmeringFlowerBlock WHITE_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.WHITE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER)
					.lightLevel(constInt(15)));
	public static final GlimmeringFlowerBlock ORANGE_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.ORANGE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock MAGENTA_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.MAGENTA,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock LIGHT_BLUE_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.LIGHT_BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock YELLOW_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.YELLOW,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock LIME_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.LIME,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock PINK_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.PINK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock GRAY_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock LIGHT_GRAY_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.LIGHT_GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock CYAN_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.CYAN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock PURPLE_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.PURPLE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock BLUE_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock BROWN_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.BROWN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock GREEN_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.GREEN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock RED_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.RED,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock BLACK_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.BLACK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));

	// buried petals
	public static final Block WHITE_BURIED_PETAL = makeBuriedPetal(DyeColor.WHITE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER)
					.sound(SoundType.MOSS)
					.lightLevel(constInt(4)));
	public static final Block ORANGE_BURIED_PETAL = makeBuriedPetal(DyeColor.ORANGE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block MAGENTA_BURIED_PETAL = makeBuriedPetal(DyeColor.MAGENTA,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block LIGHT_BLUE_BURIED_PETAL = makeBuriedPetal(DyeColor.LIGHT_BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block YELLOW_BURIED_PETAL = makeBuriedPetal(DyeColor.YELLOW,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block LIME_BURIED_PETAL = makeBuriedPetal(DyeColor.LIME,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block PINK_BURIED_PETAL = makeBuriedPetal(DyeColor.PINK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block GRAY_BURIED_PETAL = makeBuriedPetal(DyeColor.GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block LIGHT_GRAY_BURIED_PETAL = makeBuriedPetal(DyeColor.LIGHT_GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block CYAN_BURIED_PETAL = makeBuriedPetal(DyeColor.CYAN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block PURPLE_BURIED_PETAL = makeBuriedPetal(DyeColor.PURPLE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block BLUE_BURIED_PETAL = makeBuriedPetal(DyeColor.BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block BROWN_BURIED_PETAL = makeBuriedPetal(DyeColor.BROWN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block GREEN_BURIED_PETAL = makeBuriedPetal(DyeColor.GREEN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block RED_BURIED_PETAL = makeBuriedPetal(DyeColor.RED,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block BLACK_BURIED_PETAL = makeBuriedPetal(DyeColor.BLACK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));

	// floating mundane flowers
	public static final Block WHITE_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.WHITE,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.PLANT)
					.strength(0.5f)
					.sound(SoundType.GRAVEL)
					.lightLevel(state -> state.getValue(BotaniaStateProperties.DIMMED) ? 3 : 15)
	);
	public static final Block ORANGE_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.ORANGE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block MAGENTA_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.MAGENTA,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block LIGHT_BLUE_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.LIGHT_BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block YELLOW_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.YELLOW,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block LIME_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.LIME,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block PINK_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.PINK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block GRAY_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block LIGHT_GRAY_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.LIGHT_GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block CYAN_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.CYAN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block PURPLE_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.PURPLE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block BLUE_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block BROWN_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.BROWN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block GREEN_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.GREEN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block RED_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.RED,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block BLACK_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.BLACK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));

	// petal blocks
	public static final Block WHITE_PETAL_BLOCK = makePetalBlock(DyeColor.WHITE,
			// Note: map color of properties is set to dye color
			BlockBehaviour.Properties.of()
					.strength(0.4f)
					.sound(SoundType.MOSS));
	public static final Block ORANGE_PETAL_BLOCK = makePetalBlock(DyeColor.ORANGE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block MAGENTA_PETAL_BLOCK = makePetalBlock(DyeColor.MAGENTA,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block LIGHT_BLUE_PETAL_BLOCK = makePetalBlock(DyeColor.LIGHT_BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block YELLOW_PETAL_BLOCK = makePetalBlock(DyeColor.YELLOW,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block LIME_PETAL_BLOCK = makePetalBlock(DyeColor.LIME,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block PINK_PETAL_BLOCK = makePetalBlock(DyeColor.PINK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block GRAY_PETAL_BLOCK = makePetalBlock(DyeColor.GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block LIGHT_GRAY_PETAL_BLOCK = makePetalBlock(DyeColor.LIGHT_GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block CYAN_PETAL_BLOCK = makePetalBlock(DyeColor.CYAN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block PURPLE_PETAL_BLOCK = makePetalBlock(DyeColor.PURPLE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block BLUE_PETAL_BLOCK = makePetalBlock(DyeColor.BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block BROWN_PETAL_BLOCK = makePetalBlock(DyeColor.BROWN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block GREEN_PETAL_BLOCK = makePetalBlock(DyeColor.GREEN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block RED_PETAL_BLOCK = makePetalBlock(DyeColor.RED,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block BLACK_PETAL_BLOCK = makePetalBlock(DyeColor.BLACK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));

	// shimmering mushrooms
	public static final ShimmeringMushroomBlock WHITE_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.WHITE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER)
					.lightLevel(constInt(3))
					.offsetType(BlockBehaviour.OffsetType.NONE)
	);
	public static final ShimmeringMushroomBlock ORANGE_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.ORANGE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock MAGENTA_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.MAGENTA,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock LIGHT_BLUE_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.LIGHT_BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock YELLOW_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.YELLOW,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock LIME_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.LIME,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock PINK_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.PINK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock GRAY_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock LIGHT_GRAY_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.LIGHT_GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock CYAN_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.CYAN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock PURPLE_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.PURPLE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock BLUE_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock BROWN_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.BROWN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock GREEN_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.GREEN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock RED_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.RED,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock BLACK_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.BLACK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));

	// tall mundane flowers
	public static final Block WHITE_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.WHITE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final Block ORANGE_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.ORANGE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block MAGENTA_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.MAGENTA,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block LIGHT_BLUE_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.LIGHT_BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block YELLOW_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.YELLOW,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block LIME_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.LIME,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block PINK_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.PINK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block GRAY_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block LIGHT_GRAY_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.LIGHT_GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block CYAN_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.CYAN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block PURPLE_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.PURPLE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block BLUE_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block BROWN_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.BROWN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block GREEN_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.GREEN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block RED_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.RED,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block BLACK_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.BLACK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));

	// potted mundane flowers
	public static final Block POTTED_WHITE_MYSTICAL_FLOWER = makePottedMundaneFlower(WHITE_MYSTICAL_FLOWER);
	public static final Block POTTED_ORANGE_MYSTICAL_FLOWER = makePottedMundaneFlower(ORANGE_MYSTICAL_FLOWER);
	public static final Block POTTED_MAGENTA_MYSTICAL_FLOWER = makePottedMundaneFlower(MAGENTA_MYSTICAL_FLOWER);
	public static final Block POTTED_LIGHT_BLUE_MYSTICAL_FLOWER = makePottedMundaneFlower(LIGHT_BLUE_MYSTICAL_FLOWER);
	public static final Block POTTED_YELLOW_MYSTICAL_FLOWER = makePottedMundaneFlower(YELLOW_MYSTICAL_FLOWER);
	public static final Block POTTED_LIME_MYSTICAL_FLOWER = makePottedMundaneFlower(LIME_MYSTICAL_FLOWER);
	public static final Block POTTED_PINK_MYSTICAL_FLOWER = makePottedMundaneFlower(PINK_MYSTICAL_FLOWER);
	public static final Block POTTED_GRAY_MYSTICAL_FLOWER = makePottedMundaneFlower(GRAY_MYSTICAL_FLOWER);
	public static final Block POTTED_LIGHT_GRAY_MYSTICAL_FLOWER = makePottedMundaneFlower(LIGHT_GRAY_MYSTICAL_FLOWER);
	public static final Block POTTED_CYAN_MYSTICAL_FLOWER = makePottedMundaneFlower(CYAN_MYSTICAL_FLOWER);
	public static final Block POTTED_PURPLE_MYSTICAL_FLOWER = makePottedMundaneFlower(PURPLE_MYSTICAL_FLOWER);
	public static final Block POTTED_BLUE_MYSTICAL_FLOWER = makePottedMundaneFlower(BLUE_MYSTICAL_FLOWER);
	public static final Block POTTED_BROWN_MYSTICAL_FLOWER = makePottedMundaneFlower(BROWN_MYSTICAL_FLOWER);
	public static final Block POTTED_GREEN_MYSTICAL_FLOWER = makePottedMundaneFlower(GREEN_MYSTICAL_FLOWER);
	public static final Block POTTED_RED_MYSTICAL_FLOWER = makePottedMundaneFlower(RED_MYSTICAL_FLOWER);
	public static final Block POTTED_BLACK_MYSTICAL_FLOWER = makePottedMundaneFlower(BLACK_MYSTICAL_FLOWER);

	// potted shimmering flowers
	public static final Block POTTED_WHITE_GLIMMERING_FLOWER = makePottedGlimmeringFlower(WHITE_GLIMMERING_FLOWER);
	public static final Block POTTED_ORANGE_GLIMMERING_FLOWER = makePottedGlimmeringFlower(ORANGE_GLIMMERING_FLOWER);
	public static final Block POTTED_MAGENTA_GLIMMERING_FLOWER = makePottedGlimmeringFlower(MAGENTA_GLIMMERING_FLOWER);
	public static final Block POTTED_LIGHT_BLUE_GLIMMERING_FLOWER = makePottedGlimmeringFlower(LIGHT_BLUE_GLIMMERING_FLOWER);
	public static final Block POTTED_YELLOW_GLIMMERING_FLOWER = makePottedGlimmeringFlower(YELLOW_GLIMMERING_FLOWER);
	public static final Block POTTED_LIME_GLIMMERING_FLOWER = makePottedGlimmeringFlower(LIME_GLIMMERING_FLOWER);
	public static final Block POTTED_PINK_GLIMMERING_FLOWER = makePottedGlimmeringFlower(PINK_GLIMMERING_FLOWER);
	public static final Block POTTED_GRAY_GLIMMERING_FLOWER = makePottedGlimmeringFlower(GRAY_GLIMMERING_FLOWER);
	public static final Block POTTED_LIGHT_GRAY_GLIMMERING_FLOWER = makePottedGlimmeringFlower(LIGHT_GRAY_GLIMMERING_FLOWER);
	public static final Block POTTED_CYAN_GLIMMERING_FLOWER = makePottedGlimmeringFlower(CYAN_GLIMMERING_FLOWER);
	public static final Block POTTED_PURPLE_GLIMMERING_FLOWER = makePottedGlimmeringFlower(PURPLE_GLIMMERING_FLOWER);
	public static final Block POTTED_BLUE_GLIMMERING_FLOWER = makePottedGlimmeringFlower(BLUE_GLIMMERING_FLOWER);
	public static final Block POTTED_BROWN_GLIMMERING_FLOWER = makePottedGlimmeringFlower(BROWN_GLIMMERING_FLOWER);
	public static final Block POTTED_GREEN_GLIMMERING_FLOWER = makePottedGlimmeringFlower(GREEN_GLIMMERING_FLOWER);
	public static final Block POTTED_RED_GLIMMERING_FLOWER = makePottedGlimmeringFlower(RED_GLIMMERING_FLOWER);
	public static final Block POTTED_BLACK_GLIMMERING_FLOWER = makePottedGlimmeringFlower(BLACK_GLIMMERING_FLOWER);

	// potted shimmering mushrooms
	public static final Block POTTED_WHITE_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(WHITE_SHIMMERING_MUSHROOM);
	public static final Block POTTED_ORANGE_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(ORANGE_SHIMMERING_MUSHROOM);
	public static final Block POTTED_MAGENTA_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(MAGENTA_SHIMMERING_MUSHROOM);
	public static final Block POTTED_LIGHT_BLUE_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(LIGHT_BLUE_SHIMMERING_MUSHROOM);
	public static final Block POTTED_YELLOW_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(YELLOW_SHIMMERING_MUSHROOM);
	public static final Block POTTED_LIME_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(LIME_SHIMMERING_MUSHROOM);
	public static final Block POTTED_PINK_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(PINK_SHIMMERING_MUSHROOM);
	public static final Block POTTED_GRAY_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(GRAY_SHIMMERING_MUSHROOM);
	public static final Block POTTED_LIGHT_GRAY_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(LIGHT_GRAY_SHIMMERING_MUSHROOM);
	public static final Block POTTED_CYAN_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(CYAN_SHIMMERING_MUSHROOM);
	public static final Block POTTED_PURPLE_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(PURPLE_SHIMMERING_MUSHROOM);
	public static final Block POTTED_BLUE_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(BLUE_SHIMMERING_MUSHROOM);
	public static final Block POTTED_BROWN_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(BROWN_SHIMMERING_MUSHROOM);
	public static final Block POTTED_GREEN_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(GREEN_SHIMMERING_MUSHROOM);
	public static final Block POTTED_RED_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(RED_SHIMMERING_MUSHROOM);
	public static final Block POTTED_BLACK_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(BLACK_SHIMMERING_MUSHROOM);

	// special flowers (regular, floating, and potted; potentially each with petite variants)
	public static final Block PURE_DAISY = make(LibBlockNames.SUBTILE_PUREDAISY,
			new SpecialFlowerBlock(
					BotaniaMobEffects.CLEAR,
					1,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.PURE_DAISY));
	public static final Block FLOATING_PURE_DAISY = make(floating(LibBlockNames.SUBTILE_PUREDAISY),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.PURE_DAISY));
	public static final Block POTTED_PURE_DAISY = make(potted(LibBlockNames.SUBTILE_PUREDAISY),
			flowerPot(PURE_DAISY, 0));

	public static final Block MANASTAR = make(LibBlockNames.SUBTILE_MANASTAR,
			new ManastarBlock(
					MobEffects.GLOWING,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.MANASTAR));
	public static final Block FLOATING_MANASTAR = make(floating(LibBlockNames.SUBTILE_MANASTAR),
			new FloatingManastarBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.MANASTAR));
	public static final Block POTTED_MANASTAR = make(potted(LibBlockNames.SUBTILE_MANASTAR),
			flowerPot(MANASTAR, 0));

	public static final Block HYDROANGEAS = make(LibBlockNames.SUBTILE_HYDROANGEAS,
			new SlowGeneratingFlowerWithCooldownBlock(
					MobEffects.UNLUCK,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.HYDROANGEAS));
	public static final Block FLOATING_HYDROANGEAS = make(floating(LibBlockNames.SUBTILE_HYDROANGEAS),
			new FloatingSlowGeneratingFlowerWithCooldownBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.HYDROANGEAS));
	public static final Block POTTED_HYDROANGEAS = make(potted(LibBlockNames.SUBTILE_HYDROANGEAS),
			flowerPot(HYDROANGEAS, 0));

	public static final Block ENDOFLAME = make(LibBlockNames.SUBTILE_ENDOFLAME,
			new SlowGeneratingFlowerWithCooldownBlock(
					MobEffects.MOVEMENT_SLOWDOWN,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.ENDOFLAME));
	public static final Block FLOATING_ENDOFLAME = make(floating(LibBlockNames.SUBTILE_ENDOFLAME),
			new FloatingSlowGeneratingFlowerWithCooldownBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.ENDOFLAME));
	public static final Block POTTED_ENDOFLAME = make(potted(LibBlockNames.SUBTILE_ENDOFLAME),
			flowerPot(ENDOFLAME, 0));

	public static final Block THERMALILY = make(LibBlockNames.SUBTILE_THERMALILY,
			new SlowGeneratingFlowerWithCooldownBlock(
					MobEffects.FIRE_RESISTANCE,
					120,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.THERMALILY));
	public static final Block FLOATING_THERMALILY = make(floating(LibBlockNames.SUBTILE_THERMALILY),
			new FloatingSlowGeneratingFlowerWithCooldownBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.THERMALILY));
	public static final Block POTTED_THERMALILY = make(potted(LibBlockNames.SUBTILE_THERMALILY),
			flowerPot(THERMALILY, 0));

	public static final Block ROSA_ARCANA = make(LibBlockNames.SUBTILE_ARCANE_ROSE,
			new SpecialFlowerBlock(MobEffects.LUCK,
					64,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.ROSA_ARCANA));
	public static final Block FLOATING_ROSA_ARCANA = make(floating(LibBlockNames.SUBTILE_ARCANE_ROSE),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.ROSA_ARCANA));
	public static final Block POTTED_ROSA_ARCANA = make(potted(LibBlockNames.SUBTILE_ARCANE_ROSE),
			flowerPot(ROSA_ARCANA, 0));

	public static final Block MUNCHDEW = make(LibBlockNames.SUBTILE_MUNCHDEW,
			new SlowGeneratingFlowerWithCooldownBlock(
					MobEffects.SLOW_FALLING,
					300,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.MUNCHDEW));
	public static final Block FLOATING_MUNCHDEW = make(floating(LibBlockNames.SUBTILE_MUNCHDEW),
			new FloatingSlowGeneratingFlowerWithCooldownBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.MUNCHDEW));
	public static final Block POTTED_MUNCHDEW = make(potted(LibBlockNames.SUBTILE_MUNCHDEW),
			flowerPot(MUNCHDEW, 0));

	public static final Block ENTROPINNYUM = make(LibBlockNames.SUBTILE_ENTROPINNYUM,
			new SpecialFlowerBlock(
					MobEffects.DAMAGE_RESISTANCE,
					72,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.ENTROPINNYUM));
	public static final Block FLOATING_ENTROPINNYUM = make(floating(LibBlockNames.SUBTILE_ENTROPINNYUM),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.ENTROPINNYUM));
	public static final Block POTTED_ENTROPINNYUM = make(potted(LibBlockNames.SUBTILE_ENTROPINNYUM),
			flowerPot(ENTROPINNYUM, 0));

	public static final Block KEKIMURUS = make(LibBlockNames.SUBTILE_KEKIMURUS,
			new SpecialFlowerBlock(MobEffects.SATURATION,
					15,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.KEKIMURUS));
	public static final Block FLOATING_KEKIMURUS = make(floating(LibBlockNames.SUBTILE_KEKIMURUS),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.KEKIMURUS));
	public static final Block POTTED_KEKIMURUS = make(potted(LibBlockNames.SUBTILE_KEKIMURUS),
			flowerPot(KEKIMURUS, 0));

	public static final Block GOURMARYLLIS = make(LibBlockNames.SUBTILE_GOURMARYLLIS,
			new SlowGeneratingFlowerBlock(
					MobEffects.HUNGER,
					180,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.GOURMARYLLIS));
	public static final Block FLOATING_GOURMARYLLIS = make(floating(LibBlockNames.SUBTILE_GOURMARYLLIS),
			new FloatingSlowGeneratingFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.GOURMARYLLIS));
	public static final Block POTTED_GOURMARYLLIS = make(potted(LibBlockNames.SUBTILE_GOURMARYLLIS),
			flowerPot(GOURMARYLLIS, 0));

	public static final Block NARSLIMMUS = make(LibBlockNames.SUBTILE_NARSLIMMUS,
			new SpecialFlowerBlock(BotaniaMobEffects.FEATHER_FEET,
					240,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.NARSLIMMUS));
	public static final Block FLOATING_NARSLIMMUS = make(floating(LibBlockNames.SUBTILE_NARSLIMMUS),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.NARSLIMMUS));
	public static final Block POTTED_NARSLIMMUS = make(potted(LibBlockNames.SUBTILE_NARSLIMMUS),
			flowerPot(NARSLIMMUS, 0));

	public static final Block SPECTROLUS = make(LibBlockNames.SUBTILE_SPECTROLUS,
			new SpecialFlowerBlock(
					MobEffects.BLINDNESS,
					240,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.SPECTROLUS));
	public static final Block FLOATING_SPECTROLUS = make(floating(LibBlockNames.SUBTILE_SPECTROLUS),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.SPECTROLUS));
	public static final Block POTTED_SPECTROLUS = make(potted(LibBlockNames.SUBTILE_SPECTROLUS),
			flowerPot(SPECTROLUS, 0));

	public static final Block DANDELIFEON = make(LibBlockNames.SUBTILE_DANDELIFEON,
			new PoweredSpecialFlowerBlock(
					MobEffects.CONFUSION,
					240,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.DANDELIFEON));
	public static final Block FLOATING_DANDELIFEON = make(floating(LibBlockNames.SUBTILE_DANDELIFEON),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.DANDELIFEON));
	public static final Block POTTED_DANDELIFEON = make(potted(LibBlockNames.SUBTILE_DANDELIFEON),
			flowerPot(DANDELIFEON, 0));

	public static final Block RAFFLOWSIA = make(LibBlockNames.SUBTILE_RAFFLOWSIA,
			new SpecialFlowerBlock(
					MobEffects.HEALTH_BOOST,
					18,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.RAFFLOWSIA));
	public static final Block FLOATING_RAFFLOWSIA = make(floating(LibBlockNames.SUBTILE_RAFFLOWSIA),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.RAFFLOWSIA));
	public static final Block POTTED_RAFFLOWSIA = make(potted(LibBlockNames.SUBTILE_RAFFLOWSIA),
			flowerPot(RAFFLOWSIA, 0));

	public static final Block SHULK_ME_NOT = make(LibBlockNames.SUBTILE_SHULK_ME_NOT,
			new SpecialFlowerBlock(
					MobEffects.LEVITATION,
					72,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.SHULK_ME_NOT));
	public static final Block FLOATING_SHULK_ME_NOT = make(floating(LibBlockNames.SUBTILE_SHULK_ME_NOT),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.SHULK_ME_NOT));
	public static final Block POTTED_SHULK_ME_NOT = make(potted(LibBlockNames.SUBTILE_SHULK_ME_NOT),
			flowerPot(SHULK_ME_NOT, 0));

	public static final Block BELLETHORNE = make(LibBlockNames.SUBTILE_BELLETHORN,
			new PoweredSpecialFlowerBlock(
					MobEffects.WITHER,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.BELLETHORNE));
	public static final Block BELLETHORNE_PETITE = make(petite(LibBlockNames.SUBTILE_BELLETHORN),
			new PoweredSpecialFlowerBlock(
					MobEffects.WITHER,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.BELLETHORNE_PETITE));
	public static final Block FLOATING_BELLETHORNE = make(floating(LibBlockNames.SUBTILE_BELLETHORN),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.BELLETHORNE));
	public static final Block FLOATING_BELLETHORNE_PETITE = make(petite(floating(LibBlockNames.SUBTILE_BELLETHORN)),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.BELLETHORNE_PETITE));
	public static final Block POTTED_BELLETHORNE = make(potted(LibBlockNames.SUBTILE_BELLETHORN),
			flowerPot(BELLETHORNE, 0));
	public static final Block POTTED_BELLETHORNE_PETITE = make(petite(potted(LibBlockNames.SUBTILE_BELLETHORN)),
			flowerPot(BELLETHORNE_PETITE, 0));

	public static final Block BERGAMUTE = make(LibBlockNames.SUBTILE_BERGAMUTE,
			new PoweredSpecialFlowerBlock(
					MobEffects.BLINDNESS,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.BERGAMUTE));
	public static final Block FLOATING_BERGAMUTE = make(floating(LibBlockNames.SUBTILE_BERGAMUTE),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.BERGAMUTE));
	public static final Block POTTED_BERGAMUTE = make(potted(LibBlockNames.SUBTILE_BERGAMUTE),
			flowerPot(BERGAMUTE, 0));

	public static final Block DREADTHORNE = make(LibBlockNames.SUBTILE_DREADTHORN,
			new PoweredSpecialFlowerBlock(
					MobEffects.WITHER,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.DREADTHORN));
	public static final Block FLOATING_DREADTHORNE = make(floating(LibBlockNames.SUBTILE_DREADTHORN),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.DREADTHORN));
	public static final Block POTTED_DREADTHORNE = make(potted(LibBlockNames.SUBTILE_DREADTHORN),
			flowerPot(DREADTHORNE, 0));

	public static final Block HEISEI_DREAM = make(LibBlockNames.SUBTILE_HEISEI_DREAM,
			new SpecialFlowerBlock(
					BotaniaMobEffects.SOUL_CROSS,
					300,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.HEISEI_DREAM));
	public static final Block FLOATING_HEISEI_DREAM = make(floating(LibBlockNames.SUBTILE_HEISEI_DREAM),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.HEISEI_DREAM));
	public static final Block POTTED_HEISEI_DREAM = make(potted(LibBlockNames.SUBTILE_HEISEI_DREAM),
			flowerPot(HEISEI_DREAM, 0));

	public static final Block TIGERSEYE = make(LibBlockNames.SUBTILE_TIGERSEYE,
			new SpecialFlowerBlock(
					MobEffects.DAMAGE_BOOST,
					90,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.TIGERSEYE));
	public static final Block FLOATING_TIGERSEYE = make(floating(LibBlockNames.SUBTILE_TIGERSEYE),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.TIGERSEYE));
	public static final Block POTTED_TIGERSEYE = make(potted(LibBlockNames.SUBTILE_TIGERSEYE),
			flowerPot(TIGERSEYE, 0));

	public static final Block JADED_AMARANTHUS = make(LibBlockNames.SUBTILE_JADED_AMARANTHUS,
			new PoweredSpecialFlowerBlock(
					MobEffects.HEAL,
					1,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.JADED_AMARANTHUS));
	public static final Block FLOATING_JADED_AMARANTHUS = make(floating(LibBlockNames.SUBTILE_JADED_AMARANTHUS),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.JADED_AMARANTHUS));
	public static final Block POTTED_JADED_AMARANTHUS = make(potted(LibBlockNames.SUBTILE_JADED_AMARANTHUS),
			flowerPot(JADED_AMARANTHUS, 0));

	public static final Block ORECHID = make(LibBlockNames.SUBTILE_ORECHID,
			new PoweredSpecialFlowerBlock(
					MobEffects.DIG_SPEED,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.ORECHID));
	public static final Block FLOATING_ORECHID = make(floating(LibBlockNames.SUBTILE_ORECHID),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.ORECHID));
	public static final Block POTTED_ORECHID = make(potted(LibBlockNames.SUBTILE_ORECHID),
			flowerPot(ORECHID, 0));

	public static final Block FALLEN_KANADE = make(LibBlockNames.SUBTILE_FALLEN_KANADE,
			new SpecialFlowerBlock(
					MobEffects.REGENERATION,
					90,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.FALLEN_KANADE));
	public static final Block FLOATING_FALLEN_KANADE = make(floating(LibBlockNames.SUBTILE_FALLEN_KANADE),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.FALLEN_KANADE));
	public static final Block POTTED_FALLEN_KANADE = make(potted(LibBlockNames.SUBTILE_FALLEN_KANADE),
			flowerPot(FALLEN_KANADE, 0));

	public static final Block EXOFLAME = make(LibBlockNames.SUBTILE_EXOFLAME,
			new SpecialFlowerBlock(MobEffects.MOVEMENT_SPEED,
					240,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.EXOFLAME));
	public static final Block FLOATING_EXOFLAME = make(floating(LibBlockNames.SUBTILE_EXOFLAME),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.EXOFLAME));
	public static final Block POTTED_EXOFLAME = make(potted(LibBlockNames.SUBTILE_EXOFLAME),
			flowerPot(EXOFLAME, 0));

	public static final Block AGRICARNATION = make(LibBlockNames.SUBTILE_AGRICARNATION,
			new PoweredSpecialFlowerBlock(
					MobEffects.ABSORPTION,
					48,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.AGRICARNATION));
	public static final Block AGRICARNATION_PETITE = make(petite(LibBlockNames.SUBTILE_AGRICARNATION),
			new PoweredSpecialFlowerBlock(
					MobEffects.ABSORPTION,
					48,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.AGRICARNATION_PETITE));
	public static final Block FLOATING_AGRICARNATION = make(floating(LibBlockNames.SUBTILE_AGRICARNATION),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.AGRICARNATION));
	public static final Block FLOATING_AGRICARNATION_PETITE = make(petite(floating(LibBlockNames.SUBTILE_AGRICARNATION)),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.AGRICARNATION_PETITE));
	public static final Block POTTED_AGRICARNATION = make(potted(LibBlockNames.SUBTILE_AGRICARNATION),
			flowerPot(AGRICARNATION, 0));
	public static final Block POTTED_AGRICARNATION_PETITE = make(petite(potted(LibBlockNames.SUBTILE_AGRICARNATION)),
			flowerPot(AGRICARNATION_PETITE, 0));

	public static final Block HOPPERHOCK = make(LibBlockNames.SUBTILE_HOPPERHOCK,
			new HopperhockBlock(MobEffects.MOVEMENT_SPEED,
					30,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.HOPPERHOCK));
	public static final Block HOPPERHOCK_PETITE = make(petite(LibBlockNames.SUBTILE_HOPPERHOCK),
			new HopperhockBlock(
					MobEffects.MOVEMENT_SPEED,
					30,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.HOPPERHOCK_PETITE));
	public static final Block FLOATING_HOPPERHOCK = make(floating(LibBlockNames.SUBTILE_HOPPERHOCK),
			new FloatingHopperhockBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.HOPPERHOCK));
	public static final Block FLOATING_HOPPERHOCK_PETITE = make(petite(floating(LibBlockNames.SUBTILE_HOPPERHOCK)),
			new FloatingHopperhockBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.HOPPERHOCK_PETITE));
	public static final Block POTTED_HOPPERHOCK = make(potted(LibBlockNames.SUBTILE_HOPPERHOCK),
			flowerPot(HOPPERHOCK, 0));
	public static final Block POTTED_HOPPERHOCK_PETITE = make(petite(potted(LibBlockNames.SUBTILE_HOPPERHOCK)),
			flowerPot(HOPPERHOCK_PETITE, 0));

	public static final Block TANGLEBERRIE = make(LibBlockNames.SUBTILE_TANGLEBERRIE,
			new SpecialFlowerBlock(
					BotaniaMobEffects.BLOODTHIRST,
					120,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.TANGLEBERRIE));
	public static final Block TANGLEBERRIE_PETITE = make(petite(LibBlockNames.SUBTILE_TANGLEBERRIE),
			new SpecialFlowerBlock(
					BotaniaMobEffects.BLOODTHIRST,
					120,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.TANGLEBERRIE_PETITE));
	public static final Block FLOATING_TANGLEBERRIE = make(floating(LibBlockNames.SUBTILE_TANGLEBERRIE),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.TANGLEBERRIE));
	public static final Block FLOATING_TANGLEBERRIE_PETITE = make(petite(floating(LibBlockNames.SUBTILE_TANGLEBERRIE)),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.TANGLEBERRIE_PETITE));
	public static final Block POTTED_TANGLEBERRIE = make(potted(LibBlockNames.SUBTILE_TANGLEBERRIE),
			flowerPot(TANGLEBERRIE, 0));
	public static final Block POTTED_TANGLEBERRIE_PETITE = make(petite(potted(LibBlockNames.SUBTILE_TANGLEBERRIE)),
			flowerPot(TANGLEBERRIE_PETITE, 0));

	public static final Block JIYUULIA = make(LibBlockNames.SUBTILE_JIYUULIA,
			new SpecialFlowerBlock(
					BotaniaMobEffects.EMPTINESS,
					120,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.JIYUULIA));
	public static final Block JIYUULIA_PETITE = make(petite(LibBlockNames.SUBTILE_JIYUULIA),
			new SpecialFlowerBlock(
					BotaniaMobEffects.EMPTINESS,
					120,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.JIYUULIA_PETITE));
	public static final Block FLOATING_JIYUULIA = make(floating(LibBlockNames.SUBTILE_JIYUULIA),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.JIYUULIA));
	public static final Block FLOATING_JIYUULIA_PETITE = make(petite(floating(LibBlockNames.SUBTILE_JIYUULIA)),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.JIYUULIA_PETITE));
	public static final Block POTTED_JIYULLIA = make(potted(LibBlockNames.SUBTILE_JIYUULIA),
			flowerPot(JIYUULIA, 0));
	public static final Block POTTED_JIYUULIA_PETITE = make(petite(potted(LibBlockNames.SUBTILE_JIYUULIA)),
			flowerPot(JIYUULIA_PETITE, 0));

	public static final Block RANNUNCARPUS = make(LibBlockNames.SUBTILE_RANNUNCARPUS,
			new RannuncarpusBlock(
					MobEffects.JUMP,
					30,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.RANNUNCARPUS));
	public static final Block RANNUNCARPUS_PETITE = make(petite(LibBlockNames.SUBTILE_RANNUNCARPUS),
			new RannuncarpusBlock(
					MobEffects.JUMP,
					30,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.RANNUNCARPUS_PETITE));
	public static final Block FLOATING_RANNUNCARPUS = make(floating(LibBlockNames.SUBTILE_RANNUNCARPUS),
			new FloatingRannuncarpusBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.RANNUNCARPUS));
	public static final Block FLOATING_RANNUNCARPUS_PETITE = make(petite(floating(LibBlockNames.SUBTILE_RANNUNCARPUS)),
			new FloatingRannuncarpusBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.RANNUNCARPUS_PETITE));
	public static final Block POTTED_RANNUNCARPUS = make(potted(LibBlockNames.SUBTILE_RANNUNCARPUS),
			flowerPot(RANNUNCARPUS, 0));
	public static final Block POTTED_RANNUNCARPUS_PETITE = make(petite(potted(LibBlockNames.SUBTILE_RANNUNCARPUS)),
			flowerPot(RANNUNCARPUS_PETITE, 0));

	public static final Block HYACIDUS = make(LibBlockNames.SUBTILE_HYACIDUS,
			new PoweredSpecialFlowerBlock(
					MobEffects.POISON,
					48,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.HYACIDUS));
	public static final Block FLOATING_HYACIDUS = make(floating(LibBlockNames.SUBTILE_HYACIDUS),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.HYACIDUS));
	public static final Block POTTED_HYACIDUS = make(potted(LibBlockNames.SUBTILE_HYACIDUS),
			flowerPot(HYACIDUS, 0));

	public static final Block POLLIDISIAC = make(LibBlockNames.SUBTILE_POLLIDISIAC,
			new PollidisiacBlock(
					MobEffects.DIG_SPEED,
					369,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.POLLIDISIAC));
	public static final Block FLOATING_POLLIDISIAC = make(floating(LibBlockNames.SUBTILE_POLLIDISIAC),
			new FloatingPollidisiacBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.POLLIDISIAC));
	public static final Block POTTED_POLLIDISIAC = make(potted(LibBlockNames.SUBTILE_POLLIDISIAC),
			flowerPot(POLLIDISIAC, 0));

	public static final Block CLAYCONIA = make(LibBlockNames.SUBTILE_CLAYCONIA,
			new SpecialFlowerBlock(
					MobEffects.WEAKNESS,
					30,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.CLAYCONIA));
	public static final Block CLAYCONIA_PETITE = make(petite(LibBlockNames.SUBTILE_CLAYCONIA),
			new SpecialFlowerBlock(
					MobEffects.WEAKNESS,
					30,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.CLAYCONIA_PETITE));
	public static final Block FLOATING_CLAYCONIA = make(floating(LibBlockNames.SUBTILE_CLAYCONIA),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.CLAYCONIA));
	public static final Block FLOATING_CLAYCONIA_PETITE = make(petite(floating(LibBlockNames.SUBTILE_CLAYCONIA)),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.CLAYCONIA_PETITE));
	public static final Block POTTED_CLAYCONIA = make(potted(LibBlockNames.SUBTILE_CLAYCONIA),
			flowerPot(CLAYCONIA, 0));
	public static final Block POTTED_CLAYCONIA_PETITE = make(petite(potted(LibBlockNames.SUBTILE_CLAYCONIA)),
			flowerPot(CLAYCONIA_PETITE, 0));

	public static final Block LOONIUM = make(LibBlockNames.SUBTILE_LOONIUM,
			new PoweredSpecialFlowerBlock(
					BotaniaMobEffects.ALLURE,
					900,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.LOONIUM));
	public static final Block FLOATING_LOONIUM = make(floating(LibBlockNames.SUBTILE_LOONIUM),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.LOONIUM));
	public static final Block POTTED_LOONIUM = make(potted(LibBlockNames.SUBTILE_LOONIUM),
			flowerPot(LOONIUM, 0));

	public static final Block DAFFOMILL = make(LibBlockNames.SUBTILE_DAFFOMILL,
			new DaffomillBlock(
					MobEffects.LEVITATION,
					6,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.DAFFOMILL));
	public static final Block FLOATING_DAFFOMILL = make(floating(LibBlockNames.SUBTILE_DAFFOMILL),
			new FloatingDaffomillBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.DAFFOMILL));
	public static final Block POTTED_DAFFOMILL = make(potted(LibBlockNames.SUBTILE_DAFFOMILL),
			flowerPot(DAFFOMILL, 0));

	public static final Block VINCULOTUS = make(LibBlockNames.SUBTILE_VINCULOTUS,
			new PoweredSpecialFlowerBlock(
					MobEffects.NIGHT_VISION,
					900,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.VINCULOTUS));
	public static final Block FLOATING_VINCULOTUS = make(floating(LibBlockNames.SUBTILE_VINCULOTUS),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.VINCULOTUS));
	public static final Block POTTED_VINCULOTUS = make(potted(LibBlockNames.SUBTILE_VINCULOTUS),
			flowerPot(VINCULOTUS, 0));

	public static final Block SPECTRANTHEMUM = make(LibBlockNames.SUBTILE_SPECTRANTHEMUM,
			new PoweredSpecialFlowerBlock(
					MobEffects.INVISIBILITY,
					360,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.SPECTRANTHEMUM));
	public static final Block FLOATING_SPECTRANTHEMUM = make(floating(LibBlockNames.SUBTILE_SPECTRANTHEMUM),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.SPECTRANTHEMUM));
	public static final Block POTTED_SPECTRANTHEMUM = make(potted(LibBlockNames.SUBTILE_SPECTRANTHEMUM),
			flowerPot(SPECTRANTHEMUM, 0));

	public static final Block MEDUMONE = make(LibBlockNames.SUBTILE_MEDUMONE,
			new PoweredSpecialFlowerBlock(
					MobEffects.MOVEMENT_SLOWDOWN,
					3600,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.MEDUMONE));
	public static final Block FLOATING_MEDUMONE = make(floating(LibBlockNames.SUBTILE_MEDUMONE),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.MEDUMONE));
	public static final Block POTTED_MEDUMONE = make(potted(LibBlockNames.SUBTILE_MEDUMONE),
			flowerPot(MEDUMONE, 0));

	public static final Block MARIMORPHOSIS = make(LibBlockNames.SUBTILE_MARIMORPHOSIS,
			new PoweredSpecialFlowerBlock(
					MobEffects.DIG_SLOWDOWN,
					60,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.MARIMORPHOSIS));
	public static final Block MARIMORPHOSIS_PETITE = make(petite(LibBlockNames.SUBTILE_MARIMORPHOSIS),
			new PoweredSpecialFlowerBlock(
					MobEffects.DIG_SLOWDOWN,
					60,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.MARIMORPHOSIS_PETITE));
	public static final Block FLOATING_MARIMORPHOSIS = make(floating(LibBlockNames.SUBTILE_MARIMORPHOSIS),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.MARIMORPHOSIS));
	public static final Block FLOATING_MARIMORPHOSIS_PETITE = make(petite(floating(LibBlockNames.SUBTILE_MARIMORPHOSIS)),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.MARIMORPHOSIS_PETITE));
	public static final Block POTTED_MARIMORPHOSIS = make(potted(LibBlockNames.SUBTILE_MARIMORPHOSIS),
			flowerPot(MARIMORPHOSIS, 0));
	public static final Block POTTED_MARIMORPHOSIS_PETITE = make(petite(potted(LibBlockNames.SUBTILE_MARIMORPHOSIS)),
			flowerPot(MARIMORPHOSIS_PETITE, 0));

	public static final Block BUBBELL = make(LibBlockNames.SUBTILE_BUBBELL,
			new SpecialFlowerBlock(
					MobEffects.WATER_BREATHING,
					240,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.BUBBELL));
	public static final Block BUBBELL_PETITE = make(petite(LibBlockNames.SUBTILE_BUBBELL),
			new SpecialFlowerBlock(MobEffects.WATER_BREATHING,
					240,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.BUBBELL_PETITE));
	public static final Block FLOATING_BUBBELL = make(floating(LibBlockNames.SUBTILE_BUBBELL),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.BUBBELL));
	public static final Block FLOATING_BUBBELL_PETITE = make(petite(floating(LibBlockNames.SUBTILE_BUBBELL)),
			new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.BUBBELL_PETITE));
	public static final Block POTTED_BUBBELL = make(potted(LibBlockNames.SUBTILE_BUBBELL),
			flowerPot(BUBBELL, 0));
	public static final Block POTTED_BUBBELL_PETITE = make(petite(potted(LibBlockNames.SUBTILE_BUBBELL)),
			flowerPot(BUBBELL_PETITE, 0));

	public static final Block SOLEGNOLIA = make(LibBlockNames.SUBTILE_SOLEGNOLIA,
			new PoweredSpecialFlowerBlock(
					MobEffects.HARM,
					1,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.SOLEGNOLIA));
	public static final Block SOLEGNOLIA_PETITE = make(petite(LibBlockNames.SUBTILE_SOLEGNOLIA),
			new PoweredSpecialFlowerBlock(
					MobEffects.HARM,
					1,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.SOLEGNOLIA_PETITE));
	public static final Block FLOATING_SOLEGNOLIA = make(floating(LibBlockNames.SUBTILE_SOLEGNOLIA),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.SOLEGNOLIA));
	public static final Block FLOATING_SOLEGNOLIA_PETITE = make(petite(floating(LibBlockNames.SUBTILE_SOLEGNOLIA)),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.SOLEGNOLIA_PETITE));
	public static final Block POTTED_SOLEGNOLIA = make(potted(LibBlockNames.SUBTILE_SOLEGNOLIA),
			flowerPot(SOLEGNOLIA, 0));
	public static final Block POTTED_SOLEGNOLIA_PETITE = make(petite(potted(LibBlockNames.SUBTILE_SOLEGNOLIA)),
			flowerPot(SOLEGNOLIA_PETITE, 0));

	public static final Block ORECHID_IGNEM = make(LibBlockNames.SUBTILE_ORECHID_IGNEM,
			new PoweredSpecialFlowerBlock(
					MobEffects.FIRE_RESISTANCE,
					600,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.ORECHID_IGNEM));
	public static final Block FLOATING_ORECHID_IGNEM = make(floating(LibBlockNames.SUBTILE_ORECHID_IGNEM),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.ORECHID_IGNEM));
	public static final Block POTTED_ORECHID_IGNEM = make(potted(LibBlockNames.SUBTILE_ORECHID_IGNEM),
			flowerPot(ORECHID_IGNEM, 0));

	public static final Block LABELLIA = make(LibBlockNames.SUBTILE_LABELLIA,
			new PoweredSpecialFlowerBlock(
					MobEffects.FIRE_RESISTANCE,
					600,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.LABELLIA));
	public static final Block FLOATING_LABELLIA = make(floating(LibBlockNames.SUBTILE_LABELLIA),
			new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER),
					() -> BotaniaBlockEntities.LABELLIA));
	public static final Block POTTED_LABELLIA = make(potted(LibBlockNames.SUBTILE_LABELLIA),
			flowerPot(LABELLIA, 0));

	// petal apothecary variants
	public static final Block PETAL_APOTHECARY = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + "default",
			SoundType.STONE, MapColor.STONE);
	public static final Block PETAL_APOTHECARY_DEEPSLATE = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + "deepslate",
			SoundType.DEEPSLATE, MapColor.DEEPSLATE);
	public static final Block PETAL_APOTHECARY_LIVINGROCK = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + "livingrock",
			SoundType.STONE, MapColor.TERRACOTTA_WHITE);
	public static final Block PETAL_APOTHECARY_BLACKSTONE = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + "blackstone",
			SoundType.STONE, MapColor.COLOR_BLACK);
	public static final Block PETAL_APOTHECARY_NETHER_BRICKS = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + "nether_brick",
			SoundType.NETHER_BRICKS, MapColor.NETHER);
	public static final Block PETAL_APOTHECARY_RED_NETHER_BRICKS = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + "red_nether_brick",
			SoundType.NETHER_BRICKS, MapColor.NETHER);
	public static final Block PETAL_APOTHECARY_MOSSY = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + "mossy",
			SoundType.STONE, MapColor.STONE);
	public static final Block PETAL_APOTHECARY_FUCHSITE = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_FOREST,
			SoundType.TUFF, MapColor.PLANT);
	public static final Block PETAL_APOTHECARY_TALC = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_PLAINS,
			SoundType.CALCITE, MapColor.SNOW);
	public static final Block PETAL_APOTHECARY_GNEISS = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_MOUNTAIN,
			SoundType.DEEPSLATE_TILES, MapColor.COLOR_LIGHT_GRAY);
	public static final Block PETAL_APOTHECARY_MYCELITE = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_FUNGAL,
			SoundType.DEEPSLATE_BRICKS, MapColor.CRIMSON_STEM);
	public static final Block PETAL_APOTHECARY_CATACLASITE = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_SWAMP,
			SoundType.DEEPSLATE_TILES, MapColor.TERRACOTTA_BROWN);
	public static final Block PETAL_APOTHECARY_SOLITE = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_DESERT,
			SoundType.DEEPSLATE, MapColor.TERRACOTTA_ORANGE);
	public static final Block PETAL_APOTHECARY_LUNITE = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_TAIGA,
			SoundType.DEEPSLATE, MapColor.COLOR_BLUE);
	public static final Block PETAL_APOTHECARY_ROSY_TALC = makePetalApothecary(LibBlockNames.APOTHECARY_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_MESA,
			SoundType.CALCITE, MapColor.TERRACOTTA_WHITE);
	public static final Block[] ALL_APOTHECARIES = new Block[] { PETAL_APOTHECARY, PETAL_APOTHECARY_DEEPSLATE,
			PETAL_APOTHECARY_LIVINGROCK, PETAL_APOTHECARY_BLACKSTONE, PETAL_APOTHECARY_NETHER_BRICKS,
			PETAL_APOTHECARY_RED_NETHER_BRICKS, PETAL_APOTHECARY_MOSSY, PETAL_APOTHECARY_FUCHSITE,
			PETAL_APOTHECARY_TALC, PETAL_APOTHECARY_GNEISS, PETAL_APOTHECARY_MYCELITE, PETAL_APOTHECARY_CATACLASITE,
			PETAL_APOTHECARY_SOLITE, PETAL_APOTHECARY_LUNITE, PETAL_APOTHECARY_ROSY_TALC };

	// livingrock blocks
	public static final Block LIVINGROCK = make(LibBlockNames.LIVING_ROCK,
			new Block(BlockBehaviour.Properties.of()
					.strength(2, 10)
					.sound(SoundType.STONE)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.mapColor(MapColor.TERRACOTTA_WHITE)
					.requiresCorrectToolForDrops()));
	public static final Block LIVINGROCK_STAIRS = make(LibBlockNames.LIVING_ROCK + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(LIVINGROCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block LIVINGROCK_SLAB = make(LibBlockNames.LIVING_ROCK + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block LIVINGROCK_WALL = make(LibBlockNames.LIVING_ROCK + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block LIVINGROCK_BUTTON = make(LibBlockNames.LIVING_ROCK + LibBlockNames.BUTTON_SUFFIX,
			new ButtonBlock(BotaniaBlockSetTypes.LIVINGROCK_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block LIVINGROCK_PRESSURE_PLATE = make(LibBlockNames.LIVING_ROCK + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			new PressurePlateBlock(BotaniaBlockSetTypes.LIVINGROCK_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block POLISHED_LIVINGROCK = make(LibBlockNames.LIVING_ROCK_POLISHED,
			new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block POLISHED_LIVINGROCK_STAIRS = make(LibBlockNames.LIVING_ROCK_POLISHED + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(LIVINGROCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(POLISHED_LIVINGROCK)));
	public static final Block POLISHED_LIVINGROCK_SLAB = make(LibBlockNames.LIVING_ROCK_POLISHED + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_LIVINGROCK)));
	public static final Block POLISHED_LIVINGROCK_WALL = make(LibBlockNames.LIVING_ROCK_POLISHED + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_LIVINGROCK)));
	public static final Block LIVINGROCK_SLATE = make(LibBlockNames.LIVING_ROCK_SLATE,
			new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block LIVINGROCK_BRICKS = make(LibBlockNames.LIVING_ROCK_BRICK,
			new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block LIVINGROCK_BRICK_STAIRS = make(LibBlockNames.LIVING_ROCK_BRICK + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(LIVINGROCK_BRICKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(LIVINGROCK_BRICKS)));
	public static final Block LIVINGROCK_BRICK_SLAB = make(LibBlockNames.LIVING_ROCK_BRICK + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK_BRICKS)));
	public static final Block LIVINGROCK_BRICK_WALL = make(LibBlockNames.LIVING_ROCK_BRICK + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK_BRICKS)));
	public static final Block MOSSY_LIVINGROCK_BRICKS = make(LibBlockNames.LIVING_ROCK_BRICK_MOSSY,
			new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block MOSSY_LIVINGROCK_BRICK_STAIRS = make(LibBlockNames.LIVING_ROCK_BRICK_MOSSY + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(MOSSY_LIVINGROCK_BRICKS.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(MOSSY_LIVINGROCK_BRICKS)));
	public static final Block MOSSY_LIVINGROCK_BRICK_SLAB = make(LibBlockNames.LIVING_ROCK_BRICK_MOSSY + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(MOSSY_LIVINGROCK_BRICKS)));
	public static final Block MOSSY_LIVINGROCK_BRICK_WALL = make(LibBlockNames.LIVING_ROCK_BRICK_MOSSY + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(MOSSY_LIVINGROCK_BRICKS)));
	public static final Block CRACKED_LIVINGROCK_BRICKS = make(LibBlockNames.LIVING_ROCK_BRICK_CRACKED,
			new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block CHISELED_LIVINGROCK_BRICKS = make(LibBlockNames.LIVING_ROCK_BRICK_CHISELED,
			new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));

	// livingwood blocks
	public static final Block LIVINGWOOD_LOG = make(LibBlockNames.LIVING_WOOD_LOG,
			new RotatedPillarBlock(BlockBehaviour.Properties.of()
					.strength(2)
					.sound(SoundType.WOOD)
					.instrument(NoteBlockInstrument.BASS)
					.mapColor(state -> state.getValue(BlockStateProperties.AXIS) == Direction.Axis.Y
							? MapColor.TERRACOTTA_RED
							: MapColor.TERRACOTTA_BROWN)));
	public static final Block LIVINGWOOD = make(LibBlockNames.LIVING_WOOD,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_LOG)
					.mapColor(MapColor.TERRACOTTA_BROWN)));
	public static final Block LIVINGWOOD_STAIRS = make(LibBlockNames.LIVING_WOOD + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(LIVINGWOOD.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));
	public static final Block LIVINGWOOD_SLAB = make(LibBlockNames.LIVING_WOOD + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));
	public static final Block LIVINGWOOD_WALL = make(LibBlockNames.LIVING_WOOD + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));
	public static final Block STRIPPED_LIVINGWOOD_LOG = make(LibBlockNames.LIVING_WOOD_LOG_STRIPPED,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_LOG)
					.mapColor(MapColor.TERRACOTTA_RED)));
	public static final Block STRIPPED_LIVINGWOOD = make(LibBlockNames.LIVING_WOOD_STRIPPED,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(STRIPPED_LIVINGWOOD_LOG)));
	public static final Block STRIPPED_LIVINGWOOD_STAIRS = make(LibBlockNames.LIVING_WOOD_STRIPPED + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(STRIPPED_LIVINGWOOD.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));
	public static final Block STRIPPED_LIVINGWOOD_SLAB = make(LibBlockNames.LIVING_WOOD_STRIPPED + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));
	public static final Block STRIPPED_LIVINGWOOD_WALL = make(LibBlockNames.LIVING_WOOD_STRIPPED + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));
	public static final Block GLIMMERING_LIVINGWOOD_LOG = make(LibBlockNames.LIVING_WOOD_LOG_GLIMMERING,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_LOG)
					.lightLevel(constInt(12))));
	public static final Block GLIMMERING_LIVINGWOOD = make(LibBlockNames.LIVING_WOOD_GLIMMERING,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(GLIMMERING_LIVINGWOOD_LOG)
					.mapColor(MapColor.TERRACOTTA_BROWN)));
	public static final Block STRIPPED_GLIMMERING_LIVINGWOOD_LOG = make(LibBlockNames.LIVING_WOOD_LOG_GLIMMERING_STRIPPED,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(STRIPPED_LIVINGWOOD_LOG)
					.lightLevel(b -> 8)));
	public static final Block STRIPPED_GLIMMERING_LIVINGWOOD = make(LibBlockNames.LIVING_WOOD_GLIMMERING_STRIPPED,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(STRIPPED_GLIMMERING_LIVINGWOOD_LOG)
					.mapColor(MapColor.TERRACOTTA_BROWN)));
	public static final Block LIVINGWOOD_PLANKS = make(LibBlockNames.LIVING_WOOD_PLANKS,
			new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_LOG)
					.mapColor(MapColor.TERRACOTTA_RED)));
	public static final Block LIVINGWOOD_PLANK_STAIRS = make(LibBlockNames.LIVING_WOOD_PLANKS + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(LIVINGWOOD_PLANKS.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)));
	public static final Block LIVINGWOOD_PLANK_SLAB = make(LibBlockNames.LIVING_WOOD_PLANKS + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)));
	public static final Block LIVINGWOOD_FENCE = make(LibBlockNames.LIVING_WOOD + LibBlockNames.FENCE_SUFFIX,
			new FenceBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));
	public static final Block LIVINGWOOD_FENCE_GATE = make(LibBlockNames.LIVING_WOOD + LibBlockNames.FENCE_GATE_SUFFIX,
			new FenceGateBlock(BotaniaBlockSetTypes.LIVINGWOOD, BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));
	public static final Block LIVINGWOOD_DOOR = make(LibBlockNames.LIVING_WOOD + LibBlockNames.DOOR_SUFFIX,
			new DoorBlock(BotaniaBlockSetTypes.LIVINGWOOD_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)
							.noOcclusion()
							.pushReaction(PushReaction.DESTROY)));
	public static final Block LIVINGWOOD_TRAPDOOR = make(LibBlockNames.LIVING_WOOD + LibBlockNames.TRAPDOOR_SUFFIX,
			new TrapDoorBlock(BotaniaBlockSetTypes.LIVINGWOOD_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)
							.noOcclusion()
							.isValidSpawn(NO_SPAWN)));
	public static final Block LIVINGWOOD_PRESSURE_PLATE = make(LibBlockNames.LIVING_WOOD + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			new PressurePlateBlock(BotaniaBlockSetTypes.LIVINGWOOD_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)
							.noCollission()
							.strength(0.5F)
							.forceSolidOn()
							.pushReaction(PushReaction.DESTROY)));
	public static final Block LIVINGWOOD_BUTTON = make(LibBlockNames.LIVING_WOOD + LibBlockNames.BUTTON_SUFFIX,
			new ButtonBlock(BotaniaBlockSetTypes.LIVINGWOOD_BLOCK_SET, 30,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)
							.noCollission()
							.strength(0.5F)
							.pushReaction(PushReaction.DESTROY)));
	public static final Block LIVINGWOOD_SIGN = make(LibBlockNames.LIVING_WOOD + LibBlockNames.SIGN_SUFFIX,
			new StandingSignBlock(BotaniaBlockSetTypes.LIVINGWOOD,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)
							.noCollission()
							.strength(1.0f)
							.forceSolidOn()));
	public static final Block LIVINGWOOD_WALL_SIGN = make(LibBlockNames.LIVING_WOOD + LibBlockNames.WALL_INFIX + LibBlockNames.SIGN_SUFFIX,
			new WallSignBlock(BotaniaBlockSetTypes.LIVINGWOOD, BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_SIGN)));
	public static final Block LIVINGWOOD_HANGING_SIGN = make(LibBlockNames.LIVING_WOOD + LibBlockNames.HANGING_SIGN_SUFFIX,
			new CeilingHangingSignBlock(BotaniaBlockSetTypes.LIVINGWOOD,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_SIGN)));
	public static final Block LIVINGWOOD_WALL_HANGING_SIGN = make(LibBlockNames.LIVING_WOOD + LibBlockNames.WALL_INFIX + LibBlockNames.HANGING_SIGN_SUFFIX,
			new WallHangingSignBlock(BotaniaBlockSetTypes.LIVINGWOOD,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_SIGN)));
	public static final Block MOSSY_LIVINGWOOD_PLANKS = make(LibBlockNames.LIVING_WOOD_PLANKS_MOSSY,
			new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)));
	public static final Block FRAMED_LIVINGWOOD = make(LibBlockNames.LIVING_WOOD_FRAMED,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)));
	public static final Block PATTERN_FRAMED_LIVINGWOOD = make(LibBlockNames.LIVING_WOOD_PATTERN_FRAMED,
			new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)));

	// dreamwood blocks
	public static final Block DREAMWOOD_LOG = make(LibBlockNames.DREAM_WOOD_LOG,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_LOG)
					.mapColor(MapColor.QUARTZ)));
	public static final Block DREAMWOOD = make(LibBlockNames.DREAM_WOOD,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_LOG)));
	public static final Block DREAMWOOD_STAIRS = make(LibBlockNames.DREAM_WOOD + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(DREAMWOOD.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(DREAMWOOD)));
	public static final Block DREAMWOOD_SLAB = make(LibBlockNames.DREAM_WOOD + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD)));
	public static final Block DREAMWOOD_WALL = make(LibBlockNames.DREAM_WOOD + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD)));
	public static final Block STRIPPED_DREAMWOOD_LOG = make(LibBlockNames.DREAM_WOOD_LOG_STRIPPED,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_LOG)));
	public static final Block STRIPPED_DREAMWOOD = make(LibBlockNames.DREAM_WOOD_STRIPPED,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_LOG)));
	public static final Block STRIPPED_DREAMWOOD_STAIRS = make(LibBlockNames.DREAM_WOOD_STRIPPED + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(STRIPPED_DREAMWOOD.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(DREAMWOOD)));
	public static final Block STRIPPED_DREAMWOOD_SLAB = make(LibBlockNames.DREAM_WOOD_STRIPPED + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD)));
	public static final Block STRIPPED_DREAMWOOD_WALL = make(LibBlockNames.DREAM_WOOD_STRIPPED + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD)));
	public static final Block GLIMMERING_DREAMWOOD_LOG = make(LibBlockNames.DREAM_WOOD_LOG_GLIMMERING,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(GLIMMERING_LIVINGWOOD_LOG).mapColor(MapColor.QUARTZ)));
	public static final Block GLIMMERING_DREAMWOOD = make(LibBlockNames.DREAM_WOOD_GLIMMERING,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(GLIMMERING_DREAMWOOD_LOG)));
	public static final Block STRIPPED_GLIMMERING_DREAMWOOD_LOG = make(LibBlockNames.DREAM_WOOD_LOG_GLIMMERING_STRIPPED,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(STRIPPED_GLIMMERING_LIVINGWOOD_LOG)
					.mapColor(MapColor.QUARTZ)));
	public static final Block STRIPPED_GLIMMERING_DREAMWOOD = make(LibBlockNames.DREAM_WOOD_GLIMMERING_STRIPPED,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(STRIPPED_GLIMMERING_DREAMWOOD_LOG)));
	public static final Block DREAMWOOD_PLANKS = make(LibBlockNames.DREAM_WOOD_PLANKS,
			new Block(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_LOG)));
	public static final Block DREAMWOOD_PLANK_STAIRS = make(LibBlockNames.DREAM_WOOD_PLANKS + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(DREAMWOOD_PLANKS.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)));
	public static final Block DREAMWOOD_PLANK_SLAB = make(LibBlockNames.DREAM_WOOD_PLANKS + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)));
	public static final Block DREAMWOOD_FENCE = make(LibBlockNames.DREAM_WOOD + LibBlockNames.FENCE_SUFFIX,
			new FenceBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)));
	public static final Block DREAMWOOD_FENCE_GATE = make(LibBlockNames.DREAM_WOOD + LibBlockNames.FENCE_GATE_SUFFIX,
			new FenceGateBlock(BotaniaBlockSetTypes.DREAMWOOD, BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)));
	public static final Block DREAMWOOD_DOOR = make(LibBlockNames.DREAM_WOOD + LibBlockNames.DOOR_SUFFIX,
			new DoorBlock(BotaniaBlockSetTypes.DREAMWOOD_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)
							.noOcclusion()
							.pushReaction(PushReaction.DESTROY)));
	public static final Block DREAMWOOD_TRAPDOOR = make(LibBlockNames.DREAM_WOOD + LibBlockNames.TRAPDOOR_SUFFIX,
			new TrapDoorBlock(BotaniaBlockSetTypes.DREAMWOOD_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)
							.noOcclusion()
							.isValidSpawn(NO_SPAWN)));
	public static final Block DREAMWOOD_PRESSURE_PLATE = make(LibBlockNames.DREAM_WOOD + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			new PressurePlateBlock(BotaniaBlockSetTypes.DREAMWOOD_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)
							.noCollission()
							.strength(0.5F)
							.forceSolidOn()
							.pushReaction(PushReaction.DESTROY)));
	public static final Block DREAMWOOD_BUTTON = make(LibBlockNames.DREAM_WOOD + LibBlockNames.BUTTON_SUFFIX,
			new ButtonBlock(BotaniaBlockSetTypes.DREAMWOOD_BLOCK_SET, 30,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)
							.noCollission()
							.strength(0.5F)
							.pushReaction(PushReaction.DESTROY)));
	public static final Block DREAMWOOD_SIGN = make(LibBlockNames.DREAM_WOOD + LibBlockNames.SIGN_SUFFIX,
			new StandingSignBlock(BotaniaBlockSetTypes.DREAMWOOD,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)
							.noCollission()
							.strength(1.0f)
							.forceSolidOn()));
	public static final Block DREAMWOOD_WALL_SIGN = make(LibBlockNames.DREAM_WOOD + LibBlockNames.WALL_INFIX + LibBlockNames.SIGN_SUFFIX,
			new WallSignBlock(BotaniaBlockSetTypes.DREAMWOOD, BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_SIGN)));
	public static final Block DREAMWOOD_HANGING_SIGN = make(LibBlockNames.DREAM_WOOD + LibBlockNames.HANGING_SIGN_SUFFIX,
			new CeilingHangingSignBlock(BotaniaBlockSetTypes.DREAMWOOD,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_SIGN)));
	public static final Block DREAMWOOD_WALL_HANGING_SIGN = make(LibBlockNames.DREAM_WOOD + LibBlockNames.WALL_INFIX + LibBlockNames.HANGING_SIGN_SUFFIX,
			new WallHangingSignBlock(BotaniaBlockSetTypes.DREAMWOOD,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_SIGN)));
	public static final Block MOSSY_DREAMWOOD_PLANKS = make(LibBlockNames.DREAM_WOOD_PLANKS_MOSSY,
			new Block(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)));
	public static final Block FRAMED_DREAMWOOD = make(LibBlockNames.DREAM_WOOD_FRAMED,
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)));
	public static final Block PATTERN_FRAMED_DREAMWOOD = make(LibBlockNames.DREAM_WOOD_PATTERN_FRAMED,
			new Block(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)));

	// mana spreaders
	public static final ManaSpreaderBlock MANA_SPREADER = makeBlockWithColoredVariants(LibBlockNames.SPREADER,
			color -> new ManaSpreaderBlock(ManaSpreaderBlock.DEFAULT_SPREADER_PARAMETERS, color,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).isValidSpawn(NO_SPAWN).isRedstoneConductor(NEVER)),
			LibBlockNames.COVERED_INFIX);
	public static final ManaSpreaderBlock PULSE_MANA_SPREADER = makeBlockWithColoredVariants(LibBlockNames.SPREADER_REDSTONE,
			color -> new ManaSpreaderBlock(ManaSpreaderBlock.PULSE_SPREADER_PARAMETERS, color,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).isValidSpawn(NO_SPAWN).isRedstoneConductor(NEVER)),
			LibBlockNames.COVERED_INFIX);
	public static final ManaSpreaderBlock ELVEN_MANA_SPREADER = makeBlockWithColoredVariants(LibBlockNames.SPREADER_ELVEN,
			color -> new ManaSpreaderBlock(ManaSpreaderBlock.ELVEN_SPREADER_PARAMETERS, color,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD).isValidSpawn(NO_SPAWN).isRedstoneConductor(NEVER)),
			LibBlockNames.COVERED_INFIX);
	public static final ManaSpreaderBlock GAIA_MANA_SPREADER = makeBlockWithColoredVariants(LibBlockNames.SPREADER_GAIA,
			color -> new ManaSpreaderBlock(ManaSpreaderBlock.GAIA_SPREADER_PARAMETERS, color,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD).isValidSpawn(NO_SPAWN).isRedstoneConductor(NEVER)),
			LibBlockNames.COVERED_INFIX);

	// mana pools
	public static final ManaPoolBlock MANA_POOL = makeBlockWithColoredVariants(LibBlockNames.POOL,
			color -> new ManaPoolBlock(ManaPoolBlock.MAX_MANA, false, false,
					ManaPoolBlock.NORMAL_SHAPE_VARIANT, color, BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final ManaPoolBlock CREATIVE_MANA_POOL = makeBlockWithColoredVariants(LibBlockNames.POOL_CREATIVE,
			color -> new ManaPoolBlock(ManaPoolBlock.MAX_MANA, false, true,
					ManaPoolBlock.BIG_SHAPE_VARIANT, color, BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final ManaPoolBlock DILUTED_MANA_POOL = makeBlockWithColoredVariants(LibBlockNames.POOL_DILUTED,
			color -> new ManaPoolBlock(ManaPoolBlock.MAX_MANA_DILUTED, false, false,
					ManaPoolBlock.SMALL_SHAPE_VARIANT, color, BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final ManaPoolBlock FABULOUS_MANA_POOL = makeBlockWithColoredVariants(LibBlockNames.POOL_FABULOUS,
			color -> new ManaPoolBlock(ManaPoolBlock.MAX_MANA, true, false,
					ManaPoolBlock.NORMAL_SHAPE_VARIANT, color, BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block ALCHEMY_CATALYST = make(LibBlockNames.ALCHEMY_CATALYST,
			new AlchemyCatalystBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block CONJURATION_CATALYST = make(LibBlockNames.CONJURATION_CATALYST,
			new ConjurationCatalystBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));

	// resource blocks
	public static final Block MANASTEEL_BLOCK = make(LibBlockNames.MANASTEEL_BLOCK,
			new Block(BlockBehaviour.Properties.of()
					.strength(3, 10)
					.mapColor(MapColor.LAPIS)
					.sound(SoundType.METAL)
					.instrument(NoteBlockInstrument.IRON_XYLOPHONE)
					.requiresCorrectToolForDrops()));
	public static final Block TERRASTEEL_BLOCK = make(LibBlockNames.TERRASTEEL_BLOCK,
			new Block(BlockBehaviour.Properties.ofFullCopy(MANASTEEL_BLOCK)
					.mapColor(MapColor.EMERALD)));
	public static final Block ELEMENTIUM_BLOCK = make(LibBlockNames.ELEMENTIUM_BLOCK,
			new Block(BlockBehaviour.Properties.ofFullCopy(MANASTEEL_BLOCK)
					.mapColor(MapColor.COLOR_PINK)));
	public static final Block MANA_DIAMOND_BLOCK = make(LibBlockNames.MANA_DIAMOND_BLOCK,
			new Block(BlockBehaviour.Properties.ofFullCopy(MANASTEEL_BLOCK)
					.instrument(NoteBlockInstrument.HARP)
					.mapColor(MapColor.DIAMOND)));
	public static final Block DRAGONSTONE_BLOCK = make(LibBlockNames.DRAGONSTONE_BLOCK,
			new Block(BlockBehaviour.Properties.ofFullCopy(MANA_DIAMOND_BLOCK)
					.mapColor(MapColor.COLOR_PINK)));

	// glass blocks
	public static final Block MANAGLASS = make(LibBlockNames.MANA_GLASS,
			// [VanillaCopy] Blocks.GLASS
			new TransparentBlock(BlockBehaviour.Properties.of()
					.instrument(NoteBlockInstrument.HAT)
					.strength(0.3F)
					.sound(SoundType.GLASS)
					.noOcclusion()
					.isValidSpawn(NO_SPAWN)
					.isRedstoneConductor(NEVER)
					.isSuffocating(NEVER)
					.isViewBlocking(NEVER)
					// Botania: mana glasses emit a lot of light
					.lightLevel(constInt(15))));
	public static final Block ALFGLASS = make(LibBlockNames.ELF_GLASS,
			new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(MANAGLASS)));
	public static final Block TEMPORARY_BIFROST_BLOCK = make(LibBlockNames.BIFROST,
			new BifrostBlock(BlockBehaviour.Properties.ofFullCopy(MANAGLASS)
					.destroyTime(-1)));
	public static final Block BIFROST_BLOCK = make(LibBlockNames.BIFROST_PERM,
			new PermanentBifrostBlock(BlockBehaviour.Properties.ofFullCopy(MANAGLASS)));

	// recipe blocks
	public static final Block RUNIC_ALTAR = make(LibBlockNames.RUNE_ALTAR,
			new RunicAltarBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)
					.requiresCorrectToolForDrops()));
	public static final Block MANA_ENCHANTER = make(LibBlockNames.ENCHANTER,
			new ManaEnchanterBlock(BlockBehaviour.Properties.of()
					.mapColor(MapColor.LAPIS)
					.strength(3, 5)
					.lightLevel(constInt(15))
					.sound(SoundType.STONE)));
	public static final Block BOTANICAL_BREWERY = make(LibBlockNames.BREWERY,
			new BotanicalBreweryBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block TERRESTRIAL_AGGLOMERATION_PLATE = make(LibBlockNames.TERRA_PLATE,
			new TerrestrialAgglomerationPlateBlock(BlockBehaviour.Properties.of()
					.mapColor(MapColor.LAPIS)
					.strength(3, 10)
					.sound(SoundType.METAL)
					.requiresCorrectToolForDrops()));
	public static final Block ELVEN_GATEWAY_CORE = make(LibBlockNames.ALF_PORTAL,
			new AlfheimPortalBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)
					.strength(10)
					.sound(SoundType.WOOD)
					.lightLevel(state -> state.getValue(
							BotaniaStateProperties.ALFPORTAL_STATE) != AlfheimPortalState.OFF ? 15 : 0)));

	// pylons
	public static final Block MANA_PYLON = make(LibBlockNames.PYLON,
			new ManaPylonBlock(BlockBehaviour.Properties.of()
					.mapColor(DyeColor.LIGHT_BLUE)
					.strength(5.5F)
					.sound(SoundType.METAL)
					.lightLevel(constInt(7))
					.requiresCorrectToolForDrops()));
	public static final Block NATURA_PYLON = make(LibBlockNames.PYLON_NATURA,
			new NaturaPylonBlock(BlockBehaviour.Properties.ofFullCopy(MANA_PYLON)
					.mapColor(MapColor.EMERALD)));
	public static final Block GAIA_PYLON = make(LibBlockNames.PYLON_GAIA,
			new GaiaPylonBlock(BlockBehaviour.Properties.ofFullCopy(MANA_PYLON)
					.mapColor(DyeColor.PINK)));

	public static final Block MANA_SPLITTER = make(LibBlockNames.DISTRIBUTOR,
			new ManaSplitterBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block MANA_VOID = make(LibBlockNames.MANA_VOID,
			new ManaVoidBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)
					.strength(2, 2000)));
	public static final Block MANA_DETECTOR = make(LibBlockNames.MANA_DETECTOR,
			new ManaDetectorBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block FORCE_RELAY = make(LibBlockNames.PISTON_RELAY,
			new ForceRelayBlock(BlockBehaviour.Properties.of()
					.strength(2, 10)
					.sound(SoundType.METAL)
					.mapColor(MapColor.COLOR_PURPLE)
					.isValidSpawn(NO_SPAWN)));
	public static final Block SPREADER_TURNTABLE = make(LibBlockNames.TURNTABLE,
			new SpreaderTurntableBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));
	public static final Block TINY_PLANET = make(LibBlockNames.TINY_PLANET,
			new TinyPlanetBlock(BlockBehaviour.Properties.of()
					.mapColor(MapColor.DEEPSLATE)
					.strength(20, 100)
					.sound(SoundType.DEEPSLATE)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.requiresCorrectToolForDrops()));
	public static final Block DRUM_OF_THE_WILD = make(LibBlockNames.DRUM_WILD,
			new DrumOfTheWildBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block DRUM_OF_THE_GATHERING = make(LibBlockNames.DRUM_GATHERING,
			new DrumOfTheGatheringBlock(BlockBehaviour.Properties.ofFullCopy(DRUM_OF_THE_WILD)));
	public static final Block DRUM_OF_THE_CANOPY = make(LibBlockNames.DRUM_CANOPY,
			new DrumOfTheCanopyBlock(BlockBehaviour.Properties.ofFullCopy(DRUM_OF_THE_WILD)));
	public static final Block LIFE_IMBUER = make(LibBlockNames.SPAWNER_CLAW,
			new LifeImbuerBlock(BlockBehaviour.Properties.of()
					.sound(SoundType.METAL)
					.strength(3)
					.requiresCorrectToolForDrops()));
	public static final Block MANA_FLUXFIELD = make(LibBlockNames.FLUXFIELD,
			new PowerGeneratorBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block MANA_PRISM = make(LibBlockNames.PRISM,
			new ManaPrismBlock(BlockBehaviour.Properties.ofFullCopy(ALFGLASS)
					.noCollission()));
	public static final Block MANA_PUMP = make(LibBlockNames.PUMP,
			new ManaPumpBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block SPARK_TINKERER = make(LibBlockNames.SPARK_CHANGER,
			new SparkTinkererBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block MANASTORM_CHARGE = make(LibBlockNames.MANA_BOMB,
			new ManastormChargeBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)
					.strength(12)));
	public static final Block MANATIDE_BELLOWS = make(LibBlockNames.BELLOWS,
			new BellowsBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));

	public static final Block OPEN_CRATE = make(LibBlockNames.OPEN_CRATE,
			new OpenCrateBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));
	public static final Block CRAFTY_CRATE = make(LibBlockNames.CRAFT_CRATE,
			new CraftyCrateBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));
	public static final Block EYE_OF_THE_ANCIENTS = make(LibBlockNames.FOREST_EYE,
			new EyeOfTheAncientsBlock(BlockBehaviour.Properties.of()
					.strength(5, 10)
					.sound(SoundType.METAL)
					.requiresCorrectToolForDrops()));
	public static final Block SOLID_VINE = make(LibBlockNames.SOLID_VINE,
			new SolidVineBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.VINE)));
	public static final Block ABSTRUSE_PLATFORM = make(LibBlockNames.PLATFORM_ABSTRUSE,
			new AbstrusePlatformBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)
					.strength(2, 5)
					.isValidSpawn(NO_SPAWN)
					.noOcclusion()
					.isViewBlocking(NEVER)
					.isSuffocating(NEVER)));
	public static final Block SPECTRAL_PLATFORM = make(LibBlockNames.PLATFORM_SPECTRAL,
			new SpectralPlatformBlock(BlockBehaviour.Properties.ofFullCopy(ABSTRUSE_PLATFORM)));
	public static final Block INFRANGIBLE_PLATFORM = make(LibBlockNames.PLATFORM_INFRANGIBLE,
			new InfrangiblePlatformBlock(BlockBehaviour.Properties.ofFullCopy(ABSTRUSE_PLATFORM)
					.strength(-1, Float.MAX_VALUE)
					.isValidSpawn(NO_SPAWN)
					.noOcclusion()));
	public static final Block TINY_POTATO = make(LibBlockNames.TINY_POTATO,
			new TinyPotatoBlock(BlockBehaviour.Properties.of()
					.strength(0.25F)
					.mapColor(DyeColor.PINK)));
	public static final Block ENDER_OVERSEER = make(LibBlockNames.ENDER_EYE_BLOCK,
			new EnderOverseerBlock(BlockBehaviour.Properties.ofFullCopy(MANASTEEL_BLOCK)));
	public static final Block RED_STRINGED_CONTAINER = make(LibBlockNames.RED_STRING_CONTAINER,
			new RedStringContainerBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block RED_STRINGED_DISPENSER = make(LibBlockNames.RED_STRING_DISPENSER,
			new RedStringDispenserBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block RED_STRINGED_NUTRIFIER = make(LibBlockNames.RED_STRING_FERTILIZER,
			new RedStringNutrifierBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block RED_STRINGED_COMPARATOR = make(LibBlockNames.RED_STRING_COMPARATOR,
			new RedStringComparatorBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block RED_STRINGED_SPOOFER = make(LibBlockNames.RED_STRING_RELAY,
			new RedStringSpooferBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block RED_STRINGED_INTERCEPTOR = make(LibBlockNames.RED_STRING_INTERCEPTOR,
			new RedStringInterceptorBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));

	public static final Block CORPOREA_FUNNEL = make(LibBlockNames.CORPOREA_FUNNEL,
			new CorporeaFunnelBlock(BlockBehaviour.Properties.of()
					.strength(5.5F)
					.mapColor(DyeColor.PURPLE)
					.sound(SoundType.METAL)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.requiresCorrectToolForDrops()));
	public static final Block CORPOREA_INTERCEPTOR = make(LibBlockNames.CORPOREA_INTERCEPTOR,
			new CorporeaInterceptorBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_FUNNEL)));
	public static final Block CORPOREA_INDEX = make(LibBlockNames.CORPOREA_INDEX,
			new CorporeaIndexBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_FUNNEL)
					.noOcclusion()));
	public static final Block CORPOREA_CRYSTAL_CUBE = make(LibBlockNames.CORPOREA_CRYSTAL_CUBE,
			new CorporeaCrystalCubeBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_FUNNEL)));
	public static final Block CORPOREA_RETAINER = make(LibBlockNames.CORPOREA_RETAINER,
			new CorporeaRetainerBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_FUNNEL)));

	public static final Block CORPOREA_BLOCK = make(LibBlockNames.CORPOREA_BLOCK,
			new Block(BlockBehaviour.Properties.ofFullCopy(CORPOREA_FUNNEL)));
	public static final Block CORPOREA_STAIRS = make(LibBlockNames.CORPOREA_PREFIX + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(CORPOREA_BLOCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CORPOREA_BLOCK)));
	public static final Block CORPOREA_SLAB = make(LibBlockNames.CORPOREA_PREFIX + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_BLOCK)));
	public static final Block CORPOREA_WALL = make(LibBlockNames.CORPOREA_PREFIX + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_BLOCK)));
	public static final Block CORPOREA_BUTTON = make(LibBlockNames.CORPOREA_PREFIX + LibBlockNames.BUTTON_SUFFIX,
			new ButtonBlock(BotaniaBlockSetTypes.CORPOREA_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(DyeColor.PURPLE)));
	public static final Block CORPOREA_PRESSURE_PLATE = make(LibBlockNames.CORPOREA_PREFIX + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			new PressurePlateBlock(BotaniaBlockSetTypes.CORPOREA_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(DyeColor.PURPLE)));
	public static final Block CORPOREA_BRICKS = make(LibBlockNames.CORPOREA_BRICK,
			new Block(BlockBehaviour.Properties.ofFullCopy(CORPOREA_BLOCK)));
	public static final Block CORPOREA_BRICK_STAIRS = make(LibBlockNames.CORPOREA_BRICK + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(CORPOREA_BRICKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CORPOREA_BRICKS)));
	public static final Block CORPOREA_BRICK_SLAB = make(LibBlockNames.CORPOREA_BRICK + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_BRICKS)));
	public static final Block CORPOREA_BRICK_WALL = make(LibBlockNames.CORPOREA_BRICK + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_BRICKS)));

	public static final Block INCENSE_PLATE = make(LibBlockNames.INCENSE_PLATE,
			new IncensePlateBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)
					.lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 2 : 0)));
	public static final Block HOVERING_HOURGLASS = make(LibBlockNames.HOURGLASS,
			new HoveringHourglassBlock(BlockBehaviour.Properties.of()
					.mapColor(MapColor.GOLD)
					.strength(2)
					.sound(SoundType.METAL)));
	public static final Block SPECTRAL_RAIL = make(LibBlockNames.GHOST_RAIL,
			new SpectralRailBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RAIL)));
	public static final Block LUMINIZER = make(LibBlockNames.LIGHT_RELAY,
			new LuminizerBlock(BlockBehaviour.Properties.of()
					.noCollission()));
	public static final Block DETECTOR_LUMINIZER = make("detector" + LibBlockNames.LIGHT_RELAY_SUFFIX,
			new LuminizerDetectorBlock(BlockBehaviour.Properties.ofFullCopy(LUMINIZER)));
	public static final Block FORK_LUMINIZER = make("fork" + LibBlockNames.LIGHT_RELAY_SUFFIX,
			new LuminizerForkBlock(BlockBehaviour.Properties.ofFullCopy(LUMINIZER)));
	public static final Block TOGGLE_LUMINIZER = make("toggle" + LibBlockNames.LIGHT_RELAY_SUFFIX,
			new LuminizerToggleBlock(BlockBehaviour.Properties.ofFullCopy(LUMINIZER)));
	public static final Block LUMINIZER_LAUNCHER = make(LibBlockNames.LIGHT_LAUNCHER,
			new LuminizerLauncherBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));
	public static final Block CACOPHONIUM_BLOCK = make(LibBlockNames.CACOPHONIUM,
			new CacophoniumBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NOTE_BLOCK)
					.strength(0.8F)));
	public static final Block CELLULAR_BLOCK = make(LibBlockNames.CELL_BLOCK,
			new CellularBlock(BlockBehaviour.Properties.of()
					.pushReaction(PushReaction.DESTROY)
					.sound(SoundType.WOOL)));
	public static final Block TERU_TERU_BOZU = make(LibBlockNames.TERU_TERU_BOZU,
			new TeruTeruBozuBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOL)
					.instrument(NoteBlockInstrument.GUITAR)
					.mapColor(DyeColor.WHITE)));
	public static final Block LIVINGWOOD_AVATAR = make(LibBlockNames.AVATAR,
			new AvatarBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));
	public static final Block FAKE_AIR = make(LibBlockNames.FAKE_AIR,
			new FakeAirBlock(BlockBehaviour.Properties.of()
					.replaceable()
					.noCollission()
					.noLootTable()
					.air()
					.randomTicks()));
	public static final Block LIVING_ROOT = make(LibBlockNames.ROOT,
			new LivingRootBlock(BlockBehaviour.Properties.of()
					.strength(1.2F)
					.sound(SoundType.WOOD)));
	public static final Block FEL_PUMPKIN = make(LibBlockNames.FEL_PUMPKIN,
			new FelPumpkinBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CARVED_PUMPKIN)));
	public static final Block COCOON_OF_CAPRICE = make(LibBlockNames.COCOON,
			new CocoonBlock(BlockBehaviour.Properties.of()
					.strength(3, 60)
					.sound(SoundType.WOOL)));
	public static final Block ANIMATED_TORCH = make(LibBlockNames.ANIMATED_TORCH,
			new AnimatedTorchBlock(BlockBehaviour.Properties.of()
					.noCollission()
					.instabreak()
					.lightLevel(state -> state.getValue(AnimatedTorchBlock.TRIGGERED) ? 0 : 7)
					.sound(SoundType.WOOD)
					.pushReaction(PushReaction.DESTROY)));
	public static final Block STARFIELD_CREATOR = make(LibBlockNames.STARFIELD,
			new StarfieldCreatorBlock(BlockBehaviour.Properties.of()
					.mapColor(DyeColor.PINK)
					.strength(5, 2000)
					.sound(SoundType.METAL)));

	public static final Block AZULEJO_0 = make(LibBlockNames.AZULEJO_PREFIX + 0,
			new Block(BlockBehaviour.Properties.of()
					.mapColor(MapColor.LAPIS)
					.strength(2, 5)
					.sound(SoundType.STONE)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.requiresCorrectToolForDrops()));
	public static final Block AZULEJO_1 = make(LibBlockNames.AZULEJO_PREFIX + 1,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));
	public static final Block AZULEJO_2 = make(LibBlockNames.AZULEJO_PREFIX + 2,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));
	public static final Block AZULEJO_3 = make(LibBlockNames.AZULEJO_PREFIX + 3,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));
	public static final Block AZULEJO_4 = make(LibBlockNames.AZULEJO_PREFIX + 4,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));
	public static final Block AZULEJO_5 = make(LibBlockNames.AZULEJO_PREFIX + 5,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));
	public static final Block AZULEJO_6 = make(LibBlockNames.AZULEJO_PREFIX + 6,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));
	public static final Block AZULEJO_7 = make(LibBlockNames.AZULEJO_PREFIX + 7,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));
	public static final Block AZULEJO_8 = make(LibBlockNames.AZULEJO_PREFIX + 8,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));
	public static final Block AZULEJO_9 = make(LibBlockNames.AZULEJO_PREFIX + 9,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));
	public static final Block AZULEJO_10 = make(LibBlockNames.AZULEJO_PREFIX + 10,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));
	public static final Block AZULEJO_11 = make(LibBlockNames.AZULEJO_PREFIX + 11,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));
	public static final Block AZULEJO_12 = make(LibBlockNames.AZULEJO_PREFIX + 12,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));
	public static final Block AZULEJO_13 = make(LibBlockNames.AZULEJO_PREFIX + 13,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));
	public static final Block AZULEJO_14 = make(LibBlockNames.AZULEJO_PREFIX + 14,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));
	public static final Block AZULEJO_15 = make(LibBlockNames.AZULEJO_PREFIX + 15,
			new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0)));

	public static final Block MANA_FLAME = make(LibBlockNames.MANA_FLAME,
			new ManaFlameBlock(BlockBehaviour.Properties.of()
					.pushReaction(PushReaction.DESTROY)
					.sound(SoundType.WOOL)
					.lightLevel(constInt(15))
					.noCollission()));
	public static final Block BLAZE_MESH = make(LibBlockNames.BLAZE_BLOCK,
			new Block(BlockBehaviour.Properties.ofFullCopy(MANASTEEL_BLOCK)
					.instrument(NoteBlockInstrument.PLING)
					.lightLevel(constInt(15))
					.mapColor(MapColor.GOLD)));
	public static final Block GAIA_HEAD = make(LibBlockNames.GAIA_HEAD,
			new GaiaHeadBlock(BlockBehaviour.Properties.of()
					.pushReaction(PushReaction.DESTROY)
					.strength(1)));
	public static final Block GAIA_WALL_HEAD_BLOCK = make(LibBlockNames.GAIA_WALL_HEAD,
			new GaiaWallHeadBlock(BlockBehaviour.Properties.ofFullCopy(GAIA_HEAD)));

	public static final Block SHIMMERROCK = make(LibBlockNames.SHIMMERROCK,
			new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)));
	public static final Block SHIMMERROCK_STAIRS = make(LibBlockNames.SHIMMERROCK + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(SHIMMERROCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SHIMMERROCK)));
	public static final Block SHIMMERROCK_SLAB = make(LibBlockNames.SHIMMERROCK + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SHIMMERROCK)));
	public static final Block SHIMMERROCK_WALL = make(LibBlockNames.SHIMMERROCK + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(SHIMMERROCK)));
	public static final Block SHIMMERROCK_BUTTON = make(LibBlockNames.SHIMMERROCK + LibBlockNames.BUTTON_SUFFIX,
			new ButtonBlock(BotaniaBlockSetTypes.SHIMMERROCK_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block SHIMMERROCK_PRESSURE_PLATE = make(LibBlockNames.SHIMMERROCK + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			new PressurePlateBlock(BotaniaBlockSetTypes.SHIMMERROCK_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block SHIMMERWOOD_PLANKS = make(LibBlockNames.SHIMMERWOOD_PLANKS,
			new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)));
	public static final Block SHIMMERWOOD_PLANK_STAIRS = make(LibBlockNames.SHIMMERWOOD_PLANKS + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(SHIMMERWOOD_PLANKS.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SHIMMERWOOD_PLANKS)));
	public static final Block SHIMMERWOOD_PLANK_SLAB = make(LibBlockNames.SHIMMERWOOD_PLANKS + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SHIMMERWOOD_PLANKS)));
	public static final Block SHIMMERWOOD_FENCE = make(LibBlockNames.SHIMMERWOOD_PREFIX + LibBlockNames.FENCE_SUFFIX,
			new FenceBlock(BlockBehaviour.Properties.ofFullCopy(SHIMMERWOOD_PLANKS)));
	public static final Block SHIMMERWOOD_FENCE_GATE = make(LibBlockNames.SHIMMERWOOD_PREFIX + LibBlockNames.FENCE_GATE_SUFFIX,
			new FenceGateBlock(BotaniaBlockSetTypes.SHIMMERWOOD,
					BlockBehaviour.Properties.ofFullCopy(SHIMMERWOOD_PLANKS)));
	public static final Block SHIMMERWOOD_PRESSURE_PLATE = make(LibBlockNames.SHIMMERWOOD_PREFIX + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			new PressurePlateBlock(BotaniaBlockSetTypes.SHIMMERWOOD_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(SHIMMERWOOD_PLANKS)
							.noCollission()
							.strength(0.5F)
							.forceSolidOn()
							.pushReaction(PushReaction.DESTROY)));
	public static final Block SHIMMERWOOD_BUTTON = make(LibBlockNames.SHIMMERWOOD_PREFIX + LibBlockNames.BUTTON_SUFFIX,
			new ButtonBlock(BotaniaBlockSetTypes.SHIMMERWOOD_BLOCK_SET, 30,
					BlockBehaviour.Properties.ofFullCopy(SHIMMERWOOD_PLANKS)
							.noCollission()
							.strength(0.5F)
							.pushReaction(PushReaction.DESTROY)));

	// grass blocks
	public static final Block DRY_GRASS_BLOCK = make("dry" + LibBlockNames.ALT_GRASS_SUFFIX,
			new BotaniaGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
					.mapColor(MapColor.TERRACOTTA_LIGHT_GREEN)));
	public static final Block GOLDEN_GRASS_BLOCK = make("golden" + LibBlockNames.ALT_GRASS_SUFFIX,
			new BotaniaGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
					.mapColor(MapColor.GOLD)));
	public static final Block VIVID_GRASS_BLOCK = make("vivid" + LibBlockNames.ALT_GRASS_SUFFIX,
			new BotaniaGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
					.mapColor(MapColor.PLANT)));
	public static final Block SCORCHED_GRASS_BLOCK = make("scorched" + LibBlockNames.ALT_GRASS_SUFFIX,
			new BotaniaScorchedGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
					.mapColor(MapColor.NETHER)));
	public static final Block INFUSED_GRASS_BLOCK = make("infused" + LibBlockNames.ALT_GRASS_SUFFIX,
			new BotaniaInfusedGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
					.mapColor(MapColor.COLOR_CYAN)));
	public static final Block MUTATED_GRASS_BLOCK = make("mutated" + LibBlockNames.ALT_GRASS_SUFFIX,
			new BotaniaMutatedGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
					.mapColor(MapColor.WARPED_HYPHAE)));

	public static final Block DAYBLOOM_MOTIF = make(LibBlockNames.MOTIF_DAYBLOOM,
			new FlowerMotifBlock(MobEffects.BLINDNESS, 15, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY), true));
	public static final Block NIGHTSHADE_MOTIF = make(LibBlockNames.MOTIF_NIGHTSHADE,
			new FlowerMotifBlock(MobEffects.POISON, 20, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY), true));
	public static final Block HYDROANGEAS_MOTIF = make(LibBlockNames.MOTIF_HYDROANGEAS,
			new FlowerMotifBlock(MobEffects.UNLUCK, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY), false));

	public static final Block POTTED_DAYBLOOM_MOTIF = make(LibBlockNames.POTTED_PREFIX + LibBlockNames.MOTIF_DAYBLOOM,
			flowerPot(DAYBLOOM_MOTIF, 0));
	public static final Block POTTED_NIGHTSHADE_MOTIF = make(LibBlockNames.POTTED_PREFIX + LibBlockNames.MOTIF_NIGHTSHADE,
			flowerPot(NIGHTSHADE_MOTIF, 0));
	public static final Block POTTED_HYDROANGEAS_MOTIF = make(LibBlockNames.POTTED_PREFIX + LibBlockNames.MOTIF_HYDROANGEAS,
			flowerPot(HYDROANGEAS_MOTIF, 0));

	// quartz variants
	public static final Block DARK_QUARTZ_BLOCK = make(LibBlockNames.QUARTZ_DARK,
			new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
					.mapColor(MapColor.COLOR_BLACK)));
	public static final Block DARK_QUARTZ_STAIRS = make(LibBlockNames.QUARTZ_DARK + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(DARK_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(DARK_QUARTZ_BLOCK)));
	public static final Block DARK_QUARTZ_SLAB = make(LibBlockNames.QUARTZ_DARK + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DARK_QUARTZ_BLOCK)));
	public static final Block CHISELED_DARK_QUARTZ_BLOCK = make("chiseled_" + LibBlockNames.QUARTZ_DARK,
			new Block(BlockBehaviour.Properties.ofFullCopy(DARK_QUARTZ_BLOCK)));
	public static final Block DARK_QUARTZ_BRICKS = make(LibBlockNames.QUARTZ_DARK + "_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(DARK_QUARTZ_BLOCK)));
	public static final Block DARK_QUARTZ_PILLAR = make(LibBlockNames.QUARTZ_DARK + "_pillar",
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(DARK_QUARTZ_BLOCK)));
	public static final Block SMOOTH_DARK_QUARTZ_BLOCK = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_DARK,
			new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ)
					.mapColor(MapColor.COLOR_BLACK)));
	public static final Block SMOOTH_DARK_QUARTZ_STAIRS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_DARK + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(SMOOTH_DARK_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOOTH_DARK_QUARTZ_BLOCK)));
	public static final Block SMOOTH_DARK_QUARTZ_SLAB = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_DARK + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_DARK_QUARTZ_BLOCK)));

	public static final Block MANA_QUARTZ_BLOCK = make(LibBlockNames.QUARTZ_MANA,
			new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
					.mapColor(MapColor.DIAMOND)));
	public static final Block MANA_QUARTZ_STAIRS = make(LibBlockNames.QUARTZ_MANA + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(MANA_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(DARK_QUARTZ_BLOCK)));
	public static final Block MANA_QUARTZ_SLAB = make(LibBlockNames.QUARTZ_MANA + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(MANA_QUARTZ_BLOCK)));
	public static final Block CHISELED_MANA_QUARTZ_BLOCK = make("chiseled_" + LibBlockNames.QUARTZ_MANA,
			new Block(BlockBehaviour.Properties.ofFullCopy(MANA_QUARTZ_BLOCK)));
	public static final Block MANA_QUARTZ_BRICKS = make(LibBlockNames.QUARTZ_MANA + "_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(MANA_QUARTZ_BLOCK)));
	public static final Block MANA_QUARTZ_PILLAR = make(LibBlockNames.QUARTZ_MANA + "_pillar",
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(MANA_QUARTZ_BLOCK)));
	public static final Block SMOOTH_MANA_QUARTZ_BLOCK = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_MANA,
			new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ)
					.mapColor(MapColor.DIAMOND)));
	public static final Block SMOOTH_MANA_QUARTZ_STAIRS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_MANA + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(SMOOTH_MANA_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOOTH_MANA_QUARTZ_BLOCK)));
	public static final Block SMOOTH_MANA_QUARTZ_SLAB = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_MANA + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_MANA_QUARTZ_BLOCK)));

	public static final Block BLAZE_QUARTZ_BLOCK = make(LibBlockNames.QUARTZ_BLAZE,
			new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
					.mapColor(MapColor.SAND)));
	public static final Block BLAZE_QUARTZ_STAIRS = make(LibBlockNames.QUARTZ_BLAZE + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(BLAZE_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(BLAZE_QUARTZ_BLOCK)));
	public static final Block BLAZE_QUARTZ_SLAB = make(LibBlockNames.QUARTZ_BLAZE + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BLAZE_QUARTZ_BLOCK)));
	public static final Block CHISELED_BLAZE_QUARTZ_BLOCK = make("chiseled_" + LibBlockNames.QUARTZ_BLAZE,
			new Block(BlockBehaviour.Properties.ofFullCopy(BLAZE_QUARTZ_BLOCK)));
	public static final Block BLAZE_QUARTZ_BRICKS = make(LibBlockNames.QUARTZ_BLAZE + "_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(BLAZE_QUARTZ_BLOCK)));
	public static final Block BLAZE_QUARTZ_PILLAR = make(LibBlockNames.QUARTZ_BLAZE + "_pillar",
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(BLAZE_QUARTZ_BLOCK)));
	public static final Block SMOOTH_BLAZE_QUARTZ_BLOCKS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_BLAZE,
			new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ)
					.mapColor(MapColor.SAND)));
	public static final Block SMOOTH_BLAZE_QUARTZ_STAIRS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_BLAZE + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(SMOOTH_BLAZE_QUARTZ_BLOCKS.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOOTH_BLAZE_QUARTZ_BLOCKS)));
	public static final Block SMOOTH_BLAZE_QUARTZ_SLAB = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_BLAZE + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_BLAZE_QUARTZ_BLOCKS)));

	public static final Block LAVENDER_QUARTZ_BLOCK = make(LibBlockNames.QUARTZ_LAVENDER,
			new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
					.mapColor(MapColor.COLOR_PINK)));
	public static final Block LAVENDER_QUARTZ_STAIRS = make(LibBlockNames.QUARTZ_LAVENDER + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(LAVENDER_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(LAVENDER_QUARTZ_BLOCK)));
	public static final Block LAVENDER_QUARTZ_SLAB = make(LibBlockNames.QUARTZ_LAVENDER + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LAVENDER_QUARTZ_BLOCK)));
	public static final Block CHISELED_LAVENDER_QUARTZ_BLOCK = make("chiseled_" + LibBlockNames.QUARTZ_LAVENDER,
			new Block(BlockBehaviour.Properties.ofFullCopy(LAVENDER_QUARTZ_BLOCK)));
	public static final Block LAVENDER_QUARTZ_BRICKS = make(LibBlockNames.QUARTZ_LAVENDER + "_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(LAVENDER_QUARTZ_BLOCK)));
	public static final Block LAVENDER_QUARTZ_PILLAR = make(LibBlockNames.QUARTZ_LAVENDER + "_pillar",
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(LAVENDER_QUARTZ_BLOCK)));
	public static final Block SMOOTH_LAVENDER_QUARTZ_BLOCK = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_LAVENDER,
			new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ)
					.mapColor(MapColor.COLOR_PINK)));
	public static final Block SMOOTH_LAVENDER_QUARTZ_STAIRS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_LAVENDER + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(SMOOTH_LAVENDER_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOOTH_LAVENDER_QUARTZ_BLOCK)));
	public static final Block SMOOTH_LAVENDER_QUARTZ_SLAB = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_LAVENDER + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_LAVENDER_QUARTZ_BLOCK)));

	public static final Block RED_QUARTZ_BLOCK = make(LibBlockNames.QUARTZ_RED,
			new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
					.mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block RED_QUARTZ_STAIRS = make(LibBlockNames.QUARTZ_RED + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(RED_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(RED_QUARTZ_BLOCK)));
	public static final Block RED_QUARTZ_SLAB = make(LibBlockNames.QUARTZ_RED + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(RED_QUARTZ_BLOCK)));
	public static final Block CHISELED_RED_QUARTZ_BLOCK = make("chiseled_" + LibBlockNames.QUARTZ_RED,
			new Block(BlockBehaviour.Properties.ofFullCopy(RED_QUARTZ_BLOCK)));
	public static final Block RED_QUARTZ_BRICKS = make(LibBlockNames.QUARTZ_RED + "_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(RED_QUARTZ_BLOCK)));
	public static final Block RED_QUARTZ_PILLAR = make(LibBlockNames.QUARTZ_RED + "_pillar",
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(RED_QUARTZ_BLOCK)));
	public static final Block SMOOTH_RED_QUARTZ_BLOCK = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_RED,
			new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ)
					.mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block SMOOTH_RED_QUARTZ_STAIRS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_RED + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(SMOOTH_RED_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOOTH_RED_QUARTZ_BLOCK)));
	public static final Block SMOOTH_RED_QUARTZ_SLAB = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_RED + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_RED_QUARTZ_BLOCK)));

	public static final Block ELVEN_QUARTZ_BLOCK = make(LibBlockNames.QUARTZ_ELF,
			new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
					.mapColor(MapColor.COLOR_LIGHT_GREEN)));
	public static final Block ELVEN_QUARTZ_STAIRS = make(LibBlockNames.QUARTZ_ELF + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(ELVEN_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(ELVEN_QUARTZ_BLOCK)));
	public static final Block ELVEN_QUARTZ_SLAB = make(LibBlockNames.QUARTZ_ELF + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ELVEN_QUARTZ_BLOCK)));
	public static final Block CHISELED_ELVEN_QUARTZ_BLOCK = make("chiseled_" + LibBlockNames.QUARTZ_ELF,
			new Block(BlockBehaviour.Properties.ofFullCopy(ELVEN_QUARTZ_BLOCK)));
	public static final Block ELVEN_QUARTZ_BRICKS = make(LibBlockNames.QUARTZ_ELF + "_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(ELVEN_QUARTZ_BLOCK)));
	public static final Block ELVEN_QUARTZ_PILLAR = make(LibBlockNames.QUARTZ_ELF + "_pillar",
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(ELVEN_QUARTZ_BLOCK)));
	public static final Block SMOOTH_ELVEN_QUARTZ_BLOCK = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_ELF,
			new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ)
					.mapColor(MapColor.COLOR_LIGHT_GREEN)));
	public static final Block SMOOTH_ELVEN_QUARTZ_STAIRS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_ELF + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(SMOOTH_ELVEN_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOOTH_ELVEN_QUARTZ_BLOCK)));
	public static final Block SMOOTH_ELVEN_QUARTZ_SLAB = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_ELF + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_ELVEN_QUARTZ_BLOCK)));

	public static final Block SUNNY_QUART_BLOCK = make(LibBlockNames.QUARTZ_SUNNY,
			new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
					.mapColor(MapColor.COLOR_YELLOW)));
	public static final Block SUNNY_QUARTZ_STAIRS = make(LibBlockNames.QUARTZ_SUNNY + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(SUNNY_QUART_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SUNNY_QUART_BLOCK)));
	public static final Block SUNNY_QUARTZ_SLAB = make(LibBlockNames.QUARTZ_SUNNY + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SUNNY_QUART_BLOCK)));
	public static final Block CHISELED_SUNNY_QUARTZ_BLOCK = make("chiseled_" + LibBlockNames.QUARTZ_SUNNY,
			new Block(BlockBehaviour.Properties.ofFullCopy(SUNNY_QUART_BLOCK)));
	public static final Block SUNNY_QUARTZ_BRICKS = make(LibBlockNames.QUARTZ_SUNNY + "_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(SUNNY_QUART_BLOCK)));
	public static final Block SUNNY_QUARTZ_PILLAR = make(LibBlockNames.QUARTZ_SUNNY + "_pillar",
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(SUNNY_QUART_BLOCK)));
	public static final Block SMOOTH_SUNNY_QUARTZ_BLOCK = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_SUNNY,
			new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ)
					.mapColor(MapColor.COLOR_YELLOW)));
	public static final Block SMOOTH_SUNNY_QUARTZ_STAIRS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_SUNNY + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(SMOOTH_SUNNY_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOOTH_SUNNY_QUARTZ_BLOCK)));
	public static final Block SMOOTH_SUNNY_QUARTZ_SLAB = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.QUARTZ_SUNNY + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_SUNNY_QUARTZ_BLOCK)));

	// metamorphic biome blocks
	public static final Block FUCHSITE = make(LibBlockNames.METAMORPHIC_PREFIX + "forest_stone",
			new Block(BlockBehaviour.Properties.of()
					.strength(1.5F, 10)
					.sound(SoundType.TUFF)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.requiresCorrectToolForDrops()
					.mapColor(MapColor.WARPED_NYLIUM)));
	public static final Block FUCHSITE_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "forest_stone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(FUCHSITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FUCHSITE)));
	public static final Block FUCHSITE_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "forest_stone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)));
	public static final Block FUCHSITE_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "forest_stone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)));
	public static final Block FUCHSITE_BUTTON = make(LibBlockNames.METAMORPHIC_PREFIX + "forest_stone" + LibBlockNames.BUTTON_SUFFIX,
			new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_FOREST_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.WARPED_NYLIUM)));
	public static final Block FUCHSITE_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_PREFIX + "forest_stone" + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_FOREST_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.WARPED_NYLIUM)));
	public static final Block COBBLED_FUCHSITE = make(LibBlockNames.METAMORPHIC_PREFIX + "forest_cobblestone",
			new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)));
	public static final Block COBBLED_FUCHSITE_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "forest_cobblestone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(FUCHSITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FUCHSITE)));
	public static final Block COBBLED_FUCHSITE_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "forest_cobblestone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)));
	public static final Block COBBLED_FUCHSITE_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "forest_cobblestone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)));
	public static final Block FUCHSITE_BRICKS = make(LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)));
	public static final Block FUCHSITE_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(FUCHSITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FUCHSITE)));
	public static final Block FUCHSITE_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)));
	public static final Block FUCHSITE_BRICK_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)));
	public static final Block CHISELED_FUCHSITE_BRICKS = make("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "forest_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)));

	public static final Block TALC = make(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone",
			new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)
					.sound(SoundType.CALCITE)
					.mapColor(MapColor.QUARTZ)));
	public static final Block TALC_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(TALC.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(TALC)));
	public static final Block TALC_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(TALC)));
	public static final Block TALC_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(TALC)));
	public static final Block TALC_BUTTON = make(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone" + LibBlockNames.BUTTON_SUFFIX,
			new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_PLAINS_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.QUARTZ)));
	public static final Block TALC_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_PREFIX + "plains_stone" + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_PLAINS_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.QUARTZ)));
	public static final Block COBBLED_TALC = make(LibBlockNames.METAMORPHIC_PREFIX + "plains_cobblestone",
			new Block(BlockBehaviour.Properties.ofFullCopy(TALC)));
	public static final Block COBBLED_TALC_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "plains_cobblestone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(TALC.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(TALC)));
	public static final Block COBBLED_TALC_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "plains_cobblestone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(TALC)));
	public static final Block COBBLED_TALC_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "plains_cobblestone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(TALC)));
	public static final Block TALC_BRICKS = make(LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks",
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(TALC)));
	public static final Block TALC_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(TALC.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(TALC)));
	public static final Block TALC_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(TALC)));
	public static final Block TALC_BRICK_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(TALC)));
	public static final Block CHISELED_TALC_BRICKS = make("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "plains_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(TALC)));

	public static final Block GNEISS = make(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone",
			new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)
					.sound(SoundType.DEEPSLATE_TILES)
					.mapColor(MapColor.GLOW_LICHEN)));
	public static final Block GNEISS_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(GNEISS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(GNEISS)));
	public static final Block GNEISS_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(GNEISS)));
	public static final Block GNEISS_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(GNEISS)));
	public static final Block GNEISS_BUTTON = make(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone" + LibBlockNames.BUTTON_SUFFIX,
			new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_MOUNTAIN_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.GLOW_LICHEN)));
	public static final Block GNEISS_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_PREFIX + "mountain_stone" + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_MOUNTAIN_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.GLOW_LICHEN)));
	public static final Block COBBLED_GNEISS = make(LibBlockNames.METAMORPHIC_PREFIX + "mountain_cobblestone",
			new Block(BlockBehaviour.Properties.ofFullCopy(GNEISS)));
	public static final Block COBBLED_GNEISS_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "mountain_cobblestone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(GNEISS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(GNEISS)));
	public static final Block COBBLED_GNEISS_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "mountain_cobblestone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(GNEISS)));
	public static final Block COBBLED_GNEISS_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "mountain_cobblestone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(GNEISS)));
	public static final Block GNEISS_BRICKS = make(LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(GNEISS)));
	public static final Block GNEISS_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(GNEISS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(GNEISS)));
	public static final Block GNEISS_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(GNEISS)));
	public static final Block GNEISS_BRICK_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(GNEISS)));
	public static final Block CHISELED_GNEISS_BRICKS = make("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "mountain_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(GNEISS)));

	public static final Block MYCELITE = make(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone",
			new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)
					.sound(SoundType.DEEPSLATE_BRICKS)
					.mapColor(MapColor.TERRACOTTA_PURPLE)));
	public static final Block MYCELITE_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(MYCELITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(MYCELITE)));
	public static final Block MYCELITE_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(MYCELITE)));
	public static final Block MYCELITE_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(MYCELITE)));
	public static final Block MYCELITE_BUTTON = make(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone" + LibBlockNames.BUTTON_SUFFIX,
			new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_FUNGAL_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.TERRACOTTA_PURPLE)));
	public static final Block MYCELITE_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_PREFIX + "fungal_stone" + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_FUNGAL_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.TERRACOTTA_PURPLE)));
	public static final Block COBBLED_MYCELITE = make(LibBlockNames.METAMORPHIC_PREFIX + "fungal_cobblestone",
			new Block(BlockBehaviour.Properties.ofFullCopy(MYCELITE)));
	public static final Block COBBLED_MYCELITE_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "fungal_cobblestone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(MYCELITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(MYCELITE)));
	public static final Block COBBLED_MYCELITE_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "fungal_cobblestone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(MYCELITE)));
	public static final Block COBBLED_MYCELITE_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "fungal_cobblestone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(MYCELITE)));
	public static final Block MYCELITE_BRICKS = make(LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(MYCELITE)));
	public static final Block MYCELITE_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(MYCELITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(MYCELITE)));
	public static final Block MYCELITE_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(MYCELITE)));
	public static final Block MYCELITE_BRICK_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(MYCELITE)));
	public static final Block CHISELED_MYCELITE_BRICKS = make("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "fungal_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(MYCELITE)));

	public static final Block CATACLASITE = make(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone",
			new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)
					.sound(SoundType.DEEPSLATE_TILES)
					.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
	public static final Block CATACLASITE_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(CATACLASITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CATACLASITE)));
	public static final Block CATACLASITE_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE)));
	public static final Block CATACLASITE_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE)));
	public static final Block CATACLASITE_BUTTON = make(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone" + LibBlockNames.BUTTON_SUFFIX,
			new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_SWAMP_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
	public static final Block CATACLASITE_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_PREFIX + "swamp_stone" + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_SWAMP_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
	public static final Block COBBLED_CATACLASITE = make(LibBlockNames.METAMORPHIC_PREFIX + "swamp_cobblestone",
			new Block(BlockBehaviour.Properties.ofFullCopy(CATACLASITE)));
	public static final Block COBBLED_CATACLASITE_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "swamp_cobblestone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(CATACLASITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CATACLASITE)));
	public static final Block COBBLED_CATACLASITE_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "swamp_cobblestone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE)));
	public static final Block COBBLED_CATACLASITE_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "swamp_cobblestone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE)));
	public static final Block CATACLASITE_BRICKS = make(LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks",
			new BotaniaDirectionalBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE)));
	public static final Block CATACLASITE_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(CATACLASITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CATACLASITE)));
	public static final Block CATACLASITE_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE)));
	public static final Block CATACLASITE_BRICK_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE)));
	public static final Block CHISELED_CATACLASITE_BRICKS = make("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "swamp_bricks",
			new BotaniaDirectionalBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE)));

	public static final Block SOLITE = make(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone",
			new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)
					.sound(SoundType.DEEPSLATE)
					.mapColor(MapColor.DIRT)));
	public static final Block SOLITE_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(SOLITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SOLITE)));
	public static final Block SOLITE_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SOLITE)));
	public static final Block SOLITE_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(SOLITE)));
	public static final Block SOLITE_BUTTON = make(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone" + LibBlockNames.BUTTON_SUFFIX,
			new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_DESERT_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.DIRT)));
	public static final Block SOLITE_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_PREFIX + "desert_stone" + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_DESERT_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.DIRT)));
	public static final Block COBBLED_SOLITE = make(LibBlockNames.METAMORPHIC_PREFIX + "desert_cobblestone",
			new Block(BlockBehaviour.Properties.ofFullCopy(SOLITE)));
	public static final Block COBBLED_SOLITE_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "desert_cobblestone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(SOLITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SOLITE)));
	public static final Block COBBLED_SOLITE_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "desert_cobblestone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SOLITE)));
	public static final Block COBBLED_SOLITE_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "desert_cobblestone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(SOLITE)));
	public static final Block SOLITE_BRICKS = make(LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(SOLITE)));
	public static final Block SOLITE_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(SOLITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SOLITE)));
	public static final Block SOLITE_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SOLITE)));
	public static final Block SOLITE_BRICK_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(SOLITE)));
	public static final Block CHISELED_SOLITE_BRICKS = make("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "desert_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(SOLITE)));

	public static final Block LUNITE = make(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone",
			new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)
					.sound(SoundType.DEEPSLATE)
					.mapColor(MapColor.TERRACOTTA_BLUE)));
	public static final Block LUNITE_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(LUNITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(LUNITE)));
	public static final Block LUNITE_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LUNITE)));
	public static final Block LUNITE_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(LUNITE)));
	public static final Block LUNITE_BUTTON = make(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone" + LibBlockNames.BUTTON_SUFFIX,
			new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_TAIGA_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.TERRACOTTA_BLUE)));
	public static final Block LUNITE_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_PREFIX + "taiga_stone" + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_TAIGA_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.TERRACOTTA_BLUE)));
	public static final Block COBBLED_LUNITE = make(LibBlockNames.METAMORPHIC_PREFIX + "taiga_cobblestone",
			new Block(BlockBehaviour.Properties.ofFullCopy(LUNITE)));
	public static final Block COBBLED_LUNITE_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "taiga_cobblestone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(LUNITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(LUNITE)));
	public static final Block COBBLED_LUNITE_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "taiga_cobblestone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LUNITE)));
	public static final Block COBBLED_LUNITE_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "taiga_cobblestone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(LUNITE)));
	public static final Block LUNITE_BRICKS = make(LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(LUNITE)));
	public static final Block LUNITE_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(LUNITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(LUNITE)));
	public static final Block LUNITE_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LUNITE)));
	public static final Block LUNITE_BRICK_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(LUNITE)));
	public static final Block CHISELED_LUNITE_BRICKS = make("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "taiga_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(LUNITE)));

	public static final Block ROSY_TALC = make(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone",
			new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)
					.sound(SoundType.CALCITE)
					.mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block ROSY_TALC_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(ROSY_TALC.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ROSY_TALC)));
	public static final Block ROSY_TALC_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC)));
	public static final Block ROSY_TALC_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC)));
	public static final Block ROSY_TALC_BUTTON = make(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone" + LibBlockNames.BUTTON_SUFFIX,
			new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_MESA_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block ROSY_TALC_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_PREFIX + "mesa_stone" + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_MESA_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block COBBLED_ROSY_TALC = make(LibBlockNames.METAMORPHIC_PREFIX + "mesa_cobblestone",
			new Block(BlockBehaviour.Properties.ofFullCopy(
					ROSY_TALC)));
	public static final Block COBBLED_ROSY_TALC_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "mesa_cobblestone" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(ROSY_TALC.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ROSY_TALC)));
	public static final Block COBBLED_ROSY_TALC_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "mesa_cobblestone" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC)));
	public static final Block COBBLED_ROSY_TALC_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "mesa_cobblestone" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC)));
	public static final Block ROSY_TALC_BRICKS = make(LibBlockNames.METAMORPHIC_PREFIX + "mesa_bricks",
			new Block(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC)));
	public static final Block ROSY_TALC_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_PREFIX + "mesa_bricks" + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(ROSY_TALC.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ROSY_TALC)));
	public static final Block ROSY_TALC_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_PREFIX + "mesa_bricks" + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC)));
	public static final Block ROSY_TALC_BRICK_WALL = make(LibBlockNames.METAMORPHIC_PREFIX + "mesa_bricks" + LibBlockNames.WALL_SUFFIX,
			new WallBlock(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC)));
	public static final Block CHISELED_ROSY_TALC_BRICKS = make("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + "mesa_bricks",
			new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC)));

	// pavements
	public static final Block WHITE_PORTUGUESE_PAVEMENT = make("white" + LibBlockNames.PAVEMENT_SUFFIX,
			new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)
					.mapColor(MapColor.TERRACOTTA_WHITE)));
	public static final Block WHITE_PORTUGUESE_PAVEMENT_STAIRS = make("white" + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(WHITE_PORTUGUESE_PAVEMENT.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(WHITE_PORTUGUESE_PAVEMENT)));
	public static final Block WHITE_PORTUGUESE_PAVEMENT_SLAB = make("white" + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(WHITE_PORTUGUESE_PAVEMENT)));

	public static final Block BLACK_PORTUGUESE_PAVEMENT = make("black" + LibBlockNames.PAVEMENT_SUFFIX,
			new Block(BlockBehaviour.Properties.ofFullCopy(WHITE_PORTUGUESE_PAVEMENT)
					.mapColor(MapColor.COLOR_GRAY)));
	public static final Block BLACK_PORTUGUESE_PAVEMENT_STAIRS = make("black" + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(BLACK_PORTUGUESE_PAVEMENT.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(BLACK_PORTUGUESE_PAVEMENT)));
	public static final Block BLACK_PORTUGUESE_PAVEMENT_SLAB = make("black" + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BLACK_PORTUGUESE_PAVEMENT)));

	public static final Block BLUE_PORTUGUESE_PAVEMENT = make("blue" + LibBlockNames.PAVEMENT_SUFFIX,
			new Block(BlockBehaviour.Properties.ofFullCopy(WHITE_PORTUGUESE_PAVEMENT)
					.mapColor(MapColor.COLOR_BLUE)));
	public static final Block BLUE_PORTUGUESE_PAVEMENT_STAIRS = make("blue" + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(BLUE_PORTUGUESE_PAVEMENT.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(BLUE_PORTUGUESE_PAVEMENT)));
	public static final Block BLUE_PORTUGUESE_PAVEMENT_SLAB = make("blue" + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BLUE_PORTUGUESE_PAVEMENT)));

	public static final Block YELLOW_PORTUGUESE_PAVEMENT = make("yellow" + LibBlockNames.PAVEMENT_SUFFIX,
			new Block(BlockBehaviour.Properties.ofFullCopy(WHITE_PORTUGUESE_PAVEMENT)
					.mapColor(MapColor.TERRACOTTA_YELLOW)));
	public static final Block YELLOW_PORTUGUESE_PAVEMENT_STAIRS = make("yellow" + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(YELLOW_PORTUGUESE_PAVEMENT.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(YELLOW_PORTUGUESE_PAVEMENT)));
	public static final Block YELLOW_PORTUGUESE_PAVEMENT_SLAB = make("yellow" + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(YELLOW_PORTUGUESE_PAVEMENT)));

	public static final Block RED_PORTUGUESE_PAVEMENT = make("red" + LibBlockNames.PAVEMENT_SUFFIX,
			new Block(BlockBehaviour.Properties.ofFullCopy(WHITE_PORTUGUESE_PAVEMENT)
					.mapColor(MapColor.TERRACOTTA_RED)));
	public static final Block RED_PORTUGUESE_PAVEMENT_STAIRS = make("red" + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(RED_PORTUGUESE_PAVEMENT.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(RED_PORTUGUESE_PAVEMENT)));
	public static final Block RED_PORTUGUESE_PAVEMENT_SLAB = make("red" + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(RED_PORTUGUESE_PAVEMENT)));

	public static final Block GREEN_PORTUGUESE_PAVEMENT = make("green" + LibBlockNames.PAVEMENT_SUFFIX,
			new Block(BlockBehaviour.Properties.ofFullCopy(WHITE_PORTUGUESE_PAVEMENT)
					.mapColor(MapColor.TERRACOTTA_GREEN)));
	public static final Block GREEN_PORTUGUESE_PAVEMENT_STAIRS = make("green" + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIR_SUFFIX,
			new StairBlock(GREEN_PORTUGUESE_PAVEMENT.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(GREEN_PORTUGUESE_PAVEMENT)));
	public static final Block GREEN_PORTUGUESE_PAVEMENT_SLAB = make("green" + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX,
			new SlabBlock(BlockBehaviour.Properties.ofFullCopy(GREEN_PORTUGUESE_PAVEMENT)));

	public static final Block MANAGLASS_PANE = make(LibBlockNames.MANA_GLASS + "_pane",
			new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(MANAGLASS)));
	public static final Block ALFGLASS_PANE = make(LibBlockNames.ELF_GLASS + "_pane",
			new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(ALFGLASS)));
	public static final Block BIFROST_PANE = make(LibBlockNames.BIFROST + "_pane",
			new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(BIFROST_BLOCK)));

	static FlowerPotBlock flowerPot(Block block, int lightLevel) {
		BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
				.instabreak()
				.noOcclusion()
				.pushReaction(PushReaction.DESTROY);
		return new FlowerPotBlock(block, lightLevel > 0
				? properties.lightLevel(blockState -> lightLevel)
				: properties);
	}

	private static String floating(String orig) {
		return "floating_" + orig;
	}

	private static String potted(String orig) {
		return "potted_" + orig;
	}

	private static String petite(String orig) {
		return orig + "_petite";
	}

	public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {
		for (var e : ALL.entrySet()) {
			r.accept(e.getValue(), botaniaRL(e.getKey()));
		}
	}

	private static <T> ToIntFunction<T> constInt(int value) {
		return ignored -> value;
	}

	public static void registerItemBlocks(BiConsumer<Item, ResourceLocation> r) {
		Item.Properties props = BotaniaItems.defaultBuilder();
		Item.Properties uncommonProps = BotaniaItems.defaultBuilder().rarity(Rarity.UNCOMMON);
		Item.Properties rareProps = BotaniaItems.defaultBuilder().rarity(Rarity.RARE);
		Item.Properties epicProps = BotaniaItems.defaultBuilder().rarity(Rarity.EPIC);
		Stream.<Function<DyeColor, Block>>of(
				BotaniaBlocks::getMysticalFlower,
				BotaniaBlocks::getGlimmeringFlower,
				BotaniaBlocks::getFloatingFlower,
				BotaniaBlocks::getPetalBlock,
				BotaniaBlocks::getShimmeringMushroom,
				BotaniaBlocks::getTallMysticalFlower
		).forEach(
				blockGetter -> ColorHelper.supportedColors().map(blockGetter).forEach(
						block -> r.accept(new ColoredBlockItem(block, ((Colored) block).getColor(), props), BuiltInRegistries.BLOCK.getKey(block))
				)
		);

		r.accept(new SpecialFlowerBlockItem(PURE_DAISY, props), BuiltInRegistries.BLOCK.getKey(PURE_DAISY));
		r.accept(new SpecialFlowerBlockItem(FLOATING_PURE_DAISY, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_PURE_DAISY));

		r.accept(new SpecialFlowerBlockItem(MANASTAR, props), BuiltInRegistries.BLOCK.getKey(MANASTAR));
		r.accept(new SpecialFlowerBlockItem(FLOATING_MANASTAR, props), BuiltInRegistries.BLOCK.getKey(FLOATING_MANASTAR));

		r.accept(new DecayableSpecialFlowerBlockItem(HYDROANGEAS, HydroangeasBlockEntity.DECAY_TIME, props), BuiltInRegistries.BLOCK.getKey(
				HYDROANGEAS));
		r.accept(new DecayableSpecialFlowerBlockItem(FLOATING_HYDROANGEAS, HydroangeasBlockEntity.DECAY_TIME, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_HYDROANGEAS));

		r.accept(new SpecialFlowerBlockItem(ENDOFLAME, props), BuiltInRegistries.BLOCK.getKey(ENDOFLAME));
		r.accept(new SpecialFlowerBlockItem(FLOATING_ENDOFLAME, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_ENDOFLAME));

		r.accept(new SpecialFlowerBlockItem(THERMALILY, props), BuiltInRegistries.BLOCK.getKey(THERMALILY));
		r.accept(new SpecialFlowerBlockItem(FLOATING_THERMALILY, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_THERMALILY));

		r.accept(new SpecialFlowerBlockItem(ROSA_ARCANA, props), BuiltInRegistries.BLOCK.getKey(ROSA_ARCANA));
		r.accept(new SpecialFlowerBlockItem(FLOATING_ROSA_ARCANA, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_ROSA_ARCANA));

		r.accept(new SpecialFlowerBlockItem(MUNCHDEW, props), BuiltInRegistries.BLOCK.getKey(MUNCHDEW));
		r.accept(new SpecialFlowerBlockItem(FLOATING_MUNCHDEW, props), BuiltInRegistries.BLOCK.getKey(FLOATING_MUNCHDEW));

		r.accept(new SpecialFlowerBlockItem(ENTROPINNYUM, props), BuiltInRegistries.BLOCK.getKey(ENTROPINNYUM));
		r.accept(new SpecialFlowerBlockItem(FLOATING_ENTROPINNYUM, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_ENTROPINNYUM));

		r.accept(new SpecialFlowerBlockItem(KEKIMURUS, props), BuiltInRegistries.BLOCK.getKey(KEKIMURUS));
		r.accept(new SpecialFlowerBlockItem(FLOATING_KEKIMURUS, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_KEKIMURUS));

		r.accept(new SpecialFlowerBlockItem(GOURMARYLLIS, props), BuiltInRegistries.BLOCK.getKey(GOURMARYLLIS));
		r.accept(new SpecialFlowerBlockItem(FLOATING_GOURMARYLLIS, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_GOURMARYLLIS));

		r.accept(new SpecialFlowerBlockItem(NARSLIMMUS, props), BuiltInRegistries.BLOCK.getKey(NARSLIMMUS));
		r.accept(new SpecialFlowerBlockItem(FLOATING_NARSLIMMUS, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_NARSLIMMUS));

		r.accept(new SpecialFlowerBlockItem(SPECTROLUS, props), BuiltInRegistries.BLOCK.getKey(SPECTROLUS));
		r.accept(new SpecialFlowerBlockItem(FLOATING_SPECTROLUS, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_SPECTROLUS));

		r.accept(new SpecialFlowerBlockItem(DANDELIFEON, rareProps), BuiltInRegistries.BLOCK.getKey(DANDELIFEON));
		r.accept(new SpecialFlowerBlockItem(FLOATING_DANDELIFEON, rareProps), BuiltInRegistries.BLOCK.getKey(
				FLOATING_DANDELIFEON));

		r.accept(new SpecialFlowerBlockItem(RAFFLOWSIA, props), BuiltInRegistries.BLOCK.getKey(RAFFLOWSIA));
		r.accept(new SpecialFlowerBlockItem(FLOATING_RAFFLOWSIA, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_RAFFLOWSIA));

		r.accept(new SpecialFlowerBlockItem(SHULK_ME_NOT, rareProps), BuiltInRegistries.BLOCK.getKey(SHULK_ME_NOT));
		r.accept(new SpecialFlowerBlockItem(FLOATING_SHULK_ME_NOT, rareProps), BuiltInRegistries.BLOCK.getKey(
				FLOATING_SHULK_ME_NOT));

		r.accept(new SpecialFlowerBlockItem(BELLETHORNE, props), BuiltInRegistries.BLOCK.getKey(BELLETHORNE));
		r.accept(new SpecialFlowerBlockItem(BELLETHORNE_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				BELLETHORNE_PETITE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_BELLETHORNE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_BELLETHORNE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_BELLETHORNE_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_BELLETHORNE_PETITE));

		r.accept(new SpecialFlowerBlockItem(BERGAMUTE, props), BuiltInRegistries.BLOCK.getKey(BERGAMUTE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_BERGAMUTE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_BERGAMUTE));

		r.accept(new SpecialFlowerBlockItem(DREADTHORNE, props), BuiltInRegistries.BLOCK.getKey(DREADTHORNE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_DREADTHORNE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_DREADTHORNE));

		r.accept(new SpecialFlowerBlockItem(HEISEI_DREAM, props), BuiltInRegistries.BLOCK.getKey(HEISEI_DREAM));
		r.accept(new SpecialFlowerBlockItem(FLOATING_HEISEI_DREAM, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_HEISEI_DREAM));

		r.accept(new SpecialFlowerBlockItem(TIGERSEYE, props), BuiltInRegistries.BLOCK.getKey(TIGERSEYE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_TIGERSEYE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_TIGERSEYE));

		r.accept(new SpecialFlowerBlockItem(JADED_AMARANTHUS, props), BuiltInRegistries.BLOCK.getKey(JADED_AMARANTHUS));
		r.accept(new SpecialFlowerBlockItem(FLOATING_JADED_AMARANTHUS, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_JADED_AMARANTHUS));

		r.accept(new SpecialFlowerBlockItem(ORECHID, props), BuiltInRegistries.BLOCK.getKey(ORECHID));
		r.accept(new SpecialFlowerBlockItem(FLOATING_ORECHID, props), BuiltInRegistries.BLOCK.getKey(FLOATING_ORECHID));

		r.accept(new SpecialFlowerBlockItem(FALLEN_KANADE, props), BuiltInRegistries.BLOCK.getKey(FALLEN_KANADE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_FALLEN_KANADE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_FALLEN_KANADE));

		r.accept(new SpecialFlowerBlockItem(EXOFLAME, props), BuiltInRegistries.BLOCK.getKey(EXOFLAME));
		r.accept(new SpecialFlowerBlockItem(FLOATING_EXOFLAME, props), BuiltInRegistries.BLOCK.getKey(FLOATING_EXOFLAME));

		r.accept(new SpecialFlowerBlockItem(AGRICARNATION, props), BuiltInRegistries.BLOCK.getKey(AGRICARNATION));
		r.accept(new SpecialFlowerBlockItem(AGRICARNATION_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				AGRICARNATION_PETITE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_AGRICARNATION, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_AGRICARNATION));
		r.accept(new SpecialFlowerBlockItem(FLOATING_AGRICARNATION_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_AGRICARNATION_PETITE));

		r.accept(new SpecialFlowerBlockItem(HOPPERHOCK, props), BuiltInRegistries.BLOCK.getKey(HOPPERHOCK));
		r.accept(new SpecialFlowerBlockItem(HOPPERHOCK_PETITE, props), BuiltInRegistries.BLOCK.getKey(HOPPERHOCK_PETITE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_HOPPERHOCK, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_HOPPERHOCK));
		r.accept(new SpecialFlowerBlockItem(FLOATING_HOPPERHOCK_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_HOPPERHOCK_PETITE));

		r.accept(new SpecialFlowerBlockItem(TANGLEBERRIE, props), BuiltInRegistries.BLOCK.getKey(TANGLEBERRIE));
		r.accept(new SpecialFlowerBlockItem(TANGLEBERRIE_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				TANGLEBERRIE_PETITE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_TANGLEBERRIE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_TANGLEBERRIE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_TANGLEBERRIE_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_TANGLEBERRIE_PETITE));

		r.accept(new SpecialFlowerBlockItem(JIYUULIA, props), BuiltInRegistries.BLOCK.getKey(JIYUULIA));
		r.accept(new SpecialFlowerBlockItem(JIYUULIA_PETITE, props), BuiltInRegistries.BLOCK.getKey(JIYUULIA_PETITE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_JIYUULIA, props), BuiltInRegistries.BLOCK.getKey(FLOATING_JIYUULIA));
		r.accept(new SpecialFlowerBlockItem(FLOATING_JIYUULIA_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_JIYUULIA_PETITE));

		r.accept(new SpecialFlowerBlockItem(RANNUNCARPUS, props), BuiltInRegistries.BLOCK.getKey(RANNUNCARPUS));
		r.accept(new SpecialFlowerBlockItem(RANNUNCARPUS_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				RANNUNCARPUS_PETITE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_RANNUNCARPUS, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_RANNUNCARPUS));
		r.accept(new SpecialFlowerBlockItem(FLOATING_RANNUNCARPUS_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_RANNUNCARPUS_PETITE));

		r.accept(new SpecialFlowerBlockItem(HYACIDUS, props), BuiltInRegistries.BLOCK.getKey(HYACIDUS));
		r.accept(new SpecialFlowerBlockItem(FLOATING_HYACIDUS, props), BuiltInRegistries.BLOCK.getKey(FLOATING_HYACIDUS));

		r.accept(new SpecialFlowerBlockItem(POLLIDISIAC, props), BuiltInRegistries.BLOCK.getKey(POLLIDISIAC));
		r.accept(new SpecialFlowerBlockItem(FLOATING_POLLIDISIAC, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_POLLIDISIAC));

		r.accept(new SpecialFlowerBlockItem(CLAYCONIA, props), BuiltInRegistries.BLOCK.getKey(CLAYCONIA));
		r.accept(new SpecialFlowerBlockItem(CLAYCONIA_PETITE, props), BuiltInRegistries.BLOCK.getKey(CLAYCONIA_PETITE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_CLAYCONIA, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_CLAYCONIA));
		r.accept(new SpecialFlowerBlockItem(FLOATING_CLAYCONIA_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_CLAYCONIA_PETITE));

		r.accept(new SpecialFlowerBlockItem(LOONIUM, props), BuiltInRegistries.BLOCK.getKey(LOONIUM));
		r.accept(new SpecialFlowerBlockItem(FLOATING_LOONIUM, props), BuiltInRegistries.BLOCK.getKey(FLOATING_LOONIUM));

		r.accept(new SpecialFlowerBlockItem(DAFFOMILL, props), BuiltInRegistries.BLOCK.getKey(DAFFOMILL));
		r.accept(new SpecialFlowerBlockItem(FLOATING_DAFFOMILL, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_DAFFOMILL));

		r.accept(new SpecialFlowerBlockItem(VINCULOTUS, props), BuiltInRegistries.BLOCK.getKey(VINCULOTUS));
		r.accept(new SpecialFlowerBlockItem(FLOATING_VINCULOTUS, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_VINCULOTUS));

		r.accept(new SpecialFlowerBlockItem(SPECTRANTHEMUM, props), BuiltInRegistries.BLOCK.getKey(SPECTRANTHEMUM));
		r.accept(new SpecialFlowerBlockItem(FLOATING_SPECTRANTHEMUM, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_SPECTRANTHEMUM));

		r.accept(new SpecialFlowerBlockItem(MEDUMONE, props), BuiltInRegistries.BLOCK.getKey(MEDUMONE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_MEDUMONE, props), BuiltInRegistries.BLOCK.getKey(FLOATING_MEDUMONE));

		r.accept(new SpecialFlowerBlockItem(MARIMORPHOSIS, props), BuiltInRegistries.BLOCK.getKey(MARIMORPHOSIS));
		r.accept(new SpecialFlowerBlockItem(MARIMORPHOSIS_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				MARIMORPHOSIS_PETITE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_MARIMORPHOSIS, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_MARIMORPHOSIS));
		r.accept(new SpecialFlowerBlockItem(FLOATING_MARIMORPHOSIS_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_MARIMORPHOSIS_PETITE));

		r.accept(new SpecialFlowerBlockItem(BUBBELL, props), BuiltInRegistries.BLOCK.getKey(BUBBELL));
		r.accept(new SpecialFlowerBlockItem(BUBBELL_PETITE, props), BuiltInRegistries.BLOCK.getKey(BUBBELL_PETITE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_BUBBELL, props), BuiltInRegistries.BLOCK.getKey(FLOATING_BUBBELL));
		r.accept(new SpecialFlowerBlockItem(FLOATING_BUBBELL_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_BUBBELL_PETITE));

		r.accept(new SpecialFlowerBlockItem(SOLEGNOLIA, props), BuiltInRegistries.BLOCK.getKey(SOLEGNOLIA));
		r.accept(new SpecialFlowerBlockItem(SOLEGNOLIA_PETITE, props), BuiltInRegistries.BLOCK.getKey(SOLEGNOLIA_PETITE));
		r.accept(new SpecialFlowerBlockItem(FLOATING_SOLEGNOLIA, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_SOLEGNOLIA));
		r.accept(new SpecialFlowerBlockItem(FLOATING_SOLEGNOLIA_PETITE, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_SOLEGNOLIA_PETITE));

		r.accept(new SpecialFlowerBlockItem(ORECHID_IGNEM, props), BuiltInRegistries.BLOCK.getKey(ORECHID_IGNEM));
		r.accept(new SpecialFlowerBlockItem(FLOATING_ORECHID_IGNEM, props), BuiltInRegistries.BLOCK.getKey(
				FLOATING_ORECHID_IGNEM));

		r.accept(new SpecialFlowerBlockItem(LABELLIA, props), BuiltInRegistries.BLOCK.getKey(LABELLIA));
		r.accept(new SpecialFlowerBlockItem(FLOATING_LABELLIA, props), BuiltInRegistries.BLOCK.getKey(FLOATING_LABELLIA));

		r.accept(new BlockItem(PETAL_APOTHECARY, props), BuiltInRegistries.BLOCK.getKey(PETAL_APOTHECARY));
		r.accept(new BlockItem(PETAL_APOTHECARY_DEEPSLATE, props), BuiltInRegistries.BLOCK.getKey(
				PETAL_APOTHECARY_DEEPSLATE));
		r.accept(new BlockItem(PETAL_APOTHECARY_LIVINGROCK, props), BuiltInRegistries.BLOCK.getKey(
				PETAL_APOTHECARY_LIVINGROCK));
		r.accept(new BlockItem(PETAL_APOTHECARY_BLACKSTONE, props), BuiltInRegistries.BLOCK.getKey(
				PETAL_APOTHECARY_BLACKSTONE));
		r.accept(new BlockItem(PETAL_APOTHECARY_NETHER_BRICKS, props), BuiltInRegistries.BLOCK.getKey(
				PETAL_APOTHECARY_NETHER_BRICKS));
		r.accept(new BlockItem(PETAL_APOTHECARY_RED_NETHER_BRICKS, props), BuiltInRegistries.BLOCK.getKey(
				PETAL_APOTHECARY_RED_NETHER_BRICKS));
		r.accept(new BlockItem(PETAL_APOTHECARY_MOSSY, props), BuiltInRegistries.BLOCK.getKey(PETAL_APOTHECARY_MOSSY));
		r.accept(new BlockItem(PETAL_APOTHECARY_FUCHSITE, props), BuiltInRegistries.BLOCK.getKey(
				PETAL_APOTHECARY_FUCHSITE));
		r.accept(new BlockItem(PETAL_APOTHECARY_TALC, props), BuiltInRegistries.BLOCK.getKey(PETAL_APOTHECARY_TALC));
		r.accept(new BlockItem(PETAL_APOTHECARY_GNEISS, props), BuiltInRegistries.BLOCK.getKey(PETAL_APOTHECARY_GNEISS));
		r.accept(new BlockItem(PETAL_APOTHECARY_MYCELITE, props), BuiltInRegistries.BLOCK.getKey(
				PETAL_APOTHECARY_MYCELITE));
		r.accept(new BlockItem(PETAL_APOTHECARY_CATACLASITE, props), BuiltInRegistries.BLOCK.getKey(
				PETAL_APOTHECARY_CATACLASITE));
		r.accept(new BlockItem(PETAL_APOTHECARY_SOLITE, props), BuiltInRegistries.BLOCK.getKey(PETAL_APOTHECARY_SOLITE));
		r.accept(new BlockItem(PETAL_APOTHECARY_LUNITE, props), BuiltInRegistries.BLOCK.getKey(PETAL_APOTHECARY_LUNITE));
		r.accept(new BlockItem(PETAL_APOTHECARY_ROSY_TALC, props), BuiltInRegistries.BLOCK.getKey(
				PETAL_APOTHECARY_ROSY_TALC));

		r.accept(new BlockItem(LIVINGROCK, props), BuiltInRegistries.BLOCK.getKey(LIVINGROCK));
		r.accept(new BlockItem(LIVINGROCK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(LIVINGROCK_STAIRS));
		r.accept(new BlockItem(LIVINGROCK_SLAB, props), BuiltInRegistries.BLOCK.getKey(LIVINGROCK_SLAB));
		r.accept(new BlockItem(LIVINGROCK_WALL, props), BuiltInRegistries.BLOCK.getKey(LIVINGROCK_WALL));
		r.accept(new BlockItem(LIVINGROCK_BUTTON, props), BuiltInRegistries.BLOCK.getKey(LIVINGROCK_BUTTON));
		r.accept(new BlockItem(LIVINGROCK_PRESSURE_PLATE, props), BuiltInRegistries.BLOCK.getKey(
				LIVINGROCK_PRESSURE_PLATE));
		r.accept(new BlockItem(POLISHED_LIVINGROCK, props), BuiltInRegistries.BLOCK.getKey(POLISHED_LIVINGROCK));
		r.accept(new BlockItem(POLISHED_LIVINGROCK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				POLISHED_LIVINGROCK_STAIRS));
		r.accept(new BlockItem(POLISHED_LIVINGROCK_SLAB, props), BuiltInRegistries.BLOCK.getKey(
				POLISHED_LIVINGROCK_SLAB));
		r.accept(new BlockItem(POLISHED_LIVINGROCK_WALL, props), BuiltInRegistries.BLOCK.getKey(
				POLISHED_LIVINGROCK_WALL));
		r.accept(new BlockItem(LIVINGROCK_SLATE, props), BuiltInRegistries.BLOCK.getKey(LIVINGROCK_SLATE));
		r.accept(new BlockItem(LIVINGROCK_BRICKS, props), BuiltInRegistries.BLOCK.getKey(LIVINGROCK_BRICKS));
		r.accept(new BlockItem(LIVINGROCK_BRICK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(LIVINGROCK_BRICK_STAIRS));
		r.accept(new BlockItem(LIVINGROCK_BRICK_SLAB, props), BuiltInRegistries.BLOCK.getKey(LIVINGROCK_BRICK_SLAB));
		r.accept(new BlockItem(LIVINGROCK_BRICK_WALL, props), BuiltInRegistries.BLOCK.getKey(LIVINGROCK_BRICK_WALL));
		r.accept(new BlockItem(MOSSY_LIVINGROCK_BRICKS, props), BuiltInRegistries.BLOCK.getKey(MOSSY_LIVINGROCK_BRICKS));
		r.accept(new BlockItem(MOSSY_LIVINGROCK_BRICK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				MOSSY_LIVINGROCK_BRICK_STAIRS));
		r.accept(new BlockItem(MOSSY_LIVINGROCK_BRICK_SLAB, props), BuiltInRegistries.BLOCK.getKey(
				MOSSY_LIVINGROCK_BRICK_SLAB));
		r.accept(new BlockItem(MOSSY_LIVINGROCK_BRICK_WALL, props), BuiltInRegistries.BLOCK.getKey(
				MOSSY_LIVINGROCK_BRICK_WALL));
		r.accept(new BlockItem(CRACKED_LIVINGROCK_BRICKS, props), BuiltInRegistries.BLOCK.getKey(
				CRACKED_LIVINGROCK_BRICKS));
		r.accept(new BlockItem(CHISELED_LIVINGROCK_BRICKS, props), BuiltInRegistries.BLOCK.getKey(
				CHISELED_LIVINGROCK_BRICKS));

		r.accept(new BlockItem(LIVINGWOOD_LOG, props), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_LOG));
		r.accept(new BlockItem(LIVINGWOOD, props), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD));
		r.accept(new BlockItem(LIVINGWOOD_STAIRS, props), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_STAIRS));
		r.accept(new BlockItem(LIVINGWOOD_SLAB, props), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_SLAB));
		r.accept(new BlockItem(LIVINGWOOD_WALL, props), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_WALL));
		r.accept(new BlockItem(STRIPPED_LIVINGWOOD_LOG, props), BuiltInRegistries.BLOCK.getKey(STRIPPED_LIVINGWOOD_LOG));
		r.accept(new BlockItem(STRIPPED_LIVINGWOOD, props), BuiltInRegistries.BLOCK.getKey(STRIPPED_LIVINGWOOD));
		r.accept(new BlockItem(STRIPPED_LIVINGWOOD_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				STRIPPED_LIVINGWOOD_STAIRS));
		r.accept(new BlockItem(STRIPPED_LIVINGWOOD_SLAB, props), BuiltInRegistries.BLOCK.getKey(
				STRIPPED_LIVINGWOOD_SLAB));
		r.accept(new BlockItem(STRIPPED_LIVINGWOOD_WALL, props), BuiltInRegistries.BLOCK.getKey(
				STRIPPED_LIVINGWOOD_WALL));
		r.accept(new BlockItem(GLIMMERING_LIVINGWOOD_LOG, props), BuiltInRegistries.BLOCK.getKey(
				GLIMMERING_LIVINGWOOD_LOG));
		r.accept(new BlockItem(GLIMMERING_LIVINGWOOD, props), BuiltInRegistries.BLOCK.getKey(GLIMMERING_LIVINGWOOD));
		r.accept(new BlockItem(STRIPPED_GLIMMERING_LIVINGWOOD_LOG, props), BuiltInRegistries.BLOCK.getKey(
				STRIPPED_GLIMMERING_LIVINGWOOD_LOG));
		r.accept(new BlockItem(STRIPPED_GLIMMERING_LIVINGWOOD, props), BuiltInRegistries.BLOCK.getKey(
				STRIPPED_GLIMMERING_LIVINGWOOD));
		r.accept(new BlockItem(LIVINGWOOD_PLANKS, props), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_PLANKS));
		r.accept(new BlockItem(LIVINGWOOD_PLANK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_PLANK_STAIRS));
		r.accept(new BlockItem(LIVINGWOOD_PLANK_SLAB, props), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_PLANK_SLAB));
		r.accept(new BlockItem(LIVINGWOOD_FENCE, props), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_FENCE));
		r.accept(new BlockItem(LIVINGWOOD_FENCE_GATE, props), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_FENCE_GATE));
		r.accept(new BlockItem(LIVINGWOOD_DOOR, props), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_DOOR));
		r.accept(new BlockItem(LIVINGWOOD_TRAPDOOR, props), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_TRAPDOOR));
		r.accept(new BlockItem(LIVINGWOOD_PRESSURE_PLATE, props), BuiltInRegistries.BLOCK.getKey(
				LIVINGWOOD_PRESSURE_PLATE));
		r.accept(new BlockItem(LIVINGWOOD_BUTTON, props), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_BUTTON));
		r.accept(new SignItem(BotaniaItems.defaultBuilder().stacksTo(16), LIVINGWOOD_SIGN,
				LIVINGWOOD_WALL_SIGN
		), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_SIGN));
		r.accept(new HangingSignItem(
				LIVINGWOOD_HANGING_SIGN, LIVINGWOOD_WALL_HANGING_SIGN,
				BotaniaItems.defaultBuilder().stacksTo(16)), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_HANGING_SIGN));
		r.accept(new BlockItem(MOSSY_LIVINGWOOD_PLANKS, props), BuiltInRegistries.BLOCK.getKey(MOSSY_LIVINGWOOD_PLANKS));
		r.accept(new BlockItem(FRAMED_LIVINGWOOD, props), BuiltInRegistries.BLOCK.getKey(FRAMED_LIVINGWOOD));
		r.accept(new BlockItem(PATTERN_FRAMED_LIVINGWOOD, props), BuiltInRegistries.BLOCK.getKey(
				PATTERN_FRAMED_LIVINGWOOD));

		r.accept(new BlockItem(DREAMWOOD_LOG, props), BuiltInRegistries.BLOCK.getKey(DREAMWOOD_LOG));
		r.accept(new BlockItem(DREAMWOOD, props), BuiltInRegistries.BLOCK.getKey(DREAMWOOD));
		r.accept(new BlockItem(DREAMWOOD_STAIRS, props), BuiltInRegistries.BLOCK.getKey(DREAMWOOD_STAIRS));
		r.accept(new BlockItem(DREAMWOOD_SLAB, props), BuiltInRegistries.BLOCK.getKey(DREAMWOOD_SLAB));
		r.accept(new BlockItem(DREAMWOOD_WALL, props), BuiltInRegistries.BLOCK.getKey(DREAMWOOD_WALL));
		r.accept(new BlockItem(STRIPPED_DREAMWOOD_LOG, props), BuiltInRegistries.BLOCK.getKey(STRIPPED_DREAMWOOD_LOG));
		r.accept(new BlockItem(STRIPPED_DREAMWOOD, props), BuiltInRegistries.BLOCK.getKey(STRIPPED_DREAMWOOD));
		r.accept(new BlockItem(STRIPPED_DREAMWOOD_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				STRIPPED_DREAMWOOD_STAIRS));
		r.accept(new BlockItem(STRIPPED_DREAMWOOD_SLAB, props), BuiltInRegistries.BLOCK.getKey(STRIPPED_DREAMWOOD_SLAB));
		r.accept(new BlockItem(STRIPPED_DREAMWOOD_WALL, props), BuiltInRegistries.BLOCK.getKey(STRIPPED_DREAMWOOD_WALL));
		r.accept(new BlockItem(GLIMMERING_DREAMWOOD_LOG, props), BuiltInRegistries.BLOCK.getKey(
				GLIMMERING_DREAMWOOD_LOG));
		r.accept(new BlockItem(GLIMMERING_DREAMWOOD, props), BuiltInRegistries.BLOCK.getKey(GLIMMERING_DREAMWOOD));
		r.accept(new BlockItem(STRIPPED_GLIMMERING_DREAMWOOD_LOG, props), BuiltInRegistries.BLOCK.getKey(
				STRIPPED_GLIMMERING_DREAMWOOD_LOG));
		r.accept(new BlockItem(STRIPPED_GLIMMERING_DREAMWOOD, props), BuiltInRegistries.BLOCK.getKey(
				STRIPPED_GLIMMERING_DREAMWOOD));
		r.accept(new BlockItem(DREAMWOOD_PLANKS, props), BuiltInRegistries.BLOCK.getKey(DREAMWOOD_PLANKS));
		r.accept(new BlockItem(DREAMWOOD_PLANK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(DREAMWOOD_PLANK_STAIRS));
		r.accept(new BlockItem(DREAMWOOD_PLANK_SLAB, props), BuiltInRegistries.BLOCK.getKey(DREAMWOOD_PLANK_SLAB));
		r.accept(new BlockItem(DREAMWOOD_FENCE, props), BuiltInRegistries.BLOCK.getKey(DREAMWOOD_FENCE));
		r.accept(new BlockItem(DREAMWOOD_FENCE_GATE, props), BuiltInRegistries.BLOCK.getKey(DREAMWOOD_FENCE_GATE));
		r.accept(new BlockItem(DREAMWOOD_DOOR, props), BuiltInRegistries.BLOCK.getKey(DREAMWOOD_DOOR));
		r.accept(new BlockItem(DREAMWOOD_TRAPDOOR, props), BuiltInRegistries.BLOCK.getKey(DREAMWOOD_TRAPDOOR));
		r.accept(new BlockItem(DREAMWOOD_PRESSURE_PLATE, props), BuiltInRegistries.BLOCK.getKey(
				DREAMWOOD_PRESSURE_PLATE));
		r.accept(new BlockItem(DREAMWOOD_BUTTON, props), BuiltInRegistries.BLOCK.getKey(DREAMWOOD_BUTTON));
		r.accept(new SignItem(BotaniaItems.defaultBuilder().stacksTo(16), DREAMWOOD_SIGN,
				DREAMWOOD_WALL_SIGN
		), BuiltInRegistries.BLOCK.getKey(DREAMWOOD_SIGN));
		r.accept(new HangingSignItem(
				DREAMWOOD_HANGING_SIGN, DREAMWOOD_WALL_HANGING_SIGN,
				BotaniaItems.defaultBuilder().stacksTo(16)), BuiltInRegistries.BLOCK.getKey(DREAMWOOD_HANGING_SIGN));
		r.accept(new BlockItem(MOSSY_DREAMWOOD_PLANKS, props), BuiltInRegistries.BLOCK.getKey(MOSSY_DREAMWOOD_PLANKS));
		r.accept(new BlockItem(FRAMED_DREAMWOOD, props), BuiltInRegistries.BLOCK.getKey(FRAMED_DREAMWOOD));
		r.accept(new BlockItem(PATTERN_FRAMED_DREAMWOOD, props), BuiltInRegistries.BLOCK.getKey(
				PATTERN_FRAMED_DREAMWOOD));

		r.accept(new BlockItem(MANA_SPREADER, props), BuiltInRegistries.BLOCK.getKey(MANA_SPREADER));
		r.accept(new BlockItem(PULSE_MANA_SPREADER, props), BuiltInRegistries.BLOCK.getKey(PULSE_MANA_SPREADER));
		r.accept(new BlockItem(ELVEN_MANA_SPREADER, props), BuiltInRegistries.BLOCK.getKey(ELVEN_MANA_SPREADER));
		r.accept(new BlockItem(GAIA_MANA_SPREADER, rareProps), BuiltInRegistries.BLOCK.getKey(GAIA_MANA_SPREADER));
		registerBlockItemWithColoredVariants(r, MANA_POOL, props);
		registerBlockItemWithColoredVariants(r, CREATIVE_MANA_POOL, epicProps);
		registerBlockItemWithColoredVariants(r, DILUTED_MANA_POOL, props);
		registerBlockItemWithColoredVariants(r, FABULOUS_MANA_POOL, props);
		r.accept(new BlockItem(ALCHEMY_CATALYST, props), BuiltInRegistries.BLOCK.getKey(ALCHEMY_CATALYST));
		r.accept(new BlockItem(CONJURATION_CATALYST, props), BuiltInRegistries.BLOCK.getKey(CONJURATION_CATALYST));
		r.accept(new BlockItem(MANASTEEL_BLOCK, props), BuiltInRegistries.BLOCK.getKey(MANASTEEL_BLOCK));
		r.accept(new BlockItem(TERRASTEEL_BLOCK, uncommonProps), BuiltInRegistries.BLOCK.getKey(TERRASTEEL_BLOCK));
		r.accept(new BlockItem(ELEMENTIUM_BLOCK, props), BuiltInRegistries.BLOCK.getKey(ELEMENTIUM_BLOCK));
		r.accept(new BlockItem(MANA_DIAMOND_BLOCK, props), BuiltInRegistries.BLOCK.getKey(MANA_DIAMOND_BLOCK));
		r.accept(new BlockItem(DRAGONSTONE_BLOCK, props), BuiltInRegistries.BLOCK.getKey(DRAGONSTONE_BLOCK));
		r.accept(new BlockItem(MANAGLASS, props), BuiltInRegistries.BLOCK.getKey(MANAGLASS));
		r.accept(new BlockItem(ALFGLASS, props), BuiltInRegistries.BLOCK.getKey(ALFGLASS));
		r.accept(new BlockItem(BIFROST_BLOCK, props), BuiltInRegistries.BLOCK.getKey(BIFROST_BLOCK));
		r.accept(new BlockItem(RUNIC_ALTAR, props), BuiltInRegistries.BLOCK.getKey(RUNIC_ALTAR));
		r.accept(new BlockItem(MANA_ENCHANTER, props), BuiltInRegistries.BLOCK.getKey(MANA_ENCHANTER));
		r.accept(new BlockItem(BOTANICAL_BREWERY, props), BuiltInRegistries.BLOCK.getKey(BOTANICAL_BREWERY));
		r.accept(new BlockItem(TERRESTRIAL_AGGLOMERATION_PLATE, props), BuiltInRegistries.BLOCK.getKey(
				TERRESTRIAL_AGGLOMERATION_PLATE));
		r.accept(new BlockItem(ELVEN_GATEWAY_CORE, uncommonProps), BuiltInRegistries.BLOCK.getKey(ELVEN_GATEWAY_CORE));

		r.accept(new BlockItem(MANA_PYLON, props), BuiltInRegistries.BLOCK.getKey(MANA_PYLON));
		r.accept(new BlockItem(NATURA_PYLON, uncommonProps), BuiltInRegistries.BLOCK.getKey(NATURA_PYLON));
		r.accept(new BlockItem(GAIA_PYLON, props), BuiltInRegistries.BLOCK.getKey(GAIA_PYLON));
		r.accept(new BlockItem(MANA_SPLITTER, props), BuiltInRegistries.BLOCK.getKey(MANA_SPLITTER));
		r.accept(new BlockItem(MANA_VOID, props), BuiltInRegistries.BLOCK.getKey(MANA_VOID));
		r.accept(new BlockItem(MANA_DETECTOR, props), BuiltInRegistries.BLOCK.getKey(MANA_DETECTOR));
		r.accept(new BlockItem(FORCE_RELAY, props), BuiltInRegistries.BLOCK.getKey(FORCE_RELAY));
		r.accept(new BlockItem(SPREADER_TURNTABLE, props), BuiltInRegistries.BLOCK.getKey(SPREADER_TURNTABLE));
		r.accept(new BlockItem(TINY_PLANET, props), BuiltInRegistries.BLOCK.getKey(TINY_PLANET));
		r.accept(new BlockItem(DRUM_OF_THE_WILD, props), BuiltInRegistries.BLOCK.getKey(DRUM_OF_THE_WILD));
		r.accept(new BlockItem(DRUM_OF_THE_GATHERING, props), BuiltInRegistries.BLOCK.getKey(DRUM_OF_THE_GATHERING));
		r.accept(new BlockItem(DRUM_OF_THE_CANOPY, props), BuiltInRegistries.BLOCK.getKey(DRUM_OF_THE_CANOPY));
		r.accept(new BlockItem(LIFE_IMBUER, props), BuiltInRegistries.BLOCK.getKey(LIFE_IMBUER));
		r.accept(new BlockItem(MANA_FLUXFIELD, props), BuiltInRegistries.BLOCK.getKey(MANA_FLUXFIELD));
		r.accept(new BlockItem(MANA_PRISM, props), BuiltInRegistries.BLOCK.getKey(MANA_PRISM));
		r.accept(new BlockItem(MANA_PUMP, props), BuiltInRegistries.BLOCK.getKey(MANA_PUMP));
		r.accept(new BlockItem(SPARK_TINKERER, props), BuiltInRegistries.BLOCK.getKey(SPARK_TINKERER));
		r.accept(new BlockItem(MANASTORM_CHARGE, rareProps), BuiltInRegistries.BLOCK.getKey(MANASTORM_CHARGE));
		r.accept(new BlockItem(MANATIDE_BELLOWS, props), BuiltInRegistries.BLOCK.getKey(MANATIDE_BELLOWS));
		r.accept(new BlockItem(OPEN_CRATE, props), BuiltInRegistries.BLOCK.getKey(OPEN_CRATE));
		r.accept(new BlockItem(CRAFTY_CRATE, props), BuiltInRegistries.BLOCK.getKey(CRAFTY_CRATE));
		r.accept(new BlockItem(EYE_OF_THE_ANCIENTS, props), BuiltInRegistries.BLOCK.getKey(EYE_OF_THE_ANCIENTS));
		r.accept(new BlockItem(ABSTRUSE_PLATFORM, props), BuiltInRegistries.BLOCK.getKey(ABSTRUSE_PLATFORM));
		r.accept(new BlockItem(SPECTRAL_PLATFORM, props), BuiltInRegistries.BLOCK.getKey(SPECTRAL_PLATFORM));
		r.accept(new BlockItem(INFRANGIBLE_PLATFORM, epicProps), BuiltInRegistries.BLOCK.getKey(INFRANGIBLE_PLATFORM));
		r.accept(new TinyPotatoBlockItem(TINY_POTATO, props), BuiltInRegistries.BLOCK.getKey(TINY_POTATO));
		r.accept(new BlockItem(ENDER_OVERSEER, props), BuiltInRegistries.BLOCK.getKey(ENDER_OVERSEER));
		r.accept(new BlockItem(RED_STRINGED_CONTAINER, props), BuiltInRegistries.BLOCK.getKey(RED_STRINGED_CONTAINER));
		r.accept(new BlockItem(RED_STRINGED_DISPENSER, props), BuiltInRegistries.BLOCK.getKey(RED_STRINGED_DISPENSER));
		r.accept(new BlockItem(RED_STRINGED_NUTRIFIER, props), BuiltInRegistries.BLOCK.getKey(RED_STRINGED_NUTRIFIER));
		r.accept(new BlockItem(RED_STRINGED_COMPARATOR, props), BuiltInRegistries.BLOCK.getKey(RED_STRINGED_COMPARATOR));
		r.accept(new BlockItem(RED_STRINGED_SPOOFER, props), BuiltInRegistries.BLOCK.getKey(RED_STRINGED_SPOOFER));
		r.accept(new BlockItem(RED_STRINGED_INTERCEPTOR, props), BuiltInRegistries.BLOCK.getKey(
				RED_STRINGED_INTERCEPTOR));
		r.accept(new BlockItem(CORPOREA_FUNNEL, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_FUNNEL));
		r.accept(new BlockItem(CORPOREA_INTERCEPTOR, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_INTERCEPTOR));
		r.accept(new BlockItem(CORPOREA_INDEX, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_INDEX));
		r.accept(new BlockItem(CORPOREA_CRYSTAL_CUBE, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_CRYSTAL_CUBE));
		r.accept(new BlockItem(CORPOREA_RETAINER, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_RETAINER));
		r.accept(new BlockItem(CORPOREA_BLOCK, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_BLOCK));
		r.accept(new BlockItem(CORPOREA_STAIRS, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_STAIRS));
		r.accept(new BlockItem(CORPOREA_SLAB, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_SLAB));
		r.accept(new BlockItem(CORPOREA_WALL, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_WALL));
		r.accept(new BlockItem(CORPOREA_BUTTON, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_BUTTON));
		r.accept(new BlockItem(CORPOREA_PRESSURE_PLATE, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_PRESSURE_PLATE));
		r.accept(new BlockItem(CORPOREA_BRICKS, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_BRICKS));
		r.accept(new BlockItem(CORPOREA_BRICK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_BRICK_STAIRS));
		r.accept(new BlockItem(CORPOREA_BRICK_SLAB, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_BRICK_SLAB));
		r.accept(new BlockItem(CORPOREA_BRICK_WALL, props), BuiltInRegistries.BLOCK.getKey(CORPOREA_BRICK_WALL));
		r.accept(new BlockItem(INCENSE_PLATE, props), BuiltInRegistries.BLOCK.getKey(INCENSE_PLATE));
		r.accept(new BlockItem(HOVERING_HOURGLASS, props), BuiltInRegistries.BLOCK.getKey(HOVERING_HOURGLASS));
		r.accept(new BlockItem(SPECTRAL_RAIL, props), BuiltInRegistries.BLOCK.getKey(SPECTRAL_RAIL));
		r.accept(new BlockItem(LUMINIZER, props), BuiltInRegistries.BLOCK.getKey(LUMINIZER));
		r.accept(new BlockItem(DETECTOR_LUMINIZER, props), BuiltInRegistries.BLOCK.getKey(DETECTOR_LUMINIZER));
		r.accept(new BlockItem(FORK_LUMINIZER, props), BuiltInRegistries.BLOCK.getKey(FORK_LUMINIZER));
		r.accept(new BlockItem(TOGGLE_LUMINIZER, props), BuiltInRegistries.BLOCK.getKey(TOGGLE_LUMINIZER));
		r.accept(new BlockItem(LUMINIZER_LAUNCHER, props), BuiltInRegistries.BLOCK.getKey(LUMINIZER_LAUNCHER));
		r.accept(new BlockItem(CACOPHONIUM_BLOCK, props), BuiltInRegistries.BLOCK.getKey(CACOPHONIUM_BLOCK));
		r.accept(new BlockItem(CELLULAR_BLOCK, props), BuiltInRegistries.BLOCK.getKey(CELLULAR_BLOCK));
		r.accept(new BlockItem(TERU_TERU_BOZU, props), BuiltInRegistries.BLOCK.getKey(TERU_TERU_BOZU));
		r.accept(new BlockItem(LIVINGWOOD_AVATAR, props), BuiltInRegistries.BLOCK.getKey(LIVINGWOOD_AVATAR));
		r.accept(new BlockItem(LIVING_ROOT, props), BuiltInRegistries.BLOCK.getKey(LIVING_ROOT));
		r.accept(new BlockItem(FEL_PUMPKIN, props), BuiltInRegistries.BLOCK.getKey(FEL_PUMPKIN));
		r.accept(new BlockItem(COCOON_OF_CAPRICE, props), BuiltInRegistries.BLOCK.getKey(COCOON_OF_CAPRICE));
		r.accept(new BlockItem(ANIMATED_TORCH, props), BuiltInRegistries.BLOCK.getKey(ANIMATED_TORCH));
		r.accept(new BlockItem(STARFIELD_CREATOR, props), BuiltInRegistries.BLOCK.getKey(STARFIELD_CREATOR));
		r.accept(new BlockItem(AZULEJO_0, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_0));
		r.accept(new BlockItem(AZULEJO_1, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_1));
		r.accept(new BlockItem(AZULEJO_2, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_2));
		r.accept(new BlockItem(AZULEJO_3, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_3));
		r.accept(new BlockItem(AZULEJO_4, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_4));
		r.accept(new BlockItem(AZULEJO_5, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_5));
		r.accept(new BlockItem(AZULEJO_6, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_6));
		r.accept(new BlockItem(AZULEJO_7, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_7));
		r.accept(new BlockItem(AZULEJO_8, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_8));
		r.accept(new BlockItem(AZULEJO_9, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_9));
		r.accept(new BlockItem(AZULEJO_10, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_10));
		r.accept(new BlockItem(AZULEJO_11, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_11));
		r.accept(new BlockItem(AZULEJO_12, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_12));
		r.accept(new BlockItem(AZULEJO_13, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_13));
		r.accept(new BlockItem(AZULEJO_14, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_14));
		r.accept(new BlockItem(AZULEJO_15, props), BuiltInRegistries.BLOCK.getKey(AZULEJO_15));
		r.accept(new BlockItem(BLAZE_MESH, props), BuiltInRegistries.BLOCK.getKey(BLAZE_MESH));
		r.accept(new StandingAndWallBlockItem(GAIA_HEAD, GAIA_WALL_HEAD_BLOCK, rareProps, Direction.DOWN), BuiltInRegistries.BLOCK.getKey(
				GAIA_HEAD));
		r.accept(new BlockItem(SHIMMERROCK, props), BuiltInRegistries.BLOCK.getKey(SHIMMERROCK));
		r.accept(new BlockItem(SHIMMERROCK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(SHIMMERROCK_STAIRS));
		r.accept(new BlockItem(SHIMMERROCK_SLAB, props), BuiltInRegistries.BLOCK.getKey(SHIMMERROCK_SLAB));
		r.accept(new BlockItem(SHIMMERROCK_WALL, props), BuiltInRegistries.BLOCK.getKey(SHIMMERROCK_WALL));
		r.accept(new BlockItem(SHIMMERROCK_BUTTON, props), BuiltInRegistries.BLOCK.getKey(SHIMMERROCK_BUTTON));
		r.accept(new BlockItem(SHIMMERROCK_PRESSURE_PLATE, props), BuiltInRegistries.BLOCK.getKey(
				SHIMMERROCK_PRESSURE_PLATE));
		r.accept(new BlockItem(SHIMMERWOOD_PLANKS, props), BuiltInRegistries.BLOCK.getKey(SHIMMERWOOD_PLANKS));
		r.accept(new BlockItem(SHIMMERWOOD_PLANK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				SHIMMERWOOD_PLANK_STAIRS));
		r.accept(new BlockItem(SHIMMERWOOD_PLANK_SLAB, props), BuiltInRegistries.BLOCK.getKey(SHIMMERWOOD_PLANK_SLAB));
		r.accept(new BlockItem(SHIMMERWOOD_FENCE, props), BuiltInRegistries.BLOCK.getKey(SHIMMERWOOD_FENCE));
		r.accept(new BlockItem(SHIMMERWOOD_FENCE_GATE, props), BuiltInRegistries.BLOCK.getKey(SHIMMERWOOD_FENCE_GATE));
		r.accept(new BlockItem(SHIMMERWOOD_BUTTON, props), BuiltInRegistries.BLOCK.getKey(SHIMMERWOOD_BUTTON));
		r.accept(new BlockItem(SHIMMERWOOD_PRESSURE_PLATE, props), BuiltInRegistries.BLOCK.getKey(
				SHIMMERWOOD_PRESSURE_PLATE));
		r.accept(new BlockItem(DRY_GRASS_BLOCK, props), BuiltInRegistries.BLOCK.getKey(DRY_GRASS_BLOCK));
		r.accept(new BlockItem(GOLDEN_GRASS_BLOCK, props), BuiltInRegistries.BLOCK.getKey(GOLDEN_GRASS_BLOCK));
		r.accept(new BlockItem(VIVID_GRASS_BLOCK, props), BuiltInRegistries.BLOCK.getKey(VIVID_GRASS_BLOCK));
		r.accept(new BlockItem(SCORCHED_GRASS_BLOCK, props), BuiltInRegistries.BLOCK.getKey(SCORCHED_GRASS_BLOCK));
		r.accept(new BlockItem(INFUSED_GRASS_BLOCK, props), BuiltInRegistries.BLOCK.getKey(INFUSED_GRASS_BLOCK));
		r.accept(new BlockItem(MUTATED_GRASS_BLOCK, props), BuiltInRegistries.BLOCK.getKey(MUTATED_GRASS_BLOCK));
		r.accept(new BlockItem(DAYBLOOM_MOTIF, props), BuiltInRegistries.BLOCK.getKey(DAYBLOOM_MOTIF));
		r.accept(new BlockItem(NIGHTSHADE_MOTIF, props), BuiltInRegistries.BLOCK.getKey(NIGHTSHADE_MOTIF));
		r.accept(new BlockItem(HYDROANGEAS_MOTIF, props), BuiltInRegistries.BLOCK.getKey(HYDROANGEAS_MOTIF));

		r.accept(new BlockItem(DARK_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(DARK_QUARTZ_BLOCK));
		r.accept(new BlockItem(DARK_QUARTZ_STAIRS, props), BuiltInRegistries.BLOCK.getKey(DARK_QUARTZ_STAIRS));
		r.accept(new BlockItem(DARK_QUARTZ_SLAB, props), BuiltInRegistries.BLOCK.getKey(DARK_QUARTZ_SLAB));
		r.accept(new BlockItem(CHISELED_DARK_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(
				CHISELED_DARK_QUARTZ_BLOCK));
		r.accept(new BlockItem(DARK_QUARTZ_BRICKS, props), BuiltInRegistries.BLOCK.getKey(DARK_QUARTZ_BRICKS));
		r.accept(new BlockItem(DARK_QUARTZ_PILLAR, props), BuiltInRegistries.BLOCK.getKey(DARK_QUARTZ_PILLAR));
		r.accept(new BlockItem(SMOOTH_DARK_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_DARK_QUARTZ_BLOCK));
		r.accept(new BlockItem(SMOOTH_DARK_QUARTZ_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_DARK_QUARTZ_STAIRS));
		r.accept(new BlockItem(SMOOTH_DARK_QUARTZ_SLAB, props), BuiltInRegistries.BLOCK.getKey(SMOOTH_DARK_QUARTZ_SLAB));

		r.accept(new BlockItem(MANA_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(MANA_QUARTZ_BLOCK));
		r.accept(new BlockItem(MANA_QUARTZ_STAIRS, props), BuiltInRegistries.BLOCK.getKey(MANA_QUARTZ_STAIRS));
		r.accept(new BlockItem(MANA_QUARTZ_SLAB, props), BuiltInRegistries.BLOCK.getKey(MANA_QUARTZ_SLAB));
		r.accept(new BlockItem(CHISELED_MANA_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(
				CHISELED_MANA_QUARTZ_BLOCK));
		r.accept(new BlockItem(MANA_QUARTZ_BRICKS, props), BuiltInRegistries.BLOCK.getKey(MANA_QUARTZ_BRICKS));
		r.accept(new BlockItem(MANA_QUARTZ_PILLAR, props), BuiltInRegistries.BLOCK.getKey(MANA_QUARTZ_PILLAR));
		r.accept(new BlockItem(SMOOTH_MANA_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_MANA_QUARTZ_BLOCK));
		r.accept(new BlockItem(SMOOTH_MANA_QUARTZ_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_MANA_QUARTZ_STAIRS));
		r.accept(new BlockItem(SMOOTH_MANA_QUARTZ_SLAB, props), BuiltInRegistries.BLOCK.getKey(SMOOTH_MANA_QUARTZ_SLAB));

		r.accept(new BlockItem(BLAZE_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(BLAZE_QUARTZ_BLOCK));
		r.accept(new BlockItem(BLAZE_QUARTZ_STAIRS, props), BuiltInRegistries.BLOCK.getKey(BLAZE_QUARTZ_STAIRS));
		r.accept(new BlockItem(BLAZE_QUARTZ_SLAB, props), BuiltInRegistries.BLOCK.getKey(BLAZE_QUARTZ_SLAB));
		r.accept(new BlockItem(CHISELED_BLAZE_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(
				CHISELED_BLAZE_QUARTZ_BLOCK));
		r.accept(new BlockItem(BLAZE_QUARTZ_BRICKS, props), BuiltInRegistries.BLOCK.getKey(BLAZE_QUARTZ_BRICKS));
		r.accept(new BlockItem(BLAZE_QUARTZ_PILLAR, props), BuiltInRegistries.BLOCK.getKey(BLAZE_QUARTZ_PILLAR));
		r.accept(new BlockItem(SMOOTH_BLAZE_QUARTZ_BLOCKS, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_BLAZE_QUARTZ_BLOCKS));
		r.accept(new BlockItem(SMOOTH_BLAZE_QUARTZ_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_BLAZE_QUARTZ_STAIRS));
		r.accept(new BlockItem(SMOOTH_BLAZE_QUARTZ_SLAB, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_BLAZE_QUARTZ_SLAB));

		r.accept(new BlockItem(LAVENDER_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(LAVENDER_QUARTZ_BLOCK));
		r.accept(new BlockItem(LAVENDER_QUARTZ_STAIRS, props), BuiltInRegistries.BLOCK.getKey(LAVENDER_QUARTZ_STAIRS));
		r.accept(new BlockItem(LAVENDER_QUARTZ_SLAB, props), BuiltInRegistries.BLOCK.getKey(LAVENDER_QUARTZ_SLAB));
		r.accept(new BlockItem(CHISELED_LAVENDER_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(
				CHISELED_LAVENDER_QUARTZ_BLOCK));
		r.accept(new BlockItem(LAVENDER_QUARTZ_BRICKS, props), BuiltInRegistries.BLOCK.getKey(LAVENDER_QUARTZ_BRICKS));
		r.accept(new BlockItem(LAVENDER_QUARTZ_PILLAR, props), BuiltInRegistries.BLOCK.getKey(LAVENDER_QUARTZ_PILLAR));
		r.accept(new BlockItem(SMOOTH_LAVENDER_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_LAVENDER_QUARTZ_BLOCK));
		r.accept(new BlockItem(SMOOTH_LAVENDER_QUARTZ_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_LAVENDER_QUARTZ_STAIRS));
		r.accept(new BlockItem(SMOOTH_LAVENDER_QUARTZ_SLAB, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_LAVENDER_QUARTZ_SLAB));

		r.accept(new BlockItem(RED_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(RED_QUARTZ_BLOCK));
		r.accept(new BlockItem(RED_QUARTZ_STAIRS, props), BuiltInRegistries.BLOCK.getKey(RED_QUARTZ_STAIRS));
		r.accept(new BlockItem(RED_QUARTZ_SLAB, props), BuiltInRegistries.BLOCK.getKey(RED_QUARTZ_SLAB));
		r.accept(new BlockItem(CHISELED_RED_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(
				CHISELED_RED_QUARTZ_BLOCK));
		r.accept(new BlockItem(RED_QUARTZ_BRICKS, props), BuiltInRegistries.BLOCK.getKey(RED_QUARTZ_BRICKS));
		r.accept(new BlockItem(RED_QUARTZ_PILLAR, props), BuiltInRegistries.BLOCK.getKey(RED_QUARTZ_PILLAR));
		r.accept(new BlockItem(SMOOTH_RED_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(SMOOTH_RED_QUARTZ_BLOCK));
		r.accept(new BlockItem(SMOOTH_RED_QUARTZ_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_RED_QUARTZ_STAIRS));
		r.accept(new BlockItem(SMOOTH_RED_QUARTZ_SLAB, props), BuiltInRegistries.BLOCK.getKey(SMOOTH_RED_QUARTZ_SLAB));

		r.accept(new BlockItem(ELVEN_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(ELVEN_QUARTZ_BLOCK));
		r.accept(new BlockItem(ELVEN_QUARTZ_STAIRS, props), BuiltInRegistries.BLOCK.getKey(ELVEN_QUARTZ_STAIRS));
		r.accept(new BlockItem(ELVEN_QUARTZ_SLAB, props), BuiltInRegistries.BLOCK.getKey(ELVEN_QUARTZ_SLAB));
		r.accept(new BlockItem(CHISELED_ELVEN_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(
				CHISELED_ELVEN_QUARTZ_BLOCK));
		r.accept(new BlockItem(ELVEN_QUARTZ_BRICKS, props), BuiltInRegistries.BLOCK.getKey(ELVEN_QUARTZ_BRICKS));
		r.accept(new BlockItem(ELVEN_QUARTZ_PILLAR, props), BuiltInRegistries.BLOCK.getKey(ELVEN_QUARTZ_PILLAR));
		r.accept(new BlockItem(SMOOTH_ELVEN_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_ELVEN_QUARTZ_BLOCK));
		r.accept(new BlockItem(SMOOTH_ELVEN_QUARTZ_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_ELVEN_QUARTZ_STAIRS));
		r.accept(new BlockItem(SMOOTH_ELVEN_QUARTZ_SLAB, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_ELVEN_QUARTZ_SLAB));

		r.accept(new BlockItem(SUNNY_QUART_BLOCK, props), BuiltInRegistries.BLOCK.getKey(SUNNY_QUART_BLOCK));
		r.accept(new BlockItem(SUNNY_QUARTZ_STAIRS, props), BuiltInRegistries.BLOCK.getKey(SUNNY_QUARTZ_STAIRS));
		r.accept(new BlockItem(SUNNY_QUARTZ_SLAB, props), BuiltInRegistries.BLOCK.getKey(SUNNY_QUARTZ_SLAB));
		r.accept(new BlockItem(CHISELED_SUNNY_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(
				CHISELED_SUNNY_QUARTZ_BLOCK));
		r.accept(new BlockItem(SUNNY_QUARTZ_BRICKS, props), BuiltInRegistries.BLOCK.getKey(SUNNY_QUARTZ_BRICKS));
		r.accept(new BlockItem(SUNNY_QUARTZ_PILLAR, props), BuiltInRegistries.BLOCK.getKey(SUNNY_QUARTZ_PILLAR));
		r.accept(new BlockItem(SMOOTH_SUNNY_QUARTZ_BLOCK, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_SUNNY_QUARTZ_BLOCK));
		r.accept(new BlockItem(SMOOTH_SUNNY_QUARTZ_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_SUNNY_QUARTZ_STAIRS));
		r.accept(new BlockItem(SMOOTH_SUNNY_QUARTZ_SLAB, props), BuiltInRegistries.BLOCK.getKey(
				SMOOTH_SUNNY_QUARTZ_SLAB));

		r.accept(new BlockItem(FUCHSITE, props), BuiltInRegistries.BLOCK.getKey(FUCHSITE));
		r.accept(new BlockItem(FUCHSITE_STAIRS, props), BuiltInRegistries.BLOCK.getKey(FUCHSITE_STAIRS));
		r.accept(new BlockItem(FUCHSITE_SLAB, props), BuiltInRegistries.BLOCK.getKey(FUCHSITE_SLAB));
		r.accept(new BlockItem(FUCHSITE_WALL, props), BuiltInRegistries.BLOCK.getKey(FUCHSITE_WALL));
		r.accept(new BlockItem(FUCHSITE_BUTTON, props), BuiltInRegistries.BLOCK.getKey(FUCHSITE_BUTTON));
		r.accept(new BlockItem(FUCHSITE_PRESSURE_PLATE, props), BuiltInRegistries.BLOCK.getKey(FUCHSITE_PRESSURE_PLATE));
		r.accept(new BlockItem(FUCHSITE_BRICKS, props), BuiltInRegistries.BLOCK.getKey(FUCHSITE_BRICKS));
		r.accept(new BlockItem(FUCHSITE_BRICK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(FUCHSITE_BRICK_STAIRS));
		r.accept(new BlockItem(FUCHSITE_BRICK_SLAB, props), BuiltInRegistries.BLOCK.getKey(FUCHSITE_BRICK_SLAB));
		r.accept(new BlockItem(FUCHSITE_BRICK_WALL, props), BuiltInRegistries.BLOCK.getKey(FUCHSITE_BRICK_WALL));
		r.accept(new BlockItem(COBBLED_FUCHSITE, props), BuiltInRegistries.BLOCK.getKey(COBBLED_FUCHSITE));
		r.accept(new BlockItem(COBBLED_FUCHSITE_STAIRS, props), BuiltInRegistries.BLOCK.getKey(COBBLED_FUCHSITE_STAIRS));
		r.accept(new BlockItem(COBBLED_FUCHSITE_SLAB, props), BuiltInRegistries.BLOCK.getKey(COBBLED_FUCHSITE_SLAB));
		r.accept(new BlockItem(COBBLED_FUCHSITE_WALL, props), BuiltInRegistries.BLOCK.getKey(COBBLED_FUCHSITE_WALL));
		r.accept(new BlockItem(CHISELED_FUCHSITE_BRICKS, props), BuiltInRegistries.BLOCK.getKey(
				CHISELED_FUCHSITE_BRICKS));

		r.accept(new BlockItem(TALC, props), BuiltInRegistries.BLOCK.getKey(TALC));
		r.accept(new BlockItem(TALC_STAIRS, props), BuiltInRegistries.BLOCK.getKey(TALC_STAIRS));
		r.accept(new BlockItem(TALC_SLAB, props), BuiltInRegistries.BLOCK.getKey(TALC_SLAB));
		r.accept(new BlockItem(TALC_WALL, props), BuiltInRegistries.BLOCK.getKey(TALC_WALL));
		r.accept(new BlockItem(TALC_BUTTON, props), BuiltInRegistries.BLOCK.getKey(TALC_BUTTON));
		r.accept(new BlockItem(TALC_PRESSURE_PLATE, props), BuiltInRegistries.BLOCK.getKey(TALC_PRESSURE_PLATE));
		r.accept(new BlockItem(TALC_BRICKS, props), BuiltInRegistries.BLOCK.getKey(TALC_BRICKS));
		r.accept(new BlockItem(TALC_BRICK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(TALC_BRICK_STAIRS));
		r.accept(new BlockItem(TALC_BRICK_SLAB, props), BuiltInRegistries.BLOCK.getKey(TALC_BRICK_SLAB));
		r.accept(new BlockItem(TALC_BRICK_WALL, props), BuiltInRegistries.BLOCK.getKey(TALC_BRICK_WALL));
		r.accept(new BlockItem(COBBLED_TALC, props), BuiltInRegistries.BLOCK.getKey(COBBLED_TALC));
		r.accept(new BlockItem(COBBLED_TALC_STAIRS, props), BuiltInRegistries.BLOCK.getKey(COBBLED_TALC_STAIRS));
		r.accept(new BlockItem(COBBLED_TALC_SLAB, props), BuiltInRegistries.BLOCK.getKey(COBBLED_TALC_SLAB));
		r.accept(new BlockItem(COBBLED_TALC_WALL, props), BuiltInRegistries.BLOCK.getKey(COBBLED_TALC_WALL));
		r.accept(new BlockItem(CHISELED_TALC_BRICKS, props), BuiltInRegistries.BLOCK.getKey(CHISELED_TALC_BRICKS));

		r.accept(new BlockItem(GNEISS, props), BuiltInRegistries.BLOCK.getKey(GNEISS));
		r.accept(new BlockItem(GNEISS_STAIRS, props), BuiltInRegistries.BLOCK.getKey(GNEISS_STAIRS));
		r.accept(new BlockItem(GNEISS_SLAB, props), BuiltInRegistries.BLOCK.getKey(GNEISS_SLAB));
		r.accept(new BlockItem(GNEISS_WALL, props), BuiltInRegistries.BLOCK.getKey(GNEISS_WALL));
		r.accept(new BlockItem(GNEISS_BUTTON, props), BuiltInRegistries.BLOCK.getKey(GNEISS_BUTTON));
		r.accept(new BlockItem(GNEISS_PRESSURE_PLATE, props), BuiltInRegistries.BLOCK.getKey(GNEISS_PRESSURE_PLATE));
		r.accept(new BlockItem(GNEISS_BRICKS, props), BuiltInRegistries.BLOCK.getKey(GNEISS_BRICKS));
		r.accept(new BlockItem(GNEISS_BRICK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(GNEISS_BRICK_STAIRS));
		r.accept(new BlockItem(GNEISS_BRICK_SLAB, props), BuiltInRegistries.BLOCK.getKey(GNEISS_BRICK_SLAB));
		r.accept(new BlockItem(GNEISS_BRICK_WALL, props), BuiltInRegistries.BLOCK.getKey(GNEISS_BRICK_WALL));
		r.accept(new BlockItem(COBBLED_GNEISS, props), BuiltInRegistries.BLOCK.getKey(COBBLED_GNEISS));
		r.accept(new BlockItem(COBBLED_GNEISS_STAIRS, props), BuiltInRegistries.BLOCK.getKey(COBBLED_GNEISS_STAIRS));
		r.accept(new BlockItem(COBBLED_GNEISS_SLAB, props), BuiltInRegistries.BLOCK.getKey(COBBLED_GNEISS_SLAB));
		r.accept(new BlockItem(COBBLED_GNEISS_WALL, props), BuiltInRegistries.BLOCK.getKey(COBBLED_GNEISS_WALL));
		r.accept(new BlockItem(CHISELED_GNEISS_BRICKS, props), BuiltInRegistries.BLOCK.getKey(CHISELED_GNEISS_BRICKS));

		r.accept(new BlockItem(MYCELITE, props), BuiltInRegistries.BLOCK.getKey(MYCELITE));
		r.accept(new BlockItem(MYCELITE_STAIRS, props), BuiltInRegistries.BLOCK.getKey(MYCELITE_STAIRS));
		r.accept(new BlockItem(MYCELITE_SLAB, props), BuiltInRegistries.BLOCK.getKey(MYCELITE_SLAB));
		r.accept(new BlockItem(MYCELITE_WALL, props), BuiltInRegistries.BLOCK.getKey(MYCELITE_WALL));
		r.accept(new BlockItem(MYCELITE_BUTTON, props), BuiltInRegistries.BLOCK.getKey(MYCELITE_BUTTON));
		r.accept(new BlockItem(MYCELITE_PRESSURE_PLATE, props), BuiltInRegistries.BLOCK.getKey(MYCELITE_PRESSURE_PLATE));
		r.accept(new BlockItem(MYCELITE_BRICKS, props), BuiltInRegistries.BLOCK.getKey(MYCELITE_BRICKS));
		r.accept(new BlockItem(MYCELITE_BRICK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(MYCELITE_BRICK_STAIRS));
		r.accept(new BlockItem(MYCELITE_BRICK_SLAB, props), BuiltInRegistries.BLOCK.getKey(MYCELITE_BRICK_SLAB));
		r.accept(new BlockItem(MYCELITE_BRICK_WALL, props), BuiltInRegistries.BLOCK.getKey(MYCELITE_BRICK_WALL));
		r.accept(new BlockItem(COBBLED_MYCELITE, props), BuiltInRegistries.BLOCK.getKey(COBBLED_MYCELITE));
		r.accept(new BlockItem(COBBLED_MYCELITE_STAIRS, props), BuiltInRegistries.BLOCK.getKey(COBBLED_MYCELITE_STAIRS));
		r.accept(new BlockItem(COBBLED_MYCELITE_SLAB, props), BuiltInRegistries.BLOCK.getKey(COBBLED_MYCELITE_SLAB));
		r.accept(new BlockItem(COBBLED_MYCELITE_WALL, props), BuiltInRegistries.BLOCK.getKey(COBBLED_MYCELITE_WALL));
		r.accept(new BlockItem(CHISELED_MYCELITE_BRICKS, props), BuiltInRegistries.BLOCK.getKey(
				CHISELED_MYCELITE_BRICKS));

		r.accept(new BlockItem(CATACLASITE, props), BuiltInRegistries.BLOCK.getKey(CATACLASITE));
		r.accept(new BlockItem(CATACLASITE_STAIRS, props), BuiltInRegistries.BLOCK.getKey(CATACLASITE_STAIRS));
		r.accept(new BlockItem(CATACLASITE_SLAB, props), BuiltInRegistries.BLOCK.getKey(CATACLASITE_SLAB));
		r.accept(new BlockItem(CATACLASITE_WALL, props), BuiltInRegistries.BLOCK.getKey(CATACLASITE_WALL));
		r.accept(new BlockItem(CATACLASITE_BUTTON, props), BuiltInRegistries.BLOCK.getKey(CATACLASITE_BUTTON));
		r.accept(new BlockItem(CATACLASITE_PRESSURE_PLATE, props), BuiltInRegistries.BLOCK.getKey(
				CATACLASITE_PRESSURE_PLATE));
		r.accept(new BlockItem(CATACLASITE_BRICKS, props), BuiltInRegistries.BLOCK.getKey(CATACLASITE_BRICKS));
		r.accept(new BlockItem(CATACLASITE_BRICK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				CATACLASITE_BRICK_STAIRS));
		r.accept(new BlockItem(CATACLASITE_BRICK_SLAB, props), BuiltInRegistries.BLOCK.getKey(CATACLASITE_BRICK_SLAB));
		r.accept(new BlockItem(CATACLASITE_BRICK_WALL, props), BuiltInRegistries.BLOCK.getKey(CATACLASITE_BRICK_WALL));
		r.accept(new BlockItem(COBBLED_CATACLASITE, props), BuiltInRegistries.BLOCK.getKey(COBBLED_CATACLASITE));
		r.accept(new BlockItem(COBBLED_CATACLASITE_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				COBBLED_CATACLASITE_STAIRS));
		r.accept(new BlockItem(COBBLED_CATACLASITE_SLAB, props), BuiltInRegistries.BLOCK.getKey(
				COBBLED_CATACLASITE_SLAB));
		r.accept(new BlockItem(COBBLED_CATACLASITE_WALL, props), BuiltInRegistries.BLOCK.getKey(
				COBBLED_CATACLASITE_WALL));
		r.accept(new BlockItem(CHISELED_CATACLASITE_BRICKS, props), BuiltInRegistries.BLOCK.getKey(
				CHISELED_CATACLASITE_BRICKS));

		r.accept(new BlockItem(SOLITE, props), BuiltInRegistries.BLOCK.getKey(SOLITE));
		r.accept(new BlockItem(SOLITE_STAIRS, props), BuiltInRegistries.BLOCK.getKey(SOLITE_STAIRS));
		r.accept(new BlockItem(SOLITE_SLAB, props), BuiltInRegistries.BLOCK.getKey(SOLITE_SLAB));
		r.accept(new BlockItem(SOLITE_WALL, props), BuiltInRegistries.BLOCK.getKey(SOLITE_WALL));
		r.accept(new BlockItem(SOLITE_BUTTON, props), BuiltInRegistries.BLOCK.getKey(SOLITE_BUTTON));
		r.accept(new BlockItem(SOLITE_PRESSURE_PLATE, props), BuiltInRegistries.BLOCK.getKey(SOLITE_PRESSURE_PLATE));
		r.accept(new BlockItem(SOLITE_BRICKS, props), BuiltInRegistries.BLOCK.getKey(SOLITE_BRICKS));
		r.accept(new BlockItem(SOLITE_BRICK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(SOLITE_BRICK_STAIRS));
		r.accept(new BlockItem(SOLITE_BRICK_SLAB, props), BuiltInRegistries.BLOCK.getKey(SOLITE_BRICK_SLAB));
		r.accept(new BlockItem(SOLITE_BRICK_WALL, props), BuiltInRegistries.BLOCK.getKey(SOLITE_BRICK_WALL));
		r.accept(new BlockItem(COBBLED_SOLITE, props), BuiltInRegistries.BLOCK.getKey(COBBLED_SOLITE));
		r.accept(new BlockItem(COBBLED_SOLITE_STAIRS, props), BuiltInRegistries.BLOCK.getKey(COBBLED_SOLITE_STAIRS));
		r.accept(new BlockItem(COBBLED_SOLITE_SLAB, props), BuiltInRegistries.BLOCK.getKey(COBBLED_SOLITE_SLAB));
		r.accept(new BlockItem(COBBLED_SOLITE_WALL, props), BuiltInRegistries.BLOCK.getKey(COBBLED_SOLITE_WALL));
		r.accept(new BlockItem(CHISELED_SOLITE_BRICKS, props), BuiltInRegistries.BLOCK.getKey(CHISELED_SOLITE_BRICKS));

		r.accept(new BlockItem(LUNITE, props), BuiltInRegistries.BLOCK.getKey(LUNITE));
		r.accept(new BlockItem(LUNITE_STAIRS, props), BuiltInRegistries.BLOCK.getKey(LUNITE_STAIRS));
		r.accept(new BlockItem(LUNITE_SLAB, props), BuiltInRegistries.BLOCK.getKey(LUNITE_SLAB));
		r.accept(new BlockItem(LUNITE_WALL, props), BuiltInRegistries.BLOCK.getKey(LUNITE_WALL));
		r.accept(new BlockItem(LUNITE_BUTTON, props), BuiltInRegistries.BLOCK.getKey(LUNITE_BUTTON));
		r.accept(new BlockItem(LUNITE_PRESSURE_PLATE, props), BuiltInRegistries.BLOCK.getKey(LUNITE_PRESSURE_PLATE));
		r.accept(new BlockItem(LUNITE_BRICKS, props), BuiltInRegistries.BLOCK.getKey(LUNITE_BRICKS));
		r.accept(new BlockItem(LUNITE_BRICK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(LUNITE_BRICK_STAIRS));
		r.accept(new BlockItem(LUNITE_BRICK_SLAB, props), BuiltInRegistries.BLOCK.getKey(LUNITE_BRICK_SLAB));
		r.accept(new BlockItem(LUNITE_BRICK_WALL, props), BuiltInRegistries.BLOCK.getKey(LUNITE_BRICK_WALL));
		r.accept(new BlockItem(COBBLED_LUNITE, props), BuiltInRegistries.BLOCK.getKey(COBBLED_LUNITE));
		r.accept(new BlockItem(COBBLED_LUNITE_STAIRS, props), BuiltInRegistries.BLOCK.getKey(COBBLED_LUNITE_STAIRS));
		r.accept(new BlockItem(COBBLED_LUNITE_SLAB, props), BuiltInRegistries.BLOCK.getKey(COBBLED_LUNITE_SLAB));
		r.accept(new BlockItem(COBBLED_LUNITE_WALL, props), BuiltInRegistries.BLOCK.getKey(COBBLED_LUNITE_WALL));
		r.accept(new BlockItem(CHISELED_LUNITE_BRICKS, props), BuiltInRegistries.BLOCK.getKey(CHISELED_LUNITE_BRICKS));

		r.accept(new BlockItem(ROSY_TALC, props), BuiltInRegistries.BLOCK.getKey(ROSY_TALC));
		r.accept(new BlockItem(ROSY_TALC_STAIRS, props), BuiltInRegistries.BLOCK.getKey(ROSY_TALC_STAIRS));
		r.accept(new BlockItem(ROSY_TALC_SLAB, props), BuiltInRegistries.BLOCK.getKey(ROSY_TALC_SLAB));
		r.accept(new BlockItem(ROSY_TALC_WALL, props), BuiltInRegistries.BLOCK.getKey(ROSY_TALC_WALL));
		r.accept(new BlockItem(ROSY_TALC_BUTTON, props), BuiltInRegistries.BLOCK.getKey(ROSY_TALC_BUTTON));
		r.accept(new BlockItem(ROSY_TALC_PRESSURE_PLATE, props), BuiltInRegistries.BLOCK.getKey(
				ROSY_TALC_PRESSURE_PLATE));
		r.accept(new BlockItem(ROSY_TALC_BRICKS, props), BuiltInRegistries.BLOCK.getKey(ROSY_TALC_BRICKS));
		r.accept(new BlockItem(ROSY_TALC_BRICK_STAIRS, props), BuiltInRegistries.BLOCK.getKey(ROSY_TALC_BRICK_STAIRS));
		r.accept(new BlockItem(ROSY_TALC_BRICK_SLAB, props), BuiltInRegistries.BLOCK.getKey(ROSY_TALC_BRICK_SLAB));
		r.accept(new BlockItem(ROSY_TALC_BRICK_WALL, props), BuiltInRegistries.BLOCK.getKey(ROSY_TALC_BRICK_WALL));
		r.accept(new BlockItem(COBBLED_ROSY_TALC, props), BuiltInRegistries.BLOCK.getKey(COBBLED_ROSY_TALC));
		r.accept(new BlockItem(COBBLED_ROSY_TALC_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				COBBLED_ROSY_TALC_STAIRS));
		r.accept(new BlockItem(COBBLED_ROSY_TALC_SLAB, props), BuiltInRegistries.BLOCK.getKey(COBBLED_ROSY_TALC_SLAB));
		r.accept(new BlockItem(COBBLED_ROSY_TALC_WALL, props), BuiltInRegistries.BLOCK.getKey(COBBLED_ROSY_TALC_WALL));
		r.accept(new BlockItem(CHISELED_ROSY_TALC_BRICKS, props), BuiltInRegistries.BLOCK.getKey(
				CHISELED_ROSY_TALC_BRICKS));

		r.accept(new BlockItem(WHITE_PORTUGUESE_PAVEMENT, props), BuiltInRegistries.BLOCK.getKey(
				WHITE_PORTUGUESE_PAVEMENT));
		r.accept(new BlockItem(WHITE_PORTUGUESE_PAVEMENT_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				WHITE_PORTUGUESE_PAVEMENT_STAIRS));
		r.accept(new BlockItem(WHITE_PORTUGUESE_PAVEMENT_SLAB, props), BuiltInRegistries.BLOCK.getKey(
				WHITE_PORTUGUESE_PAVEMENT_SLAB));

		r.accept(new BlockItem(BLACK_PORTUGUESE_PAVEMENT, props), BuiltInRegistries.BLOCK.getKey(
				BLACK_PORTUGUESE_PAVEMENT));
		r.accept(new BlockItem(BLACK_PORTUGUESE_PAVEMENT_SLAB, props), BuiltInRegistries.BLOCK.getKey(
				BLACK_PORTUGUESE_PAVEMENT_SLAB));
		r.accept(new BlockItem(BLACK_PORTUGUESE_PAVEMENT_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				BLACK_PORTUGUESE_PAVEMENT_STAIRS));

		r.accept(new BlockItem(BLUE_PORTUGUESE_PAVEMENT, props), BuiltInRegistries.BLOCK.getKey(
				BLUE_PORTUGUESE_PAVEMENT));
		r.accept(new BlockItem(BLUE_PORTUGUESE_PAVEMENT_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				BLUE_PORTUGUESE_PAVEMENT_STAIRS));
		r.accept(new BlockItem(BLUE_PORTUGUESE_PAVEMENT_SLAB, props), BuiltInRegistries.BLOCK.getKey(
				BLUE_PORTUGUESE_PAVEMENT_SLAB));

		r.accept(new BlockItem(YELLOW_PORTUGUESE_PAVEMENT, props), BuiltInRegistries.BLOCK.getKey(
				YELLOW_PORTUGUESE_PAVEMENT));
		r.accept(new BlockItem(YELLOW_PORTUGUESE_PAVEMENT_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				YELLOW_PORTUGUESE_PAVEMENT_STAIRS));
		r.accept(new BlockItem(YELLOW_PORTUGUESE_PAVEMENT_SLAB, props), BuiltInRegistries.BLOCK.getKey(
				YELLOW_PORTUGUESE_PAVEMENT_SLAB));

		r.accept(new BlockItem(RED_PORTUGUESE_PAVEMENT, props), BuiltInRegistries.BLOCK.getKey(RED_PORTUGUESE_PAVEMENT));
		r.accept(new BlockItem(RED_PORTUGUESE_PAVEMENT_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				RED_PORTUGUESE_PAVEMENT_STAIRS));
		r.accept(new BlockItem(RED_PORTUGUESE_PAVEMENT_SLAB, props), BuiltInRegistries.BLOCK.getKey(
				RED_PORTUGUESE_PAVEMENT_SLAB));

		r.accept(new BlockItem(GREEN_PORTUGUESE_PAVEMENT, props), BuiltInRegistries.BLOCK.getKey(
				GREEN_PORTUGUESE_PAVEMENT));
		r.accept(new BlockItem(GREEN_PORTUGUESE_PAVEMENT_STAIRS, props), BuiltInRegistries.BLOCK.getKey(
				GREEN_PORTUGUESE_PAVEMENT_STAIRS));
		r.accept(new BlockItem(GREEN_PORTUGUESE_PAVEMENT_SLAB, props), BuiltInRegistries.BLOCK.getKey(
				GREEN_PORTUGUESE_PAVEMENT_SLAB));

		r.accept(new BlockItem(MANAGLASS_PANE, props), BuiltInRegistries.BLOCK.getKey(MANAGLASS_PANE));
		r.accept(new BlockItem(ALFGLASS_PANE, props), BuiltInRegistries.BLOCK.getKey(ALFGLASS_PANE));
		r.accept(new BlockItem(BIFROST_PANE, props), BuiltInRegistries.BLOCK.getKey(BIFROST_PANE));
	}

	private static <T extends Block & OptionallyColored> void registerBlockItemWithColoredVariants(
			BiConsumer<Item, ResourceLocation> r, T baseBlock, Item.Properties properties) {
		registerBlockItemWithColoredVariants(r, baseBlock, properties, "_");
	}

	private static <T extends Block & OptionallyColored> void registerBlockItemWithColoredVariants(
			BiConsumer<Item, ResourceLocation> r, T baseBlock, Item.Properties properties, String coloredBlockInfix) {
		ResourceLocation baseId = BuiltInRegistries.BLOCK.getKey(baseBlock);
		r.accept(new BlockItem(baseBlock, properties), baseId);
		ColorHelper.supportedColors().forEach(color -> {
			ResourceLocation coloredId = baseId.withPrefix(color.getSerializedName() + coloredBlockInfix);
			Block coloredBlock = ALL.get(coloredId.getPath());
			r.accept(new BlockItem(coloredBlock, properties), coloredId);
		});
	}

	private static MysticalFlowerBlock makeMysticalFlower(DyeColor color, BlockBehaviour.Properties properties) {
		return make(
				color.getSerializedName() + LibBlockNames.MYSTICAL_FLOWER_SUFFIX,
				new MysticalFlowerBlock(
						color,
						effectForFlower(color),
						MysticalFlowerBlock.STEW_DURATION,
						BotaniaBlocks::getTallMysticalFlower,
						properties
				)
		);
	}

	private static GlimmeringFlowerBlock makeGlimmeringFlower(DyeColor color, BlockBehaviour.Properties properties) {
		return make(
				color.getSerializedName() + LibBlockNames.GLIMMERING_FLOWER_SUFFIX,
				new GlimmeringFlowerBlock(
						color,
						effectForFlower(color),
						GlimmeringFlowerBlock.STEW_DURATION,
						properties
				)
		);
	}

	private static FloatingMundaneFlowerBlock makeFloatingMundaneFlower(DyeColor color, BlockBehaviour.Properties properties) {
		return make(
				color.getSerializedName() + LibBlockNames.FLOATING_FLOWER_SUFFIX,
				new FloatingMundaneFlowerBlock(
						color,
						properties
				)
		);
	}

	private static BuriedPetalBlock makeBuriedPetal(DyeColor color, BlockBehaviour.Properties properties) {
		return make(
				color.getSerializedName() + LibBlockNames.BURIED_PETAL_SUFFIX,
				new BuriedPetalBlock(
						color,
						BotaniaBlocks::getTallMysticalFlower,
						properties
				)
		);
	}

	private static PetalBlock makePetalBlock(DyeColor color, BlockBehaviour.Properties properties) {
		return make(
				color.getSerializedName() + LibBlockNames.PETAL_BLOCK_SUFFIX,
				new PetalBlock(color, properties.mapColor(color))
		);
	}

	private static ShimmeringMushroomBlock makeShimmeringMushroom(DyeColor color, BlockBehaviour.Properties properties) {
		return make(
				color.getSerializedName() + LibBlockNames.SHIMMERING_MUSHROOM_SUFFIX,
				new ShimmeringMushroomBlock(color, properties)
		);
	}

	private static <T extends Block> T make(String name, T block) {
		var old = ALL.put(name, block);
		if (old != null) {
			throw new IllegalArgumentException("Typo? Duplicate name: " + name);
		}
		return block;
	}

	private static <T extends Block & OptionallyColored> T makeBlockWithColoredVariants(String baseName,
			Function<@Nullable DyeColor, T> blockFactory) {
		return makeBlockWithColoredVariants(baseName, blockFactory, "_");
	}

	private static <T extends Block & OptionallyColored> T makeBlockWithColoredVariants(String baseName, Function<@Nullable DyeColor, T> blockFactory,
			String coloredBlockInfix) {
		T baseBlock = make(baseName, blockFactory.apply(null));
		ColorHelper.supportedColors().forEach(
				color -> make(color.getSerializedName() + coloredBlockInfix + baseName, blockFactory.apply(color)));
		return baseBlock;
	}

	private static TallMysticalFlowerBlock makeTallMysticalFlower(DyeColor color, BlockBehaviour.Properties properties) {
		return make(
				color.getSerializedName() + LibBlockNames.TALL_MYSTICAL_FLOWER_SUFFIX,
				new TallMysticalFlowerBlock(color, properties)
		);
	}

	private static FlowerPotBlock makePottedMundaneFlower(MysticalFlowerBlock flower) {
		return make(
				LibBlockNames.POTTED_PREFIX + flower.color.getSerializedName() + LibBlockNames.MYSTICAL_FLOWER_SUFFIX,
				flowerPot(flower, 0)
		);
	}

	private static FlowerPotBlock makePottedGlimmeringFlower(GlimmeringFlowerBlock flower) {
		return make(
				LibBlockNames.POTTED_PREFIX + flower.color.getSerializedName() + LibBlockNames.GLIMMERING_FLOWER_SUFFIX,
				flowerPot(flower, 15)
		);
	}

	private static FlowerPotBlock makePottedShimmeringMushroom(ShimmeringMushroomBlock mushroom) {
		return make(
				LibBlockNames.POTTED_PREFIX + mushroom.color.getSerializedName() + LibBlockNames.SHIMMERING_MUSHROOM_SUFFIX,
				flowerPot(mushroom, 3)
		);
	}

	private static PetalApothecaryBlock makePetalApothecary(String name, SoundType soundType, MapColor mapColor) {
		return make(name, new PetalApothecaryBlock(BlockBehaviour.Properties.of()
				.strength(3.5F)
				.instrument(NoteBlockInstrument.BASEDRUM)
				.requiresCorrectToolForDrops()
				.lightLevel(state -> state.getValue(PetalApothecaryBlock.FLUID) == PetalApothecary.State.LAVA ? 15 : 0)
				.sound(soundType)
				.mapColor(mapColor))
		);
	}

	public static void addDispenserBehaviours() {
		DispenserBlock.registerBehavior(BotaniaItems.WAND_OF_THE_FOREST, new WandBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.PHANTOM_INK, new PhantomInkBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.WAND_OF_THE_ELVEN_FOREST, new WandBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.FLORAL_OBEDIENCE_STICK, new StickBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.POOL_MINECART, new ManaPoolMinecartBehavior());
		DispenserBlock.registerBehavior(FEL_PUMPKIN, new FelPumpkinBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.SPARK, new ManaSparkBehavior());
		DispenserBlock.registerBehavior(
				GAIA_HEAD, new OptionalDispenseItemBehavior() {
					@Override
					protected ItemStack execute(BlockSource blockSource, ItemStack item) {
						setSuccess(ArmorItem.dispenseArmor(blockSource, item));
						return item;
					}
				});

		DispenseItemBehavior behavior = new CorporeaSparkBehavior();
		DispenserBlock.registerBehavior(BotaniaItems.CORPOREA_SPARK, behavior);
		DispenserBlock.registerBehavior(BotaniaItems.MASTER_CORPOREA_SPARK, behavior);
		DispenserBlock.registerBehavior(BotaniaItems.CREATIVE_CORPOREA_SPARK, behavior);
		DispenserBlock.registerBehavior(BotaniaItems.ENDER_AIR_BOTTLE, new ProjectileDispenseBehavior(BotaniaItems.ENDER_AIR_BOTTLE));
		behavior = DispenserBlock.DISPENSER_REGISTRY.get(Items.GLASS_BOTTLE);
		DispenserBlock.registerBehavior(Items.GLASS_BOTTLE, new EnderAirBottlingBehavior(behavior));

		behavior = new GrassSeedsBehavior();
		Item[] seedItems = {
				BotaniaItems.PASTURE_SEEDS,
				BotaniaItems.BOREAL_SEEDS,
				BotaniaItems.INFESTATION_SPORES,
				BotaniaItems.DRY_SEEDS,
				BotaniaItems.GOLDEN_SEEDS,
				BotaniaItems.VIVID_SEEDS,
				BotaniaItems.SCORCHED_SEEDS,
				BotaniaItems.INFUSED_SEEDS,
				BotaniaItems.MUTATED_SEEDS,
		};
		for (Item seed : seedItems) {
			DispenserBlock.registerBehavior(seed, behavior);
		}

		DispenserBlock.registerBehavior(BotaniaItems.MANASTEEL_SHEARS, new ShearsDispenseItemBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.ELEMENTIUM_SHEARS, new ShearsDispenseItemBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.VINE_BALL, new ProjectileDispenseBehavior(BotaniaItems.VINE_BALL));

		SeedBehaviors.init();
	}

	public static void addAxeStripping() {
		XplatAbstractions xplat = XplatAbstractions.INSTANCE;
		xplat.addAxeStripping(LIVINGWOOD_LOG, STRIPPED_LIVINGWOOD_LOG);
		xplat.addAxeStripping(GLIMMERING_LIVINGWOOD_LOG, STRIPPED_GLIMMERING_LIVINGWOOD_LOG);
		xplat.addAxeStripping(LIVINGWOOD, STRIPPED_LIVINGWOOD);
		xplat.addAxeStripping(GLIMMERING_LIVINGWOOD, STRIPPED_GLIMMERING_LIVINGWOOD);
		xplat.addAxeStripping(DREAMWOOD_LOG, STRIPPED_DREAMWOOD_LOG);
		xplat.addAxeStripping(GLIMMERING_DREAMWOOD_LOG, STRIPPED_GLIMMERING_DREAMWOOD_LOG);
		xplat.addAxeStripping(DREAMWOOD, STRIPPED_DREAMWOOD);
		xplat.addAxeStripping(GLIMMERING_DREAMWOOD, STRIPPED_GLIMMERING_DREAMWOOD);

		xplat.addAxeStripping(LIVINGWOOD_STAIRS, STRIPPED_LIVINGWOOD_STAIRS);
		xplat.addAxeStripping(LIVINGWOOD_SLAB, STRIPPED_LIVINGWOOD_SLAB);
		xplat.addAxeStripping(LIVINGWOOD_WALL, STRIPPED_LIVINGWOOD_WALL);
		xplat.addAxeStripping(DREAMWOOD_STAIRS, STRIPPED_DREAMWOOD_STAIRS);
		xplat.addAxeStripping(DREAMWOOD_SLAB, STRIPPED_DREAMWOOD_SLAB);
		xplat.addAxeStripping(DREAMWOOD_WALL, STRIPPED_DREAMWOOD_WALL);
	}

	public static Block getMysticalFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_MYSTICAL_FLOWER;
			case ORANGE -> ORANGE_MYSTICAL_FLOWER;
			case MAGENTA -> MAGENTA_MYSTICAL_FLOWER;
			case LIGHT_BLUE -> LIGHT_BLUE_MYSTICAL_FLOWER;
			case YELLOW -> YELLOW_MYSTICAL_FLOWER;
			case LIME -> LIME_MYSTICAL_FLOWER;
			case PINK -> PINK_MYSTICAL_FLOWER;
			case GRAY -> GRAY_MYSTICAL_FLOWER;
			case LIGHT_GRAY -> LIGHT_GRAY_MYSTICAL_FLOWER;
			case CYAN -> CYAN_MYSTICAL_FLOWER;
			case PURPLE -> PURPLE_MYSTICAL_FLOWER;
			case BLUE -> BLUE_MYSTICAL_FLOWER;
			case BROWN -> BROWN_MYSTICAL_FLOWER;
			case GREEN -> GREEN_MYSTICAL_FLOWER;
			case RED -> RED_MYSTICAL_FLOWER;
			case BLACK -> BLACK_MYSTICAL_FLOWER;
		};
	}

	public static Block getShimmeringMushroom(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_SHIMMERING_MUSHROOM;
			case ORANGE -> ORANGE_SHIMMERING_MUSHROOM;
			case MAGENTA -> MAGENTA_SHIMMERING_MUSHROOM;
			case LIGHT_BLUE -> LIGHT_BLUE_SHIMMERING_MUSHROOM;
			case YELLOW -> YELLOW_SHIMMERING_MUSHROOM;
			case LIME -> LIME_SHIMMERING_MUSHROOM;
			case PINK -> PINK_SHIMMERING_MUSHROOM;
			case GRAY -> GRAY_SHIMMERING_MUSHROOM;
			case LIGHT_GRAY -> LIGHT_GRAY_SHIMMERING_MUSHROOM;
			case CYAN -> CYAN_SHIMMERING_MUSHROOM;
			case PURPLE -> PURPLE_SHIMMERING_MUSHROOM;
			case BLUE -> BLUE_SHIMMERING_MUSHROOM;
			case BROWN -> BROWN_SHIMMERING_MUSHROOM;
			case GREEN -> GREEN_SHIMMERING_MUSHROOM;
			case RED -> RED_SHIMMERING_MUSHROOM;
			case BLACK -> BLACK_SHIMMERING_MUSHROOM;
		};
	}

	public static Block getBuriedPetal(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_BURIED_PETAL;
			case ORANGE -> ORANGE_BURIED_PETAL;
			case MAGENTA -> MAGENTA_BURIED_PETAL;
			case LIGHT_BLUE -> LIGHT_BLUE_BURIED_PETAL;
			case YELLOW -> YELLOW_BURIED_PETAL;
			case LIME -> LIME_BURIED_PETAL;
			case PINK -> PINK_BURIED_PETAL;
			case GRAY -> GRAY_BURIED_PETAL;
			case LIGHT_GRAY -> LIGHT_GRAY_BURIED_PETAL;
			case CYAN -> CYAN_BURIED_PETAL;
			case PURPLE -> PURPLE_BURIED_PETAL;
			case BLUE -> BLUE_BURIED_PETAL;
			case BROWN -> BROWN_BURIED_PETAL;
			case GREEN -> GREEN_BURIED_PETAL;
			case RED -> RED_BURIED_PETAL;
			case BLACK -> BLACK_BURIED_PETAL;
		};
	}

	public static Block getGlimmeringFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_GLIMMERING_FLOWER;
			case ORANGE -> ORANGE_GLIMMERING_FLOWER;
			case MAGENTA -> MAGENTA_GLIMMERING_FLOWER;
			case LIGHT_BLUE -> LIGHT_BLUE_GLIMMERING_FLOWER;
			case YELLOW -> YELLOW_GLIMMERING_FLOWER;
			case LIME -> LIME_GLIMMERING_FLOWER;
			case PINK -> PINK_GLIMMERING_FLOWER;
			case GRAY -> GRAY_GLIMMERING_FLOWER;
			case LIGHT_GRAY -> LIGHT_GRAY_GLIMMERING_FLOWER;
			case CYAN -> CYAN_GLIMMERING_FLOWER;
			case PURPLE -> PURPLE_GLIMMERING_FLOWER;
			case BLUE -> BLUE_GLIMMERING_FLOWER;
			case BROWN -> BROWN_GLIMMERING_FLOWER;
			case GREEN -> GREEN_GLIMMERING_FLOWER;
			case RED -> RED_GLIMMERING_FLOWER;
			case BLACK -> BLACK_GLIMMERING_FLOWER;
		};
	}

	public static Block getFloatingFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_FLOATING_FLOWER;
			case ORANGE -> ORANGE_FLOATING_FLOWER;
			case MAGENTA -> MAGENTA_FLOATING_FLOWER;
			case LIGHT_BLUE -> LIGHT_BLUE_FLOATING_FLOWER;
			case YELLOW -> YELLOW_FLOATING_FLOWER;
			case LIME -> LIME_FLOATING_FLOWER;
			case PINK -> PINK_FLOATING_FLOWER;
			case GRAY -> GRAY_FLOATING_FLOWER;
			case LIGHT_GRAY -> LIGHT_GRAY_FLOATING_FLOWER;
			case CYAN -> CYAN_FLOATING_FLOWER;
			case PURPLE -> PURPLE_FLOATING_FLOWER;
			case BLUE -> BLUE_FLOATING_FLOWER;
			case BROWN -> BROWN_FLOATING_FLOWER;
			case GREEN -> GREEN_FLOATING_FLOWER;
			case RED -> RED_FLOATING_FLOWER;
			case BLACK -> BLACK_FLOATING_FLOWER;
		};
	}

	@Nullable
	public static Block getTallMysticalFlower(TallFlowerGrower grower) {
		return grower instanceof Colored colored ? getTallMysticalFlower(colored.getColor()) : null;
	}

	public static Block getTallMysticalFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_TALL_MYSTICAL_FLOWER;
			case ORANGE -> ORANGE_TALL_MYSTICAL_FLOWER;
			case MAGENTA -> MAGENTA_TALL_MYSTICAL_FLOWER;
			case LIGHT_BLUE -> LIGHT_BLUE_TALL_MYSTICAL_FLOWER;
			case YELLOW -> YELLOW_TALL_MYSTICAL_FLOWER;
			case LIME -> LIME_TALL_MYSTICAL_FLOWER;
			case PINK -> PINK_TALL_MYSTICAL_FLOWER;
			case GRAY -> GRAY_TALL_MYSTICAL_FLOWER;
			case LIGHT_GRAY -> LIGHT_GRAY_TALL_MYSTICAL_FLOWER;
			case CYAN -> CYAN_TALL_MYSTICAL_FLOWER;
			case PURPLE -> PURPLE_TALL_MYSTICAL_FLOWER;
			case BLUE -> BLUE_TALL_MYSTICAL_FLOWER;
			case BROWN -> BROWN_TALL_MYSTICAL_FLOWER;
			case GREEN -> GREEN_TALL_MYSTICAL_FLOWER;
			case RED -> RED_TALL_MYSTICAL_FLOWER;
			case BLACK -> BLACK_TALL_MYSTICAL_FLOWER;
		};
	}

	public static Block getPetalBlock(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_PETAL_BLOCK;
			case ORANGE -> ORANGE_PETAL_BLOCK;
			case MAGENTA -> MAGENTA_PETAL_BLOCK;
			case LIGHT_BLUE -> LIGHT_BLUE_PETAL_BLOCK;
			case YELLOW -> YELLOW_PETAL_BLOCK;
			case LIME -> LIME_PETAL_BLOCK;
			case PINK -> PINK_PETAL_BLOCK;
			case GRAY -> GRAY_PETAL_BLOCK;
			case LIGHT_GRAY -> LIGHT_GRAY_PETAL_BLOCK;
			case CYAN -> CYAN_PETAL_BLOCK;
			case PURPLE -> PURPLE_PETAL_BLOCK;
			case BLUE -> BLUE_PETAL_BLOCK;
			case BROWN -> BROWN_PETAL_BLOCK;
			case GREEN -> GREEN_PETAL_BLOCK;
			case RED -> RED_PETAL_BLOCK;
			case BLACK -> BLACK_PETAL_BLOCK;
		};
	}

	public static Block getPottedFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> POTTED_WHITE_MYSTICAL_FLOWER;
			case ORANGE -> POTTED_ORANGE_MYSTICAL_FLOWER;
			case MAGENTA -> POTTED_MAGENTA_MYSTICAL_FLOWER;
			case LIGHT_BLUE -> POTTED_LIGHT_BLUE_MYSTICAL_FLOWER;
			case YELLOW -> POTTED_YELLOW_MYSTICAL_FLOWER;
			case LIME -> POTTED_LIME_MYSTICAL_FLOWER;
			case PINK -> POTTED_PINK_MYSTICAL_FLOWER;
			case GRAY -> POTTED_GRAY_MYSTICAL_FLOWER;
			case LIGHT_GRAY -> POTTED_LIGHT_GRAY_MYSTICAL_FLOWER;
			case CYAN -> POTTED_CYAN_MYSTICAL_FLOWER;
			case PURPLE -> POTTED_PURPLE_MYSTICAL_FLOWER;
			case BLUE -> POTTED_BLUE_MYSTICAL_FLOWER;
			case BROWN -> POTTED_BROWN_MYSTICAL_FLOWER;
			case GREEN -> POTTED_GREEN_MYSTICAL_FLOWER;
			case RED -> POTTED_RED_MYSTICAL_FLOWER;
			case BLACK -> POTTED_BLACK_MYSTICAL_FLOWER;
		};
	}

	public static Block getPottedShinyFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> POTTED_WHITE_GLIMMERING_FLOWER;
			case ORANGE -> POTTED_ORANGE_GLIMMERING_FLOWER;
			case MAGENTA -> POTTED_MAGENTA_GLIMMERING_FLOWER;
			case LIGHT_BLUE -> POTTED_LIGHT_BLUE_GLIMMERING_FLOWER;
			case YELLOW -> POTTED_YELLOW_GLIMMERING_FLOWER;
			case LIME -> POTTED_LIME_GLIMMERING_FLOWER;
			case PINK -> POTTED_PINK_GLIMMERING_FLOWER;
			case GRAY -> POTTED_GRAY_GLIMMERING_FLOWER;
			case LIGHT_GRAY -> POTTED_LIGHT_GRAY_GLIMMERING_FLOWER;
			case CYAN -> POTTED_CYAN_GLIMMERING_FLOWER;
			case PURPLE -> POTTED_PURPLE_GLIMMERING_FLOWER;
			case BLUE -> POTTED_BLUE_GLIMMERING_FLOWER;
			case BROWN -> POTTED_BROWN_GLIMMERING_FLOWER;
			case GREEN -> POTTED_GREEN_GLIMMERING_FLOWER;
			case RED -> POTTED_RED_GLIMMERING_FLOWER;
			case BLACK -> POTTED_BLACK_GLIMMERING_FLOWER;
		};
	}

	public static Block getPottedMushroom(DyeColor color) {
		return switch (color) {
			case WHITE -> POTTED_WHITE_SHIMMERING_MUSHROOM;
			case ORANGE -> POTTED_ORANGE_SHIMMERING_MUSHROOM;
			case MAGENTA -> POTTED_MAGENTA_SHIMMERING_MUSHROOM;
			case LIGHT_BLUE -> POTTED_LIGHT_BLUE_SHIMMERING_MUSHROOM;
			case YELLOW -> POTTED_YELLOW_SHIMMERING_MUSHROOM;
			case LIME -> POTTED_LIME_SHIMMERING_MUSHROOM;
			case PINK -> POTTED_PINK_SHIMMERING_MUSHROOM;
			case GRAY -> POTTED_GRAY_SHIMMERING_MUSHROOM;
			case LIGHT_GRAY -> POTTED_LIGHT_GRAY_SHIMMERING_MUSHROOM;
			case CYAN -> POTTED_CYAN_SHIMMERING_MUSHROOM;
			case PURPLE -> POTTED_PURPLE_SHIMMERING_MUSHROOM;
			case BLUE -> POTTED_BLUE_SHIMMERING_MUSHROOM;
			case BROWN -> POTTED_BROWN_SHIMMERING_MUSHROOM;
			case GREEN -> POTTED_GREEN_SHIMMERING_MUSHROOM;
			case RED -> POTTED_RED_SHIMMERING_MUSHROOM;
			case BLACK -> POTTED_BLACK_SHIMMERING_MUSHROOM;
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

	/**
	 * Applies naming conventions to find a block of the same type in the specified color variant.
	 * The ID of dyed variants is expected to use the dye name as prefix of the undyed block's ID.
	 */
	public static <T extends Block & OptionallyColored> T findOptionallyDyedBlock(T referenceBlock, @Nullable DyeColor targetColor) {
		return findOptionallyDyedBlock(referenceBlock, targetColor, "_");
	}

	/**
	 * Applies naming conventions to find a block of the same type in the specified color variant.
	 * The ID of dyed variants is expected to use the dye name as prefix of the undyed block's ID.
	 * The ID part between the dye name and the base block ID (the color block infix) is often "_", but can be longer,
	 * e.g. it would be "_stained_" for colored glass blocks, if those supported this operation.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Block & OptionallyColored> T findOptionallyDyedBlock(T referenceBlock,
			@Nullable DyeColor targetColor, String coloredBlockInfix) {
		@Nullable
		DyeColor referenceColor = referenceBlock.getOptionalColor().orElse(null);
		if (targetColor == referenceColor) {
			// already is the expected color
			return referenceBlock;
		}
		// at most one of reference color and target color is empty

		ResourceLocation referenceId = BuiltInRegistries.BLOCK.getKey(referenceBlock);
		ResourceLocation targetId = getTargetId(referenceId, referenceColor, targetColor, coloredBlockInfix);

		Block targetBlock = BuiltInRegistries.BLOCK.get(targetId);
		if (!referenceBlock.getClass().isInstance(targetBlock)) {
			throw new IllegalArgumentException(
					"No target block for %s in %s (looked for %s, found %s)".formatted(referenceBlock, targetColor, targetId, targetBlock));
		}

		return (T) targetBlock;
	}

	private static String getUndyedReferencePath(ResourceLocation referenceId, @Nullable DyeColor referenceColor, String coloredBlockInfix) {
		if (referenceColor == null) {
			return referenceId.getPath();
		}
		String referenceColorPrefix = referenceColor.getSerializedName() + coloredBlockInfix;
		if (!referenceId.getPath().startsWith(referenceColorPrefix)) {
			throw new IllegalArgumentException(
					"Block ID %s should start with color prefix %s".formatted(referenceId, referenceColorPrefix));
		}
		return referenceId.getPath().substring(referenceColorPrefix.length());
	}

	private static ResourceLocation getTargetId(ResourceLocation referenceId,
			@Nullable DyeColor referenceColor, @Nullable DyeColor targetColor, String coloredBlockInfix) {
		if (targetColor == null) {
			return referenceId.withPath(getUndyedReferencePath(referenceId, referenceColor, coloredBlockInfix));
		}
		if (referenceColor == null) {
			return referenceId.withPrefix(targetColor.getSerializedName() + coloredBlockInfix);
		}
		return referenceId.withPath(targetColor.getSerializedName() + coloredBlockInfix +
				getUndyedReferencePath(referenceId, referenceColor, coloredBlockInfix));
	}

	/**
	 * Applies naming conventions to find a block of the same type in the specified color variant. The block ID is
	 * expected to have the same format across all dye variants, with the dye name appearing once the ID.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Block & Colored> T findDyedBlock(T referenceBlock, DyeColor targetColor) {
		DyeColor referenceColor = referenceBlock.getColor();
		if (referenceColor == targetColor) {
			return referenceBlock;
		}

		ResourceLocation referenceId = BuiltInRegistries.BLOCK.getKey(referenceBlock);
		ResourceLocation targetId = referenceId.withPath(path -> path.replaceFirst(
				"(?<=_|\\b)" + referenceColor.getSerializedName() + "(?=_|\\b)", targetColor.getSerializedName()));

		Block targetBlock = BuiltInRegistries.BLOCK.get(targetId);
		if (!referenceBlock.getClass().isInstance(targetBlock)) {
			throw new IllegalArgumentException(
					"No target block for %s in %s (found %s)".formatted(referenceBlock, targetColor, targetBlock));
		}

		return (T) targetBlock;
	}

	@FunctionalInterface
	public interface BCapConsumer<T> {
		void accept(Function<BlockState, T> factory, Block... blocks);
	}

	@FunctionalInterface
	public interface BCapFallbackConsumer<T> {
		void accept(Function<BlockState, @Nullable T> factory);
	}
}
