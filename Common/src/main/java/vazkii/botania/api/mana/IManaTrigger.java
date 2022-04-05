/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import vazkii.botania.api.internal.IManaBurst;

/**
 * A Block or Block Entity with this capability will receive a callback when a burst
 * collides with it.
 */
public interface IManaTrigger {

	void onBurstCollision(IManaBurst burst);

}
