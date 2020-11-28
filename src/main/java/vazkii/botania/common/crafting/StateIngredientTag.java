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
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class StateIngredientTag extends StateIngredientBlocks {
	private final Identifier tag;

	public StateIngredientTag(Identifier id) {
		super(ImmutableSet.of());
		this.tag = id;
	}

	private Tag<Block> resolve() {
		return BlockTags.getTagGroup().getTagOrEmpty(tag);
	}

	@Override
	public boolean test(BlockState state) {
		return resolve().contains(state.getBlock());
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "tag");
		object.addProperty("tag", tag.toString());
		return object;
	}

	@Override
	protected Collection<Block> getBlocks() {
		return resolve().values();
	}

	@Override
	public List<BlockState> getDisplayed() {
		return resolve().values().stream().map(Block::getDefaultState).collect(Collectors.toList());
	}

}
