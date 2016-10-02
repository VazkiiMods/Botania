/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 1, 2014, 7:34:30 PM (GMT)]
 */
package vazkii.botania.common.item.brew;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;

public class ItemBrewFlask extends ItemBrewBase {

	public ItemBrewFlask() {
		super(LibItemNames.BREW_FLASK, 6, 24, new ItemStack(ModItems.vial, 1, 1));
	}

}
