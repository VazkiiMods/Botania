/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.fabric;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.util.Unit;

import vazkii.botania.api.capability.ApiIdBlock;
import vazkii.botania.api.capability.ApiIdEntity;
import vazkii.botania.api.capability.ApiIdItem;
import vazkii.botania.api.capability.BlockApiNoContext;
import vazkii.botania.api.capability.BlockApiWithContext;
import vazkii.botania.api.capability.EntityApiNoContext;
import vazkii.botania.api.capability.EntityApiWithContext;
import vazkii.botania.api.capability.ItemApiNoContext;
import vazkii.botania.api.capability.ItemApiWithContext;
import vazkii.botania.api.capability.registration.ApiIdRegistration;
import vazkii.botania.api.capability.registration.ApiProviderRegistration;
import vazkii.botania.api.capability.registration.BlockRegistrationNoContext;
import vazkii.botania.api.capability.registration.BlockRegistrationWithContext;
import vazkii.botania.api.capability.registration.EntityRegistrationNoContext;
import vazkii.botania.api.capability.registration.EntityRegistrationWithContext;
import vazkii.botania.api.capability.registration.ItemRegistrationNoContext;
import vazkii.botania.api.capability.registration.ItemRegistrationWithContext;

import java.util.IdentityHashMap;
import java.util.Map;

public final class BotaniaFabricCapabilities {
	private static final Map<ApiIdBlock<?>, BlockApiLookup<?, ?>> FOR_BLOCKS = new IdentityHashMap<>();
	private static final Map<ApiIdEntity<?>, EntityApiLookup<?, ?>> FOR_ENTITIES = new IdentityHashMap<>();
	private static final Map<ApiIdItem<?>, ItemApiLookup<?, ?>> FOR_ITEMS = new IdentityHashMap<>();

	public static <A> void registerBlockApiLookup(BlockApiNoContext<A> id) {
		registerBlockApiLookup(id, Unit.class);
	}

	public static <A, C> void registerBlockApiLookup(BlockApiWithContext<A, C> id) {
		registerBlockApiLookup(id, id.getContextClass());
	}

	private static <A, C> void registerBlockApiLookup(ApiIdBlock<A> id, Class<C> contextClass) {
		if (FOR_BLOCKS.containsKey(id)) {
			throw new IllegalArgumentException("Block capability API ID is already registered: " + id.getId());
		}
		BlockApiLookup<A, C> lookup = BlockApiLookup.get(id.getId(), id.getApiClass(), contextClass);
		FOR_BLOCKS.put(id, lookup);
	}

	public static <A> void registerEntityApiLookup(EntityApiNoContext<A> id) {
		registerForEntity(id, Unit.class);
	}

	public static <A, C> void registerEntityApiLookup(EntityApiWithContext<A, C> id) {
		registerForEntity(id, id.getContextClass());
	}

	private static <A, C> void registerForEntity(ApiIdEntity<A> id, Class<C> contextClass) {
		if (FOR_ENTITIES.containsKey(id)) {
			throw new IllegalArgumentException("Entity capability API ID is already registered: " + id.getId());
		}
		EntityApiLookup<A, C> lookup = EntityApiLookup.get(id.getId(), id.getApiClass(), contextClass);
		FOR_ENTITIES.put(id, lookup);
	}

	public static <A> void registerItemApiLookup(ItemApiNoContext<A> id) {
		registerForItem(id, Unit.class);
	}

	public static <A, C> void registerItemApiLookup(ItemApiWithContext<A, C> id) {
		registerForItem(id, id.getContextClass());
	}

	private static <A, C> void registerForItem(ApiIdItem<A> id, Class<C> contextClass) {
		if (FOR_ITEMS.containsKey(id)) {
			throw new IllegalArgumentException("Item capability API ID is already registered: " + id.getId());
		}
		ItemApiLookup<A, C> lookup = ItemApiLookup.get(id.getId(), id.getApiClass(), contextClass);
		FOR_ITEMS.put(id, lookup);
	}

	@SuppressWarnings("unchecked")
	public static <A> BlockApiLookup<A, Unit> getBlockApiLookupById(BlockApiNoContext<A> id) {
		return (BlockApiLookup<A, Unit>) FOR_BLOCKS.get(id);
	}

	@SuppressWarnings("unchecked")
	public static <A, C> BlockApiLookup<A, C> getBlockApiLookupById(BlockApiWithContext<A, C> id) {
		return (BlockApiLookup<A, C>) FOR_BLOCKS.get(id);
	}

	@SuppressWarnings("unchecked")
	public static <A> EntityApiLookup<A, Unit> getEntityApiLookupById(EntityApiNoContext<A> id) {
		return (EntityApiLookup<A, Unit>) FOR_ENTITIES.get(id);
	}

	@SuppressWarnings("unchecked")
	public static <A, C> EntityApiLookup<A, C> getEntityApiLookupById(EntityApiWithContext<A, C> id) {
		return (EntityApiLookup<A, C>) FOR_ENTITIES.get(id);
	}

	@SuppressWarnings("unchecked")
	public static <A> ItemApiLookup<A, Unit> getItemApiLookupById(ItemApiNoContext<A> id) {
		return (ItemApiLookup<A, Unit>) FOR_ITEMS.get(id);
	}

	@SuppressWarnings("unchecked")
	public static <A, C> ItemApiLookup<A, C> getItemApiLookupById(ItemApiWithContext<A, C> id) {
		return (ItemApiLookup<A, C>) FOR_ITEMS.get(id);
	}

	public static ApiIdRegistration getLookupRegistration() {
		return new ApiIdRegistration() {
			@Override
			public void register(BlockApiNoContext<?> apiId) {
				registerBlockApiLookup(apiId);
			}

			@Override
			public void register(BlockApiWithContext<?, ?> apiId) {
				registerBlockApiLookup(apiId);
			}

			@Override
			public void register(EntityApiNoContext<?> apiId) {
				registerEntityApiLookup(apiId);
			}

			@Override
			public void register(EntityApiWithContext<?, ?> apiId) {
				registerEntityApiLookup(apiId);
			}

			@Override
			public void register(ItemApiNoContext<?> apiId) {
				registerItemApiLookup(apiId);
			}

			@Override
			public void register(ItemApiWithContext<?, ?> apiId) {
				registerItemApiLookup(apiId);
			}
		};
	}

	public static ApiProviderRegistration getProviderRegistration() {
		return new ApiProviderRegistration() {
			@Override
			public <A> void register(BlockApiNoContext<A> apiId,
					Iterable<BlockRegistrationNoContext<A>> registrations) {
				BlockApiLookup<A, Unit> lookup = getBlockApiLookupById(apiId);
				for (BlockRegistrationNoContext<A> registration : registrations) {
					registration.apply(
							(provider, blockEntityTypes) -> lookup.registerForBlockEntities(
									provider::getApi,
									blockEntityTypes
							),
							(provider, blockEntityPredicate) -> lookup.registerFallback(
									(world, pos, state, blockEntity, context) -> blockEntity != null
											&& blockEntityPredicate.test(blockEntity)
													? provider.getApi(blockEntity)
													: null
							),
							(provider, blocks) -> lookup.registerForBlocks(provider::getApi, blocks),
							(provider, blockPredicate) -> lookup.registerFallback(
									provider.withPredicate(blockPredicate)::getApi
							)
					);
				}
			}

			@Override
			public <A, C> void register(BlockApiWithContext<A, C> apiId,
					Iterable<BlockRegistrationWithContext<A, C>> registrations) {
				BlockApiLookup<A, C> lookup = getBlockApiLookupById(apiId);
				for (BlockRegistrationWithContext<A, C> registration : registrations) {
					registration.apply(
							(provider, blockEntityTypes) -> lookup.registerForBlockEntities(
									provider::getApi,
									blockEntityTypes
							),
							(provider, blockEntityPredicate) -> lookup.registerFallback(
									(world, pos, state, blockEntity, context) -> blockEntity != null
											&& blockEntityPredicate.test(blockEntity)
													? provider.getApi(blockEntity, context)
													: null
							),
							(provider, blocks) -> lookup.registerForBlocks(provider::getApi, blocks),
							(provider, blockPredicate) -> lookup.registerFallback(
									provider.withPredicate(blockPredicate)::getApi
							)
					);
				}
			}

			@Override
			public <A> void register(EntityApiNoContext<A> apiId,
					Iterable<EntityRegistrationNoContext<A>> registrations) {
				EntityApiLookup<A, Unit> lookup = getEntityApiLookupById(apiId);
				for (EntityRegistrationNoContext<A> registration : registrations) {
					registration.apply(
							(provider, entityTypes) -> lookup.registerForTypes(
									provider::getApi,
									entityTypes),
							(provider, predicate) -> lookup.registerFallback(
									provider.withPredicate(predicate)::getApi)
					);
				}
			}

			@Override
			public <A, C> void register(EntityApiWithContext<A, C> apiId,
					Iterable<EntityRegistrationWithContext<A, C>> registrations) {
				EntityApiLookup<A, C> lookup = getEntityApiLookupById(apiId);
				for (EntityRegistrationWithContext<A, C> registration : registrations) {
					registration.apply(
							(provider, entityTypes) -> lookup.registerForTypes(
									provider::getApi,
									entityTypes),
							(provider, predicate) -> lookup.registerFallback(
									provider.withPredicate(predicate)::getApi)
					);
				}
			}

			@Override
			public <A> void register(ItemApiNoContext<A> apiId,
					Iterable<ItemRegistrationNoContext<A>> registrations) {
				ItemApiLookup<A, Unit> lookup = getItemApiLookupById(apiId);
				for (ItemRegistrationNoContext<A> registration : registrations) {
					registration.apply(
							(provider, items) -> lookup.registerForItems(provider::getApi, items)
					);
				}
			}

			@Override
			public <A, C> void register(ItemApiWithContext<A, C> apiId,
					Iterable<ItemRegistrationWithContext<A, C>> registrations) {
				ItemApiLookup<A, C> lookup = getItemApiLookupById(apiId);
				for (ItemRegistrationWithContext<A, C> registration : registrations) {
					registration.apply(
							(provider, items) -> lookup.registerForItems(provider::getApi, items)
					);
				}
			}
		};
	}

	private BotaniaFabricCapabilities() {}
}
