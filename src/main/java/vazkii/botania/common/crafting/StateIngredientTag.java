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
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StateIngredientTag extends StateIngredientBlocks {
	private final ResourceLocation tag;

	public StateIngredientTag(ResourceLocation id) {
		super(ImmutableSet.of());
		this.tag = id;
	}

	protected ITag<Block> resolve() {
		return TagCollectionManager.getManager().getBlockTags().get(tag);
	}

	@Override
	public boolean test(BlockState state) {
		return resolve().contains(state.getBlock());
	}

	@Override
	public BlockState pick(Random random) {
		return resolve().getRandomElement(random).getDefaultState();
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
		return resolve().getAllElements();
	}

	@Override
	public List<BlockState> getDisplayed() {
		return resolve().getAllElements().stream().map(Block::getDefaultState).collect(Collectors.toList());
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
}
