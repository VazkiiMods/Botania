/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 30, 2015, 1:43:17 PM (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;

/**
 * An extension of IWireframeCoordinateListProvider that allows for
 * rendering a single thicker wireframe.
 */
public interface IExtendedWireframeCoordinateListProvider extends IWireframeCoordinateListProvider {

	/**
	 * Gets the source wireframe to draw, this one will be drawn thicker.
	 */
	public ChunkCoordinates getSourceWireframe(EntityPlayer player, ItemStack stack);

}
