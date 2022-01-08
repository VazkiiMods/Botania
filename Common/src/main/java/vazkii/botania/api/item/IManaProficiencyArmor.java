/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * An armor item that implements this gives the player wearing it mana proficiency, by
 * which it makes Rods the player use have a stronger effect. This is a boolean, and only
 * one armor piece with this returning true is required for the player to be considered
 * to be having proficiency, by which it's suggested that this is used alongside an
 * armor set, where only one piece implements it.
 */
public interface IManaProficiencyArmor {

	default boolean shouldGiveProficiency(ItemStack armorStack, EquipmentSlot slot, Player player, ItemStack rod) {
		return false;
	}

}
