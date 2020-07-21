/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.helper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;

public final class ColorHelper {
	public static final BiMap<DyeColor, IRegistryDelegate<Block>> STAINED_GLASS_MAP = HashBiMap.create();
	public static final BiMap<DyeColor, IRegistryDelegate<Block>> STAINED_GLASS_PANE_MAP = HashBiMap.create();
	public static final BiMap<DyeColor, IRegistryDelegate<Block>> TERRACOTTA_MAP = HashBiMap.create();
	public static final BiMap<DyeColor, IRegistryDelegate<Block>> GLAZED_TERRACOTTA_MAP = HashBiMap.create();
	public static final BiMap<DyeColor, IRegistryDelegate<Block>> WOOL_MAP = HashBiMap.create();
	public static final BiMap<DyeColor, IRegistryDelegate<Block>> CARPET_MAP = HashBiMap.create();
	public static final BiMap<DyeColor, IRegistryDelegate<Block>> CONCRETE_MAP = HashBiMap.create();
	public static final BiMap<DyeColor, IRegistryDelegate<Block>> CONCRETE_POWDER_MAP = HashBiMap.create();

	public static void init() {
		STAINED_GLASS_MAP.put(DyeColor.WHITE, Blocks.WHITE_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.LIME, Blocks.LIME_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.PINK, Blocks.PINK_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.GRAY, Blocks.GRAY_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.CYAN, Blocks.CYAN_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.BLUE, Blocks.BLUE_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.BROWN, Blocks.BROWN_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.GREEN, Blocks.GREEN_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.RED, Blocks.RED_STAINED_GLASS.delegate);
		STAINED_GLASS_MAP.put(DyeColor.BLACK, Blocks.BLACK_STAINED_GLASS.delegate);

		STAINED_GLASS_PANE_MAP.put(DyeColor.WHITE, Blocks.WHITE_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.LIME, Blocks.LIME_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.PINK, Blocks.PINK_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.GRAY, Blocks.GRAY_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.CYAN, Blocks.CYAN_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.BLUE, Blocks.BLUE_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.BROWN, Blocks.BROWN_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.GREEN, Blocks.GREEN_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.RED, Blocks.RED_STAINED_GLASS_PANE.delegate);
		STAINED_GLASS_PANE_MAP.put(DyeColor.BLACK, Blocks.BLACK_STAINED_GLASS_PANE.delegate);

		TERRACOTTA_MAP.put(DyeColor.WHITE, Blocks.WHITE_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.LIME, Blocks.LIME_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.PINK, Blocks.PINK_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.GRAY, Blocks.GRAY_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.CYAN, Blocks.CYAN_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.BLUE, Blocks.BLUE_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.BROWN, Blocks.BROWN_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.GREEN, Blocks.GREEN_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.RED, Blocks.RED_TERRACOTTA.delegate);
		TERRACOTTA_MAP.put(DyeColor.BLACK, Blocks.BLACK_TERRACOTTA.delegate);

		GLAZED_TERRACOTTA_MAP.put(DyeColor.WHITE, Blocks.WHITE_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.LIME, Blocks.LIME_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.PINK, Blocks.PINK_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.GRAY, Blocks.GRAY_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.CYAN, Blocks.CYAN_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.BLUE, Blocks.BLUE_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.BROWN, Blocks.BROWN_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.GREEN, Blocks.GREEN_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.RED, Blocks.RED_GLAZED_TERRACOTTA.delegate);
		GLAZED_TERRACOTTA_MAP.put(DyeColor.BLACK, Blocks.BLACK_GLAZED_TERRACOTTA.delegate);

		WOOL_MAP.put(DyeColor.WHITE, Blocks.WHITE_WOOL.delegate);
		WOOL_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL.delegate);
		WOOL_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL.delegate);
		WOOL_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL.delegate);
		WOOL_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL.delegate);
		WOOL_MAP.put(DyeColor.LIME, Blocks.LIME_WOOL.delegate);
		WOOL_MAP.put(DyeColor.PINK, Blocks.PINK_WOOL.delegate);
		WOOL_MAP.put(DyeColor.GRAY, Blocks.GRAY_WOOL.delegate);
		WOOL_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL.delegate);
		WOOL_MAP.put(DyeColor.CYAN, Blocks.CYAN_WOOL.delegate);
		WOOL_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL.delegate);
		WOOL_MAP.put(DyeColor.BLUE, Blocks.BLUE_WOOL.delegate);
		WOOL_MAP.put(DyeColor.BROWN, Blocks.BROWN_WOOL.delegate);
		WOOL_MAP.put(DyeColor.GREEN, Blocks.GREEN_WOOL.delegate);
		WOOL_MAP.put(DyeColor.RED, Blocks.RED_WOOL.delegate);
		WOOL_MAP.put(DyeColor.BLACK, Blocks.BLACK_WOOL.delegate);

		CARPET_MAP.put(DyeColor.WHITE, Blocks.WHITE_CARPET.delegate);
		CARPET_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_CARPET.delegate);
		CARPET_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_CARPET.delegate);
		CARPET_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CARPET.delegate);
		CARPET_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_CARPET.delegate);
		CARPET_MAP.put(DyeColor.LIME, Blocks.LIME_CARPET.delegate);
		CARPET_MAP.put(DyeColor.PINK, Blocks.PINK_CARPET.delegate);
		CARPET_MAP.put(DyeColor.GRAY, Blocks.GRAY_CARPET.delegate);
		CARPET_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CARPET.delegate);
		CARPET_MAP.put(DyeColor.CYAN, Blocks.CYAN_CARPET.delegate);
		CARPET_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_CARPET.delegate);
		CARPET_MAP.put(DyeColor.BLUE, Blocks.BLUE_CARPET.delegate);
		CARPET_MAP.put(DyeColor.BROWN, Blocks.BROWN_CARPET.delegate);
		CARPET_MAP.put(DyeColor.GREEN, Blocks.GREEN_CARPET.delegate);
		CARPET_MAP.put(DyeColor.RED, Blocks.RED_CARPET.delegate);
		CARPET_MAP.put(DyeColor.BLACK, Blocks.BLACK_CARPET.delegate);

		CONCRETE_MAP.put(DyeColor.WHITE, Blocks.WHITE_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.LIME, Blocks.LIME_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.PINK, Blocks.PINK_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.GRAY, Blocks.GRAY_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.CYAN, Blocks.CYAN_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.BLUE, Blocks.BLUE_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.BROWN, Blocks.BROWN_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.GREEN, Blocks.GREEN_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.RED, Blocks.RED_CONCRETE.delegate);
		CONCRETE_MAP.put(DyeColor.BLACK, Blocks.BLACK_CONCRETE.delegate);

		CONCRETE_POWDER_MAP.put(DyeColor.WHITE, Blocks.WHITE_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.ORANGE, Blocks.ORANGE_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.MAGENTA, Blocks.MAGENTA_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.YELLOW, Blocks.YELLOW_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.LIME, Blocks.LIME_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.PINK, Blocks.PINK_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.GRAY, Blocks.GRAY_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.CYAN, Blocks.CYAN_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.PURPLE, Blocks.PURPLE_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.BLUE, Blocks.BLUE_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.BROWN, Blocks.BROWN_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.GREEN, Blocks.GREEN_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.RED, Blocks.RED_CONCRETE_POWDER.delegate);
		CONCRETE_POWDER_MAP.put(DyeColor.BLACK, Blocks.BLACK_CONCRETE_POWDER.delegate);
	}

	private ColorHelper() {}
}
