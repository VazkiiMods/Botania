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
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.block.block_entity.AlfheimPortalBlockEntity;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.crafting.LexiconElvenTradeRecipe;
import vazkii.botania.common.crafting.recipe.AncientWillRecipe;
import vazkii.botania.common.crafting.recipe.CompositeLensRecipe;
import vazkii.botania.common.crafting.recipe.TerraShattererTippingRecipe;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.LaputaShardItem;
import vazkii.botania.common.item.LexicaBotaniaItem;
import vazkii.botania.common.item.ManaTabletItem;
import vazkii.botania.common.item.brew.BaseBrewItem;
import vazkii.botania.common.item.equipment.bauble.FlugelTiaraItem;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import vazkii.botania.xplat.XplatAbstractions;

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
		IIngredientSubtypeInterpreter<ItemStack> interpreter = (stack, ctx) -> BaseBrewItem.getSubtype(stack);
		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, BotaniaItems.brewVial, interpreter);
		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, BotaniaItems.brewFlask, interpreter);
		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, BotaniaItems.incenseStick, interpreter);
		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, BotaniaItems.bloodPendant, interpreter);

		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, BotaniaItems.flightTiara, (stack, ctx) -> String.valueOf(FlugelTiaraItem.getVariant(stack)));
		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, BotaniaItems.lexicon, (stack, ctx) -> String.valueOf(ItemNBTHelper.getBoolean(stack, LexicaBotaniaItem.TAG_ELVEN_UNLOCK, false)));
		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, BotaniaItems.laputaShard, (stack, ctx) -> String.valueOf(LaputaShardItem.getShardLevel(stack)));

		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, BotaniaItems.terraPick, (stack, ctx) -> {
			if (ctx == UidContext.Recipe) {
				return String.valueOf(TerraShattererItem.isTipped(stack));
			}
			return String.valueOf(TerraShattererItem.getLevel(stack)) + TerraShattererItem.isTipped(stack);
		});
		registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, BotaniaItems.manaTablet, (stack, ctx) -> {
			int mana = XplatAbstractions.INSTANCE.findManaItem(stack).getMana();
			return String.valueOf(mana) + ManaTabletItem.isStackCreative(stack);
		});

		for (Item item : new Item[] { BotaniaItems.manaRing, BotaniaItems.manaRingGreater }) {
			registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, (stack, ctx) -> {
				int mana = XplatAbstractions.INSTANCE.findManaItem(stack).getMana();
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
		registration.getCraftingCategory().addCategoryExtension(TerraShattererTippingRecipe.class, TerraShattererTippingRecipeWrapper::new);
		registration.getCraftingCategory().addCategoryExtension(CompositeLensRecipe.class, CompositeLensRecipeWrapper::new);
	}

	@Override
	public void registerRecipes(@NotNull IRecipeRegistration registry) {
		registry.addRecipes(BreweryRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.BREW_TYPE, BY_ID));
		registry.addRecipes(PureDaisyRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.PURE_DAISY_TYPE, BY_ID));
		registry.addRecipes(PetalApothecaryRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.PETAL_TYPE, BY_ID));
		registry.addRecipes(ElvenTradeRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.ELVEN_TRADE_TYPE, BY_ID));
		registry.addRecipes(RunicAltarRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.RUNE_TYPE, BY_ID));
		registry.addRecipes(ManaPoolRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.MANA_INFUSION_TYPE, BY_CATALYST.thenComparing(BY_GROUP).thenComparing(BY_ID)));
		registry.addRecipes(TerrestrialAgglomerationRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.TERRA_PLATE_TYPE, BY_ID));

		Comparator<OrechidRecipe> comp = BY_INPUT.thenComparing(BY_WEIGHT).thenComparing(BY_ID);
		registry.addRecipes(OrechidRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.ORECHID_TYPE, comp));
		registry.addRecipes(OrechidIgnemRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.ORECHID_IGNEM_TYPE, comp));
		registry.addRecipes(MarimorphosisRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.MARIMORPHOSIS_TYPE, comp));
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
		Collection<T> recipes = (Collection<T>) BotaniaRecipeTypes.getRecipes(Minecraft.getInstance().level, type).values();
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
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.brewery), BreweryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.alfPortal), ElvenTradeRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.manaPool), ManaPoolRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.creativePool), ManaPoolRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.dilutedPool), ManaPoolRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.fabulousPool), ManaPoolRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.defaultAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.forestAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.plainsAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.mountainAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.fungalAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.swampAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.desertAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.taigaAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.mesaAltar), PetalApothecaryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.mossyAltar), PetalApothecaryRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.orechid), OrechidRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.orechidFloating), OrechidRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.orechidIgnem), OrechidIgnemRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.orechidIgnemFloating), OrechidIgnemRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.marimorphosis), MarimorphosisRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.marimorphosisChibi), MarimorphosisRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.marimorphosisFloating), MarimorphosisRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.marimorphosisChibiFloating), MarimorphosisRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.pureDaisy), PureDaisyRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.pureDaisyFloating), PureDaisyRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.runeAltar), RunicAltarRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.terraPlate), TerrestrialAgglomerationRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaItems.autocraftingHalo), RecipeTypes.CRAFTING);
		registry.addRecipeCatalyst(new ItemStack(BotaniaItems.craftingHalo), RecipeTypes.CRAFTING);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		IRecipeManager recipeRegistry = jeiRuntime.getRecipeManager();
		// Hide the return recipes (iron ingot/diamond/ender pearl returns, not lexicon)
		for (ElvenTradeRecipe recipe : AlfheimPortalBlockEntity.elvenTradeRecipes(Minecraft.getInstance().level)) {
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
