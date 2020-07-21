/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.manaweave;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import javax.annotation.Nullable;

import java.util.function.Consumer;

public class ItemManaweaveHelm extends ItemManaweaveArmor implements IManaDiscountArmor, IManaProficiencyArmor {

	private static final int MANA_PER_DAMAGE = 30;

	public ItemManaweaveHelm(Settings props) {
		super(EquipmentSlot.HEAD, props);
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, PlayerEntity player, @Nullable ItemStack tool) {
		return hasArmorSet(player) ? 0.35F : 0F;
	}

	@Override
	public boolean shouldGiveProficiency(ItemStack stack, EquipmentSlot slot, PlayerEntity player, ItemStack rod) {
		return hasArmorSet(player);
	}

	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		if (!world.isClient && stack.getDamage() > 0 && ManaItemHandler.instance().requestManaExact(stack, player, MANA_PER_DAMAGE * 2, true)) {
			stack.setDamage(stack.getDamage() - 1);
		}
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return ToolCommons.damageItemIfPossible(stack, amount, entity, MANA_PER_DAMAGE);
	}
}
