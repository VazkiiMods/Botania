/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.elementium;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;

import vazkii.botania.common.core.handler.PixieHandler;

import javax.annotation.Nonnull;

public class ItemElementiumLegs extends ItemElementiumArmor {

	public ItemElementiumLegs(Properties props) {
		super(EquipmentSlotType.LEGS, props);
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType slot) {
		Multimap<String, AttributeModifier> ret = super.getAttributeModifiers(slot);
		if (slot == getEquipmentSlot()) {
			ret.put(PixieHandler.PIXIE_SPAWN_CHANCE.getName(), PixieHandler.makeModifier(slot, "Armor modifier", 0.15));
		}
		return ret;
	}

}
