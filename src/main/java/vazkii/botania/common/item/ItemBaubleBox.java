/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
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

import vazkii.botania.client.gui.box.ContainerBaubleBox;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.helper.InventoryHelper;

import javax.annotation.Nonnull;

import java.util.stream.IntStream;

public class ItemBaubleBox extends Item {
	public static final int SIZE = 24;

	public ItemBaubleBox(Properties props) {
		super(props);
	}

	public static SimpleContainer getInventory(ItemStack stack) {
		return new ItemBackedInventory(stack, SIZE) {
			@Override
			public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
				return EquipmentHandler.instance.isAccessory(stack);
			}
		};
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		if (!world.isClientSide) {
			ItemStack stack = player.getItemInHand(hand);
			MenuProvider container = new ExtendedScreenHandlerFactory() {
				@Override
				public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
					buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
				}

				@Override
				public Component getDisplayName() {
					return stack.getHoverName();
				}

				@Override
				public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
					return new ContainerBaubleBox(syncId, inv, stack);
				}
			};
			player.openMenu(container);
		}
		return InteractionResultHolder.success(player.getItemInHand(hand));
	}

	@Override
	public void onDestroyed(@Nonnull ItemEntity entity) {
		var container = getInventory(entity.getItem());
		var stream = IntStream.range(0, container.getContainerSize())
				.mapToObj(container::getItem)
				.filter(s -> !s.isEmpty());
		ItemUtils.onContainerDestroyed(entity, stream);
		container.clearContent();
	}

	@Override
	public boolean overrideStackedOnOther(
			@Nonnull ItemStack box, @Nonnull Slot slot,
			@Nonnull ClickAction clickAction, @Nonnull Player player) {
		return InventoryHelper.overrideStackedOnOther(
				ItemBaubleBox::getInventory,
				player.containerMenu instanceof ContainerBaubleBox,
				box, slot, clickAction, player);
	}

	@Override
	public boolean overrideOtherStackedOnMe(
			@Nonnull ItemStack box, @Nonnull ItemStack toInsert,
			@Nonnull Slot slot, @Nonnull ClickAction clickAction,
			@Nonnull Player player, @Nonnull SlotAccess cursorAccess) {
		return InventoryHelper.overrideOtherStackedOnMe(
				ItemBaubleBox::getInventory,
				player.containerMenu instanceof ContainerBaubleBox,
				box, toInsert, clickAction, cursorAccess);
	}
}
