/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.corporea;

import net.minecraft.block.entity.BlockEntityType;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.block.tile.TileMod;

public abstract class TileCorporeaBase extends TileMod {

	public TileCorporeaBase(BlockEntityType<?> type) {
		super(type);
	}

	public ICorporeaSpark getSpark() {
		return CorporeaHelper.instance().getSparkForBlock(world, getPos());
	}

}
