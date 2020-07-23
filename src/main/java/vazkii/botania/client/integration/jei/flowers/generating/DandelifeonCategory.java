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
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.IntStream;

import static vazkii.botania.common.block.subtile.generating.SubTileDandelifeon.MANA_PER_GEN;
import static vazkii.botania.common.block.subtile.generating.SubTileDandelifeon.MAX_MANA_GENERATIONS;

public class DandelifeonCategory extends SimpleGenerationCategory {

	public DandelifeonCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.dandelifeon, ModSubtiles.dandelifeonFloating);
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		return Collections.singleton(new SimpleManaGenRecipe(Collections.singletonList(new ItemStack(ModBlocks.cellBlock)),
				IntStream.range(0, MAX_MANA_GENERATIONS)
						.map(gen -> MANA_PER_GEN)
						.toArray()));
	}

}
