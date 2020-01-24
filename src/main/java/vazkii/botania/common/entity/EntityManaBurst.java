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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;
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
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.equipment.bauble.ItemTinyPlanet;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

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
	private static final DataParameter<ItemStack> SOURCE_LENS = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.ITEMSTACK);

	float accumulatedManaLoss = 0;
	boolean fake = false;
	final Set<BlockPos> alreadyCollidedAt = new HashSet<>();
	boolean fullManaLastTick = true;
	UUID shooterIdentity = null;
	int _ticksExisted = 0;
	boolean scanBeam = false;
	public final List<PositionProperties> propsList = new ArrayList<>();

	public EntityManaBurst(World world) {
		this(TYPE, world);
	}

	public EntityManaBurst(EntityType<EntityManaBurst> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerData() {
		dataManager.register(COLOR, 0);
		dataManager.register(MANA, 0);
		dataManager.register(START_MANA, 0);
		dataManager.register(MIN_MANA_LOSS, 0);
		dataManager.register(MANA_LOSS_PER_TICK, 0F);
		dataManager.register(GRAVITY, 0F);
		dataManager.register(SOURCE_COORDS, BlockPos.ZERO);
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
		setBurstMotion(mx, my, mz);
	}

	public EntityManaBurst(PlayerEntity player) {
		super(TYPE, player, player.world);

		setBurstSourceCoords(new BlockPos(0, -1, 0));
		setRotation(player.rotationYaw + 180, -player.rotationPitch);

		float f = 0.4F;
		double mx = MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f / 2D;
		double mz = -(MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f) / 2D;
		double my = MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI) * f / 2D;
		setBurstMotion(mx, my, mz);
	}

	private void superUpdate() {
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
			this.setMotion(this.getMotion().mul((double)(this.rand.nextFloat() * 0.2F), (double)(this.rand.nextFloat() * 0.2F), (double)(this.rand.nextFloat() * 0.2F)));
		}

		AxisAlignedBB axisalignedbb = this.getBoundingBox().expand(this.getMotion()).grow(1.0D);

		/* Botania - no ignoreEntity stuff at all
		for(Entity entity : this.world.getEntitiesInAABBexcluding(this, axisalignedbb, (p_213881_0_) -> {
			return !p_213881_0_.isSpectator() && p_213881_0_.canBeCollidedWith();
		})) {
			if (entity == this.ignoreEntity) {
				++this.ignoreTime;
				break;
			}

			if (this.owner != null && this.ticksExisted < 2 && this.ignoreEntity == null) {
				this.ignoreEntity = entity;
				this.ignoreTime = 3;
				break;
			}
		}
		*/

		RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, axisalignedbb, (p_213880_1_) -> {
			return !p_213880_1_.isSpectator() && p_213880_1_.canBeCollidedWith(); // && p_213880_1_ != this.ignoreEntity;
		}, RayTraceContext.BlockMode.OUTLINE, true);
		/*
		if (this.ignoreEntity != null && this.ignoreTime-- <= 0) {
			this.ignoreEntity = null;
		}
		*/

		if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
			if (raytraceresult.getType() == RayTraceResult.Type.BLOCK && this.world.getBlockState(((BlockRayTraceResult)raytraceresult).getPos()).getBlock() == Blocks.NETHER_PORTAL) {
				this.setPortal(((BlockRayTraceResult)raytraceresult).getPos());
			} else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)){
				this.onImpact(raytraceresult);
			}
		}

		Vec3d vec3d = this.getMotion();
		double d0 = this.getX() + vec3d.x;
		double d1 = this.getY() + vec3d.y;
		double d2 = this.getZ() + vec3d.z;
		float f = MathHelper.sqrt(horizontalMag(vec3d));
		this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));

		for(this.rotationPitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * (double)(180F / (float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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

		this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
		this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
		float f1;
		if (this.isInWater()) {
			for(int i = 0; i < 4; ++i) {
				float f2 = 0.25F;
				this.world.addParticle(ParticleTypes.BUBBLE, d0 - vec3d.x * 0.25D, d1 - vec3d.y * 0.25D, d2 - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
			}

			f1 = 0.8F;
		} else {
			f1 = 0.99F;
		}

		// Botania - no drag this.setMotion(vec3d.scale((double)f1));
		if (!this.hasNoGravity()) {
			Vec3d vec3d1 = this.getMotion();
			this.setMotion(vec3d1.x, vec3d1.y - (double)this.getGravityVelocity(), vec3d1.z);
		}

		this.setPosition(d0, d1, d2);
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

		setBurstMotion(getMotion().getX(), getMotion().getY(), getMotion().getZ());

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

		par1nbtTagCompound.putDouble(TAG_LAST_MOTION_X, getMotion().getX());
		par1nbtTagCompound.putDouble(TAG_LAST_MOTION_Y, getMotion().getY());
		par1nbtTagCompound.putDouble(TAG_LAST_MOTION_Z, getMotion().getZ());

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

		setBurstMotion(lastMotionX, lastMotionY, lastMotionZ);

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

		int color = getColor();
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		float osize = getParticleSize();
		float size = osize;

		if(fake) {
			if(getMana() == getStartingMana())
				size = 2F;
			else if(fullManaLastTick)
				size = 4F;

			if(!noParticles && shouldDoFakeParticles()) {
				SparkleParticleData data = SparkleParticleData.fake(0.4F * size, r, g, b, 1);
				Botania.proxy.addParticleForce(world, data, getX(), getY(), getZ(), 0, 0, 0);
			}
		} else {
			boolean depth = !Botania.proxy.isClientPlayerWearingMonocle();

			if(ConfigHandler.CLIENT.subtlePowerSystem.get()) {
				WispParticleData data = WispParticleData.wisp(0.1F * size, r, g, b, depth);
				world.addParticle(data, getX(), getY(), getZ(), (float) (Math.random() - 0.5F) * 0.02F, (float) (Math.random() - 0.5F) * 0.02F, (float) (Math.random() - 0.5F) * 0.01F);
			} else {
				float or = r;
				float og = g;
				float ob = b;

				double luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b; // Standard relative luminance calculation

				double iterX = getX();
				double iterY = getY();
				double iterZ = getZ();

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
					WispParticleData data = WispParticleData.wisp(0.2F * size, r, g, b, depth);
					world.addParticle(data, iterX, iterY, iterZ,
							(float) -getMotion().getX() * 0.01F,
							(float) -getMotion().getY() * 0.01F,
							(float) -getMotion().getZ() * 0.01F);

					iterX += diffVecNorm.x * distance;
					iterY += diffVecNorm.y * distance;
					iterZ += diffVecNorm.z * distance;

					currentPos = new Vector3(iterX, iterY, iterZ);
					diffVec = oldPos.subtract(currentPos);
					if(getPersistentData().contains(ItemTinyPlanet.TAG_ORBIT))
						break;
				} while(Math.abs(diffVec.mag()) > distance);

				WispParticleData data = WispParticleData.wisp(0.1F * size, or, og, ob, depth);
				world.addParticle(data, iterX, iterY, iterZ, (float) (Math.random() - 0.5F) * 0.06F, (float) (Math.random() - 0.5F) * 0.06F, (float) (Math.random() - 0.5F) * 0.06F);
			}
		}
	}
	
	public float getParticleSize() {
		return (float) getMana() / (float) getStartingMana();
	}

	@Override
	protected void onImpact(@Nonnull RayTraceResult rtr) {
		BlockPos pos = null;
		boolean dead = false;

		if(rtr.getType() == RayTraceResult.Type.BLOCK) {
			pos = ((BlockRayTraceResult) rtr).getPos();
			TileEntity tile = world.getTileEntity(pos);
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			if(block instanceof IManaCollisionGhost
					&& ((IManaCollisionGhost) block).isGhost(state, world, pos)
					&& !(block instanceof IManaTrigger)
					|| block instanceof BushBlock
					|| block instanceof LeavesBlock)
				return;

			BlockPos coords = getBurstSourceBlockPos();
			if(tile != null && !tile.getPos().equals(coords))
				collidedTile = tile;

			if(tile == null || !tile.getPos().equals(coords)) {
				if(!fake && !noParticles && (!world.isRemote || tile instanceof IClientManaHandler) && tile != null && tile instanceof IManaReceiver && ((IManaReceiver) tile).canRecieveManaFromBursts())
					onRecieverImpact((IManaReceiver) tile, tile.getPos());

				if(block instanceof IManaTrigger)
					((IManaTrigger) block).onBurstCollision(this, world, pos);

				boolean ghost = block instanceof IManaCollisionGhost;
				dead = !ghost;
				if(ghost)
					return;
			}
		}

		ILensEffect lens = getLensInstance();
		if(lens != null)
			dead = lens.collideBurst(this, rtr, collidedTile instanceof IManaReceiver
							&& ((IManaReceiver) collidedTile).canRecieveManaFromBursts(), dead, getSourceLens());

		if(pos != null && !hasAlreadyCollidedAt(pos))
			alreadyCollidedAt.add(pos);

		if(dead && isAlive()) {
			if(!fake && world.isRemote) {
				int color = getColor();
				float r = (color >> 16 & 0xFF) / 255F;
				float g = (color >> 8 & 0xFF) / 255F;
				float b = (color & 0xFF) / 255F;

				int mana = getMana();
				int maxMana = getStartingMana();
				float size = (float) mana / (float) maxMana;

				if(!ConfigHandler.CLIENT.subtlePowerSystem.get())
					for(int i = 0; i < 4; i++) {
						WispParticleData data = WispParticleData.wisp(0.15F * size, r, g, b);
						world.addParticle(data, getX(), getY(), getZ(), (float) (Math.random() - 0.5F) * 0.04F, (float) (Math.random() - 0.5F) * 0.04F, (float) (Math.random() - 0.5F) * 0.04F);
					}
				SparkleParticleData data = SparkleParticleData.sparkle((float) 4, r, g, b, 2);
				world.addParticle(data, getX(), getY(), getZ(), 0, 0, 0);
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
	public void setBurstMotion(double x, double y, double z) {
		this.setMotion(x, y, z);
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

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
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
			int x = MathHelper.floor(entity.getX());
			int y = MathHelper.floor(entity.getY());
			int z = MathHelper.floor(entity.getZ());
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
