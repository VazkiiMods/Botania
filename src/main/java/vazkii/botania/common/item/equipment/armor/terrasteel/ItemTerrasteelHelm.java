/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 14, 2014, 3:13:05 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.armor.terrasteel;

import vazkii.botania.common.lib.LibItemNames;

public class ItemTerrasteelHelm extends ItemTerrasteelArmor {

	public ItemTerrasteelHelm() {
		this(LibItemNames.TERRASTEEL_HELM);
	}

	public ItemTerrasteelHelm(String name) {
		super(0, name);
	}

	@Override
	int getHealthBoost() {
		return 4;
	}

}
