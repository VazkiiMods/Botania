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

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.SerializationTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.recipe.StateIngredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class StateIngredientTag extends StateIngredientBlocks {
	private final ResourceLocation tag;

	public StateIngredientTag(ResourceLocation id) {
		super(ImmutableSet.of());
		this.tag = id;
	}

	@Nonnull
	protected Tag<Block> resolve() {
		return SerializationTags.getInstance().getOrEmpty(Registry.BLOCK_REGISTRY).getTagOrEmpty(tag);
	}

	@Override
	public boolean test(BlockState state) {
		return resolve().contains(state.getBlock());
	}

	@Override
	public BlockState pick(Random random) {
		Tag<Block> tag = resolve();
		if (tag.getValues().isEmpty()) {
			return null;
		}
		return tag.getRandomElement(random).defaultBlockState();
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
		return resolve().getValues().stream()
				.filter(b -> b.asItem() != Items.AIR)
				.map(ItemStack::new)
				.collect(Collectors.toList());
	}

	@Nonnull
	@Override
	protected List<Block> getBlocks() {
		return resolve().getValues();
	}

	@Override
	public List<BlockState> getDisplayed() {
		return resolve().getValues().stream().map(Block::defaultBlockState).collect(Collectors.toList());
	}

	@Nullable
	@Override
	public StateIngredient resolveAndFilter(UnaryOperator<List<Block>> operator) {
		if (resolve().getValues().isEmpty()) {
			return null;
		}
		List<Block> list = operator.apply(getBlocks());
		if (list != null) {
			return list.isEmpty() ? null : StateIngredientHelper.of(list);
		}
		return this;
	}

	public ResourceLocation getTagId() {
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
