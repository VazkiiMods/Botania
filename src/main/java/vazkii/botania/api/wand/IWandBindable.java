/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.wand;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * A TileEntity that implements this can be bound to another block
 * via the Wand of the Forest. Also see IWireframeAABBProvider to change
 * the displayed bounding box.
 */
public interface IWandBindable extends ITileBound {

	/**
	 * Return true if the Wand can select this tile.
	 */
	boolean canSelect(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side);

	/**
	 * Call to bind the TileEntity to where the player clicked. Return true to deselect
	 * the TileEntity for another bind or false case the TileEntity should stay selected.
	 */
	boolean bindTo(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side);

}
