/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.manapool;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.integration.jei.JEIBotaniaPlugin;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.List;

public class ManaPoolRecipeCategory implements IRecipeCategory<IManaInfusionRecipe> {

	public static final ResourceLocation UID = new ResourceLocation(LibMisc.MOD_ID, "mana_pool");
	private final IDrawable background;
	private final String localizedName;
	private final IDrawable overlay;
	private final IDrawable icon;
	private final ItemStack renderStack = new ItemStack(ModBlocks.manaPool);

	public ManaPoolRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(168, 64);
		localizedName = I18n.format("botania.nei.manaPool");
		overlay = guiHelper.createDrawable(new ResourceLocation(LibMisc.MOD_ID, "textures/gui/pure_daisy_overlay.png"),
				0, 0, 64, 46);
		ItemNBTHelper.setBoolean(renderStack, "RenderFull", true);
		icon = guiHelper.createDrawableIngredient(renderStack.copy());
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
	public void setIngredients(IManaInfusionRecipe recipe, IIngredients iIngredients) {
		ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();

		builder.add(Arrays.asList(recipe.getIngredients().get(0).getMatchingStacks()));

		if (recipe.getCatalyst() != null) {
			Block block = recipe.getCatalyst().getBlock();
			if (block.asItem() != Items.AIR) {
				builder.add(ImmutableList.of(new ItemStack(block)));
			}
		}

		iIngredients.setInputLists(VanillaTypes.ITEM, builder.build());
		iIngredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
	}

	@Override
	public void draw(IManaInfusionRecipe recipe, MatrixStack ms, double mouseX, double mouseY) {
		RenderSystem.enableAlphaTest();
		RenderSystem.enableBlend();
		overlay.draw(ms, 48, 0);
		HUDHandler.renderManaBar(ms, 28, 50, 0x0000FF, 0.75F, recipe.getManaToConsume(), TilePool.MAX_MANA / 10);
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IManaInfusionRecipe recipe, @Nonnull IIngredients ingredients) {
		int index = 0;

		recipeLayout.getItemStacks().init(index, true, 40, 12);
		recipeLayout.getItemStacks().set(index, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		index++;

		if (ingredients.getInputs(VanillaTypes.ITEM).size() > 1) {
			// Has catalyst
			recipeLayout.getItemStacks().init(index, true, 20, 12);
			recipeLayout.getItemStacks().set(index, ingredients.getInputs(VanillaTypes.ITEM).get(1));
			index++;
		}

		recipeLayout.getItemStacks().init(index, true, 70, 12);
		recipeLayout.getItemStacks().set(index, renderStack);
		index++;

		recipeLayout.getItemStacks().init(index, false, 99, 12);
		recipeLayout.getItemStacks().set(index, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

		JEIBotaniaPlugin.addDefaultRecipeIdTooltip(recipeLayout.getItemStacks(), index, recipe.getId());
	}
}
