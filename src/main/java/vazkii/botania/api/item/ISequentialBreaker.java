/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 30, 2015, 2:46:05 PM (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * An item that implements this can break multiple blocks at once
 * with a Ring of Loki. Usage of this interface requires an implementation
 * (see ItemTerraPick).
 */
public interface ISequentialBreaker {

	public void breakOtherBlock(EntityPlayer player, ItemStack stack, BlockPos pos, BlockPos originPos, EnumFacing side);

	public boolean disposeOfTrashBlocks(ItemStack stack);

}
