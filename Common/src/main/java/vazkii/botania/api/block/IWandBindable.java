/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * A TileEntity that implements this can be bound to another block
 * via the Wand of the Forest.
 */
public interface IWandBindable extends ITileBound {

	/**
	 * Return true if the Wand can select this tile.
	 */
	boolean canSelect(Player player, ItemStack wand, BlockPos pos, Direction side);

	/**
	 * Call to bind the TileEntity to where the player clicked. Return true to deselect
	 * the TileEntity for another bind or false case the TileEntity should stay selected.
	 */
	boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side);

}
