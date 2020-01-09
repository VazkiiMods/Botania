/**
 * This class was created by
 <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 24, 2015, 4:41:44 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import vazkii.botania.api.internal.IManaBurst;

public class LensExplosive extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, ThrowableEntity entity, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if(!entity.world.isRemote && !burst.isFake() && pos.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos coords = burst.getBurstSourceBlockPos();
			if(!isManaBlock && !coords.equals(((BlockRayTraceResult) pos).getPos()))
				entity.world.createExplosion(entity, entity.getX(), entity.getY(), entity.getZ(),
						burst.getMana() / 50F, Explosion.Mode.BREAK);
		} else dead = false;

		return dead;
	}

}
