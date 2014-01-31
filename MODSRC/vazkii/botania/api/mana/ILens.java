/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 31, 2014, 3:03:04 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Have an Item implement this to be counted as a lens for the mana spreader.
 */
public interface ILens {
	
	@SideOnly(Side.CLIENT)
	public int getLensColor(ItemStack stack);
	
	public void apply(ItemStack stack, BurstProperties props);
	
}
