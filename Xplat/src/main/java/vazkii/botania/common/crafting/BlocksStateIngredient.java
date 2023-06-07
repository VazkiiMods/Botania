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

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.recipe.StateIngredient;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BlocksStateIngredient implements StateIngredient {
	protected final ImmutableSet<Block> blocks;

	public BlocksStateIngredient(Collection<Block> blocks) {
		this.blocks = ImmutableSet.copyOf(blocks);
	}

	@Override
	public boolean test(BlockState state) {
		return blocks.contains(state.getBlock());
	}

	@Override
	public BlockState pick(RandomSource random) {
		return blocks.asList().get(random.nextInt(blocks.size())).defaultBlockState();
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "blocks");
		JsonArray array = new JsonArray();
		for (Block block : blocks) {
			array.add(BuiltInRegistries.BLOCK.getKey(block).toString());
		}
		object.add("blocks", array);
		return object;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		List<Block> blocks = getBlocks();
		buffer.writeVarInt(0);
		buffer.writeVarInt(blocks.size());
		for (Block block : blocks) {
			buffer.writeVarInt(BuiltInRegistries.BLOCK.getId(block));
		}
	}

	@Override
	public List<ItemStack> getDisplayedStacks() {
		return blocks.stream()
				.filter(b -> b.asItem() != Items.AIR)
				.map(ItemStack::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<BlockState> getDisplayed() {
		return blocks.stream().map(Block::defaultBlockState).collect(Collectors.toList());
	}

	@NotNull
	public List<Block> getBlocks() {
		return blocks.asList();
	}

	@Override
	public String toString() {
		return "BlocksStateIngredient{" + blocks.toString() + "}";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		return blocks.equals(((BlocksStateIngredient) o).blocks);
	}

	@Override
	public int hashCode() {
		return Objects.hash(blocks);
	}
}
