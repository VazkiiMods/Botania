/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.mana.TileManaDetector;

import javax.annotation.Nonnull;

public class BlockManaDetector extends BlockMod implements IManaCollisionGhost, BlockEntityProvider {

	public BlockManaDetector(Settings builder) {
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
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockView world, @Nonnull BlockPos pos,
			ShapeContext context) {
		if (context.getEntity() instanceof IManaBurst) {
			return VoxelShapes.empty();
		} else {
			return super.getCollisionShape(state, world, pos, context);
		}
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileManaDetector();
	}

	@Override
	public boolean isGhost(BlockState state, World world, BlockPos pos) {
		return true;
	}
}
