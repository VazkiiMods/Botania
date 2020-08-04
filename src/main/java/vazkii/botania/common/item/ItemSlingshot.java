/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityVineBall;

import javax.annotation.Nonnull;

import java.util.function.Predicate;

public class ItemSlingshot extends Item {

	private static final Predicate<ItemStack> AMMO_FUNC = s -> s != null && s.getItem() == ModItems.vineBall;

	public ItemSlingshot(Settings builder) {
		super(builder);
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity living, int duration) {
		int j = getMaxUseTime(stack) - duration;

		if (!world.isClient && (!(living instanceof PlayerEntity) || ((PlayerEntity) living).abilities.creativeMode || PlayerHelper.hasAmmo((PlayerEntity) living, AMMO_FUNC))) {
			float f = j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if (f < 1F) {
				return;
			}

			if (living instanceof PlayerEntity && !((PlayerEntity) living).abilities.creativeMode) {
				PlayerHelper.consumeAmmo((PlayerEntity) living, AMMO_FUNC);
			}

			EntityVineBall ball = new EntityVineBall(living, false);
			ball.setProperties(living, living.pitch, living.yaw, 0F, 1.5F, 1F);
			ball.setVelocity(ball.getVelocity().multiply(1.6));
			world.spawnEntity(ball);
			world.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
		}
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (player.abilities.creativeMode || PlayerHelper.hasAmmo(player, AMMO_FUNC)) {
			player.setCurrentHand(hand);
			return TypedActionResult.consume(stack);
		}

		return TypedActionResult.pass(stack);
	}

}
