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

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.EdibleBlockWithEffects;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.mixin.LivingEntityAccessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BotaniaMutatedGrassBlock extends BotaniaGrassBlock implements EdibleBlockWithEffects {

	/**
	 * Random (typically useless) effects to apply. No instant effects, event-centric effects (omens, darkness, etc.),
	 * or wither (too dangerous). Also, no glowing, since that's a bit too disruptive.
	 */
	public static final List<Holder<MobEffect>> RANDOM_EFFECTS = List.of(
			MobEffects.ABSORPTION, MobEffects.BLINDNESS, MobEffects.CONFUSION, MobEffects.DAMAGE_BOOST,
			MobEffects.DAMAGE_RESISTANCE, MobEffects.DIG_SLOWDOWN, MobEffects.DIG_SPEED, MobEffects.FIRE_RESISTANCE,
			MobEffects.HUNGER, MobEffects.INFESTED, MobEffects.INVISIBILITY, MobEffects.JUMP, MobEffects.LEVITATION,
			MobEffects.LUCK, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.MOVEMENT_SPEED, MobEffects.NIGHT_VISION,
			MobEffects.OOZING, MobEffects.POISON, MobEffects.REGENERATION, MobEffects.SATURATION,
			MobEffects.SLOW_FALLING, MobEffects.UNLUCK, MobEffects.WATER_BREATHING, MobEffects.WEAKNESS,
			MobEffects.WEAVING, MobEffects.WIND_CHARGED
	);
	public static final Object2IntMap<Holder<MobEffect>> EFFECT_DURATIONS = Util.make(new Object2IntArrayMap<>(), map -> {
		map.defaultReturnValue(300);
		map.put(MobEffects.POISON, 45);
		map.put(MobEffects.LEVITATION, 10);
	});

	public BotaniaMutatedGrassBlock(Properties builder) {
		super(builder);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		super.animateTick(state, level, pos, random);
		if (random.nextInt(100) == 0) {
			SparkleParticleData data = random.nextInt(4) > 0
					? SparkleParticleData.sparkle(random.nextFloat() * 0.2F + 1F, 1F, 0F, 1F, 5)
					: SparkleParticleData.sparkle(random.nextFloat() * 0.2F + 1F, 1F, 1F, 0F, 5);
			level.addParticle(data, pos.getX() + random.nextFloat(), pos.getY() + 1.05, pos.getZ() + random.nextFloat(), 0, 0, 0);
		}
	}

	@Override
	public void onEatenBy(BlockPos pos, BlockState state, LivingEntity entity) {
		if (entity instanceof Sheep sheep && entity.getRandom().nextInt(5) == 0) {
			((LivingEntityAccessor) entity).botania_playHurtSound(entity.damageSources().generic());
			sheep.setSheared(true);
		}
		int numEffects = entity.getRandom().nextInt(3) + entity.getRandom().nextInt(3);
		if (numEffects == 1) {
			applyEffect(entity, RANDOM_EFFECTS.get(entity.getRandom().nextInt(RANDOM_EFFECTS.size())));
		} else if (numEffects > 1) {
			var randomEffects = new ArrayList<>(RANDOM_EFFECTS);
			Collections.shuffle(randomEffects);
			for (int i = 0; i < numEffects; i++) {
				applyEffect(entity, randomEffects.get(i));
			}
		}
	}

	private static void applyEffect(LivingEntity entity, Holder<MobEffect> effect) {
		entity.addEffect(new MobEffectInstance(effect, EFFECT_DURATIONS.getInt(effect), 0,
				// make invis particles easier to see
				effect != MobEffects.INVISIBILITY, true));
	}
}
