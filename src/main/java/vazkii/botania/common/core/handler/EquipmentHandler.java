/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import top.theillusivec4.curios.api.event.DropRulesCallback;

import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.EquipmentHandler.InventoryEquipmentHandler;
import vazkii.botania.common.integration.curios.CurioIntegration;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;
import vazkii.botania.common.lib.ModTags;

import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Predicate;

public abstract class EquipmentHandler {
	public static EquipmentHandler instance;

	public static void init() {
		if (Botania.curiosLoaded) {
			instance = new CurioIntegration();
			CurioIntegration.init();
			DropRulesCallback.EVENT.register(CurioIntegration::keepCurioDrops);
		} else {
			InventoryEquipmentHandler handler = new InventoryEquipmentHandler();
			instance = handler;
		}
	}

	public static Inventory getAllWorn(LivingEntity living) {
		return instance.getAllWornItems(living);
	}

	public static ItemStack findOrEmpty(Item item, LivingEntity living) {
		return instance.findItem(item, living);
	}

	public static ItemStack findOrEmpty(Predicate<ItemStack> pred, LivingEntity living) {
		return instance.findItem(pred, living);
	}

	public static void initBaubleCap(Item item) {
		instance.registerComponentEvent(item);
	}

	protected abstract Inventory getAllWornItems(LivingEntity living);

	protected abstract ItemStack findItem(Item item, LivingEntity living);

	protected abstract ItemStack findItem(Predicate<ItemStack> pred, LivingEntity living);

	protected abstract void registerComponentEvent(Item item);

	public boolean isAccessory(ItemStack stack) {
		return ModTags.Items.RODS.contains(stack.getItem()) || stack.getItem() instanceof ItemBauble || stack.getItem() instanceof IManaItem;
	}

	// Fallback equipment handler for curios-less (or baubles-less) installs.
	public static class InventoryEquipmentHandler extends EquipmentHandler {
		private final Map<PlayerEntity, ItemStack[]> map = new WeakHashMap<>();

		public void onPlayerTick(PlayerEntity player) {
			player.world.getProfiler().push("botania:tick_wearables");

			ItemStack[] oldStacks = map.computeIfAbsent(player, p -> {
				ItemStack[] array = new ItemStack[9];
				Arrays.fill(array, ItemStack.EMPTY);
				return array;
			});

			PlayerInventory inv = player.inventory;
			for (int i = 0; i < 9; i++) {
				ItemStack old = oldStacks[i];
				ItemStack current = inv.getStack(i);

				if (!ItemStack.areEqual(old, current)) {
					if (old.getItem() instanceof ItemBauble) {
						player.getAttributes().removeModifiers(((ItemBauble) old.getItem()).getEquippedAttributeModifiers(old));
						((ItemBauble) old.getItem()).onUnequipped(old, player);
					}
					if (canEquip(current, player)) {
						player.getAttributes().addTemporaryModifiers(((ItemBauble) current.getItem()).getEquippedAttributeModifiers(current));
						((ItemBauble) current.getItem()).onEquipped(current, player);
					}
					oldStacks[i] = current.copy(); // shift-clicking mutates the stack we stored,
					// making it empty and failing the equality check - let's avoid that
				}

				if (canEquip(current, player)) {
					((ItemBauble) current.getItem()).onWornTick(current, player);
				}
			}
			player.world.getProfiler().pop();
		}

		@Override
		protected Inventory getAllWornItems(LivingEntity living) {
			return new SimpleInventory(0);
		}

		@Override
		protected ItemStack findItem(Item item, LivingEntity living) {
			if (living instanceof PlayerEntity) {
				PlayerInventory inv = ((PlayerEntity) living).inventory;
				for (int i = 0; i < 9; i++) {
					ItemStack stack = inv.getStack(i);
					if (stack.getItem() == item && canEquip(stack, living)) {
						return stack;
					}
				}
			}
			return ItemStack.EMPTY;
		}

		@Override
		protected ItemStack findItem(Predicate<ItemStack> pred, LivingEntity living) {
			if (living instanceof PlayerEntity) {
				PlayerInventory inv = ((PlayerEntity) living).inventory;
				for (int i = 0; i < 9; i++) {
					ItemStack stack = inv.getStack(i);
					if (pred.test(stack) && canEquip(stack, living)) {
						return stack;
					}
				}
			}
			return ItemStack.EMPTY;
		}

		@Override
		protected void registerComponentEvent(Item item) {}

		private static boolean canEquip(ItemStack stack, LivingEntity player) {
			return stack.getItem() instanceof ItemBauble && ((ItemBauble) stack.getItem()).canEquip(stack, player);
		}
	}
}
