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

import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringFertilizer;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.Random;

public class BlockRedStringFertilizer extends BlockRedString implements IGrowable {

	public BlockRedStringFertilizer() {
		super(LibBlockNames.RED_STRING_FERTILIZER);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.FACING, EnumFacing.DOWN));
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
	public TileRedString createTileEntity(World world, IBlockState meta) {
		return new TileRedStringFertilizer();
	}
}
