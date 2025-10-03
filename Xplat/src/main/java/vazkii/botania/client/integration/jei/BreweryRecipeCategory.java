/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.BotanicalBreweryRecipe;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class BreweryRecipeCategory extends BotaniaRecipeCategoryBase<BotanicalBreweryRecipe> {

	public static final RecipeType<BotanicalBreweryRecipe> TYPE = RecipeType.create(BotaniaAPI.MODID, "brewery", BotanicalBreweryRecipe.class);
	public static final int WIDTH = 131;
	public static final int HEIGHT = 55;

	public BreweryRecipeCategory(IGuiHelper guiHelper) {
		super(WIDTH, HEIGHT, Component.translatable("botania.nei.brewery"),
				guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BotaniaBlocks.brewery)),
				guiHelper.createDrawable(botaniaRL("textures/gui/nei_brewery.png"), 28, 6, WIDTH, HEIGHT));
	}

	@Override
	public RecipeType<BotanicalBreweryRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, BotanicalBreweryRecipe recipe, IFocusGroup focuses) {
		List<ItemStack> outputs = new ArrayList<>();
		List<ItemStack> containers = new ArrayList<>();

		for (var container : new ItemStack[] {
				new ItemStack(BotaniaItems.vial), new ItemStack(BotaniaItems.flask),
				new ItemStack(BotaniaItems.incenseStick), new ItemStack(BotaniaItems.bloodPendant)
		}) {
			ItemStack brewed = recipe.getOutput(container);
			if (!brewed.isEmpty()) {
				containers.add(container);
				outputs.add(brewed);
			}
		}

		IFocus<ItemStack> outputFocus = focuses.getFocuses(VanillaTypes.ITEM_STACK, RecipeIngredientRole.OUTPUT).findAny().orElse(null);
		IFocus<ItemStack> inputFocus = focuses.getFocuses(VanillaTypes.ITEM_STACK, RecipeIngredientRole.INPUT).findAny().orElse(null);

		builder.addSlot(RecipeIngredientRole.INPUT, 11, 36)
				.addItemStacks(getItemMatchingFocus(outputFocus, outputs, containers));

		var inputs = recipe.getIngredients();
		int posX = 67 - (inputs.size() * 9);
		for (var ingr : inputs) {
			builder.addSlot(RecipeIngredientRole.INPUT, posX, 0)
					.addIngredients(ingr);
			posX += 18;
		}

		builder.addSlot(RecipeIngredientRole.OUTPUT, 59, 36)
				.addItemStacks(getItemMatchingFocus(inputFocus, containers, outputs));
	}

	/**
	 * If an item in this recipe is focused, returns the corresponding item instead of all the containers/results.
	 */
	private List<ItemStack> getItemMatchingFocus(@Nullable IFocus<ItemStack> focus, List<ItemStack> focused, List<ItemStack> other) {
		if (focus != null) {
			ItemStack focusStack = focus.getTypedValue().getIngredient();
			for (int i = 0; i < focused.size(); i++) {
				if (ItemStack.isSameItem(focusStack, focused.get(i))) {
					return Collections.singletonList(other.get(i));
				}
			}
		}
		return other;
	}
}
