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
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.runtime.IIngredientManager;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.integration.jei.misc.ArbIngredientRenderer;
import vazkii.botania.client.integration.jei.misc.ParticleDrawable;
import vazkii.botania.common.block.ModSubtiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.function.Consumer;

import static vazkii.botania.common.block.subtile.generating.SubTileKekimurus.MANA_PER_SLICE;

public class KekimurusCategory extends SimpleGenerationCategory implements Consumer<ParticleDrawable> {

	public static final int TICKS_PER_SLICE = 20;
	private final CakeRenderer cakeRenderer;
	private final ParticleDrawable particle;

	public KekimurusCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.kekimurus, ModSubtiles.kekimurusFloating);
		cakeRenderer = new CakeRenderer();
		particle = new ParticleDrawable()
				.onTick(this);
	}

	@Override
	protected void setRecipeInputs(IRecipeLayout recipeLayout, SimpleManaGenRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(0, true, cakeRenderer, 76, 4, 18, 18, 1, 1);
		stacks.set(ingredients);
	}

	@Override
	public void draw(SimpleManaGenRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		super.draw(recipe, matrixStack, mouseX, mouseY);
		currentCake = recipe.getStacks().get(0);
		particle.draw(matrixStack, 77,5);
	}

	private ItemStack currentCake = ItemStack.EMPTY;

	@Override
	public void accept(ParticleDrawable drawable) {
		if(ClientTickHandler.ticksInGame % TICKS_PER_SLICE == 0) {
			BlockState state = getCakeState(currentCake);
			if(state == null || state.get(CakeBlock.BITES) == 0) {
				return;
			}
			BlockParticleData data = new BlockParticleData(ParticleTypes.BLOCK, state);
			for (int i = 0; i < 10; i++) {
				Random rand = new Random();
				Particle particle = drawable.addParticle(data,
						0.4 + 0.1 * rand.nextGaussian(),
						0.3 + 0.1 * rand.nextGaussian(),
						1.0 + 0.1 * rand.nextGaussian(),
						0.03 * rand.nextGaussian(),
						0.03 * rand.nextGaussian(),
						0.03 * rand.nextGaussian());
				if (particle != null) {
					particle.setMaxAge(5);
				}
			}
		}
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		ArrayList<SimpleManaGenRecipe> recipes = new ArrayList<>();
		for (Block block : ForgeRegistries.BLOCKS) {
			if (!(block instanceof CakeBlock)) {
				continue;
			}
			recipes.add(new SimpleManaGenRecipe(block, MAX_SLICES * MANA_PER_SLICE));
		}
		return recipes;
	}

	public static final int MAX_SLICES = Collections.max(CakeBlock.BITES.getAllowedValues());

	private static class CakeRenderer extends ArbIngredientRenderer<ItemStack> {

		@Override
		public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable ItemStack ingredient) {
			if(ingredient == null) {
				return;
			}

			BlockState state = getCakeState(ingredient);

			if(state == null) {
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

	@Nullable
	private static BlockState getCakeState(ItemStack ingredient) {
		BlockState state;
		try {
			Block block = ((BlockItem) ingredient.getItem()).getBlock();
			state = block.getDefaultState().with(CakeBlock.BITES, ClientTickHandler.ticksInGame / TICKS_PER_SLICE % MAX_SLICES);
		} catch (IllegalArgumentException e) {
			state = null;
		}
		return state;
	}

}
