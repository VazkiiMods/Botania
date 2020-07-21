/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.UUID;

public abstract class ItemRelicBauble extends ItemBauble implements IRelic {

	private final ItemRelic dummy = new ItemRelic(new Settings()); // Delegate for relic stuff

	public ItemRelicBauble(Settings props) {
		super(props);
	}

	public ItemRelic getDummy() {
		return dummy;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean held) {
		if (entity instanceof PlayerEntity) {
			dummy.updateRelic(stack, (PlayerEntity) entity);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void addHiddenTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext flags) {
		super.addHiddenTooltip(stack, world, tooltip, flags);
		dummy.addBindInfo(tooltip, stack);
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

	@Nonnull
	@Override
	public Rarity getRarity(ItemStack stack) {
		return BotaniaAPI.instance().getRelicRarity();
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity ePlayer = (PlayerEntity) entity;
			dummy.updateRelic(stack, ePlayer);
			if (dummy.isRightPlayer(ePlayer, stack)) {
				onValidPlayerWornTick(ePlayer);
			}
		}
	}

	public void onValidPlayerWornTick(PlayerEntity player) {}

	@Override
	public boolean canEquip(ItemStack stack, LivingEntity entity) {
		return entity instanceof PlayerEntity && dummy.isRightPlayer((PlayerEntity) entity, stack);
	}
}
