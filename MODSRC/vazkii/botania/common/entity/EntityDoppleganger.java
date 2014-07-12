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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class EntityDoppleganger extends EntityCreature implements IBossDisplayData {

	public static final int SPAWN_TICKS = 100;

	private static final float MAX_HP = 200F;

	private static final String TAG_SOURCE_X = "sourceX";
	private static final String TAG_SOURCE_Y = "sourceY";
	private static final String TAG_SOURCE_Z = "sourceZ";
	
	private static final String TAG_INVUL_TIME = "invulTime";
	private static final String TAG_AGGRO = "aggro";
	private static final String TAG_TP_DELAY = "tpDelay";
	
	private static final int[][] PYLON_LOCATIONS = new int[][] {
		{ 4, 1, 4 },
		{ 4, 1, -4 },
		{ -4, 1, 4 },
		{ -4, 1, -4 }
	};
	
	boolean spawnLandmines = false;
	boolean spawnPixies = false;

	public EntityDoppleganger(World par1World) {
		super(par1World);
		setSize(0.6F, 1.8F);
		getNavigator().setCanSwim(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, Float.MAX_VALUE));
		isImmuneToFire = true;
		experienceValue = 825;
	}
	
	public static boolean spawn(ItemStack par1ItemStack, World par3World, int par4, int par5, int par6) {
		Block block = par3World.getBlock(par4, par5, par6);
		if(block == Blocks.beacon && !par3World.isRemote) {
			for(int[] coords : PYLON_LOCATIONS) {
				int x = par4 + coords[0];
				int y = par5 + coords[1];
				int z = par6 + coords[2];

				Block blockat = par3World.getBlock(x, y, z);
				int meta = par3World.getBlockMetadata(x, y, z);
				if(blockat != ModBlocks.pylon || meta != 2)
					return false;
			}
			
			par1ItemStack.stackSize--;
			EntityDoppleganger e = new EntityDoppleganger(par3World);
			e.setPosition(par4 + 0.5, par5 + 3, par6 + 0.5);
			e.setInvulTime(EntityDoppleganger.SPAWN_TICKS);
			e.setHealth(1F);
			e.setSource(par4, par5, par6);
			par3World.playSoundAtEntity(e, "mob.enderdragon.growl", 10F, 0.1F);
			par3World.spawnEntityInWorld(e);
			return true;
		}
		return false;
	}

	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(20, 0); // Invul Time
		dataWatcher.addObject(21, (byte) 0); // Aggro
		dataWatcher.addObject(22, 0); // TP Delay
		dataWatcher.addObject(23, 0); // Source X
		dataWatcher.addObject(24, 0); // Source Y
		dataWatcher.addObject(25, 0); // Source Z
	}

	public int getInvulTime() {
		return dataWatcher.getWatchableObjectInt(20);
	}

	public boolean isAggored() {
		return dataWatcher.getWatchableObjectByte(21) == 1;
	}

	public int getTPDelay() {
		return dataWatcher.getWatchableObjectInt(22);
	}
	
	public ChunkCoordinates getSource() {
		int x = dataWatcher.getWatchableObjectInt(23);
		int y = dataWatcher.getWatchableObjectInt(24);
		int z = dataWatcher.getWatchableObjectInt(25);
		return new ChunkCoordinates(x, y, z);
	}

	public void setInvulTime(int time) {
		dataWatcher.updateObject(20, time);
	}

	public void setAggroed(boolean aggored) {
		dataWatcher.updateObject(21, (byte) (aggored ? 1 : 0));
	}

	public void setTPDelay(int delay) {
		dataWatcher.updateObject(22, delay);
	}
	
	public void setSource(int x, int y, int z) {
		dataWatcher.updateObject(23, x);
		dataWatcher.updateObject(24, y);
		dataWatcher.updateObject(25, z);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_INVUL_TIME, getInvulTime());
		par1nbtTagCompound.setBoolean(TAG_AGGRO, isAggored());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		setInvulTime(par1nbtTagCompound.getInteger(TAG_INVUL_TIME));
		setAggroed(par1nbtTagCompound.getBoolean(TAG_AGGRO));
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if((par1DamageSource.damageType.equals("player") || par1DamageSource.getEntity() instanceof EntityPixie) && getInvulTime() == 0)
			return super.attackEntityFrom(par1DamageSource, par2);
		return false;
	}

	@Override
	protected void damageEntity(DamageSource par1DamageSource, float par2) {
		super.damageEntity(par1DamageSource, par2);

		Entity attacker = par1DamageSource.getEntity(); 
		if(attacker != null) {
			Vector3 thisVector = Vector3.fromEntityCenter(this);
			Vector3 playerVector = Vector3.fromEntityCenter(attacker);
			Vector3 motionVector = thisVector.copy().sub(playerVector).copy().normalize().multiply(0.75);
			
			if(getHealth() > 0) {
				motionX = -motionVector.x;
				motionY = 0.5;
				motionZ = -motionVector.z;
				setTPDelay(4);
				spawnPixies = isAggored();
			}
			
			setAggroed(true);
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(MAX_HP);
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	@Override
	protected void dropFewItems(boolean par1, int par2) {
		if(par1) 
			entityDropItem(new ItemStack(ModItems.manaResource, 12, 5), 1F);
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		ChunkCoordinates source = getSource();

		float range = 32F;
		List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(source.posX + 0.5 - range, source.posY + 0.5 - range, source.posZ + 0.5 - range, source.posX + 0.5 + range, source.posY + 0.5 + range, source.posZ + 0.5 + range));
		
		range = 12F;
		List<EntityPlayer> playersInside = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(source.posX + 0.5 - range, source.posY + 0.5 - range, source.posZ + 0.5 - range, source.posX + 0.5 + range, source.posY + 0.5 + range, source.posZ + 0.5 + range));
		
		if(players.isEmpty())
			setDead();
		else for(EntityPlayer player : players) {
			List<PotionEffect> remove = new ArrayList();
			Collection<PotionEffect> active = player.getActivePotionEffects();
			for(PotionEffect effect : active)
				if(effect.getDuration() < 200 && !ReflectionHelper.<Boolean, Potion>getPrivateValue(Potion.class, Potion.potionTypes[effect.getPotionID()], LibObfuscation.IS_BAD_EFFECT))
					remove.add(effect);
			
			active.removeAll(remove);
			
			if(!playersInside.contains(player)) {
				Vector3 sourceVector = new Vector3(source.posX + 0.5, source.posY + 0.5, source.posZ + 0.5);
				Vector3 playerVector = Vector3.fromEntityCenter(player);
				Vector3 motion = sourceVector.copy().sub(playerVector).copy().normalize();
				
				player.motionX = motion.x;
				player.motionY = 0.2;
				player.motionZ = motion.z;
			}
		}
		
		if(isDead)
			return;
		
		int invul = getInvulTime();
		if(invul > 0) {
			if(invul < SPAWN_TICKS && invul > SPAWN_TICKS / 2 && worldObj.rand.nextInt(SPAWN_TICKS - invul + 1) == 0)
				for(int i = 0; i < 2; i++)
					spawnExplosionParticle();

			setHealth(getHealth() + ((MAX_HP - 1F) / SPAWN_TICKS));
			setInvulTime(invul - 1);
			motionY = 0;
		} else {
			if(isAggored()) {
				if(getTPDelay() > 0) {
					setTPDelay(getTPDelay() - 1);
					if(getTPDelay() == 0 && getHealth() > 0) {
						while(!teleportRandomly());
						if(spawnLandmines && !worldObj.isRemote)
							for(int i = 0; i < 6; i++) {
								int x = source.posX - 10 + rand.nextInt(20); 
								int z = source.posZ - 10 + rand.nextInt(20); 
								int y = worldObj.getTopSolidOrLiquidBlock(x, z);
								
								EntityMagicLandmine landmine = new EntityMagicLandmine(worldObj);
								landmine.setPosition(x + 0.5, y, z + 0.5);
								landmine.summoner = this;
								worldObj.spawnEntityInWorld(landmine);
							}
						
						if(!worldObj.isRemote)
							for(int i = 0; i < (spawnPixies ? worldObj.rand.nextInt(3) : 1); i++) {
								EntityPixie pixie = new EntityPixie(worldObj);
								pixie.setProps(players.get(rand.nextInt(players.size())), this, 1, 6);
								pixie.setPosition(posX + width / 2, posY + 2, posZ + width / 2);
								worldObj.spawnEntityInWorld(pixie);
							}
						
						setTPDelay(50);
						spawnLandmines = true;
						spawnPixies = false;
					}
				}
			} else {
				range = 3F;
				players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range));
				if(!players.isEmpty())
					damageEntity(DamageSource.causePlayerDamage(players.get(0)), 0);
			}
		}
	}

	// EntityEnderman code below ============================================================================
	
	protected boolean teleportRandomly() {
		double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
		double d1 = this.posY + (double)(this.rand.nextInt(64) - 32);
		double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
		return this.teleportTo(d0, d1, d2);
	}

	protected boolean teleportTo(double par1, double par3, double par5) {
		double d3 = this.posX;
		double d4 = this.posY;
		double d5 = this.posZ;
		this.posX = par1;
		this.posY = par3;
		this.posZ = par5;
		boolean flag = false;
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.posY);
		int k = MathHelper.floor_double(this.posZ);

		if(this.worldObj.blockExists(i, j, k)) {
			boolean flag1 = false;

			while(!flag1 && j > 0) {
				Block block = this.worldObj.getBlock(i, j - 1, k);

				if(block.getMaterial().blocksMovement())
					flag1 = true;
				else {
					--this.posY;
					--j;
				}
			}

			if(flag1) {
				this.setPosition(this.posX, this.posY, this.posZ);

				if(this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox))
					flag = true;
				
				// Prevent out of bounds teleporting
				ChunkCoordinates source = getSource();
				if(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(posX, posY, posZ, source.posX, source.posY, source.posZ) > 15)
					flag = false;
			}
		}

		if (!flag) {
			this.setPosition(d3, d4, d5);
			return false;
		} else  {
			short short1 = 128;

			for(int l = 0; l < short1; ++l)  {
				double d6 = (double)l / ((double)short1 - 1.0D);
				float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
				float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
				float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
				double d7 = d3 + (this.posX - d3) * d6 + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D;
				double d8 = d4 + (this.posY - d4) * d6 + this.rand.nextDouble() * (double)this.height;
				double d9 = d5 + (this.posZ - d5) * d6 + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D;
				this.worldObj.spawnParticle("portal", d7, d8, d9, (double)f, (double)f1, (double)f2);
			}

			this.worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
			this.playSound("mob.endermen.portal", 1.0F, 1.0F);
			return true;
		}
	}
}
