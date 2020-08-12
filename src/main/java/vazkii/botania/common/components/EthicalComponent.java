package vazkii.botania.common.components;

import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.entity.TntEntity;
import net.minecraft.nbt.CompoundTag;
import vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum;

public class EthicalComponent implements Component {
	private static final String TAG_UNETHICAL = "botania:unethical";
	public boolean unethical;

	public EthicalComponent(TntEntity entity) {
		// todo 1.16-fabric may cause hangs on load
		unethical = SubTileEntropinnyum.isUnethical(entity);
	}

	@Override
	public void fromTag(CompoundTag tag) {
		unethical = tag.getBoolean(TAG_UNETHICAL);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putBoolean(TAG_UNETHICAL, unethical);
		return tag;
	}
}
