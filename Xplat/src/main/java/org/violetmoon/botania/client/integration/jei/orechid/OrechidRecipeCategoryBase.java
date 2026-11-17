/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.orechid;

import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.OrechidRecipe;
import vazkii.botania.client.integration.jei.BotaniaRecipeCategoryBase;
import vazkii.botania.client.integration.shared.OrechidUIHelper;

import java.util.stream.Stream;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public abstract class OrechidRecipeCategoryBase extends BotaniaRecipeCategoryBase<OrechidRecipe> {

	private final IDrawableStatic overlay;
	private final ItemStack iconStack;

	public OrechidRecipeCategoryBase(IGuiHelper guiHelper, ItemStack iconStack, Component localizedName) {
		super(96, 44, localizedName,
				guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, iconStack), null);
		this.overlay = guiHelper.createDrawable(botaniaRL("textures/gui/pure_daisy_overlay.png"),
				0, 0, 64, 44);
		this.iconStack = iconStack;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, OrechidRecipe recipe, IFocusGroup focusGroup) {

		builder.addSlot(RecipeIngredientRole.INPUT, 9, 12)
				.addItemStacks(recipe.getInput().getDisplayedStacks());
		builder.addSlot(RecipeIngredientRole.CATALYST, 39, 12).addItemStack(iconStack);

		builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 12)
				.addItemStacks(recipe.getOutput().getDisplayedStacks())
				.addRichTooltipCallback((view, tooltip) -> tooltip.addAll(recipe.getOutput().descriptionTooltip()));
	}

	@Override
	public void draw(OrechidRecipe recipe, IRecipeSlotsView view, GuiGraphics gui, double mouseX, double mouseY) {
		super.draw(recipe, view, gui, mouseX, mouseY);
		final Double chance = getChance(recipe);
		if (chance != null) {
			final Component chanceComponent = OrechidUIHelper.getPercentageComponent(chance);
			Font font = Minecraft.getInstance().font;
			int xOffset = 90 - font.width(chanceComponent);
			gui.drawString(font, chanceComponent, xOffset, 1, 0x888888, false);
		}
		RenderSystem.enableBlend();
		overlay.draw(gui, 17, 0);
		RenderSystem.disableBlend();
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, OrechidRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (mouseX > 0.6 * getWidth() && mouseX < 90 && mouseY < 12) {
			final Double chance = getChance(recipe);
			if (chance != null) {
				getChanceTooltipComponents(chance, recipe).forEach(tooltip::add);
			}
		}
	}

	protected Stream<Component> getChanceTooltipComponents(double chance, OrechidRecipe recipe) {
		final var ratio = OrechidUIHelper.getRatioForChance(chance);
		Stream<Component> genericChanceTooltipComponents = Stream.of(OrechidUIHelper.getRatioTooltipComponent(ratio));
		Stream<Component> biomeChanceTooltipComponents = OrechidUIHelper.getBiomeChanceAndRatioTooltipComponents(chance, recipe);
		return Stream.concat(genericChanceTooltipComponents, biomeChanceTooltipComponents);
	}

	@Nullable
	protected Double getChance(OrechidRecipe recipe) {
		return OrechidUIHelper.getChance(recipe, null);
	}
}
