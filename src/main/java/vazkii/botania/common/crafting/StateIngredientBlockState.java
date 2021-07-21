/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;

import vazkii.botania.api.recipe.StateIngredient;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StateIngredientBlockState implements StateIngredient {
	private final BlockState state;

	public StateIngredientBlockState(BlockState state) {
		this.state = state;
	}

	@Override
	public boolean test(BlockState blockState) {
		return this.state == blockState;
	}

	@Override
	public BlockState pick(Random random) {
		return state;
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = StateIngredientHelper.serializeBlockState(state);
		object.addProperty("type", "state");
		return object;
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeVarInt(2);
		buffer.writeVarInt(Block.getStateId(state));
	}

	@Override
	public List<BlockState> getDisplayed() {
		return Collections.singletonList(state);
	}

	public BlockState getState() {
		return state;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		return state == ((StateIngredientBlockState) o).state;
	}

	@Override
	public int hashCode() {
		return state.hashCode();
	}

	@Override
	public String toString() {
		return "StateIngredientBlockState{" + state + "}";
	}
}
