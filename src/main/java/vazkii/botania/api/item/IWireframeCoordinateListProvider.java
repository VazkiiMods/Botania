/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 29, 2015, 11:30:41 PM (GMT)]
 */
package vazkii.botania.api.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;

/**
 * An item that implements this will allow for various wireframes to be drawn
 * around the player. This will be called while the item is in the inventory,
 * armor slot or bauble slots.
 */
public interface IWireframeCoordinateListProvider {

	/**
	 * Returns a list of ChunkCoordinates for the wireframes to draw.
	 * Can be null.
	 */
	public List<ChunkCoordinates> getWireframesToDraw(EntityPlayer player, ItemStack stack);

}
