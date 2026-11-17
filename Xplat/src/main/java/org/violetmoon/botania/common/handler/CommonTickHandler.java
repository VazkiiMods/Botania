/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.handler;

import net.minecraft.server.level.ServerLevel;

import org.violetmoon.botania.api.corporea.CorporeaHelper;
import org.violetmoon.botania.common.impl.corporea.CorporeaHelperImpl;

public final class CommonTickHandler {

	private CommonTickHandler() {}

	public static void onTick(ServerLevel world) {
		((CorporeaHelperImpl) CorporeaHelper.instance()).clearCache();
	}

}
