/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.orechid;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.block.AbstractBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import vazkii.botania.api.internal.OrechidOutput;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public abstract class OrechidRecipeCategoryBase implements IRecipeCategory<OrechidOutput> {

	private final IDrawableStatic background;
	private final String localizedName;
	private final IDrawableStatic overlay;
	private final IDrawable icon;
	private final ItemStack iconStack;
	private final ItemStack inputStack;

	public OrechidRecipeCategoryBase(IGuiHelper guiHelper, ItemStack iconStack, ItemStack inputStack, String localizedName) {
		overlay = guiHelper.createDrawable(prefix("textures/gui/pure_daisy_overlay.png"),
				0, 0, 64, 44);
		background = guiHelper.createBlankDrawable(96, 44);
		this.localizedName = localizedName;
		this.icon = guiHelper.createDrawableIngredient(iconStack);
		this.iconStack = iconStack;
		this.inputStack = inputStack;
	}

	@Nonnull
	@Override
	public Class<? extends OrechidOutput> getRecipeClass() {
		return OrechidOutput.class;
	}

	@Nonnull
	@Override
	public String getTitle() {
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
	public void setIngredients(OrechidOutput recipe, IIngredients ingredients) {
		ingredients.setInput(VanillaTypes.ITEM, inputStack);

		final int myWeight = recipe.itemWeight;
		final int amount = Math.max(1, Math.round((float) myWeight * 64 / getTotalOreWeight(getOreWeights(), myWeight)));

		// Shouldn't ever return an empty list since the ore weight
		// list is filtered to only have ores with ItemBlocks
		List<ItemStack> stackList = recipe.getOutput().getDisplayed().stream()
				.map(AbstractBlock.AbstractBlockState::getBlock)
				.filter(s -> s.asItem() != Items.AIR)
				.map(ItemStack::new)
				.collect(Collectors.toList());

		stackList.forEach(s -> s.setCount(amount));
		ingredients.setOutputLists(VanillaTypes.ITEM, Collections.singletonList(stackList));
	}

	public static float getTotalOreWeight(List<OrechidOutput> weights, int myWeight) {
		return (weights.stream()
				.map(OrechidOutput::getWeight)
				.reduce(Integer::sum)).orElse(myWeight * 64 * 64);
	}

	protected abstract List<OrechidOutput> getOreWeights();

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull OrechidOutput recipeWrapper, @Nonnull IIngredients ingredients) {
		final IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

		itemStacks.init(0, true, 9, 12);
		itemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		itemStacks.init(1, true, 39, 12);
		itemStacks.set(1, iconStack);

		itemStacks.init(2, true, 68, 12);
		itemStacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
	}

	@Override
	public void draw(OrechidOutput recipe, MatrixStack ms, double mouseX, double mouseY) {
		RenderSystem.enableAlphaTest();
		RenderSystem.enableBlend();
		overlay.draw(ms, 17, 0);
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();
	}

}
