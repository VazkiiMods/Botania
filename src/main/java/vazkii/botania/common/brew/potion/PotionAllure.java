/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 3, 2014, 12:15:09 AM (GMT)]
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.potion.EffectType;
import vazkii.botania.common.lib.LibPotionNames;

import javax.annotation.Nonnull;

public class PotionAllure extends PotionMod {

	public PotionAllure() {
		super(EffectType.BENEFICIAL, 0x0034E4, 5);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public void performEffect(@Nonnull LivingEntity living, int amplified) {
		if(living instanceof PlayerEntity) {
			FishingBobberEntity hook = ((PlayerEntity) living).fishingBobber;
			if(hook != null)
				hook.tick();
		}
	}

}
