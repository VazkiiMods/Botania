/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.annotations.SoftImplement;
import vazkii.botania.common.item.equipment.tool.manasteel.ManasteelAxeItem;

import java.util.Optional;

public class ElementiumAxeItem extends ManasteelAxeItem {

	public ElementiumAxeItem(Properties props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), props.attributes(ElementiumAxeItem.createAttributes(BotaniaAPI.instance().getElementiumItemTier(), 6F, -3.1F)));
	}

	@SoftImplement("IItemExtension")
	public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
		Optional<HolderSet<Item>> primaryItems = enchantment.value().definition().primaryItems();
		return this.supportsEnchantment(stack, enchantment) && (enchantment.is(Enchantments.LOOTING) || primaryItems.isEmpty() || stack.is(primaryItems.get()));
	}

	@SoftImplement("IItemExtension")
	public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
		return enchantment.is(Enchantments.LOOTING) || enchantment.value().isSupportedItem(stack);
	}

	// [VanillaCopy] modified from DiggerItem::hurtEnemy, actually same as SwordItem::hurtEnemy
	@Override
	public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		// only do 1 durability damage, since this is primarily a weapon
		stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
	}

}
