/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api;

import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;

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
import vazkii.botania.common.BotaniaCapabilities;

import java.util.IdentityHashMap;
import java.util.Map;

public final class BotaniaForgeCapabilities {
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

	public static BotaniaCapabilities.ApiIdRegistration getRegistration() {
		return new BotaniaCapabilities.ApiIdRegistration() {
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

	private BotaniaForgeCapabilities() {}
}
