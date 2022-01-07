/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.ColorHelper;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockBuriedPetals extends BushBlock implements BonemealableBlock {

	private static final VoxelShape SHAPE = box(0, 0, 0, 16, 1.6, 16);

	public final DyeColor color;

	public BlockBuriedPetals(DyeColor color, Properties builder) {
		super(builder);
		this.color = color;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		int hex = ColorHelper.getColorValue(color);
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;

		SparkleParticleData data = SparkleParticleData.noClip(rand.nextFloat(), r / 255F, g / 255F, b / 255F, 5);
		world.addParticle(data, pos.getX() + 0.3 + rand.nextFloat() * 0.5, pos.getY() + 0.1 + rand.nextFloat() * 0.1, pos.getZ() + 0.3 + rand.nextFloat() * 0.5, 0, 0, 0);
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public boolean isValidBonemealTarget(@Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean fuckifiknow) {
		return world.getBlockState(pos.above()).isAir();
	}

	@Override
	public boolean isBonemealSuccess(@Nonnull Level world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		return isValidBonemealTarget(world, pos, state, false);
	}

	@Override
	public void performBonemeal(@Nonnull ServerLevel world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		Block block = ModBlocks.getDoubleFlower(color);
		if (block instanceof DoublePlantBlock) {
			DoublePlantBlock.placeAt(world, block.defaultBlockState(), pos, 3);
		}
	}
}
