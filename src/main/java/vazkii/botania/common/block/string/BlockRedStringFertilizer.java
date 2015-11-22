/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 16, 2014, 7:10:46 PM (GMT)]
 */
package vazkii.botania.common.block.string;

import java.util.Random;

import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringFertilizer;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockRedStringFertilizer extends BlockRedString implements IGrowable {

	public BlockRedStringFertilizer() {
		super(LibBlockNames.RED_STRING_FERTILIZER);
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
		return ((TileRedStringFertilizer) world.getTileEntity(pos)).canGrow(world, isClient);
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
		return ((TileRedStringFertilizer) world.getTileEntity(pos)).canUseBonemeal(world, rand);
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		((TileRedStringFertilizer) world.getTileEntity(pos)).grow(world, rand);
	}

	@Override
	public TileRedString createNewTileEntity(World world, int meta) {
		return new TileRedStringFertilizer();
	}
}
