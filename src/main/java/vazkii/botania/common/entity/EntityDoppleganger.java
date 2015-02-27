/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 12, 2014, 3:47:45 PM (GMT)]
 */
package vazkii.botania.common.entity;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import org.lwjgl.opengl.ARBShaderObjects;

import vazkii.botania.api.boss.IBotaniaBossWithShader;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.core.handler.BossBarHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityDoppleganger extends EntityCreature implements IBotaniaBossWithShader {

	public static final int SPAWN_TICKS = 100;

	public static final int MOB_SPAWN_START_TICKS = 20;
	public static final int MOB_SPAWN_END_TICKS = 80;
	public static final int MOB_SPAWN_BASE_TICKS = 800;
	public static final int MOB_SPAWN_TICKS = MOB_SPAWN_BASE_TICKS + MOB_SPAWN_START_TICKS + MOB_SPAWN_END_TICKS;
	public static final int MOB_SPAWN_WAVES = 10;
	public static final int MOB_SPAWN_WAVE_TIME = MOB_SPAWN_BASE_TICKS / MOB_SPAWN_WAVES;

	private static final float MAX_HP = 300F;

	private static final String TAG_INVUL_TIME = "invulTime";
	private static final String TAG_AGGRO = "aggro";
	private static final String TAG_SOURCE_X = "sourceX";
	private static final String TAG_SOURCE_Y = "sourceY";
	private static final String TAG_SOURCE_Z = "sourcesZ";
	private static final String TAG_MOB_SPAWN_TICKS = "mobSpawnTicks";
	private static final String TAG_HARD_MODE = "hardMode";

	private static final int[][] PYLON_LOCATIONS = new int[][] {
		{ 4, 1, 4 },
		{ 4, 1, -4 },
		{ -4, 1, 4 },
		{ -4, 1, -4 }
	};

	boolean spawnLandmines = false;
	boolean spawnPixies = false;
	boolean anyWithArmor = false;

	public EntityDoppleganger(World par1World) {
		super(par1World);
		setSize(0.6F, 1.8F);
		getNavigator().setCanSwim(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, Float.MAX_VALUE));
		isImmuneToFire = true;
		experienceValue = 825;
	}

	public static boolean spawn(EntityPlayer player, ItemStack par1ItemStack, World par3World, int par4, int par5, int par6, boolean hard) {
		Block block = par3World.getBlock(par4, par5, par6);
		if(block == Blocks.beacon && !par3World.isRemote) {
			if(par3World.difficultySetting == EnumDifficulty.PEACEFUL) {
				player.addChatMessage(new ChatComponentTranslation("botaniamisc.peacefulNoob").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				return false;
			}

			for(int[] coords : PYLON_LOCATIONS) {
				int x = par4 + coords[0];
				int y = par5 + coords[1];
				int z = par6 + coords[2];

				Block blockat = par3World.getBlock(x, y, z);
				int meta = par3World.getBlockMetadata(x, y, z);
				if(blockat != ModBlocks.pylon || meta != 2) {
					player.addChatMessage(new ChatComponentTranslation("botaniamisc.needsCatalysts").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					return false;
				}
			}

			par1ItemStack.stackSize--;
			EntityDoppleganger e = new EntityDoppleganger(par3World);
			e.setPosition(par4 + 0.5, par5 + 3, par6 + 0.5);
			e.setInvulTime(SPAWN_TICKS);
			e.setHealth(1F);
			e.setSource(par4, par5, par6);
			e.setMobSpawnTicks(MOB_SPAWN_TICKS);
			e.setHardMode(hard);
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
		dataWatcher.addObject(26, 0); // Ticks spawning mobs
		dataWatcher.addObject(27, (byte) 0); // Hard Mode
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

	public int getMobSpawnTicks() {
		return dataWatcher.getWatchableObjectInt(26);
	}

	public boolean isHardMode() {
		return dataWatcher.getWatchableObjectByte(27) == 1;
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

	public void setMobSpawnTicks(int ticks) {
		dataWatcher.updateObject(26, ticks);
	}

	public void setHardMode(boolean hardMode) {
		dataWatcher.updateObject(27, (byte) (hardMode ? 1 : 0));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_INVUL_TIME, getInvulTime());
		par1nbtTagCompound.setBoolean(TAG_AGGRO, isAggored());
		par1nbtTagCompound.setInteger(TAG_MOB_SPAWN_TICKS, getMobSpawnTicks());

		ChunkCoordinates source = getSource();
		par1nbtTagCompound.setInteger(TAG_SOURCE_X, source.posX);
		par1nbtTagCompound.setInteger(TAG_SOURCE_Y, source.posY);
		par1nbtTagCompound.setInteger(TAG_SOURCE_Z, source.posZ);

		par1nbtTagCompound.setBoolean(TAG_HARD_MODE, isHardMode());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		setInvulTime(par1nbtTagCompound.getInteger(TAG_INVUL_TIME));
		setAggroed(par1nbtTagCompound.getBoolean(TAG_AGGRO));
		setMobSpawnTicks(par1nbtTagCompound.getInteger(TAG_MOB_SPAWN_TICKS));

		int x = par1nbtTagCompound.getInteger(TAG_SOURCE_X);
		int y = par1nbtTagCompound.getInteger(TAG_SOURCE_Y);
		int z = par1nbtTagCompound.getInteger(TAG_SOURCE_Z);
		setSource(x, y, z);

		setHardMode(par1nbtTagCompound.getBoolean(TAG_HARD_MODE));
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if((par1DamageSource.damageType.equals("player") || par1DamageSource.getEntity() instanceof EntityPixie) && par1DamageSource.getEntity() != null && isTruePlayer(par1DamageSource.getEntity()) && getInvulTime() == 0)
			return super.attackEntityFrom(par1DamageSource, par2 * (isHardMode() ? 0.6F : 1F));
		return false;
	}

	private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$");
	public static boolean isTruePlayer(Entity e) {
		if(!(e instanceof EntityPlayer))
			return false;

		EntityPlayer player = (EntityPlayer) e;

		String name = player.getCommandSenderName();
		return !(player instanceof FakePlayer || FAKE_PLAYER_PATTERN.matcher(name).matches());
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
	public void onDeath(DamageSource p_70645_1_) {
		super.onDeath(p_70645_1_);
		EntityLivingBase entitylivingbase = func_94060_bK();
		if(entitylivingbase instanceof EntityPlayer) {
			((EntityPlayer) entitylivingbase).addStat(ModAchievements.gaiaGuardianKill, 1);
			if(!anyWithArmor)
				((EntityPlayer) entitylivingbase).addStat(ModAchievements.gaiaGuardianNoArmor, 1);
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
		if(par1) {
			boolean hard = isHardMode();
			entityDropItem(new ItemStack(ModItems.manaResource, hard ? 16 : 8, 5), 1F);

			if(hard) {
				entityDropItem(new ItemStack(ModItems.ancientWill, 1, rand.nextInt(6)), 1F);
				if(Math.random() < 0.25)
					entityDropItem(new ItemStack(ModItems.overgrowthSeed, rand.nextInt(3) + 1), 1F);
				if(Math.random() < 0.5) {
					boolean voidLotus = Math.random() < 0.3F;
					entityDropItem(new ItemStack(ModItems.blackLotus, voidLotus ? 1 : rand.nextInt(3) + 1, voidLotus ? 1 : 0), 1F);
				}
				if(Math.random() < 0.9)
					entityDropItem(new ItemStack(ModItems.manaResource, 16 + rand.nextInt(12)), 1F);
				if(Math.random() < 0.7)
					entityDropItem(new ItemStack(ModItems.manaResource, 8 + rand.nextInt(6), 1), 1F);
				if(Math.random() < 0.5)
					entityDropItem(new ItemStack(ModItems.manaResource, 4 + rand.nextInt(3), 1), 1F);
				if(Math.random() < 0.3)
					entityDropItem(new ItemStack(ModItems.rune, 2 + rand.nextInt(3), rand.nextInt(16)), 1F);
			}
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if(!worldObj.isRemote && worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
			setDead();

		ChunkCoordinates source = getSource();
		boolean hard = isHardMode();

		float range = 32F;
		List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(source.posX + 0.5 - range, source.posY + 0.5 - range, source.posZ + 0.5 - range, source.posX + 0.5 + range, source.posY + 0.5 + range, source.posZ + 0.5 + range));

		range = 12F;
		for(int i = 0; i < 360; i += 8) {
			float r = 0.6F;
			float g = 0F;
			float b = 0.2F;
			float m = 0.15F;
			float mv = 0.35F;

			float rad = i * (float) Math.PI / 180F;
			double x = source.posX + 0.5 - Math.cos(rad) * range;
			double y = source.posY + 0.5;
			double z = source.posZ + 0.5 - Math.sin(rad) * range;

			Botania.proxy.wispFX(worldObj, x, y, z, r, g, b, 0.5F, (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * mv, (float) (Math.random() - 0.5F) * m);
		}

		if(players.isEmpty() && !worldObj.playerEntities.isEmpty())
			setDead();
		else {
			for(EntityPlayer player : players) {
				if(player.inventory.armorInventory[0] != null || player.inventory.armorInventory[1] != null || player.inventory.armorInventory[2] != null || player.inventory.armorInventory[3] != null)
					anyWithArmor = true;

				List<PotionEffect> remove = new ArrayList();
				Collection<PotionEffect> active = player.getActivePotionEffects();
				for(PotionEffect effect : active)
					if(effect.getDuration() < 200 && !ReflectionHelper.<Boolean, Potion>getPrivateValue(Potion.class, Potion.potionTypes[effect.getPotionID()], LibObfuscation.IS_BAD_EFFECT))
						remove.add(effect);

				active.removeAll(remove);

				player.capabilities.isFlying = player.capabilities.isFlying && player.capabilities.isCreativeMode;

				if(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(player.posX, player.posY, player.posZ, source.posX + 0.5, source.posY + 0.5, source.posZ + 0.5) >= range) {
					Vector3 sourceVector = new Vector3(source.posX + 0.5, source.posY + 0.5, source.posZ + 0.5);
					Vector3 playerVector = Vector3.fromEntityCenter(player);
					Vector3 motion = sourceVector.copy().sub(playerVector).copy().normalize();

					player.motionX = motion.x;
					player.motionY = 0.2;
					player.motionZ = motion.z;
				}
			}
		}

		if(isDead)
			return;

		int invul = getInvulTime();
		int mobTicks = getMobSpawnTicks();
		boolean spawnMissiles = hard && ticksExisted % 15 < 4;

		if(invul > 0 && mobTicks == MOB_SPAWN_TICKS) {
			if(invul < SPAWN_TICKS && invul > SPAWN_TICKS / 2 && worldObj.rand.nextInt(SPAWN_TICKS - invul + 1) == 0)
				for(int i = 0; i < 2; i++)
					spawnExplosionParticle();

			setHealth(getHealth() + (MAX_HP - 1F) / SPAWN_TICKS);
			setInvulTime(invul - 1);
			motionY = 0;
		} else {
			if(isAggored()) {
				boolean dying = getHealth() / getMaxHealth() < 0.2;
				if(dying && mobTicks > 0) {
					motionX = 0;
					motionY = 0;
					motionZ = 0;

					int reverseTicks = MOB_SPAWN_TICKS - mobTicks;
					if(reverseTicks < MOB_SPAWN_START_TICKS) {
						motionY = 0.2;
						setInvulTime(invul + 1);
					}

					if(reverseTicks > MOB_SPAWN_START_TICKS * 2 && mobTicks > MOB_SPAWN_END_TICKS && mobTicks % MOB_SPAWN_WAVE_TIME == 0 && !worldObj.isRemote) {
						for(int i = 0; i < 3 + worldObj.rand.nextInt(2); i++) {
							EntityLiving entity = null;
							switch(worldObj.rand.nextInt(2)) {
							case 0 : {
								entity = new EntityZombie(worldObj);
								if(worldObj.rand.nextInt(hard ? 9 : 12) == 0)
									entity = new EntityWitch(worldObj);

								break;
							}
							case 1 : {
								entity = new EntitySkeleton(worldObj);
								((EntitySkeleton) entity).setCurrentItemOrArmor(0, new ItemStack(Items.bow));
								if(worldObj.rand.nextInt(8) == 0) {
									((EntitySkeleton) entity).setSkeletonType(1);
									((EntitySkeleton) entity).setCurrentItemOrArmor(0, new ItemStack(hard ? ModItems.elementiumSword : Items.stone_sword));
								}
								break;
							}
							case 3 : {
								for(int j = 0; j < 1 + worldObj.rand.nextInt(hard ? 8 : 5); j++) {
									EntityPixie pixie = new EntityPixie(worldObj);
									pixie.setProps(players.get(rand.nextInt(players.size())), this, 1, 8);
									pixie.setPosition(posX + width / 2, posY + 2, posZ + width / 2);
									worldObj.spawnEntityInWorld(pixie);
								}
							}
							}

							if(entity != null) {
								range = 6F;
								entity.setPosition(posX + 0.5 + Math.random() * range - range / 2, posY - 1, posZ + 0.5 + Math.random() * range - range / 2);
								worldObj.spawnEntityInWorld(entity);
							}
						}

						if(hard && ticksExisted % 3 < 2) {
							spawnMissile();
							spawnMissiles = false;
						}
					}

					setMobSpawnTicks(mobTicks - 1);
					setTPDelay(10);
				} else if(getTPDelay() > 0 && !worldObj.isRemote) {
					if(invul > 0)
						setInvulTime(invul - 1);

					setTPDelay(getTPDelay() - 1);
					if(getTPDelay() == 0 && getHealth() > 0) {
						int tries = 0;
						while(!teleportRandomly() && tries < 50)
							tries++;
						if(tries >= 50)
							teleportTo(source.posX + 0.5, source.posY + 1.6, source.posZ + 0.5);

						if(spawnLandmines)
							for(int i = 0; i < 6; i++) {
								int x = source.posX - 10 + rand.nextInt(20);
								int z = source.posZ - 10 + rand.nextInt(20);
								int y = worldObj.getTopSolidOrLiquidBlock(x, z);

								EntityMagicLandmine landmine = new EntityMagicLandmine(worldObj);
								landmine.setPosition(x + 0.5, y, z + 0.5);
								landmine.summoner = this;
								worldObj.spawnEntityInWorld(landmine);
							}

						for(int i = 0; i < (spawnPixies ? worldObj.rand.nextInt(hard ? 6 : 3) : 1); i++) {
							EntityPixie pixie = new EntityPixie(worldObj);
							pixie.setProps(players.get(rand.nextInt(players.size())), this, 1, 8);
							pixie.setPosition(posX + width / 2, posY + 2, posZ + width / 2);
							worldObj.spawnEntityInWorld(pixie);
						}

						setTPDelay(hard ? dying ? 20 : 45 : dying ? 40 : 60);
						spawnLandmines = true;
						spawnPixies = false;
					}
				}

				if(spawnMissiles)
					spawnMissile();
			} else {
				range = 3F;
				players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range));
				if(!players.isEmpty())
					damageEntity(DamageSource.causePlayerDamage(players.get(0)), 0);
			}
		}
	}

	void spawnMissile() {
		if(!worldObj.isRemote) {
			EntityMagicMissile missile = new EntityMagicMissile(this, true);
			missile.setPosition(posX + (Math.random() - 0.5 * 0.1), posY + 2.4 + (Math.random() - 0.5 * 0.1), posZ + (Math.random() - 0.5 * 0.1));
			if(missile.getTarget()) {
				worldObj.playSoundAtEntity(this, "botania:missile", 0.6F, 0.8F + (float) Math.random() * 0.2F);
				worldObj.spawnEntityInWorld(missile);
			}
		}
	}

	// EntityEnderman code below ============================================================================

	protected boolean teleportRandomly() {
		double d0 = posX + (rand.nextDouble() - 0.5D) * 64.0D;
		double d1 = posY + (rand.nextInt(64) - 32);
		double d2 = posZ + (rand.nextDouble() - 0.5D) * 64.0D;
		return teleportTo(d0, d1, d2);
	}

	protected boolean teleportTo(double par1, double par3, double par5) {
		double d3 = posX;
		double d4 = posY;
		double d5 = posZ;
		posX = par1;
		posY = par3;
		posZ = par5;
		boolean flag = false;
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);

		if(worldObj.blockExists(i, j, k)) {
			boolean flag1 = false;

			while(!flag1 && j > 0) {
				Block block = worldObj.getBlock(i, j - 1, k);

				if(block.getMaterial().blocksMovement())
					flag1 = true;
				else {
					--posY;
					--j;
				}
			}

			if(flag1) {
				setPosition(posX, posY, posZ);

				if(worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() && !worldObj.isAnyLiquid(boundingBox))
					flag = true;

				// Prevent out of bounds teleporting
				ChunkCoordinates source = getSource();
				if(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(posX, posY, posZ, source.posX, source.posY, source.posZ) > 12)
					flag = false;
			}
		}

		if (!flag) {
			setPosition(d3, d4, d5);
			return false;
		} else  {
			short short1 = 128;

			for(int l = 0; l < short1; ++l)  {
				double d6 = l / (short1 - 1.0D);
				float f = (rand.nextFloat() - 0.5F) * 0.2F;
				float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
				float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
				double d7 = d3 + (posX - d3) * d6 + (rand.nextDouble() - 0.5D) * width * 2.0D;
				double d8 = d4 + (posY - d4) * d6 + rand.nextDouble() * height;
				double d9 = d5 + (posZ - d5) * d6 + (rand.nextDouble() - 0.5D) * width * 2.0D;
				worldObj.spawnParticle("portal", d7, d8, d9, f, f1, f2);
			}

			worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
			playSound("mob.endermen.portal", 1.0F, 1.0F);
			return true;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getBossBarTexture() {
		return BossBarHandler.defaultBossBar;
	}

	@SideOnly(Side.CLIENT)
	private static Rectangle barRect;
	@SideOnly(Side.CLIENT)
	private static Rectangle hpBarRect;

	@Override
	@SideOnly(Side.CLIENT)
	public Rectangle getBossBarTextureRect() {
		if(barRect == null)
			barRect = new Rectangle(0, 0, 185, 15);
		return barRect;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Rectangle getBossBarHPTextureRect() {
		if(hpBarRect == null)
			hpBarRect = new Rectangle(0, barRect.y + barRect.height, 181, 7);
		return hpBarRect;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void bossBarRenderCallback() {
		// NO-OP
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBossBarShaderProgram(boolean background) {
		return background ? 0 : ShaderHelper.dopplegangerBar;
	}

	@SideOnly(Side.CLIENT)
	private ShaderCallback shaderCallback;

	@Override
	@SideOnly(Side.CLIENT)
	public ShaderCallback getBossBarShaderCallback(boolean background, int shader) {
		if(shaderCallback == null)
			shaderCallback = new ShaderCallback() {

			@Override
			public void call(int shader) {
				int grainIntensityUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "grainIntensity");
				int hpFractUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "hpFract");

				float time = getInvulTime();
				float grainIntensity = time > 20 ? 1F : Math.max(isHardMode() ? 0.5F : 0F, time / 20F);

				ARBShaderObjects.glUniform1fARB(grainIntensityUniform, grainIntensity);
				ARBShaderObjects.glUniform1fARB(hpFractUniform, getHealth() / getMaxHealth());
			}

		};

		return background ? null : shaderCallback;
	}
}
