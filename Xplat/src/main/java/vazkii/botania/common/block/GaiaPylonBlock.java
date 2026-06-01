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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.client.fx.SparkleParticleData;

public class GaiaPylonBlock extends PylonBlock {
	private static final VoxelShape SHAPE = box(2, 0, 2, 14, 16, 14);

	public GaiaPylonBlock(Properties builder) {
		super(builder);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public void addRandomParticle(Level level, BlockPos pos) {
		SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), 1.0f, 0.5f, 1.0f, 2);
		level.addParticle(data, pos.getX() + Math.random(), pos.getY() + 0.1 + 0.9 * Math.random(), pos.getZ() + Math.random(), 0, 0, 0);
	}

	public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
		return 15f;
	}
}
