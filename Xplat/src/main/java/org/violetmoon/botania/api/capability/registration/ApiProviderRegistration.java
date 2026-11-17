/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.capability.registration;

import org.jetbrains.annotations.ApiStatus;

import vazkii.botania.api.capability.BlockApiNoContext;
import vazkii.botania.api.capability.BlockApiWithContext;
import vazkii.botania.api.capability.EntityApiNoContext;
import vazkii.botania.api.capability.EntityApiWithContext;
import vazkii.botania.api.capability.ItemApiNoContext;
import vazkii.botania.api.capability.ItemApiWithContext;

@ApiStatus.NonExtendable
public interface ApiProviderRegistration {
	// blocks
	<A> void register(BlockApiNoContext<A> apiId, Iterable<BlockRegistrationNoContext<A>> registrations);
	<A, C> void register(BlockApiWithContext<A, C> apiId, Iterable<BlockRegistrationWithContext<A, C>> registrations);

	// entities
	<A> void register(EntityApiNoContext<A> apiId, Iterable<EntityRegistrationNoContext<A>> registrations);
	<A, C> void register(EntityApiWithContext<A, C> apiId, Iterable<EntityRegistrationWithContext<A, C>> registrations);

	// items
	<A> void register(ItemApiNoContext<A> apiId, Iterable<ItemRegistrationNoContext<A>> registrations);
	<A, C> void register(ItemApiWithContext<A, C> apiId, Iterable<ItemRegistrationWithContext<A, C>> registrations);
}
