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

/**
 * Any Item that implements ILensEffect and this will have
 * a check before being pulled by the Tiny Planet.
 */
public interface ITinyPlanetExcempt {

	boolean shouldPull(ItemStack stack);

}
