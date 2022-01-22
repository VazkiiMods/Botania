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
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;

import vazkii.botania.api.recipe.IPureDaisyRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.ModSubtiles;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PureDaisyRecipeCategory implements IRecipeCategory<IPureDaisyRecipe> {

	public static final ResourceLocation UID = prefix("pure_daisy");
	private final IDrawable background;
	private final Component localizedName;
	private final IDrawable overlay;
	private final IDrawable icon;

	public PureDaisyRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(96, 44);
		localizedName = new TranslatableComponent("botania.nei.pureDaisy");
		overlay = guiHelper.createDrawable(prefix("textures/gui/pure_daisy_overlay.png"),
				0, 0, 64, 44);
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
	public void setIngredients(IPureDaisyRecipe recipe, IIngredients iIngredients) {
		StateIngredient input = recipe.getInput();
		if (input.getDisplayedStacks().size() > 1) {
			iIngredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(input.getDisplayedStacks()));
		} else {
			BlockState state = input.getDisplayed().get(0);
			Block b = state.getBlock();

			if (!state.getFluidState().isEmpty()) {
				iIngredients.setInput(VanillaTypes.FLUID, new FluidStack(state.getFluidState().getType(), 1000));
			} else {
				if (b.asItem() != Items.AIR) {
					iIngredients.setInput(VanillaTypes.ITEM, new ItemStack(b));
				}
			}
		}

		Block outBlock = recipe.getOutputState().getBlock();
		FluidState outFluid = outBlock.defaultBlockState().getFluidState();
		if (!outFluid.isEmpty()) {
			iIngredients.setOutput(VanillaTypes.FLUID, new FluidStack(outFluid.getType(), 1000));
		} else {
			if (outBlock.asItem() != Items.AIR) {
				iIngredients.setOutput(VanillaTypes.ITEM, new ItemStack(outBlock));
			}
		}
	}

	@Override
	public void draw(IPureDaisyRecipe recipe, PoseStack ms, double mouseX, double mouseY) {
		RenderSystem.enableBlend();
		overlay.draw(ms, 17, 0);
		RenderSystem.disableBlend();
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IPureDaisyRecipe recipe, @Nonnull IIngredients ingredients) {
		boolean inputFluid = !ingredients.getInputs(VanillaTypes.FLUID).isEmpty();
		boolean outputFluid = !ingredients.getOutputs(VanillaTypes.FLUID).isEmpty();

		if (inputFluid) {
			recipeLayout.getFluidStacks().init(0, true, 9, 12, 16, 16, 1000, false, null);
			recipeLayout.getFluidStacks().set(0, ingredients.getInputs(VanillaTypes.FLUID).get(0));
		} else {
			recipeLayout.getItemStacks().init(0, true, 9, 12);
			recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
		}

		recipeLayout.getItemStacks().init(1, true, 39, 12);
		recipeLayout.getItemStacks().set(1, new ItemStack(ModSubtiles.pureDaisy));

		if (outputFluid) {
			recipeLayout.getFluidStacks().init(2, false, 68, 12, 16, 16, 1000, false, null);
			recipeLayout.getFluidStacks().set(2, ingredients.getOutputs(VanillaTypes.FLUID).get(0));
		} else {
			recipeLayout.getItemStacks().init(2, false, 68, 12);
			recipeLayout.getItemStacks().set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
		}

		IGuiIngredientGroup<?> group = outputFluid ? recipeLayout.getFluidStacks() : recipeLayout.getItemStacks();
		StateIngredient catalyst = recipe.getInput();
		if (catalyst != null) {
			List<Component> description = catalyst.descriptionTooltip();
			if (!description.isEmpty()) {
				group.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
					if (slotIndex == 0) {
						tooltip.addAll(description);
					}
				});
			}
		}
	}
}
