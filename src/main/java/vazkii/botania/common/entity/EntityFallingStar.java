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
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.List;

import vazkii.botania.common.lib.LibMisc;

public class EntityFallingStar extends EntityThrowableCopy {
	@ObjectHolder(LibMisc.MOD_ID + ":falling_star")
	public static EntityType<?> TYPE;

	public EntityFallingStar(World world) {
		super(TYPE, world);
		setSize(0F, 0F);
	}

	public EntityFallingStar(EntityLivingBase e, World world) {
		super(TYPE, e, world);
		setSize(0F, 0F);
	}

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

		EntityLivingBase thrower = getThrower();
		if(!world.isRemote && thrower != null) {
			AxisAlignedBB axis = new AxisAlignedBB(posX, posY, posZ, lastTickPosX, lastTickPosY, lastTickPosZ).grow(2);
			List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, axis);
			for(EntityLivingBase living : entities) {
				if(living == thrower)
					continue;

				if(living.hurtTime == 0) {
					onImpact(new RayTraceResult(living));
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

		EntityLivingBase thrower = getThrower();
		if(pos.entity != null && thrower != null && pos.entity != thrower && !pos.entity.removed) {
			if(thrower instanceof EntityPlayer)
				pos.entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) thrower), Math.random() < 0.25 ? 10 : 5);
			else pos.entity.attackEntityFrom(DamageSource.GENERIC, Math.random() < 0.25 ? 10 : 5);
		}

		if (pos.getBlockPos() != null) {
			IBlockState state = world.getBlockState(pos.getBlockPos());
			if(ConfigHandler.blockBreakParticles && !state.getBlock().isAir(state, world, pos.getBlockPos()))
				world.playEvent(2001, pos.getBlockPos(), Block.getStateId(state));
		}

		remove();
	}
}

