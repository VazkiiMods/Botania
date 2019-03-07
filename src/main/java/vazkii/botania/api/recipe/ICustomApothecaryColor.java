/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 15, 2014, 2:36:35 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.item.IPetalApothecary;

/**
 * An Item or Block that implements this interface can have a special particle color in the Petal Apothecary.
 */
public interface ICustomApothecaryColor {
	public int getParticleColor(ItemStack stack);

}
