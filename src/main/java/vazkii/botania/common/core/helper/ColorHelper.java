/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.helper;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

import java.util.Locale;
import java.util.function.Function;

public final class ColorHelper {
	public static final Function<DyeColor, Block> STAINED_GLASS_MAP = color -> Registry.BLOCK.getOptional(new ResourceLocation(color.getSerializedName() + "_stained_glass")).get();
	public static final Function<DyeColor, Block> STAINED_GLASS_PANE_MAP = color -> Registry.BLOCK.getOptional(new ResourceLocation(color.getSerializedName() + "_stained_glass_pane")).get();
	public static final Function<DyeColor, Block> TERRACOTTA_MAP = color -> Registry.BLOCK.getOptional(new ResourceLocation(color.getSerializedName() + "_terracotta")).get();
	public static final Function<DyeColor, Block> GLAZED_TERRACOTTA_MAP = color -> Registry.BLOCK.getOptional(new ResourceLocation(color.getSerializedName() + "_glazed_terracotta")).get();
	public static final Function<DyeColor, Block> WOOL_MAP = color -> Registry.BLOCK.getOptional(new ResourceLocation(color.getSerializedName() + "_wool")).get();
	public static final Function<DyeColor, Block> CARPET_MAP = color -> Registry.BLOCK.getOptional(new ResourceLocation(color.getSerializedName() + "_carpet")).get();
	public static final Function<DyeColor, Block> CONCRETE_MAP = color -> Registry.BLOCK.getOptional(new ResourceLocation(color.getSerializedName() + "_concrete")).get();
	public static final Function<DyeColor, Block> CONCRETE_POWDER_MAP = color -> Registry.BLOCK.getOptional(new ResourceLocation(color.getSerializedName() + "_concrete_powder")).get();

	@Nullable
	public static DyeColor getWoolColor(Block b) {
		ResourceLocation name = Registry.BLOCK.getKey(b);
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
		float[] colors = color.getTextureDiffuseColors();
		int r = (int) (colors[0] * 255.0F);
		int g = (int) (colors[1] * 255.0F);
		int b = (int) (colors[2] * 255.0F);
		return (r << 16) | (g << 8) | b;
	}

	private ColorHelper() {}
}
