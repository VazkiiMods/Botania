/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.common.collect.ImmutableList;
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
import java.util.Random;
import java.util.stream.Collectors;

public class StateIngredientBlocks implements StateIngredient {
	protected final Collection<Block> blocks;

	public StateIngredientBlocks(List<Block> blocks) {
		this.blocks = ImmutableList.copyOf(blocks);
	}

	public StateIngredientBlocks(Collection<Block> blocks) {
		this.blocks = ImmutableSet.copyOf(blocks);
	}

	@Override
	public boolean test(BlockState state) {
		return blocks.contains(state.getBlock());
	}

	@Override
	public BlockState pick(Random random) {
		if (blocks instanceof List) {
			return ((List<Block>) blocks).get(random.nextInt(blocks.size())).getDefaultState();
		}

		// Slow path, iterating over the set
		int i = random.nextInt(blocks.size());
		for (Block block : blocks) {
			if (i == 0) {
				return block.getDefaultState();
			}
			i--;
		}
		throw new IllegalStateException();
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
			buffer.writeVarInt(Registry.BLOCK.getRawId(block));
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
