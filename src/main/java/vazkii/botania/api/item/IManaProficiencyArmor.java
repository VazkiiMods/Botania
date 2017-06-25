/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 28, 2015, 9:04:53 PM (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.mana.ManaProficiencyEvent;

/**
 * An armor item that implements this gives the player wearing it mana proficiency, by
 * which it makes Rods the player use have a stronger effect. This is a boolean, and only
 * one armor piece with this returning true is required for the player to be considered
 * to be having proficiency, by which it's suggested that this is used alongside an
 * armor set, where only one piece implements it.
 */
public interface IManaProficiencyArmor {

	@Deprecated
	default boolean shouldGiveProficiency(ItemStack armorStack, EntityEquipmentSlot slot, EntityPlayer player) {
		return false;
	}
	
	default boolean shouldGiveProficiency(ItemStack armorStack, EntityEquipmentSlot slot, EntityPlayer player, ItemStack rod) {
		return shouldGiveProficiency(armorStack, slot, player);
	}

	public final static class Helper {

		@Deprecated
		public static boolean hasProficiency(EntityPlayer player) {
			return hasProficiency(player, ItemStack.EMPTY);
		}
		
		public static boolean hasProficiency(EntityPlayer player, ItemStack rod) {
			boolean proficient = false;
			
			for(EntityEquipmentSlot e: EntityEquipmentSlot.values()) {
				if(e.getSlotType() != EntityEquipmentSlot.Type.ARMOR)
					continue;
				ItemStack armor = player.getItemStackFromSlot(e);
				if(!armor.isEmpty()) {
					Item item = armor.getItem();
					if(item instanceof IManaProficiencyArmor && ((IManaProficiencyArmor) item).shouldGiveProficiency(armor, e, player, rod)) {
						proficient = true;
						break;
					}
				}
			}
			
			ManaProficiencyEvent event = new ManaProficiencyEvent(player, rod, proficient);
			MinecraftForge.EVENT_BUS.post(event);
			
			return event.isProficient();
		}

	}

}
