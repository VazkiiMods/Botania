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
import net.minecraft.network.protocol.Packet;
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
import net.minecraft.world.phys.HitResult;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.network.PacketSpawnEntity;

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
	public Packet<?> getAddEntityPacket() {
		return PacketSpawnEntity.make(this);
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
			remove();
		}
	}

	@Override
	protected void onHit(HitResult pos) {
		if (level.isClientSide) {
			return;
		}

		Entity thrower = getOwner();
		if (pos.getType() == HitResult.Type.ENTITY && thrower != null) {
			Entity e = ((EntityHitResult) pos).getEntity();
			if (e != thrower && e.isAlive()) {
				if (thrower instanceof Player) {
					e.hurt(DamageSource.playerAttack((Player) thrower), Math.random() < 0.25 ? 10 : 5);
				} else {
					e.hurt(DamageSource.GENERIC, Math.random() < 0.25 ? 10 : 5);
				}
			}
		}

		if (pos.getType() == HitResult.Type.BLOCK) {
			BlockPos bpos = ((BlockHitResult) pos).getBlockPos();
			BlockState state = level.getBlockState(bpos);
			if (ConfigHandler.COMMON.blockBreakParticles.getValue() && !state.isAir()) {
				level.levelEvent(2001, bpos, Block.getId(state));
			}
		}

		remove();
	}
}
