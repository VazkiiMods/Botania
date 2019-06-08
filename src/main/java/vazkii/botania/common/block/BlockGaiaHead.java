/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 23, 2015, 11:44:35 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.SkullBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import vazkii.botania.common.block.tile.TileGaiaHead;

import javax.annotation.Nonnull;

public class BlockGaiaHead extends SkullBlock {
	public static final SkullBlock.ISkullType GAIA_TYPE = new SkullBlock.ISkullType() {};

	public BlockGaiaHead(Properties builder) {
		super(GAIA_TYPE, builder);
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileGaiaHead();
	}
}
