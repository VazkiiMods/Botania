/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.*;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;

public class EntityManaBurst extends ThrowableEntity implements IManaBurst {
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
	private static final String TAG_SHOOTER = "shooterUUID";
	private static final String TAG_LAST_COLLISION_X = "lastCollisionX";
	private static final String TAG_LAST_COLLISION_Y = "lastCollisionY";
	private static final String TAG_LAST_COLLISION_Z = "lastCollisionZ";
	private static final String TAG_WARPED = "warped";
	private static final String TAG_ORBIT_TIME = "orbitTime";
	private static final String TAG_TRIPPED = "tripped";
	private static final String TAG_MAGNETIZE_POS = "magnetizePos";

	private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> MANA = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> START_MANA = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> MIN_MANA_LOSS = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.VARINT);
	private static final DataParameter<Float> MANA_LOSS_PER_TICK = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> GRAVITY = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.FLOAT);
	private static final DataParameter<BlockPos> SOURCE_COORDS = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<ItemStack> SOURCE_LENS = EntityDataManager.createKey(EntityManaBurst.class, DataSerializers.ITEMSTACK);

	private float accumulatedManaLoss = 0;
	private boolean fake = false;
	private final Set<BlockPos> alreadyCollidedAt = new HashSet<>();
	private boolean fullManaLastTick = true;
	private UUID shooterIdentity = null;
	private int _ticksExisted = 0;
	private boolean scanBeam = false;
	private BlockPos lastCollision;
	private boolean warped = false;
	private int orbitTime = 0;
	private boolean tripped = false;
	private BlockPos magnetizePos = null;

	public final List<PositionProperties> propsList = new ArrayList<>();

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
		this(ModEntities.MANA_BURST, spreader.tileEntity().getWorld());

		TileEntity tile = spreader.tileEntity();

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
		super(ModEntities.MANA_BURST, player, player.world);

		setBurstSourceCoords(new BlockPos(0, -1, 0));
		setRotation(player.rotationYaw + 180, -player.rotationPitch);

		float f = 0.4F;
		double mx = MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f / 2D;
		double mz = -(MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f) / 2D;
		double my = MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI) * f / 2D;
		setBurstMotion(mx, my, mz);
	}

	private void superUpdate() {
		// Botania: inline Entity.tick()
		{
			if (!this.world.isRemote) {
				this.setFlag(6, this.isGlowing());
			}

			this.baseTick();
		}

		RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_);
		boolean flag = false;
		if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockRayTraceResult) raytraceresult).getPos();
			BlockState blockstate = this.world.getBlockState(blockpos);
			if (blockstate.isIn(Blocks.NETHER_PORTAL)) {
				this.setPortal(blockpos);
				flag = true;
			} else if (blockstate.isIn(Blocks.END_GATEWAY)) {
				TileEntity tileentity = this.world.getTileEntity(blockpos);
				if (tileentity instanceof EndGatewayTileEntity) {
					((EndGatewayTileEntity) tileentity).teleportEntity(this);
				}

				flag = true;
			}
		}

		if (raytraceresult.getType() != RayTraceResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
			this.onImpact(raytraceresult);
		}

		Vector3d vector3d = this.getMotion();
		double d2 = this.getPosX() + vector3d.x;
		double d0 = this.getPosY() + vector3d.y;
		double d1 = this.getPosZ() + vector3d.z;
		this.func_234617_x_();
		float f;
		if (this.isInWater()) {
			for (int i = 0; i < 4; ++i) {
				float f1 = 0.25F;
				this.world.addParticle(ParticleTypes.BUBBLE, d2 - vector3d.x * 0.25D, d0 - vector3d.y * 0.25D, d1 - vector3d.z * 0.25D, vector3d.x, vector3d.y, vector3d.z);
			}

			f = 0.8F;
		} else {
			f = 0.99F;
		}

		// Botania: no drag this.setMotion(vector3d.scale((double)f));
		if (!this.hasNoGravity()) {
			Vector3d vector3d1 = this.getMotion();
			this.setMotion(vector3d1.x, vector3d1.y - (double) this.getGravityVelocity(), vector3d1.z);
		}

		this.setPosition(d2, d0, d1);
	}

	@Override
	public void tick() {
		setTicksExisted(getTicksExisted() + 1);
		superUpdate();

		if (!fake && isAlive() && !scanBeam) {
			ping();
		}

		ILensEffect lens = getLensInstance();
		if (lens != null) {
			lens.updateBurst(this, getSourceLens());
		}

		int mana = getMana();
		if (getTicksExisted() >= getMinManaLoss()) {
			accumulatedManaLoss += getManaLossPerTick();
			int loss = (int) accumulatedManaLoss;
			setMana(mana - loss);
			accumulatedManaLoss -= loss;

			if (getMana() <= 0) {
				remove();
			}
		}

		particles();

		setBurstMotion(getMotion().getX(), getMotion().getY(), getMotion().getZ());

		fullManaLastTick = getMana() == getStartingMana();

		if (scanBeam) {
			PositionProperties props = new PositionProperties(this);
			if (propsList.isEmpty()) {
				propsList.add(props);
			} else {
				PositionProperties lastProps = propsList.get(propsList.size() - 1);
				if (!props.coordsEqual(lastProps)) {
					propsList.add(props);
				}
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
	public boolean handleFluidAcceleration(ITag<Fluid> fluid, double mag) {
		return false;
	}

	@Override
	public boolean isInLava() {
		return false;
	}

	private TileEntity collidedTile = null;
	private boolean noParticles = false;

	public TileEntity getCollidedTile(boolean noParticles) {
		this.noParticles = noParticles;

		int iterations = 0;
		while (isAlive() && iterations < ConfigHandler.COMMON.spreaderTraceTime.get()) {
			tick();
			iterations++;
		}

		if (fake) {
			incrementFakeParticleTick();
		}

		return collidedTile;
	}

	@Override
	public void writeAdditional(CompoundNBT tag) {
		super.writeAdditional(tag);
		tag.putInt(TAG_TICKS_EXISTED, getTicksExisted());
		tag.putInt(TAG_COLOR, getColor());
		tag.putInt(TAG_MANA, getMana());
		tag.putInt(TAG_STARTING_MANA, getStartingMana());
		tag.putInt(TAG_MIN_MANA_LOSS, getMinManaLoss());
		tag.putFloat(TAG_TICK_MANA_LOSS, getManaLossPerTick());
		tag.putFloat(TAG_GRAVITY, getGravity());

		ItemStack stack = getSourceLens();
		CompoundNBT lensCmp = new CompoundNBT();
		if (!stack.isEmpty()) {
			lensCmp = stack.write(lensCmp);
		}
		tag.put(TAG_LENS_STACK, lensCmp);

		BlockPos coords = getBurstSourceBlockPos();
		tag.putInt(TAG_SPREADER_X, coords.getX());
		tag.putInt(TAG_SPREADER_Y, coords.getY());
		tag.putInt(TAG_SPREADER_Z, coords.getZ());

		tag.putDouble(TAG_LAST_MOTION_X, getMotion().getX());
		tag.putDouble(TAG_LAST_MOTION_Y, getMotion().getY());
		tag.putDouble(TAG_LAST_MOTION_Z, getMotion().getZ());

		if (lastCollision != null) {
			tag.putInt(TAG_LAST_COLLISION_X, coords.getX());
			tag.putInt(TAG_LAST_COLLISION_Y, coords.getY());
			tag.putInt(TAG_LAST_COLLISION_Z, coords.getZ());
		}

		UUID identity = getShooterUUID();
		boolean hasShooter = identity != null;
		tag.putBoolean(TAG_HAS_SHOOTER, hasShooter);
		if (hasShooter) {
			tag.putUniqueId(TAG_SHOOTER, identity);
		}
		tag.putBoolean(TAG_WARPED, warped);
		tag.putInt(TAG_ORBIT_TIME, orbitTime);
		tag.putBoolean(TAG_TRIPPED, tripped);
		if (magnetizePos != null) {
			tag.put(TAG_MAGNETIZE_POS, BlockPos.CODEC.encodeStart(NBTDynamicOps.INSTANCE, magnetizePos).get().orThrow());
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
		if (!stack.isEmpty()) {
			setSourceLens(stack);
		} else {
			setSourceLens(ItemStack.EMPTY);
		}

		int x = cmp.getInt(TAG_SPREADER_X);
		int y = cmp.getInt(TAG_SPREADER_Y);
		int z = cmp.getInt(TAG_SPREADER_Z);

		setBurstSourceCoords(new BlockPos(x, y, z));

		if (cmp.contains(TAG_LAST_COLLISION_X)) {
			x = cmp.getInt(TAG_SPREADER_X);
			y = cmp.getInt(TAG_SPREADER_Y);
			z = cmp.getInt(TAG_SPREADER_Z);
			lastCollision = new BlockPos(x, y, z);
		}

		double lastMotionX = cmp.getDouble(TAG_LAST_MOTION_X);
		double lastMotionY = cmp.getDouble(TAG_LAST_MOTION_Y);
		double lastMotionZ = cmp.getDouble(TAG_LAST_MOTION_Z);

		setBurstMotion(lastMotionX, lastMotionY, lastMotionZ);

		boolean hasShooter = cmp.getBoolean(TAG_HAS_SHOOTER);
		if (hasShooter) {
			UUID serializedUuid = cmp.getUniqueId(TAG_SHOOTER);
			UUID identity = getShooterUUID();
			if (!serializedUuid.equals(identity)) {
				setShooterUUID(serializedUuid);
			}
		}
		warped = cmp.getBoolean(TAG_WARPED);
		orbitTime = cmp.getInt(TAG_ORBIT_TIME);
		tripped = cmp.getBoolean(TAG_TRIPPED);
		if (cmp.contains(TAG_MAGNETIZE_POS)) {
			magnetizePos = BlockPos.CODEC.parse(NBTDynamicOps.INSTANCE, cmp.get(TAG_MAGNETIZE_POS)).get().orThrow();
		} else {
			magnetizePos = null;
		}
	}

	public void particles() {
		if (!isAlive() || !world.isRemote) {
			return;
		}

		ILensEffect lens = getLensInstance();
		if (lens != null && !lens.doParticles(this, getSourceLens())) {
			return;
		}

		int color = getColor();
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		float osize = getParticleSize();
		float size = osize;

		if (fake) {
			if (getMana() == getStartingMana()) {
				size = 2F;
			} else if (fullManaLastTick) {
				size = 4F;
			}

			if (!noParticles && shouldDoFakeParticles()) {
				SparkleParticleData data = SparkleParticleData.fake(0.4F * size, r, g, b, 1);
				Botania.proxy.addParticleForce(world, data, getPosX(), getPosY(), getPosZ(), 0, 0, 0);
			}
		} else {
			boolean depth = !Botania.proxy.isClientPlayerWearingMonocle();

			if (ConfigHandler.CLIENT.subtlePowerSystem.get()) {
				WispParticleData data = WispParticleData.wisp(0.1F * size, r, g, b, depth);
				Botania.proxy.addParticleForceNear(world, data, getPosX(), getPosY(), getPosZ(), (float) (Math.random() - 0.5F) * 0.02F, (float) (Math.random() - 0.5F) * 0.02F, (float) (Math.random() - 0.5F) * 0.01F);
			} else {
				float or = r;
				float og = g;
				float ob = b;

				double luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b; // Standard relative luminance calculation

				double iterX = getPosX();
				double iterY = getPosY();
				double iterZ = getPosZ();

				Vector3d currentPos = getPositionVec();
				Vector3d oldPos = new Vector3d(prevPosX, prevPosY, prevPosZ);
				Vector3d diffVec = oldPos.subtract(currentPos);
				Vector3d diffVecNorm = diffVec.normalize();

				double distance = 0.095;

				do {
					if (luminance < 0.1) {
						r = or + (float) Math.random() * 0.125F;
						g = og + (float) Math.random() * 0.125F;
						b = ob + (float) Math.random() * 0.125F;
					}
					size = osize + ((float) Math.random() - 0.5F) * 0.065F + (float) Math.sin(new Random(entityUniqueID.getMostSignificantBits()).nextInt(9001)) * 0.4F;
					WispParticleData data = WispParticleData.wisp(0.2F * size, r, g, b, depth);
					Botania.proxy.addParticleForceNear(world, data, iterX, iterY, iterZ,
							(float) -getMotion().getX() * 0.01F,
							(float) -getMotion().getY() * 0.01F,
							(float) -getMotion().getZ() * 0.01F);

					iterX += diffVecNorm.x * distance;
					iterY += diffVecNorm.y * distance;
					iterZ += diffVecNorm.z * distance;

					currentPos = new Vector3d(iterX, iterY, iterZ);
					diffVec = oldPos.subtract(currentPos);
					if (getOrbitTime() > 0) {
						break;
					}
				} while (Math.abs(diffVec.length()) > distance);

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

		if (rtr.getType() == RayTraceResult.Type.BLOCK) {
			pos = ((BlockRayTraceResult) rtr).getPos();
			if (pos.equals(lastCollision)) {
				return;
			}
			lastCollision = pos.toImmutable();
			TileEntity tile = world.getTileEntity(pos);
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			if (block instanceof IManaCollisionGhost
					&& ((IManaCollisionGhost) block).isGhost(state, world, pos)
					&& !(block instanceof IManaTrigger)
					|| block instanceof BushBlock
					|| block instanceof LeavesBlock) {
				return;
			}

			BlockPos coords = getBurstSourceBlockPos();
			if (tile != null && !tile.getPos().equals(coords)) {
				collidedTile = tile;
			}

			if (tile == null || !tile.getPos().equals(coords)) {
				if (!fake && !noParticles && (!world.isRemote || tile instanceof IClientManaHandler) && tile != null && tile instanceof IManaReceiver && ((IManaReceiver) tile).canReceiveManaFromBursts()) {
					onReceiverImpact((IManaReceiver) tile, tile.getPos());
				}

				if (block instanceof IManaTrigger) {
					((IManaTrigger) block).onBurstCollision(this, world, pos);
				}

				boolean ghost = block instanceof IManaCollisionGhost;
				dead = !ghost;
				if (ghost) {
					return;
				}
			}
		}

		ILensEffect lens = getLensInstance();
		if (lens != null) {
			dead = lens.collideBurst(this, rtr, collidedTile instanceof IManaReceiver
					&& ((IManaReceiver) collidedTile).canReceiveManaFromBursts(), dead, getSourceLens());
		}

		if (pos != null && !hasAlreadyCollidedAt(pos)) {
			alreadyCollidedAt.add(pos);
		}

		if (dead && isAlive()) {
			if (!fake && world.isRemote) {
				int color = getColor();
				float r = (color >> 16 & 0xFF) / 255F;
				float g = (color >> 8 & 0xFF) / 255F;
				float b = (color & 0xFF) / 255F;

				int mana = getMana();
				int maxMana = getStartingMana();
				float size = (float) mana / (float) maxMana;

				if (!ConfigHandler.CLIENT.subtlePowerSystem.get()) {
					for (int i = 0; i < 4; i++) {
						WispParticleData data = WispParticleData.wisp(0.15F * size, r, g, b);
						world.addParticle(data, getPosX(), getPosY(), getPosZ(), (float) (Math.random() - 0.5F) * 0.04F, (float) (Math.random() - 0.5F) * 0.04F, (float) (Math.random() - 0.5F) * 0.04F);
					}
				}
				SparkleParticleData data = SparkleParticleData.sparkle((float) 4, r, g, b, 2);
				world.addParticle(data, getPosX(), getPosY(), getPosZ(), 0, 0, 0);
			}

			remove();
		}
	}

	private void onReceiverImpact(IManaReceiver tile, BlockPos pos) {
		if (hasWarped()) {
			return;
		}

		ILensEffect lens = getLensInstance();
		int mana = getMana();

		if (lens != null) {
			ItemStack stack = getSourceLens();
			mana = lens.getManaToTransfer(this, this, stack, tile);
		}

		if (tile instanceof IManaCollector) {
			mana *= ((IManaCollector) tile).getManaYieldMultiplier(this);
		}

		tile.receiveMana(mana);

		if (tile instanceof IThrottledPacket) {
			((IThrottledPacket) tile).markDispatchable();
		} else {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile.tileEntity());
		}
	}

	@Override
	public void remove() {
		super.remove();

		if (!fake) {
			TileEntity tile = getShooter();
			if (tile instanceof IManaSpreader) {
				((IManaSpreader) tile).setCanShoot(true);
			}
		} else {
			setDeathTicksForFakeParticle();
		}
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
		if (!lens.isEmpty() && lens.getItem() instanceof ILensEffect) {
			return (ILensEffect) lens.getItem();
		}

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
		if (!hasAlreadyCollidedAt(pos)) {
			alreadyCollidedAt.add(pos.toImmutable());
		}
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
		if (tile instanceof IPingable) {
			((IPingable) tile).pingback(this, getShooterUUID());
		}
	}

	@Override
	public boolean hasWarped() {
		return warped;
	}

	@Override
	public void setWarped(boolean warped) {
		this.warped = warped;
	}

	@Override
	public int getOrbitTime() {
		return orbitTime;
	}

	@Override
	public void setOrbitTime(int time) {
		this.orbitTime = time;
	}

	@Override
	public boolean hasTripped() {
		return tripped;
	}

	@Override
	public void setTripped(boolean tripped) {
		this.tripped = tripped;
	}

	@Nullable
	@Override
	public BlockPos getMagnetizedPos() {
		return magnetizePos;
	}

	@Override
	public void setMagnetizePos(@Nullable BlockPos pos) {
		this.magnetizePos = pos;
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	protected boolean shouldDoFakeParticles() {
		if (ConfigHandler.CLIENT.staticWandBeam.get()) {
			return true;
		}

		TileEntity tile = getShooter();
		return tile instanceof IManaSpreader
				&& (getMana() != getStartingMana() && fullManaLastTick
						|| Math.abs(((IManaSpreader) tile).getBurstParticleTick() - getTicksExisted()) < 4);
	}

	private void incrementFakeParticleTick() {
		TileEntity tile = getShooter();
		if (tile instanceof IManaSpreader) {
			IManaSpreader spreader = (IManaSpreader) tile;
			spreader.setBurstParticleTick(spreader.getBurstParticleTick() + 2);
			if (spreader.getLastBurstDeathTick() != -1 && spreader.getBurstParticleTick() > spreader.getLastBurstDeathTick()) {
				spreader.setBurstParticleTick(0);
			}
		}
	}

	private void setDeathTicksForFakeParticle() {
		BlockPos coords = getBurstSourceBlockPos();
		TileEntity tile = world.getTileEntity(coords);
		if (tile != null && tile instanceof IManaSpreader) {
			((IManaSpreader) tile).setLastBurstDeathTick(getTicksExisted());
		}
	}

	public static class PositionProperties {

		public final BlockPos coords;
		public final BlockState state;

		public boolean invalid = false;

		public PositionProperties(Entity entity) {
			int x = MathHelper.floor(entity.getPosX());
			int y = MathHelper.floor(entity.getPosY());
			int z = MathHelper.floor(entity.getPosZ());
			coords = new BlockPos(x, y, z);
			state = entity.world.getBlockState(coords);
		}

		public boolean coordsEqual(PositionProperties props) {
			return coords.equals(props.coords);
		}

		public boolean contentsEqual(World world) {
			if (!world.isBlockLoaded(coords)) {
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
