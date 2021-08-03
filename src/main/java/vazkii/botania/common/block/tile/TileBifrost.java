/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class TileBifrost extends TileMod implements TickableBlockEntity {
	private static final String TAG_TICKS = "ticks";

	public int ticks = 0;

	public TileBifrost() {
		super(ModTiles.BIFROST);
	}

	@Override
	public void tick() {
		if (!level.isClientSide) {
			if (ticks <= 0) {
				level.removeBlock(worldPosition, false);
			} else {
				ticks--;
			}
		}
	}

	@Nonnull
	@Override
	public CompoundTag save(CompoundTag tag) {
		CompoundTag ret = super.save(tag);
		ret.putInt(TAG_TICKS, ticks);
		return ret;
	}

	@Override
	public void load(BlockState state, CompoundTag tag) {
		super.load(state, tag);
		ticks = tag.getInt(TAG_TICKS);
	}

}
