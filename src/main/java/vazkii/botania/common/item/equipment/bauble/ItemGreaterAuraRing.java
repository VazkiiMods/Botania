/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 24, 2014, 5:12:53 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.integration.curios.BaseCurio;

public class ItemGreaterAuraRing extends ItemBauble implements IManaGivingItem {

	public ItemGreaterAuraRing(Properties props) {
		super(props);
	}

	public static class Curio extends BaseCurio {
		public Curio(ItemStack stack) {
			super(stack);
		}

		@Override
		public void onCurioTick(String identifier, LivingEntity player) {
			if(player instanceof PlayerEntity && player.ticksExisted % 2 == 0)
				ManaItemHandler.dispatchManaExact(stack, (PlayerEntity) player, 1, true);
		}
	}
}
