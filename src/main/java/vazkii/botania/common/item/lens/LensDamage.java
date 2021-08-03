/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.internal.IManaBurst;

import java.util.List;

public class LensDamage extends Lens {

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		ThrowableProjectile entity = burst.entity();
		if (entity.level.isClientSide) {
			return;
		}
		AABB axis = new AABB(entity.getX(), entity.getY(), entity.getZ(), entity.xOld, entity.yOld, entity.zOld).inflate(1);
		List<LivingEntity> entities = entity.level.getEntitiesOfClass(LivingEntity.class, axis);
		for (LivingEntity living : entities) {
			if (living instanceof Player) {
				continue;
			}

			if (living.hurtTime == 0) {
				int mana = burst.getMana();
				if (mana >= 16) {
					burst.setMana(mana - 16);
					if (!burst.isFake()) {
						DamageSource src = entity.getOwner() != null
								? DamageSource.indirectMagic(entity, entity.getOwner())
								: DamageSource.MAGIC;
						living.hurt(src, 8);
					}
					break;
				}
			}
		}
	}

}
