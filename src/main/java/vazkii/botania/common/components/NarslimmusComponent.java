package vazkii.botania.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.nbt.CompoundTag;

public class NarslimmusComponent implements Component {
	public static final String TAG_WORLD_SPAWNED = "botania:world_spawned";
	private boolean naturalSpawned = false;

	public NarslimmusComponent(SlimeEntity e) {
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		naturalSpawned = tag.getBoolean(TAG_WORLD_SPAWNED);
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean(TAG_WORLD_SPAWNED, naturalSpawned);
	}

	public boolean isNaturalSpawned() {
		return naturalSpawned;
	}

	public void setNaturalSpawn(boolean naturalSpawned) {
		this.naturalSpawned = naturalSpawned;
	}
}
