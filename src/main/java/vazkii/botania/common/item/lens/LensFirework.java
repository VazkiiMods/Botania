/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [15/11/2015, 19:12:41 (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.item.FireworkRocketEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class LensFirework extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, ThrowableEntity entity, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if(!entity.world.isRemote && !burst.isFake()) {
			BlockPos coords = burst.getBurstSourceBlockPos();
			if(pos.entity == null && !isManaBlock && !pos.getBlockPos().equals(coords)) {
				ItemStack fireworkStack = generateFirework(burst.getColor());

				FireworkRocketEntity rocket = new FireworkRocketEntity(entity.world, entity.posX, entity.posY, entity.posZ, fireworkStack);
				entity.world.spawnEntity(rocket);
			}
		} else dead = false;

		return dead;
	}

	private ItemStack generateFirework(int color) {
		ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
		CompoundNBT explosion = new CompoundNBT();
		explosion.putIntArray("Colors", new int[] { color });

		int type = 1;
		double rand = Math.random();
		if(rand > 0.25) {
			if(rand > 0.9)
				type = 2;
			else type = 0;
		}

		explosion.putInt("Type", type);

		if(Math.random() < 0.05)
			if(Math.random() < 0.5)
				explosion.putBoolean("Flicker", true);
			else explosion.putBoolean("Trail", true);

		ItemNBTHelper.setCompound(stack, "Explosion", explosion);

		CompoundNBT fireworks = new CompoundNBT();
		fireworks.putInt("Flight", (int) (Math.random() * 3 + 2));

		ListNBT explosions = new ListNBT();
		explosions.add(explosion);
		fireworks.put("Explosions", explosions);

		ItemNBTHelper.setCompound(stack, "Fireworks", fireworks);

		return stack;
	}

}
