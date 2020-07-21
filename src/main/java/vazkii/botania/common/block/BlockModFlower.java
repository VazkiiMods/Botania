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
import net.minecraft.block.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockModFlower extends FlowerBlock implements Fertilizable {
	public final DyeColor color;

	protected BlockModFlower(DyeColor color, Settings builder) {
		super(effectForFlower(color), 4, builder);
		this.color = color;
	}

	private static StatusEffect effectForFlower(DyeColor color) {
		switch (color) {
		case WHITE:
			return StatusEffects.SPEED;
		case ORANGE:
			return StatusEffects.FIRE_RESISTANCE;
		case MAGENTA:
			return StatusEffects.MINING_FATIGUE;
		case LIGHT_BLUE:
			return StatusEffects.JUMP_BOOST;
		case YELLOW:
			return StatusEffects.ABSORPTION;
		case LIME:
			return StatusEffects.POISON;
		case PINK:
			return StatusEffects.REGENERATION;
		case GRAY:
			return StatusEffects.RESISTANCE;
		case LIGHT_GRAY:
			return StatusEffects.WEAKNESS;
		case CYAN:
			return StatusEffects.WATER_BREATHING;
		case PURPLE:
			return StatusEffects.NAUSEA;
		case BLUE:
			return StatusEffects.NIGHT_VISION;
		case BROWN:
			return StatusEffects.WITHER;
		case GREEN:
			return StatusEffects.HUNGER;
		case RED:
			return StatusEffects.STRENGTH;
		case BLACK:
			return StatusEffects.BLINDNESS;
		}
		return StatusEffects.REGENERATION;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
		int hex = this.color.getColorValue();
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;
		Vec3d offset = state.getModelOffset(world, pos);
		double x = pos.getX() + offset.x;
		double y = pos.getY() + offset.y;
		double z = pos.getZ() + offset.z;

		if (rand.nextDouble() < ConfigHandler.CLIENT.flowerParticleFrequency.get()) {
			SparkleParticleData data = SparkleParticleData.sparkle(rand.nextFloat(), r / 255F, g / 255F, b / 255F, 5);
			world.addParticle(data, x + 0.3 + rand.nextFloat() * 0.5, y + 0.5 + rand.nextFloat() * 0.5, z + 0.3 + rand.nextFloat() * 0.5, 0, 0, 0);
		}
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
