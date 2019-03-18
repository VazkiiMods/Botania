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
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;
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
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class BotaniaAPI {

	private static final List<LexiconCategory> categories = new ArrayList<>();
	private static final List<LexiconEntry> allEntries = new ArrayList<>();

	public static final Map<String, KnowledgeType> knowledgeTypes = new HashMap<>();

	public static final Map<String, Brew> brewMap = new LinkedHashMap<>();

	public static final List<RecipePetals> petalRecipes = new ArrayList<>();
	public static final List<RecipePureDaisy> pureDaisyRecipes = new ArrayList<>();
	public static final List<RecipeManaInfusion> manaInfusionRecipes = new ArrayList<>();
	public static final List<RecipeRuneAltar> runeAltarRecipes = new ArrayList<>();
	public static final List<RecipeElvenTrade> elvenTradeRecipes = new ArrayList<>();
	public static final List<RecipeBrew> brewRecipes = new ArrayList<>();
	public static final List<RecipeManaInfusion> miniFlowerRecipes = new ArrayList<>();

	public static final ResourceLocation DUMMY_SUBTILE_NAME = new ResourceLocation("botania", "dummy");
	private static final RegistryNamespacedDefaultedByKey<Class<? extends SubTileEntity>> subTiles = new RegistryNamespacedDefaultedByKey<>(DUMMY_SUBTILE_NAME);
	private static final Map<Class<? extends SubTileEntity>, SubTileSignature> subTileSignatures = new HashMap<>();
	public static final Set<ResourceLocation> subtilesForCreativeMenu = new LinkedHashSet<>();
	public static final BiMap<ResourceLocation, ResourceLocation> miniFlowers = HashBiMap.create();

	public static final Map<ResourceLocation, Integer> oreWeights = new HashMap<>();
	public static final Map<ResourceLocation, Integer> oreWeightsNether = new HashMap<>();

	public static final Map<Block, Function<EnumDyeColor, Block>> paintableBlocks = new LinkedHashMap<>();
	public static final Set<Class<? extends Entity>> gravityRodBlacklist = new LinkedHashSet<>();
	public static final Set<Block> gaiaBreakBlacklist = new HashSet<>();

	private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
	public static final IArmorMaterial MANASTEEL_ARMOR_MAT = new IArmorMaterial() {
		private final int[] damageReduction = { 2, 5, 6, 2 };
		@Override
		public int getDurability(EntityEquipmentSlot slotIn) {
			return 16 * MAX_DAMAGE_ARRAY[slotIn.getIndex()];
		}

		@Override
		public int getDamageReductionAmount(EntityEquipmentSlot slotIn) {
			return damageReduction[slotIn.getIndex()];
		}

		@Override
		public int getEnchantability() {
			return 18;
		}

		@Override
		public SoundEvent getSoundEvent() {
			return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
		}

		@Override
		public Ingredient getRepairMaterial() {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "manasteel_ingot"));
			return Ingredient.fromItems(item);
		}

		@Override
		public String getName() {
			return "manasteel";
		}

		@Override
		public float getToughness() {
			return 0;
		}
	};
	public static final IItemTier MANASTEEL_ITEM_TIER = new IItemTier() {
		@Override
		public int getMaxUses() {
			return 300;
		}

		@Override
		public float getEfficiency() {
			return 6.2F;
		}

		@Override
		public float getAttackDamage() {
			return 2;
		}

		@Override
		public int getHarvestLevel() {
			return 3;
		}

		@Override
		public int getEnchantability() {
			return 20;
		}

		@Nonnull
		@Override
		public Ingredient getRepairMaterial() {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "manasteel_ingot"));
			return Ingredient.fromItems(item);
		}
	};

	public static final IArmorMaterial ELEMENTIUM_ARMOR_MAT = new IArmorMaterial() {
		private final int[] damageReduction = { 2, 5, 6, 2 };
		@Override
		public int getDurability(EntityEquipmentSlot slotIn) {
			return 18 * MAX_DAMAGE_ARRAY[slotIn.getIndex()];
		}

		@Override
		public int getDamageReductionAmount(EntityEquipmentSlot slotIn) {
			return damageReduction[slotIn.getIndex()];
		}

		@Override
		public int getEnchantability() {
			return 18;
		}

		@Override
		public SoundEvent getSoundEvent() {
			return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
		}

		@Override
		public Ingredient getRepairMaterial() {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "manasteel_ingot"));
			return Ingredient.fromItems(item);
		}

		@Override
		public String getName() {
			return "elementium";
		}

		@Override
		public float getToughness() {
			return 0;
		}
	};
	public static final IItemTier ELEMENTIUM_ITEM_TIER = new IItemTier() {
		@Override
		public int getMaxUses() {
			return 720;
		}

		@Override
		public float getEfficiency() {
			return 6.2F;
		}

		@Override
		public float getAttackDamage() {
			return 2;
		}

		@Override
		public int getHarvestLevel() {
			return 3;
		}

		@Override
		public int getEnchantability() {
			return 20;
		}

		@Nonnull
		@Override
		public Ingredient getRepairMaterial() {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "manasteel_ingot"));
			return Ingredient.fromItems(item);
		}
	};

	public static final IArmorMaterial TERRASTEEL_ARMOR_MAT = new IArmorMaterial() {
		private final int[] damageReduction = { 3, 6, 8, 3 };
		@Override
		public int getDurability(EntityEquipmentSlot slotIn) {
			return 34 * MAX_DAMAGE_ARRAY[slotIn.getIndex()];
		}

		@Override
		public int getDamageReductionAmount(EntityEquipmentSlot slotIn) {
			return damageReduction[slotIn.getIndex()];
		}

		@Override
		public int getEnchantability() {
			return 26;
		}

		@Override
		public SoundEvent getSoundEvent() {
			return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
		}

		@Override
		public Ingredient getRepairMaterial() {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "terrasteel_ingot"));
			return Ingredient.fromItems(item);
		}

		@Override
		public String getName() {
			return "terrasteel";
		}

		@Override
		public float getToughness() {
			return 3;
		}
	};
	public static final IItemTier TERRASTEEL_ITEM_TIER = new IItemTier() {
		@Override
		public int getMaxUses() {
			return 2300;
		}

		@Override
		public float getEfficiency() {
			return 9;
		}

		@Override
		public float getAttackDamage() {
			return 3;
		}

		@Override
		public int getHarvestLevel() {
			return 4;
		}

		@Override
		public int getEnchantability() {
			return 26;
		}

		@Nonnull
		@Override
		public Ingredient getRepairMaterial() {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "terrasteel_ingot"));
			return Ingredient.fromItems(item);
		}
	};
	public static final IArmorMaterial MANAWEAVE_ARMOR_MAT = new IArmorMaterial() {
		private final int[] damageReduction = { 1, 2, 2, 1 };
		@Override
		public int getDurability(EntityEquipmentSlot slotIn) {
			return 5 * MAX_DAMAGE_ARRAY[slotIn.getIndex()];
		}

		@Override
		public int getDamageReductionAmount(EntityEquipmentSlot slotIn) {
			return damageReduction[slotIn.getIndex()];
		}

		@Override
		public int getEnchantability() {
			return 18;
		}

		@Override
		public SoundEvent getSoundEvent() {
			return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
		}

		@Override
		public Ingredient getRepairMaterial() {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "manaweave_cloth"));
			return Ingredient.fromItems(item);
		}

		@Override
		public String getName() {
			return "manaweave";
		}

		@Override
		public float getToughness() {
			return 0;
		}
	};

	public static final EnumRarity rarityRelic = EnumRarity.EPIC; // todo 1.13 EnumHelper.addRarity("RELIC", TextFormatting.GOLD, "Relic");

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
		subTiles.register(0, DUMMY_SUBTILE_NAME, DummySubTile.class); // int id irrelevant, this is just to set the default value

		basicKnowledge = registerKnowledgeType("minecraft", TextFormatting.RESET, true);
		elvenKnowledge = registerKnowledgeType("alfheim", TextFormatting.DARK_GREEN, false);
		relicKnowledge = registerKnowledgeType("relic", TextFormatting.DARK_PURPLE, false);

		/* todo 1.13
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
		*/

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

		registerPaintableBlock(ColorHelper.STAINED_GLASS_MAP::get, Blocks.WHITE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS,
				Blocks.YELLOW_STAINED_GLASS, Blocks.LIME_STAINED_GLASS, Blocks.PINK_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS,
				Blocks.CYAN_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS);
		registerPaintableBlock(ColorHelper.STAINED_GLASS_PANE_MAP::get, Blocks.WHITE_STAINED_GLASS_PANE, Blocks.ORANGE_STAINED_GLASS_PANE, Blocks.MAGENTA_STAINED_GLASS_PANE, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE,
				Blocks.YELLOW_STAINED_GLASS_PANE, Blocks.LIME_STAINED_GLASS_PANE, Blocks.PINK_STAINED_GLASS_PANE, Blocks.GRAY_STAINED_GLASS_PANE, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE,
				Blocks.CYAN_STAINED_GLASS_PANE, Blocks.PURPLE_STAINED_GLASS_PANE, Blocks.BLUE_STAINED_GLASS_PANE, Blocks.BROWN_STAINED_GLASS_PANE, Blocks.GREEN_STAINED_GLASS_PANE, Blocks.RED_STAINED_GLASS_PANE, Blocks.BLACK_STAINED_GLASS_PANE);
		registerPaintableBlock(ColorHelper.TERRACOTTA_MAP::get, Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA,
				Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA,
				Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA);
		registerPaintableBlock(ColorHelper.WOOL_MAP::get, Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL, Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL,
				Blocks.YELLOW_WOOL, Blocks.LIME_WOOL, Blocks.PINK_WOOL, Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL,
				Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL, Blocks.BLUE_WOOL, Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL, Blocks.BLACK_WOOL);
		registerPaintableBlock(ColorHelper.CARPET_MAP::get, Blocks.WHITE_CARPET, Blocks.ORANGE_CARPET, Blocks.MAGENTA_CARPET, Blocks.LIGHT_BLUE_CARPET,
				Blocks.YELLOW_CARPET, Blocks.LIME_CARPET, Blocks.PINK_CARPET, Blocks.GRAY_CARPET, Blocks.LIGHT_GRAY_CARPET,
				Blocks.CYAN_CARPET, Blocks.PURPLE_CARPET, Blocks.BLUE_CARPET, Blocks.BROWN_CARPET, Blocks.GREEN_CARPET, Blocks.RED_CARPET, Blocks.BLACK_CARPET);
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
	 * Registers a block as paintable under the paint lens
	 */
	public static void registerPaintableBlock(Function<EnumDyeColor, Block> transformer, Block... paintables) {
		for(Block b : paintables) {
			paintableBlocks.put(b, transformer);
		}
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
	 * Registers a Petal Recipe.
	 * @param output The ItemStack to craft.
	 * @param inputs The objects for crafting. Can be ItemStack, MappableStackWrapper
	 * or String (case for Ore Dictionary). The array can't be larger than 16.
	 * @return The recipe created.
	 */
	public static RecipePetals registerPetalRecipe(ItemStack output, Ingredient... inputs) {
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
	public static RecipeRuneAltar registerRuneAltarRecipe(ItemStack output, int mana, Ingredient... inputs) {
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
	public static RecipeManaInfusion registerManaInfusionRecipe(ItemStack output, Ingredient input, int mana) {
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
	public static RecipeManaInfusion registerManaAlchemyRecipe(ItemStack output, Ingredient input, int mana) {
		RecipeManaInfusion recipe = registerManaInfusionRecipe(output, input, mana);
		recipe.setCatalyst(RecipeManaInfusion.alchemyState);
		return recipe;
	}

	/**
	 * Register a Mana Infusion Recipe and flags it as an Conjuration recipe (requires a
	 * Conjuration Catalyst below the pool).
	 * @see BotaniaAPI#registerManaInfusionRecipe
	 */
	public static RecipeManaInfusion registerManaConjurationRecipe(ItemStack output, Ingredient input, int mana) {
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
	public static RecipeElvenTrade registerElvenTradeRecipe(ItemStack[] outputs, Ingredient... inputs) {
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
	public static RecipeElvenTrade registerElvenTradeRecipe(ItemStack output, Ingredient... inputs) {
		return registerElvenTradeRecipe(new ItemStack[]{ output }, inputs);
	}

	/**
	 * Registers a Brew Recipe (for the Botanical Brewery).
	 * @param brew The brew in to be set in this recipe.
	 * @param inputs The items used in the recipe, no more than 6.
	 */
	public static RecipeBrew registerBrewRecipe(Brew brew, Ingredient... inputs) {
		Preconditions.checkArgument(inputs.length <= 6);
		RecipeBrew recipe = new RecipeBrew(brew, inputs);
		brewRecipes.add(recipe);
		return recipe;
	}

	/**
	 * Registers a SubTileEntity, a new special flower. Look in the subtile package of the API.
	 * Call this during {@code RegistryEvent.Register<Block>}, and don't forget to register a model in BotaniaAPIClient.
	 */
	public static void registerSubTile(ResourceLocation id, Class<? extends SubTileEntity> subtileClass) {
		subTiles.put(id, subtileClass);
	}

	/**
	 * Register a SubTileEntity and makes it a mini flower. Also adds the recipe and returns it.
	 * @see BotaniaAPI#registerSubTile
	 */
	public static RecipeManaInfusion registerMiniSubTile(ResourceLocation id, Class<? extends SubTileEntity> subtileClass, ResourceLocation original) {
		registerSubTile(id, subtileClass);
		miniFlowers.put(original, id);

		RecipeMiniFlower recipe = new RecipeMiniFlower(id, original, 2500);
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
			registerSubTileSignature(subtileClass, new BasicSignature(subTiles.getKey(subtileClass)));

		return subTileSignatures.get(subtileClass);
	}

	/**
	 * Gets the singleton signature for a SubTileEntity's name. Registers a fallback if one wasn't registered
	 * before the call.
	 */
	public static SubTileSignature getSignatureForName(ResourceLocation name) {
		Class<? extends SubTileEntity> subtileClass = subTiles.get(name);
		return getSignatureForClass(subtileClass);
	}

	/**
	 * Adds the key for a SubTileEntity into the creative menu. This goes into the
	 * subtilesForCreativeMenu Set. This does not need to be called for mini flowers,
	 * those will just use the mini flower map to add themselves next to the source.
	 */
	public static void addSubTileToCreativeMenu(ResourceLocation key) {
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
	 * Maps a block tag to it's weight on the world generation. This
	 * is used for the Orechid flower. Check the static block in the BotaniaAPI class
	 * to get the weights for the vanilla blocks.<br>
	 * Alternatively get the values with the OreDetector mod:<br>
	 * https://gist.github.com/Vazkii/9493322
	 */
	public static void addOreWeight(ResourceLocation tag, int weight) {
		oreWeights.put(tag, weight);
	}

	/**
	 * Maps a block tag to it's weight on the nether world generation. This
	 * is used for the Orechid Ignem flower. Check the static block in the BotaniaAPI class
	 * to get the weights for the vanilla blocks.<br>
	 * Alternatively get the values with the OreDetector mod:<br>
	 * https://gist.github.com/Vazkii/9493322
	 */
	public static void addOreWeightNether(ResourceLocation tag, int weight) {
		oreWeightsNether.put(tag, weight);
	}

	public static int getOreWeight(ResourceLocation tag) {
		return oreWeights.get(tag);
	}

	public static int getOreWeightNether(ResourceLocation tag) {
		return oreWeightsNether.get(tag);
	}

	/**
	 * Registers a Wiki provider for a mod so it uses that instead of the fallback
	 * FTB wiki.
	 */
	public static void registerModWiki(String mod, IWikiProvider provider) {
		WikiHooks.registerModWiki(mod, provider);
	}

	public static Class<? extends SubTileEntity> getSubTileMapping(ResourceLocation key) {
		return subTiles.get(key);
	}

	public static ResourceLocation getSubTileStringMapping(Class<? extends SubTileEntity> clazz) {
		return subTiles.getKey(clazz);
	}

	public static Set<ResourceLocation> getAllSubTiles() {
		return subTiles.keySet();
	}

	public static void blacklistBlockFromGaiaGuardian(Block block) {
		gaiaBreakBlacklist.add(block);
	}
}
