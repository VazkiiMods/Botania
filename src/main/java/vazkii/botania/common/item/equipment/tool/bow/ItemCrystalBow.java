/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 21, 2015, 6:33:40 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool.bow;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.lib.LibItemNames;

public class ItemCrystalBow extends ItemLivingwoodBow {

	private final int ARROW_COST = 200;

	public ItemCrystalBow(Properties builder) {
		super(builder);
	}

	@Override
	float chargeVelocityMultiplier() {
		return 2F;
	}

	@Override
	boolean canFire(ItemStack stack, PlayerEntity player) {
		int infinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
		return player.abilities.isCreativeMode || ManaItemHandler.requestManaExactForTool(stack, player, ARROW_COST / (infinity + 1), false);
	}

	@Override
	void onFire(ItemStack stack, LivingEntity living, boolean infinity, AbstractArrowEntity arrow) {
		arrow.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
		if(living instanceof PlayerEntity)
			ManaItemHandler.requestManaExactForTool(stack, (PlayerEntity) living, ARROW_COST / (infinity ? 2 : 1), true);
	}
}
