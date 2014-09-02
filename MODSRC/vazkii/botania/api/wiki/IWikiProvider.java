/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Sep 2, 2014, 5:57:35 PM (GMT)]
 */
package vazkii.botania.api.wiki;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public interface IWikiProvider {

	public String getBlockName(World world, MovingObjectPosition pos);
	
	public String getWikiURL(World world, MovingObjectPosition pos);
	
	public String getWikiName(World world, MovingObjectPosition pos);
	
}
