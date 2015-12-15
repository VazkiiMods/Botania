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
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileSparkChanger extends TileSimpleInventory {

	public void doSwap() {
		if(worldObj.isRemote)
			return;

		ItemStack changeStack = getStackInSlot(0);
		List<ISparkAttachable> attachables = new ArrayList();
		for(ForgeDirection dir : LibMisc.CARDINAL_DIRECTIONS) {
			TileEntity tile = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord, zCoord + dir.offsetZ);
			if(tile != null && tile instanceof ISparkAttachable) {
				ISparkAttachable attach = (ISparkAttachable) tile;
				ISparkEntity spark = attach.getAttachedSpark();
				if(spark != null) {
					int upg = spark.getUpgrade();
					int newUpg = changeStack == null ? 0 : changeStack.getItemDamage() + 1;
					if(upg != newUpg)
						attachables.add(attach);
				}
			}
		}

		if(attachables.size() > 0) {
			ISparkAttachable attach = attachables.get(worldObj.rand.nextInt(attachables.size()));
			ISparkEntity spark = attach.getAttachedSpark();
			int upg = spark.getUpgrade();
			ItemStack sparkStack = upg == 0 ? null : new ItemStack(ModItems.sparkUpgrade, 1, upg - 1);
			int newUpg = changeStack == null ? 0 : changeStack.getItemDamage() + 1;
			spark.setUpgrade(newUpg);
			Collection transfers = spark.getTransfers();
			if(transfers != null)
				transfers.clear();
			setInventorySlotContents(0, sparkStack);
			worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
			markDirty();
		}
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return itemstack != null && itemstack.getItem() == ModItems.sparkUpgrade;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

	@Override
	public String getInventoryName() {
		return LibBlockNames.SPARK_CHANGER;
	}

}
