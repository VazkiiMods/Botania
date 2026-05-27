/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.flower.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.ManastarState;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;

public class ManastarBlockEntity extends SpecialFlowerBlockEntity {

	private int lastMana = -1;

	public ManastarBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.MANASTAR, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		var state = getBlockState().getValue(BotaniaStateProperties.MANASTAR_STATE);

		if (getLevel().isClientSide()) {
			if (state != ManastarState.NEUTRAL && Math.random() > 0.6) {
				float r = state == ManastarState.INCREASING ? 0.05F : 1F;
				float b = state == ManastarState.INCREASING ? 1F : 0.05F;
				WispParticleData data = WispParticleData.wisp((float) Math.random() / 7, r, 0.05F, b, 1);
				emitParticle(data,
						0.5 + Math.random() * 0.25 - 0.125,
						0.75 + Math.random() * 0.2 - 0.1,
						0.5 + Math.random() * 0.25 - 0.125,
						0, (float) Math.random() / 50, 0);
			}
		} else if (shouldUpdateThisTick() || lastMana < 0) {
			int mana = 0;
			for (Direction dir : Direction.Plane.HORIZONTAL) {
				BlockPos pos = getEffectivePos().relative(dir);
				if (getLevel().hasChunkAt(pos)) {
					var receiver = ManaReceiver.LOOKUP.find(getLevel(), pos, dir.getOpposite());
					if (receiver instanceof ManaPool pool) {
						mana += pool.getCurrentMana();
					}
				}
			}

			if (lastMana < 0) {
				lastMana = mana;
			}

			ManastarState newState = switch (Mth.sign(mana - lastMana)) {
				case -1 -> ManastarState.DECREASING;
				case +1 -> ManastarState.INCREASING;
				default -> ManastarState.NEUTRAL;
			};
			if (newState != state) {
				getLevel().setBlock(getBlockPos(),
						getBlockState().setValue(BotaniaStateProperties.MANASTAR_STATE, newState),
						Block.UPDATE_CLIENTS);
			}

			if (shouldTick(level.getGameTime(), 60)) {
				lastMana = mana;
			}
		}
	}

	@Override
	protected int getUpdateInterval() {
		return 4;
	}

	@Nullable
	@Override
	public RadiusDescriptor getRadius() {
		// Manastar is the only flower to not have an AoE as such
		return null;
	}
}
