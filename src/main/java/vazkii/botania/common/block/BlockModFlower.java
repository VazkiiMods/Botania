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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ColorHelper;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockModFlower extends FlowerBlock implements BonemealableBlock {
	public final DyeColor color;

	protected BlockModFlower(DyeColor color, Properties builder) {
		super(effectForFlower(color), 4, builder);
		this.color = color;
	}

	private static MobEffect effectForFlower(DyeColor color) {
		switch (color) {
		case WHITE:
			return MobEffects.MOVEMENT_SPEED;
		case ORANGE:
			return MobEffects.FIRE_RESISTANCE;
		case MAGENTA:
			return MobEffects.DIG_SLOWDOWN;
		case LIGHT_BLUE:
			return MobEffects.JUMP;
		case YELLOW:
			return MobEffects.ABSORPTION;
		case LIME:
			return MobEffects.POISON;
		case PINK:
			return MobEffects.REGENERATION;
		case GRAY:
			return MobEffects.DAMAGE_RESISTANCE;
		case LIGHT_GRAY:
			return MobEffects.WEAKNESS;
		case CYAN:
			return MobEffects.WATER_BREATHING;
		case PURPLE:
			return MobEffects.CONFUSION;
		case BLUE:
			return MobEffects.NIGHT_VISION;
		case BROWN:
			return MobEffects.WITHER;
		case GREEN:
			return MobEffects.HUNGER;
		case RED:
			return MobEffects.DAMAGE_BOOST;
		case BLACK:
			return MobEffects.BLINDNESS;
		}
		return MobEffects.REGENERATION;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		int hex = ColorHelper.getColorValue(this.color);
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;
		Vec3 offset = state.getOffset(world, pos);
		double x = pos.getX() + offset.x;
		double y = pos.getY() + offset.y;
		double z = pos.getZ() + offset.z;

		if (rand.nextDouble() < ConfigHandler.CLIENT.flowerParticleFrequency.getValue()) {
			SparkleParticleData data = SparkleParticleData.sparkle(rand.nextFloat(), r / 255F, g / 255F, b / 255F, 5);
			world.addParticle(data, x + 0.3 + rand.nextFloat() * 0.5, y + 0.5 + rand.nextFloat() * 0.5, z + 0.3 + rand.nextFloat() * 0.5, 0, 0, 0);
		}
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
			((DoublePlantBlock) block).placeAt(world, pos, 3);
		}
	}
}
