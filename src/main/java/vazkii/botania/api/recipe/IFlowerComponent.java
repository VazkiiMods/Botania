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
 * TODO (breaking change): Rename to something more like "ISpecialApothecary", since now all items can go in the Apothecary
 */
public interface IFlowerComponent {

	//TODO remove 1.13, unused, all items can go in the Petal Apothecary
	@Deprecated
	public default boolean canFit(ItemStack stack, IPetalApothecary apothecary) {
		return true;
	}
	
	public int getParticleColor(ItemStack stack);

}
