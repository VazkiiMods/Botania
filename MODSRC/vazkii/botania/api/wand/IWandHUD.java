/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 5, 2014, 1:34:44 PM (GMT)]
 */
package vazkii.botania.api.wand;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.world.World;

/**
 * Any block that implements this has a HUD rendered when being hovered
 * with a Wand of the Forest.
 */
public interface IWandHUD {

	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z);

}
