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
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;

import vazkii.botania.api.recipe.IPureDaisyRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.ModSubtiles;

import javax.annotation.Nonnull;

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
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ModSubtiles.pureDaisy));
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
	public void draw(IPureDaisyRecipe recipe, IRecipeSlotsView slotsView, PoseStack ms, double mouseX, double mouseY) {
		RenderSystem.enableBlend();
		overlay.draw(ms, 17, 0);
		RenderSystem.disableBlend();
	}

	// todo fluid input rendering is probably kinda janky with waterlogged blocks. Might be worth getting rid of it entirely and only checking for full fluid blocks
	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull IPureDaisyRecipe recipe, @Nonnull IFocusGroup focusGroup) {
		StateIngredient input = recipe.getInput();

		IRecipeSlotBuilder inputSlotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, 9, 12)
				.setFluidRenderer(1000, false, 16, 16);
		for (var state : input.getDisplayed()) {
			if (!state.getFluidState().isEmpty()) {
				inputSlotBuilder.addIngredient(VanillaTypes.FLUID, new FluidStack(state.getFluidState().getType(), 1000));
			}
		}
		inputSlotBuilder.addItemStacks(input.getDisplayedStacks())
				.addTooltipCallback((view, tooltip) -> tooltip.addAll(input.descriptionTooltip()));

		builder.addSlot(RecipeIngredientRole.CATALYST, 39, 12)
				.addItemStack(new ItemStack(ModSubtiles.pureDaisy));

		Block outBlock = recipe.getOutputState().getBlock();
		FluidState outFluid = outBlock.defaultBlockState().getFluidState();
		if (!outFluid.isEmpty()) {
			builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 12)
					.setFluidRenderer(1000, false, 16, 16)
					.addIngredient(VanillaTypes.FLUID, new FluidStack(outFluid.getType(), 1000));
		} else {
			if (outBlock.asItem() != Items.AIR) {
				builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 12)
						.addItemStack(new ItemStack(outBlock));
			}
		}
	}
}
