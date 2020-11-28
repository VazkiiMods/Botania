/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.elementium;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.common.core.handler.PixieHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemElementiumHelm extends ItemElementiumArmor implements IManaDiscountArmor {
	public ItemElementiumHelm(Settings props) {
		super(EquipmentSlot.HEAD, props);
	}

	@Nonnull
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlot slot) {
		Multimap<EntityAttribute, EntityAttributeModifier> ret = super.getAttributeModifiers(slot);
		if (slot == getSlotType()) {
			ret = HashMultimap.create(ret);
			ret.put(PixieHandler.PIXIE_SPAWN_CHANCE, PixieHandler.makeModifier(slot, "Armor modifier", 0.11));
		}
		return ret;
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, PlayerEntity player, @Nullable ItemStack tool) {
		return hasArmorSet(player) ? 0.1F : 0F;
	}

}
