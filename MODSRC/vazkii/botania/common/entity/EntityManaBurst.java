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
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileSpreader;
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

	double lastXMotion, lastYMotion, lastZMotion;

	boolean fake = false;

	final int dataWatcherEntries = 10;
	final int dataWatcherStart = 32 - dataWatcherEntries;

	List<String> alreadyCollidedAt = new ArrayList();

	public EntityManaBurst(World world) {
		super(world);
		setSize(0F, 0F);
		for(int i = 0; i < dataWatcherEntries; i++) {
			int j = dataWatcherStart + i;
			if(i == 4 || i == 5)
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
		MovingObjectPosition movingobjectposition = worldObj.clip(vec3, vec31);

		if(movingobjectposition != null)
			vec31 = new Vector3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord).toVec3D();

		if(!worldObj.isRemote) {
			Entity entity = null;
			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;
			EntityLivingBase entitylivingbase = getThrower();

			for(int j = 0; j < list.size(); ++j) {
				Entity entity1 = (Entity)list.get(j);

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

		if(movingobjectposition != null) {
			if(movingobjectposition.typeOfHit == EnumMovingObjectType.TILE && worldObj.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Block.portal.blockID)
				setInPortal();
			else onImpact(movingobjectposition);
		}

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

	@Override
	public void onUpdate() {
		motionX = lastXMotion;
		motionY = lastYMotion;
		motionZ = lastZMotion;

		superUpdate();

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

		lastXMotion = motionX;
		lastYMotion = motionY;
		lastZMotion = motionZ;
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
	}

	public void particles() {
		if(worldObj.isRemote || isDead)
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
		} else Botania.proxy.wispFX(worldObj, posX, posY, posZ, r, g, b, 0.2F * size, (float) -motionX * 0.01F, (float) -motionY * 0.01F, (float) -motionZ * 0.01F);
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
					((IManaReceiver) tile).recieveMana(getMana());
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

		if(dead)
			setDead();
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

	@Override
	public void setMotion(double x, double y, double z) {
		motionX = x;
		motionY = y;
		motionZ = z;
		lastXMotion = motionX;
		lastYMotion = motionY;
		lastZMotion = motionZ;
	}

	@Override
	public boolean hasAlreadyCollidedAt(int x, int y, int z) {
		return alreadyCollidedAt.contains(getCollisionLocString(x, y, z));
	}

	private String getCollisionLocString(int x, int y, int z) {
		return x + ":" + y + ":" + z;
	}
}
