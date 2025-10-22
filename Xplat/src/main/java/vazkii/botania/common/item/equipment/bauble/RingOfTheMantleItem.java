/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.helper.EntityHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.xplat.XplatAbstractions;

public class RingOfTheMantleItem extends BaubleItem {

	public static final int MANA_COST = 5;
	public static final int HASTE_AMPLIFIER = 0; // Haste 1
	public static final float ORE_TARGET_SPEED = 3.0f;
	public static final float STONE_TARGET_SPEED = 1.5f;
	public static final float MAX_SPEED_BOOST = 2.0f;

	public RingOfTheMantleItem(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (!(entity instanceof Player player) || player.level().isClientSide) {
			return;
		}
		boolean hasMana = ManaItemHandler.instance().requestManaExact(stack, player, MANA_COST, false);
		if (!hasMana) {
			onUnequipped(stack, player);
		} else {
			onEquipped(stack, player);
		}

		if (player.attackAnim == 0.25F) {
			ManaItemHandler.instance().requestManaExact(stack, player, MANA_COST, true);
		}
	}

	@Override
	public void onEquipped(ItemStack stack, LivingEntity living) {
		boolean hasMana = living instanceof Player player
				&& ManaItemHandler.instance().requestManaExact(stack, player, MANA_COST, false);
		if (hasMana) {
			EntityHelper.addStaticEffect(living, MobEffects.DIG_SPEED, HASTE_AMPLIFIER);
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity living) {
		EntityHelper.removeStaticEffect(living, MobEffects.DIG_SPEED, HASTE_AMPLIFIER);
	}

	/**
	 * For blocks that must be mined with a pickaxe, the Ring of the Mantle reduces their hardness or "destroy speed"
	 * by up to 2 points. For ores this is capped at the hardness of typical stone ores, while for other blocks it is
	 * capped at the typical hardness of natural stone.
	 */
	public static float getModifiedBlockDestroySpeed(BlockState state, Player player, float originalDestroySpeed) {
		if (!state.requiresCorrectToolForDrops() || !state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
			return originalDestroySpeed;
		}

		float targetSpeed = state.is(XplatAbstractions.instance().getOreTag()) ? ORE_TARGET_SPEED : STONE_TARGET_SPEED;
		if (targetSpeed >= originalDestroySpeed || EquipmentHandler.findOrEmpty(BotaniaItems.miningRing, player).isEmpty()) {
			return originalDestroySpeed;
		}

		return Math.max(targetSpeed, originalDestroySpeed - MAX_SPEED_BOOST);
	}

}
