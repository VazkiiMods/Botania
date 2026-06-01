/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface SpecialBlockBreakingHandler {
	/**
	 * Called right before a block is broken with the item implementing this interface. Use to apply appropriate
	 * modifications, such as multiple block breaking, or temporary modifications that cannot be applied in any other
	 * way.
	 */
	void onBlockStartBreak(ServerLevel level, ItemStack itemstack, BlockPos pos, Player player);
}
