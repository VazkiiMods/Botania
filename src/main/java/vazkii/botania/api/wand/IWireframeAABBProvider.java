/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 19, 2014, 7:23:59 PM (GMT)]
 */
package vazkii.botania.api.wand;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * A block that implements this can provide a custom AABB
 * for rendering the wireframe with ITileBound.
 */
public interface IWireframeAABBProvider {

	public AxisAlignedBB getWireframeAABB(World world, int x, int y, int z);

}
