/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.entity.EntityThornChakram;

import javax.annotation.Nonnull;

public class ItemThornChakram extends Item {

	public ItemThornChakram(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!world.isClientSide) {
			ItemStack copy = stack.copy();
			copy.setCount(1);
			EntityThornChakram c = new EntityThornChakram(player, world, copy);
			c.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);

			SoundEvent sound = ModSounds.thornChakramThrow;
			if (stack.is(ModItems.flareChakram)) {
				c.setFire(true);
				sound = ModSounds.flareChakramThrow;
			}

			world.addFreshEntity(c);
			world.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.PLAYERS, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
			stack.shrink(1);
		}

		return InteractionResultHolder.success(stack);
	}

}
