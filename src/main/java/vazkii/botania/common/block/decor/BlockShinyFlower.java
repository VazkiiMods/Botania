/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.BlockState;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import vazkii.botania.common.block.BlockModFlower;

import javax.annotation.Nonnull;

public class BlockShinyFlower extends BlockModFlower {

	public BlockShinyFlower(DyeColor color, Settings builder) {
		super(color, builder);
	}

	@Override
	public boolean isFertilizable(@Nonnull BlockView world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean fuckifiknow) {
		return false;
	}

}
