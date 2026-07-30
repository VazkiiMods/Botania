/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import com.google.common.base.Suppliers;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.handler.PixieHandler;
import vazkii.botania.common.item.equipment.tool.manasteel.ManasteelSwordItem;

import java.util.function.Supplier;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class ElementiumSwordItem extends ManasteelSwordItem {
	private final Supplier<ItemAttributeModifiers> defaultModifiers;

	public ElementiumSwordItem(Properties props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), props);
		this.defaultModifiers = Suppliers.memoize(() -> ItemAttributeModifiers.builder()
				.add(PixieHandler.PIXIE_SPAWN_CHANCE, PixieHandler.makeModifier(
						botaniaRL("sword_modifier"), 0.05),
						EquipmentSlotGroup.bySlot(EquipmentSlot.MAINHAND))
				.build());
	}

	@Override
	@SuppressWarnings("deprecation")
	public ItemAttributeModifiers getDefaultAttributeModifiers() {
		return this.defaultModifiers.get();
	}
}
