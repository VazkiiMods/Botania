/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import vazkii.botania.common.entity.ModEntities;

import javax.annotation.Nullable;

public final class SleepingHandler {

	private SleepingHandler() {}

	public static void init() {
		EntitySleepEvents.ALLOW_SLEEPING.register(SleepingHandler::trySleep);
	}

	@Nullable
	private static Player.BedSleepingProblem trySleep(Player player, BlockPos sleepPos) {
		Level world = player.level;
		if (!world.isClientSide()) {
			boolean nearGuardian = ((ServerLevel) world).getEntities(ModEntities.DOPPLEGANGER, EntitySelector.ENTITY_STILL_ALIVE)
					.stream()
					.anyMatch(e -> e.getPlayersAround().contains(player));

			if (nearGuardian) {
				return Player.BedSleepingProblem.NOT_SAFE;
			}
		}
		return null;
	}
}
