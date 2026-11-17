/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.api.capability.registration;

import org.jetbrains.annotations.ApiStatus;

import org.violetmoon.botania.api.capability.BlockApiNoContext;
import org.violetmoon.botania.api.capability.BlockApiWithContext;
import org.violetmoon.botania.api.capability.EntityApiNoContext;
import org.violetmoon.botania.api.capability.EntityApiWithContext;
import org.violetmoon.botania.api.capability.ItemApiNoContext;
import org.violetmoon.botania.api.capability.ItemApiWithContext;

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
