/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 28, 2015, 8:47:04 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.armor.manaweave;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemManaweaveHelm extends ItemManaweaveArmor implements IManaDiscountArmor, IManaProficiencyArmor {

	private static final int MANA_PER_DAMAGE = 30;

	public ItemManaweaveHelm() {
		super(EntityEquipmentSlot.HEAD, LibItemNames.MANAWEAVE_HELM);
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, EntityPlayer player, @Nullable ItemStack tool) {
		return hasArmorSet(player) ? 0.35F : 0F;
	}

	@Override
	public boolean shouldGiveProficiency(ItemStack stack, EntityEquipmentSlot slot, EntityPlayer player) {
		return hasArmorSet(player);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		if(!world.isRemote && stack.getItemDamage() > 0 && ManaItemHandler.requestManaExact(stack, player, MANA_PER_DAMAGE * 2, true))
			stack.setItemDamage(stack.getItemDamage() - 1);
	}

	@Override
	public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
		ToolCommons.damageItem(stack, damage, entity, MANA_PER_DAMAGE);
	}

}
