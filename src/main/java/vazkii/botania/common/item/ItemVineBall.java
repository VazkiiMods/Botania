/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import vazkii.botania.common.entity.EntityVineBall;

import javax.annotation.Nonnull;

public class ItemVineBall extends Item {

	public ItemVineBall(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		if (!player.abilities.creativeMode) {
			player.getStackInHand(hand).decrement(1);
		}

		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));

		if (!world.isClient) {
			EntityVineBall ball = new EntityVineBall(player, true);
			ball.setProperties(player, player.pitch, player.yaw, 0.0F, 1.5F, 1.0F);
			world.spawnEntity(ball);
		}

		return TypedActionResult.success(player.getStackInHand(hand));
	}

}
