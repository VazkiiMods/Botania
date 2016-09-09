/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 1, 2014, 3:49:53 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import vazkii.botania.common.item.equipment.bauble.ItemTinyPlanet;

public class TileTinyPlanet extends TileMod {

	@Override
	public void update() {
		ItemTinyPlanet.applyEffect(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
	}

}
