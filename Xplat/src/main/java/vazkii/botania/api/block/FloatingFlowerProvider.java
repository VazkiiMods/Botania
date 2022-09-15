/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.block;

import org.jetbrains.annotations.Nullable;

/**
 * Block Entity implementing this may conditionally return {@link FloatingFlower}.
 * This is exposed for public reading, do not implement it on anything yourself.
 */
public interface FloatingFlowerProvider {
	@Nullable
	FloatingFlower getFloatingData();
}
