/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * A basic interface for inputs and outputs for in-world blocks.
 */
public interface StateIngredient extends Predicate<BlockState> {
	@Override
	boolean test(BlockState state);

	BlockState pick(Random random);

	JsonObject serialize();

	void write(FriendlyByteBuf buffer);

	List<ItemStack> getDisplayedStacks();

	/** A description tooltip to display in areas like JEI recipes. */
	default List<Component> descriptionTooltip() {
		return Collections.emptyList();
	}

	List<BlockState> getDisplayed();
}
