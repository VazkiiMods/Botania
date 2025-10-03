/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public interface FloatingFlower {

	IslandType getIslandType();

	void setIslandType(IslandType type);

	Tag writeNBT(HolderLookup.Provider registries);

	void readNBT(CompoundTag nbt, HolderLookup.Provider registries);

}
