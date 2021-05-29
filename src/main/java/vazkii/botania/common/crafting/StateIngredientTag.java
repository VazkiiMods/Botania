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
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import vazkii.botania.api.recipe.StateIngredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class StateIngredientTag extends StateIngredientBlocks {
	private final Identifier tag;

	public StateIngredientTag(Identifier id) {
		super(ImmutableSet.of());
		this.tag = id;
	}

	@Nonnull
	protected Tag<Block> resolve() {
		return ServerTagManagerHolder.getTagManager().getBlocks().getTagOrEmpty(tag);
	}

	@Override
	public boolean test(BlockState state) {
		return resolve().contains(state.getBlock());
	}

	@Override
	public BlockState pick(Random random) {
		return resolve().getRandom(random).getDefaultState();
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "tag");
		object.addProperty("tag", tag.toString());
		return object;
	}

	@Nonnull
	@Override
	protected List<Block> getBlocks() {
		return resolve().values();
	}

	@Override
	public List<BlockState> getDisplayed() {
		return resolve().values().stream().map(Block::getDefaultState).collect(Collectors.toList());
	}

	@Nullable
	@Override
	public StateIngredient resolveAndFilter(UnaryOperator<List<Block>> operator) {
		if (resolve().values().isEmpty()) {
			return null;
		}
		List<Block> list = operator.apply(getBlocks());
		if (list != null) {
			return list.isEmpty() ? null : StateIngredientHelper.of(list);
		}
		return this;
	}

	public Identifier getTagId() {
		return tag;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		return tag.equals(((StateIngredientTag) o).tag);
	}

	@Override
	public int hashCode() {
		return tag.hashCode();
	}

	@Override
	public String toString() {
		return "StateIngredientTag{" + tag + "}";
	}
}
