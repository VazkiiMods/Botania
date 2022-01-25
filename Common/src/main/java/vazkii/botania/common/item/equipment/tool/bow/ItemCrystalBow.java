/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.bow;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.util.function.Consumer;

public class ItemCrystalBow extends ItemLivingwoodBow {

	private static final int ARROW_COST = 200;

	public ItemCrystalBow(Properties builder) {
		super(builder);
	}

	@Override
	public float chargeVelocityMultiplier() {
		return 2F;
	}

	@Override
	boolean canFire(ItemStack stack, Player player) {
		boolean infinity = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
		return player.getAbilities().instabuild || ManaItemHandler.instance().requestManaExactForTool(stack, player, ARROW_COST / (infinity ? 2 : 1), false);
	}

	@Override
	void onFire(ItemStack stack, LivingEntity living, boolean infinity, AbstractArrow arrow) {
		arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		boolean infinity = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
		return ToolCommons.damageItemIfPossible(stack, amount, entity, ARROW_COST / (infinity ? 2 : 1));
	}
}
