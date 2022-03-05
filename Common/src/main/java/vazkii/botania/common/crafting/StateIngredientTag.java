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
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StateIngredientTag extends StateIngredientBlocks {
	private final TagKey<Block> tag;

	public StateIngredientTag(ResourceLocation tag) {
		super(ImmutableSet.of());
		this.tag = TagKey.create(Registry.BLOCK_REGISTRY, tag);
	}

	public Stream<Block> resolve() {
		return StreamSupport.stream(Registry.BLOCK.getTagOrEmpty(tag).spliterator(), false)
				.map(Holder::value);
	}

	@Override
	public boolean test(BlockState state) {
		return state.getBlock().builtInRegistryHolder().is(tag);
	}

	@Override
	public BlockState pick(Random random) {
		var values = resolve().toList();
		if (values.isEmpty()) {
			return null;
		}
		return values.get(random.nextInt(values.size())).defaultBlockState();
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "tag");
		object.addProperty("tag", tag.toString());
		return object;
	}

	@Override
	public List<ItemStack> getDisplayedStacks() {
		return resolve()
				.filter(b -> b.asItem() != Items.AIR)
				.map(ItemStack::new)
				.collect(Collectors.toList());
	}

	@Nonnull
	@Override
	protected List<Block> getBlocks() {
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
