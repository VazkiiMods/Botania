/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.neoforge;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import org.jetbrains.annotations.UnknownNullability;

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

public final class BotaniaNeoForgeCapabilities {
	private static final Map<ApiIdBlock<?>, BlockCapability<?, ?>> FOR_BLOCKS = new IdentityHashMap<>();
	private static final Map<ApiIdEntity<?>, EntityCapability<?, ?>> FOR_ENTITIES = new IdentityHashMap<>();
	private static final Map<ApiIdItem<?>, ItemCapability<?, ?>> FOR_ITEMS = new IdentityHashMap<>();

	public static <A> void registerBlockApiLookup(BlockApiNoContext<A> id) {
		registerBlockApiLookup(id, Void.class);
	}

	public static <A, C> void registerBlockApiLookup(BlockApiWithContext<A, C> id) {
		registerBlockApiLookup(id, id.getContextClass());
	}

	private static <A, C> void registerBlockApiLookup(ApiIdBlock<A> id, Class<C> contextClass) {
		if (FOR_BLOCKS.containsKey(id)) {
			throw new IllegalArgumentException("Block capability API ID is already registered: " + id.getId());
		}
		BlockCapability<A, C> lookup = BlockCapability.create(id.getId(), id.getApiClass(), contextClass);
		FOR_BLOCKS.put(id, lookup);
	}

	public static <A> void registerEntityApiLookup(EntityApiNoContext<A> id) {
		registerForEntity(id, Void.class);
	}

	public static <A, C> void registerEntityApiLookup(EntityApiWithContext<A, C> id) {
		registerForEntity(id, id.getContextClass());
	}

	private static <A, C> void registerForEntity(ApiIdEntity<A> id, Class<C> contextClass) {
		if (FOR_ENTITIES.containsKey(id)) {
			throw new IllegalArgumentException("Entity capability API ID is already registered: " + id.getId());
		}
		EntityCapability<A, C> lookup = EntityCapability.create(id.getId(), id.getApiClass(), contextClass);
		FOR_ENTITIES.put(id, lookup);
	}

	public static <A> void registerItemApiLookup(ItemApiNoContext<A> id) {
		registerForItem(id, Void.class);
	}

	public static <A, C> void registerItemApiLookup(ItemApiWithContext<A, C> id) {
		registerForItem(id, id.getContextClass());
	}

	private static <A, C> void registerForItem(ApiIdItem<A> id, Class<C> contextClass) {
		if (FOR_ITEMS.containsKey(id)) {
			throw new IllegalArgumentException("Item capability API ID is already registered: " + id.getId());
		}
		ItemCapability<A, C> lookup = ItemCapability.create(id.getId(), id.getApiClass(), contextClass);
		FOR_ITEMS.put(id, lookup);
	}

	@SuppressWarnings("unchecked")
	public static <A> BlockCapability<A, @UnknownNullability Void> getBlockApiLookupById(BlockApiNoContext<A> id) {
		return (BlockCapability<A, Void>) FOR_BLOCKS.get(id);
	}

	@SuppressWarnings("unchecked")
	public static <A, C> BlockCapability<A, @UnknownNullability C> getBlockApiLookupById(BlockApiWithContext<A, C> id) {
		return (BlockCapability<A, C>) FOR_BLOCKS.get(id);
	}

	@SuppressWarnings("unchecked")
	public static <A> EntityCapability<A, @UnknownNullability Void> getEntityApiLookupById(EntityApiNoContext<A> id) {
		return (EntityCapability<A, Void>) FOR_ENTITIES.get(id);
	}

	@SuppressWarnings("unchecked")
	public static <A, C> EntityCapability<A, @UnknownNullability C> getEntityApiLookupById(EntityApiWithContext<A, C> id) {
		return (EntityCapability<A, C>) FOR_ENTITIES.get(id);
	}

	@SuppressWarnings("unchecked")
	public static <A> ItemCapability<A, @UnknownNullability Void> getItemApiLookupById(ItemApiNoContext<A> id) {
		return (ItemCapability<A, Void>) FOR_ITEMS.get(id);
	}

	@SuppressWarnings("unchecked")
	public static <A, C> ItemCapability<A, @UnknownNullability C> getItemApiLookupById(ItemApiWithContext<A, C> id) {
		return (ItemCapability<A, C>) FOR_ITEMS.get(id);
	}

	public static ApiIdRegistration getRegistration() {
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

	public static ApiProviderRegistration getProviderRegistration(RegisterCapabilitiesEvent e) {
		return new ApiProviderRegistration() {
			@Override
			public <A> void register(BlockApiNoContext<A> apiId,
					Iterable<BlockRegistrationNoContext<A>> registrations) {

				BlockCapability<A, Void> capability = getBlockApiLookupById(apiId);
				for (var registration : registrations) {
					registration.apply(
							(provider, blockEntityTypes) -> {
								for (var blockEntityType : blockEntityTypes) {
									e.registerBlockEntity(capability, blockEntityType, provider::getApi);
								}
							},
							(provider, blockEntityPredicate) -> {
								// This is the recommended way in NeoForge:
								for (BlockEntityType<?> blockEntityType : BuiltInRegistries.BLOCK_ENTITY_TYPE) {
									e.registerBlockEntity(capability, blockEntityType,
											provider.withPredicate(blockEntityPredicate)::getApi);
								}
							},
							(provider, blocks) -> e.registerBlock(capability, provider::getApi, blocks),
							(provider, blockPredicate) -> {
								Block[] blocks = BuiltInRegistries.BLOCK.stream().filter(blockPredicate).toArray(Block[]::new);
								if (blocks.length > 0) {
									e.registerBlock(capability, provider::getApi, blocks);
								}
							}
					);
				}
			}

			@Override
			public <A, C> void register(BlockApiWithContext<A, C> apiId,
					Iterable<BlockRegistrationWithContext<A, C>> registrations) {

				BlockCapability<A, C> capability = getBlockApiLookupById(apiId);
				for (var registration : registrations) {
					registration.apply(
							(provider, blockEntityTypes) -> {
								for (var blockEntityType : blockEntityTypes) {
									e.registerBlockEntity(capability, blockEntityType, provider::getApi);
								}
							},
							(provider, blockEntityPredicate) -> {
								// This is the recommended way in NeoForge:
								for (BlockEntityType<?> blockEntityType : BuiltInRegistries.BLOCK_ENTITY_TYPE) {
									e.registerBlockEntity(capability, blockEntityType,
											provider.withPredicate(blockEntityPredicate)::getApi);
								}
							},
							(provider, blocks) -> e.registerBlock(capability, provider::getApi, blocks),
							(provider, blockPredicate) -> {
								Block[] blocks = BuiltInRegistries.BLOCK.stream().filter(blockPredicate).toArray(Block[]::new);
								if (blocks.length > 0) {
									e.registerBlock(capability, provider::getApi, blocks);
								}
							}
					);
				}
			}

			@Override
			public <A> void register(EntityApiNoContext<A> apiId,
					Iterable<EntityRegistrationNoContext<A>> registrations) {

				EntityCapability<A, Void> capability = BotaniaNeoForgeCapabilities.getEntityApiLookupById(apiId);
				for (EntityRegistrationNoContext<A> registration : registrations) {
					registration.apply(
							(provider, entityTypes) -> {
								for (EntityType<?> entityType : entityTypes) {
									e.registerEntity(capability, entityType, provider::getApi);
								}
							},
							(provider, predicate) -> {
								// This is the recommended way in NeoForge, and it's not particularly efficient; see
								// net.neoforged.neoforge.capabilities.CapabilityHooks::registerFallbackVanillaProviders
								for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
									e.registerEntity(capability, entityType, provider.withPredicate(predicate)::getApi);
								}
							}
					);
				}
			}

			@Override
			public <A, C> void register(EntityApiWithContext<A, C> apiId,
					Iterable<EntityRegistrationWithContext<A, C>> registrations) {

				EntityCapability<A, C> capability = BotaniaNeoForgeCapabilities.getEntityApiLookupById(apiId);
				for (EntityRegistrationWithContext<A, C> registration : registrations) {
					registration.apply(
							(provider, entityTypes) -> {
								for (EntityType<?> entityType : entityTypes) {
									e.registerEntity(capability, entityType, provider::getApi);
								}
							},
							(provider, predicate) -> {
								// This is the recommended way in NeoForge, and it's not particularly efficient; see
								// net.neoforged.neoforge.capabilities.CapabilityHooks::registerFallbackVanillaProviders
								for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
									e.registerEntity(capability, entityType, provider.withPredicate(predicate)::getApi);
								}
							}
					);
				}
			}

			@Override
			public <A> void register(ItemApiNoContext<A> apiId,
					Iterable<ItemRegistrationNoContext<A>> registrations) {

				ItemCapability<A, Void> capability = BotaniaNeoForgeCapabilities.getItemApiLookupById(apiId);
				for (ItemRegistrationNoContext<A> registration : registrations) {
					registration.apply(
							(provider, items) -> e.registerItem(capability, provider::getApi, items)
					);
				}
			}

			@Override
			public <A, C> void register(ItemApiWithContext<A, C> apiId,
					Iterable<ItemRegistrationWithContext<A, C>> registrations) {
				ItemCapability<A, C> capability = BotaniaNeoForgeCapabilities.getItemApiLookupById(apiId);
				for (ItemRegistrationWithContext<A, C> registration : registrations) {
					registration.apply(
							(provider, items) -> e.registerItem(capability, provider::getApi, items)
					);
				}
			}
		};
	}

	private BotaniaNeoForgeCapabilities() {}
}
