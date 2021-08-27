/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.api.item.IExoflameHeatable;
import vazkii.botania.common.Botania;
import vazkii.botania.mixin.AccessorAbstractFurnaceBlockEntity;

public class ExoflameFurnaceHandler {
	public static boolean canSmelt(AbstractFurnaceBlockEntity furnace, Recipe<?> recipe) {
		var items = ((AccessorAbstractFurnaceBlockEntity) furnace).getItems();
		return AccessorAbstractFurnaceBlockEntity.botania_canAcceptRecipeOutput(recipe, items, furnace.getMaxStackSize());
	}

	public static RecipeType<? extends AbstractCookingRecipe> getRecipeType(AbstractFurnaceBlockEntity furnace) {
		return ((AccessorAbstractFurnaceBlockEntity) furnace).getRecipeType();
	}

	public static class FurnaceExoflameHeatable implements IExoflameHeatable {
		private final AbstractFurnaceBlockEntity furnace;

		private RecipeType<? extends AbstractCookingRecipe> recipeType;
		private AbstractCookingRecipe currentRecipe;

		public FurnaceExoflameHeatable(AbstractFurnaceBlockEntity furnace) {
			this.furnace = furnace;
		}

		@Override
		public boolean canSmelt() {
			if (furnace.getItem(0).isEmpty()) {
				return false;
			}
			try {
				if (recipeType == null) {
					this.recipeType = ExoflameFurnaceHandler.getRecipeType(furnace);
				}
				if (currentRecipe != null) { // This is already more caching than Mojang does
					if (currentRecipe.matches(furnace, furnace.getLevel())
							&& ExoflameFurnaceHandler.canSmelt(furnace, currentRecipe)) {
						return true;
					}
				}
				currentRecipe = furnace.getLevel().getRecipeManager().getRecipeFor(recipeType, furnace, furnace.getLevel()).orElse(null);
				return ExoflameFurnaceHandler.canSmelt(furnace, currentRecipe);
			} catch (Throwable t) {
				Botania.LOGGER.error("Failed to determine if furnace TE can smelt", t);
				return false;
			}
		}

		@Override
		public int getBurnTime() {
			return ((AccessorAbstractFurnaceBlockEntity) furnace).getLitTime();
		}

		@Override
		public void boostBurnTime() {
			if (getBurnTime() == 0) {
				Level world = furnace.getLevel();
				BlockPos pos = furnace.getBlockPos();
				world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(BlockStateProperties.LIT, true));
			}
			int burnTime = ((AccessorAbstractFurnaceBlockEntity) furnace).getLitTime();
			((AccessorAbstractFurnaceBlockEntity) furnace).setLitTime(burnTime + 200);
		}

		@Override
		public void boostCookTime() {
			int cookTime = ((AccessorAbstractFurnaceBlockEntity) furnace).getCookingProgress();
			((AccessorAbstractFurnaceBlockEntity) furnace).setCookingProgress(Math.min(currentRecipe.getCookingTime() - 1, cookTime + 1));
		}
	}
}
