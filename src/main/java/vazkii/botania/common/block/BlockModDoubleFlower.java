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
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockModDoubleFlower extends TallFlowerBlock {
	private final DyeColor color;

	public BlockModDoubleFlower(DyeColor color, Properties builder) {
		super(builder);
		this.color = color;
	}

	// Normally, when an upper block is broken, the lower block recognizes the plant is no longer whole and automatically breaks itself.
	// But since we require shears, this is needed to pass the breaking context (tool, etc.) to the lower block.
	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (state.get(HALF) == DoubleBlockHalf.UPPER) {
			BlockState lower = world.getBlockState(pos.down());
			if (lower.getBlock() == this && lower.get(HALF) == DoubleBlockHalf.LOWER) {
				lower.getBlock().onBlockHarvested(world, pos.down(), lower, player);
			}
		}
		super.onBlockHarvested(world, pos, state, player);
	}

	@Override
	public boolean canGrow(@Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean fuckifiknow) {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		int hex = color.getColorValue();
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;

		if (rand.nextDouble() < ConfigHandler.CLIENT.flowerParticleFrequency.get()) {
			SparkleParticleData data = SparkleParticleData.sparkle(rand.nextFloat(), r / 255F, g / 255F, b / 255F, 5);
			world.addParticle(data, pos.getX() + 0.3 + rand.nextFloat() * 0.5, pos.getY() + 0.5 + rand.nextFloat() * 0.5, pos.getZ() + 0.3 + rand.nextFloat() * 0.5, 0, 0, 0);
		}

	}
}
