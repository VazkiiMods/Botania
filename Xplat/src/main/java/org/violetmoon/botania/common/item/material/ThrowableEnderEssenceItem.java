/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.material;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

import vazkii.botania.common.entity.EnderEssenceFlaskEntity;
import vazkii.botania.common.handler.BotaniaSounds;

public class ThrowableEnderEssenceItem extends Item implements ProjectileItem {
	public ThrowableEnderEssenceItem(Properties properties) {
		super(properties);
	}

	// [VanillaCopy] SplashPotionItem::use with inlined super call, except throwing an ender essence bottle
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		level.playSound(
				null,
				player.getX(),
				player.getY(),
				player.getZ(),
				BotaniaSounds.ENDER_ESSENCE_THROW,
				SoundSource.PLAYERS,
				0.5F,
				0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
		);
		// inlined from ThrowablePotionItem::use:
		ItemStack itemstack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			EnderEssenceFlaskEntity thrownBottle = new EnderEssenceFlaskEntity(player, level);
			// Botania: no need to set item, but apply different trajectory
			thrownBottle.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5f, 1);
			level.addFreshEntity(thrownBottle);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		itemstack.consume(1, player);
		return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
	}

	@Override
	public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
		return new EnderEssenceFlaskEntity(pos.x(), pos.y(), pos.z(), level);
	}
}
