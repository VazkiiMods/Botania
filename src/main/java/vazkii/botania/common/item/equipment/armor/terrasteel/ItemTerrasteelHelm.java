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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemTerrasteelHelm extends ItemTerrasteelArmor implements IManaDiscountArmor, IAncientWillContainer {

	public static final String TAG_ANCIENT_WILL = "AncientWill";

	public ItemTerrasteelHelm(Properties props) {
		super(EquipmentSlot.HEAD, props);
	}

	@Override
	public void onArmorTick(ItemStack stack, Level world, Player player) {
		super.onArmorTick(stack, world, player);
		if (!world.isClientSide && hasArmorSet(player)) {
			int food = player.getFoodData().getFoodLevel();
			if (food > 0 && food < 18 && player.isHurt() && player.tickCount % 80 == 0) {
				player.heal(1F);
			}
			if (player.tickCount % 10 == 0) {
				ManaItemHandler.instance().dispatchManaExact(stack, player, 10, true);
			}
		}
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, Player player, @Nullable ItemStack tool) {
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
	public void addArmorSetDescription(ItemStack stack, List<Component> list) {
		super.addArmorSetDescription(stack, list);
		for (AncientWillType type : AncientWillType.values()) {
			if (hasAncientWill(stack, type)) {
				list.add(new TranslatableComponent("botania.armorset.will_" + type.name().toLowerCase(Locale.ROOT) + ".desc").withStyle(ChatFormatting.GRAY));
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

	public float onCritDamageCalc(float amount, Player player) {
		if (hasArmorSet(player)) {
			ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemTerrasteelHelm
					&& hasAncientWill(stack, AncientWillType.DHAROK)) {
				return amount * (1F + (1F - player.getHealth() / player.getMaxHealth()) * 0.5F);
			}
		}
		return amount;
	}

	public void onEntityAttacked(DamageSource source, float amount, Player player, LivingEntity entity) {
		if (hasArmorSet(player)) {
			ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemTerrasteelHelm) {
				if (hasAncientWill(stack, AncientWillType.AHRIM)) {
					entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 1));
				}
				if (hasAncientWill(stack, AncientWillType.GUTHAN)) {
					player.heal(amount * 0.25F);
				}
				if (hasAncientWill(stack, AncientWillType.TORAG)) {
					entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
				}
				if (hasAncientWill(stack, AncientWillType.VERAC)) {
					source.isBypassArmor();
				}
				if (hasAncientWill(stack, AncientWillType.KARIL)) {
					entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 1));
				}
			}
		}
	}

}
