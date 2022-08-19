/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [17/11/2015, 20:10:53 (GMT)]
 */
package vazkii.botania.api.item;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.subtile.SubTileEntity;

/**
 * An Item that implements this can be placed by a Rannuncarpus.
 */
public interface IFlowerPlaceable {

	/**
	 * Gets the block to be placed, return null to not place anything.
	 */
	public Block getBlockToPlaceByFlower(ItemStack stack, SubTileEntity flower, int x, int y, int z);

	public void onBlockPlacedByFlower(ItemStack stack, SubTileEntity flower, int x, int y, int z);
}
