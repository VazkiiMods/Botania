/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.corporea;

import net.minecraft.inventory.Inventory;
import net.minecraft.tileentity.TileEntityType;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.block.tile.TileSimpleInventory;

public abstract class TileCorporeaBase extends TileSimpleInventory {
	private static final Inventory EMPTY = new Inventory(0);

	public TileCorporeaBase(TileEntityType<?> type) {
		super(type);
	}

	// These TE's only extend TileSimpleInventory to give sparks an inventory to attach to
	// TODO make sparks able to detect corporea blocks and not require them to have an inventory
	@Override
	protected final Inventory createItemHandler() {
		return EMPTY;
	}

	public ICorporeaSpark getSpark() {
		return CorporeaHelper.instance().getSparkForBlock(world, getPos());
	}

}
