/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 15, 2014, 2:36:35 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Have an Item implement this to allow it to be used in the Petal Apothecary.
 */
public interface IFlowerComponent {

	public boolean canFit(ItemStack stack, IInventory apothecary);

	public int getParticleColor(ItemStack stack);

}
