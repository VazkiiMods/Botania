/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 14, 2014, 6:15:28 PM (GMT)]
 */
package vazkii.botania.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.EnumHelper;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.internal.DummyMethodHandler;
import vazkii.botania.api.internal.DummySubTile;
import vazkii.botania.api.internal.IInternalMethodHandler;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipeMiniFlower;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.signature.BasicSignature;
import vazkii.botania.api.subtile.signature.SubTileSignature;
import vazkii.botania.api.wiki.IWikiProvider;
import vazkii.botania.api.wiki.SimpleWikiProvider;
import vazkii.botania.api.wiki.WikiHooks;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.common.Loader;

public final class BotaniaAPI {

	private static List<LexiconCategory> categories = new ArrayList<LexiconCategory>();
	private static List<LexiconEntry> allEntries = new ArrayList<LexiconEntry>();

	public static Map<String, KnowledgeType> knowledgeTypes = new HashMap<String, KnowledgeType>();

	public static Map<String, Brew> brewMap = new LinkedHashMap<String, Brew>();

	public static List<RecipePetals> petalRecipes = new ArrayList<RecipePetals>();
	public static List<RecipePureDaisy> pureDaisyRecipes = new ArrayList<RecipePureDaisy>();
	public static List<RecipeManaInfusion> manaInfusionRecipes = new ArrayList<RecipeManaInfusion>();
	public static List<RecipeRuneAltar> runeAltarRecipes = new ArrayList<RecipeRuneAltar>();
	public static List<RecipeElvenTrade> elvenTradeRecipes = new ArrayList<RecipeElvenTrade>();
	public static List<RecipeBrew> brewRecipes = new ArrayList<RecipeBrew>();
	public static List<RecipeManaInfusion> miniFlowerRecipes = new ArrayList<RecipeManaInfusion>();

	private static BiMap<String, Class<? extends SubTileEntity>> subTiles = HashBiMap.<String, Class<? extends SubTileEntity>> create();
	private static Map<Class<? extends SubTileEntity>, SubTileSignature> subTileSignatures = new HashMap<Class<? extends SubTileEntity>, SubTileSignature>();
	public static Set<String> subtilesForCreativeMenu = new LinkedHashSet();
	public static Map<String, String> subTileMods = new HashMap<String, String>();
	public static BiMap<String, String> miniFlowers = HashBiMap.<String, String> create();

	public static Map<String, Integer> oreWeights = new HashMap<String, Integer>();
	public static Map<String, Integer> oreWeightsNether = new HashMap<String, Integer>();
	public static Map<Item, Block> seeds = new HashMap();
	public static Set<Item> looniumBlacklist = new LinkedHashSet();

	public static ArmorMaterial manasteelArmorMaterial = EnumHelper.addArmorMaterial("MANASTEEL", 16, new int[] { 2, 6, 5, 2 }, 18);
	public static ToolMaterial manasteelToolMaterial = EnumHelper.addToolMaterial("MANASTEEL", 3, 300, 6.2F, 2F, 20);

	public static ArmorMaterial elementiumArmorMaterial = EnumHelper.addArmorMaterial("B_ELEMENTIUM", 18, new int[] { 2, 6, 5, 2 }, 18);
	public static ToolMaterial elementiumToolMaterial = EnumHelper.addToolMaterial("B_ELEMENTIUM", 3, 720, 6.2F, 2F, 20);

	public static ArmorMaterial terrasteelArmorMaterial = EnumHelper.addArmorMaterial("TERRASTEEL", 34, new int[] {3, 8, 6, 3}, 26);
	public static ToolMaterial terrasteelToolMaterial = EnumHelper.addToolMaterial("TERRASTEEL", 4, 2300, 9F, 3F, 26);

	public static EnumRarity rarityRelic = EnumHelper.addRarity("RELIC", EnumChatFormatting.GOLD, "Relic");

	public static KnowledgeType basicKnowledge, elvenKnowledge;

	// All of these categories are initialized during botania's PreInit stage.
	public static LexiconCategory categoryBasics;
	public static LexiconCategory categoryMana;
	public static LexiconCategory categoryFunctionalFlowers;
	public static LexiconCategory categoryGenerationFlowers;
	public static LexiconCategory categoryDevices;
	public static LexiconCategory categoryTools;
	public static LexiconCategory categoryBaubles;
	public static LexiconCategory categoryEnder;
	public static LexiconCategory categoryAlfhomancy;
	public static LexiconCategory categoryMisc;

	public static Brew fallbackBrew = new Brew("fallback", "botania.brew.fallback", 0, 0);

	static {
		registerSubTile("", DummySubTile.class);

		basicKnowledge = registerKnowledgeType("minecraft", EnumChatFormatting.RESET, true);
		elvenKnowledge = registerKnowledgeType("alfheim", EnumChatFormatting.DARK_GREEN, false);

		addOreWeight("oreAluminum", 3940); // Tinkers' Construct
		addOreWeight("oreAmber", 2075); // Thaumcraft
		addOreWeight("oreApatite", 1595); // Forestry
		addOreWeight("oreBlueTopaz", 3195); // Ars Magica
		addOreWeight("oreCertusQuartz", 3975); // Applied Energistics
		addOreWeight("oreChimerite", 3970); // Ars Magica
		addOreWeight("oreCinnabar",  2585); // Thaumcraft
		addOreWeight("oreCoal", 46525); // Vanilla
		addOreWeight("oreCopper", 8325); // IC2, Thermal Expansion, Tinkers' Construct, etc.
		addOreWeight("oreDark", 1350); // EvilCraft
		addOreWeight("oreDarkIron", 1700); // Factorization
		addOreWeight("oreDiamond", 1265); // Vanilla
		addOreWeight("oreEmerald", 780); // Vanilla
		addOreWeight("oreGalena", 1000); // Factorization
		addOreWeight("oreGold", 2970); // Vanilla
		addOreWeight("oreInfusedAir", 925); // Thaumcraft
		addOreWeight("oreInfusedEarth", 925); // Thaumcraft
		addOreWeight("oreInfusedEntropy", 925); // Thaumcraft
		addOreWeight("oreInfusedFire", 925); // Thaumcraft
		addOreWeight("oreInfusedOrder", 925); // Thaumcraft
		addOreWeight("oreInfusedWater", 925); // Thaumcraft
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
		addOreWeight("oreSulfur", 1105); // Railcraft
		addOreWeight("oreTin", 9450); // IC2, Thermal Expansion, etc.
		addOreWeight("oreUranium", 1337); // IC2
		addOreWeight("oreVinteum", 5925); // Ars Magica
		addOreWeight("oreYellorite", 3520); // Big Reactors
		addOreWeight("oreZinc", 6485); // Flaxbeard's Steam Power
		addOreWeight("oreMythril", 6485); // Simple Ores2
		addOreWeight("oreAdamantium", 2275); // Simple Ores2
		addOreWeight("oreTungsten", 3520); // Simple Tungsten
		
		addOreWeightNether("oreQuartz", 19600); // Vanilla
		addOreWeightNether("oreCobalt", 500); // Tinker's Construct
		addOreWeightNether("oreArdite", 500); // Tinker's Construct
		addOreWeightNether("oreFirestone", 5); // Railcraft
		addOreWeightNether("oreNetherCoal", 17000); // Nether Ores
		addOreWeightNether("oreNetherCopper", 4700); // Nether Ores
		addOreWeightNether("oreNetherDiamond", 175); // Nether Ores
		addOreWeightNether("oreNetherEssence", 2460); // Magical Crops
		addOreWeightNether("oreNetherGold", 3635); // Nether Ores
		addOreWeightNether("oreNetherIron", 5790); // Nether Ores
		addOreWeightNether("oreNetherLapis", 3250); // Nether Ores
		addOreWeightNether("oreNetherLead", 2790); // Nether Ores
		addOreWeightNether("oreNetherNickel", 1790); // Nether Ores
		addOreWeightNether("oreNetherPlatinum", 170); // Nether Ores
		addOreWeightNether("oreNetherRedstone", 5600); // Nether Ores
		addOreWeightNether("oreNetherSilver", 1550); // Nether Ores
		addOreWeightNether("oreNetherSteel", 1690); // Nether Ores
		addOreWeightNether("oreNetherTin", 3750); // Nether Ores
		addOreWeightNether("oreFyrite", 1000); // Netherrocks
		addOreWeightNether("oreMalachite", 1000); // Netherrocks
		addOreWeightNether("oreAshstone", 1000); // Netherrocks
		addOreWeightNether("oreDragonstone", 175); // Netherrocks
		addOreWeightNether("oreArgonite", 1000); // Netherrocks
		addOreWeightNether("oreOnyx", 500); // SimpleOres 2
		addOreWeightNether("oreHaditeCoal", 500); // Hadite

		addSeed(Items.wheat_seeds, Blocks.wheat);
		addSeed(Items.potato, Blocks.potatoes);
		addSeed(Items.carrot, Blocks.carrots);
		addSeed(Items.nether_wart, Blocks.nether_wart);
		addSeed(Items.pumpkin_seeds, Blocks.pumpkin_stem);
		addSeed(Items.melon_seeds, Blocks.melon_stem);

		registerModWiki("Minecraft", new SimpleWikiProvider("Minecraft Wiki", "http://minecraft.gamepedia.com/%s"));

		IWikiProvider technicWiki = new SimpleWikiProvider("Technic Wiki", "http://wiki.technicpack.net/%s");
		IWikiProvider mekanismWiki = new SimpleWikiProvider("Mekanism Wiki", "http://wiki.aidancbrady.com/wiki/%s");
		IWikiProvider buildcraftWiki = new SimpleWikiProvider("BuildCraft Wiki", "http://www.mod-buildcraft.com/wiki/doku.php?id=%s");

		registerModWiki("Mekanism", mekanismWiki);
		registerModWiki("MekanismGenerators", mekanismWiki);
		registerModWiki("MekanismTools", mekanismWiki);
		registerModWiki("EnderIO", new SimpleWikiProvider("EnderIO Wiki", "http://wiki.enderio.com/%s"));
		registerModWiki("TropiCraft", new SimpleWikiProvider("Tropicraft Wiki", "http://wiki.tropicraft.net/wiki/%s"));
		registerModWiki("RandomThings", new SimpleWikiProvider("Random Things Wiki", "http://randomthingsminecraftmod.wikispaces.com/%s"));
		registerModWiki("Witchery", new SimpleWikiProvider("Witchery Wiki", "https://sites.google.com/site/witcherymod/%s", "-"));
		registerModWiki("AppliedEnergistics2", new SimpleWikiProvider("AE2 Wiki", "http://ae-mod.info/%s"));
		registerModWiki("BigReactors", technicWiki);
		registerModWiki("BuildCraft|Core", buildcraftWiki);
		registerModWiki("BuildCraft|Builders", buildcraftWiki);
		registerModWiki("BuildCraft|Energy", buildcraftWiki);
		registerModWiki("BuildCraft|Factory", buildcraftWiki);
		registerModWiki("BuildCraft|Silicon", buildcraftWiki);
		registerModWiki("BuildCraft|Transport", buildcraftWiki);
		registerModWiki("ArsMagica2", new SimpleWikiProvider("ArsMagica2 Wiki", "http://wiki.arsmagicamod.com/wiki/%s"));
		registerModWiki("PneumaticCraft", new SimpleWikiProvider("PneumaticCraft Wiki", "http://www.minemaarten.com/wikis/pneumaticcraft-wiki/pneumaticcraft-wiki-%s"));
		registerModWiki("StevesCarts2", new SimpleWikiProvider("Steve's Carts Wiki", "http://stevescarts2.wikispaces.com/%s"));
		registerModWiki("GanysSurface", new SimpleWikiProvider("Gany's Surface Wiki", "http://ganys-surface.wikia.com/wiki/%s"));
		registerModWiki("GanysNether", new SimpleWikiProvider("Gany's Nether Wiki", "http://ganys-nether.wikia.com/wiki/%s"));
		registerModWiki("GanysEnd", new SimpleWikiProvider("Gany's End Wiki", "http://ganys-end.wikia.com/wiki/%s"));
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
	 * Registers a Brew and returns it.
	 */
	public static Brew registerBrew(Brew brew) {
		brewMap.put(brew.getKey(), brew);
		return brew;
	}

	/**
	 * Gets a brew from the key passed in, returns the fallback if
	 * it's not in the map.
	 */
	public static Brew getBrewFromKey(String key) {
		if(brewMap.containsKey(key))
			return brewMap.get(key);
		return fallbackBrew;
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
	 * Registers a Pure Daisy Recipe.
	 * @param input The block that works as an input for the recipe. Can be a Block or an oredict String.
	 * @param output The block to be placed upon recipe completion.
	 * @param outputMeta The metadata to be placed upon recipe completion.
	 * @return The recipe created.
	 */
	public static RecipePureDaisy registerPureDaisyRecipe(Object input, Block output, int outputMeta) {
		RecipePureDaisy recipe = new RecipePureDaisy(input, output, outputMeta);
		pureDaisyRecipes.add(recipe);
		return recipe;
	}

	/**
	 * Registers a Rune Altar Recipe.
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
	 * Registers a Brew Recipe (for the Botanical Brewery).
	 * @param brew The brew in to be set in this recipe.
	 * @inputs The items used in the recipe, no more than 6.
	 */
	public static RecipeBrew registerBrewRecipe(Brew brew, Object... inputs) {
		RecipeBrew recipe = new RecipeBrew(brew, inputs);
		brewRecipes.add(recipe);
		return recipe;
	}

	/**
	 * Registers a SubTileEntity, a new special flower. Look in the subtile package of the API.
	 * If you call this after PostInit you're a failiure and we are very disappointed in you.
	 */
	public static void registerSubTile(String key, Class<? extends SubTileEntity> subtileClass) {
		subTiles.put(key, subtileClass);
		subTileMods.put(key, Loader.instance().activeModContainer().getModId());
	}

	/**
	 * Register a SubTileEntity and makes it a mini flower. Also adds the recipe and returns it.
	 * @see BotaniaAPI#registerSubTile
	 */
	public static RecipeManaInfusion registerMiniSubTile(String key, Class<? extends SubTileEntity> subtileClass, String original) {
		registerSubTile(key, subtileClass);
		miniFlowers.put(original, key);

		RecipeMiniFlower recipe = new RecipeMiniFlower(key, original, 2500);
		manaInfusionRecipes.add(recipe);
		miniFlowerRecipes.add(recipe);
		return recipe;
	}

	/**
	 * Registers a SubTileEntity's signature.
	 * @see SubTileSignature
	 */
	public static void registerSubTileSignature(Class<? extends SubTileEntity> subtileClass, SubTileSignature signature) {
		subTileSignatures.put(subtileClass, signature);
	}

	/**
	 * Gets the singleton signature for a SubTileEntity class. Registers a fallback if one wasn't registered
	 * before the call.
	 */
	public static SubTileSignature getSignatureForClass(Class<? extends SubTileEntity> subtileClass) {
		if(!subTileSignatures.containsKey(subtileClass))
			registerSubTileSignature(subtileClass, new BasicSignature(subTiles.inverse().get(subtileClass)));

		return subTileSignatures.get(subtileClass);
	}

	/**
	 * Gets the singleton signature for a SubTileEntity's name. Registers a fallback if one wasn't registered
	 * before the call.
	 */
	public static SubTileSignature getSignatureForName(String name) {
		Class<? extends SubTileEntity> subtileClass = subTiles.get(name);
		return getSignatureForClass(subtileClass);
	}

	/**
	 * Adds the key for a SubTileEntity into the creative menu. This goes into the
	 * subtilesForCreativeMenu Set. This does not need to be called for mini flowers,
	 * those will just use the mini flower map to add themselves next to the source.
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
	 * Gets all registered entries.
	 */
	public static List<LexiconEntry> getAllEntries() {
		return allEntries;
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

	/**
	 * Maps an ore (ore dictionary key) to it's weight on the nether world generation. This
	 * is used for the Orechid Ignem flower. Check the static block in the BotaniaAPI class
	 * to get the weights for the vanilla blocks.<br>
	 * Alternatively get the values with the OreDetector mod:<br>
	 * https://gist.github.com/Vazkii/9493322
	 */
	public static void addOreWeightNether(String ore, int weight) {
		oreWeightsNether.put(ore, weight);
	}

	public static int getOreWeight(String ore) {
		return oreWeights.get(ore);
	}

	public static int getOreWeightNether(String ore) {
		return oreWeightsNether.get(ore);
	}

	/**
	 * Allows an item to be counted as a seed. Any item in this list can be
	 * dispensed by a dispenser, the block is the block to be placed.
	 */
	public static void addSeed(Item item, Block block) {
		seeds.put(item, block);
	}

	/**
	 * Blacklists an item from the Loonium drop table.
	 */
	public static void blackListItemFromLoonium(Item item) {
		looniumBlacklist.add(item);
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

	/**
	 * Registers a Wiki provider for a mod so it uses that instead of the fallback
	 * FTB wiki. Make sure to call this on PostInit only!
	 */
	public static void registerModWiki(String mod, IWikiProvider provider) {
		WikiHooks.registerModWiki(mod, provider);
	}

	public static Class<? extends SubTileEntity> getSubTileMapping(String key) {
		if(!subTiles.containsKey(key))
			key = "";

		return subTiles.get(key);
	}

	public static String getSubTileStringMapping(Class<? extends SubTileEntity> clazz) {
		return subTiles.inverse().get(clazz);
	}

	public static Set<String> getAllSubTiles() {
		return subTiles.keySet();
	}
}
