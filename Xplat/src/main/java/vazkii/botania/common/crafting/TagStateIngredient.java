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

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TagStateIngredient extends BlocksStateIngredient {
	private final TagKey<Block> tag;

	public TagStateIngredient(ResourceLocation tag) {
		super(ImmutableSet.of());
		this.tag = TagKey.create(Registries.BLOCK, tag);
	}

	public Stream<Block> resolve() {
		return StreamSupport.stream(BuiltInRegistries.BLOCK.getTagOrEmpty(tag).spliterator(), false)
				.map(Holder::value);
	}

	@Override
	public boolean test(BlockState state) {
		return state.is(tag);
	}

	@Override
	public BlockState pick(RandomSource random) {
		var blocks = getBlocks();
		if (blocks.isEmpty()) {
			return null;
		}
		return blocks.get(random.nextInt(blocks.size())).defaultBlockState();
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "tag");
		object.addProperty("tag", tag.location().toString());
		return object;
	}

	@Override
	public List<ItemStack> getDisplayedStacks() {
		return resolve()
				.filter(b -> b.asItem() != Items.AIR)
				.map(ItemStack::new)
				.collect(Collectors.toList());
	}

	@NotNull
	@Override
	public List<Block> getBlocks() {
		return resolve().toList();
	}

	@Override
	public List<BlockState> getDisplayed() {
		return resolve().map(Block::defaultBlockState).collect(Collectors.toList());
	}

	public ResourceLocation getTagId() {
		return tag.location();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		return tag.equals(((TagStateIngredient) o).tag);
	}

	@Override
	public int hashCode() {
		return tag.hashCode();
	}

	@Override
	public String toString() {
		return "TagStateIngredient{" + tag + "}";
	}
}
