/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 15, 2015, 12:49:58 AM (GMT)]
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.world.IBlockReader;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;

import javax.annotation.Nonnull;

public class BlockCorporeaIndex extends BlockCorporeaBase {
	public BlockCorporeaIndex(Block.Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public TileCorporeaBase createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileCorporeaIndex();
	}

	@Override
	public boolean isSolid(BlockState state) {
		return false;
	}
}
