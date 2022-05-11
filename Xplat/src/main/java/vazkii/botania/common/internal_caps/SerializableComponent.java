package vazkii.botania.common.internal_caps;

import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;

public abstract class SerializableComponent {
	public abstract void readFromNbt(CompoundTag tag);

	public abstract void writeToNbt(CompoundTag tag);

	@Nonnull
	public final CompoundTag serializeNBT() {
		var ret = new CompoundTag();
		writeToNbt(ret);
		return ret;
	}

	public final void deserializeNBT(CompoundTag nbt) {
		readFromNbt(nbt);
	}
}
