/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

import javax.annotation.Nonnull;

public class PotionFeatherfeet extends StatusEffect {

	public PotionFeatherfeet() {
		super(StatusEffectType.BENEFICIAL, 0x26ADFF);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyUpdateEffect(@Nonnull LivingEntity living, int amplified) {
		if (living.fallDistance > 2.5F) {
			living.fallDistance = 2.5F;
		}
	}

}
