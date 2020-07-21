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
import net.minecraft.block.Block;
import net.minecraft.block.CakeBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class KekimurusCategory extends SimpleGenerationCategory {

	public KekimurusCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.kekimurus, ModSubtiles.kekimurusFloating);
	}

	@Override
	protected Collection<ISimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		ArrayList<ISimpleManaGenRecipe> recipes = new ArrayList<>();
		for (Block block : ForgeRegistries.BLOCKS) {
			if (!(block instanceof CakeBlock)) {
				continue;
			}
			recipes.add(new KekimurusRecipe((CakeBlock) block));
		}
		return recipes;
	}

	@Override
	public Class<? extends ISimpleManaGenRecipe> getRecipeClass() {
		return KekimurusRecipe.class;
	}

	private static class KekimurusRecipe implements ISimpleManaGenRecipe {

		public static final int MAX_SLICES = Collections.max(CakeBlock.BITES.getAllowedValues());
		private final ItemStack cake;

		public KekimurusRecipe(CakeBlock cake) {
			this.cake = new ItemStack(cake);
		}

		@Override
		public int getMana() {
			return 1800 * MAX_SLICES;
		}

		@Override
		public ItemStack getStack() {
			return cake;
		}

	}

}
