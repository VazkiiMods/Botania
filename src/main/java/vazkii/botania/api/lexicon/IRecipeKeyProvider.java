/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 20, 2014, 6:08:48 PM (GMT)]
 */
package vazkii.botania.api.lexicon;

import net.minecraft.item.ItemStack;

/**
 * Have an Item implement this so that the method used for mapping it into
 * the lexicon recipe mappings isn't the typical id:meta key.
 */
public interface IRecipeKeyProvider {

	public String getKey(ItemStack stack);

}
