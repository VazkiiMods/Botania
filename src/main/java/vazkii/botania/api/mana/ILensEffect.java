/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 14, 2014, 7:30:00 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
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
	public void apply(ItemStack stack, BurstProperties props);

	/**
	 * Called when a mana burst fired from a mana spreader with this focus collides against
	 * any block. This is called after the collision is handled.
	 * @return True to kill the burst. False to keep it alive.
	 */
	public boolean collideBurst(IManaBurst burst, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack);

	/**
	 * Called when a mana burst fired from a mana spreader with this focus is updated.
	 * This is called before the update is handled.
	 */
	public void updateBurst(IManaBurst burst, ItemStack stack);

	/**
	 * Called when the mana burst should do it's particles. Return false to not
	 * do any particles.
	 */
	public boolean doParticles(IManaBurst burst, ItemStack stack);

	/**
	 * Gets the amount of mana to transfer to the passed in mana receiver block.
	 */
	public default int getManaToTransfer(IManaBurst burst, EntityThrowable entity, ItemStack stack, IManaReceiver receiver) {
		return burst.getMana();
	}
	
}
