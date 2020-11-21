package vazkii.botania.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.CompoundTag;
import vazkii.botania.common.block.subtile.functional.SubTileSpectranthemum;
import vazkii.botania.common.block.tile.TileAlfPortal;

// Component for misc internal Botania flags
public class ItemFlagsComponent implements Component {
	public boolean spectranthemumTeleported = false;
	public boolean alfPortalSpawned = false;

	@Override
	public void readFromNbt(CompoundTag tag) {
		spectranthemumTeleported = tag.getBoolean(SubTileSpectranthemum.TAG_TELEPORTED);
		alfPortalSpawned = tag.getBoolean(TileAlfPortal.TAG_PORTAL_FLAG);
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean(SubTileSpectranthemum.TAG_TELEPORTED, spectranthemumTeleported);
		tag.putBoolean(TileAlfPortal.TAG_PORTAL_FLAG, alfPortalSpawned);
	}
}
