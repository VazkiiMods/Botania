/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.tile.TileGaiaHead;

public class BlockGaiaHeadWall extends WallSkullBlock {
	public BlockGaiaHeadWall(Properties builder) {
		super(BlockGaiaHead.GAIA_TYPE, builder);
	}

	@NotNull
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileGaiaHead(pos, state);
	}
}
