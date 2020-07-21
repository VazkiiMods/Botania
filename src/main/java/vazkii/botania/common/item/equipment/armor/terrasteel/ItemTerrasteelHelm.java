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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemTerrasteelHelm extends ItemTerrasteelArmor implements IManaDiscountArmor, IAncientWillContainer, IManaGivingItem {

	public static final String TAG_ANCIENT_WILL = "AncientWill";

	public ItemTerrasteelHelm(Settings props) {
		super(EquipmentSlot.HEAD, props);
		MinecraftForge.EVENT_BUS.addListener(this::onEntityAttacked);
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

	private void onEntityAttacked(LivingHurtEvent event) {
		Entity attacker = event.getSource().getSource();
		if (attacker instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) attacker;
			if (hasArmorSet(player)) {
				// [VanillaCopy] crit logic from PlayerEntity.attackTargetEntityWithCurrentItem
				boolean strong = player.getAttackCooldownProgress(0.5F) > 0.9F;
				boolean crit = strong && player.fallDistance > 0.0F && !player.isOnGround() && !player.isClimbing() && !player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.BLINDNESS) && !player.hasVehicle();
				crit = crit && !player.isSprinting();

				ItemStack stack = player.getEquippedStack(EquipmentSlot.HEAD);
				if (crit && !stack.isEmpty() && stack.getItem() instanceof ItemTerrasteelHelm) {
					if (hasAncientWill(stack, AncientWillType.AHRIM)) {
						event.getEntityLiving().addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20, 1));
					}
					if (hasAncientWill(stack, AncientWillType.DHAROK)) {
						event.setAmount(event.getAmount() * (1F + (1F - player.getHealth() / player.getMaxHealth()) * 0.5F));
					}
					if (hasAncientWill(stack, AncientWillType.GUTHAN)) {
						player.heal(event.getAmount() * 0.25F);
					}
					if (hasAncientWill(stack, AncientWillType.TORAG)) {
						event.getEntityLiving().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1));
					}
					if (hasAncientWill(stack, AncientWillType.VERAC)) {
						event.getSource().setBypassesArmor();
					}
					if (hasAncientWill(stack, AncientWillType.KARIL)) {
						event.getEntityLiving().addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 60, 1));
					}
				}
			}
		}
	}

}
