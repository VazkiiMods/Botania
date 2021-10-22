/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl.mana;

import com.google.common.collect.Iterables;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.*;

import java.util.*;

public class ManaItemHandlerImpl implements ManaItemHandler {
	@Override
	public List<ItemStack> getManaItems(PlayerEntity player) {
		if (player == null) {
			return Collections.emptyList();
		}

		List<ItemStack> toReturn = new ArrayList<>();

		for (ItemStack stackInSlot : Iterables.concat(player.inventory.mainInventory, player.inventory.offHandInventory)) {
			if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IManaItem) {
				toReturn.add(stackInSlot);
			}
		}

		ManaItemsEvent event = new ManaItemsEvent(player, toReturn);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getItems();
	}

	@Override
	public List<ItemStack> getManaAccesories(PlayerEntity player) {
		if (player == null) {
			return Collections.emptyList();
		}

		IInventory acc = BotaniaAPI.instance().getAccessoriesInventory(player);

		List<ItemStack> toReturn = new ArrayList<>(acc.getSizeInventory());

		for (int slot = 0; slot < acc.getSizeInventory(); slot++) {
			ItemStack stackInSlot = acc.getStackInSlot(slot);

			if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IManaItem) {
				toReturn.add(stackInSlot);
			}
		}

		return toReturn;
	}

	@Override
	public int requestMana(ItemStack stack, PlayerEntity player, int manaToGet, boolean remove) {
		if (stack.isEmpty()) {
			return 0;
		}

		List<ItemStack> items = getManaItems(player);
		List<ItemStack> acc = getManaAccesories(player);
		int manaReceived = 0;
		for (ItemStack stackInSlot : Iterables.concat(items, acc)) {
			if (stackInSlot == stack) {
				continue;
			}
			IManaItem manaItem = (IManaItem) stackInSlot.getItem();
			if (manaItem.canExportManaToItem(stackInSlot, stack) && manaItem.getMana(stackInSlot) > 0) {
				if (stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canReceiveManaFromItem(stack, stackInSlot)) {
					continue;
				}

				int mana = Math.min(manaToGet - manaReceived, manaItem.getMana(stackInSlot));

				if (remove) {
					manaItem.addMana(stackInSlot, -mana);
				}

				manaReceived += mana;

				if (manaReceived >= manaToGet) {
					break;
				}
			}
		}

		return manaReceived;
	}

	@Override
	public boolean requestManaExact(ItemStack stack, PlayerEntity player, int manaToGet, boolean remove) {
		if (stack.isEmpty()) {
			return false;
		}

		List<ItemStack> items = getManaItems(player);
		List<ItemStack> acc = getManaAccesories(player);
		int manaReceived = 0;
		Map<ItemStack, Integer> manaToRemove = new HashMap<>();
		for (ItemStack stackInSlot : Iterables.concat(items, acc)) {
			if (stackInSlot == stack) {
				continue;
			}
			IManaItem manaItemSlot = (IManaItem) stackInSlot.getItem();
			if (manaItemSlot.canExportManaToItem(stackInSlot, stack)) {
				if (stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canReceiveManaFromItem(stack, stackInSlot)) {
					continue;
				}

				int mana = Math.min(manaToGet - manaReceived, manaItemSlot.getMana(stackInSlot));

				if (remove) {
					manaToRemove.put(stackInSlot, mana);
				}

				manaReceived += mana;

				if (manaReceived >= manaToGet) {
					break;
				}
			}
		}

		if (manaReceived == manaToGet) {
			for (Map.Entry<ItemStack, Integer> entry : manaToRemove.entrySet()) {
				((IManaItem) entry.getKey().getItem()).addMana(entry.getKey(), entry.getValue());
			}
			return true;
		}

		return false;
	}

	@Override
	public int dispatchMana(ItemStack stack, PlayerEntity player, int manaToSend, boolean add) {
		if (stack.isEmpty()) {
			return 0;
		}

		List<ItemStack> items = getManaItems(player);
		List<ItemStack> acc = getManaAccesories(player);
		for (ItemStack stackInSlot : Iterables.concat(items, acc)) {
			if (stackInSlot == stack) {
				continue;
			}
			IManaItem manaItemSlot = (IManaItem) stackInSlot.getItem();
			if (manaItemSlot.canReceiveManaFromItem(stackInSlot, stack)) {
				if (stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canExportManaToItem(stack, stackInSlot)) {
					continue;
				}

				int received;
				if (manaItemSlot.getMana(stackInSlot) + manaToSend <= manaItemSlot.getMaxMana(stackInSlot)) {
					received = manaToSend;
				} else {
					received = manaToSend - (manaItemSlot.getMana(stackInSlot) + manaToSend - manaItemSlot.getMaxMana(stackInSlot));
				}

				if (add) {
					manaItemSlot.addMana(stackInSlot, manaToSend);
				}

				return received;
			}
		}

		return 0;
	}

	@Override
	public boolean dispatchManaExact(ItemStack stack, PlayerEntity player, int manaToSend, boolean add) {
		if (stack.isEmpty()) {
			return false;
		}

		List<ItemStack> items = getManaItems(player);
		List<ItemStack> acc = getManaAccesories(player);
		for (ItemStack stackInSlot : Iterables.concat(items, acc)) {
			if (stackInSlot == stack) {
				continue;
			}
			IManaItem manaItemSlot = (IManaItem) stackInSlot.getItem();
			if (manaItemSlot.getMana(stackInSlot) + manaToSend <= manaItemSlot.getMaxMana(stackInSlot) && manaItemSlot.canReceiveManaFromItem(stackInSlot, stack)) {
				if (stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canExportManaToItem(stack, stackInSlot)) {
					continue;
				}

				if (add) {
					manaItemSlot.addMana(stackInSlot, manaToSend);
				}

				return true;
			}
		}

		return false;
	}

	private int discountManaForTool(ItemStack stack, PlayerEntity player, int inCost) {
		float multiplier = Math.max(0F, 1F - getFullDiscountForTools(player, stack));
		return (int) (inCost * multiplier);
	}

	@Override
	public int requestManaForTool(ItemStack stack, PlayerEntity player, int manaToGet, boolean remove) {
		int cost = discountManaForTool(stack, player, manaToGet);
		return requestMana(stack, player, cost, remove);
	}

	@Override
	public boolean requestManaExactForTool(ItemStack stack, PlayerEntity player, int manaToGet, boolean remove) {
		int cost = discountManaForTool(stack, player, manaToGet);
		return requestManaExact(stack, player, cost, remove);
	}

	@Override
	public int getInvocationCountForTool(ItemStack stack, PlayerEntity player, int manaToGet) {
		if (stack.isEmpty()) {
			return 0;
		}

		int cost = discountManaForTool(stack, player, manaToGet);
		int invocations = 0;

		List<ItemStack> items = getManaItems(player);
		List<ItemStack> acc = getManaAccesories(player);
		for (ItemStack stackInSlot : Iterables.concat(items, acc)) {
			if (stackInSlot == stack) {
				continue;
			}
			IManaItem manaItemSlot = (IManaItem) stackInSlot.getItem();
			int availableMana = manaItemSlot.getMana(stackInSlot);
			if (manaItemSlot.canExportManaToItem(stackInSlot, stack) && availableMana > cost) {
				if (stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canReceiveManaFromItem(stack, stackInSlot)) {
					continue;
				}

				invocations += availableMana / cost;
			}
		}

		return invocations;
	}

	@Override
	public float getFullDiscountForTools(PlayerEntity player, ItemStack tool) {
		float discount = 0F;
		for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
			ItemStack armor = player.inventory.armorInventory.get(i);
			if (!armor.isEmpty() && armor.getItem() instanceof IManaDiscountArmor) {
				discount += ((IManaDiscountArmor) armor.getItem()).getDiscount(armor, i, player, tool);
			}
		}

		int unbreaking = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, tool);
		discount += unbreaking * 0.05F;

		ManaDiscountEvent event = new ManaDiscountEvent(player, discount, tool);
		MinecraftForge.EVENT_BUS.post(event);
		discount = event.getDiscount();

		return discount;
	}
}
