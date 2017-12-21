package vazkii.botania.common.fixers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;

import java.util.Locale;

// Renames the pre-1.13 will tags inside Terrasteel Helmets (of revealing)
public class AttachedWills implements IFixableData {
	@Override
	public int getFixVersion() {
		return 1;
	}

	@Override
	public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
		String terraHelm = ModItems.terrasteelHelm.getRegistryName().toString();
		String terraHelmReveal = ModItems.terrasteelHelmRevealing.getRegistryName().toString();

		if((terraHelm.equals(compound.getString("id")) || terraHelmReveal.equals(compound.getString("id")))
				&& compound.hasKey("tag")) {
			NBTTagCompound tag = compound.getCompoundTag("tag");
			for(int i = 0; i < 6; i++) {
				String oldKey = ItemTerrasteelHelm.TAG_ANCIENT_WILL + i;
				if(tag.hasKey(oldKey)) {
					tag.removeTag(oldKey);
					tag.setBoolean(ItemTerrasteelHelm.TAG_ANCIENT_WILL + "_" + IAncientWillContainer.AncientWillType.values()[i].name().toLowerCase(Locale.ROOT), true);
				}
			}
		}
		return compound;
	}
}
