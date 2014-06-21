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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.EnumHelper;
import vazkii.botania.api.internal.DummyMethodHandler;
import vazkii.botania.api.internal.DummySubTile;
import vazkii.botania.api.internal.IInternalMethodHandler;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.api.subtile.SubTileEntity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public final class BotaniaAPI {

	private static List<LexiconCategory> categories = new ArrayList<LexiconCategory>();
	private static List<LexiconEntry> allEntries = new ArrayList<LexiconEntry>();

	public static Map<String, KnowledgeType> knowledgeTypes = new HashMap<String, KnowledgeType>();

	public static List<RecipePetals> petalRecipes = new ArrayList<RecipePetals>();
	public static List<RecipeRuneAltar> runeAltarRecipes = new ArrayList<RecipeRuneAltar>();
	public static List<RecipeManaInfusion> manaInfusionRecipes = new ArrayList<RecipeManaInfusion>();
	public static List<RecipeElvenTrade> elvenTradeRecipes = new ArrayList<RecipeElvenTrade>();

	private static BiMap<String, Class<? extends SubTileEntity>> subTiles = HashBiMap.<String, Class<? extends SubTileEntity>> create();
	public static Set<String> subtilesForCreativeMenu = new LinkedHashSet();

	public static Map<String, Integer> oreWeights = new HashMap<String, Integer>();

	public static Map<Item, Block> seeds = new HashMap();

	public static ArmorMaterial manasteelArmorMaterial = EnumHelper.addArmorMaterial("MANASTEEL", 16, new int[] { 2, 6, 5, 2 }, 18);
	public static ToolMaterial manasteelToolMaterial = EnumHelper.addToolMaterial("MANASTEEL", 3, 300, 6.2F, 2F, 20);

	public static ArmorMaterial elementiumArmorMaterial = EnumHelper.addArmorMaterial("B_ELEMENTIUM", 18, new int[] { 2, 6, 5, 2 }, 18);
	public static ToolMaterial elementiumToolMaterial = EnumHelper.addToolMaterial("B_ELEMENTIUM", 3, 720, 6.2F, 2F, 20);

	public static ArmorMaterial terrasteelArmorMaterial = EnumHelper.addArmorMaterial("TERRASTEEL", 34, new int[] {3, 8, 6, 3}, 26);
	public static ToolMaterial terrasteelToolMaterial = EnumHelper.addToolMaterial("TERRASTEEL", 3, 2300, 9F, 3F, 26);

	public static KnowledgeType basicKnowledge, elvenKnowledge;

	static {
		registerSubTile("", DummySubTile.class);

		basicKnowledge = registerKnowledgeType("minecraft", EnumChatFormatting.RESET, true);
		elvenKnowledge = registerKnowledgeType("alfheim", EnumChatFormatting.DARK_GREEN, false);

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

		addSeed(Items.wheat_seeds, Blocks.wheat);
		addSeed(Items.potato, Blocks.potatoes);
		addSeed(Items.carrot, Blocks.carrots);
		addSeed(Items.nether_wart, Blocks.nether_wart);
		addSeed(Items.pumpkin_seeds, Blocks.pumpkin_stem);
		addSeed(Items.melon_seeds, Blocks.melon_stem);
	}

	/**
	 * The internal method handler in use. Do not overwrite.
	 * @see IInternalMethodHandler
	 */
	public static IInternalMethodHandler internalHandler = new DummyMethodHandler();


	/**
	 * Registers a new Knowledge Type.
	 * @param id The ID for this knowledge type.
	 * @param color The color to display this knowledge type as.
	 */
	public static KnowledgeType registerKnowledgeType(String id, EnumChatFormatting color, boolean autoUnlock) {
		KnowledgeType type = new KnowledgeType(id, color, autoUnlock);
		knowledgeTypes.put(id, type);
		return type;
	}

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

	/**
	 * Register a Mana Infusion Recipe and flags it as an Alchemy recipe (requires an
	 * Alchemy Catalyst below the pool).
	 * @see BotaniaAPI#registerManaInfusionRecipe
	 */
	public static RecipeManaInfusion registerManaAlchemyRecipe(ItemStack output, Object input, int mana) {
		RecipeManaInfusion recipe = registerManaInfusionRecipe(output, input, mana);
		recipe.setAlchemy(true);
		return recipe;
	}

	/**
	 * Register a Mana Infusion Recipe and flags it as an Conjuration recipe (requires a
	 * Conjuration Catalyst below the pool).
	 * @see BotaniaAPI#registerManaInfusionRecipe
	 */
	public static RecipeManaInfusion registerManaConjurationRecipe(ItemStack output, Object input, int mana) {
		RecipeManaInfusion recipe = registerManaInfusionRecipe(output, input, mana);
		recipe.setConjuration(true);
		return recipe;
	}

	/**
	 * Registers a Elven Trade recipe (throw an item in an Alfheim Portal).
	 * @param output The ItemStack to return.
	 * @param inputs The items required, can be ItemStack or ore dictionary entry string.
	 * @return The recipe created.
	 */
	public static RecipeElvenTrade registerElvenTradeRecipe(ItemStack output, Object... inputs) {
		RecipeElvenTrade recipe = new RecipeElvenTrade(output, inputs);
		elvenTradeRecipes.add(recipe);
		return recipe;
	}

	/**
	 * Registers a SubTileEntity, a new special flower. Look in the subtile package of the API.
	 */
	public static void registerSubTile(String key, Class<? extends SubTileEntity> subtileClass) {
		subTiles.put(key, subtileClass);
	}

	/**
	 * Adds the key for a SubTileEntity into the creative menu. This goes into the
	 * subtilesForCreativeMenu Set.
	 */
	public static void addSubTileToCreativeMenu(String key) {
		subtilesForCreativeMenu.add(key);
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
	 * Allows an item to be counted as a seed. Any item in this list can be
	 * dispensed by a dispenser, the block is the block to be placed.
	 */
	public static void addSeed(Item item, Block block) {
		seeds.put(item, block);
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
