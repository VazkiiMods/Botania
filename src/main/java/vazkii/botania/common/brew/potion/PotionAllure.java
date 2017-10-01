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

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import vazkii.botania.common.lib.LibPotionNames;

import javax.annotation.Nonnull;

public class PotionAllure extends PotionMod {

	public PotionAllure() {
		super(LibPotionNames.ALLURE, false, 0x0034E4, 5);
		setBeneficial();
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public void performEffect(@Nonnull EntityLivingBase living, int amplified) {
		if(living instanceof EntityPlayer) {
			EntityFishHook hook = ((EntityPlayer) living).fishEntity;
			if(hook != null)
				hook.onUpdate();
		}
	}

}
