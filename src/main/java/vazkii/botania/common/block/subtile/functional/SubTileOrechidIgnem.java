/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.recipe.IOrechidRecipe;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.crafting.ModRecipeTypes;

public class SubTileOrechidIgnem extends SubTileOrechid {
	private static final int COST = 20000;

	public SubTileOrechidIgnem(BlockPos pos, BlockState state) {
		super(ModSubtiles.ORECHID_IGNEM, pos, state);
	}

	@Override
	public boolean canOperate() {
		return getLevel().dimensionType().hasCeiling();
	}

	@Override
	public RecipeType<? extends IOrechidRecipe> getRecipeType() {
		return ModRecipeTypes.ORECHID_IGNEM_TYPE;
	}

	@Override
	public int getCost() {
		return COST;
	}

	@Override
	public int getColor() {
		return 0xAE3030;
	}

}
