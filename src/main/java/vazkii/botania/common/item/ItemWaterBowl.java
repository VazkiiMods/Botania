/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 8, 2015, 9:07:35 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemWaterBowl extends ItemMod  {

	public ItemWaterBowl() {
		super(LibItemNames.WATER_BOWL);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new Handler(stack);
	}

	private static class Handler extends FluidHandlerItemStackSimple.SwapEmpty {

		private Handler(ItemStack stack) {
			super(stack, new ItemStack(Items.BOWL), Fluid.BUCKET_VOLUME);
			setFluid(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME));
		}

		@Override
		public boolean canFillFluidType(FluidStack fluid) {
			return false;
		}

		@Override
		public boolean canDrainFluidType(FluidStack fluid) {
			return fluid.getFluid() == FluidRegistry.WATER;
		}
	}

}
