/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import vazkii.botania.common.BotaniaDamageTypes;
import vazkii.botania.common.component.BotaniaDataComponents;

import java.util.*;

/**
 * An item that implements this can have Ancient Wills
 * crafted onto it.
 */
public interface AncientWillContainer {
	enum AncientWillType {
		AHRIM,
		DHAROK,
		GUTHAN,
		TORAG,
		VERAC,
		KARIL
	}

	static void addAncientWill(ItemStack stack, AncientWillType will) {
		Set<String> setOfWills = new TreeSet<>(getAncientWillsList(stack));
		setOfWills.add(getWillId(will));
		stack.set(BotaniaDataComponents.ANCIENT_WILLS, new ArrayList<>(setOfWills));
	}

	private static String getWillId(AncientWillType will) {
		return will.name().toLowerCase(Locale.ROOT);
	}

	@Unmodifiable
	private static List<String> getAncientWillsList(ItemStack stack) {
		return stack.getOrDefault(BotaniaDataComponents.ANCIENT_WILLS, Collections.emptyList());
	}

	static boolean hasAncientWill(ItemStack stack, AncientWillType will) {
		return getAncientWillsList(stack).contains(getWillId(will));
	}

	static float getCritDamageMult(Player player) {
		ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
		if (stack.getItem() instanceof AncientWillContainer awc && awc.hasFullArmorSet(player)) {
			if (!stack.isEmpty() && AncientWillContainer.hasAncientWill(stack, AncientWillType.DHAROK)) {
				return 1.0F + (1.0F - player.getHealth() / player.getMaxHealth()) * 0.5F;
			}
		}

		return 1.0F;
	}

	static boolean hasAnyWill(ItemStack stack) {
		for (AncientWillType type : AncientWillType.values()) {
			if (AncientWillContainer.hasAncientWill(stack, type)) {
				return true;
			}
		}

		return false;
	}

	static DamageSource onEntityAttacked(DamageSource source, float amount, Player player, LivingEntity entity) {
		ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
		if (stack.getItem() instanceof AncientWillContainer awc && awc.hasFullArmorSet(player)) {
			// TODO: might be worth refactoring this to only get the set of wills once
			if (AncientWillContainer.hasAncientWill(stack, AncientWillType.AHRIM)) {
				entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 1));
			}
			if (AncientWillContainer.hasAncientWill(stack, AncientWillType.GUTHAN)) {
				player.heal(amount * 0.25F);
			}
			if (AncientWillContainer.hasAncientWill(stack, AncientWillType.TORAG)) {
				entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
			}
			if (AncientWillContainer.hasAncientWill(stack, AncientWillType.VERAC)) {
				source = BotaniaDamageTypes.Sources.playerAttackArmorPiercing(player.level().registryAccess(), player);
			}
			if (AncientWillContainer.hasAncientWill(stack, AncientWillType.KARIL)) {
				entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 1));
			}
		}
		return source;
	}

	static void addAncientWillDescription(ItemStack stack, List<Component> list){
		for (AncientWillType type : AncientWillType.values()) {
			if (hasAncientWill(stack, type)) {
				list.add(Component.translatable("botania.armorset.will_" + getWillId(type) + ".desc").withStyle(ChatFormatting.GRAY));
			}
		}
	}

	boolean hasFullArmorSet(Player player);
}
