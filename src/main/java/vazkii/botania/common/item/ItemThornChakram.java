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

import vazkii.botania.common.entity.EntityThornChakram;

import javax.annotation.Nonnull;

public class ItemThornChakram extends Item {

	public ItemThornChakram(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);

		if (!world.isClient) {
			ItemStack copy = stack.copy();
			copy.setCount(1);
			EntityThornChakram c = new EntityThornChakram(player, world, copy);
			c.setProperties(player, player.pitch, player.yaw, 0.0F, 1.5F, 1.0F);
			if (stack.getItem() == ModItems.flareChakram) {
				c.setFire(true);
			}
			world.spawnEntity(c);
			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
			stack.decrement(1);
		}

		return TypedActionResult.success(stack);
	}

}
