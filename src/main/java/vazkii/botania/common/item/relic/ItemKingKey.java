/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 16, 2015, 2:54:35 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import vazkii.botania.common.lib.LibItemNames;

public class ItemKingKey extends ItemRelic {

	public ItemKingKey() {
		super(LibItemNames.KING_KEY);
	}

	@Override
	public boolean isFull3D() {
		return true;
	}
	
}
