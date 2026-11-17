/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.common.impl.corporea;

import org.violetmoon.botania.api.corporea.CorporeaHelper;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public final class DefaultCorporeaMatchers {
	public static void init() {
		CorporeaHelper.instance().registerRequestMatcher(botaniaRL("string"), CorporeaStringMatcher.class, CorporeaStringMatcher::createFromNBT);
		CorporeaHelper.instance().registerRequestMatcher(botaniaRL("item_stack"), CorporeaItemStackMatcher.class, CorporeaItemStackMatcher::createFromNBT);
	}

	private DefaultCorporeaMatchers() {}
}
