/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Any Block implementing this will not collide with bursts.
 * Note that {@link IManaTrigger} overrides this.
 */
public interface IManaCollisionGhost {

	boolean isGhost(BlockState state, Level world, BlockPos pos);

}
