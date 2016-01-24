/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.manapool;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nonnull;
import java.util.List;

public class ManaPoolRecipeWrapper implements IRecipeWrapper {

	private final List input;
	private final ItemStack output;
	private final int mana;

	public ManaPoolRecipeWrapper(RecipeManaInfusion recipe) {
		ImmutableList.Builder builder = ImmutableList.builder();

		if(recipe.getInput() instanceof ItemStack) {
			builder.add(recipe.getInput());
		} else if(recipe.getInput() instanceof String) {
			builder.add(OreDictionary.getOres(((String) recipe.getInput())));
		}

		if(recipe.isAlchemy()) {
			builder.add(new ItemStack(ModBlocks.alchemyCatalyst));
		} else if(recipe.isConjuration()) {
			builder.add(new ItemStack(ModBlocks.conjurationCatalyst));
		}

		input = builder.build();
		output = recipe.getOutput();
		mana = recipe.getManaToConsume();
	}

	@Override
	public List getInputs() {
		return input;
	}

	@Override
	public List getOutputs() {
		return ImmutableList.of(output);
	}

	@Override
	public List<FluidStack> getFluidInputs() {
		return ImmutableList.of();
	}

	@Override
	public List<FluidStack> getFluidOutputs() {
		return ImmutableList.of();
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
		GlStateManager.enableAlpha();
		HUDHandler.renderManaBar(28, 50, 0x0000FF, 0.75F, mana, TilePool.MAX_MANA / 10);
		GlStateManager.disableAlpha();
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
	}

}
