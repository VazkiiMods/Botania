/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:15:28 PM (GMT)]
 */
package vazkii.botania.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import vazkii.botania.api.internal.DummyMethodHandler;
import vazkii.botania.api.internal.IInternalMethodHandler;
import vazkii.botania.api.internal.DummySubTile;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public final class BotaniaAPI {

	private static List<LexiconCategory> categories = new ArrayList<LexiconCategory>();
	private static List<LexiconEntry> allEntries = new ArrayList<LexiconEntry>();

	public static List<RecipePetals> petalRecipes = new ArrayList<RecipePetals>();
	public static List<RecipeManaInfusion> manaInfusionRecipes = new ArrayList<RecipeManaInfusion>();

	private static BiMap<String, Class<? extends SubTileEntity>> subTiles = HashBiMap.<String, Class<? extends SubTileEntity>> create();
	static {
		registerSubTile("", DummySubTile.class);
	}

	/**
	 * The internal method handler in use. Do not overwrite.
	 * @see IInternalMethodHandler
	 */
	public static IInternalMethodHandler internalHandler = new DummyMethodHandler();

	/**
	 * Registers a Petal Recipe.
	 * @param output The ItemStack to craft.
	 * @param colors The required metadata petals for this recipe to be accept.
	 * Eg: 0, 0, 1 is White, White, Orange
	 * @return The recipe created.
	 */
	public static RecipePetals registerPetalRecipe(ItemStack output, int... colors) {
		RecipePetals recipe = new RecipePetals(output, colors);
		petalRecipes.add(recipe);
		return recipe;
	}

	/**
	 * Registers a Mana Infusion Recipe (throw an item in a mana pool)
	 * @param output The ItemStack to craft
	 * @param input The input item, be it an ItemStack or an ore dictionary entry String.
	 * @return The recipe created.
	 */
	public static RecipeManaInfusion registerManaInfusionRecipe(ItemStack output, Object input, int mana) {
		RecipeManaInfusion recipe = new RecipeManaInfusion(output, input, mana);
		manaInfusionRecipes.add(recipe);
		return recipe;
	}

	public static void registerSubTile(String key, Class<? extends SubTileEntity> subtileClass) {
		subTiles.put(key, subtileClass);
	}

	/**
	 * Adds a category to the list of registered categories to appear in the Lexicon.
	 */
	public static void addCategory(LexiconCategory category) {
		categories.add(category);
	}

	/**
	 * Gets all registered categories.
	 */
	public static List<LexiconCategory> getAllCategories() {
		return categories;
	}

	/**
	 * Registers a Lexicon Entry and adds it to the category passed in.
	 */
	public static void addEntry(LexiconEntry entry, LexiconCategory category) {
		allEntries.add(entry);
		category.entries.add(entry);
	}

	/**
	 * Gets the last recipe to have been added to the recipe list.
	 */
	public static IRecipe getLatestAddedRecipe() {
		List<IRecipe> list = CraftingManager.getInstance().getRecipeList();
		return list.get(list.size() - 1);
	}

	/**
	 * Gets the last x recipes added to the recipe list.
	 */
	public static List<IRecipe> getLatestAddedRecipes(int x) {
		List<IRecipe> list = CraftingManager.getInstance().getRecipeList();
		List<IRecipe> newList = new ArrayList();
		for(int i = x - 1; i >= 0; i--)
			newList.add(list.get(list.size() - 1 - i));

		return newList;
	}

	public static Class<? extends SubTileEntity> getSubTileMapping(String key) {
		if(!subTiles.containsKey(key))
			key = "";

		return subTiles.get(key);
	}

	public static String getSubTileStringMapping(Class<? extends SubTileEntity> clazz) {
		return subTiles.inverse().get(clazz);
	}
}
