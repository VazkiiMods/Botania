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

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.common.entity.EntityManaStorm;
import vazkii.botania.common.entity.ModEntities;

public class BlockManaBomb extends BlockMod {

	public BlockManaBomb(Properties builder) {
		super(builder);
	}

	public static class ManaTrigger implements IManaTrigger {
		private final Level world;
		private final BlockPos pos;
		private final BlockState state;

		public ManaTrigger(Level world, BlockPos pos, BlockState state) {
			this.world = world;
			this.pos = pos;
			this.state = state;
		}

		@Override
		public void onBurstCollision(IManaBurst burst) {
			if (!burst.isFake() && !world.isClientSide) {
				world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
				world.removeBlock(pos, false);
				EntityManaStorm storm = ModEntities.MANA_STORM.create(world);
				storm.burstColor = burst.getColor();
				storm.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
				world.addFreshEntity(storm);
			}
		}
	}
}
