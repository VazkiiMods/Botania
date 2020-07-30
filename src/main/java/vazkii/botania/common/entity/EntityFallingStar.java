/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.List;

public class EntityFallingStar extends EntityThrowableCopy {
	public EntityFallingStar(EntityType<EntityFallingStar> type, World world) {
		super(type, world);
	}

	public EntityFallingStar(LivingEntity e, World world) {
		super(ModEntities.FALLING_STAR, e, world);
	}

	@Override
	protected void initDataTracker() {}

	@Override
	public Packet<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void tick() {
		super.tick();

		float dist = 1.5F;
		SparkleParticleData data = SparkleParticleData.sparkle(2F, 1F, 0.4F, 1F, 6);
		for (int i = 0; i < 10; i++) {
			float xs = (float) (Math.random() - 0.5) * dist;
			float ys = (float) (Math.random() - 0.5) * dist;
			float zs = (float) (Math.random() - 0.5) * dist;
			world.addParticle(data, getX() + xs, getY() + ys, getZ() + zs, 0, 0, 0);
		}

		Entity thrower = getOwner();
		if (!world.isClient && thrower != null) {
			Box axis = new Box(getX(), getY(), getZ(), lastRenderX, lastRenderY, lastRenderZ).expand(2);
			List<LivingEntity> entities = world.getNonSpectatingEntities(LivingEntity.class, axis);
			for (LivingEntity living : entities) {
				if (living == thrower) {
					continue;
				}

				if (living.hurtTime == 0) {
					onCollision(new EntityHitResult(living));
					return;
				}
			}
		}

		if (age > 200) {
			remove();
		}
	}

	@Override
	protected void onCollision(HitResult pos) {
		if (world.isClient) {
			return;
		}

		Entity thrower = getOwner();
		if (pos.getType() == HitResult.Type.ENTITY && thrower != null) {
			Entity e = ((EntityHitResult) pos).getEntity();
			if (e != thrower && e.isAlive()) {
				if (thrower instanceof PlayerEntity) {
					e.damage(DamageSource.player((PlayerEntity) thrower), Math.random() < 0.25 ? 10 : 5);
				} else {
					e.damage(DamageSource.GENERIC, Math.random() < 0.25 ? 10 : 5);
				}
			}
		}

		if (pos.getType() == HitResult.Type.BLOCK) {
			BlockPos bpos = ((BlockHitResult) pos).getBlockPos();
			BlockState state = world.getBlockState(bpos);
			if (ConfigHandler.COMMON.blockBreakParticles.getValue() && !state.isAir()) {
				world.syncWorldEvent(2001, bpos, Block.getRawIdFromState(state));
			}
		}

		remove();
	}
}
