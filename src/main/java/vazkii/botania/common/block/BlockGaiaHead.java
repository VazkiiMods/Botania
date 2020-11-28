/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

import vazkii.botania.common.block.tile.TileGaiaHead;

import javax.annotation.Nonnull;

public class BlockGaiaHead extends SkullBlock {
	public static final SkullBlock.SkullType GAIA_TYPE = new SkullBlock.SkullType() {};

	public BlockGaiaHead(Settings builder) {
		super(GAIA_TYPE, builder);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileGaiaHead();
	}
}
