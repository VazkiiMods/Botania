/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.common.block.tile.TileTinyPlanet;

import javax.annotation.Nonnull;

public class BlockTinyPlanet extends BlockModWaterloggable implements IManaCollisionGhost {

	private static final VoxelShape AABB = makeCuboidShape(3, 3, 3, 13, 13, 13);

	protected BlockTinyPlanet(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return AABB;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileTinyPlanet();
	}

	@Override
	public boolean isGhost(BlockState state, World world, BlockPos pos) {
		return true;
	}
}
