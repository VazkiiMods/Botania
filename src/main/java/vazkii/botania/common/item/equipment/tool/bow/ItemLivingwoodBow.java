/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.bow;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import javax.annotation.Nonnull;

import java.util.function.Consumer;

public class ItemLivingwoodBow extends BowItem implements IManaUsingItem {
	public static final int MANA_PER_DAMAGE = 40;

	public ItemLivingwoodBow(Settings builder) {
		super(builder);
	}

	// [VanillaCopy] super
	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(@Nonnull World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
		ItemStack itemstack = playerIn.getStackInHand(handIn);
		boolean flag = canFire(itemstack, playerIn); // Botania - custom check

		TypedActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
		if (ret != null) {
			return ret;
		}

		if (!playerIn.abilities.creativeMode && !flag) {
			return flag ? new TypedActionResult<>(ActionResult.PASS, itemstack) : new TypedActionResult<>(ActionResult.FAIL, itemstack);
		} else {
			playerIn.setCurrentHand(handIn);
			return new TypedActionResult<>(ActionResult.SUCCESS, itemstack);
		}
	}

	// [VanillaCopy] super
	@Override
	public void onStoppedUsing(@Nonnull ItemStack stack, @Nonnull World worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity playerentity = (PlayerEntity) entityLiving;
			boolean flag = canFire(stack, playerentity); // Botania - custom check
			ItemStack itemstack = playerentity.getArrowType(stack);

			int i = (int) ((getMaxUseTime(stack) - timeLeft) * chargeVelocityMultiplier()); // Botania - velocity multiplier
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity, i, !itemstack.isEmpty() || flag);
			if (i < 0) {
				return;
			}

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}

				float f = getPullProgress(i);
				if (!((double) f < 0.1D)) {
					boolean flag1 = playerentity.abilities.creativeMode || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
					if (!worldIn.isClient) {
						ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
						PersistentProjectileEntity abstractarrowentity = arrowitem.createArrow(worldIn, itemstack, playerentity);
						abstractarrowentity = customArrow(abstractarrowentity);
						abstractarrowentity.setProperties(playerentity, playerentity.pitch, playerentity.yaw, 0.0F, f * 3.0F, 1.0F);
						if (f == 1.0F) {
							abstractarrowentity.setCritical(true);
						}

						int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
						if (j > 0) {
							abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double) j * 0.5D + 0.5D);
						}

						int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
						if (k > 0) {
							abstractarrowentity.setPunch(k);
						}

						if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
							abstractarrowentity.setOnFireFor(100);
						}

						// Botania - move damage into onFire
						onFire(stack, playerentity, flag1, abstractarrowentity);
						if (flag1 || playerentity.abilities.creativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
							abstractarrowentity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
						}

						worldIn.spawnEntity(abstractarrowentity);
					}

					worldIn.playSound((PlayerEntity) null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (RANDOM.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!flag1 && !playerentity.abilities.creativeMode) {
						itemstack.decrement(1);
						if (itemstack.isEmpty()) {
							playerentity.inventory.removeOne(itemstack);
						}
					}

					playerentity.incrementStat(Stats.USED.getOrCreateStat(this));
				}
			}
		}
	}

	public float chargeVelocityMultiplier() {
		return 1F;
	}

	boolean canFire(ItemStack stack, PlayerEntity player) {
		return player.abilities.creativeMode
				|| EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0
				|| !player.getArrowType(stack).isEmpty();
	}

	void onFire(ItemStack bow, LivingEntity living, boolean infinity, PersistentProjectileEntity arrow) {
		ToolCommons.damageItem(bow, 1, living, MANA_PER_DAMAGE);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {
		if (!world.isClient && player instanceof PlayerEntity && stack.getDamage() > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, (PlayerEntity) player, MANA_PER_DAMAGE * 2, true)) {
			stack.setDamage(stack.getDamage() - 1);
		}
	}

	@Override
	public boolean canRepair(ItemStack bow, ItemStack material) {
		return material.getItem() == ModItems.livingwoodTwig || super.canRepair(bow, material);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return ToolCommons.damageItemIfPossible(stack, amount, entity, MANA_PER_DAMAGE);
	}
}
