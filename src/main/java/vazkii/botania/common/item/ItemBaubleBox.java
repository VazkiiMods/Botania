/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [25/11/2015, 19:46:11 (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibGuiIDs;
import vazkii.botania.common.lib.LibItemNames;

public class ItemBaubleBox extends ItemMod {

	private static final String TAG_ITEMS = "InvItems";
	private static final String TAG_SLOT = "Slot";

	public ItemBaubleBox() {
		setUnlocalizedName(LibItemNames.BAUBLE_BOX);
		setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		player.openGui(Botania.instance, LibGuiIDs.BAUBLE_BOX, world, 0, 0, 0);
		return stack;
	}


	public static ItemStack[] loadStacks(ItemStack stack) {
		NBTTagList var2 = ItemNBTHelper.getList(stack, TAG_ITEMS, 10, false);
		ItemStack[] inventorySlots = new ItemStack[36];
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
