/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 24, 2014, 6:47:53 PM (GMT)]
 */
package vazkii.botania.api.wand;

import net.minecraft.util.ChunkCoordinates;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Any TileEntity that implements this is technically bound
 * to something, and the binding will be shown when hovering
 * over with a Wand of the Forest.
 */
public interface ITileBound {

	/**
	 * Gets where this block is bound to, can return null.
	 */
	@SideOnly(Side.CLIENT)
	public ChunkCoordinates getBinding();

}
