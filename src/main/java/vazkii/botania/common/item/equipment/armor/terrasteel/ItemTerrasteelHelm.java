/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.terrasteel;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
		super(EquipmentSlotType.HEAD, props);
	}

	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		super.onArmorTick(stack, world, player);
		if (!world.isRemote && hasArmorSet(player)) {
			int food = player.getFoodStats().getFoodLevel();
			if (food > 0 && food < 18 && player.shouldHeal() && player.ticksExisted % 80 == 0) {
				player.heal(1F);
			}
			if (player.ticksExisted % 10 == 0) {
				ManaItemHandler.instance().dispatchManaExact(stack, player, 10, true);
			}
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
	@OnlyIn(Dist.CLIENT)
	public void addArmorSetDescription(ItemStack stack, List<ITextComponent> list) {
		super.addArmorSetDescription(stack, list);
		for (AncientWillType type : AncientWillType.values()) {
			if (hasAncientWill(stack, type)) {
				list.add(new TranslationTextComponent("botania.armorset.will_" + type.name().toLowerCase(Locale.ROOT) + ".desc").mergeStyle(TextFormatting.GRAY));
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

	public float onCritDamageCalc(float amount, PlayerEntity player) {
		if (hasArmorSet(player)) {
			ItemStack stack = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemTerrasteelHelm
					&& hasAncientWill(stack, AncientWillType.DHAROK)) {
				return amount * (1F + (1F - player.getHealth() / player.getMaxHealth()) * 0.5F);
			}
		}
		return amount;
	}

	public void onEntityAttacked(DamageSource source, float amount, PlayerEntity player, LivingEntity entity) {
		if (hasArmorSet(player)) {
			ItemStack stack = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemTerrasteelHelm) {
				if (hasAncientWill(stack, AncientWillType.AHRIM)) {
					entity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 20, 1));
				}
				if (hasAncientWill(stack, AncientWillType.GUTHAN)) {
					player.heal(amount * 0.25F);
				}
				if (hasAncientWill(stack, AncientWillType.TORAG)) {
					entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 60, 1));
				}
				if (hasAncientWill(stack, AncientWillType.VERAC)) {
					source.setDamageBypassesArmor();
				}
				if (hasAncientWill(stack, AncientWillType.KARIL)) {
					entity.addPotionEffect(new EffectInstance(Effects.WITHER, 60, 1));
				}
			}
		}
	}

}
