/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.manaweave;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.item.ManaProficiencyArmor;
import vazkii.botania.api.mana.ManaDiscountArmor;

public class ManaweaveHelmItem extends ManaweaveArmorItem implements ManaDiscountArmor, ManaProficiencyArmor {

	public ManaweaveHelmItem(Properties props) {
		super(Type.HELMET, props);
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, Player player, @Nullable ItemStack tool) {
		return hasArmorSet(player) ? 0.4F : 0F;
	}

	@Override
	public boolean shouldGiveProficiency(ItemStack stack, EquipmentSlot slot, Player player, ItemStack rod) {
		return hasArmorSet(player);
	}

	@Override
	protected int getManaPerDamage() {
		return 30;
	}
}
