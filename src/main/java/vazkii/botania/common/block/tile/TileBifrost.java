/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import javax.annotation.Nonnull;

public class TileBifrost extends TileMod implements Tickable {
	private static final String TAG_TICKS = "ticks";

	public int ticks = 0;

	public TileBifrost() {
		super(ModTiles.BIFROST);
	}

	@Override
	public void tick() {
		if (!world.isClient) {
			if (ticks <= 0) {
				world.removeBlock(pos, false);
			} else {
				ticks--;
			}
		}
	}

	@Nonnull
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag ret = super.toTag(tag);
		ret.putInt(TAG_TICKS, ticks);
		return ret;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		ticks = tag.getInt(TAG_TICKS);
	}

}
