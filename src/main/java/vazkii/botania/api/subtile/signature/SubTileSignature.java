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

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * A singleton instance for a SubTileEntity, this is called for a few methods.
 */
public interface SubTileSignature {

	public static final String SPECIAL_FLOWER_PREFIX = "flower.";

	/**
	 * Gets the display name for the flower item.
	 */
	String getUnlocalizedNameForStack(ItemStack stack);

	/**
	 * Gets the lore text for the flower item, displayed in the item's tooltip.
	 * If you do not want a reference return a key that does not have localization such
	 * as "botaniamisc.noloc".
	 */
	String getUnlocalizedLoreTextForStack(ItemStack stack);

	/**
	 * Adds additional text to the tooltip. This text is added after getUnlocalizedLoreTextForStack.
	 */
	@SideOnly(Side.CLIENT)
	default void addTooltip(ItemStack stack, World world, List<String> tooltip) {}

}
