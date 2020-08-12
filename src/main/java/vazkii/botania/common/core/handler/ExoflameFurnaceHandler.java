/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.item.IExoflameHeatable;
import vazkii.botania.common.Botania;
import vazkii.botania.common.capability.SimpleCapProvider;
import vazkii.botania.mixin.AccessorAbstractFurnaceTileEntity;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import Capability;

public class ExoflameFurnaceHandler {

	@CapabilityInject(IExoflameHeatable.class)
	public static Capability<IExoflameHeatable> CAPABILITY;
	public static final Identifier ID = prefix("exoflame_heatable");

	public static void attachFurnaceCapability(AttachCapabilitiesEvent<BlockEntity> event) {
		BlockEntity te = event.getObject();
		if (te instanceof AbstractFurnaceBlockEntity) {
			AbstractFurnaceBlockEntity furnace = (AbstractFurnaceBlockEntity) te;
			SimpleCapProvider.attach(event, ID, CAPABILITY, new FurnaceExoflameHeatable(furnace));
		}
	}

	public static boolean canSmelt(AbstractFurnaceBlockEntity furnace, Recipe<?> recipe) {
		return ((AccessorAbstractFurnaceTileEntity) furnace).invokeCanAcceptRecipeOutput(recipe);
	}

	public static RecipeType<? extends AbstractCookingRecipe> getRecipeType(AbstractFurnaceBlockEntity furnace) {
		return ((AccessorAbstractFurnaceTileEntity) furnace).getRecipeType();
	}

	private static class FurnaceExoflameHeatable implements IExoflameHeatable {
		private final AbstractFurnaceBlockEntity furnace;

		private RecipeType<? extends AbstractCookingRecipe> recipeType;
		private AbstractCookingRecipe currentRecipe;

		FurnaceExoflameHeatable(AbstractFurnaceBlockEntity furnace) {
			this.furnace = furnace;
		}

		@Override
		public boolean canSmelt() {
			if (furnace.getStack(0).isEmpty()) {
				return false;
			}
			try {
				if (recipeType == null) {
					this.recipeType = ExoflameFurnaceHandler.getRecipeType(furnace);
				}
				if (currentRecipe != null) { // This is already more caching than Mojang does
					if (currentRecipe.matches(furnace, furnace.getWorld())
							&& ExoflameFurnaceHandler.canSmelt(furnace, currentRecipe)) {
						return true;
					}
				}
				currentRecipe = furnace.getWorld().getRecipeManager().getFirstMatch(recipeType, furnace, furnace.getWorld()).orElse(null);
				return ExoflameFurnaceHandler.canSmelt(furnace, currentRecipe);
			} catch (Throwable t) {
				Botania.LOGGER.error("Failed to determine if furnace TE can smelt", t);
				return false;
			}
		}

		@Override
		public int getBurnTime() {
			return ((AccessorAbstractFurnaceTileEntity) furnace).getBurnTime();
		}

		@Override
		public void boostBurnTime() {
			if (getBurnTime() == 0) {
				World world = furnace.getWorld();
				BlockPos pos = furnace.getPos();
				world.setBlockState(pos, world.getBlockState(pos).with(Properties.LIT, true));
			}
			int burnTime = ((AccessorAbstractFurnaceTileEntity) furnace).getBurnTime();
			((AccessorAbstractFurnaceTileEntity) furnace).setBurnTime(burnTime + 200);
		}

		@Override
		public void boostCookTime() {
			int cookTime = ((AccessorAbstractFurnaceTileEntity) furnace).getCookTime();
			((AccessorAbstractFurnaceTileEntity) furnace).setCookTime(Math.min(currentRecipe.getCookTime() - 1, cookTime + 1));
		}
	}
}
