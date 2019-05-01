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
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IRegistryDelegate;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.internal.DummyMethodHandler;
import vazkii.botania.api.internal.IInternalMethodHandler;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.recipe.RecipeRuneAltar;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class BotaniaAPI {
	@CapabilityInject(IFloatingFlower.class)
	public static Capability<IFloatingFlower> FLOATING_FLOWER_CAP;

	private static final List<LexiconCategory> categories = new ArrayList<>();
	private static final List<LexiconEntry> allEntries = new ArrayList<>();

	public static final Map<String, KnowledgeType> knowledgeTypes = new HashMap<>();

	public static final Map<String, Brew> brewMap = new LinkedHashMap<>();

	public static final List<RecipePetals> petalRecipes = new ArrayList<>();
	public static final List<RecipePureDaisy> pureDaisyRecipes = new ArrayList<>();
	public static Map<ResourceLocation, RecipeManaInfusion> manaInfusionRecipes = Collections.emptyMap();
	public static final List<RecipeRuneAltar> runeAltarRecipes = new ArrayList<>();
	public static final List<RecipeElvenTrade> elvenTradeRecipes = new ArrayList<>();
	public static final List<RecipeBrew> brewRecipes = new ArrayList<>();
	public static final List<RecipeManaInfusion> miniFlowerRecipes = new ArrayList<>();

	public static Map<ResourceLocation, Integer> oreWeights = Collections.emptyMap();
	public static Map<ResourceLocation, Integer> oreWeightsNether = Collections.emptyMap();

	public static Map<IRegistryDelegate<Block>, Function<EnumDyeColor, Block>> paintableBlocks = Collections.emptyMap();
	public static final Set<Class<? extends Entity>> gravityRodBlacklist = new LinkedHashSet<>();

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
		basicKnowledge = registerKnowledgeType("minecraft", TextFormatting.RESET, true);
		elvenKnowledge = registerKnowledgeType("alfheim", TextFormatting.DARK_GREEN, false);
		relicKnowledge = registerKnowledgeType("relic", TextFormatting.DARK_PURPLE, false);
	}

	/**
	 * The internal method handler in use.
	 * <b>DO NOT OVERWRITE THIS OR YOU'RE GOING TO FEEL MY WRATH WHEN I UPDATE THE API.</b>
	 * The fact I have to write that means some moron already tried, don't be that moron.
	 * @see IInternalMethodHandler
	 */
	public static volatile IInternalMethodHandler internalHandler = new DummyMethodHandler();

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
		return new RecipeManaInfusion(new ResourceLocation("put", "idhere"), output, input, mana);
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

	public static int getOreWeight(ResourceLocation tag) {
		return oreWeights.get(tag);
	}

	public static int getOreWeightNether(ResourceLocation tag) {
		return oreWeightsNether.get(tag);
	}
}
