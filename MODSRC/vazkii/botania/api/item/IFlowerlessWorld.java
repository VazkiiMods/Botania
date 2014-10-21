/**
 * This class was created by <TheLoneDevil>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.world.World;

/**
 * A WorldProvider that implements this will not have Botania flowers generated.
 */
public interface IFlowerlessWorld {

	/**
	 * @return Should this world be allowed to generate flowers?
	 */
	public boolean generateFlowers(World world);
}
