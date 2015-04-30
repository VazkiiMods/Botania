/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 17, 2014, 5:29:26 PM (GMT)]
 */
package vazkii.botania.api.subtile.signature;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
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

	/**
	 * Adds additional text to the tooltip. This text is added after getUnlocalizedLoreTextForStack.
	 */
	public void addTooltip(ItemStack stack, EntityPlayer player, List<String> tooltip) {
		// NO-OP
	}

}
