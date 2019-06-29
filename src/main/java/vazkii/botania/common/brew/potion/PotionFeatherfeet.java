/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 3, 2014, 12:12:04 AM (GMT)]
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;

import javax.annotation.Nonnull;

public class PotionFeatherfeet extends PotionMod {

	public PotionFeatherfeet() {
		super(EffectType.BENEFICIAL, 0x26ADFF, 1);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public void performEffect(@Nonnull LivingEntity living, int amplified) {
		if(living.fallDistance > 2.5F)
			living.fallDistance = 2.5F;
	}

}
