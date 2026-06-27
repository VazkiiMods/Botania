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
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.ManaTrigger;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.entity.ManaStormEntity;
import vazkii.botania.mixin.ProjectileAccessor;

public class ManastormChargeBlock extends BotaniaBlock {

	public ManastormChargeBlock(Properties builder) {
		super(builder);
	}

	public static class ManaTriggerImpl implements ManaTrigger {
		private final Level world;
		private final BlockPos pos;

		public ManaTriggerImpl(Level world, BlockPos pos) {
			this.world = world;
			this.pos = pos;
		}

		@Override
		public void onBurstCollision(ManaBurst burst) {
			Projectile entity = burst.entity();
			if (!burst.isFake() && !world.isClientSide && world.destroyBlock(pos, false, entity)) {
				ManaStormEntity storm = BotaniaEntities.MANA_STORM.create(world);
				if (storm == null) {
					return;
				}
				storm.setOwner(entity.getOwner(), ((ProjectileAccessor) entity).botania_getOwnerUUID());
				storm.burstColor = burst.getColor();
				storm.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
				world.addFreshEntity(storm);
			}
		}
	}
}
