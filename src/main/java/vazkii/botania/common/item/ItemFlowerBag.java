/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 16, 2015, 6:43:33 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibGuiIDs;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ItemFlowerBag extends ItemMod {

	private static final String TAG_ITEMS = "InvItems";
	private static final String TAG_SLOT = "Slot";

	public ItemFlowerBag() {
		setUnlocalizedName(LibItemNames.FLOWER_BAG);
		setMaxStackSize(1);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onPickupItem(EntityItemPickupEvent event) {
		ItemStack stack = event.item.getEntityItem();
		if(stack.getItem() == Item.getItemFromBlock(ModBlocks.flower) && stack.stackSize > 0) {
			int color = stack.getItemDamage();
			if(color > 15)
				return;

			for(int i = 0; i < event.entityPlayer.inventory.getSizeInventory(); i++) {
				if(i == event.entityPlayer.inventory.currentItem)
					continue; // prevent item deletion

				ItemStack invStack = event.entityPlayer.inventory.getStackInSlot(i);
				if(invStack != null && invStack.getItem() == this) {
					ItemStack[] bagInv = loadStacks(invStack);
					ItemStack stackAt = bagInv[color];
					boolean didChange = false;
					if(stackAt == null) {
						bagInv[color] = stack.copy();
						stack.stackSize = 0;
						didChange = true;
					} else {
						int stackAtSize = stackAt.stackSize;
						int stackSize = stack.stackSize;
						int spare = 64 - stackAtSize;
						int pass = Math.min(spare, stackSize);
						if(pass > 0) {
							stackAt.stackSize += pass;
							stack.stackSize -= pass;
							didChange = true;
						}
					}

					if(didChange)
						setStacks(invStack, bagInv);
				}

				if(stack.stackSize == 0)
					return;
			}
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		player.openGui(Botania.instance, LibGuiIDs.FLOWER_BAG, world, 0, 0, 0);
		return stack;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int s, float xs, float ys, float zs) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null && tile instanceof IInventory) {
			if(!world.isRemote) {
				ForgeDirection side = ForgeDirection.getOrientation(s);
				IInventory inv = (IInventory) tile;
				ItemStack[] stacks = loadStacks(stack);
				ItemStack[] newStacks = new ItemStack[stacks.length];
				boolean putAny = false;

				int i = 0;
				for(ItemStack petal : stacks) {
					if(petal != null) {
						int count = InventoryHelper.testInventoryInsertion(inv, petal, side);
						InventoryHelper.insertItemIntoInventory(inv, petal, side, -1);

						ItemStack newPetal = petal.copy();
						if(newPetal.stackSize == 0)
							newPetal = null;

						newStacks[i] = newPetal;
						putAny |= count > 0;
					}

					i++;
				}

				setStacks(stack, newStacks);
				if(putAny && inv instanceof TileEntityChest) {
					inv = InventoryHelper.getInventory(inv);
					player.displayGUIChest(inv);
				}
			}

			return true;
		}
		return false;
	}

	public static ItemStack[] loadStacks(ItemStack stack) {
		NBTTagList var2 = ItemNBTHelper.getList(stack, TAG_ITEMS, 10, false);
		ItemStack[] inventorySlots = new ItemStack[16];
		for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
			NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			byte var5 = var4.getByte(TAG_SLOT);
			if(var5 >= 0 && var5 < inventorySlots.length)
				inventorySlots[var5] = ItemStack.loadItemStackFromNBT(var4);
		}

		return inventorySlots;
	}

	public static void setStacks(ItemStack stack, ItemStack[] inventorySlots) {
		NBTTagList var2 = new NBTTagList();
		for(int var3 = 0; var3 < inventorySlots.length; ++var3)
			if(inventorySlots[var3] != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte(TAG_SLOT, (byte)var3);
				inventorySlots[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}

		ItemNBTHelper.setList(stack, TAG_ITEMS, var2);
	}

}
