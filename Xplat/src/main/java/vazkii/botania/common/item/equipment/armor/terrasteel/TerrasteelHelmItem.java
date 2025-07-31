/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.terrasteel;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.item.AncientWillContainer;
import vazkii.botania.api.mana.ManaDiscountArmor;
import vazkii.botania.api.mana.ManaItemHandler;

import java.util.*;

public class TerrasteelHelmItem extends TerrasteelArmorItem implements ManaDiscountArmor, AncientWillContainer {

	public TerrasteelHelmItem(Properties props) {
		super(Type.HELMET, props);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		if (!world.isClientSide && entity instanceof Player player && player.getInventory().armor.contains(stack) && hasArmorSet(player)) {
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
	public void addArmorSetDescription(ItemStack stack, List<Component> list) {
		super.addArmorSetDescription(stack, list);
		AncientWillContainer.addAncientWillDescription(stack, list);
	}

	@Override
	public boolean hasFullArmorSet(Player player) {
		return hasArmorSet(player);
	}
}
