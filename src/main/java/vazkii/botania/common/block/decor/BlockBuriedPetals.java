/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockBuriedPetals extends PlantBlock implements Fertilizable {

	private static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 1.6, 16);

	public final DyeColor color;

	public BlockBuriedPetals(DyeColor color, Settings builder) {
		super(builder);
		this.color = color;
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, @Nonnull BlockView world, @Nonnull BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
		int hex = color.getColorValue();
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;

		SparkleParticleData data = SparkleParticleData.noClip(rand.nextFloat(), r / 255F, g / 255F, b / 255F, 5);
		world.addParticle(data, pos.getX() + 0.3 + rand.nextFloat() * 0.5, pos.getY() + 0.1 + rand.nextFloat() * 0.1, pos.getZ() + 0.3 + rand.nextFloat() * 0.5, 0, 0, 0);
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public boolean isFertilizable(@Nonnull BlockView world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean fuckifiknow) {
		return world.getBlockState(pos.up()).isAir(world, pos);
	}

	@Override
	public boolean canGrow(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		return isFertilizable(world, pos, state, false);
	}

	@Override
	public void grow(@Nonnull ServerWorld world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		Block block = ModBlocks.getDoubleFlower(color);
		if (block instanceof TallPlantBlock) {
			((TallPlantBlock) block).placeAt(world, pos, 3);
		}
	}
}
