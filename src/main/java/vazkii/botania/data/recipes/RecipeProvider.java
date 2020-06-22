/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import com.google.gson.JsonArray;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalAdvancement;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.common.crafting.conditions.TrueCondition;

import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.crafting.FluxfieldCondition;
import vazkii.botania.common.crafting.FuzzyNBTIngredient;
import vazkii.botania.common.crafting.recipe.*;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.common.lib.ResourceLocationHelper;

import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class RecipeProvider extends net.minecraft.data.RecipeProvider {
	public RecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		specialRecipe(consumer, AncientWillRecipe.SERIALIZER);
		specialRecipe(consumer, BannerRecipe.SERIALIZER);
		specialRecipe(consumer, BlackHoleTalismanExtractRecipe.SERIALIZER);
		specialRecipe(consumer, CompositeLensRecipe.SERIALIZER);
		specialRecipe(consumer, CosmeticAttachRecipe.SERIALIZER);
		specialRecipe(consumer, CosmeticRemoveRecipe.SERIALIZER);
		specialRecipe(consumer, KeepIvyRecipe.SERIALIZER);
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

	private void registerMain(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.manaSpreader)
				.key('P', ModTags.Items.PETALS)
				.key('W', ModTags.Items.LIVINGWOOD)
				.key('G', Tags.Items.INGOTS_GOLD)
				.patternLine("WWW")
				.patternLine("GP ")
				.patternLine("WWW")
				.setGroup("botania:spreader")
				.addCriterion("has_item", hasItem(ModTags.Items.LIVINGWOOD))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.redstoneSpreader)
				.addIngredient(ModBlocks.manaSpreader)
				.addIngredient(Tags.Items.DUSTS_REDSTONE)
				.setGroup("botania:spreader")
				.addCriterion("has_item", hasItem(ModBlocks.manaSpreader))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.elvenSpreader)
				.key('P', ModTags.Items.PETALS)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.key('W', ModBlocks.dreamwood)
				.patternLine("WWW")
				.patternLine("EP ")
				.patternLine("WWW")
				.setGroup("botania:spreader")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_ELEMENTIUM))
				.addCriterion("has_alt_item", hasItem(ModBlocks.dreamwood))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.gaiaSpreader)
				.key('S', ModBlocks.elvenSpreader)
				.key('D', ModTags.Items.GEMS_DRAGONSTONE)
				.key('E', ModItems.lifeEssence)
				.patternLine("ESD")
				.setGroup("botania:spreader")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.manaPool)
				.key('R', ModTags.Items.LIVINGROCK)
				.patternLine("R R")
				.patternLine("RRR")
				.addCriterion("has_item", hasItem(ModTags.Items.LIVINGROCK))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.dilutedPool)
				.key('R', ModFluffBlocks.livingrockSlab)
				.patternLine("R R")
				.patternLine("RRR")
				.addCriterion("has_item", hasItem(ModTags.Items.LIVINGROCK))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.fabulousPool)
				.key('R', ModBlocks.shimmerrock)
				.patternLine("R R")
				.patternLine("RRR")
				.addCriterion("has_item", hasItem(ModBlocks.shimmerrock))
				.addCriterion("has_alt_item", hasItem(ModItems.rainbowRod))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.runeAltar)
				.key('P', Ingredient.fromItemListStream(Stream.of(
						new Ingredient.SingleItemList(new ItemStack(ModItems.manaPearl)),
						new Ingredient.TagList(ModTags.Items.GEMS_MANA_DIAMOND))))
				.key('S', ModTags.Items.LIVINGROCK)
				.patternLine("SSS")
				.patternLine("SPS")
				.addCriterion("has_item", hasItem(ModItems.manaPearl))
				.addCriterion("has_alt_item", hasItem(ModTags.Items.GEMS_MANA_DIAMOND))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.manaPylon)
				.key('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.key('G', Tags.Items.INGOTS_GOLD)
				.key('M', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine(" G ")
				.patternLine("MDM")
				.patternLine(" G ")
				.addCriterion("has_item", hasItem(ModTags.Items.GEMS_MANA_DIAMOND))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.naturaPylon)
				.key('P', ModBlocks.manaPylon)
				.key('T', ModTags.Items.NUGGETS_TERRASTEEL)
				.key('E', Items.ENDER_EYE)
				.patternLine(" T ")
				.patternLine("TPT")
				.patternLine(" E ")
				.addCriterion("has_item", hasItem(ModBlocks.manaPylon))
				.addCriterion("has_alt_item", hasItem(ModTags.Items.INGOTS_TERRASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.gaiaPylon)
				.key('P', ModBlocks.manaPylon)
				.key('D', ModItems.pixieDust)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.patternLine(" D ")
				.patternLine("EPE")
				.patternLine(" D ")
				.addCriterion("has_item", hasItem(ModItems.pixieDust))
				.addCriterion("has_alt_item", hasItem(ModTags.Items.INGOTS_ELEMENTIUM))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.distributor)
				.key('R', ModTags.Items.LIVINGROCK)
				.key('S', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine("RRR")
				.patternLine("S S")
				.patternLine("RRR")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.manaVoid)
				.key('S', ModTags.Items.LIVINGROCK)
				.key('O', Items.OBSIDIAN)
				.patternLine("SSS")
				.patternLine("O O")
				.patternLine("SSS")
				.addCriterion("has_item", hasItem(ModTags.Items.LIVINGROCK))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.manaDetector)
				.key('R', Tags.Items.DUSTS_REDSTONE)
				.key('C', Items.COMPARATOR)
				.key('S', ModTags.Items.LIVINGROCK)
				.patternLine("RSR")
				.patternLine("SCS")
				.patternLine("RSR")
				.addCriterion("has_item", hasItem(Items.COMPARATOR))
				.addCriterion("has_alt_item", hasItem(ModTags.Items.LIVINGROCK))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.turntable)
				.key('P', Items.STICKY_PISTON)
				.key('W', ModTags.Items.LIVINGWOOD)
				.patternLine("WWW")
				.patternLine("WPW")
				.patternLine("WWW")
				.addCriterion("has_item", hasItem(ModTags.Items.LIVINGWOOD))
				.addCriterion("has_alt_item", hasItem(Items.STICKY_PISTON))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.tinyPlanet)
				.key('P', ModItems.tinyPlanet)
				.key('S', Tags.Items.STONE)
				.patternLine("SSS")
				.patternLine("SPS")
				.patternLine("SSS")
				.addCriterion("has_item", hasItem(ModItems.tinyPlanet))
				.addCriterion("has_alt_item", hasItem(ModItems.manaPearl))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.alchemyCatalyst)
				.key('P', ModItems.manaPearl)
				.key('B', Items.BREWING_STAND)
				.key('S', ModTags.Items.LIVINGROCK)
				.key('G', Tags.Items.INGOTS_GOLD)
				.patternLine("SGS")
				.patternLine("BPB")
				.patternLine("SGS")
				.addCriterion("has_item", hasItem(ModItems.manaPearl))
				.addCriterion("has_alt_item", hasItem(Items.BREWING_STAND))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.openCrate)
				.key('W', ModBlocks.livingwoodPlanks)
				.patternLine("WWW")
				.patternLine("W W")
				.patternLine("W W")
				.addCriterion("has_item", hasItem(ModBlocks.livingwoodPlanks))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.craftCrate)
				.key('C', Items.CRAFTING_TABLE)
				.key('W', ModBlocks.dreamwoodPlanks)
				.patternLine("WCW")
				.patternLine("W W")
				.patternLine("W W")
				.addCriterion("has_item", hasItem(ModBlocks.dreamwoodPlanks))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.forestEye)
				.key('S', ModTags.Items.LIVINGROCK)
				.key('E', Items.ENDER_EYE)
				.key('M', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine("MSM")
				.patternLine("SES")
				.patternLine("MSM")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.wildDrum)
				.key('W', ModTags.Items.LIVINGWOOD)
				.key('H', ModItems.grassHorn)
				.key('L', Items.LEATHER)
				.patternLine("WLW")
				.patternLine("WHW")
				.patternLine("WLW")
				.addCriterion("has_item", hasItem(ModItems.grassHorn))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.gatheringDrum)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.key('W', ModBlocks.dreamwood)
				.key('L', Items.LEATHER)
				.patternLine("WLW")
				.patternLine("WEW")
				.patternLine("WLW")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_ELEMENTIUM))
				.addCriterion("has_alt_item", hasItem(ModBlocks.dreamwood))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.canopyDrum)
				.key('W', ModTags.Items.LIVINGWOOD)
				.key('H', ModItems.leavesHorn)
				.key('L', Items.LEATHER)
				.patternLine("WLW")
				.patternLine("WHW")
				.patternLine("WLW")
				.addCriterion("has_item", hasItem(ModItems.leavesHorn))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.abstrusePlatform, 2)
				.key('0', ModBlocks.livingwood)
				.key('P', ModItems.manaPearl)
				.key('3', ModBlocks.livingwoodFramed)
				.key('4', ModBlocks.livingwoodPatternFramed)
				.patternLine("343")
				.patternLine("0P0")
				.addCriterion("has_item", hasItem(ModItems.manaPearl))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.spectralPlatform, 2)
				.key('0', ModBlocks.dreamwood)
				.key('3', ModBlocks.dreamwoodFramed)
				.key('4', ModBlocks.dreamwoodPatternFramed)
				.key('D', ModItems.pixieDust)
				.patternLine("343")
				.patternLine("0D0")
				.addCriterion("has_item", hasItem(ModItems.pixieDust))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.alfPortal)
				.key('T', ModTags.Items.NUGGETS_TERRASTEEL)
				.key('W', ModTags.Items.LIVINGWOOD)
				.patternLine("WTW")
				.patternLine("WTW")
				.patternLine("WTW")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_TERRASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.conjurationCatalyst)
				.key('P', ModBlocks.alchemyCatalyst)
				.key('B', ModItems.pixieDust)
				.key('S', ModTags.Items.LIVINGROCK)
				.key('G', ModTags.Items.INGOTS_ELEMENTIUM)
				.patternLine("SBS")
				.patternLine("GPG")
				.patternLine("SGS")
				.addCriterion("has_item", hasItem(ModItems.pixieDust))
				.addCriterion("has_alt_item", hasItem(ModTags.Items.INGOTS_ELEMENTIUM))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.spawnerClaw)
				.key('P', Items.PRISMARINE_BRICKS)
				.key('B', Items.BLAZE_ROD)
				.key('S', ModTags.Items.INGOTS_ELEMENTIUM)
				.key('E', ModItems.enderAirBottle)
				.key('M', ModTags.Items.BLOCKS_MANASTEEL)
				.patternLine("BSB")
				.patternLine("PMP")
				.patternLine("PEP")
				.addCriterion("has_item", hasItem(ModItems.enderAirBottle))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.enderEye)
				.key('R', Tags.Items.DUSTS_REDSTONE)
				.key('E', Items.ENDER_EYE)
				.key('O', Items.OBSIDIAN)
				.patternLine("RER")
				.patternLine("EOE")
				.patternLine("RER")
				.addCriterion("has_item", hasItem(Items.ENDER_EYE))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.starfield)
				.key('P', ModItems.pixieDust)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.key('O', Items.OBSIDIAN)
				.patternLine("EPE")
				.patternLine("EOE")
				.addCriterion("has_item", hasItem(ModItems.pixieDust))
				.addCriterion("has_alt_item", hasItem(ModTags.Items.INGOTS_ELEMENTIUM))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.rfGenerator)
				.key('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.key('S', ModTags.Items.LIVINGROCK)
				.key('M', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine("SRS")
				.patternLine("RMR")
				.patternLine("SRS")
				.addCriterion("has_item", hasItem(Tags.Items.STORAGE_BLOCKS_REDSTONE))
				.addCriterion("has_alt_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(WrapperResult.transformJson(consumer, json -> {
					JsonArray array = new JsonArray();
					array.add(FluxfieldCondition.SERIALIZER.getJson(new FluxfieldCondition(true)));
					json.add("conditions", array);
				}
				));
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.brewery)
				.key('A', ModTags.Items.RUNES_MANA)
				.key('R', ModTags.Items.LIVINGROCK)
				.key('S', Items.BREWING_STAND)
				.key('M', ModTags.Items.BLOCKS_MANASTEEL)
				.patternLine("RSR")
				.patternLine("RAR")
				.patternLine("RMR")
				.addCriterion("has_item", hasItem(ModTags.Items.RUNES_MANA))
				.addCriterion("has_alt_item", hasItem(Items.BREWING_STAND))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.terraPlate)
				.key('0', ModTags.Items.RUNES_WATER)
				.key('1', ModTags.Items.RUNES_FIRE)
				.key('2', ModTags.Items.RUNES_EARTH)
				.key('3', ModTags.Items.RUNES_AIR)
				.key('8', ModTags.Items.RUNES_MANA)
				.key('L', Tags.Items.STORAGE_BLOCKS_LAPIS)
				.key('M', ModTags.Items.BLOCKS_MANASTEEL)
				.patternLine("LLL")
				.patternLine("0M1")
				.patternLine("283")
				.addCriterion("has_item", hasItem(ModTags.Items.RUNES))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.prism)
				.key('P', Tags.Items.GEMS_PRISMARINE)
				.key('S', ModBlocks.spectralPlatform)
				.key('G', Items.GLASS)
				.patternLine("GPG")
				.patternLine("GSG")
				.patternLine("GPG")
				.addCriterion("has_item", hasItem(Tags.Items.GEMS_PRISMARINE))
				.addCriterion("has_alt_item", hasItem(ModBlocks.spectralPlatform))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.pump)
				.key('B', Items.BUCKET)
				.key('S', ModTags.Items.LIVINGROCK)
				.key('I', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine("SSS")
				.patternLine("IBI")
				.patternLine("SSS")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.incensePlate)
				.key('S', ModFluffBlocks.livingwoodSlab)
				.key('W', ModTags.Items.LIVINGWOOD)
				.patternLine("WSS")
				.addCriterion("has_item", hasItem(ModTags.Items.LIVINGWOOD))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.hourglass)
				.key('R', Tags.Items.DUSTS_REDSTONE)
				.key('S', ModTags.Items.INGOTS_MANASTEEL)
				.key('G', Tags.Items.INGOTS_GOLD)
				.key('M', ModBlocks.manaGlass)
				.patternLine("GMG")
				.patternLine("RSR")
				.patternLine("GMG")
				.addCriterion("has_item", hasItem(ModBlocks.manaGlass))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.ghostRail)
				.addIngredient(Items.RAIL)
				.addIngredient(ModBlocks.spectralPlatform)
				.addCriterion("has_item", hasItem(Items.RAIL))
				.addCriterion("has_alt_item", hasItem(ModBlocks.spectralPlatform))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.sparkChanger)
				.key('R', Tags.Items.DUSTS_REDSTONE)
				.key('S', ModTags.Items.LIVINGROCK)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.patternLine("ESE")
				.patternLine("SRS")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_ELEMENTIUM))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.felPumpkin)
				.key('P', Items.PUMPKIN)
				.key('B', Items.BONE)
				.key('S', Items.STRING)
				.key('F', Items.ROTTEN_FLESH)
				.key('G', Items.GUNPOWDER)
				.patternLine(" S ")
				.patternLine("BPF")
				.patternLine(" G ")
				.addCriterion("has_item", hasItem(Items.PUMPKIN))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.cocoon)
				.key('S', Items.STRING)
				.key('C', ModItems.manaweaveCloth)
				.key('P', ModBlocks.felPumpkin)
				.key('D', ModItems.pixieDust)
				.patternLine("SSS")
				.patternLine("CPC")
				.patternLine("SDS")
				.addCriterion("has_item", hasItem(ModBlocks.felPumpkin))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.lightRelayDefault)
				.addIngredient(ModItems.redString)
				.addIngredient(ModTags.Items.GEMS_DRAGONSTONE)
				.addIngredient(Tags.Items.DUSTS_GLOWSTONE)
				.addIngredient(Tags.Items.DUSTS_GLOWSTONE)
				.addCriterion("has_item", hasItem(ModTags.Items.GEMS_DRAGONSTONE))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.lightRelayDetector)
				.addIngredient(ModBlocks.lightRelayDefault)
				.addIngredient(Tags.Items.DUSTS_REDSTONE)
				.addCriterion("has_item", hasItem(ModBlocks.lightRelayDefault))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.lightRelayFork)
				.addIngredient(ModBlocks.lightRelayDefault)
				.addIngredient(Items.REDSTONE_TORCH)
				.addCriterion("has_item", hasItem(ModBlocks.lightRelayDefault))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.lightRelayToggle)
				.addIngredient(ModBlocks.lightRelayDefault)
				.addIngredient(Items.LEVER)
				.addCriterion("has_item", hasItem(ModBlocks.lightRelayDefault))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.lightLauncher)
				.key('D', ModBlocks.dreamwood)
				.key('L', ModBlocks.lightRelayDefault)
				.patternLine("DDD")
				.patternLine("DLD")
				.addCriterion("has_item", hasItem(ModBlocks.lightRelayDefault))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.manaBomb)
				.key('T', Items.TNT)
				.key('G', ModItems.lifeEssence)
				.key('L', ModTags.Items.LIVINGWOOD)
				.patternLine("LTL")
				.patternLine("TGT")
				.patternLine("LTL")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.bellows)
				.key('R', ModTags.Items.RUNES_AIR)
				.key('S', ModFluffBlocks.livingwoodSlab)
				.key('L', Items.LEATHER)
				.patternLine("SSS")
				.patternLine("RL ")
				.patternLine("SSS")
				.addCriterion("has_item", hasItem(ModTags.Items.RUNES_AIR))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.bifrostPerm)
				.addIngredient(ModItems.rainbowRod)
				.addIngredient(ModBlocks.elfGlass)
				.addCriterion("has_item", hasItem(ModItems.rainbowRod))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.cellBlock, 3)
				.addIngredient(Items.CACTUS, 3)
				.addIngredient(Items.BEETROOT)
				.addIngredient(Items.CARROT)
				.addIngredient(Items.POTATO)
				.addCriterion("has_item", hasItem(ModSubtiles.dandelifeon))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.teruTeruBozu)
				.key('C', ModItems.manaweaveCloth)
				.key('S', Items.SUNFLOWER)
				.patternLine("C")
				.patternLine("C")
				.patternLine("S")
				.addCriterion("has_item", hasItem(ModItems.manaweaveCloth))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.avatar)
				.key('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.key('W', ModTags.Items.LIVINGWOOD)
				.patternLine(" W ")
				.patternLine("WDW")
				.patternLine("W W")
				.addCriterion("has_item", hasItem(ModTags.Items.GEMS_MANA_DIAMOND))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.animatedTorch)
				.key('D', ModTags.Items.DUSTS_MANA)
				.key('T', Items.REDSTONE_TORCH)
				.patternLine("D")
				.patternLine("T")
				.addCriterion("has_item", hasItem(Items.REDSTONE_TORCH))
				.addCriterion("has_alt_item", hasItem(ModTags.Items.DUSTS_MANA))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.livingwoodTwig)
				.key('W', ModTags.Items.LIVINGWOOD)
				.patternLine("W")
				.patternLine("W")
				.addCriterion("has_item", hasItem(ModTags.Items.LIVINGWOOD))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.redstoneRoot)
				.addIngredient(Tags.Items.DUSTS_REDSTONE)
				.addIngredient(Ingredient.fromItems(Items.FERN, Items.GRASS))
				.addCriterion("has_item", hasItem(Tags.Items.DUSTS_REDSTONE))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.dreamwoodTwig)
				.key('W', ModBlocks.dreamwood)
				.patternLine("W")
				.patternLine("W")
				.addCriterion("has_item", hasItem(ModBlocks.dreamwood))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.gaiaIngot)
				.key('S', ModItems.lifeEssence)
				.key('I', ModTags.Items.INGOTS_TERRASTEEL)
				.patternLine(" S ")
				.patternLine("SIS")
				.patternLine(" S ")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.manaweaveCloth)
				.key('S', ModItems.manaString)
				.patternLine("SS")
				.patternLine("SS")
				.addCriterion("has_item", hasItem(ModItems.manaString))
				.build(consumer);
		Ingredient dyes = Ingredient.fromItems(Items.WHITE_DYE, Items.ORANGE_DYE, Items.MAGENTA_DYE,
				Items.LIGHT_BLUE_DYE, Items.YELLOW_DYE, Items.LIME_DYE, Items.PINK_DYE, Items.GRAY_DYE,
				Items.LIGHT_GRAY_DYE, Items.CYAN_DYE, Items.PURPLE_DYE, Items.BLUE_DYE, Items.BROWN_DYE,
				Items.GREEN_DYE, Items.RED_DYE, Items.BLACK_DYE);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.fertilizer)
				.addIngredient(Items.BONE_MEAL)
				.addIngredient(dyes, 4)
				.addCriterion("has_item", hasItem(Tags.Items.DYES))
				.build(consumer, "botania:fertilizer_dye");
		Ingredient floralDyes = Ingredient.fromItems(Arrays.stream(DyeColor.values()).map(ModItems::getDye).toArray(Item[]::new));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.fertilizer)
				.addIngredient(Items.BONE_MEAL)
				.addIngredient(floralDyes, 4)
				.addCriterion("has_item", hasItem(ModTags.Items.DYES))
				.build(consumer, "botania:fertilizer_powder");
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.drySeeds)
				.addIngredient(ModItems.grassSeeds)
				.addIngredient(Items.DEAD_BUSH)
				.setGroup("botania:seeds")
				.addCriterion("has_item", hasItem(ModItems.grassSeeds))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.goldenSeeds)
				.addIngredient(ModItems.grassSeeds)
				.addIngredient(Items.WHEAT)
				.setGroup("botania:seeds")
				.addCriterion("has_item", hasItem(ModItems.grassSeeds))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.vividSeeds)
				.addIngredient(ModItems.grassSeeds)
				.addIngredient(Items.GREEN_DYE)
				.setGroup("botania:seeds")
				.addCriterion("has_item", hasItem(ModItems.grassSeeds))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.scorchedSeeds)
				.addIngredient(ModItems.grassSeeds)
				.addIngredient(Items.BLAZE_POWDER)
				.setGroup("botania:seeds")
				.addCriterion("has_item", hasItem(ModItems.grassSeeds))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.infusedSeeds)
				.addIngredient(ModItems.grassSeeds)
				.addIngredient(Items.PRISMARINE_SHARD)
				.setGroup("botania:seeds")
				.addCriterion("has_item", hasItem(ModItems.grassSeeds))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.mutatedSeeds)
				.addIngredient(ModItems.grassSeeds)
				.addIngredient(Items.SPIDER_EYE)
				.setGroup("botania:seeds")
				.addCriterion("has_item", hasItem(ModItems.grassSeeds))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.darkQuartz, 8)
				.key('Q', Tags.Items.GEMS_QUARTZ)
				.key('C', Ingredient.fromItems(Items.COAL, Items.CHARCOAL))
				.patternLine("QQQ")
				.patternLine("QCQ")
				.patternLine("QQQ")
				.addCriterion("has_item", hasItem(Tags.Items.GEMS_QUARTZ))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.blazeQuartz, 8)
				.key('Q', Tags.Items.GEMS_QUARTZ)
				.key('C', Items.BLAZE_POWDER)
				.patternLine("QQQ")
				.patternLine("QCQ")
				.patternLine("QQQ")
				.addCriterion("has_item", hasItem(Tags.Items.GEMS_QUARTZ))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.lavenderQuartz, 8)
				.key('Q', Tags.Items.GEMS_QUARTZ)
				.key('C', Ingredient.fromItems(Items.ALLIUM, Items.PINK_TULIP, Items.LILAC, Items.PEONY))
				.patternLine("QQQ")
				.patternLine("QCQ")
				.patternLine("QQQ")
				.addCriterion("has_item", hasItem(Tags.Items.GEMS_QUARTZ))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.redQuartz, 8)
				.key('Q', Tags.Items.GEMS_QUARTZ)
				.key('C', Items.REDSTONE)
				.patternLine("QQQ")
				.patternLine("QCQ")
				.patternLine("QQQ")
				.addCriterion("has_item", hasItem(Tags.Items.GEMS_QUARTZ))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.sunnyQuartz, 8)
				.key('Q', Tags.Items.GEMS_QUARTZ)
				.key('C', Items.SUNFLOWER)
				.patternLine("QQQ")
				.patternLine("QCQ")
				.patternLine("QQQ")
				.addCriterion("has_item", hasItem(Tags.Items.GEMS_QUARTZ))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.vineBall)
				.key('V', Items.VINE)
				.patternLine("VVV")
				.patternLine("VVV")
				.patternLine("VVV")
				.addCriterion("has_item", hasItem(Items.VINE))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.necroVirus)
				.addIngredient(ModItems.pixieDust)
				.addIngredient(ModItems.vineBall)
				.addIngredient(Items.MAGMA_CREAM)
				.addIngredient(Items.FERMENTED_SPIDER_EYE)
				.addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.ZOMBIE_HEAD)
				.addCriterion("has_item", hasItem(ModItems.pixieDust))
				.addCriterion("has_alt_item", hasItem(Items.ZOMBIE_HEAD))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.nullVirus)
				.addIngredient(ModItems.pixieDust)
				.addIngredient(ModItems.vineBall)
				.addIngredient(Items.MAGMA_CREAM)
				.addIngredient(Items.FERMENTED_SPIDER_EYE)
				.addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.SKELETON_SKULL)
				.addCriterion("has_item", hasItem(ModItems.pixieDust))
				.addCriterion("has_alt_item", hasItem(Items.SKELETON_SKULL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.spark)
				.key('P', ModTags.Items.PETALS)
				.key('B', Items.BLAZE_POWDER)
				.key('N', Items.GOLD_NUGGET)
				.patternLine(" P ")
				.patternLine("BNB")
				.patternLine(" P ")
				.addCriterion("has_item", hasItem(Items.BLAZE_POWDER))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.sparkUpgradeDispersive)
				.addIngredient(ModItems.pixieDust)
				.addIngredient(ModTags.Items.INGOTS_MANASTEEL)
				.addIngredient(ModTags.Items.RUNES_WATER)
				.setGroup("botania:spark_upgrade")
				.addCriterion("has_item", hasItem(ModItems.pixieDust))
				.addCriterion("has_alt_item", hasItem(ModItems.spark))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.sparkUpgradeDominant)
				.addIngredient(ModItems.pixieDust)
				.addIngredient(ModTags.Items.INGOTS_MANASTEEL)
				.addIngredient(ModTags.Items.RUNES_FIRE)
				.setGroup("botania:spark_upgrade")
				.addCriterion("has_item", hasItem(ModItems.pixieDust))
				.addCriterion("has_alt_item", hasItem(ModItems.spark))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.sparkUpgradeRecessive)
				.addIngredient(ModItems.pixieDust)
				.addIngredient(ModTags.Items.INGOTS_MANASTEEL)
				.addIngredient(ModTags.Items.RUNES_EARTH)
				.setGroup("botania:spark_upgrade")
				.addCriterion("has_item", hasItem(ModItems.pixieDust))
				.addCriterion("has_alt_item", hasItem(ModItems.spark))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.sparkUpgradeIsolated)
				.addIngredient(ModItems.pixieDust)
				.addIngredient(ModTags.Items.INGOTS_MANASTEEL)
				.addIngredient(ModTags.Items.RUNES_AIR)
				.setGroup("botania:spark_upgrade")
				.addCriterion("has_item", hasItem(ModItems.pixieDust))
				.addCriterion("has_alt_item", hasItem(ModItems.spark))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.vial, 3)
				.key('G', ModBlocks.manaGlass)
				.patternLine("G G")
				.patternLine(" G ")
				.addCriterion("has_item", hasItem(ModBlocks.manaGlass))
				.addCriterion("has_alt_item", hasItem(ModBlocks.brewery))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.flask, 3)
				.key('G', ModBlocks.elfGlass)
				.patternLine("G G")
				.patternLine(" G ")
				.addCriterion("has_item", hasItem(ModBlocks.elfGlass))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.worldSeed, 4)
				.key('S', Items.WHEAT_SEEDS)
				.key('D', ModTags.Items.GEMS_DRAGONSTONE)
				.key('G', Items.GRASS_BLOCK)
				.patternLine("G")
				.patternLine("S")
				.patternLine("D")
				.addCriterion("has_item", hasItem(ModTags.Items.GEMS_DRAGONSTONE))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.thornChakram, 2)
				.key('T', ModTags.Items.INGOTS_TERRASTEEL)
				.key('V', Items.VINE)
				.patternLine("VVV")
				.patternLine("VTV")
				.patternLine("VVV")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_TERRASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.flareChakram, 2)
				.key('P', ModItems.pixieDust)
				.key('B', Items.BLAZE_POWDER)
				.key('C', ModItems.thornChakram)
				.patternLine("BBB")
				.patternLine("CPC")
				.patternLine("BBB")
				.addCriterion("has_item", hasItem(ModItems.thornChakram))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.phantomInk, 4)
				.addIngredient(ModItems.manaPearl)
				.addIngredient(Tags.Items.DYES)
				.addIngredient(Ingredient.fromItems(Items.GLASS, Items.WHITE_STAINED_GLASS, Items.ORANGE_STAINED_GLASS,
						Items.MAGENTA_STAINED_GLASS, Items.LIGHT_BLUE_STAINED_GLASS, Items.YELLOW_STAINED_GLASS,
						Items.LIME_STAINED_GLASS, Items.PINK_STAINED_GLASS, Items.GRAY_STAINED_GLASS,
						Items.LIGHT_GRAY_STAINED_GLASS, Items.CYAN_STAINED_GLASS, Items.PURPLE_STAINED_GLASS,
						Items.BLUE_STAINED_GLASS, Items.BROWN_STAINED_GLASS, Items.GREEN_STAINED_GLASS,
						Items.RED_STAINED_GLASS, Items.BLACK_STAINED_GLASS))
				.addIngredient(Items.GLASS_BOTTLE, 4)
				.addCriterion("has_item", hasItem(ModItems.manaPearl))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.keepIvy)
				.addIngredient(ModItems.pixieDust)
				.addIngredient(Items.VINE)
				.addIngredient(ModItems.enderAirBottle)
				.addCriterion("has_item", hasItem(ModItems.enderAirBottle))
				.build(consumer);

	}

	private void registerMisc(Consumer<IFinishedRecipe> consumer) {
		Ingredient mushrooms = Ingredient.fromItems(ModBlocks.whiteMushroom, ModBlocks.orangeMushroom,
				ModBlocks.magentaMushroom, ModBlocks.lightBlueMushroom, ModBlocks.yellowMushroom,
				ModBlocks.limeMushroom, ModBlocks.pinkMushroom, ModBlocks.grayMushroom, ModBlocks.lightGrayMushroom,
				ModBlocks.cyanMushroom, ModBlocks.purpleMushroom, ModBlocks.blueMushroom, ModBlocks.brownMushroom,
				ModBlocks.greenMushroom, ModBlocks.redMushroom, ModBlocks.blackMushroom);
		ShapelessRecipeBuilder.shapelessRecipe(Items.MUSHROOM_STEW)
				.addIngredient(mushrooms, 2)
				.addIngredient(Items.BOWL)
				.addCriterion("has_item", hasItem(Items.BOWL))
				.addCriterion("has_orig_recipe", new RecipeUnlockedTrigger.Instance(new ResourceLocation("mushroom_stew")))
				.build(consumer, "botania:mushroom_stew");

		ShapedRecipeBuilder.shapedRecipe(Items.COBWEB)
				.key('S', Items.STRING)
				.key('M', ModItems.manaString)
				.patternLine("S S")
				.patternLine(" M ")
				.patternLine("S S")
				.addCriterion("has_item", hasItem(ModItems.manaString))
				.build(consumer, prefix("cobweb"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.defaultAltar)
				.key('P', ModTags.Items.PETALS)
				.key('S', Items.COBBLESTONE_SLAB)
				.key('C', Tags.Items.COBBLESTONE)
				.patternLine("SPS")
				.patternLine(" C ")
				.patternLine("CCC")
				.addCriterion("has_item", hasItem(ModTags.Items.PETALS))
				.build(consumer);
		for (String metamorphicVariant : LibBlockNames.METAMORPHIC_VARIANTS) {
			Block altar = Registry.BLOCK.getValue(prefix("apothecary_" + metamorphicVariant.replaceAll("_", ""))).get();
			Block cobble = Registry.BLOCK.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + metamorphicVariant + "_cobblestone")).get();
			ShapedRecipeBuilder.shapedRecipe(altar)
					.key('A', ModBlocks.defaultAltar)
					.key('S', cobble)
					.patternLine("SSS")
					.patternLine("SAS")
					.patternLine("SSS")
					.setGroup("botania:metamorphic_apothecary")
					.addCriterion("has_item", hasItem(cobble))
					.addCriterion("has_flower_item", hasItem(ModSubtiles.marimorphosis))
					.build(consumer);
		}
		EnumMap<DyeColor, Ingredient> colorOverrides = new EnumMap<>(DyeColor.class);
		colorOverrides.put(DyeColor.LIGHT_BLUE, Ingredient.fromItemListStream(Stream.of(
				new Ingredient.SingleItemList(new ItemStack(Items.BLUE_ORCHID)),
				new Ingredient.TagList(ModTags.Items.PETALS_LIGHT_BLUE)
		)));
		colorOverrides.put(DyeColor.ORANGE, Ingredient.fromItemListStream(Stream.of(
				new Ingredient.SingleItemList(new ItemStack(Items.ORANGE_TULIP)),
				new Ingredient.TagList(ModTags.Items.PETALS_ORANGE)
		)));
		colorOverrides.put(DyeColor.PINK, Ingredient.fromItemListStream(Stream.of(
				new Ingredient.SingleItemList(new ItemStack(Items.PINK_TULIP)),
				new Ingredient.TagList(ModTags.Items.PETALS_PINK)
		)));
		colorOverrides.put(DyeColor.RED, Ingredient.fromItemListStream(Stream.of(
				new Ingredient.SingleItemList(new ItemStack(Items.POPPY)),
				new Ingredient.SingleItemList(new ItemStack(Items.RED_TULIP)),
				new Ingredient.TagList(ModTags.Items.PETALS_RED)
		)));
		colorOverrides.put(DyeColor.LIGHT_GRAY, Ingredient.fromItemListStream(Stream.of(
				new Ingredient.SingleItemList(new ItemStack(Items.AZURE_BLUET)),
				new Ingredient.SingleItemList(new ItemStack(Items.WHITE_TULIP)),
				new Ingredient.SingleItemList(new ItemStack(Items.OXEYE_DAISY)),
				new Ingredient.TagList(ModTags.Items.PETALS_LIGHT_GRAY)
		)));
		colorOverrides.put(DyeColor.YELLOW, Ingredient.fromItemListStream(Stream.of(
				new Ingredient.SingleItemList(new ItemStack(Items.DANDELION)),
				new Ingredient.TagList(ModTags.Items.PETALS_YELLOW)
		)));
		for (DyeColor color : DyeColor.values()) {
			ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.getShinyFlower(color))
					.addIngredient(Tags.Items.DUSTS_GLOWSTONE)
					.addIngredient(Tags.Items.DUSTS_GLOWSTONE)
					.addIngredient(ModBlocks.getFlower(color))
					.setGroup("botania:shiny_flower")
					.addCriterion("has_item", hasItem(ModBlocks.getFlower(color)))
					.build(consumer);
			ShapedRecipeBuilder.shapedRecipe(ModBlocks.getFloatingFlower(color))
					.key('S', ModItems.grassSeeds)
					.key('D', Items.DIRT)
					.key('F', ModBlocks.getShinyFlower(color))
					.patternLine("F")
					.patternLine("S")
					.patternLine("D")
					.setGroup("botania:floating_flowers")
					.addCriterion("has_item", hasItem(ModBlocks.getShinyFlower(color)))
					.build(consumer);
			ShapedRecipeBuilder.shapedRecipe(ModBlocks.getPetalBlock(color))
					.key('P', ModItems.getPetal(color))
					.patternLine("PPP")
					.patternLine("PPP")
					.patternLine("PPP")
					.setGroup("botania:petal_block")
					.addCriterion("has_item", hasItem(ModItems.getPetal(color)))
					.build(consumer);
			ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.getMushroom(color))
					.addIngredient(Ingredient.fromItems(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM))
					.addIngredient(ModItems.getDye(color))
					.setGroup("botania:mushroom")
					.addCriterion("has_item", hasItem(Items.RED_MUSHROOM))
					.addCriterion("has_alt_item", hasItem(Items.BROWN_MUSHROOM))
					.build(consumer, "botania:mushroom_" + color.ordinal());
			ShapelessRecipeBuilder.shapelessRecipe(ModItems.getPetal(color), 4)
					.addIngredient(ModBlocks.getDoubleFlower(color))
					.setGroup("botania:petal_double")
					.addCriterion("has_item", hasItem(ModBlocks.getDoubleFlower(color)))
					.addCriterion("has_alt_item", hasItem(ModItems.getPetal(color)))
					.build(consumer, "botania:petal_" + color.getName() + "_double");
			ShapelessRecipeBuilder.shapelessRecipe(ModItems.getPetal(color), 2)
					.addIngredient(ModBlocks.getFlower(color))
					.setGroup("botania:petal")
					.addCriterion("has_item", hasItem(ModBlocks.getFlower(color)))
					.addCriterion("has_alt_item", hasItem(ModItems.getPetal(color)))
					.build(consumer, "botania:petal_" + color.getName());
			ShapelessRecipeBuilder.shapelessRecipe(ModItems.getDye(color))
					.addIngredient(colorOverrides.getOrDefault(color, Ingredient.fromTag(ModTags.Items.getFlowerTag(color))))
					.addIngredient(ModItems.pestleAndMortar)
					.setGroup("botania:dye")
					.addCriterion("has_item", hasItem(ModItems.getDye(color)))
					.addCriterion("has_alt_item", hasItem(ModItems.getPetal(color)))
					.build(consumer, "botania:dye_" + color.getName());
		}

		ShapelessRecipeBuilder.shapelessRecipe(ModItems.magentaDye, 2)
				.addIngredient(Items.LILAC)
				.addIngredient(ModItems.pestleAndMortar)
				.setGroup("botania:dye_double")
				.addCriterion("has_item", hasItem(ModItems.yellowDye))
				.addCriterion("has_alt_item", hasItem(Items.LILAC))
				.build(consumer, "botania:dye_magenta_double");
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.pinkDye, 2)
				.addIngredient(Items.PEONY)
				.addIngredient(ModItems.pestleAndMortar)
				.setGroup("botania:dye_double")
				.addCriterion("has_item", hasItem(ModItems.pinkDye))
				.addCriterion("has_alt_item", hasItem(Items.PEONY))
				.build(consumer, "botania:dye_pink_double");
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.redDye, 2)
				.addIngredient(Items.ROSE_BUSH)
				.addIngredient(ModItems.pestleAndMortar)
				.setGroup("botania:dye_double")
				.addCriterion("has_item", hasItem(ModItems.redDye))
				.addCriterion("has_alt_item", hasItem(Items.ROSE_BUSH))
				.build(consumer, "botania:dye_red_double");
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.yellowDye, 2)
				.addIngredient(Items.SUNFLOWER)
				.addIngredient(ModItems.pestleAndMortar)
				.setGroup("botania:dye_double")
				.addCriterion("has_item", hasItem(ModItems.yellowDye))
				.addCriterion("has_alt_item", hasItem(Items.SUNFLOWER))
				.build(consumer, "botania:dye_yellow_double");
	}

	private void registerTools(Consumer<IFinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lexicon)
				.addIngredient(ItemTags.SAPLINGS)
				.addIngredient(Items.BOOK)
				.addCriterion("has_item", hasItem(ItemTags.SAPLINGS))
				.addCriterion("has_alt_item", hasItem(Items.BOOK))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.twigWand)
				.key('P', ModTags.Items.PETALS)
				.key('S', ModItems.livingwoodTwig)
				.patternLine(" PS")
				.patternLine(" SP")
				.patternLine("S  ")
				.setGroup("botania:twig_wand")
				.addCriterion("has_item", hasItem(ModTags.Items.PETALS))
				.build(WrapperResult.ofType(TwigWandRecipe.SERIALIZER, consumer));
		ShapedRecipeBuilder.shapedRecipe(ModItems.manaTablet)
				.key('P', Ingredient.fromItemListStream(Stream.of(
						new Ingredient.SingleItemList(new ItemStack(ModItems.manaPearl)),
						new Ingredient.TagList(ModTags.Items.GEMS_MANA_DIAMOND))))
				.key('S', ModTags.Items.LIVINGROCK)
				.patternLine("SSS")
				.patternLine("SPS")
				.patternLine("SSS")
				.addCriterion("has_item", hasItem(ModTags.Items.GEMS_MANA_DIAMOND))
				.build(consumer);

		ResourceLocation cacophoniumId = Registry.ITEM.getKey(ModItems.cacophonium);
		Supplier<ShapedRecipeBuilder> cacophoniumBase = () -> ShapedRecipeBuilder.shapedRecipe(ModItems.cacophonium)
				.key('N', Items.NOTE_BLOCK)
				.patternLine(" G ")
				.patternLine("GNG")
				.patternLine("GG ")
				.addCriterion("has_item", hasItem(Items.NOTE_BLOCK));
		ConditionalRecipe.builder()
				.addCondition(new TagEmptyCondition("forge", "ingots/brass"))
				.addRecipe(cacophoniumBase.get().key('G', Tags.Items.INGOTS_GOLD)::build)
				.addCondition(TrueCondition.INSTANCE)
				.addRecipe(
						cacophoniumBase.get().key('G', Ingredient.fromItemListStream(Stream.of(
								new Ingredient.TagList(Tags.Items.INGOTS_GOLD),
								new Ingredient.TagList(new ItemTags.Wrapper(new ResourceLocation("forge", "ingots/brass"))))
						))::build
				).setAdvancement(prefix("recipes/botania/cacophonium"),
						ConditionalAdvancement.builder()
								.addCondition(TrueCondition.INSTANCE)
								.addAdvancement(Advancement.Builder.builder()
										.withParentId(new ResourceLocation("recipes/root"))
										.withRewards(AdvancementRewards.Builder.recipe(cacophoniumId))
										.withCriterion("has_item", hasItem(Items.NOTE_BLOCK))
										.withCriterion("has_the_recipe", new RecipeUnlockedTrigger.Instance(cacophoniumId))
										.withRequirementsStrategy(IRequirementsStrategy.OR))
				).build(consumer, cacophoniumId);

		ShapedRecipeBuilder.shapedRecipe(ModItems.grassHorn)
				.key('S', ModItems.grassSeeds)
				.key('W', ModTags.Items.LIVINGWOOD)
				.patternLine(" W ")
				.patternLine("WSW")
				.patternLine("WW ")
				.addCriterion("has_item", hasItem(ModTags.Items.LIVINGWOOD))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.leavesHorn)
				.addIngredient(ModItems.grassHorn)
				.addIngredient(ItemTags.LEAVES)
				.addCriterion("has_item", hasItem(ModItems.grassHorn))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.snowHorn)
				.addIngredient(ModItems.grassHorn)
				.addIngredient(Items.SNOWBALL)
				.addCriterion("has_item", hasItem(ModItems.grassHorn))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.manaMirror)
				.key('P', ModItems.manaPearl)
				.key('R', ModTags.Items.LIVINGROCK)
				.key('S', ModItems.livingwoodTwig)
				.key('T', ModItems.manaTablet)
				.key('I', ModTags.Items.INGOTS_TERRASTEEL)
				.patternLine(" PR")
				.patternLine(" SI")
				.patternLine("T  ")
				.addCriterion("has_item", hasItem(ModItems.manaTablet))
				.addCriterion("has_alt_item", hasItem(ModTags.Items.INGOTS_TERRASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.openBucket)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.patternLine("E E")
				.patternLine(" E ")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_ELEMENTIUM))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.spawnerMover)
				.key('A', ModItems.enderAirBottle)
				.key('D', ModTags.Items.GEMS_DRAGONSTONE)
				.key('E', ModItems.lifeEssence)
				.key('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.patternLine("EIE")
				.patternLine("ADA")
				.patternLine("EIE")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.slingshot)
				.key('A', ModTags.Items.RUNES_AIR)
				.key('T', ModItems.livingwoodTwig)
				.patternLine(" TA")
				.patternLine(" TT")
				.patternLine("T  ")
				.addCriterion("has_item", hasItem(ModTags.Items.RUNES_AIR))
				.build(consumer);

		registerSimpleArmorSet(consumer, Ingredient.fromTag(ModTags.Items.INGOTS_MANASTEEL), "manasteel", hasItem(ModTags.Items.INGOTS_MANASTEEL));
		registerSimpleArmorSet(consumer, Ingredient.fromTag(ModTags.Items.INGOTS_ELEMENTIUM), "elementium", hasItem(ModTags.Items.INGOTS_ELEMENTIUM));
		registerSimpleArmorSet(consumer, Ingredient.fromItems(ModItems.manaweaveCloth), "manaweave", hasItem(ModItems.manaweaveCloth));

		registerTerrasteelUpgradeRecipe(consumer, ModItems.terrasteelHelm, ModItems.manasteelHelm, ModTags.Items.RUNES_SPRING);
		registerTerrasteelUpgradeRecipe(consumer, ModItems.terrasteelChest, ModItems.manasteelChest, ModTags.Items.RUNES_SUMMER);
		registerTerrasteelUpgradeRecipe(consumer, ModItems.terrasteelLegs, ModItems.manasteelLegs, ModTags.Items.RUNES_AUTUMN);
		registerTerrasteelUpgradeRecipe(consumer, ModItems.terrasteelBoots, ModItems.manasteelBoots, ModTags.Items.RUNES_WINTER);

		registerToolSetRecipes(consumer, Ingredient.fromTag(ModTags.Items.INGOTS_MANASTEEL), Ingredient.fromItems(ModItems.livingwoodTwig),
				hasItem(ModTags.Items.INGOTS_MANASTEEL), ModItems.manasteelSword, ModItems.manasteelPick, ModItems.manasteelAxe,
				ModItems.manasteelShovel, ModItems.manasteelShears);
		registerToolSetRecipes(consumer, Ingredient.fromTag(ModTags.Items.INGOTS_ELEMENTIUM), Ingredient.fromItems(ModItems.dreamwoodTwig),
				hasItem(ModTags.Items.INGOTS_ELEMENTIUM), ModItems.elementiumSword, ModItems.elementiumPick, ModItems.elementiumAxe,
				ModItems.elementiumShovel, ModItems.elementiumShears);

		ShapedRecipeBuilder.shapedRecipe(ModItems.terraSword)
				.key('S', ModItems.livingwoodTwig)
				.key('I', ModTags.Items.INGOTS_TERRASTEEL)
				.patternLine("I")
				.patternLine("I")
				.patternLine("S")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_TERRASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.terraPick)
				.key('T', ModItems.manaTablet)
				.key('I', ModTags.Items.INGOTS_TERRASTEEL)
				.key('L', ModItems.livingwoodTwig)
				.patternLine("ITI")
				.patternLine("ILI")
				.patternLine(" L ")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_TERRASTEEL))
				.build(WrapperResult.ofType(ManaUpgradeRecipe.SERIALIZER, consumer));
		ShapedRecipeBuilder.shapedRecipe(ModItems.terraAxe)
				.key('S', ModItems.livingwoodTwig)
				.key('T', ModTags.Items.INGOTS_TERRASTEEL)
				.key('G', Items.GLOWSTONE)
				.patternLine("TTG")
				.patternLine("TST")
				.patternLine(" S ")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_TERRASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.starSword)
				.key('A', ModItems.enderAirBottle)
				.key('D', ModTags.Items.GEMS_DRAGONSTONE)
				.key('T', ModItems.terraSword)
				.key('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.patternLine("  I")
				.patternLine("AD ")
				.patternLine("TA ")
				.addCriterion("has_item", hasItem(ModItems.terraAxe))
				.addCriterion("has_terrasteel", hasItem(ModTags.Items.INGOTS_TERRASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.thunderSword)
				.key('A', ModItems.enderAirBottle)
				.key('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.key('T', ModItems.terraSword)
				.key('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.patternLine("  I")
				.patternLine("AD ")
				.patternLine("TA ")
				.addCriterion("has_item", hasItem(ModItems.terraAxe))
				.addCriterion("has_terrasteel", hasItem(ModTags.Items.INGOTS_TERRASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.glassPick)
				.key('T', ModItems.livingwoodTwig)
				.key('G', Items.GLASS)
				.key('I', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine("GIG")
				.patternLine(" T ")
				.patternLine(" T ")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.livingwoodBow)
				.key('S', ModItems.manaString)
				.key('T', ModItems.livingwoodTwig)
				.patternLine(" TS")
				.patternLine("T S")
				.patternLine(" TS")
				.addCriterion("has_item", hasItem(ModItems.manaString))
				.addCriterion("has_twig", hasItem(ModItems.livingwoodTwig))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.crystalBow)
				.key('S', ModItems.manaString)
				.key('T', ModItems.livingwoodTwig)
				.key('D', ModTags.Items.GEMS_DRAGONSTONE)
				.patternLine(" DS")
				.patternLine("T S")
				.patternLine(" DS")
				.addCriterion("has_item", hasItem(ModTags.Items.GEMS_DRAGONSTONE))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.enderDagger)
				.key('P', ModItems.manaPearl)
				.key('S', ModTags.Items.INGOTS_MANASTEEL)
				.key('T', ModItems.livingwoodTwig)
				.patternLine("P")
				.patternLine("S")
				.patternLine("T")
				.addCriterion("has_item", hasItem(ModItems.manaPearl))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.enderHand)
				.key('P', ModItems.manaPearl)
				.key('E', Items.ENDER_CHEST)
				.key('L', Items.LEATHER)
				.key('O', Items.OBSIDIAN)
				.patternLine("PLO")
				.patternLine("LEL")
				.patternLine("OL ")
				.addCriterion("has_item", hasItem(Items.ENDER_CHEST))
				.addCriterion("has_alt_item", hasItem(Items.ENDER_EYE))
				.build(consumer);

		ShapelessRecipeBuilder.shapelessRecipe(ModItems.placeholder, 32)
				.addIngredient(Items.CRAFTING_TABLE)
				.addIngredient(ModTags.Items.LIVINGROCK)
				.addCriterion("has_dreamwood", hasItem(ModBlocks.dreamwood))
				.addCriterion("has_crafty_crate", hasItem(ModBlocks.craftCrate))
				.build(consumer);

		for (CratePattern pattern : CratePattern.values()) {
			if (pattern == CratePattern.NONE) {
				continue;
			}
			Item item = Registry.ITEM.getValue(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + pattern.getName().split("_", 2)[1])).get();
			String s = pattern.openSlots.stream().map(bool -> bool ? "R" : "P").collect(Collectors.joining());
			ShapedRecipeBuilder.shapedRecipe(item)
					.key('P', ModItems.placeholder)
					.key('R', Tags.Items.DUSTS_REDSTONE)
					.patternLine(s.substring(0, 3))
					.patternLine(s.substring(3, 6))
					.patternLine(s.substring(6, 9))
					.setGroup("botania:craft_pattern")
					.addCriterion("has_item", hasItem(ModItems.placeholder))
					.addCriterion("has_crafty_crate", hasItem(ModBlocks.craftCrate))
					.build(consumer);
		}

		ShapedRecipeBuilder.shapedRecipe(ModItems.pestleAndMortar)
				.key('B', Items.BOWL)
				.key('S', Tags.Items.RODS_WOODEN)
				.key('W', ItemTags.PLANKS)
				.patternLine(" S")
				.patternLine("W ")
				.patternLine("B ")
				.addCriterion("has_item", hasItem(ItemTags.PLANKS))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.manaGun)
				.key('S', ModBlocks.redstoneSpreader)
				.key('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.key('T', Items.TNT)
				.key('W', ModTags.Items.LIVINGWOOD)
				.key('M', ModTags.Items.RUNES_MANA)
				.patternLine("SMD")
				.patternLine(" WT")
				.patternLine("  W")
				.addCriterion("has_item", hasItem(ModTags.Items.GEMS_MANA_DIAMOND))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.dirtRod)
				.key('D', Items.DIRT)
				.key('T', ModItems.livingwoodTwig)
				.key('E', ModTags.Items.RUNES_EARTH)
				.patternLine("  D")
				.patternLine(" T ")
				.patternLine("E  ")
				.addCriterion("has_item", hasItem(ModTags.Items.RUNES_EARTH))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.terraformRod)
				.key('A', ModTags.Items.RUNES_AUTUMN)
				.key('R', ModItems.dirtRod)
				.key('S', ModTags.Items.RUNES_SPRING)
				.key('T', ModTags.Items.INGOTS_TERRASTEEL)
				.key('G', ModItems.grassSeeds)
				.key('W', ModTags.Items.RUNES_WINTER)
				.key('M', ModTags.Items.RUNES_SUMMER)
				.patternLine(" WT")
				.patternLine("ARS")
				.patternLine("GM ")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_TERRASTEEL))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.waterRod)
				.key('B', new FuzzyNBTIngredient(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER)))
				.key('R', ModTags.Items.RUNES_WATER)
				.key('T', ModItems.livingwoodTwig)
				.patternLine("  B")
				.patternLine(" T ")
				.patternLine("R  ")
				.addCriterion("has_item", hasItem(ModTags.Items.RUNES_WATER))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.rainbowRod)
				.key('P', ModItems.pixieDust)
				.key('D', ModTags.Items.GEMS_DRAGONSTONE)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.patternLine(" PD")
				.patternLine(" EP")
				.patternLine("E  ")
				.addCriterion("has_item", hasItem(ModTags.Items.GEMS_DRAGONSTONE))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.tornadoRod)
				.key('R', ModTags.Items.RUNES_AIR)
				.key('T', ModItems.livingwoodTwig)
				.key('F', Items.FEATHER)
				.patternLine("  F")
				.patternLine(" T ")
				.patternLine("R  ")
				.addCriterion("has_item", hasItem(ModTags.Items.RUNES_AIR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.fireRod)
				.key('R', ModTags.Items.RUNES_FIRE)
				.key('T', ModItems.livingwoodTwig)
				.key('F', Items.BLAZE_POWDER)
				.patternLine("  F")
				.patternLine(" T ")
				.patternLine("R  ")
				.addCriterion("has_item", hasItem(ModTags.Items.RUNES_FIRE))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.skyDirtRod)
				.addIngredient(ModItems.dirtRod)
				.addIngredient(ModItems.pixieDust)
				.addIngredient(ModTags.Items.RUNES_AIR)
				.addCriterion("has_item", hasItem(ModItems.pixieDust))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.diviningRod)
				.key('T', ModItems.livingwoodTwig)
				.key('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.patternLine(" TD")
				.patternLine(" TT")
				.patternLine("T  ")
				.addCriterion("has_item", hasItem(ModTags.Items.GEMS_MANA_DIAMOND))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.gravityRod)
				.key('T', ModItems.dreamwoodTwig)
				.key('D', ModTags.Items.GEMS_DRAGONSTONE)
				.key('W', Items.WHEAT)
				.patternLine(" TD")
				.patternLine(" WT")
				.patternLine("T  ")
				.addCriterion("has_item", hasItem(ModTags.Items.GEMS_DRAGONSTONE))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.missileRod)
				.key('D', ModTags.Items.GEMS_DRAGONSTONE)
				.key('T', ModItems.dreamwoodTwig)
				.key('G', ModItems.lifeEssence)
				.patternLine("GDD")
				.patternLine(" TD")
				.patternLine("T G")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.cobbleRod)
				.key('C', Tags.Items.COBBLESTONE)
				.key('T', ModItems.livingwoodTwig)
				.key('F', ModTags.Items.RUNES_FIRE)
				.key('W', ModTags.Items.RUNES_WATER)
				.patternLine(" FC")
				.patternLine(" TW")
				.patternLine("T  ")
				.addCriterion("has_item", hasItem(ModTags.Items.RUNES_FIRE))
				.addCriterion("has_alt_item", hasItem(ModTags.Items.RUNES_WATER))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.smeltRod)
				.key('B', Items.BLAZE_ROD)
				.key('T', ModItems.livingwoodTwig)
				.key('F', ModTags.Items.RUNES_FIRE)
				.patternLine(" BF")
				.patternLine(" TB")
				.patternLine("T  ")
				.addCriterion("has_item", hasItem(ModTags.Items.RUNES_FIRE))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.exchangeRod)
				.key('R', ModTags.Items.RUNES_SLOTH)
				.key('S', Tags.Items.STONE)
				.key('T', ModItems.livingwoodTwig)
				.patternLine(" SR")
				.patternLine(" TS")
				.patternLine("T  ")
				.addCriterion("has_item", hasItem(ModTags.Items.RUNES_SLOTH))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.laputaShard)
				.key('P', Tags.Items.GEMS_PRISMARINE)
				.key('A', ModTags.Items.RUNES_AIR)
				.key('S', ModItems.lifeEssence)
				.key('D', ModTags.Items.GEMS_DRAGONSTONE)
				.key('E', ModTags.Items.RUNES_EARTH)
				.key('F', ModTags.Items.MUNDANE_FLOATING_FLOWERS)
				.patternLine("SFS")
				.patternLine("PDP")
				.patternLine("ASE")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(WrapperResult.transformJson(consumer, json -> json.getAsJsonObject("result").addProperty("nbt", "{level:0}")),
						"botania:laputa_shard_0");
		for (int i = 1; i < 20; i++) {
			final int outputLevel = i;
			ItemStack stack = new ItemStack(ModItems.laputaShard);
			stack.getOrCreateTag().putInt("level", i - 1);
			ShapelessRecipeBuilder.shapelessRecipe(ModItems.laputaShard)
					.addIngredient(ModItems.lifeEssence)
					.addIngredient(new FuzzyNBTIngredient(stack, i == 1))
					.setGroup("botania:laputa_shard_upgrade")
					.addCriterion("has_item", hasItem(ModItems.laputaShard))
					.build(WrapperResult.transformJson(consumer, json -> json.getAsJsonObject("result").addProperty("nbt", "{level:" + outputLevel + "}")
					), "botania:laputa_shard_" + i);
		}

		ShapedRecipeBuilder.shapedRecipe(ModItems.craftingHalo)
				.key('P', ModItems.manaPearl)
				.key('C', Items.CRAFTING_TABLE)
				.key('I', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine(" P ")
				.patternLine("ICI")
				.patternLine(" I ")
				.addCriterion("has_item", hasItem(ModItems.manaPearl))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.clip)
				.key('D', ModBlocks.dreamwood)
				.patternLine(" D ")
				.patternLine("D D")
				.patternLine("DD ")
				.addCriterion("has_item", hasItem(ModBlocks.dreamwood))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.spellCloth)
				.key('P', ModItems.manaPearl)
				.key('C', ModItems.manaweaveCloth)
				.patternLine(" C ")
				.patternLine("CPC")
				.patternLine(" C ")
				.addCriterion("has_item", hasItem(ModItems.manaweaveCloth))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.flowerBag)
				.key('P', ModTags.Items.PETALS)
				.key('W', ItemTags.WOOL)
				.patternLine("WPW")
				.patternLine("W W")
				.patternLine(" W ")
				.addCriterion("has_item", hasItem(ModTags.Items.MYSTICAL_FLOWERS))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.poolMinecart)
				.addIngredient(Items.MINECART)
				.addIngredient(ModBlocks.manaPool)
				.addCriterion("has_item", hasItem(Items.MINECART))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.blackHoleTalisman)
				.key('A', ModItems.enderAirBottle)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.key('G', ModItems.lifeEssence)
				.patternLine(" G ")
				.patternLine("EAE")
				.patternLine(" E ")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.temperanceStone)
				.key('R', ModTags.Items.RUNES_EARTH)
				.key('S', Tags.Items.STONE)
				.patternLine(" S ")
				.patternLine("SRS")
				.patternLine(" S ")
				.addCriterion("has_item", hasItem(ModTags.Items.RUNES_EARTH))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.incenseStick)
				.key('B', Items.BLAZE_POWDER)
				.key('T', ModItems.livingwoodTwig)
				.key('G', Items.GHAST_TEAR)
				.patternLine("  G")
				.patternLine(" B ")
				.patternLine("T  ")
				.addCriterion("has_item", hasItem(Items.GHAST_TEAR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.obedienceStick)
				.key('T', ModItems.livingwoodTwig)
				.key('M', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine("  M")
				.patternLine(" T ")
				.patternLine("T  ")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.slimeBottle)
				.key('S', Items.SLIME_BALL)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.key('G', ModBlocks.elfGlass)
				.patternLine("EGE")
				.patternLine("ESE")
				.patternLine(" E ")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_ELEMENTIUM))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.autocraftingHalo)
				.addIngredient(ModItems.craftingHalo)
				.addIngredient(ModTags.Items.GEMS_MANA_DIAMOND)
				.addCriterion("has_item", hasItem(ModTags.Items.GEMS_MANA_DIAMOND))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.sextant)
				.key('T', ModItems.livingwoodTwig)
				.key('I', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine(" TI")
				.patternLine(" TT")
				.patternLine("III")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.baubleBox)
				.key('C', Tags.Items.CHESTS_WOODEN)
				.key('G', Tags.Items.INGOTS_GOLD)
				.key('M', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine(" M ")
				.patternLine("MCG")
				.patternLine(" M ")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.astrolabe)
				.key('D', ModBlocks.dreamwood)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.key('G', ModItems.lifeEssence)
				.patternLine(" EG")
				.patternLine("EEE")
				.patternLine("GED")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(consumer);

	}

	private void registerTrinkets(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModItems.tinyPlanet)
				.key('P', ModItems.manaPearl)
				.key('S', Tags.Items.STONE)
				.key('L', ModTags.Items.LIVINGROCK)
				.patternLine("LSL")
				.patternLine("SPS")
				.patternLine("LSL")
				.addCriterion("has_item", hasItem(ModItems.manaPearl))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.manaRing)
				.key('T', ModItems.manaTablet)
				.key('I', ModItems.manaSteel)
				.patternLine("TI ")
				.patternLine("I I")
				.patternLine(" I ")
				.addCriterion("has_item", hasItem(ModItems.manaTablet))
				.build(WrapperResult.ofType(ManaUpgradeRecipe.SERIALIZER, consumer));
		ShapedRecipeBuilder.shapedRecipe(ModItems.auraRing)
				.key('R', ModTags.Items.RUNES_MANA)
				.key('I', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine("RI ")
				.patternLine("I I")
				.patternLine(" I ")
				.addCriterion("has_item", hasItem(ModTags.Items.RUNES_MANA))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.manaRingGreater)
				.addIngredient(ModTags.Items.INGOTS_TERRASTEEL)
				.addIngredient(ModItems.manaRing)
				.addCriterion("has_item", hasItem(ModItems.terrasteel))
				.build(WrapperResult.ofType(ShapelessManaUpgradeRecipe.SERIALIZER, consumer));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.auraRingGreater)
				.addIngredient(ModTags.Items.INGOTS_TERRASTEEL)
				.addIngredient(ModItems.auraRing)
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_TERRASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.travelBelt)
				.key('A', ModTags.Items.RUNES_AIR)
				.key('S', ModTags.Items.INGOTS_MANASTEEL)
				.key('E', ModTags.Items.RUNES_EARTH)
				.key('L', Items.LEATHER)
				.patternLine("EL ")
				.patternLine("L L")
				.patternLine("SLA")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.knockbackBelt)
				.key('A', ModTags.Items.RUNES_FIRE)
				.key('S', ModTags.Items.INGOTS_MANASTEEL)
				.key('E', ModTags.Items.RUNES_EARTH)
				.key('L', Items.LEATHER)
				.patternLine("AL ")
				.patternLine("L L")
				.patternLine("SLE")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.icePendant)
				.key('R', ModTags.Items.RUNES_WATER)
				.key('S', ModItems.manaString)
				.key('W', ModTags.Items.RUNES_WINTER)
				.key('M', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine("WS ")
				.patternLine("S S")
				.patternLine("MSR")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.lavaPendant)
				.key('S', ModItems.manaString)
				.key('D', ModTags.Items.INGOTS_MANASTEEL)
				.key('F', ModTags.Items.RUNES_FIRE)
				.key('M', ModTags.Items.RUNES_SUMMER)
				.patternLine("MS ")
				.patternLine("S S")
				.patternLine("DSF")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.magnetRing)
				.key('L', ModItems.lensMagnet)
				.key('M', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine("LM ")
				.patternLine("M M")
				.patternLine(" M ")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.waterRing)
				.key('P', Items.PUFFERFISH)
				.key('C', Items.COD)
				.key('H', Items.HEART_OF_THE_SEA)
				.key('W', ModTags.Items.RUNES_WATER)
				.key('M', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine("WMP")
				.patternLine("MHM")
				.patternLine("CM ")
				.addCriterion("has_item", hasItem(Items.HEART_OF_THE_SEA))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.miningRing)
				.key('P', Items.GOLDEN_PICKAXE)
				.key('E', ModTags.Items.RUNES_EARTH)
				.key('M', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine("EMP")
				.patternLine("M M")
				.patternLine(" M ")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.divaCharm)
				.key('P', ModItems.tinyPlanet)
				.key('G', Tags.Items.INGOTS_GOLD)
				.key('H', ModTags.Items.RUNES_PRIDE)
				.key('L', ModItems.lifeEssence)
				.patternLine("LGP")
				.patternLine(" HG")
				.patternLine(" GL")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.flightTiara)
				.key('E', ModItems.enderAirBottle)
				.key('F', Items.FEATHER)
				.key('I', ModTags.Items.INGOTS_ELEMENTIUM)
				.key('L', ModItems.lifeEssence)
				.patternLine("LLL")
				.patternLine("ILI")
				.patternLine("FEF")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(consumer, "botania:flighttiara_0");

		// Normal quartz and not Tags.Items.QUARTZ because the recipes conflict.
		Item[] items = { Items.QUARTZ, ModItems.darkQuartz, ModItems.manaQuartz, ModItems.blazeQuartz,
				ModItems.lavenderQuartz, ModItems.redQuartz, ModItems.elfQuartz, ModItems.sunnyQuartz };
		for (int i = 0; i < items.length; i++) {
			int tiaraType = i + 1;
			ShapelessRecipeBuilder.shapelessRecipe(ModItems.flightTiara)
					.addIngredient(ModItems.flightTiara)
					.addIngredient(items[i])
					.setGroup("botania:flight_tiara_wings")
					.addCriterion("has_item", hasItem(ModItems.flightTiara))
					.build(WrapperResult.transformJson(consumer, json -> json.getAsJsonObject("result").addProperty("nbt", "{variant:" + tiaraType + "}")
					), "botania:flighttiara_" + tiaraType);
		}
		ShapedRecipeBuilder.shapedRecipe(ModItems.pixieRing)
				.key('D', ModItems.pixieDust)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.patternLine("DE ")
				.patternLine("E E")
				.patternLine(" E ")
				.addCriterion("has_item", hasItem(ModItems.pixieDust))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.superTravelBelt)
				.key('S', ModItems.travelBelt)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.key('L', ModItems.lifeEssence)
				.patternLine("E  ")
				.patternLine(" S ")
				.patternLine("L E")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.reachRing)
				.key('R', ModTags.Items.RUNES_PRIDE)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.patternLine("RE ")
				.patternLine("E E")
				.patternLine(" E ")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_ELEMENTIUM))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.itemFinder)
				.key('E', Tags.Items.GEMS_EMERALD)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('Y', Items.ENDER_EYE)
				.patternLine(" I ")
				.patternLine("IYI")
				.patternLine("IEI")
				.addCriterion("has_item", hasItem(Items.ENDER_EYE))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.superLavaPendant)
				.key('P', ModItems.lavaPendant)
				.key('B', Items.BLAZE_ROD)
				.key('G', ModItems.lifeEssence)
				.key('N', Items.NETHER_BRICK)
				.patternLine("BBB")
				.patternLine("BPB")
				.patternLine("NGN")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.bloodPendant)
				.key('P', Tags.Items.GEMS_PRISMARINE)
				.key('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.key('G', Items.GHAST_TEAR)
				.patternLine(" P ")
				.patternLine("PGP")
				.patternLine("DP ")
				.addCriterion("has_item", hasItem(Items.GHAST_TEAR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.holyCloak)
				.key('S', ModItems.lifeEssence)
				.key('W', Items.WHITE_WOOL)
				.key('G', Tags.Items.DUSTS_GLOWSTONE)
				.patternLine("WWW")
				.patternLine("GWG")
				.patternLine("GSG")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.unholyCloak)
				.key('R', Tags.Items.DUSTS_REDSTONE)
				.key('S', ModItems.lifeEssence)
				.key('W', Items.BLACK_WOOL)
				.patternLine("WWW")
				.patternLine("RWR")
				.patternLine("RSR")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.balanceCloak)
				.key('R', Tags.Items.GEMS_EMERALD)
				.key('S', ModItems.lifeEssence)
				.key('W', Items.LIGHT_GRAY_WOOL)
				.patternLine("WWW")
				.patternLine("RWR")
				.patternLine("RSR")
				.addCriterion("has_item", hasItem(ModItems.lifeEssence))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.monocle)
				.key('G', ModBlocks.manaGlass)
				.key('I', ModTags.Items.INGOTS_MANASTEEL)
				.key('N', Items.GOLD_NUGGET)
				.patternLine("GN")
				.patternLine("IN")
				.patternLine(" N")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.swapRing)
				.key('C', Items.CLAY)
				.key('M', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine("CM ")
				.patternLine("M M")
				.patternLine(" M ")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.magnetRingGreater)
				.addIngredient(ModTags.Items.INGOTS_TERRASTEEL)
				.addIngredient(ModItems.magnetRing)
				.addCriterion("has_item", hasItem(ModItems.magnetRing))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.speedUpBelt)
				.key('P', ModItems.grassSeeds)
				.key('B', ModItems.travelBelt)
				.key('S', Items.SUGAR)
				.key('M', Items.MAP)
				.patternLine(" M ")
				.patternLine("PBP")
				.patternLine(" S ")
				.addCriterion("has_item", hasItem(Items.MAP))
				.addCriterion("has_alt_item", hasItem(ModItems.travelBelt))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.dodgeRing)
				.key('R', ModTags.Items.RUNES_AIR)
				.key('E', Tags.Items.GEMS_EMERALD)
				.key('M', ModTags.Items.INGOTS_MANASTEEL)
				.patternLine("EM ")
				.patternLine("M M")
				.patternLine(" MR")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.invisibilityCloak)
				.key('P', ModItems.manaPearl)
				.key('C', Items.PRISMARINE_CRYSTALS)
				.key('W', Items.WHITE_WOOL)
				.key('G', ModBlocks.manaGlass)
				.patternLine("CWC")
				.patternLine("GWG")
				.patternLine("GPG")
				.addCriterion("has_item", hasItem(ModItems.manaPearl))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.cloudPendant)
				.key('S', ModItems.manaString)
				.key('D', ModTags.Items.INGOTS_MANASTEEL)
				.key('F', ModTags.Items.RUNES_AIR)
				.key('M', ModTags.Items.RUNES_AUTUMN)
				.patternLine("MS ")
				.patternLine("S S")
				.patternLine("DSF")
				.addCriterion("has_item", hasItem(ModItems.manaString))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.superCloudPendant)
				.key('P', ModItems.cloudPendant)
				.key('B', Items.GHAST_TEAR)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.key('G', ModItems.lifeEssence)
				.key('N', Items.WHITE_WOOL)
				.patternLine("BEB")
				.patternLine("BPB")
				.patternLine("NGN")
				.addCriterion("has_item", hasItem(ModItems.cloudPendant))
				.addCriterion("has_alt_item", hasItem(ModItems.lifeEssence))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.thirdEye)
				.key('Q', Items.QUARTZ_BLOCK)
				.key('R', Items.GOLDEN_CARROT)
				.key('S', ModTags.Items.RUNES_EARTH)
				.key('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.key('E', Items.ENDER_EYE)
				.patternLine("RSR")
				.patternLine("QEQ")
				.patternLine("RDR")
				.addCriterion("has_item", hasItem(ModTags.Items.GEMS_MANA_DIAMOND))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.goddessCharm)
				.key('P', ModTags.Items.PETALS_PINK)
				.key('A', ModTags.Items.RUNES_WATER)
				.key('S', ModTags.Items.RUNES_SPRING)
				.key('D', ModTags.Items.GEMS_MANA_DIAMOND)
				.patternLine(" P ")
				.patternLine(" P ")
				.patternLine("ADS")
				.addCriterion("has_item", hasItem(ModTags.Items.GEMS_MANA_DIAMOND))
				.build(consumer);

	}

	private void registerCorporeaAndRedString(Consumer<IFinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.redString)
				.addIngredient(Items.STRING)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.addIngredient(ModItems.pixieDust)
				.addIngredient(ModItems.enderAirBottle)
				.setGroup("botania:red_string")
				.addCriterion("has_item", hasItem(ModItems.enderAirBottle))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.redString)
				.addIngredient(Items.STRING)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.addIngredient(ModItems.pixieDust)
				.addIngredient(ModItems.enderAirBottle)
				.addIngredient(Items.PUMPKIN)
				.setGroup("botania:red_string")
				.addCriterion("has_item", hasItem(ModItems.enderAirBottle))
				.build(consumer, "botania:red_string_alt");
		registerRedStringBlock(consumer, ModBlocks.redStringContainer, Ingredient.fromTag(Tags.Items.CHESTS_WOODEN), hasItem(Tags.Items.CHESTS_WOODEN));
		registerRedStringBlock(consumer, ModBlocks.redStringDispenser, Ingredient.fromItems(Items.DISPENSER), hasItem(Items.DISPENSER));
		registerRedStringBlock(consumer, ModBlocks.redStringFertilizer, Ingredient.fromItems(ModItems.fertilizer), hasItem(ModItems.fertilizer));
		registerRedStringBlock(consumer, ModBlocks.redStringComparator, Ingredient.fromItems(Items.COMPARATOR), hasItem(Items.COMPARATOR));
		registerRedStringBlock(consumer, ModBlocks.redStringRelay, Ingredient.fromItems(ModBlocks.manaSpreader), hasItem(ModBlocks.manaSpreader));
		registerRedStringBlock(consumer, ModBlocks.redStringInterceptor, Ingredient.fromItems(Items.STONE_BUTTON), hasItem(Items.STONE_BUTTON));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.corporeaSpark)
				.addIngredient(ModItems.spark)
				.addIngredient(ModItems.pixieDust)
				.addIngredient(ModItems.enderAirBottle)
				.addCriterion("has_item", hasItem(ModItems.enderAirBottle))
				.addCriterion("has_alt_item", hasItem(ModItems.pixieDust))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.corporeaSparkMaster)
				.addIngredient(ModItems.corporeaSpark)
				.addIngredient(ModTags.Items.GEMS_DRAGONSTONE)
				.addCriterion("has_item", hasItem(ModItems.corporeaSpark))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.corporeaIndex)
				.key('A', ModItems.enderAirBottle)
				.key('S', ModItems.corporeaSpark)
				.key('D', ModTags.Items.GEMS_DRAGONSTONE)
				.key('O', Items.OBSIDIAN)
				.patternLine("AOA")
				.patternLine("OSO")
				.patternLine("DOD")
				.addCriterion("has_item", hasItem(ModItems.corporeaSpark))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.corporeaFunnel)
				.addIngredient(Items.DROPPER)
				.addIngredient(ModItems.corporeaSpark)
				.addCriterion("has_item", hasItem(ModItems.corporeaSpark))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.corporeaInterceptor)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.addIngredient(ModItems.corporeaSpark)
				.addCriterion("has_item", hasItem(ModItems.corporeaSpark))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.corporeaCrystalCube)
				.key('C', ModItems.corporeaSpark)
				.key('G', ModBlocks.elfGlass)
				.key('W', ModBlocks.dreamwood)
				.patternLine("C")
				.patternLine("G")
				.patternLine("W")
				.addCriterion("has_item", hasItem(ModItems.corporeaSpark))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.corporeaRetainer)
				.addIngredient(Tags.Items.CHESTS_WOODEN)
				.addIngredient(ModItems.corporeaSpark)
				.addCriterion("has_item", hasItem(ModItems.corporeaSpark))
				.build(consumer);

	}

	private void registerLenses(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModItems.lensNormal)
				.key('S', ModTags.Items.INGOTS_MANASTEEL)
				.key('G', Ingredient.fromItems(Items.GLASS, Items.GLASS_PANE))
				.patternLine(" S ")
				.patternLine("SGS")
				.patternLine(" S ")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_MANASTEEL))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensSpeed)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(ModTags.Items.RUNES_AIR)
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensPower)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(ModTags.Items.RUNES_FIRE)
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensTime)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(ModTags.Items.RUNES_EARTH)
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensEfficiency)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(ModTags.Items.RUNES_WATER)
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensBounce)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(ModTags.Items.RUNES_SUMMER)
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensGravity)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(ModTags.Items.RUNES_WINTER)
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.lensMine)
				.key('P', Items.PISTON)
				.key('A', Tags.Items.GEMS_LAPIS)
				.key('R', Tags.Items.DUSTS_REDSTONE)
				.key('L', ModItems.lensNormal)
				.patternLine(" P ")
				.patternLine("ALA")
				.patternLine(" R ")
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensDamage)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(ModTags.Items.RUNES_WRATH)
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensPhantom)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(ModBlocks.abstrusePlatform)
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensMagnet)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(Tags.Items.INGOTS_IRON)
				.addIngredient(Tags.Items.INGOTS_GOLD)
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensExplosive)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(ModTags.Items.RUNES_ENVY)
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.lensInfluence)
				.key('P', Tags.Items.GEMS_PRISMARINE)
				.key('R', ModTags.Items.RUNES_AIR)
				.key('L', ModItems.lensNormal)
				.patternLine("PRP")
				.patternLine("PLP")
				.patternLine("PPP")
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.lensWeight)
				.key('P', Tags.Items.GEMS_PRISMARINE)
				.key('R', ModTags.Items.RUNES_WATER)
				.key('L', ModItems.lensNormal)
				.patternLine("PPP")
				.patternLine("PLP")
				.patternLine("PRP")
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.lensPaint)
				.key('E', ModTags.Items.INGOTS_ELEMENTIUM)
				.key('W', ItemTags.WOOL)
				.key('L', ModItems.lensNormal)
				.patternLine(" E ")
				.patternLine("WLW")
				.patternLine(" E ")
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensFire)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(Items.FIRE_CHARGE)
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensPiston)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(ModBlocks.pistonRelay)
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.lensLight)
				.key('F', Items.FIRE_CHARGE)
				.key('G', Items.GLOWSTONE)
				.key('L', ModItems.lensNormal)
				.patternLine("GFG")
				.patternLine("FLF")
				.patternLine("GFG")
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.lensLight)
				.key('F', Items.FIRE_CHARGE)
				.key('G', Items.GLOWSTONE)
				.key('L', ModItems.lensNormal)
				.patternLine("FGF")
				.patternLine("GLG")
				.patternLine("FGF")
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer, "botania:lens_light_alt");
		ShapedRecipeBuilder.shapedRecipe(ModItems.lensMessenger)
				.key('P', Items.PAPER)
				.key('L', ModItems.lensNormal)
				.patternLine(" P ")
				.patternLine("PLP")
				.patternLine(" P ")
				.addCriterion("has_item", hasItem(ModItems.lensNormal))
				.build(consumer);

		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensWarp)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(ModItems.pixieDust)
				.addCriterion("has_item", hasItem(ModItems.pixieDust))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensRedirect)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(ModTags.Items.LIVINGWOOD)
				.addIngredient(ModTags.Items.INGOTS_ELEMENTIUM)
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_ELEMENTIUM))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensFirework)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(Items.FIREWORK_ROCKET)
				.addIngredient(ModTags.Items.INGOTS_ELEMENTIUM)
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_ELEMENTIUM))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensFlare)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(ModBlocks.elfGlass)
				.addIngredient(ModTags.Items.INGOTS_ELEMENTIUM)
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_ELEMENTIUM))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.lensTripwire)
				.addIngredient(ModItems.lensNormal)
				.addIngredient(Items.TRIPWIRE_HOOK)
				.addIngredient(ModTags.Items.INGOTS_ELEMENTIUM)
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_ELEMENTIUM))
				.build(consumer);
	}

	private void registerFloatingFlowers(Consumer<IFinishedRecipe> consumer) {
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

	private void registerConversions(Consumer<IFinishedRecipe> consumer) {
		compression(ModItems.manaSteel, ModTags.Items.NUGGETS_MANASTEEL)
				.build(consumer, prefix("conversions/manasteel_from_nuggets"));
		compression(ModItems.elementium, ModTags.Items.NUGGETS_ELEMENTIUM)
				.build(consumer, prefix("conversions/elementium_from_nuggets"));
		compression(ModItems.terrasteel, ModTags.Items.NUGGETS_TERRASTEEL)
				.build(consumer, prefix("conversions/terrasteel_from_nugget"));
		compression(ModBlocks.manasteelBlock, ModTags.Items.INGOTS_MANASTEEL).build(consumer);
		compression(ModBlocks.terrasteelBlock, ModTags.Items.INGOTS_TERRASTEEL).build(consumer);
		compression(ModBlocks.elementiumBlock, ModTags.Items.INGOTS_ELEMENTIUM).build(consumer);
		compression(ModBlocks.manaDiamondBlock, ModTags.Items.GEMS_MANA_DIAMOND).build(consumer);
		compression(ModBlocks.dragonstoneBlock, ModTags.Items.GEMS_DRAGONSTONE).build(consumer);
		compression(ModBlocks.blazeBlock, Tags.Items.RODS_BLAZE).build(consumer);

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

	private void registerDecor(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.livingrockBrick, 4)
				.key('R', ModTags.Items.LIVINGROCK)
				.patternLine("RR")
				.patternLine("RR")
				.addCriterion("has_item", hasItem(ModTags.Items.LIVINGROCK))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.livingrockBrickChiseled, 4)
				.key('R', ModBlocks.livingrockBrick)
				.patternLine("RR")
				.patternLine("RR")
				.addCriterion("has_item", hasItem(ModBlocks.livingrockBrick))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.livingrockBrickCracked, 2)
				.addIngredient(ModBlocks.livingrockBrick)
				.addIngredient(Tags.Items.COBBLESTONE)
				.addCriterion("has_item", hasItem(ModBlocks.livingrockBrick))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.livingrockBrickMossy)
				.addIngredient(ModBlocks.livingrockBrick)
				.addIngredient(Items.WHEAT_SEEDS)
				.addCriterion("has_item", hasItem(ModBlocks.livingrockBrick))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.livingwoodPlanksMossy)
				.addIngredient(ModBlocks.livingwoodPlanks)
				.addIngredient(Items.WHEAT_SEEDS)
				.addCriterion("has_item", hasItem(ModBlocks.livingwoodPlanks))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.livingwoodFramed, 4)
				.key('W', ModBlocks.livingwoodPlanks)
				.patternLine("WW")
				.patternLine("WW")
				.addCriterion("has_item", hasItem(ModBlocks.livingwoodPlanks))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.livingwoodGlimmering)
				.addIngredient(ModTags.Items.LIVINGWOOD)
				.addIngredient(Tags.Items.DUSTS_GLOWSTONE)
				.addCriterion("has_item", hasItem(ModTags.Items.LIVINGWOOD))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.dreamwoodPlanksMossy)
				.addIngredient(ModBlocks.dreamwoodPlanks)
				.addIngredient(Items.WHEAT_SEEDS)
				.addCriterion("has_item", hasItem(ModBlocks.dreamwoodPlanks))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.dreamwoodFramed, 4)
				.key('W', ModBlocks.dreamwoodPlanks)
				.patternLine("WW")
				.patternLine("WW")
				.addCriterion("has_item", hasItem(ModBlocks.dreamwoodPlanks))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.dreamwoodGlimmering)
				.addIngredient(ModBlocks.dreamwood)
				.addIngredient(Tags.Items.DUSTS_GLOWSTONE)
				.addCriterion("has_item", hasItem(ModBlocks.dreamwood))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.shimmerrock)
				.addIngredient(ModTags.Items.LIVINGROCK)
				.addIngredient(ModBlocks.bifrostPerm)
				.addCriterion("has_item", hasItem(ModBlocks.bifrostPerm))
				.addCriterion("has_alt_item", hasItem(ModItems.rainbowRod))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.shimmerwoodPlanks)
				.addIngredient(ModBlocks.dreamwoodPlanks)
				.addIngredient(ModBlocks.bifrostPerm)
				.addCriterion("has_item", hasItem(ModBlocks.bifrostPerm))
				.addCriterion("has_alt_item", hasItem(ModItems.rainbowRod))
				.build(consumer);

		registerForQuartz(consumer, LibBlockNames.QUARTZ_DARK, ModItems.darkQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_MANA, ModItems.manaQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_BLAZE, ModItems.blazeQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_LAVENDER, ModItems.lavenderQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_RED, ModItems.redQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_ELF, ModItems.elfQuartz);
		registerForQuartz(consumer, LibBlockNames.QUARTZ_SUNNY, ModItems.sunnyQuartz);

		registerForWood(consumer, LibBlockNames.LIVING_WOOD);
		registerForWood(consumer, LibBlockNames.DREAM_WOOD);

		stairs(ModFluffBlocks.livingrockStairs, ModBlocks.livingrock).build(consumer);
		slabShape(ModFluffBlocks.livingrockSlab, ModBlocks.livingrock).build(consumer);
		wallShape(ModFluffBlocks.livingrockWall, ModBlocks.livingrock, 6).build(consumer);

		stairs(ModFluffBlocks.livingrockBrickStairs, ModBlocks.livingrockBrick).build(consumer);
		slabShape(ModFluffBlocks.livingrockBrickSlab, ModBlocks.livingrockBrick).build(consumer);
		stairs(ModFluffBlocks.shimmerrockStairs, ModBlocks.shimmerrock).build(consumer);
		slabShape(ModFluffBlocks.shimmerrockSlab, ModBlocks.shimmerrock).build(consumer);
		stairs(ModFluffBlocks.livingwoodPlankStairs, ModBlocks.livingwoodPlanks).build(consumer);
		slabShape(ModFluffBlocks.livingwoodPlankSlab, ModBlocks.livingwoodPlanks).build(consumer);
		stairs(ModFluffBlocks.dreamwoodPlankStairs, ModBlocks.dreamwoodPlanks).build(consumer);
		slabShape(ModFluffBlocks.dreamwoodPlankSlab, ModBlocks.dreamwoodPlanks).build(consumer);
		stairs(ModFluffBlocks.shimmerwoodPlankStairs, ModBlocks.shimmerwoodPlanks).build(consumer);
		slabShape(ModFluffBlocks.shimmerwoodPlankSlab, ModBlocks.shimmerwoodPlanks).build(consumer);

		ringShape(ModBlocks.livingwoodPatternFramed, ModBlocks.livingwoodPlanks).build(consumer);
		ringShape(ModBlocks.dreamwoodPatternFramed, ModBlocks.dreamwoodPlanks).build(consumer);

		for (String variant : LibBlockNames.METAMORPHIC_VARIANTS) {
			registerForMetamorphic(consumer, variant);
		}

		Item[] pavementIngredients = { Items.AIR, Items.COAL, Items.LAPIS_LAZULI, Items.REDSTONE, Items.WHEAT, Items.SLIME_BALL };
		for (int i = 0; i < pavementIngredients.length; i++) {
			registerForPavement(consumer, LibBlockNames.PAVEMENT_VARIANTS[i], pavementIngredients[i]);
		}

		wallShape(ModFluffBlocks.managlassPane, ModBlocks.manaGlass, 16).build(consumer);
		wallShape(ModFluffBlocks.alfglassPane, ModBlocks.elfGlass, 16).build(consumer);
		wallShape(ModFluffBlocks.bifrostPane, ModBlocks.bifrostPerm, 16).build(consumer);

		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.azulejo0)
				.addIngredient(Tags.Items.DYES_BLUE)
				.addIngredient(ModTags.Items.BLOCKS_QUARTZ)
				.addCriterion("has_item", hasItem(Tags.Items.DYES_BLUE))
				.build(consumer);

		List<Item> allAzulejos = IntStream.range(0, 16).mapToObj(i -> "azulejo_" + i)
				.map(ResourceLocationHelper::prefix)
				.map(Registry.ITEM::getValue)
				.map(Optional::get)
				.collect(Collectors.toList());
		for (int i = 0; i < allAzulejos.size(); i++) {
			int resultIndex = i + 1 == allAzulejos.size() ? 0 : i + 1;
			String recipeName = "azulejo_" + resultIndex;
			if (resultIndex == 0) {
				recipeName += "_alt";
			}
			ShapelessRecipeBuilder.shapelessRecipe(allAzulejos.get(resultIndex))
					.addIngredient(allAzulejos.get(i))
					.addCriterion("has_azulejo", hasItem(ModBlocks.azulejo0))
					.setGroup("botania:azulejo_cycling")
					.build(consumer, prefix(recipeName));
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
		cosmeticBauble(consumer, ModItems.fourLeafClover, ModItems.whiteDye);
		cosmeticBauble(consumer, ModItems.clockEye, ModItems.orangeDye);
		cosmeticBauble(consumer, ModItems.unicornHorn, ModItems.magentaDye);
		cosmeticBauble(consumer, ModItems.devilHorns, ModItems.lightBlueDye);
		cosmeticBauble(consumer, ModItems.hyperPlus, ModItems.yellowDye);
		cosmeticBauble(consumer, ModItems.botanistEmblem, ModItems.limeDye);
		cosmeticBauble(consumer, ModItems.ancientMask, ModItems.pinkDye);
		cosmeticBauble(consumer, ModItems.eerieMask, ModItems.grayDye);
		cosmeticBauble(consumer, ModItems.alienAntenna, ModItems.lightGrayDye);
		cosmeticBauble(consumer, ModItems.anaglyphGlasses, ModItems.cyanDye);
		cosmeticBauble(consumer, ModItems.orangeShades, ModItems.purpleDye);
		cosmeticBauble(consumer, ModItems.grouchoGlasses, ModItems.blueDye);
		cosmeticBauble(consumer, ModItems.thickEyebrows, ModItems.brownDye);
		cosmeticBauble(consumer, ModItems.lusitanicShield, ModItems.greenDye);
		cosmeticBauble(consumer, ModItems.tinyPotatoMask, ModItems.redDye);
		cosmeticBauble(consumer, ModItems.questgiverMark, ModItems.blackDye);
		cosmeticBauble(consumer, ModItems.thinkingHand, ModBlocks.tinyPotato);
	}

	private void registerSimpleArmorSet(Consumer<IFinishedRecipe> consumer, Ingredient item, String variant,
			ICriterionInstance criterion) {
		Item helmet = Registry.ITEM.getValue(prefix(variant + "_helmet")).get();
		Item chestplate = Registry.ITEM.getValue(prefix(variant + "_chestplate")).get();
		Item leggings = Registry.ITEM.getValue(prefix(variant + "_leggings")).get();
		Item boots = Registry.ITEM.getValue(prefix(variant + "_boots")).get();
		ShapedRecipeBuilder.shapedRecipe(helmet)
				.key('S', item)
				.patternLine("SSS")
				.patternLine("S S")
				.addCriterion("has_item", criterion)
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(chestplate)
				.key('S', item)
				.patternLine("S S")
				.patternLine("SSS")
				.patternLine("SSS")
				.addCriterion("has_item", criterion)
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(leggings)
				.key('S', item)
				.patternLine("SSS")
				.patternLine("S S")
				.patternLine("S S")
				.addCriterion("has_item", criterion)
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(boots)
				.key('S', item)
				.patternLine("S S")
				.patternLine("S S")
				.addCriterion("has_item", criterion)
				.build(consumer);
	}

	private void registerToolSetRecipes(Consumer<IFinishedRecipe> consumer, Ingredient item, Ingredient stick,
			ICriterionInstance criterion, IItemProvider sword, IItemProvider pickaxe,
			IItemProvider axe, IItemProvider shovel, IItemProvider shears) {
		ShapedRecipeBuilder.shapedRecipe(pickaxe)
				.key('S', item)
				.key('T', stick)
				.patternLine("SSS")
				.patternLine(" T ")
				.patternLine(" T ")
				.addCriterion("has_item", criterion)
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(shovel)
				.key('S', item)
				.key('T', stick)
				.patternLine("S")
				.patternLine("T")
				.patternLine("T")
				.addCriterion("has_item", criterion)
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(axe)
				.key('S', item)
				.key('T', stick)
				.patternLine("SS")
				.patternLine("TS")
				.patternLine("T ")
				.addCriterion("has_item", criterion)
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(sword)
				.key('S', item)
				.key('T', stick)
				.patternLine("S")
				.patternLine("S")
				.patternLine("T")
				.addCriterion("has_item", criterion)
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(shears)
				.key('S', item)
				.patternLine("S ")
				.patternLine(" S")
				.addCriterion("has_item", criterion)
				.build(consumer);

	}

	private void registerTerrasteelUpgradeRecipe(Consumer<IFinishedRecipe> consumer, IItemProvider output,
			IItemProvider upgradedInput, Tag<Item> runeInput) {
		ShapedRecipeBuilder.shapedRecipe(output)
				.key('T', ModItems.livingwoodTwig)
				.key('S', ModTags.Items.INGOTS_TERRASTEEL)
				.key('R', runeInput)
				.key('A', upgradedInput)
				.patternLine("TRT")
				.patternLine("SAS")
				.patternLine(" S ")
				.addCriterion("has_item", hasItem(ModTags.Items.INGOTS_TERRASTEEL))
				.addCriterion("has_prev_tier", hasItem(upgradedInput))
				.build(WrapperResult.ofType(ArmorUpgradeRecipe.SERIALIZER, consumer));
	}

	private void registerRedStringBlock(Consumer<IFinishedRecipe> consumer, IItemProvider output, Ingredient input, ICriterionInstance criterion) {
		ShapedRecipeBuilder.shapedRecipe(output)
				.key('R', ModTags.Items.LIVINGROCK)
				.key('S', ModItems.redString)
				.key('M', input)
				.patternLine("RRR")
				.patternLine("RMS")
				.patternLine("RRR")
				.addCriterion("has_item", hasItem(ModItems.redString))
				.addCriterion("has_base_block", criterion)
				.build(consumer);
	}

	private void createFloatingFlowerRecipe(Consumer<IFinishedRecipe> consumer, IItemProvider input) {
		ResourceLocation inputName = Registry.ITEM.getKey(input.asItem());
		Item output = Registry.ITEM.getValue(new ResourceLocation(inputName.getNamespace(), "floating_" + inputName.getPath())).get();
		ShapelessRecipeBuilder.shapelessRecipe(output)
				.addIngredient(ModTags.Items.FLOATING_FLOWERS)
				.addIngredient(input)
				.addCriterion("has_item", hasItem(input))
				.build(consumer);
	}

	private void deconstruct(Consumer<IFinishedRecipe> consumer, IItemProvider output, IItemProvider input, String name) {
		ShapelessRecipeBuilder.shapelessRecipe(output, 9)
				.addCriterion("has_item", hasItem(output))
				.addIngredient(input)
				.build(consumer, prefix("conversions/" + name));
	}

	private void deconstruct(Consumer<IFinishedRecipe> consumer, IItemProvider output, Tag<Item> input, String name) {
		ShapelessRecipeBuilder.shapelessRecipe(output, 9)
				.addCriterion("has_item", hasItem(output))
				.addIngredient(input)
				.build(consumer, prefix("conversions/" + name));
	}

	private void deconstructPetalBlock(Consumer<IFinishedRecipe> consumer, IItemProvider output, IItemProvider input) {
		ShapelessRecipeBuilder.shapelessRecipe(output, 9)
				.addCriterion("has_item", hasItem(output))
				.addIngredient(input).setGroup("botania:petal_block_deconstruct")
				.build(consumer, prefix("conversions/" + Registry.ITEM.getKey(input.asItem()).getPath() + "_deconstruct"));
	}

	private void recombineSlab(Consumer<IFinishedRecipe> consumer, IItemProvider fullBlock, IItemProvider slab) {
		ShapedRecipeBuilder.shapedRecipe(fullBlock)
				.key('Q', slab)
				.patternLine("Q")
				.patternLine("Q")
				.addCriterion("has_item", hasItem(fullBlock))
				.build(consumer, prefix("slab_recombine/" + Registry.ITEM.getKey(fullBlock.asItem())).getPath());
	}

	private void registerForQuartz(Consumer<IFinishedRecipe> consumer, String variant, IItemProvider baseItem) {
		Block base = Registry.BLOCK.getValue(prefix(variant)).get();
		Block slab = Registry.BLOCK.getValue(prefix(variant + LibBlockNames.SLAB_SUFFIX)).get();
		Block stairs = Registry.BLOCK.getValue(prefix(variant + LibBlockNames.STAIR_SUFFIX)).get();
		Block chiseled = Registry.BLOCK.getValue(prefix("chiseled_" + variant)).get();
		Block pillar = Registry.BLOCK.getValue(prefix(variant + "_pillar")).get();

		ShapedRecipeBuilder.shapedRecipe(base)
				.key('Q', baseItem)
				.patternLine("QQ")
				.patternLine("QQ")
				.addCriterion("has_item", hasItem(baseItem))
				.build(consumer);
		stairs(stairs, base).build(consumer);
		slabShape(slab, base).build(consumer);
		pillar(pillar, base).build(consumer);
		chiseled(chiseled, slab).addCriterion("has_base_item", hasItem(base)).build(consumer);
	}

	private void registerForWood(Consumer<IFinishedRecipe> consumer, String variant) {
		Block base = Registry.BLOCK.getValue(prefix(variant)).get();
		Block planks = Registry.BLOCK.getValue(prefix(variant + "_planks")).get();
		Block slab = Registry.BLOCK.getValue(prefix(variant + LibBlockNames.SLAB_SUFFIX)).get();
		Block stairs = Registry.BLOCK.getValue(prefix(variant + LibBlockNames.STAIR_SUFFIX)).get();
		Block wall = Registry.BLOCK.getValue(prefix(variant + LibBlockNames.WALL_SUFFIX)).get();
		Block fence = Registry.BLOCK.getValue(prefix(variant + LibBlockNames.FENCE_SUFFIX)).get();
		Block fenceGate = Registry.BLOCK.getValue(prefix(variant + LibBlockNames.FENCE_GATE_SUFFIX)).get();

		ShapelessRecipeBuilder.shapelessRecipe(planks, 4).addIngredient(base)
				.addCriterion("has_item", hasItem(base)).build(consumer);
		stairs(stairs, base).build(consumer);
		slabShape(slab, base).build(consumer);
		wallShape(wall, base, 6).build(consumer);
		fence(fence, planks).build(consumer);
		fenceGate(fenceGate, planks).build(consumer);
	}

	private void registerForPavement(Consumer<IFinishedRecipe> consumer, String color, @Nullable Item mainInput) {
		String baseName = color + LibBlockNames.PAVEMENT_SUFFIX;
		Block base = Registry.BLOCK.getValue(prefix(baseName)).get();
		Block stair = Registry.BLOCK.getValue(prefix(baseName + LibBlockNames.STAIR_SUFFIX)).get();
		Block slab = Registry.BLOCK.getValue(prefix(baseName + LibBlockNames.SLAB_SUFFIX)).get();

		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapelessRecipe(base, 3)
				.addIngredient(ModTags.Items.LIVINGROCK)
				.addIngredient(Tags.Items.COBBLESTONE)
				.addIngredient(Items.GRAVEL)
				.setGroup("botania:pavement")
				.addCriterion("has_item", hasItem(ModTags.Items.LIVINGROCK));
		if (mainInput != Items.AIR) {
			builder.addIngredient(mainInput);
		}
		builder.build(consumer);

		slabShape(slab, base).setGroup("botania:pavement_slab").build(consumer);
		stairs(stair, base).setGroup("botania:pavement_stairs").build(consumer);
	}

	private void registerForMetamorphic(Consumer<IFinishedRecipe> consumer, String variant) {
		Block base = Registry.BLOCK.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone")).get();
		Block slab = Registry.BLOCK.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.SLAB_SUFFIX)).get();
		Block stair = Registry.BLOCK.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.STAIR_SUFFIX)).get();
		Block brick = Registry.BLOCK.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks")).get();
		Block brickSlab = Registry.BLOCK.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.SLAB_SUFFIX)).get();
		Block brickStair = Registry.BLOCK.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.STAIR_SUFFIX)).get();
		Block chiseledBrick = Registry.BLOCK.getValue(prefix("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks")).get();
		Block cobble = Registry.BLOCK.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone")).get();
		Block cobbleSlab = Registry.BLOCK.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.SLAB_SUFFIX)).get();
		Block cobbleStair = Registry.BLOCK.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.STAIR_SUFFIX)).get();
		Block cobbleWall = Registry.BLOCK.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.WALL_SUFFIX)).get();

		InventoryChangeTrigger.Instance marimorphosis = hasItem(ModSubtiles.marimorphosis);
		slabShape(slab, base).setGroup("botania:metamorphic_stone_slab")
				.addCriterion("has_flower_item", marimorphosis).build(consumer);
		stairs(stair, base).setGroup("botania:metamorphic_stone_stairs")
				.addCriterion("has_flower_item", marimorphosis).build(consumer);

		brick(brick, base).setGroup("botania:metamorphic_brick")
				.addCriterion("has_flower_item", marimorphosis).build(consumer);
		slabShape(brickSlab, brick).setGroup("botania:metamorphic_brick_slab")
				.addCriterion("has_flower_item", marimorphosis).build(consumer);
		stairs(brickStair, brick).setGroup("botania:metamorphic_brick_stairs")
				.addCriterion("has_flower_item", marimorphosis).build(consumer);
		brick(chiseledBrick, brickSlab).addCriterion("has_base_item", hasItem(brick))
				.addCriterion("has_flower_item", marimorphosis).build(consumer);

		slabShape(cobbleSlab, cobble).setGroup("botania:metamorphic_cobble_slab")
				.addCriterion("has_flower_item", marimorphosis).build(consumer);
		stairs(cobbleStair, cobble).setGroup("botania:metamorphic_cobble_stairs")
				.addCriterion("has_flower_item", marimorphosis).build(consumer);
		wallShape(cobbleWall, cobble, 6).setGroup("botania:metamorphic_cobble_wall")
				.addCriterion("has_flower_item", marimorphosis).build(consumer);
	}

	private ShapedRecipeBuilder compression(IItemProvider output, Tag<Item> input) {
		return ShapedRecipeBuilder.shapedRecipe(output)
				.key('I', input)
				.patternLine("III")
				.patternLine("III")
				.patternLine("III")
				.addCriterion("has_item", hasItem(input));
	}

	private ShapedRecipeBuilder brick(IItemProvider output, IItemProvider input) {
		return ShapedRecipeBuilder.shapedRecipe(output, 4)
				.addCriterion("has_item", hasItem(input))
				.key('Q', input)
				.patternLine("QQ")
				.patternLine("QQ");
	}

	private ShapedRecipeBuilder stairs(IItemProvider output, IItemProvider input) {
		return ShapedRecipeBuilder.shapedRecipe(output, 4)
				.addCriterion("has_item", hasItem(input))
				.key('Q', input)
				.patternLine("  Q")
				.patternLine(" QQ")
				.patternLine("QQQ");
	}

	private ShapedRecipeBuilder slabShape(IItemProvider output, IItemProvider input) {
		return ShapedRecipeBuilder.shapedRecipe(output, 6)
				.addCriterion("has_item", hasItem(input))
				.key('Q', input)
				.patternLine("QQQ");
	}

	private ShapedRecipeBuilder pillar(IItemProvider output, IItemProvider input) {
		return ShapedRecipeBuilder.shapedRecipe(output, 2)
				.addCriterion("has_item", hasItem(input))
				.key('Q', input)
				.patternLine("Q")
				.patternLine("Q");
	}

	private ShapedRecipeBuilder chiseled(IItemProvider output, IItemProvider input) {
		return ShapedRecipeBuilder.shapedRecipe(output)
				.addCriterion("has_item", hasItem(input))
				.key('Q', input)
				.patternLine("Q")
				.patternLine("Q");
	}

	private ShapedRecipeBuilder wallShape(IItemProvider output, IItemProvider input, int amount) {
		return ShapedRecipeBuilder.shapedRecipe(output, amount)
				.addCriterion("has_item", hasItem(input))
				.key('B', input)
				.patternLine("BBB")
				.patternLine("BBB");
	}

	private ShapedRecipeBuilder fence(IItemProvider output, IItemProvider input) {
		return ShapedRecipeBuilder.shapedRecipe(output, 3)
				.addCriterion("has_item", hasItem(input))
				.key('B', input)
				.key('S', Tags.Items.RODS_WOODEN)
				.patternLine("BSB")
				.patternLine("BSB");
	}

	private ShapedRecipeBuilder fenceGate(IItemProvider output, IItemProvider input) {
		return ShapedRecipeBuilder.shapedRecipe(output, 3)
				.addCriterion("has_item", hasItem(input))
				.key('B', input)
				.key('S', Tags.Items.RODS_WOODEN)
				.patternLine("SBS")
				.patternLine("SBS");
	}

	private ShapedRecipeBuilder ringShape(IItemProvider output, IItemProvider input) {
		return ShapedRecipeBuilder.shapedRecipe(output, 4)
				.key('W', input)
				.patternLine(" W ")
				.patternLine("W W")
				.patternLine(" W ")
				.addCriterion("has_item", hasItem(input));
	}

	private void cosmeticBauble(Consumer<IFinishedRecipe> consumer, IItemProvider output, IItemProvider input) {
		ShapedRecipeBuilder.shapedRecipe(output)
				.key('P', input)
				.key('S', ModItems.manaString)
				.patternLine("PPP")
				.patternLine("PSP")
				.patternLine("PPP")
				.setGroup("botania:cosmetic_bauble")
				.addCriterion("has_item", hasItem(ModItems.manaString))
				.build(consumer);
	}

	private void specialRecipe(Consumer<IFinishedRecipe> consumer, SpecialRecipeSerializer<?> serializer) {
		ResourceLocation name = Registry.RECIPE_SERIALIZER.getKey(serializer);
		CustomRecipeBuilder.customRecipe(serializer).build(consumer, prefix("dynamic/" + name.getPath()).toString());
	}

	@Override
	public String getName() {
		return "Botania crafting recipes";
	}
}
