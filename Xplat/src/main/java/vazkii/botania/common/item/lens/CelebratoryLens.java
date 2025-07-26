/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.xplat.XplatAbstractions;

public class CelebratoryLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();
		if (pos.getType() == HitResult.Type.BLOCK) {
			if (!entity.level().isClientSide && !burst.isFake() && !isManaBlock) {
				BlockHitResult blockHit = (BlockHitResult) pos;
				Level level = entity.level();
				BlockPos targetPos = blockHit.getBlockPos();
				
				// Generate firework before firing event
				ItemStack fireworkStack = generateFirework(burst.getColor());
				
				// Fire event before firework creation
				var player = XplatAbstractions.INSTANCE.getPlayer(level, burst.getShooterUUID(), getClass().getName());
				if (XplatAbstractions.INSTANCE.celebratoryLensFireworkEvent(player, targetPos, fireworkStack, stack)) {
					return shouldKill; // Event cancelled, don't create firework
				}

				FireworkRocketEntity rocket = new FireworkRocketEntity(level, entity.getX(), entity.getY(), entity.getZ(), fireworkStack);
				level.addFreshEntity(rocket);
			}
			return true;
		}

		return shouldKill;
	}

	private ItemStack generateFirework(int color) {
		ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
		CompoundTag explosion = new CompoundTag();
		explosion.putIntArray("Colors", new int[] { color });

		int type = 1;
		double rand = Math.random();
		if (rand > 0.25) {
			if (rand > 0.9) {
				type = 2;
			} else {
				type = 0;
			}
		}

		explosion.putInt("Type", type);

		if (Math.random() < 0.05) {
			if (Math.random() < 0.5) {
				explosion.putBoolean("Flicker", true);
			} else {
				explosion.putBoolean("Trail", true);
			}
		}

		ItemNBTHelper.setCompound(stack, "Explosion", explosion);

		CompoundTag fireworks = new CompoundTag();
		fireworks.putInt("Flight", (int) (Math.random() * 3 + 2));

		ListTag explosions = new ListTag();
		explosions.add(explosion);
		fireworks.put("Explosions", explosions);

		ItemNBTHelper.setCompound(stack, "Fireworks", fireworks);

		return stack;
	}

}
