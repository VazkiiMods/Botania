/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.flowers.generating;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.runtime.IIngredientManager;

import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;

import vazkii.botania.client.integration.jei.misc.ParticleDrawable;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.subtile.generating.SubTileEndoflame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EndoflameCategory extends SimpleGenerationCategory {

	public EndoflameCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.endoflame, ModSubtiles.endoflameFloating);
		particle = new ParticleDrawable((drawable) -> {
			if (world.rand.nextInt(10) == 0) {
				drawable.addParticle(ParticleTypes.FLAME,
						0.4 + Math.random() * 0.2,
						0.7,
						0,
						0.0D,
						0.0D,
						0.0D);
			}
		});
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		List<SimpleManaGenRecipe> recipes = new ArrayList<>();
		for (ItemStack stack : ingredientManager.getAllIngredients(VanillaTypes.ITEM)) {
			if (stack.getItem().hasContainerItem(stack)) {
				continue;
			}
			int burnTime = SubTileEndoflame.getBurnTime(stack);
			if (burnTime > 0) {
				recipes.add(new SimpleManaGenRecipe(stack, burnTime * SubTileEndoflame.GEN));
			}
		}
		return recipes;
	}

}
