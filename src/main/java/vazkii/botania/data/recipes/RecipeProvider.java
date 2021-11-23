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
import net.minecraft.tags.Tag;
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

import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.crafting.recipe.*;
import vazkii.botania.common.item.ModItems;
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
		specialRecipe(consumer, KeepIvyRecipe.SERIALIZER);
		specialRecipe(consumer, LaputaShardUpgradeRecipe.SERIALIZER);
		specialRecipe(consumer, LensDyeingRecipe.SERIALIZER);
		specialRecipe(consumer, ManaGunClipRecipe.SERIALIZER);
		specialRecipe(consumer, ManaGunLensRecipe.SERIALIZER);
		specialRecipe(consumer, ManaGunRemoveLensRecipe.SERIALIZER);
		specialRecipe(consumer, MergeVialRecipe.SERIALIZER);
		specialRecipe(consumer, PhantomInkRecipe.SERIALIZER);
		specialRecipe(consumer, SpellClothRecipe.SERIALIZER);
		specialRecipe(consumer, SplitLensRecipe.SERIALIZER);
		specialRecipe(consumer, TerraPickTippingRecipe.SERIALIZER);

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

	private static InventoryChangeTrigger.TriggerInstance conditionsFromTag(Tag<Item> tag) {
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
		ShapedRecipeBuilder.shaped(ModBlocks.manaSpreader)
				.define('P', ModTags.Items.PETALS)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.define('G', Items.GOLD_INGOT)
				.pattern("WWW")
				.pattern("GP ")
				.pattern("WWW")
				.group("botania:spreader")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD_LOGS))
				.save(base::setValue);
		ShapedRecipeBuilder.shaped(ModBlocks.manaSpreader)
				.define('P', ModTags.Items.PETALS)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern("WWW")
				.pattern("WP ")
				.pattern("WWW")
				.group("botania:spreader")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD_LOGS))
				.save(gog::setValue);
		consumer.accept(new GogAlternationResult(gog.getValue(), base.getValue()));
		ShapelessRecipeBuilder.shapeless(ModBlocks.redstoneSpreader)
				.requires(ModBlocks.manaSpreader)
				.requires(Items.REDSTONE)
				.group("botania:spreader")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.manaSpreader))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.elvenSpreader)
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
		ShapelessRecipeBuilder.shapeless(ModBlocks.gaiaSpreader)
				.requires(ModBlocks.elvenSpreader)
				.requires(ModTags.Items.GEMS_DRAGONSTONE)
				.requires(ModItems.lifeEssence)
				.group("botania:spreader")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.manaPool)
				.define('R', ModBlocks.livingrock)
				.pattern("R R")
				.pattern("RRR")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.livingrock))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.dilutedPool)
				.define('R', ModFluffBlocks.livingrockSlab)
				.pattern("R R")
				.pattern("RRR")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.livingrock))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.fabulousPool)
				.define('R', ModBlocks.shimmerrock)
				.pattern("R R")
				.pattern("RRR")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.shimmerrock))
				.unlockedBy("has_alt_item", conditionsFromItem(ModItems.rainbowRod))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.fabulousPool)
				.define('P', ModBlocks.manaPool)
				.define('B', ModBlocks.bifrostPerm)
				.pattern("BPB")
				.pattern("BBB")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.bifrostPerm))
				.unlockedBy("has_alt_item", conditionsFromItem(ModItems.rainbowRod))
				.save(consumer, prefix(Registry.ITEM.getKey(ModBlocks.fabulousPool.asItem()).getPath() + "_upgrade"));
		ShapedRecipeBuilder.shaped(ModBlocks.runeAltar)
				.define('P', AccessorIngredient.callFromValues(Stream.of(
						new Ingredient.ItemValue(new ItemStack(ModItems.manaPearl)),
						new Ingredient.TagValue(ModTags.Items.GEMS_MANA_DIAMOND))))
				.define('S', ModBlocks.livingrock)
				.pattern("SSS")
				.pattern("SPS")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaPearl))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.manaPylon)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.define('G', Items.GOLD_INGOT)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern(" G ")
				.pattern("MDM")
				.pattern(" G ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.naturaPylon)
				.define('P', ModBlocks.manaPylon)
				.define('T', ModTags.Items.NUGGETS_TERRASTEEL)
				.define('E', Items.ENDER_EYE)
				.pattern(" T ")
				.pattern("TPT")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.manaPylon))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.gaiaPylon)
				.define('P', ModBlocks.manaPylon)
				.define('D', ModItems.pixieDust)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern(" D ")
				.pattern("EPE")
				.pattern(" D ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.distributor)
				.define('R', ModBlocks.livingrock)
				.define('S', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("RRR")
				.pattern("S S")
				.pattern("RRR")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.manaVoid)
				.define('S', ModBlocks.livingrock)
				.define('O', Items.OBSIDIAN)
				.pattern("SSS")
				.pattern("O O")
				.pattern("SSS")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.livingrock))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.manaDetector)
				.define('R', Items.REDSTONE)
				.define('C', Items.COMPARATOR)
				.define('S', ModBlocks.livingrock)
				.pattern("RSR")
				.pattern("SCS")
				.pattern("RSR")
				.unlockedBy("has_item", conditionsFromItem(Items.COMPARATOR))
				.unlockedBy("has_alt_item", conditionsFromItem(ModBlocks.livingrock))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.turntable)
				.define('P', Items.STICKY_PISTON)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern("WWW")
				.pattern("WPW")
				.pattern("WWW")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD_LOGS))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.STICKY_PISTON))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.tinyPlanet)
				.define('P', ModItems.tinyPlanet)
				.define('S', Items.STONE)
				.pattern("SSS")
				.pattern("SPS")
				.pattern("SSS")
				.unlockedBy("has_item", conditionsFromItem(ModItems.tinyPlanet))
				.unlockedBy("has_alt_item", conditionsFromItem(ModItems.manaPearl))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.alchemyCatalyst)
				.define('P', ModItems.manaPearl)
				.define('B', Items.BREWING_STAND)
				.define('S', ModBlocks.livingrock)
				.define('G', Items.GOLD_INGOT)
				.pattern("SGS")
				.pattern("BPB")
				.pattern("SGS")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaPearl))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.BREWING_STAND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.openCrate)
				.define('W', ModBlocks.livingwoodPlanks)
				.pattern("WWW")
				.pattern("W W")
				.pattern("W W")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.livingwoodPlanks))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.craftCrate)
				.define('C', Items.CRAFTING_TABLE)
				.define('W', ModBlocks.dreamwoodPlanks)
				.pattern("WCW")
				.pattern("W W")
				.pattern("W W")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.dreamwoodPlanks))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.forestEye)
				.define('S', ModBlocks.livingrock)
				.define('E', Items.ENDER_EYE)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("MSM")
				.pattern("SES")
				.pattern("MSM")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.wildDrum)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.define('H', ModItems.grassHorn)
				.define('L', Items.LEATHER)
				.pattern("WLW")
				.pattern("WHW")
				.pattern("WLW")
				.unlockedBy("has_item", conditionsFromItem(ModItems.grassHorn))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.gatheringDrum)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('W', ModTags.Items.DREAMWOOD_LOGS)
				.define('L', Items.LEATHER)
				.pattern("WLW")
				.pattern("WEW")
				.pattern("WLW")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.DREAMWOOD_LOGS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.canopyDrum)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.define('H', ModItems.leavesHorn)
				.define('L', Items.LEATHER)
				.pattern("WLW")
				.pattern("WHW")
				.pattern("WLW")
				.unlockedBy("has_item", conditionsFromItem(ModItems.leavesHorn))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.abstrusePlatform, 2)
				.define('0', ModTags.Items.LIVINGWOOD_LOGS)
				.define('P', ModItems.manaPearl)
				.define('3', ModBlocks.livingwoodFramed)
				.define('4', ModBlocks.livingwoodPatternFramed)
				.pattern("343")
				.pattern("0P0")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaPearl))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.spectralPlatform, 2)
				.define('0', ModTags.Items.DREAMWOOD_LOGS)
				.define('3', ModBlocks.dreamwoodFramed)
				.define('4', ModBlocks.dreamwoodPatternFramed)
				.define('D', ModItems.pixieDust)
				.pattern("343")
				.pattern("0D0")
				.unlockedBy("has_item", conditionsFromItem(ModItems.pixieDust))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.alfPortal)
				.define('T', ModTags.Items.NUGGETS_TERRASTEEL)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern("WTW")
				.pattern("WTW")
				.pattern("WTW")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.conjurationCatalyst)
				.define('P', ModBlocks.alchemyCatalyst)
				.define('B', ModItems.pixieDust)
				.define('S', ModBlocks.livingrock)
				.define('G', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("SBS")
				.pattern("GPG")
				.pattern("SGS")
				.unlockedBy("has_item", conditionsFromItem(ModItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.spawnerClaw)
				.define('P', Items.PRISMARINE_BRICKS)
				.define('B', Items.BLAZE_ROD)
				.define('S', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('E', ModItems.enderAirBottle)
				.define('M', ModTags.Items.BLOCKS_MANASTEEL)
				.pattern("BSB")
				.pattern("PMP")
				.pattern("PEP")
				.unlockedBy("has_item", conditionsFromItem(ModItems.enderAirBottle))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.enderEye)
				.define('R', Items.REDSTONE)
				.define('E', Items.ENDER_EYE)
				.define('O', Items.OBSIDIAN)
				.pattern("RER")
				.pattern("EOE")
				.pattern("RER")
				.unlockedBy("has_item", conditionsFromItem(Items.ENDER_EYE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.starfield)
				.define('P', ModItems.pixieDust)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('O', Items.OBSIDIAN)
				.pattern("EPE")
				.pattern("EOE")
				.unlockedBy("has_item", conditionsFromItem(ModItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.rfGenerator)
				.define('R', Items.REDSTONE_BLOCK)
				.define('S', ModBlocks.livingrock)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("SRS")
				.pattern("RMR")
				.pattern("SRS")
				.unlockedBy("has_item", conditionsFromItem(Items.REDSTONE_BLOCK))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.brewery)
				.define('A', ModItems.runeMana)
				.define('R', ModBlocks.livingrock)
				.define('S', Items.BREWING_STAND)
				.define('M', ModTags.Items.BLOCKS_MANASTEEL)
				.pattern("RSR")
				.pattern("RAR")
				.pattern("RMR")
				.unlockedBy("has_item", conditionsFromItem(ModItems.runeMana))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.BREWING_STAND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.terraPlate)
				.define('0', ModItems.runeWater)
				.define('1', ModItems.runeFire)
				.define('2', ModItems.runeEarth)
				.define('3', ModItems.runeAir)
				.define('8', ModItems.runeMana)
				.define('L', Blocks.LAPIS_BLOCK)
				.define('M', ModTags.Items.BLOCKS_MANASTEEL)
				.pattern("LLL")
				.pattern("0M1")
				.pattern("283")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.RUNES))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.prism)
				.define('P', Items.PRISMARINE_CRYSTALS)
				.define('S', ModBlocks.spectralPlatform)
				.define('G', Items.GLASS)
				.pattern("GPG")
				.pattern("GSG")
				.pattern("GPG")
				.unlockedBy("has_item", conditionsFromItem(Items.PRISMARINE_CRYSTALS))
				.unlockedBy("has_alt_item", conditionsFromItem(ModBlocks.spectralPlatform))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.pump)
				.define('B', Items.BUCKET)
				.define('S', ModBlocks.livingrock)
				.define('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("SSS")
				.pattern("IBI")
				.pattern("SSS")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.incensePlate)
				.define('S', ModFluffBlocks.livingwoodSlab)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern("WSS")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD_LOGS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.hourglass)
				.define('R', Items.REDSTONE)
				.define('S', ModTags.Items.INGOTS_MANASTEEL)
				.define('G', Items.GOLD_INGOT)
				.define('M', ModBlocks.manaGlass)
				.pattern("GMG")
				.pattern("RSR")
				.pattern("GMG")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.manaGlass))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModBlocks.ghostRail)
				.requires(Items.RAIL)
				.requires(ModBlocks.spectralPlatform)
				.unlockedBy("has_item", conditionsFromItem(Items.RAIL))
				.unlockedBy("has_alt_item", conditionsFromItem(ModBlocks.spectralPlatform))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.sparkChanger)
				.define('R', Items.REDSTONE)
				.define('S', ModBlocks.livingrock)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("ESE")
				.pattern("SRS")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.felPumpkin)
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
		ShapedRecipeBuilder.shaped(ModBlocks.cocoon)
				.define('S', Items.STRING)
				.define('C', ModItems.manaweaveCloth)
				.define('P', ModBlocks.felPumpkin)
				.define('D', ModItems.pixieDust)
				.pattern("SSS")
				.pattern("CPC")
				.pattern("SDS")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.felPumpkin))
				.save(base::setValue);
		ShapedRecipeBuilder.shaped(ModBlocks.cocoon)
				.define('S', Items.STRING)
				.define('P', ModBlocks.felPumpkin)
				.define('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("SSS")
				.pattern("SPS")
				.pattern("SIS")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.felPumpkin))
				.save(gog::setValue);
		consumer.accept(new GogAlternationResult(gog.getValue(), base.getValue()));
		ShapelessRecipeBuilder.shapeless(ModBlocks.lightRelayDefault)
				.requires(ModItems.redString)
				.requires(ModTags.Items.GEMS_DRAGONSTONE)
				.requires(Items.GLOWSTONE_DUST)
				.requires(Items.GLOWSTONE_DUST)
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModBlocks.lightRelayDetector)
				.requires(ModBlocks.lightRelayDefault)
				.requires(Items.REDSTONE)
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.lightRelayDefault))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModBlocks.lightRelayFork)
				.requires(ModBlocks.lightRelayDefault)
				.requires(Items.REDSTONE_TORCH)
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.lightRelayDefault))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModBlocks.lightRelayToggle)
				.requires(ModBlocks.lightRelayDefault)
				.requires(Items.LEVER)
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.lightRelayDefault))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.lightLauncher)
				.define('D', ModTags.Items.DREAMWOOD_LOGS)
				.define('L', ModBlocks.lightRelayDefault)
				.pattern("DDD")
				.pattern("DLD")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.lightRelayDefault))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.manaBomb)
				.define('T', Items.TNT)
				.define('G', ModItems.lifeEssence)
				.define('L', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern("LTL")
				.pattern("TGT")
				.pattern("LTL")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.bellows)
				.define('R', ModItems.runeAir)
				.define('S', ModFluffBlocks.livingwoodSlab)
				.define('L', Items.LEATHER)
				.pattern("SSS")
				.pattern("RL ")
				.pattern("SSS")
				.unlockedBy("has_item", conditionsFromItem(ModItems.runeAir))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModBlocks.bifrostPerm)
				.requires(ModItems.rainbowRod)
				.requires(ModBlocks.elfGlass)
				.unlockedBy("has_item", conditionsFromItem(ModItems.rainbowRod))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModBlocks.cellBlock, 3)
				.requires(Items.CACTUS, 3)
				.requires(Items.BEETROOT)
				.requires(Items.CARROT)
				.requires(Items.POTATO)
				.unlockedBy("has_item", conditionsFromItem(ModSubtiles.dandelifeon))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.teruTeruBozu)
				.define('C', ModItems.manaweaveCloth)
				.define('S', Items.SUNFLOWER)
				.pattern("C")
				.pattern("C")
				.pattern("S")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaweaveCloth))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.avatar)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern(" W ")
				.pattern("WDW")
				.pattern("W W")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.animatedTorch)
				.define('D', ModTags.Items.DUSTS_MANA)
				.define('T', Items.REDSTONE_TORCH)
				.pattern("D")
				.pattern("T")
				.unlockedBy("has_item", conditionsFromItem(Items.REDSTONE_TORCH))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.DUSTS_MANA))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.livingwoodTwig)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern("W")
				.pattern("W")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD_LOGS))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.redstoneRoot)
				.requires(Items.REDSTONE)
				.requires(Ingredient.of(Items.FERN, Items.GRASS))
				.unlockedBy("has_item", conditionsFromItem(Items.REDSTONE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.dreamwoodTwig)
				.define('W', ModTags.Items.DREAMWOOD_LOGS)
				.pattern("W")
				.pattern("W")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.DREAMWOOD_LOGS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.gaiaIngot)
				.define('S', ModItems.lifeEssence)
				.define('I', ModTags.Items.INGOTS_TERRASTEEL)
				.pattern(" S ")
				.pattern("SIS")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.manaweaveCloth)
				.define('S', ModItems.manaString)
				.pattern("SS")
				.pattern("SS")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaString))
				.save(consumer);
		Ingredient dyes = Ingredient.of(Items.WHITE_DYE, Items.ORANGE_DYE, Items.MAGENTA_DYE,
				Items.LIGHT_BLUE_DYE, Items.YELLOW_DYE, Items.LIME_DYE, Items.PINK_DYE, Items.GRAY_DYE,
				Items.LIGHT_GRAY_DYE, Items.CYAN_DYE, Items.PURPLE_DYE, Items.BLUE_DYE, Items.BROWN_DYE,
				Items.GREEN_DYE, Items.RED_DYE, Items.BLACK_DYE);
		ShapelessRecipeBuilder.shapeless(ModItems.fertilizer)
				.requires(Items.BONE_MEAL)
				.requires(dyes, 4)
				.unlockedBy("has_item", hasAnyDye)
				.save(base::setValue, "botania:fertilizer_dye");
		ShapelessRecipeBuilder.shapeless(ModItems.fertilizer, 3)
				.requires(Items.BONE_MEAL)
				.requires(dyes, 4)
				.unlockedBy("has_item", hasAnyDye)
				.save(gog::setValue, "botania:fertilizer_dye");
		consumer.accept(new GogAlternationResult(gog.getValue(), base.getValue()));
		ShapelessRecipeBuilder.shapeless(ModItems.drySeeds)
				.requires(ModItems.grassSeeds)
				.requires(Items.DEAD_BUSH)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(ModItems.grassSeeds))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.goldenSeeds)
				.requires(ModItems.grassSeeds)
				.requires(Items.WHEAT)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(ModItems.grassSeeds))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.vividSeeds)
				.requires(ModItems.grassSeeds)
				.requires(Items.GREEN_DYE)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(ModItems.grassSeeds))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.scorchedSeeds)
				.requires(ModItems.grassSeeds)
				.requires(Items.BLAZE_POWDER)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(ModItems.grassSeeds))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.infusedSeeds)
				.requires(ModItems.grassSeeds)
				.requires(Items.PRISMARINE_SHARD)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(ModItems.grassSeeds))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.mutatedSeeds)
				.requires(ModItems.grassSeeds)
				.requires(Items.SPIDER_EYE)
				.group("botania:seeds")
				.unlockedBy("has_item", conditionsFromItem(ModItems.grassSeeds))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.darkQuartz, 8)
				.define('Q', Items.QUARTZ)
				.define('C', Ingredient.of(Items.COAL, Items.CHARCOAL))
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.blazeQuartz, 8)
				.define('Q', Items.QUARTZ)
				.define('C', Items.BLAZE_POWDER)
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.lavenderQuartz, 8)
				.define('Q', Items.QUARTZ)
				.define('C', Ingredient.of(Items.ALLIUM, Items.PINK_TULIP, Items.LILAC, Items.PEONY))
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.redQuartz, 8)
				.define('Q', Items.QUARTZ)
				.define('C', Items.REDSTONE)
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.sunnyQuartz, 8)
				.define('Q', Items.QUARTZ)
				.define('C', Items.SUNFLOWER)
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", conditionsFromItem(Items.QUARTZ))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.vineBall)
				.define('V', Items.VINE)
				.pattern("VVV")
				.pattern("VVV")
				.pattern("VVV")
				.unlockedBy("has_item", conditionsFromItem(Items.VINE))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.necroVirus)
				.requires(ModItems.pixieDust)
				.requires(ModItems.vineBall)
				.requires(Items.MAGMA_CREAM)
				.requires(Items.FERMENTED_SPIDER_EYE)
				.requires(Items.ENDER_EYE)
				.requires(Items.ZOMBIE_HEAD)
				.unlockedBy("has_item", conditionsFromItem(ModItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.ZOMBIE_HEAD))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.nullVirus)
				.requires(ModItems.pixieDust)
				.requires(ModItems.vineBall)
				.requires(Items.MAGMA_CREAM)
				.requires(Items.FERMENTED_SPIDER_EYE)
				.requires(Items.ENDER_EYE)
				.requires(Items.SKELETON_SKULL)
				.unlockedBy("has_item", conditionsFromItem(ModItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.SKELETON_SKULL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.spark)
				.define('P', ModTags.Items.PETALS)
				.define('B', Items.BLAZE_POWDER)
				.define('N', Items.GOLD_NUGGET)
				.pattern(" P ")
				.pattern("BNB")
				.pattern(" P ")
				.unlockedBy("has_item", conditionsFromItem(Items.BLAZE_POWDER))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.sparkUpgradeDispersive)
				.requires(ModItems.pixieDust)
				.requires(ModTags.Items.INGOTS_MANASTEEL)
				.requires(ModItems.runeWater)
				.group("botania:spark_upgrade")
				.unlockedBy("has_item", conditionsFromItem(ModItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromItem(ModItems.spark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.sparkUpgradeDominant)
				.requires(ModItems.pixieDust)
				.requires(ModTags.Items.INGOTS_MANASTEEL)
				.requires(ModItems.runeFire)
				.group("botania:spark_upgrade")
				.unlockedBy("has_item", conditionsFromItem(ModItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromItem(ModItems.spark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.sparkUpgradeRecessive)
				.requires(ModItems.pixieDust)
				.requires(ModTags.Items.INGOTS_MANASTEEL)
				.requires(ModItems.runeEarth)
				.group("botania:spark_upgrade")
				.unlockedBy("has_item", conditionsFromItem(ModItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromItem(ModItems.spark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.sparkUpgradeIsolated)
				.requires(ModItems.pixieDust)
				.requires(ModTags.Items.INGOTS_MANASTEEL)
				.requires(ModItems.runeAir)
				.group("botania:spark_upgrade")
				.unlockedBy("has_item", conditionsFromItem(ModItems.pixieDust))
				.unlockedBy("has_alt_item", conditionsFromItem(ModItems.spark))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.vial, 3)
				.define('G', ModBlocks.manaGlass)
				.pattern("G G")
				.pattern(" G ")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.manaGlass))
				.unlockedBy("has_alt_item", conditionsFromItem(ModBlocks.brewery))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.flask, 3)
				.define('G', ModBlocks.elfGlass)
				.pattern("G G")
				.pattern(" G ")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.elfGlass))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.worldSeed, 4)
				.define('S', Items.WHEAT_SEEDS)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('G', Items.GRASS_BLOCK)
				.pattern("G")
				.pattern("S")
				.pattern("D")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.thornChakram, 2)
				.define('T', ModTags.Items.INGOTS_TERRASTEEL)
				.define('V', Items.VINE)
				.pattern("VVV")
				.pattern("VTV")
				.pattern("VVV")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.flareChakram, 2)
				.define('P', ModItems.pixieDust)
				.define('B', Items.BLAZE_POWDER)
				.define('C', ModItems.thornChakram)
				.pattern("BBB")
				.pattern("CPC")
				.pattern("BBB")
				.unlockedBy("has_item", conditionsFromItem(ModItems.thornChakram))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.phantomInk, 4)
				.requires(ModItems.manaPearl)
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
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaPearl))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.keepIvy)
				.requires(ModItems.pixieDust)
				.requires(Items.VINE)
				.requires(ModItems.enderAirBottle)
				.unlockedBy("has_item", conditionsFromItem(ModItems.enderAirBottle))
				.save(consumer);

	}

	private void registerMisc(Consumer<FinishedRecipe> consumer) {
		Ingredient mushrooms = Ingredient.of(ModBlocks.whiteMushroom, ModBlocks.orangeMushroom,
				ModBlocks.magentaMushroom, ModBlocks.lightBlueMushroom, ModBlocks.yellowMushroom,
				ModBlocks.limeMushroom, ModBlocks.pinkMushroom, ModBlocks.grayMushroom, ModBlocks.lightGrayMushroom,
				ModBlocks.cyanMushroom, ModBlocks.purpleMushroom, ModBlocks.blueMushroom, ModBlocks.brownMushroom,
				ModBlocks.greenMushroom, ModBlocks.redMushroom, ModBlocks.blackMushroom);
		ShapelessRecipeBuilder.shapeless(Items.MUSHROOM_STEW)
				.requires(mushrooms, 2)
				.requires(Items.BOWL)
				.unlockedBy("has_item", conditionsFromItem(Items.BOWL))
				.unlockedBy("has_orig_recipe", RecipeUnlockedTrigger.unlocked(new ResourceLocation("mushroom_stew")))
				.save(consumer, "botania:mushroom_stew");

		ShapedRecipeBuilder.shaped(Items.COBWEB)
				.define('S', Items.STRING)
				.define('M', ModItems.manaString)
				.pattern("S S")
				.pattern(" M ")
				.pattern("S S")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaString))
				.save(consumer, prefix("cobweb"));

		ShapedRecipeBuilder.shaped(ModBlocks.defaultAltar)
				.define('P', ModTags.Items.PETALS)
				.define('S', Items.COBBLESTONE_SLAB)
				.define('C', Items.COBBLESTONE)
				.pattern("SPS")
				.pattern(" C ")
				.pattern("CCC")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.PETALS))
				.save(consumer);
		for (String metamorphicVariant : LibBlockNames.METAMORPHIC_VARIANTS) {
			Block altar = Registry.BLOCK.getOptional(prefix("apothecary_" + metamorphicVariant.replaceAll("_", ""))).get();
			Block cobble = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + metamorphicVariant + "_cobblestone")).get();
			ShapedRecipeBuilder.shaped(altar)
					.define('A', ModBlocks.defaultAltar)
					.define('S', cobble)
					.pattern("SSS")
					.pattern("SAS")
					.pattern("SSS")
					.group("botania:metamorphic_apothecary")
					.unlockedBy("has_item", conditionsFromItem(cobble))
					.unlockedBy("has_flower_item", conditionsFromItem(ModSubtiles.marimorphosis))
					.save(consumer);
		}
		for (DyeColor color : DyeColor.values()) {
			ShapelessRecipeBuilder.shapeless(ModBlocks.getShinyFlower(color))
					.requires(Items.GLOWSTONE_DUST)
					.requires(Items.GLOWSTONE_DUST)
					.requires(ModBlocks.getFlower(color))
					.group("botania:shiny_flower")
					.unlockedBy("has_item", conditionsFromItem(ModBlocks.getFlower(color)))
					.save(consumer);
			ShapedRecipeBuilder.shaped(ModBlocks.getFloatingFlower(color))
					.define('S', ModItems.grassSeeds)
					.define('D', Items.DIRT)
					.define('F', ModBlocks.getShinyFlower(color))
					.pattern("F")
					.pattern("S")
					.pattern("D")
					.group("botania:floating_flowers")
					.unlockedBy("has_item", conditionsFromItem(ModBlocks.getShinyFlower(color)))
					.save(consumer);
			ShapedRecipeBuilder.shaped(ModBlocks.getPetalBlock(color))
					.define('P', ModItems.getPetal(color))
					.pattern("PPP")
					.pattern("PPP")
					.pattern("PPP")
					.group("botania:petal_block")
					.unlockedBy("has_item", conditionsFromItem(ModItems.getPetal(color)))
					.save(consumer);
			ShapelessRecipeBuilder.shapeless(ModBlocks.getMushroom(color))
					.requires(Ingredient.of(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM))
					.requires(DyeItem.byColor(color))
					.group("botania:mushroom")
					.unlockedBy("has_item", conditionsFromItem(Items.RED_MUSHROOM))
					.unlockedBy("has_alt_item", conditionsFromItem(Items.BROWN_MUSHROOM))
					.save(consumer, "botania:mushroom_" + color.ordinal());
			ShapelessRecipeBuilder.shapeless(ModItems.getPetal(color), 4)
					.requires(ModBlocks.getDoubleFlower(color))
					.group("botania:petal_double")
					.unlockedBy("has_item", conditionsFromItem(ModBlocks.getDoubleFlower(color)))
					.unlockedBy("has_alt_item", conditionsFromItem(ModItems.getPetal(color)))
					.save(consumer, "botania:petal_" + color.getName() + "_double");
			ShapelessRecipeBuilder.shapeless(ModItems.getPetal(color), 2)
					.requires(ModBlocks.getFlower(color))
					.group("botania:petal")
					.unlockedBy("has_item", conditionsFromItem(ModBlocks.getFlower(color)))
					.unlockedBy("has_alt_item", conditionsFromItem(ModItems.getPetal(color)))
					.save(consumer, "botania:petal_" + color.getName());
			ShapelessRecipeBuilder.shapeless(DyeItem.byColor(color))
					.requires(Ingredient.of(ModTags.Items.getPetalTag(color)))
					.requires(ModItems.pestleAndMortar)
					.group("botania:dye")
					.unlockedBy("has_item", conditionsFromItem(ModItems.getPetal(color)))
					.save(consumer, "botania:dye_" + color.getName());
		}
	}

	private void registerTools(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(ModItems.lexicon)
				.requires(ItemTags.SAPLINGS)
				.requires(Items.BOOK)
				.unlockedBy("has_item", conditionsFromTag(ItemTags.SAPLINGS))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.BOOK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.twigWand)
				.define('P', ModTags.Items.PETALS)
				.define('S', ModItems.livingwoodTwig)
				.pattern(" PS")
				.pattern(" SP")
				.pattern("S  ")
				.group("botania:twig_wand")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.PETALS))
				.save(WrapperResult.ofType(TwigWandRecipe.SERIALIZER, consumer));
		ShapedRecipeBuilder.shaped(ModItems.manaTablet)
				.define('P', AccessorIngredient.callFromValues(Stream.of(
						new Ingredient.ItemValue(new ItemStack(ModItems.manaPearl)),
						new Ingredient.TagValue(ModTags.Items.GEMS_MANA_DIAMOND))))
				.define('S', ModBlocks.livingrock)
				.pattern("SSS")
				.pattern("SPS")
				.pattern("SSS")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.cacophonium)
				.define('N', Items.NOTE_BLOCK)
				.define('G', Items.COPPER_INGOT)
				.pattern(" G ")
				.pattern("GNG")
				.pattern("GG ")
				.unlockedBy("has_item", conditionsFromItem(Items.NOTE_BLOCK))
				.save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.grassHorn)
				.define('S', ModItems.grassSeeds)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.pattern(" W ")
				.pattern("WSW")
				.pattern("WW ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD_LOGS))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.leavesHorn)
				.requires(ModItems.grassHorn)
				.requires(ItemTags.LEAVES)
				.unlockedBy("has_item", conditionsFromItem(ModItems.grassHorn))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.snowHorn)
				.requires(ModItems.grassHorn)
				.requires(Items.SNOWBALL)
				.unlockedBy("has_item", conditionsFromItem(ModItems.grassHorn))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.manaMirror)
				.define('P', ModItems.manaPearl)
				.define('R', ModBlocks.livingrock)
				.define('S', ModItems.livingwoodTwig)
				.define('T', ModItems.manaTablet)
				.define('I', ModTags.Items.INGOTS_TERRASTEEL)
				.pattern(" PR")
				.pattern(" SI")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaTablet))
				.unlockedBy("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.openBucket)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("E E")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.spawnerMover)
				.define('A', ModItems.enderAirBottle)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('E', ModItems.lifeEssence)
				.define('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("EIE")
				.pattern("ADA")
				.pattern("EIE")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.slingshot)
				.define('A', ModItems.runeAir)
				.define('T', ModItems.livingwoodTwig)
				.pattern(" TA")
				.pattern(" TT")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.runeAir))
				.save(consumer);

		registerSimpleArmorSet(consumer, Ingredient.of(ModTags.Items.INGOTS_MANASTEEL), "manasteel", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL));
		registerSimpleArmorSet(consumer, Ingredient.of(ModTags.Items.INGOTS_ELEMENTIUM), "elementium", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM));
		registerSimpleArmorSet(consumer, Ingredient.of(ModItems.manaweaveCloth), "manaweave", conditionsFromItem(ModItems.manaweaveCloth));

		registerTerrasteelUpgradeRecipe(consumer, ModItems.terrasteelHelm, ModItems.manasteelHelm, ModItems.runeSpring);
		registerTerrasteelUpgradeRecipe(consumer, ModItems.terrasteelChest, ModItems.manasteelChest, ModItems.runeSummer);
		registerTerrasteelUpgradeRecipe(consumer, ModItems.terrasteelLegs, ModItems.manasteelLegs, ModItems.runeAutumn);
		registerTerrasteelUpgradeRecipe(consumer, ModItems.terrasteelBoots, ModItems.manasteelBoots, ModItems.runeWinter);

		registerToolSetRecipes(consumer, Ingredient.of(ModTags.Items.INGOTS_MANASTEEL), Ingredient.of(ModItems.livingwoodTwig),
				conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL), ModItems.manasteelSword, ModItems.manasteelPick, ModItems.manasteelAxe,
				ModItems.manasteelHoe, ModItems.manasteelShovel, ModItems.manasteelShears);
		registerToolSetRecipes(consumer, Ingredient.of(ModTags.Items.INGOTS_ELEMENTIUM), Ingredient.of(ModItems.dreamwoodTwig),
				conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM), ModItems.elementiumSword, ModItems.elementiumPick, ModItems.elementiumAxe,
				ModItems.elementiumHoe, ModItems.elementiumShovel, ModItems.elementiumShears);

		ShapedRecipeBuilder.shaped(ModItems.terraSword)
				.define('S', ModItems.livingwoodTwig)
				.define('I', ModTags.Items.INGOTS_TERRASTEEL)
				.pattern("I")
				.pattern("I")
				.pattern("S")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.terraPick)
				.define('T', ModItems.manaTablet)
				.define('I', ModTags.Items.INGOTS_TERRASTEEL)
				.define('L', ModItems.livingwoodTwig)
				.pattern("ITI")
				.pattern("ILI")
				.pattern(" L ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(WrapperResult.ofType(ManaUpgradeRecipe.SERIALIZER, consumer));
		ShapedRecipeBuilder.shaped(ModItems.terraAxe)
				.define('S', ModItems.livingwoodTwig)
				.define('T', ModTags.Items.INGOTS_TERRASTEEL)
				.define('G', Items.GLOWSTONE)
				.pattern("TTG")
				.pattern("TST")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.starSword)
				.define('A', ModItems.enderAirBottle)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('T', ModItems.terraSword)
				.define('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("  I")
				.pattern("AD ")
				.pattern("TA ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.terraAxe))
				.unlockedBy("has_terrasteel", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.thunderSword)
				.define('A', ModItems.enderAirBottle)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.define('T', ModItems.terraSword)
				.define('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("  I")
				.pattern("AD ")
				.pattern("TA ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.terraAxe))
				.unlockedBy("has_terrasteel", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.glassPick)
				.define('T', ModItems.livingwoodTwig)
				.define('G', Items.GLASS)
				.define('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("GIG")
				.pattern(" T ")
				.pattern(" T ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.livingwoodBow)
				.define('S', ModItems.manaString)
				.define('T', ModItems.livingwoodTwig)
				.pattern(" TS")
				.pattern("T S")
				.pattern(" TS")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaString))
				.unlockedBy("has_twig", conditionsFromItem(ModItems.livingwoodTwig))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.crystalBow)
				.define('S', ModItems.manaString)
				.define('T', ModItems.livingwoodTwig)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.pattern(" DS")
				.pattern("T S")
				.pattern(" DS")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.enderDagger)
				.define('P', ModItems.manaPearl)
				.define('S', ModTags.Items.INGOTS_MANASTEEL)
				.define('T', ModItems.livingwoodTwig)
				.pattern("P")
				.pattern("S")
				.pattern("T")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaPearl))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.enderHand)
				.define('P', ModItems.manaPearl)
				.define('E', Items.ENDER_CHEST)
				.define('L', Items.LEATHER)
				.define('O', Items.OBSIDIAN)
				.pattern("PLO")
				.pattern("LEL")
				.pattern("OL ")
				.unlockedBy("has_item", conditionsFromItem(Items.ENDER_CHEST))
				.unlockedBy("has_alt_item", conditionsFromItem(Items.ENDER_EYE))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(ModItems.placeholder, 32)
				.requires(Items.CRAFTING_TABLE)
				.requires(ModBlocks.livingrock)
				.unlockedBy("has_dreamwood", conditionsFromTag(ModTags.Items.DREAMWOOD_LOGS))
				.unlockedBy("has_crafty_crate", conditionsFromItem(ModBlocks.craftCrate))
				.save(consumer);

		for (CratePattern pattern : CratePattern.values()) {
			if (pattern == CratePattern.NONE) {
				continue;
			}
			Item item = Registry.ITEM.getOptional(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + pattern.getSerializedName().split("_", 2)[1])).get();
			String s = pattern.openSlots.stream().map(bool -> bool ? "R" : "P").collect(Collectors.joining());
			ShapedRecipeBuilder.shaped(item)
					.define('P', ModItems.placeholder)
					.define('R', Items.REDSTONE)
					.pattern(s.substring(0, 3))
					.pattern(s.substring(3, 6))
					.pattern(s.substring(6, 9))
					.group("botania:craft_pattern")
					.unlockedBy("has_item", conditionsFromItem(ModItems.placeholder))
					.unlockedBy("has_crafty_crate", conditionsFromItem(ModBlocks.craftCrate))
					.save(consumer);
		}

		ShapedRecipeBuilder.shaped(ModItems.pestleAndMortar)
				.define('B', Items.BOWL)
				.define('S', Items.STICK)
				.define('W', ItemTags.PLANKS)
				.pattern(" S")
				.pattern("W ")
				.pattern("B ")
				.unlockedBy("has_item", conditionsFromTag(ItemTags.PLANKS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.manaGun)
				.define('S', ModBlocks.redstoneSpreader)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.define('T', Items.TNT)
				.define('W', ModTags.Items.LIVINGWOOD_LOGS)
				.define('M', ModItems.runeMana)
				.pattern("SMD")
				.pattern(" WT")
				.pattern("  W")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.dirtRod)
				.define('D', Items.DIRT)
				.define('T', ModItems.livingwoodTwig)
				.define('E', ModItems.runeEarth)
				.pattern("  D")
				.pattern(" T ")
				.pattern("E  ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.runeEarth))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.terraformRod)
				.define('A', ModItems.runeAutumn)
				.define('R', ModItems.dirtRod)
				.define('S', ModItems.runeSpring)
				.define('T', ModTags.Items.INGOTS_TERRASTEEL)
				.define('G', ModItems.grassSeeds)
				.define('W', ModItems.runeWinter)
				.define('M', ModItems.runeSummer)
				.pattern(" WT")
				.pattern("ARS")
				.pattern("GM ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.waterRod)
				.define('B', Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)))
				.define('R', ModItems.runeWater)
				.define('T', ModItems.livingwoodTwig)
				.pattern("  B")
				.pattern(" T ")
				.pattern("R  ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.runeWater))
				.save(WrapperResult.ofType(WaterBottleMatchingRecipe.SERIALIZER, consumer));

		ShapedRecipeBuilder.shaped(ModItems.rainbowRod)
				.define('P', ModItems.pixieDust)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern(" PD")
				.pattern(" EP")
				.pattern("E  ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.tornadoRod)
				.define('R', ModItems.runeAir)
				.define('T', ModItems.livingwoodTwig)
				.define('F', Items.FEATHER)
				.pattern("  F")
				.pattern(" T ")
				.pattern("R  ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.runeAir))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.fireRod)
				.define('R', ModItems.runeFire)
				.define('T', ModItems.livingwoodTwig)
				.define('F', Items.BLAZE_POWDER)
				.pattern("  F")
				.pattern(" T ")
				.pattern("R  ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.runeFire))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.skyDirtRod)
				.requires(ModItems.dirtRod)
				.requires(ModItems.pixieDust)
				.requires(ModItems.runeAir)
				.unlockedBy("has_item", conditionsFromItem(ModItems.pixieDust))
				.save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.diviningRod)
				.define('T', ModItems.livingwoodTwig)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.pattern(" TD")
				.pattern(" TT")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.gravityRod)
				.define('T', ModItems.dreamwoodTwig)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('W', Items.WHEAT)
				.pattern(" TD")
				.pattern(" WT")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.missileRod)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('T', ModItems.dreamwoodTwig)
				.define('G', ModItems.lifeEssence)
				.pattern("GDD")
				.pattern(" TD")
				.pattern("T G")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.cobbleRod)
				.define('C', Items.COBBLESTONE)
				.define('T', ModItems.livingwoodTwig)
				.define('F', ModItems.runeFire)
				.define('W', ModItems.runeWater)
				.pattern(" FC")
				.pattern(" TW")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.runeFire))
				.unlockedBy("has_alt_item", conditionsFromItem(ModItems.runeWater))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.smeltRod)
				.define('B', Items.BLAZE_ROD)
				.define('T', ModItems.livingwoodTwig)
				.define('F', ModItems.runeFire)
				.pattern(" BF")
				.pattern(" TB")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.runeFire))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.exchangeRod)
				.define('R', ModItems.runeSloth)
				.define('S', Items.STONE)
				.define('T', ModItems.livingwoodTwig)
				.pattern(" SR")
				.pattern(" TS")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.runeSloth))
				.save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.laputaShard)
				.define('P', Items.PRISMARINE_CRYSTALS)
				.define('A', ModItems.runeAir)
				.define('S', ModItems.lifeEssence)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('E', ModItems.runeEarth)
				.define('F', ModTags.Items.MUNDANE_FLOATING_FLOWERS)
				.pattern("SFS")
				.pattern("PDP")
				.pattern("ASE")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.craftingHalo)
				.define('P', ModItems.manaPearl)
				.define('C', Items.CRAFTING_TABLE)
				.define('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern(" P ")
				.pattern("ICI")
				.pattern(" I ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaPearl))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.clip)
				.define('D', ModTags.Items.DREAMWOOD_LOGS)
				.pattern(" D ")
				.pattern("D D")
				.pattern("DD ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.DREAMWOOD_LOGS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.spellCloth)
				.define('P', ModItems.manaPearl)
				.define('C', ModItems.manaweaveCloth)
				.pattern(" C ")
				.pattern("CPC")
				.pattern(" C ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaweaveCloth))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.flowerBag)
				.define('P', ModTags.Items.PETALS)
				.define('W', ItemTags.WOOL)
				.pattern("WPW")
				.pattern("W W")
				.pattern(" W ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.MYSTICAL_FLOWERS))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.poolMinecart)
				.requires(Items.MINECART)
				.requires(ModBlocks.manaPool)
				.unlockedBy("has_item", conditionsFromItem(Items.MINECART))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.blackHoleTalisman)
				.define('A', ModItems.enderAirBottle)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('G', ModItems.lifeEssence)
				.pattern(" G ")
				.pattern("EAE")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.temperanceStone)
				.define('R', ModItems.runeEarth)
				.define('S', Items.STONE)
				.pattern(" S ")
				.pattern("SRS")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.runeEarth))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.incenseStick)
				.define('B', Items.BLAZE_POWDER)
				.define('T', ModItems.livingwoodTwig)
				.define('G', Items.GHAST_TEAR)
				.pattern("  G")
				.pattern(" B ")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromItem(Items.GHAST_TEAR))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.obedienceStick)
				.define('T', ModItems.livingwoodTwig)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("  M")
				.pattern(" T ")
				.pattern("T  ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.slimeBottle)
				.define('S', Items.SLIME_BALL)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('G', ModBlocks.elfGlass)
				.pattern("EGE")
				.pattern("ESE")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.autocraftingHalo)
				.requires(ModItems.craftingHalo)
				.requires(ModTags.Items.GEMS_MANA_DIAMOND)
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.sextant)
				.define('T', ModItems.livingwoodTwig)
				.define('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern(" TI")
				.pattern(" TT")
				.pattern("III")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.baubleBox)
				.define('C', Items.CHEST)
				.define('G', Items.GOLD_INGOT)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern(" M ")
				.pattern("MCG")
				.pattern(" M ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.astrolabe)
				.define('D', ModTags.Items.DREAMWOOD_LOGS)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('G', ModItems.lifeEssence)
				.pattern(" EG")
				.pattern("EEE")
				.pattern("GED")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);

	}

	private void registerTrinkets(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ModItems.tinyPlanet)
				.define('P', ModItems.manaPearl)
				.define('S', Items.STONE)
				.define('L', ModBlocks.livingrock)
				.pattern("LSL")
				.pattern("SPS")
				.pattern("LSL")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaPearl))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.manaRing)
				.define('T', ModItems.manaTablet)
				.define('I', ModItems.manaSteel)
				.pattern("TI ")
				.pattern("I I")
				.pattern(" I ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaTablet))
				.save(WrapperResult.ofType(ManaUpgradeRecipe.SERIALIZER, consumer));
		ShapedRecipeBuilder.shaped(ModItems.auraRing)
				.define('R', ModItems.runeMana)
				.define('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("RI ")
				.pattern("I I")
				.pattern(" I ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.runeMana))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.manaRingGreater)
				.requires(ModTags.Items.INGOTS_TERRASTEEL)
				.requires(ModItems.manaRing)
				.unlockedBy("has_item", conditionsFromItem(ModItems.terrasteel))
				.save(WrapperResult.ofType(ShapelessManaUpgradeRecipe.SERIALIZER, consumer));
		ShapelessRecipeBuilder.shapeless(ModItems.auraRingGreater)
				.requires(ModTags.Items.INGOTS_TERRASTEEL)
				.requires(ModItems.auraRing)
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.travelBelt)
				.define('A', ModItems.runeAir)
				.define('S', ModTags.Items.INGOTS_MANASTEEL)
				.define('E', ModItems.runeEarth)
				.define('L', Items.LEATHER)
				.pattern("EL ")
				.pattern("L L")
				.pattern("SLA")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.knockbackBelt)
				.define('A', ModItems.runeFire)
				.define('S', ModTags.Items.INGOTS_MANASTEEL)
				.define('E', ModItems.runeEarth)
				.define('L', Items.LEATHER)
				.pattern("AL ")
				.pattern("L L")
				.pattern("SLE")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.icePendant)
				.define('R', ModItems.runeWater)
				.define('S', ModItems.manaString)
				.define('W', ModItems.runeWinter)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("WS ")
				.pattern("S S")
				.pattern("MSR")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.lavaPendant)
				.define('S', ModItems.manaString)
				.define('D', ModTags.Items.INGOTS_MANASTEEL)
				.define('F', ModItems.runeFire)
				.define('M', ModItems.runeSummer)
				.pattern("MS ")
				.pattern("S S")
				.pattern("DSF")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.magnetRing)
				.define('L', ModItems.lensMagnet)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("LM ")
				.pattern("M M")
				.pattern(" M ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.waterRing)
				.define('P', Items.PUFFERFISH)
				.define('C', Items.COD)
				.define('H', Items.HEART_OF_THE_SEA)
				.define('W', ModItems.runeWater)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("WMP")
				.pattern("MHM")
				.pattern("CM ")
				.unlockedBy("has_item", conditionsFromItem(Items.HEART_OF_THE_SEA))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.miningRing)
				.define('P', Items.GOLDEN_PICKAXE)
				.define('E', ModItems.runeEarth)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("EMP")
				.pattern("M M")
				.pattern(" M ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.divaCharm)
				.define('P', ModItems.tinyPlanet)
				.define('G', Items.GOLD_INGOT)
				.define('H', ModItems.runePride)
				.define('L', ModItems.lifeEssence)
				.pattern("LGP")
				.pattern(" HG")
				.pattern(" GL")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.flightTiara)
				.define('E', ModItems.enderAirBottle)
				.define('F', Items.FEATHER)
				.define('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('L', ModItems.lifeEssence)
				.pattern("LLL")
				.pattern("ILI")
				.pattern("FEF")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer, "botania:flighttiara_0");

		// Normal quartz and not Tags.Items.QUARTZ because the recipes conflict.
		Item[] items = { Items.QUARTZ, ModItems.darkQuartz, ModItems.manaQuartz, ModItems.blazeQuartz,
				ModItems.lavenderQuartz, ModItems.redQuartz, ModItems.elfQuartz, ModItems.sunnyQuartz };
		for (int i = 0; i < items.length; i++) {
			int tiaraType = i + 1;
			ShapelessRecipeBuilder.shapeless(ModItems.flightTiara)
					.requires(ModItems.flightTiara)
					.requires(items[i])
					.group("botania:flight_tiara_wings")
					.unlockedBy("has_item", conditionsFromItem(ModItems.flightTiara))
					.save(NbtOutputResult.with(consumer, tag -> tag.putInt("variant", tiaraType)),
							"botania:flighttiara_" + tiaraType);
		}
		ShapedRecipeBuilder.shaped(ModItems.pixieRing)
				.define('D', ModItems.pixieDust)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("DE ")
				.pattern("E E")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.pixieDust))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.superTravelBelt)
				.define('S', ModItems.travelBelt)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('L', ModItems.lifeEssence)
				.pattern("E  ")
				.pattern(" S ")
				.pattern("L E")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.reachRing)
				.define('R', ModItems.runePride)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("RE ")
				.pattern("E E")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.itemFinder)
				.define('E', Items.EMERALD)
				.define('I', Items.IRON_INGOT)
				.define('Y', Items.ENDER_EYE)
				.pattern(" I ")
				.pattern("IYI")
				.pattern("IEI")
				.unlockedBy("has_item", conditionsFromItem(Items.ENDER_EYE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.superLavaPendant)
				.define('P', ModItems.lavaPendant)
				.define('B', Items.BLAZE_ROD)
				.define('G', ModItems.lifeEssence)
				.define('N', Items.NETHER_BRICK)
				.pattern("BBB")
				.pattern("BPB")
				.pattern("NGN")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.bloodPendant)
				.define('P', Items.PRISMARINE_CRYSTALS)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.define('G', Items.GHAST_TEAR)
				.pattern(" P ")
				.pattern("PGP")
				.pattern("DP ")
				.unlockedBy("has_item", conditionsFromItem(Items.GHAST_TEAR))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.holyCloak)
				.define('S', ModItems.lifeEssence)
				.define('W', Items.WHITE_WOOL)
				.define('G', Items.GLOWSTONE_DUST)
				.pattern("WWW")
				.pattern("GWG")
				.pattern("GSG")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.unholyCloak)
				.define('R', Items.REDSTONE)
				.define('S', ModItems.lifeEssence)
				.define('W', Items.BLACK_WOOL)
				.pattern("WWW")
				.pattern("RWR")
				.pattern("RSR")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.balanceCloak)
				.define('R', Items.EMERALD)
				.define('S', ModItems.lifeEssence)
				.define('W', Items.LIGHT_GRAY_WOOL)
				.pattern("WWW")
				.pattern("RWR")
				.pattern("RSR")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.monocle)
				.define('G', ModBlocks.manaGlass)
				.define('I', ModTags.Items.INGOTS_MANASTEEL)
				.define('N', Items.GOLD_NUGGET)
				.pattern("GN")
				.pattern("IN")
				.pattern(" N")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.swapRing)
				.define('C', Items.CLAY)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("CM ")
				.pattern("M M")
				.pattern(" M ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.magnetRingGreater)
				.requires(ModTags.Items.INGOTS_TERRASTEEL)
				.requires(ModItems.magnetRing)
				.unlockedBy("has_item", conditionsFromItem(ModItems.magnetRing))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.speedUpBelt)
				.define('P', ModItems.grassSeeds)
				.define('B', ModItems.travelBelt)
				.define('S', Items.SUGAR)
				.define('M', Items.MAP)
				.pattern(" M ")
				.pattern("PBP")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromItem(Items.MAP))
				.unlockedBy("has_alt_item", conditionsFromItem(ModItems.travelBelt))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.dodgeRing)
				.define('R', ModItems.runeAir)
				.define('E', Items.EMERALD)
				.define('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("EM ")
				.pattern("M M")
				.pattern(" MR")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.invisibilityCloak)
				.define('P', ModItems.manaPearl)
				.define('C', Items.PRISMARINE_CRYSTALS)
				.define('W', Items.WHITE_WOOL)
				.define('G', ModBlocks.manaGlass)
				.pattern("CWC")
				.pattern("GWG")
				.pattern("GPG")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaPearl))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.cloudPendant)
				.define('S', ModItems.manaString)
				.define('D', ModTags.Items.INGOTS_MANASTEEL)
				.define('F', ModItems.runeAir)
				.define('M', ModItems.runeAutumn)
				.pattern("MS ")
				.pattern("S S")
				.pattern("DSF")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaString))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.superCloudPendant)
				.define('P', ModItems.cloudPendant)
				.define('B', Items.GHAST_TEAR)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('G', ModItems.lifeEssence)
				.define('N', Items.WHITE_WOOL)
				.pattern("BEB")
				.pattern("BPB")
				.pattern("NGN")
				.unlockedBy("has_item", conditionsFromItem(ModItems.cloudPendant))
				.unlockedBy("has_alt_item", conditionsFromItem(ModItems.lifeEssence))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.thirdEye)
				.define('Q', Items.QUARTZ_BLOCK)
				.define('R', Items.GOLDEN_CARROT)
				.define('S', ModItems.runeEarth)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.define('E', Items.ENDER_EYE)
				.pattern("RSR")
				.pattern("QEQ")
				.pattern("RDR")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.goddessCharm)
				.define('P', ModTags.Items.PETALS_PINK)
				.define('A', ModItems.runeWater)
				.define('S', ModItems.runeSpring)
				.define('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.pattern(" P ")
				.pattern(" P ")
				.pattern("ADS")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.save(consumer);

	}

	private void registerCorporeaAndRedString(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(ModItems.redString)
				.requires(Items.STRING)
				.requires(Items.REDSTONE_BLOCK)
				.requires(ModItems.pixieDust)
				.requires(ModItems.enderAirBottle)
				.group("botania:red_string")
				.unlockedBy("has_item", conditionsFromItem(ModItems.enderAirBottle))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.redString)
				.requires(Items.STRING)
				.requires(Items.REDSTONE_BLOCK)
				.requires(ModItems.pixieDust)
				.requires(ModItems.enderAirBottle)
				.requires(Items.PUMPKIN)
				.group("botania:red_string")
				.unlockedBy("has_item", conditionsFromItem(ModItems.enderAirBottle))
				.save(consumer, "botania:red_string_alt");
		registerRedStringBlock(consumer, ModBlocks.redStringContainer, Ingredient.of(Items.CHEST), conditionsFromItem(Items.CHEST));
		registerRedStringBlock(consumer, ModBlocks.redStringDispenser, Ingredient.of(Items.DISPENSER), conditionsFromItem(Items.DISPENSER));
		registerRedStringBlock(consumer, ModBlocks.redStringFertilizer, Ingredient.of(ModItems.fertilizer), conditionsFromItem(ModItems.fertilizer));
		registerRedStringBlock(consumer, ModBlocks.redStringComparator, Ingredient.of(Items.COMPARATOR), conditionsFromItem(Items.COMPARATOR));
		registerRedStringBlock(consumer, ModBlocks.redStringRelay, Ingredient.of(ModBlocks.manaSpreader), conditionsFromItem(ModBlocks.manaSpreader));
		registerRedStringBlock(consumer, ModBlocks.redStringInterceptor, Ingredient.of(Items.STONE_BUTTON), conditionsFromItem(Items.STONE_BUTTON));
		ShapelessRecipeBuilder.shapeless(ModItems.corporeaSpark)
				.requires(ModItems.spark)
				.requires(ModItems.pixieDust)
				.requires(ModItems.enderAirBottle)
				.unlockedBy("has_item", conditionsFromItem(ModItems.enderAirBottle))
				.unlockedBy("has_alt_item", conditionsFromItem(ModItems.pixieDust))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.corporeaSparkMaster)
				.requires(ModItems.corporeaSpark)
				.requires(ModTags.Items.GEMS_DRAGONSTONE)
				.unlockedBy("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.corporeaIndex)
				.define('A', ModItems.enderAirBottle)
				.define('S', ModItems.corporeaSpark)
				.define('D', ModTags.Items.GEMS_DRAGONSTONE)
				.define('O', Items.OBSIDIAN)
				.pattern("AOA")
				.pattern("OSO")
				.pattern("DOD")
				.unlockedBy("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModBlocks.corporeaFunnel)
				.requires(Items.DROPPER)
				.requires(ModItems.corporeaSpark)
				.unlockedBy("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModBlocks.corporeaInterceptor)
				.requires(Items.REDSTONE_BLOCK)
				.requires(ModItems.corporeaSpark)
				.unlockedBy("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.corporeaCrystalCube)
				.define('C', ModItems.corporeaSpark)
				.define('G', ModBlocks.elfGlass)
				.define('W', ModTags.Items.DREAMWOOD_LOGS)
				.pattern("C")
				.pattern("G")
				.pattern("W")
				.unlockedBy("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModBlocks.corporeaRetainer)
				.requires(Items.CHEST)
				.requires(ModItems.corporeaSpark)
				.unlockedBy("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModBlocks.corporeaBlock, 8)
				.requires(ModBlocks.livingrockBrick)
				.requires(ModItems.corporeaSpark)
				.unlockedBy("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.save(consumer);
		slabShape(ModBlocks.corporeaSlab, ModBlocks.corporeaBlock).save(consumer);
		stairs(ModBlocks.corporeaStairs, ModBlocks.corporeaBlock).save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.corporeaBrick, 4)
				.define('R', ModBlocks.corporeaBlock)
				.pattern("RR")
				.pattern("RR")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.corporeaBlock))
				.save(consumer);
		slabShape(ModBlocks.corporeaBrickSlab, ModBlocks.corporeaBrick).save(consumer);
		stairs(ModBlocks.corporeaBrickStairs, ModBlocks.corporeaBrick).save(consumer);
		wallShape(ModBlocks.corporeaBrickWall, ModBlocks.corporeaBrick, 6).save(consumer);
	}

	private void registerLenses(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ModItems.lensNormal)
				.define('S', ModTags.Items.INGOTS_MANASTEEL)
				.define('G', Ingredient.of(Items.GLASS, Items.GLASS_PANE))
				.pattern(" S ")
				.pattern("SGS")
				.pattern(" S ")
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensSpeed)
				.requires(ModItems.lensNormal)
				.requires(ModItems.runeAir)
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensPower)
				.requires(ModItems.lensNormal)
				.requires(ModItems.runeFire)
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensTime)
				.requires(ModItems.lensNormal)
				.requires(ModItems.runeEarth)
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensEfficiency)
				.requires(ModItems.lensNormal)
				.requires(ModItems.runeWater)
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensBounce)
				.requires(ModItems.lensNormal)
				.requires(ModItems.runeSummer)
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensGravity)
				.requires(ModItems.lensNormal)
				.requires(ModItems.runeWinter)
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.lensMine)
				.define('P', Items.PISTON)
				.define('A', Items.LAPIS_LAZULI)
				.define('R', Items.REDSTONE)
				.define('L', ModItems.lensNormal)
				.pattern(" P ")
				.pattern("ALA")
				.pattern(" R ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensDamage)
				.requires(ModItems.lensNormal)
				.requires(ModItems.runeWrath)
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensPhantom)
				.requires(ModItems.lensNormal)
				.requires(ModBlocks.abstrusePlatform)
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensMagnet)
				.requires(ModItems.lensNormal)
				.requires(Items.IRON_INGOT)
				.requires(Items.GOLD_INGOT)
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensExplosive)
				.requires(ModItems.lensNormal)
				.requires(ModItems.runeEnvy)
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.lensInfluence)
				.define('P', Items.PRISMARINE_CRYSTALS)
				.define('R', ModItems.runeAir)
				.define('L', ModItems.lensNormal)
				.pattern("PRP")
				.pattern("PLP")
				.pattern("PPP")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.lensWeight)
				.define('P', Items.PRISMARINE_CRYSTALS)
				.define('R', ModItems.runeWater)
				.define('L', ModItems.lensNormal)
				.pattern("PPP")
				.pattern("PLP")
				.pattern("PRP")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.lensPaint)
				.define('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.define('W', ItemTags.WOOL)
				.define('L', ModItems.lensNormal)
				.pattern(" E ")
				.pattern("WLW")
				.pattern(" E ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensFire)
				.requires(ModItems.lensNormal)
				.requires(Items.FIRE_CHARGE)
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensPiston)
				.requires(ModItems.lensNormal)
				.requires(ModBlocks.pistonRelay)
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.lensLight)
				.define('F', Items.FIRE_CHARGE)
				.define('G', Items.GLOWSTONE)
				.define('L', ModItems.lensNormal)
				.pattern("GFG")
				.pattern("FLF")
				.pattern("GFG")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.lensLight)
				.define('F', Items.FIRE_CHARGE)
				.define('G', Items.GLOWSTONE)
				.define('L', ModItems.lensNormal)
				.pattern("FGF")
				.pattern("GLG")
				.pattern("FGF")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer, "botania:lens_light_alt");
		ShapedRecipeBuilder.shaped(ModItems.lensMessenger)
				.define('P', Items.PAPER)
				.define('L', ModItems.lensNormal)
				.pattern(" P ")
				.pattern("PLP")
				.pattern(" P ")
				.unlockedBy("has_item", conditionsFromItem(ModItems.lensNormal))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(ModItems.lensWarp)
				.requires(ModItems.lensNormal)
				.requires(ModItems.pixieDust)
				.unlockedBy("has_item", conditionsFromItem(ModItems.pixieDust))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensRedirect)
				.requires(ModItems.lensNormal)
				.requires(ModTags.Items.LIVINGWOOD_LOGS)
				.requires(ModTags.Items.INGOTS_ELEMENTIUM)
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensFirework)
				.requires(ModItems.lensNormal)
				.requires(Items.FIREWORK_ROCKET)
				.requires(ModTags.Items.INGOTS_ELEMENTIUM)
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensFlare)
				.requires(ModItems.lensNormal)
				.requires(ModBlocks.elfGlass)
				.requires(ModTags.Items.INGOTS_ELEMENTIUM)
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.lensTripwire)
				.requires(ModItems.lensNormal)
				.requires(Items.TRIPWIRE_HOOK)
				.requires(ModTags.Items.INGOTS_ELEMENTIUM)
				.unlockedBy("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.save(consumer);
	}

	private void registerFloatingFlowers(Consumer<FinishedRecipe> consumer) {
		for (Block block : new Block[] {
				ModSubtiles.pureDaisy, ModSubtiles.manastar, ModSubtiles.hydroangeas, ModSubtiles.endoflame,
				ModSubtiles.thermalily, ModSubtiles.rosaArcana, ModSubtiles.munchdew, ModSubtiles.entropinnyum,
				ModSubtiles.kekimurus, ModSubtiles.gourmaryllis, ModSubtiles.narslimmus, ModSubtiles.spectrolus,
				ModSubtiles.dandelifeon, ModSubtiles.rafflowsia, ModSubtiles.shulkMeNot, ModSubtiles.bellethorn,
				ModSubtiles.bellethornChibi, ModSubtiles.bergamute, ModSubtiles.dreadthorn, ModSubtiles.heiseiDream,
				ModSubtiles.tigerseye, ModSubtiles.jadedAmaranthus, ModSubtiles.orechid, ModSubtiles.fallenKanade,
				ModSubtiles.exoflame, ModSubtiles.agricarnation, ModSubtiles.agricarnationChibi, ModSubtiles.hopperhock,
				ModSubtiles.hopperhockChibi, ModSubtiles.tangleberrie, ModSubtiles.tangleberrieChibi,
				ModSubtiles.jiyuulia, ModSubtiles.jiyuuliaChibi, ModSubtiles.rannuncarpus, ModSubtiles.rannuncarpusChibi,
				ModSubtiles.hyacidus, ModSubtiles.pollidisiac, ModSubtiles.clayconia,
				ModSubtiles.clayconiaChibi, ModSubtiles.loonium, ModSubtiles.daffomill, ModSubtiles.vinculotus,
				ModSubtiles.spectranthemum, ModSubtiles.medumone, ModSubtiles.marimorphosis, ModSubtiles.marimorphosisChibi,
				ModSubtiles.bubbell, ModSubtiles.bubbellChibi, ModSubtiles.solegnolia, ModSubtiles.solegnoliaChibi,
				ModSubtiles.orechidIgnem, ModSubtiles.labellia }) {
			createFloatingFlowerRecipe(consumer, block);
		}
	}

	private void registerConversions(Consumer<FinishedRecipe> consumer) {
		compression(ModItems.manaSteel, ModTags.Items.NUGGETS_MANASTEEL)
				.save(consumer, prefix("conversions/manasteel_from_nuggets"));
		compression(ModItems.elementium, ModTags.Items.NUGGETS_ELEMENTIUM)
				.save(consumer, prefix("conversions/elementium_from_nuggets"));
		compression(ModItems.terrasteel, ModTags.Items.NUGGETS_TERRASTEEL)
				.save(consumer, prefix("conversions/terrasteel_from_nugget"));
		compression(ModBlocks.manasteelBlock, ModTags.Items.INGOTS_MANASTEEL).save(consumer);
		compression(ModBlocks.terrasteelBlock, ModTags.Items.INGOTS_TERRASTEEL).save(consumer);
		compression(ModBlocks.elementiumBlock, ModTags.Items.INGOTS_ELEMENTIUM).save(consumer);
		compression(ModBlocks.manaDiamondBlock, ModTags.Items.GEMS_MANA_DIAMOND).save(consumer);
		compression(ModBlocks.dragonstoneBlock, ModTags.Items.GEMS_DRAGONSTONE).save(consumer);

		MutableObject<FinishedRecipe> base = new MutableObject<>();
		MutableObject<FinishedRecipe> gog = new MutableObject<>();
		compression(ModBlocks.blazeBlock, Items.BLAZE_ROD).save(base::setValue);
		ShapedRecipeBuilder.shaped(ModBlocks.blazeBlock)
				.define('I', Items.BLAZE_POWDER)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_item", conditionsFromItem(Items.BLAZE_POWDER))
				.save(gog::setValue);
		consumer.accept(new GogAlternationResult(gog.getValue(), base.getValue()));

		deconstructPetalBlock(consumer, ModItems.whitePetal, ModBlocks.petalBlockWhite);
		deconstructPetalBlock(consumer, ModItems.orangePetal, ModBlocks.petalBlockOrange);
		deconstructPetalBlock(consumer, ModItems.magentaPetal, ModBlocks.petalBlockMagenta);
		deconstructPetalBlock(consumer, ModItems.lightBluePetal, ModBlocks.petalBlockLightBlue);
		deconstructPetalBlock(consumer, ModItems.yellowPetal, ModBlocks.petalBlockYellow);
		deconstructPetalBlock(consumer, ModItems.limePetal, ModBlocks.petalBlockLime);
		deconstructPetalBlock(consumer, ModItems.pinkPetal, ModBlocks.petalBlockPink);
		deconstructPetalBlock(consumer, ModItems.grayPetal, ModBlocks.petalBlockGray);
		deconstructPetalBlock(consumer, ModItems.lightGrayPetal, ModBlocks.petalBlockSilver);
		deconstructPetalBlock(consumer, ModItems.cyanPetal, ModBlocks.petalBlockCyan);
		deconstructPetalBlock(consumer, ModItems.purplePetal, ModBlocks.petalBlockPurple);
		deconstructPetalBlock(consumer, ModItems.bluePetal, ModBlocks.petalBlockBlue);
		deconstructPetalBlock(consumer, ModItems.brownPetal, ModBlocks.petalBlockBrown);
		deconstructPetalBlock(consumer, ModItems.greenPetal, ModBlocks.petalBlockGreen);
		deconstructPetalBlock(consumer, ModItems.redPetal, ModBlocks.petalBlockRed);
		deconstructPetalBlock(consumer, ModItems.blackPetal, ModBlocks.petalBlockBlack);

		deconstruct(base::setValue, Items.BLAZE_ROD, ModBlocks.blazeBlock, "blazeblock_deconstruct");
		deconstruct(gog::setValue, Items.BLAZE_POWDER, ModBlocks.blazeBlock, "blazeblock_deconstruct");
		consumer.accept(new GogAlternationResult(gog.getValue(), base.getValue()));
		deconstruct(consumer, ModItems.manaSteel, ModTags.Items.BLOCKS_MANASTEEL, "manasteel_block_deconstruct");
		deconstruct(consumer, ModItems.manaDiamond, ModBlocks.manaDiamondBlock, "manadiamond_block_deconstruct");
		deconstruct(consumer, ModItems.terrasteel, ModTags.Items.BLOCKS_TERRASTEEL, "terrasteel_block_deconstruct");
		deconstruct(consumer, ModItems.elementium, ModTags.Items.BLOCKS_ELEMENTIUM, "elementium_block_deconstruct");
		deconstruct(consumer, ModItems.dragonstone, ModBlocks.dragonstoneBlock, "dragonstone_block_deconstruct");
		deconstruct(consumer, ModItems.manasteelNugget, ModTags.Items.INGOTS_MANASTEEL, "manasteel_to_nuggets");
		deconstruct(consumer, ModItems.terrasteelNugget, ModTags.Items.INGOTS_TERRASTEEL, "terrasteel_to_nugget");
		deconstruct(consumer, ModItems.elementiumNugget, ModTags.Items.INGOTS_ELEMENTIUM, "elementium_to_nuggets");

		recombineSlab(consumer, ModBlocks.livingrock, ModFluffBlocks.livingrockSlab);
		recombineSlab(consumer, ModBlocks.livingrockBrick, ModFluffBlocks.livingrockBrickSlab);
		recombineSlab(consumer, ModBlocks.livingwood, ModFluffBlocks.livingwoodSlab);
		recombineSlab(consumer, ModBlocks.livingwoodPlanks, ModFluffBlocks.livingwoodPlankSlab);
		recombineSlab(consumer, ModBlocks.dreamwood, ModFluffBlocks.dreamwoodSlab);
		recombineSlab(consumer, ModBlocks.dreamwoodPlanks, ModFluffBlocks.dreamwoodPlankSlab);
		recombineSlab(consumer, ModBlocks.shimmerrock, ModFluffBlocks.shimmerrockSlab);
		recombineSlab(consumer, ModBlocks.shimmerwoodPlanks, ModFluffBlocks.shimmerwoodPlankSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeStoneForest, ModFluffBlocks.biomeStoneForestSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeBrickForest, ModFluffBlocks.biomeBrickForestSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeCobblestoneForest, ModFluffBlocks.biomeCobblestoneForestSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeStonePlains, ModFluffBlocks.biomeStonePlainsSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeBrickPlains, ModFluffBlocks.biomeBrickPlainsSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeCobblestonePlains, ModFluffBlocks.biomeCobblestonePlainsSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeStoneMountain, ModFluffBlocks.biomeStoneMountainSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeBrickMountain, ModFluffBlocks.biomeBrickMountainSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeCobblestoneMountain, ModFluffBlocks.biomeCobblestoneMountainSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeStoneFungal, ModFluffBlocks.biomeStoneFungalSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeBrickFungal, ModFluffBlocks.biomeBrickFungalSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeCobblestoneFungal, ModFluffBlocks.biomeCobblestoneFungalSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeStoneSwamp, ModFluffBlocks.biomeStoneSwampSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeBrickSwamp, ModFluffBlocks.biomeBrickSwampSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeCobblestoneSwamp, ModFluffBlocks.biomeCobblestoneSwampSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeStoneDesert, ModFluffBlocks.biomeStoneDesertSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeBrickDesert, ModFluffBlocks.biomeBrickDesertSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeCobblestoneDesert, ModFluffBlocks.biomeCobblestoneDesertSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeStoneTaiga, ModFluffBlocks.biomeStoneTaigaSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeBrickTaiga, ModFluffBlocks.biomeBrickTaigaSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeCobblestoneTaiga, ModFluffBlocks.biomeCobblestoneTaigaSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeStoneMesa, ModFluffBlocks.biomeStoneMesaSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeBrickMesa, ModFluffBlocks.biomeBrickMesaSlab);
		recombineSlab(consumer, ModFluffBlocks.biomeCobblestoneMesa, ModFluffBlocks.biomeCobblestoneMesaSlab);
		recombineSlab(consumer, ModFluffBlocks.whitePavement, ModFluffBlocks.whitePavementSlab);
		recombineSlab(consumer, ModFluffBlocks.blackPavement, ModFluffBlocks.blackPavementSlab);
		recombineSlab(consumer, ModFluffBlocks.bluePavement, ModFluffBlocks.bluePavementSlab);
		recombineSlab(consumer, ModFluffBlocks.yellowPavement, ModFluffBlocks.yellowPavementSlab);
		recombineSlab(consumer, ModFluffBlocks.redPavement, ModFluffBlocks.redPavementSlab);
		recombineSlab(consumer, ModFluffBlocks.greenPavement, ModFluffBlocks.greenPavementSlab);
	}

	private void registerDecor(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ModBlocks.livingrockBrick, 4)
				.define('R', ModBlocks.livingrock)
				.pattern("RR")
				.pattern("RR")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.livingrock))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.livingrockBrickChiseled, 4)
				.define('R', ModBlocks.livingrockBrick)
				.pattern("RR")
				.pattern("RR")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.livingrockBrick))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModBlocks.livingrockBrickCracked, 2)
				.requires(ModBlocks.livingrockBrick)
				.requires(Items.COBBLESTONE)
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.livingrockBrick))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModBlocks.livingrockBrickMossy)
				.requires(ModBlocks.livingrockBrick)
				.requires(Items.MOSS_BLOCK)
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.livingrockBrick))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModBlocks.livingrockBrickMossy)
				.requires(ModBlocks.livingrockBrick)
				.requires(Items.VINE)
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.livingrockBrick))
				.save(consumer, "botania:mossy_livingrock_bricks_vine");
		ShapelessRecipeBuilder.shapeless(ModBlocks.shimmerrock)
				.requires(ModBlocks.livingrock)
				.requires(ModBlocks.bifrostPerm)
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.bifrostPerm))
				.unlockedBy("has_alt_item", conditionsFromItem(ModItems.rainbowRod))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModBlocks.shimmerwoodPlanks)
				.requires(ModBlocks.dreamwoodPlanks)
				.requires(ModBlocks.bifrostPerm)
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.bifrostPerm))
				.unlockedBy("has_alt_item", conditionsFromItem(ModItems.rainbowRod))
				.save(consumer);

		registerForQuartz(consumer, LibBlockNames.QUARTZ_DARK, ModItems.darkQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_MANA, ModItems.manaQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_BLAZE, ModItems.blazeQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_LAVENDER, ModItems.lavenderQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_RED, ModItems.redQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_ELF, ModItems.elfQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_SUNNY, ModItems.sunnyQuartz);

		registerForWood(consumer, LibBlockNames.LIVING_WOOD);
		registerForWood(consumer, LibBlockNames.DREAM_WOOD);

		stairs(ModFluffBlocks.livingrockStairs, ModBlocks.livingrock).save(consumer);
		slabShape(ModFluffBlocks.livingrockSlab, ModBlocks.livingrock).save(consumer);
		wallShape(ModFluffBlocks.livingrockWall, ModBlocks.livingrock, 6).save(consumer);

		stairs(ModFluffBlocks.livingrockBrickStairs, ModBlocks.livingrockBrick).save(consumer);
		slabShape(ModFluffBlocks.livingrockBrickSlab, ModBlocks.livingrockBrick).save(consumer);
		wallShape(ModFluffBlocks.livingrockBrickWall, ModBlocks.livingrockBrick, 6).save(consumer);

		stairs(ModFluffBlocks.livingrockBrickMossyStairs, ModBlocks.livingrockBrickMossy).save(consumer);
		slabShape(ModFluffBlocks.livingrockBrickMossySlab, ModBlocks.livingrockBrickMossy).save(consumer);
		wallShape(ModFluffBlocks.livingrockBrickMossyWall, ModBlocks.livingrockBrickMossy, 6).save(consumer);

		stairs(ModFluffBlocks.shimmerrockStairs, ModBlocks.shimmerrock).save(consumer);
		slabShape(ModFluffBlocks.shimmerrockSlab, ModBlocks.shimmerrock).save(consumer);
		stairs(ModFluffBlocks.shimmerwoodPlankStairs, ModBlocks.shimmerwoodPlanks).save(consumer);
		slabShape(ModFluffBlocks.shimmerwoodPlankSlab, ModBlocks.shimmerwoodPlanks).save(consumer);

		for (String variant : LibBlockNames.METAMORPHIC_VARIANTS) {
			registerForMetamorphic(consumer, variant);
		}

		Item[] pavementIngredients = { Items.AIR, Items.COAL, Items.LAPIS_LAZULI, Items.REDSTONE, Items.WHEAT, Items.SLIME_BALL };
		for (int i = 0; i < pavementIngredients.length; i++) {
			registerForPavement(consumer, LibBlockNames.PAVEMENT_VARIANTS[i], pavementIngredients[i]);
		}

		wallShape(ModFluffBlocks.managlassPane, ModBlocks.manaGlass, 16).save(consumer);
		wallShape(ModFluffBlocks.alfglassPane, ModBlocks.elfGlass, 16).save(consumer);
		wallShape(ModFluffBlocks.bifrostPane, ModBlocks.bifrostPerm, 16).save(consumer);

		ShapelessRecipeBuilder.shapeless(ModBlocks.azulejo0)
				.requires(Items.BLUE_DYE)
				.requires(ModTags.Items.BLOCKS_QUARTZ)
				.unlockedBy("has_item", conditionsFromItem(Items.BLUE_DYE))
				.save(consumer);

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
					.unlockedBy("has_azulejo", conditionsFromItem(ModBlocks.azulejo0))
					.group("botania:azulejo_cycling")
					.save(consumer, prefix(recipeName));
		}

		cosmeticBauble(consumer, ModItems.blackBowtie, ModItems.whitePetal);
		cosmeticBauble(consumer, ModItems.blackTie, ModItems.orangePetal);
		cosmeticBauble(consumer, ModItems.redGlasses, ModItems.magentaPetal);
		cosmeticBauble(consumer, ModItems.puffyScarf, ModItems.lightBluePetal);
		cosmeticBauble(consumer, ModItems.engineerGoggles, ModItems.yellowPetal);
		cosmeticBauble(consumer, ModItems.eyepatch, ModItems.limePetal);
		cosmeticBauble(consumer, ModItems.wickedEyepatch, ModItems.pinkPetal);
		cosmeticBauble(consumer, ModItems.redRibbons, ModItems.grayPetal);
		cosmeticBauble(consumer, ModItems.pinkFlowerBud, ModItems.lightGrayPetal);
		cosmeticBauble(consumer, ModItems.polkaDottedBows, ModItems.cyanPetal);
		cosmeticBauble(consumer, ModItems.blueButterfly, ModItems.purplePetal);
		cosmeticBauble(consumer, ModItems.catEars, ModItems.bluePetal);
		cosmeticBauble(consumer, ModItems.witchPin, ModItems.brownPetal);
		cosmeticBauble(consumer, ModItems.devilTail, ModItems.greenPetal);
		cosmeticBauble(consumer, ModItems.kamuiEye, ModItems.redPetal);
		cosmeticBauble(consumer, ModItems.googlyEyes, ModItems.blackPetal);
		cosmeticBauble(consumer, ModItems.fourLeafClover, Items.WHITE_DYE);
		cosmeticBauble(consumer, ModItems.clockEye, Items.ORANGE_DYE);
		cosmeticBauble(consumer, ModItems.unicornHorn, Items.MAGENTA_DYE);
		cosmeticBauble(consumer, ModItems.devilHorns, Items.LIGHT_BLUE_DYE);
		cosmeticBauble(consumer, ModItems.hyperPlus, Items.YELLOW_DYE);
		cosmeticBauble(consumer, ModItems.botanistEmblem, Items.LIME_DYE);
		cosmeticBauble(consumer, ModItems.ancientMask, Items.PINK_DYE);
		cosmeticBauble(consumer, ModItems.eerieMask, Items.GRAY_DYE);
		cosmeticBauble(consumer, ModItems.alienAntenna, Items.LIGHT_GRAY_DYE);
		cosmeticBauble(consumer, ModItems.anaglyphGlasses, Items.CYAN_DYE);
		cosmeticBauble(consumer, ModItems.orangeShades, Items.PURPLE_DYE);
		cosmeticBauble(consumer, ModItems.grouchoGlasses, Items.BLUE_DYE);
		cosmeticBauble(consumer, ModItems.thickEyebrows, Items.BROWN_DYE);
		cosmeticBauble(consumer, ModItems.lusitanicShield, Items.GREEN_DYE);
		cosmeticBauble(consumer, ModItems.tinyPotatoMask, Items.RED_DYE);
		cosmeticBauble(consumer, ModItems.questgiverMark, Items.BLACK_DYE);
		cosmeticBauble(consumer, ModItems.thinkingHand, ModBlocks.tinyPotato);
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
				.define('T', ModItems.livingwoodTwig)
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

	protected void registerRedStringBlock(Consumer<FinishedRecipe> consumer, ItemLike output, Ingredient input, CriterionTriggerInstance criterion) {
		ShapedRecipeBuilder.shaped(output)
				.define('R', ModBlocks.livingrock)
				.define('S', ModItems.redString)
				.define('M', input)
				.pattern("RRR")
				.pattern("RMS")
				.pattern("RRR")
				.unlockedBy("has_item", conditionsFromItem(ModItems.redString))
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

	protected void deconstruct(Consumer<FinishedRecipe> consumer, ItemLike output, Tag<Item> input, String name) {
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

		Tag<Item> tag = variant.contains("livingwood") ? ModTags.Items.LIVINGWOOD_LOGS : ModTags.Items.DREAMWOOD_LOGS;
		Block log = Registry.BLOCK.getOptional(prefix(variant + "_log")).orElseThrow(); //
		Block bark = Registry.BLOCK.getOptional(prefix(variant)).orElseThrow(); //
		Block strippedLog = Registry.BLOCK.getOptional(prefix("stripped_" + variant + "_log")).orElseThrow(); //
		Block strippedBark = Registry.BLOCK.getOptional(prefix("stripped_" + variant)).orElseThrow(); //
		Block glimmeringLog = Registry.BLOCK.getOptional(prefix("glimmering_" + variant + "_log")).orElseThrow();
		Block glimmeringBark = Registry.BLOCK.getOptional(prefix("glimmering_" + variant)).orElseThrow();
		Block glimmeringStrippedLog = Registry.BLOCK.getOptional(prefix("glimmering_stripped_" + variant + "_log")).orElseThrow();
		Block glimmeringStrippedBark = Registry.BLOCK.getOptional(prefix("glimmering_stripped_" + variant)).orElseThrow();
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
		ShapedRecipeBuilder.shaped(bark, 3).group("bark").unlockedBy("has_log", conditionsFromItem(log))
				.define('#', log)
				.pattern("##")
				.pattern("##").save(consumer);
		ShapedRecipeBuilder.shaped(strippedBark, 3).group("bark").unlockedBy("has_log", conditionsFromItem(strippedLog))
				.define('#', strippedLog)
				.pattern("##")
				.pattern("##").save(consumer);
		ShapelessRecipeBuilder.shapeless(glimmeringLog).group("botania:glimmering_" + variant)
				.requires(log)
				.requires(Items.GLOWSTONE_DUST)
				.unlockedBy("has_item", conditionsFromItem(log))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(glimmeringBark).group("botania:glimmering_" + variant)
				.requires(bark)
				.requires(Items.GLOWSTONE_DUST)
				.unlockedBy("has_item", conditionsFromItem(bark))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(glimmeringStrippedLog).group("botania:glimmering_" + variant)
				.requires(strippedLog)
				.requires(Items.GLOWSTONE_DUST)
				.unlockedBy("has_item", conditionsFromItem(strippedLog))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(glimmeringStrippedBark).group("botania:glimmering_" + variant)
				.requires(strippedBark)
				.requires(Items.GLOWSTONE_DUST)
				.unlockedBy("has_item", conditionsFromItem(strippedBark))
				.save(consumer);
		ShapedRecipeBuilder.shaped(glimmeringBark, 3).group("botania:glimmering_" + variant)
				.unlockedBy("has_log", conditionsFromItem(glimmeringLog))
				.define('#', glimmeringLog)
				.pattern("##")
				.pattern("##").save(consumer, prefix("glimmering_" + variant + "_alt"));
		ShapedRecipeBuilder.shaped(glimmeringStrippedBark, 3).group("botania:glimmering_" + variant)
				.unlockedBy("has_log", conditionsFromItem(glimmeringStrippedLog))
				.define('#', glimmeringStrippedLog)
				.pattern("##")
				.pattern("##").save(consumer, prefix("glimmering_stripped_" + variant + "_alt"));

		stairs(stairs, bark).save(consumer);
		slabShape(slab, bark).save(consumer);
		wallShape(wall, bark, 6).save(consumer);
		fence(fence, planks).save(consumer);
		fenceGate(fenceGate, planks).save(consumer);

		stairs(strippedStairs, strippedBark).save(consumer);
		slabShape(strippedSlab, strippedBark).save(consumer);
		wallShape(strippedWall, strippedBark, 6).save(consumer);

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
				.save(consumer, "botania:mossy_" + variant + "_planks_vine");
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
				.requires(ModBlocks.livingrock)
				.requires(Items.COBBLESTONE)
				.requires(Items.GRAVEL)
				.group("botania:pavement")
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.livingrock));
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
		Block brick = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks")).get();
		Block brickSlab = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.SLAB_SUFFIX)).get();
		Block brickStair = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.STAIR_SUFFIX)).get();
		Block brickWall = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.WALL_SUFFIX)).get();
		Block chiseledBrick = Registry.BLOCK.getOptional(prefix("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks")).get();
		Block cobble = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone")).get();
		Block cobbleSlab = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.SLAB_SUFFIX)).get();
		Block cobbleStair = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.STAIR_SUFFIX)).get();
		Block cobbleWall = Registry.BLOCK.getOptional(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.WALL_SUFFIX)).get();

		InventoryChangeTrigger.TriggerInstance marimorphosis = conditionsFromItem(ModSubtiles.marimorphosis);
		slabShape(slab, base).group("botania:metamorphic_stone_slab")
				.unlockedBy("has_flower_item", marimorphosis).save(consumer);
		stairs(stair, base).group("botania:metamorphic_stone_stairs")
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

	private ShapedRecipeBuilder compression(ItemLike output, Tag<Item> input) {
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
				.define('S', ModItems.manaString)
				.pattern("PPP")
				.pattern("PSP")
				.pattern("PPP")
				.group("botania:cosmetic_bauble")
				.unlockedBy("has_item", conditionsFromItem(ModItems.manaString))
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
