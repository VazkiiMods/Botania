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
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;

public class StateIngredientTag extends StateIngredientBlocks {
	private final ITag.INamedTag<Block> tag;

	public StateIngredientTag(ITag.INamedTag<Block> tag) {
		super(ImmutableSet.of());
		this.tag = tag;
	}

	public StateIngredientTag(ResourceLocation id) {
		this(BlockTags.makeWrapperTag(id.toString()));
	}

	@Override
	public boolean test(BlockState state) {
		return tag.func_230235_a_(state.getBlock());
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "tag");
		object.addProperty("tag", tag.func_230234_a_().toString());
		return object;
	}

	@Override
	public void write(PacketBuffer buffer) {
		super.write(buffer); // We're sending super's contents instead as tags are sent *after* recipes.
	}

	@Override
	public List<BlockState> getDisplayed() {
		return tag.func_230236_b_().stream().map(Block::getDefaultState).collect(Collectors.toList());
	}

}
