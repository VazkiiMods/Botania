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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public class EntityFallingStar extends EntityThrowableCopy {
	public EntityFallingStar(EntityType<EntityFallingStar> type, World world) {
		super(type, world);
	}

	public EntityFallingStar(LivingEntity e, World world) {
		super(ModEntities.FALLING_STAR, e, world);
	}

	@Override
	protected void registerData() {}

	@Override
	public IPacket<?> createSpawnPacket() {
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
			world.addParticle(data, getPosX() + xs, getPosY() + ys, getPosZ() + zs, 0, 0, 0);
		}

		LivingEntity thrower = getThrower();
		if (!world.isRemote && thrower != null) {
			AxisAlignedBB axis = new AxisAlignedBB(getPosX(), getPosY(), getPosZ(), lastTickPosX, lastTickPosY, lastTickPosZ).grow(2);
			List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, axis);
			for (LivingEntity living : entities) {
				if (living == thrower) {
					continue;
				}

				if (living.hurtTime == 0) {
					onImpact(new EntityRayTraceResult(living));
					return;
				}
			}
		}

		if (ticksExisted > 200) {
			remove();
		}
	}

	@Override
	protected void onImpact(RayTraceResult pos) {
		if (world.isRemote) {
			return;
		}

		LivingEntity thrower = getThrower();
		if (pos.getType() == RayTraceResult.Type.ENTITY && thrower != null) {
			Entity e = ((EntityRayTraceResult) pos).getEntity();
			if (e != thrower && e.isAlive()) {
				if (thrower instanceof PlayerEntity) {
					e.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) thrower), Math.random() < 0.25 ? 10 : 5);
				} else {
					e.attackEntityFrom(DamageSource.GENERIC, Math.random() < 0.25 ? 10 : 5);
				}
			}
		}

		if (pos.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos bpos = ((BlockRayTraceResult) pos).getPos();
			BlockState state = world.getBlockState(bpos);
			if (ConfigHandler.COMMON.blockBreakParticles.get() && !state.isAir(world, bpos)) {
				world.playEvent(2001, bpos, Block.getStateId(state));
			}
		}

		remove();
	}
}
