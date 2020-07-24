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

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.runtime.IIngredientManager;

import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;

import vazkii.botania.client.integration.jei.misc.ParticleDrawable;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.IntStream;

import static vazkii.botania.common.block.subtile.generating.SubTileDandelifeon.MANA_PER_GEN;
import static vazkii.botania.common.block.subtile.generating.SubTileDandelifeon.MAX_MANA_GENERATIONS;

public class DandelifeonCategory extends SimpleGenerationCategory {

	private final ParticleDrawable particle;

	public DandelifeonCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.dandelifeon, ModSubtiles.dandelifeonFloating);
		particle = new ParticleDrawable(drawable -> {
			if (world.rand.nextInt(8) == 0) {
				drawable.addParticle(ParticleTypes.ENCHANT,
						0.5,
						1.5,
						0.5,
						world.rand.nextFloat() - 0.5D,
						-world.rand.nextFloat() - 1.0F,
						world.rand.nextFloat() - 0.5D);
			}
		});
	}

	@Override
	public void draw(SimpleManaGenRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		super.draw(recipe, matrixStack, mouseX, mouseY);
		particle.draw(matrixStack, LEXICON_X, LEXICON_Y);
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		return Collections.singleton(new SimpleManaGenRecipe(Collections.singletonList(new ItemStack(ModBlocks.cellBlock)),
				IntStream.range(0, MAX_MANA_GENERATIONS)
						.map(gen -> MANA_PER_GEN)
						.toArray()));
	}

}
