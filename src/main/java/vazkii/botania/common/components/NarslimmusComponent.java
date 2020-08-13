package vazkii.botania.common.components;

import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.extension.TypeAwareComponent;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.nbt.CompoundTag;

public class NarslimmusComponent implements TypeAwareComponent {
	public static final String TAG_WORLD_SPAWNED = "botania:world_spawned";
	private boolean naturalSpawned = false;

	public NarslimmusComponent(SlimeEntity e) {
	}

	@Override
	public ComponentType<?> getComponentType() {
		return EntityComponents.NARSLIMMUS;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		naturalSpawned = tag.getBoolean(TAG_WORLD_SPAWNED);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putBoolean(TAG_WORLD_SPAWNED, naturalSpawned);
		return tag;
	}

	public boolean isNaturalSpawned() {
		return naturalSpawned;
	}

	public void setNaturalSpawn(boolean naturalSpawned) {
		this.naturalSpawned = naturalSpawned;
	}
}
