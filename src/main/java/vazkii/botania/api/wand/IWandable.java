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
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Any block that implements this can be used with the Wand for the Forest for some purpose.
 */
public interface IWandable {

	/**
	 * Called when the block is used by a wand. Note that the player parameter can be null
	 * if this function is called from a dispenser.
	 */
	boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side);

}
