/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.internal_caps;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.VoidFluidHandler;

public class ExtrapolatedBucketFluidHandler extends VoidFluidHandler implements IFluidHandlerItem {
	private final ItemStack container;

	public ExtrapolatedBucketFluidHandler(ItemStack container) {
		this.container = container;
	}

	@Override
	public ItemStack getContainer() {
		return container;
	}

	/**
	 * Drain things one bucket worth of fluid at a time.
	 */
	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return Math.min(FluidType.BUCKET_VOLUME, resource.getAmount());
	}
}
