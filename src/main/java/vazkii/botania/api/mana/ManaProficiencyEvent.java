/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ManaProficiencyEvent extends Event {

	private final PlayerEntity entityPlayer;
	private ItemStack rod;
	private boolean proficient;

	public ManaProficiencyEvent(PlayerEntity entityPlayer, ItemStack rod, boolean proficient) {
		this.entityPlayer = entityPlayer;
		this.rod = rod;
		this.proficient = proficient;
	}

	public PlayerEntity getEntityPlayer() {
		return entityPlayer;
	}

	public ItemStack getRod() {
		return rod;
	}

	public boolean isProficient() {
		return proficient;
	}

	public void setProficient(boolean proficient) {
		this.proficient = proficient;
	}
}
