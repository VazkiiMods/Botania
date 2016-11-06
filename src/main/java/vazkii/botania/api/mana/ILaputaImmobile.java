/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 26, 2014, 9:51:58 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A block that implements this has a flag for whether it can be moved by the Shard of Laputa.
 */
public interface ILaputaImmobile {

	public boolean canMove(World world, BlockPos pos);

}
