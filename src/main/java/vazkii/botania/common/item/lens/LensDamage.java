/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import vazkii.botania.api.internal.IManaBurst;

import java.util.List;

public class LensDamage extends Lens {

	@Override
	public void updateBurst(IManaBurst burst, ThrownEntity entity, ItemStack stack) {
		if (entity.world.isClient) {
			return;
		}
		Box axis = new Box(entity.getX(), entity.getY(), entity.getZ(), entity.lastRenderX, entity.lastRenderY, entity.lastRenderZ).expand(1);
		List<LivingEntity> entities = entity.world.getNonSpectatingEntities(LivingEntity.class, axis);
		for (LivingEntity living : entities) {
			if (living instanceof PlayerEntity) {
				continue;
			}

			if (living.hurtTime == 0) {
				int mana = burst.getMana();
				if (mana >= 16) {
					burst.setMana(mana - 16);
					if (!burst.isFake()) {
						DamageSource src = entity.getOwner() != null
								? DamageSource.magic(entity, entity.getOwner())
								: DamageSource.MAGIC;
						living.damage(src, 8);
					}
					break;
				}
			}
		}
	}

}
