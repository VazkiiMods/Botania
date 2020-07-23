/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.flowers.generating;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.runtime.IIngredientManager;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.subtile.generating.SubTileDandelifeon;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Collections;

public class DandelifeonCategory extends SimpleGenerationCategory {

	public DandelifeonCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.dandelifeon, ModSubtiles.dandelifeonFloating);
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		return Collections.singleton(new SimpleManaGenRecipe(new ItemStack(ModBlocks.cellBlock), SubTileDandelifeon.MANA_PER_GEN * SubTileDandelifeon.MAX_MANA_GENERATIONS));
	}

	@Override
	public Class<? extends SimpleManaGenRecipe> getRecipeClass() {
		return SimpleManaGenRecipe.class;
	}

}
