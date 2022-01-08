/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.xplat.BotaniaConfig;

import javax.annotation.Nonnull;

import java.util.List;

public class EntityFallingStar extends EntityThrowableCopy {
	public EntityFallingStar(EntityType<EntityFallingStar> type, Level world) {
		super(type, world);
	}

	public EntityFallingStar(LivingEntity e, Level world) {
		super(ModEntities.FALLING_STAR, e, world);
	}

	@Override
	protected void defineSynchedData() {}

	@Override
	public void tick() {
		super.tick();

		float dist = 1.5F;
		SparkleParticleData data = SparkleParticleData.sparkle(2F, 1F, 0.4F, 1F, 6);
		for (int i = 0; i < 10; i++) {
			float xs = (float) (Math.random() - 0.5) * dist;
			float ys = (float) (Math.random() - 0.5) * dist;
			float zs = (float) (Math.random() - 0.5) * dist;
			level.addParticle(data, getX() + xs, getY() + ys, getZ() + zs, 0, 0, 0);
		}

		Entity thrower = getOwner();
		if (!level.isClientSide && thrower != null) {
			AABB axis = new AABB(getX(), getY(), getZ(), xOld, yOld, zOld).inflate(2);
			List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, axis);
			for (LivingEntity living : entities) {
				if (living == thrower) {
					continue;
				}

				if (living.hurtTime == 0) {
					onHit(new EntityHitResult(living));
					return;
				}
			}
		}

		if (tickCount > 200) {
			discard();
		}
	}

	@Override
	protected void onHitEntity(@Nonnull EntityHitResult hit) {
		super.onHitEntity(hit);
		Entity e = hit.getEntity();
		if (!level.isClientSide) {
			if (e != getOwner() && e.isAlive()) {
				if (getOwner() instanceof Player player) {
					e.hurt(DamageSource.playerAttack(player), Math.random() < 0.25 ? 10 : 5);
				} else {
					e.hurt(DamageSource.GENERIC, Math.random() < 0.25 ? 10 : 5);
				}
			}
			discard();
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult hit) {
		super.onHitBlock(hit);
		if (!level.isClientSide) {
			BlockPos bpos = hit.getBlockPos();
			BlockState state = level.getBlockState(bpos);
			if (BotaniaConfig.common().blockBreakParticles() && !state.isAir()) {
				level.levelEvent(2001, bpos, Block.getId(state));
			}
			discard();
		}
	}
}
