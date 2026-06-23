/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.*;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

import vazkii.botania.client.gui.box.TrinketCaseMenu;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.helper.InventoryHelper;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.stream.IntStream;

public class BaubleBoxItem extends Item {
	public static final int SIZE = 24;

	public BaubleBoxItem(Properties props) {
		super(props);
	}

	public static SimpleContainer getInventory(ItemStack stack) {
		return new ItemBackedInventory(stack, SIZE) {
			@Override
			public boolean canPlaceItem(int index, ItemStack stack) {
				return EquipmentHandler.instance.isAccessory(stack) && stack.getItem().canFitInsideContainerItems();
			}
		};
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (!world.isClientSide) {
			ItemStack stack = player.getItemInHand(hand);
			stack.set(BotaniaDataComponents.ACTIVE_TRANSIENT, Unit.INSTANCE);
			XplatAbstractions.INSTANCE.openMenu((ServerPlayer) player, new MenuProvider() {
				@Override
				public Component getDisplayName() {
					return stack.getHoverName();
				}

				@Override
				public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
					return new TrinketCaseMenu(syncId, inv, hand == InteractionHand.MAIN_HAND);
				}
			}, hand == InteractionHand.MAIN_HAND, ByteBufCodecs.BOOL);
		}
		return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
	}

	@Override
	public void onDestroyed(ItemEntity entity) {
		var container = getInventory(entity.getItem());
		var stream = IntStream.range(0, container.getContainerSize())
				.mapToObj(container::getItem)
				.filter(s -> !s.isEmpty());
		ItemUtils.onContainerDestroyed(entity, stream::iterator);
		container.clearContent();
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack box, Slot slot, ClickAction clickAction, Player player) {
		return InventoryHelper.overrideStackedOnOther(
				BaubleBoxItem::getInventory,
				player.containerMenu instanceof TrinketCaseMenu,
				box, slot, clickAction, player);
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack box, ItemStack toInsert, Slot slot, ClickAction clickAction,
			Player player, SlotAccess cursorAccess) {
		return InventoryHelper.overrideOtherStackedOnMe(
				BaubleBoxItem::getInventory,
				player.containerMenu instanceof TrinketCaseMenu,
				box, toInsert, clickAction, cursorAccess);
	}
}
