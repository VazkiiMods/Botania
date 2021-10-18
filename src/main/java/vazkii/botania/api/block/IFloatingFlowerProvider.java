/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.block;

import javax.annotation.Nullable;

/**
 * Block Entity implementing this may conditionally return {@link IFloatingFlower}.
 * This is exposed for public reading, do not implement it on anything yourself.
 */
public interface IFloatingFlowerProvider {
	@Nullable
	IFloatingFlower getFloatingData();
}
