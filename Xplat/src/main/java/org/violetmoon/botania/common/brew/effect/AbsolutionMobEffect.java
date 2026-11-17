/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew.effect;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import org.jetbrains.annotations.Nullable;

public class AbsolutionMobEffect extends InstantenousMobEffect {

	public AbsolutionMobEffect() {
		super(MobEffectCategory.NEUTRAL, 0xFFFFFF);
	}

	@Override
	public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity livingEntity, int amplifier, double health) {
		livingEntity.removeAllEffects();
	}

	@Override
	public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
		// SuspiciousStewItem does not support instant effects, so need to implement removal here.
		// This exits the loop over all potions effects with a ConcurrentModificationException, but It's Fine(tm),
		// since that loop is specifically guarded against that case.
		livingEntity.removeAllEffects();
		return true;
	}
}
