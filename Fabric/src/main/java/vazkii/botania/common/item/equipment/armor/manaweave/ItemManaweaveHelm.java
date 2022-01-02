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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import javax.annotation.Nullable;

import java.util.function.Consumer;

public class ItemManaweaveHelm extends ItemManaweaveArmor implements IManaDiscountArmor, IManaProficiencyArmor {

	private static final int MANA_PER_DAMAGE = 30;

	public ItemManaweaveHelm(Properties props) {
		super(EquipmentSlot.HEAD, props);
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, Player player, @Nullable ItemStack tool) {
		return hasArmorSet(player) ? 0.35F : 0F;
	}

	@Override
	public boolean shouldGiveProficiency(ItemStack stack, EquipmentSlot slot, Player player, ItemStack rod) {
		return hasArmorSet(player);
	}

	@Override
	public void onArmorTick(ItemStack stack, Level world, Player player) {
		if (!world.isClientSide && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExact(stack, player, MANA_PER_DAMAGE * 2, true)) {
			stack.setDamageValue(stack.getDamageValue() - 1);
		}
	}

	public static <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return ToolCommons.damageItemIfPossible(stack, amount, entity, MANA_PER_DAMAGE);
	}
}
