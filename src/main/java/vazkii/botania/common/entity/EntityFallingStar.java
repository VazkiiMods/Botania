/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 17, 2015, 4:19:52 PM (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.List;

import vazkii.botania.common.lib.LibMisc;

public class EntityFallingStar extends EntityThrowableCopy {
	@ObjectHolder(LibMisc.MOD_ID + ":falling_star")
	public static EntityType<EntityFallingStar> TYPE;

	public EntityFallingStar(EntityType<EntityFallingStar> type, World world) {
		super(type, world);
	}

	public EntityFallingStar(LivingEntity e, World world) {
		super(TYPE, e, world);
	}

	@Override
	protected void registerData() {}

	@Override
	public void tick() {
		super.tick();

		float dist = 1.5F;
		for(int i = 0; i < 10; i++) {
			float xs = (float) (Math.random() - 0.5) * dist;
			float ys = (float) (Math.random() - 0.5) * dist;
			float zs = (float) (Math.random() - 0.5) * dist;
			Botania.proxy.sparkleFX(posX + xs, posY + ys, posZ + zs, 1F, 0.4F, 1F, 2F, 6);
		}

		LivingEntity thrower = getThrower();
		if(!world.isRemote && thrower != null) {
			AxisAlignedBB axis = new AxisAlignedBB(posX, posY, posZ, lastTickPosX, lastTickPosY, lastTickPosZ).grow(2);
			List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, axis);
			for(LivingEntity living : entities) {
				if(living == thrower)
					continue;

				if(living.hurtTime == 0) {
					onImpact(new EntityRayTraceResult(living));
					return;
				}
			}
		}

		if(ticksExisted > 200)
			remove();
	}

	@Override
	protected void onImpact(RayTraceResult pos) {
		if (world.isRemote)
			return;

		LivingEntity thrower = getThrower();
		if(pos.getType() == RayTraceResult.Type.ENTITY && thrower != null ) {
			Entity e = ((EntityRayTraceResult) pos).getEntity();
			if(e != thrower && e.isAlive()) {
				if(thrower instanceof PlayerEntity)
					e.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) thrower), Math.random() < 0.25 ? 10 : 5);
				else e.attackEntityFrom(DamageSource.GENERIC, Math.random() < 0.25 ? 10 : 5);
			}
		}

		if (pos.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos bpos = ((BlockRayTraceResult) pos).getPos();
			BlockState state = world.getBlockState(bpos);
			if(ConfigHandler.COMMON.blockBreakParticles.get() && !state.isAir(world, bpos))
				world.playEvent(2001, bpos, Block.getStateId(state));
		}

		remove();
	}
}

