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
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;

import java.util.function.Consumer;

public class WrapperResult implements RecipeJsonProvider {
	private final RecipeJsonProvider delegate;
	@Nullable
	private final RecipeSerializer<?> type;
	@Nullable
	private final Consumer<JsonObject> transform;

	/**
	 * Wraps recipe consumer with one that swaps the recipe type to a different one.
	 */
	public static Consumer<RecipeJsonProvider> ofType(RecipeSerializer<?> type, Consumer<RecipeJsonProvider> parent) {
		return recipe -> parent.accept(new WrapperResult(recipe, type, null));
	}

	/**
	 * Transforms the resulting recipe json with the specified action, eg. adding NBT to an item result.
	 */
	public static Consumer<RecipeJsonProvider> transformJson(Consumer<RecipeJsonProvider> parent, Consumer<JsonObject> transform) {
		return recipe -> parent.accept(new WrapperResult(recipe, null, transform));
	}

	private WrapperResult(RecipeJsonProvider delegate, @Nullable RecipeSerializer<?> type, @Nullable Consumer<JsonObject> transform) {
		this.delegate = delegate;
		this.type = type;
		this.transform = transform;
	}

	@Override
	public void serialize(JsonObject json) {
		delegate.serialize(json);
		if (transform != null) {
			transform.accept(json);
		}
	}

	@Override
	public JsonObject toJson() {
		if (type == null) {
			return RecipeJsonProvider.super.toJson();
		}
		JsonObject jsonobject = new JsonObject();
		jsonobject.addProperty("type", Registry.RECIPE_SERIALIZER.getId(this.type).toString());
		this.serialize(jsonobject);
		return jsonobject;
	}

	@Override
	public Identifier getRecipeId() {
		return delegate.getRecipeId();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return type != null ? type : delegate.getSerializer();
	}

	@Nullable
	@Override
	public JsonObject toAdvancementJson() {
		return delegate.toAdvancementJson();
	}

	@Nullable
	@Override
	public Identifier getAdvancementId() {
		return delegate.getAdvancementId();
	}
}
