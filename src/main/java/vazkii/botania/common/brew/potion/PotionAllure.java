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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import javax.annotation.Nonnull;

public class PotionAllure extends StatusEffect {

	public PotionAllure() {
		super(StatusEffectType.BENEFICIAL, 0x0034E4);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyUpdateEffect(@Nonnull LivingEntity living, int amplified) {
		if (living instanceof PlayerEntity) {
			FishingBobberEntity hook = ((PlayerEntity) living).fishHook;
			if (hook != null) {
				hook.tick();
			}
		}
	}

}
