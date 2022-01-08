/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class TileManaFlame extends TileMod {
	private static final String TAG_COLOR = "color";

	private int color = 0x20FF20;

	public TileManaFlame(BlockPos pos, BlockState state) {
		super(ModTiles.MANA_FLAME, pos, state);
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putInt(TAG_COLOR, color);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		color = cmp.getInt(TAG_COLOR);
	}

}
