/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import vazkii.botania.api.internal.IManaBurst;

public class LensPhantom extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, ThrownEntity entity, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if (!isManaBlock) {
			dead = false;
			burst.setMinManaLoss(Math.max(0, burst.getMinManaLoss() - 4));
		}

		return dead;
	}

}
