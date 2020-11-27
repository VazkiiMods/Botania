/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.recipe.ComplexRecipeJsonFactory;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
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
import vazkii.botania.mixin.AccessorRecipesProvider;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class RecipeProvider extends net.minecraft.data.server.RecipesProvider implements BotaniaRecipeProvider {
	public RecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	public void registerRecipes(Consumer<RecipeJsonProvider> consumer) {
		specialRecipe(consumer, AncientWillRecipe.SERIALIZER);
		specialRecipe(consumer, BannerRecipe.SERIALIZER);
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
		specialRecipe(consumer, PhantomInkRecipe.SERIALIZER);
		specialRecipe(consumer, SpellClothRecipe.SERIALIZER);
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

	private static InventoryChangedCriterion.Conditions conditionsFromItem(ItemConvertible item) {
		return AccessorRecipesProvider.botania_condition(item);
	}

	private static InventoryChangedCriterion.Conditions conditionsFromItems(ItemConvertible... items) {
		ItemPredicate[] preds = new ItemPredicate[items.length];
		for (int i = 0; i < items.length; i++) {
			preds[i] = ItemPredicate.Builder.create().item(items[i]).build();
		}

		return AccessorRecipesProvider.botania_condition(preds);
	}

	private static InventoryChangedCriterion.Conditions conditionsFromTag(Tag<Item> tag) {
		return AccessorRecipesProvider.botania_condition(tag);
	}

	private void registerMain(Consumer<RecipeJsonProvider> consumer) {
		InventoryChangedCriterion.Conditions hasAnyDye = conditionsFromItems(
			Arrays.stream(DyeColor.values()).map(DyeItem::byColor).toArray(ItemConvertible[]::new)
		);
		ShapedRecipeJsonFactory.create(ModBlocks.manaSpreader)
				.input('P', ModTags.Items.PETALS)
				.input('W', ModTags.Items.LIVINGWOOD)
				.input('G', Items.GOLD_INGOT)
				.pattern("WWW")
				.pattern("GP ")
				.pattern("WWW")
				.group("botania:spreader")
				.criterion("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.redstoneSpreader)
				.input(ModBlocks.manaSpreader)
				.input(Items.REDSTONE)
				.group("botania:spreader")
				.criterion("has_item", conditionsFromItem(ModBlocks.manaSpreader))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.elvenSpreader)
				.input('P', ModTags.Items.PETALS)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.input('W', ModBlocks.dreamwood)
				.pattern("WWW")
				.pattern("EP ")
				.pattern("WWW")
				.group("botania:spreader")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.criterion("has_alt_item", conditionsFromItem(ModBlocks.dreamwood))
				.offerTo(consumer);
        ShapelessRecipeJsonFactory.create(ModBlocks.gaiaSpreader)
				.input(ModBlocks.elvenSpreader)
				.input(ModTags.Items.GEMS_DRAGONSTONE)
				.input(ModItems.lifeEssence)
				.group("botania:spreader")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.manaPool)
				.input('R', ModTags.Items.LIVINGROCK)
				.pattern("R R")
				.pattern("RRR")
				.criterion("has_item", conditionsFromTag(ModTags.Items.LIVINGROCK))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.dilutedPool)
				.input('R', ModFluffBlocks.livingrockSlab)
				.pattern("R R")
				.pattern("RRR")
				.criterion("has_item", conditionsFromTag(ModTags.Items.LIVINGROCK))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.fabulousPool)
				.input('R', ModBlocks.shimmerrock)
				.pattern("R R")
				.pattern("RRR")
				.criterion("has_item", conditionsFromItem(ModBlocks.shimmerrock))
				.criterion("has_alt_item", conditionsFromItem(ModItems.rainbowRod))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.runeAltar)
				.input('P', AccessorIngredient.botania_ofEntries(Stream.of(
						new Ingredient.StackEntry(new ItemStack(ModItems.manaPearl)),
						new Ingredient.TagEntry(ModTags.Items.GEMS_MANA_DIAMOND))))
				.input('S', ModTags.Items.LIVINGROCK)
				.pattern("SSS")
				.pattern("SPS")
				.criterion("has_item", conditionsFromItem(ModItems.manaPearl))
				.criterion("has_alt_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.manaPylon)
				.input('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.input('G', Items.GOLD_INGOT)
				.input('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern(" G ")
				.pattern("MDM")
				.pattern(" G ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.naturaPylon)
				.input('P', ModBlocks.manaPylon)
				.input('T', ModTags.Items.NUGGETS_TERRASTEEL)
				.input('E', Items.ENDER_EYE)
				.pattern(" T ")
				.pattern("TPT")
				.pattern(" E ")
				.criterion("has_item", conditionsFromItem(ModBlocks.manaPylon))
				.criterion("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.gaiaPylon)
				.input('P', ModBlocks.manaPylon)
				.input('D', ModItems.pixieDust)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern(" D ")
				.pattern("EPE")
				.pattern(" D ")
				.criterion("has_item", conditionsFromItem(ModItems.pixieDust))
				.criterion("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.distributor)
				.input('R', ModTags.Items.LIVINGROCK)
				.input('S', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("RRR")
				.pattern("S S")
				.pattern("RRR")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.manaVoid)
				.input('S', ModTags.Items.LIVINGROCK)
				.input('O', Items.OBSIDIAN)
				.pattern("SSS")
				.pattern("O O")
				.pattern("SSS")
				.criterion("has_item", conditionsFromTag(ModTags.Items.LIVINGROCK))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.manaDetector)
				.input('R', Items.REDSTONE)
				.input('C', Items.COMPARATOR)
				.input('S', ModTags.Items.LIVINGROCK)
				.pattern("RSR")
				.pattern("SCS")
				.pattern("RSR")
				.criterion("has_item", conditionsFromItem(Items.COMPARATOR))
				.criterion("has_alt_item", conditionsFromTag(ModTags.Items.LIVINGROCK))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.turntable)
				.input('P', Items.STICKY_PISTON)
				.input('W', ModTags.Items.LIVINGWOOD)
				.pattern("WWW")
				.pattern("WPW")
				.pattern("WWW")
				.criterion("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD))
				.criterion("has_alt_item", conditionsFromItem(Items.STICKY_PISTON))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.tinyPlanet)
				.input('P', ModItems.tinyPlanet)
				.input('S', Items.STONE)
				.pattern("SSS")
				.pattern("SPS")
				.pattern("SSS")
				.criterion("has_item", conditionsFromItem(ModItems.tinyPlanet))
				.criterion("has_alt_item", conditionsFromItem(ModItems.manaPearl))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.alchemyCatalyst)
				.input('P', ModItems.manaPearl)
				.input('B', Items.BREWING_STAND)
				.input('S', ModTags.Items.LIVINGROCK)
				.input('G', Items.GOLD_INGOT)
				.pattern("SGS")
				.pattern("BPB")
				.pattern("SGS")
				.criterion("has_item", conditionsFromItem(ModItems.manaPearl))
				.criterion("has_alt_item", conditionsFromItem(Items.BREWING_STAND))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.openCrate)
				.input('W', ModBlocks.livingwoodPlanks)
				.pattern("WWW")
				.pattern("W W")
				.pattern("W W")
				.criterion("has_item", conditionsFromItem(ModBlocks.livingwoodPlanks))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.craftCrate)
				.input('C', Items.CRAFTING_TABLE)
				.input('W', ModBlocks.dreamwoodPlanks)
				.pattern("WCW")
				.pattern("W W")
				.pattern("W W")
				.criterion("has_item", conditionsFromItem(ModBlocks.dreamwoodPlanks))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.forestEye)
				.input('S', ModTags.Items.LIVINGROCK)
				.input('E', Items.ENDER_EYE)
				.input('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("MSM")
				.pattern("SES")
				.pattern("MSM")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.wildDrum)
				.input('W', ModTags.Items.LIVINGWOOD)
				.input('H', ModItems.grassHorn)
				.input('L', Items.LEATHER)
				.pattern("WLW")
				.pattern("WHW")
				.pattern("WLW")
				.criterion("has_item", conditionsFromItem(ModItems.grassHorn))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.gatheringDrum)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.input('W', ModBlocks.dreamwood)
				.input('L', Items.LEATHER)
				.pattern("WLW")
				.pattern("WEW")
				.pattern("WLW")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.criterion("has_alt_item", conditionsFromItem(ModBlocks.dreamwood))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.canopyDrum)
				.input('W', ModTags.Items.LIVINGWOOD)
				.input('H', ModItems.leavesHorn)
				.input('L', Items.LEATHER)
				.pattern("WLW")
				.pattern("WHW")
				.pattern("WLW")
				.criterion("has_item", conditionsFromItem(ModItems.leavesHorn))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.abstrusePlatform, 2)
				.input('0', ModBlocks.livingwood)
				.input('P', ModItems.manaPearl)
				.input('3', ModBlocks.livingwoodFramed)
				.input('4', ModBlocks.livingwoodPatternFramed)
				.pattern("343")
				.pattern("0P0")
				.criterion("has_item", conditionsFromItem(ModItems.manaPearl))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.spectralPlatform, 2)
				.input('0', ModBlocks.dreamwood)
				.input('3', ModBlocks.dreamwoodFramed)
				.input('4', ModBlocks.dreamwoodPatternFramed)
				.input('D', ModItems.pixieDust)
				.pattern("343")
				.pattern("0D0")
				.criterion("has_item", conditionsFromItem(ModItems.pixieDust))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.alfPortal)
				.input('T', ModTags.Items.NUGGETS_TERRASTEEL)
				.input('W', ModTags.Items.LIVINGWOOD)
				.pattern("WTW")
				.pattern("WTW")
				.pattern("WTW")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.conjurationCatalyst)
				.input('P', ModBlocks.alchemyCatalyst)
				.input('B', ModItems.pixieDust)
				.input('S', ModTags.Items.LIVINGROCK)
				.input('G', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("SBS")
				.pattern("GPG")
				.pattern("SGS")
				.criterion("has_item", conditionsFromItem(ModItems.pixieDust))
				.criterion("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.spawnerClaw)
				.input('P', Items.PRISMARINE_BRICKS)
				.input('B', Items.BLAZE_ROD)
				.input('S', ModTags.Items.INGOTS_ELEMENTIUM)
				.input('E', ModItems.enderAirBottle)
				.input('M', ModTags.Items.BLOCKS_MANASTEEL)
				.pattern("BSB")
				.pattern("PMP")
				.pattern("PEP")
				.criterion("has_item", conditionsFromItem(ModItems.enderAirBottle))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.enderEye)
				.input('R', Items.REDSTONE)
				.input('E', Items.ENDER_EYE)
				.input('O', Items.OBSIDIAN)
				.pattern("RER")
				.pattern("EOE")
				.pattern("RER")
				.criterion("has_item", conditionsFromItem(Items.ENDER_EYE))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.starfield)
				.input('P', ModItems.pixieDust)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.input('O', Items.OBSIDIAN)
				.pattern("EPE")
				.pattern("EOE")
				.criterion("has_item", conditionsFromItem(ModItems.pixieDust))
				.criterion("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.rfGenerator)
				.input('R', Items.REDSTONE_BLOCK)
				.input('S', ModTags.Items.LIVINGROCK)
				.input('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("SRS")
				.pattern("RMR")
				.pattern("SRS")
				.criterion("has_item", conditionsFromItem(Items.REDSTONE_BLOCK))
				.criterion("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(/* todo 1.16-fabricWrapperResult.transformJson(consumer, json -> {
					JsonArray array = new JsonArray();
					array.add(FluxfieldCondition.SERIALIZER.getJson(new FluxfieldCondition(true)));
					json.add("conditions", array);
				}
				)*/ consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.brewery)
				.input('A', ModTags.Items.RUNES_MANA)
				.input('R', ModTags.Items.LIVINGROCK)
				.input('S', Items.BREWING_STAND)
				.input('M', ModTags.Items.BLOCKS_MANASTEEL)
				.pattern("RSR")
				.pattern("RAR")
				.pattern("RMR")
				.criterion("has_item", conditionsFromTag(ModTags.Items.RUNES_MANA))
				.criterion("has_alt_item", conditionsFromItem(Items.BREWING_STAND))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.terraPlate)
				.input('0', ModTags.Items.RUNES_WATER)
				.input('1', ModTags.Items.RUNES_FIRE)
				.input('2', ModTags.Items.RUNES_EARTH)
				.input('3', ModTags.Items.RUNES_AIR)
				.input('8', ModTags.Items.RUNES_MANA)
				.input('L', Blocks.LAPIS_BLOCK)
				.input('M', ModTags.Items.BLOCKS_MANASTEEL)
				.pattern("LLL")
				.pattern("0M1")
				.pattern("283")
				.criterion("has_item", conditionsFromTag(ModTags.Items.RUNES))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.prism)
				.input('P', Items.PRISMARINE_CRYSTALS)
				.input('S', ModBlocks.spectralPlatform)
				.input('G', Items.GLASS)
				.pattern("GPG")
				.pattern("GSG")
				.pattern("GPG")
				.criterion("has_item", conditionsFromItem(Items.PRISMARINE_CRYSTALS))
				.criterion("has_alt_item", conditionsFromItem(ModBlocks.spectralPlatform))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.pump)
				.input('B', Items.BUCKET)
				.input('S', ModTags.Items.LIVINGROCK)
				.input('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("SSS")
				.pattern("IBI")
				.pattern("SSS")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.incensePlate)
				.input('S', ModFluffBlocks.livingwoodSlab)
				.input('W', ModTags.Items.LIVINGWOOD)
				.pattern("WSS")
				.criterion("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.hourglass)
				.input('R', Items.REDSTONE)
				.input('S', ModTags.Items.INGOTS_MANASTEEL)
				.input('G', Items.GOLD_INGOT)
				.input('M', ModBlocks.manaGlass)
				.pattern("GMG")
				.pattern("RSR")
				.pattern("GMG")
				.criterion("has_item", conditionsFromItem(ModBlocks.manaGlass))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.ghostRail)
				.input(Items.RAIL)
				.input(ModBlocks.spectralPlatform)
				.criterion("has_item", conditionsFromItem(Items.RAIL))
				.criterion("has_alt_item", conditionsFromItem(ModBlocks.spectralPlatform))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.sparkChanger)
				.input('R', Items.REDSTONE)
				.input('S', ModTags.Items.LIVINGROCK)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("ESE")
				.pattern("SRS")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.felPumpkin)
				.input('P', Items.PUMPKIN)
				.input('B', Items.BONE)
				.input('S', Items.STRING)
				.input('F', Items.ROTTEN_FLESH)
				.input('G', Items.GUNPOWDER)
				.pattern(" S ")
				.pattern("BPF")
				.pattern(" G ")
				.criterion("has_item", conditionsFromItem(Items.PUMPKIN))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.cocoon)
				.input('S', Items.STRING)
				.input('C', ModItems.manaweaveCloth)
				.input('P', ModBlocks.felPumpkin)
				.input('D', ModItems.pixieDust)
				.pattern("SSS")
				.pattern("CPC")
				.pattern("SDS")
				.criterion("has_item", conditionsFromItem(ModBlocks.felPumpkin))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.lightRelayDefault)
				.input(ModItems.redString)
				.input(ModTags.Items.GEMS_DRAGONSTONE)
				.input(Items.GLOWSTONE_DUST)
				.input(Items.GLOWSTONE_DUST)
				.criterion("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.lightRelayDetector)
				.input(ModBlocks.lightRelayDefault)
				.input(Items.REDSTONE)
				.criterion("has_item", conditionsFromItem(ModBlocks.lightRelayDefault))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.lightRelayFork)
				.input(ModBlocks.lightRelayDefault)
				.input(Items.REDSTONE_TORCH)
				.criterion("has_item", conditionsFromItem(ModBlocks.lightRelayDefault))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.lightRelayToggle)
				.input(ModBlocks.lightRelayDefault)
				.input(Items.LEVER)
				.criterion("has_item", conditionsFromItem(ModBlocks.lightRelayDefault))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.lightLauncher)
				.input('D', ModBlocks.dreamwood)
				.input('L', ModBlocks.lightRelayDefault)
				.pattern("DDD")
				.pattern("DLD")
				.criterion("has_item", conditionsFromItem(ModBlocks.lightRelayDefault))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.manaBomb)
				.input('T', Items.TNT)
				.input('G', ModItems.lifeEssence)
				.input('L', ModTags.Items.LIVINGWOOD)
				.pattern("LTL")
				.pattern("TGT")
				.pattern("LTL")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.bellows)
				.input('R', ModTags.Items.RUNES_AIR)
				.input('S', ModFluffBlocks.livingwoodSlab)
				.input('L', Items.LEATHER)
				.pattern("SSS")
				.pattern("RL ")
				.pattern("SSS")
				.criterion("has_item", conditionsFromTag(ModTags.Items.RUNES_AIR))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.bifrostPerm)
				.input(ModItems.rainbowRod)
				.input(ModBlocks.elfGlass)
				.criterion("has_item", conditionsFromItem(ModItems.rainbowRod))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.cellBlock, 3)
				.input(Items.CACTUS, 3)
				.input(Items.BEETROOT)
				.input(Items.CARROT)
				.input(Items.POTATO)
				.criterion("has_item", conditionsFromItem(ModSubtiles.dandelifeon))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.teruTeruBozu)
				.input('C', ModItems.manaweaveCloth)
				.input('S', Items.SUNFLOWER)
				.pattern("C")
				.pattern("C")
				.pattern("S")
				.criterion("has_item", conditionsFromItem(ModItems.manaweaveCloth))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.avatar)
				.input('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.input('W', ModTags.Items.LIVINGWOOD)
				.pattern(" W ")
				.pattern("WDW")
				.pattern("W W")
				.criterion("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.animatedTorch)
				.input('D', ModTags.Items.DUSTS_MANA)
				.input('T', Items.REDSTONE_TORCH)
				.pattern("D")
				.pattern("T")
				.criterion("has_item", conditionsFromItem(Items.REDSTONE_TORCH))
				.criterion("has_alt_item", conditionsFromTag(ModTags.Items.DUSTS_MANA))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.livingwoodTwig)
				.input('W', ModTags.Items.LIVINGWOOD)
				.pattern("W")
				.pattern("W")
				.criterion("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.redstoneRoot)
				.input(Items.REDSTONE)
				.input(Ingredient.ofItems(Items.FERN, Items.GRASS))
				.criterion("has_item", conditionsFromItem(Items.REDSTONE))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.dreamwoodTwig)
				.input('W', ModBlocks.dreamwood)
				.pattern("W")
				.pattern("W")
				.criterion("has_item", conditionsFromItem(ModBlocks.dreamwood))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.gaiaIngot)
				.input('S', ModItems.lifeEssence)
				.input('I', ModTags.Items.INGOTS_TERRASTEEL)
				.pattern(" S ")
				.pattern("SIS")
				.pattern(" S ")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.manaweaveCloth)
				.input('S', ModItems.manaString)
				.pattern("SS")
				.pattern("SS")
				.criterion("has_item", conditionsFromItem(ModItems.manaString))
				.offerTo(consumer);
		Ingredient dyes = Ingredient.ofItems(Items.WHITE_DYE, Items.ORANGE_DYE, Items.MAGENTA_DYE,
				Items.LIGHT_BLUE_DYE, Items.YELLOW_DYE, Items.LIME_DYE, Items.PINK_DYE, Items.GRAY_DYE,
				Items.LIGHT_GRAY_DYE, Items.CYAN_DYE, Items.PURPLE_DYE, Items.BLUE_DYE, Items.BROWN_DYE,
				Items.GREEN_DYE, Items.RED_DYE, Items.BLACK_DYE);
		ShapelessRecipeJsonFactory.create(ModItems.fertilizer)
				.input(Items.BONE_MEAL)
				.input(dyes, 4)
				.criterion("has_item", hasAnyDye)
				.offerTo(consumer, "botania:fertilizer_dye");
		ShapelessRecipeJsonFactory.create(ModItems.drySeeds)
				.input(ModItems.grassSeeds)
				.input(Items.DEAD_BUSH)
				.group("botania:seeds")
				.criterion("has_item", conditionsFromItem(ModItems.grassSeeds))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.goldenSeeds)
				.input(ModItems.grassSeeds)
				.input(Items.WHEAT)
				.group("botania:seeds")
				.criterion("has_item", conditionsFromItem(ModItems.grassSeeds))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.vividSeeds)
				.input(ModItems.grassSeeds)
				.input(Items.GREEN_DYE)
				.group("botania:seeds")
				.criterion("has_item", conditionsFromItem(ModItems.grassSeeds))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.scorchedSeeds)
				.input(ModItems.grassSeeds)
				.input(Items.BLAZE_POWDER)
				.group("botania:seeds")
				.criterion("has_item", conditionsFromItem(ModItems.grassSeeds))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.infusedSeeds)
				.input(ModItems.grassSeeds)
				.input(Items.PRISMARINE_SHARD)
				.group("botania:seeds")
				.criterion("has_item", conditionsFromItem(ModItems.grassSeeds))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.mutatedSeeds)
				.input(ModItems.grassSeeds)
				.input(Items.SPIDER_EYE)
				.group("botania:seeds")
				.criterion("has_item", conditionsFromItem(ModItems.grassSeeds))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.darkQuartz, 8)
				.input('Q', Items.QUARTZ)
				.input('C', Ingredient.ofItems(Items.COAL, Items.CHARCOAL))
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.criterion("has_item", conditionsFromItem(Items.QUARTZ))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.blazeQuartz, 8)
				.input('Q', Items.QUARTZ)
				.input('C', Items.BLAZE_POWDER)
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.criterion("has_item", conditionsFromItem(Items.QUARTZ))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.lavenderQuartz, 8)
				.input('Q', Items.QUARTZ)
				.input('C', Ingredient.ofItems(Items.ALLIUM, Items.PINK_TULIP, Items.LILAC, Items.PEONY))
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.criterion("has_item", conditionsFromItem(Items.QUARTZ))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.redQuartz, 8)
				.input('Q', Items.QUARTZ)
				.input('C', Items.REDSTONE)
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.criterion("has_item", conditionsFromItem(Items.QUARTZ))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.sunnyQuartz, 8)
				.input('Q', Items.QUARTZ)
				.input('C', Items.SUNFLOWER)
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.criterion("has_item", conditionsFromItem(Items.QUARTZ))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.vineBall)
				.input('V', Items.VINE)
				.pattern("VVV")
				.pattern("VVV")
				.pattern("VVV")
				.criterion("has_item", conditionsFromItem(Items.VINE))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.necroVirus)
				.input(ModItems.pixieDust)
				.input(ModItems.vineBall)
				.input(Items.MAGMA_CREAM)
				.input(Items.FERMENTED_SPIDER_EYE)
				.input(Items.ENDER_EYE)
				.input(Items.ZOMBIE_HEAD)
				.criterion("has_item", conditionsFromItem(ModItems.pixieDust))
				.criterion("has_alt_item", conditionsFromItem(Items.ZOMBIE_HEAD))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.nullVirus)
				.input(ModItems.pixieDust)
				.input(ModItems.vineBall)
				.input(Items.MAGMA_CREAM)
				.input(Items.FERMENTED_SPIDER_EYE)
				.input(Items.ENDER_EYE)
				.input(Items.SKELETON_SKULL)
				.criterion("has_item", conditionsFromItem(ModItems.pixieDust))
				.criterion("has_alt_item", conditionsFromItem(Items.SKELETON_SKULL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.spark)
				.input('P', ModTags.Items.PETALS)
				.input('B', Items.BLAZE_POWDER)
				.input('N', Items.GOLD_NUGGET)
				.pattern(" P ")
				.pattern("BNB")
				.pattern(" P ")
				.criterion("has_item", conditionsFromItem(Items.BLAZE_POWDER))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.sparkUpgradeDispersive)
				.input(ModItems.pixieDust)
				.input(ModTags.Items.INGOTS_MANASTEEL)
				.input(ModTags.Items.RUNES_WATER)
				.group("botania:spark_upgrade")
				.criterion("has_item", conditionsFromItem(ModItems.pixieDust))
				.criterion("has_alt_item", conditionsFromItem(ModItems.spark))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.sparkUpgradeDominant)
				.input(ModItems.pixieDust)
				.input(ModTags.Items.INGOTS_MANASTEEL)
				.input(ModTags.Items.RUNES_FIRE)
				.group("botania:spark_upgrade")
				.criterion("has_item", conditionsFromItem(ModItems.pixieDust))
				.criterion("has_alt_item", conditionsFromItem(ModItems.spark))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.sparkUpgradeRecessive)
				.input(ModItems.pixieDust)
				.input(ModTags.Items.INGOTS_MANASTEEL)
				.input(ModTags.Items.RUNES_EARTH)
				.group("botania:spark_upgrade")
				.criterion("has_item", conditionsFromItem(ModItems.pixieDust))
				.criterion("has_alt_item", conditionsFromItem(ModItems.spark))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.sparkUpgradeIsolated)
				.input(ModItems.pixieDust)
				.input(ModTags.Items.INGOTS_MANASTEEL)
				.input(ModTags.Items.RUNES_AIR)
				.group("botania:spark_upgrade")
				.criterion("has_item", conditionsFromItem(ModItems.pixieDust))
				.criterion("has_alt_item", conditionsFromItem(ModItems.spark))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.vial, 3)
				.input('G', ModBlocks.manaGlass)
				.pattern("G G")
				.pattern(" G ")
				.criterion("has_item", conditionsFromItem(ModBlocks.manaGlass))
				.criterion("has_alt_item", conditionsFromItem(ModBlocks.brewery))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.flask, 3)
				.input('G', ModBlocks.elfGlass)
				.pattern("G G")
				.pattern(" G ")
				.criterion("has_item", conditionsFromItem(ModBlocks.elfGlass))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.worldSeed, 4)
				.input('S', Items.WHEAT_SEEDS)
				.input('D', ModTags.Items.GEMS_DRAGONSTONE)
				.input('G', Items.GRASS_BLOCK)
				.pattern("G")
				.pattern("S")
				.pattern("D")
				.criterion("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.thornChakram, 2)
				.input('T', ModTags.Items.INGOTS_TERRASTEEL)
				.input('V', Items.VINE)
				.pattern("VVV")
				.pattern("VTV")
				.pattern("VVV")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.flareChakram, 2)
				.input('P', ModItems.pixieDust)
				.input('B', Items.BLAZE_POWDER)
				.input('C', ModItems.thornChakram)
				.pattern("BBB")
				.pattern("CPC")
				.pattern("BBB")
				.criterion("has_item", conditionsFromItem(ModItems.thornChakram))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.phantomInk, 4)
				.input(ModItems.manaPearl)
				.input(Ingredient.ofItems(
					Arrays.stream(DyeColor.values()).map(DyeItem::byColor).toArray(ItemConvertible[]::new)
				))
				.input(Ingredient.ofItems(Items.GLASS, Items.WHITE_STAINED_GLASS, Items.ORANGE_STAINED_GLASS,
						Items.MAGENTA_STAINED_GLASS, Items.LIGHT_BLUE_STAINED_GLASS, Items.YELLOW_STAINED_GLASS,
						Items.LIME_STAINED_GLASS, Items.PINK_STAINED_GLASS, Items.GRAY_STAINED_GLASS,
						Items.LIGHT_GRAY_STAINED_GLASS, Items.CYAN_STAINED_GLASS, Items.PURPLE_STAINED_GLASS,
						Items.BLUE_STAINED_GLASS, Items.BROWN_STAINED_GLASS, Items.GREEN_STAINED_GLASS,
						Items.RED_STAINED_GLASS, Items.BLACK_STAINED_GLASS))
				.input(Items.GLASS_BOTTLE, 4)
				.criterion("has_item", conditionsFromItem(ModItems.manaPearl))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.keepIvy)
				.input(ModItems.pixieDust)
				.input(Items.VINE)
				.input(ModItems.enderAirBottle)
				.criterion("has_item", conditionsFromItem(ModItems.enderAirBottle))
				.offerTo(consumer);

	}

	private void registerMisc(Consumer<RecipeJsonProvider> consumer) {
		Ingredient mushrooms = Ingredient.ofItems(ModBlocks.whiteMushroom, ModBlocks.orangeMushroom,
				ModBlocks.magentaMushroom, ModBlocks.lightBlueMushroom, ModBlocks.yellowMushroom,
				ModBlocks.limeMushroom, ModBlocks.pinkMushroom, ModBlocks.grayMushroom, ModBlocks.lightGrayMushroom,
				ModBlocks.cyanMushroom, ModBlocks.purpleMushroom, ModBlocks.blueMushroom, ModBlocks.brownMushroom,
				ModBlocks.greenMushroom, ModBlocks.redMushroom, ModBlocks.blackMushroom);
		ShapelessRecipeJsonFactory.create(Items.MUSHROOM_STEW)
				.input(mushrooms, 2)
				.input(Items.BOWL)
				.criterion("has_item", conditionsFromItem(Items.BOWL))
				.criterion("has_orig_recipe", RecipeUnlockedCriterion.create(new Identifier("mushroom_stew")))
				.offerTo(consumer, "botania:mushroom_stew");

		ShapedRecipeJsonFactory.create(Items.COBWEB)
				.input('S', Items.STRING)
				.input('M', ModItems.manaString)
				.pattern("S S")
				.pattern(" M ")
				.pattern("S S")
				.criterion("has_item", conditionsFromItem(ModItems.manaString))
				.offerTo(consumer, prefix("cobweb"));

		ShapedRecipeJsonFactory.create(ModBlocks.defaultAltar)
				.input('P', ModTags.Items.PETALS)
				.input('S', Items.COBBLESTONE_SLAB)
				.input('C', Items.COBBLESTONE)
				.pattern("SPS")
				.pattern(" C ")
				.pattern("CCC")
				.criterion("has_item", conditionsFromTag(ModTags.Items.PETALS))
				.offerTo(consumer);
		for (String metamorphicVariant : LibBlockNames.METAMORPHIC_VARIANTS) {
			Block altar = Registry.BLOCK.getOrEmpty(prefix("apothecary_" + metamorphicVariant.replaceAll("_", ""))).get();
			Block cobble = Registry.BLOCK.getOrEmpty(prefix(LibBlockNames.METAMORPHIC_PREFIX + metamorphicVariant + "_cobblestone")).get();
			ShapedRecipeJsonFactory.create(altar)
					.input('A', ModBlocks.defaultAltar)
					.input('S', cobble)
					.pattern("SSS")
					.pattern("SAS")
					.pattern("SSS")
					.group("botania:metamorphic_apothecary")
					.criterion("has_item", conditionsFromItem(cobble))
					.criterion("has_flower_item", conditionsFromItem(ModSubtiles.marimorphosis))
					.offerTo(consumer);
		}
		for (DyeColor color : DyeColor.values()) {
			ShapelessRecipeJsonFactory.create(ModBlocks.getShinyFlower(color))
					.input(Items.GLOWSTONE_DUST)
					.input(Items.GLOWSTONE_DUST)
					.input(ModBlocks.getFlower(color))
					.group("botania:shiny_flower")
					.criterion("has_item", conditionsFromItem(ModBlocks.getFlower(color)))
					.offerTo(consumer);
			ShapedRecipeJsonFactory.create(ModBlocks.getFloatingFlower(color))
					.input('S', ModItems.grassSeeds)
					.input('D', Items.DIRT)
					.input('F', ModBlocks.getShinyFlower(color))
					.pattern("F")
					.pattern("S")
					.pattern("D")
					.group("botania:floating_flowers")
					.criterion("has_item", conditionsFromItem(ModBlocks.getShinyFlower(color)))
					.offerTo(consumer);
			ShapedRecipeJsonFactory.create(ModBlocks.getPetalBlock(color))
					.input('P', ModItems.getPetal(color))
					.pattern("PPP")
					.pattern("PPP")
					.pattern("PPP")
					.group("botania:petal_block")
					.criterion("has_item", conditionsFromItem(ModItems.getPetal(color)))
					.offerTo(consumer);
			ShapelessRecipeJsonFactory.create(ModBlocks.getMushroom(color))
					.input(Ingredient.ofItems(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM))
					.input(DyeItem.byColor(color))
					.group("botania:mushroom")
					.criterion("has_item", conditionsFromItem(Items.RED_MUSHROOM))
					.criterion("has_alt_item", conditionsFromItem(Items.BROWN_MUSHROOM))
					.offerTo(consumer, "botania:mushroom_" + color.ordinal());
			ShapelessRecipeJsonFactory.create(ModItems.getPetal(color), 4)
					.input(ModBlocks.getDoubleFlower(color))
					.group("botania:petal_double")
					.criterion("has_item", conditionsFromItem(ModBlocks.getDoubleFlower(color)))
					.criterion("has_alt_item", conditionsFromItem(ModItems.getPetal(color)))
					.offerTo(consumer, "botania:petal_" + color.getName() + "_double");
			ShapelessRecipeJsonFactory.create(ModItems.getPetal(color), 2)
					.input(ModBlocks.getFlower(color))
					.group("botania:petal")
					.criterion("has_item", conditionsFromItem(ModBlocks.getFlower(color)))
					.criterion("has_alt_item", conditionsFromItem(ModItems.getPetal(color)))
					.offerTo(consumer, "botania:petal_" + color.getName());
			ShapelessRecipeJsonFactory.create(DyeItem.byColor(color))
					.input(Ingredient.fromTag(ModTags.Items.getPetalTag(color)))
					.input(ModItems.pestleAndMortar)
					.group("botania:dye")
					.criterion("has_item", conditionsFromItem(ModItems.getPetal(color)))
					.offerTo(consumer, "botania:dye_" + color.getName());
		}
	}

	private void registerTools(Consumer<RecipeJsonProvider> consumer) {
		ShapelessRecipeJsonFactory.create(ModItems.lexicon)
				.input(ItemTags.SAPLINGS)
				.input(Items.BOOK)
				.criterion("has_item", conditionsFromTag(ItemTags.SAPLINGS))
				.criterion("has_alt_item", conditionsFromItem(Items.BOOK))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.twigWand)
				.input('P', ModTags.Items.PETALS)
				.input('S', ModItems.livingwoodTwig)
				.pattern(" PS")
				.pattern(" SP")
				.pattern("S  ")
				.group("botania:twig_wand")
				.criterion("has_item", conditionsFromTag(ModTags.Items.PETALS))
				.offerTo(WrapperResult.ofType(TwigWandRecipe.SERIALIZER, consumer));
		ShapedRecipeJsonFactory.create(ModItems.manaTablet)
				.input('P', AccessorIngredient.botania_ofEntries(Stream.of(
						new Ingredient.StackEntry(new ItemStack(ModItems.manaPearl)),
						new Ingredient.TagEntry(ModTags.Items.GEMS_MANA_DIAMOND))))
				.input('S', ModTags.Items.LIVINGROCK)
				.pattern("SSS")
				.pattern("SPS")
				.pattern("SSS")
				.criterion("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.offerTo(consumer);

		ShapedRecipeJsonFactory.create(ModItems.cacophonium)
				.input('N', Items.NOTE_BLOCK)
				.input('G', Items.GOLD_INGOT)
				.pattern(" G ")
				.pattern("GNG")
				.pattern("GG ")
				.criterion("has_item", conditionsFromItem(Items.NOTE_BLOCK))
				.offerTo(consumer);

		ShapedRecipeJsonFactory.create(ModItems.grassHorn)
				.input('S', ModItems.grassSeeds)
				.input('W', ModTags.Items.LIVINGWOOD)
				.pattern(" W ")
				.pattern("WSW")
				.pattern("WW ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.leavesHorn)
				.input(ModItems.grassHorn)
				.input(ItemTags.LEAVES)
				.criterion("has_item", conditionsFromItem(ModItems.grassHorn))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.snowHorn)
				.input(ModItems.grassHorn)
				.input(Items.SNOWBALL)
				.criterion("has_item", conditionsFromItem(ModItems.grassHorn))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.manaMirror)
				.input('P', ModItems.manaPearl)
				.input('R', ModTags.Items.LIVINGROCK)
				.input('S', ModItems.livingwoodTwig)
				.input('T', ModItems.manaTablet)
				.input('I', ModTags.Items.INGOTS_TERRASTEEL)
				.pattern(" PR")
				.pattern(" SI")
				.pattern("T  ")
				.criterion("has_item", conditionsFromItem(ModItems.manaTablet))
				.criterion("has_alt_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.openBucket)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("E E")
				.pattern(" E ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.spawnerMover)
				.input('A', ModItems.enderAirBottle)
				.input('D', ModTags.Items.GEMS_DRAGONSTONE)
				.input('E', ModItems.lifeEssence)
				.input('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("EIE")
				.pattern("ADA")
				.pattern("EIE")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.slingshot)
				.input('A', ModTags.Items.RUNES_AIR)
				.input('T', ModItems.livingwoodTwig)
				.pattern(" TA")
				.pattern(" TT")
				.pattern("T  ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.RUNES_AIR))
				.offerTo(consumer);

		registerSimpleArmorSet(consumer, Ingredient.fromTag(ModTags.Items.INGOTS_MANASTEEL), "manasteel", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL));
		registerSimpleArmorSet(consumer, Ingredient.fromTag(ModTags.Items.INGOTS_ELEMENTIUM), "elementium", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM));
		registerSimpleArmorSet(consumer, Ingredient.ofItems(ModItems.manaweaveCloth), "manaweave", conditionsFromItem(ModItems.manaweaveCloth));

		registerTerrasteelUpgradeRecipe(consumer, ModItems.terrasteelHelm, ModItems.manasteelHelm, ModTags.Items.RUNES_SPRING);
		registerTerrasteelUpgradeRecipe(consumer, ModItems.terrasteelChest, ModItems.manasteelChest, ModTags.Items.RUNES_SUMMER);
		registerTerrasteelUpgradeRecipe(consumer, ModItems.terrasteelLegs, ModItems.manasteelLegs, ModTags.Items.RUNES_AUTUMN);
		registerTerrasteelUpgradeRecipe(consumer, ModItems.terrasteelBoots, ModItems.manasteelBoots, ModTags.Items.RUNES_WINTER);

		registerToolSetRecipes(consumer, Ingredient.fromTag(ModTags.Items.INGOTS_MANASTEEL), Ingredient.ofItems(ModItems.livingwoodTwig),
				conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL), ModItems.manasteelSword, ModItems.manasteelPick, ModItems.manasteelAxe,
				ModItems.manasteelShovel, ModItems.manasteelShears);
		registerToolSetRecipes(consumer, Ingredient.fromTag(ModTags.Items.INGOTS_ELEMENTIUM), Ingredient.ofItems(ModItems.dreamwoodTwig),
				conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM), ModItems.elementiumSword, ModItems.elementiumPick, ModItems.elementiumAxe,
				ModItems.elementiumShovel, ModItems.elementiumShears);

		ShapedRecipeJsonFactory.create(ModItems.terraSword)
				.input('S', ModItems.livingwoodTwig)
				.input('I', ModTags.Items.INGOTS_TERRASTEEL)
				.pattern("I")
				.pattern("I")
				.pattern("S")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.terraPick)
				.input('T', ModItems.manaTablet)
				.input('I', ModTags.Items.INGOTS_TERRASTEEL)
				.input('L', ModItems.livingwoodTwig)
				.pattern("ITI")
				.pattern("ILI")
				.pattern(" L ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.offerTo(WrapperResult.ofType(ManaUpgradeRecipe.SERIALIZER, consumer));
		ShapedRecipeJsonFactory.create(ModItems.terraAxe)
				.input('S', ModItems.livingwoodTwig)
				.input('T', ModTags.Items.INGOTS_TERRASTEEL)
				.input('G', Items.GLOWSTONE)
				.pattern("TTG")
				.pattern("TST")
				.pattern(" S ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.starSword)
				.input('A', ModItems.enderAirBottle)
				.input('D', ModTags.Items.GEMS_DRAGONSTONE)
				.input('T', ModItems.terraSword)
				.input('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("  I")
				.pattern("AD ")
				.pattern("TA ")
				.criterion("has_item", conditionsFromItem(ModItems.terraAxe))
				.criterion("has_terrasteel", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.thunderSword)
				.input('A', ModItems.enderAirBottle)
				.input('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.input('T', ModItems.terraSword)
				.input('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("  I")
				.pattern("AD ")
				.pattern("TA ")
				.criterion("has_item", conditionsFromItem(ModItems.terraAxe))
				.criterion("has_terrasteel", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.glassPick)
				.input('T', ModItems.livingwoodTwig)
				.input('G', Items.GLASS)
				.input('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("GIG")
				.pattern(" T ")
				.pattern(" T ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.livingwoodBow)
				.input('S', ModItems.manaString)
				.input('T', ModItems.livingwoodTwig)
				.pattern(" TS")
				.pattern("T S")
				.pattern(" TS")
				.criterion("has_item", conditionsFromItem(ModItems.manaString))
				.criterion("has_twig", conditionsFromItem(ModItems.livingwoodTwig))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.crystalBow)
				.input('S', ModItems.manaString)
				.input('T', ModItems.livingwoodTwig)
				.input('D', ModTags.Items.GEMS_DRAGONSTONE)
				.pattern(" DS")
				.pattern("T S")
				.pattern(" DS")
				.criterion("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.offerTo(consumer);

		ShapedRecipeJsonFactory.create(ModItems.enderDagger)
				.input('P', ModItems.manaPearl)
				.input('S', ModTags.Items.INGOTS_MANASTEEL)
				.input('T', ModItems.livingwoodTwig)
				.pattern("P")
				.pattern("S")
				.pattern("T")
				.criterion("has_item", conditionsFromItem(ModItems.manaPearl))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.enderHand)
				.input('P', ModItems.manaPearl)
				.input('E', Items.ENDER_CHEST)
				.input('L', Items.LEATHER)
				.input('O', Items.OBSIDIAN)
				.pattern("PLO")
				.pattern("LEL")
				.pattern("OL ")
				.criterion("has_item", conditionsFromItem(Items.ENDER_CHEST))
				.criterion("has_alt_item", conditionsFromItem(Items.ENDER_EYE))
				.offerTo(consumer);

		ShapelessRecipeJsonFactory.create(ModItems.placeholder, 32)
				.input(Items.CRAFTING_TABLE)
				.input(ModTags.Items.LIVINGROCK)
				.criterion("has_dreamwood", conditionsFromItem(ModBlocks.dreamwood))
				.criterion("has_crafty_crate", conditionsFromItem(ModBlocks.craftCrate))
				.offerTo(consumer);

		for (CratePattern pattern : CratePattern.values()) {
			if (pattern == CratePattern.NONE) {
				continue;
			}
			Item item = Registry.ITEM.getOrEmpty(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + pattern.asString().split("_", 2)[1])).get();
			String s = pattern.openSlots.stream().map(bool -> bool ? "R" : "P").collect(Collectors.joining());
			ShapedRecipeJsonFactory.create(item)
					.input('P', ModItems.placeholder)
					.input('R', Items.REDSTONE)
					.pattern(s.substring(0, 3))
					.pattern(s.substring(3, 6))
					.pattern(s.substring(6, 9))
					.group("botania:craft_pattern")
					.criterion("has_item", conditionsFromItem(ModItems.placeholder))
					.criterion("has_crafty_crate", conditionsFromItem(ModBlocks.craftCrate))
					.offerTo(consumer);
		}

		ShapedRecipeJsonFactory.create(ModItems.pestleAndMortar)
				.input('B', Items.BOWL)
				.input('S', Items.STICK)
				.input('W', ItemTags.PLANKS)
				.pattern(" S")
				.pattern("W ")
				.pattern("B ")
				.criterion("has_item", conditionsFromTag(ItemTags.PLANKS))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.manaGun)
				.input('S', ModBlocks.redstoneSpreader)
				.input('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.input('T', Items.TNT)
				.input('W', ModTags.Items.LIVINGWOOD)
				.input('M', ModTags.Items.RUNES_MANA)
				.pattern("SMD")
				.pattern(" WT")
				.pattern("  W")
				.criterion("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.offerTo(consumer);

		ShapedRecipeJsonFactory.create(ModItems.dirtRod)
				.input('D', Items.DIRT)
				.input('T', ModItems.livingwoodTwig)
				.input('E', ModTags.Items.RUNES_EARTH)
				.pattern("  D")
				.pattern(" T ")
				.pattern("E  ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.RUNES_EARTH))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.terraformRod)
				.input('A', ModTags.Items.RUNES_AUTUMN)
				.input('R', ModItems.dirtRod)
				.input('S', ModTags.Items.RUNES_SPRING)
				.input('T', ModTags.Items.INGOTS_TERRASTEEL)
				.input('G', ModItems.grassSeeds)
				.input('W', ModTags.Items.RUNES_WINTER)
				.input('M', ModTags.Items.RUNES_SUMMER)
				.pattern(" WT")
				.pattern("ARS")
				.pattern("GM ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.offerTo(consumer);

		// todo 1.16-fabric fuzzynbt for water bottle
		ShapedRecipeJsonFactory.create(ModItems.waterRod)
				.input('B', Ingredient.ofStacks(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER)))
				.input('R', ModTags.Items.RUNES_WATER)
				.input('T', ModItems.livingwoodTwig)
				.pattern("  B")
				.pattern(" T ")
				.pattern("R  ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.RUNES_WATER))
				.offerTo(consumer);

		ShapedRecipeJsonFactory.create(ModItems.rainbowRod)
				.input('P', ModItems.pixieDust)
				.input('D', ModTags.Items.GEMS_DRAGONSTONE)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern(" PD")
				.pattern(" EP")
				.pattern("E  ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.tornadoRod)
				.input('R', ModTags.Items.RUNES_AIR)
				.input('T', ModItems.livingwoodTwig)
				.input('F', Items.FEATHER)
				.pattern("  F")
				.pattern(" T ")
				.pattern("R  ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.RUNES_AIR))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.fireRod)
				.input('R', ModTags.Items.RUNES_FIRE)
				.input('T', ModItems.livingwoodTwig)
				.input('F', Items.BLAZE_POWDER)
				.pattern("  F")
				.pattern(" T ")
				.pattern("R  ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.RUNES_FIRE))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.skyDirtRod)
				.input(ModItems.dirtRod)
				.input(ModItems.pixieDust)
				.input(ModTags.Items.RUNES_AIR)
				.criterion("has_item", conditionsFromItem(ModItems.pixieDust))
				.offerTo(consumer);

		ShapedRecipeJsonFactory.create(ModItems.diviningRod)
				.input('T', ModItems.livingwoodTwig)
				.input('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.pattern(" TD")
				.pattern(" TT")
				.pattern("T  ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.gravityRod)
				.input('T', ModItems.dreamwoodTwig)
				.input('D', ModTags.Items.GEMS_DRAGONSTONE)
				.input('W', Items.WHEAT)
				.pattern(" TD")
				.pattern(" WT")
				.pattern("T  ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.GEMS_DRAGONSTONE))
				.offerTo(consumer);

		ShapedRecipeJsonFactory.create(ModItems.missileRod)
				.input('D', ModTags.Items.GEMS_DRAGONSTONE)
				.input('T', ModItems.dreamwoodTwig)
				.input('G', ModItems.lifeEssence)
				.pattern("GDD")
				.pattern(" TD")
				.pattern("T G")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.cobbleRod)
				.input('C', Items.COBBLESTONE)
				.input('T', ModItems.livingwoodTwig)
				.input('F', ModTags.Items.RUNES_FIRE)
				.input('W', ModTags.Items.RUNES_WATER)
				.pattern(" FC")
				.pattern(" TW")
				.pattern("T  ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.RUNES_FIRE))
				.criterion("has_alt_item", conditionsFromTag(ModTags.Items.RUNES_WATER))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.smeltRod)
				.input('B', Items.BLAZE_ROD)
				.input('T', ModItems.livingwoodTwig)
				.input('F', ModTags.Items.RUNES_FIRE)
				.pattern(" BF")
				.pattern(" TB")
				.pattern("T  ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.RUNES_FIRE))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.exchangeRod)
				.input('R', ModTags.Items.RUNES_SLOTH)
				.input('S', Items.STONE)
				.input('T', ModItems.livingwoodTwig)
				.pattern(" SR")
				.pattern(" TS")
				.pattern("T  ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.RUNES_SLOTH))
				.offerTo(consumer);

		ShapedRecipeJsonFactory.create(ModItems.laputaShard)
				.input('P', Items.PRISMARINE_CRYSTALS)
				.input('A', ModTags.Items.RUNES_AIR)
				.input('S', ModItems.lifeEssence)
				.input('D', ModTags.Items.GEMS_DRAGONSTONE)
				.input('E', ModTags.Items.RUNES_EARTH)
				.input('F', ModTags.Items.MUNDANE_FLOATING_FLOWERS)
				.pattern("SFS")
				.pattern("PDP")
				.pattern("ASE")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);

		ShapedRecipeJsonFactory.create(ModItems.craftingHalo)
				.input('P', ModItems.manaPearl)
				.input('C', Items.CRAFTING_TABLE)
				.input('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern(" P ")
				.pattern("ICI")
				.pattern(" I ")
				.criterion("has_item", conditionsFromItem(ModItems.manaPearl))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.clip)
				.input('D', ModBlocks.dreamwood)
				.pattern(" D ")
				.pattern("D D")
				.pattern("DD ")
				.criterion("has_item", conditionsFromItem(ModBlocks.dreamwood))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.spellCloth)
				.input('P', ModItems.manaPearl)
				.input('C', ModItems.manaweaveCloth)
				.pattern(" C ")
				.pattern("CPC")
				.pattern(" C ")
				.criterion("has_item", conditionsFromItem(ModItems.manaweaveCloth))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.flowerBag)
				.input('P', ModTags.Items.PETALS)
				.input('W', ItemTags.WOOL)
				.pattern("WPW")
				.pattern("W W")
				.pattern(" W ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.MYSTICAL_FLOWERS))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.poolMinecart)
				.input(Items.MINECART)
				.input(ModBlocks.manaPool)
				.criterion("has_item", conditionsFromItem(Items.MINECART))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.blackHoleTalisman)
				.input('A', ModItems.enderAirBottle)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.input('G', ModItems.lifeEssence)
				.pattern(" G ")
				.pattern("EAE")
				.pattern(" E ")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.temperanceStone)
				.input('R', ModTags.Items.RUNES_EARTH)
				.input('S', Items.STONE)
				.pattern(" S ")
				.pattern("SRS")
				.pattern(" S ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.RUNES_EARTH))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.incenseStick)
				.input('B', Items.BLAZE_POWDER)
				.input('T', ModItems.livingwoodTwig)
				.input('G', Items.GHAST_TEAR)
				.pattern("  G")
				.pattern(" B ")
				.pattern("T  ")
				.criterion("has_item", conditionsFromItem(Items.GHAST_TEAR))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.obedienceStick)
				.input('T', ModItems.livingwoodTwig)
				.input('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("  M")
				.pattern(" T ")
				.pattern("T  ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.slimeBottle)
				.input('S', Items.SLIME_BALL)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.input('G', ModBlocks.elfGlass)
				.pattern("EGE")
				.pattern("ESE")
				.pattern(" E ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.autocraftingHalo)
				.input(ModItems.craftingHalo)
				.input(ModTags.Items.GEMS_MANA_DIAMOND)
				.criterion("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.sextant)
				.input('T', ModItems.livingwoodTwig)
				.input('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern(" TI")
				.pattern(" TT")
				.pattern("III")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.baubleBox)
				.input('C', Items.CHEST)
				.input('G', Items.GOLD_INGOT)
				.input('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern(" M ")
				.pattern("MCG")
				.pattern(" M ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.astrolabe)
				.input('D', ModBlocks.dreamwood)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.input('G', ModItems.lifeEssence)
				.pattern(" EG")
				.pattern("EEE")
				.pattern("GED")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);

	}

	private void registerTrinkets(Consumer<RecipeJsonProvider> consumer) {
		ShapedRecipeJsonFactory.create(ModItems.tinyPlanet)
				.input('P', ModItems.manaPearl)
				.input('S', Items.STONE)
				.input('L', ModTags.Items.LIVINGROCK)
				.pattern("LSL")
				.pattern("SPS")
				.pattern("LSL")
				.criterion("has_item", conditionsFromItem(ModItems.manaPearl))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.manaRing)
				.input('T', ModItems.manaTablet)
				.input('I', ModItems.manaSteel)
				.pattern("TI ")
				.pattern("I I")
				.pattern(" I ")
				.criterion("has_item", conditionsFromItem(ModItems.manaTablet))
				.offerTo(WrapperResult.ofType(ManaUpgradeRecipe.SERIALIZER, consumer));
		ShapedRecipeJsonFactory.create(ModItems.auraRing)
				.input('R', ModTags.Items.RUNES_MANA)
				.input('I', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("RI ")
				.pattern("I I")
				.pattern(" I ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.RUNES_MANA))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.manaRingGreater)
				.input(ModTags.Items.INGOTS_TERRASTEEL)
				.input(ModItems.manaRing)
				.criterion("has_item", conditionsFromItem(ModItems.terrasteel))
				.offerTo(WrapperResult.ofType(ShapelessManaUpgradeRecipe.SERIALIZER, consumer));
		ShapelessRecipeJsonFactory.create(ModItems.auraRingGreater)
				.input(ModTags.Items.INGOTS_TERRASTEEL)
				.input(ModItems.auraRing)
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.travelBelt)
				.input('A', ModTags.Items.RUNES_AIR)
				.input('S', ModTags.Items.INGOTS_MANASTEEL)
				.input('E', ModTags.Items.RUNES_EARTH)
				.input('L', Items.LEATHER)
				.pattern("EL ")
				.pattern("L L")
				.pattern("SLA")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.knockbackBelt)
				.input('A', ModTags.Items.RUNES_FIRE)
				.input('S', ModTags.Items.INGOTS_MANASTEEL)
				.input('E', ModTags.Items.RUNES_EARTH)
				.input('L', Items.LEATHER)
				.pattern("AL ")
				.pattern("L L")
				.pattern("SLE")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.icePendant)
				.input('R', ModTags.Items.RUNES_WATER)
				.input('S', ModItems.manaString)
				.input('W', ModTags.Items.RUNES_WINTER)
				.input('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("WS ")
				.pattern("S S")
				.pattern("MSR")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.lavaPendant)
				.input('S', ModItems.manaString)
				.input('D', ModTags.Items.INGOTS_MANASTEEL)
				.input('F', ModTags.Items.RUNES_FIRE)
				.input('M', ModTags.Items.RUNES_SUMMER)
				.pattern("MS ")
				.pattern("S S")
				.pattern("DSF")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.magnetRing)
				.input('L', ModItems.lensMagnet)
				.input('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("LM ")
				.pattern("M M")
				.pattern(" M ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.waterRing)
				.input('P', Items.PUFFERFISH)
				.input('C', Items.COD)
				.input('H', Items.HEART_OF_THE_SEA)
				.input('W', ModTags.Items.RUNES_WATER)
				.input('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("WMP")
				.pattern("MHM")
				.pattern("CM ")
				.criterion("has_item", conditionsFromItem(Items.HEART_OF_THE_SEA))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.miningRing)
				.input('P', Items.GOLDEN_PICKAXE)
				.input('E', ModTags.Items.RUNES_EARTH)
				.input('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("EMP")
				.pattern("M M")
				.pattern(" M ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.divaCharm)
				.input('P', ModItems.tinyPlanet)
				.input('G', Items.GOLD_INGOT)
				.input('H', ModTags.Items.RUNES_PRIDE)
				.input('L', ModItems.lifeEssence)
				.pattern("LGP")
				.pattern(" HG")
				.pattern(" GL")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.flightTiara)
				.input('E', ModItems.enderAirBottle)
				.input('F', Items.FEATHER)
				.input('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.input('L', ModItems.lifeEssence)
				.pattern("LLL")
				.pattern("ILI")
				.pattern("FEF")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer, "botania:flighttiara_0");

		// Normal quartz and not Tags.Items.QUARTZ because the recipes conflict.
		Item[] items = { Items.QUARTZ, ModItems.darkQuartz, ModItems.manaQuartz, ModItems.blazeQuartz,
				ModItems.lavenderQuartz, ModItems.redQuartz, ModItems.elfQuartz, ModItems.sunnyQuartz };
		for (int i = 0; i < items.length; i++) {
			int tiaraType = i + 1;
			ShapelessRecipeJsonFactory.create(ModItems.flightTiara)
					.input(ModItems.flightTiara)
					.input(items[i])
					.group("botania:flight_tiara_wings")
					.criterion("has_item", conditionsFromItem(ModItems.flightTiara))
					.offerTo(WrapperResult.transformJson(consumer, json -> json.getAsJsonObject("result").addProperty("nbt", "{variant:" + tiaraType + "}")
					), "botania:flighttiara_" + tiaraType);
		}
		ShapedRecipeJsonFactory.create(ModItems.pixieRing)
				.input('D', ModItems.pixieDust)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("DE ")
				.pattern("E E")
				.pattern(" E ")
				.criterion("has_item", conditionsFromItem(ModItems.pixieDust))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.superTravelBelt)
				.input('S', ModItems.travelBelt)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.input('L', ModItems.lifeEssence)
				.pattern("E  ")
				.pattern(" S ")
				.pattern("L E")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.reachRing)
				.input('R', ModTags.Items.RUNES_PRIDE)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.pattern("RE ")
				.pattern("E E")
				.pattern(" E ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.itemFinder)
				.input('E', Items.EMERALD)
				.input('I', Items.IRON_INGOT)
				.input('Y', Items.ENDER_EYE)
				.pattern(" I ")
				.pattern("IYI")
				.pattern("IEI")
				.criterion("has_item", conditionsFromItem(Items.ENDER_EYE))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.superLavaPendant)
				.input('P', ModItems.lavaPendant)
				.input('B', Items.BLAZE_ROD)
				.input('G', ModItems.lifeEssence)
				.input('N', Items.NETHER_BRICK)
				.pattern("BBB")
				.pattern("BPB")
				.pattern("NGN")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.bloodPendant)
				.input('P', Items.PRISMARINE_CRYSTALS)
				.input('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.input('G', Items.GHAST_TEAR)
				.pattern(" P ")
				.pattern("PGP")
				.pattern("DP ")
				.criterion("has_item", conditionsFromItem(Items.GHAST_TEAR))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.holyCloak)
				.input('S', ModItems.lifeEssence)
				.input('W', Items.WHITE_WOOL)
				.input('G', Items.GLOWSTONE_DUST)
				.pattern("WWW")
				.pattern("GWG")
				.pattern("GSG")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.unholyCloak)
				.input('R', Items.REDSTONE)
				.input('S', ModItems.lifeEssence)
				.input('W', Items.BLACK_WOOL)
				.pattern("WWW")
				.pattern("RWR")
				.pattern("RSR")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.balanceCloak)
				.input('R', Items.EMERALD)
				.input('S', ModItems.lifeEssence)
				.input('W', Items.LIGHT_GRAY_WOOL)
				.pattern("WWW")
				.pattern("RWR")
				.pattern("RSR")
				.criterion("has_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.monocle)
				.input('G', ModBlocks.manaGlass)
				.input('I', ModTags.Items.INGOTS_MANASTEEL)
				.input('N', Items.GOLD_NUGGET)
				.pattern("GN")
				.pattern("IN")
				.pattern(" N")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.swapRing)
				.input('C', Items.CLAY)
				.input('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("CM ")
				.pattern("M M")
				.pattern(" M ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.magnetRingGreater)
				.input(ModTags.Items.INGOTS_TERRASTEEL)
				.input(ModItems.magnetRing)
				.criterion("has_item", conditionsFromItem(ModItems.magnetRing))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.speedUpBelt)
				.input('P', ModItems.grassSeeds)
				.input('B', ModItems.travelBelt)
				.input('S', Items.SUGAR)
				.input('M', Items.MAP)
				.pattern(" M ")
				.pattern("PBP")
				.pattern(" S ")
				.criterion("has_item", conditionsFromItem(Items.MAP))
				.criterion("has_alt_item", conditionsFromItem(ModItems.travelBelt))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.dodgeRing)
				.input('R', ModTags.Items.RUNES_AIR)
				.input('E', Items.EMERALD)
				.input('M', ModTags.Items.INGOTS_MANASTEEL)
				.pattern("EM ")
				.pattern("M M")
				.pattern(" MR")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.invisibilityCloak)
				.input('P', ModItems.manaPearl)
				.input('C', Items.PRISMARINE_CRYSTALS)
				.input('W', Items.WHITE_WOOL)
				.input('G', ModBlocks.manaGlass)
				.pattern("CWC")
				.pattern("GWG")
				.pattern("GPG")
				.criterion("has_item", conditionsFromItem(ModItems.manaPearl))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.cloudPendant)
				.input('S', ModItems.manaString)
				.input('D', ModTags.Items.INGOTS_MANASTEEL)
				.input('F', ModTags.Items.RUNES_AIR)
				.input('M', ModTags.Items.RUNES_AUTUMN)
				.pattern("MS ")
				.pattern("S S")
				.pattern("DSF")
				.criterion("has_item", conditionsFromItem(ModItems.manaString))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.superCloudPendant)
				.input('P', ModItems.cloudPendant)
				.input('B', Items.GHAST_TEAR)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.input('G', ModItems.lifeEssence)
				.input('N', Items.WHITE_WOOL)
				.pattern("BEB")
				.pattern("BPB")
				.pattern("NGN")
				.criterion("has_item", conditionsFromItem(ModItems.cloudPendant))
				.criterion("has_alt_item", conditionsFromItem(ModItems.lifeEssence))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.thirdEye)
				.input('Q', Items.QUARTZ_BLOCK)
				.input('R', Items.GOLDEN_CARROT)
				.input('S', ModTags.Items.RUNES_EARTH)
				.input('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.input('E', Items.ENDER_EYE)
				.pattern("RSR")
				.pattern("QEQ")
				.pattern("RDR")
				.criterion("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.goddessCharm)
				.input('P', ModTags.Items.PETALS_PINK)
				.input('A', ModTags.Items.RUNES_WATER)
				.input('S', ModTags.Items.RUNES_SPRING)
				.input('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.pattern(" P ")
				.pattern(" P ")
				.pattern("ADS")
				.criterion("has_item", conditionsFromTag(ModTags.Items.GEMS_MANA_DIAMOND))
				.offerTo(consumer);

	}

	private void registerCorporeaAndRedString(Consumer<RecipeJsonProvider> consumer) {
		ShapelessRecipeJsonFactory.create(ModItems.redString)
				.input(Items.STRING)
				.input(Items.REDSTONE_BLOCK)
				.input(ModItems.pixieDust)
				.input(ModItems.enderAirBottle)
				.group("botania:red_string")
				.criterion("has_item", conditionsFromItem(ModItems.enderAirBottle))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.redString)
				.input(Items.STRING)
				.input(Items.REDSTONE_BLOCK)
				.input(ModItems.pixieDust)
				.input(ModItems.enderAirBottle)
				.input(Items.PUMPKIN)
				.group("botania:red_string")
				.criterion("has_item", conditionsFromItem(ModItems.enderAirBottle))
				.offerTo(consumer, "botania:red_string_alt");
		registerRedStringBlock(consumer, ModBlocks.redStringContainer, Ingredient.ofItems(Items.CHEST), conditionsFromItem(Items.CHEST));
		registerRedStringBlock(consumer, ModBlocks.redStringDispenser, Ingredient.ofItems(Items.DISPENSER), conditionsFromItem(Items.DISPENSER));
		registerRedStringBlock(consumer, ModBlocks.redStringFertilizer, Ingredient.ofItems(ModItems.fertilizer), conditionsFromItem(ModItems.fertilizer));
		registerRedStringBlock(consumer, ModBlocks.redStringComparator, Ingredient.ofItems(Items.COMPARATOR), conditionsFromItem(Items.COMPARATOR));
		registerRedStringBlock(consumer, ModBlocks.redStringRelay, Ingredient.ofItems(ModBlocks.manaSpreader), conditionsFromItem(ModBlocks.manaSpreader));
		registerRedStringBlock(consumer, ModBlocks.redStringInterceptor, Ingredient.ofItems(Items.STONE_BUTTON), conditionsFromItem(Items.STONE_BUTTON));
		ShapelessRecipeJsonFactory.create(ModItems.corporeaSpark)
				.input(ModItems.spark)
				.input(ModItems.pixieDust)
				.input(ModItems.enderAirBottle)
				.criterion("has_item", conditionsFromItem(ModItems.enderAirBottle))
				.criterion("has_alt_item", conditionsFromItem(ModItems.pixieDust))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.corporeaSparkMaster)
				.input(ModItems.corporeaSpark)
				.input(ModTags.Items.GEMS_DRAGONSTONE)
				.criterion("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.corporeaIndex)
				.input('A', ModItems.enderAirBottle)
				.input('S', ModItems.corporeaSpark)
				.input('D', ModTags.Items.GEMS_DRAGONSTONE)
				.input('O', Items.OBSIDIAN)
				.pattern("AOA")
				.pattern("OSO")
				.pattern("DOD")
				.criterion("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.corporeaFunnel)
				.input(Items.DROPPER)
				.input(ModItems.corporeaSpark)
				.criterion("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.corporeaInterceptor)
				.input(Items.REDSTONE_BLOCK)
				.input(ModItems.corporeaSpark)
				.criterion("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.corporeaCrystalCube)
				.input('C', ModItems.corporeaSpark)
				.input('G', ModBlocks.elfGlass)
				.input('W', ModBlocks.dreamwood)
				.pattern("C")
				.pattern("G")
				.pattern("W")
				.criterion("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.corporeaRetainer)
				.input(Items.CHEST)
				.input(ModItems.corporeaSpark)
				.criterion("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.corporeaBlock, 8)
				.input(ModBlocks.livingrockBrick)
				.input(ModItems.corporeaSpark)
				.criterion("has_item", conditionsFromItem(ModItems.corporeaSpark))
				.offerTo(consumer);
		slabShape(ModBlocks.corporeaSlab, ModBlocks.corporeaBlock).offerTo(consumer);
		stairs(ModBlocks.corporeaStairs, ModBlocks.corporeaBlock).offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.corporeaBrick, 4)
				.input('R', ModBlocks.corporeaBlock)
				.pattern("RR")
				.pattern("RR")
				.criterion("has_item", conditionsFromItem(ModBlocks.corporeaBlock))
				.offerTo(consumer);
		slabShape(ModBlocks.corporeaBrickSlab, ModBlocks.corporeaBrick).offerTo(consumer);
		stairs(ModBlocks.corporeaBrickStairs, ModBlocks.corporeaBrick).offerTo(consumer);
		wallShape(ModBlocks.corporeaBrickWall, ModBlocks.corporeaBrick, 6).offerTo(consumer);
	}

	private void registerLenses(Consumer<RecipeJsonProvider> consumer) {
		ShapedRecipeJsonFactory.create(ModItems.lensNormal)
				.input('S', ModTags.Items.INGOTS_MANASTEEL)
				.input('G', Ingredient.ofItems(Items.GLASS, Items.GLASS_PANE))
				.pattern(" S ")
				.pattern("SGS")
				.pattern(" S ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_MANASTEEL))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensSpeed)
				.input(ModItems.lensNormal)
				.input(ModTags.Items.RUNES_AIR)
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensPower)
				.input(ModItems.lensNormal)
				.input(ModTags.Items.RUNES_FIRE)
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensTime)
				.input(ModItems.lensNormal)
				.input(ModTags.Items.RUNES_EARTH)
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensEfficiency)
				.input(ModItems.lensNormal)
				.input(ModTags.Items.RUNES_WATER)
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensBounce)
				.input(ModItems.lensNormal)
				.input(ModTags.Items.RUNES_SUMMER)
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensGravity)
				.input(ModItems.lensNormal)
				.input(ModTags.Items.RUNES_WINTER)
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.lensMine)
				.input('P', Items.PISTON)
				.input('A', Items.LAPIS_LAZULI)
				.input('R', Items.REDSTONE)
				.input('L', ModItems.lensNormal)
				.pattern(" P ")
				.pattern("ALA")
				.pattern(" R ")
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensDamage)
				.input(ModItems.lensNormal)
				.input(ModTags.Items.RUNES_WRATH)
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensPhantom)
				.input(ModItems.lensNormal)
				.input(ModBlocks.abstrusePlatform)
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensMagnet)
				.input(ModItems.lensNormal)
				.input(Items.IRON_INGOT)
				.input(Items.GOLD_INGOT)
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensExplosive)
				.input(ModItems.lensNormal)
				.input(ModTags.Items.RUNES_ENVY)
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.lensInfluence)
				.input('P', Items.PRISMARINE_CRYSTALS)
				.input('R', ModTags.Items.RUNES_AIR)
				.input('L', ModItems.lensNormal)
				.pattern("PRP")
				.pattern("PLP")
				.pattern("PPP")
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.lensWeight)
				.input('P', Items.PRISMARINE_CRYSTALS)
				.input('R', ModTags.Items.RUNES_WATER)
				.input('L', ModItems.lensNormal)
				.pattern("PPP")
				.pattern("PLP")
				.pattern("PRP")
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.lensPaint)
				.input('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.input('W', ItemTags.WOOL)
				.input('L', ModItems.lensNormal)
				.pattern(" E ")
				.pattern("WLW")
				.pattern(" E ")
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensFire)
				.input(ModItems.lensNormal)
				.input(Items.FIRE_CHARGE)
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensPiston)
				.input(ModItems.lensNormal)
				.input(ModBlocks.pistonRelay)
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.lensLight)
				.input('F', Items.FIRE_CHARGE)
				.input('G', Items.GLOWSTONE)
				.input('L', ModItems.lensNormal)
				.pattern("GFG")
				.pattern("FLF")
				.pattern("GFG")
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModItems.lensLight)
				.input('F', Items.FIRE_CHARGE)
				.input('G', Items.GLOWSTONE)
				.input('L', ModItems.lensNormal)
				.pattern("FGF")
				.pattern("GLG")
				.pattern("FGF")
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer, "botania:lens_light_alt");
		ShapedRecipeJsonFactory.create(ModItems.lensMessenger)
				.input('P', Items.PAPER)
				.input('L', ModItems.lensNormal)
				.pattern(" P ")
				.pattern("PLP")
				.pattern(" P ")
				.criterion("has_item", conditionsFromItem(ModItems.lensNormal))
				.offerTo(consumer);

		ShapelessRecipeJsonFactory.create(ModItems.lensWarp)
				.input(ModItems.lensNormal)
				.input(ModItems.pixieDust)
				.criterion("has_item", conditionsFromItem(ModItems.pixieDust))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensRedirect)
				.input(ModItems.lensNormal)
				.input(ModTags.Items.LIVINGWOOD)
				.input(ModTags.Items.INGOTS_ELEMENTIUM)
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensFirework)
				.input(ModItems.lensNormal)
				.input(Items.FIREWORK_ROCKET)
				.input(ModTags.Items.INGOTS_ELEMENTIUM)
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensFlare)
				.input(ModItems.lensNormal)
				.input(ModBlocks.elfGlass)
				.input(ModTags.Items.INGOTS_ELEMENTIUM)
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModItems.lensTripwire)
				.input(ModItems.lensNormal)
				.input(Items.TRIPWIRE_HOOK)
				.input(ModTags.Items.INGOTS_ELEMENTIUM)
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_ELEMENTIUM))
				.offerTo(consumer);
	}

	private void registerFloatingFlowers(Consumer<RecipeJsonProvider> consumer) {
		for (Block block : new Block[] {
				ModSubtiles.pureDaisy, ModSubtiles.manastar, ModSubtiles.hydroangeas, ModSubtiles.endoflame,
				ModSubtiles.thermalily, ModSubtiles.rosaArcana, ModSubtiles.munchdew, ModSubtiles.entropinnyum,
				ModSubtiles.kekimurus, ModSubtiles.gourmaryllis, ModSubtiles.narslimmus, ModSubtiles.spectrolus,
				ModSubtiles.dandelifeon, ModSubtiles.rafflowsia, ModSubtiles.shulkMeNot, ModSubtiles.bellethorn,
				ModSubtiles.bellethornChibi, ModSubtiles.bergamute, ModSubtiles.dreadthorn, ModSubtiles.heiseiDream,
				ModSubtiles.tigerseye, ModSubtiles.jadedAmaranthus, ModSubtiles.orechid, ModSubtiles.fallenKanade,
				ModSubtiles.exoflame, ModSubtiles.agricarnation, ModSubtiles.agricarnationChibi, ModSubtiles.hopperhock,
				ModSubtiles.hopperhockChibi, ModSubtiles.tangleberrie, ModSubtiles.jiyuulia, ModSubtiles.rannuncarpus,
				ModSubtiles.rannuncarpusChibi, ModSubtiles.hyacidus, ModSubtiles.pollidisiac, ModSubtiles.clayconia,
				ModSubtiles.clayconiaChibi, ModSubtiles.loonium, ModSubtiles.daffomill, ModSubtiles.vinculotus,
				ModSubtiles.spectranthemum, ModSubtiles.medumone, ModSubtiles.marimorphosis, ModSubtiles.marimorphosisChibi,
				ModSubtiles.bubbell, ModSubtiles.bubbellChibi, ModSubtiles.solegnolia, ModSubtiles.solegnoliaChibi,
				ModSubtiles.orechidIgnem }) {
			createFloatingFlowerRecipe(consumer, block);
		}
	}

	private void registerConversions(Consumer<RecipeJsonProvider> consumer) {
		compression(ModItems.manaSteel, ModTags.Items.NUGGETS_MANASTEEL)
				.offerTo(consumer, prefix("conversions/manasteel_from_nuggets"));
		compression(ModItems.elementium, ModTags.Items.NUGGETS_ELEMENTIUM)
				.offerTo(consumer, prefix("conversions/elementium_from_nuggets"));
		compression(ModItems.terrasteel, ModTags.Items.NUGGETS_TERRASTEEL)
				.offerTo(consumer, prefix("conversions/terrasteel_from_nugget"));
		compression(ModBlocks.manasteelBlock, ModTags.Items.INGOTS_MANASTEEL).offerTo(consumer);
		compression(ModBlocks.terrasteelBlock, ModTags.Items.INGOTS_TERRASTEEL).offerTo(consumer);
		compression(ModBlocks.elementiumBlock, ModTags.Items.INGOTS_ELEMENTIUM).offerTo(consumer);
		compression(ModBlocks.manaDiamondBlock, ModTags.Items.GEMS_MANA_DIAMOND).offerTo(consumer);
		compression(ModBlocks.dragonstoneBlock, ModTags.Items.GEMS_DRAGONSTONE).offerTo(consumer);
		compression(ModBlocks.blazeBlock, Items.BLAZE_ROD).offerTo(consumer);

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

		deconstruct(consumer, Items.BLAZE_ROD, ModBlocks.blazeBlock, "blazeblock_deconstruct");
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

	private void registerDecor(Consumer<RecipeJsonProvider> consumer) {
		ShapedRecipeJsonFactory.create(ModBlocks.livingrockBrick, 4)
				.input('R', ModTags.Items.LIVINGROCK)
				.pattern("RR")
				.pattern("RR")
				.criterion("has_item", conditionsFromTag(ModTags.Items.LIVINGROCK))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.livingrockBrickChiseled, 4)
				.input('R', ModBlocks.livingrockBrick)
				.pattern("RR")
				.pattern("RR")
				.criterion("has_item", conditionsFromItem(ModBlocks.livingrockBrick))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.livingrockBrickCracked, 2)
				.input(ModBlocks.livingrockBrick)
				.input(Items.COBBLESTONE)
				.criterion("has_item", conditionsFromItem(ModBlocks.livingrockBrick))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.livingrockBrickMossy)
				.input(ModBlocks.livingrockBrick)
				.input(Items.WHEAT_SEEDS)
				.criterion("has_item", conditionsFromItem(ModBlocks.livingrockBrick))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.livingwoodPlanksMossy)
				.input(ModBlocks.livingwoodPlanks)
				.input(Items.WHEAT_SEEDS)
				.criterion("has_item", conditionsFromItem(ModBlocks.livingwoodPlanks))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.livingwoodFramed, 4)
				.input('W', ModBlocks.livingwoodPlanks)
				.pattern("WW")
				.pattern("WW")
				.criterion("has_item", conditionsFromItem(ModBlocks.livingwoodPlanks))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.livingwoodGlimmering)
				.input(ModTags.Items.LIVINGWOOD)
				.input(Items.GLOWSTONE_DUST)
				.criterion("has_item", conditionsFromTag(ModTags.Items.LIVINGWOOD))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.dreamwoodPlanksMossy)
				.input(ModBlocks.dreamwoodPlanks)
				.input(Items.WHEAT_SEEDS)
				.criterion("has_item", conditionsFromItem(ModBlocks.dreamwoodPlanks))
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(ModBlocks.dreamwoodFramed, 4)
				.input('W', ModBlocks.dreamwoodPlanks)
				.pattern("WW")
				.pattern("WW")
				.criterion("has_item", conditionsFromItem(ModBlocks.dreamwoodPlanks))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.dreamwoodGlimmering)
				.input(ModBlocks.dreamwood)
				.input(Items.GLOWSTONE_DUST)
				.criterion("has_item", conditionsFromItem(ModBlocks.dreamwood))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.shimmerrock)
				.input(ModTags.Items.LIVINGROCK)
				.input(ModBlocks.bifrostPerm)
				.criterion("has_item", conditionsFromItem(ModBlocks.bifrostPerm))
				.criterion("has_alt_item", conditionsFromItem(ModItems.rainbowRod))
				.offerTo(consumer);
		ShapelessRecipeJsonFactory.create(ModBlocks.shimmerwoodPlanks)
				.input(ModBlocks.dreamwoodPlanks)
				.input(ModBlocks.bifrostPerm)
				.criterion("has_item", conditionsFromItem(ModBlocks.bifrostPerm))
				.criterion("has_alt_item", conditionsFromItem(ModItems.rainbowRod))
				.offerTo(consumer);

		registerForQuartz(consumer, LibBlockNames.QUARTZ_DARK, ModItems.darkQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_MANA, ModItems.manaQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_BLAZE, ModItems.blazeQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_LAVENDER, ModItems.lavenderQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_RED, ModItems.redQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_ELF, ModItems.elfQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_SUNNY, ModItems.sunnyQuartz);

		registerForWood(consumer, LibBlockNames.LIVING_WOOD);
		registerForWood(consumer, LibBlockNames.DREAM_WOOD);

		stairs(ModFluffBlocks.livingrockStairs, ModBlocks.livingrock).offerTo(consumer);
		slabShape(ModFluffBlocks.livingrockSlab, ModBlocks.livingrock).offerTo(consumer);
		wallShape(ModFluffBlocks.livingrockWall, ModBlocks.livingrock, 6).offerTo(consumer);

		stairs(ModFluffBlocks.livingrockBrickStairs, ModBlocks.livingrockBrick).offerTo(consumer);
		slabShape(ModFluffBlocks.livingrockBrickSlab, ModBlocks.livingrockBrick).offerTo(consumer);
		stairs(ModFluffBlocks.shimmerrockStairs, ModBlocks.shimmerrock).offerTo(consumer);
		slabShape(ModFluffBlocks.shimmerrockSlab, ModBlocks.shimmerrock).offerTo(consumer);
		stairs(ModFluffBlocks.livingwoodPlankStairs, ModBlocks.livingwoodPlanks).offerTo(consumer);
		slabShape(ModFluffBlocks.livingwoodPlankSlab, ModBlocks.livingwoodPlanks).offerTo(consumer);
		stairs(ModFluffBlocks.dreamwoodPlankStairs, ModBlocks.dreamwoodPlanks).offerTo(consumer);
		slabShape(ModFluffBlocks.dreamwoodPlankSlab, ModBlocks.dreamwoodPlanks).offerTo(consumer);
		stairs(ModFluffBlocks.shimmerwoodPlankStairs, ModBlocks.shimmerwoodPlanks).offerTo(consumer);
		slabShape(ModFluffBlocks.shimmerwoodPlankSlab, ModBlocks.shimmerwoodPlanks).offerTo(consumer);

		ringShape(ModBlocks.livingwoodPatternFramed, ModBlocks.livingwoodPlanks).offerTo(consumer);
		ringShape(ModBlocks.dreamwoodPatternFramed, ModBlocks.dreamwoodPlanks).offerTo(consumer);

		for (String variant : LibBlockNames.METAMORPHIC_VARIANTS) {
			registerForMetamorphic(consumer, variant);
		}

		Item[] pavementIngredients = { Items.AIR, Items.COAL, Items.LAPIS_LAZULI, Items.REDSTONE, Items.WHEAT, Items.SLIME_BALL };
		for (int i = 0; i < pavementIngredients.length; i++) {
			registerForPavement(consumer, LibBlockNames.PAVEMENT_VARIANTS[i], pavementIngredients[i]);
		}

		wallShape(ModFluffBlocks.managlassPane, ModBlocks.manaGlass, 16).offerTo(consumer);
		wallShape(ModFluffBlocks.alfglassPane, ModBlocks.elfGlass, 16).offerTo(consumer);
		wallShape(ModFluffBlocks.bifrostPane, ModBlocks.bifrostPerm, 16).offerTo(consumer);

		ShapelessRecipeJsonFactory.create(ModBlocks.azulejo0)
				.input(Items.BLUE_DYE)
				.input(ModTags.Items.BLOCKS_QUARTZ)
				.criterion("has_item", conditionsFromItem(Items.BLUE_DYE))
				.offerTo(consumer);

		List<Item> allAzulejos = IntStream.range(0, 16).mapToObj(i -> "azulejo_" + i)
				.map(ResourceLocationHelper::prefix)
				.map(Registry.ITEM::getOrEmpty)
				.map(Optional::get)
				.collect(Collectors.toList());
		for (int i = 0; i < allAzulejos.size(); i++) {
			int resultIndex = i + 1 == allAzulejos.size() ? 0 : i + 1;
			String recipeName = "azulejo_" + resultIndex;
			if (resultIndex == 0) {
				recipeName += "_alt";
			}
			ShapelessRecipeJsonFactory.create(allAzulejos.get(resultIndex))
					.input(allAzulejos.get(i))
					.criterion("has_azulejo", conditionsFromItem(ModBlocks.azulejo0))
					.group("botania:azulejo_cycling")
					.offerTo(consumer, prefix(recipeName));
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

	private void registerSimpleArmorSet(Consumer<RecipeJsonProvider> consumer, Ingredient item, String variant,
			CriterionConditions criterion) {
		Item helmet = Registry.ITEM.getOrEmpty(prefix(variant + "_helmet")).get();
		Item chestplate = Registry.ITEM.getOrEmpty(prefix(variant + "_chestplate")).get();
		Item leggings = Registry.ITEM.getOrEmpty(prefix(variant + "_leggings")).get();
		Item boots = Registry.ITEM.getOrEmpty(prefix(variant + "_boots")).get();
		ShapedRecipeJsonFactory.create(helmet)
				.input('S', item)
				.pattern("SSS")
				.pattern("S S")
				.criterion("has_item", criterion)
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(chestplate)
				.input('S', item)
				.pattern("S S")
				.pattern("SSS")
				.pattern("SSS")
				.criterion("has_item", criterion)
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(leggings)
				.input('S', item)
				.pattern("SSS")
				.pattern("S S")
				.pattern("S S")
				.criterion("has_item", criterion)
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(boots)
				.input('S', item)
				.pattern("S S")
				.pattern("S S")
				.criterion("has_item", criterion)
				.offerTo(consumer);
	}

	private void registerToolSetRecipes(Consumer<RecipeJsonProvider> consumer, Ingredient item, Ingredient stick,
			CriterionConditions criterion, ItemConvertible sword, ItemConvertible pickaxe,
			ItemConvertible axe, ItemConvertible shovel, ItemConvertible shears) {
		ShapedRecipeJsonFactory.create(pickaxe)
				.input('S', item)
				.input('T', stick)
				.pattern("SSS")
				.pattern(" T ")
				.pattern(" T ")
				.criterion("has_item", criterion)
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(shovel)
				.input('S', item)
				.input('T', stick)
				.pattern("S")
				.pattern("T")
				.pattern("T")
				.criterion("has_item", criterion)
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(axe)
				.input('S', item)
				.input('T', stick)
				.pattern("SS")
				.pattern("TS")
				.pattern("T ")
				.criterion("has_item", criterion)
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(sword)
				.input('S', item)
				.input('T', stick)
				.pattern("S")
				.pattern("S")
				.pattern("T")
				.criterion("has_item", criterion)
				.offerTo(consumer);
		ShapedRecipeJsonFactory.create(shears)
				.input('S', item)
				.pattern("S ")
				.pattern(" S")
				.criterion("has_item", criterion)
				.offerTo(consumer);

	}

	private void registerTerrasteelUpgradeRecipe(Consumer<RecipeJsonProvider> consumer, ItemConvertible output,
			ItemConvertible upgradedInput, Tag<Item> runeInput) {
		ShapedRecipeJsonFactory.create(output)
				.input('T', ModItems.livingwoodTwig)
				.input('S', ModTags.Items.INGOTS_TERRASTEEL)
				.input('R', runeInput)
				.input('A', upgradedInput)
				.pattern("TRT")
				.pattern("SAS")
				.pattern(" S ")
				.criterion("has_item", conditionsFromTag(ModTags.Items.INGOTS_TERRASTEEL))
				.criterion("has_prev_tier", conditionsFromItem(upgradedInput))
				.offerTo(WrapperResult.ofType(ArmorUpgradeRecipe.SERIALIZER, consumer));
	}

	private void registerRedStringBlock(Consumer<RecipeJsonProvider> consumer, ItemConvertible output, Ingredient input, CriterionConditions criterion) {
		ShapedRecipeJsonFactory.create(output)
				.input('R', ModTags.Items.LIVINGROCK)
				.input('S', ModItems.redString)
				.input('M', input)
				.pattern("RRR")
				.pattern("RMS")
				.pattern("RRR")
				.criterion("has_item", conditionsFromItem(ModItems.redString))
				.criterion("has_base_block", criterion)
				.offerTo(consumer);
	}

	private void createFloatingFlowerRecipe(Consumer<RecipeJsonProvider> consumer, ItemConvertible input) {
		Identifier inputName = Registry.ITEM.getId(input.asItem());
		Item output = Registry.ITEM.getOrEmpty(new Identifier(inputName.getNamespace(), "floating_" + inputName.getPath())).get();
		ShapelessRecipeJsonFactory.create(output)
				.input(ModTags.Items.FLOATING_FLOWERS)
				.input(input)
				.criterion("has_item", conditionsFromItem(input))
				.offerTo(consumer);
	}

	private void deconstruct(Consumer<RecipeJsonProvider> consumer, ItemConvertible output, ItemConvertible input, String name) {
		ShapelessRecipeJsonFactory.create(output, 9)
				.criterion("has_item", conditionsFromItem(output))
				.input(input)
				.offerTo(consumer, prefix("conversions/" + name));
	}

	private void deconstruct(Consumer<RecipeJsonProvider> consumer, ItemConvertible output, Tag<Item> input, String name) {
		ShapelessRecipeJsonFactory.create(output, 9)
				.criterion("has_item", conditionsFromItem(output))
				.input(input)
				.offerTo(consumer, prefix("conversions/" + name));
	}

	private void deconstructPetalBlock(Consumer<RecipeJsonProvider> consumer, ItemConvertible output, ItemConvertible input) {
		ShapelessRecipeJsonFactory.create(output, 9)
				.criterion("has_item", conditionsFromItem(output))
				.input(input).group("botania:petal_block_deconstruct")
				.offerTo(consumer, prefix("conversions/" + Registry.ITEM.getId(input.asItem()).getPath() + "_deconstruct"));
	}

	private void recombineSlab(Consumer<RecipeJsonProvider> consumer, ItemConvertible fullBlock, ItemConvertible slab) {
		ShapedRecipeJsonFactory.create(fullBlock)
				.input('Q', slab)
				.pattern("Q")
				.pattern("Q")
				.criterion("has_item", conditionsFromItem(fullBlock))
				.offerTo(consumer, prefix("slab_recombine/" + Registry.ITEM.getId(fullBlock.asItem()).getPath()));
	}

	private void registerForQuartz(Consumer<RecipeJsonProvider> consumer, String variant, ItemConvertible baseItem) {
		Block base = Registry.BLOCK.getOrEmpty(prefix(variant)).get();
		Block slab = Registry.BLOCK.getOrEmpty(prefix(variant + LibBlockNames.SLAB_SUFFIX)).get();
		Block stairs = Registry.BLOCK.getOrEmpty(prefix(variant + LibBlockNames.STAIR_SUFFIX)).get();
		Block chiseled = Registry.BLOCK.getOrEmpty(prefix("chiseled_" + variant)).get();
		Block pillar = Registry.BLOCK.getOrEmpty(prefix(variant + "_pillar")).get();

		ShapedRecipeJsonFactory.create(base)
				.input('Q', baseItem)
				.pattern("QQ")
				.pattern("QQ")
				.criterion("has_item", conditionsFromItem(baseItem))
				.offerTo(consumer);
		stairs(stairs, base).offerTo(consumer);
		slabShape(slab, base).offerTo(consumer);
		pillar(pillar, base).offerTo(consumer);
		chiseled(chiseled, slab).criterion("has_base_item", conditionsFromItem(base)).offerTo(consumer);
	}

	private void registerForWood(Consumer<RecipeJsonProvider> consumer, String variant) {
		Block base = Registry.BLOCK.getOrEmpty(prefix(variant)).get();
		Block planks = Registry.BLOCK.getOrEmpty(prefix(variant + "_planks")).get();
		Block slab = Registry.BLOCK.getOrEmpty(prefix(variant + LibBlockNames.SLAB_SUFFIX)).get();
		Block stairs = Registry.BLOCK.getOrEmpty(prefix(variant + LibBlockNames.STAIR_SUFFIX)).get();
		Block wall = Registry.BLOCK.getOrEmpty(prefix(variant + LibBlockNames.WALL_SUFFIX)).get();
		Block fence = Registry.BLOCK.getOrEmpty(prefix(variant + LibBlockNames.FENCE_SUFFIX)).get();
		Block fenceGate = Registry.BLOCK.getOrEmpty(prefix(variant + LibBlockNames.FENCE_GATE_SUFFIX)).get();

		ShapelessRecipeJsonFactory.create(planks, 4).input(base)
				.criterion("has_item", conditionsFromItem(base)).offerTo(consumer);
		stairs(stairs, base).offerTo(consumer);
		slabShape(slab, base).offerTo(consumer);
		wallShape(wall, base, 6).offerTo(consumer);
		fence(fence, planks).offerTo(consumer);
		fenceGate(fenceGate, planks).offerTo(consumer);
	}

	private void registerForPavement(Consumer<RecipeJsonProvider> consumer, String color, @Nullable Item mainInput) {
		String baseName = color + LibBlockNames.PAVEMENT_SUFFIX;
		Block base = Registry.BLOCK.getOrEmpty(prefix(baseName)).get();
		Block stair = Registry.BLOCK.getOrEmpty(prefix(baseName + LibBlockNames.STAIR_SUFFIX)).get();
		Block slab = Registry.BLOCK.getOrEmpty(prefix(baseName + LibBlockNames.SLAB_SUFFIX)).get();

		ShapelessRecipeJsonFactory builder = ShapelessRecipeJsonFactory.create(base, 3)
				.input(ModTags.Items.LIVINGROCK)
				.input(Items.COBBLESTONE)
				.input(Items.GRAVEL)
				.group("botania:pavement")
				.criterion("has_item", conditionsFromTag(ModTags.Items.LIVINGROCK));
		if (mainInput != Items.AIR) {
			builder.input(mainInput);
		}
		builder.offerTo(consumer);

		slabShape(slab, base).group("botania:pavement_slab").offerTo(consumer);
		stairs(stair, base).group("botania:pavement_stairs").offerTo(consumer);
	}

	private void registerForMetamorphic(Consumer<RecipeJsonProvider> consumer, String variant) {
		Block base = Registry.BLOCK.getOrEmpty(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone")).get();
		Block slab = Registry.BLOCK.getOrEmpty(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.SLAB_SUFFIX)).get();
		Block stair = Registry.BLOCK.getOrEmpty(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.STAIR_SUFFIX)).get();
		Block brick = Registry.BLOCK.getOrEmpty(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks")).get();
		Block brickSlab = Registry.BLOCK.getOrEmpty(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.SLAB_SUFFIX)).get();
		Block brickStair = Registry.BLOCK.getOrEmpty(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.STAIR_SUFFIX)).get();
		Block chiseledBrick = Registry.BLOCK.getOrEmpty(prefix("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks")).get();
		Block cobble = Registry.BLOCK.getOrEmpty(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone")).get();
		Block cobbleSlab = Registry.BLOCK.getOrEmpty(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.SLAB_SUFFIX)).get();
		Block cobbleStair = Registry.BLOCK.getOrEmpty(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.STAIR_SUFFIX)).get();
		Block cobbleWall = Registry.BLOCK.getOrEmpty(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.WALL_SUFFIX)).get();

		InventoryChangedCriterion.Conditions marimorphosis = conditionsFromItem(ModSubtiles.marimorphosis);
		slabShape(slab, base).group("botania:metamorphic_stone_slab")
				.criterion("has_flower_item", marimorphosis).offerTo(consumer);
		stairs(stair, base).group("botania:metamorphic_stone_stairs")
				.criterion("has_flower_item", marimorphosis).offerTo(consumer);

		brick(brick, base).group("botania:metamorphic_brick")
				.criterion("has_flower_item", marimorphosis).offerTo(consumer);
		slabShape(brickSlab, brick).group("botania:metamorphic_brick_slab")
				.criterion("has_flower_item", marimorphosis).offerTo(consumer);
		stairs(brickStair, brick).group("botania:metamorphic_brick_stairs")
				.criterion("has_flower_item", marimorphosis).offerTo(consumer);
		brick(chiseledBrick, brickSlab).criterion("has_base_item", conditionsFromItem(brick))
				.criterion("has_flower_item", marimorphosis).offerTo(consumer);

		slabShape(cobbleSlab, cobble).group("botania:metamorphic_cobble_slab")
				.criterion("has_flower_item", marimorphosis).offerTo(consumer);
		stairs(cobbleStair, cobble).group("botania:metamorphic_cobble_stairs")
				.criterion("has_flower_item", marimorphosis).offerTo(consumer);
		wallShape(cobbleWall, cobble, 6).group("botania:metamorphic_cobble_wall")
				.criterion("has_flower_item", marimorphosis).offerTo(consumer);
	}

	private ShapedRecipeJsonFactory compression(ItemConvertible output, Tag<Item> input) {
		return ShapedRecipeJsonFactory.create(output)
				.input('I', input)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.criterion("has_item", conditionsFromTag(input));
	}

	private ShapedRecipeJsonFactory compression(ItemConvertible output, ItemConvertible input) {
		return ShapedRecipeJsonFactory.create(output)
			.input('I', input)
			.pattern("III")
			.pattern("III")
			.pattern("III")
			.criterion("has_item", conditionsFromItem(input));
	}

	private ShapedRecipeJsonFactory brick(ItemConvertible output, ItemConvertible input) {
		return ShapedRecipeJsonFactory.create(output, 4)
				.criterion("has_item", conditionsFromItem(input))
				.input('Q', input)
				.pattern("QQ")
				.pattern("QQ");
	}

	private ShapedRecipeJsonFactory stairs(ItemConvertible output, ItemConvertible input) {
		return ShapedRecipeJsonFactory.create(output, 4)
				.criterion("has_item", conditionsFromItem(input))
				.input('Q', input)
				.pattern("  Q")
				.pattern(" QQ")
				.pattern("QQQ");
	}

	private ShapedRecipeJsonFactory slabShape(ItemConvertible output, ItemConvertible input) {
		return ShapedRecipeJsonFactory.create(output, 6)
				.criterion("has_item", conditionsFromItem(input))
				.input('Q', input)
				.pattern("QQQ");
	}

	private ShapedRecipeJsonFactory pillar(ItemConvertible output, ItemConvertible input) {
		return ShapedRecipeJsonFactory.create(output, 2)
				.criterion("has_item", conditionsFromItem(input))
				.input('Q', input)
				.pattern("Q")
				.pattern("Q");
	}

	private ShapedRecipeJsonFactory chiseled(ItemConvertible output, ItemConvertible input) {
		return ShapedRecipeJsonFactory.create(output)
				.criterion("has_item", conditionsFromItem(input))
				.input('Q', input)
				.pattern("Q")
				.pattern("Q");
	}

	private ShapedRecipeJsonFactory wallShape(ItemConvertible output, ItemConvertible input, int amount) {
		return ShapedRecipeJsonFactory.create(output, amount)
				.criterion("has_item", conditionsFromItem(input))
				.input('B', input)
				.pattern("BBB")
				.pattern("BBB");
	}

	private ShapedRecipeJsonFactory fence(ItemConvertible output, ItemConvertible input) {
		return ShapedRecipeJsonFactory.create(output, 3)
				.criterion("has_item", conditionsFromItem(input))
				.input('B', input)
				.input('S', Items.STICK)
				.pattern("BSB")
				.pattern("BSB");
	}

	private ShapedRecipeJsonFactory fenceGate(ItemConvertible output, ItemConvertible input) {
		return ShapedRecipeJsonFactory.create(output, 3)
				.criterion("has_item", conditionsFromItem(input))
				.input('B', input)
				.input('S', Items.STICK)
				.pattern("SBS")
				.pattern("SBS");
	}

	private ShapedRecipeJsonFactory ringShape(ItemConvertible output, ItemConvertible input) {
		return ShapedRecipeJsonFactory.create(output, 4)
				.input('W', input)
				.pattern(" W ")
				.pattern("W W")
				.pattern(" W ")
				.criterion("has_item", conditionsFromItem(input));
	}

	private void cosmeticBauble(Consumer<RecipeJsonProvider> consumer, ItemConvertible output, ItemConvertible input) {
		ShapedRecipeJsonFactory.create(output)
				.input('P', input)
				.input('S', ModItems.manaString)
				.pattern("PPP")
				.pattern("PSP")
				.pattern("PPP")
				.group("botania:cosmetic_bauble")
				.criterion("has_item", conditionsFromItem(ModItems.manaString))
				.offerTo(consumer);
	}

	private void specialRecipe(Consumer<RecipeJsonProvider> consumer, SpecialRecipeSerializer<?> serializer) {
		Identifier name = Registry.RECIPE_SERIALIZER.getId(serializer);
		ComplexRecipeJsonFactory.create(serializer).offerTo(consumer, prefix("dynamic/" + name.getPath()).toString());
	}

	@Override
	public String getName() {
		return "Botania crafting recipes";
	}
}
