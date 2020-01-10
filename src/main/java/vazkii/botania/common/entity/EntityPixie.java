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

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class EntityPixie extends FlyingEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":pixie")
	public static EntityType<EntityPixie> TYPE;
	private static final DataParameter<Integer> PIXIE_TYPE = EntityDataManager.createKey(EntityPixie.class, DataSerializers.VARINT);

	private LivingEntity summoner = null;
	private float damage = 0;
	private EffectInstance effect = null;

	public EntityPixie(EntityType<EntityPixie> type, World world) {
		super(type, world);
	}

	public EntityPixie(World world) {
		this(TYPE, world);
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(PIXIE_TYPE, 0);
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
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
			double d0 = target.getX() + target.getWidth() / 2 - getX();
			double d1 = target.getY() + target.getHeight() / 2 - getY();
			double d2 = target.getZ() + target.getWidth() / 2 - getZ();
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;

			float mod = 0.45F;
			if(getPixieType() == 1)
				mod = 0.1F;

			setMotion(d0 / d3 * mod, d1 / d3 * mod, d2 / d3 * mod);

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

		renderYawOffset = rotationYaw = -((float)Math.atan2(getMotion().getX(), getMotion().getZ()))
				* 180.0F / (float)Math.PI;
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
			for(int i = 0; i < 4; i++) {
                float r = dark ? 0.1F : 1F;
                float g = dark ? 0.025F : 0.25F;
                float b = dark ? 0.09F : 0.9F;
                SparkleParticleData data = SparkleParticleData.sparkle(0.1F + (float) Math.random() * 0.25F, r, g, b, 12);
                world.addParticle(data, getX() + (Math.random() - 0.5) * 0.25, getY() + 0.5  + (Math.random() - 0.5) * 0.25, getZ() + (Math.random() - 0.5) * 0.25, 0, 0, 0);
            }
	}

	@Override
	public void remove() {
		if(world != null && world.isRemote && getPixieType() == 0)
			for(int i = 0; i < 12; i++) {
                SparkleParticleData data = SparkleParticleData.sparkle(1F + (float) Math.random() * 0.25F, 1F, 0.25F, 0.9F, 5);
                world.addParticle(data, getX() + (Math.random() - 0.5) * 0.25, getY() + 0.5  + (Math.random() - 0.5) * 0.25, getZ() + (Math.random() - 0.5) * 0.25, 0, 0, 0);
            }
		super.remove();
	}

	@Override
	public boolean canDespawn(double dist) {
		return false;
	}

	@Override
	public boolean canBeLeashedTo(PlayerEntity player) {
		return false;
	}
}