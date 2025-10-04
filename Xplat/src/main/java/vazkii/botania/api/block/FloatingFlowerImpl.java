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
import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.block_entity.flower.BotaniaIslandTypes;

public class FloatingFlowerImpl implements FloatingFlower {
	private IslandType type = BotaniaIslandTypes.GRASS;

	@Override
	public IslandType getIslandType() {
		return type;
	}

	@Override
	public void setIslandType(IslandType type) {
		this.type = type;
	}

	@Override
	public Tag writeNBT(HolderLookup.Provider registries) {
		CompoundTag ret = new CompoundTag();
		ret.putString("islandType", BotaniaAPI.instance().getIslandTypeRegistry().getKey(type).toString());
		return ret;
	}

	@Override
	public void readNBT(CompoundTag nbt, HolderLookup.Provider registries) {
		ResourceLocation islandTypeId = ResourceLocation.tryParse(nbt.getString("islandType"));
		if (islandTypeId != null) {
			setIslandType(BotaniaAPI.instance().getIslandTypeRegistry().get(islandTypeId));
		}
	}
}
