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

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BotaniaAPI {

	private static final List<LexiconCategory> categories = new ArrayList<>();
	private static final List<LexiconEntry> allEntries = new ArrayList<>();

	public static final Map<String, KnowledgeType> knowledgeTypes = new HashMap<>();

	public static final Map<String, Brew> brewMap = new LinkedHashMap<>();

	public static final List<String> disposableBlocks = new ArrayList<>();
	public static final List<String> semiDisposableBlocks = new ArrayList<>();

	public static final List<RecipePetals> petalRecipes = new ArrayList<>();
	public static final List<RecipePureDaisy> pureDaisyRecipes = new ArrayList<>();
	public static final List<RecipeManaInfusion> manaInfusionRecipes = new ArrayList<>();
	public static final List<RecipeRuneAltar> runeAltarRecipes = new ArrayList<>();
	public static final List<RecipeElvenTrade> elvenTradeRecipes = new ArrayList<>();
	public static final List<RecipeBrew> brewRecipes = new ArrayList<>();
	public static final List<RecipeManaInfusion> miniFlowerRecipes = new ArrayList<>();

	private static final BiMap<String, Class<? extends SubTileEntity>> subTiles = HashBiMap.create();
	private static final Map<Class<? extends SubTileEntity>, SubTileSignature> subTileSignatures = new HashMap<>();
	public static final Set<String> subtilesForCreativeMenu = new LinkedHashSet<>();
	public static final Map<String, String> subTileMods = new HashMap<>();
	public static final BiMap<String, String> miniFlowers = HashBiMap.create();

	public static final Map<String, Integer> oreWeights = new HashMap<>();
	public static final Map<String, Integer> oreWeightsNether = new HashMap<>();

	public static final Set<Item> looniumBlacklist = new LinkedHashSet<>();
	public static final Map<Block, PropertyEnum<EnumDyeColor>> paintableBlocks = new LinkedHashMap<>();
	public static final Set<String> magnetBlacklist = new LinkedHashSet<>();
	public static final Set<Class<? extends Entity>> gravityRodBlacklist = new LinkedHashSet<>();
	
	public static final Set<Block> gaiaBreakBlacklist = new HashSet<>();
	
	public static final ArmorMaterial manasteelArmorMaterial = EnumHelper.addArmorMaterial("MANASTEEL", "manasteel", 16,
			new int[] { 2, 5, 6, 2 }, 18, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);
	public static final ToolMaterial manasteelToolMaterial = EnumHelper.addToolMaterial("MANASTEEL", 3, 300, 6.2F, 2F, 20);

	public static final ArmorMaterial elementiumArmorMaterial = EnumHelper.addArmorMaterial("B_ELEMENTIUM", "b_elementium", 18,
			new int[] { 2, 5, 6, 2 }, 18, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);
	public static final ToolMaterial elementiumToolMaterial = EnumHelper.addToolMaterial("B_ELEMENTIUM", 3, 720, 6.2F, 2F, 20);

	public static final ArmorMaterial terrasteelArmorMaterial = EnumHelper.addArmorMaterial("TERRASTEEL", "terrasteel", 34,
			new int[] { 3, 6, 8, 3 }, 26, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3F);
	public static final ToolMaterial terrasteelToolMaterial = EnumHelper.addToolMaterial("TERRASTEEL", 4, 2300, 9F, 3F, 26);

	public static final ArmorMaterial manaweaveArmorMaterial = EnumHelper.addArmorMaterial("MANAWEAVE", "manaweave", 5,
			new int[] { 1, 2, 2, 1 }, 18, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);

	public static final EnumRarity rarityRelic = EnumHelper.addRarity("RELIC", TextFormatting.GOLD, "Relic");

	public static final KnowledgeType basicKnowledge;
	public static final KnowledgeType elvenKnowledge;

	// This is here for completeness sake, but you shouldn't use it
	public static final KnowledgeType relicKnowledge;

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

	public static final Brew fallbackBrew = new Brew("fallback", "botania.brew.fallback", 0, 0);

	static {
		registerSubTile("", DummySubTile.class);

		basicKnowledge = registerKnowledgeType("minecraft", TextFormatting.RESET, true);
		elvenKnowledge = registerKnowledgeType("alfheim", TextFormatting.DARK_GREEN, false);
		relicKnowledge = registerKnowledgeType("relic", TextFormatting.DARK_PURPLE, false);

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
		addOreWeight("oreDarkIron", 1700); // Factorization (older versions)
		addOreWeight("oreFzDarkIron", 1700); // Factorization (newer versions)
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
		addOreWeight("oreMithril", 8); // Thermal Expansion
		addOreWeight("oreNickel", 2275); // Thermal Expansion
		addOreWeight("oreOlivine", 1100); // Project RED
		addOreWeight("orePlatinum", 365); // Thermal Expansion
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
		addOreWeight("oreOsmium", 6915); // Mekanism
		addOreWeight("oreQuartzBlack", 5535); // Actually Additions

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
		addOreWeightNether("oreAshstone", 1000); // Netherrocks
		addOreWeightNether("oreDragonstone", 175); // Netherrocks
		addOreWeightNether("oreArgonite", 1000); // Netherrocks
		addOreWeightNether("oreOnyx", 500); // SimpleOres 2
		addOreWeightNether("oreHaditeCoal", 500); // Hadite

		registerModWiki("minecraft", new SimpleWikiProvider("Minecraft Wiki", "https://minecraft.gamepedia.com/%s"));

		IWikiProvider technicWiki = new SimpleWikiProvider("Technic Wiki", "http://wiki.technicpack.net/%s");
		IWikiProvider mekanismWiki = new SimpleWikiProvider("Mekanism Wiki", "http://wiki.aidancbrady.com/wiki/%s");

		registerModWiki("mekanism", mekanismWiki);
		registerModWiki("mekanismgenerators", mekanismWiki);
		registerModWiki("mekanismtools", mekanismWiki);
		registerModWiki("enderio", new SimpleWikiProvider("EnderIO Wiki", "http://wiki.enderio.com/%s"));
		registerModWiki("tropicraft", new SimpleWikiProvider("Tropicraft Wiki", "http://wiki.tropicraft.net/wiki/%s"));
		registerModWiki("randomthings", new SimpleWikiProvider("Random Things Wiki", "https://lumien.net/rtwiki/blocks/%s/", "-", true));
		registerModWiki("appliedenergistics2", new SimpleWikiProvider("AE2 Wiki", "http://ae-mod.info/%s"));
		registerModWiki("bigreactors", technicWiki);

		registerPaintableBlock(Blocks.STAINED_GLASS, BlockStainedGlass.COLOR);
		registerPaintableBlock(Blocks.STAINED_GLASS_PANE, BlockStainedGlassPane.COLOR);
		registerPaintableBlock(Blocks.STAINED_HARDENED_CLAY, BlockColored.COLOR);
		registerPaintableBlock(Blocks.WOOL, BlockColored.COLOR);
		registerPaintableBlock(Blocks.CARPET, BlockCarpet.COLOR);

		registerDisposableBlock("dirt"); // Vanilla
		registerDisposableBlock("sand"); // Vanilla
		registerDisposableBlock("gravel"); // Vanilla
		registerDisposableBlock("cobblestone"); // Vanilla
		registerDisposableBlock("netherrack"); // Vanilla
		registerSemiDisposableBlock("stoneAndesite"); // Vanilla
		registerSemiDisposableBlock("stoneBasalt"); // Vanilla
		registerSemiDisposableBlock("stoneDiorite"); // Vanilla
		registerSemiDisposableBlock("stoneGranite"); // Vanilla
		
		blacklistBlockFromGaiaGuardian(Blocks.BEACON);
	}

	/**
	 * The internal method handler in use.
	 * <b>DO NOT OVERWRITE THIS OR YOU'RE GOING TO FEEL MY WRATH WHEN I UPDATE THE API.</b>
	 * The fact I have to write that means some moron already tried, don't be that moron.
	 * @see IInternalMethodHandler
	 */
	public static IInternalMethodHandler internalHandler = new DummyMethodHandler();

	/**
	 * Registers a new Knowledge Type.
	 * @param id The ID for this knowledge type.
	 * @param color The color to display this knowledge type as.
	 */
	public static KnowledgeType registerKnowledgeType(String id, TextFormatting color, boolean autoUnlock) {
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
	 * Registers a Block as disposable using its Ore Dictionary Name.
	 */
	public static void registerDisposableBlock(String oreDictName) {
		disposableBlocks.add(oreDictName);
	}

	/**
	 * Registers a Block as semi disposable using its Ore Dictionary Name.
	 * This means it will not be trashed when sneaking.
	 */
	public static void registerSemiDisposableBlock(String oreDictName) {
		semiDisposableBlocks.add(oreDictName);
	}

	/**
	 * Registers a paintableBlock and returns it.
	 * You must also provide the PropertyEnum that this block uses to express its color
	 */
	public static Block registerPaintableBlock(Block paintable, PropertyEnum<EnumDyeColor> colorProp){
		paintableBlocks.put(paintable, colorProp);
		return paintable;
	}

	/**
	 * Blacklists an Entity from being affected by the Rod of the Shaded Mesa.
	 * Pass in the class for the Entity, e.g. EntityCow.class
	 */
	public static void blacklistEntityFromGravityRod(Class<? extends Entity> entity) {
		gravityRodBlacklist.add(entity);
	}

	/**
	 * Checks if the provided Entity is contained in the Blacklist.
	 * Pass in the class for the Entity, e.g. entity.getClass()
	 */
	public static boolean isEntityBlacklistedFromGravityRod(Class entity) {
		return gravityRodBlacklist.contains(entity);
	}

	/**
	 * Blacklists an item from being pulled by the Ring of Magnetization.
	 * Short.MAX_VALUE can be used as the stack's damage for a wildcard.
	 */
	public static void blacklistItemFromMagnet(ItemStack stack) {
		String key = getMagnetKey(stack);
		magnetBlacklist.add(key);
	}

	/**
	 * Blacklists a block from having items on top of it being pulled by the Ring of Magnetization.
	 * Short.MAX_VALUE can be used as meta for a wildcard.
	 */
	public static void blacklistBlockFromMagnet(Block block, int meta) {
		String key = getMagnetKey(block, meta);
		magnetBlacklist.add(key);
	}

	public static boolean isItemBlacklistedFromMagnet(ItemStack stack) {
		return isItemBlacklistedFromMagnet(stack, 0);
	}

	public static boolean isItemBlacklistedFromMagnet(ItemStack stack, int recursion) {
		if(recursion > 5)
			return false;

		if(stack.getItemDamage() != Short.MAX_VALUE) {
			ItemStack copy = new ItemStack(stack.getItem(), 1, Short.MAX_VALUE);
			boolean general = isItemBlacklistedFromMagnet(copy, recursion + 1);
			if(general)
				return true;
		}

		String key = getMagnetKey(stack);
		return magnetBlacklist.contains(key);
	}

	public static boolean isBlockBlacklistedFromMagnet(IBlockState state) {
		return isBlockBlacklistedFromMagnet(state.getBlock(), state.getBlock().getMetaFromState(state));
	}

	public static boolean isBlockBlacklistedFromMagnet(Block block, int meta) {
		return isBlockBlacklistedFromMagnet(block, meta, 0);
	}

	public static boolean isBlockBlacklistedFromMagnet(Block block, int meta, int recursion) {
		if(recursion >= 5)
			return false;

		if(meta != Short.MAX_VALUE) {
			boolean general = isBlockBlacklistedFromMagnet(block, Short.MAX_VALUE, recursion + 1);
			if(general)
				return true;
		}

		String key = getMagnetKey(block, meta);
		return magnetBlacklist.contains(key);
	}

	/**
	 * Registers a Petal Recipe.
	 * @param output The ItemStack to craft.
	 * @param inputs The objects for crafting. Can be ItemStack, MappableStackWrapper
	 * or String (case for Ore Dictionary). The array can't be larger than 16.
	 * @return The recipe created.
	 */
	public static RecipePetals registerPetalRecipe(ItemStack output, Object... inputs) {
		Preconditions.checkArgument(inputs.length <= 16);
		RecipePetals recipe = new RecipePetals(output, inputs);
		petalRecipes.add(recipe);
		return recipe;
	}

	/**
	 * Registers a Pure Daisy Recipe with the default time
	 * @param input The input for the recipe. Can be a Block (meta-insensitive), IBlockState (meta-sensitive), or an oredict String.
	 * @param outputState The blockstate to be placed upon recipe completion.
	 * @return The recipe created.
	 */
	public static RecipePureDaisy registerPureDaisyRecipe(Object input, IBlockState outputState) {
		return registerPureDaisyRecipe(input, outputState, RecipePureDaisy.DEFAULT_TIME);
	}

	/**
	 * Registers a Pure Daisy Recipe.
	 * @param input The input for the recipe. Can be a Block (meta-insensitive), IBlockState (meta-sensitive), or an oredict String.
	 * @param outputState The blockstate to be placed upon recipe completion.
	 * @param time The amount of time in ticks to complete this recipe. Note that this is ticks on your block, not total time.
	 *             The Pure Daisy only ticks one block at a time in a round robin fashion.
	 * @return The recipe created.
	 */
	public static RecipePureDaisy registerPureDaisyRecipe(Object input, IBlockState outputState, int time) {
		RecipePureDaisy recipe = new RecipePureDaisy(input, outputState, time);
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
		Preconditions.checkArgument(inputs.length <= 16);
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
		Preconditions.checkArgument(mana <= 1000000);
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
		recipe.setCatalyst(RecipeManaInfusion.alchemyState);
		return recipe;
	}

	/**
	 * Register a Mana Infusion Recipe and flags it as an Conjuration recipe (requires a
	 * Conjuration Catalyst below the pool).
	 * @see BotaniaAPI#registerManaInfusionRecipe
	 */
	public static RecipeManaInfusion registerManaConjurationRecipe(ItemStack output, Object input, int mana) {
		RecipeManaInfusion recipe = registerManaInfusionRecipe(output, input, mana);
		recipe.setCatalyst(RecipeManaInfusion.conjurationState);
		return recipe;
	}

	/**
	 * Registers a Elven Trade recipe (throw an item in an Alfheim Portal).
	 * @param outputs The ItemStacks to return.
	 * @param inputs The items required, can be ItemStack or ore dictionary entry string.
	 * @return The recipe created.
	 */
	public static RecipeElvenTrade registerElvenTradeRecipe(ItemStack[] outputs, Object... inputs) {
		RecipeElvenTrade recipe = new RecipeElvenTrade(outputs, inputs);
		elvenTradeRecipes.add(recipe);
		return recipe;
	}

	/**
	 * Registers a Elven Trade recipe (throw an item into an Alfeim Portal).
	 * @param output The ItemStack to return
	 * @param inputs The items required, can be an ItemStack or an Ore Dictionary entry string.
	 * @return The recipe created.
	 */
	public static RecipeElvenTrade registerElvenTradeRecipe(ItemStack output, Object... inputs) {
		return registerElvenTradeRecipe(new ItemStack[]{ output }, inputs);
	}

	/**
	 * Registers a Brew Recipe (for the Botanical Brewery).
	 * @param brew The brew in to be set in this recipe.
	 * @param inputs The items used in the recipe, no more than 6.
	 */
	public static RecipeBrew registerBrewRecipe(Brew brew, Object... inputs) {
		Preconditions.checkArgument(inputs.length <= 6);
		RecipeBrew recipe = new RecipeBrew(brew, inputs);
		brewRecipes.add(recipe);
		return recipe;
	}

	/**
	 * Registers a SubTileEntity, a new special flower. Look in the subtile package of the API.
	 * Call this during {@code RegistryEvent.Register<Block>}, and don't forget to register a model in BotaniaAPIClient.
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
		if(ore.contains("Nether") && OreDictionary.getOres(ore.replace("Nether", "")).size() == 0)
			return;

		oreWeightsNether.put(ore, weight);
	}

	public static int getOreWeight(String ore) {
		return oreWeights.get(ore);
	}

	public static int getOreWeightNether(String ore) {
		return oreWeightsNether.get(ore);
	}

	/**
	 * Blacklists an item from the Loonium drop table.
	 */
	public static void blackListItemFromLoonium(Item item) {
		looniumBlacklist.add(item);
	}

	/**
	 * Registers a Wiki provider for a mod so it uses that instead of the fallback
	 * FTB wiki.
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

	private static String getMagnetKey(ItemStack stack) {
		if(stack.isEmpty())
			return "";

		return "i_" + stack.getItem().getTranslationKey() + "@" + stack.getItemDamage();
	}

	private static String getMagnetKey(Block block, int meta) {
		return "bm_" + block.getTranslationKey() + "@" + meta;
	}

	public static void blacklistBlockFromGaiaGuardian(Block block) {
		gaiaBreakBlacklist.add(block);
	}
}
