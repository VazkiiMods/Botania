/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.mana.TileRFGenerator;

import javax.annotation.Nonnull;

public class BlockRFGenerator extends BlockMod implements ITileEntityProvider {

	public BlockRFGenerator(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileRFGenerator();
	}

}
