/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.capability;

import net.minecraft.nbt.Tag;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;

public class NoopCapStorage<T> implements Capability.IStorage<T> {
	@Nullable
	@Override
	public Tag writeNBT(Capability<T> capability, T instance, Direction side) {
		return null;
	}

	@Override
	public void readNBT(Capability<T> capability, T instance, Direction side, Tag nbt) {}
}
