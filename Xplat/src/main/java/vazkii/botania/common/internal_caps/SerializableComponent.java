package vazkii.botania.common.internal_caps;

import net.minecraft.nbt.CompoundTag;

import org.jetbrains.annotations.NotNull;

public abstract class SerializableComponent {
	public abstract void readFromNbt(CompoundTag tag);

	public abstract void writeToNbt(CompoundTag tag);

	@NotNull
	public final CompoundTag serializeNBT() {
		var ret = new CompoundTag();
		writeToNbt(ret);
		return ret;
	}

	public final void deserializeNBT(CompoundTag nbt) {
		readFromNbt(nbt);
	}
}
