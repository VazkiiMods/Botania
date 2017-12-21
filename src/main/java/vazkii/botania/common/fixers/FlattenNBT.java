package vazkii.botania.common.fixers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import vazkii.botania.common.item.ItemBottledMana;
import vazkii.botania.common.item.ItemTemperanceStone;
import vazkii.botania.common.item.ModItems;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

// Flattens meta variants by moving things to NBT
public class FlattenNBT implements IFixableData {
	// id to consumer<meta, tag>, fixes tag in place
	private static final Map<String, BiConsumer<Integer, NBTTagCompound>> LOOKUP = new HashMap<>();

	static {
		LOOKUP.put(ModItems.manaBottle.getRegistryName().toString(), (swigsTaken, tag) -> tag.setInteger(ItemBottledMana.TAG_SWIGS_LEFT, ItemBottledMana.SWIGS - swigsTaken));
		LOOKUP.put(ModItems.temperanceStone.getRegistryName().toString(), (meta, tag) -> tag.setBoolean(ItemTemperanceStone.TAG_ACTIVE, meta == 1));
	}

	@Override
	public int getFixVersion() {
		return 1;
	}

	@Override
	public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
		String id = compound.getString("id");
		if(LOOKUP.containsKey(id)) {
			int meta = compound.getShort("Damage");
			NBTTagCompound tag = compound.getCompoundTag("tag");

			LOOKUP.get(id).accept(meta, tag);

			compound.setTag("tag", tag); // needed if the stack had no NBT before
			compound.removeTag("Damage");
		}
		return compound;
	}
}
