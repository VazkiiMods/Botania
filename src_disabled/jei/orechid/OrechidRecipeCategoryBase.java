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
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import vazkii.botania.client.integration.jei.JEIBotaniaPlugin;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public abstract class OrechidRecipeCategoryBase<T extends OrechidRecipeWrapper> implements IRecipeCategory<T> {

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
	public abstract Class<? extends T> getRecipeClass();

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
	public void setIngredients(T recipe, IIngredients ingredients) {
		ingredients.setInput(VanillaTypes.ITEM, inputStack);

		final int myWeight = recipe.entry.getValue();
		final int amount = Math.max(1, Math.round((float) myWeight * 64 / getTotalOreWeight(getOreWeights(), myWeight)));

		// Shouldn't ever return an empty list since the ore weight
		// list is filtered to only have ores with ItemBlocks
		List<ItemStack> stackList = BlockTags.getContainer().getOrCreate(recipe.entry.getKey())
				.values()
				.stream()
				.filter(s -> s.asItem() != Items.AIR)
				.map(ItemStack::new)
				.collect(Collectors.toList());

		stackList.forEach(s -> s.setCount(amount));
		ingredients.setOutputLists(VanillaTypes.ITEM, Collections.singletonList(stackList));
	}

	public static float getTotalOreWeight(Map<Identifier, Integer> weights, int myWeight) {
		return (weights.entrySet().stream()
				.filter(e -> JEIBotaniaPlugin.doesOreExist(e.getKey()))
				.map(Map.Entry::getValue)
				.reduce(Integer::sum)).orElse(myWeight * 64 * 64);
	}

	protected abstract Map<Identifier, Integer> getOreWeights();

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull T recipeWrapper, @Nonnull IIngredients ingredients) {
		final IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

		itemStacks.init(0, true, 9, 12);
		itemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		itemStacks.init(1, true, 39, 12);
		itemStacks.set(1, iconStack);

		itemStacks.init(2, true, 68, 12);
		itemStacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
	}

	@Override
	public void draw(T recipe, MatrixStack ms, double mouseX, double mouseY) {
		RenderSystem.enableAlphaTest();
		RenderSystem.enableBlend();
		overlay.draw(ms, 17, 0);
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();
	}

}
