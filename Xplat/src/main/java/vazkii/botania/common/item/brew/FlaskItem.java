/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.item.brew;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.entity.EnderAirCloudEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.item.BotaniaItems;

public class FlaskItem extends VialItem {
	public FlaskItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
		ItemStack brewStack = new ItemStack(BotaniaItems.BREW_FLASK);
		BaseBrewItem.setBrew(brewStack, brew);
		return brewStack;
	}

	@Override
	public int getManaCost(Brew brew, ItemStack stack) {
		return brew.getManaCost() * 2;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(player,
				e -> !e.isSpectator() && (e.isPickable() || e instanceof EnderAirCloudEntity),
				player.entityInteractionRange());
		if (!(hitResult instanceof EntityHitResult entityHitResult)
				|| !(entityHitResult.getEntity() instanceof EnderAirCloudEntity cloud)
				|| !player.mayInteract(level, cloud.blockPosition())) {
			return super.use(level, player, usedHand);
		}

		ItemStack stackInHand = player.getItemInHand(usedHand);
		player.playSound(BotaniaSounds.enderEssenceFill, 1, 1);
		player.awardStat(Stats.ITEM_USED.get(stackInHand.getItem()));
		level.gameEvent(player, GameEvent.FLUID_PICKUP, cloud.position());
		if (player instanceof ServerPlayer serverplayer) {
			CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(serverplayer, stackInHand, cloud);
		}

		ItemStack filledResult = ItemUtils.createFilledResult(stackInHand, player, cloud.getBottledItem());
		cloud.discard();

		return InteractionResultHolder.sidedSuccess(filledResult, level.isClientSide());
	}

}
