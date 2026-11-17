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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;

public class AbstrusePlatformBlock extends PlatformBlock {
	public AbstrusePlatformBlock(Properties builder) {
		super(builder);
	}

	@Override
	public boolean testCollision(BlockPos pos, CollisionContext context) {
		if (context instanceof EntityCollisionContext entityCollisionContext) {
			Entity e = entityCollisionContext.getEntity();
			return (e == null || e.getY() > pos.getY() + 0.9 && !context.isDescending());
		}
		return true;
	}
}
