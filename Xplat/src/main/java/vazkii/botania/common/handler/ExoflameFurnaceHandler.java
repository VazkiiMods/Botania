/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.IExoflameHeatable;
import vazkii.botania.mixin.AccessorAbstractFurnaceBlockEntity;
import vazkii.botania.xplat.IXplatAbstractions;

public class ExoflameFurnaceHandler {
	public static boolean canSmeltRecipe(AbstractFurnaceBlockEntity furnace, Recipe<?> recipe) {
		var items = ((AccessorAbstractFurnaceBlockEntity) furnace).getItems();
		return IXplatAbstractions.INSTANCE.canFurnaceBurn(furnace, recipe, items, furnace.getMaxStackSize());
	}

	public static boolean canSmelt(AbstractFurnaceBlockEntity furnace) {
		if (furnace.getItem(0).isEmpty()) {
			return false;
		}
		try {
			var qc = ((AccessorAbstractFurnaceBlockEntity) furnace).getQuickCheck();
			var currentRecipe = qc.getRecipeFor(furnace, furnace.getLevel());
			return currentRecipe.isPresent() && ExoflameFurnaceHandler.canSmeltRecipe(furnace, currentRecipe.get());
		} catch (Throwable t) {
			BotaniaAPI.LOGGER.error("Failed to determine if furnace TE can smelt", t);
			return false;
		}
	}

	public static class FurnaceExoflameHeatable implements IExoflameHeatable {
		private final AbstractFurnaceBlockEntity furnace;

		public FurnaceExoflameHeatable(AbstractFurnaceBlockEntity furnace) {
			this.furnace = furnace;
		}

		@Override
		public boolean canSmelt() {
			return ExoflameFurnaceHandler.canSmelt(furnace);
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
			int cookTimeTotal = ((AccessorAbstractFurnaceBlockEntity) furnace).getCookingTotalTime();
			((AccessorAbstractFurnaceBlockEntity) furnace).setCookingProgress(Math.min(cookTimeTotal - 1, cookTime + 1));
		}
	}
}
