/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.BlockLightRelay;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.network.PacketSpawnEntity;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class TileLightRelay extends TileMod implements Tickable, IWandBindable {
	public static final int MAX_DIST = 20;

	private static final String TAG_BIND_X = "bindX";
	private static final String TAG_BIND_Y = "bindY";
	private static final String TAG_BIND_Z = "bindZ";

	private BlockPos bindPos = new BlockPos(0, -1, 0);
	private int ticksElapsed = 0;

	public TileLightRelay() {
		super(ModTiles.LIGHT_RELAY);
	}

	public void mountEntity(Entity e) {
		BlockPos nextDest = getNextDestination();
		if (e.hasVehicle() || world.isClient || nextDest == null || !isValidBinding()) {
			return;
		}

		EntityPlayerMover mover = new EntityPlayerMover(world, pos, nextDest);
		world.spawnEntity(mover);
		e.startRiding(mover);
		if (!(e instanceof ItemEntity)) {
			mover.playSound(ModSounds.lightRelay, 0.2F, (float) Math.random() * 0.3F + 0.7F);
		}
		if (e instanceof ServerPlayerEntity) {
			PlayerHelper.grantCriterion((ServerPlayerEntity) e, prefix("main/luminizer_ride"), "code_triggered");
		}
	}

	@Override
	public void tick() {
		ticksElapsed++;

		BlockPos nextDest = getNextDestination();
		if (nextDest != null && nextDest.getY() > -1 && isValidBinding()) {
			if (world.isClient) {
				Vector3 vec = getMovementVector();
				if (vec != null) {
					double dist = 0.1;
					int size = (int) (vec.mag() / dist);
					int count = 10;
					int start = ticksElapsed % size;

					Vector3 vecMag = vec.normalize().multiply(dist);
					Vector3 vecTip = vecMag.multiply(start).add(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

					double radPer = Math.PI / 16.0;
					float mul = 0.5F;
					float mulPer = 0.4F;
					float maxMul = 2;
					WispParticleData data = WispParticleData.wisp(0.1F, 0.4F, 0.4F, 1F, 1);
					for (int i = start; i < start + count; i++) {
						mul = Math.min(maxMul, mul + mulPer);
						double rad = radPer * (i + ticksElapsed * 0.4);
						Vector3 vecRot = vecMag.crossProduct(Vector3.ONE).multiply(mul).rotate(rad, vecMag).add(vecTip);
						world.addParticle(data, vecRot.x, vecRot.y, vecRot.z, (float) -vecMag.x, (float) -vecMag.y, (float) -vecMag.z);
						vecTip = vecTip.add(vecMag);
					}
				}
			} else {
				BlockPos endpoint = getEndpoint();

				if (endpoint != null) {
					Box aabb = getCachedState().getOutlineShape(world, pos).getBoundingBox().offset(pos);
					float range = 0.6F;
					List<EnderPearlEntity> enderPearls = world.getNonSpectatingEntities(EnderPearlEntity.class, aabb.expand(range));
					for (EnderPearlEntity pearl : enderPearls) {
						pearl.requestTeleport(
								endpoint.getX() + pearl.getX() - pos.getX(),
								endpoint.getY() + pearl.getY() - pos.getY(),
								endpoint.getZ() + pearl.getZ() - pos.getZ()
						);
					}
				}
			}
		}
	}

	private boolean isValidBinding() {
		BlockPos nextDest = getNextDestination();
		if (nextDest == null) {
			return false;
		}

		Block block = world.getBlockState(nextDest).getBlock();
		return block instanceof BlockLightRelay;
	}

	private BlockPos getEndpoint() {
		List<TileLightRelay> pointsPassed = new ArrayList<>();
		TileLightRelay relay = this;
		BlockPos lastCoords = null;

		// Doing while(true) gives an unreachable code error
		boolean run = true;
		while (run) {
			if (pointsPassed.contains(relay)) {
				return null; // Circular path
			}
			pointsPassed.add(relay);

			BlockPos coords = relay.getNextDestination();
			if (coords == null) {
				return lastCoords;
			}

			BlockEntity tile = world.getBlockEntity(coords);
			if (tile != null && tile instanceof TileLightRelay) {
				relay = (TileLightRelay) tile;
			} else {
				return lastCoords;
			}

			lastCoords = coords;
		}

		return null;
	}

	public Vector3 getMovementVector() {
		BlockPos dest = getNextDestination();
		if (dest == null) {
			return null;
		}

		return new Vector3(dest.getX() - pos.getX(), dest.getY() - pos.getY(), dest.getZ() - pos.getZ());
	}

	@Override
	public BlockPos getBinding() {
		return bindPos;
	}

	public BlockPos getNextDestination() {
		BlockState state = getCachedState();
		if (state.getBlock() == ModBlocks.lightRelayToggle && state.get(Properties.POWERED)) {
			return null;
		} else if (state.getBlock() == ModBlocks.lightRelayFork) {
			BlockPos torchPos = null;
			for (int i = -2; i < 3; i++) {
				BlockPos testPos = pos.add(0, i, 0);

				BlockState testState = world.getBlockState(testPos);
				if (testState.getBlock() == ModBlocks.animatedTorch) {
					torchPos = testPos;
					break;
				}
			}

			if (torchPos != null) {
				TileAnimatedTorch torch = (TileAnimatedTorch) world.getBlockEntity(torchPos);
				Direction side = TileAnimatedTorch.SIDES[torch.side].getOpposite();
				for (int i = 1; i < MAX_DIST; i++) {
					BlockPos testPos = pos.offset(side, i);
					BlockState testState = world.getBlockState(testPos);
					if (testState.getBlock() instanceof BlockLightRelay) {
						return testPos;
					}
				}
			}
		}

		return getBinding();
	}

	@Override
	public boolean canSelect(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public boolean bindTo(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
		if (!(player.world.getBlockState(pos).getBlock() instanceof BlockLightRelay)
				|| pos.getSquaredDistance(getPos()) > MAX_DIST * MAX_DIST) {
			return false;
		}

		bindPos = pos;
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		return true;
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		bindPos = new BlockPos(
				cmp.getInt(TAG_BIND_X),
				cmp.getInt(TAG_BIND_Y),
				cmp.getInt(TAG_BIND_Z)
		);
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putInt(TAG_BIND_X, bindPos.getX());
		cmp.putInt(TAG_BIND_Y, bindPos.getY());
		cmp.putInt(TAG_BIND_Z, bindPos.getZ());
	}

	public static class EntityPlayerMover extends Entity {
		private static final String TAG_EXIT_X = "exitX";
		private static final String TAG_EXIT_Y = "exitY";
		private static final String TAG_EXIT_Z = "exitZ";
		private static final TrackedData<BlockPos> EXIT_POS = DataTracker.registerData(EntityPlayerMover.class, TrackedDataHandlerRegistry.BLOCK_POS);

		public EntityPlayerMover(EntityType<EntityPlayerMover> type, World world) {
			super(type, world);
			noClip = true;
		}

		public EntityPlayerMover(World world, BlockPos pos, BlockPos exitPos) {
			this(ModEntities.PLAYER_MOVER, world);
			updatePosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			setExit(exitPos);
		}

		@Override
		protected void initDataTracker() {
			dataTracker.startTracking(EXIT_POS, BlockPos.ORIGIN);
		}

		@Override
		public void tick() {
			super.tick();

			if (getPassengerList().isEmpty() && !world.isClient) {
				remove();
				return;
			}

			boolean isItem = getVehicle() instanceof ItemEntity;
			if (!isItem && age % 30 == 0) {
				playSound(ModSounds.lightRelay, 0.05F, (float) Math.random() * 0.3F + 0.7F);
			}

			BlockPos pos = getBlockPos();
			BlockPos exitPos = getExitPos();

			if (pos.equals(exitPos)) {
				BlockEntity tile = world.getBlockEntity(pos);
				if (tile instanceof TileLightRelay) {
					BlockState state = world.getBlockState(pos);
					if (state.getBlock() == ModBlocks.lightRelayDetector) {
						world.setBlockState(pos, state.with(Properties.POWERED, true));
						world.getBlockTickScheduler().schedule(pos, state.getBlock(), 2);
					}

					TileLightRelay relay = (TileLightRelay) tile;
					BlockPos bind = relay.getNextDestination();
					if (bind != null && relay.isValidBinding()) {
						setExit(bind);
						return;
					}
				}

				for (Entity e : getPassengerList()) {
					e.stopRiding();
				}
				remove();
			} else {
				Vec3d thisVec = getPos();
				Vec3d motVec = thisVec.negate().add(exitPos.getX() + 0.5, exitPos.getY() + 0.5, exitPos.getZ() + 0.5).normalize().multiply(0.5);

				int color;

				int count = 4;
				for (int i = 0; i < count; i++) {
					color = MathHelper.hsvToRgb(age / 36F + 1F / count * i, 1F, 1F);
					double rad = Math.PI * 2.0 / count * i + age / Math.PI;
					double cos = Math.cos(rad);
					double sin = Math.sin(rad);
					double s = 0.4;

					int r = (color >> 16) & 0xFF;
					int g = (color >> 8) & 0xFF;
					int b = color & 0xFF;
					SparkleParticleData data = SparkleParticleData.sparkle(1.2F, r / 255F, g / 255F, b / 255F, 10);
					world.addParticle(data, getX() + cos * s, getY() - 0.5, getZ() + sin * s, 0, 0, 0);
				}

				updatePosition(getX() + motVec.x, getY() + motVec.y, getZ() + motVec.z);
			}
		}

		/* todo 1.16-fabric
		@Override
		public boolean shouldRiderSit() {
			return false;
		}
		*/

		@Override
		public boolean damage(@Nonnull DamageSource source, float damage) {
			return false;
		}

		@Override
		protected void readCustomDataFromTag(@Nonnull CompoundTag cmp) {
			setExit(new BlockPos(cmp.getInt(TAG_EXIT_X), cmp.getInt(TAG_EXIT_Y), cmp.getInt(TAG_EXIT_Z)));
		}

		@Override
		protected void writeCustomDataToTag(@Nonnull CompoundTag cmp) {
			BlockPos exit = getExitPos();
			cmp.putInt(TAG_EXIT_X, exit.getX());
			cmp.putInt(TAG_EXIT_Y, exit.getY());
			cmp.putInt(TAG_EXIT_Z, exit.getZ());
		}

		// [VanillaCopy] PigEntity logic to select a dismount location
		@Override
		public Vec3d updatePassengerForDismount(LivingEntity living) {
			Direction direction = living.getHorizontalFacing();
			int[][] aint = Dismounting.getDismountOffsets(direction);
			BlockPos blockpos = this.getBlockPos();
			BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

			for (EntityPose pose : living.getPoses()) {
				Box axisalignedbb = living.getBoundingBox(pose);

				for (int[] aint1 : aint) {
					blockpos$mutable.set(blockpos.getX() + aint1[0], blockpos.getY(), blockpos.getZ() + aint1[1]);
					double d0 = this.world.getDismountHeight(blockpos$mutable);
					if (Dismounting.canDismountInBlock(d0)) {
						Vec3d vector3d = Vec3d.ofCenter(blockpos$mutable, d0);
						if (Dismounting.canPlaceEntityAt(this.world, living, axisalignedbb.offset(vector3d))) {
							living.setPose(pose);
							return vector3d;
						}
					}
				}
			}

			return super.updatePassengerForDismount(living);
		}

		@Nonnull
		@Override
		public Packet<?> createSpawnPacket() {
			return PacketSpawnEntity.make(this);
		}

		public BlockPos getExitPos() {
			return dataTracker.get(EXIT_POS);
		}

		public void setExit(BlockPos pos) {
			dataTracker.set(EXIT_POS, pos);
		}

	}

}
