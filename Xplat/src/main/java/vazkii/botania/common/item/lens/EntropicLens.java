/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.block.BotaniaBlocks;

public class EntropicLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		ThrowableProjectile entity = burst.entity();
		if (pos.getType() == HitResult.Type.BLOCK) {
			//TODO https://github.com/VazkiiMods/Botania/pull/4219#issuecomment-1321442839
			BlockPos hit = ((BlockHitResult) pos).getBlockPos();
			if (entity.level.getBlockState(hit).is(BotaniaBlocks.pistonRelay)) {
				return shouldKill;
			}

			if (!entity.level.isClientSide && !burst.isFake() && !isManaBlock) {
				entity.level.explode(entity, entity.getX(), entity.getY(), entity.getZ(),
						burst.getMana() / 50F, Explosion.BlockInteraction.BREAK);
			}
			return true;
		}
		return shouldKill;
	}

}
