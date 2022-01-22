/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.mana.ManaItemHandler;

public class ItemInvisibilityCloak extends ItemBauble {

	public ItemInvisibilityCloak(Properties props) {
		super(props);
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity player) {
		MobEffectInstance effect = player.getEffect(MobEffects.INVISIBILITY);
		if (effect != null && player instanceof Player && effect.getAmplifier() == -42) {
			player.removeEffect(MobEffects.INVISIBILITY);
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		if (player instanceof Player && !player.level.isClientSide) {
			int manaCost = 2;
			boolean hasMana = ManaItemHandler.instance().requestManaExact(stack, (Player) player, manaCost, true);
			if (!hasMana) {
				onUnequipped(stack, player);
			} else {
				if (player.getEffect(MobEffects.INVISIBILITY) != null) {
					player.removeEffect(MobEffects.INVISIBILITY);
				}

				player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE, -42, true, true));
			}
		}
	}
}
