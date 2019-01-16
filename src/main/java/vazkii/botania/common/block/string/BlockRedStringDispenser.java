/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 14, 2014, 10:59:20 PM (GMT)]
 */
package vazkii.botania.common.block.string;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringDispenser;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockRedStringDispenser extends BlockRedString {

	public BlockRedStringDispenser() {
		super(LibBlockNames.RED_STRING_DISPENSER);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.FACING, EnumFacing.DOWN).withProperty(BotaniaStateProps.POWERED, false));
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.FACING, BotaniaStateProps.POWERED);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(BotaniaStateProps.FACING).getIndex();
		if (state.getValue(BotaniaStateProps.POWERED)) {
			meta |= 8;
		} else {
			meta &= -9;
		}
		return meta;
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean powered = (meta & 8) != 0;
		meta &= -9;
		EnumFacing facing = EnumFacing.byIndex(meta);
		return getDefaultState().withProperty(BotaniaStateProps.FACING, facing).withProperty(BotaniaStateProps.POWERED, powered);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		boolean power = world.getRedstonePowerFromNeighbors(pos) > 0 || world.getRedstonePowerFromNeighbors(pos.up()) > 0;
		boolean powered = state.getValue(BotaniaStateProps.POWERED);

		if(power && !powered) {
			((TileRedStringDispenser) world.getTileEntity(pos)).tickDispenser();
			world.setBlockState(pos, state.withProperty(BotaniaStateProps.POWERED, true), 4);
		} else if(!power && powered)
			world.setBlockState(pos, state.withProperty(BotaniaStateProps.POWERED, false), 4);
	}

	@Nonnull
	@Override
	public TileRedString createTileEntity(@Nonnull World world, @Nonnull IBlockState meta) {
		return new TileRedStringDispenser();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(BotaniaStateProps.POWERED).build());
		super.registerModels();
	}

}
