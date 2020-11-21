package vazkii.botania.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.TntEntity;
import net.minecraft.nbt.CompoundTag;
import vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum;

public class EthicalComponent implements Component {
	private static final String TAG_UNETHICAL = "botania:unethical";
	public boolean unethical;

	public EthicalComponent(TntEntity entity) {
		unethical = SubTileEntropinnyum.isUnethical(entity);
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		unethical = tag.getBoolean(TAG_UNETHICAL);
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean(TAG_UNETHICAL, unethical);
	}
}
