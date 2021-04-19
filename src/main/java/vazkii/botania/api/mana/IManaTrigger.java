/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.internal.IManaBurst;

/**
 * Have a block implement this class to make it do something when a mana burst collides with it.
 * This interface overrides {@link IManaCollisionGhost}.
 */
public interface IManaTrigger {

	void onBurstCollision(IManaBurst burst, World world, BlockPos pos);

}
