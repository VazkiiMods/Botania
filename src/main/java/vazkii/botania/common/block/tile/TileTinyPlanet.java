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

import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.common.item.equipment.bauble.ItemTinyPlanet;

public class TileTinyPlanet extends TileMod implements IManaCollisionGhost {

	@Override
	public void updateEntity() {
		ItemTinyPlanet.applyEffect(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
	}

	@Override
	public boolean isGhost() {
		return true;
	}

}
