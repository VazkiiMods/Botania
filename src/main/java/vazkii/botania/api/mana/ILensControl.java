/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [15/11/2015, 21:00:32 (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.item.ItemStack;

/**
 * An extension of ILens that allows for the lens item to control the
 * spreader's behaviour.
 */
public interface ILensControl extends ILens {

	public boolean isControlLens(ItemStack stack);

	public boolean allowBurstShooting(ItemStack stack, IManaSpreader spreader, boolean redstone);

	/**
	 * Used for the tick of a non-redstone spreader.
	 */
	public void onControlledSpreaderTick(ItemStack stack, IManaSpreader spreader, boolean redstone);

	/**
	 * Used for when a redstone spreader gets a pulse.
	 */
	public void onControlledSpreaderPulse(ItemStack stack, IManaSpreader spreader, boolean redstone);

}
