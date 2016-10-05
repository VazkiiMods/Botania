/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 22, 2014, 2:26:14 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.item.ItemStack;

/**
 * Any Item that implements ILensEffect and this will have
 * a check before being pulled by the Tiny Planet.
 */
public interface ITinyPlanetExcempt {

	public boolean shouldPull(ItemStack stack);

}
