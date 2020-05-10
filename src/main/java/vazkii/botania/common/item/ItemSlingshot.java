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
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityVineBall;

import javax.annotation.Nonnull;

import java.util.function.Predicate;

public class ItemSlingshot extends Item {

	private static final Predicate<ItemStack> AMMO_FUNC = s -> s != null && s.getItem() == ModItems.vineBall;

	public ItemSlingshot(Properties builder) {
		super(builder);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity living, int duration) {
		int j = getUseDuration(stack) - duration;

		if (!world.isRemote && (!(living instanceof PlayerEntity) || ((PlayerEntity) living).abilities.isCreativeMode || PlayerHelper.hasAmmo((PlayerEntity) living, AMMO_FUNC))) {
			float f = j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if (f < 1F) {
				return;
			}

			if (living instanceof PlayerEntity && !((PlayerEntity) living).abilities.isCreativeMode) {
				PlayerHelper.consumeAmmo((PlayerEntity) living, AMMO_FUNC);
			}

			EntityVineBall ball = new EntityVineBall(living, false);
			ball.shoot(living, living.rotationPitch, living.rotationYaw, 0F, 1.5F, 1F);
			ball.setMotion(ball.getMotion().scale(1.6));
			world.addEntity(ball);
			world.playSound(null, living.getPosX(), living.getPosY(), living.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (player.abilities.isCreativeMode || PlayerHelper.hasAmmo(player, AMMO_FUNC)) {
			player.setActiveHand(hand);
			return ActionResult.resultSuccess(stack);
		}

		return ActionResult.resultPass(stack);
	}

}
