/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 16, 2014, 7:52:53 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;

/**
 * Have a block implement this class to make it do something when a mana burst collides with it.
 */
public interface IManaTrigger {

	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos);

}
