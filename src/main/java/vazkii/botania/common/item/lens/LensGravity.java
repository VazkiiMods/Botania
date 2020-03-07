/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.item.ItemStack;

import vazkii.botania.api.mana.BurstProperties;

public class LensGravity extends Lens {

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		props.gravity = 0.0015F;
		props.ticksBeforeManaLoss *= 1.2F;
	}

}
