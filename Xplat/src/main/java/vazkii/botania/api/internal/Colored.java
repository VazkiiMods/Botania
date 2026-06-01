/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.internal;

import net.minecraft.world.item.DyeColor;

import java.util.Optional;

/**
 * Specifies the color of a thing. That doesn't necessarily mean that thing is dyeable.
 * The thing is expected to not exist in an undyed form, which is a restriction over {@link OptionallyColored}.
 */
public interface Colored extends OptionallyColored {
	DyeColor getColor();

	default Optional<DyeColor> getOptionalColor() {
		return Optional.of(getColor());
	}
}
