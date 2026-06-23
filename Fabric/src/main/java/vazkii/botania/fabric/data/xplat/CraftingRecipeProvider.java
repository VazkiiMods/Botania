/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */
package vazkii.botania.fabric.data.xplat;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.state.enums.CraftyCratePattern;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.crafting.recipe.*;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.ConventionalBotaniaTags;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.data.recipes.builder.BotaniaSpecialRecipeBuilder;
import vazkii.botania.data.recipes.builder.TiaraWingsRecipeBuilder;
import vazkii.botania.data.recipes.builder.WrapperRecipeBuilder;
import vazkii.botania.fabric.data.FabricDatagenInitializer;
import vazkii.botania.mixin.RecipeProviderAccessor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class CraftingRecipeProvider extends FabricRecipeProvider {

	public CraftingRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	public void buildRecipes(RecipeOutput exporter) {
		specialRecipe(exporter, AncientWillRecipe::new, CraftingBookCategory.EQUIPMENT);
		specialRecipe(exporter, BlackHoleTalismanExtractRecipe::new, CraftingBookCategory.MISC);
		specialRecipe(exporter, CompositeLensRecipe::new, CraftingBookCategory.REDSTONE);
		specialRecipe(exporter, CosmeticAttachRecipe::new, CraftingBookCategory.EQUIPMENT);
		specialRecipe(exporter, CosmeticRemoveRecipe::new, CraftingBookCategory.EQUIPMENT);
		specialRecipe(exporter, LaputaShardUpgradeRecipe::new, CraftingBookCategory.MISC);
		specialRecipe(exporter, LensDyeingRecipe::new, CraftingBookCategory.REDSTONE);
		specialRecipe(exporter, ManaBlasterClipRecipe::new, CraftingBookCategory.EQUIPMENT);
		specialRecipe(exporter, ManaBlasterLensRecipe::new, CraftingBookCategory.EQUIPMENT);
		specialRecipe(exporter, ManaBlasterRemoveLensRecipe::new, CraftingBookCategory.EQUIPMENT);
		specialRecipe(exporter, MergeVialRecipe::new, CraftingBookCategory.EQUIPMENT);
		specialRecipe(exporter, PhantomInkRecipe::new, CraftingBookCategory.EQUIPMENT);
		specialRecipe(exporter, ResoluteIvyRecipe::new, CraftingBookCategory.EQUIPMENT);
		specialRecipe(exporter, SpellbindingClothRecipe::new, CraftingBookCategory.EQUIPMENT);
		specialRecipe(exporter, SplitLensRecipe::new, CraftingBookCategory.REDSTONE);
		specialRecipe(exporter, TerraShattererTippingRecipe::new, CraftingBookCategory.EQUIPMENT);

		registerMain(exporter);
		registerMisc(exporter);
		registerTools(exporter);
		registerTrinkets(exporter);
		registerLenses(exporter);
		registerCorporeaAndRedString(exporter);
		registerFloatingFlowers(exporter);
		registerConversions(exporter);
		registerDecor(exporter);
	}

	public static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromItem(ItemLike item) {
		return RecipeProviderAccessor.botania_inventoryTrigger(ItemPredicate.Builder.item().of(item));
	}

	private static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromItems(ItemLike... items) {
		ItemPredicate.Builder[] preds = new ItemPredicate.Builder[items.length];
		for (int i = 0; i < items.length; i++) {
			preds[i] = ItemPredicate.Builder.item().of(items[i]);
		}

		return RecipeProviderAccessor.botania_inventoryTrigger(preds);
	}

	public static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromTag(TagKey<Item> tag) {
		return RecipeProviderAccessor.botania_inventoryTrigger(ItemPredicate.Builder.item().of(tag));
	}

	/** Addons: override this to return your modid */
	protected ResourceLocation prefix(String path) {
		return botaniaRL(path);
	}

	private void registerMain(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.MANA_SPREADER)
				.define('P', BotaniaTags.Items.PETALS)
				.define('W', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.define('C', ConventionalItemTags.COPPER_INGOTS)
				.pattern("WWW")
				.pattern("CP ")
				.pattern("WWW")
				.group("botania:spreader")
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.LIVINGWOOD_LOGS))
				.save(withConditions(recipeOutput, FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BotaniaBlocks.PULSE_MANA_SPREADER)
				.requires(BotaniaBlocks.MANA_SPREADER)
				.requires(ConventionalItemTags.REDSTONE_DUSTS)
				.group("botania:spreader")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.MANA_SPREADER))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.ELVEN_MANA_SPREADER)
				.define('P', BotaniaTags.Items.PETALS)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.define('W', BotaniaTags.Items.DREAMWOOD_LOGS)
				.pattern("WWW")
				.pattern("EP ")
				.pattern("WWW")
				.group("botania:spreader")
				.unlockedBy("has_elementium", conditionsFromTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS))
				.unlockedBy("has_dreamwood", conditionsFromTag(BotaniaTags.Items.DREAMWOOD_LOGS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BotaniaBlocks.GAIA_MANA_SPREADER)
				.requires(BotaniaBlocks.ELVEN_MANA_SPREADER)
				.requires(ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS)
				.requires(BotaniaItems.GAIA_SPIRIT)
				.group("botania:spreader")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.MANA_POOL)
				.define('R', BotaniaBlocks.LIVINGROCK)
				.pattern("R R")
				.pattern("RRR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LIVINGROCK))
				.save(recipeOutput);
		dyedPools(recipeOutput, BotaniaBlocks.MANA_POOL, "mana_pool_dyeing", BotaniaTags.Items.DYED_MANA_POOLS);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.DILUTED_MANA_POOL)
				.define('R', BotaniaBlocks.LIVINGROCK_SLAB)
				.pattern("R R")
				.pattern("RRR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LIVINGROCK))
				.save(recipeOutput);
		dyedPools(recipeOutput, BotaniaBlocks.DILUTED_MANA_POOL, "diluted_pool_dyeing", BotaniaTags.Items.DYED_DILUTED_MANA_POOLS);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.FABULOUS_MANA_POOL)
				.define('R', BotaniaBlocks.SHIMMERROCK)
				.pattern("R R")
				.pattern("RRR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.SHIMMERROCK))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.ROD_OF_THE_BIFROST))
				.save(recipeOutput);
		dyedPools(recipeOutput, BotaniaBlocks.FABULOUS_MANA_POOL, "fabulous_pool_dyeing", BotaniaTags.Items.DYED_FABULOUS_MANA_POOLS);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.FABULOUS_MANA_POOL)
				.define('P', BotaniaBlocks.MANA_POOL)
				.define('B', BotaniaBlocks.BIFROST)
				.pattern("BPB")
				.pattern("BBB")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.BIFROST))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.ROD_OF_THE_BIFROST))
				.save(recipeOutput, prefix(BuiltInRegistries.ITEM.getKey(BotaniaBlocks.FABULOUS_MANA_POOL.asItem()).getPath() + "_upgrade"));
		dyedPools(recipeOutput, BotaniaBlocks.CREATIVE_MANA_POOL, "creative_pool_dyeing", BotaniaTags.Items.DYED_CREATIVE_MANA_POOLS);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.RUNIC_ALTAR)
				.define('P', ConventionalBotaniaTags.Items.MANA_PEARL_GEMS)
				.define('S', BotaniaBlocks.LIVINGROCK)
				.pattern("SSS")
				.pattern("SPS")
				.group("botania:runic_altar")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.RUNIC_ALTAR)
				.define('P', ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				.define('S', BotaniaBlocks.LIVINGROCK)
				.pattern("SSS")
				.pattern("SPS")
				.group("botania:runic_altar")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS))
				.save(recipeOutput, prefix("runic_altar_alt"));
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.MANA_PYLON)
				.define('D', ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				.define('G', ConventionalItemTags.GOLD_INGOTS)
				.define('M', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern(" G ")
				.pattern("MDM")
				.pattern(" G ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.NATURA_PYLON)
				.define('P', BotaniaBlocks.MANA_PYLON)
				.define('T', ConventionalBotaniaTags.Items.TERRASTEEL_NUGGETS)
				.define('E', Items.ENDER_EYE)
				.pattern(" T ")
				.pattern("TPT")
				.pattern(" E ")
				.unlockedBy("has_pylon", conditionsFromItem(BotaniaBlocks.MANA_PYLON))
				.unlockedBy("has_ingot", conditionsFromTag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS))
				.unlockedBy("has_nugget", conditionsFromTag(ConventionalBotaniaTags.Items.TERRASTEEL_NUGGETS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.GAIA_PYLON)
				.define('P', BotaniaBlocks.MANA_PYLON)
				.define('D', ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.pattern(" D ")
				.pattern("EPE")
				.pattern(" D ")
				.unlockedBy("has_pixie_dust", conditionsFromTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS))
				.unlockedBy("has_elementium", conditionsFromTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.MANA_SPLITTER)
				.define('R', BotaniaBlocks.LIVINGROCK)
				.define('S', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("RRR")
				.pattern("S S")
				.pattern("RRR")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.MANA_VOID)
				.define('S', BotaniaBlocks.LIVINGROCK)
				.define('O', Items.OBSIDIAN)
				.pattern("SSS")
				.pattern("O O")
				.pattern("SSS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LIVINGROCK))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.MANA_DETECTOR)
				.define('R', ConventionalItemTags.REDSTONE_DUSTS)
				.define('T', Blocks.TARGET)
				.define('S', BotaniaBlocks.LIVINGROCK)
				.pattern("RSR")
				.pattern("STS")
				.pattern("RSR")
				.unlockedBy("has_target", conditionsFromItem(Blocks.TARGET))
				.unlockedBy("has_livingrock", conditionsFromItem(BotaniaBlocks.LIVINGROCK))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.SPREADER_TURNTABLE)
				.define('P', Items.STICKY_PISTON)
				.define('W', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.pattern("WWW")
				.pattern("WPW")
				.pattern("WWW")
				.unlockedBy("has_livingwood", conditionsFromTag(BotaniaTags.Items.LIVINGWOOD_LOGS))
				.unlockedBy("has_piston", conditionsFromItem(Items.STICKY_PISTON))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.TINY_PLANET)
				.define('P', BotaniaItems.TINY_PLANET)
				.define('S', Items.STONE)
				.pattern("SSS")
				.pattern("SPS")
				.pattern("SSS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.TINY_PLANET))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.ALCHEMY_CATALYST)
				.define('P', ConventionalBotaniaTags.Items.MANA_PEARL_GEMS)
				.define('B', Items.BREWING_STAND)
				.define('S', BotaniaBlocks.LIVINGROCK)
				.define('G', ConventionalItemTags.GOLD_INGOTS)
				.pattern("SGS")
				.pattern("BPB")
				.pattern("SGS")
				.unlockedBy("has_pearl", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS))
				.unlockedBy("has_brewing_stand", conditionsFromItem(Items.BREWING_STAND))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.OPEN_CRATE)
				.define('W', BotaniaBlocks.LIVINGWOOD_PLANKS)
				.pattern("WWW")
				.pattern("W W")
				.pattern("W W")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LIVINGWOOD_PLANKS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.CRAFTY_CRATE)
				.define('C', ConventionalItemTags.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
				.define('W', BotaniaBlocks.DREAMWOOD_PLANKS)
				.pattern("WCW")
				.pattern("W W")
				.pattern("W W")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.DREAMWOOD_PLANKS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.EYE_OF_THE_ANCIENTS)
				.define('S', BotaniaBlocks.LIVINGROCK)
				.define('E', Items.ENDER_EYE)
				.define('M', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("MSM")
				.pattern("SES")
				.pattern("MSM")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.DRUM_OF_THE_WILD)
				.define('W', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.define('H', BotaniaItems.HORN_OF_THE_WILD)
				.define('L', Items.LEATHER)
				.pattern("WLW")
				.pattern("WHW")
				.pattern("WLW")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.HORN_OF_THE_WILD))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.DRUM_OF_THE_GATHERING)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.define('W', BotaniaTags.Items.DREAMWOOD_LOGS)
				.define('L', Items.LEATHER)
				.pattern("WLW")
				.pattern("WEW")
				.pattern("WLW")
				.unlockedBy("has_elementium", conditionsFromTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS))
				.unlockedBy("has_dreamwood", conditionsFromTag(BotaniaTags.Items.DREAMWOOD_LOGS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.DRUM_OF_THE_CANOPY)
				.define('W', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.define('H', BotaniaItems.HORN_OF_THE_CANOPY)
				.define('L', Items.LEATHER)
				.pattern("WLW")
				.pattern("WHW")
				.pattern("WLW")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.HORN_OF_THE_CANOPY))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.ABSTRUSE_PLATFORM, 2)
				.define('0', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.define('P', ConventionalBotaniaTags.Items.MANA_PEARL_GEMS)
				.define('3', BotaniaBlocks.FRAMED_LIVINGWOOD)
				.define('4', BotaniaBlocks.PATTERN_FRAMED_LIVINGWOOD)
				.pattern("343")
				.pattern("0P0")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.SPECTRAL_PLATFORM, 2)
				.define('0', BotaniaTags.Items.DREAMWOOD_LOGS)
				.define('3', BotaniaBlocks.FRAMED_DREAMWOOD)
				.define('4', BotaniaBlocks.PATTERN_FRAMED_DREAMWOOD)
				.define('D', ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.pattern("343")
				.pattern("0D0")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.ELVEN_GATEWAY_CORE)
				.define('T', ConventionalBotaniaTags.Items.TERRASTEEL_NUGGETS)
				.define('W', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.pattern("WTW")
				.pattern("WTW")
				.pattern("WTW")
				.unlockedBy("has_ingot", conditionsFromTag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS))
				.unlockedBy("has_nugget", conditionsFromTag(ConventionalBotaniaTags.Items.TERRASTEEL_NUGGETS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.CONJURATION_CATALYST)
				.define('P', BotaniaBlocks.ALCHEMY_CATALYST)
				.define('B', ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.define('S', BotaniaBlocks.LIVINGROCK)
				.define('G', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.pattern("SBS")
				.pattern("GPG")
				.pattern("SGS")
				.unlockedBy("has_pixie_dust", conditionsFromTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS))
				.unlockedBy("has_elementium", conditionsFromTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.LIFE_IMBUER)
				.define('P', Items.PRISMARINE_BRICKS)
				.define('B', Items.BLAZE_ROD)
				.define('S', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.define('E', BotaniaItems.ENDER_AIR_BOTTLE)
				.define('M', ConventionalBotaniaTags.Items.MANASTEEL_STORAGE_BLOCKS)
				.pattern("BSB")
				.pattern("PMP")
				.pattern("PEP")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.ENDER_AIR_BOTTLE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.ENDER_OVERSEER)
				.define('R', ConventionalItemTags.REDSTONE_DUSTS)
				.define('E', Items.ENDER_EYE)
				.define('O', Items.OBSIDIAN)
				.pattern("RER")
				.pattern("EOE")
				.pattern("RER")
				.unlockedBy("has_item", conditionsFromItem(Items.ENDER_EYE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.STARFIELD_CREATOR)
				.define('P', ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.define('O', Items.OBSIDIAN)
				.pattern("EPE")
				.pattern("EOE")
				.unlockedBy("has_pixie_dust", conditionsFromTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS))
				.unlockedBy("has_elementium", conditionsFromTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.MANA_FLUXFIELD)
				.define('R', ConventionalItemTags.STORAGE_BLOCKS_REDSTONE)
				.define('S', BotaniaBlocks.LIVINGROCK)
				.define('M', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("SRS")
				.pattern("RMR")
				.pattern("SRS")
				.unlockedBy("has_redstone_block", conditionsFromTag(ConventionalItemTags.STORAGE_BLOCKS_REDSTONE))
				.unlockedBy("has_manasteel", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.BOTANICAL_BREWERY)
				.define('A', BotaniaItems.RUNE_OF_MANA)
				.define('R', BotaniaBlocks.LIVINGROCK)
				.define('S', Items.BREWING_STAND)
				.define('M', ConventionalBotaniaTags.Items.MANASTEEL_STORAGE_BLOCKS)
				.pattern("RSR")
				.pattern("RAR")
				.pattern("RMR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.RUNE_OF_MANA))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.BREWING_STAND))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.TERRESTRIAL_AGGLOMERATION_PLATE)
				.define('0', BotaniaItems.RUNE_OF_WATER)
				.define('1', BotaniaItems.RUNE_OF_FIRE)
				.define('2', BotaniaItems.RUNE_OF_EARTH)
				.define('3', BotaniaItems.RUNE_OF_AIR)
				.define('8', BotaniaItems.RUNE_OF_MANA)
				.define('L', ConventionalItemTags.STORAGE_BLOCKS_LAPIS)
				.define('M', BotaniaBlocks.MANA_QUARTZ_BLOCK)
				.pattern("LLL")
				.pattern("0M1")
				.pattern("283")
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.RUNES))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.MANA_PRISM)
				.define('P', ConventionalItemTags.PRISMARINE_GEMS)
				.define('S', BotaniaBlocks.SPECTRAL_PLATFORM)
				.define('G', ConventionalItemTags.GLASS_BLOCKS_COLORLESS)
				.pattern("GPG")
				.pattern("GSG")
				.pattern("GPG")
				.unlockedBy("has_prismarine", conditionsFromTag(ConventionalItemTags.PRISMARINE_GEMS))
				.unlockedBy("has_platform", conditionsFromItem(BotaniaBlocks.SPECTRAL_PLATFORM))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.MANA_PUMP)
				.define('B', Items.BUCKET)
				.define('S', BotaniaBlocks.LIVINGROCK)
				.define('I', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("SSS")
				.pattern("IBI")
				.pattern("SSS")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.INCENSE_PLATE)
				.define('S', BotaniaBlocks.LIVINGWOOD_SLAB)
				.define('W', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.pattern("SSW")
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.LIVINGWOOD_LOGS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.HOVERING_HOURGLASS)
				.define('R', ConventionalItemTags.REDSTONE_DUSTS)
				.define('S', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.define('G', ConventionalItemTags.GOLD_INGOTS)
				.define('M', BotaniaBlocks.MANAGLASS)
				.pattern("GMG")
				.pattern("RSR")
				.pattern("GMG")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.MANAGLASS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, BotaniaBlocks.SPECTRAL_RAIL)
				.requires(Items.RAIL)
				.requires(BotaniaBlocks.SPECTRAL_PLATFORM)
				.unlockedBy("has_item", conditionsFromItem(Items.RAIL))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaBlocks.SPECTRAL_PLATFORM))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.SPARK_TINKERER)
				.define('R', ConventionalItemTags.REDSTONE_DUSTS)
				.define('S', BotaniaBlocks.LIVINGROCK)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.pattern("ESE")
				.pattern("SRS")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.FEL_PUMPKIN)
				.define('P', Items.PUMPKIN)
				.define('B', Items.BONE)
				.define('S', Items.STRING)
				.define('F', Items.ROTTEN_FLESH)
				.define('G', Items.GUNPOWDER)
				.pattern(" S ")
				.pattern("BPF")
				.pattern(" G ")
				.unlockedBy("has_item", conditionsFromItem(Items.PUMPKIN))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaBlocks.COCOON_OF_CAPRICE)
				.define('S', Items.STRING)
				.define('C', BotaniaItems.MANAWEAVE_CLOTH)
				.define('P', BotaniaBlocks.FEL_PUMPKIN)
				.define('D', ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.pattern("SSS")
				.pattern("CPC")
				.pattern("SDS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.FEL_PUMPKIN))
				.save(withConditions(recipeOutput, FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, BotaniaBlocks.LUMINIZER)
				.requires(BotaniaItems.RED_STRING)
				.requires(ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS)
				.requires(ConventionalItemTags.GLOWSTONE_DUSTS)
				.requires(ConventionalItemTags.GLOWSTONE_DUSTS)
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, BotaniaBlocks.DETECTOR_LUMINIZER)
				.requires(BotaniaBlocks.LUMINIZER)
				.requires(ConventionalItemTags.REDSTONE_DUSTS)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LUMINIZER))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, BotaniaBlocks.FORK_LUMINIZER)
				.requires(BotaniaBlocks.LUMINIZER)
				.requires(Items.REDSTONE_TORCH)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LUMINIZER))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, BotaniaBlocks.TOGGLE_LUMINIZER)
				.requires(BotaniaBlocks.LUMINIZER)
				.requires(Items.LEVER)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LUMINIZER))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.LUMINIZER_LAUNCHER)
				.define('D', BotaniaTags.Items.DREAMWOOD_LOGS)
				.define('L', BotaniaBlocks.LUMINIZER)
				.pattern("DDD")
				.pattern("DLD")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LUMINIZER))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaBlocks.MANASTORM_CHARGE)
				.define('T', Items.TNT)
				.define('G', BotaniaItems.GAIA_SPIRIT)
				.define('L', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.pattern("LTL")
				.pattern("TGT")
				.pattern("LTL")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.MANATIDE_BELLOWS)
				.define('R', BotaniaItems.RUNE_OF_AIR)
				.define('S', BotaniaBlocks.LIVINGWOOD_SLAB)
				.define('L', Items.LEATHER)
				.pattern("SSS")
				.pattern("RL ")
				.pattern("SSS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.RUNE_OF_AIR))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.BIFROST)
				.requires(BotaniaItems.ROD_OF_THE_BIFROST)
				.requires(BotaniaBlocks.ALFGLASS)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.ROD_OF_THE_BIFROST))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, BotaniaBlocks.CELLULAR_BLOCK, 3)
				.requires(Items.CACTUS, 3)
				.requires(Items.BEETROOT)
				.requires(Items.CARROT)
				.requires(Items.POTATO)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.DANDELIFEON))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.TERU_TERU_BOZU)
				.define('C', BotaniaItems.MANAWEAVE_CLOTH)
				.define('S', Items.SUNFLOWER)
				.pattern("C")
				.pattern("C")
				.pattern("S")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANAWEAVE_CLOTH))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.LIVINGWOOD_AVATAR)
				.define('D', ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				.define('W', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.pattern(" W ")
				.pattern("WDW")
				.pattern("W W")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.ANIMATED_TORCH)
				.define('D', ConventionalBotaniaTags.Items.MANA_DUSTS)
				.define('T', Items.REDSTONE_TORCH)
				.pattern("D")
				.pattern("T")
				.unlockedBy("has_torch", conditionsFromItem(Items.REDSTONE_TORCH))
				.unlockedBy("has_mana_dust", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_DUSTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.LIVINGWOOD_TWIG)
				.define('W', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.pattern(" W")
				.pattern("W ")
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.LIVINGWOOD_LOGS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.REDSTONE_ROOT)
				.requires(ConventionalItemTags.REDSTONE_DUSTS)
				.requires(Ingredient.of(Items.FERN, Items.SHORT_GRASS))
				.unlockedBy("has_item", conditionsFromTag(ConventionalItemTags.REDSTONE_DUSTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.DREAMWOOD_TWIG)
				.define('W', BotaniaTags.Items.DREAMWOOD_LOGS)
				.pattern(" W")
				.pattern("W ")
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.DREAMWOOD_LOGS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.GAIA_INGOT)
				.define('S', BotaniaItems.GAIA_SPIRIT)
				.define('I', ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS)
				.pattern(" S ")
				.pattern("SIS")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.MANAWEAVE_CLOTH)
				.define('S', BotaniaItems.MANA_INFUSED_STRING)
				.pattern("SS")
				.pattern("SS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_INFUSED_STRING))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.FLORAL_FERTILIZER)
				.requires(Items.BONE_MEAL)
				.requires(Ingredient.of(ConventionalItemTags.DYES), 4)
				.unlockedBy("has_bonemeal", has(Items.BONE_MEAL))
				.unlockedBy("has_any_dye", has(ConventionalItemTags.DYES))
				.save(withConditions(recipeOutput, FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.DRY_SEEDS)
				.requires(BotaniaItems.PASTURE_SEEDS)
				.requires(Items.DEAD_BUSH)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.PASTURE_SEEDS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.GOLDEN_SEEDS)
				.requires(BotaniaItems.PASTURE_SEEDS)
				.requires(Items.WHEAT)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.PASTURE_SEEDS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.VIVID_SEEDS)
				.requires(BotaniaItems.PASTURE_SEEDS)
				.requires(Items.GREEN_DYE)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.PASTURE_SEEDS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.SCORCHED_SEEDS)
				.requires(BotaniaItems.PASTURE_SEEDS)
				.requires(Items.BLAZE_POWDER)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.PASTURE_SEEDS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.INFUSED_SEEDS)
				.requires(BotaniaItems.PASTURE_SEEDS)
				.requires(Items.PRISMARINE_SHARD)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.PASTURE_SEEDS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.MUTATED_SEEDS)
				.requires(BotaniaItems.PASTURE_SEEDS)
				.requires(Items.SPIDER_EYE)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.PASTURE_SEEDS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.DARK_QUARTZ, 8)
				.define('Q', Items.QUARTZ)
				.define('C', Ingredient.of(Items.COAL, Items.CHARCOAL))
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.BLAZE_QUARTZ, 8)
				.define('Q', Items.QUARTZ)
				.define('C', Items.BLAZE_POWDER)
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.LAVENDER_QUARTZ, 8)
				.define('Q', Items.QUARTZ)
				.define('C', Ingredient.of(Items.ALLIUM, Items.PINK_TULIP, Items.LILAC, Items.PEONY))
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.RED_QUARTZ, 8)
				.define('Q', Items.QUARTZ)
				.define('C', ConventionalItemTags.REDSTONE_DUSTS)
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.SUNNY_QUARTZ, 8)
				.define('Q', Items.QUARTZ)
				.define('C', Items.SUNFLOWER)
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.VINE_BALL)
				.define('V', Items.VINE)
				.pattern("VVV")
				.pattern("VVV")
				.pattern("VVV")
				.unlockedBy("has_item", conditionsFromItem(Items.VINE))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, BotaniaItems.NECRODERMAL_VIRUS)
				.requires(ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.requires(BotaniaItems.VINE_BALL)
				.requires(Items.MAGMA_CREAM)
				.requires(Items.FERMENTED_SPIDER_EYE)
				.requires(Items.ENDER_EYE)
				.requires(Items.ZOMBIE_HEAD)
				.unlockedBy("has_pixie_dust", conditionsFromTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS))
				.unlockedBy("has_head", conditionsFromItem(Items.ZOMBIE_HEAD))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, BotaniaItems.NULLODERMAL_VIRUS)
				.requires(ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.requires(BotaniaItems.VINE_BALL)
				.requires(Items.MAGMA_CREAM)
				.requires(Items.FERMENTED_SPIDER_EYE)
				.requires(Items.ENDER_EYE)
				.requires(Items.SKELETON_SKULL)
				.unlockedBy("has_pixie_dust", conditionsFromTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS))
				.unlockedBy("has_head", conditionsFromItem(Items.SKELETON_SKULL))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaItems.SPARK)
				.define('P', BotaniaTags.Items.PETALS)
				.define('B', Items.BLAZE_POWDER)
				.define('N', ConventionalItemTags.GOLD_NUGGETS)
				.pattern(" P ")
				.pattern("BNB")
				.pattern(" P ")
				.unlockedBy("has_item", conditionsFromItem(Items.BLAZE_POWDER))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.SPARK_AUGMENT_DISPERSIVE)
				.requires(ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.requires(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.requires(BotaniaItems.RUNE_OF_WATER)
				.group("botania:spark_augment")
				.unlockedBy("has_pixie_dust", conditionsFromTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS))
				.unlockedBy("has_spark", conditionsFromItem(BotaniaItems.SPARK))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.SPARK_AUGMENT_DOMINANT)
				.requires(ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.requires(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.requires(BotaniaItems.RUNE_OF_FIRE)
				.group("botania:spark_augment")
				.unlockedBy("has_pixie_dust", conditionsFromTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS))
				.unlockedBy("has_spark", conditionsFromItem(BotaniaItems.SPARK))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.SPARK_AUGMENT_RECESSIVE)
				.requires(ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.requires(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.requires(BotaniaItems.RUNE_OF_EARTH)
				.group("botania:spark_augment")
				.unlockedBy("has_pixie_dust", conditionsFromTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS))
				.unlockedBy("has_spark", conditionsFromItem(BotaniaItems.SPARK))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.SPARK_AUGMENT_ISOLATED)
				.requires(ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.requires(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.requires(BotaniaItems.RUNE_OF_AIR)
				.group("botania:spark_augment")
				.unlockedBy("has_pixie_dust", conditionsFromTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS))
				.unlockedBy("has_spark", conditionsFromItem(BotaniaItems.SPARK))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.MANAGLASS_VIAL, 3)
				.define('G', BotaniaBlocks.MANAGLASS)
				.pattern("G G")
				.pattern(" G ")
				.unlockedBy("has_glass", conditionsFromItem(BotaniaBlocks.MANAGLASS))
				.unlockedBy("has_brewery", conditionsFromItem(BotaniaBlocks.BOTANICAL_BREWERY))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.ALFGLASS_FLASK, 3)
				.define('G', BotaniaBlocks.ALFGLASS)
				.pattern("G G")
				.pattern(" G ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.ALFGLASS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.WORLD_SEED, 4)
				.define('S', Items.WHEAT_SEEDS)
				.define('D', ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS)
				.define('G', Items.GRASS_BLOCK)
				.pattern("G")
				.pattern("S")
				.pattern("D")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BotaniaItems.THORN_CHAKRAM, 2)
				.define('T', ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS)
				.define('V', Items.VINE)
				.pattern("VVV")
				.pattern("VTV")
				.pattern("VVV")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BotaniaItems.FLARE_CHAKRAM, 2)
				.define('P', ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.define('B', Items.BLAZE_POWDER)
				.define('C', BotaniaItems.THORN_CHAKRAM)
				.pattern("BBB")
				.pattern("CPC")
				.pattern("BBB")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.THORN_CHAKRAM))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.PHANTOM_INK, 4)
				.requires(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS)
				.requires(Ingredient.of(
						ColorHelper.supportedColors().map(DyeItem::byColor).toArray(ItemLike[]::new)
				))
				.requires(Ingredient.of(ConventionalItemTags.GLASS_BLOCKS_CHEAP))
				.requires(Items.GLASS_BOTTLE, 4)
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.RESOLUTE_IVY)
				.requires(ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.requires(Items.VINE)
				.requires(BotaniaItems.ENDER_AIR_BOTTLE)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.ENDER_AIR_BOTTLE))
				.save(recipeOutput);

	}

	private static void dyedPools(RecipeOutput recipeOutput, ManaPoolBlock basePool, String groupName, TagKey<Item> dyedPoolsTag) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, basePool)
				.requires(dyedPoolsTag)
				.requires(BotaniaTags.Items.MANA_POOL_DYE_REMOVER)
				.group(groupName)
				.unlockedBy("has_dyed", conditionsFromTag(dyedPoolsTag))
				.save(recipeOutput, BuiltInRegistries.BLOCK.getKey(basePool).withSuffix("_undyed"));
		ColorHelper.supportedColors().forEach(color -> {
			ManaPoolBlock dyedBlock = BotaniaBlocks.findOptionallyDyedBlock(basePool, color);
			ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, dyedBlock)
					.requires(basePool)
					.requires(BotaniaTags.Items.getPetalTag(color))
					.group(groupName)
					.unlockedBy("has_item", conditionsFromItem(basePool))
					.save(recipeOutput);
		});
	}

	private void registerMisc(RecipeOutput recipeOutput) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.MUSHROOM_STEW)
				.requires(Ingredient.of(BotaniaTags.Items.SHIMMERING_MUSHROOMS), 2)
				.requires(Items.BOWL)
				.unlockedBy("has_item", conditionsFromItem(Items.BOWL))
				.unlockedBy("has_orig_recipe", RecipeUnlockedTrigger.unlocked(ResourceLocation.withDefaultNamespace("mushroom_stew")))
				.save(recipeOutput, "botania:mushroom_stew");

		WrapperRecipeBuilder.wrap(LexiconReturningShapelessRecipe.SERIALIZER,
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.BANNER_PATTERN_BOTANIA)
						.requires(Items.PAPER)
						.requires(BotaniaItems.LEXICA_BOTANIA)
						.requires(BotaniaBlocks.TINY_POTATO)
						.requires(BotaniaItems.TERRASTEEL_INGOT)
						.unlockedBy("has_potato", conditionsFromItem(BotaniaBlocks.TINY_POTATO))
						.unlockedBy("has_terrasteel", conditionsFromItem(BotaniaItems.TERRASTEEL_INGOT))
		).save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.BANNER_PATTERN_MATERIALS)
				.requires(Items.PAPER)
				.requires(ConventionalItemTags.RAW_FISH_FOODS)
				.requires(BotaniaTags.Items.SMALL_MYSTICAL_FLOWERS)
				.requires(ItemTags.SAPLINGS)
				.unlockedBy("has_fish", conditionsFromTag(ConventionalItemTags.RAW_FISH_FOODS))
				.unlockedBy("has_flower", conditionsFromTag(BotaniaTags.Items.SMALL_MYSTICAL_FLOWERS))
				.unlockedBy("has_sapling", conditionsFromTag(ItemTags.SAPLINGS))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.BANNER_PATTERN_SPARK_AUGMENTS)
				.requires(Items.PAPER)
				.requires(Ingredient.of(
						BotaniaItems.SPARK_AUGMENT_DISPERSIVE, BotaniaItems.SPARK_AUGMENT_DOMINANT,
						BotaniaItems.SPARK_AUGMENT_ISOLATED, BotaniaItems.SPARK_AUGMENT_RECESSIVE
				))
				.unlockedBy("has_dispersive", conditionsFromItem(BotaniaItems.SPARK_AUGMENT_DISPERSIVE))
				.unlockedBy("has_dominant", conditionsFromItem(BotaniaItems.SPARK_AUGMENT_DOMINANT))
				.unlockedBy("has_isolated", conditionsFromItem(BotaniaItems.SPARK_AUGMENT_ISOLATED))
				.unlockedBy("has_recessive", conditionsFromItem(BotaniaItems.SPARK_AUGMENT_RECESSIVE))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.BANNER_PATTERN_TOOLS)
				.requires(Items.PAPER)
				.requires(Ingredient.of(
						BotaniaItems.MANASTEEL_AXE, BotaniaItems.MANASTEEL_HOE, BotaniaItems.MANASTEEL_PICKAXE,
						BotaniaItems.MANASTEEL_SHOVEL, BotaniaItems.MANASTEEL_SWORD
				))
				.unlockedBy("has_axe", conditionsFromItem(BotaniaItems.MANASTEEL_AXE))
				.unlockedBy("has_hoe", conditionsFromItem(BotaniaItems.MANASTEEL_HOE))
				.unlockedBy("has_pickaxe", conditionsFromItem(BotaniaItems.MANASTEEL_PICKAXE))
				.unlockedBy("has_shovel", conditionsFromItem(BotaniaItems.MANASTEEL_SHOVEL))
				.unlockedBy("has_sword", conditionsFromItem(BotaniaItems.MANASTEEL_SWORD))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.COBWEB)
				.define('S', Items.STRING)
				.define('M', BotaniaItems.MANA_INFUSED_STRING)
				.pattern("S S")
				.pattern(" M ")
				.pattern("S S")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_INFUSED_STRING))
				.save(recipeOutput, prefix("cobweb"));

		petalApothecary(Items.COBBLESTONE, BotaniaBlocks.PETAL_APOTHECARY)
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.PETALS))
				.save(recipeOutput);
		petalApothecary(Items.MOSSY_COBBLESTONE, BotaniaBlocks.PETAL_APOTHECARY_MOSSY)
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.PETALS))
				.save(recipeOutput);
		petalApothecary(BotaniaBlocks.LIVINGROCK, BotaniaBlocks.PETAL_APOTHECARY_LIVINGROCK)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LIVINGROCK))
				.save(recipeOutput);
		petalApothecary(Items.COBBLED_DEEPSLATE, BotaniaBlocks.PETAL_APOTHECARY_DEEPSLATE)
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.PETALS))
				.save(recipeOutput);
		petalApothecary(Items.BLACKSTONE, BotaniaBlocks.PETAL_APOTHECARY_BLACKSTONE)
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.PETALS))
				.save(recipeOutput);
		petalApothecary(Items.NETHER_BRICKS, BotaniaBlocks.PETAL_APOTHECARY_NETHER_BRICKS)
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.PETALS))
				.save(recipeOutput);
		petalApothecary(Items.RED_NETHER_BRICKS, BotaniaBlocks.PETAL_APOTHECARY_RED_NETHER_BRICKS)
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.PETALS))
				.save(recipeOutput);
		for (String metamorphicVariant : LibBlockNames.METAMORPHIC_VARIANTS) {
			Block apothecary = getBlockOrThrow(prefix(metamorphicVariant + LibBlockNames.PETAL_APOTHECARY_SUFFIX));
			Block cobble = getBlockOrThrow(prefix(LibBlockNames.COBBLED_PREFIX + metamorphicVariant));
			petalApothecary(cobble, apothecary)
					.group("botania:metamorphic_apothecary")
					.unlockedBy("has_item", conditionsFromItem(cobble))
					.unlockedBy("has_flower_item", conditionsFromItem(BotaniaBlocks.MARIMORPHOSIS))
					.save(recipeOutput);
		}
		ColorHelper.supportedColors().forEach(color -> {
			ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, BotaniaBlocks.getGlimmeringFlower(color))
					.requires(ConventionalItemTags.GLOWSTONE_DUSTS)
					.requires(ConventionalItemTags.GLOWSTONE_DUSTS)
					.requires(BotaniaBlocks.getMysticalFlower(color))
					.group("botania:glimmering_flower")
					.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.getMysticalFlower(color)))
					.save(recipeOutput);
			ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.getFloatingFlower(color))
					.define('S', BotaniaItems.PASTURE_SEEDS)
					.define('D', Items.DIRT)
					.define('F', BotaniaBlocks.getGlimmeringFlower(color))
					.pattern("F")
					.pattern("S")
					.pattern("D")
					.group("botania:floating_flowers")
					.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.getGlimmeringFlower(color)))
					.save(recipeOutput);
			ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaBlocks.getPetalBlock(color))
					.define('P', BotaniaItems.getPetal(color))
					.pattern("PPP")
					.pattern("PPP")
					.pattern("PPP")
					.group("botania:petal_block")
					.unlockedBy("has_item", conditionsFromItem(BotaniaItems.getPetal(color)))
					.save(recipeOutput);
			ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, BotaniaBlocks.getShimmeringMushroom(color))
					.requires(Ingredient.of(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM))
					.requires(DyeItem.byColor(color))
					.group("botania:mushroom")
					.unlockedBy("has_item", conditionsFromItem(Items.RED_MUSHROOM))
					.unlockedBy("has_alt_item", conditionsFromItem(Items.BROWN_MUSHROOM))
					.save(recipeOutput);
			ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.getPetal(color), 4)
					.requires(BotaniaBlocks.getTallMysticalFlower(color))
					.group("botania:petal")
					.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.getTallMysticalFlower(color)))
					.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.getPetal(color)))
					.save(recipeOutput, "botania:" + getConversionRecipeName(
							BotaniaItems.getPetal(color), BotaniaBlocks.getTallMysticalFlower(color)));
			ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.getPetal(color), 2)
					.requires(BotaniaBlocks.getMysticalFlower(color))
					.group("botania:petal")
					.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.getMysticalFlower(color)))
					.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.getPetal(color)))
					.save(recipeOutput, "botania:" + getConversionRecipeName(
							BotaniaItems.getPetal(color), BotaniaBlocks.getMysticalFlower(color)));
			ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DyeItem.byColor(color))
					.requires(Ingredient.of(BotaniaTags.Items.getPetalTag(color)))
					.group("botania:dye")
					.unlockedBy("has_item", conditionsFromItem(BotaniaItems.getPetal(color)))
					.save(recipeOutput, "botania:dye_" + color.getName());
		});
	}

	private void registerTools(RecipeOutput recipeOutput) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, BotaniaItems.LEXICA_BOTANIA)
				.requires(ItemTags.SAPLINGS)
				.requires(Items.BOOK)
				.unlockedBy("has_item", conditionsFromTag(ItemTags.SAPLINGS))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.BOOK))
				.save(recipeOutput);
		WrapperRecipeBuilder.wrap(WandOfTheForestRecipe.SERIALIZER,
				ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.WAND_OF_THE_FOREST)
						.define('P', BotaniaTags.Items.PETALS)
						.define('S', BotaniaItems.LIVINGWOOD_TWIG)
						.pattern(" PS")
						.pattern(" SP")
						.pattern("S  ")
						.group("botania:wand_of_the_forest")
						.unlockedBy("has_item", conditionsFromItem(BotaniaItems.LIVINGWOOD_TWIG))
		).save(recipeOutput);
		WrapperRecipeBuilder.wrap(WandOfTheForestRecipe.SERIALIZER,
				ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.WAND_OF_THE_ELVEN_FOREST)
						.define('P', BotaniaTags.Items.PETALS)
						.define('S', BotaniaItems.DREAMWOOD_TWIG)
						.pattern(" PS")
						.pattern(" SP")
						.pattern("S  ")
						.group("botania:wand_of_the_forest")
						.unlockedBy("has_item", conditionsFromItem(BotaniaItems.DREAMWOOD_TWIG))
		).save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.MANA_TABLET)
				.define('P', ConventionalBotaniaTags.Items.MANA_PEARL_GEMS)
				.define('S', BotaniaBlocks.LIVINGROCK)
				.pattern("SSS")
				.pattern("SPS")
				.pattern("SSS")
				.group("botania:mana_tablet")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.MANA_TABLET)
				.define('P', ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				.define('S', BotaniaBlocks.LIVINGROCK)
				.pattern("SSS")
				.pattern("SPS")
				.pattern("SSS")
				.group("botania:mana_tablet")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS))
				.save(recipeOutput, prefix("mana_tablet_alt"));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.CACOPHONIUM)
				.define('N', Items.NOTE_BLOCK)
				.define('G', ConventionalItemTags.COPPER_INGOTS)
				.pattern(" G ")
				.pattern("GNG")
				.pattern("GG ")
				.unlockedBy("has_item", conditionsFromItem(Items.NOTE_BLOCK))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.HORN_OF_THE_WILD)
				.define('S', BotaniaItems.PASTURE_SEEDS)
				.define('W', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.pattern(" W ")
				.pattern("WSW")
				.pattern("WW ")
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.LIVINGWOOD_LOGS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, BotaniaItems.HORN_OF_THE_CANOPY)
				.requires(BotaniaItems.HORN_OF_THE_WILD)
				.requires(ItemTags.LEAVES)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.HORN_OF_THE_WILD))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, BotaniaItems.HORN_OF_THE_COVERING)
				.requires(BotaniaItems.HORN_OF_THE_WILD)
				.requires(Items.SNOWBALL)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.HORN_OF_THE_WILD))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.MANA_MIRROR)
				.define('P', ConventionalBotaniaTags.Items.MANA_PEARL_GEMS)
				.define('R', BotaniaBlocks.LIVINGROCK)
				.define('S', BotaniaItems.LIVINGWOOD_TWIG)
				.define('T', BotaniaItems.MANA_TABLET)
				.define('I', ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS)
				.pattern(" PR")
				.pattern(" SI")
				.pattern("T  ")
				.unlockedBy("has_tablet", conditionsFromItem(BotaniaItems.MANA_TABLET))
				.unlockedBy("has_terrasteel", conditionsFromTag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.EXTRAPOLATED_BUCKET)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.pattern("E E")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.LIFE_AGGREGATOR)
				.define('A', BotaniaItems.ENDER_AIR_BOTTLE)
				.define('D', ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS)
				.define('E', BotaniaItems.GAIA_SPIRIT)
				.define('I', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.pattern("EIE")
				.pattern("ADA")
				.pattern("EIE")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.LIVINGWOOD_SLINGSHOT)
				.define('A', BotaniaItems.RUNE_OF_AIR)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.pattern(" TA")
				.pattern(" TT")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.RUNE_OF_AIR))
				.save(recipeOutput);

		registerSimpleArmorSet(recipeOutput, Ingredient.of(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS), "manasteel", conditionsFromTag(
				ConventionalBotaniaTags.Items.MANASTEEL_INGOTS));
		registerSimpleArmorSet(recipeOutput, Ingredient.of(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS), "elementium", conditionsFromTag(
				ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS));
		registerSimpleArmorSet(recipeOutput, Ingredient.of(BotaniaItems.MANAWEAVE_CLOTH), "manaweave", conditionsFromItem(BotaniaItems.MANAWEAVE_CLOTH));

		registerTerrasteelUpgradeRecipe(recipeOutput, BotaniaItems.TERRASTEEL_HELMET,
				BotaniaItems.MANASTEEL_HELMET, BotaniaItems.RUNE_OF_SPRING);
		registerTerrasteelUpgradeRecipe(recipeOutput, BotaniaItems.TERRASTEEL_CHESTPLATE,
				BotaniaItems.MANASTEEL_CHESTPLATE, BotaniaItems.RUNE_OF_SUMMER);
		registerTerrasteelUpgradeRecipe(recipeOutput, BotaniaItems.TERRASTEEL_LEGGINGS,
				BotaniaItems.MANASTEEL_LEGGINGS, BotaniaItems.RUNE_OF_AUTUMN);
		registerTerrasteelUpgradeRecipe(recipeOutput, BotaniaItems.TERRASTEEL_BOOTS,
				BotaniaItems.MANASTEEL_BOOTS, BotaniaItems.RUNE_OF_WINTER);

		registerToolSetRecipes(recipeOutput, Ingredient.of(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS),
				Ingredient.of(BotaniaItems.LIVINGWOOD_TWIG),
				conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS), BotaniaItems.MANASTEEL_SWORD,
				BotaniaItems.MANASTEEL_PICKAXE, BotaniaItems.MANASTEEL_AXE, BotaniaItems.MANASTEEL_HOE,
				BotaniaItems.MANASTEEL_SHOVEL, BotaniaItems.MANASTEEL_SHEARS
		);
		registerToolSetRecipes(recipeOutput, Ingredient.of(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS),
				Ingredient.of(BotaniaItems.DREAMWOOD_TWIG),
				conditionsFromTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS), BotaniaItems.ELEMENTIUM_SWORD,
				BotaniaItems.ELEMENTIUM_PICKAXE, BotaniaItems.ELEMENTIUM_AXE, BotaniaItems.ELEMENTIUM_HOE,
				BotaniaItems.ELEMENTIUM_SHOVEL, BotaniaItems.ELEMENTIUM_SHEARS
		);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BotaniaItems.TERRA_BLADE)
				.define('S', BotaniaItems.LIVINGWOOD_TWIG)
				.define('I', ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS)
				.pattern("I")
				.pattern("I")
				.pattern("S")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS))
				.save(recipeOutput);
		WrapperRecipeBuilder.wrap(ManaUpgradeRecipe.SERIALIZER,
				ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.TERRA_SHATTERER)
						.define('T', BotaniaItems.MANA_TABLET)
						.define('I', ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS)
						.define('L', BotaniaItems.LIVINGWOOD_TWIG)
						.pattern("ITI")
						.pattern("ILI")
						.pattern(" L ")
						.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS))
		).save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.TERRA_TRUNCATOR)
				.define('S', BotaniaItems.LIVINGWOOD_TWIG)
				.define('T', ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS)
				.define('G', Items.GLOWSTONE)
				.pattern("TTG")
				.pattern("TST")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BotaniaItems.STARCALLER)
				.define('A', BotaniaItems.ENDER_AIR_BOTTLE)
				.define('D', ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS)
				.define('T', BotaniaItems.TERRA_BLADE)
				.define('I', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.pattern("  I")
				.pattern("AD ")
				.pattern("TA ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.TERRA_BLADE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BotaniaItems.THUNDERCALLER)
				.define('A', BotaniaItems.ENDER_AIR_BOTTLE)
				.define('D', ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				.define('T', BotaniaItems.TERRA_BLADE)
				.define('I', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.pattern("  I")
				.pattern("AD ")
				.pattern("TA ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.TERRA_BLADE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.VITREOUS_PICKAXE)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.define('G', ConventionalItemTags.GLASS_BLOCKS_COLORLESS)
				.define('I', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("GIG")
				.pattern(" T ")
				.pattern(" T ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BotaniaItems.LIVINGWOOD_BOW)
				.define('S', BotaniaItems.MANA_INFUSED_STRING)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.pattern(" TS")
				.pattern("T S")
				.pattern(" TS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_INFUSED_STRING))
				.unlockedBy("has_twig", conditionsFromItem(BotaniaItems.LIVINGWOOD_TWIG))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BotaniaItems.CRYSTAL_BOW)
				.define('S', BotaniaItems.MANA_INFUSED_STRING)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.define('D', ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS)
				.pattern(" DS")
				.pattern("T S")
				.pattern(" DS")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BotaniaItems.SOULSCRIBE)
				.define('P', ConventionalBotaniaTags.Items.MANA_PEARL_GEMS)
				.define('S', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.pattern("P")
				.pattern("S")
				.pattern("T")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.HAND_OF_ENDER)
				.define('P', ConventionalBotaniaTags.Items.MANA_PEARL_GEMS)
				.define('E', Items.ENDER_CHEST)
				.define('L', Items.LEATHER)
				.define('O', Items.OBSIDIAN)
				.pattern("PLO")
				.pattern("LEL")
				.pattern("OL ")
				.unlockedBy("has_chest", conditionsFromItem(Items.ENDER_CHEST))
				.unlockedBy("has_eye", conditionsFromItem(Items.ENDER_EYE))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.CRAFTING_PLACEHOLDER, 32)
				.requires(ConventionalItemTags.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
				.requires(BotaniaBlocks.LIVINGROCK)
				.unlockedBy("has_dreamwood", conditionsFromTag(BotaniaTags.Items.DREAMWOOD_LOGS))
				.unlockedBy("has_crafty_crate", conditionsFromItem(BotaniaBlocks.CRAFTY_CRATE))
				.save(recipeOutput);

		for (CraftyCratePattern pattern : CraftyCratePattern.values()) {
			if (pattern == CraftyCratePattern.NONE) {
				continue;
			}
			Item item = getItemOrThrow(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + pattern.getSerializedName().split("_", 2)[1]));
			String s = pattern.openSlots.stream().map(bool -> bool ? "R" : "P").collect(Collectors.joining());
			ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item)
					.define('P', BotaniaItems.CRAFTING_PLACEHOLDER)
					.define('R', ConventionalItemTags.REDSTONE_DUSTS)
					.pattern(s.substring(0, 3))
					.pattern(s.substring(3, 6))
					.pattern(s.substring(6, 9))
					.group("botania:craft_pattern")
					.unlockedBy("has_item", conditionsFromItem(BotaniaItems.CRAFTING_PLACEHOLDER))
					.unlockedBy("has_crafty_crate", conditionsFromItem(BotaniaBlocks.CRAFTY_CRATE))
					.save(recipeOutput);
		}

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.MANA_BLASTER)
				.define('S', BotaniaBlocks.PULSE_MANA_SPREADER)
				.define('D', ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				.define('T', Items.TNT)
				.define('W', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.define('M', BotaniaItems.RUNE_OF_MANA)
				.pattern("SMD")
				.pattern(" WT")
				.pattern("  W")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.ROD_OF_THE_LANDS)
				.define('D', Items.DIRT)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.define('E', BotaniaItems.RUNE_OF_EARTH)
				.pattern("  D")
				.pattern(" T ")
				.pattern("E  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.RUNE_OF_EARTH))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.ROD_OF_THE_TERRA_FIRMA)
				.define('A', BotaniaItems.RUNE_OF_AUTUMN)
				.define('R', BotaniaItems.ROD_OF_THE_LANDS)
				.define('S', BotaniaItems.RUNE_OF_SPRING)
				.define('T', ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS)
				.define('G', BotaniaItems.PASTURE_SEEDS)
				.define('W', BotaniaItems.RUNE_OF_WINTER)
				.define('M', BotaniaItems.RUNE_OF_SUMMER)
				.pattern(" WT")
				.pattern("ARS")
				.pattern("GM ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS))
				.save(recipeOutput);

		WrapperRecipeBuilder.wrap(WaterBottleMatchingRecipe.SERIALIZER,
				ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.ROD_OF_THE_SEAS)
						.define('B', Ingredient.of(PotionContents.createItemStack(Items.POTION, Potions.WATER)))
						.define('R', BotaniaItems.RUNE_OF_WATER)
						.define('T', BotaniaItems.LIVINGWOOD_TWIG)
						.pattern("  B")
						.pattern(" T ")
						.pattern("R  ")
						.unlockedBy("has_item", conditionsFromItem(BotaniaItems.RUNE_OF_WATER))
		).save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.ROD_OF_THE_BIFROST)
				.define('P', ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.define('D', ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.pattern(" PD")
				.pattern(" EP")
				.pattern("E  ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.ROD_OF_THE_SKIES)
				.define('R', BotaniaItems.RUNE_OF_AIR)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.define('F', Items.FEATHER)
				.pattern("  F")
				.pattern(" T ")
				.pattern("R  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.RUNE_OF_AIR))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BotaniaItems.ROD_OF_THE_HELLS)
				.define('R', BotaniaItems.RUNE_OF_FIRE)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.define('F', Items.BLAZE_POWDER)
				.pattern("  F")
				.pattern(" T ")
				.pattern("R  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.RUNE_OF_FIRE))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, BotaniaItems.ROD_OF_THE_HIGHLANDS)
				.requires(BotaniaItems.ROD_OF_THE_LANDS)
				.requires(ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.requires(BotaniaItems.RUNE_OF_AIR)
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.ROD_OF_THE_PLENTIFUL_MANTLE)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.define('D', ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				.pattern(" TD")
				.pattern(" TT")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.ROD_OF_THE_SHADED_MESA)
				.define('T', BotaniaItems.DREAMWOOD_TWIG)
				.define('D', ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS)
				.define('W', Items.WHEAT)
				.pattern(" TD")
				.pattern(" WT")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BotaniaItems.ROD_OF_THE_UNSTABLE_RESERVOIR)
				.define('D', ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS)
				.define('T', BotaniaItems.DREAMWOOD_TWIG)
				.define('G', BotaniaItems.GAIA_SPIRIT)
				.pattern("GDD")
				.pattern(" TD")
				.pattern("T G")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.ROD_OF_THE_DEPTHS)
				.define('C', Items.COBBLESTONE)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.define('F', BotaniaItems.RUNE_OF_FIRE)
				.define('W', BotaniaItems.RUNE_OF_WATER)
				.pattern(" FC")
				.pattern(" TW")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.RUNE_OF_FIRE))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.RUNE_OF_WATER))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.ROD_OF_THE_MOLTEN_CORE)
				.define('B', Items.BLAZE_ROD)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.define('F', BotaniaItems.RUNE_OF_FIRE)
				.pattern(" BF")
				.pattern(" TB")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.RUNE_OF_FIRE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.ROD_OF_THE_SHIFTING_CRUST)
				.define('R', BotaniaItems.RUNE_OF_SLOTH)
				.define('S', Items.STONE)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.pattern(" SR")
				.pattern(" TS")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.RUNE_OF_SLOTH))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.SHARD_OF_LAPUTA)
				.define('P', ConventionalItemTags.PRISMARINE_GEMS)
				.define('A', BotaniaItems.RUNE_OF_AIR)
				.define('S', BotaniaItems.GAIA_SPIRIT)
				.define('D', ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS)
				.define('E', BotaniaItems.RUNE_OF_EARTH)
				.define('F', BotaniaTags.Items.MUNDANE_FLOATING_FLOWERS)
				.pattern("SFS")
				.pattern("PDP")
				.pattern("ASE")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.ASSEMBLY_HALO)
				.define('P', ConventionalBotaniaTags.Items.MANA_PEARL_GEMS)
				.define('C', ConventionalItemTags.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
				.define('I', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern(" P ")
				.pattern("ICI")
				.pattern(" I ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.LENS_CLIP)
				.define('D', BotaniaTags.Items.DREAMWOOD_LOGS)
				.pattern(" D ")
				.pattern("D D")
				.pattern("DD ")
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.DREAMWOOD_LOGS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.SPELLBINDING_CLOTH)
				.define('P', ConventionalBotaniaTags.Items.MANA_PEARL_GEMS)
				.define('C', BotaniaItems.MANAWEAVE_CLOTH)
				.pattern(" C ")
				.pattern("CPC")
				.pattern(" C ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANAWEAVE_CLOTH))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.FLOWER_POUCH)
				.define('P', BotaniaTags.Items.PETALS)
				.define('W', ItemTags.WOOL)
				.pattern("WPW")
				.pattern("W W")
				.pattern(" W ")
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.SMALL_MYSTICAL_FLOWERS))
				.save(recipeOutput);
		// TODO: temporary Petal Pouch recipe
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.PETAL_POUCH)
				.define('P', BotaniaItems.FLOWER_POUCH)
				.define('D', ConventionalBotaniaTags.Items.MANA_DUSTS)
				.pattern(" D ")
				.pattern("DPD")
				.pattern(" D ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.FLOWER_POUCH))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, BotaniaItems.POOL_MINECART)
				.requires(Items.MINECART)
				.requires(BotaniaBlocks.MANA_POOL)
				.unlockedBy("has_item", conditionsFromItem(Items.MINECART))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.BLACK_HOLE_TALISMAN)
				.define('A', BotaniaItems.ENDER_AIR_BOTTLE)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.define('G', BotaniaItems.GAIA_SPIRIT)
				.pattern(" G ")
				.pattern("EAE")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.STONE_OF_TEMPERANCE)
				.define('R', BotaniaItems.RUNE_OF_EARTH)
				.define('S', Items.STONE)
				.pattern(" S ")
				.pattern("SRS")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.RUNE_OF_EARTH))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.INCENSE_STICK)
				.define('B', Items.BLAZE_POWDER)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.define('G', Items.GHAST_TEAR)
				.pattern("  G")
				.pattern(" B ")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(Items.GHAST_TEAR))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.FLORAL_OBEDIENCE_STICK)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.define('M', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("  M")
				.pattern(" T ")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.SLIME_IN_A_BOTTLE)
				.define('S', Items.SLIME_BALL)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.define('G', BotaniaBlocks.ALFGLASS)
				.pattern("EGE")
				.pattern("ESE")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, BotaniaItems.MANUFACTORY_HALO)
				.requires(BotaniaItems.ASSEMBLY_HALO)
				.requires(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.WORLDSHAPERS_SEXTANT)
				.define('T', BotaniaItems.LIVINGWOOD_TWIG)
				.define('I', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern(" TI")
				.pattern(" TT")
				.pattern("III")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.WORLDSHAPERS_ASTROLABE)
				.define('D', BotaniaTags.Items.DREAMWOOD_LOGS)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.define('G', BotaniaItems.GAIA_SPIRIT)
				.pattern(" EG")
				.pattern("EEE")
				.pattern("GED")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);

	}

	private void registerTrinkets(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.TINY_PLANET)
				.define('P', ConventionalBotaniaTags.Items.MANA_PEARL_GEMS)
				.define('S', Items.STONE)
				.define('L', BotaniaBlocks.LIVINGROCK)
				.pattern("LSL")
				.pattern("SPS")
				.pattern("LSL")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS))
				.save(recipeOutput);
		WrapperRecipeBuilder.wrap(ManaUpgradeRecipe.SERIALIZER,
				ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.BAND_OF_MANA)
						.define('T', BotaniaItems.MANA_TABLET)
						.define('I', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
						.pattern("TI ")
						.pattern("I I")
						.pattern(" I ")
						.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_TABLET))
		).save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.BAND_OF_AURA)
				.define('R', BotaniaItems.RUNE_OF_MANA)
				.define('I', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("RI ")
				.pattern("I I")
				.pattern(" I ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.RUNE_OF_MANA))
				.save(recipeOutput);
		WrapperRecipeBuilder.wrap(ShapelessManaUpgradeRecipe.SERIALIZER,
				ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, BotaniaItems.GREATER_BAND_OF_MANA)
						.requires(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS)
						.requires(BotaniaItems.BAND_OF_MANA)
						.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS))
		).save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, BotaniaItems.GREATER_BAND_OF_AURA)
				.requires(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS)
				.requires(BotaniaItems.BAND_OF_AURA)
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.SOJOURNERS_SASH)
				.define('A', BotaniaItems.RUNE_OF_AIR)
				.define('S', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.define('E', BotaniaItems.RUNE_OF_EARTH)
				.define('L', Items.LEATHER)
				.pattern("EL ")
				.pattern("L L")
				.pattern("SLA")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.TECTONIC_GIRDLE)
				.define('A', BotaniaItems.RUNE_OF_FIRE)
				.define('S', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.define('E', BotaniaItems.RUNE_OF_EARTH)
				.define('L', Items.LEATHER)
				.pattern("AL ")
				.pattern("L L")
				.pattern("SLE")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.SNOWFLAKE_PENDANT)
				.define('R', BotaniaItems.RUNE_OF_WATER)
				.define('S', BotaniaItems.MANA_INFUSED_STRING)
				.define('W', BotaniaItems.RUNE_OF_WINTER)
				.define('M', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("WS ")
				.pattern("S S")
				.pattern("MSR")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.PYROCLAST_PENDANT)
				.define('S', BotaniaItems.MANA_INFUSED_STRING)
				.define('D', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.define('F', BotaniaItems.RUNE_OF_FIRE)
				.define('M', BotaniaItems.RUNE_OF_SUMMER)
				.pattern("MS ")
				.pattern("S S")
				.pattern("DSF")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.RING_OF_MAGNETIZATION)
				.define('L', BotaniaItems.MAGNETIZING_LENS)
				.define('M', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("LM ")
				.pattern("M M")
				.pattern(" M ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.RING_OF_CHORDATA)
				.define('P', Items.PUFFERFISH)
				.define('C', Items.COD)
				.define('H', Items.HEART_OF_THE_SEA)
				.define('W', BotaniaItems.RUNE_OF_WATER)
				.define('M', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("WMP")
				.pattern("MHM")
				.pattern("CM ")
				.unlockedBy("has_item", conditionsFromItem(Items.HEART_OF_THE_SEA))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.RING_OF_THE_MANTLE)
				.define('P', Items.GOLDEN_PICKAXE)
				.define('E', BotaniaItems.RUNE_OF_EARTH)
				.define('M', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("EMP")
				.pattern("M M")
				.pattern(" M ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.CHARM_OF_THE_DIVA)
				.define('P', BotaniaItems.TINY_PLANET)
				.define('G', ConventionalItemTags.GOLD_INGOTS)
				.define('H', BotaniaItems.RUNE_OF_PRIDE)
				.define('L', BotaniaItems.GAIA_SPIRIT)
				.pattern("LGP")
				.pattern(" HG")
				.pattern(" GL")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.FLUEGEL_TIARA)
				.define('E', BotaniaItems.ENDER_AIR_BOTTLE)
				.define('F', Items.FEATHER)
				.define('I', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.define('L', BotaniaItems.GAIA_SPIRIT)
				.pattern("LLL")
				.pattern("ILI")
				.pattern("FEF")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);

		// Normal quartz and not Tags.Items.QUARTZ because the recipes conflict.
		Item[] items = {
				null, Items.QUARTZ, BotaniaItems.DARK_QUARTZ,
				BotaniaItems.MANA_QUARTZ, BotaniaItems.BLAZE_QUARTZ, BotaniaItems.LAVENDER_QUARTZ,
				BotaniaItems.RED_QUARTZ, BotaniaItems.ELVEN_QUARTZ, BotaniaItems.SUNNY_QUARTZ
		};
		for (int variant = 0; variant < items.length; variant++) {
			Ingredient material = items[variant] == null ? Ingredient.EMPTY : Ingredient.of(items[variant]);
			TiaraWingsRecipeBuilder.with(material, variant)
					.unlockedBy("has_item", conditionsFromItem(BotaniaItems.FLUEGEL_TIARA))
					.save(recipeOutput);
		}
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BotaniaItems.GREAT_FAIRY_RING)
				.define('D', ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.pattern("DE ")
				.pattern("E E")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.GLOBETROTTERS_SASH)
				.define('S', BotaniaItems.SOJOURNERS_SASH)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.define('L', BotaniaItems.GAIA_SPIRIT)
				.pattern("E  ")
				.pattern(" S ")
				.pattern("L E")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.RING_OF_FAR_REACH)
				.define('R', BotaniaItems.RUNE_OF_PRIDE)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.pattern("RE ")
				.pattern("E E")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.THE_SPECTATOR)
				.define('E', ConventionalItemTags.EMERALD_GEMS)
				.define('I', ConventionalItemTags.IRON_INGOTS)
				.define('Y', Items.ENDER_EYE)
				.pattern(" I ")
				.pattern("IYI")
				.pattern("IEI")
				.unlockedBy("has_item", conditionsFromItem(Items.ENDER_EYE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.CRIMSON_PENDANT)
				.define('P', BotaniaItems.PYROCLAST_PENDANT)
				.define('B', Items.BLAZE_ROD)
				.define('G', BotaniaItems.GAIA_SPIRIT)
				.define('N', ConventionalItemTags.NETHER_BRICKS)
				.pattern("BBB")
				.pattern("BPB")
				.pattern("NGN")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.TAINTED_BLOOD_PENDANT)
				.define('P', ConventionalItemTags.PRISMARINE_GEMS)
				.define('D', ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				.define('G', Items.GHAST_TEAR)
				.pattern(" P ")
				.pattern("PGP")
				.pattern("DP ")
				.unlockedBy("has_item", conditionsFromItem(Items.GHAST_TEAR))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BotaniaItems.CLOAK_OF_VIRTUE)
				.define('S', BotaniaItems.GAIA_SPIRIT)
				.define('W', Items.WHITE_WOOL)
				.define('G', ConventionalItemTags.GLOWSTONE_DUSTS)
				.pattern("WWW")
				.pattern("GWG")
				.pattern("GSG")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BotaniaItems.CLOAK_OF_SIN)
				.define('R', ConventionalItemTags.REDSTONE_DUSTS)
				.define('S', BotaniaItems.GAIA_SPIRIT)
				.define('W', Items.BLACK_WOOL)
				.pattern("WWW")
				.pattern("RWR")
				.pattern("RSR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BotaniaItems.CLOAK_OF_BALANCE)
				.define('R', ConventionalItemTags.EMERALD_GEMS)
				.define('S', BotaniaItems.GAIA_SPIRIT)
				.define('W', Items.LIGHT_GRAY_WOOL)
				.pattern("WWW")
				.pattern("RWR")
				.pattern("RSR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.MANASEER_MONOCLE)
				.define('G', BotaniaBlocks.MANAGLASS)
				.define('I', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.define('N', ConventionalItemTags.GOLD_NUGGETS)
				.pattern("GN")
				.pattern("IN")
				.pattern(" N")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.RING_OF_CORRECTION)
				.define('C', Items.CLAY)
				.define('M', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("CM ")
				.pattern("M M")
				.pattern(" M ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, BotaniaItems.GREATER_RING_OF_MAGNETIZATION)
				.requires(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS)
				.requires(BotaniaItems.RING_OF_MAGNETIZATION)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.RING_OF_MAGNETIZATION))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.PLANESTRIDERS_SASH)
				.define('P', BotaniaItems.PASTURE_SEEDS)
				.define('B', BotaniaItems.SOJOURNERS_SASH)
				.define('S', Items.SUGAR)
				.define('M', Items.MAP)
				.pattern(" M ")
				.pattern("PBP")
				.pattern(" S ")
				.unlockedBy("has_map", conditionsFromItem(Items.MAP))
				.unlockedBy("has_belt", conditionsFromItem(BotaniaItems.SOJOURNERS_SASH))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.RING_OF_DEXTEROUS_MOTION)
				.define('R', BotaniaItems.RUNE_OF_AIR)
				.define('E', ConventionalItemTags.EMERALD_GEMS)
				.define('M', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern("EM ")
				.pattern("M M")
				.pattern(" MR")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.INVINSIBILITY_CLOAK)
				.define('P', ConventionalBotaniaTags.Items.MANA_PEARL_GEMS)
				.define('C', ConventionalItemTags.PRISMARINE_GEMS)
				.define('W', Items.WHITE_WOOL)
				.define('G', BotaniaBlocks.MANAGLASS)
				.pattern("CWC")
				.pattern("GWG")
				.pattern("GPG")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.CIRRUS_AMULET)
				.define('S', BotaniaItems.MANA_INFUSED_STRING)
				.define('D', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.define('F', BotaniaItems.RUNE_OF_AIR)
				.define('M', BotaniaItems.RUNE_OF_AUTUMN)
				.pattern("MS ")
				.pattern("S S")
				.pattern("DSF")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_INFUSED_STRING))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.NIMBUS_AMULET)
				.define('P', BotaniaItems.CIRRUS_AMULET)
				.define('B', Items.GHAST_TEAR)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.define('G', BotaniaItems.GAIA_SPIRIT)
				.define('N', Items.WHITE_WOOL)
				.pattern("BEB")
				.pattern("BPB")
				.pattern("NGN")
				.unlockedBy("has_pendant", conditionsFromItem(BotaniaItems.CIRRUS_AMULET))
				.unlockedBy("has_spirit", conditionsFromItem(BotaniaItems.GAIA_SPIRIT))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.THIRD_EYE)
				.define('Q', Items.QUARTZ_BLOCK)
				.define('R', Items.GOLDEN_CARROT)
				.define('S', BotaniaItems.RUNE_OF_EARTH)
				.define('D', ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				.define('E', Items.ENDER_EYE)
				.pattern("RSR")
				.pattern("QEQ")
				.pattern("RDR")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.BENEVOLENT_GODDESS_CHARM)
				.define('P', BotaniaTags.Items.PETALS_PINK)
				.define('A', BotaniaItems.RUNE_OF_WATER)
				.define('S', BotaniaItems.RUNE_OF_SPRING)
				.define('D', ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				.pattern(" P ")
				.pattern(" P ")
				.pattern("ADS")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS))
				.save(recipeOutput);

	}

	private void registerCorporeaAndRedString(RecipeOutput recipeOutput) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.RED_STRING)
				.requires(Items.STRING)
				.requires(ConventionalItemTags.STORAGE_BLOCKS_REDSTONE)
				.requires(ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.requires(BotaniaItems.ENDER_AIR_BOTTLE)
				.group("botania:red_string")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.ENDER_AIR_BOTTLE))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.RED_STRING)
				.requires(Items.STRING)
				.requires(ConventionalItemTags.STORAGE_BLOCKS_REDSTONE)
				.requires(ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.requires(BotaniaItems.ENDER_AIR_BOTTLE)
				.requires(Items.PUMPKIN)
				.group("botania:red_string")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.ENDER_AIR_BOTTLE))
				.save(recipeOutput, "botania:red_string_alt");
		registerRedStringBlock(recipeOutput, BotaniaBlocks.RED_STRINGED_CONTAINER, Ingredient.of(ConventionalItemTags.WOODEN_CHESTS));
		registerRedStringBlock(recipeOutput, BotaniaBlocks.RED_STRINGED_DISPENSER, Ingredient.of(Items.DISPENSER));
		registerRedStringBlock(recipeOutput, BotaniaBlocks.RED_STRINGED_NUTRIFIER, Ingredient.of(BotaniaItems.FLORAL_FERTILIZER));
		registerRedStringBlock(recipeOutput, BotaniaBlocks.RED_STRINGED_COMPARATOR, Ingredient.of(Items.COMPARATOR));
		registerRedStringBlock(recipeOutput, BotaniaBlocks.RED_STRINGED_SPOOFER, Ingredient.of(BotaniaBlocks.MANA_SPREADER));
		registerRedStringBlock(recipeOutput, BotaniaBlocks.RED_STRINGED_INTERCEPTOR, Ingredient.of(ItemTags.STONE_BUTTONS));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BotaniaItems.CORPOREA_SPARK, 4)
				.requires(BotaniaItems.SPARK)
				.requires(ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.requires(BotaniaItems.ENDER_AIR_BOTTLE)
				.unlockedBy("has_bottle", conditionsFromItem(BotaniaItems.ENDER_AIR_BOTTLE))
				.unlockedBy("has_pixie_dust", conditionsFromTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BotaniaItems.MASTER_CORPOREA_SPARK)
				.requires(BotaniaItems.CORPOREA_SPARK)
				.requires(ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.CORPOREA_SPARK))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.CORPOREA_INDEX)
				.define('A', BotaniaItems.ENDER_AIR_BOTTLE)
				.define('S', BotaniaItems.CORPOREA_SPARK)
				.define('D', ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS)
				.define('O', Items.OBSIDIAN)
				.pattern("AOA")
				.pattern("OSO")
				.pattern("DOD")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.CORPOREA_SPARK))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BotaniaBlocks.CORPOREA_FUNNEL)
				.requires(Items.DROPPER)
				.requires(BotaniaItems.CORPOREA_SPARK)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.CORPOREA_SPARK))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BotaniaBlocks.CORPOREA_INTERCEPTOR)
				.requires(ConventionalItemTags.STORAGE_BLOCKS_REDSTONE)
				.requires(BotaniaItems.CORPOREA_SPARK)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.CORPOREA_SPARK))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BotaniaBlocks.CORPOREA_RETAINER)
				.requires(ConventionalItemTags.WOODEN_CHESTS)
				.requires(BotaniaItems.CORPOREA_SPARK)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.CORPOREA_SPARK))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaBlocks.CORPOREA_CRYSTAL_CUBE)
				.define('C', BotaniaItems.CORPOREA_SPARK)
				.define('G', BotaniaBlocks.ALFGLASS)
				.define('W', BotaniaTags.Items.DREAMWOOD_LOGS)
				.pattern("C")
				.pattern("G")
				.pattern("W")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.CORPOREA_SPARK))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.CORPOREA_BLOCK, 8)
				.requires(BotaniaBlocks.POLISHED_LIVINGROCK)
				.requires(BotaniaItems.CORPOREA_SPARK)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.CORPOREA_SPARK))
				.save(recipeOutput);
		slabShape(BotaniaBlocks.CORPOREA_SLAB, BotaniaBlocks.CORPOREA_BLOCK).save(recipeOutput);
		stairs(BotaniaBlocks.CORPOREA_STAIRS, BotaniaBlocks.CORPOREA_BLOCK).save(recipeOutput);
		wallShape(BotaniaBlocks.CORPOREA_WALL, BotaniaBlocks.CORPOREA_BLOCK, 6).save(recipeOutput);
		button(BotaniaBlocks.CORPOREA_BUTTON, BotaniaBlocks.CORPOREA_BLOCK).save(recipeOutput);
		pressurePlate(BotaniaBlocks.CORPOREA_PRESSURE_PLATE, BotaniaBlocks.CORPOREA_BLOCK).save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.CORPOREA_BRICKS, 4)
				.define('R', BotaniaBlocks.CORPOREA_BLOCK)
				.pattern("RR")
				.pattern("RR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.CORPOREA_BLOCK))
				.save(recipeOutput);
		slabShape(BotaniaBlocks.CORPOREA_BRICK_SLAB, BotaniaBlocks.CORPOREA_BRICKS).save(recipeOutput);
		stairs(BotaniaBlocks.CORPOREA_BRICK_STAIRS, BotaniaBlocks.CORPOREA_BRICKS).save(recipeOutput);
		wallShape(BotaniaBlocks.CORPOREA_BRICK_WALL, BotaniaBlocks.CORPOREA_BRICKS, 6).save(recipeOutput);
	}

	private void registerLenses(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.MANA_LENS)
				.define('S', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.define('G', Ingredient.of(ConventionalItemTags.GLASS_BLOCKS_COLORLESS))
				.group("botania:mana_lens")
				.pattern(" S ")
				.pattern("SGS")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.MANA_LENS)
				.define('S', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.define('G', Ingredient.of(ConventionalItemTags.GLASS_PANES_COLORLESS))
				.group("botania:mana_lens")
				.pattern(" S ")
				.pattern("SGS")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput, "botania:mana_lens_pane");
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.VELOCITY_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(BotaniaItems.RUNE_OF_AIR)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.POTENCY_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(BotaniaItems.RUNE_OF_FIRE)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.RESISTANCE_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(BotaniaItems.RUNE_OF_EARTH)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.EFFICIENCY_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(BotaniaItems.RUNE_OF_WATER)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.BOUNCE_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(BotaniaItems.RUNE_OF_SUMMER)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.GRAVITY_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(BotaniaItems.RUNE_OF_WINTER)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.BORE_LENS)
				.define('P', Items.PISTON)
				.define('A', ConventionalItemTags.STORAGE_BLOCKS_LAPIS)
				.define('R', ConventionalItemTags.REDSTONE_DUSTS)
				.define('L', BotaniaItems.MANA_LENS)
				.pattern(" P ")
				.pattern("ALA")
				.pattern(" R ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.DAMAGING_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(BotaniaItems.RUNE_OF_WRATH)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.PHANTOM_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(BotaniaBlocks.ABSTRUSE_PLATFORM)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.MAGNETIZING_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(ConventionalItemTags.IRON_INGOTS)
				.requires(ConventionalItemTags.GOLD_INGOTS)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.ENTROPIC_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(BotaniaItems.RUNE_OF_ENVY)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.INFLUENCE_LENS)
				.define('P', ConventionalItemTags.PRISMARINE_GEMS)
				.define('R', BotaniaItems.RUNE_OF_AIR)
				.define('L', BotaniaItems.MANA_LENS)
				.pattern("PRP")
				.pattern("PLP")
				.pattern("PPP")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.WEIGHT_LENS)
				.define('P', ConventionalItemTags.PRISMARINE_GEMS)
				.define('R', BotaniaItems.RUNE_OF_WATER)
				.define('L', BotaniaItems.MANA_LENS)
				.pattern("PPP")
				.pattern("PLP")
				.pattern("PRP")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.PAINTSLINGER_LENS)
				.define('E', ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.define('W', ItemTags.WOOL)
				.define('L', BotaniaItems.MANA_LENS)
				.pattern(" E ")
				.pattern("WLW")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.KINDLE_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(Items.FIRE_CHARGE)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.FORCE_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(BotaniaBlocks.FORCE_RELAY)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.FLASH_LENS)
				.define('F', Items.FIRE_CHARGE)
				.define('G', Items.GLOWSTONE)
				.define('L', BotaniaItems.MANA_LENS)
				.pattern("GFG")
				.pattern("FLF")
				.pattern("GFG")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.FLASH_LENS)
				.define('F', Items.FIRE_CHARGE)
				.define('G', Items.GLOWSTONE)
				.define('L', BotaniaItems.MANA_LENS)
				.pattern("FGF")
				.pattern("GLG")
				.pattern("FGF")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput, "botania:flash_lens_alt");
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaItems.MESSENGER_LENS)
				.define('P', Items.PAPER)
				.define('L', BotaniaItems.MANA_LENS)
				.pattern(" P ")
				.pattern("PLP")
				.pattern(" P ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_LENS))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.WARP_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(ConventionalBotaniaTags.Items.PIXIE_DUSTS)
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.PIXIE_DUSTS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.REDIRECTIVE_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(BotaniaTags.Items.LIVINGWOOD_LOGS)
				.requires(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.CELEBRATORY_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(Items.FIREWORK_ROCKET)
				.requires(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.FLARE_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(BotaniaBlocks.ALFGLASS)
				.requires(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.TRIPWIRE_LENS)
				.requires(BotaniaItems.MANA_LENS)
				.requires(Items.TRIPWIRE_HOOK)
				.requires(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS)
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS))
				.save(recipeOutput);
	}

	private void registerFloatingFlowers(RecipeOutput recipeOutput) {
		for (Block block : new Block[] {
				BotaniaBlocks.PURE_DAISY, BotaniaBlocks.MANASTAR, BotaniaBlocks.HYDROANGEAS, BotaniaBlocks.ENDOFLAME,
				BotaniaBlocks.THERMALILY, BotaniaBlocks.ROSA_ARCANA, BotaniaBlocks.MUNCHDEW, BotaniaBlocks.ENTROPINNYUM,
				BotaniaBlocks.KEKIMURUS, BotaniaBlocks.GOURMARYLLIS, BotaniaBlocks.NARSLIMMUS, BotaniaBlocks.SPECTROLUS,
				BotaniaBlocks.DANDELIFEON, BotaniaBlocks.RAFFLOWSIA, BotaniaBlocks.SHULK_ME_NOT,
				BotaniaBlocks.BELLETHORNE, BotaniaBlocks.BELLETHORNE_PETITE, BotaniaBlocks.BERGAMUTE,
				BotaniaBlocks.DREADTHORNE, BotaniaBlocks.HEISEI_DREAM, BotaniaBlocks.TIGERSEYE,
				BotaniaBlocks.JADED_AMARANTHUS, BotaniaBlocks.ORECHID, BotaniaBlocks.FALLEN_KANADE,
				BotaniaBlocks.EXOFLAME, BotaniaBlocks.AGRICARNATION, BotaniaBlocks.AGRICARNATION_PETITE,
				BotaniaBlocks.HOPPERHOCK, BotaniaBlocks.HOPPERHOCK_PETITE, BotaniaBlocks.TANGLEBERRIE,
				BotaniaBlocks.TANGLEBERRIE_PETITE, BotaniaBlocks.JIYUULIA, BotaniaBlocks.JIYUULIA_PETITE,
				BotaniaBlocks.RANNUNCARPUS, BotaniaBlocks.RANNUNCARPUS_PETITE, BotaniaBlocks.HYACIDUS,
				BotaniaBlocks.POLLIDISIAC, BotaniaBlocks.CLAYCONIA, BotaniaBlocks.CLAYCONIA_PETITE,
				BotaniaBlocks.LOONIUM, BotaniaBlocks.DAFFOMILL, BotaniaBlocks.VINCULOTUS, BotaniaBlocks.SPECTRANTHEMUM,
				BotaniaBlocks.MEDUMONE, BotaniaBlocks.MARIMORPHOSIS, BotaniaBlocks.MARIMORPHOSIS_PETITE,
				BotaniaBlocks.BUBBELL, BotaniaBlocks.BUBBELL_PETITE, BotaniaBlocks.SOLEGNOLIA,
				BotaniaBlocks.SOLEGNOLIA_PETITE, BotaniaBlocks.ORECHID_IGNEM, BotaniaBlocks.LABELLIA
		}) {
			createFloatingFlowerRecipe(recipeOutput, block);
		}
	}

	private void registerConversions(RecipeOutput recipeOutput) {
		compression(BotaniaItems.MANASTEEL_INGOT, ConventionalBotaniaTags.Items.MANASTEEL_NUGGETS)
				.save(recipeOutput, prefix("conversions/manasteel_from_nuggets"));
		compression(BotaniaItems.ELEMENTIUM_INGOT, ConventionalBotaniaTags.Items.ELEMENTIUM_NUGGETS)
				.save(recipeOutput, prefix("conversions/elementium_from_nuggets"));
		compression(BotaniaItems.TERRASTEEL_INGOT, ConventionalBotaniaTags.Items.TERRASTEEL_NUGGETS)
				.save(recipeOutput, prefix("conversions/terrasteel_from_nugget"));
		compression(BotaniaBlocks.MANASTEEL_BLOCK, ConventionalBotaniaTags.Items.MANASTEEL_INGOTS).save(recipeOutput);
		compression(BotaniaBlocks.TERRASTEEL_BLOCK, ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS).save(recipeOutput);
		compression(BotaniaBlocks.ELEMENTIUM_BLOCK, ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS).save(recipeOutput);
		compression(BotaniaBlocks.MANA_DIAMOND_BLOCK, ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS).save(recipeOutput);
		compression(BotaniaBlocks.DRAGONSTONE_BLOCK, ConventionalBotaniaTags.Items.DRAGONSTONE_GEMS).save(recipeOutput);

		nineBlockStorageRecipesRecipesWithCustomUnpacking(
				withConditions(recipeOutput, FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION),
				RecipeCategory.MISC, Items.BLAZE_ROD,
				RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.BLAZE_MESH,
				"botania:conversions/blazeblock_deconstruct", null);

		deconstructPetalBlock(recipeOutput, BotaniaItems.WHITE_MYSTICAL_PETAL, BotaniaBlocks.WHITE_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.ORANGE_MYSTICAL_PETAL, BotaniaBlocks.ORANGE_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.MAGENTA_MYSTICAL_PETAL, BotaniaBlocks.MAGENTA_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.LIGHT_BLUE_MYSTICAL_PETAL, BotaniaBlocks.LIGHT_BLUE_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.YELLOW_MYSTICAL_PETAL, BotaniaBlocks.YELLOW_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.LIME_MYSTICAL_PETAL, BotaniaBlocks.LIME_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.PINK_MYSTICAL_PETAL, BotaniaBlocks.PINK_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.GRAY_MYSTICAL_PETAL, BotaniaBlocks.GRAY_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.LIGHT_GRAY_MYSTICAL_PETAL, BotaniaBlocks.LIGHT_GRAY_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.CYAN_MYSTICAL_PETAL, BotaniaBlocks.CYAN_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.PURPLE_MYSTICAL_PETAL, BotaniaBlocks.PURPLE_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.BLUE_MYSTICAL_PETAL, BotaniaBlocks.BLUE_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.BROWN_MYSTICAL_PETAL, BotaniaBlocks.BROWN_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.GREEN_MYSTICAL_PETAL, BotaniaBlocks.GREEN_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.RED_MYSTICAL_PETAL, BotaniaBlocks.RED_PETAL_BLOCK);
		deconstructPetalBlock(recipeOutput, BotaniaItems.BLACK_MYSTICAL_PETAL, BotaniaBlocks.BLACK_PETAL_BLOCK);

		deconstruct(recipeOutput, BotaniaItems.MANASTEEL_INGOT, ConventionalBotaniaTags.Items.MANASTEEL_STORAGE_BLOCKS, "manasteel_block_deconstruct");
		deconstruct(recipeOutput, BotaniaItems.MANA_DIAMOND, ConventionalBotaniaTags.Items.MANA_DIAMOND_STORAGE_BLOCKS, "manadiamond_block_deconstruct");
		deconstruct(recipeOutput, BotaniaItems.TERRASTEEL_INGOT, ConventionalBotaniaTags.Items.TERRASTEEL_STORAGE_BLOCKS, "terrasteel_block_deconstruct");
		deconstruct(recipeOutput, BotaniaItems.ELEMENTIUM_INGOT, ConventionalBotaniaTags.Items.ELEMENTIUM_STORAGE_BLOCKS, "elementium_block_deconstruct");
		deconstruct(recipeOutput, BotaniaItems.DRAGONSTONE, ConventionalBotaniaTags.Items.DRAGONSTONE_STORAGE_BLOCKS, "dragonstone_block_deconstruct");
		deconstruct(recipeOutput, BotaniaItems.MANASTEEL_NUGGET, ConventionalBotaniaTags.Items.MANASTEEL_INGOTS, "manasteel_to_nuggets");
		deconstruct(recipeOutput, BotaniaItems.TERRASTEEL_NUGGET, ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS, "terrasteel_to_nugget");
		deconstruct(recipeOutput, BotaniaItems.ELEMENTIUM_NUGGET, ConventionalBotaniaTags.Items.ELEMENTIUM_INGOTS, "elementium_to_nuggets");

		recombineSlab(recipeOutput, BotaniaBlocks.LIVINGROCK, BotaniaBlocks.LIVINGROCK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.POLISHED_LIVINGROCK, BotaniaBlocks.POLISHED_LIVINGROCK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.LIVINGROCK_BRICKS, BotaniaBlocks.LIVINGROCK_BRICK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.LIVINGWOOD, BotaniaBlocks.LIVINGWOOD_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.LIVINGWOOD_PLANKS, BotaniaBlocks.LIVINGWOOD_PLANK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.DREAMWOOD, BotaniaBlocks.DREAMWOOD_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.DREAMWOOD_PLANKS, BotaniaBlocks.DREAMWOOD_PLANK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.SHIMMERROCK, BotaniaBlocks.SHIMMERROCK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.SHIMMERWOOD_PLANKS, BotaniaBlocks.SHIMMERWOOD_PLANK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.FUCHSITE, BotaniaBlocks.FUCHSITE_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.FUCHSITE_BRICKS, BotaniaBlocks.FUCHSITE_BRICK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.COBBLED_FUCHSITE, BotaniaBlocks.COBBLED_FUCHSITE_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.TALC, BotaniaBlocks.TALC_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.TALC_BRICKS, BotaniaBlocks.TALC_BRICK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.COBBLED_TALC, BotaniaBlocks.COBBLED_TALC_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.GNEISS, BotaniaBlocks.GNEISS_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.GNEISS_BRICKS, BotaniaBlocks.GNEISS_BRICK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.COBBLED_GNEISS, BotaniaBlocks.COBBLED_GNEISS_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.MYCELITE, BotaniaBlocks.MYCELITE_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.MYCELITE_BRICKS, BotaniaBlocks.MYCELITE_BRICK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.COBBLED_MYCELITE, BotaniaBlocks.COBBLED_MYCELITE_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.CATACLASITE, BotaniaBlocks.CATACLASITE_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.CATACLASITE_BRICKS, BotaniaBlocks.CATACLASITE_BRICK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.COBBLED_CATACLASITE, BotaniaBlocks.COBBLED_CATACLASITE_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.SOLITE, BotaniaBlocks.SOLITE_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.SOLITE_BRICKS, BotaniaBlocks.SOLITE_BRICK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.COBBLED_SOLITE, BotaniaBlocks.COBBLED_SOLITE_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.LUNITE, BotaniaBlocks.LUNITE_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.LUNITE_BRICKS, BotaniaBlocks.LUNITE_BRICK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.COBBLED_LUNITE, BotaniaBlocks.COBBLED_LUNITE_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.ROSY_TALC, BotaniaBlocks.ROSY_TALC_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.ROSY_TALC_BRICKS, BotaniaBlocks.ROSY_TALC_BRICK_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.COBBLED_ROSY_TALC, BotaniaBlocks.COBBLED_ROSY_TALC_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.WHITE_PORTUGUESE_PAVEMENT, BotaniaBlocks.WHITE_PORTUGUESE_PAVEMENT_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.BLACK_PORTUGUESE_PAVEMENT, BotaniaBlocks.BLACK_PORTUGUESE_PAVEMENT_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.BLUE_PORTUGUESE_PAVEMENT, BotaniaBlocks.BLUE_PORTUGUESE_PAVEMENT_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.YELLOW_PORTUGUESE_PAVEMENT, BotaniaBlocks.YELLOW_PORTUGUESE_PAVEMENT_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.RED_PORTUGUESE_PAVEMENT, BotaniaBlocks.RED_PORTUGUESE_PAVEMENT_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.GREEN_PORTUGUESE_PAVEMENT, BotaniaBlocks.GREEN_PORTUGUESE_PAVEMENT_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.SMOKEY_QUARTZ_BLOCK, BotaniaBlocks.SMOKEY_QUARTZ_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.MANA_QUARTZ_BLOCK, BotaniaBlocks.MANA_QUARTZ_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.BLAZE_QUARTZ_BLOCK, BotaniaBlocks.BLAZE_QUARTZ_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.LAVENDER_QUARTZ_BLOCK, BotaniaBlocks.LAVENDER_QUARTZ_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.RED_QUARTZ_BLOCK, BotaniaBlocks.RED_QUARTZ_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.ELVEN_QUARTZ_BLOCK, BotaniaBlocks.ELVEN_QUARTZ_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.SUNNY_QUART_BLOCK, BotaniaBlocks.SUNNY_QUARTZ_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.CORPOREA_BLOCK, BotaniaBlocks.CORPOREA_SLAB);
		recombineSlab(recipeOutput, BotaniaBlocks.CORPOREA_BRICKS, BotaniaBlocks.CORPOREA_BRICK_SLAB);
	}

	private void registerDecor(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.POLISHED_LIVINGROCK, 4)
				.define('R', BotaniaBlocks.LIVINGROCK)
				.pattern("RR")
				.pattern("RR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LIVINGROCK))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.LIVINGROCK_SLATE)
				.define('R', BotaniaBlocks.LIVINGROCK_SLAB)
				.pattern("R")
				.pattern("R")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LIVINGROCK))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.LIVINGROCK_BRICKS, 4)
				.define('R', BotaniaBlocks.POLISHED_LIVINGROCK)
				.pattern("RR")
				.pattern("RR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.POLISHED_LIVINGROCK))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.CHISELED_LIVINGROCK_BRICKS)
				.define('R', BotaniaBlocks.LIVINGROCK_BRICK_SLAB)
				.pattern("R")
				.pattern("R")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LIVINGROCK_BRICKS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.MOSSY_LIVINGROCK_BRICKS)
				.requires(BotaniaBlocks.LIVINGROCK_BRICKS)
				.requires(Items.MOSS_BLOCK)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LIVINGROCK_BRICKS))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.MOSSY_LIVINGROCK_BRICKS)
				.requires(BotaniaBlocks.LIVINGROCK_BRICKS)
				.requires(Items.VINE)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LIVINGROCK_BRICKS))
				.save(recipeOutput, "botania:mossy_livingrock_bricks_vine");
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.SHIMMERROCK)
				.requires(BotaniaBlocks.LIVINGROCK)
				.requires(BotaniaBlocks.BIFROST)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.BIFROST))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.ROD_OF_THE_BIFROST))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.SHIMMERWOOD_PLANKS)
				.requires(BotaniaBlocks.DREAMWOOD_PLANKS)
				.requires(BotaniaBlocks.BIFROST)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.BIFROST))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.ROD_OF_THE_BIFROST))
				.save(recipeOutput);

		registerForQuartz(recipeOutput, LibBlockNames.SMOKEY_QUARTZ, BotaniaItems.DARK_QUARTZ);
		registerForQuartz(recipeOutput, LibBlockNames.MANA_QUARTZ, BotaniaItems.MANA_QUARTZ);
		registerForQuartz(recipeOutput, LibBlockNames.BLAZE_QUARTZ, BotaniaItems.BLAZE_QUARTZ);
		registerForQuartz(recipeOutput, LibBlockNames.LAVENDER_QUARTZ, BotaniaItems.LAVENDER_QUARTZ);
		registerForQuartz(recipeOutput, LibBlockNames.RED_QUARTZ, BotaniaItems.RED_QUARTZ);
		registerForQuartz(recipeOutput, LibBlockNames.ELVEN_QUARTZ, BotaniaItems.ELVEN_QUARTZ);
		registerForQuartz(recipeOutput, LibBlockNames.SUNNY_QUARTZ, BotaniaItems.SUNNY_QUARTZ);

		registerForWood(recipeOutput, LibBlockNames.LIVINGWOOD);
		registerForWood(recipeOutput, LibBlockNames.DREAMWOOD);

		stairs(BotaniaBlocks.SHIMMERWOOD_PLANK_STAIRS, BotaniaBlocks.SHIMMERWOOD_PLANKS).save(recipeOutput);
		slabShape(BotaniaBlocks.SHIMMERWOOD_PLANK_SLAB, BotaniaBlocks.SHIMMERWOOD_PLANKS).save(recipeOutput);
		fence(BotaniaBlocks.SHIMMERWOOD_FENCE, BotaniaBlocks.SHIMMERWOOD_PLANKS).save(recipeOutput);
		fenceGate(BotaniaBlocks.SHIMMERWOOD_FENCE_GATE, BotaniaBlocks.SHIMMERWOOD_PLANKS).save(recipeOutput);
		button(BotaniaBlocks.SHIMMERWOOD_BUTTON, BotaniaBlocks.SHIMMERWOOD_PLANKS).save(recipeOutput);
		pressurePlate(BotaniaBlocks.SHIMMERWOOD_PRESSURE_PLATE, BotaniaBlocks.SHIMMERWOOD_PLANKS).save(recipeOutput);

		stairs(BotaniaBlocks.LIVINGROCK_STAIRS, BotaniaBlocks.LIVINGROCK).save(recipeOutput);
		slabShape(BotaniaBlocks.LIVINGROCK_SLAB, BotaniaBlocks.LIVINGROCK).save(recipeOutput);
		wallShape(BotaniaBlocks.LIVINGROCK_WALL, BotaniaBlocks.LIVINGROCK, 6).save(recipeOutput);
		button(BotaniaBlocks.LIVINGROCK_BUTTON, BotaniaBlocks.LIVINGROCK).save(recipeOutput);
		pressurePlate(BotaniaBlocks.LIVINGROCK_PRESSURE_PLATE, BotaniaBlocks.LIVINGROCK).save(recipeOutput);

		stairs(BotaniaBlocks.POLISHED_LIVINGROCK_STAIRS, BotaniaBlocks.POLISHED_LIVINGROCK).save(recipeOutput);
		slabShape(BotaniaBlocks.POLISHED_LIVINGROCK_SLAB, BotaniaBlocks.POLISHED_LIVINGROCK).save(recipeOutput);
		wallShape(BotaniaBlocks.POLISHED_LIVINGROCK_WALL, BotaniaBlocks.POLISHED_LIVINGROCK, 6).save(recipeOutput);

		stairs(BotaniaBlocks.LIVINGROCK_BRICK_STAIRS, BotaniaBlocks.LIVINGROCK_BRICKS).save(recipeOutput);
		slabShape(BotaniaBlocks.LIVINGROCK_BRICK_SLAB, BotaniaBlocks.LIVINGROCK_BRICKS).save(recipeOutput);
		wallShape(BotaniaBlocks.LIVINGROCK_BRICK_WALL, BotaniaBlocks.LIVINGROCK_BRICKS, 6).save(recipeOutput);

		stairs(BotaniaBlocks.MOSSY_LIVINGROCK_BRICK_STAIRS, BotaniaBlocks.MOSSY_LIVINGROCK_BRICKS).save(recipeOutput);
		slabShape(BotaniaBlocks.MOSSY_LIVINGROCK_BRICK_SLAB, BotaniaBlocks.MOSSY_LIVINGROCK_BRICKS).save(recipeOutput);
		wallShape(BotaniaBlocks.MOSSY_LIVINGROCK_BRICK_WALL, BotaniaBlocks.MOSSY_LIVINGROCK_BRICKS, 6).save(recipeOutput);

		stairs(BotaniaBlocks.SHIMMERROCK_STAIRS, BotaniaBlocks.SHIMMERROCK).save(recipeOutput);
		slabShape(BotaniaBlocks.SHIMMERROCK_SLAB, BotaniaBlocks.SHIMMERROCK).save(recipeOutput);
		wallShape(BotaniaBlocks.SHIMMERROCK_WALL, BotaniaBlocks.SHIMMERROCK, 6).save(recipeOutput);
		button(BotaniaBlocks.SHIMMERROCK_BUTTON, BotaniaBlocks.SHIMMERROCK).save(recipeOutput);
		pressurePlate(BotaniaBlocks.SHIMMERROCK_PRESSURE_PLATE, BotaniaBlocks.SHIMMERROCK).save(recipeOutput);

		for (String variant : LibBlockNames.METAMORPHIC_VARIANTS) {
			registerForMetamorphic(recipeOutput, variant);
		}

		Ingredient[] pavementIngredients = {
				Ingredient.EMPTY,
				Ingredient.of(Items.COAL),
				Ingredient.of(ConventionalItemTags.STORAGE_BLOCKS_LAPIS),
				Ingredient.of(ConventionalItemTags.REDSTONE_DUSTS),
				Ingredient.of(Items.WHEAT),
				Ingredient.of(Items.SLIME_BALL)
		};
		for (int i = 0; i < pavementIngredients.length; i++) {
			registerForPavement(recipeOutput, LibBlockNames.PAVEMENT_VARIANTS[i], pavementIngredients[i]);
		}

		wallShape(BotaniaBlocks.MANAGLASS_PANE, BotaniaBlocks.MANAGLASS, 16).save(recipeOutput);
		wallShape(BotaniaBlocks.ALFGLASS_PANE, BotaniaBlocks.ALFGLASS, 16).save(recipeOutput);
		wallShape(BotaniaBlocks.BIFROST_PANE, BotaniaBlocks.BIFROST, 16).save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.AZULEJO_0)
				.requires(Items.BLUE_DYE)
				.requires(BotaniaTags.Items.BLOCKS_QUARTZ)
				.unlockedBy("has_item", conditionsFromTag(BotaniaTags.Items.BLOCKS_QUARTZ))
				.save(recipeOutput, "botania:azulejo");

		List<Item> allAzulejos = IntStream.range(0, 16).mapToObj(i -> "azulejo_" + i)
				.map(BotaniaAPI::botaniaRL)
				.map(BuiltInRegistries.ITEM::get)
				.toList();
		for (int i = 0; i < allAzulejos.size(); i++) {
			int resultIndex = (i + 1) % allAzulejos.size();
			String recipeName = "azulejo_" + resultIndex;
			ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, allAzulejos.get(resultIndex))
					.requires(allAzulejos.get(i))
					.unlockedBy("has_azulejo", conditionsFromItem(BotaniaBlocks.AZULEJO_0))
					.group("botania:azulejo_cycling")
					.save(recipeOutput, prefix(recipeName));
		}

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaItems.TRINKET_CASE)
				.define('C', ConventionalItemTags.WOODEN_CHESTS)
				.define('G', ConventionalItemTags.GOLD_INGOTS)
				.define('M', ConventionalBotaniaTags.Items.MANASTEEL_INGOTS)
				.pattern(" M ")
				.pattern("MCM")
				.pattern(" G ")
				.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS))
				.save(recipeOutput);

		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_BLACK_BOWTIE, BotaniaItems.WHITE_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_BLACK_TIE, BotaniaItems.ORANGE_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_RED_GLASSES, BotaniaItems.MAGENTA_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_PUFFY_SCARF, BotaniaItems.LIGHT_BLUE_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_ENGINEER_GOGGLES, BotaniaItems.YELLOW_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_EYEPATCH, BotaniaItems.LIME_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_WICKED_EYEPATCH, BotaniaItems.PINK_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_RED_RIBBONS, BotaniaItems.GRAY_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_PINK_FLOWER_BUD, BotaniaItems.LIGHT_GRAY_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_POLKA_DOTTED_BOWS, BotaniaItems.CYAN_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_BLUE_BUTTERFLY, BotaniaItems.PURPLE_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_CAT_EARS, BotaniaItems.BLUE_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_WITCH_PIN, BotaniaItems.BROWN_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_DEVIL_TAIL, BotaniaItems.GREEN_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_KAMUI_EYE, BotaniaItems.RED_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_GOOGLY_EYES, BotaniaItems.BLACK_MYSTICAL_PETAL);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_FOUR_LEAFED_CLOVER, Items.WHITE_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_CLOCK_EYE, Items.ORANGE_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_UNICORN_HORN, Items.MAGENTA_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_DEVIL_HORNS, Items.LIGHT_BLUE_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_HYPER_PLUS, Items.YELLOW_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_BOTANIST_EMBLEM, Items.LIME_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_ANCIENT_MASK, Items.PINK_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_EERIE_MASK, Items.GRAY_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_ALIEN_ANTENNA, Items.LIGHT_GRAY_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_ANAGLYPH_GLASSES, Items.CYAN_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_ORANGE_SHADES, Items.PURPLE_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_GROUCHO_GLASSES, Items.BLUE_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_THICK_EYEBROWS, Items.BROWN_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_LUSITANIC_SHIELD, Items.GREEN_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_TINY_POTATO_MASK, Items.RED_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_QUESTGIVER_MARK, Items.BLACK_DYE);
		cosmeticBauble(recipeOutput, BotaniaItems.COSMETIC_THINKING_HAND, BotaniaBlocks.TINY_POTATO);
	}

	protected void registerSimpleArmorSet(RecipeOutput recipeOutput, Ingredient item, String variant,
			Criterion<InventoryChangeTrigger.TriggerInstance> criterion) {
		Item helmet = getItemOrThrow(prefix(variant + "_helmet"));
		Item chestplate = getItemOrThrow(prefix(variant + "_chestplate"));
		Item leggings = getItemOrThrow(prefix(variant + "_leggings"));
		Item boots = getItemOrThrow(prefix(variant + "_boots"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet)
				.define('S', item)
				.pattern("SSS")
				.pattern("S S")
				.unlockedBy("has_item", criterion)
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate)
				.define('S', item)
				.pattern("S S")
				.pattern("SSS")
				.pattern("SSS")
				.unlockedBy("has_item", criterion)
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggings)
				.define('S', item)
				.pattern("SSS")
				.pattern("S S")
				.pattern("S S")
				.unlockedBy("has_item", criterion)
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots)
				.define('S', item)
				.pattern("S S")
				.pattern("S S")
				.unlockedBy("has_item", criterion)
				.save(recipeOutput);
	}

	protected void registerToolSetRecipes(RecipeOutput recipeOutput, Ingredient item, Ingredient stick,
			Criterion<InventoryChangeTrigger.TriggerInstance> criterion, ItemLike sword, ItemLike pickaxe,
			ItemLike axe, ItemLike hoe, ItemLike shovel, ItemLike shears) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pickaxe)
				.define('S', item)
				.define('T', stick)
				.pattern("SSS")
				.pattern(" T ")
				.pattern(" T ")
				.unlockedBy("has_item", criterion)
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, shovel)
				.define('S', item)
				.define('T', stick)
				.pattern("S")
				.pattern("T")
				.pattern("T")
				.unlockedBy("has_item", criterion)
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axe)
				.define('S', item)
				.define('T', stick)
				.pattern("SS")
				.pattern("ST")
				.pattern(" T")
				.unlockedBy("has_item", criterion)
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoe)
				.define('S', item)
				.define('T', stick)
				.pattern("SS")
				.pattern(" T")
				.pattern(" T")
				.unlockedBy("has_item", criterion)
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, sword)
				.define('S', item)
				.define('T', stick)
				.pattern("S")
				.pattern("S")
				.pattern("T")
				.unlockedBy("has_item", criterion)
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, shears)
				.define('S', item)
				.pattern(" S")
				.pattern("S ")
				.unlockedBy("has_item", criterion)
				.save(recipeOutput);

	}

	protected void registerTerrasteelUpgradeRecipe(RecipeOutput recipeOutput, ItemLike output,
			ItemLike upgradedInput, ItemLike runeInput) {
		WrapperRecipeBuilder.wrap(ArmorUpgradeRecipe.SERIALIZER,
				ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
						.define('T', BotaniaItems.LIVINGWOOD_TWIG)
						.define('S', ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS)
						.define('R', runeInput)
						.define('A', upgradedInput)
						.pattern("TRT")
						.pattern("SAS")
						.pattern(" S ")
						.unlockedBy("has_item", conditionsFromTag(ConventionalBotaniaTags.Items.TERRASTEEL_INGOTS))
						.unlockedBy("has_prev_tier", conditionsFromItem(upgradedInput))
		).save(recipeOutput);
	}

	public static void registerRedStringBlock(RecipeOutput recipeOutput, ItemLike output, Ingredient input) {
		ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, output)
				.define('R', BotaniaBlocks.LIVINGROCK)
				.define('S', BotaniaItems.RED_STRING)
				.define('M', input)
				.pattern("RRR")
				.pattern("RMS")
				.pattern("RRR")
				.unlockedBy("has_redstring", conditionsFromItem(BotaniaItems.RED_STRING));
		builder.save(recipeOutput);
	}

	protected void createFloatingFlowerRecipe(RecipeOutput recipeOutput, ItemLike input) {
		ResourceLocation inputName = BuiltInRegistries.ITEM.getKey(input.asItem());
		Item output = getItemOrThrow(ResourceLocation.fromNamespaceAndPath(inputName.getNamespace(), "floating_" + inputName.getPath()));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, output)
				.requires(BotaniaTags.Items.FLOATING_FLOWERS)
				.requires(input)
				.group("botania:floating_flower")
				.unlockedBy("has_item", conditionsFromItem(input))
				.save(recipeOutput);
	}

	protected void deconstruct(RecipeOutput recipeOutput, ItemLike output, ItemLike input, String name) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 9)
				.unlockedBy("has_item", conditionsFromItem(output))
				.requires(input)
				.save(recipeOutput, prefix("conversions/" + name));
	}

	protected void deconstruct(RecipeOutput recipeOutput, ItemLike output, TagKey<Item> input, String name) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 9)
				.unlockedBy("has_item", conditionsFromItem(output))
				.requires(input)
				.save(recipeOutput, prefix("conversions/" + name));
	}

	protected void deconstructPetalBlock(RecipeOutput recipeOutput, ItemLike output, ItemLike input) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 9)
				.unlockedBy("has_item", conditionsFromItem(output))
				.requires(input).group("botania:petal_block_deconstruct")
				.save(recipeOutput, prefix("conversions/" + BuiltInRegistries.ITEM.getKey(input.asItem()).getPath() + "_deconstruct"));
	}

	protected void recombineSlab(RecipeOutput recipeOutput, ItemLike fullBlock, ItemLike slab) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, fullBlock)
				.define('Q', slab)
				.pattern("QQ")
				.unlockedBy("has_item", conditionsFromItem(slab))
				.save(recipeOutput, prefix("slab_recombine/" + BuiltInRegistries.ITEM.getKey(fullBlock.asItem()).getPath()));
	}

	protected ShapedRecipeBuilder petalApothecary(ItemLike block, ItemLike apothecary) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, apothecary)
				.define('P', BotaniaTags.Items.PETALS)
				.define('C', block)
				.pattern("CPC")
				.pattern(" C ")
				.pattern("CCC");
	}

	protected void registerForQuartz(RecipeOutput recipeOutput, String variant, ItemLike baseItem) {
		Block base = getBlockOrThrow(prefix(variant + LibBlockNames.BLOCK_SUFFIX));
		Block slab = getBlockOrThrow(prefix(variant + LibBlockNames.SLAB_SUFFIX));
		Block stairs = getBlockOrThrow(prefix(variant + LibBlockNames.STAIRS_SUFFIX));
		Block chiseled = getBlockOrThrow(prefix(LibBlockNames.CHISELED_PREFIX + variant + LibBlockNames.BLOCK_SUFFIX));
		Block pillar = getBlockOrThrow(prefix(variant + LibBlockNames.PILLAR_SUFFIX));

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, base)
				.define('Q', baseItem)
				.pattern("QQ")
				.pattern("QQ")
				.group("botania:quartz_block")
				.unlockedBy("has_item", conditionsFromItem(baseItem))
				.save(recipeOutput);
		stairs(stairs, base).group("botania:quartz_stairs").save(recipeOutput);
		slabShape(slab, base).group("botania:quartz_slab").save(recipeOutput);
		pillar(pillar, base).group("botania:quartz_pillar").save(recipeOutput);
		chiseled(chiseled, slab).group("botania:quartz_chiseled")
				.unlockedBy("has_base_item", conditionsFromItem(base)).save(recipeOutput);

		// TODO: smooth block to stairs/slabs recipes
	}

	protected void registerForWood(RecipeOutput recipeOutput, String variant) {

		TagKey<Item> tag = variant.contains(LibBlockNames.LIVINGWOOD)
				? BotaniaTags.Items.LIVINGWOOD_LOGS
				: BotaniaTags.Items.DREAMWOOD_LOGS;
		Block log = getBlockOrThrow(prefix(variant + LibBlockNames.LOG_SUFFIX));
		Block wood = getBlockOrThrow(prefix(variant));
		Block strippedLog = getBlockOrThrow(prefix(
				LibBlockNames.STRIPPED_PREFIX + variant + LibBlockNames.LOG_SUFFIX));
		Block strippedWood = getBlockOrThrow(prefix(LibBlockNames.STRIPPED_PREFIX + variant));
		Block glimmeringLog = getBlockOrThrow(prefix(
				LibBlockNames.GLIMMERING_PREFIX + variant + LibBlockNames.LOG_SUFFIX));
		Block glimmeringWood = getBlockOrThrow(prefix(LibBlockNames.GLIMMERING_PREFIX + variant));
		Block glimmeringStrippedLog = getBlockOrThrow(prefix(
				LibBlockNames.GLIMMERING_PREFIX + LibBlockNames.STRIPPED_PREFIX + variant + LibBlockNames.LOG_SUFFIX));
		Block glimmeringStrippedWood = getBlockOrThrow(prefix(
				LibBlockNames.GLIMMERING_PREFIX + LibBlockNames.STRIPPED_PREFIX + variant));
		Block stairs = getBlockOrThrow(prefix(variant + LibBlockNames.STAIRS_SUFFIX));
		Block slab = getBlockOrThrow(prefix(variant + LibBlockNames.SLAB_SUFFIX));
		Block wall = getBlockOrThrow(prefix(variant + LibBlockNames.WALL_SUFFIX));
		Block strippedStairs = getBlockOrThrow(prefix(
				LibBlockNames.STRIPPED_PREFIX + variant + LibBlockNames.STAIRS_SUFFIX));
		Block strippedSlab = getBlockOrThrow(prefix(
				LibBlockNames.STRIPPED_PREFIX + variant + LibBlockNames.SLAB_SUFFIX));
		Block strippedWall = getBlockOrThrow(prefix(
				LibBlockNames.STRIPPED_PREFIX + variant + LibBlockNames.WALL_SUFFIX));

		Block planks = getBlockOrThrow(prefix(variant + LibBlockNames.PLANKS_SUFFIX));
		Block planksStairs = getBlockOrThrow(prefix(
				variant + LibBlockNames.PLANK_INFIX + LibBlockNames.STAIRS_SUFFIX));
		Block planksSlab = getBlockOrThrow(prefix(
				variant + LibBlockNames.PLANK_INFIX + LibBlockNames.SLAB_SUFFIX));
		Block mossyPlanks = getBlockOrThrow(prefix(
				LibBlockNames.MOSSY_PREFIX + variant + LibBlockNames.PLANKS_SUFFIX));
		Block framed = getBlockOrThrow(prefix(LibBlockNames.FRAMED_PREFIX + variant));
		Block patternFramed = getBlockOrThrow(prefix(LibBlockNames.PATTERN_FRAMED_PREFIX + variant));
		Block fence = getBlockOrThrow(prefix(variant + LibBlockNames.FENCE_SUFFIX));
		Block fenceGate = getBlockOrThrow(prefix(variant + LibBlockNames.FENCE_GATE_SUFFIX));
		Block button = getBlockOrThrow(prefix(variant + LibBlockNames.BUTTON_SUFFIX));
		Block pressurePlate = getBlockOrThrow(prefix(variant + LibBlockNames.PRESSURE_PLATE_SUFFIX));

		Block door = getBlockOrThrow(prefix(variant + LibBlockNames.DOOR_SUFFIX));
		Block trapdoor = getBlockOrThrow(prefix(variant + LibBlockNames.TRAPDOOR_SUFFIX));
		Block sign = getBlockOrThrow(prefix(variant + LibBlockNames.SIGN_SUFFIX));
		Block hangingSign = getBlockOrThrow(prefix(variant + LibBlockNames.HANGING_SIGN_SUFFIX));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
				.requires(tag)
				.group("planks")
				.unlockedBy("has_item", conditionsFromTag(tag)).save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood, 3)
				.group("wood")
				.unlockedBy("has_log", conditionsFromItem(log))
				.define('#', log)
				.pattern("##")
				.pattern("##")
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, strippedWood, 3)
				.group("wood")
				.unlockedBy("has_log", conditionsFromItem(strippedLog))
				.define('#', strippedLog)
				.pattern("##")
				.pattern("##")
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, glimmeringLog)
				.group("botania:glimmering_" + variant)
				.requires(log)
				.requires(ConventionalItemTags.GLOWSTONE_DUSTS)
				.unlockedBy("has_item", conditionsFromItem(log))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, glimmeringWood)
				.group("botania:glimmering_" + variant)
				.requires(wood)
				.requires(ConventionalItemTags.GLOWSTONE_DUSTS)
				.unlockedBy("has_item", conditionsFromItem(wood))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, glimmeringStrippedLog)
				.group("botania:glimmering_" + variant)
				.requires(strippedLog)
				.requires(ConventionalItemTags.GLOWSTONE_DUSTS)
				.unlockedBy("has_item", conditionsFromItem(strippedLog))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, glimmeringStrippedWood)
				.group("botania:glimmering_" + variant)
				.requires(strippedWood)
				.requires(ConventionalItemTags.GLOWSTONE_DUSTS)
				.unlockedBy("has_item", conditionsFromItem(strippedWood))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, glimmeringWood, 3)
				.group("botania:glimmering_" + variant)
				.unlockedBy("has_log", conditionsFromItem(glimmeringLog))
				.define('#', glimmeringLog)
				.pattern("##")
				.pattern("##")
				.save(recipeOutput, prefix("glimmering_" + variant + "_from_log"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, glimmeringStrippedWood, 3)
				.group("botania:glimmering_" + variant)
				.unlockedBy("has_log", conditionsFromItem(glimmeringStrippedLog))
				.define('#', glimmeringStrippedLog)
				.pattern("##")
				.pattern("##")
				.save(recipeOutput, prefix("glimmering_stripped_" + variant + "_from_log"));

		stairs(stairs, wood).save(recipeOutput);
		slabShape(slab, wood).save(recipeOutput);
		wallShape(wall, wood, 6).save(recipeOutput);
		fence(fence, planks).save(recipeOutput);
		fenceGate(fenceGate, planks).save(recipeOutput);
		button(button, planks).save(recipeOutput);
		pressurePlate(pressurePlate, planks).save(recipeOutput);

		stairs(strippedStairs, strippedWood).save(recipeOutput);
		slabShape(strippedSlab, strippedWood).save(recipeOutput);
		wallShape(strippedWall, strippedWood, 6).save(recipeOutput);

		stairs(planksStairs, planks).save(recipeOutput);
		slabShape(planksSlab, planks).save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, mossyPlanks)
				.requires(planks)
				.requires(Items.MOSS_BLOCK)
				.unlockedBy("has_item", conditionsFromItem(planks))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, mossyPlanks)
				.requires(planks)
				.requires(Items.VINE)
				.unlockedBy("has_item", conditionsFromItem(planks))
				.save(recipeOutput, prefix("mossy_" + variant + "_planks_vine"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, framed, 4)
				.define('W', planks)
				.pattern("W W")
				.pattern("W W")
				.unlockedBy("has_item", conditionsFromItem(planks))
				.save(recipeOutput);
		ringShape(patternFramed, planks).save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, door, 3)
				.define('#', planks)
				.pattern("##")
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_item", conditionsFromItem(planks))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, trapdoor, 2)
				.define('#', planks)
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_item", conditionsFromItem(planks))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, sign, 3)
				.group("sign")
				.define('#', planks)
				.define('X', Items.STICK)
				.pattern("###")
				.pattern("###")
				.pattern(" X ")
				.unlockedBy("has_item", conditionsFromItem(planks))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, hangingSign, 6)
				.group("hanging_sign")
				.define('#', strippedLog)
				.define('X', Items.CHAIN)
				.pattern("X X")
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_stripped_logs", conditionsFromItem(strippedLog))
				.save(recipeOutput);
	}

	private void registerForPavement(RecipeOutput recipeOutput, String color, Ingredient mainInput) {
		String baseName = color + LibBlockNames.PAVEMENT_SUFFIX;
		Block base = getBlockOrThrow(prefix(baseName));
		Block stair = getBlockOrThrow(prefix(baseName + LibBlockNames.STAIRS_SUFFIX));
		Block slab = getBlockOrThrow(prefix(baseName + LibBlockNames.SLAB_SUFFIX));

		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, base, 3)
				.requires(BotaniaBlocks.LIVINGROCK)
				.requires(Items.COBBLESTONE)
				.requires(Items.GRAVEL)
				.group("botania:pavement")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.LIVINGROCK));
		if (mainInput != Ingredient.EMPTY) {
			builder.requires(mainInput);
		}
		builder.save(recipeOutput);

		slabShape(slab, base).group("botania:pavement_slab").save(recipeOutput);
		stairs(stair, base).group("botania:pavement_stairs").save(recipeOutput);
	}

	private void registerForMetamorphic(RecipeOutput recipeOutput, String variant) {
		Block base = getBlockOrThrow(prefix(variant));
		Block slab = getBlockOrThrow(prefix(variant + LibBlockNames.SLAB_SUFFIX));
		Block stair = getBlockOrThrow(prefix(variant + LibBlockNames.STAIRS_SUFFIX));
		Block wall = getBlockOrThrow(prefix(variant + LibBlockNames.WALL_SUFFIX));
		Block button = getBlockOrThrow(prefix(variant + LibBlockNames.BUTTON_SUFFIX));
		Block pressurePlate = getBlockOrThrow(prefix(variant + LibBlockNames.PRESSURE_PLATE_SUFFIX));
		Block brick = getBlockOrThrow(prefix(variant + LibBlockNames.BRICKS_SUFFIX));
		Block brickSlab = getBlockOrThrow(prefix(variant + LibBlockNames.BRICK_INFIX + LibBlockNames.SLAB_SUFFIX));
		Block brickStair = getBlockOrThrow(prefix(variant + LibBlockNames.BRICK_INFIX + LibBlockNames.STAIRS_SUFFIX));
		Block brickWall = getBlockOrThrow(prefix(variant + LibBlockNames.BRICK_INFIX + LibBlockNames.WALL_SUFFIX));
		Block chiseledBrick = getBlockOrThrow(prefix(LibBlockNames.CHISELED_PREFIX + variant + LibBlockNames.BRICKS_SUFFIX));
		Block cobble = getBlockOrThrow(prefix(LibBlockNames.COBBLED_PREFIX + variant));
		Block cobbleSlab = getBlockOrThrow(prefix(LibBlockNames.COBBLED_PREFIX + variant + LibBlockNames.SLAB_SUFFIX));
		Block cobbleStair = getBlockOrThrow(prefix(LibBlockNames.COBBLED_PREFIX + variant + LibBlockNames.STAIRS_SUFFIX));
		Block cobbleWall = getBlockOrThrow(prefix(LibBlockNames.COBBLED_PREFIX + variant + LibBlockNames.WALL_SUFFIX));

		slabShape(slab, base).group("botania:metamorphic_stone_slab").save(recipeOutput);
		stairs(stair, base).group("botania:metamorphic_stone_stairs").save(recipeOutput);
		wallShape(wall, base, 6).group("botania:metamorphic_stone_wall").save(recipeOutput);
		button(button, base).group("botania:metamorphic_stone_button").save(recipeOutput);
		pressurePlate(pressurePlate, base).group("botania:metamorphic_stone_pressure_plate").save(recipeOutput);

		brick(brick, base).group("botania:metamorphic_brick").save(recipeOutput);
		slabShape(brickSlab, brick).group("botania:metamorphic_brick_slab").save(recipeOutput);
		stairs(brickStair, brick).group("botania:metamorphic_brick_stairs").save(recipeOutput);
		wallShape(brickWall, brick, 6).group("botania:metamorphic_brick_wall").save(recipeOutput);
		chiseled(chiseledBrick, brickSlab).unlockedBy("has_base_item", conditionsFromItem(brick)).save(recipeOutput);

		slabShape(cobbleSlab, cobble).group("botania:metamorphic_cobble_slab").save(recipeOutput);
		stairs(cobbleStair, cobble).group("botania:metamorphic_cobble_stairs").save(recipeOutput);
		wallShape(cobbleWall, cobble, 6).group("botania:metamorphic_cobble_wall").save(recipeOutput);
	}

	private ShapedRecipeBuilder compression(ItemLike output, TagKey<Item> input) {
		return ShapedRecipeBuilder.shaped(output instanceof Block ? RecipeCategory.BUILDING_BLOCKS : RecipeCategory.MISC, output)
				.define('I', input)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_item", conditionsFromTag(input));
	}

	protected ShapedRecipeBuilder compression(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(output instanceof Block ? RecipeCategory.BUILDING_BLOCKS : RecipeCategory.MISC, output)
				.define('I', input)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_item", conditionsFromItem(input));
	}

	protected ShapedRecipeBuilder brick(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 4)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('Q', input)
				.pattern("QQ")
				.pattern("QQ");
	}

	protected ShapedRecipeBuilder stairs(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 4)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('Q', input)
				.pattern("Q  ")
				.pattern("QQ ")
				.pattern("QQQ");
	}

	protected ShapedRecipeBuilder slabShape(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 6)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('Q', input)
				.pattern("QQQ");
	}

	protected ShapelessRecipeBuilder button(ItemLike output, ItemLike input) {
		return ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, output)
				.unlockedBy("has_item", conditionsFromItem(input))
				.requires(input);
	}

	protected ShapedRecipeBuilder pressurePlate(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, output)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('#', input)
				.pattern("##");
	}

	protected ShapedRecipeBuilder pillar(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 2)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('Q', input)
				.pattern("Q")
				.pattern("Q");
	}

	protected ShapedRecipeBuilder chiseled(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('Q', input)
				.pattern("Q")
				.pattern("Q");
	}

	protected ShapedRecipeBuilder wallShape(ItemLike output, ItemLike input, int amount) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, amount)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('B', input)
				.pattern("BBB")
				.pattern("BBB");
	}

	protected ShapedRecipeBuilder fence(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, 3)
				.unlockedBy("has_item", conditionsFromItem(input))
				.group("wooden_fence")
				.define('B', input)
				.define('S', Items.STICK)
				.pattern("BSB")
				.pattern("BSB");
	}

	protected ShapedRecipeBuilder fenceGate(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, output, 1)
				.unlockedBy("has_item", conditionsFromItem(input))
				.group("wooden_fence_gate")
				.define('B', input)
				.define('S', Items.STICK)
				.pattern("SBS")
				.pattern("SBS");
	}

	protected ShapedRecipeBuilder ringShape(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 4)
				.define('W', input)
				.pattern(" W ")
				.pattern("W W")
				.pattern(" W ")
				.unlockedBy("has_item", conditionsFromItem(input));
	}

	protected void cosmeticBauble(RecipeOutput recipeOutput, ItemLike output, ItemLike input) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output)
				.define('P', input)
				.define('S', BotaniaItems.MANA_INFUSED_STRING)
				.pattern("PPP")
				.pattern("PSP")
				.pattern("PPP")
				.group("botania:cosmetic_bauble")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.MANA_INFUSED_STRING))
				.save(recipeOutput);
	}

	protected void specialRecipe(RecipeOutput recipeOutput, Function<CraftingBookCategory, Recipe<?>> factory,
			CraftingBookCategory category) {
		BotaniaSpecialRecipeBuilder.special(factory, category).save(recipeOutput);
	}

	protected Block getBlockOrThrow(ResourceLocation location) {
		return BuiltInRegistries.BLOCK.getOrThrow(ResourceKey.create(Registries.BLOCK, location));
	}

	protected Item getItemOrThrow(ResourceLocation location) {
		return BuiltInRegistries.ITEM.getOrThrow(ResourceKey.create(Registries.ITEM, location));
	}

	@Override
	public String getName() {
		return "Botania crafting recipes";
	}
}
