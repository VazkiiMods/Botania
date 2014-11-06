package vazkii.botania.api.mana;

import net.minecraft.tileentity.TileEntity;

public class TileSignature {

	public final TileEntity tile;
	public final boolean remoteWorld;

	public TileSignature(TileEntity tile, boolean remoteWorld) {
		this.tile = tile;
		this.remoteWorld = remoteWorld;
	}

}
