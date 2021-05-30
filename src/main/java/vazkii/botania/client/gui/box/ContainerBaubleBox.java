/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [25/11/2015, 19:59:06 (GMT)]
 */
package vazkii.botania.client.gui.box;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.container.SlotBauble;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.client.gui.SlotLocked;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public class ContainerBaubleBox extends Container {

	public static final Set<ResourceLocation> RODS = ImmutableSet.of(
			ModItems.dirtRod.getRegistryName(),
			ModItems.terraformRod.getRegistryName(),
			ModItems.waterRod.getRegistryName(),
			ModItems.rainbowRod.getRegistryName(),
			ModItems.tornadoRod.getRegistryName(),
			ModItems.fireRod.getRegistryName(),
			ModItems.skyDirtRod.getRegistryName(),
			ModItems.gravityRod.getRegistryName(),
			ModItems.missileRod.getRegistryName(),
			ModItems.cobbleRod.getRegistryName(),
			ModItems.smeltRod.getRegistryName(),
			ModItems.exchangeRod.getRegistryName(),
			new ResourceLocation("incorporeal", "fractured_space_rod")
	);

	private final InventoryBaubleBox baubleBoxInv;
	public IBaublesItemHandler baubles;

	public ContainerBaubleBox(EntityPlayer player, InventoryBaubleBox boxInv) {
		int i;
		int j;

		IInventory playerInv = player.inventory;
		baubleBoxInv = boxInv;

        baubles = BaublesApi.getBaublesHandler(player);

		addSlotToContainer(new SlotBauble(player, baubles, 0, 8, 8 + 0 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 1, 8, 8 + 1 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 2, 8, 8 + 2 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 3, 8, 8 + 3 * 18));

		addSlotToContainer(new SlotBauble(player, baubles, 4, 27, 8 + 0 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 5, 27, 8 + 1 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 6, 27, 8 + 2 * 18));

		for(i = 0; i < 4; ++i)
			for(j = 0; j < 6; ++j) {
				int k = j + i * 6;
				addSlotToContainer(new SlotItemHandler(baubleBoxInv, k, 62 + j * 18, 8 + i * 18));
			}

		for(i = 0; i < 3; ++i)
			for(j = 0; j < 9; ++j)
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for(i = 0; i < 9; ++i) {
			if(playerInv.getStackInSlot(i) == baubleBoxInv.box)
				addSlotToContainer(new SlotLocked(playerInv, i, 8 + i * 18, 142));
			else addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return player.getHeldItemMainhand() == baubleBoxInv.box
				|| player.getHeldItemOffhand() == baubleBoxInv.box;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void setAll(List<ItemStack> l) {
    	baubles.setEventBlock(true);
		super.setAll(l);
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(slotIndex);

		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			Item item = itemstack1.getItem();

			int boxStart = 7;
			int boxEnd = boxStart + 24;
			int invEnd = boxEnd + 36;
			
			if(slotIndex < boxEnd) {
				if(!mergeItemStack(itemstack1, boxEnd, invEnd, true))
					return ItemStack.EMPTY;
			} else {
				if(!itemstack1.isEmpty()
						&& (item instanceof IBauble || item instanceof IManaItem || RODS.contains(item.getRegistryName()))
						&& !mergeItemStack(itemstack1, boxStart, boxEnd, false))
					return ItemStack.EMPTY;
			}

			if(itemstack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else slot.onSlotChanged();

			if(itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

}
