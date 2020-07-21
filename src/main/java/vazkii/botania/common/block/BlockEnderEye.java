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
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileEnderEye;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockEnderEye extends BlockMod implements BlockEntityProvider {

	protected BlockEnderEye(Settings builder) {
		super(builder);
		setDefaultState(getDefaultState().with(Properties.POWERED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.POWERED);
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction side) {
		return state.get(Properties.POWERED) ? 15 : 0;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileEnderEye();
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (state.get(Properties.POWERED)) {
			for (int i = 0; i < 20; i++) {
				double x = pos.getX() - 0.1 + Math.random() * 1.2;
				double y = pos.getY() - 0.1 + Math.random() * 1.2;
				double z = pos.getZ() - 0.1 + Math.random() * 1.2;

				world.addParticle(DustParticleEffect.RED, x, y, z, 0, 0, 0);
			}
		}
	}
}
