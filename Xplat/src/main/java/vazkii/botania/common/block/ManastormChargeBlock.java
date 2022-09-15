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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.ManaTrigger;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.entity.ManaStormEntity;

public class ManastormChargeBlock extends BotaniaBlock {

	public ManastormChargeBlock(Properties builder) {
		super(builder);
	}

	public static class ManaTriggerImpl implements ManaTrigger {
		private final Level world;
		private final BlockPos pos;
		private final BlockState state;

		public ManaTriggerImpl(Level world, BlockPos pos, BlockState state) {
			this.world = world;
			this.pos = pos;
			this.state = state;
		}

		@Override
		public void onBurstCollision(ManaBurst burst) {
			if (!burst.isFake() && !world.isClientSide) {
				world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
				world.removeBlock(pos, false);
				ManaStormEntity storm = BotaniaEntities.MANA_STORM.create(world);
				storm.burstColor = burst.getColor();
				storm.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
				world.addFreshEntity(storm);
			}
		}
	}
}
