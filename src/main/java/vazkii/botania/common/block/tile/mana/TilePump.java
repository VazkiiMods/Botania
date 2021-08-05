/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileMod;

public class TilePump extends TileMod implements TickableBlockEntity {
	private static final String TAG_ACTIVE = "active";

	public float innerRingPos;
	private boolean active = false;
	public boolean hasCart = false;
	public boolean hasCartOnTop = false;
	public float moving = 0F;

	public int comparator;
	public boolean hasRedstone = false;
	private int lastComparator = 0;

	public TilePump(BlockPos pos, BlockState state) {
		super(ModTiles.PUMP, pos, state);
	}

	@Override
	public void tick() {
		hasRedstone = level.hasNeighborSignal(worldPosition);

		float max = 8F;
		float min = 0F;

		float incr = max / 10F;

		if (innerRingPos < max && active && moving >= 0F) {
			innerRingPos += incr;
			moving = incr;
			if (innerRingPos >= max) {
				innerRingPos = Math.min(max, innerRingPos);
				moving = 0F;
				for (int x = 0; x < 2; x++) {
					level.addParticle(ParticleTypes.SMOKE, getBlockPos().getX() + Math.random(), getBlockPos().getY() + Math.random(), getBlockPos().getZ() + Math.random(), 0, 0, 0);
				}
			}
		} else if (innerRingPos > min) {
			innerRingPos -= incr * 2;
			moving = -incr * 2;
			if (innerRingPos <= min) {
				innerRingPos = Math.max(min, innerRingPos);
				moving = 0F;
			}
		}

		if (!hasCartOnTop) {
			comparator = 0;
		}
		if (!hasCart && active) {
			setActive(false);
		}
		if (active && hasRedstone) {
			setActive(false);
		}

		hasCart = false;
		hasCartOnTop = false;

		if (comparator != lastComparator) {
			level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
		}
		lastComparator = comparator;
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putBoolean(TAG_ACTIVE, active);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		active = cmp.getBoolean(TAG_ACTIVE);
	}

	public void setActive(boolean active) {
		if (!level.isClientSide) {
			boolean diff = this.active != active;
			this.active = active;
			if (diff) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			}
		}
	}
}
