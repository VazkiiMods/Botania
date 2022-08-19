/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 13, 2014, 5:32:24 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;

public final class ManaItemHandler {

	/**
	 * Requests mana from items in a given player's inventory.
	 * @param manaToGet How much mana is to be requested, if less mana exists than this amount,
	 * the amount of mana existent will be returned instead, if you want exact values use requestManaExact.
	 * @param remove If true, the mana will be removed from the target item. Set to false to just check.
	 * @return The amount of mana received from the request.
	 */
	public static int requestMana(ItemStack stack, EntityPlayer player, int manaToGet, boolean remove) {
		if(stack == null)
			return 0;

		IInventory mainInv = player.inventory;
		IInventory baublesInv = BotaniaAPI.internalHandler.getBaublesInventory(player);

		int invSize = mainInv.getSizeInventory();
		int size = invSize;
		if(baublesInv != null)
			size += baublesInv.getSizeInventory();

		for(int i = 0; i < size; i++) {
			boolean useBaubles = i >= invSize;
			IInventory inv = useBaubles ? baublesInv : mainInv;
			int slot = i - (useBaubles ? invSize : 0);
			ItemStack stackInSlot = inv.getStackInSlot(slot);
			if(stackInSlot == stack)
				continue;

			if(stackInSlot != null && stackInSlot.getItem() instanceof IManaItem) {
				IManaItem manaItem = (IManaItem) stackInSlot.getItem();
				if(manaItem.canExportManaToItem(stackInSlot, stack) && manaItem.getMana(stackInSlot) > 0) {
					if(stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canReceiveManaFromItem(stack, stackInSlot))
						continue;

					int mana = Math.min(manaToGet, manaItem.getMana(stackInSlot));

					if(remove)
						manaItem.addMana(stackInSlot, -mana);
					if(useBaubles)
						BotaniaAPI.internalHandler.sendBaubleUpdatePacket(player, slot);

					return mana;
				}
			}
		}

		return 0;
	}

	/**
	 * Requests an exact amount of mana from items in a given player's inventory.
	 * @param manaToGet How much mana is to be requested, if less mana exists than this amount,
	 * false will be returned instead, and nothing will happen.
	 * @param remove If true, the mana will be removed from the target item. Set to false to just check.
	 * @return If the request was succesful.
	 */
	public static boolean requestManaExact(ItemStack stack, EntityPlayer player, int manaToGet, boolean remove) {
		if(stack == null)
			return false;

		IInventory mainInv = player.inventory;
		IInventory baublesInv = BotaniaAPI.internalHandler.getBaublesInventory(player);

		int invSize = mainInv.getSizeInventory();
		int size = invSize;
		if(baublesInv != null)
			size += baublesInv.getSizeInventory();

		for(int i = 0; i < size; i++) {
			boolean useBaubles = i >= invSize;
			IInventory inv = useBaubles ? baublesInv : mainInv;
			int slot = i - (useBaubles ? invSize : 0);
			ItemStack stackInSlot = inv.getStackInSlot(slot);
			if(stackInSlot == stack)
				continue;

			if(stackInSlot != null && stackInSlot.getItem() instanceof IManaItem) {
				IManaItem manaItemSlot = (IManaItem) stackInSlot.getItem();
				if(manaItemSlot.canExportManaToItem(stackInSlot, stack) && manaItemSlot.getMana(stackInSlot) > manaToGet) {
					if(stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canReceiveManaFromItem(stack, stackInSlot))
						continue;

					if(remove)
						manaItemSlot.addMana(stackInSlot, -manaToGet);
					if(useBaubles)
						BotaniaAPI.internalHandler.sendBaubleUpdatePacket(player, slot);

					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Dispatches mana to items in a given player's inventory. Note that this method
	 * does not automatically remove mana from the item which is exporting.
	 * @param manaToSend How much mana is to be sent.
	 * @param remove If true, the mana will be added from the target item. Set to false to just check.
	 * @return The amount of mana actually sent.
	 */
	public static int dispatchMana(ItemStack stack, EntityPlayer player, int manaToSend, boolean add) {
		if(stack == null)
			return 0;

		IInventory mainInv = player.inventory;
		IInventory baublesInv = BotaniaAPI.internalHandler.getBaublesInventory(player);

		int invSize = mainInv.getSizeInventory();
		int size = invSize;
		if(baublesInv != null)
			size += baublesInv.getSizeInventory();

		for(int i = 0; i < size; i++) {
			boolean useBaubles = i >= invSize;
			IInventory inv = useBaubles ? baublesInv : mainInv;
			int slot = i - (useBaubles ? invSize : 0);
			ItemStack stackInSlot = inv.getStackInSlot(slot);
			if(stackInSlot == stack)
				continue;

			if(stackInSlot != null && stackInSlot.getItem() instanceof IManaItem) {
				IManaItem manaItemSlot = (IManaItem) stackInSlot.getItem();

				if(manaItemSlot.canReceiveManaFromItem(stackInSlot, stack)) {
					if(stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canExportManaToItem(stack, stackInSlot))
						continue;

					int received = 0;
					if(manaItemSlot.getMana(stackInSlot) + manaToSend <= manaItemSlot.getMaxMana(stackInSlot))
						received = manaToSend;
					else received = manaToSend - (manaItemSlot.getMana(stackInSlot) + manaToSend - manaItemSlot.getMaxMana(stackInSlot));


					if(add)
						manaItemSlot.addMana(stackInSlot, manaToSend);
					if(useBaubles)
						BotaniaAPI.internalHandler.sendBaubleUpdatePacket(player, slot);

					return received;
				}
			}
		}

		return 0;
	}

	/**
	 * Dispatches an exact amount of mana to items in a given player's inventory. Note that this method
	 * does not automatically remove mana from the item which is exporting.
	 * @param manaToSend How much mana is to be sent.
	 * @param remove If true, the mana will be added from the target item. Set to false to just check.
	 * @return If an item received the mana sent.
	 */
	public static boolean dispatchManaExact(ItemStack stack, EntityPlayer player, int manaToSend, boolean add) {
		if(stack == null)
			return false;

		IInventory mainInv = player.inventory;
		IInventory baublesInv = BotaniaAPI.internalHandler.getBaublesInventory(player);

		int invSize = mainInv.getSizeInventory();
		int size = invSize;
		if(baublesInv != null)
			size += baublesInv.getSizeInventory();

		for(int i = 0; i < size; i++) {
			boolean useBaubles = i >= invSize;
			IInventory inv = useBaubles ? baublesInv : mainInv;
			int slot = i - (useBaubles ? invSize : 0);
			ItemStack stackInSlot = inv.getStackInSlot(slot);
			if(stackInSlot == stack)
				continue;

			if(stackInSlot != null && stackInSlot.getItem() instanceof IManaItem) {
				IManaItem manaItemSlot = (IManaItem) stackInSlot.getItem();
				if(manaItemSlot.getMana(stackInSlot) + manaToSend <= manaItemSlot.getMaxMana(stackInSlot) && manaItemSlot.canReceiveManaFromItem(stackInSlot, stack)) {
					if(stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canExportManaToItem(stack, stackInSlot))
						continue;

					if(add)
						manaItemSlot.addMana(stackInSlot, manaToSend);
					if(useBaubles)
						BotaniaAPI.internalHandler.sendBaubleUpdatePacket(player, slot);

					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Requests mana from items in a given player's inventory. This version also
	 * checks for IManaDiscountArmor items equipped to lower the cost.
	 * @param manaToGet How much mana is to be requested, if less mana exists than this amount,
	 * the amount of mana existent will be returned instead, if you want exact values use requestManaExact.
	 * @param remove If true, the mana will be removed from the target item. Set to false to just check.
	 * @return The amount of mana received from the request.
	 */
	public static int requestManaForTool(ItemStack stack, EntityPlayer player, int manaToGet, boolean remove) {
		float multiplier = Math.max(0F, 1F - getFullDiscountForTools(player));
		int cost = (int) (manaToGet * multiplier);
		return (int) (requestMana(stack, player, cost, remove) / multiplier);
	}

	/**
	 * Requests an exact amount of mana from items in a given player's inventory. This version also
	 * checks for IManaDiscountArmor items equipped to lower the cost.
	 * @param manaToGet How much mana is to be requested, if less mana exists than this amount,
	 * false will be returned instead, and nothing will happen.
	 * @param remove If true, the mana will be removed from the target item. Set to false to just check.
	 * @return If the request was succesful.
	 */
	public static boolean requestManaExactForTool(ItemStack stack, EntityPlayer player, int manaToGet, boolean remove) {
		float multiplier = Math.max(0F, 1F - getFullDiscountForTools(player));
		int cost = (int) (manaToGet * multiplier);
		return requestManaExact(stack, player, cost, remove);
	}

	/**
	 * Gets the sum of all the discounts on IManaDiscountArmor items equipped
	 * on the player passed in.
	 */
	public static float getFullDiscountForTools(EntityPlayer player) {
		float discount = 0F;
		for(int i = 0; i < player.inventory.armorInventory.length; i++) {
			ItemStack armor = player.inventory.armorInventory[i];
			if(armor != null && armor.getItem() instanceof IManaDiscountArmor)
				discount += ((IManaDiscountArmor) armor.getItem()).getDiscount(armor, i, player);
		}

		return discount;
	}
}
