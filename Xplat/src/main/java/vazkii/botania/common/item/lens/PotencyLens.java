/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.mana.BurstProperties;

public class PotencyLens extends Lens {

	public static final int MAX_MANA_FACTOR = 2;

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		props.maxMana *= MAX_MANA_FACTOR;
		props.motionModifier *= 0.85F;
		props.manaLossPerTick *= 2F;
	}

}
