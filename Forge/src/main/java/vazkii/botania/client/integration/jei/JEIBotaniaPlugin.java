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
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
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
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.api.recipe.IOrechidRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.client.core.handler.CorporeaInputHandler;
import vazkii.botania.client.gui.crafting.ContainerCraftingHalo;
import vazkii.botania.client.integration.jei.crafting.AncientWillRecipeWrapper;
import vazkii.botania.client.integration.jei.crafting.CompositeLensRecipeWrapper;
import vazkii.botania.client.integration.jei.crafting.TerraPickTippingRecipeWrapper;
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

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@JeiPlugin
public class JEIBotaniaPlugin implements IModPlugin {
	private static final ResourceLocation ID = prefix("main");

	@Override
	public void registerItemSubtypes(@Nonnull ISubtypeRegistration registry) {
		IIngredientSubtypeInterpreter<ItemStack> interpreter = (stack, ctx) -> ItemBrewBase.getSubtype(stack);
		registry.registerSubtypeInterpreter(ModItems.brewVial, interpreter);
		registry.registerSubtypeInterpreter(ModItems.brewFlask, interpreter);
		registry.registerSubtypeInterpreter(ModItems.incenseStick, interpreter);
		registry.registerSubtypeInterpreter(ModItems.bloodPendant, interpreter);

		registry.registerSubtypeInterpreter(ModItems.flightTiara, (stack, ctx) -> String.valueOf(ItemFlightTiara.getVariant(stack)));
		registry.registerSubtypeInterpreter(ModItems.lexicon, (stack, ctx) -> String.valueOf(ItemNBTHelper.getBoolean(stack, ItemLexicon.TAG_ELVEN_UNLOCK, false)));
		registry.registerSubtypeInterpreter(ModItems.laputaShard, (stack, ctx) -> String.valueOf(ItemLaputaShard.getShardLevel(stack)));

		registry.registerSubtypeInterpreter(ModItems.terraPick, (stack, ctx) -> {
			if (ctx == UidContext.Recipe) {
				return String.valueOf(ItemTerraPick.isTipped(stack));
			}
			return String.valueOf(ItemTerraPick.getLevel(stack)) + ItemTerraPick.isTipped(stack);
		});
		registry.registerSubtypeInterpreter(ModItems.manaTablet, (stack, ctx) -> {
			int mana = IXplatAbstractions.INSTANCE.findManaItem(stack).getMana();
			return String.valueOf(mana) + ItemManaTablet.isStackCreative(stack);
		});

		for (Item item : new Item[] { ModItems.manaRing, ModItems.manaRingGreater }) {
			registry.registerSubtypeInterpreter(item, (stack, ctx) -> {
				int mana = IXplatAbstractions.INSTANCE.findManaItem(stack).getMana();
				return String.valueOf(mana);
			});
		}
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(
				new PureDaisyRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new ManaPoolRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new PetalApothecaryRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new RunicAltarRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new ElvenTradeRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new BreweryRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new OrechidRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new OrechidIgnemRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new MarimorphosisRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new TerraPlateRecipeCategory(registry.getJeiHelpers().getGuiHelper())
		);
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
		registration.getCraftingCategory().addCategoryExtension(AncientWillRecipe.class, AncientWillRecipeWrapper::new);
		registration.getCraftingCategory().addCategoryExtension(TerraPickTippingRecipe.class, TerraPickTippingRecipeWrapper::new);
		registration.getCraftingCategory().addCategoryExtension(CompositeLensRecipe.class, CompositeLensRecipeWrapper::new);
	}

	@Override
	public void registerRecipes(@Nonnull IRecipeRegistration registry) {
		registry.addRecipes(sortRecipes(ModRecipeTypes.BREW_TYPE, BY_ID), BreweryRecipeCategory.UID);
		registry.addRecipes(sortRecipes(ModRecipeTypes.PURE_DAISY_TYPE, BY_ID), PureDaisyRecipeCategory.UID);
		registry.addRecipes(sortRecipes(ModRecipeTypes.PETAL_TYPE, BY_ID), PetalApothecaryRecipeCategory.UID);
		registry.addRecipes(sortRecipes(ModRecipeTypes.ELVEN_TRADE_TYPE, BY_ID), ElvenTradeRecipeCategory.UID);
		registry.addRecipes(sortRecipes(ModRecipeTypes.RUNE_TYPE, BY_ID), RunicAltarRecipeCategory.UID);
		registry.addRecipes(sortRecipes(ModRecipeTypes.MANA_INFUSION_TYPE, BY_CATALYST.thenComparing(BY_GROUP).thenComparing(BY_ID)), ManaPoolRecipeCategory.UID);
		registry.addRecipes(sortRecipes(ModRecipeTypes.TERRA_PLATE_TYPE, BY_ID), TerraPlateRecipeCategory.UID);

		Comparator<IOrechidRecipe> comp = BY_INPUT.thenComparing(BY_WEIGHT).thenComparing(BY_ID);
		registry.addRecipes(sortRecipes(ModRecipeTypes.ORECHID_TYPE, comp), OrechidRecipeCategory.UID);
		registry.addRecipes(sortRecipes(ModRecipeTypes.ORECHID_IGNEM_TYPE, comp), OrechidIgnemRecipeCategory.UID);
		registry.addRecipes(sortRecipes(ModRecipeTypes.MARIMORPHOSIS_TYPE, comp), MarimorphosisRecipeCategory.UID);
	}

	private static final Comparator<Recipe<?>> BY_ID = Comparator.comparing(Recipe::getId);
	private static final Comparator<Recipe<?>> BY_GROUP = Comparator.comparing(Recipe::getGroup);
	private static final Comparator<IOrechidRecipe> BY_INPUT = Comparator.comparing(r -> Registry.BLOCK.getId(r.getInput()));
	private static final Comparator<IOrechidRecipe> BY_WEIGHT = Comparator.<IOrechidRecipe, Integer>comparing(IOrechidRecipe::getWeight).reversed();
	private static final Comparator<IManaInfusionRecipe> BY_CATALYST = (l, r) -> {
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

	private static <T extends Recipe<C>, C extends Container> Collection<T> sortRecipes(RecipeType<T> type, Comparator<? super T> comparator) {
		@SuppressWarnings("unchecked")
		Collection<T> recipes = (Collection<T>) ModRecipeTypes.getRecipes(Minecraft.getInstance().level, type).values();
		List<T> list = new ArrayList<>(recipes);
		list.sort(comparator);
		return list;
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {
		registry.addRecipeTransferHandler(ContainerCraftingHalo.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.brewery), BreweryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.alfPortal), ElvenTradeRecipeCategory.UID);

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.manaPool), ManaPoolRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.creativePool), ManaPoolRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.dilutedPool), ManaPoolRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.fabulousPool), ManaPoolRecipeCategory.UID);

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.defaultAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.forestAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.plainsAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.mountainAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.fungalAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.swampAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.desertAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.taigaAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.mesaAltar), PetalApothecaryRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.mossyAltar), PetalApothecaryRecipeCategory.UID);

		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.orechid), OrechidRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.orechidFloating), OrechidRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.orechidIgnem), OrechidIgnemRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.orechidIgnemFloating), OrechidIgnemRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.marimorphosis), MarimorphosisRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.marimorphosisChibi), MarimorphosisRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.marimorphosisFloating), MarimorphosisRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.marimorphosisChibiFloating), MarimorphosisRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.pureDaisy), PureDaisyRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModSubtiles.pureDaisyFloating), PureDaisyRecipeCategory.UID);

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.runeAltar), RunicAltarRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.terraPlate), TerraPlateRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModItems.autocraftingHalo), VanillaRecipeCategoryUid.CRAFTING);
		registry.addRecipeCatalyst(new ItemStack(ModItems.craftingHalo), VanillaRecipeCategoryUid.CRAFTING);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		IRecipeManager recipeRegistry = jeiRuntime.getRecipeManager();
		// Hide the return recipes (iron ingot/diamond/ender pearl returns, not lexicon)
		for (IElvenTradeRecipe recipe : TileAlfPortal.elvenTradeRecipes(Minecraft.getInstance().level)) {
			if (recipe instanceof LexiconElvenTradeRecipe) {
				continue;
			}
			List<Ingredient> inputs = recipe.getIngredients();
			List<ItemStack> outputs = recipe.getOutputs();
			if (inputs.size() == 1 && outputs.size() == 1 && recipe.containsItem(outputs.get(0))) {
				recipeRegistry.hideRecipe(recipe, ElvenTradeRecipeCategory.UID);
			}
		}

		RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
		recipeManager.byKey(prefix("petal_apothecary/daybloom_motif"))
				.ifPresent(r -> recipeRegistry.hideRecipe(r, PetalApothecaryRecipeCategory.UID));
		recipeManager.byKey(prefix("petal_apothecary/nightshade_motif"))
				.ifPresent(r -> recipeRegistry.hideRecipe(r, PetalApothecaryRecipeCategory.UID));

		CorporeaInputHandler.hoveredStackGetter = () -> {
			ItemStack stack = jeiRuntime.getIngredientListOverlay().getIngredientUnderMouse(VanillaTypes.ITEM);

			if (stack == null && Minecraft.getInstance().screen == jeiRuntime.getRecipesGui()) {
				stack = jeiRuntime.getRecipesGui().getIngredientUnderMouse(VanillaTypes.ITEM);
			}

			if (stack == null) {
				stack = jeiRuntime.getBookmarkOverlay().getIngredientUnderMouse(VanillaTypes.ITEM);
			}

			if (stack != null) {
				return stack;
			}
			return ItemStack.EMPTY;
		};

		CorporeaInputHandler.supportedGuiFilter = gui -> gui instanceof AbstractContainerScreen<?> || gui instanceof IRecipesGui;
	}

	@Nonnull
	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}
}
