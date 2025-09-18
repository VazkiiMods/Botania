package vazkii.botania.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.block.Bound;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.annotations.SoftImplement;
import vazkii.botania.common.block.LuminizerBlock;
import vazkii.botania.common.block.block_entity.LuminizerBlockEntity;
import vazkii.botania.common.handler.BotaniaSounds;

public class LuminizerMoverEntity extends Entity {
	private static final String TAG_EXIT_X = "exitX";
	private static final String TAG_EXIT_Y = "exitY";
	private static final String TAG_EXIT_Z = "exitZ";
	private static final EntityDataAccessor<BlockPos> EXIT_POS = SynchedEntityData.defineId(LuminizerMoverEntity.class, EntityDataSerializers.BLOCK_POS);

	public LuminizerMoverEntity(EntityType<LuminizerMoverEntity> type, Level world) {
		super(type, world);
		noPhysics = true;
	}

	public LuminizerMoverEntity(Level world, BlockPos pos, BlockPos exitPos) {
		this(BotaniaEntities.PLAYER_MOVER, world);
		setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		setExit(exitPos);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(EXIT_POS, Bound.UNBOUND_POS);
	}

	@Override
	public void tick() {
		super.tick();

		if (getPassengers().isEmpty() && !level().isClientSide) {
			discard();
			return;
		}

		boolean isItem = getPassengers().stream().allMatch(ItemEntity.class::isInstance);
		if (!isItem && tickCount % 30 == 0) {
			playSound(BotaniaSounds.lightRelay, 0.25F, (float) Math.random() * 0.3F + 0.7F);
		}
		if (!isItem && tickCount % 10 == 0) {
			gameEvent(GameEvent.ELYTRA_GLIDE);
		}

		BlockPos pos = blockPosition();
		BlockPos exitPos = getExitPos();

		if (!level().isClientSide && pos.equals(exitPos)) {
			boolean done = true;
			if (level().getBlockEntity(pos) instanceof LuminizerBlockEntity relay) {
				BlockState state = level().getBlockState(pos);
				if (state.getBlock() instanceof LuminizerBlock luminizer) {
					luminizer.onMoverPassing(level(), state, pos, this);
				}

				BlockPos bind = relay.getNextDestination();
				if (bind != null && relay.isValidBinding()) {
					setExit(bind);
					done = false;
				}
			}

			if (done) {
				for (Entity e : getPassengers()) {
					e.stopRiding();
				}
				discard();
				return;
			}
		}
		Vec3 thisVec = position();
		Vec3 motVec =
				thisVec.reverse().add(exitPos.getX() + 0.5, exitPos.getY() + 0.5, exitPos.getZ() + 0.5).normalize()
						.scale(0.5);

		int color;

		int count = 4;
		for (int i = 0; i < count; i++) {
			color = Mth.hsvToRgb(tickCount / 36F + 1F / count * i, 1F, 1F);
			double rad = Math.PI * 2.0 / count * i + tickCount / Math.PI;
			double cos = Math.cos(rad);
			double sin = Math.sin(rad);
			double s = 0.4;

			int r = (color >> 16) & 0xFF;
			int g = (color >> 8) & 0xFF;
			int b = color & 0xFF;
			SparkleParticleData data = SparkleParticleData.sparkle(1.2F, r / 255F, g / 255F, b / 255F, 10);
			level().addParticle(data, getX() + cos * s, getY() - 0.5, getZ() + sin * s, 0, 0, 0);
		}

		setPos(getX() + motVec.x, getY() + motVec.y, getZ() + motVec.z);
	}

	@SoftImplement("IEntityExtension") // todo implement on fabric
	public boolean shouldRiderSit() {
		return false;
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag cmp) {
		setExit(new BlockPos(cmp.getInt(TAG_EXIT_X), cmp.getInt(TAG_EXIT_Y), cmp.getInt(TAG_EXIT_Z)));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag cmp) {
		BlockPos exit = getExitPos();
		cmp.putInt(TAG_EXIT_X, exit.getX());
		cmp.putInt(TAG_EXIT_Y, exit.getY());
		cmp.putInt(TAG_EXIT_Z, exit.getZ());
	}

	// [VanillaCopy] Pig logic to select a dismount location, but using horizontal passenger direction instead of luminizer movement direction
	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity living) {
		Direction direction = living.getDirection();
		int[][] aint = DismountHelper.offsetsForDirection(direction);
		BlockPos blockpos = this.blockPosition();
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (Pose pose : living.getDismountPoses()) {
			AABB aabb = living.getLocalBoundsForPose(pose);

			for (int[] aint1 : aint) {
				blockpos$mutableblockpos.set(blockpos.getX() + aint1[0], blockpos.getY(), blockpos.getZ() + aint1[1]);
				double d0 = this.level().getBlockFloorHeight(blockpos$mutableblockpos);
				if (DismountHelper.isBlockFloorValid(d0)) {
					Vec3 vec3 = Vec3.upFromBottomCenterOf(blockpos$mutableblockpos, d0);
					if (DismountHelper.canDismountTo(this.level(), living, aabb.move(vec3))) {
						living.setPose(pose);
						return vec3;
					}
				}
			}
		}

		return super.getDismountLocationForPassenger(living);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
		return new ClientboundAddEntityPacket(this, entity);
	}

	public BlockPos getExitPos() {
		return entityData.get(EXIT_POS);
	}

	public void setExit(BlockPos pos) {
		entityData.set(EXIT_POS, pos);
	}

}
