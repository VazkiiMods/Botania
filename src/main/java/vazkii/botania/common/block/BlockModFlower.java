/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.*;
import net.minecraft.item.DyeColor;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockModFlower extends FlowerBlock implements IGrowable {
	public final DyeColor color;

	protected BlockModFlower(DyeColor color, Properties builder) {
		super(effectForFlower(color), 4, builder);
		this.color = color;
	}

	private static Effect effectForFlower(DyeColor color) {
		switch (color) {
		case WHITE:
			return Effects.SPEED;
		case ORANGE:
			return Effects.FIRE_RESISTANCE;
		case MAGENTA:
			return Effects.MINING_FATIGUE;
		case LIGHT_BLUE:
			return Effects.JUMP_BOOST;
		case YELLOW:
			return Effects.ABSORPTION;
		case LIME:
			return Effects.POISON;
		case PINK:
			return Effects.REGENERATION;
		case GRAY:
			return Effects.RESISTANCE;
		case LIGHT_GRAY:
			return Effects.WEAKNESS;
		case CYAN:
			return Effects.WATER_BREATHING;
		case PURPLE:
			return Effects.NAUSEA;
		case BLUE:
			return Effects.NIGHT_VISION;
		case BROWN:
			return Effects.WITHER;
		case GREEN:
			return Effects.HUNGER;
		case RED:
			return Effects.STRENGTH;
		case BLACK:
			return Effects.BLINDNESS;
		}
		return Effects.REGENERATION;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		int hex = this.color.getColorValue();
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;
		Vector3d offset = state.getOffset(world, pos);
		double x = pos.getX() + offset.x;
		double y = pos.getY() + offset.y;
		double z = pos.getZ() + offset.z;

		if (rand.nextDouble() < ConfigHandler.CLIENT.flowerParticleFrequency.get()) {
			SparkleParticleData data = SparkleParticleData.sparkle(rand.nextFloat(), r / 255F, g / 255F, b / 255F, 5);
			world.addParticle(data, x + 0.3 + rand.nextFloat() * 0.5, y + 0.5 + rand.nextFloat() * 0.5, z + 0.3 + rand.nextFloat() * 0.5, 0, 0, 0);
		}
	}

	@Override
	public boolean canGrow(@Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean fuckifiknow) {
		return world.getBlockState(pos.up()).isAir(world, pos);
	}

	@Override
	public boolean canUseBonemeal(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		return canGrow(world, pos, state, false);
	}

	@Override
	public void grow(@Nonnull ServerWorld world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		Block block = ModBlocks.getDoubleFlower(color);
		if (block instanceof DoublePlantBlock) {
			((DoublePlantBlock) block).placeAt(world, pos, 3);
		}
	}
}
