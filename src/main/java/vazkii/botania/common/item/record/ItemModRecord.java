/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.record;

import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;

public class ItemModRecord extends MusicDiscItem {
	public ItemModRecord(int comparator, SoundEvent sound, Settings builder) {
		super(comparator, sound, builder);
	}
}
