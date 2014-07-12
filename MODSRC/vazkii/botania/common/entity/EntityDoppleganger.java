/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 12, 2014, 3:47:45 PM (GMT)]
 */
package vazkii.botania.common.entity;

import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;

public class EntityDoppleganger extends EntityLiving implements IBossDisplayData {

	public static final int SPAWN_TICKS = 100;

	private static final float MAX_HP = 5F; // TODO

	private static final String TAG_INVUL_TIME = "invulTime";

	public EntityDoppleganger(World par1World) {
		super(par1World);
		setSize(0.6F, 1.8F);
        getNavigator().setCanSwim(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, Float.MAX_VALUE));
        experienceValue = 100;
	}
	
	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(20, new Integer(0)); // Invul Time
	}

	public int getInvulTime() {
		return dataWatcher.getWatchableObjectInt(20);
	}

	public void setInvulTime(int time) {
		dataWatcher.updateObject(20, time);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_INVUL_TIME, getInvulTime());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		setInvulTime(par1nbtTagCompound.getInteger(TAG_INVUL_TIME));
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if(par1DamageSource.damageType.equals("player") && getInvulTime() == 0)
			return super.attackEntityFrom(par1DamageSource, par2);
		return false;
	}
	
	@Override
	protected void damageEntity(DamageSource par1DamageSource, float par2) {
		super.damageEntity(par1DamageSource, par2);
		if(par1DamageSource.getEntity() != null) {
			Vector3 thisVector = Vector3.fromEntityCenter(this);
			Vector3 playerVector = Vector3.fromEntityCenter(par1DamageSource.getEntity());
			Vector3 motionVector = thisVector.copy().sub(playerVector).copy().normalize();
			
			motionX = motionVector.x;
			motionY = 0.5;
			motionZ = motionVector.z;
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(MAX_HP);
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		int invul = getInvulTime();
		if(invul > 0) {
			if(invul < SPAWN_TICKS && invul > SPAWN_TICKS / 2 && worldObj.rand.nextInt(SPAWN_TICKS - invul + 1) == 0)
				for(int i = 0; i < 2; i++)
					spawnExplosionParticle();

			setHealth(getHealth() + ((MAX_HP - 1F) / SPAWN_TICKS));
			setInvulTime(invul - 1);
			motionY = 0;
		}
	}
}
