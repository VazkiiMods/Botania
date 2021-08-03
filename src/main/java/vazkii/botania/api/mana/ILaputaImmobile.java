/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * A block that implements this has a flag for whether it can be moved by the Shard of Laputa.
 */
public interface ILaputaImmobile {

	boolean canMove(Level world, BlockPos pos);

}
