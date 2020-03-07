/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.core.handler.PixieHandler;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;

import javax.annotation.Nonnull;

public class ItemElementiumSword extends ItemManasteelSword {

	public ItemElementiumSword(Properties props) {
		super(BotaniaAPI.ELEMENTIUM_ITEM_TIER, props);
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType slot) {
		Multimap<String, AttributeModifier> ret = super.getAttributeModifiers(slot);
		if (slot == EquipmentSlotType.MAINHAND) {
			ret.put(PixieHandler.PIXIE_SPAWN_CHANCE.getName(), PixieHandler.makeModifier(slot, "Sword modifier", 0.05));
		}
		return ret;
	}

}
