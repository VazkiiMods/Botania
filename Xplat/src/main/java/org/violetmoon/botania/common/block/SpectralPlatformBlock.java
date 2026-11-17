/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;

public class SpectralPlatformBlock extends PlatformBlock {
	public SpectralPlatformBlock(Properties builder) {
		super(builder);
	}

	@Override
	public boolean testCollision(BlockPos pos, CollisionContext context) {
		return false;
	}
}
