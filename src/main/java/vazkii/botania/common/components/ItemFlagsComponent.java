/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.components;

import net.minecraft.nbt.CompoundTag;

import vazkii.botania.common.block.subtile.functional.SubTileSpectranthemum;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.block.tile.TileAltar;


import dev.onyxstudios.cca.api.v3.component.Component;

// Component for misc internal Botania flags
public class ItemFlagsComponent implements Component {
	public boolean spectranthemumTeleported = false;
	public boolean alfPortalSpawned = false;
	public boolean apothecarySpawned = false;

	@Override
	public void readFromNbt(CompoundTag tag) {
		spectranthemumTeleported = tag.getBoolean(SubTileSpectranthemum.TAG_TELEPORTED);
		alfPortalSpawned = tag.getBoolean(TileAlfPortal.TAG_PORTAL_FLAG);
		apothecarySpawned = tag.getBoolean(TileAltar.ITEM_TAG_APOTHECARY_SPAWNED);
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean(SubTileSpectranthemum.TAG_TELEPORTED, spectranthemumTeleported);
		tag.putBoolean(TileAlfPortal.TAG_PORTAL_FLAG, alfPortalSpawned);
		tag.putBoolean(TileAltar.ITEM_TAG_APOTHECARY_SPAWNED, apothecarySpawned);
	}
}
