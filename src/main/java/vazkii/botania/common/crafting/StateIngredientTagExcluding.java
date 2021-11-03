/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.recipe.StateIngredient;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class StateIngredientTagExcluding extends StateIngredientTag {
	private final List<StateIngredient> excludes;

	public StateIngredientTagExcluding(ResourceLocation id, Collection<StateIngredient> excludes) {
		super(id);
		this.excludes = List.copyOf(excludes);
	}

	@Override
	public boolean test(BlockState state) {
		if (!super.test(state)) {
			return false;
		}
		return isNotExcluded(state);
	}

	@Override
	public BlockState pick(Random random) {
		List<Block> blocks = getBlocks();
		if (blocks.isEmpty()) {
			return null;
		}
		return blocks.get(random.nextInt(blocks.size())).defaultBlockState();
	}

	private boolean isNotExcluded(BlockState state) {
		for (StateIngredient exclude : excludes) {
			if (exclude.test(state)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o) && this.excludes.equals(((StateIngredientTagExcluding) o).excludes);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "tag_excluding");
		object.addProperty("tag", getTagId().toString());
		JsonArray array = new JsonArray();
		for (StateIngredient exclude : excludes) {
			array.add(exclude.serialize());
		}
		object.add("exclude", array);
		return object;
	}

	@Override
	public List<ItemStack> getDisplayedStacks() {
		return getBlocks().stream()
				.filter(b -> b.asItem() != Items.AIR)
				.map(ItemStack::new)
				.toList();
	}

	@NotNull
	@Override
	protected List<Block> getBlocks() {
		return super.getBlocks().stream()
				.filter(b -> isNotExcluded(b.defaultBlockState()))
				.toList();
	}

	@Override
	public List<BlockState> getDisplayed() {
		return super.getDisplayed().stream()
				.filter(this::isNotExcluded)
				.toList();
	}
}
