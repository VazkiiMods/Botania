package vazkii.botania.common.fixers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.IntFunction;

public class FlattenItems implements IFixableData {
	// old id to (meta -> new id)
	private static final Map<String, IntFunction<String>> LOOKUP = new HashMap<>();

	static {
		LOOKUP.put("botania:manaresource", i -> {
			if (i == 10) {
				// support ancient prismarine from 1.7
				return "minecraft:prismarine_shard";
			} else {
				return LibMisc.MOD_ID + ":" + LibItemNames.MANA_RESOURCE_NAMES[i].toLowerCase(Locale.ROOT);
			}
		});
	}

	@Override
	public int getFixVersion() {
		return 1;
	}

	@Nonnull
	@Override
	public NBTTagCompound fixTagCompound(@Nonnull NBTTagCompound compound) {
		String id = compound.getString("id");
		if(LOOKUP.containsKey(id)) {
			int meta = compound.getShort("Damage");
			compound.setString("id", LOOKUP.get(id).apply(meta));
			compound.removeTag("Damage");
		}
		return compound;
	}
}
