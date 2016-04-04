package vazkii.botania.api.mana;

import net.minecraft.tileentity.TileEntity;

public class TileSignature {

	public final TileEntity tile;
	public final boolean remoteWorld;

	public TileSignature(TileEntity tile, boolean remoteWorld) {
		this.tile = tile;
		this.remoteWorld = remoteWorld;
	}

	@Override
	public int hashCode() {
		return Boolean.hashCode(remoteWorld) ^ System.identityHashCode(tile);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof TileSignature
				&& tile == ((TileSignature) o).tile
				&& remoteWorld == ((TileSignature) o).remoteWorld;
	}

}
