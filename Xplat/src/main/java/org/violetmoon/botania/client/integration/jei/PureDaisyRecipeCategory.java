/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei;

import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.PureDaisyRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.BotaniaBlocks;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class PureDaisyRecipeCategory extends BotaniaRecipeCategoryBase<PureDaisyRecipe> {

	public static final RecipeType<PureDaisyRecipe> TYPE = RecipeType.create(BotaniaAPI.MODID, "pure_daisy", PureDaisyRecipe.class);
	private final IDrawable overlay;
	@SuppressWarnings("rawtypes")
	private final IPlatformFluidHelper fluidHelper;

	public PureDaisyRecipeCategory(IGuiHelper guiHelper, IPlatformFluidHelper<?> fluidHelper) {
		super(96, 44, Component.translatable("botania.nei.pureDaisy"),
				guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BotaniaBlocks.PURE_DAISY)), null);
		overlay = guiHelper.createDrawable(botaniaRL("textures/gui/pure_daisy_overlay.png"),
				0, 0, 64, 44);
		this.fluidHelper = fluidHelper;
	}

	@Override
	public RecipeType<PureDaisyRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	public void draw(PureDaisyRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics gui, double mouseX, double mouseY) {
		super.draw(recipe, slotsView, gui, mouseX, mouseY);
		RenderSystem.enableBlend();
		overlay.draw(gui, 17, 0);
		RenderSystem.disableBlend();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, PureDaisyRecipe recipe, IFocusGroup focusGroup) {
		buildSlot(recipe.getInput(), builder, RecipeIngredientRole.INPUT, 9, 12);

		builder.addSlot(RecipeIngredientRole.CATALYST, 39, 12)
				.addItemStack(new ItemStack(BotaniaBlocks.PURE_DAISY));

		buildSlot(recipe.getOutput(), builder, RecipeIngredientRole.OUTPUT, 68, 12);
	}

	@SuppressWarnings("unchecked")
	private void buildSlot(StateIngredient ingredient, IRecipeLayoutBuilder builder, RecipeIngredientRole role, int x, int y) {
		IRecipeSlotBuilder inputSlotBuilder = builder.addSlot(role, x, y)
				.setFluidRenderer(1000, false, 16, 16);
		for (var state : ingredient.getDisplayed()) {
			if (!state.getFluidState().isEmpty()) {
				inputSlotBuilder.addIngredient(this.fluidHelper.getFluidIngredientType(),
						this.fluidHelper.create(state.getFluidState().holder(), 1000));
			}
		}
		inputSlotBuilder.addItemStacks(ingredient.getDisplayedStacks())
				.addRichTooltipCallback((view, tooltip) -> tooltip.addAll(ingredient.descriptionTooltip()));
	}
}
