/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.item.ItemStack;

/**
 * An Item that implements this interface can have a special particle color when in the Petal Apothecary.
 */
public interface ICustomApothecaryColor {
	int getParticleColor(ItemStack stack);

}
