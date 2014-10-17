/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Oct 17, 2014, 5:29:26 PM (GMT)]
 */
package vazkii.botania.api.subtile.signature;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * A singleton instance for a SubTileEntity, this is called for a few methods.
 */
public abstract class SubTileSignature {

	public static final String SPECIAL_FLOWER_PREFIX = "flower.";

	/**
	 * Equivalent to Block.registerBlockIcons.
	 */
	public abstract void registerIcons(IIconRegister register);

	/**
	 * Gets the icon to display for the flower item.
	 */
	public abstract IIcon getIconForStack(ItemStack stack);

	/**
	 * Gets the display name for the flower item.
	 */
	public abstract String getUnlocalizedNameForStack(ItemStack stack);

	/**
	 * Gets the lore text for the flower item, displayed in the item's tooltip.
	 * If you do not want a reference return a key that does not have localization such
	 * as "botaniamisc.noloc".
	 */
	public abstract String getUnlocalizedLoreTextForStack(ItemStack stack);

}
