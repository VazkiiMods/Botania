/**
 * This class was created by <Adubbz>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;

public class EntityPixie extends EntityFlyingCreature {

	EntityPlayer player = null;
	float damage = 0;

	public EntityPixie(World world) {
		super(world);
		setSize(1.0F, 1.0F);
	}

	public void setProps(EntityLivingBase target, EntityPlayer player, float damage) {
		setAttackTarget(target);
		this.player = player;
		this.damage = damage;
	}

	@Override
	protected void updateEntityActionState() {
		EntityLivingBase target = getAttackTarget();
		if(target != null) {
			double d0 = target.posX + target.width / 2 - posX;
			double d1 = target.posY + target.height / 2 - posY;
			double d2 = target.posZ + target.width / 2 - posZ;
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;

			float mod = 0.45F;
			motionX += d0 / d3 * mod;
			motionY += d1 / d3 * mod;
			motionZ += d2 / d3 * mod;

			if(Math.sqrt(d3) < 1F) {
				if(player != null)
					target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
				else target.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
				die();
			}
		}

		renderYawOffset = rotationYaw = -((float)Math.atan2(motionX, motionZ)) * 180.0F / (float)Math.PI;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if(getAttackTarget() == null && ticksExisted > 100)
			die();

		if(worldObj.isRemote)
			for(int i = 0; i < 4; i++)
				Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5  + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1F, 0.25F, 0.9F, 0.1F + (float) Math.random() * 0.25F, 12);
	}

	public void die() {
		setDead();

		if(worldObj.isRemote)
			for(int i = 0; i < 12; i++)
				Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5  + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1F, 0.25F, 0.9F, 1F + (float) Math.random() * 0.25F, 5);
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

}