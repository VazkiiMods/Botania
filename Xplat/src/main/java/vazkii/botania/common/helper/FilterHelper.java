/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.ToIntBiFunction;

public class FilterHelper {

	public static List<ItemStack> getFiltersOnBlock(Level level, BlockPos pos, boolean includeOtherHalfOfChests) {
		List<ItemStack> filter = new ArrayList<>();

		Consumer<ItemFrame> addToFilterList = frame -> filter.addAll(getFilterItems(frame));
		if (includeOtherHalfOfChests) {
			var otherPos = getOtherChestHalf(level, pos);
			if (otherPos.isPresent()) {
				populateFiltersAroundPosition(level, otherPos.get(), addToFilterList);
			}
		}

		populateFiltersAroundPosition(level, pos, addToFilterList);

		return filter;
	}

	private static Optional<BlockPos> getOtherChestHalf(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (state.getOptionalValue(ChestBlock.TYPE).orElse(ChestType.SINGLE) == ChestType.SINGLE) {
			return Optional.empty();
		}
		Direction connectedDirection = ChestBlock.getConnectedDirection(state);
		BlockPos other = pos.relative(connectedDirection);
		BlockState otherState = level.getBlockState(other);
		return otherState.is(state.getBlock()) && otherState.hasProperty(ChestBlock.TYPE)
				&& ChestBlock.getConnectedDirection(otherState) == connectedDirection.getOpposite()
						? Optional.of(other)
						: Optional.empty();
	}

	public static WeightedRandomList<WeightedItemStack> getWeightedFiltersOnBlock(Level level, BlockPos pos,
			BiFunction<ItemFrame, ItemStack, ItemStack> stackTransform,
			ToIntBiFunction<ItemFrame, ItemStack> weightFunction) {
		List<FilterHelper.WeightedItemStack> filter = new ArrayList<>();

		populateFiltersAroundPosition(level, pos, frame -> {
			List<ItemStack> filterStacks = FilterHelper.getFilterItems(frame);
			if (!filterStacks.isEmpty()) {
				filterStacks.stream()
						.map(s -> FilterHelper.WeightedItemStack.of(stackTransform.apply(frame, s),
								weightFunction.applyAsInt(frame, s)))
						.forEach(filter::add);
			}
		});

		return WeightedRandomList.create(filter);
	}

	private static void populateFiltersAroundPosition(Level level, BlockPos pos, Consumer<ItemFrame> filterConsumer) {
		AABB aabb = new AABB(pos).inflate(0.05);
		for (ItemFrame frame : level.getEntitiesOfClass(ItemFrame.class, aabb,
				f -> f.blockPosition().equals(pos.relative(f.getDirection())))) {
			filterConsumer.accept(frame);
		}
	}

	public static List<ItemStack> getFilterItems(ItemFrame filterFrame) {
		ItemStack filterStack = filterFrame.getItem();
		if (filterStack.isEmpty()) {
			return List.of();
		}
		return filterFrame instanceof GlowItemFrame ? getFilterStacks(filterStack) : List.of(filterStack);
	}

	/**
	 * Expands the given filter item into the list of filter items it represents. This is NOT recursive.
	 * Non-empty container items stand for their contents, not for themselves.
	 */
	public static List<ItemStack> getFilterStacks(ItemStack filterStack) {
		BundleContents bundleContents = filterStack.get(DataComponents.BUNDLE_CONTENTS);
		if (bundleContents != null && !bundleContents.isEmpty()) {
			return bundleContents.itemCopyStream().toList();
		}

		ItemContainerContents containerContents = filterStack.get(DataComponents.CONTAINER);
		if (containerContents != null) {
			List<ItemStack> items = containerContents.nonEmptyStream().toList();
			if (!items.isEmpty()) {
				return items;
			}
		}
		return List.of(filterStack);
	}

	public record WeightedItemStack(ItemStack stack, Weight weight) implements WeightedEntry {
		public static WeightedItemStack of(ItemStack stack, int weight) {
			return new WeightedItemStack(stack, Weight.of(weight));
		}

		@Override
		public Weight getWeight() {
			return weight;
		}
	}
}
