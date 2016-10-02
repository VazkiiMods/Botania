/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 24, 2014, 6:47:53 PM (GMT)]
 */
package vazkii.botania.api.wand;

import net.minecraft.util.math.BlockPos;

/**
 * Any TileEntity that implements this is technically bound
 * to something, and the binding will be shown when hovering
 * over with a Wand of the Forest.
 */
public interface ITileBound {

	/**
	 * Gets where this block is bound to, can return null.
	 */
	public BlockPos getBinding();

}
