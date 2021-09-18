/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityVineBall;

import javax.annotation.Nonnull;

import java.util.function.Predicate;

public class ItemSlingshot extends Item {

	private static final Predicate<ItemStack> AMMO_FUNC = s -> s != null && s.is(ModItems.vineBall);

	public ItemSlingshot(Properties builder) {
		super(builder);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity living, int duration) {
		int j = getUseDuration(stack) - duration;

		if (!world.isClientSide && (!(living instanceof Player) || ((Player) living).getAbilities().instabuild || PlayerHelper.hasAmmo((Player) living, AMMO_FUNC))) {
			float f = j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if (f < 1F) {
				return;
			}

			if (living instanceof Player && !((Player) living).getAbilities().instabuild) {
				PlayerHelper.consumeAmmo((Player) living, AMMO_FUNC);
			}

			EntityVineBall ball = new EntityVineBall(living, false);
			ball.shootFromRotation(living, living.getXRot(), living.getYRot(), 0F, 1.5F, 1F);
			ball.setDeltaMovement(ball.getDeltaMovement().scale(1.6));
			world.addFreshEntity(ball);
			world.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5F, 0.4F / (living.getRandom().nextFloat() * 0.4F + 0.8F));
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Nonnull
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.getAbilities().instabuild || PlayerHelper.hasAmmo(player, AMMO_FUNC)) {
			return ItemUtils.startUsingInstantly(world, player, hand);
		}

		return InteractionResultHolder.pass(stack);
	}

}
