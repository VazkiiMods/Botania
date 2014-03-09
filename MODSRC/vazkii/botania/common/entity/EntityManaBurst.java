/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 26, 2014, 5:09:12 PM (GMT)]
 */
package vazkii.botania.common.entity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileSpreader;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;
import cpw.mods.fml.common.network.PacketDispatcher;

public class EntityManaBurst extends EntityThrowable implements IManaBurst {

	private static final String TAG_COLOR = "color";
	private static final String TAG_MANA = "mana";
	private static final String TAG_STARTING_MANA = "startingMana";
	private static final String TAG_MIN_MANA_LOSS = "minManaLoss";
	private static final String TAG_TICK_MANA_LOSS = "manaLossTick";
	private static final String TAG_SPREADER_X = "spreaderX";
	private static final String TAG_SPREADER_Y = "spreaderY";
	private static final String TAG_SPREADER_Z = "spreaderZ";
	private static final String TAG_GRAVITY = "gravity";
	private static final String TAG_LAST_MOTION_X = "lastMotionX";
	private static final String TAG_LAST_MOTION_Y = "lastMotionY";
	private static final String TAG_LAST_MOTION_Z = "lastMotionZ";

	boolean fake = false;

	final int dataWatcherEntries = 10;
	final int dataWatcherStart = 32 - dataWatcherEntries;

	List<String> alreadyCollidedAt = new ArrayList();

	public EntityManaBurst(World world) {
		super(world);
		setSize(0F, 0F);
		for(int i = 0; i < dataWatcherEntries; i++) {
			int j = dataWatcherStart + i;
			if(i == 4 || i == 5 || i == 10 || i == 11 || i == 12)
				dataWatcher.addObject(j, 0F);
			else if(i == 9)
				dataWatcher.addObject(j, new ItemStack(1, 0, 0));
			else dataWatcher.addObject(j, 0);

			dataWatcher.setObjectWatched(j);
		}
	}

	public EntityManaBurst(World par1World, TileSpreader spreader, boolean fake) {
		this(par1World);
		this.fake = fake;

		setBurstSourceCoords(spreader.xCoord, spreader.yCoord, spreader.zCoord);
		setLocationAndAngles(spreader.xCoord + 0.5, spreader.yCoord + 0.5, spreader.zCoord + 0.5, 0, 0);
		rotationYaw = -(spreader.rotationX + 90F);
		rotationPitch = spreader.rotationY;

		float f = 0.4F;
		double mx = MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f / 2D;
		double mz = -(MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f) / 2D;
		double my = MathHelper.sin((rotationPitch + func_70183_g()) / 180.0F * (float) Math.PI) * f / 2D;
		setMotion(mx, my, mz);
	}

	float accumulatedManaLoss = 0;

	private int ticksInAir;

	// Hacked code copied from super.onUpdate, to use Vector3 rather than the world vector pool
	public void superUpdate() {
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;
		onEntityUpdate();

		if(throwableShake > 0)
			--throwableShake;

		Vec3 vec3 = new Vector3(posX, posY, posZ).toVec3D();
		Vec3 vec31 = new Vector3(posX + motionX, posY + motionY, posZ + motionZ).toVec3D();
		MovingObjectPosition movingobjectposition = clip(vec3, vec31);

		if(movingobjectposition != null)
			vec31 = new Vector3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord).toVec3D();

		if(!worldObj.isRemote) {
			Entity entity = null;
			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;
			EntityLivingBase entitylivingbase = getThrower();

			for(int j = 0; j < list.size(); ++j) {
				Entity entity1 = (Entity) list.get(j);

				if(entity1.canBeCollidedWith() && (entity1 != entitylivingbase || ticksInAir >= 5)) {
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f, f, f);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

					if(movingobjectposition1 != null) {
						double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if(entity != null)
				movingobjectposition = new MovingObjectPosition(entity);
		}

		if(movingobjectposition != null)
			onImpact(movingobjectposition);

		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		float f1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		rotationYaw = (float)(Math.atan2(motionX, motionZ) * 180.0D / Math.PI);

		for(rotationPitch = (float)(Math.atan2(motionY, f1) * 180.0D / Math.PI); rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F);

		while(rotationPitch - prevRotationPitch >= 180.0F)
			prevRotationPitch += 360.0F;

		while(rotationYaw - prevRotationYaw < -180.0F)
			prevRotationYaw -= 360.0F;

		while(rotationYaw - prevRotationYaw >= 180.0F)
			prevRotationYaw += 360.0F;

		rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
		rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
		float f3 = getGravityVelocity();
		motionY -= f3;
		setPosition(posX, posY, posZ);
	}

	public MovingObjectPosition clip(Vec3 par1Vec3, Vec3 par2Vec3) {
		boolean par3 = false;
		boolean par4 = false;
		if (!Double.isNaN(par1Vec3.xCoord) && !Double.isNaN(par1Vec3.yCoord) && !Double.isNaN(par1Vec3.zCoord)) {
			if (!Double.isNaN(par2Vec3.xCoord) && !Double.isNaN(par2Vec3.yCoord) && !Double.isNaN(par2Vec3.zCoord)) {
				int i = MathHelper.floor_double(par2Vec3.xCoord);
				int j = MathHelper.floor_double(par2Vec3.yCoord);
				int k = MathHelper.floor_double(par2Vec3.zCoord);
				int l = MathHelper.floor_double(par1Vec3.xCoord);
				int i1 = MathHelper.floor_double(par1Vec3.yCoord);
				int j1 = MathHelper.floor_double(par1Vec3.zCoord);
				int k1 = worldObj.getBlockId(l, i1, j1);
				int l1 = worldObj.getBlockMetadata(l, i1, j1);
				Block block = Block.blocksList[k1];

				if (block != null && (!par4 || block == null || block.getCollisionBoundingBoxFromPool(worldObj, l, i1, j1) != null) && k1 > 0 && block.canCollideCheck(l1, par3)) {
					MovingObjectPosition movingobjectposition = block.collisionRayTrace(worldObj, l, i1, j1, par1Vec3, par2Vec3);

					if (movingobjectposition != null)
						return movingobjectposition;
				}

				k1 = 200;

				while (k1-- >= 0) {
					if (Double.isNaN(par1Vec3.xCoord) || Double.isNaN(par1Vec3.yCoord) || Double.isNaN(par1Vec3.zCoord))
						return null;

					if (l == i && i1 == j && j1 == k)
						return null;

					boolean flag2 = true;
					boolean flag3 = true;
					boolean flag4 = true;
					double d0 = 999.0D;
					double d1 = 999.0D;
					double d2 = 999.0D;

					if (i > l)
						d0 = (double)l + 1.0D;
					else if (i < l)
						d0 = (double)l + 0.0D;
					else flag2 = false;

					if (j > i1)
						d1 = (double)i1 + 1.0D;
					else if (j < i1)
						d1 = (double)i1 + 0.0D;
					else flag3 = false;

					if (k > j1)
						d2 = (double)j1 + 1.0D;
					else if (k < j1)
						d2 = (double)j1 + 0.0D;
					else flag4 = false;

					double d3 = 999.0D;
					double d4 = 999.0D;
					double d5 = 999.0D;
					double d6 = par2Vec3.xCoord - par1Vec3.xCoord;
					double d7 = par2Vec3.yCoord - par1Vec3.yCoord;
					double d8 = par2Vec3.zCoord - par1Vec3.zCoord;

					if (flag2)
						d3 = (d0 - par1Vec3.xCoord) / d6;

					if (flag3)
						d4 = (d1 - par1Vec3.yCoord) / d7;

					if (flag4)
						d5 = (d2 - par1Vec3.zCoord) / d8;

					byte b0;

					if (d3 < d4 && d3 < d5) {
						if (i > l)
							b0 = 4;
						else b0 = 5;

						par1Vec3.xCoord = d0;
						par1Vec3.yCoord += d7 * d3;
						par1Vec3.zCoord += d8 * d3;
					} else if (d4 < d5) {
						if (j > i1)
							b0 = 0;
						else b0 = 1;

						par1Vec3.xCoord += d6 * d4;
						par1Vec3.yCoord = d1;
						par1Vec3.zCoord += d8 * d4;
					} else {
						if (k > j1)
							b0 = 2;
						else b0 = 3;

						par1Vec3.xCoord += d6 * d5;
						par1Vec3.yCoord += d7 * d5;
						par1Vec3.zCoord = d2;
					}

					Vec3 vec32 = new Vector3(par1Vec3.xCoord, par1Vec3.yCoord, par1Vec3.zCoord).toVec3D();
					l = (int)(vec32.xCoord = (double)MathHelper.floor_double(par1Vec3.xCoord));

					if (b0 == 5) {
						--l;
						++vec32.xCoord;
					}

					i1 = (int)(vec32.yCoord = (double)MathHelper.floor_double(par1Vec3.yCoord));

					if (b0 == 1) {
						--i1;
						++vec32.yCoord;
					}

					j1 = (int)(vec32.zCoord = (double)MathHelper.floor_double(par1Vec3.zCoord));

					if (b0 == 3) {
						--j1;
						++vec32.zCoord;
					}

					int i2 = worldObj.getBlockId(l, i1, j1);
					int j2 = worldObj.getBlockMetadata(l, i1, j1);
					Block block1 = Block.blocksList[i2];

					if ((!par4 || block1 == null || block1.getCollisionBoundingBoxFromPool(worldObj, l, i1, j1) != null) && i2 > 0 && block1.canCollideCheck(j2, par3)) {
						MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(worldObj, l, i1, j1, par1Vec3, par2Vec3);

						if (movingobjectposition1 != null)
							return movingobjectposition1;
					}
				}

				return null;
			}
			else return null;
		} else return null;
	}

	@Override
	public void onUpdate() {
		if(fake)
			superUpdate();
		else super.onUpdate();

		ILens lens = getLensInstance();
		if(lens != null)
			lens.updateBurst(this, getSourceLens());

		particles();

		int mana = getMana();
		if(ticksExisted >= getMinManaLoss()) {
			accumulatedManaLoss += getManaLossPerTick();
			int loss = (int) accumulatedManaLoss;
			setMana(mana - loss);
			accumulatedManaLoss -= loss;

			if(getMana() <= 0)
				setDead();
		}

		setMotion(motionX, motionY, motionZ);
	}

	@Override
	public boolean handleWaterMovement() {
		return false;
	}

	TileEntity collidedTile = null;
	boolean noParticles = false;

	public TileEntity getCollidedTile(boolean noParticles) {
		this.noParticles = noParticles;

		while(!isDead) {
			++ticksExisted;
			onUpdate();
		}

		return collidedTile;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_COLOR, getColor());
		par1nbtTagCompound.setInteger(TAG_MANA, getMana());
		par1nbtTagCompound.setInteger(TAG_STARTING_MANA, getStartingMana());
		par1nbtTagCompound.setInteger(TAG_MIN_MANA_LOSS, getMinManaLoss());
		par1nbtTagCompound.setFloat(TAG_TICK_MANA_LOSS, getManaLossPerTick());
		par1nbtTagCompound.setFloat(TAG_GRAVITY, getGravity());

		ChunkCoordinates coords = getBurstSourceChunkCoordinates();
		par1nbtTagCompound.setInteger(TAG_SPREADER_X, coords.posX);
		par1nbtTagCompound.setInteger(TAG_SPREADER_Y, coords.posY);
		par1nbtTagCompound.setInteger(TAG_SPREADER_Z, coords.posZ);

		par1nbtTagCompound.setDouble(TAG_LAST_MOTION_X, motionX);
		par1nbtTagCompound.setDouble(TAG_LAST_MOTION_Y, motionY);
		par1nbtTagCompound.setDouble(TAG_LAST_MOTION_Z, motionZ);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		setColor(par1nbtTagCompound.getInteger(TAG_COLOR));
		setMana(par1nbtTagCompound.getInteger(TAG_MANA));
		setStartingMana(par1nbtTagCompound.getInteger(TAG_STARTING_MANA));
		setMinManaLoss(par1nbtTagCompound.getInteger(TAG_MIN_MANA_LOSS));
		setManaLossPerTick(par1nbtTagCompound.getFloat(TAG_TICK_MANA_LOSS));
		setGravity(par1nbtTagCompound.getFloat(TAG_GRAVITY));

		int x = par1nbtTagCompound.getInteger(TAG_SPREADER_X);
		int y = par1nbtTagCompound.getInteger(TAG_SPREADER_Y);
		int z = par1nbtTagCompound.getInteger(TAG_SPREADER_Z);

		setBurstSourceCoords(x, y, z);

		double lastMotionX = par1nbtTagCompound.getDouble(TAG_LAST_MOTION_X);
		double lastMotionY = par1nbtTagCompound.getDouble(TAG_LAST_MOTION_Y);
		double lastMotionZ = par1nbtTagCompound.getDouble(TAG_LAST_MOTION_Z);

		setMotion(lastMotionX, lastMotionY, lastMotionZ);
	}

	public void particles() {
		if(isDead || !worldObj.isRemote)
			return;

		ILens lens = getLensInstance();
		if(lens != null && !lens.doParticles(this, getSourceLens()))
			return;

		Color color = new Color(getColor());
		float r = color.getRed() / 255F;
		float g = color.getGreen() / 255F;
		float b = color.getBlue() / 255F;

		int mana = getMana();
		int maxMana = getStartingMana();
		float size = (float) mana / (float) maxMana;

		if(fake) {
			if(getMana() == getStartingMana())
				size = 2F;

			if(!noParticles)
				Botania.proxy.sparkleFX(worldObj, posX, posY, posZ, r, g, b, 0.4F * size, 1, true);
		} else {
			if(ConfigHandler.subtlePowerSystem)
				for(int i = 0; i < 1; i++)
					Botania.proxy.wispFX(worldObj, posX, posY, posZ, r, g, b, 0.1F * size, (float) (Math.random() - 0.5F) * 0.02F, (float) (Math.random() - 0.5F) * 0.02F, (float) (Math.random() - 0.5F) * 0.01F);
			else {
				posX += motionX;
				posY += motionY;
				posZ += motionZ;

				Botania.proxy.wispFX(worldObj, posX, posY, posZ, r, g, b, 0.2F * size, (float) -motionX * 0.01F, (float) -motionY * 0.01F, (float) -motionZ * 0.01F);
				Botania.proxy.wispFX(worldObj, posX, posY, posZ, r, g, b, 0.1F * size, (float) (Math.random() - 0.5F) * 0.06F, (float) (Math.random() - 0.5F) * 0.06F, (float) (Math.random() - 0.5F) * 0.06F);

				posX -= motionX / 2.0;
				posY -= motionY / 2.0;
				posZ -= motionZ / 2.0;

				Botania.proxy.wispFX(worldObj, posX, posY, posZ, r, g, b, 0.2F * size, (float) -motionX * 0.01F, (float) -motionY * 0.01F, (float) -motionZ * 0.01F);

				posX -= motionX / 2.0;
				posY -= motionY / 2.0;
				posZ -= motionZ / 2.0;
			}
		}
	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		boolean collided = false;
		boolean dead = false;

		if(movingobjectposition.entityHit == null) {
			TileEntity tile = worldObj.getBlockTileEntity(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);

			ChunkCoordinates coords = getBurstSourceChunkCoordinates();
			if(tile != null && (tile.xCoord != coords.posX || tile.yCoord != coords.posY || tile.zCoord != coords.posZ))
				collidedTile = tile;

			if(tile == null || tile.xCoord != coords.posX || tile.yCoord != coords.posY || tile.zCoord != coords.posZ) {
				if(!fake && !noParticles && !worldObj.isRemote && tile != null && tile instanceof IManaReceiver && ((IManaReceiver) tile).canRecieveManaFromBursts()) {
					int mana = getMana();
					if(tile instanceof IManaCollector)
						mana *= ((IManaCollector) tile).getManaYieldMultiplier(this);

					((IManaReceiver) tile).recieveMana(mana);
					PacketDispatcher.sendPacketToAllInDimension(tile.getDescriptionPacket(), worldObj.provider.dimensionId);
				}

				dead = true;
			}

			collided = true;
		}

		ILens lens = getLensInstance();
		if(lens != null)
			dead = lens.collideBurst(this, movingobjectposition, collidedTile != null && collidedTile instanceof IManaReceiver && ((IManaReceiver) collidedTile).canRecieveManaFromBursts(), dead, getSourceLens());

		if(collided && !hasAlreadyCollidedAt(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ))
			alreadyCollidedAt.add(getCollisionLocString(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ));

		if(dead && !isDead) {
			if(!fake) {
				Color color = new Color(getColor());
				float r = color.getRed() / 255F;
				float g = color.getGreen() / 255F;
				float b = color.getBlue() / 255F;

				int mana = getMana();
				int maxMana = getStartingMana();
				float size = (float) mana / (float) maxMana;

				if(!ConfigHandler.subtlePowerSystem)
					for(int i = 0; i < 4; i++)
						Botania.proxy.wispFX(worldObj, posX, posY, posZ, r, g, b, 0.15F * size, (float) (Math.random() - 0.5F) * 0.04F, (float) (Math.random() - 0.5F) * 0.04F, (float) (Math.random() - 0.5F) * 0.04F);
				Botania.proxy.sparkleFX(worldObj, (float) posX, (float) posY, (float) posZ, r, g, b, 4, 2);
			}

			setDead();
		}
	}

	@Override
	public void setDead() {
		super.setDead();

		if(!fake) {
			ChunkCoordinates coords = getBurstSourceChunkCoordinates();
			TileEntity tile = worldObj.getBlockTileEntity(coords.posX, coords.posY, coords.posZ);
			if(tile != null && tile instanceof TileSpreader)
				((TileSpreader) tile).canShootBurst = true;
		}
	}

	@Override
	protected float getGravityVelocity() {
		return getGravity();
	}

	@Override
	public boolean isFake() {
		return fake;
	}

	@Override
	public int getColor() {
		return dataWatcher.getWatchableObjectInt(dataWatcherStart);
	}

	@Override
	public void setColor(int color) {
		dataWatcher.updateObject(dataWatcherStart, color);
	}

	@Override
	public int getMana() {
		return dataWatcher.getWatchableObjectInt(dataWatcherStart + 1);
	}

	@Override
	public void setMana(int mana) {
		dataWatcher.updateObject(dataWatcherStart + 1, mana);
	}

	@Override
	public int getStartingMana() {
		return dataWatcher.getWatchableObjectInt(dataWatcherStart + 2);
	}

	@Override
	public void setStartingMana(int mana) {
		dataWatcher.updateObject(dataWatcherStart + 2, mana);
	}

	@Override
	public int getMinManaLoss() {
		return dataWatcher.getWatchableObjectInt(dataWatcherStart + 3);
	}

	@Override
	public void setMinManaLoss(int minManaLoss) {
		dataWatcher.updateObject(dataWatcherStart + 3, minManaLoss);
	}

	@Override
	public float getManaLossPerTick() {
		return dataWatcher.getWatchableObjectFloat(dataWatcherStart + 4);
	}

	@Override
	public void setManaLossPerTick(float mana) {
		dataWatcher.updateObject(dataWatcherStart + 4, mana);
	}


	@Override
	public float getGravity() {
		return dataWatcher.getWatchableObjectFloat(dataWatcherStart + 5);
	}

	@Override
	public void setGravity(float gravity) {
		dataWatcher.updateObject(dataWatcherStart + 5, gravity);
	}


	final int coordsStart = dataWatcherStart + 6;

	@Override
	public ChunkCoordinates getBurstSourceChunkCoordinates() {
		int x = dataWatcher.getWatchableObjectInt(coordsStart);
		int y = dataWatcher.getWatchableObjectInt(coordsStart + 1);
		int z = dataWatcher.getWatchableObjectInt(coordsStart + 2);

		return new ChunkCoordinates(x, y, z);
	}

	@Override
	public void setBurstSourceCoords(int x, int y, int z) {
		dataWatcher.updateObject(coordsStart, x);
		dataWatcher.updateObject(coordsStart + 1, y);
		dataWatcher.updateObject(coordsStart + 2, z);
	}

	@Override
	public ItemStack getSourceLens() {
		return dataWatcher.getWatchableObjectItemStack(dataWatcherStart + 9);
	}

	@Override
	public void setSourceLens(ItemStack lens) {
		dataWatcher.updateObject(dataWatcherStart + 9, lens == null ? new ItemStack(1, 0, 0) : lens);
	}

	public ILens getLensInstance() {
		ItemStack lens = getSourceLens();
		if(lens != null && lens.getItem() instanceof ILens)
			return (ILens) lens.getItem();

		return null;
	}

	final int motionStart = dataWatcherStart + 10;

	@Override
	public void setMotion(double x, double y, double z) {
		motionX = x;
		motionY = y;
		motionZ = z;
	}

	@Override
	public boolean hasAlreadyCollidedAt(int x, int y, int z) {
		return alreadyCollidedAt.contains(getCollisionLocString(x, y, z));
	}

	private String getCollisionLocString(int x, int y, int z) {
		return x + ":" + y + ":" + z;
	}
}
