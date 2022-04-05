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
import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.helper.ItemNBTHelper;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ManaPoolRecipeCategory implements IRecipeCategory<IManaInfusionRecipe> {

	public static final ResourceLocation UID = prefix("mana_pool");
	private final IDrawable background;
	private final Component localizedName;
	private final IDrawable overlay;
	private final IDrawable icon;
	private final ItemStack renderStack = new ItemStack(ModBlocks.manaPool);

	public ManaPoolRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(142, 55);
		localizedName = new TranslatableComponent("botania.nei.manaPool");
		overlay = guiHelper.createDrawable(prefix("textures/gui/pure_daisy_overlay.png"),
				0, 0, 64, 46);
		ItemNBTHelper.setBoolean(renderStack, "RenderFull", true);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, renderStack.copy());
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends IManaInfusionRecipe> getRecipeClass() {
		return IManaInfusionRecipe.class;
	}

	@Nonnull
	@Override
	public Component getTitle() {
		return localizedName;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Nonnull
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void draw(IManaInfusionRecipe recipe, @Nonnull IRecipeSlotsView slotsView, @Nonnull PoseStack ms, double mouseX, double mouseY) {
		RenderSystem.enableBlend();
		overlay.draw(ms, 40, 0);
		HUDHandler.renderManaBar(ms, 20, 50, 0x0000FF, 0.75F, recipe.getManaToConsume(), TilePool.MAX_MANA / 10);
		RenderSystem.disableBlend();
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull IManaInfusionRecipe recipe, @Nonnull IFocusGroup focusGroup) {
		builder.addSlot(RecipeIngredientRole.INPUT, 32, 12)
				.addIngredients(recipe.getIngredients().get(0));

		var catalyst = recipe.getRecipeCatalyst();
		if (catalyst != null) {
			builder.addSlot(RecipeIngredientRole.CATALYST, 12, 12)
					.addItemStacks(catalyst.getDisplayedStacks())
					.addTooltipCallback((view, tooltip) -> tooltip.addAll(catalyst.descriptionTooltip()));
		}

		builder.addSlot(RecipeIngredientRole.CATALYST, 62, 12).addItemStack(renderStack);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 93, 12).addItemStack(recipe.getResultItem());
	}
}
