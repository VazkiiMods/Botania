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

public final class EntityRegistrationNoContext<A> {
	private final Provider<A> provider;
	private final EntityType<?> @Nullable [] entities;
	private final @Nullable Predicate<Entity> predicate;

	private EntityRegistrationNoContext(Provider<A> provider, EntityType<?> @Nullable [] entities,
			@Nullable Predicate<Entity> predicate) {

		this.provider = provider;
		this.entities = entities;
		this.predicate = predicate;
	}

	public static <A> EntityRegistrationNoContext<A> forEntities(Provider<A> provider, EntityType<?>... entities) {

		Objects.requireNonNull(provider);
		Objects.requireNonNull(entities);

		if (entities.length == 0) {
			throw new IllegalArgumentException("No entity types specified");
		}
		return new EntityRegistrationNoContext<>(provider, entities, null);
	}

	public static <A> EntityRegistrationNoContext<A> forEntityPredicate(Provider<A> provider,
			Predicate<Entity> predicate) {

		Objects.requireNonNull(provider);
		Objects.requireNonNull(predicate);

		return new EntityRegistrationNoContext<>(provider, null, predicate);
	}

	public void apply(
			BiConsumer<Provider<A>, EntityType<?>[]> listConsumer,
			BiConsumer<Provider<A>, Predicate<Entity>> predicateConsumer) {

		if (this.entities != null) {
			listConsumer.accept(this.provider, this.entities);
		} else if (this.predicate != null) {
			predicateConsumer.accept(this.provider, this.predicate);
		}
	}

	@FunctionalInterface
	public interface Provider<A> {
		@Nullable
		A getApi(Entity entity);

		@Nullable
		default <C> A getApi(Entity entity, @SuppressWarnings("unused") @Nullable C context) {
			return getApi(entity);
		}

		default EntityRegistrationNoContext.Provider<A> withPredicate(Predicate<Entity> predicate) {
			return (entity) -> {
				if (!predicate.test(entity)) {
					return null;
				}
				return Provider.this.getApi(entity);
			};
		}
	}
}
