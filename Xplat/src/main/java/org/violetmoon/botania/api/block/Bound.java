/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.block;

import net.minecraft.core.BlockPos;

import org.jetbrains.annotations.Nullable;

/**
 * Any BlockEntity that implements this is technically bound
 * to something, and the binding will be shown when hovering
 * over with a Wand of the Forest.
 */
public interface Bound {
	/**
	 * @deprecated use {@code null} instead
	 */
	@Deprecated
	BlockPos UNBOUND_POS = new BlockPos(0, Integer.MIN_VALUE, 0);

	/**
	 * Gets where this block is bound to
	 */
	@Nullable
	BlockPos getBinding();

}
