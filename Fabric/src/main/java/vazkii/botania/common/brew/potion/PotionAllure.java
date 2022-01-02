/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;

import javax.annotation.Nonnull;

public class PotionAllure extends MobEffect {

	public PotionAllure() {
		super(MobEffectCategory.BENEFICIAL, 0x0034E4);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyEffectTick(@Nonnull LivingEntity living, int amplified) {
		if (living instanceof Player) {
			FishingHook hook = ((Player) living).fishing;
			if (hook != null) {
				hook.tick();
			}
		}
	}

}
