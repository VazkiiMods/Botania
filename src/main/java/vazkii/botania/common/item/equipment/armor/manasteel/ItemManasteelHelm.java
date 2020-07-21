/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.manasteel;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import vazkii.botania.api.mana.IManaDiscountArmor;

import javax.annotation.Nullable;

public class ItemManasteelHelm extends ItemManasteelArmor implements IManaDiscountArmor {

	public ItemManasteelHelm(Settings props) {
		super(EquipmentSlot.HEAD, props);
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, PlayerEntity player, @Nullable ItemStack tool) {
		return hasArmorSet(player) ? 0.1F : 0F;
	}

}
