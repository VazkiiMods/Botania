/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.helper;

import net.minecraft.world.entity.item.ItemEntity;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.xplat.IXplatAbstractions;

public class DelayHelper {
	public static final int FUNCTIONAL_INHERENT_DELAY = 60;
	public static final int GENERATING_INHERENT_DELAY = FUNCTIONAL_INHERENT_DELAY - 1;

	/**
	 * Like {@link #canInteractWith}, but does not use inherent delay
	 */
	public static boolean canInteractWithImmediate(SpecialFlowerBlockEntity tile, ItemEntity item) {
		return item.isAlive() && !item.getItem().isEmpty()
				&& IXplatAbstractions.INSTANCE.itemFlagsComponent(item).timeCounter > tile.getModulatedDelay();
	}

	/**
	 * @return Whether the given flower can act on the given item, taking into account inherent delay and modulating
	 *         delay
	 */
	public static boolean canInteractWith(SpecialFlowerBlockEntity tile, ItemEntity item) {
		if (!item.isAlive() || item.getItem().isEmpty()) {
			return false;
		}
		var flags = IXplatAbstractions.INSTANCE.itemFlagsComponent(item);
		int inherentDelay = 0;
		if (tile instanceof FunctionalFlowerBlockEntity) {
			inherentDelay = FUNCTIONAL_INHERENT_DELAY;
		} else if (tile instanceof GeneratingFlowerBlockEntity) {
			inherentDelay = GENERATING_INHERENT_DELAY;
		}
		return flags.timeCounter > inherentDelay + tile.getModulatedDelay();
	}
}
