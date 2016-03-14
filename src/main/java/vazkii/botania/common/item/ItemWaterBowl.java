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

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import vazkii.botania.common.lib.LibItemNames;

public class ItemWaterBowl extends ItemMod implements IFluidContainerItem {

	private static final FluidStack STACK = new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);

	public ItemWaterBowl() {
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.WATER_BOWL);
	}

	// Needed for rendering water dynamic model

	@Override
	public FluidStack getFluid(ItemStack container) {
		return STACK;
	}

	@Override
	public int getCapacity(ItemStack container) {
		return 0;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
		return null;
	}
}
