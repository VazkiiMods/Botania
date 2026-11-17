/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.bow;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.component.BotaniaDataComponents;

import java.util.List;

public class CrystalBowItem extends BowItem {

	// unenchanted base cost per shot: arrow conjuration + damage prevention = 200
	private static final int ARROW_COST = 160;
	// typical end-game cost without armor bonus: infinity + unbreaking 3 = 90
	private static final int INFINITY_ARROW_COST = 80;

	public CrystalBowItem(Properties properties) {
		super(properties);
	}

	// [VanillaCopy] BowItem::use
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		boolean foundProjectile = !getProjectile(itemStack, player).isEmpty(); // Botania - check conjuration option
		if (!player.hasInfiniteMaterials() && !foundProjectile) {
			return InteractionResultHolder.fail(itemStack);
		}

		player.startUsingItem(hand);
		return InteractionResultHolder.consume(itemStack);
	}

	// [VanillaCopy] BowItem::releaseUsing
	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player player) {
			ItemStack projectile = getProjectile(stack, player); // Botania - potentially conjure an arrow
			if (projectile.isEmpty()) {
				return;
			}

			int timeHeld = (this.getUseDuration(stack, entityLiving) - timeLeft) * 2; // Botania - twice the draw speed
			float pow = getPowerForTime(timeHeld);
			if (pow < 0.1
					// Botania - consume mana if firing conjured projectile
					|| projectile.has(BotaniaDataComponents.CONJURED_PROJECTILE) && !ManaItemHandler.instance()
							.requestManaExactForTool(stack, player, getArrowCost(stack, player.level()), true)) {
				return;
			}

			List<ItemStack> firedProjectiles = draw(stack, projectile, player);
			if (level instanceof ServerLevel serverLevel && !firedProjectiles.isEmpty()) {
				this.shoot(serverLevel, player, player.getUsedItemHand(), stack, firedProjectiles,
						pow * 3, 1, pow == 1, null);
			}

			level.playSound(
					null,
					player.getX(),
					player.getY(),
					player.getZ(),
					SoundEvents.ARROW_SHOOT,
					SoundSource.PLAYERS,
					1,
					1f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + pow * 0.5f
			);
			player.awardStat(Stats.ITEM_USED.get(this));
		}
	}

	protected ItemStack getProjectile(ItemStack stack, Player player) {
		ItemStack projectile = player.getProjectile(stack);
		if ((projectile.isEmpty() || projectile.is(Items.ARROW)) && ManaItemHandler.instance()
				.requestManaExactForTool(stack, player, getArrowCost(stack, player.level()), false)) {
			// player has mana, fire a conjured arrow if player doesn't have special arrows
			ItemStack conjuredArrow = new ItemStack(Items.ARROW);
			conjuredArrow.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
			conjuredArrow.set(BotaniaDataComponents.CONJURED_PROJECTILE, Unit.INSTANCE);
			return conjuredArrow;
		}
		return projectile;
	}

	public int getArrowCost(ItemStack stack, Level level) {
		HolderLookup<Enchantment> lookup = level.holderLookup(Registries.ENCHANTMENT);
		boolean infinity = EnchantmentHelper.getItemEnchantmentLevel(lookup.getOrThrow(Enchantments.INFINITY), stack) > 0;
		return infinity ? INFINITY_ARROW_COST : ARROW_COST;
	}
}
