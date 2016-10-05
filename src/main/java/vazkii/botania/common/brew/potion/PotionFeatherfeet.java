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

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLivingBase;
import vazkii.botania.common.lib.LibPotionNames;

public class PotionFeatherfeet extends PotionMod {

	public PotionFeatherfeet() {
		super(LibPotionNames.FEATHER_FEET, false, 0x26ADFF, 1);
		setBeneficial();
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public void performEffect(@Nonnull EntityLivingBase living, int amplified) {
		living.fallDistance = 2.5F;
	}

}
