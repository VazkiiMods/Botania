/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import javax.annotation.Nullable;

/**
 * Block Entity implementing this may conditionally return {@link IFloatingFlower}.
 * This is exposed for public reading, but do not implement it on anything.
 */
public interface IFloatingFlowerProvider {
	@Nullable
	IFloatingFlower getFloatingData();
}
