/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
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
