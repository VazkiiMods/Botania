/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

import java.util.List;
import java.util.UUID;

public abstract class ItemRelicBauble extends ItemBauble implements IRelic {

	private final ItemRelic dummy = new ItemRelic(new Properties()); // Delegate for relic stuff

	public ItemRelicBauble(Properties props) {
		super(props);
	}

	public ItemRelic getDummy() {
		return dummy;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean held) {
		if (entity instanceof Player) {
			dummy.updateRelic(stack, (Player) entity);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, world, tooltip, flags);
		dummy.appendHoverText(stack, world, tooltip, flags);
	}

	@Override
	public void bindToUUID(UUID uuid, ItemStack stack) {
		dummy.bindToUUID(uuid, stack);
	}

	@Override
	public UUID getSoulbindUUID(ItemStack stack) {
		return dummy.getSoulbindUUID(stack);
	}

	@Override
	public boolean hasUUID(ItemStack stack) {
		return dummy.hasUUID(stack);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (entity instanceof Player ePlayer) {
			dummy.updateRelic(stack, ePlayer);
			if (dummy.isRightPlayer(ePlayer, stack)) {
				onValidPlayerWornTick(ePlayer);
			}
		}
	}

	public void onValidPlayerWornTick(Player player) {}

	@Override
	public boolean canEquip(ItemStack stack, LivingEntity entity) {
		return entity instanceof Player && dummy.isRightPlayer((Player) entity, stack);
	}
}
