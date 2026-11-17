/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.EdibleBlockWithEffects;
import vazkii.botania.client.fx.SparkleParticleData;

public class BotaniaInfusedGrassBlock extends BotaniaGrassBlock implements EdibleBlockWithEffects {

	public static final int EFFECT_DURATION = 160;

	public BotaniaInfusedGrassBlock(Properties builder) {
		super(builder);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		super.animateTick(state, level, pos, random);
		if (random.nextInt(100) == 0) {
			SparkleParticleData data = SparkleParticleData.sparkle(random.nextFloat() * 0.2F + 1F, 0F, 1F, 1F, 5);
			level.addParticle(data, pos.getX() + random.nextFloat(), pos.getY() + 1.05, pos.getZ() + random.nextFloat(), 0, 0, 0);
		}
	}

	@Override
	public void onEatenBy(BlockPos pos, BlockState state, LivingEntity entity) {
		entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, EFFECT_DURATION, 0, true, true));
	}
}
