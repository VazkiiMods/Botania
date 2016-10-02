/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 28, 2015, 10:02:11 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.common.item.ModItems;

public class TileSparkChanger extends TileSimpleInventory {

	public void doSwap() {
		if(worldObj.isRemote)
			return;

		ItemStack changeStack = itemHandler.getStackInSlot(0);
		List<ISparkAttachable> attachables = new ArrayList<>();
		for(EnumFacing dir : EnumFacing.HORIZONTALS) {
			TileEntity tile = worldObj.getTileEntity(pos.offset(dir));
			if(tile != null && tile instanceof ISparkAttachable) {
				ISparkAttachable attach = (ISparkAttachable) tile;
				ISparkEntity spark = attach.getAttachedSpark();
				if(spark != null) {
					SparkUpgradeType upg = spark.getUpgrade();
					SparkUpgradeType newUpg = changeStack == null ? SparkUpgradeType.NONE : SparkUpgradeType.values()[changeStack.getItemDamage() + 1];
					if(upg != newUpg)
						attachables.add(attach);
				}
			}
		}

		if(attachables.size() > 0) {
			ISparkAttachable attach = attachables.get(worldObj.rand.nextInt(attachables.size()));
			ISparkEntity spark = attach.getAttachedSpark();
			SparkUpgradeType upg = spark.getUpgrade();
			ItemStack sparkStack = upg == SparkUpgradeType.NONE ? null : new ItemStack(ModItems.sparkUpgrade, 1, upg.ordinal() - 1);
			SparkUpgradeType newUpg = changeStack == null ? SparkUpgradeType.NONE : SparkUpgradeType.values()[changeStack.getItemDamage() + 1];
			spark.setUpgrade(newUpg);
			Collection transfers = spark.getTransfers();
			if(transfers != null)
				transfers.clear();
			itemHandler.setStackInSlot(0, sparkStack);
			worldObj.updateComparatorOutputLevel(pos, worldObj.getBlockState(pos).getBlock());
			markDirty();
		}
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, true) {
			@Override
			protected int getStackLimit(int slot, ItemStack stack) {
				return 1;
			}

			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				if(stack != null && stack.getItem() == ModItems.sparkUpgrade)
					return super.insertItem(slot, stack, simulate);
				else return stack;
			}
		};
	}

	@Override
	public void markDirty() {
		super.markDirty();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

}
