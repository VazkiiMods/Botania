/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.ManaTrigger;
import vazkii.botania.common.block.BotaniaWaterloggedBlock;
import vazkii.botania.common.handler.BotaniaSounds;

public abstract class DrumBlock extends BotaniaWaterloggedBlock {

	private static final VoxelShape SHAPE = Block.box(3, 1, 3, 13, 15, 13);

	protected DrumBlock(Properties builder) {
		super(builder);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	public abstract void activate(Level level, BlockPos pos);

	public static class ManaTriggerImpl implements ManaTrigger {
		private final Level world;
		private final BlockPos pos;
		private final DrumBlock drumBlock;

		public ManaTriggerImpl(Level world, BlockPos pos, BlockState state) {
			this.world = world;
			this.pos = pos;
			this.drumBlock = ((DrumBlock) state.getBlock());
		}

		@Override
		public void onBurstCollision(ManaBurst burst) {
			if (burst.isFake()) {
				return;
			}
			if (world.isClientSide) {
				world.addParticle(ParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5D, 1.0 / 24.0, 0, 0);
				return;
			} else if (burst.entity().getOwner() instanceof ServerPlayer player
					&& player.blockActionRestricted(world, pos, player.gameMode.getGameModeForPlayer())) {
				return;
			}
			drumBlock.activate(world, pos);

			for (int i = 0; i < 10; i++) {
				world.playSound(null, pos, BotaniaSounds.DRUM, SoundSource.BLOCKS, 1F, 1F);
			}
		}
	}
}
