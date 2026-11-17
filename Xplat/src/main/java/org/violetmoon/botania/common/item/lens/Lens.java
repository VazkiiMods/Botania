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
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.ManaSpreader;

public class Lens {

	public void apply(ItemStack stack, BurstProperties props) {}

	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		return shouldKill;
	}

	public void updateBurst(ManaBurst burst, ItemStack stack) {}

	public boolean allowBurstShooting(ItemStack stack, ManaSpreader spreader, boolean redstone) {
		return true;
	}

	public void onControlledSpreaderTick(ItemStack stack, ManaSpreader spreader, boolean redstone) {}

	public void onControlledSpreaderPulse(ItemStack stack, ManaSpreader spreader) {}

	public int getManaToTransfer(ManaBurst burst, ItemStack stack, ManaReceiver receiver) {
		return burst.getMana();
	}

}
