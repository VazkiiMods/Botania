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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;

import javax.annotation.Nullable;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * A basic interface for inputs and outputs for in-world blocks.
 */
public interface StateIngredient extends Predicate<BlockState> {
	@Override
	boolean test(BlockState state);

	BlockState pick(Random random);

	JsonObject serialize();

	void write(PacketByteBuf buffer);

	List<BlockState> getDisplayed();

	/**
	 * Resolves tag ingredients, returning null if their tag doesn't exist.
	 * Then filters the ingredient down to contents returned by the operator.
	 * The operator must return a list of contents that passed the filter, or null if unchanged.
	 * Used to filter ores deprioritized with the orechid config.
	 */
	@Nullable
	default StateIngredient resolveAndFilter(UnaryOperator<List<Block>> operator) {
		return this;
	}
}
