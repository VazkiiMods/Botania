/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.registry.Registry;

import vazkii.botania.api.recipe.StateIngredient;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StateIngredientBlocks implements StateIngredient {
	private final Set<Block> blocks;

	public StateIngredientBlocks(Collection<Block> blocks) {
		this.blocks = ImmutableSet.copyOf(blocks);
	}

	@Override
	public boolean test(BlockState state) {
		return blocks.contains(state.getBlock());
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "blocks");
		JsonArray array = new JsonArray();
		for (Block block : blocks) {
			array.add(Registry.BLOCK.getId(block).toString());
		}
		object.add("blocks", array);
		return object;
	}

	@Override
	public void write(PacketByteBuf buffer) {
		buffer.writeVarInt(0);
		Collection<Block> blocks = getBlocks();
		buffer.writeVarInt(blocks.size());
		for (Block block : blocks) {
			buffer.writeRegistryIdUnsafe(ForgeRegistries.BLOCKS, block);
		}
	}

	@Override
	public List<BlockState> getDisplayed() {
		return blocks.stream().map(Block::getDefaultState).collect(Collectors.toList());
	}

	protected Collection<Block> getBlocks() {
		return blocks;
	}
}
