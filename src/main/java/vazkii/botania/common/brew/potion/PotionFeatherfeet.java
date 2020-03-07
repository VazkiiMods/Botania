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
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import javax.annotation.Nonnull;

public class PotionFeatherfeet extends Effect {

	public PotionFeatherfeet() {
		super(EffectType.BENEFICIAL, 0x26ADFF);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public void performEffect(@Nonnull LivingEntity living, int amplified) {
		if (living.fallDistance > 2.5F) {
			living.fallDistance = 2.5F;
		}
	}

}
