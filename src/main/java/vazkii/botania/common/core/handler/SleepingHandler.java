/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.ModEntities;

import javax.annotation.Nullable;

public final class SleepingHandler {

	private SleepingHandler() {}

	@Nullable
	public static PlayerEntity.SleepFailureReason trySleep(PlayerEntity player) {
		World world = player.world;
		if (!world.isClient()) {
			boolean nearGuardian = ((ServerWorld) world).getEntitiesByType(ModEntities.DOPPLEGANGER, EntityPredicates.VALID_ENTITY)
					.stream()
					.anyMatch(e -> ((EntityDoppleganger) e).getPlayersAround().contains(player));

			if (nearGuardian) {
				return PlayerEntity.SleepFailureReason.NOT_SAFE;
			}
		}
		return null;
	}
}
