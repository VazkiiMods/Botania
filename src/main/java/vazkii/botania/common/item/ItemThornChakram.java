/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2015, 6:42:47 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import vazkii.botania.common.entity.EntityThornChakram;

import javax.annotation.Nonnull;

public class ItemThornChakram extends Item {

	public ItemThornChakram(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand)  {
		ItemStack stack = player.getHeldItem(hand);

		if(!world.isRemote) {
			ItemStack copy = stack.copy();
			copy.setCount(1);
			EntityThornChakram c = new EntityThornChakram(player, world, copy);
			c.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
			if(stack.getItem() == ModItems.flareChakram)
				c.setFire(true);
			world.addEntity(c);
			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
			stack.shrink(1);
		}

		return ActionResult.success(stack);
	}

}
