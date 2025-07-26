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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.xplat.XplatAbstractions;

public class EntropicLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		ThrowableProjectile entity = burst.entity();
		if (pos.getType() == HitResult.Type.BLOCK) {
			//TODO https://github.com/VazkiiMods/Botania/pull/4219#issuecomment-1321442839
			BlockPos hit = ((BlockHitResult) pos).getBlockPos();
			if (entity.level().getBlockState(hit).is(BotaniaBlocks.pistonRelay)) {
				return shouldKill;
			}

			if (!entity.level().isClientSide && !burst.isFake() && !isManaBlock) {
				// Fire event before explosion creation
				var player = XplatAbstractions.INSTANCE.getPlayer(entity.level(), burst.getShooterUUID(), getClass().getName());
				float explosionPower = burst.getMana() / 50F;
				
				if (XplatAbstractions.INSTANCE.entropicLensExplodeEvent(player, hit, explosionPower)) {
					return shouldKill; // Event cancelled, don't create explosion
				}
				
				entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(),
						explosionPower, Level.ExplosionInteraction.TNT);
			}
			return true;
		}
		return shouldKill;
	}

}
