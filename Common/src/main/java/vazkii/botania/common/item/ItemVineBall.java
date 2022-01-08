/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.entity.EntityVineBall;

import javax.annotation.Nonnull;

public class ItemVineBall extends Item {

	public ItemVineBall(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		if (!player.getAbilities().instabuild) {
			player.getItemInHand(hand).shrink(1);
		}

		world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.vineBallThrow, SoundSource.NEUTRAL, 1F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));

		if (!world.isClientSide) {
			EntityVineBall ball = new EntityVineBall(player, true);
			ball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
			world.addFreshEntity(ball);
		}

		return InteractionResultHolder.success(player.getItemInHand(hand));
	}

}
