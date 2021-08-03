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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Have a block implement this class to make it do something when an adjacent
 * Hovering Hourglass turns.
 */
public interface IHourglassTrigger {

	void onTriggeredByHourglass(Level world, BlockPos pos, BlockEntity hourglass);

}
