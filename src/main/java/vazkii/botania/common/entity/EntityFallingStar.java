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

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;

public class EntityFallingStar extends EntityThrowableCopy {

	public EntityFallingStar(World world) {
		super(world);
		setSize(0F, 0F);
	}

	public EntityFallingStar(World world, EntityLivingBase e) {
		super(world, e);
		setSize(0F, 0F);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		float dist = 1.5F;
		for(int i = 0; i < 10; i++) {
			float xs = (float) (Math.random() - 0.5) * dist;
			float ys = (float) (Math.random() - 0.5) * dist;
			float zs = (float) (Math.random() - 0.5) * dist;
			Botania.proxy.sparkleFX(worldObj, posX + xs, posY + ys, posZ + zs, 1F, 0.4F, 1F, 2F, 6);
		}

		EntityLivingBase thrower = getThrower();
		if(!worldObj.isRemote && thrower != null) {
			AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(posX, posY, posZ, lastTickPosX, lastTickPosY, lastTickPosZ).expand(2, 2, 2);
			List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axis);
			for(EntityLivingBase living : entities) {
				if(living == thrower)
					continue;

				if(living.hurtTime == 0) {
					onImpact(new MovingObjectPosition(living));
					return;
				}
			}
		}

		if(ticksExisted > 200)
			setDead();
	}

	@Override
	protected void onImpact(MovingObjectPosition pos) {
		EntityLivingBase thrower = getThrower();
		if(pos.entityHit != null && thrower != null && pos.entityHit != thrower && !pos.entityHit.isDead) {
			if(thrower instanceof EntityPlayer)
				pos.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) thrower), 10);
			else pos.entityHit.attackEntityFrom(DamageSource.generic, 10);
		}

		Block block = worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ);
		if(ConfigHandler.blockBreakParticles && !block.isAir(worldObj, pos.blockX, pos.blockY, pos.blockZ))
			worldObj.playAuxSFX(2001, pos.blockX, pos.blockY, pos.blockZ, Block.getIdFromBlock(block) + (worldObj.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) << 12));

		setDead();
	}

}

