/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 14, 2014, 3:14:11 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.armor.terrasteel;

import vazkii.botania.common.lib.LibItemNames;

public class ItemTerrasteelBoots extends ItemTerrasteelArmor {

	public ItemTerrasteelBoots() {
		super(3, LibItemNames.TERRASTEEL_BOOTS);
	}

	@Override
	int getHealthBoost() {
		return 3;
	}

}
