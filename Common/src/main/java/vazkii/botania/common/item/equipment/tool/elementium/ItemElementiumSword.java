/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.core.handler.PixieHandler;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;

import javax.annotation.Nonnull;

public class ItemElementiumSword extends ItemManasteelSword {

	public ItemElementiumSword(Properties props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), props);
	}

	@Nonnull
	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@Nonnull EquipmentSlot slot) {
		Multimap<Attribute, AttributeModifier> ret = super.getDefaultAttributeModifiers(slot);
		if (slot == EquipmentSlot.MAINHAND) {
			ret = HashMultimap.create(ret);
			ret.put(PixieHandler.PIXIE_SPAWN_CHANCE, PixieHandler.makeModifier(slot, "Sword modifier", 0.05));
		}
		return ret;
	}

}
