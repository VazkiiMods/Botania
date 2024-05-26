/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.mixin.GrowingPlantBodyBlockMixin;
import vazkii.botania.xplat.BotaniaConfig;

public class AgricarnationBlockEntity extends FunctionalFlowerBlockEntity {
	private static final int RANGE = 5;
	private static final int RANGE_MINI = 2;
	private static final float BONEMEAL_SUCCESS_CHANCE = 0.5f;

	protected AgricarnationBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public AgricarnationBlockEntity(BlockPos pos, BlockState state) {
		this(BotaniaFlowerBlocks.AGRICARNATION, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!(getLevel() instanceof ServerLevel serverLevel)) {
			return;
		}

		if (ticksExisted % 200 == 0) {
			sync();
		}

		if (ticksExisted % 6 == 0 && redstoneSignal == 0) {
			int range = getRange();
			int x = getEffectivePos().getX() + serverLevel.random.nextInt(range * 2 + 1) - range;
			int z = getEffectivePos().getZ() + serverLevel.random.nextInt(range * 2 + 1) - range;

			for (int i = 4; i > -2; i--) {
				int y = getEffectivePos().getY() + i;
				BlockPos pos = new BlockPos(x, y, z);
				BlockState state = serverLevel.getBlockState(pos);
				if (state.isAir()) {
					continue;
				}

				Block block = state.getBlock();
				if (block instanceof GrowingPlantBodyBlock) {
					var headPos = ((GrowingPlantBodyBlockMixin) block).botania_getHeadPos(serverLevel, pos, block);
					if (headPos.isPresent()) {
						pos = headPos.get();
					}
				}

				if (isPlant(serverLevel, pos, state, block) && getMana() > 5) {
					addMana(-5);
					if (state.is(BotaniaTags.Blocks.AGRICARNATION_APPLY_BONEMEAL)
							&& block instanceof BonemealableBlock bonemealableBlock
							&& bonemealableBlock.isValidBonemealTarget(serverLevel, pos, state, false)) {
						if (serverLevel.random.nextFloat() < BONEMEAL_SUCCESS_CHANCE
								&& bonemealableBlock.isBonemealSuccess(serverLevel, serverLevel.random, pos, state)) {
							bonemealableBlock.performBonemeal(serverLevel, serverLevel.random, pos, state);
						}
					} else {
						state.randomTick(serverLevel, pos, serverLevel.random);
					}
					if (BotaniaConfig.common().blockBreakParticles()) {
						serverLevel.levelEvent(LevelEvent.PARTICLES_PLANT_GROWTH, pos, 6 + serverLevel.random.nextInt(4));
					}
					serverLevel.playSound(null, x, y, z, BotaniaSounds.agricarnation, SoundSource.BLOCKS, 1F, 0.5F + (float) Math.random() * 0.5F);

					break;
				}
			}
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	/**
	 * @return Whether the block at {@code pos} grows "naturally". That is, whether its IGrowable action is simply
	 *         growing itself, instead of something like spreading around or creating flowers around, etc, and whether
	 *         this action would have happened normally over time without bonemeal.
	 */
	private boolean isPlant(Level level, BlockPos pos, BlockState state, Block block) {
		if (state.is(BotaniaTags.Blocks.AGRICARNATION_GROWTH_EXCLUDED)
				// grass/mycelium/nylium-like spreading blocks are excluded unless tagged otherwise
				|| (block instanceof SpreadingSnowyDirtBlock || block instanceof NyliumBlock)
						&& !state.is(BotaniaTags.Blocks.AGRICARNATION_GROWTH_CANDIDATE)) {
			return false;
		}

		boolean couldApplyBonemeal = block instanceof BonemealableBlock bonemealableBlock
				&& bonemealableBlock.isValidBonemealTarget(level, pos, state, level.isClientSide);

		boolean isTargetCandidate = couldApplyBonemeal
				|| block instanceof BushBlock
				|| state.is(BotaniaTags.Blocks.AGRICARNATION_GROWTH_CANDIDATE);
		boolean acceptsGrowthBoost = state.isRandomlyTicking()
				|| couldApplyBonemeal && state.is(BotaniaTags.Blocks.AGRICARNATION_APPLY_BONEMEAL);

		return isTargetCandidate && acceptsGrowthBoost;

	}

	@Override
	public int getColor() {
		return 0x8EF828;
	}

	@Override
	public int getMaxMana() {
		return 200;
	}

	public int getRange() {
		return RANGE;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), getRange());
	}

	public static class Mini extends AgricarnationBlockEntity {
		public Mini(BlockPos pos, BlockState state) {
			super(BotaniaFlowerBlocks.AGRICARNATION_CHIBI, pos, state);
		}

		@Override
		public int getRange() {
			return RANGE_MINI;
		}
	}

}
