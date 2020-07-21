/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.common.block.tile.TileTinyPlanet;

import javax.annotation.Nonnull;

public class BlockTinyPlanet extends BlockModWaterloggable implements IManaCollisionGhost, BlockEntityProvider {

	private static final VoxelShape AABB = createCuboidShape(3, 3, 3, 13, 13, 13);

	protected BlockTinyPlanet(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return AABB;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileTinyPlanet();
	}

	@Override
	public boolean isGhost(BlockState state, World world, BlockPos pos) {
		return true;
	}
}
