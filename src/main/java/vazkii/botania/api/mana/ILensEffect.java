/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.IManaBurst;

/**
 * Have an item implement this for it to count as a lens effect and
 * be able to change the properties of Mana Bursts.
 */
public interface ILensEffect {

	/**
	 * Called when a mana spreader that has this focus shoots a burst. This is where
	 * you change the properties of the burst.
	 */
	void apply(ItemStack stack, BurstProperties props);

	/**
	 * Called when a mana burst fired from a mana spreader with this focus collides against
	 * any block. This is called after the collision is handled.
	 * 
	 * @return True to kill the burst. False to keep it alive.
	 */
	boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack);

	/**
	 * Called when a mana burst fired from a mana spreader with this focus is updated.
	 * This is called before the update is handled.
	 */
	void updateBurst(IManaBurst burst, ItemStack stack);

	/**
	 * Called when the mana burst should do it's particles. Return false to not
	 * do any particles.
	 */
	boolean doParticles(IManaBurst burst, ItemStack stack);

	/**
	 * Gets the amount of mana to transfer to the passed in mana receiver block.
	 */
	default int getManaToTransfer(IManaBurst burst, ItemStack stack, IManaReceiver receiver) {
		return burst.getMana();
	}

}
