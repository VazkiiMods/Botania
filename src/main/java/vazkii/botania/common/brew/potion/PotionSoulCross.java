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

import vazkii.botania.common.brew.ModPotions;

public class PotionSoulCross extends MobEffect {

	public PotionSoulCross() {
		super(MobEffectCategory.BENEFICIAL, 0x47453d);
	}

	public static void onEntityKill(LivingEntity dying, LivingEntity killer) {
		if (killer.hasEffect(ModPotions.soulCross)) {
			killer.heal(dying.getMaxHealth() / 20);
		}
	}

}
