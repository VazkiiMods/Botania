/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;

import javax.annotation.Nonnull;

public class BlockCorporeaIndex extends BlockModWaterloggable implements EntityBlock {
	public BlockCorporeaIndex(BlockBehaviour.Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public TileCorporeaBase newBlockEntity(@Nonnull BlockGetter world) {
		return new TileCorporeaIndex();
	}
}
