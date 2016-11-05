/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 24, 2014, 5:12:53 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import vazkii.botania.common.lib.LibItemNames;

public class ItemGreaterAuraRing extends ItemAuraRing {

	public ItemGreaterAuraRing() {
		super(LibItemNames.AURA_RING_GREATER);
	}

	@Override
	int getDelay() {
		return 2;
	}
}
