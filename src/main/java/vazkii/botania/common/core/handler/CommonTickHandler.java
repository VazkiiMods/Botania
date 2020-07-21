/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;


import net.minecraft.server.world.ServerWorld;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.impl.corporea.CorporeaHelperImpl;

public final class CommonTickHandler {

	private CommonTickHandler() {}

	public static void onTick(ServerWorld world) {
		((CorporeaHelperImpl) CorporeaHelper.instance()).clearCache();
	}

}
