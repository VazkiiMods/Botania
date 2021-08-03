/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.common.block.tile.TileTinyPlanet;

import javax.annotation.Nonnull;

public class BlockTinyPlanet extends BlockModWaterloggable implements IManaCollisionGhost, EntityBlock {

	private static final VoxelShape AABB = box(3, 3, 3, 13, 13, 13);

	protected BlockTinyPlanet(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return AABB;
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockGetter world) {
		return new TileTinyPlanet();
	}

	@Override
	public boolean isGhost(BlockState state, Level world, BlockPos pos) {
		return true;
	}
}
