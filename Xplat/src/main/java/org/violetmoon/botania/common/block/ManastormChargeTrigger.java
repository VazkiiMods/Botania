/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.ManaTrigger;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.entity.ManaStormEntity;
import vazkii.botania.mixin.ProjectileAccessor;

public record ManastormChargeTrigger(Level level, BlockPos pos) implements ManaTrigger {

	@Override
	public void onBurstCollision(ManaBurst burst) {
		Projectile entity = burst.entity();
		if (!burst.isFake() && !level.isClientSide()
				&& (!(entity.getOwner() instanceof ServerPlayer player)
						|| !player.blockActionRestricted(level, pos, player.gameMode.getGameModeForPlayer()))
				&& level.destroyBlock(pos, false, entity)) {
			ManaStormEntity storm = BotaniaEntities.MANASTORM_EPICENTER.create(level);
			if (storm == null) {
				return;
			}
			storm.setOwner(entity.getOwner(), ((ProjectileAccessor) entity).botania_getOwnerUUID());
			storm.burstColor = burst.getColor();
			storm.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			level.addFreshEntity(storm);
		}
	}
}
