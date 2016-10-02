/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 10, 2014, 7:49:19 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Any Block that implements this can be counted as a "ghost" block of
 * sorts, that won't call the collision code for the mana bursts.
 */
public interface IManaCollisionGhost {

	public boolean isGhost(IBlockState state, World world, BlockPos pos);

}
