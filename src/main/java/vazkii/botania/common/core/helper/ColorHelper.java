/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.helper;

import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vazkii.botania.mixin.AccessorDyeColor;

import javax.annotation.Nullable;

import java.util.Locale;
import java.util.function.Function;

public final class ColorHelper {
	public static final Function<DyeColor, Block> STAINED_GLASS_MAP = color -> Registry.BLOCK.getOrEmpty(new Identifier(color.asString() + "_stained_glass")).get();
	public static final Function<DyeColor, Block> STAINED_GLASS_PANE_MAP = color -> Registry.BLOCK.getOrEmpty(new Identifier(color.asString() + "_stained_glass_pane")).get();
	public static final Function<DyeColor, Block> TERRACOTTA_MAP = color -> Registry.BLOCK.getOrEmpty(new Identifier(color.asString() + "_terracotta")).get();
	public static final Function<DyeColor, Block> GLAZED_TERRACOTTA_MAP = color -> Registry.BLOCK.getOrEmpty(new Identifier(color.asString() + "_glazed_terracotta")).get();
	public static final Function<DyeColor, Block> WOOL_MAP = color -> Registry.BLOCK.getOrEmpty(new Identifier(color.asString() + "_wool")).get();
	public static final Function<DyeColor, Block> CARPET_MAP = color -> Registry.BLOCK.getOrEmpty(new Identifier(color.asString() + "_carpet")).get();
	public static final Function<DyeColor, Block> CONCRETE_MAP = color -> Registry.BLOCK.getOrEmpty(new Identifier(color.asString() + "_concrete")).get();
	public static final Function<DyeColor, Block> CONCRETE_POWDER_MAP = color -> Registry.BLOCK.getOrEmpty(new Identifier(color.asString() + "_concrete_powder")).get();

	@Nullable
	public static DyeColor getWoolColor(Block b) {
		Identifier name = Registry.BLOCK.getId(b);
		if ("minecraft".equals(name.getNamespace()) && name.getPath().endsWith("_wool")) {
			String color = name.getPath().substring(0, name.getPath().length() - "_wool".length());
			return DyeColor.valueOf(color.toUpperCase(Locale.ROOT));
		}
		return null;
	}

	public static boolean isWool(Block b) {
		return getWoolColor(b) != null;
	}

	public static int getColorValue(DyeColor color) {
		return ((AccessorDyeColor) (Object) color).getColor();
	}

	private ColorHelper() {}
}
