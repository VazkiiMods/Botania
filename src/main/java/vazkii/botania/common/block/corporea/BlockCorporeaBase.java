/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 15, 2015, 12:23:33 AM (GMT)]
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;

import javax.annotation.Nonnull;

public abstract class BlockCorporeaBase extends BlockMod {

	public BlockCorporeaBase(Material material, String name) {
		super(material, name);
	}

	@Override
	public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);

		InventoryHelper.dropInventory(inv, world, state, pos);

		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
}
