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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.IPoolOverlayProvider;
import vazkii.botania.client.core.handler.MiscellaneousModels;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.BlockMod;

public class BlockManaVoid extends BlockMod implements IPoolOverlayProvider {
	private static final int SPARKLE_EVENT = 0;

	public BlockManaVoid(Properties builder) {
		super(builder);
	}

	@Override
	public ResourceLocation getIcon(Level world, BlockPos pos) {
		return MiscellaneousModels.INSTANCE.manaVoidOverlay.texture();
	}

	@Override
	public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int payload) {
		if (id == SPARKLE_EVENT) {
			if (level.isClientSide) {
				for (int i = 0; i < 10; i++) {
					SparkleParticleData data = SparkleParticleData.sparkle(0.7F + 0.5F * (float) Math.random(), 0.2F, 0.2F, 0.2F, 5);
					level.addParticle(data, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);
				}
			}
			return true;
		}
		return super.triggerEvent(state, level, pos, id, payload);
	}

	public static class ManaReceiver implements IManaReceiver {
		private final Level level;
		private final BlockPos pos;
		private final BlockState state;

		public ManaReceiver(Level level, BlockPos pos, BlockState state) {
			this.level = level;
			this.pos = pos;
			this.state = state;
		}

		@Override
		public Level getManaReceiverLevel() {
			return level;
		}

		@Override
		public BlockPos getManaReceiverPos() {
			return pos;
		}

		@Override
		public int getCurrentMana() {
			return 0;
		}

		@Override
		public boolean isFull() {
			return false;
		}

		@Override
		public void receiveMana(int mana) {
			if (mana > 0) {
				level.blockEvent(pos, state.getBlock(), SPARKLE_EVENT, 0);
			}
		}

		@Override
		public boolean canReceiveManaFromBursts() {
			return true;
		}
	}

}
