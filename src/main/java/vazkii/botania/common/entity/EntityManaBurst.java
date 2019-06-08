/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 26, 2014, 5:09:12 PM (GMT)]
 */
package vazkii.botania.common.entity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BushBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.BushBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.block.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IClientManaHandler;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.IManaSpreader;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.mana.IPingable;
import vazkii.botania.api.mana.IThrottledPacket;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.equipment.bauble.ItemTinyPlanet;
import vazkii.botania.common.lib.LibMisc;

public class EntityManaBurst extends ThrowableEntity implements IManaBurst {
	@ObjectHolder(LibMisc.MOD_ID + ":mana_burst")
	public static EntityType<EntityManaBurst> TYPE;
	private static final String TAG_TICKS_EXISTED = "ticksExisted";
	private static final String TAG_COLOR = "color";
	private static final String TAG_MANA = "mana";
	private static final String TAG_STARTING_MANA = "startingMana";
	private static final String TAG_MIN_MANA_LOSS = "minManaLoss";
	private static final String TAG_TICK_MANA_LOSS = "manaLossTick";
	private static final String TAG_SPREADER_X = "spreaderX";
	private static final String TAG_SPREADER_Y = "spreaderY";
	private static final String TAG_SPREADER_Z = "spreaderZ";
	private static final String TAG_GRAVITY = "gravity";
	private static final String TAG_LENS_STACK = "lensStack";
	private static final String TAG_LAST_MOTION_X = "lastMotionX";
	private static final String TAG_LAST_MOTION_Y = "lastMotionY";
	private static final String TAG_LAST_MOTION_Z = "lastMotionZ";
	private static final String TAG_HAS_SHOOTER = "hasShooter";
	private static final String TAG_SHOOTER_UUID_MOST = "shooterUUIDMost";
	private static final String TAG_SHOOTER_UUID_LEAST = "shooterUUIDLeast";

	private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> MANA = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> START_MANA = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> MIN_MANA_LOSS = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.VARINT);
	private static final DataParameter<Float> MANA_LOSS_PER_TICK = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> GRAVITY = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.FLOAT);
	private static final DataParameter<BlockPos> SOURCE_COORDS = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<ItemStack> SOURCE_LENS = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.ITEM_STACK);

	float accumulatedManaLoss = 0;
	boolean fake = false;
	final Set<BlockPos> alreadyCollidedAt = new HashSet<>();
	boolean fullManaLastTick = true;
	UUID shooterIdentity = null;
	int _ticksExisted = 0;
	boolean scanBeam = false;
	public final List<PositionProperties> propsList = new ArrayList<>();

	public EntityManaBurst(World world) {
		super(TYPE, world);
		setSize(0F, 0F);
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(COLOR, 0);
		dataManager.register(MANA, 0);
		dataManager.register(START_MANA, 0);
		dataManager.register(MIN_MANA_LOSS, 0);
		dataManager.register(MANA_LOSS_PER_TICK, 0F);
		dataManager.register(GRAVITY, 0F);
		dataManager.register(SOURCE_COORDS, BlockPos.ORIGIN);
		dataManager.register(SOURCE_LENS, ItemStack.EMPTY);
	}

	public EntityManaBurst(IManaSpreader spreader, boolean fake) {
		this(((TileEntity)spreader).getWorld());

		TileEntity tile = (TileEntity) spreader;

		this.fake = fake;

		setBurstSourceCoords(tile.getPos());
		setLocationAndAngles(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.5, tile.getPos().getZ() + 0.5, 0, 0);
		rotationYaw = -(spreader.getRotationX() + 90F);
		rotationPitch = spreader.getRotationY();

		float f = 0.4F;
		double mx = MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f / 2D;
		double mz = -(MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f) / 2D;
		double my = MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI) * f / 2D;
		setMotion(mx, my, mz);
	}

	public EntityManaBurst(PlayerEntity player, Hand hand) {
		this(player.world);

		setBurstSourceCoords(new BlockPos(0, -1, 0));
		setLocationAndAngles(player.posX, player.posY + player.getEyeHeight(), player.posZ, player.rotationYaw + 180, -player.rotationPitch);

		posX -= (hand == Hand.OFF_HAND ? -1 : 1) * MathHelper.cos((rotationYaw + 180) / 180.0F * (float) Math.PI) * 0.16F;
		posY -= 0.10000000149011612D;
		posZ -= (hand == Hand.OFF_HAND ? -1 : 1) * MathHelper.sin((rotationYaw + 180) / 180.0F * (float) Math.PI) * 0.16F;

		setPosition(posX, posY, posZ);
		float f = 0.4F;
		double mx = MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f / 2D;
		double mz = -(MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f) / 2D;
		double my = MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI) * f / 2D;
		setMotion(mx, my, mz);
	}

	private void superUpdate() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		// Botania - inline supersuperclass.tick()
		{
			if (!this.world.isRemote) {
				this.setFlag(6, this.isGlowing());
			}

			this.baseTick();
		}
		if (this.throwableShake > 0) {
			--this.throwableShake;
		}

		if (this.inGround) {
			this.inGround = false;
			this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
		}

		Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1);
		vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if (raytraceresult != null) {
			vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		if (!scanBeam && !world.isRemote) { // Botania - collide only on server and non-scanbeam
			Entity entity = null;
			List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
			double d0 = 0.0D;
			boolean flag = false;

			for (int i = 0; i < list.size(); ++i) {
				Entity entity1 = list.get(i);
				if (entity1.canBeCollidedWith()) {
					if (entity1 == this.ignoreEntity) {
						flag = true;
					} else if (this.thrower != null && this.ticksExisted < 2 && this.ignoreEntity == null) {
						this.ignoreEntity = entity1;
						flag = true;
					} else {
						flag = false;
						AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow((double) 0.3F);
						RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);
						if (raytraceresult1 != null) {
							double d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec);
							if (d1 < d0 || d0 == 0.0D) {
								entity = entity1;
								d0 = d1;
							}
						}
					}
				}
			}

			if (this.ignoreEntity != null) {
			/*
			if (flag) {
				this.ignoreTime = 2;
			} else if (this.ignoreTime-- <= 0) {
				this.ignoreEntity = null;
			}
			*/
			}

			if (entity != null) {
				raytraceresult = new RayTraceResult(entity);
			}
		}

		if (raytraceresult != null) {
			if (raytraceresult.type == RayTraceResult.Type.BLOCK && this.world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.NETHER_PORTAL) {
				this.setPortal(raytraceresult.getBlockPos());
			} else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)){
				this.onImpact(raytraceresult);
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (double)(180F / (float)Math.PI));

		for(this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (double)(180F / (float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float f1 = 0.99F;
		float f2 = this.getGravityVelocity();
		if (this.isInWater()) {
			for(int j = 0; j < 4; ++j) {
				float f3 = 0.25F;
				this.world.addParticle(Particles.BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
			}

			f1 = 0.8F;
		}

		/* Botania - no drag
		this.motionX *= (double)f1;
		this.motionY *= (double)f1;
		this.motionZ *= (double)f1;
		*/
		if (!this.hasNoGravity()) {
			this.motionY -= (double)f2;
		}

		this.setPosition(this.posX, this.posY, this.posZ);
	}

	@Override
	public void tick() {
		setTicksExisted(getTicksExisted() + 1);
		superUpdate();

		if(!fake && isAlive() && !scanBeam)
			ping();

		ILensEffect lens = getLensInstance();
		if(lens != null)
			lens.updateBurst(this, getSourceLens());

		int mana = getMana();
		if(getTicksExisted() >= getMinManaLoss()) {
			accumulatedManaLoss += getManaLossPerTick();
			int loss = (int) accumulatedManaLoss;
			setMana(mana - loss);
			accumulatedManaLoss -= loss;

			if(getMana() <= 0)
				remove();
		}

		particles();

		setMotion(motionX, motionY, motionZ);

		fullManaLastTick = getMana() == getStartingMana();

		if(scanBeam) {
			PositionProperties props = new PositionProperties(this);
			if(propsList.isEmpty())
				propsList.add(props);
			else {
				PositionProperties lastProps = propsList.get(propsList.size() - 1);
				if(!props.coordsEqual(lastProps))
					propsList.add(props);
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
		setPosition(x, y, z);
		setRotation(yaw, pitch);
	}

	@Override
	public boolean handleWaterMovement() {
		return false;
	}

	@Override
	public boolean isInLava() {
		//Avoids expensive getBlockState check in Entity#onEntityUpdate (see super impl)
		return false;
	}

	private TileEntity collidedTile = null;
	private boolean noParticles = false;

	public TileEntity getCollidedTile(boolean noParticles) {
		this.noParticles = noParticles;

		int iterations = 0;
		while(isAlive() && iterations < ConfigHandler.COMMON.spreaderTraceTime.get()) {
			tick();
			iterations++;
		}

		if(fake)
			incrementFakeParticleTick();

		return collidedTile;
	}

	@Override
	public void writeAdditional(CompoundNBT par1nbtTagCompound) {
		super.writeAdditional(par1nbtTagCompound);
		par1nbtTagCompound.putInt(TAG_TICKS_EXISTED, getTicksExisted());
		par1nbtTagCompound.putInt(TAG_COLOR, getColor());
		par1nbtTagCompound.putInt(TAG_MANA, getMana());
		par1nbtTagCompound.putInt(TAG_STARTING_MANA, getStartingMana());
		par1nbtTagCompound.putInt(TAG_MIN_MANA_LOSS, getMinManaLoss());
		par1nbtTagCompound.putFloat(TAG_TICK_MANA_LOSS, getManaLossPerTick());
		par1nbtTagCompound.putFloat(TAG_GRAVITY, getGravity());

		ItemStack stack = getSourceLens();
		CompoundNBT lensCmp = new CompoundNBT();
		if(!stack.isEmpty())
			lensCmp = stack.write(lensCmp);
		par1nbtTagCompound.put(TAG_LENS_STACK, lensCmp);

		BlockPos coords = getBurstSourceBlockPos();
		par1nbtTagCompound.putInt(TAG_SPREADER_X, coords.getX());
		par1nbtTagCompound.putInt(TAG_SPREADER_Y, coords.getY());
		par1nbtTagCompound.putInt(TAG_SPREADER_Z, coords.getZ());

		par1nbtTagCompound.putDouble(TAG_LAST_MOTION_X, motionX);
		par1nbtTagCompound.putDouble(TAG_LAST_MOTION_Y, motionY);
		par1nbtTagCompound.putDouble(TAG_LAST_MOTION_Z, motionZ);

		UUID identity = getShooterUUID();
		boolean hasShooter = identity != null;
		par1nbtTagCompound.putBoolean(TAG_HAS_SHOOTER, hasShooter);
		if(hasShooter) {
			par1nbtTagCompound.putLong(TAG_SHOOTER_UUID_MOST, identity.getMostSignificantBits());
			par1nbtTagCompound.putLong(TAG_SHOOTER_UUID_LEAST, identity.getLeastSignificantBits());
		}
	}

	@Override
	public void readAdditional(CompoundNBT cmp) {
		super.readAdditional(cmp);
		setTicksExisted(cmp.getInt(TAG_TICKS_EXISTED));
		setColor(cmp.getInt(TAG_COLOR));
		setMana(cmp.getInt(TAG_MANA));
		setStartingMana(cmp.getInt(TAG_STARTING_MANA));
		setMinManaLoss(cmp.getInt(TAG_MIN_MANA_LOSS));
		setManaLossPerTick(cmp.getFloat(TAG_TICK_MANA_LOSS));
		setGravity(cmp.getFloat(TAG_GRAVITY));

		CompoundNBT lensCmp = cmp.getCompound(TAG_LENS_STACK);
		ItemStack stack = ItemStack.read(lensCmp);
		if(!stack.isEmpty())
			setSourceLens(stack);
		else setSourceLens(ItemStack.EMPTY);

		int x = cmp.getInt(TAG_SPREADER_X);
		int y = cmp.getInt(TAG_SPREADER_Y);
		int z = cmp.getInt(TAG_SPREADER_Z);

		setBurstSourceCoords(new BlockPos(x, y, z));

		double lastMotionX = cmp.getDouble(TAG_LAST_MOTION_X);
		double lastMotionY = cmp.getDouble(TAG_LAST_MOTION_Y);
		double lastMotionZ = cmp.getDouble(TAG_LAST_MOTION_Z);

		setMotion(lastMotionX, lastMotionY, lastMotionZ);

		boolean hasShooter = cmp.getBoolean(TAG_HAS_SHOOTER);
		if(hasShooter) {
			long most = cmp.getLong(TAG_SHOOTER_UUID_MOST);
			long least = cmp.getLong(TAG_SHOOTER_UUID_LEAST);
			UUID identity = getShooterUUID();
			if(identity == null || most != identity.getMostSignificantBits() || least != identity.getLeastSignificantBits())
				shooterIdentity = new UUID(most, least);
		}
	}

	public void particles() {
		if(!isAlive() || !world.isRemote)
			return;

		ILensEffect lens = getLensInstance();
		if(lens != null && !lens.doParticles(this, getSourceLens()))
			return;

		Color color = new Color(getColor());
		float r = color.getRed() / 255F;
		float g = color.getGreen() / 255F;
		float b = color.getBlue() / 255F;
		float osize = getParticleSize();
		float size = osize;

		if(fake) {
			if(getMana() == getStartingMana())
				size = 2F;
			else if(fullManaLastTick)
				size = 4F;

			if(!noParticles && shouldDoFakeParticles())
				Botania.proxy.sparkleFX(posX, posY, posZ, r, g, b, 0.4F * size, 1, true);
		} else {
			boolean monocle = Botania.proxy.isClientPlayerWearingMonocle();
			if(monocle)
				Botania.proxy.setWispFXDepthTest(false);

			if(ConfigHandler.CLIENT.subtlePowerSystem.get())
				Botania.proxy.wispFX(posX, posY, posZ, r, g, b, 0.1F * size, (float) (Math.random() - 0.5F) * 0.02F, (float) (Math.random() - 0.5F) * 0.02F, (float) (Math.random() - 0.5F) * 0.01F);
			else {
				float or = r;
				float og = g;
				float ob = b;

				double luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b; // Standard relative luminance calculation

				double savedPosX = posX;
				double savedPosY = posY;
				double savedPosZ = posZ;

				Vector3 currentPos = Vector3.fromEntity(this);
				Vector3 oldPos = new Vector3(prevPosX, prevPosY, prevPosZ);
				Vector3 diffVec = oldPos.subtract(currentPos);
				Vector3 diffVecNorm = diffVec.normalize();

				double distance = 0.095;

				do {
					if (luminance < 0.1) {
						r = or + (float) Math.random() * 0.125F;
						g = og + (float) Math.random() * 0.125F;
						b = ob + (float) Math.random() * 0.125F;
					}
					size = osize + ((float) Math.random() - 0.5F) * 0.065F + (float) Math.sin(new Random(entityUniqueID.getMostSignificantBits()).nextInt(9001)) * 0.4F;
					Botania.proxy.wispFX(posX, posY, posZ, r, g, b, 0.2F * size, (float) -motionX * 0.01F, (float) -motionY * 0.01F, (float) -motionZ * 0.01F);

					posX += diffVecNorm.x * distance;
					posY += diffVecNorm.y * distance;
					posZ += diffVecNorm.z * distance;

					currentPos = Vector3.fromEntity(this);
					diffVec = oldPos.subtract(currentPos);
					if(getEntityData().contains(ItemTinyPlanet.TAG_ORBIT))
						break;
				} while(Math.abs(diffVec.mag()) > distance);

				Botania.proxy.wispFX(posX, posY, posZ, or, og, ob, 0.1F * size, (float) (Math.random() - 0.5F) * 0.06F, (float) (Math.random() - 0.5F) * 0.06F, (float) (Math.random() - 0.5F) * 0.06F);

				posX = savedPosX;
				posY = savedPosY;
				posZ = savedPosZ;
			}

			if(monocle)
				Botania.proxy.setWispFXDepthTest(true);
		}
	}
	
	public float getParticleSize() {
		return (float) getMana() / (float) getStartingMana();
	}

	@Override
	protected void onImpact(@Nonnull RayTraceResult rtr) {
		boolean collided = false;
		boolean dead = false;

		if(rtr.entity == null) {
			TileEntity tile = world.getTileEntity(rtr.getBlockPos());
			BlockState state = world.getBlockState(rtr.getBlockPos());
			Block block = state.getBlock();

			if(block instanceof IManaCollisionGhost && ((IManaCollisionGhost) block).isGhost(state, world, rtr.getBlockPos()) && !(block instanceof IManaTrigger) || block instanceof BushBlock || block instanceof LeavesBlock)
				return;

			if(BotaniaAPI.internalHandler.isBuildcraftPipe(tile))
				return;

			BlockPos coords = getBurstSourceBlockPos();
			if(tile != null && !tile.getPos().equals(coords))
				collidedTile = tile;

			if(tile == null || !tile.getPos().equals(coords)) {
				if(!fake && !noParticles && (!world.isRemote || tile instanceof IClientManaHandler) && tile != null && tile instanceof IManaReceiver && ((IManaReceiver) tile).canRecieveManaFromBursts())
					onRecieverImpact((IManaReceiver) tile, tile.getPos());

				if(block instanceof IManaTrigger)
					((IManaTrigger) block).onBurstCollision(this, world, rtr.getBlockPos());

				boolean ghost = block instanceof IManaCollisionGhost;
				dead = !ghost;
				if(ghost)
					return;
			}

			collided = true;
		}

		ILensEffect lens = getLensInstance();
		if(lens != null)
			dead = lens.collideBurst(this, rtr, collidedTile != null && collidedTile instanceof IManaReceiver && ((IManaReceiver) collidedTile).canRecieveManaFromBursts(), dead, getSourceLens());

		if(collided && !hasAlreadyCollidedAt(rtr.getBlockPos()))
			alreadyCollidedAt.add(rtr.getBlockPos());

		if(dead && isAlive()) {
			if(!fake && world.isRemote) {
				Color color = new Color(getColor());
				float r = color.getRed() / 255F;
				float g = color.getGreen() / 255F;
				float b = color.getBlue() / 255F;

				int mana = getMana();
				int maxMana = getStartingMana();
				float size = (float) mana / (float) maxMana;

				if(!ConfigHandler.CLIENT.subtlePowerSystem.get())
					for(int i = 0; i < 4; i++)
						Botania.proxy.wispFX(posX, posY, posZ, r, g, b, 0.15F * size, (float) (Math.random() - 0.5F) * 0.04F, (float) (Math.random() - 0.5F) * 0.04F, (float) (Math.random() - 0.5F) * 0.04F);
				Botania.proxy.sparkleFX((float) posX, (float) posY, (float) posZ, r, g, b, 4, 2);
			}

			remove();
		}
	}

	private void onRecieverImpact(IManaReceiver tile, BlockPos pos) {
		ILensEffect lens = getLensInstance();
		int mana = getMana();
		
		if(lens != null) {
			ItemStack stack = getSourceLens();
			mana = lens.getManaToTransfer(this, this, stack, tile);
		}
		
		if(tile instanceof IManaCollector)
			mana *= ((IManaCollector) tile).getManaYieldMultiplier(this);

		tile.recieveMana(mana);
		
		if(tile instanceof IThrottledPacket)
			((IThrottledPacket) tile).markDispatchable();
		else VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
	}

	@Override
	public void remove() {
		super.remove();

		if(!fake) {
			TileEntity tile = getShooter();
			if(tile instanceof IManaSpreader)
				((IManaSpreader) tile).setCanShoot(true);
		} else setDeathTicksForFakeParticle();
	}

	private TileEntity getShooter() {
		return world.getTileEntity(getBurstSourceBlockPos());
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
	public void setFake(boolean fake) {
		this.fake = fake;
	}

	public void setScanBeam() {
		scanBeam = true;
	}

	@Override
	public int getColor() {
		return dataManager.get(COLOR);
	}

	@Override
	public void setColor(int color) {
		dataManager.set(COLOR, color);
	}

	@Override
	public int getMana() {
		return dataManager.get(MANA);
	}

	@Override
	public void setMana(int mana) {
		dataManager.set(MANA, mana);
	}

	@Override
	public int getStartingMana() {
		return dataManager.get(START_MANA);
	}

	@Override
	public void setStartingMana(int mana) {
		dataManager.set(START_MANA, mana);
	}

	@Override
	public int getMinManaLoss() {
		return dataManager.get(MIN_MANA_LOSS);
	}

	@Override
	public void setMinManaLoss(int minManaLoss) {
		dataManager.set(MIN_MANA_LOSS, minManaLoss);
	}

	@Override
	public float getManaLossPerTick() {
		return dataManager.get(MANA_LOSS_PER_TICK);
	}

	@Override
	public void setManaLossPerTick(float mana) {
		dataManager.set(MANA_LOSS_PER_TICK, mana);
	}

	@Override
	public float getGravity() {
		return dataManager.get(GRAVITY);
	}

	@Override
	public void setGravity(float gravity) {
		dataManager.set(GRAVITY, gravity);
	}

	@Override
	public BlockPos getBurstSourceBlockPos() {
		return dataManager.get(SOURCE_COORDS);
	}

	@Override
	public void setBurstSourceCoords(BlockPos pos) {
		dataManager.set(SOURCE_COORDS, pos);
	}

	@Override
	public ItemStack getSourceLens() {
		return dataManager.get(SOURCE_LENS);
	}

	@Override
	public void setSourceLens(ItemStack lens) {
		dataManager.set(SOURCE_LENS, lens);
	}

	@Override
	public int getTicksExisted() {
		return _ticksExisted;
	}

	public void setTicksExisted(int ticks) {
		_ticksExisted = ticks;
	}

	private ILensEffect getLensInstance() {
		ItemStack lens = getSourceLens();
		if(!lens.isEmpty() && lens.getItem() instanceof ILensEffect)
			return (ILensEffect) lens.getItem();

		return null;
	}

	@Override
	public void setMotion(double x, double y, double z) {
		motionX = x;
		motionY = y;
		motionZ = z;
	}

	@Override
	public boolean hasAlreadyCollidedAt(BlockPos pos) {
		return alreadyCollidedAt.contains(pos);
	}

	@Override
	public void setCollidedAt(BlockPos pos) {
		if(!hasAlreadyCollidedAt(pos))
			alreadyCollidedAt.add(pos.toImmutable());
	}

	@Override
	public void setShooterUUID(UUID uuid) {
		shooterIdentity = uuid;
	}

	@Override
	public UUID getShooterUUID() {
		return shooterIdentity;
	}

	@Override
	public void ping() {
		TileEntity tile = getShooter();
		if(tile != null && tile instanceof IPingable)
			((IPingable) tile).pingback(this, getShooterUUID());
	}

	protected boolean shouldDoFakeParticles() {
		if (ConfigHandler.CLIENT.staticWandBeam.get())
			return true;

		TileEntity tile = getShooter();
		return tile instanceof IManaSpreader
				&& (getMana() != getStartingMana() && fullManaLastTick
				|| Math.abs(((IManaSpreader) tile).getBurstParticleTick() - getTicksExisted()) < 4);
	}

	private void incrementFakeParticleTick() {
		TileEntity tile = getShooter();
		if(tile instanceof IManaSpreader) {
			IManaSpreader spreader = (IManaSpreader) tile;
			spreader.setBurstParticleTick(spreader.getBurstParticleTick()+2);
			if(spreader.getLastBurstDeathTick() != -1 && spreader.getBurstParticleTick() > spreader.getLastBurstDeathTick())
				spreader.setBurstParticleTick(0);
		}
	}

	private void setDeathTicksForFakeParticle() {
		BlockPos coords = getBurstSourceBlockPos();
		TileEntity tile = world.getTileEntity(coords);
		if(tile != null && tile instanceof IManaSpreader)
			((IManaSpreader) tile).setLastBurstDeathTick(getTicksExisted());
	}

	public static class PositionProperties {

		public final BlockPos coords;
		public final BlockState state;

		public boolean invalid = false;

		public PositionProperties(Entity entity) {
			int x = MathHelper.floor(entity.posX);
			int y = MathHelper.floor(entity.posY);
			int z = MathHelper.floor(entity.posZ);
			coords = new BlockPos(x, y, z);
			state = entity.world.getBlockState(coords);
		}

		public boolean coordsEqual(PositionProperties props) {
			return coords.equals(props.coords);
		}

		public boolean contentsEqual(World world) {
			if(!world.isBlockLoaded(coords)) {
				invalid = true;
				return false;
			}

			return world.getBlockState(coords) == state;
		}

		@Override
		public int hashCode() {
			return Objects.hash(coords, state);
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof PositionProperties
					&& ((PositionProperties) o).state == state
					&& ((PositionProperties) o).coords.equals(coords);
		}
	}

}
