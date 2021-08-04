/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.bow;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import javax.annotation.Nonnull;

import java.util.function.Consumer;

public class ItemLivingwoodBow extends BowItem implements IManaUsingItem {
	public static final int MANA_PER_DAMAGE = 40;

	public ItemLivingwoodBow(Properties builder) {
		super(builder);
	}

	// [VanillaCopy] super
	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(@Nonnull Level worldIn, Player playerIn, @Nonnull InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		boolean flag = canFire(itemstack, playerIn); // Botania - custom check

		if (!playerIn.abilities.instabuild && !flag) {
			return InteractionResultHolder.fail(itemstack);
		} else {
			playerIn.startUsingItem(handIn);
			return InteractionResultHolder.consume(itemstack);
		}
	}

	// [VanillaCopy] super
	@Override
	public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player) {
			Player playerentity = (Player) entityLiving;
			boolean flag = canFire(stack, playerentity); // Botania - custom check
			ItemStack itemstack = playerentity.getProjectile(stack);

			int i = (int) ((getUseDuration(stack) - timeLeft) * chargeVelocityMultiplier()); // Botania - velocity multiplier
			if (i < 0) {
				return;
			}

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}

				float f = getPowerForTime(i);
				if (!((double) f < 0.1D)) {
					boolean flag1 = playerentity.abilities.instabuild || itemstack.getItem() == Items.ARROW;
					if (!worldIn.isClientSide) {
						ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
						AbstractArrow abstractarrowentity = arrowitem.createArrow(worldIn, itemstack, playerentity);
						abstractarrowentity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot, 0.0F, f * 3.0F, 1.0F);
						if (f == 1.0F) {
							abstractarrowentity.setCritArrow(true);
						}

						int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
						if (j > 0) {
							abstractarrowentity.setBaseDamage(abstractarrowentity.getBaseDamage() + (double) j * 0.5D + 0.5D);
						}

						int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
						if (k > 0) {
							abstractarrowentity.setKnockback(k);
						}

						if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
							abstractarrowentity.setSecondsOnFire(100);
						}

						// Botania - onFire
						onFire(stack, playerentity, flag1, abstractarrowentity);
						stack.hurtAndBreak(1, playerentity, (p_220009_1_) -> {
							p_220009_1_.broadcastBreakEvent(playerentity.getUsedItemHand());
						});
						if (flag1 || playerentity.abilities.instabuild && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
							abstractarrowentity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
						}

						worldIn.addFreshEntity(abstractarrowentity);
					}

					worldIn.playSound((Player) null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!flag1 && !playerentity.abilities.instabuild) {
						itemstack.shrink(1);
						if (itemstack.isEmpty()) {
							playerentity.inventory.removeItem(itemstack);
						}
					}

					playerentity.awardStat(Stats.ITEM_USED.get(this));
				}
			}
		}
	}

	public float chargeVelocityMultiplier() {
		return 1F;
	}

	boolean canFire(ItemStack stack, Player player) {
		return player.getAbilities().instabuild
				|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0
				|| !player.getProjectile(stack).isEmpty();
	}

	void onFire(ItemStack bow, LivingEntity living, boolean infinity, AbstractArrow arrow) {}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity player, int slot, boolean selected) {
		if (!world.isClientSide && player instanceof Player && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, (Player) player, MANA_PER_DAMAGE * 2, true)) {
			stack.setDamageValue(stack.getDamageValue() - 1);
		}
	}

	@Override
	public boolean isValidRepairItem(ItemStack bow, ItemStack material) {
		return material.getItem() == ModItems.livingwoodTwig || super.isValidRepairItem(bow, material);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	public static <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return ToolCommons.damageItemIfPossible(stack, amount, entity, MANA_PER_DAMAGE);
	}
}
