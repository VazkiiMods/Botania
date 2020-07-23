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
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lib.ModTags;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static vazkii.botania.common.block.subtile.generating.SubTileRafflowsia.STREAK_OUTPUTS;

public class RafflowsiaCategory extends SimpleGenerationCategory {
	public RafflowsiaCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.rafflowsia, ModSubtiles.rafflowsiaFloating);
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		List<ItemStack> flowers = new ArrayList<>();
		for (Block block : ModTags.Blocks.SPECIAL_FLOWERS.getAllElements()) {
			if (block != ModSubtiles.rafflowsia) {
				flowers.add(new ItemStack(block));
			}
		}
		return Collections.singletonList(new SimpleManaGenRecipe(flowers, STREAK_OUTPUTS));
	}
}
