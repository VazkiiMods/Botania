/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [28/09/2016, 20:01:14 (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Have a block implement this class to make it do something when an adjacent
 * Hovering Hourglass turns.
 */
public interface IHourglassTrigger {

	public void onTriggeredByHourglass(World world, BlockPos pos, TileEntity hourglass);

}
