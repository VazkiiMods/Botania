/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.PrimedTnt;

import vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum;

public class EthicalComponent implements Component {
	private static final String TAG_UNETHICAL = "botania:unethical";
	public boolean unethical;

	public EthicalComponent(PrimedTnt entity) {
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
