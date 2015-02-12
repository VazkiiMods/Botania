/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 29, 2014, 4:52:04 PM (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.world.World;

/**
 * A BlockBush can implement this to check if it can be broken
 * by a Horn/Drum of the Wild or not.
 */
public interface IGrassHornExcempt {

	public boolean canUproot(World world, int x, int y, int z);

}
