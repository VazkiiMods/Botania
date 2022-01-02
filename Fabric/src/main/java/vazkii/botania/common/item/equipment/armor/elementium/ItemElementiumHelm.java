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

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.common.core.handler.PixieHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemElementiumHelm extends ItemElementiumArmor implements IManaDiscountArmor {
	public ItemElementiumHelm(Properties props) {
		super(EquipmentSlot.HEAD, props);
	}

	@Nonnull
	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@Nonnull EquipmentSlot slot) {
		Multimap<Attribute, AttributeModifier> ret = super.getDefaultAttributeModifiers(slot);
		if (slot == getSlot()) {
			ret = HashMultimap.create(ret);
			ret.put(PixieHandler.PIXIE_SPAWN_CHANCE, PixieHandler.makeModifier(slot, "Armor modifier", 0.11));
		}
		return ret;
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, Player player, @Nullable ItemStack tool) {
		return hasArmorSet(player) ? 0.1F : 0F;
	}

}
