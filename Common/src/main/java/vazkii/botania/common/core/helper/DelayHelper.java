/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.helper;

import net.minecraft.world.entity.item.ItemEntity;

import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.xplat.IXplatAbstractions;

public class DelayHelper {
	private static final int FUNCTIONAL_INHERENT_DELAY = 60;
	private static final int GENERATING_INHERENT_DELAY = FUNCTIONAL_INHERENT_DELAY - 1;

	/**
	 * Like {@link #canInteractWith}, but does not use inherent delay
	 */
	public static boolean canInteractWithImmediate(TileEntitySpecialFlower tile, ItemEntity item) {
		return item.isAlive() && !item.getItem().isEmpty()
				&& IXplatAbstractions.INSTANCE.itemFlagsComponent(item).timeCounter > tile.getModulatedDelay();
	}

	/**
	 * @return Whether the given flower can act on the given item, taking into account inherent delay and modulating
	 *         delay
	 */
	public static boolean canInteractWith(TileEntitySpecialFlower tile, ItemEntity item) {
		if (!item.isAlive() || item.getItem().isEmpty()) {
			return false;
		}
		var flags = IXplatAbstractions.INSTANCE.itemFlagsComponent(item);
		int inherentDelay = 0;
		if (tile instanceof TileEntityFunctionalFlower) {
			inherentDelay = FUNCTIONAL_INHERENT_DELAY;
		} else if (tile instanceof TileEntityGeneratingFlower) {
			inherentDelay = GENERATING_INHERENT_DELAY;
		}
		return flags.timeCounter > inherentDelay + tile.getModulatedDelay();
	}
}
