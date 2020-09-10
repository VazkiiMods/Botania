/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import alexiil.mc.lib.attributes.AttributeProviderItem;
import alexiil.mc.lib.attributes.ItemAttributeList;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidExtractable;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.misc.AbstractItemBasedAttribute;
import alexiil.mc.lib.attributes.misc.LimitedConsumer;
import alexiil.mc.lib.attributes.misc.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ItemWaterBowl extends Item implements AttributeProviderItem {

	public ItemWaterBowl(Settings builder) {
		super(builder);
	}

	@Override
	public void addAllAttributes(Reference<ItemStack> stack, LimitedConsumer<ItemStack> excess, ItemAttributeList<?> to) {
		to.offer(new Handler(stack, excess));
	}

	private static class Handler extends AbstractItemBasedAttribute implements FluidExtractable {

		private Handler(Reference<ItemStack> stack, LimitedConsumer<ItemStack> excess) {
			super(stack, excess);
		}

		@Override
		public FluidVolume attemptExtraction(FluidFilter filter, FluidAmount maxAmount, Simulation simulation) {
			if (!filter.matches(FluidKeys.WATER) || !maxAmount.equals(FluidAmount.BUCKET)) {
				return FluidVolumeUtil.EMPTY;
			}

			ItemStack oldStack = stackRef.get().copy();
			oldStack.decrement(1);

			if (setStacks(simulation, oldStack, new ItemStack(Items.BOWL))) {
				return FluidKeys.WATER.withAmount(FluidAmount.BUCKET);
			} else {
				return FluidVolumeUtil.EMPTY;
			}
		}
	}

}
