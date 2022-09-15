/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import org.apache.commons.lang3.mutable.MutableObject;

import vazkii.botania.api.state.enums.CraftyCratePattern;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.block.BotaniaFluffBlocks;
import vazkii.botania.common.crafting.recipe.*;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.common.lib.ResourceLocationHelper;
import vazkii.botania.mixin.AccessorIngredient;
import vazkii.botania.mixin.AccessorRecipeProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RecipeProvider extends BotaniaRecipeProvider {
	public RecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	public void registerRecipes(Consumer<FinishedRecipe> consumer) {
		specialRecipe(consumer, AncientWillRecipe.SERIALIZER);
		specialRecipe(consumer, BlackHoleTalismanExtractRecipe.SERIALIZER);
		specialRecipe(consumer, CompositeLensRecipe.SERIALIZER);
		specialRecipe(consumer, CosmeticAttachRecipe.SERIALIZER);
		specialRecipe(consumer, CosmeticRemoveRecipe.SERIALIZER);
		specialRecipe(consumer, ResoluteIvyRecipe.SERIALIZER);
		specialRecipe(consumer, LaputaShardUpgradeRecipe.SERIALIZER);
		specialRecipe(consumer, LensDyeingRecipe.SERIALIZER);
		specialRecipe(consumer, ManaBlasterClipRecipe.SERIALIZER);
		specialRecipe(consumer, ManaBlasterLensRecipe.SERIALIZER);
		specialRecipe(consumer, ManaBlasterRemoveLensRecipe.SERIALIZER);
		specialRecipe(consumer, MergeVialRecipe.SERIALIZER);
		specialRecipe(consumer, PhantomInkRecipe.SERIALIZER);
		specialRecipe(consumer, SpellbindingClothRecipe.SERIALIZER);
		specialRecipe(consumer, SplitLensRecipe.SERIALIZER);
		specialRecipe(consumer, TerraShattererTippingRecipe.SERIALIZER);

		registerMain(consumer);
		registerMisc(consumer);
		registerTools(consumer);
		registerTrinkets(consumer);
		registerLenses(consumer);
		registerCorporeaAndRedString(consumer);
		registerFloatingFlowers(consumer);
		registerConversions(consumer);
		registerDecor(consumer);
	}

	public static InventoryChangeTrigger.TriggerInstance conditionsFromItem(ItemLike item) {
		return AccessorRecipeProvider.botania_condition(ItemPredicate.Builder.item().of(item).build());
	}

	private static InventoryChangeTrigger.TriggerInstance conditionsFromItems(ItemLike... items) {
		ItemPredicate[] preds = new ItemPredicate[items.length];
		for (int i = 0; i < items.length; i++) {
			preds[i] = ItemPredicate.Builder.item().of(items[i]).build();
		}

		return AccessorRecipeProvider.botania_condition(preds);
	}

	public static InventoryChangeTrigger.TriggerInstance conditionsFromTag(TagKey<Item> tag) {
		return AccessorRecipeProvider.botania_condition(ItemPredicate.Builder.item().of(tag).build());
	}

	/** Addons: override this to return your modid */
	protected ResourceLocation prefix(String path) {
		return ResourceLocationHelper.prefix(path);
	}

	private void registerMain(Consumer<FinishedRecipe> consumer) {
		InventoryChangeTrigger.TriggerInstance hasAnyDye = conditionsFromItems(
				Arrays.stream(DyeColor.values()).map(DyeItem::byColor).toArray(ItemLike[]::new)
		);
		MutableObject<FinishedRecipe> base = new MutableObject<>();
		MutableObject<FinishedRecipe> gog = new MutableObject<>();
		ShapedRecipeBuilder.shaped(BotaniaBlocks.manaSpreader)
				.define('P', ModTags.Items.PETALS)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.define('G', Items.GOLD_INGOT)
				.pattern("WWW")
				.pattern("GP ")
				.pattern("WWW")
				.group("botania:spreader")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD_LOGS))
				.save(base::setValue);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.manaSpreader)
				.define('P', ModTags.Items.PETALS)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern("WWW")
				.pattern("WP ")
				.pattern("WWW")
				.group("botania:spreader")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD_LOGS))
				.save(gog::setValue);
		consumer.accept(new GogAlternationResult(gog.getValue(), base.getValue()));
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.redstoneSpreader)
				.requires(BotaniaBlocks.manaSpreader)
				.requires(Items.REDSTONE)
				.group("botania:spreader")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.manaSpreader))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.elvenSpreader)
				.define('P', ModTags.Items.PETALS)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('W', ModTags.Items.DREAMWOOD_LOGS)
				.pattern("WWW")
				.pattern("EP ")
				.pattern("WWW")
				.group("botania:spreader")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.DREAMWOOD_LOGS))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.gaiaSpreader)
				.requires(BotaniaBlocks.elvenSpreader)
				.requires(ModTags.Items.GEMS_DRAGONSTONE)
				.requires(BotaniaItems.lifeEssence)
				.group("botania:spreader")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.manaPool)
				.define('R', BotaniaBlocks.livingrock)
				.pattern("R R")
				.pattern("RRR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.livingrock))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.dilutedPool)
				.define('R', BotaniaFluffBlocks.livingrockSlab)
				.pattern("R R")
				.pattern("RRR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.livingrock))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.fabulousPool)
				.define('R', BotaniaBlocks.shimmerrock)
				.pattern("R R")
				.pattern("RRR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.shimmerrock))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.rainbowRod))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.fabulousPool)
				.define('P', BotaniaBlocks.manaPool)
				.define('B', BotaniaBlocks.bifrostPerm)
				.pattern("BPB")
				.pattern("BBB")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.bifrostPerm))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.rainbowRod))
				.save(consumer, prefix(Registry.ITEM.getKey(BotaniaBlocks.fabulousPool.asItem()).getPath() + "_upgrade"));
		ShapedRecipeBuilder.shaped(BotaniaBlocks.runeAltar)
				.define('P', AccessorIngredient.callFromValues(Stream.of(
						new Ingredient.ItemValue(new ItemStack(BotaniaItems.manaPearl)),
						new Ingredient.TagValue(ModTags.Items.GEMS_MANA_DIAMOND))))
				.define('S', BotaniaBlocks.livingrock)
				.pattern("SSS")
				.pattern("SPS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaPearl))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.manaPylon)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.define('G', Items.GOLD_INGOT)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern(" G ")
				.pattern("MDM")
				.pattern(" G ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.naturaPylon)
				.define('P', BotaniaBlocks.manaPylon)
				.define('T', ModTags.Items.NUGGETS_TERRASTEEL)
				.define('E', Items.ENDER_EYE)
				.pattern(" T ")
				.pattern("TPT")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.manaPylon))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.gaiaPylon)
				.define('P', BotaniaBlocks.manaPylon)
				.define('D', BotaniaItems.pixieDust)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern(" D ")
				.pattern("EPE")
				.pattern(" D ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.distributor)
				.define('R', BotaniaBlocks.livingrock)
				.define('S', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("RRR")
				.pattern("S S")
				.pattern("RRR")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.manaVoid)
				.define('S', BotaniaBlocks.livingrock)
				.define('O', Items.OBSIDIAN)
				.pattern("SSS")
				.pattern("O O")
				.pattern("SSS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.livingrock))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.manaDetector)
				.define('R', Items.REDSTONE)
				.define('T', Blocks.TARGET)
				.define('S', BotaniaBlocks.livingrock)
				.pattern("RSR")
				.pattern("STS")
				.pattern("RSR")
				.unlockedBy("has_item", conditionsFromItem(Blocks.TARGET))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaBlocks.livingrock))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.turntable)
				.define('P', Items.STICKY_PISTON)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern("WWW")
				.pattern("WPW")
				.pattern("WWW")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD_LOGS))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.STICKY_PISTON))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.tinyPlanet)
				.define('P', BotaniaItems.tinyPlanet)
				.define('S', Items.STONE)
				.pattern("SSS")
				.pattern("SPS")
				.pattern("SSS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.tinyPlanet))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.manaPearl))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.alchemyCatalyst)
				.define('P', BotaniaItems.manaPearl)
				.define('B', Items.BREWING_STAND)
				.define('S', BotaniaBlocks.livingrock)
				.define('G', Items.GOLD_INGOT)
				.pattern("SGS")
				.pattern("BPB")
				.pattern("SGS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaPearl))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.BREWING_STAND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.openCrate)
				.define('W', BotaniaBlocks.livingwoodPlanks)
				.pattern("WWW")
				.pattern("W W")
				.pattern("W W")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.livingwoodPlanks))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.craftCrate)
				.define('C', Items.CRAFTING_TABLE)
				.define('W', BotaniaBlocks.dreamwoodPlanks)
				.pattern("WCW")
				.pattern("W W")
				.pattern("W W")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.dreamwoodPlanks))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.forestEye)
				.define('S', BotaniaBlocks.livingrock)
				.define('E', Items.ENDER_EYE)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("MSM")
				.pattern("SES")
				.pattern("MSM")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.wildDrum)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.define('H', BotaniaItems.grassHorn)
				.define('L', Items.LEATHER)
				.pattern("WLW")
				.pattern("WHW")
				.pattern("WLW")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.grassHorn))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.gatheringDrum)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('W', ModTags.Items.DREAMWOOD_LOGS)
				.define('L', Items.LEATHER)
				.pattern("WLW")
				.pattern("WEW")
				.pattern("WLW")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.DREAMWOOD_LOGS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.canopyDrum)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.define('H', BotaniaItems.leavesHorn)
				.define('L', Items.LEATHER)
				.pattern("WLW")
				.pattern("WHW")
				.pattern("WLW")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.leavesHorn))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.abstrusePlatform, 2)
				.define('0', ModTags.Items.LIVINGWOOD_LOGS)
				.define('P', BotaniaItems.manaPearl)
				.define('3', BotaniaBlocks.livingwoodFramed)
				.define('4', BotaniaBlocks.livingwoodPatternFramed)
				.pattern("343")
				.pattern("0P0")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaPearl))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.spectralPlatform, 2)
				.define('0', ModTags.Items.DREAMWOOD_LOGS)
				.define('3', BotaniaBlocks.dreamwoodFramed)
				.define('4', BotaniaBlocks.dreamwoodPatternFramed)
				.define('D', BotaniaItems.pixieDust)
				.pattern("343")
				.pattern("0D0")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.pixieDust))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.alfPortal)
				.define('T', ModTags.Items.NUGGETS_TERRASTEEL)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern("WTW")
				.pattern("WTW")
				.pattern("WTW")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.conjurationCatalyst)
				.define('P', BotaniaBlocks.alchemyCatalyst)
				.define('B', BotaniaItems.pixieDust)
				.define('S', BotaniaBlocks.livingrock)
				.define('G', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("SBS")
				.pattern("GPG")
				.pattern("SGS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.spawnerClaw)
				.define('P', Items.PRISMARINE_BRICKS)
				.define('B', Items.BLAZE_ROD)
				.define('S', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('E', BotaniaItems.enderAirBottle)
				.define('M', ModTags.Items.BLOCKS_MANASTEEL)
				.pattern("BSB")
				.pattern("PMP")
				.pattern("PEP")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.enderAirBottle))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.enderEye)
				.define('R', Items.REDSTONE)
				.define('E', Items.ENDER_EYE)
				.define('O', Items.OBSIDIAN)
				.pattern("RER")
				.pattern("EOE")
				.pattern("RER")
				.unlockedBy("has_item", conditionsFromItem(Items.ENDER_EYE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.starfield)
				.define('P', BotaniaItems.pixieDust)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('O', Items.OBSIDIAN)
				.pattern("EPE")
				.pattern("EOE")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.rfGenerator)
				.define('R', Items.REDSTONE_BLOCK)
				.define('S', BotaniaBlocks.livingrock)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("SRS")
				.pattern("RMR")
				.pattern("SRS")
				.unlockedBy("has_item", conditionsFromItem(Items.REDSTONE_BLOCK))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.brewery)
				.define('A', BotaniaItems.runeMana)
				.define('R', BotaniaBlocks.livingrock)
				.define('S', Items.BREWING_STAND)
				.define('M', ModTags.Items.BLOCKS_MANASTEEL)
				.pattern("RSR")
				.pattern("RAR")
				.pattern("RMR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.runeMana))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.BREWING_STAND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.terraPlate)
				.define('0', BotaniaItems.runeWater)
				.define('1', BotaniaItems.runeFire)
				.define('2', BotaniaItems.runeEarth)
				.define('3', BotaniaItems.runeAir)
				.define('8', BotaniaItems.runeMana)
				.define('L', Blocks.LAPIS_BLOCK)
				.define('M', ModTags.Items.BLOCKS_MANASTEEL)
				.pattern("LLL")
				.pattern("0M1")
				.pattern("283")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.RUNES))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.prism)
				.define('P', Items.PRISMARINE_CRYSTALS)
				.define('S', BotaniaBlocks.spectralPlatform)
				.define('G', Items.GLASS)
				.pattern("GPG")
				.pattern("GSG")
				.pattern("GPG")
				.unlockedBy("has_item", conditionsFromItem(Items.PRISMARINE_CRYSTALS))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaBlocks.spectralPlatform))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.pump)
				.define('B', Items.BUCKET)
				.define('S', BotaniaBlocks.livingrock)
				.define('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("SSS")
				.pattern("IBI")
				.pattern("SSS")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.incensePlate)
				.define('S', BotaniaFluffBlocks.livingwoodSlab)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern("WSS")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD_LOGS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.hourglass)
				.define('R', Items.REDSTONE)
				.define('S', ModTags.Items.INGOTS_MANASTEEL)
				.define('G', Items.GOLD_INGOT)
				.define('M', BotaniaBlocks.manaGlass)
				.pattern("GMG")
				.pattern("RSR")
				.pattern("GMG")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.manaGlass))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.ghostRail)
				.requires(Items.RAIL)
				.requires(BotaniaBlocks.spectralPlatform)
				.unlockedBy("has_item", conditionsFromItem(Items.RAIL))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaBlocks.spectralPlatform))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.sparkChanger)
				.define('R', Items.REDSTONE)
				.define('S', BotaniaBlocks.livingrock)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("ESE")
				.pattern("SRS")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.felPumpkin)
				.define('P', Items.PUMPKIN)
				.define('B', Items.BONE)
				.define('S', Items.STRING)
				.define('F', Items.ROTTEN_FLESH)
				.define('G', Items.GUNPOWDER)
				.pattern(" S ")
				.pattern("BPF")
				.pattern(" G ")
				.unlockedBy("has_item", conditionsFromItem(Items.PUMPKIN))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.cocoon)
				.define('S', Items.STRING)
				.define('C', BotaniaItems.manaweaveCloth)
				.define('P', BotaniaBlocks.felPumpkin)
				.define('D', BotaniaItems.pixieDust)
				.pattern("SSS")
				.pattern("CPC")
				.pattern("SDS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.felPumpkin))
				.save(base::setValue);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.cocoon)
				.define('S', Items.STRING)
				.define('P', BotaniaBlocks.felPumpkin)
				.define('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("SSS")
				.pattern("SPS")
				.pattern("SIS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.felPumpkin))
				.save(gog::setValue);
		consumer.accept(new GogAlternationResult(gog.getValue(), base.getValue()));
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.lightRelayDefault)
				.requires(BotaniaItems.redString)
				.requires(ModTags.Items.GEMS_DRAGONSTONE)
				.requires(Items.GLOWSTONE_DUST)
				.requires(Items.GLOWSTONE_DUST)
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.lightRelayDetector)
				.requires(BotaniaBlocks.lightRelayDefault)
				.requires(Items.REDSTONE)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.lightRelayDefault))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.lightRelayFork)
				.requires(BotaniaBlocks.lightRelayDefault)
				.requires(Items.REDSTONE_TORCH)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.lightRelayDefault))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.lightRelayToggle)
				.requires(BotaniaBlocks.lightRelayDefault)
				.requires(Items.LEVER)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.lightRelayDefault))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.lightLauncher)
				.define('D', ModTags.Items.DREAMWOOD_LOGS)
				.define('L', BotaniaBlocks.lightRelayDefault)
				.pattern("DDD")
				.pattern("DLD")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.lightRelayDefault))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.manaBomb)
				.define('T', Items.TNT)
				.define('G', BotaniaItems.lifeEssence)
				.define('L', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern("LTL")
				.pattern("TGT")
				.pattern("LTL")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.bellows)
				.define('R', BotaniaItems.runeAir)
				.define('S', BotaniaFluffBlocks.livingwoodSlab)
				.define('L', Items.LEATHER)
				.pattern("SSS")
				.pattern("RL ")
				.pattern("SSS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.runeAir))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.bifrostPerm)
				.requires(BotaniaItems.rainbowRod)
				.requires(BotaniaBlocks.elfGlass)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.rainbowRod))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.cellBlock, 3)
				.requires(Items.CACTUS, 3)
				.requires(Items.BEETROOT)
				.requires(Items.CARROT)
				.requires(Items.POTATO)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFlowerBlocks.dandelifeon))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.teruTeruBozu)
				.define('C', BotaniaItems.manaweaveCloth)
				.define('S', Items.SUNFLOWER)
				.pattern("C")
				.pattern("C")
				.pattern("S")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaweaveCloth))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.avatar)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern(" W ")
				.pattern("WDW")
				.pattern("W W")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.animatedTorch)
				.define('D', ModTags.Items.DUSTS_MANA)
				.define('T', Items.REDSTONE_TORCH)
				.pattern("D")
				.pattern("T")
				.unlockedBy("has_item", conditionsFromItem(Items.REDSTONE_TORCH))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.DUSTS_MANA))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.livingwoodTwig)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern("W")
				.pattern("W")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD_LOGS))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.redstoneRoot)
				.requires(Items.REDSTONE)
				.requires(Ingredient.of(Items.FERN, Items.GRASS))
				.unlockedBy("has_item", conditionsFromItem(Items.REDSTONE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.dreamwoodTwig)
				.define('W', ModTags.Items.DREAMWOOD_LOGS)
				.pattern("W")
				.pattern("W")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.DREAMWOOD_LOGS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.gaiaIngot)
				.define('S', BotaniaItems.lifeEssence)
				.define('I', ModTags.Items.INGOTS_TERRASTEEL)
				.pattern(" S ")
				.pattern("SIS")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.manaweaveCloth)
				.define('S', BotaniaItems.manaString)
				.pattern("SS")
				.pattern("SS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaString))
				.save(consumer);
		Ingredient dyes = Ingredient.of(Items.WHITE_DYE, Items.ORANGE_DYE, Items.MAGENTA_DYE,
				Items.LIGHT_BLUE_DYE, Items.YELLOW_DYE, Items.LIME_DYE, Items.PINK_DYE, Items.GRAY_DYE,
				Items.LIGHT_GRAY_DYE, Items.CYAN_DYE, Items.PURPLE_DYE, Items.BLUE_DYE, Items.BROWN_DYE,
				Items.GREEN_DYE, Items.RED_DYE, Items.BLACK_DYE);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.fertilizer)
				.requires(Items.BONE_MEAL)
				.requires(dyes, 4)
				.unlockedBy("has_item", hasAnyDye)
				.save(base::setValue, "botania:fertilizer_dye");
		ShapelessRecipeBuilder.shapeless(BotaniaItems.fertilizer, 3)
				.requires(Items.BONE_MEAL)
				.requires(dyes, 4)
				.unlockedBy("has_item", hasAnyDye)
				.save(gog::setValue, "botania:fertilizer_dye");
		consumer.accept(new GogAlternationResult(gog.getValue(), base.getValue()));
		ShapelessRecipeBuilder.shapeless(BotaniaItems.drySeeds)
				.requires(BotaniaItems.grassSeeds)
				.requires(Items.DEAD_BUSH)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.grassSeeds))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.goldenSeeds)
				.requires(BotaniaItems.grassSeeds)
				.requires(Items.WHEAT)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.grassSeeds))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.vividSeeds)
				.requires(BotaniaItems.grassSeeds)
				.requires(Items.GREEN_DYE)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.grassSeeds))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.scorchedSeeds)
				.requires(BotaniaItems.grassSeeds)
				.requires(Items.BLAZE_POWDER)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.grassSeeds))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.infusedSeeds)
				.requires(BotaniaItems.grassSeeds)
				.requires(Items.PRISMARINE_SHARD)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.grassSeeds))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.mutatedSeeds)
				.requires(BotaniaItems.grassSeeds)
				.requires(Items.SPIDER_EYE)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.grassSeeds))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.darkQuartz, 8)
				.define('Q', Items.QUARTZ)
				.define('C', Ingredient.of(Items.COAL, Items.CHARCOAL))
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.blazeQuartz, 8)
				.define('Q', Items.QUARTZ)
				.define('C', Items.BLAZE_POWDER)
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.lavenderQuartz, 8)
				.define('Q', Items.QUARTZ)
				.define('C', Ingredient.of(Items.ALLIUM, Items.PINK_TULIP, Items.LILAC, Items.PEONY))
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.redQuartz, 8)
				.define('Q', Items.QUARTZ)
				.define('C', Items.REDSTONE)
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.sunnyQuartz, 8)
				.define('Q', Items.QUARTZ)
				.define('C', Items.SUNFLOWER)
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.vineBall)
				.define('V', Items.VINE)
				.pattern("VVV")
				.pattern("VVV")
				.pattern("VVV")
				.unlockedBy("has_item", conditionsFromItem(Items.VINE))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.necroVirus)
				.requires(BotaniaItems.pixieDust)
				.requires(BotaniaItems.vineBall)
				.requires(Items.MAGMA_CREAM)
				.requires(Items.FERMENTED_SPIDER_EYE)
				.requires(Items.ENDER_EYE)
				.requires(Items.ZOMBIE_HEAD)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.ZOMBIE_HEAD))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.nullVirus)
				.requires(BotaniaItems.pixieDust)
				.requires(BotaniaItems.vineBall)
				.requires(Items.MAGMA_CREAM)
				.requires(Items.FERMENTED_SPIDER_EYE)
				.requires(Items.ENDER_EYE)
				.requires(Items.SKELETON_SKULL)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.SKELETON_SKULL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.spark)
				.define('P', ModTags.Items.PETALS)
				.define('B', Items.BLAZE_POWDER)
				.define('N', Items.GOLD_NUGGET)
				.pattern(" P ")
				.pattern("BNB")
				.pattern(" P ")
				.unlockedBy("has_item", conditionsFromItem(Items.BLAZE_POWDER))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.sparkUpgradeDispersive)
				.requires(BotaniaItems.pixieDust)
				.requires(ModTags.Items.INGOTS_MANASTEEL)
				.requires(BotaniaItems.runeWater)
				.group("botania:spark_upgrade")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.spark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.sparkUpgradeDominant)
				.requires(BotaniaItems.pixieDust)
				.requires(ModTags.Items.INGOTS_MANASTEEL)
				.requires(BotaniaItems.runeFire)
				.group("botania:spark_upgrade")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.spark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.sparkUpgradeRecessive)
				.requires(BotaniaItems.pixieDust)
				.requires(ModTags.Items.INGOTS_MANASTEEL)
				.requires(BotaniaItems.runeEarth)
				.group("botania:spark_upgrade")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.spark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.sparkUpgradeIsolated)
				.requires(BotaniaItems.pixieDust)
				.requires(ModTags.Items.INGOTS_MANASTEEL)
				.requires(BotaniaItems.runeAir)
				.group("botania:spark_upgrade")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.spark))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.vial, 3)
				.define('G', BotaniaBlocks.manaGlass)
				.pattern("G G")
				.pattern(" G ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.manaGlass))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaBlocks.brewery))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.flask, 3)
				.define('G', BotaniaBlocks.elfGlass)
				.pattern("G G")
				.pattern(" G ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.elfGlass))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.worldSeed, 4)
				.define('S', Items.WHEAT_SEEDS)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('G', Items.GRASS_BLOCK)
				.pattern("G")
				.pattern("S")
				.pattern("D")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.thornChakram, 2)
				.define('T', ModTags.Items.INGOTS_TERRASTEEL)
				.define('V', Items.VINE)
				.pattern("VVV")
				.pattern("VTV")
				.pattern("VVV")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.flareChakram, 2)
				.define('P', BotaniaItems.pixieDust)
				.define('B', Items.BLAZE_POWDER)
				.define('C', BotaniaItems.thornChakram)
				.pattern("BBB")
				.pattern("CPC")
				.pattern("BBB")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.thornChakram))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.phantomInk, 4)
				.requires(BotaniaItems.manaPearl)
				.requires(Ingredient.of(
						Arrays.stream(DyeColor.values()).map(DyeItem::byColor).toArray(ItemLike[]::new)
				))
				.requires(Ingredient.of(Items.GLASS, Items.WHITE_STAINED_GLASS, Items.ORANGE_STAINED_GLASS,
						Items.MAGENTA_STAINED_GLASS, Items.LIGHT_BLUE_STAINED_GLASS, Items.YELLOW_STAINED_GLASS,
						Items.LIME_STAINED_GLASS, Items.PINK_STAINED_GLASS, Items.GRAY_STAINED_GLASS,
						Items.LIGHT_GRAY_STAINED_GLASS, Items.CYAN_STAINED_GLASS, Items.PURPLE_STAINED_GLASS,
						Items.BLUE_STAINED_GLASS, Items.BROWN_STAINED_GLASS, Items.GREEN_STAINED_GLASS,
						Items.RED_STAINED_GLASS, Items.BLACK_STAINED_GLASS))
				.requires(Items.GLASS_BOTTLE, 4)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaPearl))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.keepIvy)
				.requires(BotaniaItems.pixieDust)
				.requires(Items.VINE)
				.requires(BotaniaItems.enderAirBottle)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.enderAirBottle))
				.save(consumer);

	}

	private void registerMisc(Consumer<FinishedRecipe> consumer) {
		Ingredient mushrooms = Ingredient.of(BotaniaBlocks.whiteMushroom, BotaniaBlocks.orangeMushroom,
				BotaniaBlocks.magentaMushroom, BotaniaBlocks.lightBlueMushroom, BotaniaBlocks.yellowMushroom,
				BotaniaBlocks.limeMushroom, BotaniaBlocks.pinkMushroom, BotaniaBlocks.grayMushroom, BotaniaBlocks.lightGrayMushroom,
				BotaniaBlocks.cyanMushroom, BotaniaBlocks.purpleMushroom, BotaniaBlocks.blueMushroom, BotaniaBlocks.brownMushroom,
				BotaniaBlocks.greenMushroom, BotaniaBlocks.redMushroom, BotaniaBlocks.blackMushroom);
		ShapelessRecipeBuilder.shapeless(Items.MUSHROOM_STEW)
				.requires(mushrooms, 2)
				.requires(Items.BOWL)
				.unlockedBy("has_item", conditionsFromItem(Items.BOWL))
				.unlockedBy("has_orig_recipe", RecipeUnlockedTrigger.unlocked(new ResourceLocation("mushroom_stew")))
				.save(consumer, "botania:mushroom_stew");

		ShapedRecipeBuilder.shaped(Items.COBWEB)
				.define('S', Items.STRING)
				.define('M', BotaniaItems.manaString)
				.pattern("S S")
				.pattern(" M ")
				.pattern("S S")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaString))
				.save(consumer, prefix("cobweb"));

		ShapedRecipeBuilder.shaped(BotaniaBlocks.defaultAltar)
				.define('P', ModTags.Items.PETALS)
				.define('C', Items.COBBLESTONE)
				.pattern("CPC")
				.pattern(" C ")
				.pattern("CCC")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.PETALS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.mossyAltar)
				.define('P', ModTags.Items.PETALS)
				.define('C', Items.MOSSY_COBBLESTONE)
				.pattern("CPC")
				.pattern(" C ")
				.pattern("CCC")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.PETALS))
				.save(consumer);
		for (String metamorphicVariant : LibBlockNames.METAMORPHIC_VARIANTS) {
			Block apothecary = Registry.BLOCK.getOptional(prefix("apothecary_" + metamorphicVariant)).get();
			Block cobble = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + metamorphicVariant + "_cobblestone")).get();
			ShapedRecipeBuilder.shaped(apothecary)
					.define('P', ModTags.Items.PETALS)
					.define('C', cobble)
					.pattern("CPC")
					.pattern(" C ")
					.pattern("CCC")
					.group("botania:metamorphic_apothecary")
					.unlockedBy("has_item", conditionsFromItem(cobble))
					.unlockedBy("has_flower_item", conditionsFromItem(BotaniaFlowerBlocks.marimorphosis))
					.save(consumer);
		}
		for (DyeColor color : DyeColor.values()) {
			ShapelessRecipeBuilder.shapeless(BotaniaBlocks.getShinyFlower(color))
					.requires(Items.GLOWSTONE_DUST)
					.requires(Items.GLOWSTONE_DUST)
					.requires(BotaniaBlocks.getFlower(color))
					.group("botania:shiny_flower")
					.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.getFlower(color)))
					.save(consumer);
			ShapedRecipeBuilder.shaped(BotaniaBlocks.getFloatingFlower(color))
					.define('S', BotaniaItems.grassSeeds)
					.define('D', Items.DIRT)
					.define('F', BotaniaBlocks.getShinyFlower(color))
					.pattern("F")
					.pattern("S")
					.pattern("D")
					.group("botania:floating_flowers")
					.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.getShinyFlower(color)))
					.save(consumer);
			ShapedRecipeBuilder.shaped(BotaniaBlocks.getPetalBlock(color))
					.define('P', BotaniaItems.getPetal(color))
					.pattern("PPP")
					.pattern("PPP")
					.pattern("PPP")
					.group("botania:petal_block")
					.unlockedBy("has_item", conditionsFromItem(BotaniaItems.getPetal(color)))
					.save(consumer);
			ShapelessRecipeBuilder.shapeless(BotaniaBlocks.getMushroom(color))
					.requires(Ingredient.of(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM))
					.requires(DyeItem.byColor(color))
					.group("botania:mushroom")
					.unlockedBy("has_item", conditionsFromItem(Items.RED_MUSHROOM))
					.unlockedBy("has_alt_item", conditionsFromItem(Items.BROWN_MUSHROOM))
					.save(consumer, "botania:mushroom_" + color.ordinal());
			ShapelessRecipeBuilder.shapeless(BotaniaItems.getPetal(color), 4)
					.requires(BotaniaBlocks.getDoubleFlower(color))
					.group("botania:petal_double")
					.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.getDoubleFlower(color)))
					.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.getPetal(color)))
					.save(consumer, "botania:petal_" + color.getName() + "_double");
			ShapelessRecipeBuilder.shapeless(BotaniaItems.getPetal(color), 2)
					.requires(BotaniaBlocks.getFlower(color))
					.group("botania:petal")
					.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.getFlower(color)))
					.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.getPetal(color)))
					.save(consumer, "botania:petal_" + color.getName());
			ShapelessRecipeBuilder.shapeless(DyeItem.byColor(color))
					.requires(Ingredient.of(ModTags.Items.getPetalTag(color)))
					.group("botania:dye")
					.unlockedBy("has_item", conditionsFromItem(BotaniaItems.getPetal(color)))
					.save(consumer, "botania:dye_" + color.getName());
		}
	}

	private void registerTools(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lexicon)
				.requires(ItemTags.SAPLINGS)
				.requires(Items.BOOK)
				.unlockedBy("has_item", conditionsFromTag(ItemTags.SAPLINGS))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.BOOK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.twigWand)
				.define('P', ModTags.Items.PETALS)
				.define('S', BotaniaItems.livingwoodTwig)
				.pattern(" PS")
				.pattern(" SP")
				.pattern("S  ")
				.group("botania:twig_wand")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.PETALS))
				.save(WrapperResult.ofType(WandOfTheForestRecipe.SERIALIZER, consumer));
		ShapedRecipeBuilder.shaped(BotaniaItems.dreamwoodWand)
				.define('P', ModTags.Items.PETALS)
				.define('S', BotaniaItems.dreamwoodTwig)
				.pattern(" PS")
				.pattern(" SP")
				.pattern("S  ")
				.group("botania:twig_wand")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.PETALS))
				.save(WrapperResult.ofType(WandOfTheForestRecipe.SERIALIZER, consumer));
		ShapedRecipeBuilder.shaped(BotaniaItems.manaTablet)
				.define('P', AccessorIngredient.callFromValues(Stream.of(
						new Ingredient.ItemValue(new ItemStack(BotaniaItems.manaPearl)),
						new Ingredient.TagValue(ModTags.Items.GEMS_MANA_DIAMOND))))
				.define('S', BotaniaBlocks.livingrock)
				.pattern("SSS")
				.pattern("SPS")
				.pattern("SSS")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);

		ShapedRecipeBuilder.shaped(BotaniaItems.cacophonium)
				.define('N', Items.NOTE_BLOCK)
				.define('G', Items.COPPER_INGOT)
				.pattern(" G ")
				.pattern("GNG")
				.pattern("GG ")
				.unlockedBy("has_item", conditionsFromItem(Items.NOTE_BLOCK))
				.save(consumer);

		ShapedRecipeBuilder.shaped(BotaniaItems.grassHorn)
				.define('S', BotaniaItems.grassSeeds)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern(" W ")
				.pattern("WSW")
				.pattern("WW ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD_LOGS))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.leavesHorn)
				.requires(BotaniaItems.grassHorn)
				.requires(ItemTags.LEAVES)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.grassHorn))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.snowHorn)
				.requires(BotaniaItems.grassHorn)
				.requires(Items.SNOWBALL)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.grassHorn))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.manaMirror)
				.define('P', BotaniaItems.manaPearl)
				.define('R', BotaniaBlocks.livingrock)
				.define('S', BotaniaItems.livingwoodTwig)
				.define('T', BotaniaItems.manaTablet)
				.define('I', ModTags.Items.INGOTS_TERRASTEEL)
				.pattern(" PR")
				.pattern(" SI")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaTablet))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.openBucket)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("E E")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.spawnerMover)
				.define('A', BotaniaItems.enderAirBottle)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('E', BotaniaItems.lifeEssence)
				.define('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("EIE")
				.pattern("ADA")
				.pattern("EIE")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.slingshot)
				.define('A', BotaniaItems.runeAir)
				.define('T', BotaniaItems.livingwoodTwig)
				.pattern(" TA")
				.pattern(" TT")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.runeAir))
				.save(consumer);

		registerSimpleArmorSet(consumer, Ingredient.of(ModTags.Items.INGOTS_MANASTEEL), "manasteel", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL));
		registerSimpleArmorSet(consumer, Ingredient.of(ModTags.Items.INGOTS_ELEMENTIUM), "elementium", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM));
		registerSimpleArmorSet(consumer, Ingredient.of(BotaniaItems.manaweaveCloth), "manaweave", conditionsFromItem(BotaniaItems.manaweaveCloth));

		registerTerrasteelUpgradeRecipe(consumer, BotaniaItems.terrasteelHelm, BotaniaItems.manasteelHelm, BotaniaItems.runeSpring);
		registerTerrasteelUpgradeRecipe(consumer, BotaniaItems.terrasteelChest, BotaniaItems.manasteelChest, BotaniaItems.runeSummer);
		registerTerrasteelUpgradeRecipe(consumer, BotaniaItems.terrasteelLegs, BotaniaItems.manasteelLegs, BotaniaItems.runeAutumn);
		registerTerrasteelUpgradeRecipe(consumer, BotaniaItems.terrasteelBoots, BotaniaItems.manasteelBoots, BotaniaItems.runeWinter);

		registerToolSetRecipes(consumer, Ingredient.of(ModTags.Items.INGOTS_MANASTEEL), Ingredient.of(BotaniaItems.livingwoodTwig),
				conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL), BotaniaItems.manasteelSword, BotaniaItems.manasteelPick, BotaniaItems.manasteelAxe,
				BotaniaItems.manasteelHoe, BotaniaItems.manasteelShovel, BotaniaItems.manasteelShears);
		registerToolSetRecipes(consumer, Ingredient.of(ModTags.Items.INGOTS_ELEMENTIUM), Ingredient.of(BotaniaItems.dreamwoodTwig),
				conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM), BotaniaItems.elementiumSword, BotaniaItems.elementiumPick, BotaniaItems.elementiumAxe,
				BotaniaItems.elementiumHoe, BotaniaItems.elementiumShovel, BotaniaItems.elementiumShears);

		ShapedRecipeBuilder.shaped(BotaniaItems.terraSword)
				.define('S', BotaniaItems.livingwoodTwig)
				.define('I', ModTags.Items.INGOTS_TERRASTEEL)
				.pattern("I")
				.pattern("I")
				.pattern("S")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.terraPick)
				.define('T', BotaniaItems.manaTablet)
				.define('I', ModTags.Items.INGOTS_TERRASTEEL)
				.define('L', BotaniaItems.livingwoodTwig)
				.pattern("ITI")
				.pattern("ILI")
				.pattern(" L ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(WrapperResult.ofType(ManaUpgradeRecipe.SERIALIZER, consumer));
		ShapedRecipeBuilder.shaped(BotaniaItems.terraAxe)
				.define('S', BotaniaItems.livingwoodTwig)
				.define('T', ModTags.Items.INGOTS_TERRASTEEL)
				.define('G', Items.GLOWSTONE)
				.pattern("TTG")
				.pattern("TST")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.starSword)
				.define('A', BotaniaItems.enderAirBottle)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('T', BotaniaItems.terraSword)
				.define('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("  I")
				.pattern("AD ")
				.pattern("TA ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.terraAxe))
				.unlockedBy("has_terrasteel", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.thunderSword)
				.define('A', BotaniaItems.enderAirBottle)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.define('T', BotaniaItems.terraSword)
				.define('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("  I")
				.pattern("AD ")
				.pattern("TA ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.terraAxe))
				.unlockedBy("has_terrasteel", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.glassPick)
				.define('T', BotaniaItems.livingwoodTwig)
				.define('G', Items.GLASS)
				.define('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("GIG")
				.pattern(" T ")
				.pattern(" T ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.livingwoodBow)
				.define('S', BotaniaItems.manaString)
				.define('T', BotaniaItems.livingwoodTwig)
				.pattern(" TS")
				.pattern("T S")
				.pattern(" TS")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaString))
				.unlockedBy("has_twig", conditionsFromItem(BotaniaItems.livingwoodTwig))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.crystalBow)
				.define('S', BotaniaItems.manaString)
				.define('T', BotaniaItems.livingwoodTwig)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.pattern(" DS")
				.pattern("T S")
				.pattern(" DS")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(BotaniaItems.enderDagger)
				.define('P', BotaniaItems.manaPearl)
				.define('S', ModTags.Items.INGOTS_MANASTEEL)
				.define('T', BotaniaItems.livingwoodTwig)
				.pattern("P")
				.pattern("S")
				.pattern("T")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaPearl))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.enderHand)
				.define('P', BotaniaItems.manaPearl)
				.define('E', Items.ENDER_CHEST)
				.define('L', Items.LEATHER)
				.define('O', Items.OBSIDIAN)
				.pattern("PLO")
				.pattern("LEL")
				.pattern("OL ")
				.unlockedBy("has_item", conditionsFromItem(Items.ENDER_CHEST))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.ENDER_EYE))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(BotaniaItems.placeholder, 32)
				.requires(Items.CRAFTING_TABLE)
				.requires(BotaniaBlocks.livingrock)
				.unlockedBy("has_dreamwood", conditionsFromTag(ModTags.Items.DREAMWOOD_LOGS))
				.unlockedBy("has_crafty_crate", conditionsFromItem(BotaniaBlocks.craftCrate))
				.save(consumer);

		for (CraftyCratePattern pattern : CraftyCratePattern.values()) {
			if (pattern == CraftyCratePattern.NONE) {
				continue;
			}
			Item item = Registry.ITEM.getOptional(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + pattern.getSerializedName().split("_", 2)[1])).get();
			String s = pattern.openSlots.stream().map(bool -> bool ? "R" : "P").collect(Collectors.joining());
			ShapedRecipeBuilder.shaped(item)
					.define('P', BotaniaItems.placeholder)
					.define('R', Items.REDSTONE)
					.pattern(s.substring(0, 3))
					.pattern(s.substring(3, 6))
					.pattern(s.substring(6, 9))
					.group("botania:craft_pattern")
					.unlockedBy("has_item", conditionsFromItem(BotaniaItems.placeholder))
					.unlockedBy("has_crafty_crate", conditionsFromItem(BotaniaBlocks.craftCrate))
					.save(consumer);
		}

		ShapedRecipeBuilder.shaped(BotaniaItems.manaGun)
				.define('S', BotaniaBlocks.redstoneSpreader)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.define('T', Items.TNT)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.define('M', BotaniaItems.runeMana)
				.pattern("SMD")
				.pattern(" WT")
				.pattern("  W")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);

		ShapedRecipeBuilder.shaped(BotaniaItems.dirtRod)
				.define('D', Items.DIRT)
				.define('T', BotaniaItems.livingwoodTwig)
				.define('E', BotaniaItems.runeEarth)
				.pattern("  D")
				.pattern(" T ")
				.pattern("E  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.runeEarth))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.terraformRod)
				.define('A', BotaniaItems.runeAutumn)
				.define('R', BotaniaItems.dirtRod)
				.define('S', BotaniaItems.runeSpring)
				.define('T', ModTags.Items.INGOTS_TERRASTEEL)
				.define('G', BotaniaItems.grassSeeds)
				.define('W', BotaniaItems.runeWinter)
				.define('M', BotaniaItems.runeSummer)
				.pattern(" WT")
				.pattern("ARS")
				.pattern("GM ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);

		ShapedRecipeBuilder.shaped(BotaniaItems.waterRod)
				.define('B', Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)))
				.define('R', BotaniaItems.runeWater)
				.define('T', BotaniaItems.livingwoodTwig)
				.pattern("  B")
				.pattern(" T ")
				.pattern("R  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.runeWater))
				.save(WrapperResult.ofType(WaterBottleMatchingRecipe.SERIALIZER, consumer));

		ShapedRecipeBuilder.shaped(BotaniaItems.rainbowRod)
				.define('P', BotaniaItems.pixieDust)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern(" PD")
				.pattern(" EP")
				.pattern("E  ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.tornadoRod)
				.define('R', BotaniaItems.runeAir)
				.define('T', BotaniaItems.livingwoodTwig)
				.define('F', Items.FEATHER)
				.pattern("  F")
				.pattern(" T ")
				.pattern("R  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.runeAir))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.fireRod)
				.define('R', BotaniaItems.runeFire)
				.define('T', BotaniaItems.livingwoodTwig)
				.define('F', Items.BLAZE_POWDER)
				.pattern("  F")
				.pattern(" T ")
				.pattern("R  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.runeFire))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.skyDirtRod)
				.requires(BotaniaItems.dirtRod)
				.requires(BotaniaItems.pixieDust)
				.requires(BotaniaItems.runeAir)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.pixieDust))
				.save(consumer);

		ShapedRecipeBuilder.shaped(BotaniaItems.diviningRod)
				.define('T', BotaniaItems.livingwoodTwig)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.pattern(" TD")
				.pattern(" TT")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.gravityRod)
				.define('T', BotaniaItems.dreamwoodTwig)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('W', Items.WHEAT)
				.pattern(" TD")
				.pattern(" WT")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(BotaniaItems.missileRod)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('T', BotaniaItems.dreamwoodTwig)
				.define('G', BotaniaItems.lifeEssence)
				.pattern("GDD")
				.pattern(" TD")
				.pattern("T G")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.cobbleRod)
				.define('C', Items.COBBLESTONE)
				.define('T', BotaniaItems.livingwoodTwig)
				.define('F', BotaniaItems.runeFire)
				.define('W', BotaniaItems.runeWater)
				.pattern(" FC")
				.pattern(" TW")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.runeFire))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.runeWater))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.smeltRod)
				.define('B', Items.BLAZE_ROD)
				.define('T', BotaniaItems.livingwoodTwig)
				.define('F', BotaniaItems.runeFire)
				.pattern(" BF")
				.pattern(" TB")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.runeFire))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.exchangeRod)
				.define('R', BotaniaItems.runeSloth)
				.define('S', Items.STONE)
				.define('T', BotaniaItems.livingwoodTwig)
				.pattern(" SR")
				.pattern(" TS")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.runeSloth))
				.save(consumer);

		ShapedRecipeBuilder.shaped(BotaniaItems.laputaShard)
				.define('P', Items.PRISMARINE_CRYSTALS)
				.define('A', BotaniaItems.runeAir)
				.define('S', BotaniaItems.lifeEssence)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('E', BotaniaItems.runeEarth)
				.define('F', ModTags.Items.MUNDANE_FLOATING_FLOWERS)
				.pattern("SFS")
				.pattern("PDP")
				.pattern("ASE")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);

		ShapedRecipeBuilder.shaped(BotaniaItems.craftingHalo)
				.define('P', BotaniaItems.manaPearl)
				.define('C', Items.CRAFTING_TABLE)
				.define('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern(" P ")
				.pattern("ICI")
				.pattern(" I ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaPearl))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.clip)
				.define('D', ModTags.Items.DREAMWOOD_LOGS)
				.pattern(" D ")
				.pattern("D D")
				.pattern("DD ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.DREAMWOOD_LOGS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.spellCloth)
				.define('P', BotaniaItems.manaPearl)
				.define('C', BotaniaItems.manaweaveCloth)
				.pattern(" C ")
				.pattern("CPC")
				.pattern(" C ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaweaveCloth))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.flowerBag)
				.define('P', ModTags.Items.PETALS)
				.define('W', ItemTags.WOOL)
				.pattern("WPW")
				.pattern("W W")
				.pattern(" W ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.MYSTICAL_FLOWERS))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.poolMinecart)
				.requires(Items.MINECART)
				.requires(BotaniaBlocks.manaPool)
				.unlockedBy("has_item", conditionsFromItem(Items.MINECART))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.blackHoleTalisman)
				.define('A', BotaniaItems.enderAirBottle)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('G', BotaniaItems.lifeEssence)
				.pattern(" G ")
				.pattern("EAE")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.temperanceStone)
				.define('R', BotaniaItems.runeEarth)
				.define('S', Items.STONE)
				.pattern(" S ")
				.pattern("SRS")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.runeEarth))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.incenseStick)
				.define('B', Items.BLAZE_POWDER)
				.define('T', BotaniaItems.livingwoodTwig)
				.define('G', Items.GHAST_TEAR)
				.pattern("  G")
				.pattern(" B ")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(Items.GHAST_TEAR))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.obedienceStick)
				.define('T', BotaniaItems.livingwoodTwig)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("  M")
				.pattern(" T ")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.slimeBottle)
				.define('S', Items.SLIME_BALL)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('G', BotaniaBlocks.elfGlass)
				.pattern("EGE")
				.pattern("ESE")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.autocraftingHalo)
				.requires(BotaniaItems.craftingHalo)
				.requires(ModTags.Items.GEMS_MANA_DIAMOND)
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.sextant)
				.define('T', BotaniaItems.livingwoodTwig)
				.define('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern(" TI")
				.pattern(" TT")
				.pattern("III")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.astrolabe)
				.define('D', ModTags.Items.DREAMWOOD_LOGS)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('G', BotaniaItems.lifeEssence)
				.pattern(" EG")
				.pattern("EEE")
				.pattern("GED")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);

	}

	private void registerTrinkets(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(BotaniaItems.tinyPlanet)
				.define('P', BotaniaItems.manaPearl)
				.define('S', Items.STONE)
				.define('L', BotaniaBlocks.livingrock)
				.pattern("LSL")
				.pattern("SPS")
				.pattern("LSL")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaPearl))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.manaRing)
				.define('T', BotaniaItems.manaTablet)
				.define('I', BotaniaItems.manaSteel)
				.pattern("TI ")
				.pattern("I I")
				.pattern(" I ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaTablet))
				.save(WrapperResult.ofType(ManaUpgradeRecipe.SERIALIZER, consumer));
		ShapedRecipeBuilder.shaped(BotaniaItems.auraRing)
				.define('R', BotaniaItems.runeMana)
				.define('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("RI ")
				.pattern("I I")
				.pattern(" I ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.runeMana))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.manaRingGreater)
				.requires(ModTags.Items.INGOTS_TERRASTEEL)
				.requires(BotaniaItems.manaRing)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.terrasteel))
				.save(WrapperResult.ofType(ShapelessManaUpgradeRecipe.SERIALIZER, consumer));
		ShapelessRecipeBuilder.shapeless(BotaniaItems.auraRingGreater)
				.requires(ModTags.Items.INGOTS_TERRASTEEL)
				.requires(BotaniaItems.auraRing)
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.travelBelt)
				.define('A', BotaniaItems.runeAir)
				.define('S', ModTags.Items.INGOTS_MANASTEEL)
				.define('E', BotaniaItems.runeEarth)
				.define('L', Items.LEATHER)
				.pattern("EL ")
				.pattern("L L")
				.pattern("SLA")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.knockbackBelt)
				.define('A', BotaniaItems.runeFire)
				.define('S', ModTags.Items.INGOTS_MANASTEEL)
				.define('E', BotaniaItems.runeEarth)
				.define('L', Items.LEATHER)
				.pattern("AL ")
				.pattern("L L")
				.pattern("SLE")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.icePendant)
				.define('R', BotaniaItems.runeWater)
				.define('S', BotaniaItems.manaString)
				.define('W', BotaniaItems.runeWinter)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("WS ")
				.pattern("S S")
				.pattern("MSR")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.lavaPendant)
				.define('S', BotaniaItems.manaString)
				.define('D', ModTags.Items.INGOTS_MANASTEEL)
				.define('F', BotaniaItems.runeFire)
				.define('M', BotaniaItems.runeSummer)
				.pattern("MS ")
				.pattern("S S")
				.pattern("DSF")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.magnetRing)
				.define('L', BotaniaItems.lensMagnet)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("LM ")
				.pattern("M M")
				.pattern(" M ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.waterRing)
				.define('P', Items.PUFFERFISH)
				.define('C', Items.COD)
				.define('H', Items.HEART_OF_THE_SEA)
				.define('W', BotaniaItems.runeWater)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("WMP")
				.pattern("MHM")
				.pattern("CM ")
				.unlockedBy("has_item", conditionsFromItem(Items.HEART_OF_THE_SEA))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.miningRing)
				.define('P', Items.GOLDEN_PICKAXE)
				.define('E', BotaniaItems.runeEarth)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("EMP")
				.pattern("M M")
				.pattern(" M ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.divaCharm)
				.define('P', BotaniaItems.tinyPlanet)
				.define('G', Items.GOLD_INGOT)
				.define('H', BotaniaItems.runePride)
				.define('L', BotaniaItems.lifeEssence)
				.pattern("LGP")
				.pattern(" HG")
				.pattern(" GL")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.flightTiara)
				.define('E', BotaniaItems.enderAirBottle)
				.define('F', Items.FEATHER)
				.define('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('L', BotaniaItems.lifeEssence)
				.pattern("LLL")
				.pattern("ILI")
				.pattern("FEF")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer, "botania:flighttiara_0");

		// Normal quartz and not Tags.Items.QUARTZ because the recipes conflict.
		Item[] items = { Items.QUARTZ, BotaniaItems.darkQuartz, BotaniaItems.manaQuartz, BotaniaItems.blazeQuartz,
				BotaniaItems.lavenderQuartz, BotaniaItems.redQuartz, BotaniaItems.elfQuartz, BotaniaItems.sunnyQuartz };
		for (int i = 0; i < items.length; i++) {
			int tiaraType = i + 1;
			ShapelessRecipeBuilder.shapeless(BotaniaItems.flightTiara)
					.requires(BotaniaItems.flightTiara)
					.requires(items[i])
					.group("botania:flight_tiara_wings")
					.unlockedBy("has_item", conditionsFromItem(BotaniaItems.flightTiara))
					.save(NbtOutputResult.with(consumer, tag -> tag.putInt("variant", tiaraType)),
							"botania:flighttiara_" + tiaraType);
		}
		ShapedRecipeBuilder.shaped(BotaniaItems.pixieRing)
				.define('D', BotaniaItems.pixieDust)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("DE ")
				.pattern("E E")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.pixieDust))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.superTravelBelt)
				.define('S', BotaniaItems.travelBelt)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('L', BotaniaItems.lifeEssence)
				.pattern("E  ")
				.pattern(" S ")
				.pattern("L E")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.reachRing)
				.define('R', BotaniaItems.runePride)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("RE ")
				.pattern("E E")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.itemFinder)
				.define('E', Items.EMERALD)
				.define('I', Items.IRON_INGOT)
				.define('Y', Items.ENDER_EYE)
				.pattern(" I ")
				.pattern("IYI")
				.pattern("IEI")
				.unlockedBy("has_item", conditionsFromItem(Items.ENDER_EYE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.superLavaPendant)
				.define('P', BotaniaItems.lavaPendant)
				.define('B', Items.BLAZE_ROD)
				.define('G', BotaniaItems.lifeEssence)
				.define('N', Items.NETHER_BRICK)
				.pattern("BBB")
				.pattern("BPB")
				.pattern("NGN")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.bloodPendant)
				.define('P', Items.PRISMARINE_CRYSTALS)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.define('G', Items.GHAST_TEAR)
				.pattern(" P ")
				.pattern("PGP")
				.pattern("DP ")
				.unlockedBy("has_item", conditionsFromItem(Items.GHAST_TEAR))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.holyCloak)
				.define('S', BotaniaItems.lifeEssence)
				.define('W', Items.WHITE_WOOL)
				.define('G', Items.GLOWSTONE_DUST)
				.pattern("WWW")
				.pattern("GWG")
				.pattern("GSG")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.unholyCloak)
				.define('R', Items.REDSTONE)
				.define('S', BotaniaItems.lifeEssence)
				.define('W', Items.BLACK_WOOL)
				.pattern("WWW")
				.pattern("RWR")
				.pattern("RSR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.balanceCloak)
				.define('R', Items.EMERALD)
				.define('S', BotaniaItems.lifeEssence)
				.define('W', Items.LIGHT_GRAY_WOOL)
				.pattern("WWW")
				.pattern("RWR")
				.pattern("RSR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.monocle)
				.define('G', BotaniaBlocks.manaGlass)
				.define('I', ModTags.Items.INGOTS_MANASTEEL)
				.define('N', Items.GOLD_NUGGET)
				.pattern("GN")
				.pattern("IN")
				.pattern(" N")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.swapRing)
				.define('C', Items.CLAY)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("CM ")
				.pattern("M M")
				.pattern(" M ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.magnetRingGreater)
				.requires(ModTags.Items.INGOTS_TERRASTEEL)
				.requires(BotaniaItems.magnetRing)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.magnetRing))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.speedUpBelt)
				.define('P', BotaniaItems.grassSeeds)
				.define('B', BotaniaItems.travelBelt)
				.define('S', Items.SUGAR)
				.define('M', Items.MAP)
				.pattern(" M ")
				.pattern("PBP")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromItem(Items.MAP))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.travelBelt))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.dodgeRing)
				.define('R', BotaniaItems.runeAir)
				.define('E', Items.EMERALD)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("EM ")
				.pattern("M M")
				.pattern(" MR")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.invisibilityCloak)
				.define('P', BotaniaItems.manaPearl)
				.define('C', Items.PRISMARINE_CRYSTALS)
				.define('W', Items.WHITE_WOOL)
				.define('G', BotaniaBlocks.manaGlass)
				.pattern("CWC")
				.pattern("GWG")
				.pattern("GPG")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaPearl))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.cloudPendant)
				.define('S', BotaniaItems.manaString)
				.define('D', ModTags.Items.INGOTS_MANASTEEL)
				.define('F', BotaniaItems.runeAir)
				.define('M', BotaniaItems.runeAutumn)
				.pattern("MS ")
				.pattern("S S")
				.pattern("DSF")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaString))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.superCloudPendant)
				.define('P', BotaniaItems.cloudPendant)
				.define('B', Items.GHAST_TEAR)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('G', BotaniaItems.lifeEssence)
				.define('N', Items.WHITE_WOOL)
				.pattern("BEB")
				.pattern("BPB")
				.pattern("NGN")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.cloudPendant))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.thirdEye)
				.define('Q', Items.QUARTZ_BLOCK)
				.define('R', Items.GOLDEN_CARROT)
				.define('S', BotaniaItems.runeEarth)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.define('E', Items.ENDER_EYE)
				.pattern("RSR")
				.pattern("QEQ")
				.pattern("RDR")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.goddessCharm)
				.define('P', ModTags.Items.PETALS_PINK)
				.define('A', BotaniaItems.runeWater)
				.define('S', BotaniaItems.runeSpring)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.pattern(" P ")
				.pattern(" P ")
				.pattern("ADS")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);

	}

	private void registerCorporeaAndRedString(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(BotaniaItems.redString)
				.requires(Items.STRING)
				.requires(Items.REDSTONE_BLOCK)
				.requires(BotaniaItems.pixieDust)
				.requires(BotaniaItems.enderAirBottle)
				.group("botania:red_string")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.enderAirBottle))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.redString)
				.requires(Items.STRING)
				.requires(Items.REDSTONE_BLOCK)
				.requires(BotaniaItems.pixieDust)
				.requires(BotaniaItems.enderAirBottle)
				.requires(Items.PUMPKIN)
				.group("botania:red_string")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.enderAirBottle))
				.save(consumer, "botania:red_string_alt");
		registerRedStringBlock(consumer, BotaniaBlocks.redStringDispenser, Ingredient.of(Items.DISPENSER), conditionsFromItem(Items.DISPENSER));
		registerRedStringBlock(consumer, BotaniaBlocks.redStringFertilizer, Ingredient.of(BotaniaItems.fertilizer), conditionsFromItem(BotaniaItems.fertilizer));
		registerRedStringBlock(consumer, BotaniaBlocks.redStringComparator, Ingredient.of(Items.COMPARATOR), conditionsFromItem(Items.COMPARATOR));
		registerRedStringBlock(consumer, BotaniaBlocks.redStringRelay, Ingredient.of(BotaniaBlocks.manaSpreader), conditionsFromItem(BotaniaBlocks.manaSpreader));
		registerRedStringBlock(consumer, BotaniaBlocks.redStringInterceptor, Ingredient.of(Items.STONE_BUTTON), conditionsFromItem(Items.STONE_BUTTON));
		ShapelessRecipeBuilder.shapeless(BotaniaItems.corporeaSpark)
				.requires(BotaniaItems.spark)
				.requires(BotaniaItems.pixieDust)
				.requires(BotaniaItems.enderAirBottle)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.enderAirBottle))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.pixieDust))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.corporeaSparkMaster)
				.requires(BotaniaItems.corporeaSpark)
				.requires(ModTags.Items.GEMS_DRAGONSTONE)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.corporeaSpark))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.corporeaIndex)
				.define('A', BotaniaItems.enderAirBottle)
				.define('S', BotaniaItems.corporeaSpark)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('O', Items.OBSIDIAN)
				.pattern("AOA")
				.pattern("OSO")
				.pattern("DOD")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.corporeaSpark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.corporeaFunnel)
				.requires(Items.DROPPER)
				.requires(BotaniaItems.corporeaSpark)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.corporeaSpark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.corporeaInterceptor)
				.requires(Items.REDSTONE_BLOCK)
				.requires(BotaniaItems.corporeaSpark)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.corporeaSpark))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.corporeaCrystalCube)
				.define('C', BotaniaItems.corporeaSpark)
				.define('G', BotaniaBlocks.elfGlass)
				.define('W', ModTags.Items.DREAMWOOD_LOGS)
				.pattern("C")
				.pattern("G")
				.pattern("W")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.corporeaSpark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.corporeaBlock, 8)
				.requires(BotaniaBlocks.livingrockBrick)
				.requires(BotaniaItems.corporeaSpark)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.corporeaSpark))
				.save(consumer);
		slabShape(BotaniaBlocks.corporeaSlab, BotaniaBlocks.corporeaBlock).save(consumer);
		stairs(BotaniaBlocks.corporeaStairs, BotaniaBlocks.corporeaBlock).save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.corporeaBrick, 4)
				.define('R', BotaniaBlocks.corporeaBlock)
				.pattern("RR")
				.pattern("RR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.corporeaBlock))
				.save(consumer);
		slabShape(BotaniaBlocks.corporeaBrickSlab, BotaniaBlocks.corporeaBrick).save(consumer);
		stairs(BotaniaBlocks.corporeaBrickStairs, BotaniaBlocks.corporeaBrick).save(consumer);
		wallShape(BotaniaBlocks.corporeaBrickWall, BotaniaBlocks.corporeaBrick, 6).save(consumer);
	}

	private void registerLenses(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(BotaniaItems.lensNormal)
				.define('S', ModTags.Items.INGOTS_MANASTEEL)
				.define('G', Ingredient.of(Items.GLASS, Items.GLASS_PANE))
				.pattern(" S ")
				.pattern("SGS")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensSpeed)
				.requires(BotaniaItems.lensNormal)
				.requires(BotaniaItems.runeAir)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensPower)
				.requires(BotaniaItems.lensNormal)
				.requires(BotaniaItems.runeFire)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensTime)
				.requires(BotaniaItems.lensNormal)
				.requires(BotaniaItems.runeEarth)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensEfficiency)
				.requires(BotaniaItems.lensNormal)
				.requires(BotaniaItems.runeWater)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensBounce)
				.requires(BotaniaItems.lensNormal)
				.requires(BotaniaItems.runeSummer)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensGravity)
				.requires(BotaniaItems.lensNormal)
				.requires(BotaniaItems.runeWinter)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.lensMine)
				.define('P', Items.PISTON)
				.define('A', Items.LAPIS_LAZULI)
				.define('R', Items.REDSTONE)
				.define('L', BotaniaItems.lensNormal)
				.pattern(" P ")
				.pattern("ALA")
				.pattern(" R ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensDamage)
				.requires(BotaniaItems.lensNormal)
				.requires(BotaniaItems.runeWrath)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensPhantom)
				.requires(BotaniaItems.lensNormal)
				.requires(BotaniaBlocks.abstrusePlatform)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensMagnet)
				.requires(BotaniaItems.lensNormal)
				.requires(Items.IRON_INGOT)
				.requires(Items.GOLD_INGOT)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensExplosive)
				.requires(BotaniaItems.lensNormal)
				.requires(BotaniaItems.runeEnvy)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.lensInfluence)
				.define('P', Items.PRISMARINE_CRYSTALS)
				.define('R', BotaniaItems.runeAir)
				.define('L', BotaniaItems.lensNormal)
				.pattern("PRP")
				.pattern("PLP")
				.pattern("PPP")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.lensWeight)
				.define('P', Items.PRISMARINE_CRYSTALS)
				.define('R', BotaniaItems.runeWater)
				.define('L', BotaniaItems.lensNormal)
				.pattern("PPP")
				.pattern("PLP")
				.pattern("PRP")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.lensPaint)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('W', ItemTags.WOOL)
				.define('L', BotaniaItems.lensNormal)
				.pattern(" E ")
				.pattern("WLW")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensFire)
				.requires(BotaniaItems.lensNormal)
				.requires(Items.FIRE_CHARGE)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensPiston)
				.requires(BotaniaItems.lensNormal)
				.requires(BotaniaBlocks.pistonRelay)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.lensLight)
				.define('F', Items.FIRE_CHARGE)
				.define('G', Items.GLOWSTONE)
				.define('L', BotaniaItems.lensNormal)
				.pattern("GFG")
				.pattern("FLF")
				.pattern("GFG")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaItems.lensLight)
				.define('F', Items.FIRE_CHARGE)
				.define('G', Items.GLOWSTONE)
				.define('L', BotaniaItems.lensNormal)
				.pattern("FGF")
				.pattern("GLG")
				.pattern("FGF")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer, "botania:lens_light_alt");
		ShapedRecipeBuilder.shaped(BotaniaItems.lensMessenger)
				.define('P', Items.PAPER)
				.define('L', BotaniaItems.lensNormal)
				.pattern(" P ")
				.pattern("PLP")
				.pattern(" P ")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.lensNormal))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensWarp)
				.requires(BotaniaItems.lensNormal)
				.requires(BotaniaItems.pixieDust)
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.pixieDust))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensRedirect)
				.requires(BotaniaItems.lensNormal)
				.requires(ModTags.Items.LIVINGWOOD_LOGS)
				.requires(ModTags.Items.INGOTS_ELEMENTIUM)
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensFirework)
				.requires(BotaniaItems.lensNormal)
				.requires(Items.FIREWORK_ROCKET)
				.requires(ModTags.Items.INGOTS_ELEMENTIUM)
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensFlare)
				.requires(BotaniaItems.lensNormal)
				.requires(BotaniaBlocks.elfGlass)
				.requires(ModTags.Items.INGOTS_ELEMENTIUM)
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaItems.lensTripwire)
				.requires(BotaniaItems.lensNormal)
				.requires(Items.TRIPWIRE_HOOK)
				.requires(ModTags.Items.INGOTS_ELEMENTIUM)
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
	}

	private void registerFloatingFlowers(Consumer<FinishedRecipe> consumer) {
		for (Block block : new Block[] {
				BotaniaFlowerBlocks.pureDaisy, BotaniaFlowerBlocks.manastar, BotaniaFlowerBlocks.hydroangeas, BotaniaFlowerBlocks.endoflame,
				BotaniaFlowerBlocks.thermalily, BotaniaFlowerBlocks.rosaArcana, BotaniaFlowerBlocks.munchdew, BotaniaFlowerBlocks.entropinnyum,
				BotaniaFlowerBlocks.kekimurus, BotaniaFlowerBlocks.gourmaryllis, BotaniaFlowerBlocks.narslimmus, BotaniaFlowerBlocks.spectrolus,
				BotaniaFlowerBlocks.dandelifeon, BotaniaFlowerBlocks.rafflowsia, BotaniaFlowerBlocks.shulkMeNot, BotaniaFlowerBlocks.bellethorn,
				BotaniaFlowerBlocks.bellethornChibi, BotaniaFlowerBlocks.bergamute, BotaniaFlowerBlocks.dreadthorn, BotaniaFlowerBlocks.heiseiDream,
				BotaniaFlowerBlocks.tigerseye, BotaniaFlowerBlocks.jadedAmaranthus, BotaniaFlowerBlocks.orechid, BotaniaFlowerBlocks.fallenKanade,
				BotaniaFlowerBlocks.exoflame, BotaniaFlowerBlocks.agricarnation, BotaniaFlowerBlocks.agricarnationChibi, BotaniaFlowerBlocks.hopperhock,
				BotaniaFlowerBlocks.hopperhockChibi, BotaniaFlowerBlocks.tangleberrie, BotaniaFlowerBlocks.tangleberrieChibi,
				BotaniaFlowerBlocks.jiyuulia, BotaniaFlowerBlocks.jiyuuliaChibi, BotaniaFlowerBlocks.rannuncarpus, BotaniaFlowerBlocks.rannuncarpusChibi,
				BotaniaFlowerBlocks.hyacidus, BotaniaFlowerBlocks.pollidisiac, BotaniaFlowerBlocks.clayconia,
				BotaniaFlowerBlocks.clayconiaChibi, BotaniaFlowerBlocks.loonium, BotaniaFlowerBlocks.daffomill, BotaniaFlowerBlocks.vinculotus,
				BotaniaFlowerBlocks.spectranthemum, BotaniaFlowerBlocks.medumone, BotaniaFlowerBlocks.marimorphosis, BotaniaFlowerBlocks.marimorphosisChibi,
				BotaniaFlowerBlocks.bubbell, BotaniaFlowerBlocks.bubbellChibi, BotaniaFlowerBlocks.solegnolia, BotaniaFlowerBlocks.solegnoliaChibi,
				BotaniaFlowerBlocks.orechidIgnem, BotaniaFlowerBlocks.labellia }) {
			createFloatingFlowerRecipe(consumer, block);
		}
	}

	private void registerConversions(Consumer<FinishedRecipe> consumer) {
		compression(BotaniaItems.manaSteel, ModTags.Items.NUGGETS_MANASTEEL)
				.save(consumer, prefix("conversions/manasteel_from_nuggets"));
		compression(BotaniaItems.elementium, ModTags.Items.NUGGETS_ELEMENTIUM)
				.save(consumer, prefix("conversions/elementium_from_nuggets"));
		compression(BotaniaItems.terrasteel, ModTags.Items.NUGGETS_TERRASTEEL)
				.save(consumer, prefix("conversions/terrasteel_from_nugget"));
		compression(BotaniaBlocks.manasteelBlock, ModTags.Items.INGOTS_MANASTEEL).save(consumer);
		compression(BotaniaBlocks.terrasteelBlock, ModTags.Items.INGOTS_TERRASTEEL).save(consumer);
		compression(BotaniaBlocks.elementiumBlock, ModTags.Items.INGOTS_ELEMENTIUM).save(consumer);
		compression(BotaniaBlocks.manaDiamondBlock, ModTags.Items.GEMS_MANA_DIAMOND).save(consumer);
		compression(BotaniaBlocks.dragonstoneBlock, ModTags.Items.GEMS_DRAGONSTONE).save(consumer);

		MutableObject<FinishedRecipe> base = new MutableObject<>();
		MutableObject<FinishedRecipe> gog = new MutableObject<>();
		compression(BotaniaBlocks.blazeBlock, Items.BLAZE_ROD).save(base::setValue);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.blazeBlock)
				.define('I', Items.BLAZE_POWDER)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_item", conditionsFromItem(Items.BLAZE_POWDER))
				.save(gog::setValue);
		consumer.accept(new GogAlternationResult(gog.getValue(), base.getValue()));

		deconstructPetalBlock(consumer, BotaniaItems.whitePetal, BotaniaBlocks.petalBlockWhite);
		deconstructPetalBlock(consumer, BotaniaItems.orangePetal, BotaniaBlocks.petalBlockOrange);
		deconstructPetalBlock(consumer, BotaniaItems.magentaPetal, BotaniaBlocks.petalBlockMagenta);
		deconstructPetalBlock(consumer, BotaniaItems.lightBluePetal, BotaniaBlocks.petalBlockLightBlue);
		deconstructPetalBlock(consumer, BotaniaItems.yellowPetal, BotaniaBlocks.petalBlockYellow);
		deconstructPetalBlock(consumer, BotaniaItems.limePetal, BotaniaBlocks.petalBlockLime);
		deconstructPetalBlock(consumer, BotaniaItems.pinkPetal, BotaniaBlocks.petalBlockPink);
		deconstructPetalBlock(consumer, BotaniaItems.grayPetal, BotaniaBlocks.petalBlockGray);
		deconstructPetalBlock(consumer, BotaniaItems.lightGrayPetal, BotaniaBlocks.petalBlockSilver);
		deconstructPetalBlock(consumer, BotaniaItems.cyanPetal, BotaniaBlocks.petalBlockCyan);
		deconstructPetalBlock(consumer, BotaniaItems.purplePetal, BotaniaBlocks.petalBlockPurple);
		deconstructPetalBlock(consumer, BotaniaItems.bluePetal, BotaniaBlocks.petalBlockBlue);
		deconstructPetalBlock(consumer, BotaniaItems.brownPetal, BotaniaBlocks.petalBlockBrown);
		deconstructPetalBlock(consumer, BotaniaItems.greenPetal, BotaniaBlocks.petalBlockGreen);
		deconstructPetalBlock(consumer, BotaniaItems.redPetal, BotaniaBlocks.petalBlockRed);
		deconstructPetalBlock(consumer, BotaniaItems.blackPetal, BotaniaBlocks.petalBlockBlack);

		deconstruct(base::setValue, Items.BLAZE_ROD, BotaniaBlocks.blazeBlock, "blazeblock_deconstruct");
		deconstruct(gog::setValue, Items.BLAZE_POWDER, BotaniaBlocks.blazeBlock, "blazeblock_deconstruct");
		consumer.accept(new GogAlternationResult(gog.getValue(), base.getValue()));
		deconstruct(consumer, BotaniaItems.manaSteel, ModTags.Items.BLOCKS_MANASTEEL, "manasteel_block_deconstruct");
		deconstruct(consumer, BotaniaItems.manaDiamond, BotaniaBlocks.manaDiamondBlock, "manadiamond_block_deconstruct");
		deconstruct(consumer, BotaniaItems.terrasteel, ModTags.Items.BLOCKS_TERRASTEEL, "terrasteel_block_deconstruct");
		deconstruct(consumer, BotaniaItems.elementium, ModTags.Items.BLOCKS_ELEMENTIUM, "elementium_block_deconstruct");
		deconstruct(consumer, BotaniaItems.dragonstone, BotaniaBlocks.dragonstoneBlock, "dragonstone_block_deconstruct");
		deconstruct(consumer, BotaniaItems.manasteelNugget, ModTags.Items.INGOTS_MANASTEEL, "manasteel_to_nuggets");
		deconstruct(consumer, BotaniaItems.terrasteelNugget, ModTags.Items.INGOTS_TERRASTEEL, "terrasteel_to_nugget");
		deconstruct(consumer, BotaniaItems.elementiumNugget, ModTags.Items.INGOTS_ELEMENTIUM, "elementium_to_nuggets");

		recombineSlab(consumer, BotaniaBlocks.livingrock, BotaniaFluffBlocks.livingrockSlab);
		recombineSlab(consumer, BotaniaBlocks.livingrockBrick, BotaniaFluffBlocks.livingrockBrickSlab);
		recombineSlab(consumer, BotaniaBlocks.livingwood, BotaniaFluffBlocks.livingwoodSlab);
		recombineSlab(consumer, BotaniaBlocks.livingwoodPlanks, BotaniaFluffBlocks.livingwoodPlankSlab);
		recombineSlab(consumer, BotaniaBlocks.dreamwood, BotaniaFluffBlocks.dreamwoodSlab);
		recombineSlab(consumer, BotaniaBlocks.dreamwoodPlanks, BotaniaFluffBlocks.dreamwoodPlankSlab);
		recombineSlab(consumer, BotaniaBlocks.shimmerrock, BotaniaFluffBlocks.shimmerrockSlab);
		recombineSlab(consumer, BotaniaBlocks.shimmerwoodPlanks, BotaniaFluffBlocks.shimmerwoodPlankSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeStoneForest, BotaniaFluffBlocks.biomeStoneForestSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeBrickForest, BotaniaFluffBlocks.biomeBrickForestSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeCobblestoneForest, BotaniaFluffBlocks.biomeCobblestoneForestSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeStonePlains, BotaniaFluffBlocks.biomeStonePlainsSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeBrickPlains, BotaniaFluffBlocks.biomeBrickPlainsSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeCobblestonePlains, BotaniaFluffBlocks.biomeCobblestonePlainsSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeStoneMountain, BotaniaFluffBlocks.biomeStoneMountainSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeBrickMountain, BotaniaFluffBlocks.biomeBrickMountainSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeCobblestoneMountain, BotaniaFluffBlocks.biomeCobblestoneMountainSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeStoneFungal, BotaniaFluffBlocks.biomeStoneFungalSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeBrickFungal, BotaniaFluffBlocks.biomeBrickFungalSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeCobblestoneFungal, BotaniaFluffBlocks.biomeCobblestoneFungalSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeStoneSwamp, BotaniaFluffBlocks.biomeStoneSwampSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeBrickSwamp, BotaniaFluffBlocks.biomeBrickSwampSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeCobblestoneSwamp, BotaniaFluffBlocks.biomeCobblestoneSwampSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeStoneDesert, BotaniaFluffBlocks.biomeStoneDesertSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeBrickDesert, BotaniaFluffBlocks.biomeBrickDesertSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeCobblestoneDesert, BotaniaFluffBlocks.biomeCobblestoneDesertSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeStoneTaiga, BotaniaFluffBlocks.biomeStoneTaigaSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeBrickTaiga, BotaniaFluffBlocks.biomeBrickTaigaSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeCobblestoneTaiga, BotaniaFluffBlocks.biomeCobblestoneTaigaSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeStoneMesa, BotaniaFluffBlocks.biomeStoneMesaSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeBrickMesa, BotaniaFluffBlocks.biomeBrickMesaSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.biomeCobblestoneMesa, BotaniaFluffBlocks.biomeCobblestoneMesaSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.whitePavement, BotaniaFluffBlocks.whitePavementSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.blackPavement, BotaniaFluffBlocks.blackPavementSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.bluePavement, BotaniaFluffBlocks.bluePavementSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.yellowPavement, BotaniaFluffBlocks.yellowPavementSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.redPavement, BotaniaFluffBlocks.redPavementSlab);
		recombineSlab(consumer, BotaniaFluffBlocks.greenPavement, BotaniaFluffBlocks.greenPavementSlab);
	}

	private void registerDecor(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(BotaniaBlocks.livingrockBrick, 4)
				.define('R', BotaniaBlocks.livingrock)
				.pattern("RR")
				.pattern("RR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.livingrock))
				.save(consumer);
		ShapedRecipeBuilder.shaped(BotaniaBlocks.livingrockBrickChiseled, 4)
				.define('R', BotaniaBlocks.livingrockBrick)
				.pattern("RR")
				.pattern("RR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.livingrockBrick))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.livingrockBrickMossy)
				.requires(BotaniaBlocks.livingrockBrick)
				.requires(Items.MOSS_BLOCK)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.livingrockBrick))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.livingrockBrickMossy)
				.requires(BotaniaBlocks.livingrockBrick)
				.requires(Items.VINE)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.livingrockBrick))
				.save(consumer, "botania:mossy_livingrock_bricks_vine");
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.shimmerrock)
				.requires(BotaniaBlocks.livingrock)
				.requires(BotaniaBlocks.bifrostPerm)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.bifrostPerm))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.rainbowRod))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(BotaniaBlocks.shimmerwoodPlanks)
				.requires(BotaniaBlocks.dreamwoodPlanks)
				.requires(BotaniaBlocks.bifrostPerm)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.bifrostPerm))
				.unlockedBy("has_alt_item", conditionsFromItem(BotaniaItems.rainbowRod))
				.save(consumer);

		registerForQuartz(consumer, LibBlockNames.QUARTZ_DARK, BotaniaItems.darkQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_MANA, BotaniaItems.manaQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_BLAZE, BotaniaItems.blazeQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_LAVENDER, BotaniaItems.lavenderQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_RED, BotaniaItems.redQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_ELF, BotaniaItems.elfQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_SUNNY, BotaniaItems.sunnyQuartz);

		registerForWood(consumer, LibBlockNames.LIVING_WOOD);
		registerForWood(consumer, LibBlockNames.DREAM_WOOD);

		stairs(BotaniaFluffBlocks.livingrockStairs, BotaniaBlocks.livingrock).save(consumer);
		slabShape(BotaniaFluffBlocks.livingrockSlab, BotaniaBlocks.livingrock).save(consumer);
		wallShape(BotaniaFluffBlocks.livingrockWall, BotaniaBlocks.livingrock, 6).save(consumer);

		stairs(BotaniaFluffBlocks.livingrockBrickStairs, BotaniaBlocks.livingrockBrick).save(consumer);
		slabShape(BotaniaFluffBlocks.livingrockBrickSlab, BotaniaBlocks.livingrockBrick).save(consumer);
		wallShape(BotaniaFluffBlocks.livingrockBrickWall, BotaniaBlocks.livingrockBrick, 6).save(consumer);

		stairs(BotaniaFluffBlocks.livingrockBrickMossyStairs, BotaniaBlocks.livingrockBrickMossy).save(consumer);
		slabShape(BotaniaFluffBlocks.livingrockBrickMossySlab, BotaniaBlocks.livingrockBrickMossy).save(consumer);
		wallShape(BotaniaFluffBlocks.livingrockBrickMossyWall, BotaniaBlocks.livingrockBrickMossy, 6).save(consumer);

		stairs(BotaniaFluffBlocks.shimmerrockStairs, BotaniaBlocks.shimmerrock).save(consumer);
		slabShape(BotaniaFluffBlocks.shimmerrockSlab, BotaniaBlocks.shimmerrock).save(consumer);
		stairs(BotaniaFluffBlocks.shimmerwoodPlankStairs, BotaniaBlocks.shimmerwoodPlanks).save(consumer);
		slabShape(BotaniaFluffBlocks.shimmerwoodPlankSlab, BotaniaBlocks.shimmerwoodPlanks).save(consumer);

		for (String variant : LibBlockNames.METAMORPHIC_VARIANTS) {
			registerForMetamorphic(consumer, variant);
		}

		Item[] pavementIngredients = { Items.AIR, Items.COAL, Items.LAPIS_LAZULI, Items.REDSTONE, Items.WHEAT, Items.SLIME_BALL };
		for (int i = 0; i < pavementIngredients.length; i++) {
			registerForPavement(consumer, LibBlockNames.PAVEMENT_VARIANTS[i], pavementIngredients[i]);
		}

		wallShape(BotaniaFluffBlocks.managlassPane, BotaniaBlocks.manaGlass, 16).save(consumer);
		wallShape(BotaniaFluffBlocks.alfglassPane, BotaniaBlocks.elfGlass, 16).save(consumer);
		wallShape(BotaniaFluffBlocks.bifrostPane, BotaniaBlocks.bifrostPerm, 16).save(consumer);

		// azulejo0 recipe in loader-specific datagen

		List<Item> allAzulejos = IntStream.range(0, 16).mapToObj(i -> "azulejo_" + i)
				.map(ResourceLocationHelper::prefix)
				.map(Registry.ITEM::getOptional)
				.map(Optional::get)
				.collect(Collectors.toList());
		for (int i = 0; i < allAzulejos.size(); i++) {
			int resultIndex = i + 1 == allAzulejos.size() ? 0 : i + 1;
			String recipeName = "azulejo_" + resultIndex;
			if (resultIndex == 0) {
				recipeName += "_alt";
			}
			ShapelessRecipeBuilder.shapeless(allAzulejos.get(resultIndex))
					.requires(allAzulejos.get(i))
					.unlockedBy("has_azulejo", conditionsFromItem(BotaniaBlocks.azulejo0))
					.group("botania:azulejo_cycling")
					.save(consumer, prefix(recipeName));
		}

		cosmeticBauble(consumer, BotaniaItems.blackBowtie, BotaniaItems.whitePetal);
		cosmeticBauble(consumer, BotaniaItems.blackTie, BotaniaItems.orangePetal);
		cosmeticBauble(consumer, BotaniaItems.redGlasses, BotaniaItems.magentaPetal);
		cosmeticBauble(consumer, BotaniaItems.puffyScarf, BotaniaItems.lightBluePetal);
		cosmeticBauble(consumer, BotaniaItems.engineerGoggles, BotaniaItems.yellowPetal);
		cosmeticBauble(consumer, BotaniaItems.eyepatch, BotaniaItems.limePetal);
		cosmeticBauble(consumer, BotaniaItems.wickedEyepatch, BotaniaItems.pinkPetal);
		cosmeticBauble(consumer, BotaniaItems.redRibbons, BotaniaItems.grayPetal);
		cosmeticBauble(consumer, BotaniaItems.pinkFlowerBud, BotaniaItems.lightGrayPetal);
		cosmeticBauble(consumer, BotaniaItems.polkaDottedBows, BotaniaItems.cyanPetal);
		cosmeticBauble(consumer, BotaniaItems.blueButterfly, BotaniaItems.purplePetal);
		cosmeticBauble(consumer, BotaniaItems.catEars, BotaniaItems.bluePetal);
		cosmeticBauble(consumer, BotaniaItems.witchPin, BotaniaItems.brownPetal);
		cosmeticBauble(consumer, BotaniaItems.devilTail, BotaniaItems.greenPetal);
		cosmeticBauble(consumer, BotaniaItems.kamuiEye, BotaniaItems.redPetal);
		cosmeticBauble(consumer, BotaniaItems.googlyEyes, BotaniaItems.blackPetal);
		cosmeticBauble(consumer, BotaniaItems.fourLeafClover, Items.WHITE_DYE);
		cosmeticBauble(consumer, BotaniaItems.clockEye, Items.ORANGE_DYE);
		cosmeticBauble(consumer, BotaniaItems.unicornHorn, Items.MAGENTA_DYE);
		cosmeticBauble(consumer, BotaniaItems.devilHorns, Items.LIGHT_BLUE_DYE);
		cosmeticBauble(consumer, BotaniaItems.hyperPlus, Items.YELLOW_DYE);
		cosmeticBauble(consumer, BotaniaItems.botanistEmblem, Items.LIME_DYE);
		cosmeticBauble(consumer, BotaniaItems.ancientMask, Items.PINK_DYE);
		cosmeticBauble(consumer, BotaniaItems.eerieMask, Items.GRAY_DYE);
		cosmeticBauble(consumer, BotaniaItems.alienAntenna, Items.LIGHT_GRAY_DYE);
		cosmeticBauble(consumer, BotaniaItems.anaglyphGlasses, Items.CYAN_DYE);
		cosmeticBauble(consumer, BotaniaItems.orangeShades, Items.PURPLE_DYE);
		cosmeticBauble(consumer, BotaniaItems.grouchoGlasses, Items.BLUE_DYE);
		cosmeticBauble(consumer, BotaniaItems.thickEyebrows, Items.BROWN_DYE);
		cosmeticBauble(consumer, BotaniaItems.lusitanicShield, Items.GREEN_DYE);
		cosmeticBauble(consumer, BotaniaItems.tinyPotatoMask, Items.RED_DYE);
		cosmeticBauble(consumer, BotaniaItems.questgiverMark, Items.BLACK_DYE);
		cosmeticBauble(consumer, BotaniaItems.thinkingHand, BotaniaBlocks.tinyPotato);
	}

	protected void registerSimpleArmorSet(Consumer<FinishedRecipe> consumer, Ingredient item, String variant,
			CriterionTriggerInstance criterion) {
		Item helmet = Registry.ITEM.getOptional(prefix(variant + "_helmet")).get();
		Item chestplate = Registry.ITEM.getOptional(prefix(variant + "_chestplate")).get();
		Item leggings = Registry.ITEM.getOptional(prefix(variant + "_leggings")).get();
		Item boots = Registry.ITEM.getOptional(prefix(variant + "_boots")).get();
		ShapedRecipeBuilder.shaped(helmet)
				.define('S', item)
				.pattern("SSS")
				.pattern("S S")
				.unlockedBy("has_item", criterion)
				.save(consumer);
		ShapedRecipeBuilder.shaped(chestplate)
				.define('S', item)
				.pattern("S S")
				.pattern("SSS")
				.pattern("SSS")
				.unlockedBy("has_item", criterion)
				.save(consumer);
		ShapedRecipeBuilder.shaped(leggings)
				.define('S', item)
				.pattern("SSS")
				.pattern("S S")
				.pattern("S S")
				.unlockedBy("has_item", criterion)
				.save(consumer);
		ShapedRecipeBuilder.shaped(boots)
				.define('S', item)
				.pattern("S S")
				.pattern("S S")
				.unlockedBy("has_item", criterion)
				.save(consumer);
	}

	protected void registerToolSetRecipes(Consumer<FinishedRecipe> consumer, Ingredient item, Ingredient stick,
			CriterionTriggerInstance criterion, ItemLike sword, ItemLike pickaxe,
			ItemLike axe, ItemLike hoe, ItemLike shovel, ItemLike shears) {
		ShapedRecipeBuilder.shaped(pickaxe)
				.define('S', item)
				.define('T', stick)
				.pattern("SSS")
				.pattern(" T ")
				.pattern(" T ")
				.unlockedBy("has_item", criterion)
				.save(consumer);
		ShapedRecipeBuilder.shaped(shovel)
				.define('S', item)
				.define('T', stick)
				.pattern("S")
				.pattern("T")
				.pattern("T")
				.unlockedBy("has_item", criterion)
				.save(consumer);
		ShapedRecipeBuilder.shaped(axe)
				.define('S', item)
				.define('T', stick)
				.pattern("SS")
				.pattern("TS")
				.pattern("T ")
				.unlockedBy("has_item", criterion)
				.save(consumer);
		ShapedRecipeBuilder.shaped(hoe)
				.define('S', item)
				.define('T', stick)
				.pattern("SS")
				.pattern(" T")
				.pattern(" T")
				.unlockedBy("has_item", criterion)
				.save(consumer);
		ShapedRecipeBuilder.shaped(sword)
				.define('S', item)
				.define('T', stick)
				.pattern("S")
				.pattern("S")
				.pattern("T")
				.unlockedBy("has_item", criterion)
				.save(consumer);
		ShapedRecipeBuilder.shaped(shears)
				.define('S', item)
				.pattern("S ")
				.pattern(" S")
				.unlockedBy("has_item", criterion)
				.save(consumer);

	}

	protected void registerTerrasteelUpgradeRecipe(Consumer<FinishedRecipe> consumer, ItemLike output,
			ItemLike upgradedInput, ItemLike runeInput) {
		ShapedRecipeBuilder.shaped(output)
				.define('T', BotaniaItems.livingwoodTwig)
				.define('S', ModTags.Items.INGOTS_TERRASTEEL)
				.define('R', runeInput)
				.define('A', upgradedInput)
				.pattern("TRT")
				.pattern("SAS")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.unlockedBy("has_prev_tier", conditionsFromItem(upgradedInput))
				.save(WrapperResult.ofType(ArmorUpgradeRecipe.SERIALIZER, consumer));
	}

	public static void registerRedStringBlock(Consumer<FinishedRecipe> consumer, ItemLike output, Ingredient input, CriterionTriggerInstance criterion) {
		ShapedRecipeBuilder.shaped(output)
				.define('R', BotaniaBlocks.livingrock)
				.define('S', BotaniaItems.redString)
				.define('M', input)
				.pattern("RRR")
				.pattern("RMS")
				.pattern("RRR")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.redString))
				.unlockedBy("has_base_block", criterion)
				.save(consumer);
	}

	protected void createFloatingFlowerRecipe(Consumer<FinishedRecipe> consumer, ItemLike input) {
		ResourceLocation inputName = Registry.ITEM.getKey(input.asItem());
		Item output = Registry.ITEM.getOptional(new ResourceLocation(inputName.getNamespace(), "floating_" + inputName.getPath())).get();
		ShapelessRecipeBuilder.shapeless(output)
				.requires(ModTags.Items.FLOATING_FLOWERS)
				.requires(input)
				.unlockedBy("has_item", conditionsFromItem(input))
				.save(consumer);
	}

	protected void deconstruct(Consumer<FinishedRecipe> consumer, ItemLike output, ItemLike input, String name) {
		ShapelessRecipeBuilder.shapeless(output, 9)
				.unlockedBy("has_item", conditionsFromItem(output))
				.requires(input)
				.save(consumer, prefix("conversions/" + name));
	}

	protected void deconstruct(Consumer<FinishedRecipe> consumer, ItemLike output, TagKey<Item> input, String name) {
		ShapelessRecipeBuilder.shapeless(output, 9)
				.unlockedBy("has_item", conditionsFromItem(output))
				.requires(input)
				.save(consumer, prefix("conversions/" + name));
	}

	protected void deconstructPetalBlock(Consumer<FinishedRecipe> consumer, ItemLike output, ItemLike input) {
		ShapelessRecipeBuilder.shapeless(output, 9)
				.unlockedBy("has_item", conditionsFromItem(output))
				.requires(input).group("botania:petal_block_deconstruct")
				.save(consumer, prefix("conversions/" + Registry.ITEM.getKey(input.asItem()).getPath() + "_deconstruct"));
	}

	protected void recombineSlab(Consumer<FinishedRecipe> consumer, ItemLike fullBlock, ItemLike slab) {
		ShapedRecipeBuilder.shaped(fullBlock)
				.define('Q', slab)
				.pattern("Q")
				.pattern("Q")
				.unlockedBy("has_item", conditionsFromItem(fullBlock))
				.save(consumer, prefix("slab_recombine/" + Registry.ITEM.getKey(fullBlock.asItem()).getPath()));
	}

	protected void registerForQuartz(Consumer<FinishedRecipe> consumer, String variant, ItemLike baseItem) {
		Block base = Registry.BLOCK.getOptional(prefix(variant)).get();
		Block slab = Registry.BLOCK.getOptional(prefix(variant + LibBlockNames.SLAB_SUFFIX)).get();
		Block stairs = Registry.BLOCK.getOptional(prefix(variant + LibBlockNames.STAIR_SUFFIX)).get();
		Block chiseled = Registry.BLOCK.getOptional(prefix("chiseled_" + variant)).get();
		Block pillar = Registry.BLOCK.getOptional(prefix(variant + "_pillar")).get();

		ShapedRecipeBuilder.shaped(base)
				.define('Q', baseItem)
				.pattern("QQ")
				.pattern("QQ")
				.unlockedBy("has_item", conditionsFromItem(baseItem))
				.save(consumer);
		stairs(stairs, base).save(consumer);
		slabShape(slab, base).save(consumer);
		pillar(pillar, base).save(consumer);
		chiseled(chiseled, slab).unlockedBy("has_base_item", conditionsFromItem(base)).save(consumer);
	}

	protected void registerForWood(Consumer<FinishedRecipe> consumer, String variant) {

		TagKey<Item> tag = variant.contains("livingwood") ? ModTags.Items.LIVINGWOOD_LOGS : ModTags.Items.DREAMWOOD_LOGS;
		Block log = Registry.BLOCK.getOptional(prefix(variant + "_log")).orElseThrow();
		Block wood = Registry.BLOCK.getOptional(prefix(variant)).orElseThrow();
		Block strippedLog = Registry.BLOCK.getOptional(prefix("stripped_" + variant + "_log")).orElseThrow();
		Block strippedWood = Registry.BLOCK.getOptional(prefix("stripped_" + variant)).orElseThrow();
		Block glimmeringLog = Registry.BLOCK.getOptional(prefix("glimmering_" + variant + "_log")).orElseThrow();
		Block glimmeringWood = Registry.BLOCK.getOptional(prefix("glimmering_" + variant)).orElseThrow();
		Block glimmeringStrippedLog = Registry.BLOCK.getOptional(prefix("glimmering_stripped_" + variant + "_log")).orElseThrow();
		Block glimmeringStrippedWood = Registry.BLOCK.getOptional(prefix("glimmering_stripped_" + variant)).orElseThrow();
		Block stairs = Registry.BLOCK.getOptional(prefix(variant + "_stairs")).orElseThrow();
		Block slab = Registry.BLOCK.getOptional(prefix(variant + "_slab")).orElseThrow();
		Block wall = Registry.BLOCK.getOptional(prefix(variant + "_wall")).orElseThrow();
		Block strippedStairs = Registry.BLOCK.getOptional(prefix("stripped_" + variant + "_stairs")).orElseThrow();
		Block strippedSlab = Registry.BLOCK.getOptional(prefix("stripped_" + variant + "_slab")).orElseThrow();
		Block strippedWall = Registry.BLOCK.getOptional(prefix("stripped_" + variant + "_wall")).orElseThrow();

		Block planks = Registry.BLOCK.getOptional(prefix(variant + "_planks")).orElseThrow();
		Block planksStairs = Registry.BLOCK.getOptional(prefix(variant + "_planks_stairs")).orElseThrow();
		Block planksSlab = Registry.BLOCK.getOptional(prefix(variant + "_planks_slab")).orElseThrow();
		Block mossyPlanks = Registry.BLOCK.getOptional(prefix("mossy_" + variant + "_planks")).orElseThrow();
		Block framed = Registry.BLOCK.getOptional(prefix("framed_" + variant)).orElseThrow();
		Block patternFramed = Registry.BLOCK.getOptional(prefix("pattern_framed_" + variant)).orElseThrow();
		Block fence = Registry.BLOCK.getOptional(prefix(variant + "_fence")).orElseThrow();
		Block fenceGate = Registry.BLOCK.getOptional(prefix(variant + "_fence_gate")).orElseThrow();

		ShapelessRecipeBuilder.shapeless(planks, 4).requires(tag).group("planks")
				.unlockedBy("has_item", conditionsFromTag(tag)).save(consumer);
		ShapedRecipeBuilder.shaped(wood, 3).group("wood").unlockedBy("has_log", conditionsFromItem(log))
				.define('#', log)
				.pattern("##")
				.pattern("##")
				.save(consumer);
		ShapedRecipeBuilder.shaped(strippedWood, 3).group("wood").unlockedBy("has_log", conditionsFromItem(strippedLog))
				.define('#', strippedLog)
				.pattern("##")
				.pattern("##")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(glimmeringLog).group("botania:glimmering_" + variant)
				.requires(log)
				.requires(Items.GLOWSTONE_DUST)
				.unlockedBy("has_item", conditionsFromItem(log))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(glimmeringWood).group("botania:glimmering_" + variant)
				.requires(wood)
				.requires(Items.GLOWSTONE_DUST)
				.unlockedBy("has_item", conditionsFromItem(wood))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(glimmeringStrippedLog).group("botania:glimmering_" + variant)
				.requires(strippedLog)
				.requires(Items.GLOWSTONE_DUST)
				.unlockedBy("has_item", conditionsFromItem(strippedLog))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(glimmeringStrippedWood).group("botania:glimmering_" + variant)
				.requires(strippedWood)
				.requires(Items.GLOWSTONE_DUST)
				.unlockedBy("has_item", conditionsFromItem(strippedWood))
				.save(consumer);
		ShapedRecipeBuilder.shaped(glimmeringWood, 3).group("botania:glimmering_" + variant)
				.unlockedBy("has_log", conditionsFromItem(glimmeringLog))
				.define('#', glimmeringLog)
				.pattern("##")
				.pattern("##")
				.save(consumer, prefix("glimmering_" + variant + "_from_log"));
		ShapedRecipeBuilder.shaped(glimmeringStrippedWood, 3).group("botania:glimmering_" + variant)
				.unlockedBy("has_log", conditionsFromItem(glimmeringStrippedLog))
				.define('#', glimmeringStrippedLog)
				.pattern("##")
				.pattern("##")
				.save(consumer, prefix("glimmering_stripped_" + variant + "_from_log"));

		stairs(stairs, wood).save(consumer);
		slabShape(slab, wood).save(consumer);
		wallShape(wall, wood, 6).save(consumer);
		fence(fence, planks).save(consumer);
		fenceGate(fenceGate, planks).save(consumer);

		stairs(strippedStairs, strippedWood).save(consumer);
		slabShape(strippedSlab, strippedWood).save(consumer);
		wallShape(strippedWall, strippedWood, 6).save(consumer);

		stairs(planksStairs, planks).save(consumer);
		slabShape(planksSlab, planks).save(consumer);
		ShapelessRecipeBuilder.shapeless(mossyPlanks)
				.requires(planks)
				.requires(Items.MOSS_BLOCK)
				.unlockedBy("has_item", conditionsFromItem(planks))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(mossyPlanks)
				.requires(planks)
				.requires(Items.VINE)
				.unlockedBy("has_item", conditionsFromItem(planks))
				.save(consumer, prefix("mossy_" + variant + "_planks_vine"));
		ShapedRecipeBuilder.shaped(framed, 4)
				.define('W', planks)
				.pattern("W W")
				.pattern("W W")
				.unlockedBy("has_item", conditionsFromItem(planks))
				.save(consumer);
		ringShape(patternFramed, planks).save(consumer);
	}

	private void registerForPavement(Consumer<FinishedRecipe> consumer, String color, Item mainInput) {
		String baseName = color + LibBlockNames.PAVEMENT_SUFFIX;
		Block base = Registry.BLOCK.getOptional(prefix(baseName)).get();
		Block stair = Registry.BLOCK.getOptional(prefix(baseName + LibBlockNames.STAIR_SUFFIX)).get();
		Block slab = Registry.BLOCK.getOptional(prefix(baseName + LibBlockNames.SLAB_SUFFIX)).get();

		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(base, 3)
				.requires(BotaniaBlocks.livingrock)
				.requires(Items.COBBLESTONE)
				.requires(Items.GRAVEL)
				.group("botania:pavement")
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.livingrock));
		if (mainInput != Items.AIR) {
			builder.requires(mainInput);
		}
		builder.save(consumer);

		slabShape(slab, base).group("botania:pavement_slab").save(consumer);
		stairs(stair, base).group("botania:pavement_stairs").save(consumer);
	}

	private void registerForMetamorphic(Consumer<FinishedRecipe> consumer, String variant) {
		Block base = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone")).get();
		Block slab = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.SLAB_SUFFIX)).get();
		Block stair = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.STAIR_SUFFIX)).get();
		Block wall = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.WALL_SUFFIX)).get();
		Block brick = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks")).get();
		Block brickSlab = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.SLAB_SUFFIX)).get();
		Block brickStair = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.STAIR_SUFFIX)).get();
		Block brickWall = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.WALL_SUFFIX)).get();
		Block chiseledBrick = Registry.BLOCK.getOptional(prefix("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks")).get();
		Block cobble = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone")).get();
		Block cobbleSlab = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.SLAB_SUFFIX)).get();
		Block cobbleStair = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.STAIR_SUFFIX)).get();
		Block cobbleWall = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.WALL_SUFFIX)).get();

		InventoryChangeTrigger.TriggerInstance marimorphosis = conditionsFromItem(BotaniaFlowerBlocks.marimorphosis);
		slabShape(slab, base).group("botania:metamorphic_stone_slab")
				.unlockedBy("has_flower_item", marimorphosis).save(consumer);
		stairs(stair, base).group("botania:metamorphic_stone_stairs")
				.unlockedBy("has_flower_item", marimorphosis).save(consumer);
		wallShape(wall, base, 6).group("botania:metamorphic_stone_wall")
				.unlockedBy("has_flower_item", marimorphosis).save(consumer);

		brick(brick, base).group("botania:metamorphic_brick")
				.unlockedBy("has_flower_item", marimorphosis).save(consumer);
		slabShape(brickSlab, brick).group("botania:metamorphic_brick_slab")
				.unlockedBy("has_flower_item", marimorphosis).save(consumer);
		stairs(brickStair, brick).group("botania:metamorphic_brick_stairs")
				.unlockedBy("has_flower_item", marimorphosis).save(consumer);
		wallShape(brickWall, brick, 6).group("botania:metamorphic_brick_wall")
				.unlockedBy("has_flower_item", marimorphosis).save(consumer);
		brick(chiseledBrick, brickSlab).unlockedBy("has_base_item", conditionsFromItem(brick))
				.unlockedBy("has_flower_item", marimorphosis).save(consumer);

		slabShape(cobbleSlab, cobble).group("botania:metamorphic_cobble_slab")
				.unlockedBy("has_flower_item", marimorphosis).save(consumer);
		stairs(cobbleStair, cobble).group("botania:metamorphic_cobble_stairs")
				.unlockedBy("has_flower_item", marimorphosis).save(consumer);
		wallShape(cobbleWall, cobble, 6).group("botania:metamorphic_cobble_wall")
				.unlockedBy("has_flower_item", marimorphosis).save(consumer);
	}

	private ShapedRecipeBuilder compression(ItemLike output, TagKey<Item> input) {
		return ShapedRecipeBuilder.shaped(output)
				.define('I', input)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_item", conditionsFromTag(input));
	}

	protected ShapedRecipeBuilder compression(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(output)
				.define('I', input)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_item", conditionsFromItem(input));
	}

	protected ShapedRecipeBuilder brick(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(output, 4)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('Q', input)
				.pattern("QQ")
				.pattern("QQ");
	}

	protected ShapedRecipeBuilder stairs(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(output, 4)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('Q', input)
				.pattern("  Q")
				.pattern(" QQ")
				.pattern("QQQ");
	}

	protected ShapedRecipeBuilder slabShape(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(output, 6)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('Q', input)
				.pattern("QQQ");
	}

	protected ShapedRecipeBuilder pillar(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(output, 2)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('Q', input)
				.pattern("Q")
				.pattern("Q");
	}

	protected ShapedRecipeBuilder chiseled(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(output)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('Q', input)
				.pattern("Q")
				.pattern("Q");
	}

	protected ShapedRecipeBuilder wallShape(ItemLike output, ItemLike input, int amount) {
		return ShapedRecipeBuilder.shaped(output, amount)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('B', input)
				.pattern("BBB")
				.pattern("BBB");
	}

	protected ShapedRecipeBuilder fence(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(output, 3)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('B', input)
				.define('S', Items.STICK)
				.pattern("BSB")
				.pattern("BSB");
	}

	protected ShapedRecipeBuilder fenceGate(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(output, 3)
				.unlockedBy("has_item", conditionsFromItem(input))
				.define('B', input)
				.define('S', Items.STICK)
				.pattern("SBS")
				.pattern("SBS");
	}

	protected ShapedRecipeBuilder ringShape(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(output, 4)
				.define('W', input)
				.pattern(" W ")
				.pattern("W W")
				.pattern(" W ")
				.unlockedBy("has_item", conditionsFromItem(input));
	}

	protected void cosmeticBauble(Consumer<FinishedRecipe> consumer, ItemLike output, ItemLike input) {
		ShapedRecipeBuilder.shaped(output)
				.define('P', input)
				.define('S', BotaniaItems.manaString)
				.pattern("PPP")
				.pattern("PSP")
				.pattern("PPP")
				.group("botania:cosmetic_bauble")
				.unlockedBy("has_item", conditionsFromItem(BotaniaItems.manaString))
				.save(consumer);
	}

	protected void specialRecipe(Consumer<FinishedRecipe> consumer, SimpleRecipeSerializer<?> serializer) {
		ResourceLocation name = Registry.RECIPE_SERIALIZER.getKey(serializer);
		SpecialRecipeBuilder.special(serializer).save(consumer, prefix("dynamic/" + name.getPath()).toString());
	}

	@Override
	public String getName() {
		return "Botania crafting recipes";
	}
}
