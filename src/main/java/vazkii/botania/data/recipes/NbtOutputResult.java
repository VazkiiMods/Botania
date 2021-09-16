/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import com.google.gson.JsonObject;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.crafting.recipe.NbtOutputRecipe;

import java.util.function.Consumer;

public class NbtOutputResult implements FinishedRecipe {
	private final FinishedRecipe innerRecipe;
	private final CompoundTag tag;

	public NbtOutputResult(FinishedRecipe innerRecipe, CompoundTag tag) {
		this.innerRecipe = innerRecipe;
		this.tag = tag;
	}

	public static Consumer<FinishedRecipe> with(Consumer<FinishedRecipe> parent, Consumer<CompoundTag> tagSetup) {
		CompoundTag tag = new CompoundTag();
		tagSetup.accept(tag);
		return recipe -> parent.accept(new NbtOutputResult(recipe, tag));
	}

	@Override
	public void serializeRecipeData(JsonObject json) {
		json.add("recipe", innerRecipe.serializeRecipe());
		json.addProperty("nbt", tag.toString());
	}

	@Override
	public ResourceLocation getId() {
		return innerRecipe.getId();
	}

	@Override
	public RecipeSerializer<?> getType() {
		return NbtOutputRecipe.SERIALIZER;
	}

	@Nullable
	@Override
	public JsonObject serializeAdvancement() {
		return innerRecipe.serializeAdvancement();
	}

	@Nullable
	@Override
	public ResourceLocation getAdvancementId() {
		return innerRecipe.getAdvancementId();
	}
}
