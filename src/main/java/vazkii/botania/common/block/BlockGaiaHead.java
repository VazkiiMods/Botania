/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.common.block.tile.TileGaiaHead;

import javax.annotation.Nonnull;

public class BlockGaiaHead extends SkullBlock {
	public static final SkullBlock.Type GAIA_TYPE = new SkullBlock.Type() {};

	public BlockGaiaHead(Properties builder) {
		super(GAIA_TYPE, builder);
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(BlockGetter world) {
		return new TileGaiaHead();
	}
}
