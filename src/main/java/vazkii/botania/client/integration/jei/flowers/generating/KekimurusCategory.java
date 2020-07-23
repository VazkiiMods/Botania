/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.flowers.generating;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.runtime.IIngredientManager;
import vazkii.botania.client.integration.jei.misc.ArbIngredientRenderer;
import vazkii.botania.common.block.ModSubtiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class KekimurusCategory extends SimpleGenerationCategory {

	private final CakeRenderer cakeRenderer;

	public KekimurusCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.kekimurus, ModSubtiles.kekimurusFloating);
		cakeRenderer = new CakeRenderer(guiHelper.createTickTimer(120, 6, false));
	}

	@Override
	protected void setRecipeInputs(IRecipeLayout recipeLayout, SimpleManaGenRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(0, true, cakeRenderer, 76, 4, 18, 18, 1, 1);
		stacks.set(ingredients);
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		ArrayList<SimpleManaGenRecipe> recipes = new ArrayList<>();
		for (Block block : ForgeRegistries.BLOCKS) {
			if (!(block instanceof CakeBlock)) {
				continue;
			}
			recipes.add(new KekimurusRecipe((CakeBlock) block));
		}
		return recipes;
	}

	@Override
	public Class<? extends SimpleManaGenRecipe> getRecipeClass() {
		return KekimurusRecipe.class;
	}

	private static class KekimurusRecipe extends SimpleManaGenRecipe {

		public static final int MAX_SLICES = Collections.max(CakeBlock.BITES.getAllowedValues());

		public KekimurusRecipe(CakeBlock cake) {
			super(Collections.singletonList(new ItemStack(cake)), 1800 * MAX_SLICES);
		}

	}

	private static class CakeRenderer extends ArbIngredientRenderer<ItemStack> {

		private final ITickTimer tickTimer;

		public CakeRenderer(ITickTimer tickTimer) {
			this.tickTimer = tickTimer;
		}

		@Override
		public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable ItemStack ingredient) {
			if(ingredient == null) {
				return;
			}

			Block block = ((BlockItem) ingredient.getItem()).getBlock();
			BlockState state;
			try {
				state = block.getDefaultState().with(CakeBlock.BITES, tickTimer.getValue());
			} catch (IllegalArgumentException e) {
				fallback(ingredient).render(matrixStack, xPosition, yPosition, ingredient);
				return;
			}

			Minecraft.getInstance().textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);

			matrixStack.push();

			setupDefaultBlockTransforms(matrixStack, xPosition, yPosition);

			IRenderTypeBuffer.Impl buffers = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
			BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
			brd.renderBlock(state, matrixStack, buffers, 0xF00000, 0xF00000, EmptyModelData.INSTANCE);
			buffers.finish();

			matrixStack.pop();
		}
	}

}
