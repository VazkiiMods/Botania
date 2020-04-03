/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileEnderEye;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockEnderEye extends BlockMod {

	protected BlockEnderEye(Properties builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState().with(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public boolean canProvidePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		return state.get(BlockStateProperties.POWERED) ? 15 : 0;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileEnderEye();
	}

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (state.get(BlockStateProperties.POWERED)) {
			for (int i = 0; i < 20; i++) {
				double x = pos.getX() - 0.1 + Math.random() * 1.2;
				double y = pos.getY() - 0.1 + Math.random() * 1.2;
				double z = pos.getZ() - 0.1 + Math.random() * 1.2;

				world.addParticle(RedstoneParticleData.REDSTONE_DUST, x, y, z, 0, 0, 0);
			}
		}
	}
}
