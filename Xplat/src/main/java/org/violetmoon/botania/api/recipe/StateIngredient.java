/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.mixin.LiquidBlockAccessor;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A basic interface for inputs and outputs for in-world blocks.
 */
public interface StateIngredient extends Predicate<BlockState> {
	@Override
	boolean test(BlockState state);

	BlockState pick(RandomSource random);

	StateIngredientType<?> getType();

	default List<ItemStack> getDisplayedStacks() {
		return streamBlockStates()
				.map(BlockState::getBlock)
				.map(StateIngredient::getRecipeItemForBlock)
				.filter(item -> item != Items.AIR)
				.map(ItemStack::new)
				.toList();
	}

	/** A description tooltip to display in areas like JEI recipes. */
	default List<Component> descriptionTooltip() {
		return Collections.emptyList();
	}

	List<BlockState> getDisplayed();

	Stream<BlockState> streamBlockStates();

	private static Item getRecipeItemForBlock(Block block) {
		Item item = block.asItem();
		if (item == Items.AIR && block instanceof LiquidBlock liquidBlock) {
			return ((LiquidBlockAccessor) liquidBlock).botania_getFluid().getBucket();
		}
		return item;
	}
}
