/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item.equipment.tool.elementium;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import org.violetmoon.botania.common.handler.PixieHandler;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class ElementiumSwordItem extends SwordItem {
	public ElementiumSwordItem(Tier tier, Properties properties) {
		super(tier, properties);
	}

	public static ItemAttributeModifiers createAttributes(Tier tier, int attackDamage, float attackSpeed) {
		return SwordItem.createAttributes(tier, attackDamage, attackSpeed)
				.withModifierAdded(
						PixieHandler.PIXIE_SPAWN_CHANCE,
						PixieHandler.makeModifier(botaniaRL("sword_modifier"), 0.05),
						EquipmentSlotGroup.MAINHAND
				);
	}
}
