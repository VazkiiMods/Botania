/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.tileentity.ITickableTileEntity;

import vazkii.botania.common.item.equipment.bauble.ItemTinyPlanet;

public class TileTinyPlanet extends TileMod implements ITickableTileEntity {
	public TileTinyPlanet() {
		super(ModTiles.TINY_PLANET);
	}

	@Override
	public void tick() {
		ItemTinyPlanet.applyEffect(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
	}

}
