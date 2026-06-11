/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.item;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import vazkii.botania.api.internal.Colored;
import vazkii.botania.client.gui.bag.ColoredContentsPouchContainer;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.helper.EntityHelper;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * A pouch-like item that stores colored items, i.e. items implementing the {@link Colored} interface. The pouch uses
 * two data components for defining the storage slot layout, one is a list of item tags, the other is a list of
 * {@link DyeColor}s.
 * The item tags should ideally be disjoint for best usability, but storage will also work if they overlap. The number
 * of slots available will be the product of the sizes of the two lists. Slots are ordered such that items matching the
 * first tag get one slot per color, followed by one slot per color for the next item tag, and so on.
 */
public abstract class ColoredContentsPouchItem extends Item {

	protected ColoredContentsPouchItem(Properties properties) {
		super(properties);
	}

	public static List<TagKey<Item>> getStoredItemTypes(ItemStack pouch) {
		return Collections.unmodifiableList(pouch.getOrDefault(BotaniaDataComponents.ITEM_TAGS, Collections.emptyList()));
	}

	public SimpleContainer getInventory(ItemStack pouch) {
		List<TagKey<Item>> supportedItemTypes = getStoredItemTypes(pouch);
		List<DyeColor> supportedColors = ColorHelper.supportedColors().toList();

		return new ItemBackedInventory(pouch, supportedItemTypes.size() * supportedColors.size()) {
			@Override
			public boolean canPlaceItem(int slot, ItemStack stack) {
				return isValidItemForSlot(slot, stack, supportedColors, supportedItemTypes);
			}
		};
	}

	public int getInventorySize(ItemStack pouch) {
		return getStoredItemTypes(pouch).size() * (int) ColorHelper.supportedColors().count();
	}

	public boolean isValidItemForSlot(int slot, ItemStack stack, List<DyeColor> supportedColors,
			List<TagKey<Item>> supportedItemTypes) {
		if (!(stack.getItem() instanceof Colored colored) || !stack.getItem().canFitInsideContainerItems()) {
			return false;
		}
		int colorIndex = supportedColors.indexOf(colored.getColor());
		int numColors = supportedColors.size();
		if (colorIndex < 0 || slot % numColors != colorIndex) {
			return false;
		}
		int typeIndex = slot / numColors;
		return typeIndex < supportedItemTypes.size() && stack.is(supportedItemTypes.get(typeIndex));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (!world.isClientSide) {
			ItemStack stack = player.getItemInHand(hand);
			XplatAbstractions.INSTANCE.openMenu((ServerPlayer) player, new MenuProvider() {
				@Override
				public Component getDisplayName() {
					return stack.getHoverName();
				}

				@Override
				public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
					return new ColoredContentsPouchContainer(syncId, inv, hand == InteractionHand.MAIN_HAND);
				}
			}, hand == InteractionHand.MAIN_HAND, ByteBufCodecs.BOOL);
		}
		return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		Direction side = ctx.getClickedFace();

		if ((ctx.getPlayer() == null || ctx.getPlayer().isSecondaryUseActive())
				&& XplatAbstractions.INSTANCE.hasInventory(world, pos, side)) {
			if (!world.isClientSide) {
				Container bagInv = getInventory(ctx.getItemInHand());
				for (int i = 0; i < bagInv.getContainerSize(); i++) {
					ItemStack flower = bagInv.getItem(i);
					ItemStack rem = XplatAbstractions.INSTANCE.insertToInventory(world, pos, side, flower, false);
					bagInv.setItem(i, rem);
				}

			}

			return InteractionResult.sidedSuccess(world.isClientSide());
		}
		return InteractionResult.PASS;
	}

	@Override
	public void onDestroyed(ItemEntity entity) {
		SimpleContainer container = getInventory(entity.getItem());
		List<ItemStack> nonEmptyStacks = container.getItems().stream().filter(s -> !s.isEmpty()).toList();
		ItemUtils.onContainerDestroyed(entity, nonEmptyStacks);
		container.clearContent();
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack pouch, Slot slot, ClickAction clickAction, Player player) {
		if (clickAction != ClickAction.SECONDARY) {
			return false;
		}
		ItemStack toInsert = slot.getItem();
		if (!isSupportedItem(pouch, toInsert)) {
			return false;
		}

		return tryPutItemStack(pouch, player, toInsert, slot::set);
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack pouch, ItemStack toInsert, Slot slot, ClickAction clickAction,
			Player player, SlotAccess cursorAccess) {
		if (clickAction != ClickAction.SECONDARY || !slot.allowModification(player)
				|| !isSupportedItem(pouch, toInsert)) {
			return false;
		}

		return tryPutItemStack(pouch, player, toInsert, cursorAccess::set);
	}

	private boolean tryPutItemStack(ItemStack pouch, Player player, ItemStack toInsert,
			Consumer<ItemStack> slotSetter) {
		IntList slots = findCandidateSlots(player.level(), pouch, toInsert);
		if (slots.isEmpty()) {
			return false;
		}
		SimpleContainer container = getInventory(pouch);
		boolean inserted = false;
		for (int slotIndex : slots) {
			if (tryPutItem(player.level(), pouch, toInsert, container, slotIndex)) {
				inserted = true;
			}
		}
		if (inserted) {
			slotSetter.accept(toInsert);
			return true;
		}
		return false;
	}

	public boolean isSupportedItem(ItemStack pouch, ItemStack pickupStack) {
		return getStoredItemTypes(pouch).stream().anyMatch(pickupStack::is);
	}

	protected void tryPickupItem(ItemStack pouch, ItemEntity entity, Player player) {
		if (!canPickupItems(pouch, player)) {
			return;
		}
		ItemStack pickupStack = entity.getItem();
		if (!isSupportedItem(pouch, pickupStack)) {
			return;
		}

		IntList slots = findCandidateSlots(player.level(), pouch, pickupStack);
		if (slots.isEmpty()) {
			return;
		}
		SimpleContainer container = getInventory(pouch);
		for (int slot : slots) {
			if (tryPutItem(player.level(), pouch, pickupStack, container, slot)) {
				EntityHelper.syncItem(entity);
			}
		}
	}

	protected boolean tryPutItem(Level level, ItemStack pouch, ItemStack pickupStack, SimpleContainer container, int slot) {
		ItemStack existing = container.getItem(slot);
		if (existing.isEmpty()) {
			container.setItem(slot, pickupStack.copyAndClear());
			return true;
		}
		if (ItemStack.isSameItemSameComponents(existing, pickupStack)) {
			int maxSize = existing.getMaxStackSize();
			int toMove = Math.min(pickupStack.getCount(), maxSize - existing.getCount());
			if (toMove > 0) {
				existing.grow(toMove);
				pickupStack.shrink(toMove);
				container.setChanged();
				return true;
			}
		}
		return false;
	}

	protected boolean canPickupItems(ItemStack pouch, Player player) {
		return player.getInventory().getSelected() != pouch;
	}

	public IntList findCandidateSlots(Level level, ItemStack pouch, ItemStack pickupStack) {
		if (!(pickupStack.getItem() instanceof Colored colored)) {
			return IntList.of();
		}
		List<DyeColor> supportedColors = ColorHelper.supportedColors().toList();
		int colorIndex = supportedColors.indexOf(colored.getColor());
		if (colorIndex < 0) {
			return IntList.of();
		}
		int numColors = supportedColors.size();

		List<TagKey<Item>> supportedItemTypes = getStoredItemTypes(pouch);
		IntList candidateSlots = new IntArrayList(supportedItemTypes.size());
		for (int typeIndex = 0; typeIndex < supportedItemTypes.size(); typeIndex++) {
			if (pickupStack.is(supportedItemTypes.get(typeIndex))) {
				candidateSlots.add(typeIndex * numColors + colorIndex);
			}
		}
		return candidateSlots;
	}

	public static boolean onPickupItem(ItemEntity entity, Player player) {
		if (!(entity.getItem().getItem() instanceof Colored)) {
			// might add dyed blocks/items tag support later
			return false;
		}
		Inventory inventory = player.getInventory();
		int containerSize = inventory.getContainerSize();
		for (int i = 0; i < containerSize; i++) {
			ItemStack invStack = inventory.getItem(i);
			if (invStack.getItem() instanceof ColoredContentsPouchItem pouchItem) {
				int originalCount = entity.getItem().getCount();
				pouchItem.tryPickupItem(invStack, entity, player);
				if (entity.getItem().getCount() < originalCount) {
					player.take(entity, originalCount - entity.getItem().getCount());
				}
			}
			if (entity.getItem().isEmpty()) {
				return true;
			}
		}
		return false;
	}
}
