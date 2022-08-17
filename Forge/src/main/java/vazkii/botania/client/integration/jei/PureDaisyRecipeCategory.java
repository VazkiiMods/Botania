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
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.recipe.IPureDaisyRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lib.LibMisc;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PureDaisyRecipeCategory implements IRecipeCategory<IPureDaisyRecipe> {

	public static final RecipeType<IPureDaisyRecipe> TYPE = RecipeType.create(LibMisc.MOD_ID, "pure_daisy", IPureDaisyRecipe.class);
	private final IDrawable background;
	private final Component localizedName;
	private final IDrawable overlay;
	private final IDrawable icon;
	@SuppressWarnings("rawtypes")
	private final IPlatformFluidHelper fluidHelper;

	public PureDaisyRecipeCategory(IGuiHelper guiHelper, IPlatformFluidHelper<?> fluidHelper) {
		background = guiHelper.createBlankDrawable(96, 44);
		localizedName = Component.translatable("botania.nei.pureDaisy");
		overlay = guiHelper.createDrawable(prefix("textures/gui/pure_daisy_overlay.png"),
				0, 0, 64, 44);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModSubtiles.pureDaisy));
		this.fluidHelper = fluidHelper;
	}

	@NotNull
	@Override
	public RecipeType<IPureDaisyRecipe> getRecipeType() {
		return TYPE;
	}

	@NotNull
	@Override
	public Component getTitle() {
		return localizedName;
	}

	@NotNull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@NotNull
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

	@SuppressWarnings("unchecked")
	@Override
	public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull IPureDaisyRecipe recipe, @NotNull IFocusGroup focusGroup) {
		StateIngredient input = recipe.getInput();

		IRecipeSlotBuilder inputSlotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, 9, 12)
				.setFluidRenderer(1000, false, 16, 16);
		for (var state : input.getDisplayed()) {
			if (!state.getFluidState().isEmpty()) {
				inputSlotBuilder.addIngredient(this.fluidHelper.getFluidIngredientType(),
						this.fluidHelper.create(state.getFluidState().getType(), 1000));
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
					.addIngredient(this.fluidHelper.getFluidIngredientType(),
							this.fluidHelper.create(outFluid.getType(), 1000));
		} else {
			if (outBlock.asItem() != Items.AIR) {
				builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 12)
						.addItemStack(new ItemStack(outBlock));
			}
		}
	}
}
