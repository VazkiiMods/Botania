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
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.ManaInfusionRecipe;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.crafting.StateIngredients;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class ManaPoolRecipeCategory extends BotaniaRecipeCategoryBase<ManaInfusionRecipe> {

	public static final RecipeType<ManaInfusionRecipe> TYPE =
			RecipeType.create(BotaniaAPI.MODID, "mana_pool", ManaInfusionRecipe.class);
	private final IDrawable overlay;
	private final ItemStack renderStack;

	public ManaPoolRecipeCategory(IGuiHelper guiHelper) {
		super(142, 55, Component.translatable("botania.nei.manaPool"),
				guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, makeFullPool()), null);
		overlay = guiHelper.createDrawable(botaniaRL("textures/gui/pure_daisy_overlay.png"),
				0, 0, 64, 46);
		renderStack = makeFullPool();
	}

	private static ItemStack makeFullPool() {
		ItemStack stack = new ItemStack(BotaniaBlocks.manaPool);
		stack.set(BotaniaDataComponents.RENDER_FULL, Unit.INSTANCE);
		return stack;
	}

	@Override
	public RecipeType<ManaInfusionRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	public void draw(ManaInfusionRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics gui, double mouseX, double mouseY) {
		super.draw(recipe, slotsView, gui, mouseX, mouseY);
		RenderSystem.enableBlend();
		overlay.draw(gui, 40, 0);
		HUDHandler.renderManaBar(gui, 20, 50, 0x0000FF, 0.75F, recipe.getManaToConsume(), ManaPoolBlock.MAX_MANA / 10);
		RenderSystem.disableBlend();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ManaInfusionRecipe recipe, IFocusGroup focusGroup) {
		builder.addSlot(RecipeIngredientRole.INPUT, 32, 12)
				.addIngredients(recipe.getIngredients().getFirst());

		var catalyst = recipe.getRecipeCatalyst();
		if (catalyst != StateIngredients.NONE) {
			builder.addSlot(RecipeIngredientRole.CATALYST, 12, 12)
					.addItemStacks(catalyst.getDisplayedStacks())
					.addRichTooltipCallback((view, tooltip) -> tooltip.addAll(catalyst.descriptionTooltip()));
		}

		builder.addSlot(RecipeIngredientRole.CATALYST, 62, 12).addItemStack(renderStack);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 93, 12).addItemStack(recipe.getResultItem(getRegistryAccess()));
	}
}
