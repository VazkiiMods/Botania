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

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManaItemHandlerImpl implements ManaItemHandler {
	@Override
	public List<ItemStack> getManaItems(Player player) {
		if (player == null) {
			return Collections.emptyList();
		}

		List<ItemStack> toReturn = new ArrayList<>();

		for (ItemStack stackInSlot : Iterables.concat(player.inventory.items, player.inventory.offhand)) {
			if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IManaItem) {
				toReturn.add(stackInSlot);
			}
		}

		ManaItemsCallback.EVENT.invoker().getManaItems(player, toReturn);
		return toReturn;
	}

	@Override
	public List<ItemStack> getManaAccesories(Player player) {
		if (player == null) {
			return Collections.emptyList();
		}

		Container acc = BotaniaAPI.instance().getAccessoriesInventory(player);

		List<ItemStack> toReturn = new ArrayList<>(acc.getContainerSize());

		for (int slot = 0; slot < acc.getContainerSize(); slot++) {
			ItemStack stackInSlot = acc.getItem(slot);

			if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IManaItem) {
				toReturn.add(stackInSlot);
			}
		}

		return toReturn;
	}

	@Override
	public int requestMana(ItemStack stack, Player player, int manaToGet, boolean remove) {
		if (stack.isEmpty()) {
			return 0;
		}

		List<ItemStack> items = getManaItems(player);
		List<ItemStack> acc = getManaAccesories(player);
		for (ItemStack stackInSlot : Iterables.concat(items, acc)) {
			if (stackInSlot == stack) {
				continue;
			}
			IManaItem manaItem = (IManaItem) stackInSlot.getItem();
			if (manaItem.canExportManaToItem(stackInSlot, stack) && manaItem.getMana(stackInSlot) > 0) {
				if (stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canReceiveManaFromItem(stack, stackInSlot)) {
					continue;
				}

				int mana = Math.min(manaToGet, manaItem.getMana(stackInSlot));

				if (remove) {
					manaItem.addMana(stackInSlot, -mana);
				}

				return mana;
			}
		}

		return 0;
	}

	@Override
	public boolean requestManaExact(ItemStack stack, Player player, int manaToGet, boolean remove) {
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
			if (manaItemSlot.canExportManaToItem(stackInSlot, stack) && manaItemSlot.getMana(stackInSlot) > manaToGet) {
				if (stack.getItem() instanceof IManaItem && !((IManaItem) stack.getItem()).canReceiveManaFromItem(stack, stackInSlot)) {
					continue;
				}

				if (remove) {
					manaItemSlot.addMana(stackInSlot, -manaToGet);
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public int dispatchMana(ItemStack stack, Player player, int manaToSend, boolean add) {
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
	public boolean dispatchManaExact(ItemStack stack, Player player, int manaToSend, boolean add) {
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

	private int discountManaForTool(ItemStack stack, Player player, int inCost) {
		float multiplier = Math.max(0F, 1F - getFullDiscountForTools(player, stack));
		return (int) (inCost * multiplier);
	}

	@Override
	public int requestManaForTool(ItemStack stack, Player player, int manaToGet, boolean remove) {
		int cost = discountManaForTool(stack, player, manaToGet);
		return requestMana(stack, player, cost, remove);
	}

	@Override
	public boolean requestManaExactForTool(ItemStack stack, Player player, int manaToGet, boolean remove) {
		int cost = discountManaForTool(stack, player, manaToGet);
		return requestManaExact(stack, player, cost, remove);
	}

	@Override
	public int getInvocationCountForTool(ItemStack stack, Player player, int manaToGet) {
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
	public float getFullDiscountForTools(Player player, ItemStack tool) {
		float discount = 0F;
		for (int i = 0; i < player.inventory.armor.size(); i++) {
			ItemStack armor = player.inventory.armor.get(i);
			if (!armor.isEmpty() && armor.getItem() instanceof IManaDiscountArmor) {
				discount += ((IManaDiscountArmor) armor.getItem()).getDiscount(armor, i, player, tool);
			}
		}

		int unbreaking = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, tool);
		discount += unbreaking * 0.05F;
		discount = ManaDiscountCallback.EVENT.invoker().getManaDiscount(player, discount, tool);

		return discount;
	}
}
