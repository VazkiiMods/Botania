/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 26, 2014, 9:51:58 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.world.World;

/**
 * A block that implements this has a flag for whether it can be moved by the Shard of Laputa.
 */
public interface ILaputaImmobile {

	public boolean canMove(World world, int x, int y, int z);

}
