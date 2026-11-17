/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.data.util;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Provides a collection of tags that are considered to be existing already, so they can be used without having to
 * define empty tag files for them. This implementation provides no TagBuilders for its tags.
 * 
 * @param providedTags Collection of provided tags. (Providing a set is recommended.)
 * @param <T>          The tag type.
 */
public record DummyTagLookup<T>(Collection<TagKey<T>> providedTags) implements TagsProvider.TagLookup<T> {

	public static <T> CompletableFuture<TagsProvider.TagLookup<T>> completedFuture(Collection<TagKey<T>> providedTags) {
		return CompletableFuture.completedFuture(new DummyTagLookup<>(providedTags));
	}

	@Override
	public Optional<TagBuilder> apply(TagKey<T> biomeTagKey) {
		return Optional.empty();
	}

	@Override
	public boolean contains(TagKey<T> tagKey) {
		return providedTags.contains(tagKey);
	}
}
