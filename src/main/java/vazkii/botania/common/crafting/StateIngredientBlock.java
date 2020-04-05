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

import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.registries.ForgeRegistries;

import vazkii.botania.api.recipe.StateIngredient;

import java.util.Collections;
import java.util.List;

public class StateIngredientBlock implements StateIngredient {
	private final net.minecraft.block.Block block;

	public StateIngredientBlock(net.minecraft.block.Block block) {
		this.block = block;
	}

	@Override
	public boolean test(BlockState blockState) {
		return block == blockState.getBlock();
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "block");
		object.addProperty("block", block.getRegistryName().toString());
		return object;
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeVarInt(1);
		buffer.writeRegistryIdUnsafe(ForgeRegistries.BLOCKS, block);
	}

	@Override
	public List<BlockState> getDisplayed() {
		return Collections.singletonList(block.getDefaultState());
	}

}
