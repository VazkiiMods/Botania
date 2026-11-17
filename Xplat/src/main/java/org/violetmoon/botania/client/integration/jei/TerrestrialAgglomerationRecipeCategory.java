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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.TerrestrialAgglomerationRecipe;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.common.block.BotaniaBlocks;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class TerrestrialAgglomerationRecipeCategory extends BotaniaRecipeCategoryBase<TerrestrialAgglomerationRecipe> {
	public static final RecipeType<TerrestrialAgglomerationRecipe> TYPE =
			RecipeType.create(BotaniaAPI.MODID, "terra_plate", TerrestrialAgglomerationRecipe.class);

	private final IDrawable overlay;

	private final IDrawable terraPlate;

	public TerrestrialAgglomerationRecipeCategory(IGuiHelper guiHelper) {
		super(114, 131, Component.translatable("botania.nei.terraPlate"),
				guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BotaniaBlocks.TERRESTRIAL_AGGLOMERATION_PLATE)), null);
		overlay = guiHelper.createDrawable(botaniaRL("textures/gui/terrasteel_jei_overlay.png"), 42, 29, 64, 64);

		IDrawable livingrock = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BotaniaBlocks.LIVINGROCK));
		terraPlate = new TerrestrialAgglomerationDrawable(livingrock, livingrock,
				guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.LAPIS_BLOCK))
		);
	}

	@Override
	public RecipeType<TerrestrialAgglomerationRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	public void draw(TerrestrialAgglomerationRecipe recipe, IRecipeSlotsView view, GuiGraphics gui, double mouseX, double mouseY) {
		super.draw(recipe, view, gui, mouseX, mouseY);
		RenderSystem.enableBlend();
		overlay.draw(gui, 25, 14);
		HUDHandler.renderManaBar(gui, 6, 126, 0x0000FF, 0.75F, recipe.getMana(), 100000);
		terraPlate.draw(gui, 35, 92);
		RenderSystem.disableBlend();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, TerrestrialAgglomerationRecipe recipe, IFocusGroup focusGroup) {
		builder.addSlot(RecipeIngredientRole.OUTPUT, 48, 37)
				.addItemStack(recipe.getResultItem(getRegistryAccess()));

		double angleBetweenEach = 360.0 / recipe.getIngredients().size();
		Vec2 point = new Vec2(48, 5), center = new Vec2(48, 37);

		for (var ingr : recipe.getIngredients()) {
			builder.addSlot(RecipeIngredientRole.INPUT, (int) point.x, (int) point.y)
					.addIngredients(ingr);
			point = PetalApothecaryRecipeCategory.rotatePointAbout(point, center, angleBetweenEach);
		}

		builder.addSlot(RecipeIngredientRole.CATALYST, 48, 92)
				.addItemStack(new ItemStack(BotaniaBlocks.TERRESTRIAL_AGGLOMERATION_PLATE));
	}
}
