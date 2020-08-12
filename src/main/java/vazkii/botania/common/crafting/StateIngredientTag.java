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

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class StateIngredientTag extends StateIngredientBlocks {
	private final Tag.Identified<Block> tag;

	public StateIngredientTag(Tag.Identified<Block> tag) {
		super(ImmutableSet.of());
		this.tag = tag;
	}

	public StateIngredientTag(Identifier id) {
		this(TagRegistry.create(id, BlockTags::getTagGroup));
	}

	@Override
	public boolean test(BlockState state) {
		return tag.contains(state.getBlock());
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "tag");
		object.addProperty("tag", tag.getId().toString());
		return object;
	}

	@Override
	protected Collection<Block> getBlocks() {
		return tag.values();
	}

	@Override
	public List<BlockState> getDisplayed() {
		return tag.values().stream().map(Block::getDefaultState).collect(Collectors.toList());
	}

}
