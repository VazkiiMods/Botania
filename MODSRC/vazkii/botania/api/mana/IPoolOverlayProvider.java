/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 2, 2014, 6:36:54 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * A block that implements this can provide an IIcon (block icons only)
 * to be used as an overlay for the mana pool, similarly to the mana void
 * and catalysts.
 */
public interface IPoolOverlayProvider {

	public IIcon getIcon(World world, int x, int y, int z);

}
