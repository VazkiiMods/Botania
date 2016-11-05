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
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.subtile.SubTileEntity;

/**
 * An Item that implements this can be placed by a Rannuncarpus.
 */
public interface IFlowerPlaceable {

	/**
	 * Gets the state to be placed, return null to not place anything.
	 */
	public IBlockState getBlockToPlaceByFlower(ItemStack stack, SubTileEntity flower, BlockPos pos);

	/**
	 * Called after the block is placed.
	 * @param stack The stack that was placed.
	 * @param flower The flower doing the placing.
	 * @param pos The position that was placed at.
	 */
	public void onBlockPlacedByFlower(ItemStack stack, SubTileEntity flower, BlockPos pos);
}
