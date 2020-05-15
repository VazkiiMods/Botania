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
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StateIngredientTag extends StateIngredientBlocks {
	private final Tag<Block> tag;

	public StateIngredientTag(Tag<Block> tag) {
		super(tag.getAllElements() instanceof Set ? (Set<Block>) tag.getAllElements() : new HashSet<>(tag.getAllElements()));
		this.tag = tag;
	}

	public StateIngredientTag(ResourceLocation id) {
		this(new BlockTags.Wrapper(id));
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
	public void write(PacketBuffer buffer) {
		super.write(buffer); // We're sending super's contents instead as tags are sent *after* recipes.
	}

	@Override
	public List<BlockState> getDisplayed() {
		return tag.getAllElements().stream().map(Block::getDefaultState).collect(Collectors.toList());
	}

}
