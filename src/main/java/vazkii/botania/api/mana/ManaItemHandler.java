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
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.api.BotaniaAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class ManaItemHandler {

	/**
	 * Gets a list containing all mana-holding items in a player's inventory.
	 * Also includes a call to ManaItemsEvent, so other mods can add items from
	 * their player-associated inventories.
	 * @return The list of items
	 */
	public static List<ItemStack> getManaItems(EntityPlayer player) {
		if (player == null)
			return new ArrayList<ItemStack>();

		IItemHandler mainInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		List<ItemStack> toReturn = new ArrayList<ItemStack>();
		int size = mainInv.getSlots();

		for(int slot = 0; slot < size; slot++) {
			ItemStack stackInSlot = mainInv.getStackInSlot(slot);

			if(!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IManaItem) {
				toReturn.add(stackInSlot);
			}
		}

		ManaItemsEvent event = new ManaItemsEvent(player, toReturn);
		MinecraftForge.EVENT_BUS.post(event);
		toReturn = event.getItems();
		return toReturn;
	}

	/**
	 * Gets a list containing all mana-holding items in a player's baubles inventory.
	 * @return The list of items
	 */
	public static Map<Integer, ItemStack> getManaBaubles(EntityPlayer player) {
		if (player == null)
			return new HashMap<Integer, ItemStack>();

		IItemHandler baublesInv = BotaniaAPI.internalHandler.getBaublesInventoryWrapped(player);
		if (baublesInv == null)
			return new HashMap<Integer, ItemStack>();


		Map<Integer, ItemStack> toReturn = new HashMap<Integer, ItemStack>();
		int size = baublesInv.getSlots();

		for(int slot = 0; slot < size; slot++) {
			ItemStack stackInSlot = baublesInv.getStackInSlot(slot);

			if(!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IManaItem) {
				toReturn.put(slot, stackInSlot);
			}
		}

		return toReturn;
	}

	/**
	 * Requests mana from items in a given player's inventory.
	 * @param manaToGet How much mana is to be requested, if less mana exists than this amount,
	 * the amount of mana existent will be returned instead, if you want exact values use requestManaExact.
	 * @param remove If true, the mana will be removed from the target item. Set to false to just check.
	 * @return The amount of mana received from the request.
	 */
	public static int requestMana(ItemStack stack, EntityPlayer player, int manaToGet, boolean remove) {
		if(stack.isEmpty())
			return 0;

		List<ItemStack> items = getManaItems(player);
		for (ItemStack stackInSlot : items) {
			if(stackInSlot == stack)
				continue;
			IManaItem manaItem = (IManaItem) stackInSlot.getItem();
			if(manaItem.canExportManaToItem(stackInSlot, stack) && manaItem.getMana(stackInSlot) > 0) {
				if(stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canReceiveManaFromItem(stack, stackInSlot))
					continue;

				int mana = Math.min(manaToGet, manaItem.getMana(stackInSlot));

				if(remove)
					manaItem.addMana(stackInSlot, -mana);

				return mana;
			}
		}

		Map<Integer, ItemStack> baubles = getManaBaubles(player);
		for (Entry<Integer, ItemStack> entry : baubles.entrySet()) {
			ItemStack stackInSlot = entry.getValue();
			if(stackInSlot == stack)
				continue;
			IManaItem manaItem = (IManaItem) stackInSlot.getItem();
			if(manaItem.canExportManaToItem(stackInSlot, stack) && manaItem.getMana(stackInSlot) > 0) {
				if(stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canReceiveManaFromItem(stack, stackInSlot))
					continue;

				int mana = Math.min(manaToGet, manaItem.getMana(stackInSlot));

				if(remove)
					manaItem.addMana(stackInSlot, -mana);

				BotaniaAPI.internalHandler.sendBaubleUpdatePacket(player, entry.getKey());

				return mana;
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
		if(stack.isEmpty())
			return false;

		List<ItemStack> items = getManaItems(player);
		for (ItemStack stackInSlot : items) {
			if(stackInSlot == stack)
				continue;
			IManaItem manaItemSlot = (IManaItem) stackInSlot.getItem();
			if(manaItemSlot.canExportManaToItem(stackInSlot, stack) && manaItemSlot.getMana(stackInSlot) > manaToGet) {
				if(stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canReceiveManaFromItem(stack, stackInSlot))
					continue;

				if(remove)
					manaItemSlot.addMana(stackInSlot, -manaToGet);

				return true;
			}
		}

		Map<Integer, ItemStack> baubles = getManaBaubles(player);
		for (Entry<Integer, ItemStack> entry : baubles.entrySet()) {
			ItemStack stackInSlot = entry.getValue();
			if(stackInSlot == stack)
				continue;
			IManaItem manaItemSlot = (IManaItem) stackInSlot.getItem();
			if(manaItemSlot.canExportManaToItem(stackInSlot, stack) && manaItemSlot.getMana(stackInSlot) > manaToGet) {
				if(stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canReceiveManaFromItem(stack, stackInSlot))
					continue;

				if(remove)
					manaItemSlot.addMana(stackInSlot, -manaToGet);
				BotaniaAPI.internalHandler.sendBaubleUpdatePacket(player, entry.getKey());

				return true;
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
		if(stack.isEmpty())
			return 0;

		List<ItemStack> items = getManaItems(player);
		for (ItemStack stackInSlot : items) {
			if(stackInSlot == stack)
				continue;
			IManaItem manaItemSlot = (IManaItem) stackInSlot.getItem();
			if(manaItemSlot.canReceiveManaFromItem(stackInSlot, stack)) {
				if(stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canExportManaToItem(stack, stackInSlot))
					continue;

				int received;
				if(manaItemSlot.getMana(stackInSlot) + manaToSend <= manaItemSlot.getMaxMana(stackInSlot))
					received = manaToSend;
				else received = manaToSend - (manaItemSlot.getMana(stackInSlot) + manaToSend - manaItemSlot.getMaxMana(stackInSlot));


				if(add)
					manaItemSlot.addMana(stackInSlot, manaToSend);

				return received;
			}
		}

		Map<Integer, ItemStack> baubles = getManaBaubles(player);
		for (Entry<Integer, ItemStack> entry : baubles.entrySet()) {
			ItemStack stackInSlot = entry.getValue();
			if(stackInSlot == stack)
				continue;
			IManaItem manaItemSlot = (IManaItem) stackInSlot.getItem();
			if(manaItemSlot.canReceiveManaFromItem(stackInSlot, stack)) {
				if(stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canExportManaToItem(stack, stackInSlot))
					continue;

				int received;
				if(manaItemSlot.getMana(stackInSlot) + manaToSend <= manaItemSlot.getMaxMana(stackInSlot))
					received = manaToSend;
				else received = manaToSend - (manaItemSlot.getMana(stackInSlot) + manaToSend - manaItemSlot.getMaxMana(stackInSlot));


				if(add)
					manaItemSlot.addMana(stackInSlot, manaToSend);
				BotaniaAPI.internalHandler.sendBaubleUpdatePacket(player, entry.getKey());

				return received;
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
		if(stack.isEmpty())
			return false;

		List<ItemStack> items = getManaItems(player);
		for (ItemStack stackInSlot : items) {
			if(stackInSlot == stack)
				continue;
			IManaItem manaItemSlot = (IManaItem) stackInSlot.getItem();
			if(manaItemSlot.getMana(stackInSlot) + manaToSend <= manaItemSlot.getMaxMana(stackInSlot) && manaItemSlot.canReceiveManaFromItem(stackInSlot, stack)) {
				if(stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canExportManaToItem(stack, stackInSlot))
					continue;

				if(add)
					manaItemSlot.addMana(stackInSlot, manaToSend);

				return true;
			}
		}

		Map<Integer, ItemStack> baubles = getManaBaubles(player);
		for (Entry<Integer, ItemStack> entry : baubles.entrySet()) {
			ItemStack stackInSlot = entry.getValue();
			if(stackInSlot == stack)
				continue;
			IManaItem manaItemSlot = (IManaItem) stackInSlot.getItem();
			if(manaItemSlot.getMana(stackInSlot) + manaToSend <= manaItemSlot.getMaxMana(stackInSlot) && manaItemSlot.canReceiveManaFromItem(stackInSlot, stack)) {
				if(stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canExportManaToItem(stack, stackInSlot))
					continue;

				if(add)
					manaItemSlot.addMana(stackInSlot, manaToSend);
				BotaniaAPI.internalHandler.sendBaubleUpdatePacket(player, entry.getKey());

				return true;
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
		float multiplier = Math.max(0F, 1F - getFullDiscountForTools(player, stack));
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
		float multiplier = Math.max(0F, 1F - getFullDiscountForTools(player, stack));
		int cost = (int) (manaToGet * multiplier);
		return requestManaExact(stack, player, cost, remove);
	}

	@Deprecated
	public static float getFullDiscountForTools(EntityPlayer player) {
		return getFullDiscountForTools(player, null);
	}
	
	/**
	 * Gets the sum of all the discounts on IManaDiscountArmor items equipped
	 * on the player passed in. This discount can vary based on what the passed tool is.
	 */
	public static float getFullDiscountForTools(EntityPlayer player, ItemStack tool) {
		float discount = 0F;
		for(int i = 0; i < player.inventory.armorInventory.size(); i++) {
			ItemStack armor = player.inventory.armorInventory.get(i);
			if(!armor.isEmpty() && armor.getItem() instanceof IManaDiscountArmor)
				discount += ((IManaDiscountArmor) armor.getItem()).getDiscount(armor, i, player, tool);
		}

		ManaDiscountEvent event = new ManaDiscountEvent(player, discount, tool);
		MinecraftForge.EVENT_BUS.post(event);
		discount = event.getDiscount();

		return discount;
	}
}
