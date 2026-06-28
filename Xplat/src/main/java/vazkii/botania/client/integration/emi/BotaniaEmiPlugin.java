/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.integration.emi;

import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import org.apache.commons.lang3.StringUtils;

import vazkii.botania.client.core.handler.CorporeaInputHandler;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.crafting.*;
import vazkii.botania.common.crafting.recipe.RecipeUtils;
import vazkii.botania.common.crafting.recipe.TiaraWingsRecipe;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.GrassSeedsItem;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import vazkii.botania.common.item.lens.LensItem;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

@EmiEntrypoint
public class BotaniaEmiPlugin implements EmiPlugin {
	private static final Comparator<EmiRecipe> BY_ID = EmiRecipeSorting.identifier();
	private static final Comparator<EmiRecipe> BY_GROUP =
			Comparator.comparing(emiRecipe -> emiRecipe instanceof BotaniaEmiRecipe ber ? ber.getGroup() : "");
	private static final Comparator<EmiRecipe> BY_CATALYST =
			Comparator.comparing(emiRecipe -> emiRecipe.getCatalysts()
					.stream()
					.flatMap(emiIngredient -> emiIngredient.getEmiStacks().stream())
					.map(emiStack -> emiStack.getId().toString())
					.filter(StringUtils::isNotEmpty)
					.findFirst()
					.orElse(""));
	private static final Comparator<EmiRecipe> BY_WEIGHT = Comparator.<EmiRecipe, Integer>comparing(
			emiRecipe -> emiRecipe instanceof OrechidEmiRecipe orechidEmiRecipe ? orechidEmiRecipe.getWeight() : 0).reversed();
	private static final Comparator<EmiRecipe> ORECHID_COMPARATOR = BY_WEIGHT.thenComparing(BY_ID);

	public static final EmiRecipeCategory PETAL_APOTHECARY = createCategory("petal_apothecary",
			EmiStack.of(BotaniaBlocks.PETAL_APOTHECARY), BY_ID);
	public static final EmiRecipeCategory MANA_INFUSION = createCategory("mana_infusion",
			EmiStack.of(makeFullManaPool()), BY_CATALYST.thenComparing(BY_GROUP).thenComparing(BY_ID));
	public static final EmiRecipeCategory RUNIC_ALTAR = createCategory("runic_altar",
			EmiStack.of(BotaniaBlocks.RUNIC_ALTAR), BY_GROUP.thenComparing(BY_ID));
	public static final EmiRecipeCategory TERRESTRIAL_AGGLOMERATION = createCategory("terrestrial_agglomeration",
			EmiStack.of(BotaniaBlocks.TERRESTRIAL_AGGLOMERATION_PLATE), BY_ID);
	public static final EmiRecipeCategory ELVEN_TRADE = createCategory("elven_trade",
			EmiStack.of(BotaniaBlocks.ELVEN_GATEWAY_CORE), BY_ID);
	public static final EmiRecipeCategory BOTANICAL_BREWERY = createCategory("botanical_brewery",
			EmiStack.of(BotaniaBlocks.BOTANICAL_BREWERY), BY_ID);
	public static final EmiRecipeCategory PURE_DAISY = createCategory("pure_daisy",
			EmiStack.of(BotaniaBlocks.PURE_DAISY), BY_ID);
	public static final EmiRecipeCategory ORECHID = createCategory("orechid",
			EmiStack.of(BotaniaBlocks.ORECHID), ORECHID_COMPARATOR);
	public static final EmiRecipeCategory ORECHID_IGNEM = createCategory("orechid_ignem",
			EmiStack.of(BotaniaBlocks.ORECHID_IGNEM), ORECHID_COMPARATOR);
	public static final EmiRecipeCategory MARIMORPHOSIS = createCategory("marimorphosis",
			EmiStack.of(BotaniaBlocks.MARIMORPHOSIS), ORECHID_COMPARATOR);

	private static EmiRecipeCategory createCategory(String idPath, EmiRenderable icon, Comparator<EmiRecipe> comp) {
		return new EmiRecipeCategory(botaniaRL(idPath), icon, icon, comp);
	}

	private static ItemStack makeFullManaPool() {
		ItemStack pool = new ItemStack(BotaniaBlocks.MANA_POOL);
		pool.set(BotaniaDataComponents.RENDER_FULL, Unit.INSTANCE);
		return pool;
	}

	private static final Supplier<ItemStack> HOVERED_STACK_GETTER = () -> {
		EmiIngredient ingredient = EmiApi.getHoveredStack(true).getStack();
		if (!ingredient.getEmiStacks().isEmpty()) {
			var stack = ingredient.getEmiStacks().getFirst().getItemStack();
			if (!stack.isEmpty()) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	};

	public BotaniaEmiPlugin() {
		CorporeaInputHandler.supportedGuiFilter = CorporeaInputHandler.supportedGuiFilter.or(screen -> {
			final var handledScreen = EmiApi.getHandledScreen();
			// should apply to EMI's recipe and BOM screens:
			return handledScreen != null && Minecraft.getInstance().screen != handledScreen;
		});
	}

	@Override
	public void register(EmiRegistry registry) {
		if (!CorporeaInputHandler.hoveredStackGetters.contains(HOVERED_STACK_GETTER)) {
			CorporeaInputHandler.hoveredStackGetters.add(HOVERED_STACK_GETTER);
		}
		registry.addCategory(PETAL_APOTHECARY);
		registry.addCategory(MANA_INFUSION);
		registry.addCategory(RUNIC_ALTAR);
		registry.addCategory(TERRESTRIAL_AGGLOMERATION);
		registry.addCategory(ELVEN_TRADE);
		registry.addCategory(BOTANICAL_BREWERY);
		registry.addCategory(PURE_DAISY);
		registry.addCategory(ORECHID);
		registry.addCategory(ORECHID_IGNEM);
		registry.addCategory(MARIMORPHOSIS);

		registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(BotaniaItems.ASSEMBLY_HALO));
		registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(BotaniaItems.MANUFACTORY_HALO));

		registry.addWorkstation(PETAL_APOTHECARY, EmiIngredient.of(BotaniaTags.Items.PETAL_APOTHECARIES));
		registry.addWorkstation(MANA_INFUSION, EmiStack.of(BotaniaBlocks.MANA_POOL));
		registry.addWorkstation(MANA_INFUSION, EmiStack.of(BotaniaBlocks.DILUTED_MANA_POOL));
		registry.addWorkstation(MANA_INFUSION, EmiStack.of(BotaniaBlocks.FABULOUS_MANA_POOL));
		registry.addWorkstation(RUNIC_ALTAR, EmiStack.of(BotaniaBlocks.RUNIC_ALTAR));
		registry.addWorkstation(TERRESTRIAL_AGGLOMERATION, EmiStack.of(BotaniaBlocks.TERRESTRIAL_AGGLOMERATION_PLATE));
		registry.addWorkstation(ELVEN_TRADE, EmiStack.of(BotaniaBlocks.ELVEN_GATEWAY_CORE));
		registry.addWorkstation(BOTANICAL_BREWERY, EmiStack.of(BotaniaBlocks.BOTANICAL_BREWERY));

		registry.addWorkstation(PURE_DAISY, EmiStack.of(BotaniaBlocks.PURE_DAISY));
		registry.addWorkstation(PURE_DAISY, EmiStack.of(BotaniaBlocks.FLOATING_PURE_DAISY));
		registry.addWorkstation(ORECHID, EmiStack.of(BotaniaBlocks.ORECHID));
		registry.addWorkstation(ORECHID, EmiStack.of(BotaniaBlocks.FLOATING_ORECHID));
		registry.addWorkstation(ORECHID_IGNEM, EmiStack.of(BotaniaBlocks.ORECHID_IGNEM));
		registry.addWorkstation(ORECHID_IGNEM, EmiStack.of(BotaniaBlocks.FLOATING_ORECHID_IGNEM));
		registry.addWorkstation(MARIMORPHOSIS, EmiStack.of(BotaniaBlocks.MARIMORPHOSIS));
		registry.addWorkstation(MARIMORPHOSIS, EmiStack.of(BotaniaBlocks.FLOATING_MARIMORPHOSIS));
		registry.addWorkstation(MARIMORPHOSIS, EmiStack.of(BotaniaBlocks.MARIMORPHOSIS_PETITE));
		registry.addWorkstation(MARIMORPHOSIS, EmiStack.of(BotaniaBlocks.FLOATING_MARIMORPHOSIS_PETITE));

		registry.setDefaultComparison(BotaniaItems.LEXICA_BOTANIA, Comparison.compareComponents());
		registry.setDefaultComparison(BotaniaItems.BREW_FLASK, Comparison.compareComponents());
		registry.setDefaultComparison(BotaniaItems.BREW_VIAL, Comparison.compareComponents());
		registry.setDefaultComparison(BotaniaItems.TAINTED_BLOOD_PENDANT, Comparison.compareComponents());
		registry.setDefaultComparison(BotaniaItems.INCENSE_STICK, Comparison.compareComponents());
		registry.setDefaultComparison(BotaniaItems.FLUEGEL_TIARA, Comparison.compareComponents());

		for (RecipeHolder<CraftingRecipe> recipe : registry.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING)) {
			if (recipe.value() instanceof TiaraWingsRecipe tiaraWingsRecipe) {
				registry.addRecipe(new TiaraWingsEmiRecipe(tiaraWingsRecipe, recipe.id()));
			}
		}

		registry.addRecipe(new AncientWillEmiRecipe(EmiStack.of(BotaniaItems.TERRASTEEL_HELMET), EmiIngredient.of(List.of(
				EmiStack.of(BotaniaItems.WILL_OF_AHRIM),
				EmiStack.of(BotaniaItems.WILL_OF_DHAROK),
				EmiStack.of(BotaniaItems.WILL_OF_GUTHAN),
				EmiStack.of(BotaniaItems.WILL_OF_KARIL),
				EmiStack.of(BotaniaItems.WILL_OF_TORAG),
				EmiStack.of(BotaniaItems.WILL_OF_VERAC)
		))));

		registry.addRecipe(new CompositeLensEmiRecipe(
				StreamSupport.stream(BuiltInRegistries.ITEM.getOrCreateTag(BotaniaTags.Items.LENS).spliterator(), false)
						.map(ItemStack::new)
						.filter(s -> !((LensItem) s.getItem()).isControlLens(s))
						.filter(s -> ((LensItem) s.getItem()).isCombinable(s))
						.map(EmiStack::of)
						.toList()));

		ItemStack tipped = new ItemStack(BotaniaItems.TERRA_SHATTERER);
		TerraShattererItem.setTipped(tipped);
		registry.addRecipe(new EmiCraftingRecipe(List.of(EmiStack.of(BotaniaItems.TERRA_SHATTERER),
				EmiStack.of(BotaniaItems.ELEMENTIUM_PICKAXE)), EmiStack.of(tipped), null));

		for (var recipe : registry.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.PETAL_APOTHECARY_TYPE)) {
			registry.addRecipe(new PetalApothecaryEmiRecipe(recipe));
		}
		for (var recipe : registry.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.MANA_INFUSION_TYPE)) {
			registry.addRecipe(new ManaInfusionEmiRecipe(recipe));
		}
		for (var recipe : registry.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.RUNIC_ALTAR_TYPE)) {
			registry.addRecipe(new RunicAltarEmiRecipe(recipe));
		}
		for (var recipe : registry.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.TERRA_PLATE_TYPE)) {
			registry.addRecipe(new TerrestrialAgglomerationEmiRecipe(recipe));
		}
		for (var recipe : registry.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.ELVEN_TRADE_TYPE)) {
			registry.addRecipe(new ElvenTradeEmiRecipe(recipe));
		}
		for (var recipe : registry.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.BREW_TYPE)) {
			for (ItemStack container : RecipeUtils.getBrewContainerIngredient().getItems()) {
				if (!recipe.value().getOutput(container.copy()).isEmpty()) {
					registry.addRecipe(new BotanicalBreweryEmiRecipe(recipe, container));
				}
			}
		}
		for (var recipe : registry.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.PURE_DAISY_TYPE)) {
			registry.addRecipe(new PureDaisyEmiRecipe(recipe));
		}

		EmiIngredient orechid = EmiStack.of(BotaniaBlocks.ORECHID);
		for (var recipe : registry.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.ORECHID_TYPE)) {
			registry.addRecipe(new OrechidEmiRecipe(ORECHID, recipe, orechid));
		}
		EmiIngredient orechidIgnem = EmiStack.of(BotaniaBlocks.ORECHID_IGNEM);
		for (var recipe : registry.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.ORECHID_IGNEM_TYPE)) {
			registry.addRecipe(new OrechidEmiRecipe(ORECHID_IGNEM, recipe, orechidIgnem));
		}
		EmiIngredient marimorphosis = EmiStack.of(BotaniaBlocks.MARIMORPHOSIS);
		for (var recipe : registry.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.MARIMORPHOSIS_TYPE)) {
			registry.addRecipe(new OrechidEmiRecipe(MARIMORPHOSIS, recipe, marimorphosis));
		}

		// pool item washing
		EmiIngredient cauldron = EmiIngredient.of(Ingredient.of(Blocks.CAULDRON));
		int bottleAmount = XplatAbstractions.instance().isForge() ? 250 : 27_000;
		EmiStack waterThird = EmiStack.of(Fluids.WATER, bottleAmount);
		for (var e : Map.of(
				BotaniaTags.Items.DYED_MANA_POOLS, BotaniaBlocks.MANA_POOL,
				BotaniaTags.Items.DYED_DILUTED_MANA_POOLS, BotaniaBlocks.DILUTED_MANA_POOL,
				BotaniaTags.Items.DYED_FABULOUS_MANA_POOLS, BotaniaBlocks.FABULOUS_MANA_POOL,
				BotaniaTags.Items.DYED_CREATIVE_MANA_POOLS, BotaniaBlocks.CREATIVE_MANA_POOL
		).entrySet()) {
			ManaPoolBlock poolBlock = e.getValue();
			ResourceLocation poolBlockId = BuiltInRegistries.BLOCK.getKey(poolBlock);
			EmiIngredient inputPool = EmiIngredient.of(e.getKey());
			EmiStack undyedPool = EmiStack.of(poolBlock);
			registry.addRecipe(EmiWorldInteractionRecipe.builder()
					.id(poolBlockId.withPrefix("/world/cauldron_washing/"))
					.leftInput(inputPool)
					.rightInput(cauldron, true)
					.rightInput(waterThird, false)
					.output(undyedPool)
					.supportsRecipeTree(false)
					.build());
			registry.addRecipe(EmiWorldInteractionRecipe.builder()
					.id(poolBlockId.withPrefix("/world/pool_cleaning/"))
					.leftInput(inputPool)
					.rightInput(EmiIngredient.of(BotaniaTags.Items.MANA_POOL_DYE_REMOVER), false)
					.output(undyedPool)
					.supportsRecipeTree(false)
					.build());
			ColorHelper.supportedColors().forEach(color -> {
				Item petalItem = BotaniaItems.getPetal(color);
				ManaPoolBlock dyedPoolBlock = BotaniaBlocks.findOptionallyDyedBlock(poolBlock, color);
				ResourceLocation dyedPoolBlockId = BuiltInRegistries.BLOCK.getKey(dyedPoolBlock);
				registry.addRecipe(EmiWorldInteractionRecipe.builder()
						.id(dyedPoolBlockId.withPrefix("/world/pool_dyeing/"))
						.leftInput(EmiIngredient.of(List.of(inputPool, undyedPool)))
						.rightInput(EmiStack.of(petalItem), false)
						.output(EmiStack.of(dyedPoolBlock))
						.supportsRecipeTree(false)
						.build());
			});
		}

		// pasture seed usage
		EmiIngredient dirtBlock = EmiIngredient.of(BotaniaTags.Blocks.PASTURE_SEED_REPLACEABLE);
		BuiltInRegistries.ITEM.stream().filter(GrassSeedsItem.class::isInstance).map(GrassSeedsItem.class::cast)
				.forEach(item -> {
					Block grassBlock = item.getGrassBlock();
					registry.addRecipe(EmiWorldInteractionRecipe.builder()
							.id(BuiltInRegistries.ITEM.getKey(item).withPrefix("/world/grass_conversion/"))
							.leftInput(dirtBlock)
							.rightInput(EmiIngredient.of(Ingredient.of(item)), false)
							.output(EmiStack.of(grassBlock))
							.supportsRecipeTree(false)
							.build());
				});

		// rods
		Map.of(
				BotaniaItems.ROD_OF_THE_LANDS, EmiStack.of(Blocks.DIRT),
				BotaniaItems.ROD_OF_THE_HIGHLANDS, EmiStack.of(Blocks.DIRT),
				BotaniaItems.ROD_OF_THE_DEPTHS, EmiStack.of(Blocks.COBBLESTONE),
				BotaniaItems.ROD_OF_THE_SEAS, EmiStack.of(Fluids.WATER)
		).forEach((in, out) -> {
			registry.addRecipe(EmiWorldInteractionRecipe.builder()
					.id(BuiltInRegistries.ITEM.getKey(in).withPrefix("/world/rod/"))
					.leftInput(EmiStack.EMPTY)
					.rightInput(EmiStack.of(in), true)
					.output(out)
					.build());
		});

		// rod of the molten core can smelt blocks into blocks
		EmiIngredient moltenCoreRod = EmiStack.of(BotaniaItems.ROD_OF_THE_MOLTEN_CORE);
		for (RecipeHolder<SmeltingRecipe> recipeHolder : registry.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING)) {
			SmeltingRecipe recipe = recipeHolder.value();
			ResourceLocation id = recipeHolder.id();
			Level level = Minecraft.getInstance().level;
			ItemStack output = recipe.getResultItem(level.registryAccess());
			if (output.isEmpty() || !(output.getItem() instanceof BlockItem)) {
				continue;
			}
			List<ItemStack> filteredInputStacks = new ArrayList<>();
			for (ItemStack stack : recipe.getIngredients().getFirst().getItems()) {
				if (stack.getItem() instanceof BlockItem) {
					filteredInputStacks.add(stack);
				}
			}
			if (filteredInputStacks.isEmpty()) {
				continue;
			}
			Ingredient filteredInput = Ingredient.of(filteredInputStacks.stream());
			registry.addRecipe(EmiWorldInteractionRecipe.builder()
					.leftInput(EmiIngredient.of(filteredInput))
					.rightInput(moltenCoreRod, true)
					.output(EmiStack.of(output))
					.id(id.withPrefix("/world/molten_core_rod/"))
					.build());

		}
	}

	public static int rotateXAround(int x, int y, int cx, int cy, double degrees) {
		double rad = Math.toRadians(degrees);
		return (int) (Math.cos(rad) * (x - cx) - Math.sin(rad) * (y - cy) + cx);
	}

	public static int rotateYAround(int x, int y, int cx, int cy, double degrees) {
		double rad = Math.toRadians(degrees);
		return (int) (Math.sin(rad) * (x - cx) - Math.cos(rad) * (y - cy) + cy);
	}
}
