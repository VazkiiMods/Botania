/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.wand;

import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

/**
 * Any TileEntity that implements this is technically bound
 * to something, and the binding will be shown when hovering
 * over with a Wand of the Forest.
 */
public interface ITileBound {

	/**
	 * Gets where this block is bound to
	 */
	@Nullable
	BlockPos getBinding();

}
