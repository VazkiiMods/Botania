/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 15, 2015, 12:25:19 AM (GMT)]
 */
package vazkii.botania.common.block.tile.corporea;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.block.tile.TileSimpleInventory;

public abstract class TileCorporeaBase extends TileSimpleInventory {
	// These TE's only extend TileSimpleInventory to give sparks an inventory to attach to
	// TODO make sparks able to detect corporea blocks and not require them to have an inventory
	@Override
	public final int getSizeInventory() {
		return 0;
	}

	public ICorporeaSpark getSpark() {
		return CorporeaHelper.getSparkForBlock(world, getPos());
	}

}
