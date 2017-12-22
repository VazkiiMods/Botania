package vazkii.botania.common.fixers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.datafix.IFixableData;
import vazkii.botania.common.block.tile.TileCraftCrate;

// Shifts pattern tag in Crafty Crate TE NBT up one to account for CratePattern.NONE
public class CraftyCrateTE implements IFixableData {
	@Override
	public int getFixVersion() {
		return 1;
	}

	@Override
	public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
		if(TileEntity.getKey(TileCraftCrate.class).toString().equals(compound.getString("id"))) {
			compound.setInteger(TileCraftCrate.TAG_PATTERN, compound.getInteger(TileCraftCrate.TAG_PATTERN) + 1);
		}
		return compound;
	}
}
