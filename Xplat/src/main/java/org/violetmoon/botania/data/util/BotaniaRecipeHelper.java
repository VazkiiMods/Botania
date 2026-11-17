/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.data.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import org.jetbrains.annotations.Nullable;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class BotaniaRecipeHelper {
	public static ResourceLocation deriveRecipeId(RecipeType<?> type, ItemLike output) {
		return deriveRecipeId(type, null, output, null);
	}

	public static ResourceLocation deriveRecipeId(RecipeType<?> type, String itemId) {
		return deriveRecipeId(type, null, itemId, null);
	}

	public static ResourceLocation deriveRecipeId(RecipeType<?> type, ItemLike output, String suffix) {
		return deriveRecipeId(type, null, output, suffix);
	}

	public static ResourceLocation deriveRecipeId(RecipeType<?> type, StringRepresentable category, ItemLike output) {
		return deriveRecipeId(type, category, output, null);
	}

	public static ResourceLocation deriveRecipeId(RecipeType<?> type, @Nullable StringRepresentable category, ItemLike output, @Nullable String suffix) {
		return deriveRecipeId(type, category, BuiltInRegistries.ITEM.getKey(output.asItem()).getPath(), suffix);
	}

	public static ResourceLocation deriveRecipeId(RecipeType<?> type, @Nullable StringRepresentable category, String itemId, @Nullable String suffix) {
		StringBuilder sb = new StringBuilder(type.toString()).append('/');
		if (category != null) {
			sb.append(category.getSerializedName()).append('/');
		}
		sb.append(itemId);
		if (suffix != null) {
			sb.append(suffix);
		}
		return botaniaRL(sb.toString());
	}
}
