/**
 * This class was created by <Adubbz>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [? (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.Botania;

import javax.annotation.Nonnull;

import vazkii.botania.common.lib.LibMisc;

public class EntityPixie extends FlyingEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":pixie")
	public static EntityType<?> TYPE;
	private static final DataParameter<Integer> PIXIE_TYPE = EntityDataManager.createKey(EntityPixie.class, DataSerializers.VARINT);

	private LivingEntity summoner = null;
	private float damage = 0;
	private EffectInstance effect = null;

	public EntityPixie(World world) {
		super(TYPE, world);
		setSize(1.0F, 1.0F);
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(PIXIE_TYPE, 0);
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2.0);
	}

	public void setPixieType(int type) {
		dataManager.set(PIXIE_TYPE, type);
	}

	public int getPixieType() {
		return dataManager.get(PIXIE_TYPE);
	}

	public void setProps(LivingEntity target, LivingEntity summoner, int type, float damage) {
		setAttackTarget(target);
		this.summoner = summoner;
		this.damage = damage;
		setPixieType(type);
	}

	public void setApplyPotionEffect(EffectInstance effect) {
		this.effect = effect;
	}

	@Override
	protected void updateAITasks() {
		LivingEntity target = getAttackTarget();
		if(target != null) {
			double d0 = target.posX + target.width / 2 - posX;
			double d1 = target.posY + target.height / 2 - posY;
			double d2 = target.posZ + target.width / 2 - posZ;
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;

			float mod = 0.45F;
			if(getPixieType() == 1)
				mod = 0.1F;

			motionX += d0 / d3 * mod;
			motionY += d1 / d3 * mod;
			motionZ += d2 / d3 * mod;

			if(Math.sqrt(d3) < 1F) {
				if(summoner != null) {
					if(summoner instanceof PlayerEntity)
						target.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) summoner), damage);
					else {
						target.attackEntityFrom(DamageSource.causeMobDamage(summoner), damage);
					}
				} else target.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
				if(effect != null && !(target instanceof PlayerEntity))
					target.addPotionEffect(effect);
				remove();
			}
		}

		renderYawOffset = rotationYaw = -((float)Math.atan2(motionX, motionZ)) * 180.0F / (float)Math.PI;
	}

	@Override
	public boolean attackEntityFrom(@Nonnull DamageSource par1DamageSource, float par2) {
		if(getPixieType() == 0 && par1DamageSource.getTrueSource() != summoner || getPixieType() == 1 && par1DamageSource.getTrueSource() instanceof PlayerEntity)
			return super.attackEntityFrom(par1DamageSource, par2);
		return false;
	}

	@Override
	public void baseTick() {
		super.baseTick();

		if(!world.isRemote
				&& (getAttackTarget() == null || ticksExisted > 200))
			remove();

		boolean dark = getPixieType() == 1;
		if(world.isRemote)
			for(int i = 0; i < 4; i++)
				Botania.proxy.sparkleFX(posX + (Math.random() - 0.5) * 0.25, posY + 0.5  + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, dark ? 0.1F : 1F, dark ? 0.025F : 0.25F, dark ? 0.09F : 0.9F, 0.1F + (float) Math.random() * 0.25F, 12);
	}

	@Override
	public void remove() {
		if(world != null && world.isRemote && getPixieType() == 0)
			for(int i = 0; i < 12; i++)
				Botania.proxy.sparkleFX(posX + (Math.random() - 0.5) * 0.25, posY + 0.5  + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1F, 0.25F, 0.9F, 1F + (float) Math.random() * 0.25F, 5);
		super.remove();
	}

	@Override
	public boolean canDespawn() {
		return false;
	}

	@Override
	public boolean canBeLeashedTo(PlayerEntity player) {
		return false;
	}
}