/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.advancements.RelicBindTrigger;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.List;
import java.util.UUID;

public class ItemRelic extends Item implements IRelic {

	private static final String TAG_SOULBIND_UUID = "soulbindUUID";

	public ItemRelic(Properties props) {
		super(props);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (!world.isClientSide && entity instanceof Player) {
			updateRelic(stack, (Player) entity);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
		if (!hasUUID(stack)) {
			tooltip.add(new TranslatableComponent("botaniamisc.relicUnbound"));
		} else {
			if (!getSoulbindUUID(stack).equals(Minecraft.getInstance().player.getUUID())) {
				tooltip.add(new TranslatableComponent("botaniamisc.notYourSagittarius"));
			} else {
				tooltip.add(new TranslatableComponent("botaniamisc.relicSoulbound", Minecraft.getInstance().player.getName()));
			}
		}
	}

	public boolean shouldDamageWrongPlayer() {
		return true;
	}

	public void updateRelic(ItemStack stack, Player player) {
		if (stack.isEmpty() || !(stack.getItem() instanceof IRelic)) {
			return;
		}

		boolean rightPlayer = true;

		if (!hasUUID(stack)) {
			bindToUUID(player.getUUID(), stack);
			if (player instanceof ServerPlayer) {
				RelicBindTrigger.INSTANCE.trigger((ServerPlayer) player, stack);
			}
		} else if (!getSoulbindUUID(stack).equals(player.getUUID())) {
			rightPlayer = false;
		}

		if (!rightPlayer && player.tickCount % 10 == 0 && (!(stack.getItem() instanceof ItemRelic) || ((ItemRelic) stack.getItem()).shouldDamageWrongPlayer())) {
			player.hurt(damageSource(), 2);
		}
	}

	public boolean isRightPlayer(Player player, ItemStack stack) {
		return hasUUID(stack) && getSoulbindUUID(stack).equals(player.getUUID());
	}

	public static DamageSource damageSource() {
		return new DamageSource("botania-relic") {};
	}

	@Override
	public void bindToUUID(UUID uuid, ItemStack stack) {
		ItemNBTHelper.setString(stack, TAG_SOULBIND_UUID, uuid.toString());
	}

	@Override
	public UUID getSoulbindUUID(ItemStack stack) {
		if (ItemNBTHelper.verifyExistance(stack, TAG_SOULBIND_UUID)) {
			try {
				return UUID.fromString(ItemNBTHelper.getString(stack, TAG_SOULBIND_UUID, ""));
			} catch (IllegalArgumentException ex) { // Bad UUID in tag
				ItemNBTHelper.removeEntry(stack, TAG_SOULBIND_UUID);
			}
		}

		return null;
	}

	@Override
	public boolean hasUUID(ItemStack stack) {
		return getSoulbindUUID(stack) != null;
	}
}
