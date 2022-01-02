/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.IManaBurst;

public class LensPhantom extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		if (!isManaBlock) {
			shouldKill = false;
			burst.setMinManaLoss(Math.max(0, burst.getMinManaLoss() - 4));
		}

		return shouldKill;
	}

}
