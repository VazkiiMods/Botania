/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;

import vazkii.botania.common.Botania;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.patchouli.api.IVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PatchouliUtils {
	private static boolean crafttweakerInfoNote = false;

	/**
	 * Gets a recipe of a specified type and ID, and replaces the namespace
	 * with {@code crafttweaker} to try and find replacements if the recipe doesn't exist.
	 *
	 * If the recipe has no replacement, it will be logged.
	 */
	public static <T extends IRecipe<C>, C extends IInventory> T getRecipe(IRecipeType<T> type, ResourceLocation id) {
		@SuppressWarnings("unchecked")
		Map<ResourceLocation, T> map = (Map<ResourceLocation, T>) ModRecipeTypes.getRecipes(Minecraft.getInstance().world, type);
		T r = map.get(id);
		if (r != null) {
			return r;
		}
		r = map.get(new ResourceLocation("crafttweaker", id.getPath()));
		if (r != null) {
			return r;
		}

		Botania.LOGGER.warn("Template references nonexistent recipe {} of type {}", id, type);
		if (!crafttweakerInfoNote) {
			crafttweakerInfoNote = true;
			if (ModList.get().isLoaded("crafttweaker")) {
				Botania.LOGGER.info("To add a recipe that replaces a builtin recipe with CT, \n" +
						"add one with the same type, named the same as the path of the missing recipe.\n" +
						"eg. for recipe {}, add a recipe named \"{}\".", id, id.getPath());
			}
		}
		return null;
	}

	/**
	 * Get all recipes of the specified type that belong to the specified recipe group.
	 */
	public static <T extends IRecipe<C>, C extends IInventory> List<T> getRecipeGroup(IRecipeType<T> type, String group) {
		@SuppressWarnings("unchecked")
		Map<ResourceLocation, T> map = (Map<ResourceLocation, T>) ModRecipeTypes.getRecipes(Minecraft.getInstance().world, type);
		List<T> list = new ArrayList<>();
		for (T value : map.values()) {
			if (group.equals(value.getGroup())) {
				list.add(value);
			}
		}
		if (list.isEmpty()) {
			Botania.LOGGER.warn("Template references empty group {} of recipe type {}", group, type);
		}
		return list;
	}

	/**
	 * Combines the ingredients, returning the first matching stack of each, then the second stack of each, etc.
	 * looping back ingredients that run out of matched stacks, until the ingredients reach the length
	 * of the longest ingredient in the recipe set.
	 *
	 * @param ingredients           List of ingredients in the specific slot
	 * @param longestIngredientSize Longest ingredient in the entire recipe
	 * @return Serialized Patchouli ingredient string
	 */
	public static IVariable interweaveIngredients(List<Ingredient> ingredients, int longestIngredientSize) {
		if (ingredients.size() == 1) {
			return IVariable.wrapList(Arrays.stream(ingredients.get(0).getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
		}

		ItemStack[] empty = { ItemStack.EMPTY };
		List<ItemStack[]> stacks = new ArrayList<>();
		for (Ingredient ingredient : ingredients) {
			if (ingredient != null && !ingredient.hasNoMatchingItems()) {
				stacks.add(ingredient.getMatchingStacks());
			} else {
				stacks.add(empty);
			}
		}
		List<IVariable> list = new ArrayList<>(stacks.size() * longestIngredientSize);
		for (int i = 0; i < longestIngredientSize; i++) {
			for (ItemStack[] stack : stacks) {
				list.add(IVariable.from(stack[i % stack.length]));
			}
		}
		return IVariable.wrapList(list);
	}

	/**
	 * Overload of the method above that uses the provided list's longest ingredient size.
	 */
	public static IVariable interweaveIngredients(List<Ingredient> ingredients) {
		return interweaveIngredients(ingredients, ingredients.stream().mapToInt(ingr -> ingr.getMatchingStacks().length).max().orElse(1));
	}
}
