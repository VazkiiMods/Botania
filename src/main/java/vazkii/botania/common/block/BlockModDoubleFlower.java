/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ColorHelper;

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
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			BlockState lower = world.getBlockState(pos.below());
			if (lower.is(this) && lower.getValue(HALF) == DoubleBlockHalf.LOWER) {
				lower.getBlock().playerWillDestroy(world, pos.below(), lower, player);
			}
		}
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public boolean isValidBonemealTarget(@Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean fuckifiknow) {
		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		int hex = ColorHelper.getColorValue(color);
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;

		if (rand.nextDouble() < ConfigHandler.CLIENT.flowerParticleFrequency.getValue()) {
			SparkleParticleData data = SparkleParticleData.sparkle(rand.nextFloat(), r / 255F, g / 255F, b / 255F, 5);
			world.addParticle(data, pos.getX() + 0.3 + rand.nextFloat() * 0.5, pos.getY() + 0.5 + rand.nextFloat() * 0.5, pos.getZ() + 0.3 + rand.nextFloat() * 0.5, 0, 0, 0);
		}

	}
}
