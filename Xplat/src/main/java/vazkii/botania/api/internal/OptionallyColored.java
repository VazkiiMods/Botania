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
 * Specifies an optional color of a thing. That doesn't necessarily mean that thing is dyeable.
 * The thing may have an undyed form unless it also implements {@link Colored}.
 */
public interface OptionallyColored {
	Optional<DyeColor> getOptionalColor();
}
