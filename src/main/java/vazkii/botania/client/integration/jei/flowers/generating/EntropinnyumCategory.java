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
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.runtime.IIngredientManager;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.integration.jei.misc.ParticleDrawable;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Matrix4f;

import java.util.Collection;
import java.util.Collections;

public class EntropinnyumCategory extends SimpleGenerationCategory {

	private final IDrawable water;
	private final IDrawable barrier;
	private final ParticleDrawable particle;

	public EntropinnyumCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.entropinnyum, ModSubtiles.entropinnyumFloating);
		water = guiHelper.createDrawableIngredient(new ItemStack(Items.WATER_BUCKET));
		barrier = guiHelper.createDrawableIngredient(new ItemStack(Blocks.BARRIER));
		particle = new ParticleDrawable(drawable -> {
			int cycle = ClientTickHandler.ticksInGame % 80;
			if (cycle == 0) {

				for (int i = 0; i < 50; i++) {
					SparkleParticleData data = SparkleParticleData.sparkle((float) (Math.random() * 0.65F + 1.25F), 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 12);
					drawable.addParticle(data,
							Math.random() * 4 - 2,
							Math.random() * 4 - 2,
							0,
							0,
							0,
							0);
				}
			}

			if (cycle <= 8) {
				for (int i = 0; i < 6; ++i) {
					drawable.addParticle(ParticleTypes.EXPLOSION,
							(world.rand.nextDouble() - world.rand.nextDouble()) * 2.0D,
							(world.rand.nextDouble() - world.rand.nextDouble()) * 2.0D,
							(world.rand.nextDouble() - world.rand.nextDouble()) * 2.0D,
							cycle / 8F,
							0.0D,
							0.0D);
				}
			}
		});
	}

	@Override
	public void draw(SimpleManaGenRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		super.draw(recipe, matrixStack, mouseX, mouseY);
		particle.draw(matrixStack, 77,5);

		matrixStack.push();
		matrixStack.translate(97, 5, 0);
		Matrix4f matrix = matrixStack.getLast().getMatrix();
		RenderSystem.pushMatrix();
		RenderSystem.multMatrix(matrix);
		water.draw(matrixStack);
		barrier.draw(matrixStack);
		RenderSystem.popMatrix();
		matrixStack.pop();
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		return Collections.singleton(new SimpleManaGenRecipe(new ItemStack(Blocks.TNT), SubTileEntropinnyum.MAX_MANA));
	}

}
