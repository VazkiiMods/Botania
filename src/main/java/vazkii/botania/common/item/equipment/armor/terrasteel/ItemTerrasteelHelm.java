/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.terrasteel;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.mixin.AccessorDamageSource;

import javax.annotation.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemTerrasteelHelm extends ItemTerrasteelArmor implements IManaDiscountArmor, IAncientWillContainer {

	public static final String TAG_ANCIENT_WILL = "AncientWill";

	public ItemTerrasteelHelm(Settings props) {
		super(EquipmentSlot.HEAD, props);
	}

	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		super.onArmorTick(stack, world, player);
		if (!world.isClient && hasArmorSet(player)) {
			int food = player.getHungerManager().getFoodLevel();
			if (food > 0 && food < 18 && player.canFoodHeal() && player.age % 80 == 0) {
				player.heal(1F);
			}
			ManaItemHandler.instance().dispatchManaExact(stack, player, 1, true);
		}
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, PlayerEntity player, @Nullable ItemStack tool) {
		return hasArmorSet(player) ? 0.2F : 0F;
	}

	@Override
	public void addAncientWill(ItemStack stack, AncientWillType will) {
		ItemNBTHelper.setBoolean(stack, TAG_ANCIENT_WILL + "_" + will.name().toLowerCase(Locale.ROOT), true);
	}

	@Override
	public boolean hasAncientWill(ItemStack stack, AncientWillType will) {
		return hasAncientWill_(stack, will);
	}

	private static boolean hasAncientWill_(ItemStack stack, AncientWillType will) {
		return ItemNBTHelper.getBoolean(stack, TAG_ANCIENT_WILL + "_" + will.name().toLowerCase(Locale.ROOT), false);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void addArmorSetDescription(ItemStack stack, List<Text> list) {
		super.addArmorSetDescription(stack, list);
		for (AncientWillType type : AncientWillType.values()) {
			if (hasAncientWill(stack, type)) {
				list.add(new TranslatableText("botania.armorset.will_" + type.name().toLowerCase(Locale.ROOT) + ".desc").formatted(Formatting.GRAY));
			}
		}
	}

	public static boolean hasAnyWill(ItemStack stack) {
		for (AncientWillType type : AncientWillType.values()) {
			if (hasAncientWill_(stack, type)) {
				return true;
			}
		}

		return false;
	}

	private float onEntityAttacked(DamageSource source, float amount, PlayerEntity player, Entity entity) {
		if (entity instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) entity;
			if (hasArmorSet(player)) {
				// TODO 1.16 Move to mixin, this does not actually work as it's triggered after the attack strength changes
				// [VanillaCopy] crit logic from PlayerEntity.attackTargetEntityWithCurrentItem
				boolean strong = player.getAttackCooldownProgress(0.5F) > 0.9F;
				boolean crit = strong && player.fallDistance > 0.0F && !player.isOnGround() && !player.isClimbing() && !player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.BLINDNESS) && !player.hasVehicle();
				crit = crit && !player.isSprinting();

				ItemStack stack = player.getEquippedStack(EquipmentSlot.HEAD);
				if (crit && !stack.isEmpty() && stack.getItem() instanceof ItemTerrasteelHelm) {
					if (hasAncientWill(stack, AncientWillType.AHRIM)) {
						living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20, 1));
					}
					if (hasAncientWill(stack, AncientWillType.GUTHAN)) {
						player.heal(amount * 0.25F);
					}
					if (hasAncientWill(stack, AncientWillType.TORAG)) {
						living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1));
					}
					if (hasAncientWill(stack, AncientWillType.VERAC)) {
						((AccessorDamageSource) source).callSetBypassesArmor();
					}
					if (hasAncientWill(stack, AncientWillType.KARIL)) {
						living.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 60, 1));
					}
					if (hasAncientWill(stack, AncientWillType.DHAROK)) {
						return amount * (1F + (1F - player.getHealth() / player.getMaxHealth()) * 0.5F);
					}
				}
			}
		}
		return amount;
	}

}
