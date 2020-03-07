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

import com.google.common.collect.Iterables;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.api.BotaniaAPI;

import java.util.ArrayList;
import java.util.Collections;
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
	public static List<ItemStack> getManaItems(PlayerEntity player) {
		if (player == null)
			return Collections.emptyList();

		List<ItemStack> toReturn = new ArrayList<>();
		player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(mainInv -> {
			int size = mainInv.getSlots();

			for(int slot = 0; slot < size; slot++) {
				ItemStack stackInSlot = mainInv.getStackInSlot(slot);

				if(!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IManaItem) {
					toReturn.add(stackInSlot);
				}
			}
		});

		ManaItemsEvent event = new ManaItemsEvent(player, toReturn);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getItems();
	}

	/**
	 * Gets a list containing all mana-holding items in a player's accessories inventory.
	 * @return The list of items
	 */
	public static List<ItemStack> getManaAccesories(PlayerEntity player) {
		if (player == null)
			return Collections.emptyList();

		IItemHandler acc = BotaniaAPI.internalHandler.getAccessoriesInventory(player);
		if (acc == null)
			return Collections.emptyList();


		List<ItemStack> toReturn = new ArrayList<>(acc.getSlots());

		for(int slot = 0; slot < acc.getSlots(); slot++) {
			ItemStack stackInSlot = acc.getStackInSlot(slot);

			if(!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IManaItem) {
				toReturn.add(stackInSlot);
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
	public static int requestMana(ItemStack stack, PlayerEntity player, int manaToGet, boolean remove) {
		if(stack.isEmpty())
			return 0;

		List<ItemStack> items = getManaItems(player);
		List<ItemStack> acc = getManaAccesories(player);
		for (ItemStack stackInSlot : Iterables.concat(items, acc)) {
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

		return 0;
	}

	/**
	 * Requests an exact amount of mana from items in a given player's inventory.
	 * @param manaToGet How much mana is to be requested, if less mana exists than this amount,
	 * false will be returned instead, and nothing will happen.
	 * @param remove If true, the mana will be removed from the target item. Set to false to just check.
	 * @return If the request was succesful.
	 */
	public static boolean requestManaExact(ItemStack stack, PlayerEntity player, int manaToGet, boolean remove) {
		if(stack.isEmpty())
			return false;

		List<ItemStack> items = getManaItems(player);
		List<ItemStack> acc = getManaAccesories(player);
		for (ItemStack stackInSlot : Iterables.concat(items, acc)) {
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

		return false;
	}

	/**
	 * Dispatches mana to items in a given player's inventory. Note that this method
	 * does not automatically remove mana from the item which is exporting.
	 * @param manaToSend How much mana is to be sent.
	 * @param add If true, the mana will be added from the target item. Set to false to just check.
	 * @return The amount of mana actually sent.
	 */
	public static int dispatchMana(ItemStack stack, PlayerEntity player, int manaToSend, boolean add) {
		if(stack.isEmpty())
			return 0;

		List<ItemStack> items = getManaItems(player);
		List<ItemStack> acc = getManaAccesories(player);
		for (ItemStack stackInSlot : Iterables.concat(items, acc)) {
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

		return 0;
	}

	/**
	 * Dispatches an exact amount of mana to items in a given player's inventory. Note that this method
	 * does not automatically remove mana from the item which is exporting.
	 * @param manaToSend How much mana is to be sent.
	 * @param add If true, the mana will be added from the target item. Set to false to just check.
	 * @return If an item received the mana sent.
	 */
	public static boolean dispatchManaExact(ItemStack stack, PlayerEntity player, int manaToSend, boolean add) {
		if(stack.isEmpty())
			return false;

		List<ItemStack> items = getManaItems(player);
		List<ItemStack> acc = getManaAccesories(player);
		for (ItemStack stackInSlot : Iterables.concat(items, acc)) {
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
	public static int requestManaForTool(ItemStack stack, PlayerEntity player, int manaToGet, boolean remove) {
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
	public static boolean requestManaExactForTool(ItemStack stack, PlayerEntity player, int manaToGet, boolean remove) {
		float multiplier = Math.max(0F, 1F - getFullDiscountForTools(player, stack));
		int cost = (int) (manaToGet * multiplier);
		return requestManaExact(stack, player, cost, remove);
	}
	
	/**
	 * Gets the sum of all the discounts on IManaDiscountArmor items equipped
	 * on the player passed in. This discount can vary based on what the passed tool is.
	 */
	public static float getFullDiscountForTools(PlayerEntity player, ItemStack tool) {
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
