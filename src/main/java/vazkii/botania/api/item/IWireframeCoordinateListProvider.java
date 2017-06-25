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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * An item that implements this will allow for various wireframes to be drawn
 * around the player. This will be called while the item is in the inventory,
 * armor slot or bauble slots.
 */
public interface IWireframeCoordinateListProvider {

	/**
	 * Returns a list of BlockPos for the wireframes to draw.
	 * Will not be null.
	 */
	public List<BlockPos> getWireframesToDraw(EntityPlayer player, ItemStack stack);

	/**
	 * Gets a wireframe to draw thicker than the rest.
	 * This is useful to indicate the precedence of some position over the others.
	 * @return The position of a single wireframe to draw thicker than all the others.
	 */
	public BlockPos getSourceWireframe(EntityPlayer player, ItemStack stack);


}
