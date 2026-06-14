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
import mezz.jei.api.recipe.RecipeType;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RunicAltarRecipe;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.mana.ManaPoolBlock;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class RunicAltarRecipeCategory extends BotaniaRecipeCategoryBase<RunicAltarRecipe> {

	public static final RecipeType<RunicAltarRecipe> TYPE =
			RecipeType.create(BotaniaAPI.MODID, "runic_altar", RunicAltarRecipe.class);
	private final IDrawable overlay;

	public RunicAltarRecipeCategory(IGuiHelper guiHelper) {
		super(114, 104, Component.translatable("botania.nei.runicAltar"),
				guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BotaniaBlocks.RUNIC_ALTAR)), null);
		overlay = guiHelper.createDrawable(botaniaRL("textures/gui/petal_overlay.png"),
				17, 11, 114, 82);
	}

	@Override
	public RecipeType<RunicAltarRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	public void draw(RunicAltarRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics gui, double mouseX, double mouseY) {
		super.draw(recipe, slotsView, gui, mouseX, mouseY);
		RenderSystem.enableBlend();
		overlay.draw(gui, 0, 4);
		HUDHandler.renderManaBar(gui, 6, 98, 0x0000FF, 0.75F, recipe.getMana(), ManaPoolBlock.MAX_MANA / 10);
		RenderSystem.disableBlend();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RunicAltarRecipe recipe, IFocusGroup focusGroup) {
		PetalApothecaryRecipeCategory.setRecipeLayout(builder, recipe.getIngredients(), recipe.getCatalysts(),
				BotaniaBlocks.RUNIC_ALTAR, recipe.getResultItem(RegistryAccess.EMPTY), recipe.getReagent());
	}

}
