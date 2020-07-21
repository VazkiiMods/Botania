/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;

public class ItemWaterBowl extends Item {

	public ItemWaterBowl(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return new Handler(stack);
	}

	private static class Handler extends FluidHandlerItemStackSimple.SwapEmpty {

		private Handler(ItemStack stack) {
			super(stack, new ItemStack(Items.BOWL), FluidAttributes.BUCKET_VOLUME);
			setFluid(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME));
		}

		@Override
		public boolean canFillFluidType(FluidStack fluid) {
			return false;
		}

		@Override
		public boolean canDrainFluidType(FluidStack fluid) {
			return fluid.getFluid() == Fluids.WATER;
		}
	}

}
