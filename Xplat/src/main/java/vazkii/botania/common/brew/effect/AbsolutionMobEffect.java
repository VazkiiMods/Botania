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

import org.jetbrains.annotations.NotNull;

public class AbsolutionMobEffect extends InstantenousMobEffect {

	public AbsolutionMobEffect() {
		super(MobEffectCategory.NEUTRAL, 0xFFFFFF);
	}

	@Override
	public void applyInstantenousEffect(Entity e, Entity e1, @NotNull LivingEntity e2, int t, double d) {
		e2.removeAllEffects();
	}

}
