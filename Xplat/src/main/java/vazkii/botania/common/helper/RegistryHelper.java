/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.helper;

import com.mojang.datafixers.util.Either;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RegistryHelper {
	@SuppressWarnings("unchecked")
	public static <T> DefaultedRegistry<T> getDefaultedRegistry(ResourceKey<Registry<T>> resourceKey) {
		return (DefaultedRegistry<T>) BuiltInRegistries.REGISTRY.get(resourceKey.location());
	}

	public static <T> HolderProxy<T> lazyHolderProxy(ResourceKey<Registry<T>> registryKey, ResourceLocation id, Supplier<T> valueSupplier) {
		return new HolderProxy<>(ResourceKey.create(registryKey, id), valueSupplier);
	}

	public static <T> HolderProxy<T> holderProxy(ResourceKey<Registry<T>> registryKey, ResourceLocation id, T value) {
		return new HolderProxy<>(ResourceKey.create(registryKey, id), value);
	}

	public static class HolderProxy<T> extends Holder.Reference<T> {
		private static final HolderOwner<?> DUMMY_OWNER = new HolderOwner<>() {};
		private final ResourceKey<T> resourceKey;
		private final Supplier<T> value;
		private final boolean directValue;
		@Nullable
		private Reference<T> reference = null;

		@SuppressWarnings("unchecked")
		public HolderProxy(ResourceKey<T> resourceKey, T value) {
			super(Type.STAND_ALONE, (HolderOwner<T>) DUMMY_OWNER, resourceKey, value);
			this.resourceKey = resourceKey;
			this.value = () -> value;
			this.directValue = true;
		}

		@SuppressWarnings("unchecked")
		public HolderProxy(ResourceKey<T> resourceKey, Supplier<T> valueSupplier) {
			super(Type.STAND_ALONE, (HolderOwner<T>) DUMMY_OWNER, resourceKey, null);
			this.resourceKey = resourceKey;
			this.value = valueSupplier;
			this.directValue = false;
		}

		public void register(Registry<T> registry) {
			if (!resourceKey.isFor(registry.key())) {
				throw new IllegalArgumentException("Mismatched registry: expected %s, got %s"
						.formatted(resourceKey.registry(), registry.key().location()));
			}
			reference = Registry.registerForHolder(registry, resourceKey, value.get());
		}

		@NotNull
		@Override
		public T value() {
			if (reference == null) {
				if (!directValue) {
					throw new IllegalStateException("Attempted to access value of " + resourceKey + " before initialization");
				}
				return value.get();
			}
			return reference.value();
		}

		@Override
		public boolean isBound() {
			return reference != null && reference.isBound();
		}

		@Override
		public boolean is(ResourceLocation location) {
			return reference != null ? reference.is(location) : resourceKey.location().equals(location);
		}

		@Override
		public boolean is(ResourceKey<T> resourceKey) {
			return this.resourceKey.equals(resourceKey);
		}

		@Override
		public boolean is(Predicate<ResourceKey<T>> predicate) {
			return predicate.test(resourceKey);
		}

		@Override
		public boolean is(TagKey<T> tagKey) {
			return reference != null && reference.is(tagKey);
		}

		@Override
		public boolean is(Holder<T> holder) {
			return holder.kind() == Kind.DIRECT ? value().equals(holder.value()) : holder.is(resourceKey);
		}

		@Override
		public Stream<TagKey<T>> tags() {
			return reference != null ? reference.tags() : Stream.empty();
		}

		@Override
		public Either<ResourceKey<T>, T> unwrap() {
			return Either.left(resourceKey);
		}

		@Override
		public Optional<ResourceKey<T>> unwrapKey() {
			return Optional.of(resourceKey);
		}

		@Override
		public Kind kind() {
			return Kind.REFERENCE;
		}

		@Override
		public boolean canSerializeIn(HolderOwner<T> owner) {
			return reference != null && reference.canSerializeIn(owner);
		}

		@Override
		public String toString() {
			return "HolderProxy{" +
					"resourceKey=" + resourceKey +
					",directValue=" + directValue +
					", value=" + (directValue || reference != null ? value() : "(not initialized)") +
					", reference=" + reference +
					'}';
		}
	}
}
