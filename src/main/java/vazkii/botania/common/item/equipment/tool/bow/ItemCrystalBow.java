/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 21, 2015, 6:33:40 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool.bow;

import vazkii.botania.common.lib.LibItemNames;

public class ItemCrystalBow extends ItemLivingwoodBow {

	public ItemCrystalBow() {
		super(LibItemNames.CRYSTAL_BOW);
	}
	
	@Override
	boolean postsEvent() {
		return false;
	}
	
}
