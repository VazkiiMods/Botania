/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.puredaisy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import vazkii.botania.api.recipe.IPureDaisyRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.client.integration.jei.JEIBotaniaPlugin;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.stream.Collectors;

public class PureDaisyRecipeCategory implements IRecipeCategory<IPureDaisyRecipe> {

	public static final ResourceLocation UID = new ResourceLocation(LibMisc.MOD_ID, "pure_daisy");
	private final IDrawable background;
	private final String localizedName;
	private final IDrawable overlay;
	private final IDrawable icon;

	public PureDaisyRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(168, 46);
		localizedName = I18n.format("botania.nei.pureDaisy");
		overlay = guiHelper.createDrawable(new ResourceLocation(LibMisc.MOD_ID, "textures/gui/pure_daisy_overlay.png"),
				0, 0, 64, 46);
		icon = guiHelper.createDrawableIngredient(new ItemStack(ModSubtiles.pureDaisy));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends IPureDaisyRecipe> getRecipeClass() {
		return IPureDaisyRecipe.class;
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
	public void setIngredients(IPureDaisyRecipe recipe, IIngredients iIngredients) {
		StateIngredient input = recipe.getInput();
		if (input.getDisplayed().size() > 1) {
			iIngredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(input.getDisplayed().stream()
					.map(BlockState::getBlock)
					.map(ItemStack::new)
					.filter(s -> !s.isEmpty())
					.collect(Collectors.toList())));
		} else {
			BlockState state = input.getDisplayed().get(0);
			Block b = state.getBlock();

			if (b instanceof FlowingFluidBlock) {
				iIngredients.setInput(VanillaTypes.FLUID, new FluidStack(((FlowingFluidBlock) b).getFluid(), 1000));
			} else {
				if (b.asItem() != Items.AIR) {
					iIngredients.setInput(VanillaTypes.ITEM, new ItemStack(b));
				}
			}
		}

		Block outBlock = recipe.getOutputState().getBlock();
		if (outBlock instanceof FlowingFluidBlock) {
			iIngredients.setOutput(VanillaTypes.FLUID, new FluidStack(((FlowingFluidBlock) outBlock).getFluid(), 1000));
		} else {
			if (outBlock.asItem() != Items.AIR) {
				iIngredients.setOutput(VanillaTypes.ITEM, new ItemStack(outBlock));
			}
		}
	}

	@Override
	public void draw(IPureDaisyRecipe recipe, MatrixStack ms, double mouseX, double mouseY) {
		RenderSystem.enableAlphaTest();
		RenderSystem.enableBlend();
		overlay.draw(ms, 48, 0);
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IPureDaisyRecipe recipe, @Nonnull IIngredients ingredients) {
		boolean inputFluid = !ingredients.getInputs(VanillaTypes.FLUID).isEmpty();
		boolean outputFluid = !ingredients.getOutputs(VanillaTypes.FLUID).isEmpty();

		if (inputFluid) {
			recipeLayout.getFluidStacks().init(0, true, 40, 12, 16, 16, 1000, false, null);
			recipeLayout.getFluidStacks().set(0, ingredients.getInputs(VanillaTypes.FLUID).get(0));
		} else {
			recipeLayout.getItemStacks().init(0, true, 40, 12);
			recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
		}

		recipeLayout.getItemStacks().init(1, true, 70, 12);
		recipeLayout.getItemStacks().set(1, new ItemStack(ModSubtiles.pureDaisy));

		if (outputFluid) {
			recipeLayout.getFluidStacks().init(2, false, 99, 12, 16, 16, 1000, false, null);
			recipeLayout.getFluidStacks().set(2, ingredients.getOutputs(VanillaTypes.FLUID).get(0));
		} else {
			recipeLayout.getItemStacks().init(2, false, 99, 12);
			recipeLayout.getItemStacks().set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
		}

		IGuiIngredientGroup<?> group = outputFluid ? recipeLayout.getFluidStacks() : recipeLayout.getItemStacks();
		JEIBotaniaPlugin.addDefaultRecipeIdTooltip(group, 2, recipe.getId());
	}
}
