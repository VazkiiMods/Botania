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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.mana.ManaProficiencyCallback;

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

	static boolean hasProficiency(Player player, ItemStack rod) {
		boolean proficient = false;

		for (EquipmentSlot e : EquipmentSlot.values()) {
			if (e.getType() != EquipmentSlot.Type.ARMOR) {
				continue;
			}
			ItemStack armor = player.getItemBySlot(e);
			if (!armor.isEmpty()) {
				Item item = armor.getItem();
				if (item instanceof IManaProficiencyArmor && ((IManaProficiencyArmor) item).shouldGiveProficiency(armor, e, player, rod)) {
					proficient = true;
					break;
				}
			}
		}

		return ManaProficiencyCallback.EVENT.invoker().getProficient(player, rod, proficient);
	}

}
