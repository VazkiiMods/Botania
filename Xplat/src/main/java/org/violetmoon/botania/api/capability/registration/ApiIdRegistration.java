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
public interface ApiIdRegistration {
	// blocks
	void register(BlockApiNoContext<?> apiId);
	void register(BlockApiWithContext<?, ?> apiId);

	// entities
	void register(EntityApiNoContext<?> apiId);
	void register(EntityApiWithContext<?, ?> apiId);

	// items
	void register(ItemApiNoContext<?> apiId);
	void register(ItemApiWithContext<?, ?> apiId);
}
