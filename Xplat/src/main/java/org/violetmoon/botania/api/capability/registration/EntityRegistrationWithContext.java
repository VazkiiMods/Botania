/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.capability.registration;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public final class EntityRegistrationWithContext<A, C> {
	private final Provider<A, C> provider;
	private final EntityType<?> @Nullable [] entities;
	private final @Nullable Predicate<Entity> predicate;

	private EntityRegistrationWithContext(Provider<A, C> provider, EntityType<?> @Nullable [] entities,
			@Nullable Predicate<Entity> predicate) {
		this.provider = provider;
		this.entities = entities;
		this.predicate = predicate;
	}

	public static <A, C> EntityRegistrationWithContext<A, C> forEntities(Provider<A, C> provider,
			EntityType<?>... entities) {

		Objects.requireNonNull(provider);
		Objects.requireNonNull(entities);

		if (entities.length == 0) {
			throw new IllegalArgumentException("No entity types specified");
		}
		return new EntityRegistrationWithContext<>(provider, entities, null);
	}

	public static <A, C> EntityRegistrationWithContext<A, C> forEntityPredicate(Provider<A, C> provider,
			Predicate<Entity> predicate) {

		Objects.requireNonNull(provider);
		Objects.requireNonNull(predicate);

		return new EntityRegistrationWithContext<>(provider, null, predicate);
	}

	public void apply(
			BiConsumer<Provider<A, C>, EntityType<?>[]> listConsumer,
			BiConsumer<Provider<A, C>, Predicate<Entity>> predicateConsumer) {

		if (this.entities != null) {
			listConsumer.accept(this.provider, this.entities);
		} else if (this.predicate != null) {
			predicateConsumer.accept(this.provider, this.predicate);
		}
	}

	@FunctionalInterface
	public interface Provider<A, C> {
		@Nullable
		A getApi(Entity entity, C context);

		default Provider<A, C> withPredicate(Predicate<Entity> predicate) {
			return (entity, context) -> {
				if (!predicate.test(entity)) {
					return null;
				}
				return Provider.this.getApi(entity, context);
			};
		}
	}
}
