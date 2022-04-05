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
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;

import vazkii.botania.api.recipe.IPetalRecipe;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PetalApothecaryRecipeCategory implements IRecipeCategory<IPetalRecipe> {

	public static final ResourceLocation UID = prefix("petals");
	private final IDrawableStatic background;
	private final Component localizedName;
	private final IDrawableStatic overlay;
	private final IDrawable icon;

	public PetalApothecaryRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(114, 97);
		localizedName = new TranslatableComponent("botania.nei.petalApothecary");
		overlay = guiHelper.createDrawable(prefix("textures/gui/petal_overlay.png"),
				17, 11, 114, 82);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ModBlocks.defaultAltar));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends IPetalRecipe> getRecipeClass() {
		return IPetalRecipe.class;
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
	public void draw(@Nonnull IPetalRecipe recipe, @Nonnull IRecipeSlotsView slotsView, @Nonnull PoseStack ms, double mouseX, double mouseY) {
		RenderSystem.enableBlend();
		overlay.draw(ms, 0, 4);
		RenderSystem.disableBlend();
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull IPetalRecipe recipe, @Nonnull IFocusGroup focusGroup) {
		builder.addSlot(RecipeIngredientRole.CATALYST, 48, 45)
				.addItemStack(new ItemStack(ModBlocks.defaultAltar));

		double angleBetweenEach = 360.0 / recipe.getIngredients().size();
		Vec2 point = new Vec2(48, 13), center = new Vec2(48, 45);

		for (var ingr : recipe.getIngredients()) {
			builder.addSlot(RecipeIngredientRole.INPUT, (int) point.x, (int) point.y)
					.addIngredients(ingr);
			point = rotatePointAbout(point, center, angleBetweenEach);
		}

		builder.addSlot(RecipeIngredientRole.OUTPUT, 86, 10)
				.addItemStack(recipe.getResultItem());
	}

	public static Vec2 rotatePointAbout(Vec2 in, Vec2 about, double degrees) {
		double rad = degrees * Math.PI / 180.0;
		double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
		double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
		return new Vec2((float) newX, (float) newY);
	}
}
