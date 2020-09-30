/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.string;

import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringRelay;

import javax.annotation.Nonnull;

public class BlockRedStringRelay extends BlockRedString {

	public BlockRedStringRelay(Properties builder) {
		super(builder);
		setDefaultState(getDefaultState().with(BlockStateProperties.FACING, Direction.DOWN));
	}

	@Nonnull
	@Override
	public TileRedString createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileRedStringRelay();
	}

}
