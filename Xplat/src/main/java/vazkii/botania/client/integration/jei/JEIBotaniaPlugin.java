/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.runtime.IRecipesGui;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.recipe.*;
import vazkii.botania.client.core.handler.CorporeaInputHandler;
import vazkii.botania.client.gui.crafting.AssemblyHaloContainer;
import vazkii.botania.client.integration.jei.crafting.AncientWillRecipeWrapper;
import vazkii.botania.client.integration.jei.crafting.CompositeLensRecipeWrapper;
import vazkii.botania.client.integration.jei.crafting.TerraShattererTippingRecipeWrapper;
import vazkii.botania.client.integration.jei.orechid.MarimorphosisRecipeCategory;
import vazkii.botania.client.integration.jei.orechid.OrechidIgnemRecipeCategory;
import vazkii.botania.client.integration.jei.orechid.OrechidRecipeCategory;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.crafting.LexiconElvenTradeRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.recipe.AncientWillRecipe;
import vazkii.botania.common.crafting.recipe.CompositeLensRecipe;
import vazkii.botania.common.crafting.recipe.TerraPickTippingRecipe;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemLaputaShard;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.brew.ItemBrewBase;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@JeiPlugin
public class JEIBotaniaPlugin implements IModPlugin {
	private static final ResourceLocation ID = prefix("main");

	@Override
	public void registerItemSubtypes(@NotNull ISubtypeRegistration registry) {
		IIngredientSubtypeInterpreter<ItemStack> interpreter = (stack, ctx) -> ItemBrewBase.getSubtype(stack);
		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.brewVial, interpreter);
		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.brewFlask, interpreter);
		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.incenseStick, interpreter);
		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.bloodPendant, interpreter);

		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.flightTiara, (stack, ctx) -> String.valueOf(ItemFlightTiara.getVariant(stack)));
		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.lexicon, (stack, ctx) -> String.valueOf(ItemNBTHelper.getBoolean(stack, ItemLexicon.TAG_ELVEN_UNLOCK, false)));
		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.laputaShard, (stack, ctx) -> String.valueOf(ItemLaputaShard.getShardLevel(stack)));

		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.terraPick, (stack, ctx) -> {
			if (ctx == UidContext.Recipe) {
				return String.valueOf(ItemTerraPick.isTipped(stack));
			}
			return String.valueOf(ItemTerraPick.getLevel(stack)) + ItemTerraPick.isTipped(stack);
		});
		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.manaTablet, (stack, ctx) -> {
			int mana = IXplatAbstractions.INSTANCE.findManaItem(stack).getMana();
			return String.valueOf(mana) + ItemManaTablet.isStackCreative(stack);
		});

		for (Item item : new Item[] { ModItems.manaRing, ModItems.manaRingGreater }) {
			registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, (stack, ctx) -> {
				int mana = IXplatAbstractions.INSTANCE.findManaItem(stack).getMana();
				return String.valueOf(mana);
			});
		}
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(
				new PureDaisyRecipeCategory(registry.getJeiHelpers().getGuiHelper(), registry.getJeiHelpers().getPlatformFluidHelper()),
				new ManaPoolRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new PetalApothecaryRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new RunicAltarRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new ElvenTradeRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new BreweryRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new OrechidRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new OrechidIgnemRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new MarimorphosisRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new TerrestrialAgglomerationRecipeCategory(registry.getJeiHelpers().getGuiHelper())
		);
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
		registration.getCraftingCategory().addCategoryExtension(AncientWillRecipe.class, AncientWillRecipeWrapper::new);
		registration.getCraftingCategory().addCategoryExtension(TerraPickTippingRecipe.class, TerraShattererTippingRecipeWrapper::new);
		registration.getCraftingCategory().addCategoryExtension(CompositeLensRecipe.class, CompositeLensRecipeWrapper::new);
	}

	@Override
	public void registerRecipes(@NotNull IRecipeRegistration registry) {
		registry.addRecipes(BreweryRecipeCategory.TYPE, sortRecipes(ModRecipeTypes.BREW_TYPE, BY_ID));
		registry.addRecipes(PureDaisyRecipeCategory.TYPE, sortRecipes(ModRecipeTypes.PURE_DAISY_TYPE, BY_ID));
		registry.addRecipes(PetalApothecaryRecipeCategory.TYPE, sortRecipes(ModRecipeTypes.PETAL_TYPE, BY_ID));
		registry.addRecipes(ElvenTradeRecipeCategory.TYPE, sortRecipes(ModRecipeTypes.ELVEN_TRADE_TYPE, BY_ID));
		registry.addRecipes(RunicAltarRecipeCategory.TYPE, sortRecipes(ModRecipeTypes.RUNE_TYPE, BY_ID));
		registry.addRecipes(ManaPoolRecipeCategory.TYPE, sortRecipes(ModRecipeTypes.MANA_INFUSION_TYPE, BY_CATALYST.thenComparing(BY_GROUP).thenComparing(BY_ID)));
		registry.addRecipes(TerrestrialAgglomerationRecipeCategory.TYPE, sortRecipes(ModRecipeTypes.TERRA_PLATE_TYPE, BY_ID));

		Comparator<OrechidRecipe> comp = BY_INPUT.thenComparing(BY_WEIGHT).thenComparing(BY_ID);
		registry.addRecipes(OrechidRecipeCategory.TYPE, sortRecipes(ModRecipeTypes.ORECHID_TYPE, comp));
		registry.addRecipes(OrechidIgnemRecipeCategory.TYPE, sortRecipes(ModRecipeTypes.ORECHID_IGNEM_TYPE, comp));
		registry.addRecipes(MarimorphosisRecipeCategory.TYPE, sortRecipes(ModRecipeTypes.MARIMORPHOSIS_TYPE, comp));
	}

	private static final Comparator<Recipe<?>> BY_ID = Comparator.comparing(Recipe::getId);
	private static final Comparator<Recipe<?>> BY_GROUP = Comparator.comparing(Recipe::getGroup);
	private static final Comparator<OrechidRecipe> BY_INPUT = Comparator.comparing(r -> Registry.BLOCK.getId(r.getInput()));
	private static final Comparator<OrechidRecipe> BY_WEIGHT = Comparator.<OrechidRecipe, Integer>comparing(OrechidRecipe::getWeight).reversed();
	private static final Comparator<ManaInfusionRecipe> BY_CATALYST = (l, r) -> {
		StateIngredient left = l.getRecipeCatalyst();
		StateIngredient right = r.getRecipeCatalyst();
		if (left == null) {
			return right == null ? 0 : -1;
		} else if (right == null) {
			return 1;
		} else {
			return left.toString().compareTo(right.toString());
		}
	};

	private static <T extends Recipe<C>, C extends Container> List<T> sortRecipes(RecipeType<T> type, Comparator<? super T> comparator) {
		@SuppressWarnings("unchecked")
		Collection<T> recipes = (Collection<T>) ModRecipeTypes.getRecipes(Minecraft.getInstance().level, type).values();
		List<T> list = new ArrayList<>(recipes);
		list.sort(comparator);
		return list;
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {
		registry.addRecipeTransferHandler(AssemblyHaloContainer.class, null, RecipeTypes.CRAFTING, 1, 9, 10, 36);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.brewery), BreweryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.alfPortal), ElvenTradeRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.manaPool), ManaPoolRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.creativePool), ManaPoolRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.dilutedPool), ManaPoolRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.fabulousPool), ManaPoolRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.defaultAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.forestAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.plainsAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.mountainAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.fungalAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.swampAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.desertAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.taigaAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.mesaAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.mossyAltar), PetalApothecaryRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.orechid), OrechidRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.orechidFloating), OrechidRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.orechidIgnem), OrechidIgnemRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.orechidIgnemFloating), OrechidIgnemRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.marimorphosis), MarimorphosisRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.marimorphosisChibi), MarimorphosisRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.marimorphosisFloating), MarimorphosisRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.marimorphosisChibiFloating), MarimorphosisRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.pureDaisy), PureDaisyRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.pureDaisyFloating), PureDaisyRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.runeAltar), RunicAltarRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.terraPlate), TerrestrialAgglomerationRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(ModItems.autocraftingHalo), RecipeTypes.CRAFTING);
		registry.addRecipeCatalyst(new ItemStack(ModItems.craftingHalo), RecipeTypes.CRAFTING);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		IRecipeManager recipeRegistry = jeiRuntime.getRecipeManager();
		// Hide the return recipes (iron ingot/diamond/ender pearl returns, not lexicon)
		for (ElvenTradeRecipe recipe : TileAlfPortal.elvenTradeRecipes(Minecraft.getInstance().level)) {
			if (recipe instanceof LexiconElvenTradeRecipe) {
				continue;
			}
			List<Ingredient> inputs = recipe.getIngredients();
			List<ItemStack> outputs = recipe.getOutputs();
			if (inputs.size() == 1 && outputs.size() == 1 && recipe.containsItem(outputs.get(0))) {
				recipeRegistry.hideRecipes(ElvenTradeRecipeCategory.TYPE, List.of(recipe));
			}
		}

		RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
		recipeManager.byKey(prefix("petal_apothecary/daybloom_motif"))
				.ifPresent(r -> {
					if (r instanceof PetalApothecaryRecipe pr) {
						recipeRegistry.hideRecipes(PetalApothecaryRecipeCategory.TYPE, List.of(pr));
					}
				});
		recipeManager.byKey(prefix("petal_apothecary/nightshade_motif"))
				.ifPresent(r -> {
					if (r instanceof PetalApothecaryRecipe pr) {
						recipeRegistry.hideRecipes(PetalApothecaryRecipeCategory.TYPE, List.of(pr));
					}
				});

		var old = CorporeaInputHandler.hoveredStackGetter;
		CorporeaInputHandler.hoveredStackGetter = () -> ObjectUtils.getFirstNonNull(
				() -> jeiRuntime.getIngredientListOverlay().getIngredientUnderMouse(VanillaTypes.ITEM_STACK),
				() -> jeiRuntime.getRecipesGui().getIngredientUnderMouse(VanillaTypes.ITEM_STACK).orElse(null),
				() -> jeiRuntime.getBookmarkOverlay().getIngredientUnderMouse(VanillaTypes.ITEM_STACK),
				old
		);

		CorporeaInputHandler.supportedGuiFilter = CorporeaInputHandler.supportedGuiFilter.or(gui -> gui instanceof IRecipesGui);
	}

	@NotNull
	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}
}
