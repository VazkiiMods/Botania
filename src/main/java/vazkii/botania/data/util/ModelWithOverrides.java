/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.data.client.model.Texture;
import net.minecraft.data.client.model.TextureKey;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Copy of {@link net.minecraft.data.client.model.Model} with support for generating item predicate overrides.
 */
public class ModelWithOverrides {
	private final Identifier parent;
	private final TextureKey[] requiredTextures;

	public ModelWithOverrides(Identifier parent, TextureKey... requiredTextures) {
		this.parent = parent;
		this.requiredTextures = requiredTextures;
	}

	public void upload(Identifier modelId, Texture textures, OverrideHolder overrides, BiConsumer<Identifier, Supplier<JsonElement>> consumer) {
		Map<TextureKey, Identifier> textureMap = Streams.concat(Arrays.stream(this.requiredTextures), textures.getInherited())
				.collect(ImmutableMap.toImmutableMap(Function.identity(), textures::getTexture));
		consumer.accept(modelId, () -> {
			JsonObject ret = new JsonObject();
			ret.addProperty("parent", parent.toString());
			if (!textureMap.isEmpty()) {
				JsonObject textureJson = new JsonObject();
				textureMap.forEach((k, path) -> textureJson.addProperty(k.getName(), path.toString()));
				ret.add("textures", textureJson);
			}
			JsonArray overridesJson = overrides.toJson();
			if (overridesJson != null) {
				ret.add("overrides", overridesJson);
			}
			return ret;
		});
	}

}
