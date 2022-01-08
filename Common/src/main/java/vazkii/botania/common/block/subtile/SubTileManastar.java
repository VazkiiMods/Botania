/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.ModSubtiles;

public class SubTileManastar extends TileEntitySpecialFlower {
	private static final int SET_STATE_EVENT = 0;
	private static final int NONE = 0, DECREASING = 1, INCREASING = 2;

	private int lastMana = 0;
	private int state = NONE;

	public SubTileManastar(BlockPos pos, BlockState state) {
		super(ModSubtiles.MANASTAR, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide) {
			if (state != NONE && Math.random() > 0.6) {
				float r = state == INCREASING ? 0.05F : 1F;
				float b = state == INCREASING ? 1F : 0.05F;
				WispParticleData data = WispParticleData.wisp((float) Math.random() / 7, r, 0.05F, b, 1);
				level.addParticle(data, getEffectivePos().getX() + 0.55 + Math.random() * 0.2 - 0.1, getEffectivePos().getY() + 0.75 + Math.random() * 0.2 - 0.1, getEffectivePos().getZ() + 0.5, 0, (float) Math.random() / 50, 0);
			}
		} else {
			int mana = 0;
			for (Direction dir : Direction.Plane.HORIZONTAL) {
				BlockPos pos = getEffectivePos().relative(dir);
				if (getLevel().hasChunkAt(pos)) {
					BlockEntity tile = getLevel().getBlockEntity(pos);
					if (tile instanceof IManaPool) {
						mana += ((IManaPool) tile).getCurrentMana();
					}
				}
			}

			int newState = mana > lastMana ? INCREASING : mana < lastMana ? DECREASING : NONE;
			if (newState != state) {
				getLevel().blockEvent(getBlockPos(), getBlockState().getBlock(), SET_STATE_EVENT, newState);
			}

			if (ticksExisted % 60 == 0) {
				lastMana = mana;
			}
		}
	}

	@Override
	public boolean triggerEvent(int id, int param) {
		if (id == SET_STATE_EVENT) {
			state = param;
			return true;
		} else {
			return super.triggerEvent(id, param);
		}
	}

}
