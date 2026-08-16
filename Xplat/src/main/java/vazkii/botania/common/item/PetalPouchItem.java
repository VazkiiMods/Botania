/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.item;

import it.unimi.dsi.fastutil.ints.IntList;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeCache;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.DataComponentHelper;

import java.util.Collections;
import java.util.List;

public class PetalPouchItem extends ColoredContentsPouchItem {
	private static final RecipeCache RECIPE_CACHE = new RecipeCache(16);

	protected PetalPouchItem(Properties properties) {
		super(properties);
	}

	public static List<TagKey<Item>> getConvertedItemTypes(ItemStack pouch) {
		return Collections.unmodifiableList(pouch.getOrDefault(BotaniaDataComponents.CRAFTABLE_ITEM_TAGS, Collections.emptyList()));
	}

	public boolean isActive(ItemStack stack) {
		return stack.has(BotaniaDataComponents.ACTIVE);
	}

	public boolean isSupportedItem(ItemStack pouch, ItemStack pickupStack) {
		return super.isSupportedItem(pouch, pickupStack)
				|| isActive(pouch) && getConvertedItemTypes(pouch).stream().anyMatch(pickupStack::is);
	}

	@Override
	public IntList findCandidateSlots(Level level, ItemStack pouch, ItemStack pickupStack) {
		if (isActive(pouch) && !super.isSupportedItem(pouch, pickupStack)) {
			CraftingContainer craft = getCraftingContainer();
			craft.setItem(0, pickupStack.copyWithCount(1));
			return RECIPE_CACHE.get(level, craft.asCraftInput())
					.map(RecipeHolder::value)
					.map(recipe -> recipe.assemble(craft.asCraftInput(), level.registryAccess()))
					.map(stack -> super.findCandidateSlots(level, pouch, stack))
					.orElseGet(IntList::of);
		}
		return super.findCandidateSlots(level, pouch, pickupStack);
	}

	private static CraftingContainer getCraftingContainer() {
		AbstractContainerMenu menu = new AbstractContainerMenu(MenuType.CRAFTING, -1) {
			@Override
			public ItemStack quickMoveStack(Player player, int i) {
				return ItemStack.EMPTY;
			}

			@Override
			public boolean stillValid(Player player) {
				return false;
			}
		};
		return new TransientCraftingContainer(menu, 1, 1);
	}

	@Override
	protected boolean tryPutItem(Level level, ItemStack pouch, ItemStack pickupStack, SimpleContainer container, int slot) {
		if (super.isSupportedItem(pouch, pickupStack) || !isActive(pouch)) {
			return super.tryPutItem(level, pouch, pickupStack, container, slot);
		}
		CraftingContainer craft = getCraftingContainer();
		craft.setItem(0, pickupStack.copyWithCount(1));
		var convertedStackOptional = RECIPE_CACHE.get(level, craft.asCraftInput()).map(RecipeHolder::value)
				.map(recipe -> recipe.assemble(craft.asCraftInput(), level.registryAccess()));
		if (convertedStackOptional.map(ItemStack::isEmpty).orElse(true)) {
			return false;
		}
		ItemStack convertedStack = convertedStackOptional.get();
		int countPerInput = convertedStack.getCount();
		ItemStack existing = container.getItem(slot);
		if (existing.isEmpty()) {
			int maxStackSize = convertedStack.getMaxStackSize();
			int maxNumInputItems = maxStackSize / countPerInput;
			int numConsumedInputItems = Math.min(pickupStack.getCount(), maxNumInputItems);
			container.setItem(slot, convertedStack.copyWithCount(numConsumedInputItems * countPerInput));
			pickupStack.shrink(numConsumedInputItems);
			return true;
		}
		if (ItemStack.isSameItemSameComponents(existing, convertedStack)) {
			int maxStackSize = existing.getMaxStackSize();
			int maxNumInputItems = (maxStackSize - existing.getCount()) / countPerInput;
			int numConsumedInputItems = Math.min(pickupStack.getCount(), maxNumInputItems);
			if (numConsumedInputItems > 0) {
				existing.grow(numConsumedInputItems * countPerInput);
				pickupStack.shrink(numConsumedInputItems);
				container.setChanged();
				return true;
			}
		}
		return false;

	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> stacks, TooltipFlag flags) {
		stacks.add(Component.translatable(isActive(stack) ? "botaniamisc.active" : "botaniamisc.inactive"));
		super.appendHoverText(stack, context, stacks, flags);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.isSecondaryUseActive()) {
			toggleActive(level, player, stack);
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
		}
		return super.use(level, player, hand);
	}

	private void toggleActive(Level level, Player player, ItemStack stack) {
		DataComponentHelper.setFlag(stack, BotaniaDataComponents.ACTIVE, !isActive(stack));
		level.playSound(player, player.getX(), player.getY(), player.getZ(), BotaniaSounds.PETAL_POUCH_CONFIGURE, SoundSource.NEUTRAL, 1f, 1f);
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack pouch, ItemStack toInsert, Slot slot, ClickAction clickAction,
			Player player, SlotAccess cursorAccess) {
		if (clickAction == ClickAction.SECONDARY && cursorAccess.get().isEmpty()) {
			toggleActive(player.level(), player, pouch);
			slot.set(pouch);
			return true;
		}
		return super.overrideOtherStackedOnMe(pouch, toInsert, slot, clickAction, player, cursorAccess);
	}
}
