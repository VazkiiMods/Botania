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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.util.EnumHelper;
import vazkii.botania.api.internal.DummyMethodHandler;
import vazkii.botania.api.internal.DummySubTile;
import vazkii.botania.api.internal.IInternalMethodHandler;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.api.subtile.SubTileEntity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public final class BotaniaAPI {

	private static List<LexiconCategory> categories = new ArrayList<LexiconCategory>();
	private static List<LexiconEntry> allEntries = new ArrayList<LexiconEntry>();

	public static List<RecipePetals> petalRecipes = new ArrayList<RecipePetals>();
	public static List<RecipeRuneAltar> runeAltarRecipes = new ArrayList<RecipeRuneAltar>();
	public static List<RecipeManaInfusion> manaInfusionRecipes = new ArrayList<RecipeManaInfusion>();

	private static BiMap<String, Class<? extends SubTileEntity>> subTiles = HashBiMap.<String, Class<? extends SubTileEntity>> create();

	public static Map<String, Integer> oreWeights = new HashMap<String, Integer>();

	public static ArmorMaterial manasteelArmorMaterial = EnumHelper.addArmorMaterial("MANASTEEL", 16, new int[] { 2, 6, 5, 2 }, 18);
	public static ToolMaterial manasteelToolMaterial = EnumHelper.addToolMaterial("MANASTEEL", 3, 300, 6.2F, 2F, 20);

	public static ArmorMaterial terrasteelArmorMaterial = EnumHelper.addArmorMaterial("TERRASTEEL", 34, new int[] {3, 8, 6, 3}, 26);
	public static ToolMaterial terrasteelToolMaterial = EnumHelper.addToolMaterial("TERRASTEEL", 3, 2300, 9F, 3F, 26);

	static {
		registerSubTile("", DummySubTile.class);

		addOreWeight("oreAluminum", 3940); // Tinkers' Construct
		addOreWeight("oreAmber", 2075); // Thaumcraft
		addOreWeight("oreApatite", 1595); // Forestry
		addOreWeight("oreBlueTopaz", 3195); // Ars Magica
		addOreWeight("oreCassiterite", 1634); // GregTech
		addOreWeight("oreCertusQuartz", 3975); // Applied Energistics
		addOreWeight("oreChimerite", 3970); // Ars Magica
		addOreWeight("oreCinnabar",  2585); // Thaumcraft
		addOreWeight("oreCoal", 46525); // Vanilla
		addOreWeight("oreCooperite", 5); // GregTech
		addOreWeight("oreCopper", 8325); // IC2, Thermal Expansion, Tinkers' Construct, etc.
		addOreWeight("oreDarkIron", 1700); // Factorization
		addOreWeight("oreDiamond", 1265); // Vanilla
		addOreWeight("oreEmerald", 780); // Vanilla
		addOreWeight("oreEmery", 415); // GregTech
		addOreWeight("oreGalena", 1000); // Factorization
		addOreWeight("oreGold", 2970); // Vanilla
		addOreWeight("oreInfusedAir", 925); // Thaumcraft
		addOreWeight("oreInfusedEarth", 925); // Thaumcraft
		addOreWeight("oreInfusedEntropy", 925); // Thaumcraft
		addOreWeight("oreInfusedFire", 925); // Thaumcraft
		addOreWeight("oreInfusedOrder", 925); // Thaumcraft
		addOreWeight("oreInfusedWater", 925); // Thaumcraft
		addOreWeight("oreIridium", 30); // GregTech
		addOreWeight("oreIron", 20665); // Vanilla
		addOreWeight("oreLapis", 1285); // Vanilla
		addOreWeight("oreLead", 7985); // IC2, Thermal Expansion, Factorization, etc.
		addOreWeight("oreMCropsEssence", 3085); // Magical Crops
		addOreWeight("oreNickel", 2275); // Thermal Expansion
		addOreWeight("oreOlivine", 1100); // Project RED
		addOreWeight("oreRedstone", 6885); // Vanilla
		addOreWeight("oreRuby", 1100); // Project RED
		addOreWeight("oreSapphire", 1100); // Project RED
		addOreWeight("oreSilver", 6300); // Thermal Expansion, Factorization, etc.
		addOreWeight("oreSphalerite", 25); // GregTech
		addOreWeight("oreSulfur", 1105); // Railcraft
		addOreWeight("oreTetrahedrite", 4040); // GregTech
		addOreWeight("oreTin", 9450); // IC2, Thermal Expansion, etc.
		addOreWeight("oreTungstate", 20); // GregTech
		addOreWeight("oreUranium", 1337); // IC2
		addOreWeight("oreVinteum", 5925); // Ars Magica
		addOreWeight("oreYellorite", 3520); // Big Reactors
	}

	/**
	 * The internal method handler in use. Do not overwrite.
	 * @see IInternalMethodHandler
	 */
	public static IInternalMethodHandler internalHandler = new DummyMethodHandler();

	/**
	 * Registers a Petal Recipe.
	 * @param output The ItemStack to craft.
	 * @param inputs The objects for crafting. Can be ItemStack, MappableStackWrapper
	 * or String (case for Ore Dictionary). The array can't be larger than 16.
	 * @return The recipe created.
	 */
	public static RecipePetals registerPetalRecipe(ItemStack output, Object... inputs) {
		RecipePetals recipe = new RecipePetals(output, inputs);
		petalRecipes.add(recipe);
		return recipe;
	}

	/**
	 * Registers a Rune Altar
	 * @param output The ItemStack to craft.
	 * @param mana The amount of mana required. Don't go over 100000!
	 * @param inputs The objects for crafting. Can be ItemStack, MappableStackWrapper
	 * or String (case for Ore Dictionary). The array can't be larger than 16.
	 * @return The recipe created.
	 */
	public static RecipeRuneAltar registerRuneAltarRecipe(ItemStack output, int mana, Object... inputs) {
		RecipeRuneAltar recipe = new RecipeRuneAltar(output, mana, inputs);
		runeAltarRecipes.add(recipe);
		return recipe;
	}

	/**
	 * Registers a Mana Infusion Recipe (throw an item in a mana pool)
	 * @param output The ItemStack to craft
	 * @param input The input item, be it an ItemStack or an ore dictionary entry String.
	 * @param mana The amount of mana required. Don't go over 100000!
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
	 * Maps an ore (ore dictionary key) to it's weight on the world generation. This
	 * is used for the Orechid flower. Check the static block in the BotaniaAPI class
	 * to get the weights for the vanilla blocks.<br>
	 * Alternatively get the values with the OreDetector mod:<br>
	 * https://gist.github.com/Vazkii/9493322
	 */
	public static void addOreWeight(String ore, int weight) {
		oreWeights.put(ore, weight);
	}

	public static int getOreWeight(String ore) {
		return oreWeights.get(ore);
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
