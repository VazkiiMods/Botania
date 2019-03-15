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
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.subtile.SubTileEntity;

/**
 * An Item that implements this can be placed by a Rannuncarpus.
 */
public interface IFlowerPlaceable {

	/**
     * @param ctx The placement context, aiming downwards towards the downward neighbor position
	 * @return Whether the block was placed successfully
	 */
	boolean tryPlace(SubTileEntity flower, ItemUseContext ctx);
}
