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

public class VelocityLens extends Lens {

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		props.motionModifier *= 2F;
		props.maxMana *= 0.75F;
		props.ticksBeforeManaLoss /= 3F;
		props.manaLossPerTick *= 2F;
	}

}
